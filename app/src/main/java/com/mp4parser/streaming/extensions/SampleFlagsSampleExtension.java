package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.SampleExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SampleFlagsSampleExtension implements SampleExtension {
    public static Map<Long, SampleFlagsSampleExtension> pool = Collections.synchronizedMap(new HashMap());
    private byte isLeading;
    private int sampleDegradationPriority;
    private byte sampleDependsOn;
    private byte sampleHasRedundancy;
    private byte sampleIsDependedOn;
    private boolean sampleIsNonSyncSample;
    private byte samplePaddingValue;

    public static SampleFlagsSampleExtension create(byte b, byte b2, byte b3, byte b4, byte b5, boolean z, int i) {
        long j = ((long) ((b2 << 2) + b + (b3 << 4) + (b4 << 6))) + ((long) (b5 << 8)) + ((long) (i << 11)) + ((long) ((z ? 1 : 0) << true));
        SampleFlagsSampleExtension sampleFlagsSampleExtension = pool.get(Long.valueOf(j));
        if (sampleFlagsSampleExtension != null) {
            return sampleFlagsSampleExtension;
        }
        SampleFlagsSampleExtension sampleFlagsSampleExtension2 = new SampleFlagsSampleExtension();
        sampleFlagsSampleExtension2.isLeading = b;
        sampleFlagsSampleExtension2.sampleDependsOn = b2;
        sampleFlagsSampleExtension2.sampleIsDependedOn = b3;
        sampleFlagsSampleExtension2.sampleHasRedundancy = b4;
        sampleFlagsSampleExtension2.samplePaddingValue = b5;
        sampleFlagsSampleExtension2.sampleIsNonSyncSample = z;
        sampleFlagsSampleExtension2.sampleDegradationPriority = i;
        pool.put(Long.valueOf(j), sampleFlagsSampleExtension2);
        return sampleFlagsSampleExtension2;
    }

    public byte getIsLeading() {
        return this.isLeading;
    }

    public void setIsLeading(byte b) {
        this.isLeading = b;
    }

    public byte getSampleDependsOn() {
        return this.sampleDependsOn;
    }

    public void setSampleDependsOn(byte b) {
        this.sampleDependsOn = b;
    }

    public byte getSampleIsDependedOn() {
        return this.sampleIsDependedOn;
    }

    public void setSampleIsDependedOn(byte b) {
        this.sampleIsDependedOn = b;
    }

    public byte getSampleHasRedundancy() {
        return this.sampleHasRedundancy;
    }

    public void setSampleHasRedundancy(byte b) {
        this.sampleHasRedundancy = b;
    }

    public byte getSamplePaddingValue() {
        return this.samplePaddingValue;
    }

    public void setSamplePaddingValue(byte b) {
        this.samplePaddingValue = b;
    }

    public boolean isSampleIsNonSyncSample() {
        return this.sampleIsNonSyncSample;
    }

    public boolean isSyncSample() {
        return !this.sampleIsNonSyncSample;
    }

    public void setSampleIsNonSyncSample(boolean z) {
        this.sampleIsNonSyncSample = z;
    }

    public int getSampleDegradationPriority() {
        return this.sampleDegradationPriority;
    }

    public void setSampleDegradationPriority(int i) {
        this.sampleDegradationPriority = i;
    }
}
