package com.googlecode.mp4parser.authoring.tracks;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.DTSSpecificBox;
import io.reactivex.annotations.SchedulerSupport;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kotlin.UShort;
import kotlin.jvm.internal.ByteCompanionObject;
import org.jetbrains.anko.DimensionsKt;

public class DTSTrackImpl extends AbstractTrack {
    private static final int BUFFER = 67108864;
    int bcCoreBitRate = 0;
    int bcCoreChannelMask = 0;
    int bcCoreMaxSampleRate = 0;
    int bitrate;
    int channelCount;
    int channelMask = 0;
    int codecDelayAtMaxFs = 0;
    int coreBitRate = 0;
    int coreChannelMask = 0;
    int coreFramePayloadInBytes = 0;
    int coreMaxSampleRate = 0;
    boolean coreSubStreamPresent = false;
    private int dataOffset = 0;
    private DataSource dataSource;
    DTSSpecificBox ddts = new DTSSpecificBox();
    int extAvgBitrate = 0;
    int extFramePayloadInBytes = 0;
    int extPeakBitrate = 0;
    int extSmoothBuffSize = 0;
    boolean extensionSubStreamPresent = false;
    int frameSize = 0;
    boolean isVBR = false;
    private String lang = "eng";
    int lbrCodingPresent = 0;
    int lsbTrimPercent = 0;
    int maxSampleRate = 0;
    int numExtSubStreams = 0;
    int numFramesTotal = 0;
    int numSamplesOrigAudioAtMaxFs = 0;
    SampleDescriptionBox sampleDescriptionBox;
    private long[] sampleDurations;
    int sampleSize;
    int samplerate;
    private List<Sample> samples;
    int samplesPerFrame;
    int samplesPerFrameAtMaxFs = 0;
    TrackMetaData trackMetaData = new TrackMetaData();
    String type = SchedulerSupport.NONE;

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public String getHandler() {
        return "soun";
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return null;
    }

    public long[] getSyncSamples() {
        return null;
    }

    public DTSTrackImpl(DataSource dataSource2, String str) throws IOException {
        super(dataSource2.toString());
        this.lang = str;
        this.dataSource = dataSource2;
        parse();
    }

    public DTSTrackImpl(DataSource dataSource2) throws IOException {
        super(dataSource2.toString());
        this.dataSource = dataSource2;
        parse();
    }

    public void close() throws IOException {
        this.dataSource.close();
    }

