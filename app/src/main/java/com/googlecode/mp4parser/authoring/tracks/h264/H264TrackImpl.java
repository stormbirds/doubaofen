package com.googlecode.mp4parser.authoring.tracks.h264;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.tracks.AbstractH26XTrack;
import com.googlecode.mp4parser.authoring.tracks.h264.SliceHeader;
import com.googlecode.mp4parser.h264.model.PictureParameterSet;
import com.googlecode.mp4parser.h264.model.SeqParameterSet;
import com.googlecode.mp4parser.h264.read.CAVLCReader;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.googlecode.mp4parser.util.RangeStartMap;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import kotlin.UByte;

public class H264TrackImpl extends AbstractH26XTrack {
    /* access modifiers changed from: private */
    public static final Logger LOG = Logger.getLogger(H264TrackImpl.class.getName());
    PictureParameterSet currentPictureParameterSet;
    SeqParameterSet currentSeqParameterSet;
    private boolean determineFrameRate;
    PictureParameterSet firstPictureParameterSet;
    SeqParameterSet firstSeqParameterSet;
    int frameNrInGop;
    private int frametick;
    private int height;
    private String lang;
    int[] pictureOrderCounts;
    RangeStartMap<Integer, byte[]> pictureParameterRangeMap;
    Map<Integer, PictureParameterSet> ppsIdToPps;
    Map<Integer, byte[]> ppsIdToPpsBytes;
    int prevPicOrderCntLsb;
    int prevPicOrderCntMsb;
    SampleDescriptionBox sampleDescriptionBox;
    private List<Sample> samples;
    private SEIMessage seiMessage;
    RangeStartMap<Integer, byte[]> seqParameterRangeMap;
    Map<Integer, SeqParameterSet> spsIdToSps;
    Map<Integer, byte[]> spsIdToSpsBytes;
    private long timescale;
    private int width;

    public String getHandler() {
        return "vide";
    }

    public H264TrackImpl(DataSource dataSource, String str, long j, int i) throws IOException {
        super(dataSource);
        this.spsIdToSpsBytes = new HashMap();
        this.spsIdToSps = new HashMap();
        this.ppsIdToPpsBytes = new HashMap();
        this.ppsIdToPps = new HashMap();
        this.firstSeqParameterSet = null;
        this.firstPictureParameterSet = null;
        this.currentSeqParameterSet = null;
        this.currentPictureParameterSet = null;
        this.seqParameterRangeMap = new RangeStartMap<>();
        this.pictureParameterRangeMap = new RangeStartMap<>();
        this.frameNrInGop = 0;
        this.pictureOrderCounts = new int[0];
        this.prevPicOrderCntLsb = 0;
        this.prevPicOrderCntMsb = 0;
        this.determineFrameRate = true;
        this.lang = "eng";
        this.lang = str;
        this.timescale = j;
        this.frametick = i;
        if (j > 0 && i > 0) {
            this.determineFrameRate = false;
        }
        parse(new AbstractH26XTrack.LookAhead(dataSource));
    }

    public H264TrackImpl(DataSource dataSource, String str) throws IOException {
        this(dataSource, str, -1, -1);
    }

    public H264TrackImpl(DataSource dataSource) throws IOException {
        this(dataSource, "eng");
    }

    public static H264NalUnitHeader getNalUnitHeader(ByteBuffer byteBuffer) {
        H264NalUnitHeader h264NalUnitHeader = new H264NalUnitHeader();
        byte b = byteBuffer.get(0);
        h264NalUnitHeader.nal_ref_idc = (b >> 5) & 3;
        h264NalUnitHeader.nal_unit_type = b & 31;
        return h264NalUnitHeader;
    }

