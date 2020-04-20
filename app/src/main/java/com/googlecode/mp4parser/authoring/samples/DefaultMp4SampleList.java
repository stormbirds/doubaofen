package com.googlecode.mp4parser.authoring.samples;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

public class DefaultMp4SampleList extends AbstractList<Sample> {
    /* access modifiers changed from: private */
    public static final Logger LOG = Logger.getLogger(DefaultMp4SampleList.class);
    SoftReference<ByteBuffer>[] cache = null;
    int[] chunkNumsStartSampleNum;
    long[] chunkOffsets;
    long[] chunkSizes;
    int lastChunk;
    long[][] sampleOffsetsWithinChunks;
    SampleSizeBox ssb;
    Container topLevel;
    TrackBox trackBox = null;

    public DefaultMp4SampleList(long j, Container container) {
        int i;
        long j2 = j;
        Container container2 = container;
        int i2 = 0;
        this.lastChunk = 0;
        this.topLevel = container2;
        for (TrackBox next : container2.getBoxes(MovieBox.class).get(0).getBoxes(TrackBox.class)) {
            if (next.getTrackHeaderBox().getTrackId() == j2) {
                this.trackBox = next;
            }
        }
        TrackBox trackBox2 = this.trackBox;
        if (trackBox2 != null) {
            this.chunkOffsets = trackBox2.getSampleTableBox().getChunkOffsetBox().getChunkOffsets();
            long[] jArr = this.chunkOffsets;
            this.chunkSizes = new long[jArr.length];
            this.cache = new SoftReference[jArr.length];
            Arrays.fill(this.cache, new SoftReference((Object) null));
            this.sampleOffsetsWithinChunks = new long[this.chunkOffsets.length][];
            this.ssb = this.trackBox.getSampleTableBox().getSampleSizeBox();
            List<SampleToChunkBox.Entry> entries = this.trackBox.getSampleTableBox().getSampleToChunkBox().getEntries();
            SampleToChunkBox.Entry[] entryArr = (SampleToChunkBox.Entry[]) entries.toArray(new SampleToChunkBox.Entry[entries.size()]);
            SampleToChunkBox.Entry entry = entryArr[0];
            long firstChunk = entry.getFirstChunk();
            int l2i = CastUtils.l2i(entry.getSamplesPerChunk());
            int size = size();
            int i3 = l2i;
            int i4 = 0;
            int i5 = 1;
            int i6 = 0;
            int i7 = 1;
            do {
                i4++;
                if (((long) i4) == firstChunk) {
                    if (entryArr.length > i5) {
                        SampleToChunkBox.Entry entry2 = entryArr[i5];
                        i6 = i3;
                        i3 = CastUtils.l2i(entry2.getSamplesPerChunk());
                        i5++;
                        firstChunk = entry2.getFirstChunk();
                    } else {
                        i6 = i3;
                        i3 = -1;
                        firstChunk = LongCompanionObject.MAX_VALUE;
                    }
                }
                this.sampleOffsetsWithinChunks[i4 - 1] = new long[i6];
                i7 += i6;
            } while (i7 <= size);
            this.chunkNumsStartSampleNum = new int[(i4 + 1)];
            SampleToChunkBox.Entry entry3 = entryArr[0];
            long firstChunk2 = entry3.getFirstChunk();
            int i8 = 1;
            int i9 = 1;
            int i10 = 0;
            int l2i2 = CastUtils.l2i(entry3.getSamplesPerChunk());
            int i11 = 0;
            while (true) {
                i = i11 + 1;
                this.chunkNumsStartSampleNum[i11] = i8;
                if (((long) i) == firstChunk2) {
                    if (entryArr.length > i9) {
                        int i12 = i9 + 1;
                        SampleToChunkBox.Entry entry4 = entryArr[i9];
                        int l2i3 = CastUtils.l2i(entry4.getSamplesPerChunk());
                        firstChunk2 = entry4.getFirstChunk();
                        i9 = i12;
                        int i13 = l2i3;
                        i10 = l2i2;
                        l2i2 = i13;
                    } else {
                        i10 = l2i2;
                        l2i2 = -1;
                        firstChunk2 = LongCompanionObject.MAX_VALUE;
                    }
                }
                i8 += i10;
                if (i8 > size) {
                    break;
                }
                i11 = i;
            }
            this.chunkNumsStartSampleNum[i] = Integer.MAX_VALUE;
            long j3 = 0;
            for (int i14 = 1; ((long) i14) <= this.ssb.getSampleCount(); i14++) {
                while (i14 == this.chunkNumsStartSampleNum[i2]) {
                    i2++;
                    j3 = 0;
                }
                long[] jArr2 = this.chunkSizes;
                int i15 = i2 - 1;
                int i16 = i14 - 1;
                jArr2[i15] = jArr2[i15] + this.ssb.getSampleSizeAtIndex(i16);
                this.sampleOffsetsWithinChunks[i15][i14 - this.chunkNumsStartSampleNum[i15]] = j3;
                j3 += this.ssb.getSampleSizeAtIndex(i16);
            }
            return;
        }
        throw new RuntimeException("This MP4 does not contain track " + j2);
    }

