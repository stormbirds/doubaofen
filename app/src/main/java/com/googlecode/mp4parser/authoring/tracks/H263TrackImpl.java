package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.MultiFileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.util.Path;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class H263TrackImpl extends AbstractH26XTrack {
    private static Logger LOG = Logger.getLogger(ESDescriptor.class.getName());
    int BINARY = 1;
    int BINARY_ONLY;
    int GRAYSCALE;
    int RECTANGULAR = 0;
    boolean esdsComplete;
    List<ByteBuffer> esdsStuff;
    int fixed_vop_time_increment;
    List<Sample> samples;
    SampleDescriptionBox stsd;
    int vop_time_increment_resolution;

    public String getHandler() {
        return "vide";
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public H263TrackImpl(com.googlecode.mp4parser.DataSource r23) throws java.io.IOException {
        /*
            r22 = this;
            r0 = r22
            r1 = r23
            r2 = 0
            r0.<init>(r1, r2)
            r0.RECTANGULAR = r2
            r3 = 1
            r0.BINARY = r3
            r4 = 2
            r0.BINARY_ONLY = r4
            r5 = 3
            r0.GRAYSCALE = r5
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r0.samples = r5
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r0.esdsStuff = r5
            r0.esdsComplete = r2
            r5 = -1
            r0.fixed_vop_time_increment = r5
            r0.vop_time_increment_resolution = r2
            com.googlecode.mp4parser.authoring.tracks.AbstractH26XTrack$LookAhead r5 = new com.googlecode.mp4parser.authoring.tracks.AbstractH26XTrack$LookAhead
            r5.<init>(r1)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            com.coremedia.iso.boxes.sampleentry.VisualSampleEntry r6 = new com.coremedia.iso.boxes.sampleentry.VisualSampleEntry
            java.lang.String r7 = "mp4v"
            r6.<init>(r7)
            com.coremedia.iso.boxes.SampleDescriptionBox r7 = new com.coremedia.iso.boxes.SampleDescriptionBox
            r7.<init>()
            r0.stsd = r7
            com.coremedia.iso.boxes.SampleDescriptionBox r7 = r0.stsd
            r7.addBox(r6)
            r9 = 0
            r10 = r9
            r9 = 0
            r12 = -1
        L_0x004b:
            java.nio.ByteBuffer r14 = r0.findNextNal(r5)
            r15 = 32
            if (r14 != 0) goto L_0x00bf
            long[] r1 = r0.decodingTimes
            long[] r5 = new long[r3]
            long[] r7 = r0.decodingTimes
            long[] r8 = r0.decodingTimes
            int r8 = r8.length
            int r8 = r8 - r3
            r8 = r7[r8]
            r5[r2] = r8
            long[] r1 = com.googlecode.mp4parser.util.Mp4Arrays.copyOfAndAppend((long[]) r1, (long[]) r5)
            r0.decodingTimes = r1
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor r1 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor
            r1.<init>()
            r1.setEsId(r3)
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor r2 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor
            r2.<init>()
            r2.setObjectTypeIndication(r15)
            r3 = 4
            r2.setStreamType(r3)
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderSpecificInfo r3 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderSpecificInfo
            r3.<init>()
            java.util.List<java.nio.ByteBuffer> r5 = r0.esdsStuff
            com.googlecode.mp4parser.authoring.Sample r5 = r0.createSampleObject(r5)
            long r7 = r5.getSize()
            int r7 = com.googlecode.mp4parser.util.CastUtils.l2i(r7)
            byte[] r7 = new byte[r7]
            java.nio.ByteBuffer r5 = r5.asByteBuffer()
            r5.get(r7)
            r3.setData(r7)
            r2.setDecoderSpecificInfo(r3)
            r1.setDecoderConfigDescriptor(r2)
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor r2 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor
            r2.<init>()
            r2.setPredefined(r4)
            r1.setSlConfigDescriptor(r2)
            com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox r2 = new com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox
            r2.<init>()
            r2.setEsDescriptor(r1)
            r6.addBox(r2)
            com.googlecode.mp4parser.authoring.TrackMetaData r1 = r0.trackMetaData
            int r2 = r0.vop_time_increment_resolution
            long r2 = (long) r2
            r1.setTimescale(r2)
            return
        L_0x00bf:
            java.nio.ByteBuffer r2 = r14.duplicate()
            int r7 = com.coremedia.iso.IsoTypeReader.readUInt8(r14)
            r8 = 176(0xb0, float:2.47E-43)
            r4 = 181(0xb5, float:2.54E-43)
            if (r7 == r8) goto L_0x01b6
            if (r7 == r4) goto L_0x01b6
            if (r7 == 0) goto L_0x01b6
            if (r7 == r15) goto L_0x01b6
            r8 = 178(0xb2, float:2.5E-43)
            if (r7 != r8) goto L_0x00d9
            goto L_0x01b6
        L_0x00d9:
            r4 = 179(0xb3, float:2.51E-43)
            if (r7 != r4) goto L_0x0112
            r0.esdsComplete = r3
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer r4 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer
            r4.<init>(r14)
            r7 = 18
            int r4 = r4.readBits(r7)
            r7 = r4 & 63
            int r8 = r4 >>> 7
            r8 = r8 & 63
            int r8 = r8 * 60
            int r7 = r7 + r8
            int r4 = r4 >>> 13
            r4 = r4 & 31
            int r4 = r4 * 60
            int r4 = r4 * 60
            int r7 = r7 + r4
            long r10 = (long) r7
            java.util.List r4 = r0.stss
            java.util.List<com.googlecode.mp4parser.authoring.Sample> r7 = r0.samples
            int r7 = r7.size()
            int r7 = r7 + r3
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r4.add(r7)
            r1.add(r2)
            goto L_0x01d9
        L_0x0112:
            r4 = 182(0xb6, float:2.55E-43)
            if (r7 != r4) goto L_0x01ae
            com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer r4 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer
            r4.<init>(r14)
            r8 = 2
            r4.readBits(r8)
        L_0x011f:
            boolean r7 = r4.readBool()
            if (r7 != 0) goto L_0x01a2
            r4.readBool()
            r7 = 0
        L_0x0129:
            int r14 = r0.vop_time_increment_resolution
            int r15 = r3 << r7
            if (r14 >= r15) goto L_0x0198
            int r4 = r4.readBits(r7)
            int r7 = r0.vop_time_increment_resolution
            long r14 = (long) r7
            long r14 = r14 * r10
            int r7 = r4 % r7
            r19 = r9
            long r8 = (long) r7
            long r7 = r14 + r8
            r17 = -1
            int r9 = (r12 > r17 ? 1 : (r12 == r17 ? 0 : -1))
            if (r9 == 0) goto L_0x0156
            long[] r9 = r0.decodingTimes
            long[] r14 = new long[r3]
            long r20 = r7 - r12
            r16 = 0
            r14[r16] = r20
            long[] r9 = com.googlecode.mp4parser.util.Mp4Arrays.copyOfAndAppend((long[]) r9, (long[]) r14)
            r0.decodingTimes = r9
            goto L_0x0158
        L_0x0156:
            r16 = 0
        L_0x0158:
            java.io.PrintStream r9 = java.lang.System.err
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            java.lang.String r15 = "Frame increment: "
            r14.<init>(r15)
            long r12 = r7 - r12
            r14.append(r12)
            java.lang.String r12 = " vop time increment: "
            r14.append(r12)
            r14.append(r4)
            java.lang.String r4 = " last_sync_point: "
            r14.append(r4)
            r14.append(r10)
            java.lang.String r4 = " time_code: "
            r14.append(r4)
            r14.append(r7)
            java.lang.String r4 = r14.toString()
            r9.println(r4)
            r1.add(r2)
            java.util.List<com.googlecode.mp4parser.authoring.Sample> r2 = r0.samples
            com.googlecode.mp4parser.authoring.Sample r4 = r0.createSampleObject(r1)
            r2.add(r4)
            r1.clear()
            r12 = r7
            r9 = r19
            goto L_0x01d9
        L_0x0198:
            r19 = r9
            r16 = 0
            r17 = -1
            int r7 = r7 + 1
            r8 = 2
            goto L_0x0129
        L_0x01a2:
            r19 = r9
            r16 = 0
            r17 = -1
            r7 = 1
            long r10 = r10 + r7
            r8 = 2
            goto L_0x011f
        L_0x01ae:
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            java.lang.String r2 = "Got start code I don't know. Ask Sebastian via mp4parser mailing list what to do"
            r1.<init>(r2)
            throw r1
        L_0x01b6:
            r19 = r9
            r16 = 0
            r17 = -1
            boolean r8 = r0.esdsComplete
            if (r8 != 0) goto L_0x01d6
            java.util.List<java.nio.ByteBuffer> r8 = r0.esdsStuff
            r8.add(r2)
            if (r7 != r15) goto L_0x01cd
            r2 = r19
            r0.parse0x20Unit(r14, r2, r6)
            goto L_0x01d8
        L_0x01cd:
            r2 = r19
            if (r7 != r4) goto L_0x01d8
            int r9 = r0.parse0x05Unit(r14)
            goto L_0x01d9
        L_0x01d6:
            r2 = r19
        L_0x01d8:
            r9 = r2
        L_0x01d9:
            r2 = 0
            r4 = 2
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.H263TrackImpl.<init>(com.googlecode.mp4parser.DataSource):void");
    }

    private int parse0x05Unit(ByteBuffer byteBuffer) {
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(byteBuffer);
        if (!bitReaderBuffer.readBool()) {
            return 0;
        }
        int readBits = bitReaderBuffer.readBits(4);
        bitReaderBuffer.readBits(3);
        return readBits;
    }

    private void parse0x20Unit(ByteBuffer byteBuffer, int i, VisualSampleEntry visualSampleEntry) {
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(byteBuffer);
        bitReaderBuffer.readBool();
        bitReaderBuffer.readBits(8);
        if (bitReaderBuffer.readBool()) {
            i = bitReaderBuffer.readBits(4);
            bitReaderBuffer.readBits(3);
        }
        if (bitReaderBuffer.readBits(4) == 15) {
            bitReaderBuffer.readBits(8);
            bitReaderBuffer.readBits(8);
        }
        if (bitReaderBuffer.readBool()) {
            bitReaderBuffer.readBits(2);
            bitReaderBuffer.readBool();
            if (bitReaderBuffer.readBool()) {
                throw new RuntimeException("Implemented when needed");
            }
        }
        int readBits = bitReaderBuffer.readBits(2);
        if (readBits == this.GRAYSCALE && i != 1) {
            bitReaderBuffer.readBits(4);
        }
        bitReaderBuffer.readBool();
        this.vop_time_increment_resolution = bitReaderBuffer.readBits(16);
        bitReaderBuffer.readBool();
        if (bitReaderBuffer.readBool()) {
            LOG.info("Fixed Frame Rate");
            int i2 = 0;
            while (this.vop_time_increment_resolution >= (1 << i2)) {
                i2++;
            }
            this.fixed_vop_time_increment = bitReaderBuffer.readBits(i2);
        }
        if (readBits == this.BINARY_ONLY) {
            throw new RuntimeException("Please implmenet me");
        } else if (readBits == this.RECTANGULAR) {
            bitReaderBuffer.readBool();
            visualSampleEntry.setWidth(bitReaderBuffer.readBits(13));
            bitReaderBuffer.readBool();
            visualSampleEntry.setHeight(bitReaderBuffer.readBits(13));
            bitReaderBuffer.readBool();
        }
    }

    /* access modifiers changed from: protected */
    public Sample createSampleObject(List<? extends ByteBuffer> list) {
        byte[] bArr = new byte[3];
        bArr[2] = 1;
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        ByteBuffer[] byteBufferArr = new ByteBuffer[(list.size() * 2)];
        for (int i = 0; i < list.size(); i++) {
            int i2 = i * 2;
            byteBufferArr[i2] = wrap;
            byteBufferArr[i2 + 1] = (ByteBuffer) list.get(i);
        }
        return new SampleImpl(byteBufferArr);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public static void main1(String[] strArr) throws IOException {
        File[] listFiles = new File("C:\\dev\\mp4parser\\frames").listFiles();
        Arrays.sort(listFiles);
        Movie movie = new Movie();
        movie.addTrack(new H263TrackImpl(new MultiFileDataSourceImpl(listFiles)));
        new DefaultMp4Builder().build(movie).writeContainer(Channels.newChannel(new FileOutputStream("output.mp4")));
    }

    public static void main(String[] strArr) throws IOException {
        FileDataSourceImpl fileDataSourceImpl = new FileDataSourceImpl("C:\\content\\bbb.h263");
        Movie movie = new Movie();
        movie.addTrack(new H263TrackImpl(fileDataSourceImpl));
        new DefaultMp4Builder().build(movie).writeContainer(Channels.newChannel(new FileOutputStream("output.mp4")));
    }

    public static void main2(String[] strArr) throws IOException {
        ESDescriptorBox eSDescriptorBox = (ESDescriptorBox) Path.getPath((Container) new IsoFile("C:\\content\\bbb.mp4"), "/moov[0]/trak[0]/mdia[0]/minf[0]/stbl[0]/stsd[0]/mp4v[0]/esds[0]");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        eSDescriptorBox.getBox(Channels.newChannel(byteArrayOutputStream));
        System.err.println(Hex.encodeHex(byteArrayOutputStream.toByteArray()));
        System.err.println(eSDescriptorBox.getEsDescriptor());
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        eSDescriptorBox.getBox(Channels.newChannel(byteArrayOutputStream2));
        System.err.println(Hex.encodeHex(byteArrayOutputStream2.toByteArray()));
    }
}
