package com.mp4parser.iso14496.part15;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class HevcDecoderConfigurationRecord {
    List<Array> arrays = new ArrayList();
    int avgFrameRate;
    int bitDepthChromaMinus8;
    int bitDepthLumaMinus8;
    int chromaFormat;
    int configurationVersion;
    int constantFrameRate;
    boolean frame_only_constraint_flag;
    long general_constraint_indicator_flags;
    int general_level_idc;
    long general_profile_compatibility_flags;
    int general_profile_idc;
    int general_profile_space;
    boolean general_tier_flag;
    boolean interlaced_source_flag;
    int lengthSizeMinusOne;
    int min_spatial_segmentation_idc;
    boolean non_packed_constraint_flag;
    int numTemporalLayers;
    int parallelismType;
    boolean progressive_source_flag;
    int reserved1 = 15;
    int reserved2 = 63;
    int reserved3 = 63;
    int reserved4 = 31;
    int reserved5 = 31;
    boolean temporalIdNested;

    public void parse(ByteBuffer byteBuffer) {
        this.configurationVersion = IsoTypeReader.readUInt8(byteBuffer);
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        this.general_profile_space = (readUInt8 & Opcodes.CHECKCAST) >> 6;
        this.general_tier_flag = (readUInt8 & 32) > 0;
        this.general_profile_idc = readUInt8 & 31;
        this.general_profile_compatibility_flags = IsoTypeReader.readUInt32(byteBuffer);
        this.general_constraint_indicator_flags = IsoTypeReader.readUInt48(byteBuffer);
        this.frame_only_constraint_flag = ((this.general_constraint_indicator_flags >> 44) & 8) > 0;
        this.non_packed_constraint_flag = ((this.general_constraint_indicator_flags >> 44) & 4) > 0;
        this.interlaced_source_flag = ((this.general_constraint_indicator_flags >> 44) & 2) > 0;
        this.progressive_source_flag = ((this.general_constraint_indicator_flags >> 44) & 1) > 0;
        this.general_constraint_indicator_flags &= 140737488355327L;
        this.general_level_idc = IsoTypeReader.readUInt8(byteBuffer);
        int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
        this.reserved1 = (61440 & readUInt16) >> 12;
        this.min_spatial_segmentation_idc = readUInt16 & 4095;
        int readUInt82 = IsoTypeReader.readUInt8(byteBuffer);
        this.reserved2 = (readUInt82 & 252) >> 2;
        this.parallelismType = readUInt82 & 3;
        int readUInt83 = IsoTypeReader.readUInt8(byteBuffer);
        this.reserved3 = (readUInt83 & 252) >> 2;
        this.chromaFormat = readUInt83 & 3;
        int readUInt84 = IsoTypeReader.readUInt8(byteBuffer);
        this.reserved4 = (readUInt84 & 248) >> 3;
        this.bitDepthLumaMinus8 = readUInt84 & 7;
        int readUInt85 = IsoTypeReader.readUInt8(byteBuffer);
        this.reserved5 = (readUInt85 & 248) >> 3;
        this.bitDepthChromaMinus8 = readUInt85 & 7;
        this.avgFrameRate = IsoTypeReader.readUInt16(byteBuffer);
        int readUInt86 = IsoTypeReader.readUInt8(byteBuffer);
        this.constantFrameRate = (readUInt86 & Opcodes.CHECKCAST) >> 6;
        this.numTemporalLayers = (readUInt86 & 56) >> 3;
        this.temporalIdNested = (readUInt86 & 4) > 0;
        this.lengthSizeMinusOne = readUInt86 & 3;
        int readUInt87 = IsoTypeReader.readUInt8(byteBuffer);
        this.arrays = new ArrayList();
        for (int i = 0; i < readUInt87; i++) {
            Array array = new Array();
            int readUInt88 = IsoTypeReader.readUInt8(byteBuffer);
            array.array_completeness = (readUInt88 & 128) > 0;
            array.reserved = (readUInt88 & 64) > 0;
            array.nal_unit_type = readUInt88 & 63;
            int readUInt162 = IsoTypeReader.readUInt16(byteBuffer);
            array.nalUnits = new ArrayList();
            for (int i2 = 0; i2 < readUInt162; i2++) {
                byte[] bArr = new byte[IsoTypeReader.readUInt16(byteBuffer)];
                byteBuffer.get(bArr);
                array.nalUnits.add(bArr);
            }
            this.arrays.add(array);
        }
    }

    public void write(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, this.configurationVersion);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.general_profile_space << 6) + (this.general_tier_flag ? 32 : 0) + this.general_profile_idc);
        IsoTypeWriter.writeUInt32(byteBuffer, this.general_profile_compatibility_flags);
        long j = this.general_constraint_indicator_flags;
        if (this.frame_only_constraint_flag) {
            j |= 140737488355328L;
        }
        if (this.non_packed_constraint_flag) {
            j |= 70368744177664L;
        }
        if (this.interlaced_source_flag) {
            j |= 35184372088832L;
        }
        if (this.progressive_source_flag) {
            j |= 17592186044416L;
        }
        IsoTypeWriter.writeUInt48(byteBuffer, j);
        IsoTypeWriter.writeUInt8(byteBuffer, this.general_level_idc);
        IsoTypeWriter.writeUInt16(byteBuffer, (this.reserved1 << 12) + this.min_spatial_segmentation_idc);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved2 << 2) + this.parallelismType);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved3 << 2) + this.chromaFormat);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved4 << 3) + this.bitDepthLumaMinus8);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved5 << 3) + this.bitDepthChromaMinus8);
        IsoTypeWriter.writeUInt16(byteBuffer, this.avgFrameRate);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.constantFrameRate << 6) + (this.numTemporalLayers << 3) + (this.temporalIdNested ? 4 : 0) + this.lengthSizeMinusOne);
        IsoTypeWriter.writeUInt8(byteBuffer, this.arrays.size());
        for (Array next : this.arrays) {
            IsoTypeWriter.writeUInt8(byteBuffer, (next.array_completeness ? 128 : 0) + (next.reserved ? 64 : 0) + next.nal_unit_type);
            IsoTypeWriter.writeUInt16(byteBuffer, next.nalUnits.size());
            for (byte[] next2 : next.nalUnits) {
                IsoTypeWriter.writeUInt16(byteBuffer, next2.length);
                byteBuffer.put(next2);
            }
        }
    }

    public int getSize() {
        int i = 23;
        for (Array array : this.arrays) {
            i += 3;
            for (byte[] length : array.nalUnits) {
                i = i + 2 + length.length;
            }
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HevcDecoderConfigurationRecord hevcDecoderConfigurationRecord = (HevcDecoderConfigurationRecord) obj;
        if (this.avgFrameRate != hevcDecoderConfigurationRecord.avgFrameRate || this.bitDepthChromaMinus8 != hevcDecoderConfigurationRecord.bitDepthChromaMinus8 || this.bitDepthLumaMinus8 != hevcDecoderConfigurationRecord.bitDepthLumaMinus8 || this.chromaFormat != hevcDecoderConfigurationRecord.chromaFormat || this.configurationVersion != hevcDecoderConfigurationRecord.configurationVersion || this.constantFrameRate != hevcDecoderConfigurationRecord.constantFrameRate || this.general_constraint_indicator_flags != hevcDecoderConfigurationRecord.general_constraint_indicator_flags || this.general_level_idc != hevcDecoderConfigurationRecord.general_level_idc || this.general_profile_compatibility_flags != hevcDecoderConfigurationRecord.general_profile_compatibility_flags || this.general_profile_idc != hevcDecoderConfigurationRecord.general_profile_idc || this.general_profile_space != hevcDecoderConfigurationRecord.general_profile_space || this.general_tier_flag != hevcDecoderConfigurationRecord.general_tier_flag || this.lengthSizeMinusOne != hevcDecoderConfigurationRecord.lengthSizeMinusOne || this.min_spatial_segmentation_idc != hevcDecoderConfigurationRecord.min_spatial_segmentation_idc || this.numTemporalLayers != hevcDecoderConfigurationRecord.numTemporalLayers || this.parallelismType != hevcDecoderConfigurationRecord.parallelismType || this.reserved1 != hevcDecoderConfigurationRecord.reserved1 || this.reserved2 != hevcDecoderConfigurationRecord.reserved2 || this.reserved3 != hevcDecoderConfigurationRecord.reserved3 || this.reserved4 != hevcDecoderConfigurationRecord.reserved4 || this.reserved5 != hevcDecoderConfigurationRecord.reserved5 || this.temporalIdNested != hevcDecoderConfigurationRecord.temporalIdNested) {
            return false;
        }
        List<Array> list = this.arrays;
        List<Array> list2 = hevcDecoderConfigurationRecord.arrays;
        return list == null ? list2 == null : list.equals(list2);
    }

    public int hashCode() {
        long j = this.general_profile_compatibility_flags;
        long j2 = this.general_constraint_indicator_flags;
        int i = ((((((((((((((((((((((((((((((((((((((((((this.configurationVersion * 31) + this.general_profile_space) * 31) + (this.general_tier_flag ? 1 : 0)) * 31) + this.general_profile_idc) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + this.general_level_idc) * 31) + this.reserved1) * 31) + this.min_spatial_segmentation_idc) * 31) + this.reserved2) * 31) + this.parallelismType) * 31) + this.reserved3) * 31) + this.chromaFormat) * 31) + this.reserved4) * 31) + this.bitDepthLumaMinus8) * 31) + this.reserved5) * 31) + this.bitDepthChromaMinus8) * 31) + this.avgFrameRate) * 31) + this.constantFrameRate) * 31) + this.numTemporalLayers) * 31) + (this.temporalIdNested ? 1 : 0)) * 31) + this.lengthSizeMinusOne) * 31;
        List<Array> list = this.arrays;
        return i + (list != null ? list.hashCode() : 0);
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        String str4;
        StringBuilder sb = new StringBuilder("HEVCDecoderConfigurationRecord{configurationVersion=");
        sb.append(this.configurationVersion);
        sb.append(", general_profile_space=");
        sb.append(this.general_profile_space);
        sb.append(", general_tier_flag=");
        sb.append(this.general_tier_flag);
        sb.append(", general_profile_idc=");
        sb.append(this.general_profile_idc);
        sb.append(", general_profile_compatibility_flags=");
        sb.append(this.general_profile_compatibility_flags);
        sb.append(", general_constraint_indicator_flags=");
        sb.append(this.general_constraint_indicator_flags);
        sb.append(", general_level_idc=");
        sb.append(this.general_level_idc);
        String str5 = "";
        if (this.reserved1 != 15) {
            str = ", reserved1=" + this.reserved1;
        } else {
            str = str5;
        }
        sb.append(str);
        sb.append(", min_spatial_segmentation_idc=");
        sb.append(this.min_spatial_segmentation_idc);
        if (this.reserved2 != 63) {
            str2 = ", reserved2=" + this.reserved2;
        } else {
            str2 = str5;
        }
        sb.append(str2);
        sb.append(", parallelismType=");
        sb.append(this.parallelismType);
        if (this.reserved3 != 63) {
            str3 = ", reserved3=" + this.reserved3;
        } else {
            str3 = str5;
        }
        sb.append(str3);
        sb.append(", chromaFormat=");
        sb.append(this.chromaFormat);
        if (this.reserved4 != 31) {
            str4 = ", reserved4=" + this.reserved4;
        } else {
            str4 = str5;
        }
        sb.append(str4);
        sb.append(", bitDepthLumaMinus8=");
        sb.append(this.bitDepthLumaMinus8);
        if (this.reserved5 != 31) {
            str5 = ", reserved5=" + this.reserved5;
        }
        sb.append(str5);
        sb.append(", bitDepthChromaMinus8=");
        sb.append(this.bitDepthChromaMinus8);
        sb.append(", avgFrameRate=");
        sb.append(this.avgFrameRate);
        sb.append(", constantFrameRate=");
        sb.append(this.constantFrameRate);
        sb.append(", numTemporalLayers=");
        sb.append(this.numTemporalLayers);
        sb.append(", temporalIdNested=");
        sb.append(this.temporalIdNested);
        sb.append(", lengthSizeMinusOne=");
        sb.append(this.lengthSizeMinusOne);
        sb.append(", arrays=");
        sb.append(this.arrays);
        sb.append('}');
        return sb.toString();
    }

    public int getConfigurationVersion() {
        return this.configurationVersion;
    }

    public void setConfigurationVersion(int i) {
        this.configurationVersion = i;
    }

    public int getGeneral_profile_space() {
        return this.general_profile_space;
    }

    public void setGeneral_profile_space(int i) {
        this.general_profile_space = i;
    }

    public boolean isGeneral_tier_flag() {
        return this.general_tier_flag;
    }

    public void setGeneral_tier_flag(boolean z) {
        this.general_tier_flag = z;
    }

    public int getGeneral_profile_idc() {
        return this.general_profile_idc;
    }

    public void setGeneral_profile_idc(int i) {
        this.general_profile_idc = i;
    }

    public long getGeneral_profile_compatibility_flags() {
        return this.general_profile_compatibility_flags;
    }

    public void setGeneral_profile_compatibility_flags(long j) {
        this.general_profile_compatibility_flags = j;
    }

    public long getGeneral_constraint_indicator_flags() {
        return this.general_constraint_indicator_flags;
    }

    public void setGeneral_constraint_indicator_flags(long j) {
        this.general_constraint_indicator_flags = j;
    }

    public int getGeneral_level_idc() {
        return this.general_level_idc;
    }

    public void setGeneral_level_idc(int i) {
        this.general_level_idc = i;
    }

    public int getMin_spatial_segmentation_idc() {
        return this.min_spatial_segmentation_idc;
    }

    public void setMin_spatial_segmentation_idc(int i) {
        this.min_spatial_segmentation_idc = i;
    }

    public int getParallelismType() {
        return this.parallelismType;
    }

    public void setParallelismType(int i) {
        this.parallelismType = i;
    }

    public int getChromaFormat() {
        return this.chromaFormat;
    }

    public void setChromaFormat(int i) {
        this.chromaFormat = i;
    }

    public int getBitDepthLumaMinus8() {
        return this.bitDepthLumaMinus8;
    }

    public void setBitDepthLumaMinus8(int i) {
        this.bitDepthLumaMinus8 = i;
    }

    public int getBitDepthChromaMinus8() {
        return this.bitDepthChromaMinus8;
    }

    public void setBitDepthChromaMinus8(int i) {
        this.bitDepthChromaMinus8 = i;
    }

    public int getAvgFrameRate() {
        return this.avgFrameRate;
    }

    public void setAvgFrameRate(int i) {
        this.avgFrameRate = i;
    }

    public int getNumTemporalLayers() {
        return this.numTemporalLayers;
    }

    public void setNumTemporalLayers(int i) {
        this.numTemporalLayers = i;
    }

    public int getLengthSizeMinusOne() {
        return this.lengthSizeMinusOne;
    }

    public void setLengthSizeMinusOne(int i) {
        this.lengthSizeMinusOne = i;
    }

    public boolean isTemporalIdNested() {
        return this.temporalIdNested;
    }

    public void setTemporalIdNested(boolean z) {
        this.temporalIdNested = z;
    }

    public int getConstantFrameRate() {
        return this.constantFrameRate;
    }

    public void setConstantFrameRate(int i) {
        this.constantFrameRate = i;
    }

    public List<Array> getArrays() {
        return this.arrays;
    }

    public void setArrays(List<Array> list) {
        this.arrays = list;
    }

    public boolean isFrame_only_constraint_flag() {
        return this.frame_only_constraint_flag;
    }

    public void setFrame_only_constraint_flag(boolean z) {
        this.frame_only_constraint_flag = z;
    }

    public boolean isNon_packed_constraint_flag() {
        return this.non_packed_constraint_flag;
    }

    public void setNon_packed_constraint_flag(boolean z) {
        this.non_packed_constraint_flag = z;
    }

    public boolean isInterlaced_source_flag() {
        return this.interlaced_source_flag;
    }

    public void setInterlaced_source_flag(boolean z) {
        this.interlaced_source_flag = z;
    }

    public boolean isProgressive_source_flag() {
        return this.progressive_source_flag;
    }

    public void setProgressive_source_flag(boolean z) {
        this.progressive_source_flag = z;
    }

    public static class Array {
        public boolean array_completeness;
        public List<byte[]> nalUnits;
        public int nal_unit_type;
        public boolean reserved;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Array array = (Array) obj;
            if (this.array_completeness != array.array_completeness || this.nal_unit_type != array.nal_unit_type || this.reserved != array.reserved) {
                return false;
            }
            ListIterator<byte[]> listIterator = this.nalUnits.listIterator();
            ListIterator<byte[]> listIterator2 = array.nalUnits.listIterator();
            while (listIterator.hasNext() && listIterator2.hasNext()) {
                byte[] next = listIterator.next();
                byte[] next2 = listIterator2.next();
                if (next == null) {
                    if (next2 != null) {
                    }
                } else if (!Arrays.equals(next, next2)) {
                }
                return false;
            }
            if (listIterator.hasNext() || listIterator2.hasNext()) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = (((((this.array_completeness ? 1 : 0) * true) + (this.reserved ? 1 : 0)) * 31) + this.nal_unit_type) * 31;
            List<byte[]> list = this.nalUnits;
            return i + (list != null ? list.hashCode() : 0);
        }

        public String toString() {
            return "Array{nal_unit_type=" + this.nal_unit_type + ", reserved=" + this.reserved + ", array_completeness=" + this.array_completeness + ", num_nals=" + this.nalUnits.size() + '}';
        }
    }
}
