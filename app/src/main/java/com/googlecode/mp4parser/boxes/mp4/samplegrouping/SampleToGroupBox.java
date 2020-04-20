package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleToGroupBox extends AbstractFullBox {
    public static final String TYPE = "sbgp";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    List<Entry> entries = new LinkedList();
    private String groupingType;
    private String groupingTypeParameter;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SampleToGroupBox.java", SampleToGroupBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "", "", "", "java.lang.String"), 150);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "java.lang.String", "groupingType", "", "void"), (int) Opcodes.IFNE);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getGroupingTypeParameter", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "", "", "", "java.lang.String"), (int) Opcodes.IFLE);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setGroupingTypeParameter", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "java.lang.String", "groupingTypeParameter", "", "void"), (int) Opcodes.IF_ICMPGE);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "", "", "", "java.util.List"), 166);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "java.util.List", "entries", "", "void"), 170);
    }

    public SampleToGroupBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (getVersion() == 1 ? (this.entries.size() * 8) + 16 : (this.entries.size() * 8) + 12);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(this.groupingType.getBytes());
        if (getVersion() == 1) {
            byteBuffer.put(this.groupingTypeParameter.getBytes());
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry next : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, next.getSampleCount());
            IsoTypeWriter.writeUInt32(byteBuffer, (long) next.getGroupDescriptionIndex());
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.groupingType = IsoTypeReader.read4cc(byteBuffer);
        if (getVersion() == 1) {
            this.groupingTypeParameter = IsoTypeReader.read4cc(byteBuffer);
        }
        long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
        while (true) {
            long j = readUInt32 - 1;
            if (readUInt32 > 0) {
                this.entries.add(new Entry((long) CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer)), CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer))));
                readUInt32 = j;
            } else {
                return;
            }
        }
    }

    public static class Entry {
        private int groupDescriptionIndex;
        private long sampleCount;

        public Entry(long j, int i) {
            this.sampleCount = j;
            this.groupDescriptionIndex = i;
        }

        public long getSampleCount() {
            return this.sampleCount;
        }

        public void setSampleCount(long j) {
            this.sampleCount = j;
        }

        public int getGroupDescriptionIndex() {
            return this.groupDescriptionIndex;
        }

        public void setGroupDescriptionIndex(int i) {
            this.groupDescriptionIndex = i;
        }

        public String toString() {
            return "Entry{sampleCount=" + this.sampleCount + ", groupDescriptionIndex=" + this.groupDescriptionIndex + '}';
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.groupDescriptionIndex == entry.groupDescriptionIndex && this.sampleCount == entry.sampleCount;
        }

        public int hashCode() {
            long j = this.sampleCount;
            return (((int) (j ^ (j >>> 32))) * 31) + this.groupDescriptionIndex;
        }
    }

    public String getGroupingType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.groupingType;
    }

    public void setGroupingType(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) str));
        this.groupingType = str;
    }

    public String getGroupingTypeParameter() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.groupingTypeParameter;
    }

    public void setGroupingTypeParameter(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) str));
        this.groupingTypeParameter = str;
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }
}
