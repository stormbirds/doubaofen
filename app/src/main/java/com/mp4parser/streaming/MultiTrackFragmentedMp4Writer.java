package com.mp4parser.streaming;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.HintMediaHeaderBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.NullMediaHeaderBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.SubtitleMediaHeaderBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.SampleFlags;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBaseMediaDecodeTimeBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.util.Math;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.mp4parser.streaming.extensions.CencEncryptTrackExtension;
import com.mp4parser.streaming.extensions.CompositionTimeSampleExtension;
import com.mp4parser.streaming.extensions.CompositionTimeTrackExtension;
import com.mp4parser.streaming.extensions.SampleFlagsSampleExtension;
import com.mp4parser.streaming.extensions.SampleFlagsTrackExtension;
import com.mp4parser.streaming.extensions.TrackIdTrackExtension;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiTrackFragmentedMp4Writer implements StreamingMp4Writer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    CompositionTimeTrackExtension compositionTimeTrackExtension;
    Date creationTime;
    private long currentFragmentStartTime = 0;
    private long currentTime = 0;
    Map<StreamingTrack, List<StreamingSample>> fragmentBuffers = new HashMap();
    private final OutputStream outputStream;
    SampleFlagsTrackExtension sampleDependencyTrackExtension;
    private long sequenceNumber = 1;
    StreamingTrack[] source;

    public void close() {
    }

    public MultiTrackFragmentedMp4Writer(StreamingTrack[] streamingTrackArr, OutputStream outputStream2) {
        this.source = streamingTrackArr;
        this.outputStream = outputStream2;
        this.creationTime = new Date();
        HashSet hashSet = new HashSet();
        int length = streamingTrackArr.length;
        int i = 0;
        while (i < length) {
            StreamingTrack streamingTrack = streamingTrackArr[i];
            if (streamingTrack.getTrackExtension(TrackIdTrackExtension.class) == null || !hashSet.contains(Long.valueOf(((TrackIdTrackExtension) streamingTrack.getTrackExtension(TrackIdTrackExtension.class)).getTrackId()))) {
                i++;
            } else {
                throw new RuntimeException("There may not be two tracks with the same trackID within one file");
            }
        }
        for (StreamingTrack streamingTrack2 : streamingTrackArr) {
            if (streamingTrack2.getTrackExtension(TrackIdTrackExtension.class) != null) {
                ArrayList arrayList = new ArrayList(hashSet);
                Collections.sort(arrayList);
                streamingTrack2.addTrackExtension(new TrackIdTrackExtension(arrayList.size() > 0 ? ((Long) arrayList.get(arrayList.size() - 1)).longValue() + 1 : 1));
            }
        }
    }

    /* access modifiers changed from: protected */
    public Box createMvhd() {
        MovieHeaderBox movieHeaderBox = new MovieHeaderBox();
        movieHeaderBox.setVersion(1);
        movieHeaderBox.setCreationTime(this.creationTime);
        movieHeaderBox.setModificationTime(this.creationTime);
        movieHeaderBox.setDuration(0);
        long[] jArr = new long[0];
        StreamingTrack[] streamingTrackArr = this.source;
        int length = streamingTrackArr.length;
        for (int i = 0; i < length; i++) {
            Mp4Arrays.copyOfAndAppend(jArr, streamingTrackArr[i].getTimescale());
        }
        movieHeaderBox.setTimescale(Math.lcm(jArr));
        movieHeaderBox.setNextTrackId(2);
        return movieHeaderBox;
    }

    /* access modifiers changed from: protected */
    public Box createMdiaHdlr(StreamingTrack streamingTrack) {
        HandlerBox handlerBox = new HandlerBox();
        handlerBox.setHandlerType(streamingTrack.getHandler());
        return handlerBox;
    }

    /* access modifiers changed from: protected */
    public Box createMdhd(StreamingTrack streamingTrack) {
        MediaHeaderBox mediaHeaderBox = new MediaHeaderBox();
        mediaHeaderBox.setCreationTime(this.creationTime);
        mediaHeaderBox.setModificationTime(this.creationTime);
        mediaHeaderBox.setDuration(0);
        mediaHeaderBox.setTimescale(streamingTrack.getTimescale());
        mediaHeaderBox.setLanguage(streamingTrack.getLanguage());
        return mediaHeaderBox;
    }

    /* access modifiers changed from: protected */
    public Box createMdia(StreamingTrack streamingTrack) {
        MediaBox mediaBox = new MediaBox();
        mediaBox.addBox(createMdhd(streamingTrack));
        mediaBox.addBox(createMdiaHdlr(streamingTrack));
        mediaBox.addBox(createMinf(streamingTrack));
        return mediaBox;
    }

    /* access modifiers changed from: protected */
    public Box createMinf(StreamingTrack streamingTrack) {
        MediaInformationBox mediaInformationBox = new MediaInformationBox();
        if (streamingTrack.getHandler().equals("vide")) {
            mediaInformationBox.addBox(new VideoMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("soun")) {
            mediaInformationBox.addBox(new SoundMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("text")) {
            mediaInformationBox.addBox(new NullMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("subt")) {
            mediaInformationBox.addBox(new SubtitleMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("hint")) {
            mediaInformationBox.addBox(new HintMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("sbtl")) {
            mediaInformationBox.addBox(new NullMediaHeaderBox());
        }
        mediaInformationBox.addBox(createDinf());
        mediaInformationBox.addBox(createStbl(streamingTrack));
        return mediaInformationBox;
    }

    /* access modifiers changed from: protected */
    public Box createStbl(StreamingTrack streamingTrack) {
        SampleTableBox sampleTableBox = new SampleTableBox();
        sampleTableBox.addBox(streamingTrack.getSampleDescriptionBox());
        sampleTableBox.addBox(new TimeToSampleBox());
        sampleTableBox.addBox(new SampleToChunkBox());
        sampleTableBox.addBox(new SampleSizeBox());
        sampleTableBox.addBox(new StaticChunkOffsetBox());
        return sampleTableBox;
    }

    /* access modifiers changed from: protected */
    public DataInformationBox createDinf() {
        DataInformationBox dataInformationBox = new DataInformationBox();
        DataReferenceBox dataReferenceBox = new DataReferenceBox();
        dataInformationBox.addBox(dataReferenceBox);
        DataEntryUrlBox dataEntryUrlBox = new DataEntryUrlBox();
        dataEntryUrlBox.setFlags(1);
        dataReferenceBox.addBox(dataEntryUrlBox);
        return dataInformationBox;
    }

    /* access modifiers changed from: protected */
    public Box createTrak(StreamingTrack streamingTrack) {
        TrackBox trackBox = new TrackBox();
        trackBox.addBox(streamingTrack.getTrackHeaderBox());
        trackBox.addBox(streamingTrack.getTrackHeaderBox());
        trackBox.addBox(createMdia(streamingTrack));
        return trackBox;
    }

    public Box createFtyp() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("isom");
        linkedList.add("iso6");
        linkedList.add(VisualSampleEntry.TYPE3);
        return new FileTypeBox("isom", 0, linkedList);
    }

    /* access modifiers changed from: protected */
    public Box createMvex() {
        MovieExtendsBox movieExtendsBox = new MovieExtendsBox();
        MovieExtendsHeaderBox movieExtendsHeaderBox = new MovieExtendsHeaderBox();
        movieExtendsHeaderBox.setVersion(1);
        movieExtendsHeaderBox.setFragmentDuration(0);
        movieExtendsBox.addBox(movieExtendsHeaderBox);
        for (StreamingTrack createTrex : this.source) {
            movieExtendsBox.addBox(createTrex(createTrex));
        }
        return movieExtendsBox;
    }

    /* access modifiers changed from: protected */
    public Box createTrex(StreamingTrack streamingTrack) {
        TrackExtendsBox trackExtendsBox = new TrackExtendsBox();
        trackExtendsBox.setTrackId(streamingTrack.getTrackHeaderBox().getTrackId());
        trackExtendsBox.setDefaultSampleDescriptionIndex(1);
        trackExtendsBox.setDefaultSampleDuration(0);
        trackExtendsBox.setDefaultSampleSize(0);
        SampleFlags sampleFlags = new SampleFlags();
        if ("soun".equals(streamingTrack.getHandler()) || "subt".equals(streamingTrack.getHandler())) {
            sampleFlags.setSampleDependsOn(2);
            sampleFlags.setSampleIsDependedOn(2);
        }
        trackExtendsBox.setDefaultSampleFlags(sampleFlags);
        return trackExtendsBox;
    }

    /* access modifiers changed from: protected */
    public Box createMoov() {
        MovieBox movieBox = new MovieBox();
        movieBox.addBox(createMvhd());
        for (StreamingTrack createTrak : this.source) {
            movieBox.addBox(createTrak(createTrak));
        }
        movieBox.addBox(createMvex());
        return movieBox;
    }

    class ConsumeSamplesCallable implements Callable {
        private StreamingTrack streamingTrack;

        public ConsumeSamplesCallable(StreamingTrack streamingTrack2) {
            this.streamingTrack = streamingTrack2;
        }

        public Object call() throws Exception {
            while (true) {
                try {
                    StreamingSample poll = this.streamingTrack.getSamples().poll(100, TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        MultiTrackFragmentedMp4Writer.this.consumeSample(this.streamingTrack, poll);
                    } else if (!this.streamingTrack.hasMoreSamples()) {
                        return null;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void write() throws IOException {
        WritableByteChannel newChannel = Channels.newChannel(this.outputStream);
        createFtyp().getBox(newChannel);
        createMoov().getBox(newChannel);
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(this.source.length);
        for (StreamingTrack consumeSamplesCallable : this.source) {
            newFixedThreadPool.submit(new ConsumeSamplesCallable(consumeSamplesCallable));
        }
    }

    /* access modifiers changed from: private */
    public synchronized void consumeSample(StreamingTrack streamingTrack, StreamingSample streamingSample) throws IOException {
        SampleFlagsSampleExtension sampleFlagsSampleExtension = null;
        for (SampleExtension sampleExtension : streamingSample.getExtensions()) {
            if (sampleExtension instanceof SampleFlagsSampleExtension) {
                sampleFlagsSampleExtension = (SampleFlagsSampleExtension) sampleExtension;
            } else if (sampleExtension instanceof CompositionTimeSampleExtension) {
                CompositionTimeSampleExtension compositionTimeSampleExtension = (CompositionTimeSampleExtension) sampleExtension;
            }
        }
        this.currentTime += streamingSample.getDuration();
        this.fragmentBuffers.get(streamingTrack).add(streamingSample);
        long j = this.currentTime;
        long j2 = this.currentFragmentStartTime;
        long timescale = streamingTrack.getTimescale();
        Long.signum(timescale);
        if (j > j2 + (timescale * 3)) {
            if (this.fragmentBuffers.size() > 0 && (this.sampleDependencyTrackExtension == null || sampleFlagsSampleExtension == null || sampleFlagsSampleExtension.isSyncSample())) {
                WritableByteChannel newChannel = Channels.newChannel(this.outputStream);
                createMoof(streamingTrack).getBox(newChannel);
                createMdat(streamingTrack).getBox(newChannel);
                this.currentFragmentStartTime = this.currentTime;
                this.fragmentBuffers.clear();
            }
        }
    }

    private Box createMoof(StreamingTrack streamingTrack) {
        MovieFragmentBox movieFragmentBox = new MovieFragmentBox();
        createMfhd(this.sequenceNumber, movieFragmentBox);
        createTraf(streamingTrack, movieFragmentBox);
        TrackRunBox trackRunBox = movieFragmentBox.getTrackRunBoxes().get(0);
        trackRunBox.setDataOffset(1);
        trackRunBox.setDataOffset((int) (movieFragmentBox.getSize() + 8));
        this.sequenceNumber++;
        return movieFragmentBox;
    }

    /* access modifiers changed from: protected */
    public void createTfhd(StreamingTrack streamingTrack, TrackFragmentBox trackFragmentBox) {
        TrackFragmentHeaderBox trackFragmentHeaderBox = new TrackFragmentHeaderBox();
        trackFragmentHeaderBox.setDefaultSampleFlags(new SampleFlags());
        trackFragmentHeaderBox.setBaseDataOffset(-1);
        trackFragmentHeaderBox.setTrackId(((TrackIdTrackExtension) streamingTrack.getTrackExtension(TrackIdTrackExtension.class)).getTrackId());
        trackFragmentHeaderBox.setDefaultBaseIsMoof(true);
        trackFragmentBox.addBox(trackFragmentHeaderBox);
    }

    /* access modifiers changed from: protected */
    public void createTfdt(TrackFragmentBox trackFragmentBox) {
        TrackFragmentBaseMediaDecodeTimeBox trackFragmentBaseMediaDecodeTimeBox = new TrackFragmentBaseMediaDecodeTimeBox();
        trackFragmentBaseMediaDecodeTimeBox.setVersion(1);
        trackFragmentBaseMediaDecodeTimeBox.setBaseMediaDecodeTime(this.currentFragmentStartTime);
        trackFragmentBox.addBox(trackFragmentBaseMediaDecodeTimeBox);
    }

    /* access modifiers changed from: protected */
    public void createTrun(StreamingTrack streamingTrack, TrackFragmentBox trackFragmentBox) {
        TrackRunBox trackRunBox = new TrackRunBox();
        boolean z = true;
        trackRunBox.setVersion(1);
        trackRunBox.setSampleDurationPresent(true);
        trackRunBox.setSampleSizePresent(true);
        ArrayList arrayList = new ArrayList(this.fragmentBuffers.size());
        trackRunBox.setSampleCompositionTimeOffsetPresent(streamingTrack.getTrackExtension(CompositionTimeTrackExtension.class) != null);
        if (streamingTrack.getTrackExtension(SampleFlagsTrackExtension.class) == null) {
            z = false;
        }
        trackRunBox.setSampleFlagsPresent(z);
        for (StreamingSample streamingSample : this.fragmentBuffers.get(streamingTrack)) {
            TrackRunBox.Entry entry = new TrackRunBox.Entry();
            entry.setSampleSize((long) streamingSample.getContent().remaining());
            if (z) {
                SampleFlagsSampleExtension sampleFlagsSampleExtension = (SampleFlagsSampleExtension) StreamingSampleHelper.getSampleExtension(streamingSample, SampleFlagsSampleExtension.class);
                SampleFlags sampleFlags = new SampleFlags();
                sampleFlags.setIsLeading(sampleFlagsSampleExtension.getIsLeading());
                sampleFlags.setSampleIsDependedOn(sampleFlagsSampleExtension.getSampleIsDependedOn());
                sampleFlags.setSampleDependsOn(sampleFlagsSampleExtension.getSampleDependsOn());
                sampleFlags.setSampleHasRedundancy(sampleFlagsSampleExtension.getSampleHasRedundancy());
                sampleFlags.setSampleIsDifferenceSample(sampleFlagsSampleExtension.isSampleIsNonSyncSample());
                sampleFlags.setSamplePaddingValue(sampleFlagsSampleExtension.getSamplePaddingValue());
                sampleFlags.setSampleDegradationPriority(sampleFlagsSampleExtension.getSampleDegradationPriority());
                entry.setSampleFlags(sampleFlags);
            }
            entry.setSampleDuration(streamingSample.getDuration());
            if (trackRunBox.isSampleCompositionTimeOffsetPresent()) {
                entry.setSampleCompositionTimeOffset(((CompositionTimeSampleExtension) StreamingSampleHelper.getSampleExtension(streamingSample, CompositionTimeSampleExtension.class)).getCompositionTimeOffset());
            }
            arrayList.add(entry);
        }
        trackRunBox.setEntries(arrayList);
        trackFragmentBox.addBox(trackRunBox);
    }

    private void createTraf(StreamingTrack streamingTrack, MovieFragmentBox movieFragmentBox) {
        TrackFragmentBox trackFragmentBox = new TrackFragmentBox();
        movieFragmentBox.addBox(trackFragmentBox);
        createTfhd(streamingTrack, trackFragmentBox);
        createTfdt(trackFragmentBox);
        createTrun(streamingTrack, trackFragmentBox);
        streamingTrack.getTrackExtension(CencEncryptTrackExtension.class);
    }

    private void createMfhd(long j, MovieFragmentBox movieFragmentBox) {
        MovieFragmentHeaderBox movieFragmentHeaderBox = new MovieFragmentHeaderBox();
        movieFragmentHeaderBox.setSequenceNumber(j);
        movieFragmentBox.addBox(movieFragmentHeaderBox);
    }

    private Box createMdat(final StreamingTrack streamingTrack) {
        return new WriteOnlyBox(MediaDataBox.TYPE) {
            public long getSize() {
                long j = 8;
                for (StreamingSample content : MultiTrackFragmentedMp4Writer.this.fragmentBuffers.get(streamingTrack)) {
                    j += (long) content.getContent().remaining();
                }
                return j;
            }

            public void getBox(WritableByteChannel writableByteChannel) throws IOException {
                ArrayList arrayList = new ArrayList();
                long j = 8;
                for (StreamingSample content : MultiTrackFragmentedMp4Writer.this.fragmentBuffers.get(streamingTrack)) {
                    ByteBuffer content2 = content.getContent();
                    arrayList.add(content2);
                    j += (long) content2.remaining();
                }
                ByteBuffer allocate = ByteBuffer.allocate(8);
                IsoTypeWriter.writeUInt32(allocate, j);
                allocate.put(IsoFile.fourCCtoBytes(getType()));
                writableByteChannel.write((ByteBuffer) allocate.rewind());
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    writableByteChannel.write((ByteBuffer) it.next());
                }
            }
        };
    }
}
