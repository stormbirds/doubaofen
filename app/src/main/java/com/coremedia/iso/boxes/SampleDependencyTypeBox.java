package com.coremedia.iso.boxes;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleDependencyTypeBox extends AbstractFullBox {
    public static final String TYPE = "sdtp";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private List<Entry> entries = new ArrayList();

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SampleDependencyTypeBox.java", SampleDependencyTypeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.SampleDependencyTypeBox", "", "", "", "java.util.List"), 139);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.SampleDependencyTypeBox", "java.util.List", "entries", "", "void"), 143);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SampleDependencyTypeBox", "", "", "", "java.lang.String"), (int) Opcodes.LCMP);
    }

    public static class Entry {
        /* access modifiers changed from: private */
        public int value;

        public Entry(int i) {
            this.value = i;
        }

        public int getIsLeading() {
            return (this.value >> 6) & 3;
        }

        public void setIsLeading(int i) {
            this.value = ((i & 3) << 6) | (this.value & 63);
        }

        public int getSampleDependsOn() {
            return (this.value >> 4) & 3;
        }

        public void setSampleDependsOn(int i) {
            this.value = ((i & 3) << 4) | (this.value & 207);
        }

        public int getSampleIsDependentOn() {
            return (this.value >> 2) & 3;
        }

        public void setSampleIsDependentOn(int i) {
            this.value = ((i & 3) << 2) | (this.value & 243);
        }

        public int getSampleHasRedundancy() {
            return this.value & 3;
        }

        public void setSampleHasRedundancy(int i) {
            this.value = (i & 3) | (this.value & 252);
        }

        public String toString() {
            return "Entry{isLeading=" + getIsLeading() + ", sampleDependsOn=" + getSampleDependsOn() + ", sampleIsDependentOn=" + getSampleIsDependentOn() + ", sampleHasRedundancy=" + getSampleHasRedundancy() + '}';
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.value == ((Entry) obj).value;
        }

        public int hashCode() {
            return this.value;
        }
    }

    public SampleDependencyTypeBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (this.entries.size() + 4);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        for (Entry access$0 : this.entries) {
            IsoTypeWriter.writeUInt8(byteBuffer, access$0.value);
        }
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        while (byteBuffer.remaining() > 0) {
            this.entries.add(new Entry(IsoTypeReader.readUInt8(byteBuffer)));
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

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "SampleDependencyTypeBox" + "{entries=" + this.entries + '}';
    }
}
