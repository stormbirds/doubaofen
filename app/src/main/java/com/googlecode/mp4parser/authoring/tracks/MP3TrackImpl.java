package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MP3TrackImpl extends AbstractTrack {
    private static final int[] BIT_RATE;
    private static final int ES_OBJECT_TYPE_INDICATION = 107;
    private static final int ES_STREAM_TYPE = 5;
    private static final int MPEG_L3 = 1;
    private static final int MPEG_V1 = 3;
    private static final int SAMPLES_PER_FRAME = 1152;
    private static final int[] SAMPLE_RATE;
    long avgBitRate;
    private final DataSource dataSource;
    private long[] durations;
    MP3Header firstHeader;
    long maxBitRate;
    SampleDescriptionBox sampleDescriptionBox;
    private List<Sample> samples;
    TrackMetaData trackMetaData;

    public String getHandler() {
        return "soun";
    }

    public String toString() {
        return "MP3TrackImpl";
    }

    static {
        int[] iArr = new int[4];
        iArr[0] = 44100;
        iArr[1] = 48000;
        iArr[2] = 32000;
        SAMPLE_RATE = iArr;
        int[] iArr2 = new int[16];
        iArr2[1] = 32000;
        iArr2[2] = 40000;
        iArr2[3] = 48000;
        iArr2[4] = 56000;
        iArr2[5] = 64000;
        iArr2[6] = 80000;
        iArr2[7] = 96000;
        iArr2[8] = 112000;
        iArr2[9] = 128000;
        iArr2[10] = 160000;
        iArr2[11] = 192000;
        iArr2[12] = 224000;
        iArr2[13] = 256000;
        iArr2[14] = 320000;
        BIT_RATE = iArr2;
    }

    public MP3TrackImpl(DataSource dataSource2) throws IOException {
        this(dataSource2, "eng");
    }

    public MP3TrackImpl(DataSource dataSource2, String str) throws IOException {
        super(dataSource2.toString());
        this.trackMetaData = new TrackMetaData();
        this.dataSource = dataSource2;
        this.samples = new LinkedList();
        this.firstHeader = readSamples(dataSource2);
        double d = ((double) this.firstHeader.sampleRate) / 1152.0d;
        double size = ((double) this.samples.size()) / d;
        LinkedList linkedList = new LinkedList();
        Iterator<Sample> it = this.samples.iterator();
        long j = 0;
        while (true) {
            int i = 0;
            if (!it.hasNext()) {
                this.avgBitRate = (long) ((int) (((double) (j * 8)) / size));
                this.sampleDescriptionBox = new SampleDescriptionBox();
                AudioSampleEntry audioSampleEntry = new AudioSampleEntry(AudioSampleEntry.TYPE3);
                audioSampleEntry.setChannelCount(this.firstHeader.channelCount);
                audioSampleEntry.setSampleRate((long) this.firstHeader.sampleRate);
                audioSampleEntry.setDataReferenceIndex(1);
                audioSampleEntry.setSampleSize(16);
                ESDescriptorBox eSDescriptorBox = new ESDescriptorBox();
                ESDescriptor eSDescriptor = new ESDescriptor();
                eSDescriptor.setEsId(0);
                SLConfigDescriptor sLConfigDescriptor = new SLConfigDescriptor();
                sLConfigDescriptor.setPredefined(2);
                eSDescriptor.setSlConfigDescriptor(sLConfigDescriptor);
                DecoderConfigDescriptor decoderConfigDescriptor = new DecoderConfigDescriptor();
                decoderConfigDescriptor.setObjectTypeIndication(107);
                decoderConfigDescriptor.setStreamType(5);
                decoderConfigDescriptor.setMaxBitRate(this.maxBitRate);
                decoderConfigDescriptor.setAvgBitRate(this.avgBitRate);
                eSDescriptor.setDecoderConfigDescriptor(decoderConfigDescriptor);
                eSDescriptorBox.setData(eSDescriptor.serialize());
                audioSampleEntry.addBox(eSDescriptorBox);
                this.sampleDescriptionBox.addBox(audioSampleEntry);
                this.trackMetaData.setCreationTime(new Date());
                this.trackMetaData.setModificationTime(new Date());
                this.trackMetaData.setLanguage(str);
                this.trackMetaData.setVolume(1.0f);
                this.trackMetaData.setTimescale((long) this.firstHeader.sampleRate);
                this.durations = new long[this.samples.size()];
                Arrays.fill(this.durations, 1152);
                return;
            }
            int size2 = (int) it.next().getSize();
            j += (long) size2;
            linkedList.add(Integer.valueOf(size2));
            while (((double) linkedList.size()) > d) {
                linkedList.pop();
            }
            if (linkedList.size() == ((int) d)) {
                Iterator it2 = linkedList.iterator();
                while (it2.hasNext()) {
                    i += ((Integer) it2.next()).intValue();
                }
                double size3 = ((((double) i) * 8.0d) / ((double) linkedList.size())) * d;
                if (size3 > ((double) this.maxBitRate)) {
                    this.maxBitRate = (long) ((int) size3);
                }
            }
        }
    }

    public void close() throws IOException {
        this.dataSource.close();
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        return this.durations;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    private MP3Header readSamples(DataSource dataSource2) throws IOException {
        MP3Header mP3Header = null;
        while (true) {
            long position = dataSource2.position();
            MP3Header readMP3Header = readMP3Header(dataSource2);
            if (readMP3Header == null) {
                return mP3Header;
            }
            if (mP3Header == null) {
                mP3Header = readMP3Header;
            }
            dataSource2.position(position);
            ByteBuffer allocate = ByteBuffer.allocate(readMP3Header.getFrameLength());
            dataSource2.read(allocate);
            allocate.rewind();
            this.samples.add(new SampleImpl(allocate));
        }
    }

    private MP3Header readMP3Header(DataSource dataSource2) throws IOException {
        MP3Header mP3Header = new MP3Header();
        ByteBuffer allocate = ByteBuffer.allocate(4);
        while (allocate.position() < 4) {
            if (dataSource2.read(allocate) == -1) {
                return null;
            }
        }
        int i = 2;
        if (allocate.get(0) == 84 && allocate.get(1) == 65 && allocate.get(2) == 71) {
            return null;
        }
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer((ByteBuffer) allocate.rewind());
        if (bitReaderBuffer.readBits(11) == 2047) {
            mP3Header.mpegVersion = bitReaderBuffer.readBits(2);
            if (mP3Header.mpegVersion == 3) {
                mP3Header.layer = bitReaderBuffer.readBits(2);
                if (mP3Header.layer == 1) {
                    mP3Header.protectionAbsent = bitReaderBuffer.readBits(1);
                    mP3Header.bitRateIndex = bitReaderBuffer.readBits(4);
                    mP3Header.bitRate = BIT_RATE[mP3Header.bitRateIndex];
                    if (mP3Header.bitRate != 0) {
                        mP3Header.sampleFrequencyIndex = bitReaderBuffer.readBits(2);
                        mP3Header.sampleRate = SAMPLE_RATE[mP3Header.sampleFrequencyIndex];
                        if (mP3Header.sampleRate != 0) {
                            mP3Header.padding = bitReaderBuffer.readBits(1);
                            bitReaderBuffer.readBits(1);
                            mP3Header.channelMode = bitReaderBuffer.readBits(2);
                            if (mP3Header.channelMode == 3) {
                                i = 1;
                            }
                            mP3Header.channelCount = i;
                            return mP3Header;
                        }
                        throw new IOException("Unexpected (reserved) sample rate frequency");
                    }
                    throw new IOException("Unexpected (free/bad) bit rate");
                }
                throw new IOException("Expected Layer III");
            }
            throw new IOException("Expected MPEG Version 1 (ISO/IEC 11172-3)");
        }
        throw new IOException("Expected Start Word 0x7ff");
    }

    class MP3Header {
        int bitRate;
        int bitRateIndex;
        int channelCount;
        int channelMode;
        int layer;
        int mpegVersion;
        int padding;
        int protectionAbsent;
        int sampleFrequencyIndex;
        int sampleRate;

        MP3Header() {
        }

        /* access modifiers changed from: package-private */
        public int getFrameLength() {
            return ((this.bitRate * 144) / this.sampleRate) + this.padding;
        }
    }
}