    private void parse() throws IOException {
        if (readVariables()) {
            this.sampleDescriptionBox = new SampleDescriptionBox();
            AudioSampleEntry audioSampleEntry = new AudioSampleEntry(this.type);
            audioSampleEntry.setChannelCount(this.channelCount);
            audioSampleEntry.setSampleRate((long) this.samplerate);
            audioSampleEntry.setDataReferenceIndex(1);
            audioSampleEntry.setSampleSize(16);
            audioSampleEntry.addBox(this.ddts);
            this.sampleDescriptionBox.addBox(audioSampleEntry);
            this.trackMetaData.setCreationTime(new Date());
            this.trackMetaData.setModificationTime(new Date());
            this.trackMetaData.setLanguage(this.lang);
            this.trackMetaData.setTimescale((long) this.samplerate);
            return;
        }
        throw new IOException();
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        return this.sampleDurations;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    private void parseDtshdhdr(int i, ByteBuffer byteBuffer) {
        byteBuffer.getInt();
        byteBuffer.get();
        byteBuffer.getInt();
        byteBuffer.get();
        short s = byteBuffer.getShort();
        byteBuffer.get();
        this.numExtSubStreams = byteBuffer.get();
        if ((s & 1) == 1) {
            this.isVBR = true;
        }
        if ((s & 8) == 8) {
            this.coreSubStreamPresent = true;
        }
        if ((s & 16) == 16) {
            this.extensionSubStreamPresent = true;
            this.numExtSubStreams++;
        } else {
            this.numExtSubStreams = 0;
        }
        for (int i2 = 14; i2 < i; i2++) {
            byteBuffer.get();
        }
    }

    private boolean parseCoressmd(int i, ByteBuffer byteBuffer) {
        this.coreMaxSampleRate = (byteBuffer.get() << 16) | (byteBuffer.getShort() & UShort.MAX_VALUE);
        this.coreBitRate = byteBuffer.getShort();
        this.coreChannelMask = byteBuffer.getShort();
        this.coreFramePayloadInBytes = byteBuffer.getInt();
        for (int i2 = 11; i2 < i; i2++) {
            byteBuffer.get();
        }
        return true;
    }

    private boolean parseAuprhdr(int i, ByteBuffer byteBuffer) {
        int i2;
        byteBuffer.get();
        short s = byteBuffer.getShort();
        this.maxSampleRate = (byteBuffer.get() << 16) | (byteBuffer.getShort() & UShort.MAX_VALUE);
        this.numFramesTotal = byteBuffer.getInt();
        this.samplesPerFrameAtMaxFs = byteBuffer.getShort();
        this.numSamplesOrigAudioAtMaxFs = (byteBuffer.get() << 32) | (byteBuffer.getInt() & SupportMenu.USER_MASK);
        this.channelMask = byteBuffer.getShort();
        this.codecDelayAtMaxFs = byteBuffer.getShort();
        if ((s & 3) == 3) {
            this.bcCoreMaxSampleRate = (byteBuffer.get() << 16) | (byteBuffer.getShort() & UShort.MAX_VALUE);
            this.bcCoreBitRate = byteBuffer.getShort();
            this.bcCoreChannelMask = byteBuffer.getShort();
            i2 = 28;
        } else {
            i2 = 21;
        }
        if ((s & 4) > 0) {
            this.lsbTrimPercent = byteBuffer.get();
            i2++;
        }
        if ((s & 8) > 0) {
            this.lbrCodingPresent = 1;
        }
        while (i2 < i) {
            byteBuffer.get();
            i2++;
        }
        return true;
    }

    private boolean parseExtssmd(int i, ByteBuffer byteBuffer) {
        int i2;
        this.extAvgBitrate = (byteBuffer.get() << 16) | (byteBuffer.getShort() & UShort.MAX_VALUE);
        if (this.isVBR) {
            this.extPeakBitrate = (byteBuffer.get() << 16) | (byteBuffer.getShort() & UShort.MAX_VALUE);
            this.extSmoothBuffSize = byteBuffer.getShort();
            i2 = 8;
        } else {
            this.extFramePayloadInBytes = byteBuffer.getInt();
            i2 = 7;
        }
        while (i2 < i) {
            byteBuffer.get();
            i2++;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00af, code lost:
        if (r11 == true) goto L_0x008d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean readVariables() throws java.io.IOException {
        /*
            r24 = this;
            r6 = r24
            com.googlecode.mp4parser.DataSource r0 = r6.dataSource
            r1 = 0
            r3 = 25000(0x61a8, double:1.23516E-319)
            java.nio.ByteBuffer r0 = r0.map(r1, r3)
            int r1 = r0.getInt()
            int r2 = r0.getInt()
            r3 = 1146377032(0x44545348, float:849.3013)
            if (r1 != r3) goto L_0x04fe
            r3 = 1145586770(0x44484452, float:801.0675)
            if (r2 != r3) goto L_0x04fe
        L_0x001e:
            r3 = 1398035021(0x5354524d, float:9.1191384E11)
            r4 = 0
            if (r1 != r3) goto L_0x0029
            r3 = 1145132097(0x44415441, float:773.31647)
            if (r2 == r3) goto L_0x0031
        L_0x0029:
            int r3 = r0.remaining()
            r5 = 100
            if (r3 > r5) goto L_0x04a4
        L_0x0031:
            long r7 = r0.getLong()
            int r1 = r0.position()
            r6.dataOffset = r1
            r1 = -1
            r2 = 0
            r3 = -1
            r5 = -1
            r10 = -1
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 0
            r15 = 0
            r16 = 0
            r17 = 0
            r18 = 0
            r19 = 0
        L_0x004d:
            r9 = 2
            if (r2 == 0) goto L_0x02c1
            int r0 = r6.samplesPerFrame
            r2 = 512(0x200, float:7.175E-43)
            if (r0 == r2) goto L_0x006a
            r2 = 1024(0x400, float:1.435E-42)
            if (r0 == r2) goto L_0x0068
            r2 = 2048(0x800, float:2.87E-42)
            if (r0 == r2) goto L_0x0066
            r2 = 4096(0x1000, float:5.74E-42)
            if (r0 == r2) goto L_0x0064
            r0 = -1
            goto L_0x006b
        L_0x0064:
            r0 = 3
            goto L_0x006b
        L_0x0066:
            r0 = 2
            goto L_0x006b
        L_0x0068:
            r0 = 1
            goto L_0x006b
        L_0x006a:
            r0 = 0
        L_0x006b:
            if (r0 != r1) goto L_0x006e
            return r4
        L_0x006e:
            r1 = 31
            if (r3 == 0) goto L_0x0078
            if (r3 == r9) goto L_0x0078
            switch(r3) {
                case 4: goto L_0x0078;
                case 5: goto L_0x0078;
                case 6: goto L_0x0078;
                case 7: goto L_0x0078;
                case 8: goto L_0x0078;
                case 9: goto L_0x0078;
                default: goto L_0x0077;
            }
        L_0x0077:
            goto L_0x0079
        L_0x0078:
            r1 = r3
        L_0x0079:
            java.lang.String r2 = "dtsh"
            if (r5 != 0) goto L_0x00bd
            r3 = 1
            if (r11 != r3) goto L_0x0090
            if (r15 != 0) goto L_0x0089
            r2 = 17
            java.lang.String r10 = "dtsl"
            r6.type = r10
            goto L_0x00b3
        L_0x0089:
            r10 = 21
            r6.type = r2
        L_0x008d:
            r2 = 21
            goto L_0x00b3
        L_0x0090:
            if (r14 != r3) goto L_0x0099
            r2 = 18
            java.lang.String r10 = "dtse"
            r6.type = r10
            goto L_0x00b3
        L_0x0099:
            if (r15 != r3) goto L_0x00b2
            r6.type = r2
            r12 = r16
            if (r12 != 0) goto L_0x00a6
            if (r11 != 0) goto L_0x00a6
            r2 = 19
            goto L_0x00b3
        L_0x00a6:
            if (r12 != r3) goto L_0x00ad
            if (r11 != 0) goto L_0x00ad
            r2 = 20
            goto L_0x00b3
        L_0x00ad:
            if (r12 != 0) goto L_0x00b2
            if (r11 != r3) goto L_0x00b2
            goto L_0x008d
        L_0x00b2:
            r2 = 0
        L_0x00b3:
            int r3 = r6.maxSampleRate
            r6.samplerate = r3
            r3 = 24
            r6.sampleSize = r3
            goto L_0x01cf
        L_0x00bd:
            r12 = r16
            r3 = 1
            if (r10 >= r3) goto L_0x00ec
            if (r13 <= 0) goto L_0x00e5
            r3 = r17
            if (r3 == 0) goto L_0x00de
            if (r3 == r9) goto L_0x00d7
            r10 = 6
            if (r3 == r10) goto L_0x00d2
            r6.type = r2
        L_0x00cf:
            r2 = 0
            goto L_0x01cf
        L_0x00d2:
            r6.type = r2
            r2 = 3
            goto L_0x01cf
        L_0x00d7:
            java.lang.String r2 = "dtsc"
            r6.type = r2
            r2 = 4
            goto L_0x01cf
        L_0x00de:
            java.lang.String r2 = "dtsc"
            r6.type = r2
            r2 = 2
            goto L_0x01cf
        L_0x00e5:
            java.lang.String r2 = "dtsc"
            r6.type = r2
            r2 = 1
            goto L_0x01cf
        L_0x00ec:
            r3 = r17
            r6.type = r2
            if (r13 != 0) goto L_0x015e
            if (r15 != 0) goto L_0x0106
            r2 = 1
            if (r12 != r2) goto L_0x0106
            r2 = r18
            r10 = r19
            if (r2 != 0) goto L_0x010a
            if (r10 != 0) goto L_0x010a
            if (r11 != 0) goto L_0x010a
            if (r14 != 0) goto L_0x010a
            r2 = 5
            goto L_0x01cf
        L_0x0106:
            r2 = r18
            r10 = r19
        L_0x010a:
            if (r15 != 0) goto L_0x011a
            if (r12 != 0) goto L_0x011a
            if (r2 != 0) goto L_0x011a
            r3 = 1
            if (r10 != r3) goto L_0x011b
            if (r11 != 0) goto L_0x011b
            if (r14 != 0) goto L_0x011b
            r2 = 6
            goto L_0x01cf
        L_0x011a:
            r3 = 1
        L_0x011b:
            if (r15 != 0) goto L_0x012b
            if (r12 != r3) goto L_0x012b
            if (r2 != 0) goto L_0x012b
            if (r10 != r3) goto L_0x012b
            if (r11 != 0) goto L_0x012b
            if (r14 != 0) goto L_0x012b
            r2 = 9
            goto L_0x01cf
        L_0x012b:
            if (r15 != 0) goto L_0x013c
            if (r12 != 0) goto L_0x013c
            r3 = 1
            if (r2 != r3) goto L_0x013c
            if (r10 != 0) goto L_0x013c
            if (r11 != 0) goto L_0x013c
            if (r14 != 0) goto L_0x013c
            r2 = 10
            goto L_0x01cf
        L_0x013c:
            if (r15 != 0) goto L_0x014d
            r3 = 1
            if (r12 != r3) goto L_0x014d
            if (r2 != r3) goto L_0x014d
            if (r10 != 0) goto L_0x014d
            if (r11 != 0) goto L_0x014d
            if (r14 != 0) goto L_0x014d
            r2 = 13
            goto L_0x01cf
        L_0x014d:
            if (r15 != 0) goto L_0x00cf
            if (r12 != 0) goto L_0x00cf
            if (r2 != 0) goto L_0x00cf
            if (r10 != 0) goto L_0x00cf
            r2 = 1
            if (r11 != r2) goto L_0x00cf
            if (r14 != 0) goto L_0x00cf
            r2 = 14
            goto L_0x01cf
        L_0x015e:
            r2 = r18
            r10 = r19
            if (r3 != 0) goto L_0x0174
            if (r15 != 0) goto L_0x0174
            if (r12 != 0) goto L_0x0174
            if (r2 != 0) goto L_0x0174
            r13 = 1
            if (r10 != r13) goto L_0x0174
            if (r11 != 0) goto L_0x0174
            if (r14 != 0) goto L_0x0174
            r2 = 7
            goto L_0x01cf
        L_0x0174:
            r13 = 6
            if (r3 != r13) goto L_0x0187
            if (r15 != 0) goto L_0x0187
            if (r12 != 0) goto L_0x0187
            if (r2 != 0) goto L_0x0187
            r13 = 1
            if (r10 != r13) goto L_0x0187
            if (r11 != 0) goto L_0x0187
            if (r14 != 0) goto L_0x0187
            r2 = 8
            goto L_0x01cf
        L_0x0187:
            if (r3 != 0) goto L_0x0199
            if (r15 != 0) goto L_0x0199
            if (r12 != 0) goto L_0x0199
            r13 = 1
            if (r2 != r13) goto L_0x0199
            if (r10 != 0) goto L_0x0199
            if (r11 != 0) goto L_0x0199
            if (r14 != 0) goto L_0x0199
            r2 = 11
            goto L_0x01cf
        L_0x0199:
            r13 = 6
            if (r3 != r13) goto L_0x01ac
            if (r15 != 0) goto L_0x01ac
            if (r12 != 0) goto L_0x01ac
            r13 = 1
            if (r2 != r13) goto L_0x01ac
            if (r10 != 0) goto L_0x01ac
            if (r11 != 0) goto L_0x01ac
            if (r14 != 0) goto L_0x01ac
            r2 = 12
            goto L_0x01cf
        L_0x01ac:
            if (r3 != 0) goto L_0x01be
            if (r15 != 0) goto L_0x01be
            if (r12 != 0) goto L_0x01be
            if (r2 != 0) goto L_0x01be
            if (r10 != 0) goto L_0x01be
            r13 = 1
            if (r11 != r13) goto L_0x01be
            if (r14 != 0) goto L_0x01be
            r2 = 15
            goto L_0x01cf
        L_0x01be:
            if (r3 != r9) goto L_0x00cf
            if (r15 != 0) goto L_0x00cf
            if (r12 != 0) goto L_0x00cf
            if (r2 != 0) goto L_0x00cf
            if (r10 != 0) goto L_0x00cf
            r2 = 1
            if (r11 != r2) goto L_0x00cf
            if (r14 != 0) goto L_0x00cf
            r2 = 16
        L_0x01cf:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r3 = r6.ddts
            int r10 = r6.maxSampleRate
            long r10 = (long) r10
            r3.setDTSSamplingFrequency(r10)
            boolean r3 = r6.isVBR
            if (r3 == 0) goto L_0x01e9
            com.googlecode.mp4parser.boxes.DTSSpecificBox r3 = r6.ddts
            int r10 = r6.coreBitRate
            int r11 = r6.extPeakBitrate
            int r10 = r10 + r11
            int r10 = r10 * 1000
            long r10 = (long) r10
            r3.setMaxBitRate(r10)
            goto L_0x01f6
        L_0x01e9:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r3 = r6.ddts
            int r10 = r6.coreBitRate
            int r11 = r6.extAvgBitrate
            int r10 = r10 + r11
            int r10 = r10 * 1000
            long r10 = (long) r10
            r3.setMaxBitRate(r10)
        L_0x01f6:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r3 = r6.ddts
            int r10 = r6.coreBitRate
            int r11 = r6.extAvgBitrate
            int r10 = r10 + r11
            int r10 = r10 * 1000
            long r10 = (long) r10
            r3.setAvgBitRate(r10)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r3 = r6.ddts
            int r10 = r6.sampleSize
            r3.setPcmSampleDepth(r10)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r3 = r6.ddts
            r3.setFrameDuration(r0)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r0.setStreamConstruction(r2)
            int r0 = r6.coreChannelMask
            r2 = r0 & 8
            if (r2 > 0) goto L_0x0225
            r0 = r0 & 4096(0x1000, float:5.74E-42)
            if (r0 <= 0) goto L_0x021f
            goto L_0x0225
        L_0x021f:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r0.setCoreLFEPresent(r4)
            goto L_0x022b
        L_0x0225:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r2 = 1
            r0.setCoreLFEPresent(r2)
        L_0x022b:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r0.setCoreLayout(r1)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            int r1 = r6.coreFramePayloadInBytes
            r0.setCoreSize(r1)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r0.setStereoDownmix(r4)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r1 = 4
            r0.setRepresentationType(r1)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            int r1 = r6.channelMask
            r0.setChannelLayout(r1)
            int r0 = r6.coreMaxSampleRate
            if (r0 <= 0) goto L_0x0258
            int r0 = r6.extAvgBitrate
            if (r0 <= 0) goto L_0x0258
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r1 = 1
            r0.setMultiAssetFlag(r1)
            goto L_0x025d
        L_0x0258:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r0.setMultiAssetFlag(r4)
        L_0x025d:
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            int r1 = r6.lbrCodingPresent
            r0.setLBRDurationMod(r1)
            com.googlecode.mp4parser.boxes.DTSSpecificBox r0 = r6.ddts
            r0.setReservedBoxPresent(r4)
            r6.channelCount = r4
        L_0x026b:
            r0 = 16
            if (r4 < r0) goto L_0x0290
            com.googlecode.mp4parser.DataSource r1 = r6.dataSource
            int r2 = r6.dataOffset
            r0 = r24
            r3 = r7
            java.util.List r0 = r0.generateSamples(r1, r2, r3, r5)
            r6.samples = r0
            java.util.List<com.googlecode.mp4parser.authoring.Sample> r0 = r6.samples
            int r0 = r0.size()
            long[] r0 = new long[r0]
            r6.sampleDurations = r0
            long[] r0 = r6.sampleDurations
            int r1 = r6.samplesPerFrame
            long r1 = (long) r1
            java.util.Arrays.fill(r0, r1)
            r0 = 1
            return r0
        L_0x0290:
            r0 = 1
            int r1 = r6.channelMask
            int r1 = r1 >> r4
            r1 = r1 & r0
            if (r1 != r0) goto L_0x02bb
            r0 = 12
            if (r4 == 0) goto L_0x02b4
            if (r4 == r0) goto L_0x02b4
            r1 = 14
            if (r4 == r1) goto L_0x02b4
            r1 = 3
            if (r4 == r1) goto L_0x02b4
            r1 = 4
            if (r4 == r1) goto L_0x02b4
            r1 = 7
            if (r4 == r1) goto L_0x02b4
            r1 = 8
            if (r4 == r1) goto L_0x02b4
            int r1 = r6.channelCount
            int r1 = r1 + r9
            r6.channelCount = r1
            goto L_0x02bd
        L_0x02b4:
            int r1 = r6.channelCount
            r2 = 1
            int r1 = r1 + r2
            r6.channelCount = r1
            goto L_0x02be
        L_0x02bb:
            r0 = 12
        L_0x02bd:
            r2 = 1
        L_0x02be:
            int r4 = r4 + 1
            goto L_0x026b
        L_0x02c1:
            r21 = r16
            r16 = r17
            r22 = r18
            r23 = r19
            r1 = 1
            r17 = 12
            int r19 = r0.position()
            int r9 = r0.getInt()
            r4 = 2147385345(0x7ffe8001, float:NaN)
            if (r9 != r4) goto L_0x03ca
            if (r5 != r1) goto L_0x02e8
            r17 = r16
            r16 = r21
            r18 = r22
            r19 = r23
            r1 = -1
            r2 = 1
            r4 = 0
            goto L_0x004d
        L_0x02e8:
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer r3 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer
            r3.<init>(r0)
            int r4 = r3.readBits(r1)
            r5 = 5
            int r9 = r3.readBits(r5)
            int r5 = r3.readBits(r1)
            if (r4 != r1) goto L_0x03c8
            r4 = 31
            if (r9 != r4) goto L_0x03c8
            if (r5 == 0) goto L_0x0304
            goto L_0x03c8
        L_0x0304:
            r4 = 7
            int r9 = r3.readBits(r4)
            int r9 = r9 + r1
            int r9 = r9 * 32
            r6.samplesPerFrame = r9
            r1 = 14
            int r1 = r3.readBits(r1)
            int r4 = r6.frameSize
            int r9 = r1 + 1
            int r4 = r4 + r9
            r6.frameSize = r4
            r4 = 6
            int r9 = r3.readBits(r4)
            r4 = 4
            int r13 = r3.readBits(r4)
            int r4 = r6.getSampleRate(r13)
            r6.samplerate = r4
            r4 = 5
            int r13 = r3.readBits(r4)
            int r4 = r6.getBitRate(r13)
            r6.bitrate = r4
            r4 = 1
            int r13 = r3.readBits(r4)
            if (r13 == 0) goto L_0x033f
            r13 = 0
            return r13
        L_0x033f:
            r3.readBits(r4)
            r3.readBits(r4)
            r3.readBits(r4)
            r3.readBits(r4)
            r13 = 3
            int r17 = r3.readBits(r13)
            int r13 = r3.readBits(r4)
            r3.readBits(r4)
            r20 = r2
            r2 = 2
            r3.readBits(r2)
            r3.readBits(r4)
            if (r5 != r4) goto L_0x0367
            r5 = 16
            r3.readBits(r5)
        L_0x0367:
            r3.readBits(r4)
            r5 = 4
            int r4 = r3.readBits(r5)
            r3.readBits(r2)
            r5 = 3
            int r2 = r3.readBits(r5)
            if (r2 == 0) goto L_0x0394
            r5 = 1
            if (r2 == r5) goto L_0x0394
            r5 = 2
            if (r2 == r5) goto L_0x038f
            r5 = 3
            if (r2 == r5) goto L_0x038f
            r5 = 5
            if (r2 == r5) goto L_0x038a
            r5 = 6
            if (r2 == r5) goto L_0x038a
            r2 = 0
            return r2
        L_0x038a:
            r2 = 24
            r6.sampleSize = r2
            goto L_0x0398
        L_0x038f:
            r2 = 20
            r6.sampleSize = r2
            goto L_0x0398
        L_0x0394:
            r2 = 16
            r6.sampleSize = r2
        L_0x0398:
            r2 = 1
            r3.readBits(r2)
            r3.readBits(r2)
            r5 = 6
            if (r4 == r5) goto L_0x03af
            r5 = 7
            if (r4 == r5) goto L_0x03aa
            r4 = 4
            r3.readBits(r4)
            goto L_0x03b3
        L_0x03aa:
            r4 = 4
            r3.readBits(r4)
            goto L_0x03b3
        L_0x03af:
            r4 = 4
            r3.readBits(r4)
        L_0x03b3:
            int r19 = r19 + r1
            int r1 = r19 + 1
            r0.position(r1)
            r3 = r9
            r2 = r20
            r16 = r21
            r18 = r22
            r19 = r23
            r1 = -1
            r4 = 0
            r5 = 1
            goto L_0x004d
        L_0x03c8:
            r0 = 0
            return r0
        L_0x03ca:
            r20 = r2
            r1 = 16
            r2 = 20
            r4 = 1683496997(0x64582025, float:1.5947252E22)
            if (r9 != r4) goto L_0x048c
            r4 = -1
            if (r5 != r4) goto L_0x03dd
            int r5 = r6.samplesPerFrameAtMaxFs
            r6.samplesPerFrame = r5
            r5 = 0
        L_0x03dd:
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer r9 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer
            r9.<init>(r0)
            r10 = 8
            r9.readBits(r10)
            r1 = 2
            r9.readBits(r1)
            r1 = 1
            int r18 = r9.readBits(r1)
            if (r18 != 0) goto L_0x03f5
            r2 = 16
            goto L_0x03f7
        L_0x03f5:
            r10 = 12
        L_0x03f7:
            int r10 = r9.readBits(r10)
            int r10 = r10 + r1
            int r2 = r9.readBits(r2)
            int r2 = r2 + r1
            int r10 = r19 + r10
            r0.position(r10)
            int r9 = r0.getInt()
            r10 = 1515870810(0x5a5a5a5a, float:1.53652219E16)
            if (r9 != r10) goto L_0x041b
            if (r12 != r1) goto L_0x0413
            r20 = 1
        L_0x0413:
            r17 = r3
            r10 = r21
            r4 = r22
            r12 = 1
            goto L_0x046f
        L_0x041b:
            r10 = 1191201283(0x47004a03, float:32842.01)
            if (r9 != r10) goto L_0x042c
            r10 = r21
            if (r10 != r1) goto L_0x0426
            r20 = 1
        L_0x0426:
            r17 = r3
            r4 = r22
            r10 = 1
            goto L_0x046f
        L_0x042c:
            r10 = r21
            r4 = 496366178(0x1d95f262, float:3.969059E-21)
            if (r9 != r4) goto L_0x043d
            r4 = r22
            if (r4 != r1) goto L_0x0439
            r20 = 1
        L_0x0439:
            r17 = r3
            r4 = 1
            goto L_0x046f
        L_0x043d:
            r17 = r3
            r4 = r22
            r3 = 1700671838(0x655e315e, float:6.557975E22)
            if (r9 != r3) goto L_0x044f
            r3 = r23
            if (r3 != r1) goto L_0x044c
            r20 = 1
        L_0x044c:
            r23 = 1
            goto L_0x046f
        L_0x044f:
            r3 = 176167201(0xa801921, float:1.2335404E-32)
            if (r9 != r3) goto L_0x045a
            if (r14 != r1) goto L_0x0458
            r20 = 1
        L_0x0458:
            r14 = 1
            goto L_0x046f
        L_0x045a:
            r3 = 1101174087(0x41a29547, float:20.32289)
            if (r9 != r3) goto L_0x0465
            if (r11 != r1) goto L_0x0463
            r20 = 1
        L_0x0463:
            r11 = 1
            goto L_0x046f
        L_0x0465:
            r3 = 45126241(0x2b09261, float:2.5944893E-37)
            if (r9 != r3) goto L_0x046f
            if (r15 != r1) goto L_0x046e
            r20 = 1
        L_0x046e:
            r15 = 1
        L_0x046f:
            if (r20 != 0) goto L_0x0476
            int r3 = r6.frameSize
            int r3 = r3 + r2
            r6.frameSize = r3
        L_0x0476:
            int r2 = r19 + r2
            r0.position(r2)
            r18 = r4
            r3 = r17
            r2 = r20
            r19 = r23
            r1 = -1
            r4 = 0
            r17 = r16
            r16 = r10
            r10 = 1
            goto L_0x004d
        L_0x048c:
            java.io.IOException r1 = new java.io.IOException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "No DTS_SYNCWORD_* found at "
            r2.<init>(r3)
            int r0 = r0.position()
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0)
            throw r1
        L_0x04a4:
            long r3 = r0.getLong()
            int r4 = (int) r3
            r3 = 1146377032(0x44545348, float:849.3013)
            if (r1 != r3) goto L_0x04b7
            r3 = 1145586770(0x44484452, float:801.0675)
            if (r2 != r3) goto L_0x04b7
            r6.parseDtshdhdr(r4, r0)
            goto L_0x04ee
        L_0x04b7:
            r3 = 1129271877(0x434f5245, float:207.32137)
            if (r1 != r3) goto L_0x04c9
            r3 = 1397968196(0x53534d44, float:9.075344E11)
            if (r2 != r3) goto L_0x04c9
            boolean r1 = r6.parseCoressmd(r4, r0)
            if (r1 != 0) goto L_0x04ee
            r13 = 0
            return r13
        L_0x04c9:
            r13 = 0
            r3 = 1096110162(0x41555052, float:13.332109)
            if (r1 != r3) goto L_0x04db
            r3 = 759710802(0x2d484452, float:1.1383854E-11)
            if (r2 != r3) goto L_0x04db
            boolean r1 = r6.parseAuprhdr(r4, r0)
            if (r1 != 0) goto L_0x04ee
            return r13
        L_0x04db:
            r3 = 1163416659(0x45585453, float:3461.2703)
            if (r1 != r3) goto L_0x04ec
            r1 = 1398754628(0x535f4d44, float:9.5907401E11)
            if (r2 != r1) goto L_0x04ec
            boolean r1 = r6.parseExtssmd(r4, r0)
            if (r1 != 0) goto L_0x04ee
            return r13
        L_0x04ec:
            if (r13 < r4) goto L_0x04f8
        L_0x04ee:
            int r1 = r0.getInt()
            int r2 = r0.getInt()
            goto L_0x001e
        L_0x04f8:
            r0.get()
            int r13 = r13 + 1
            goto L_0x04ec
        L_0x04fe:
            java.io.IOException r0 = new java.io.IOException
            java.lang.String r1 = "data does not start with 'DTSHDHDR' as required for a DTS-HD file"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.DTSTrackImpl.readVariables():boolean");
    }

    private List<Sample> generateSamples(DataSource dataSource2, int i, long j, int i2) throws IOException {
        LookAhead lookAhead = new LookAhead(dataSource2, (long) i, j, i2);
        ArrayList arrayList = new ArrayList();
        while (true) {
            final ByteBuffer findNextStart = lookAhead.findNextStart();
            if (findNextStart == null) {
                System.err.println("all samples found");
                return arrayList;
            }
            arrayList.add(new Sample() {
                public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                    writableByteChannel.write((ByteBuffer) findNextStart.rewind());
                }

                public long getSize() {
                    return (long) findNextStart.rewind().remaining();
                }

                public ByteBuffer asByteBuffer() {
                    return findNextStart;
                }
            });
        }
    }

    private int getBitRate(int i) throws IOException {
        switch (i) {
            case 0:
                return 32;
            case 1:
                return 56;
            case 2:
                return 64;
            case 3:
                return 96;
            case 4:
                return 112;
            case 5:
                return 128;
            case 6:
                return Opcodes.CHECKCAST;
            case 7:
                return 224;
            case 8:
                return 256;
            case 9:
                return DimensionsKt.XHDPI;
            case 10:
                return 384;
            case 11:
                return 448;
            case 12:
                return 512;
            case 13:
                return 576;
            case 14:
                return DimensionsKt.XXXHDPI;
            case 15:
                return 768;
            case 16:
                return 960;
            case 17:
                return 1024;
            case 18:
                return 1152;
            case 19:
                return 1280;
            case 20:
                return 1344;
            case 21:
                return 1408;
            case 22:
                return 1411;
            case 23:
                return 1472;
            case 24:
                return 1536;
            case 25:
                return -1;
            default:
                throw new IOException("Unknown bitrate value");
        }
    }

    private int getSampleRate(int i) throws IOException {
        switch (i) {
            case 1:
                return 8000;
            case 2:
                return 16000;
            case 3:
                return 32000;
            case 6:
                return 11025;
            case 7:
                return 22050;
            case 8:
                return 44100;
            case 11:
                return 12000;
            case 12:
                return 24000;
            case 13:
                return 48000;
            default:
                throw new IOException("Unknown Sample Rate");
        }
    }

    class LookAhead {
        ByteBuffer buffer;
        long bufferStartPos;
        private final int corePresent;
        long dataEnd;
        DataSource dataSource;
        int inBufferPos = 0;
        long start;

        LookAhead(DataSource dataSource2, long j, long j2, int i) throws IOException {
            this.dataSource = dataSource2;
            this.bufferStartPos = j;
            this.dataEnd = j2 + j;
            this.corePresent = i;
            fillBuffer();
        }

        public ByteBuffer findNextStart() throws IOException {
            while (true) {
                try {
                    if (this.corePresent == 1) {
                        if (nextFourEquals0x7FFE8001()) {
                            break;
                        }
                        discardByte();
                    } else if (nextFourEquals0x64582025()) {
                        break;
                    } else {
                        discardByte();
                    }
                } catch (EOFException unused) {
                    return null;
                }
            }
            discardNext4AndMarkStart();
            while (true) {
                if (this.corePresent == 1) {
                    if (nextFourEquals0x7FFE8001orEof()) {
                        break;
                    }
                    discardQWord();
                } else if (nextFourEquals0x64582025orEof()) {
                    break;
                } else {
                    discardQWord();
                }
            }
            return getSample();
        }

        private void fillBuffer() throws IOException {
            System.err.println("Fill Buffer");
            DataSource dataSource2 = this.dataSource;
            long j = this.bufferStartPos;
            this.buffer = dataSource2.map(j, Math.min(this.dataEnd - j, 67108864));
        }

        private boolean nextFourEquals0x64582025() throws IOException {
            return nextFourEquals((byte) 100, (byte) 88, (byte) 32, (byte) 37);
        }

        private boolean nextFourEquals0x7FFE8001() throws IOException {
            return nextFourEquals(ByteCompanionObject.MAX_VALUE, (byte) -2, ByteCompanionObject.MIN_VALUE, (byte) 1);
        }

        private boolean nextFourEquals(byte b, byte b2, byte b3, byte b4) throws IOException {
            int limit = this.buffer.limit();
            int i = this.inBufferPos;
            if (limit - i >= 4) {
                if (this.buffer.get(i) == b && this.buffer.get(this.inBufferPos + 1) == b2 && this.buffer.get(this.inBufferPos + 2) == b3 && this.buffer.get(this.inBufferPos + 3) == b4) {
                    return true;
                }
                return false;
            } else if (this.bufferStartPos + ((long) i) + 4 < this.dataSource.size()) {
                return false;
            } else {
                throw new EOFException();
            }
        }

        private boolean nextFourEquals0x64582025orEof() throws IOException {
            return nextFourEqualsOrEof((byte) 100, (byte) 88, (byte) 32, (byte) 37);
        }

        private boolean nextFourEquals0x7FFE8001orEof() throws IOException {
            return nextFourEqualsOrEof(ByteCompanionObject.MAX_VALUE, (byte) -2, ByteCompanionObject.MIN_VALUE, (byte) 1);
        }

        private boolean nextFourEqualsOrEof(byte b, byte b2, byte b3, byte b4) throws IOException {
            int limit = this.buffer.limit();
            int i = this.inBufferPos;
            if (limit - i >= 4) {
                if ((this.bufferStartPos + ((long) i)) % PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED == 0) {
                    PrintStream printStream = System.err;
                    StringBuilder sb = new StringBuilder();
                    sb.append(((this.bufferStartPos + ((long) this.inBufferPos)) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
                    printStream.println(sb.toString());
                }
                return this.buffer.get(this.inBufferPos) == b && this.buffer.get(this.inBufferPos + 1) == b2 && this.buffer.get(this.inBufferPos + 2) == b3 && this.buffer.get(this.inBufferPos + 3) == b4;
            }
            long j = this.bufferStartPos;
            long j2 = this.dataEnd;
            if (((long) i) + j + 4 > j2) {
                return j + ((long) i) == j2;
            }
            this.bufferStartPos = this.start;
            this.inBufferPos = 0;
            fillBuffer();
            return nextFourEquals0x7FFE8001();
        }

        private void discardByte() {
            this.inBufferPos++;
        }

        private void discardQWord() {
            this.inBufferPos += 4;
        }

        private void discardNext4AndMarkStart() {
            long j = this.bufferStartPos;
            int i = this.inBufferPos;
            this.start = j + ((long) i);
            this.inBufferPos = i + 4;
        }

        private ByteBuffer getSample() {
            long j = this.start;
            long j2 = this.bufferStartPos;
            if (j >= j2) {
                this.buffer.position((int) (j - j2));
                ByteBuffer slice = this.buffer.slice();
                slice.limit((int) (((long) this.inBufferPos) - (this.start - this.bufferStartPos)));
                return slice;
            }
            throw new RuntimeException("damn! NAL exceeds buffer");
        }
    }
}