    private void parse(AbstractH26XTrack.LookAhead lookAhead) throws IOException {
        this.samples = new ArrayList();
        if (!readSamples(lookAhead)) {
            throw new IOException();
        } else if (readVariables()) {
            this.sampleDescriptionBox = new SampleDescriptionBox();
            VisualSampleEntry visualSampleEntry = new VisualSampleEntry(VisualSampleEntry.TYPE3);
            visualSampleEntry.setDataReferenceIndex(1);
            visualSampleEntry.setDepth(24);
            visualSampleEntry.setFrameCount(1);
            visualSampleEntry.setHorizresolution(72.0d);
            visualSampleEntry.setVertresolution(72.0d);
            visualSampleEntry.setWidth(this.width);
            visualSampleEntry.setHeight(this.height);
            visualSampleEntry.setCompressorname("AVC Coding");
            AvcConfigurationBox avcConfigurationBox = new AvcConfigurationBox();
            avcConfigurationBox.setSequenceParameterSets(new ArrayList(this.spsIdToSpsBytes.values()));
            avcConfigurationBox.setPictureParameterSets(new ArrayList(this.ppsIdToPpsBytes.values()));
            avcConfigurationBox.setAvcLevelIndication(this.firstSeqParameterSet.level_idc);
            avcConfigurationBox.setAvcProfileIndication(this.firstSeqParameterSet.profile_idc);
            avcConfigurationBox.setBitDepthLumaMinus8(this.firstSeqParameterSet.bit_depth_luma_minus8);
            avcConfigurationBox.setBitDepthChromaMinus8(this.firstSeqParameterSet.bit_depth_chroma_minus8);
            avcConfigurationBox.setChromaFormat(this.firstSeqParameterSet.chroma_format_idc.getId());
            avcConfigurationBox.setConfigurationVersion(1);
            avcConfigurationBox.setLengthSizeMinusOne(3);
            int i = 0;
            int i2 = (this.firstSeqParameterSet.constraint_set_0_flag ? 128 : 0) + (this.firstSeqParameterSet.constraint_set_1_flag ? 64 : 0) + (this.firstSeqParameterSet.constraint_set_2_flag ? 32 : 0) + (this.firstSeqParameterSet.constraint_set_3_flag ? 16 : 0);
            if (this.firstSeqParameterSet.constraint_set_4_flag) {
                i = 8;
            }
            avcConfigurationBox.setProfileCompatibility(i2 + i + ((int) (this.firstSeqParameterSet.reserved_zero_2bits & 3)));
            visualSampleEntry.addBox(avcConfigurationBox);
            this.sampleDescriptionBox.addBox(visualSampleEntry);
            this.trackMetaData.setCreationTime(new Date());
            this.trackMetaData.setModificationTime(new Date());
            this.trackMetaData.setLanguage(this.lang);
            this.trackMetaData.setTimescale(this.timescale);
            this.trackMetaData.setWidth((double) this.width);
            this.trackMetaData.setHeight((double) this.height);
        } else {
            throw new IOException();
        }
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    private boolean readVariables() {
        int i;
        this.width = (this.firstSeqParameterSet.pic_width_in_mbs_minus1 + 1) * 16;
        int i2 = this.firstSeqParameterSet.frame_mbs_only_flag ? 1 : 2;
        this.height = (this.firstSeqParameterSet.pic_height_in_map_units_minus1 + 1) * 16 * i2;
        if (this.firstSeqParameterSet.frame_cropping_flag) {
            int i3 = 0;
            if (!this.firstSeqParameterSet.residual_color_transform_flag) {
                i3 = this.firstSeqParameterSet.chroma_format_idc.getId();
            }
            if (i3 != 0) {
                i = this.firstSeqParameterSet.chroma_format_idc.getSubWidth();
                i2 *= this.firstSeqParameterSet.chroma_format_idc.getSubHeight();
            } else {
                i = 1;
            }
            this.width -= i * (this.firstSeqParameterSet.frame_crop_left_offset + this.firstSeqParameterSet.frame_crop_right_offset);
            this.height -= i2 * (this.firstSeqParameterSet.frame_crop_top_offset + this.firstSeqParameterSet.frame_crop_bottom_offset);
        }
        return true;
    }

    private boolean readSamples(AbstractH26XTrack.LookAhead lookAhead) throws IOException {
        ArrayList arrayList = new ArrayList();
        AnonymousClass1FirstVclNalDetector r2 = null;
        while (true) {
            ByteBuffer findNextNal = findNextNal(lookAhead);
            if (findNextNal != null) {
                H264NalUnitHeader nalUnitHeader = getNalUnitHeader(findNextNal);
                switch (nalUnitHeader.nal_unit_type) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        AnonymousClass1FirstVclNalDetector r5 = new Object(findNextNal, nalUnitHeader.nal_ref_idc, nalUnitHeader.nal_unit_type) {
                            boolean bottom_field_flag;
                            int delta_pic_order_cnt_0;
                            int delta_pic_order_cnt_1;
                            int delta_pic_order_cnt_bottom;
                            boolean field_pic_flag;
                            int frame_num;
                            boolean idrPicFlag;
                            int idr_pic_id;
                            int nal_ref_idc;
                            int pic_order_cnt_lsb;
                            int pic_order_cnt_type;
                            int pic_parameter_set_id;

                            {
                                SliceHeader sliceHeader = new SliceHeader(H264TrackImpl.cleanBuffer(new ByteBufferBackedInputStream(r6)), H264TrackImpl.this.spsIdToSps, H264TrackImpl.this.ppsIdToPps, r8 == 5);
                                this.frame_num = sliceHeader.frame_num;
                                this.pic_parameter_set_id = sliceHeader.pic_parameter_set_id;
                                this.field_pic_flag = sliceHeader.field_pic_flag;
                                this.bottom_field_flag = sliceHeader.bottom_field_flag;
                                this.nal_ref_idc = r7;
                                this.pic_order_cnt_type = H264TrackImpl.this.spsIdToSps.get(Integer.valueOf(H264TrackImpl.this.ppsIdToPps.get(Integer.valueOf(sliceHeader.pic_parameter_set_id)).seq_parameter_set_id)).pic_order_cnt_type;
                                this.delta_pic_order_cnt_bottom = sliceHeader.delta_pic_order_cnt_bottom;
                                this.pic_order_cnt_lsb = sliceHeader.pic_order_cnt_lsb;
                                this.delta_pic_order_cnt_0 = sliceHeader.delta_pic_order_cnt_0;
                                this.delta_pic_order_cnt_1 = sliceHeader.delta_pic_order_cnt_1;
                                this.idr_pic_id = sliceHeader.idr_pic_id;
                            }

                            /* access modifiers changed from: package-private */
                            public boolean isFirstInNew(AnonymousClass1FirstVclNalDetector r4) {
                                boolean z;
                                boolean z2;
                                boolean z3;
                                if (r4.frame_num != this.frame_num || r4.pic_parameter_set_id != this.pic_parameter_set_id || (z = r4.field_pic_flag) != this.field_pic_flag) {
                                    return true;
                                }
                                if ((z && r4.bottom_field_flag != this.bottom_field_flag) || r4.nal_ref_idc != this.nal_ref_idc) {
                                    return true;
                                }
                                if (r4.pic_order_cnt_type == 0 && this.pic_order_cnt_type == 0 && (r4.pic_order_cnt_lsb != this.pic_order_cnt_lsb || r4.delta_pic_order_cnt_bottom != this.delta_pic_order_cnt_bottom)) {
                                    return true;
                                }
                                if ((r4.pic_order_cnt_type == 1 && this.pic_order_cnt_type == 1 && (r4.delta_pic_order_cnt_0 != this.delta_pic_order_cnt_0 || r4.delta_pic_order_cnt_1 != this.delta_pic_order_cnt_1)) || (z2 = r4.idrPicFlag) != (z3 = this.idrPicFlag)) {
                                    return true;
                                }
                                if (!z2 || !z3 || r4.idr_pic_id == this.idr_pic_id) {
                                    return false;
                                }
                                return true;
                            }
                        };
                        if (r2 != null && r2.isFirstInNew(r5)) {
                            LOG.finer("Wrapping up cause of first vcl nal is found");
                            createSample(arrayList);
                        }
                        arrayList.add((ByteBuffer) findNextNal.rewind());
                        r2 = r5;
                        continue;
                    case 6:
                        if (r2 != null) {
                            LOG.finer("Wrapping up cause of SEI after vcl marks new sample");
                            createSample(arrayList);
                            r2 = null;
                        }
                        this.seiMessage = new SEIMessage(this, cleanBuffer(new ByteBufferBackedInputStream(findNextNal)), this.currentSeqParameterSet);
                        arrayList.add(findNextNal);
                        continue;
                    case 7:
                        if (r2 != null) {
                            LOG.finer("Wrapping up cause of SPS after vcl marks new sample");
                            createSample(arrayList);
                            r2 = null;
                        }
                        handleSPS((ByteBuffer) findNextNal.rewind());
                        continue;
                    case 8:
                        if (r2 != null) {
                            LOG.finer("Wrapping up cause of PPS after vcl marks new sample");
                            createSample(arrayList);
                            r2 = null;
                        }
                        handlePPS((ByteBuffer) findNextNal.rewind());
                        continue;
                    case 9:
                        if (r2 != null) {
                            LOG.finer("Wrapping up cause of AU after vcl marks new sample");
                            createSample(arrayList);
                            r2 = null;
                        }
                        arrayList.add(findNextNal);
                        continue;
                    case 10:
                    case 11:
                        break;
                    case 13:
                        throw new RuntimeException("Sequence parameter set extension is not yet handled. Needs TLC.");
                    default:
                        LOG.warning("Unknown NAL unit type: " + nalUnitHeader.nal_unit_type);
                        continue;
                }
            }
        }
        if (arrayList.size() > 0) {
            createSample(arrayList);
        }
        calcCtts();
        this.decodingTimes = new long[this.samples.size()];
        Arrays.fill(this.decodingTimes, (long) this.frametick);
        return true;
    }

