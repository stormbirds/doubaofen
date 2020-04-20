package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import com.mp4parser.iso14496.part15.StepwiseTemporalLayerEntry;
import com.mp4parser.iso14496.part15.SyncSampleEntry;
import com.mp4parser.iso14496.part15.TemporalLayerSampleGroup;
import com.mp4parser.iso14496.part15.TemporalSubLayerSampleGroup;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleGroupDescriptionBox extends AbstractFullBox {
    public static final String TYPE = "sgpd";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8 = null;
    private int defaultLength;
    private List<GroupEntry> groupEntries = new LinkedList();
    private String groupingType;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SampleGroupDescriptionBox.java", SampleGroupDescriptionBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "java.lang.String"), 57);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "java.lang.String", "groupingType", "", "void"), 61);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getDefaultLength", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "int"), (int) Opcodes.IFEQ);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setDefaultLength", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "int", "defaultLength", "", "void"), 157);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getGroupEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "java.util.List"), 161);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setGroupEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "java.util.List", "groupEntries", "", "void"), (int) Opcodes.IF_ACMPEQ);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "equals", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "java.lang.Object", "o", "", "boolean"), 170);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "hashCode", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "int"), 191);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "java.lang.String"), (int) Opcodes.IFNONNULL);
    }

    public SampleGroupDescriptionBox() {
        super(TYPE);
        setVersion(1);
    }

    public String getGroupingType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.groupingType;
    }

    public void setGroupingType(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) str));
        this.groupingType = str;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long j = (getVersion() == 1 ? 12 : 8) + 4;
        for (GroupEntry next : this.groupEntries) {
            if (getVersion() == 1 && this.defaultLength == 0) {
                j += 4;
            }
            j += (long) next.size();
        }
        return j;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(IsoFile.fourCCtoBytes(this.groupingType));
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) this.defaultLength);
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.groupEntries.size());
        for (GroupEntry next : this.groupEntries) {
            if (getVersion() == 1 && this.defaultLength == 0) {
                IsoTypeWriter.writeUInt32(byteBuffer, (long) next.get().limit());
            }
            byteBuffer.put(next.get());
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            this.groupingType = IsoTypeReader.read4cc(byteBuffer);
            if (getVersion() == 1) {
                this.defaultLength = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
            }
            long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
            while (true) {
                long j = readUInt32 - 1;
                if (readUInt32 > 0) {
                    int i = this.defaultLength;
                    if (getVersion() == 1) {
                        if (this.defaultLength == 0) {
                            i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
                        }
                        int position = byteBuffer.position() + i;
                        ByteBuffer slice = byteBuffer.slice();
                        slice.limit(i);
                        this.groupEntries.add(parseGroupEntry(slice, this.groupingType));
                        byteBuffer.position(position);
                        readUInt32 = j;
                    } else {
                        throw new RuntimeException("This should be implemented");
                    }
                } else {
                    return;
                }
            }
        } else {
            throw new RuntimeException("SampleGroupDescriptionBox are only supported in version 1");
        }
    }

    private GroupEntry parseGroupEntry(ByteBuffer byteBuffer, String str) {
        GroupEntry groupEntry;
        if (RollRecoveryEntry.TYPE.equals(str)) {
            groupEntry = new RollRecoveryEntry();
        } else if (RateShareEntry.TYPE.equals(str)) {
            groupEntry = new RateShareEntry();
        } else if (CencSampleEncryptionInformationGroupEntry.TYPE.equals(str)) {
            groupEntry = new CencSampleEncryptionInformationGroupEntry();
        } else if (VisualRandomAccessEntry.TYPE.equals(str)) {
            groupEntry = new VisualRandomAccessEntry();
        } else if (TemporalLevelEntry.TYPE.equals(str)) {
            groupEntry = new TemporalLevelEntry();
        } else if (SyncSampleEntry.TYPE.equals(str)) {
            groupEntry = new SyncSampleEntry();
        } else if (TemporalLayerSampleGroup.TYPE.equals(str)) {
            groupEntry = new TemporalLayerSampleGroup();
        } else if (TemporalSubLayerSampleGroup.TYPE.equals(str)) {
            groupEntry = new TemporalSubLayerSampleGroup();
        } else if (StepwiseTemporalLayerEntry.TYPE.equals(str)) {
            groupEntry = new StepwiseTemporalLayerEntry();
        } else {
            groupEntry = new UnknownEntry(str);
        }
        groupEntry.parse(byteBuffer);
        return groupEntry;
    }

    public int getDefaultLength() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.defaultLength;
    }

    public void setDefaultLength(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(i)));
        this.defaultLength = i;
    }

    public List<GroupEntry> getGroupEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.groupEntries;
    }

    public void setGroupEntries(List<GroupEntry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) list));
        this.groupEntries = list;
    }

    public boolean equals(Object obj) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, obj));
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SampleGroupDescriptionBox sampleGroupDescriptionBox = (SampleGroupDescriptionBox) obj;
        if (this.defaultLength != sampleGroupDescriptionBox.defaultLength) {
            return false;
        }
        List<GroupEntry> list = this.groupEntries;
        List<GroupEntry> list2 = sampleGroupDescriptionBox.groupEntries;
        return list == null ? list2 == null : list.equals(list2);
    }

    public int hashCode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        int i = 0;
        int i2 = (this.defaultLength + 0) * 31;
        List<GroupEntry> list = this.groupEntries;
        if (list != null) {
            i = list.hashCode();
        }
        return i2 + i;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        StringBuilder sb = new StringBuilder("SampleGroupDescriptionBox{groupingType='");
        sb.append(this.groupEntries.size() > 0 ? this.groupEntries.get(0).getType() : "????");
        sb.append('\'');
        sb.append(", defaultLength=");
        sb.append(this.defaultLength);
        sb.append(", groupEntries=");
        sb.append(this.groupEntries);
        sb.append('}');
        return sb.toString();
    }
}
