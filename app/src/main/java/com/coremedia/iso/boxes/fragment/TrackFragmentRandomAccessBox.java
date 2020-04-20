package com.coremedia.iso.boxes.fragment;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoTypeWriterVariable;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackFragmentRandomAccessBox extends AbstractFullBox {
    public static final String TYPE = "tfra";
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
    private List<Entry> entries = Collections.emptyList();
    private int lengthSizeOfSampleNum = 2;
    private int lengthSizeOfTrafNum = 2;
    private int lengthSizeOfTrunNum = 2;
    private int reserved;
    private long trackId;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("TrackFragmentRandomAccessBox.java", TrackFragmentRandomAccessBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setTrackId", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "long", "trackId", "", "void"), 145);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setLengthSizeOfTrafNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "int", "lengthSizeOfTrafNum", "", "void"), (int) Opcodes.FCMPL);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "java.util.List"), (int) Opcodes.INVOKEINTERFACE);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "java.util.List", "entries", "", "void"), 189);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "java.lang.String"), 290);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setLengthSizeOfTrunNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "int", "lengthSizeOfTrunNum", "", "void"), (int) Opcodes.IFEQ);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setLengthSizeOfSampleNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "int", "lengthSizeOfSampleNum", "", "void"), 157);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getTrackId", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "long"), 161);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getReserved", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) Opcodes.IF_ACMPEQ);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getLengthSizeOfTrafNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) Opcodes.RET);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getLengthSizeOfTrunNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), 173);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getLengthSizeOfSampleNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) Opcodes.RETURN);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getNumberOfEntries", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "long"), (int) Opcodes.PUTFIELD);
    }

    public TrackFragmentRandomAccessBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        int i;
        if (getVersion() == 1) {
            i = this.entries.size() * 16;
        } else {
            i = this.entries.size() * 8;
        }
        return 16 + ((long) i) + ((long) (this.lengthSizeOfTrafNum * this.entries.size())) + ((long) (this.lengthSizeOfTrunNum * this.entries.size())) + ((long) (this.lengthSizeOfSampleNum * this.entries.size()));
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.trackId = IsoTypeReader.readUInt32(byteBuffer);
        long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
        this.reserved = (int) (readUInt32 >> 6);
        this.lengthSizeOfTrafNum = (((int) (63 & readUInt32)) >> 4) + 1;
        this.lengthSizeOfTrunNum = (((int) (12 & readUInt32)) >> 2) + 1;
        this.lengthSizeOfSampleNum = ((int) (readUInt32 & 3)) + 1;
        long readUInt322 = IsoTypeReader.readUInt32(byteBuffer);
        this.entries = new ArrayList();
        for (int i = 0; ((long) i) < readUInt322; i++) {
            Entry entry = new Entry();
            if (getVersion() == 1) {
                entry.time = IsoTypeReader.readUInt64(byteBuffer);
                entry.moofOffset = IsoTypeReader.readUInt64(byteBuffer);
            } else {
                entry.time = IsoTypeReader.readUInt32(byteBuffer);
                entry.moofOffset = IsoTypeReader.readUInt32(byteBuffer);
            }
            entry.trafNumber = IsoTypeReaderVariable.read(byteBuffer, this.lengthSizeOfTrafNum);
            entry.trunNumber = IsoTypeReaderVariable.read(byteBuffer, this.lengthSizeOfTrunNum);
            entry.sampleNumber = IsoTypeReaderVariable.read(byteBuffer, this.lengthSizeOfSampleNum);
            this.entries.add(entry);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
        IsoTypeWriter.writeUInt32(byteBuffer, ((long) (this.reserved << 6)) | ((long) (((this.lengthSizeOfTrafNum - 1) & 3) << 4)) | ((long) (((this.lengthSizeOfTrunNum - 1) & 3) << 2)) | ((long) ((this.lengthSizeOfSampleNum - 1) & 3)));
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry next : this.entries) {
            if (getVersion() == 1) {
                IsoTypeWriter.writeUInt64(byteBuffer, next.time);
                IsoTypeWriter.writeUInt64(byteBuffer, next.moofOffset);
            } else {
                IsoTypeWriter.writeUInt32(byteBuffer, next.time);
                IsoTypeWriter.writeUInt32(byteBuffer, next.moofOffset);
            }
            IsoTypeWriterVariable.write(next.trafNumber, byteBuffer, this.lengthSizeOfTrafNum);
            IsoTypeWriterVariable.write(next.trunNumber, byteBuffer, this.lengthSizeOfTrunNum);
            IsoTypeWriterVariable.write(next.sampleNumber, byteBuffer, this.lengthSizeOfSampleNum);
        }
    }

    public void setTrackId(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, (Object) this, (Object) this, Conversions.longObject(j)));
        this.trackId = j;
    }

    public void setLengthSizeOfTrafNum(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(i)));
        this.lengthSizeOfTrafNum = i;
    }

    public void setLengthSizeOfTrunNum(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, Conversions.intObject(i)));
        this.lengthSizeOfTrunNum = i;
    }

    public void setLengthSizeOfSampleNum(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(i)));
        this.lengthSizeOfSampleNum = i;
    }

    public long getTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.trackId;
    }

    public int getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.reserved;
    }

    public int getLengthSizeOfTrafNum() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.lengthSizeOfTrafNum;
    }

    public int getLengthSizeOfTrunNum() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.lengthSizeOfTrunNum;
    }

    public int getLengthSizeOfSampleNum() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.lengthSizeOfSampleNum;
    }

    public long getNumberOfEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return (long) this.entries.size();
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return Collections.unmodifiableList(this.entries);
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    public static class Entry {
        /* access modifiers changed from: private */
        public long moofOffset;
        /* access modifiers changed from: private */
        public long sampleNumber;
        /* access modifiers changed from: private */
        public long time;
        /* access modifiers changed from: private */
        public long trafNumber;
        /* access modifiers changed from: private */
        public long trunNumber;

        public Entry() {
        }

        public Entry(long j, long j2, long j3, long j4, long j5) {
            this.moofOffset = j2;
            this.sampleNumber = j5;
            this.time = j;
            this.trafNumber = j3;
            this.trunNumber = j4;
        }

        public long getTime() {
            return this.time;
        }

        public long getMoofOffset() {
            return this.moofOffset;
        }

        public long getTrafNumber() {
            return this.trafNumber;
        }

        public long getTrunNumber() {
            return this.trunNumber;
        }

        public long getSampleNumber() {
            return this.sampleNumber;
        }

        public void setTime(long j) {
            this.time = j;
        }

        public void setMoofOffset(long j) {
            this.moofOffset = j;
        }

        public void setTrafNumber(long j) {
            this.trafNumber = j;
        }

        public void setTrunNumber(long j) {
            this.trunNumber = j;
        }

        public void setSampleNumber(long j) {
            this.sampleNumber = j;
        }

        public String toString() {
            return "Entry{time=" + this.time + ", moofOffset=" + this.moofOffset + ", trafNumber=" + this.trafNumber + ", trunNumber=" + this.trunNumber + ", sampleNumber=" + this.sampleNumber + '}';
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.moofOffset == entry.moofOffset && this.sampleNumber == entry.sampleNumber && this.time == entry.time && this.trafNumber == entry.trafNumber && this.trunNumber == entry.trunNumber;
        }

        public int hashCode() {
            long j = this.time;
            long j2 = this.moofOffset;
            long j3 = this.trafNumber;
            long j4 = this.trunNumber;
            long j5 = this.sampleNumber;
            return (((((((((int) (j ^ (j >>> 32))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 31) + ((int) (j4 ^ (j4 >>> 32)))) * 31) + ((int) (j5 ^ (j5 >>> 32)));
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return "TrackFragmentRandomAccessBox{trackId=" + this.trackId + ", entries=" + this.entries + '}';
    }
}