    public void calcCtts() {
        int i = 0;
        int i2 = 0;
        int i3 = -1;
        while (i < this.pictureOrderCounts.length) {
            int i4 = 0;
            int i5 = Integer.MAX_VALUE;
            for (int max = Math.max(0, i - 128); max < Math.min(this.pictureOrderCounts.length, i + 128); max++) {
                int[] iArr = this.pictureOrderCounts;
                if (iArr[max] > i3 && iArr[max] < i5) {
                    i5 = iArr[max];
                    i4 = max;
                }
            }
            int[] iArr2 = this.pictureOrderCounts;
            int i6 = iArr2[i4];
            iArr2[i4] = i2;
            i++;
            i3 = i6;
            i2++;
        }
        for (int i7 = 0; i7 < this.pictureOrderCounts.length; i7++) {
            this.ctts.add(new CompositionTimeToSample.Entry(1, this.pictureOrderCounts[i7] - i7));
        }
        this.pictureOrderCounts = new int[0];
    }

    private void createSample(List<ByteBuffer> list) throws IOException {
        int i;
        SampleDependencyTypeBox.Entry entry = new SampleDependencyTypeBox.Entry(0);
        H264NalUnitHeader h264NalUnitHeader = null;
        boolean z = false;
        for (ByteBuffer nalUnitHeader : list) {
            H264NalUnitHeader nalUnitHeader2 = getNalUnitHeader(nalUnitHeader);
            int i2 = nalUnitHeader2.nal_unit_type;
            if (!(i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4)) {
                if (i2 == 5) {
                    z = true;
                }
            }
            h264NalUnitHeader = nalUnitHeader2;
        }
        if (h264NalUnitHeader == null) {
            LOG.warning("Sample without Slice");
            return;
        }
        if (z) {
            calcCtts();
        }
        SliceHeader sliceHeader = new SliceHeader(cleanBuffer(new ByteBufferBackedInputStream(list.get(list.size() - 1))), this.spsIdToSps, this.ppsIdToPps, z);
        if (h264NalUnitHeader.nal_ref_idc == 0) {
            entry.setSampleIsDependentOn(2);
        } else {
            entry.setSampleIsDependentOn(1);
        }
        if (sliceHeader.slice_type == SliceHeader.SliceType.I || sliceHeader.slice_type == SliceHeader.SliceType.SI) {
            entry.setSampleDependsOn(2);
        } else {
            entry.setSampleDependsOn(1);
        }
        Sample createSampleObject = createSampleObject(list);
        list.clear();
        SEIMessage sEIMessage = this.seiMessage;
        if (sEIMessage == null || sEIMessage.n_frames == 0) {
            this.frameNrInGop = 0;
        }
        if (sliceHeader.sps.pic_order_cnt_type == 0) {
            int i3 = 1 << (sliceHeader.sps.log2_max_pic_order_cnt_lsb_minus4 + 4);
            int i4 = sliceHeader.pic_order_cnt_lsb;
            int i5 = this.prevPicOrderCntLsb;
            if (i4 >= i5 || i5 - i4 < i3 / 2) {
                int i6 = this.prevPicOrderCntLsb;
                if (i4 <= i6 || i4 - i6 <= i3 / 2) {
                    i = this.prevPicOrderCntMsb;
                } else {
                    i = this.prevPicOrderCntMsb - i3;
                }
            } else {
                i = this.prevPicOrderCntMsb + i3;
            }
            this.pictureOrderCounts = Mp4Arrays.copyOfAndAppend(this.pictureOrderCounts, i + i4);
            this.prevPicOrderCntLsb = i4;
            this.prevPicOrderCntMsb = i;
        } else if (sliceHeader.sps.pic_order_cnt_type == 1) {
            throw new RuntimeException("pic_order_cnt_type == 1 needs to be implemented");
        } else if (sliceHeader.sps.pic_order_cnt_type == 2) {
            this.pictureOrderCounts = Mp4Arrays.copyOfAndAppend(this.pictureOrderCounts, this.samples.size());
        }
        this.sdtp.add(entry);
        this.frameNrInGop++;
        this.samples.add(createSampleObject);
        if (z) {
            this.stss.add(Integer.valueOf(this.samples.size()));
        }
    }

