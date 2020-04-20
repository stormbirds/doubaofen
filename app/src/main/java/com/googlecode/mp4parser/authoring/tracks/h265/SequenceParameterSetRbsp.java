package com.googlecode.mp4parser.authoring.tracks.h265;

import com.googlecode.mp4parser.h264.read.CAVLCReader;
import java.io.IOException;
import java.io.InputStream;

public class SequenceParameterSetRbsp {
    public SequenceParameterSetRbsp(InputStream inputStream) throws IOException {
        CAVLCReader cAVLCReader = new CAVLCReader(inputStream);
        cAVLCReader.readNBit(4, "sps_video_parameter_set_id");
        int readNBit = (int) cAVLCReader.readNBit(3, "sps_max_sub_layers_minus1");
        cAVLCReader.readBool("sps_temporal_id_nesting_flag");
        profile_tier_level(readNBit, cAVLCReader);
        cAVLCReader.readUE("sps_seq_parameter_set_id");
        if (cAVLCReader.readUE("chroma_format_idc") == 3) {
            cAVLCReader.read1Bit();
            cAVLCReader.readUE("pic_width_in_luma_samples");
            cAVLCReader.readUE("pic_width_in_luma_samples");
            if (cAVLCReader.readBool("conformance_window_flag")) {
                cAVLCReader.readUE("conf_win_left_offset");
                cAVLCReader.readUE("conf_win_right_offset");
                cAVLCReader.readUE("conf_win_top_offset");
                cAVLCReader.readUE("conf_win_bottom_offset");
            }
        }
        cAVLCReader.readUE("bit_depth_luma_minus8");
        cAVLCReader.readUE("bit_depth_chroma_minus8");
        cAVLCReader.readUE("log2_max_pic_order_cnt_lsb_minus4");
        boolean readBool = cAVLCReader.readBool("sps_sub_layer_ordering_info_present_flag");
        int i = 0;
        int i2 = (readNBit - (readBool ? 0 : readNBit)) + 1;
        int[] iArr = new int[i2];
        int[] iArr2 = new int[i2];
        int[] iArr3 = new int[i2];
        for (i = !readBool ? readNBit : i; i <= readNBit; i++) {
            iArr[i] = cAVLCReader.readUE("sps_max_dec_pic_buffering_minus1[" + i + "]");
            iArr2[i] = cAVLCReader.readUE("sps_max_num_reorder_pics[" + i + "]");
            iArr3[i] = cAVLCReader.readUE("sps_max_latency_increase_plus1[" + i + "]");
        }
        cAVLCReader.readUE("log2_min_luma_coding_block_size_minus3");
        cAVLCReader.readUE("log2_diff_max_min_luma_coding_block_size");
        cAVLCReader.readUE("log2_min_transform_block_size_minus2");
        cAVLCReader.readUE("log2_diff_max_min_transform_block_size");
        cAVLCReader.readUE("max_transform_hierarchy_depth_inter");
        cAVLCReader.readUE("max_transform_hierarchy_depth_intra");
        if (cAVLCReader.readBool("scaling_list_enabled_flag") && cAVLCReader.readBool("sps_scaling_list_data_present_flag")) {
            scaling_list_data(cAVLCReader);
        }
        cAVLCReader.readBool("amp_enabled_flag");
        cAVLCReader.readBool("sample_adaptive_offset_enabled_flag");
        if (cAVLCReader.readBool("pcm_enabled_flag")) {
            cAVLCReader.readNBit(4, "pcm_sample_bit_depth_luma_minus1");
            cAVLCReader.readNBit(4, "pcm_sample_bit_depth_chroma_minus1");
            cAVLCReader.readUE("log2_min_pcm_luma_coding_block_size_minus3");
        }
    }

