package com.googlecode.mp4parser.authoring.samples;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentedMp4SampleList extends AbstractList<Sample> {
    private List<TrackFragmentBox> allTrafs;
    private int[] firstSamples;
    IsoFile[] fragments;
    private SoftReference<Sample>[] sampleCache;
    private int size_ = -1;
    Container topLevel;
    TrackBox trackBox = null;
    TrackExtendsBox trex = null;
    private Map<TrackRunBox, SoftReference<ByteBuffer>> trunDataCache = new HashMap();

    public FragmentedMp4SampleList(long j, Container container, IsoFile... isoFileArr) {
        this.topLevel = container;
        this.fragments = isoFileArr;
        for (TrackBox trackBox2 : Path.getPaths(container, "moov[0]/trak")) {
            if (trackBox2.getTrackHeaderBox().getTrackId() == j) {
                this.trackBox = trackBox2;
            }
        }
        if (this.trackBox != null) {
            for (TrackExtendsBox trackExtendsBox : Path.getPaths(container, "moov[0]/mvex[0]/trex")) {
                if (trackExtendsBox.getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                    this.trex = trackExtendsBox;
                }
            }
            this.sampleCache = (SoftReference[]) Array.newInstance(SoftReference.class, size());
            initAllFragments();
            return;
        }
        throw new RuntimeException("This MP4 does not contain track " + j);
    }

    private List<TrackFragmentBox> initAllFragments() {
        List<TrackFragmentBox> list = this.allTrafs;
        if (list != null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (MovieFragmentBox boxes : this.topLevel.getBoxes(MovieFragmentBox.class)) {
            for (TrackFragmentBox next : boxes.getBoxes(TrackFragmentBox.class)) {
                if (next.getTrackFragmentHeaderBox().getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                    arrayList.add(next);
                }
            }
        }
        IsoFile[] isoFileArr = this.fragments;
        if (isoFileArr != null) {
            for (IsoFile boxes2 : isoFileArr) {
                for (MovieFragmentBox boxes3 : boxes2.getBoxes(MovieFragmentBox.class)) {
                    for (TrackFragmentBox next2 : boxes3.getBoxes(TrackFragmentBox.class)) {
                        if (next2.getTrackFragmentHeaderBox().getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                            arrayList.add(next2);
                        }
                    }
                }
            }
        }
        this.allTrafs = arrayList;
        this.firstSamples = new int[this.allTrafs.size()];
        int i = 1;
        for (int i2 = 0; i2 < this.allTrafs.size(); i2++) {
            this.firstSamples[i2] = i;
            i += getTrafSize(this.allTrafs.get(i2));
        }
        return arrayList;
    }

    private int getTrafSize(TrackFragmentBox trackFragmentBox) {
        List<Box> boxes = trackFragmentBox.getBoxes();
        int i = 0;
        for (int i2 = 0; i2 < boxes.size(); i2++) {
            Box box = boxes.get(i2);
            if (box instanceof TrackRunBox) {
                i += CastUtils.l2i(((TrackRunBox) box).getSampleCount());
            }
        }
        return i;
    }

    public Sample get(int i) {
        long j;
        ByteBuffer byteBuffer;
        long defaultSampleSize;
        Sample sample;
        SoftReference<Sample>[] softReferenceArr = this.sampleCache;
        if (softReferenceArr[i] != null && (sample = softReferenceArr[i].get()) != null) {
            return sample;
        }
        int i2 = i + 1;
        int length = this.firstSamples.length;
        while (true) {
            length--;
            if (i2 - this.firstSamples[length] >= 0) {
                break;
            }
        }
        TrackFragmentBox trackFragmentBox = this.allTrafs.get(length);
        int i3 = i2 - this.firstSamples[length];
        MovieFragmentBox movieFragmentBox = (MovieFragmentBox) trackFragmentBox.getParent();
        int i4 = 0;
        for (Box next : trackFragmentBox.getBoxes()) {
            if (next instanceof TrackRunBox) {
                TrackRunBox trackRunBox = (TrackRunBox) next;
                int i5 = i3 - i4;
                if (trackRunBox.getEntries().size() <= i5) {
                    i4 += trackRunBox.getEntries().size();
                } else {
                    List<TrackRunBox.Entry> entries = trackRunBox.getEntries();
                    TrackFragmentHeaderBox trackFragmentHeaderBox = trackFragmentBox.getTrackFragmentHeaderBox();
                    boolean isSampleSizePresent = trackRunBox.isSampleSizePresent();
                    boolean hasDefaultSampleSize = trackFragmentHeaderBox.hasDefaultSampleSize();
                    long j2 = 0;
                    if (!isSampleSizePresent) {
                        if (hasDefaultSampleSize) {
                            defaultSampleSize = trackFragmentHeaderBox.getDefaultSampleSize();
                        } else {
                            TrackExtendsBox trackExtendsBox = this.trex;
                            if (trackExtendsBox != null) {
                                defaultSampleSize = trackExtendsBox.getDefaultSampleSize();
                            } else {
                                throw new RuntimeException("File doesn't contain trex box but track fragments aren't fully self contained. Cannot determine sample size.");
                            }
                        }
                        j = defaultSampleSize;
                    } else {
                        j = 0;
                    }
                    SoftReference softReference = this.trunDataCache.get(trackRunBox);
                    ByteBuffer byteBuffer2 = softReference != null ? (ByteBuffer) softReference.get() : null;
                    if (byteBuffer2 == null) {
                        Container container = movieFragmentBox;
                        if (trackFragmentHeaderBox.hasBaseDataOffset()) {
                            j2 = 0 + trackFragmentHeaderBox.getBaseDataOffset();
                            container = movieFragmentBox.getParent();
                        }
                        if (trackRunBox.isDataOffsetPresent()) {
                            j2 += (long) trackRunBox.getDataOffset();
                        }
                        int i6 = 0;
                        for (TrackRunBox.Entry sampleSize : entries) {
                            i6 = isSampleSizePresent ? (int) (((long) i6) + sampleSize.getSampleSize()) : (int) (((long) i6) + j);
                        }
                        try {
                            ByteBuffer byteBuffer3 = container.getByteBuffer(j2, (long) i6);
                            this.trunDataCache.put(trackRunBox, new SoftReference(byteBuffer3));
                            byteBuffer = byteBuffer3;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        byteBuffer = byteBuffer2;
                    }
                    int i7 = 0;
                    for (int i8 = 0; i8 < i5; i8++) {
                        i7 = (int) (isSampleSizePresent ? ((long) i7) + entries.get(i8).getSampleSize() : ((long) i7) + j);
                    }
                    final long sampleSize2 = isSampleSizePresent ? entries.get(i5).getSampleSize() : j;
                    final ByteBuffer byteBuffer4 = byteBuffer;
                    final int i9 = i7;
                    AnonymousClass1 r1 = new Sample() {
                        public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                            writableByteChannel.write(asByteBuffer());
                        }

                        public long getSize() {
                            return sampleSize2;
                        }

                        public ByteBuffer asByteBuffer() {
                            return (ByteBuffer) ((ByteBuffer) byteBuffer4.position(i9)).slice().limit(CastUtils.l2i(sampleSize2));
                        }
                    };
                    this.sampleCache[i] = new SoftReference<>(r1);
                    return r1;
                }
            }
        }
        throw new RuntimeException("Couldn't find sample in the traf I was looking");
    }

    public int size() {
        int i = this.size_;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (MovieFragmentBox boxes : this.topLevel.getBoxes(MovieFragmentBox.class)) {
            for (TrackFragmentBox next : boxes.getBoxes(TrackFragmentBox.class)) {
                if (next.getTrackFragmentHeaderBox().getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                    for (TrackRunBox sampleCount : next.getBoxes(TrackRunBox.class)) {
                        i2 = (int) (((long) i2) + sampleCount.getSampleCount());
                    }
                }
            }
        }
        for (IsoFile boxes2 : this.fragments) {
            for (MovieFragmentBox boxes3 : boxes2.getBoxes(MovieFragmentBox.class)) {
                for (TrackFragmentBox next2 : boxes3.getBoxes(TrackFragmentBox.class)) {
                    if (next2.getTrackFragmentHeaderBox().getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                        for (TrackRunBox sampleCount2 : next2.getBoxes(TrackRunBox.class)) {
                            i2 = (int) (((long) i2) + sampleCount2.getSampleCount());
                        }
                    }
                }
            }
        }
        this.size_ = i2;
        return i2;
    }
}
