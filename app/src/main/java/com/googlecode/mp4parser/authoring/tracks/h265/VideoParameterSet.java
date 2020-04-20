package com.googlecode.mp4parser.authoring.tracks.h265;

import com.googlecode.mp4parser.h264.read.CAVLCReader;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoParameterSet {
    ByteBuffer vps;
    int vps_parameter_set_id;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v0, resolved type: boolean[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public VideoParameterSet(java.nio.ByteBuffer r14) throws java.io.IOException {
        /*
            r13 = this;
            r13.<init>()
            r13.vps = r14
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
            int r14 = r0.readU(r14, r1)
            r13.vps_parameter_set_id = r14
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
            r4 = 1
            if (r3 == 0) goto L_0x0050
            r5 = 1
            goto L_0x0052
        L_0x0050:
            int r5 = r1 + 1
        L_0x0052:
            int[] r5 = new int[r5]
            if (r3 == 0) goto L_0x0058
            r6 = 1
            goto L_0x005a
        L_0x0058:
            int r6 = r1 + 1
        L_0x005a:
            int[] r6 = new int[r6]
            if (r3 == 0) goto L_0x0060
            r7 = 1
            goto L_0x0062
        L_0x0060:
            int r7 = r1 + 1
        L_0x0062:
            int[] r7 = new int[r7]
            if (r3 == 0) goto L_0x0068
            r3 = 0
            goto L_0x0069
        L_0x0068:
            r3 = r1
        L_0x0069:
            java.lang.String r8 = "]"
            if (r3 <= r1) goto L_0x0137
            java.lang.String r3 = "vps_max_layer_id"
            int r9 = r0.readU(r14, r3)
            java.lang.String r14 = "vps_num_layer_sets_minus1"
            int r10 = r0.readUE(r14)
            int[] r14 = new int[]{r10, r9}
            java.lang.Class<boolean> r3 = boolean.class
            java.lang.Object r14 = java.lang.reflect.Array.newInstance(r3, r14)
            r11 = r14
            boolean[][] r11 = (boolean[][]) r11
            r12 = 1
        L_0x0087:
            if (r12 <= r10) goto L_0x010c
            java.lang.String r14 = "vps_timing_info_present_flag"
            boolean r14 = r0.readBool(r14)
            if (r14 == 0) goto L_0x00f3
            r14 = 32
            java.lang.String r3 = "vps_num_units_in_tick"
            r0.readU(r14, r3)
            java.lang.String r3 = "vps_time_scale"
            r0.readU(r14, r3)
            java.lang.String r14 = "vps_poc_proportional_to_timing_flag"
            boolean r14 = r0.readBool(r14)
            if (r14 == 0) goto L_0x00aa
            java.lang.String r14 = "vps_num_ticks_poc_diff_one_minus1"
            r0.readUE(r14)
        L_0x00aa:
            java.lang.String r14 = "vps_num_hrd_parameters"
            int r14 = r0.readUE(r14)
            int[] r3 = new int[r14]
            boolean[] r5 = new boolean[r14]
            r6 = 0
        L_0x00b5:
            if (r6 < r14) goto L_0x00b8
            goto L_0x00f3
        L_0x00b8:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r9 = "hrd_layer_set_idx["
            r7.<init>(r9)
            r7.append(r6)
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            int r7 = r0.readUE(r7)
            r3[r6] = r7
            if (r6 <= 0) goto L_0x00e9
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r9 = "cprms_present_flag["
            r7.<init>(r9)
            r7.append(r6)
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            boolean r7 = r0.readBool(r7)
            r5[r6] = r7
            goto L_0x00eb
        L_0x00e9:
            r5[r2] = r4
        L_0x00eb:
            boolean r7 = r5[r6]
            r13.hrd_parameters(r7, r1, r0)
            int r6 = r6 + 1
            goto L_0x00b5
        L_0x00f3:
            java.lang.String r14 = "vps_extension_flag"
            boolean r14 = r0.readBool(r14)
            if (r14 == 0) goto L_0x0108
        L_0x00fb:
            boolean r14 = r0.moreRBSPData()
            if (r14 != 0) goto L_0x0102
            goto L_0x0108
        L_0x0102:
            java.lang.String r14 = "vps_extension_data_flag"
            r0.readBool(r14)
            goto L_0x00fb
        L_0x0108:
            r0.readTrailingBits()
            return
        L_0x010c:
            r14 = 0
        L_0x010d:
            if (r14 <= r9) goto L_0x0113
            int r12 = r12 + 1
            goto L_0x0087
        L_0x0113:
            r3 = r11[r12]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "layer_id_included_flag["
            r5.<init>(r6)
            r5.append(r12)
            java.lang.String r6 = "]["
            r5.append(r6)
            r5.append(r14)
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            boolean r5 = r0.readBool(r5)
            r3[r14] = r5
            int r14 = r14 + 1
            goto L_0x010d
        L_0x0137:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "vps_max_dec_pic_buffering_minus1["
            r9.<init>(r10)
            r9.append(r3)
            r9.append(r8)
            java.lang.String r9 = r9.toString()
            int r9 = r0.readUE(r9)
            r5[r3] = r9
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r10)
            r9.append(r3)
            r9.append(r8)
            java.lang.String r9 = r9.toString()
            int r9 = r0.readUE(r9)
            r6[r3] = r9
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r10)
            r9.append(r3)
            r9.append(r8)
            java.lang.String r8 = r9.toString()
            int r8 = r0.readUE(r8)
            r7[r3] = r8
            int r3 = r3 + 1
            goto L_0x0069
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.VideoParameterSet.<init>(java.nio.ByteBuffer):void");
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
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.VideoParameterSet.profile_tier_level(int, com.googlecode.mp4parser.h264.read.CAVLCReader):void");
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
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.VideoParameterSet.hrd_parameters(boolean, int, com.googlecode.mp4parser.h264.read.CAVLCReader):void");
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

    public ByteBuffer toByteBuffer() {
        return this.vps;
    }
}