    private int calcPoc(int i, H264NalUnitHeader h264NalUnitHeader, SliceHeader sliceHeader) {
        if (sliceHeader.sps.pic_order_cnt_type == 0) {
            return calcPOC0(h264NalUnitHeader, sliceHeader);
        }
        if (sliceHeader.sps.pic_order_cnt_type == 1) {
            return calcPOC1(i, h264NalUnitHeader, sliceHeader);
        }
        return calcPOC2(i, h264NalUnitHeader, sliceHeader);
    }

    private int calcPOC2(int i, H264NalUnitHeader h264NalUnitHeader, SliceHeader sliceHeader) {
        return h264NalUnitHeader.nal_ref_idc == 0 ? (i * 2) - 1 : i * 2;
    }

    private int calcPOC1(int i, H264NalUnitHeader h264NalUnitHeader, SliceHeader sliceHeader) {
        int i2;
        if (sliceHeader.sps.num_ref_frames_in_pic_order_cnt_cycle == 0) {
            i = 0;
        }
        if (h264NalUnitHeader.nal_ref_idc == 0 && i > 0) {
            i--;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < sliceHeader.sps.num_ref_frames_in_pic_order_cnt_cycle; i4++) {
            i3 += sliceHeader.sps.offsetForRefFrame[i4];
        }
        if (i > 0) {
            int i5 = i - 1;
            int i6 = i5 / sliceHeader.sps.num_ref_frames_in_pic_order_cnt_cycle;
            int i7 = i5 % sliceHeader.sps.num_ref_frames_in_pic_order_cnt_cycle;
            i2 = i6 * i3;
            for (int i8 = 0; i8 <= i7; i8++) {
                i2 += sliceHeader.sps.offsetForRefFrame[i8];
            }
        } else {
            i2 = 0;
        }
        if (h264NalUnitHeader.nal_ref_idc == 0) {
            i2 += sliceHeader.sps.offset_for_non_ref_pic;
        }
        return i2 + sliceHeader.delta_pic_order_cnt_0;
    }

