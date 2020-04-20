package com.googlecode.mp4parser.authoring.tracks;

import android.support.v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AACTrackImpl extends AbstractTrack {
    static Map<Integer, String> audioObjectTypes = new HashMap();
    public static Map<Integer, Integer> samplingFrequencyIndexMap = new HashMap();
    long avgBitRate;
    int bufferSizeDB;
    /* access modifiers changed from: private */
    public DataSource dataSource;
    long[] decTimes;
    AdtsHeader firstHeader;
    private String lang;
    long maxBitRate;
    SampleDescriptionBox sampleDescriptionBox;
    private List<Sample> samples;
    TrackMetaData trackMetaData;

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public String getHandler() {
        return "soun";
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return null;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return null;
    }

    public long[] getSyncSamples() {
        return null;
    }

    static {
        audioObjectTypes.put(1, "AAC Main");
        audioObjectTypes.put(2, "AAC LC (Low Complexity)");
        audioObjectTypes.put(3, "AAC SSR (Scalable Sample Rate)");
        audioObjectTypes.put(4, "AAC LTP (Long Term Prediction)");
        audioObjectTypes.put(5, "SBR (Spectral Band Replication)");
        audioObjectTypes.put(6, "AAC Scalable");
        audioObjectTypes.put(7, "TwinVQ");
        audioObjectTypes.put(8, "CELP (Code Excited Linear Prediction)");
        audioObjectTypes.put(9, "HXVC (Harmonic Vector eXcitation Coding)");
        audioObjectTypes.put(10, "Reserved");
        audioObjectTypes.put(11, "Reserved");
        audioObjectTypes.put(12, "TTSI (Text-To-Speech Interface)");
        audioObjectTypes.put(13, "Main Synthesis");
        audioObjectTypes.put(14, "Wavetable Synthesis");
        audioObjectTypes.put(15, "General MIDI");
        audioObjectTypes.put(16, "Algorithmic Synthesis and Audio Effects");
        audioObjectTypes.put(17, "ER (Error Resilient) AAC LC");
        audioObjectTypes.put(18, "Reserved");
        audioObjectTypes.put(19, "ER AAC LTP");
        audioObjectTypes.put(20, "ER AAC Scalable");
        audioObjectTypes.put(21, "ER TwinVQ");
        audioObjectTypes.put(22, "ER BSAC (Bit-Sliced Arithmetic Coding)");
        audioObjectTypes.put(23, "ER AAC LD (Low Delay)");
        audioObjectTypes.put(24, "ER CELP");
        audioObjectTypes.put(25, "ER HVXC");
        audioObjectTypes.put(26, "ER HILN (Harmonic and Individual Lines plus Noise)");
        audioObjectTypes.put(27, "ER Parametric");
        audioObjectTypes.put(28, "SSC (SinuSoidal Coding)");
        audioObjectTypes.put(29, "PS (Parametric Stereo)");
        audioObjectTypes.put(30, "MPEG Surround");
        audioObjectTypes.put(31, "(Escape value)");
        audioObjectTypes.put(32, "Layer-1");
        audioObjectTypes.put(33, "Layer-2");
        audioObjectTypes.put(34, "Layer-3");
        audioObjectTypes.put(35, "DST (Direct Stream Transfer)");
        audioObjectTypes.put(36, "ALS (Audio Lossless)");
        audioObjectTypes.put(37, "SLS (Scalable LosslesS)");
        audioObjectTypes.put(38, "SLS non-core");
        audioObjectTypes.put(39, "ER AAC ELD (Enhanced Low Delay)");
        audioObjectTypes.put(40, "SMR (Symbolic Music Representation) Simple");
        audioObjectTypes.put(41, "SMR Main");
        audioObjectTypes.put(42, "USAC (Unified Speech and Audio Coding) (no SBR)");
        audioObjectTypes.put(43, "SAOC (Spatial Audio Object Coding)");
        audioObjectTypes.put(44, "LD MPEG Surround");
        audioObjectTypes.put(45, "USAC");
        samplingFrequencyIndexMap.put(96000, 0);
        samplingFrequencyIndexMap.put(88200, 1);
        samplingFrequencyIndexMap.put(64000, 2);
        samplingFrequencyIndexMap.put(48000, 3);
        samplingFrequencyIndexMap.put(44100, 4);
        samplingFrequencyIndexMap.put(32000, 5);
        samplingFrequencyIndexMap.put(24000, 6);
        samplingFrequencyIndexMap.put(22050, 7);
        samplingFrequencyIndexMap.put(16000, 8);
        samplingFrequencyIndexMap.put(12000, 9);
        samplingFrequencyIndexMap.put(11025, 10);
        samplingFrequencyIndexMap.put(8000, 11);
        samplingFrequencyIndexMap.put(0, 96000);
        samplingFrequencyIndexMap.put(1, 88200);
        samplingFrequencyIndexMap.put(2, 64000);
        samplingFrequencyIndexMap.put(3, 48000);
        samplingFrequencyIndexMap.put(4, 44100);
        samplingFrequencyIndexMap.put(5, 32000);
        samplingFrequencyIndexMap.put(6, 24000);
        samplingFrequencyIndexMap.put(7, 22050);
        samplingFrequencyIndexMap.put(8, 16000);
        samplingFrequencyIndexMap.put(9, 12000);
        samplingFrequencyIndexMap.put(10, 11025);
        samplingFrequencyIndexMap.put(11, 8000);
    }

    public void close() throws IOException {
        this.dataSource.close();
    }

    public AACTrackImpl(DataSource dataSource2) throws IOException {
        this(dataSource2, "eng");
    }

    public AACTrackImpl(DataSource dataSource2, String str) throws IOException {
        super(dataSource2.toString());
        this.trackMetaData = new TrackMetaData();
        this.lang = "eng";
        this.lang = str;
        this.dataSource = dataSource2;
        this.samples = new ArrayList();
        this.firstHeader = readSamples(dataSource2);
        double d = ((double) this.firstHeader.sampleRate) / 1024.0d;
        double size = ((double) this.samples.size()) / d;
        LinkedList linkedList = new LinkedList();
        Iterator<Sample> it = this.samples.iterator();
        long j = 0;
        while (true) {
            int i = 0;
            if (!it.hasNext()) {
                break;
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
        this.avgBitRate = (long) ((int) (((double) (j * 8)) / size));
        this.bufferSizeDB = 1536;
        this.sampleDescriptionBox = new SampleDescriptionBox();
        AudioSampleEntry audioSampleEntry = new AudioSampleEntry(AudioSampleEntry.TYPE3);
        if (this.firstHeader.channelconfig == 7) {
            audioSampleEntry.setChannelCount(8);
        } else {
            audioSampleEntry.setChannelCount(this.firstHeader.channelconfig);
        }
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
        decoderConfigDescriptor.setObjectTypeIndication(64);
        decoderConfigDescriptor.setStreamType(5);
        decoderConfigDescriptor.setBufferSizeDB(this.bufferSizeDB);
        decoderConfigDescriptor.setMaxBitRate(this.maxBitRate);
        decoderConfigDescriptor.setAvgBitRate(this.avgBitRate);
        AudioSpecificConfig audioSpecificConfig = new AudioSpecificConfig();
        audioSpecificConfig.setOriginalAudioObjectType(2);
        audioSpecificConfig.setSamplingFrequencyIndex(this.firstHeader.sampleFrequencyIndex);
        audioSpecificConfig.setChannelConfiguration(this.firstHeader.channelconfig);
        decoderConfigDescriptor.setAudioSpecificInfo(audioSpecificConfig);
        eSDescriptor.setDecoderConfigDescriptor(decoderConfigDescriptor);
        eSDescriptorBox.setEsDescriptor(eSDescriptor);
        audioSampleEntry.addBox(eSDescriptorBox);
        this.sampleDescriptionBox.addBox(audioSampleEntry);
        this.trackMetaData.setCreationTime(new Date());
        this.trackMetaData.setModificationTime(new Date());
        this.trackMetaData.setLanguage(str);
        this.trackMetaData.setVolume(1.0f);
        this.trackMetaData.setTimescale((long) this.firstHeader.sampleRate);
        this.decTimes = new long[this.samples.size()];
        Arrays.fill(this.decTimes, PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        return this.decTimes;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    class AdtsHeader {
        int bufferFullness;
        int channelconfig;
        int copyrightStart;
        int copyrightedStream;
        int frameLength;
        int home;
        int layer;
        int mpegVersion;
        int numAacFramesPerAdtsFrame;
        int original;
        int profile;
        int protectionAbsent;
        int sampleFrequencyIndex;
        int sampleRate;

        AdtsHeader() {
        }

        /* access modifiers changed from: package-private */
        public int getSize() {
            return (this.protectionAbsent == 0 ? 2 : 0) + 7;
        }
    }

    private AdtsHeader readADTSHeader(DataSource dataSource2) throws IOException {
        AdtsHeader adtsHeader = new AdtsHeader();
        ByteBuffer allocate = ByteBuffer.allocate(7);
        while (allocate.position() < 7) {
            if (dataSource2.read(allocate) == -1) {
                return null;
            }
        }
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer((ByteBuffer) allocate.rewind());
        if (bitReaderBuffer.readBits(12) == 4095) {
            adtsHeader.mpegVersion = bitReaderBuffer.readBits(1);
            adtsHeader.layer = bitReaderBuffer.readBits(2);
            adtsHeader.protectionAbsent = bitReaderBuffer.readBits(1);
            adtsHeader.profile = bitReaderBuffer.readBits(2) + 1;
            adtsHeader.sampleFrequencyIndex = bitReaderBuffer.readBits(4);
            adtsHeader.sampleRate = samplingFrequencyIndexMap.get(Integer.valueOf(adtsHeader.sampleFrequencyIndex)).intValue();
            bitReaderBuffer.readBits(1);
            adtsHeader.channelconfig = bitReaderBuffer.readBits(3);
            adtsHeader.original = bitReaderBuffer.readBits(1);
            adtsHeader.home = bitReaderBuffer.readBits(1);
            adtsHeader.copyrightedStream = bitReaderBuffer.readBits(1);
            adtsHeader.copyrightStart = bitReaderBuffer.readBits(1);
            adtsHeader.frameLength = bitReaderBuffer.readBits(13);
            adtsHeader.bufferFullness = bitReaderBuffer.readBits(11);
            adtsHeader.numAacFramesPerAdtsFrame = bitReaderBuffer.readBits(2) + 1;
            if (adtsHeader.numAacFramesPerAdtsFrame == 1) {
                if (adtsHeader.protectionAbsent == 0) {
                    dataSource2.read(ByteBuffer.allocate(2));
                }
                return adtsHeader;
            }
            throw new IOException("This muxer can only work with 1 AAC frame per ADTS frame");
        }
        throw new IOException("Expected Start Word 0xfff");
    }

    private AdtsHeader readSamples(DataSource dataSource2) throws IOException {
        AdtsHeader adtsHeader = null;
        while (true) {
            AdtsHeader readADTSHeader = readADTSHeader(dataSource2);
            if (readADTSHeader == null) {
                return adtsHeader;
            }
            if (adtsHeader == null) {
                adtsHeader = readADTSHeader;
            }
            final long position = dataSource2.position();
            final long size = (long) (readADTSHeader.frameLength - readADTSHeader.getSize());
            this.samples.add(new Sample() {
                public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                    AACTrackImpl.this.dataSource.transferTo(position, size, writableByteChannel);
                }

                public long getSize() {
                    return size;
                }

                public ByteBuffer asByteBuffer() {
                    try {
                        return AACTrackImpl.this.dataSource.map(position, size);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            dataSource2.position((dataSource2.position() + ((long) readADTSHeader.frameLength)) - ((long) readADTSHeader.getSize()));
        }
    }

    public String toString() {
        return "AACTrackImpl{sampleRate=" + this.firstHeader.sampleRate + ", channelconfig=" + this.firstHeader.channelconfig + '}';
    }
}
