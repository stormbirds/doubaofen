package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SubSampleInformationBox extends AbstractFullBox {
    public static final String TYPE = "subs";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private List<SubSampleEntry> entries = new ArrayList();

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SubSampleInformationBox.java", SubSampleInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.SubSampleInformationBox", "", "", "", "java.util.List"), 50);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.SubSampleInformationBox", "java.util.List", "entries", "", "void"), 54);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SubSampleInformationBox", "", "", "", "java.lang.String"), 124);
    }

    public SubSampleInformationBox() {
        super(TYPE);
    }

    public List<SubSampleEntry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<SubSampleEntry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long j = 8;
        for (SubSampleEntry next : this.entries) {
            j = j + 4 + 2;
            for (int i = 0; i < next.getSubsampleEntries().size(); i++) {
                j = (getVersion() == 1 ? j + 4 : j + 2) + 2 + 4;
            }
        }
        return j;
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
        for (int i = 0; ((long) i) < readUInt32; i++) {
            SubSampleEntry subSampleEntry = new SubSampleEntry();
            subSampleEntry.setSampleDelta(IsoTypeReader.readUInt32(byteBuffer));
            int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
            for (int i2 = 0; i2 < readUInt16; i2++) {
                SubSampleEntry.SubsampleEntry subsampleEntry = new SubSampleEntry.SubsampleEntry();
                subsampleEntry.setSubsampleSize(getVersion() == 1 ? IsoTypeReader.readUInt32(byteBuffer) : (long) IsoTypeReader.readUInt16(byteBuffer));
                subsampleEntry.setSubsamplePriority(IsoTypeReader.readUInt8(byteBuffer));
                subsampleEntry.setDiscardable(IsoTypeReader.readUInt8(byteBuffer));
                subsampleEntry.setReserved(IsoTypeReader.readUInt32(byteBuffer));
                subSampleEntry.getSubsampleEntries().add(subsampleEntry);
            }
            this.entries.add(subSampleEntry);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (SubSampleEntry next : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, next.getSampleDelta());
            IsoTypeWriter.writeUInt16(byteBuffer, next.getSubsampleCount());
            for (SubSampleEntry.SubsampleEntry next2 : next.getSubsampleEntries()) {
                if (getVersion() == 1) {
                    IsoTypeWriter.writeUInt32(byteBuffer, next2.getSubsampleSize());
                } else {
                    IsoTypeWriter.writeUInt16(byteBuffer, CastUtils.l2i(next2.getSubsampleSize()));
                }
                IsoTypeWriter.writeUInt8(byteBuffer, next2.getSubsamplePriority());
                IsoTypeWriter.writeUInt8(byteBuffer, next2.getDiscardable());
                IsoTypeWriter.writeUInt32(byteBuffer, next2.getReserved());
            }
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "SubSampleInformationBox{entryCount=" + this.entries.size() + ", entries=" + this.entries + '}';
    }

    public static class SubSampleEntry {
        private long sampleDelta;
        private List<SubsampleEntry> subsampleEntries = new ArrayList();

        public long getSampleDelta() {
            return this.sampleDelta;
        }

        public void setSampleDelta(long j) {
            this.sampleDelta = j;
        }

        public int getSubsampleCount() {
            return this.subsampleEntries.size();
        }

        public List<SubsampleEntry> getSubsampleEntries() {
            return this.subsampleEntries;
        }

        public static class SubsampleEntry {
            private int discardable;
            private long reserved;
            private int subsamplePriority;
            private long subsampleSize;

            public long getSubsampleSize() {
                return this.subsampleSize;
            }

            public void setSubsampleSize(long j) {
                this.subsampleSize = j;
            }

            public int getSubsamplePriority() {
                return this.subsamplePriority;
            }

            public void setSubsamplePriority(int i) {
                this.subsamplePriority = i;
            }

            public int getDiscardable() {
                return this.discardable;
            }

            public void setDiscardable(int i) {
                this.discardable = i;
            }

            public long getReserved() {
                return this.reserved;
            }

            public void setReserved(long j) {
                this.reserved = j;
            }

            public String toString() {
                return "SubsampleEntry{subsampleSize=" + this.subsampleSize + ", subsamplePriority=" + this.subsamplePriority + ", discardable=" + this.discardable + ", reserved=" + this.reserved + '}';
            }
        }

        public String toString() {
            return "SampleEntry{sampleDelta=" + this.sampleDelta + ", subsampleCount=" + this.subsampleEntries.size() + ", subsampleEntries=" + this.subsampleEntries + '}';
        }
    }
}