    private int calcPOC0(H264NalUnitHeader h264NalUnitHeader, SliceHeader sliceHeader) {
        int i;
        int i2 = sliceHeader.pic_order_cnt_lsb;
        int i3 = 1 << (sliceHeader.sps.log2_max_pic_order_cnt_lsb_minus4 + 4);
        int i4 = this.prevPicOrderCntLsb;
        if (i2 >= i4 || i4 - i2 < i3 / 2) {
            int i5 = this.prevPicOrderCntLsb;
            if (i2 <= i5 || i2 - i5 <= i3 / 2) {
                i = this.prevPicOrderCntMsb;
            } else {
                i = this.prevPicOrderCntMsb - i3;
            }
        } else {
            i = this.prevPicOrderCntMsb + i3;
        }
        if (h264NalUnitHeader.nal_ref_idc != 0) {
            this.prevPicOrderCntMsb = i;
            this.prevPicOrderCntLsb = i2;
        }
        return i + i2;
    }

    private void handlePPS(ByteBuffer byteBuffer) throws IOException {
        ByteBufferBackedInputStream byteBufferBackedInputStream = new ByteBufferBackedInputStream(byteBuffer);
        byteBufferBackedInputStream.read();
        PictureParameterSet read = PictureParameterSet.read((InputStream) byteBufferBackedInputStream);
        if (this.firstPictureParameterSet == null) {
            this.firstPictureParameterSet = read;
        }
        this.currentPictureParameterSet = read;
        byte[] array = toArray((ByteBuffer) byteBuffer.rewind());
        byte[] bArr = this.ppsIdToPpsBytes.get(Integer.valueOf(read.pic_parameter_set_id));
        if (bArr == null || Arrays.equals(bArr, array)) {
            if (bArr == null) {
                this.pictureParameterRangeMap.put(Integer.valueOf(this.samples.size()), array);
            }
            this.ppsIdToPpsBytes.put(Integer.valueOf(read.pic_parameter_set_id), array);
            this.ppsIdToPps.put(Integer.valueOf(read.pic_parameter_set_id), read);
            return;
        }
        throw new RuntimeException("OMG - I got two SPS with same ID but different settings! (AVC3 is the solution)");
    }