    private void scaling_list_data(CAVLCReader cAVLCReader) throws IOException {
        CAVLCReader cAVLCReader2 = cAVLCReader;
        int i = 4;
        boolean[][] zArr = new boolean[4][];
        int[][] iArr = new int[4][];
        int[][] iArr2 = new int[2][];
        int[][][] iArr3 = new int[4][][];
        int i2 = 0;
        while (i2 < i) {
            int i3 = 0;
            while (true) {
                int i4 = 6;
                if (i3 >= (i2 == 3 ? 2 : 6)) {
                    break;
                }
                zArr[i2] = new boolean[(i2 == 3 ? 2 : 6)];
                iArr[i2] = new int[(i2 == 3 ? 2 : 6)];
                if (i2 == 3) {
                    i4 = 2;
                }
                iArr3[i2] = new int[i4][];
                zArr[i2][i3] = cAVLCReader.readBool();
                if (!zArr[i2][i3]) {
                    iArr[i2][i3] = cAVLCReader2.readUE("scaling_list_pred_matrix_id_delta[" + i2 + "][" + i3 + "]");
                } else {
                    int min = Math.min(64, 1 << ((i2 << 1) + i));
                    int i5 = 8;
                    if (i2 > 1) {
                        int i6 = i2 - 2;
                        iArr2[i6][i3] = cAVLCReader2.readSE("scaling_list_dc_coef_minus8[" + i2 + "- 2][" + i3 + "]");
                        i5 = 8 + iArr2[i6][i3];
                    }
                    iArr3[i2][i3] = new int[min];
                    for (int i7 = 0; i7 < min; i7++) {
                        i5 = ((i5 + cAVLCReader2.readSE("scaling_list_delta_coef ")) + 256) % 256;
                        iArr3[i2][i3][i7] = i5;
                    }
                }
                i3++;
                i = 4;
            }
            i2++;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v1, resolved type: boolean[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void profile_tier_level(int r21, com.googlecode.mp4parser.h264.read.CAVLCReader r22) throws java.io.IOException {
        /*
            r20 = this;
            r0 = r21
            r1 = r22
            r2 = 2
            java.lang.String r3 = "general_profile_space"
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
            if (r7 < r4) goto L_0x01d8
            java.lang.String r5 = "general_progressive_source_flag"
            r1.readBool(r5)
            java.lang.String r5 = "general_interlaced_source_flag"
            r1.readBool(r5)
            java.lang.String r5 = "general_non_packed_constraint_flag"
            r1.readBool(r5)
            java.lang.String r5 = "general_frame_only_constraint_flag"
            r1.readBool(r5)
            r8 = 44
            java.lang.String r5 = "general_reserved_zero_44bits"
            r1.readNBit(r8, r5)
            r22.readByte()
            boolean[] r9 = new boolean[r0]
            boolean[] r10 = new boolean[r0]
            r5 = 0
        L_0x003f:
            java.lang.String r7 = "]"
            if (r5 < r0) goto L_0x019c
            r11 = 8
            if (r0 <= 0) goto L_0x0067
            int[] r5 = new int[r11]
            r12 = r0
        L_0x004a:
            if (r12 < r11) goto L_0x004d
            goto L_0x0067
        L_0x004d:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            java.lang.String r14 = "reserved_zero_2bits["
            r13.<init>(r14)
            r13.append(r12)
            r13.append(r7)
            java.lang.String r13 = r13.toString()
            int r13 = r1.readU(r2, r13)
            r5[r12] = r13
            int r12 = r12 + 1
            goto L_0x004a
        L_0x0067:
            int[] r12 = new int[r0]
            boolean[] r13 = new boolean[r0]
            int[] r14 = new int[r0]
            int[] r5 = new int[]{r0, r4}
            java.lang.Class<boolean> r15 = boolean.class
            java.lang.Object r5 = java.lang.reflect.Array.newInstance(r15, r5)
            r15 = r5
            boolean[][] r15 = (boolean[][]) r15
            boolean[] r5 = new boolean[r0]
            boolean[] r6 = new boolean[r0]
            boolean[] r11 = new boolean[r0]
            boolean[] r8 = new boolean[r0]
            long[] r4 = new long[r0]
            int[] r3 = new int[r0]
            r2 = 0
        L_0x0087:
            if (r2 < r0) goto L_0x008a
            return
        L_0x008a:
            boolean r17 = r9[r2]
            if (r17 == 0) goto L_0x016e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r17 = r9
            java.lang.String r9 = "sub_layer_profile_space["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            r9 = 2
            int r0 = r1.readU(r9, r0)
            r12[r2] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r9 = "sub_layer_tier_flag["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r13[r2] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r9 = "sub_layer_profile_idc["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            r9 = 5
            int r0 = r1.readU(r9, r0)
            r14[r2] = r0
            r0 = 0
        L_0x00d8:
            r9 = 32
            if (r0 < r9) goto L_0x0143
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r9 = "sub_layer_progressive_source_flag["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r5[r2] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r9 = "sub_layer_interlaced_source_flag["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r6[r2] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r9 = "sub_layer_non_packed_constraint_flag["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r11[r2] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r9 = "sub_layer_frame_only_constraint_flag["
            r0.<init>(r9)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r8[r2] = r0
            r9 = 44
            long r18 = r1.readNBit(r9)
            r4[r2] = r18
            r19 = r4
            goto L_0x0172
        L_0x0143:
            r9 = 44
            r16 = r15[r2]
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r19 = r4
            java.lang.String r4 = "sub_layer_profile_compatibility_flag["
            r9.<init>(r4)
            r9.append(r2)
            java.lang.String r4 = "]["
            r9.append(r4)
            r9.append(r0)
            r9.append(r7)
            java.lang.String r4 = r9.toString()
            boolean r4 = r1.readBool(r4)
            r16[r0] = r4
            int r0 = r0 + 1
            r4 = r19
            goto L_0x00d8
        L_0x016e:
            r19 = r4
            r17 = r9
        L_0x0172:
            boolean r0 = r10[r2]
            if (r0 == 0) goto L_0x0190
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r4 = "sub_layer_level_idc["
            r0.<init>(r4)
            r0.append(r2)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            r4 = 8
            int r0 = r1.readU(r4, r0)
            r3[r2] = r0
            goto L_0x0192
        L_0x0190:
            r4 = 8
        L_0x0192:
            int r2 = r2 + 1
            r0 = r21
            r9 = r17
            r4 = r19
            goto L_0x0087
        L_0x019c:
            r17 = r9
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_profile_present_flag["
            r0.<init>(r2)
            r0.append(r5)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r17[r5] = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "sub_layer_level_present_flag["
            r0.<init>(r2)
            r0.append(r5)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            boolean r0 = r1.readBool(r0)
            r10[r5] = r0
            int r5 = r5 + 1
            r0 = r21
            r2 = 2
            r3 = 5
            r4 = 32
            r8 = 44
            goto L_0x003f
        L_0x01d8:
            boolean r0 = r22.readBool()
            r5[r7] = r0
            int r7 = r7 + 1
            r0 = r21
            r2 = 2
            r3 = 5
            r4 = 32
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.tracks.h265.SequenceParameterSetRbsp.profile_tier_level(int, com.googlecode.mp4parser.h264.read.CAVLCReader):void");
    }
}