    /* access modifiers changed from: package-private */
    public synchronized int getChunkForSample(int i) {
        int i2 = i + 1;
        if (i2 >= this.chunkNumsStartSampleNum[this.lastChunk] && i2 < this.chunkNumsStartSampleNum[this.lastChunk + 1]) {
            return this.lastChunk;
        } else if (i2 < this.chunkNumsStartSampleNum[this.lastChunk]) {
            this.lastChunk = 0;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= i2) {
                this.lastChunk++;
            }
            return this.lastChunk;
        } else {
            this.lastChunk++;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= i2) {
                this.lastChunk++;
            }
            return this.lastChunk;
        }
    }

    public Sample get(int i) {
        if (((long) i) < this.ssb.getSampleCount()) {
            return new SampleImpl(i);
        }
        throw new IndexOutOfBoundsException();
    }

    public int size() {
        return CastUtils.l2i(this.trackBox.getSampleTableBox().getSampleSizeBox().getSampleCount());
    }

    class SampleImpl implements Sample {
        private int index;

        public SampleImpl(int i) {
            this.index = i;
        }

        public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
            writableByteChannel.write(asByteBuffer());
        }

        public long getSize() {
            return DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex(this.index);
        }

        public synchronized ByteBuffer asByteBuffer() {
            long j;
            ByteBuffer byteBuffer;
            int chunkForSample = DefaultMp4SampleList.this.getChunkForSample(this.index);
            SoftReference<ByteBuffer> softReference = DefaultMp4SampleList.this.cache[chunkForSample];
            int i = DefaultMp4SampleList.this.chunkNumsStartSampleNum[chunkForSample] - 1;
            long j2 = (long) chunkForSample;
            long[] jArr = DefaultMp4SampleList.this.sampleOffsetsWithinChunks[CastUtils.l2i(j2)];
            j = jArr[this.index - i];
            if (softReference == null || (byteBuffer = softReference.get()) == null) {
                try {
                    byteBuffer = DefaultMp4SampleList.this.topLevel.getByteBuffer(DefaultMp4SampleList.this.chunkOffsets[CastUtils.l2i(j2)], jArr[jArr.length - 1] + DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex((i + jArr.length) - 1));
                    DefaultMp4SampleList.this.cache[chunkForSample] = new SoftReference<>(byteBuffer);
                } catch (IOException e) {
                    StringWriter stringWriter = new StringWriter();
                    e.printStackTrace(new PrintWriter(stringWriter));
                    DefaultMp4SampleList.LOG.logError(stringWriter.toString());
                    throw new IndexOutOfBoundsException(e.getMessage());
                }
            }
            return (ByteBuffer) ((ByteBuffer) byteBuffer.duplicate().position(CastUtils.l2i(j))).slice().limit(CastUtils.l2i(DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex(this.index)));
        }

        public String toString() {
            return "Sample(index: " + this.index + " size: " + DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex(this.index) + ")";
        }
    }
}