    private void handleSPS(ByteBuffer byteBuffer) throws IOException {
        InputStream cleanBuffer = cleanBuffer(new ByteBufferBackedInputStream(byteBuffer));
        cleanBuffer.read();
        SeqParameterSet read = SeqParameterSet.read(cleanBuffer);
        if (this.firstSeqParameterSet == null) {
            this.firstSeqParameterSet = read;
            configureFramerate();
        }
        this.currentSeqParameterSet = read;
        byte[] array = toArray((ByteBuffer) byteBuffer.rewind());
        byte[] bArr = this.spsIdToSpsBytes.get(Integer.valueOf(read.seq_parameter_set_id));
        if (bArr == null || Arrays.equals(bArr, array)) {
            if (bArr != null) {
                this.seqParameterRangeMap.put(Integer.valueOf(this.samples.size()), array);
            }
            this.spsIdToSpsBytes.put(Integer.valueOf(read.seq_parameter_set_id), array);
            this.spsIdToSps.put(Integer.valueOf(read.seq_parameter_set_id), read);
            return;
        }
        throw new RuntimeException("OMG - I got two SPS with same ID but different settings!");
    }

    private void configureFramerate() {
        if (!this.determineFrameRate) {
            return;
        }
        if (this.firstSeqParameterSet.vuiParams != null) {
            this.timescale = (long) (this.firstSeqParameterSet.vuiParams.time_scale >> 1);
            this.frametick = this.firstSeqParameterSet.vuiParams.num_units_in_tick;
            if (this.timescale == 0 || this.frametick == 0) {
                Logger logger = LOG;
                logger.warning("vuiParams contain invalid values: time_scale: " + this.timescale + " and frame_tick: " + this.frametick + ". Setting frame rate to 25fps");
                this.timescale = 90000;
                this.frametick = 3600;
            }
            if (this.timescale / ((long) this.frametick) > 100) {
                Logger logger2 = LOG;
                logger2.warning("Framerate is " + (this.timescale / ((long) this.frametick)) + ". That is suspicious.");
                return;
            }
            return;
        }
        LOG.warning("Can't determine frame rate. Guessing 25 fps");
        this.timescale = 90000;
        this.frametick = 3600;
    }

    public class ByteBufferBackedInputStream extends InputStream {
        private final ByteBuffer buf;

        public ByteBufferBackedInputStream(ByteBuffer byteBuffer) {
            this.buf = byteBuffer.duplicate();
        }

