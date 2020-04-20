package com.coremedia.iso.boxes.fragment;

import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.fastjson.parser.JSONLexer;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class SampleFlags {
    private byte isLeading;
    private byte reserved;
    private int sampleDegradationPriority;
    private byte sampleDependsOn;
    private byte sampleHasRedundancy;
    private byte sampleIsDependedOn;
    private boolean sampleIsDifferenceSample;
    private byte samplePaddingValue;

    public SampleFlags() {
    }

    public SampleFlags(ByteBuffer byteBuffer) {
        long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
        this.reserved = (byte) ((int) ((-268435456 & readUInt32) >> 28));
        this.isLeading = (byte) ((int) ((201326592 & readUInt32) >> 26));
        this.sampleDependsOn = (byte) ((int) ((50331648 & readUInt32) >> 24));
        this.sampleIsDependedOn = (byte) ((int) ((12582912 & readUInt32) >> 22));
        this.sampleHasRedundancy = (byte) ((int) ((3145728 & readUInt32) >> 20));
        this.samplePaddingValue = (byte) ((int) ((917504 & readUInt32) >> 17));
        this.sampleIsDifferenceSample = ((PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH & readUInt32) >> 16) > 0;
        this.sampleDegradationPriority = (int) (readUInt32 & 65535);
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt32(byteBuffer, ((long) (this.reserved << 28)) | 0 | ((long) (this.isLeading << JSONLexer.EOI)) | ((long) (this.sampleDependsOn << 24)) | ((long) (this.sampleIsDependedOn << 22)) | ((long) (this.sampleHasRedundancy << 20)) | ((long) (this.samplePaddingValue << 17)) | ((long) ((this.sampleIsDifferenceSample ? 1 : 0) << true)) | ((long) this.sampleDegradationPriority));
    }

    public int getReserved() {
        return this.reserved;
    }

    public void setReserved(int i) {
        this.reserved = (byte) i;
    }

    public byte getIsLeading() {
        return this.isLeading;
    }

    public void setIsLeading(byte b) {
        this.isLeading = b;
    }

    public int getSampleDependsOn() {
        return this.sampleDependsOn;
    }

    public void setSampleDependsOn(int i) {
        this.sampleDependsOn = (byte) i;
    }

    public int getSampleIsDependedOn() {
        return this.sampleIsDependedOn;
    }

    public void setSampleIsDependedOn(int i) {
        this.sampleIsDependedOn = (byte) i;
    }

    public int getSampleHasRedundancy() {
        return this.sampleHasRedundancy;
    }

    public void setSampleHasRedundancy(int i) {
        this.sampleHasRedundancy = (byte) i;
    }

    public int getSamplePaddingValue() {
        return this.samplePaddingValue;
    }

    public void setSamplePaddingValue(int i) {
        this.samplePaddingValue = (byte) i;
    }

    public boolean isSampleIsDifferenceSample() {
        return this.sampleIsDifferenceSample;
    }

    public void setSampleIsDifferenceSample(boolean z) {
        this.sampleIsDifferenceSample = z;
    }

    public int getSampleDegradationPriority() {
        return this.sampleDegradationPriority;
    }

    public void setSampleDegradationPriority(int i) {
        this.sampleDegradationPriority = i;
    }

    public String toString() {
        return "SampleFlags{reserved=" + this.reserved + ", isLeading=" + this.isLeading + ", depOn=" + this.sampleDependsOn + ", isDepOn=" + this.sampleIsDependedOn + ", hasRedundancy=" + this.sampleHasRedundancy + ", padValue=" + this.samplePaddingValue + ", isDiffSample=" + this.sampleIsDifferenceSample + ", degradPrio=" + this.sampleDegradationPriority + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SampleFlags sampleFlags = (SampleFlags) obj;
        return this.isLeading == sampleFlags.isLeading && this.reserved == sampleFlags.reserved && this.sampleDegradationPriority == sampleFlags.sampleDegradationPriority && this.sampleDependsOn == sampleFlags.sampleDependsOn && this.sampleHasRedundancy == sampleFlags.sampleHasRedundancy && this.sampleIsDependedOn == sampleFlags.sampleIsDependedOn && this.sampleIsDifferenceSample == sampleFlags.sampleIsDifferenceSample && this.samplePaddingValue == sampleFlags.samplePaddingValue;
    }

    public int hashCode() {
        return (((((((((((((this.reserved * 31) + this.isLeading) * 31) + this.sampleDependsOn) * 31) + this.sampleIsDependedOn) * 31) + this.sampleHasRedundancy) * 31) + this.samplePaddingValue) * 31) + (this.sampleIsDifferenceSample ? 1 : 0)) * 31) + this.sampleDegradationPriority;
    }
}
