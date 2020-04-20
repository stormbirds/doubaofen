package com.googlecode.mp4parser.authoring.tracks.h265;

import com.coremedia.iso.IsoTypeReader;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.h264.read.CAVLCReader;
import com.mp4parser.iso14496.part15.HevcDecoderConfigurationRecord;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class H265TrackImplOld {
    public static final int AUD_NUT = 35;
    private static final int BLA_N_LP = 18;
    private static final int BLA_W_LP = 16;
    private static final int BLA_W_RADL = 17;
    private static final long BUFFER = 1048576;
    private static final int CRA_NUT = 21;
    private static final int IDR_N_LP = 20;
    private static final int IDR_W_RADL = 19;
    public static final int PPS_NUT = 34;
    public static final int PREFIX_SEI_NUT = 39;
    private static final int RADL_N = 6;
    private static final int RADL_R = 7;
    private static final int RASL_N = 8;
    private static final int RASL_R = 9;
    public static final int RSV_NVCL41 = 41;
    public static final int RSV_NVCL42 = 42;
    public static final int RSV_NVCL43 = 43;
    public static final int RSV_NVCL44 = 44;
    public static final int SPS_NUT = 33;
    private static final int STSA_N = 4;
    private static final int STSA_R = 5;
    private static final int TRAIL_N = 0;
    private static final int TRAIL_R = 1;
    private static final int TSA_N = 2;
    private static final int TSA_R = 3;
    public static final int UNSPEC48 = 48;
    public static final int UNSPEC49 = 49;
    public static final int UNSPEC50 = 50;
    public static final int UNSPEC51 = 51;
    public static final int UNSPEC52 = 52;
    public static final int UNSPEC53 = 53;
    public static final int UNSPEC54 = 54;
    public static final int UNSPEC55 = 55;
    public static final int VPS_NUT = 32;
    LinkedHashMap<Long, ByteBuffer> pictureParamterSets = new LinkedHashMap<>();
    List<Sample> samples = new ArrayList();
    LinkedHashMap<Long, ByteBuffer> sequenceParamterSets = new LinkedHashMap<>();
    List<Long> syncSamples = new ArrayList();
    LinkedHashMap<Long, ByteBuffer> videoParamterSets = new LinkedHashMap<>();

    public static class NalUnitHeader {
        int forbiddenZeroFlag;
        int nalUnitType;
        int nuhLayerId;
        int nuhTemporalIdPlusOne;
    }

    public enum PARSE_STATE {
        AUD_SEI_SLICE,
        SEI_SLICE,
        SLICE_OES_EOB
    }

    public H265TrackImplOld(DataSource dataSource) throws IOException {
        LookAhead lookAhead = new LookAhead(dataSource);
        ArrayList<ByteBuffer> arrayList = new ArrayList<>();
        long j = 1;
        long j2 = 1;
        int i = 0;
        while (true) {
            ByteBuffer findNextNal = findNextNal(lookAhead);
            if (findNextNal == null) {
                System.err.println("");
                HevcDecoderConfigurationRecord hevcDecoderConfigurationRecord = new HevcDecoderConfigurationRecord();
                hevcDecoderConfigurationRecord.setArrays(getArrays());
                hevcDecoderConfigurationRecord.setAvgFrameRate(0);
                return;
            }
            NalUnitHeader nalUnitHeader = getNalUnitHeader(findNextNal);
            switch (nalUnitHeader.nalUnitType) {
                case 32:
                    this.videoParamterSets.put(Long.valueOf(j2), findNextNal);
                    break;
                case 33:
                    this.sequenceParamterSets.put(Long.valueOf(j2), findNextNal);
                    break;
                case 34:
                    this.pictureParamterSets.put(Long.valueOf(j2), findNextNal);
                    break;
            }
            i = nalUnitHeader.nalUnitType < 32 ? nalUnitHeader.nalUnitType : i;
            if (isFirstOfAU(nalUnitHeader.nalUnitType, findNextNal, arrayList) && !arrayList.isEmpty()) {
                System.err.println("##########################");
                for (ByteBuffer byteBuffer : arrayList) {
                    NalUnitHeader nalUnitHeader2 = getNalUnitHeader(byteBuffer);
                    System.err.println(String.format("type: %3d - layer: %3d - tempId: %3d - size: %3d", new Object[]{Integer.valueOf(nalUnitHeader2.nalUnitType), Integer.valueOf(nalUnitHeader2.nuhLayerId), Integer.valueOf(nalUnitHeader2.nuhTemporalIdPlusOne), Integer.valueOf(byteBuffer.limit())}));
                    j = 1;
                }
                System.err.println("                          ##########################");
                this.samples.add(createSample(arrayList));
                arrayList.clear();
                j2 += j;
            }
            arrayList.add(findNextNal);
            if (i >= 16 && i <= 21) {
                this.syncSamples.add(Long.valueOf(j2));
            }
            j = 1;
        }
    }

    public static void main(String[] strArr) throws IOException {
        new H265TrackImplOld(new FileDataSourceImpl("c:\\content\\test-UHD-HEVC_01_FMV_Med_track1.hvc"));
    }

    private ByteBuffer findNextNal(LookAhead lookAhead) throws IOException {
        while (!lookAhead.nextThreeEquals001()) {
            try {
                lookAhead.discardByte();
            } catch (EOFException unused) {
                return null;
            }
        }
        lookAhead.discardNext3AndMarkStart();
        while (!lookAhead.nextThreeEquals000or001orEof()) {
            lookAhead.discardByte();
        }
        return lookAhead.getNal();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v1, resolved type: boolean[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void profile_tier_level(int r20, com.googlecode.mp4parser.h264.read.CAVLCReader r21) throws java.io.IOException {
        /*
            r19 = this;
            r0 = r20
            r1 = r21
            r2 = 2
            java.lang.String r3 = "general_profile_space "
            r1.readU(r2, r3)
            java.lang.String r3 = "general_tier_flag"
            r1.readBool(r3)
            r3 = 5
            java.lang.String r4 = "general_profile_idc"
            r1.readU(r3, r4)
            r4 = 32
            boolean[] r5 = new boolean[r4]
            r7 = 0
        L_0x001a:
            java.lang.String r8 = "]"
            if (r7 < r4) goto L_0x01af
            java.lang.String r5 = "general_progressive_source_flag"
            r1.readBool(r5)
            java.lang.String r5 = "general_interlaced_source_flag"
            r1.readBool(r5)
            java.lang.String r5 = "general_non_packed_constraint_flag"
            r1.readBool(r5)
            java.lang.String r5 = "general_frame_only_constraint_flag"
            r1.readBool(r5)
            r9 = 44
            java.lang.String r5 = "general_reserved_zero_44bits"
            r1.readU(r9, r5)
            r10 = 8
            java.lang.String r5 = "general_level_idc"
            r1.readU(r10, r5)
            boolean[] r11 = new boolean[r0]
            boolean[] r12 = new boolean[r0]
            r5 = 0
        L_0x0045:
            if (r5 < r0) goto L_0x0171
            if (r0 <= 0) goto L_0x0055
            r5 = r0
        L_0x004a:
            if (r5 < r10) goto L_0x004d
            goto L_0x0055
        L_0x004d:
            java.lang.String r7 = "reserved_zero_2bits"
            r1.readU(r2, r7)
            int r5 = r5 + 1
            goto L_0x004a
        L_0x0055:
            int[] r7 = new int[r0]
            boolean[] r13 = new boolean[r0]
            int[] r14 = new int[r0]
            int[] r5 = new int[]{r0, r4}
            java.lang.Class<boolean> r15 = boolean.class
            java.lang.Object r5 = java.lang.reflect.Array.newInstance(r15, r5)
            r15 = r5
            boolean[][] r15 = (boolean[][]) r15
            boolean[] r5 = new boolean[r0]
            boolean[] r6 = new boolean[r0]
            boolean[] r10 = new boolean[r0]
            boolean[] r9 = new boolean[r0]
            int[] r4 = new int[r0]
            r3 = 0
        L_0x0073:
            if (r3 < r0) goto L_0x0076
            return
        L_0x0076:
            boolean r17 = r11[r3]
            if (r17 == 0) goto L_0x0155
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r0 = "sub_layer_profile_space["
            r2.<init>(r0)
            r2.append(r3)
            r2.append(r8)
            java.lang.String r0 = r2.toString()
            r2 = 2
            int r0 = r1.readU(r2, r0)
            r7[r3] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_tier_flag["
            r0.<init>(r2)
            r0.append(r3)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r13[r3] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_profile_idc["
            r0.<init>(r2)
            r0.append(r3)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            r2 = 5
            int r0 = r1.readU(r2, r0)
            r14[r3] = r0
            r0 = 0
        L_0x00c2:
            r2 = 32
            if (r0 < r2) goto L_0x012a
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_progressive_source_flag["
            r0.<init>(r2)
            r0.append(r3)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r5[r3] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_interlaced_source_flag["
            r0.<init>(r2)
            r0.append(r3)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r6[r3] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_non_packed_constraint_flag["
            r0.<init>(r2)
            r0.append(r3)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r10[r3] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_frame_only_constraint_flag["
            r0.<init>(r2)
            r0.append(r3)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r9[r3] = r0
            java.lang.String r0 = "reserved"
            r2 = 44
            r1.readNBit(r2, r0)
            goto L_0x0155
        L_0x012a:
            r2 = 44
            r16 = r15[r3]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r18 = r5
            java.lang.String r5 = "sub_layer_profile_compatibility_flag["
            r2.<init>(r5)
            r2.append(r3)
            java.lang.String r5 = "]["
            r2.append(r5)
            r2.append(r0)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            boolean r2 = r1.readBool(r2)
            r16[r0] = r2
            int r0 = r0 + 1
            r5 = r18
            goto L_0x00c2
        L_0x0155:
            r18 = r5
            boolean r0 = r12[r3]
            if (r0 == 0) goto L_0x0166
            java.lang.String r0 = "sub_layer_level_idc"
            r2 = 8
            int r0 = r1.readU(r2, r0)
            r4[r3] = r0
            goto L_0x0168
        L_0x0166:
            r2 = 8
        L_0x0168:
            int r3 = r3 + 1
            r0 = r20
            r5 = r18
            r2 = 2
            goto L_0x0073
        L_0x0171:
            r2 = 8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r3 = "sub_layer_profile_present_flag["
            r0.<init>(r3)
            r0.append(r5)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r11[r5] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r3 = "sub_layer_level_present_flag["
            r0.<init>(r3)
            r0.append(r5)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r12[r5] = r0
            int r5 = r5 + 1
            r0 = r20
            r2 = 2
            r3 = 5
            r4 = 32
            r9 = 44
            r10 = 8
            goto L_0x0045
        L_0x01af:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "general_profile_compatibility_flag["
            r0.<init>(r2)
            r0.append(r7)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r5[r7] = r0
            int r7 = r7 + 1
            r0 = r20
            r2 = 2
            r3 = 5
            r4 = 32
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.H265TrackImplOld.profile_tier_level(int, com.googlecode.mp4parser.h264.read.CAVLCReader):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v0, resolved type: boolean[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getFrameRate(java.nio.ByteBuffer r14) throws java.io.IOException {
        /*
            r13 = this;
            com.googlecode.mp4parser.h264.read.CAVLCReader r0 = new com.googlecode.mp4parser.h264.read.CAVLCReader
            com.googlecode.mp4parser.util.ByteBufferByteChannel r1 = new com.googlecode.mp4parser.util.ByteBufferByteChannel
            r2 = 0
            java.nio.Buffer r14 = r14.position(r2)
            java.nio.ByteBuffer r14 = (java.nio.ByteBuffer) r14
            r1.<init>(r14)
            java.io.InputStream r14 = java.nio.channels.Channels.newInputStream(r1)
            r0.<init>(r14)
            r14 = 4
            java.lang.String r1 = "vps_parameter_set_id"
            r0.readU(r14, r1)
            r14 = 2
            java.lang.String r1 = "vps_reserved_three_2bits"
            r0.readU(r14, r1)
            r14 = 6
            java.lang.String r1 = "vps_max_layers_minus1"
            r0.readU(r14, r1)
            r1 = 3
            java.lang.String r3 = "vps_max_sub_layers_minus1"
            int r1 = r0.readU(r1, r3)
            java.lang.String r3 = "vps_temporal_id_nesting_flag"
            r0.readBool(r3)
            r3 = 16
            java.lang.String r4 = "vps_reserved_0xffff_16bits"
            r0.readU(r3, r4)
            r13.profile_tier_level(r1, r0)
            java.lang.String r3 = "vps_sub_layer_ordering_info_present_flag"
            boolean r3 = r0.readBool(r3)
            if (r3 == 0) goto L_0x0047
            r4 = 0
            goto L_0x0048
        L_0x0047:
            r4 = r1
        L_0x0048:
            int[] r4 = new int[r4]
            if (r3 == 0) goto L_0x004e
            r5 = 0
            goto L_0x004f
        L_0x004e:
            r5 = r1
        L_0x004f:
            int[] r5 = new int[r5]
            if (r3 == 0) goto L_0x0055
            r6 = 0
            goto L_0x0056
        L_0x0055:
            r6 = r1
        L_0x0056:
            int[] r6 = new int[r6]
            if (r3 == 0) goto L_0x005c
            r3 = 0
            goto L_0x005d
        L_0x005c:
            r3 = r1
        L_0x005d:
            java.lang.String r7 = "]"
            if (r3 <= r1) goto L_0x012c
            java.lang.String r3 = "vps_max_layer_id"
            int r8 = r0.readU(r14, r3)
            java.lang.String r14 = "vps_num_layer_sets_minus1"
            int r9 = r0.readUE(r14)
            int[] r14 = new int[]{r9, r8}
            java.lang.Class<boolean> r3 = boolean.class
            java.lang.Object r14 = java.lang.reflect.Array.newInstance(r3, r14)
            r10 = r14
            boolean[][] r10 = (boolean[][]) r10
            r11 = 1
            r12 = 1
        L_0x007c:
            if (r12 <= r9) goto L_0x0101
            java.lang.String r14 = "vps_timing_info_present_flag"
            boolean r14 = r0.readBool(r14)
            if (r14 == 0) goto L_0x00e8
            r14 = 32
            java.lang.String r3 = "vps_num_units_in_tick"
            r0.readU(r14, r3)
            java.lang.String r3 = "vps_time_scale"
            r0.readU(r14, r3)
            java.lang.String r14 = "vps_poc_proportional_to_timing_flag"
            boolean r14 = r0.readBool(r14)
            if (r14 == 0) goto L_0x009f
            java.lang.String r14 = "vps_num_ticks_poc_diff_one_minus1"
            r0.readUE(r14)
        L_0x009f:
            java.lang.String r14 = "vps_num_hrd_parameters"
            int r14 = r0.readUE(r14)
            int[] r3 = new int[r14]
            boolean[] r4 = new boolean[r14]
            r5 = 0
        L_0x00aa:
            if (r5 < r14) goto L_0x00ad
            goto L_0x00e8
        L_0x00ad:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r8 = "hrd_layer_set_idx["
            r6.<init>(r8)
            r6.append(r5)
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            int r6 = r0.readUE(r6)
            r3[r5] = r6
            if (r5 <= 0) goto L_0x00de
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r8 = "cprms_present_flag["
            r6.<init>(r8)
            r6.append(r5)
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            boolean r6 = r0.readBool(r6)
            r4[r5] = r6
            goto L_0x00e0
        L_0x00de:
            r4[r2] = r11
        L_0x00e0:
            boolean r6 = r4[r5]
            r13.hrd_parameters(r6, r1, r0)
            int r5 = r5 + 1
            goto L_0x00aa
        L_0x00e8:
            java.lang.String r14 = "vps_extension_flag"
            boolean r14 = r0.readBool(r14)
            if (r14 == 0) goto L_0x00fd
        L_0x00f0:
            boolean r14 = r0.moreRBSPData()
            if (r14 != 0) goto L_0x00f7
            goto L_0x00fd
        L_0x00f7:
            java.lang.String r14 = "vps_extension_data_flag"
            r0.readBool(r14)
            goto L_0x00f0
        L_0x00fd:
            r0.readTrailingBits()
            return r2
        L_0x0101:
            r14 = 0
        L_0x0102:
            if (r14 <= r8) goto L_0x0108
            int r12 = r12 + 1
            goto L_0x007c
        L_0x0108:
            r3 = r10[r12]
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "layer_id_included_flag["
            r4.<init>(r5)
            r4.append(r12)
            java.lang.String r5 = "]["
            r4.append(r5)
            r4.append(r14)
            r4.append(r7)
            java.lang.String r4 = r4.toString()
            boolean r4 = r0.readBool(r4)
            r3[r14] = r4
            int r14 = r14 + 1
            goto L_0x0102
        L_0x012c:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "vps_max_dec_pic_buffering_minus1["
            r8.<init>(r9)
            r8.append(r3)
            r8.append(r7)
            java.lang.String r8 = r8.toString()
            int r8 = r0.readUE(r8)
            r4[r3] = r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>(r9)
            r8.append(r3)
            r8.append(r7)
            java.lang.String r8 = r8.toString()
            int r8 = r0.readUE(r8)
            r5[r3] = r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>(r9)
            r8.append(r3)
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            int r7 = r0.readUE(r7)
            r6[r3] = r7
            int r3 = r3 + 1
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.H265TrackImplOld.getFrameRate(java.nio.ByteBuffer):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0064  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void hrd_parameters(boolean r12, int r13, com.googlecode.mp4parser.h264.read.CAVLCReader r14) throws java.io.IOException {
        /*
            r11 = this;
            r0 = 0
            if (r12 == 0) goto L_0x0054
            java.lang.String r12 = "nal_hrd_parameters_present_flag"
            boolean r12 = r14.readBool(r12)
            java.lang.String r1 = "vcl_hrd_parameters_present_flag"
            boolean r1 = r14.readBool(r1)
            if (r12 != 0) goto L_0x0013
            if (r1 == 0) goto L_0x0056
        L_0x0013:
            java.lang.String r2 = "sub_pic_hrd_params_present_flag"
            boolean r2 = r14.readBool(r2)
            r3 = 5
            if (r2 == 0) goto L_0x0032
            r4 = 8
            java.lang.String r5 = "tick_divisor_minus2"
            r14.readU(r4, r5)
            java.lang.String r4 = "du_cpb_removal_delay_increment_length_minus1"
            r14.readU(r3, r4)
            java.lang.String r4 = "sub_pic_cpb_params_in_pic_timing_sei_flag"
            r14.readBool(r4)
            java.lang.String r4 = "dpb_output_delay_du_length_minus1"
            r14.readU(r3, r4)
        L_0x0032:
            r4 = 4
            java.lang.String r5 = "bit_rate_scale"
            r14.readU(r4, r5)
            java.lang.String r5 = "cpb_size_scale"
            r14.readU(r4, r5)
            if (r2 == 0) goto L_0x0044
            java.lang.String r5 = "cpb_size_du_scale"
            r14.readU(r4, r5)
        L_0x0044:
            java.lang.String r4 = "initial_cpb_removal_delay_length_minus1"
            r14.readU(r3, r4)
            java.lang.String r4 = "au_cpb_removal_delay_length_minus1"
            r14.readU(r3, r4)
            java.lang.String r4 = "dpb_output_delay_length_minus1"
            r14.readU(r3, r4)
            goto L_0x0057
        L_0x0054:
            r12 = 0
            r1 = 0
        L_0x0056:
            r2 = 0
        L_0x0057:
            boolean[] r3 = new boolean[r13]
            boolean[] r4 = new boolean[r13]
            boolean[] r5 = new boolean[r13]
            int[] r6 = new int[r13]
            int[] r7 = new int[r13]
        L_0x0061:
            if (r0 <= r13) goto L_0x0064
            return
        L_0x0064:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "fixed_pic_rate_general_flag["
            r8.<init>(r9)
            r8.append(r0)
            java.lang.String r9 = "]"
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            boolean r8 = r14.readBool(r8)
            r3[r0] = r8
            boolean r8 = r3[r0]
            if (r8 != 0) goto L_0x0098
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r10 = "fixed_pic_rate_within_cvs_flag["
            r8.<init>(r10)
            r8.append(r0)
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            boolean r8 = r14.readBool(r8)
            r4[r0] = r8
        L_0x0098:
            boolean r8 = r4[r0]
            if (r8 == 0) goto L_0x00b4
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r10 = "elemental_duration_in_tc_minus1["
            r8.<init>(r10)
            r8.append(r0)
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            int r8 = r14.readUE(r8)
            r7[r0] = r8
            goto L_0x00cb
        L_0x00b4:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r10 = "low_delay_hrd_flag["
            r8.<init>(r10)
            r8.append(r0)
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            boolean r8 = r14.readBool(r8)
            r5[r0] = r8
        L_0x00cb:
            boolean r8 = r5[r0]
            if (r8 != 0) goto L_0x00e6
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r10 = "cpb_cnt_minus1["
            r8.<init>(r10)
            r8.append(r0)
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            int r8 = r14.readUE(r8)
            r6[r0] = r8
        L_0x00e6:
            if (r12 == 0) goto L_0x00ed
            r8 = r6[r0]
            r11.sub_layer_hrd_parameters(r0, r8, r2, r14)
        L_0x00ed:
            if (r1 == 0) goto L_0x00f4
            r8 = r6[r0]
            r11.sub_layer_hrd_parameters(r0, r8, r2, r14)
        L_0x00f4:
            int r0 = r0 + 1
            goto L_0x0061
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.H265TrackImplOld.hrd_parameters(boolean, int, com.googlecode.mp4parser.h264.read.CAVLCReader):void");
    }

    /* access modifiers changed from: package-private */
    public void sub_layer_hrd_parameters(int i, int i2, boolean z, CAVLCReader cAVLCReader) throws IOException {
        int[] iArr = new int[i2];
        int[] iArr2 = new int[i2];
        int[] iArr3 = new int[i2];
        int[] iArr4 = new int[i2];
        boolean[] zArr = new boolean[i2];
        for (int i3 = 0; i3 <= i2; i3++) {
            iArr[i3] = cAVLCReader.readUE("bit_rate_value_minus1[" + i3 + "]");
            iArr2[i3] = cAVLCReader.readUE("cpb_size_value_minus1[" + i3 + "]");
            if (z) {
                iArr3[i3] = cAVLCReader.readUE("cpb_size_du_value_minus1[" + i3 + "]");
                iArr4[i3] = cAVLCReader.readUE("bit_rate_du_value_minus1[" + i3 + "]");
            }
            zArr[i3] = cAVLCReader.readBool("cbr_flag[" + i3 + "]");
        }
    }

    private List<HevcDecoderConfigurationRecord.Array> getArrays() {
        HevcDecoderConfigurationRecord.Array array = new HevcDecoderConfigurationRecord.Array();
        array.array_completeness = true;
        array.nal_unit_type = 32;
        array.nalUnits = new ArrayList();
        for (ByteBuffer next : this.videoParamterSets.values()) {
            byte[] bArr = new byte[next.limit()];
            next.position(0);
            next.get(bArr);
            array.nalUnits.add(bArr);
        }
        HevcDecoderConfigurationRecord.Array array2 = new HevcDecoderConfigurationRecord.Array();
        array2.array_completeness = true;
        array2.nal_unit_type = 33;
        array2.nalUnits = new ArrayList();
        for (ByteBuffer next2 : this.sequenceParamterSets.values()) {
            byte[] bArr2 = new byte[next2.limit()];
            next2.position(0);
            next2.get(bArr2);
            array2.nalUnits.add(bArr2);
        }
        HevcDecoderConfigurationRecord.Array array3 = new HevcDecoderConfigurationRecord.Array();
        array3.array_completeness = true;
        array3.nal_unit_type = 33;
        array3.nalUnits = new ArrayList();
        for (ByteBuffer next3 : this.pictureParamterSets.values()) {
            byte[] bArr3 = new byte[next3.limit()];
            next3.position(0);
            next3.get(bArr3);
            array3.nalUnits.add(bArr3);
        }
        return Arrays.asList(new HevcDecoderConfigurationRecord.Array[]{array, array2, array3});
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstOfAU(int i, ByteBuffer byteBuffer, List<ByteBuffer> list) {
        if (list.isEmpty()) {
            return true;
        }
        boolean z = getNalUnitHeader(list.get(list.size() - 1)).nalUnitType <= 31;
        switch (i) {
            case 32:
            case 33:
            case 34:
            case 35:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                if (z) {
                    return true;
                }
                break;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                break;
            default:
                switch (i) {
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        break;
                    default:
                        return false;
                }
        }
        byteBuffer.position(0);
        byteBuffer.get(new byte[50]);
        byteBuffer.position(2);
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        if (!z || (readUInt8 & 128) <= 0) {
            return false;
        }
        return true;
    }

    public NalUnitHeader getNalUnitHeader(ByteBuffer byteBuffer) {
        byteBuffer.position(0);
        int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
        NalUnitHeader nalUnitHeader = new NalUnitHeader();
        nalUnitHeader.forbiddenZeroFlag = (32768 & readUInt16) >> 15;
        nalUnitHeader.nalUnitType = (readUInt16 & 32256) >> 9;
        nalUnitHeader.nuhLayerId = (readUInt16 & 504) >> 3;
        nalUnitHeader.nuhTemporalIdPlusOne = readUInt16 & 7;
        return nalUnitHeader;
    }

    /* access modifiers changed from: protected */
    public Sample createSample(List<ByteBuffer> list) {
        byte[] bArr = new byte[(list.size() * 4)];
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        for (ByteBuffer remaining : list) {
            wrap.putInt(remaining.remaining());
        }
        ByteBuffer[] byteBufferArr = new ByteBuffer[(list.size() * 2)];
        for (int i = 0; i < list.size(); i++) {
            int i2 = i * 2;
            byteBufferArr[i2] = ByteBuffer.wrap(bArr, i * 4, 4);
            byteBufferArr[i2 + 1] = list.get(i);
        }
        return new SampleImpl(byteBufferArr);
    }

    class LookAhead {
        ByteBuffer buffer;
        long bufferStartPos = 0;
        DataSource dataSource;
        int inBufferPos = 0;
        long start;

        LookAhead(DataSource dataSource2) throws IOException {
            this.dataSource = dataSource2;
            fillBuffer();
        }

        public void fillBuffer() throws IOException {
            DataSource dataSource2 = this.dataSource;
            this.buffer = dataSource2.map(this.bufferStartPos, Math.min(dataSource2.size() - this.bufferStartPos, 1048576));
        }

        /* access modifiers changed from: package-private */
        public boolean nextThreeEquals001() throws IOException {
            int limit = this.buffer.limit();
            int i = this.inBufferPos;
            if (limit - i >= 3) {
                if (this.buffer.get(i) == 0 && this.buffer.get(this.inBufferPos + 1) == 0 && this.buffer.get(this.inBufferPos + 2) == 1) {
                    return true;
                }
                return false;
            } else if (this.bufferStartPos + ((long) i) == this.dataSource.size()) {
                throw new EOFException();
            } else {
                throw new RuntimeException("buffer repositioning require");
            }
        }

        /* access modifiers changed from: package-private */
        public boolean nextThreeEquals000or001orEof() throws IOException {
            int limit = this.buffer.limit();
            int i = this.inBufferPos;
            if (limit - i >= 3) {
                return this.buffer.get(i) == 0 && this.buffer.get(this.inBufferPos + 1) == 0 && (this.buffer.get(this.inBufferPos + 2) == 0 || this.buffer.get(this.inBufferPos + 2) == 1);
            }
            if (this.bufferStartPos + ((long) i) + 3 > this.dataSource.size()) {
                return this.bufferStartPos + ((long) this.inBufferPos) == this.dataSource.size();
            }
            this.bufferStartPos = this.start;
            this.inBufferPos = 0;
            fillBuffer();
            return nextThreeEquals000or001orEof();
        }

        /* access modifiers changed from: package-private */
        public void discardByte() {
            this.inBufferPos++;
        }

        /* access modifiers changed from: package-private */
        public void discardNext3AndMarkStart() {
            this.inBufferPos += 3;
            this.start = this.bufferStartPos + ((long) this.inBufferPos);
        }

        public ByteBuffer getNal() {
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
