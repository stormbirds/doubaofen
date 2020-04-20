package com.googlecode.mp4parser.boxes.threegpp26244;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SegmentIndexBox extends AbstractFullBox {
    public static final String TYPE = "sidx";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9 = null;
    long earliestPresentationTime;
    List<Entry> entries = new ArrayList();
    long firstOffset;
    long referenceId;
    int reserved;
    long timeScale;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SegmentIndexBox.java", SegmentIndexBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "java.util.List"), 128);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "java.util.List", "entries", "", "void"), 132);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getReserved", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "int"), 168);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setReserved", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "int", "reserved", "", "void"), 172);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "java.lang.String"), 298);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getReferenceId", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 136);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setReferenceId", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "referenceId", "", "void"), 140);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getTimeScale", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 144);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setTimeScale", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "timeScale", "", "void"), (int) Opcodes.LCMP);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEarliestPresentationTime", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 152);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEarliestPresentationTime", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "earliestPresentationTime", "", "void"), 156);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getFirstOffset", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 160);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setFirstOffset", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "firstOffset", "", "void"), 164);
    }

    public SegmentIndexBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 12 + ((long) (getVersion() == 0 ? 8 : 16)) + 2 + 2 + ((long) (this.entries.size() * 12));
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.referenceId);
        IsoTypeWriter.writeUInt32(byteBuffer, this.timeScale);
        if (getVersion() == 0) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.earliestPresentationTime);
            IsoTypeWriter.writeUInt32(byteBuffer, this.firstOffset);
        } else {
            IsoTypeWriter.writeUInt64(byteBuffer, this.earliestPresentationTime);
            IsoTypeWriter.writeUInt64(byteBuffer, this.firstOffset);
        }
        IsoTypeWriter.writeUInt16(byteBuffer, this.reserved);
        IsoTypeWriter.writeUInt16(byteBuffer, this.entries.size());
        for (Entry next : this.entries) {
            BitWriterBuffer bitWriterBuffer = new BitWriterBuffer(byteBuffer);
            bitWriterBuffer.writeBits(next.getReferenceType(), 1);
            bitWriterBuffer.writeBits(next.getReferencedSize(), 31);
            IsoTypeWriter.writeUInt32(byteBuffer, next.getSubsegmentDuration());
            BitWriterBuffer bitWriterBuffer2 = new BitWriterBuffer(byteBuffer);
            bitWriterBuffer2.writeBits(next.getStartsWithSap(), 1);
            bitWriterBuffer2.writeBits(next.getSapType(), 3);
            bitWriterBuffer2.writeBits(next.getSapDeltaTime(), 28);
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.referenceId = IsoTypeReader.readUInt32(byteBuffer);
        this.timeScale = IsoTypeReader.readUInt32(byteBuffer);
        if (getVersion() == 0) {
            this.earliestPresentationTime = IsoTypeReader.readUInt32(byteBuffer);
            this.firstOffset = IsoTypeReader.readUInt32(byteBuffer);
        } else {
            this.earliestPresentationTime = IsoTypeReader.readUInt64(byteBuffer);
            this.firstOffset = IsoTypeReader.readUInt64(byteBuffer);
        }
        this.reserved = IsoTypeReader.readUInt16(byteBuffer);
        int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
        for (int i = 0; i < readUInt16; i++) {
            BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(byteBuffer);
            Entry entry = new Entry();
            entry.setReferenceType((byte) bitReaderBuffer.readBits(1));
            entry.setReferencedSize(bitReaderBuffer.readBits(31));
            entry.setSubsegmentDuration(IsoTypeReader.readUInt32(byteBuffer));
            BitReaderBuffer bitReaderBuffer2 = new BitReaderBuffer(byteBuffer);
            entry.setStartsWithSap((byte) bitReaderBuffer2.readBits(1));
            entry.setSapType((byte) bitReaderBuffer2.readBits(3));
            entry.setSapDeltaTime(bitReaderBuffer2.readBits(28));
            this.entries.add(entry);
        }
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    public long getReferenceId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.referenceId;
    }

    public void setReferenceId(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.longObject(j)));
        this.referenceId = j;
    }

    public long getTimeScale() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.timeScale;
    }

    public void setTimeScale(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.longObject(j)));
        this.timeScale = j;
    }

    public long getEarliestPresentationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.earliestPresentationTime;
    }

    public void setEarliestPresentationTime(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.longObject(j)));
        this.earliestPresentationTime = j;
    }

    public long getFirstOffset() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.firstOffset;
    }

    public void setFirstOffset(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.longObject(j)));
        this.firstOffset = j;
    }

    public int getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.reserved;
    }

    public void setReserved(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.intObject(i)));
        this.reserved = i;
    }

    public static class Entry {
        byte referenceType;
        int referencedSize;
        int sapDeltaTime;
        byte sapType;
        byte startsWithSap;
        long subsegmentDuration;

        public Entry() {
        }

        public Entry(int i, int i2, long j, boolean z, int i3, int i4) {
            this.referenceType = (byte) i;
            this.referencedSize = i2;
            this.subsegmentDuration = j;
            this.startsWithSap = z ? (byte) 1 : 0;
            this.sapType = (byte) i3;
            this.sapDeltaTime = i4;
        }

        public byte getReferenceType() {
            return this.referenceType;
        }

        public void setReferenceType(byte b) {
            this.referenceType = b;
        }

        public int getReferencedSize() {
            return this.referencedSize;
        }

        public void setReferencedSize(int i) {
            this.referencedSize = i;
        }

        public long getSubsegmentDuration() {
            return this.subsegmentDuration;
        }

        public void setSubsegmentDuration(long j) {
            this.subsegmentDuration = j;
        }

        public byte getStartsWithSap() {
            return this.startsWithSap;
        }

        public void setStartsWithSap(byte b) {
            this.startsWithSap = b;
        }

        public byte getSapType() {
            return this.sapType;
        }

        public void setSapType(byte b) {
            this.sapType = b;
        }

        public int getSapDeltaTime() {
            return this.sapDeltaTime;
        }

        public void setSapDeltaTime(int i) {
            this.sapDeltaTime = i;
        }

        public String toString() {
            return "Entry{referenceType=" + this.referenceType + ", referencedSize=" + this.referencedSize + ", subsegmentDuration=" + this.subsegmentDuration + ", startsWithSap=" + this.startsWithSap + ", sapType=" + this.sapType + ", sapDeltaTime=" + this.sapDeltaTime + '}';
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.referenceType == entry.referenceType && this.referencedSize == entry.referencedSize && this.sapDeltaTime == entry.sapDeltaTime && this.sapType == entry.sapType && this.startsWithSap == entry.startsWithSap && this.subsegmentDuration == entry.subsegmentDuration;
        }

        public int hashCode() {
            long j = this.subsegmentDuration;
            return (((((((((this.referenceType * 31) + this.referencedSize) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + this.startsWithSap) * 31) + this.sapType) * 31) + this.sapDeltaTime;
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return "SegmentIndexBox{entries=" + this.entries + ", referenceId=" + this.referenceId + ", timeScale=" + this.timeScale + ", earliestPresentationTime=" + this.earliestPresentationTime + ", firstOffset=" + this.firstOffset + ", reserved=" + this.reserved + '}';
    }
}
