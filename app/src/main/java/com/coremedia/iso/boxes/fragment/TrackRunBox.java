package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.anko.DimensionsKt;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackRunBox extends AbstractFullBox {
    public static final String TYPE = "trun";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_14 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_15 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_16 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_17 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_18 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_19 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9 = null;
    private int dataOffset;
    private List<Entry> entries = new ArrayList();
    private SampleFlags firstSampleFlags;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("TrackRunBox.java", TrackRunBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "java.util.List"), 57);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setDataOffset", "com.coremedia.iso.boxes.fragment.TrackRunBox", "int", "dataOffset", "", "void"), (int) DimensionsKt.LDPI);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setDataOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), 267);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setSampleSizePresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), 275);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setSampleDurationPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), 283);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setSampleFlagsPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), 292);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setSampleCompositionTimeOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), 300);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getDataOffset", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "int"), 309);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getFirstSampleFlags", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "com.coremedia.iso.boxes.fragment.SampleFlags"), 313);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setFirstSampleFlags", "com.coremedia.iso.boxes.fragment.TrackRunBox", "com.coremedia.iso.boxes.fragment.SampleFlags", "firstSampleFlags", "", "void"), 317);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "java.lang.String"), 327);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.fragment.TrackRunBox", "java.util.List", "entries", "", "void"), 342);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getSampleCompositionTimeOffsets", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "[J"), 129);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getSampleCount", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "long"), 238);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isDataOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 242);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isFirstSampleFlagsPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 246);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isSampleSizePresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 251);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isSampleDurationPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 255);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isSampleFlagsPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 259);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isSampleCompositionTimeOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 263);
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public static class Entry {
        /* access modifiers changed from: private */
        public long sampleCompositionTimeOffset;
        /* access modifiers changed from: private */
        public long sampleDuration;
        /* access modifiers changed from: private */
        public SampleFlags sampleFlags;
        /* access modifiers changed from: private */
        public long sampleSize;

        public Entry() {
        }

        public Entry(long j, long j2, SampleFlags sampleFlags2, int i) {
            this.sampleDuration = j;
            this.sampleSize = j2;
            this.sampleFlags = sampleFlags2;
            this.sampleCompositionTimeOffset = (long) i;
        }

        public long getSampleDuration() {
            return this.sampleDuration;
        }

        public long getSampleSize() {
            return this.sampleSize;
        }

        public SampleFlags getSampleFlags() {
            return this.sampleFlags;
        }

        public long getSampleCompositionTimeOffset() {
            return this.sampleCompositionTimeOffset;
        }

        public void setSampleDuration(long j) {
            this.sampleDuration = j;
        }

        public void setSampleSize(long j) {
            this.sampleSize = j;
        }

        public void setSampleFlags(SampleFlags sampleFlags2) {
            this.sampleFlags = sampleFlags2;
        }

        public void setSampleCompositionTimeOffset(int i) {
            this.sampleCompositionTimeOffset = (long) i;
        }

        public String toString() {
            return "Entry{duration=" + this.sampleDuration + ", size=" + this.sampleSize + ", dlags=" + this.sampleFlags + ", compTimeOffset=" + this.sampleCompositionTimeOffset + '}';
        }
    }

    public void setDataOffset(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(i)));
        if (i == -1) {
            setFlags(getFlags() & 16777214);
        } else {
            setFlags(getFlags() | 1);
        }
        this.dataOffset = i;
    }

    public long[] getSampleCompositionTimeOffsets() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        if (!isSampleCompositionTimeOffsetPresent()) {
            return null;
        }
        long[] jArr = new long[this.entries.size()];
        for (int i = 0; i < jArr.length; i++) {
            jArr[i] = this.entries.get(i).getSampleCompositionTimeOffset();
        }
        return jArr;
    }

    public TrackRunBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        int flags = getFlags();
        long j = (flags & 1) == 1 ? 12 : 8;
        if ((flags & 4) == 4) {
            j += 4;
        }
        long j2 = 0;
        if ((flags & 256) == 256) {
            j2 = 4;
        }
        if ((flags & 512) == 512) {
            j2 += 4;
        }
        if ((flags & 1024) == 1024) {
            j2 += 4;
        }
        if ((flags & 2048) == 2048) {
            j2 += 4;
        }
        return j + (j2 * ((long) this.entries.size()));
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        int flags = getFlags();
        if ((flags & 1) == 1) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) this.dataOffset);
        }
        if ((flags & 4) == 4) {
            this.firstSampleFlags.getContent(byteBuffer);
        }
        for (Entry next : this.entries) {
            if ((flags & 256) == 256) {
                IsoTypeWriter.writeUInt32(byteBuffer, next.sampleDuration);
            }
            if ((flags & 512) == 512) {
                IsoTypeWriter.writeUInt32(byteBuffer, next.sampleSize);
            }
            if ((flags & 1024) == 1024) {
                next.sampleFlags.getContent(byteBuffer);
            }
            if ((flags & 2048) == 2048) {
                if (getVersion() == 0) {
                    IsoTypeWriter.writeUInt32(byteBuffer, next.sampleCompositionTimeOffset);
                } else {
                    byteBuffer.putInt((int) next.sampleCompositionTimeOffset);
                }
            }
        }
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
        if ((getFlags() & 1) == 1) {
            this.dataOffset = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        } else {
            this.dataOffset = -1;
        }
        if ((getFlags() & 4) == 4) {
            this.firstSampleFlags = new SampleFlags(byteBuffer);
        }
        for (int i = 0; ((long) i) < readUInt32; i++) {
            Entry entry = new Entry();
            if ((getFlags() & 256) == 256) {
                entry.sampleDuration = IsoTypeReader.readUInt32(byteBuffer);
            }
            if ((getFlags() & 512) == 512) {
                entry.sampleSize = IsoTypeReader.readUInt32(byteBuffer);
            }
            if ((getFlags() & 1024) == 1024) {
                entry.sampleFlags = new SampleFlags(byteBuffer);
            }
            if ((getFlags() & 2048) == 2048) {
                entry.sampleCompositionTimeOffset = (long) byteBuffer.getInt();
            }
            this.entries.add(entry);
        }
    }

    public long getSampleCount() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return (long) this.entries.size();
    }

    public boolean isDataOffsetPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return (getFlags() & 1) == 1;
    }

    public boolean isFirstSampleFlagsPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return (getFlags() & 4) == 4;
    }

    public boolean isSampleSizePresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return (getFlags() & 512) == 512;
    }

    public boolean isSampleDurationPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return (getFlags() & 256) == 256;
    }

    public boolean isSampleFlagsPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return (getFlags() & 1024) == 1024;
    }

    public boolean isSampleCompositionTimeOffsetPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return (getFlags() & 2048) == 2048;
    }

    public void setDataOffsetPresent(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 1);
        } else {
            setFlags(getFlags() & 16777214);
        }
    }

    public void setSampleSizePresent(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 512);
        } else {
            setFlags(getFlags() & 16776703);
        }
    }

    public void setSampleDurationPresent(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 256);
        } else {
            setFlags(getFlags() & 16776959);
        }
    }

    public void setSampleFlagsPresent(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 1024);
        } else {
            setFlags(getFlags() & 16776191);
        }
    }

    public void setSampleCompositionTimeOffsetPresent(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 2048);
        } else {
            setFlags(getFlags() & 16775167);
        }
    }

    public int getDataOffset() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, this, this));
        return this.dataOffset;
    }

    public SampleFlags getFirstSampleFlags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, this, this));
        return this.firstSampleFlags;
    }

    public void setFirstSampleFlags(SampleFlags sampleFlags) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, (Object) sampleFlags));
        if (sampleFlags == null) {
            setFlags(getFlags() & 16777211);
        } else {
            setFlags(getFlags() | 4);
        }
        this.firstSampleFlags = sampleFlags;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, this, this));
        return "TrackRunBox" + "{sampleCount=" + this.entries.size() + ", dataOffset=" + this.dataOffset + ", dataOffsetPresent=" + isDataOffsetPresent() + ", sampleSizePresent=" + isSampleSizePresent() + ", sampleDurationPresent=" + isSampleDurationPresent() + ", sampleFlagsPresentPresent=" + isSampleFlagsPresent() + ", sampleCompositionTimeOffsetPresent=" + isSampleCompositionTimeOffsetPresent() + ", firstSampleFlags=" + this.firstSampleFlags + '}';
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }
}