        public int read() throws IOException {
            if (!this.buf.hasRemaining()) {
                return -1;
            }
            return this.buf.get() & UByte.MAX_VALUE;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (!this.buf.hasRemaining()) {
                return -1;
            }
            int min = Math.min(i2, this.buf.remaining());
            this.buf.get(bArr, i, min);
            return min;
        }
    }

    public class SEIMessage {
        boolean clock_timestamp_flag;
        int cnt_dropped_flag;
        int counting_type;
        int cpb_removal_delay;
        int ct_type;
        int discontinuity_flag;
        int dpb_removal_delay;
        int full_timestamp_flag;
        int hours_value;
        int minutes_value;
        int n_frames;
        int nuit_field_based_flag;
        int payloadSize = 0;
        int payloadType = 0;
        int pic_struct;
        boolean removal_delay_flag;
        int seconds_value;
        SeqParameterSet sps;
        final /* synthetic */ H264TrackImpl this$0;
        int time_offset;
        int time_offset_length;

        public SEIMessage(H264TrackImpl h264TrackImpl, InputStream inputStream, SeqParameterSet seqParameterSet) throws IOException {
            int i;
            SeqParameterSet seqParameterSet2 = seqParameterSet;
            this.this$0 = h264TrackImpl;
            boolean z = false;
            this.sps = seqParameterSet2;
            inputStream.read();
            int available = inputStream.available();
            int i2 = 0;
            while (i2 < available) {
                this.payloadType = z ? 1 : 0;
                this.payloadSize = z;
                int read = inputStream.read();
                int i3 = i2 + 1;
                while (read == 255) {
                    InputStream inputStream2 = inputStream;
                    this.payloadType += read;
                    read = inputStream.read();
                    i3++;
                    z = false;
                }
                this.payloadType += read;
                int read2 = inputStream.read();
                i2 = i3 + 1;
                while (read2 == 255) {
                    InputStream inputStream3 = inputStream;
                    this.payloadSize += read2;
                    read2 = inputStream.read();
                    i2++;
                    z = false;
                }
                this.payloadSize += read2;
                if (available - i2 < this.payloadSize) {
                    InputStream inputStream4 = inputStream;
                    i2 = available;
                } else if (this.payloadType != 1) {
                    InputStream inputStream5 = inputStream;
                    for (int i4 = 0; i4 < this.payloadSize; i4++) {
                        inputStream.read();
                        i2++;
                    }
                } else if (seqParameterSet2.vuiParams == null || (seqParameterSet2.vuiParams.nalHRDParams == null && seqParameterSet2.vuiParams.vclHRDParams == null && !seqParameterSet2.vuiParams.pic_struct_present_flag)) {
                    InputStream inputStream6 = inputStream;
                    for (int i5 = 0; i5 < this.payloadSize; i5++) {
                        inputStream.read();
                        i2++;
                    }
                } else {
                    byte[] bArr = new byte[this.payloadSize];
                    inputStream.read(bArr);
                    i2 += this.payloadSize;
                    CAVLCReader cAVLCReader = new CAVLCReader(new ByteArrayInputStream(bArr));
                    if (seqParameterSet2.vuiParams.nalHRDParams == null && seqParameterSet2.vuiParams.vclHRDParams == null) {
                        this.removal_delay_flag = z;
                    } else {
                        this.removal_delay_flag = true;
                        this.cpb_removal_delay = cAVLCReader.readU(seqParameterSet2.vuiParams.nalHRDParams.cpb_removal_delay_length_minus1 + 1, "SEI: cpb_removal_delay");
                        this.dpb_removal_delay = cAVLCReader.readU(seqParameterSet2.vuiParams.nalHRDParams.dpb_output_delay_length_minus1 + 1, "SEI: dpb_removal_delay");
                    }
                    if (seqParameterSet2.vuiParams.pic_struct_present_flag) {
                        this.pic_struct = cAVLCReader.readU(4, "SEI: pic_struct");
                        switch (this.pic_struct) {
                            case 3:
                            case 4:
                            case 7:
                                i = 2;
                                break;
                            case 5:
                            case 6:
                            case 8:
                                i = 3;
                                break;
                            default:
                                i = 1;
                                break;
                        }
                        for (int i6 = 0; i6 < i; i6++) {
                            this.clock_timestamp_flag = cAVLCReader.readBool("pic_timing SEI: clock_timestamp_flag[" + i6 + "]");
                            if (this.clock_timestamp_flag) {
                                this.ct_type = cAVLCReader.readU(2, "pic_timing SEI: ct_type");
                                this.nuit_field_based_flag = cAVLCReader.readU(1, "pic_timing SEI: nuit_field_based_flag");
                                this.counting_type = cAVLCReader.readU(5, "pic_timing SEI: counting_type");
                                this.full_timestamp_flag = cAVLCReader.readU(1, "pic_timing SEI: full_timestamp_flag");
                                this.discontinuity_flag = cAVLCReader.readU(1, "pic_timing SEI: discontinuity_flag");
                                this.cnt_dropped_flag = cAVLCReader.readU(1, "pic_timing SEI: cnt_dropped_flag");
                                this.n_frames = cAVLCReader.readU(8, "pic_timing SEI: n_frames");
                                if (this.full_timestamp_flag == 1) {
                                    this.seconds_value = cAVLCReader.readU(6, "pic_timing SEI: seconds_value");
                                    this.minutes_value = cAVLCReader.readU(6, "pic_timing SEI: minutes_value");
                                    this.hours_value = cAVLCReader.readU(5, "pic_timing SEI: hours_value");
                                } else if (cAVLCReader.readBool("pic_timing SEI: seconds_flag")) {
                                    this.seconds_value = cAVLCReader.readU(6, "pic_timing SEI: seconds_value");
                                    if (cAVLCReader.readBool("pic_timing SEI: minutes_flag")) {
                                        this.minutes_value = cAVLCReader.readU(6, "pic_timing SEI: minutes_value");
                                        if (cAVLCReader.readBool("pic_timing SEI: hours_flag")) {
                                            this.hours_value = cAVLCReader.readU(5, "pic_timing SEI: hours_value");
                                        }
                                    }
                                }
                                if (seqParameterSet2.vuiParams.nalHRDParams != null) {
                                    this.time_offset_length = seqParameterSet2.vuiParams.nalHRDParams.time_offset_length;
                                } else if (seqParameterSet2.vuiParams.vclHRDParams != null) {
                                    this.time_offset_length = seqParameterSet2.vuiParams.vclHRDParams.time_offset_length;
                                } else {
                                    this.time_offset_length = 24;
                                }
                                this.time_offset = cAVLCReader.readU(24, "pic_timing SEI: time_offset");
                            }
                        }
                    }
                }
                H264TrackImpl.LOG.fine(toString());
                z = false;
            }
        }

        public String toString() {
            String str = "SEIMessage{payloadType=" + this.payloadType + ", payloadSize=" + this.payloadSize;
            if (this.payloadType == 1) {
                if (!(this.sps.vuiParams.nalHRDParams == null && this.sps.vuiParams.vclHRDParams == null)) {
                    str = String.valueOf(str) + ", cpb_removal_delay=" + this.cpb_removal_delay + ", dpb_removal_delay=" + this.dpb_removal_delay;
                }
                if (this.sps.vuiParams.pic_struct_present_flag) {
                    str = String.valueOf(str) + ", pic_struct=" + this.pic_struct;
                    if (this.clock_timestamp_flag) {
                        str = String.valueOf(str) + ", ct_type=" + this.ct_type + ", nuit_field_based_flag=" + this.nuit_field_based_flag + ", counting_type=" + this.counting_type + ", full_timestamp_flag=" + this.full_timestamp_flag + ", discontinuity_flag=" + this.discontinuity_flag + ", cnt_dropped_flag=" + this.cnt_dropped_flag + ", n_frames=" + this.n_frames + ", seconds_value=" + this.seconds_value + ", minutes_value=" + this.minutes_value + ", hours_value=" + this.hours_value + ", time_offset_length=" + this.time_offset_length + ", time_offset=" + this.time_offset;
                    }
                }
            }
            return String.valueOf(str) + '}';
        }
    }
}
