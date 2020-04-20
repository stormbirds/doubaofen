package com.coremedia.iso.boxes.fragment;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SegmentTypeBox extends AbstractBox {
    public static final String TYPE = "styp";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private List<String> compatibleBrands = Collections.emptyList();
    private String majorBrand;
    private long minorVersion;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SegmentTypeBox.java", SegmentTypeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getMajorBrand", "com.coremedia.iso.boxes.fragment.SegmentTypeBox", "", "", "", "java.lang.String"), 85);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setMajorBrand", "com.coremedia.iso.boxes.fragment.SegmentTypeBox", "java.lang.String", "majorBrand", "", "void"), 94);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setMinorVersion", "com.coremedia.iso.boxes.fragment.SegmentTypeBox", "long", "minorVersion", "", "void"), 103);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getMinorVersion", "com.coremedia.iso.boxes.fragment.SegmentTypeBox", "", "", "", "long"), 113);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getCompatibleBrands", "com.coremedia.iso.boxes.fragment.SegmentTypeBox", "", "", "", "java.util.List"), 122);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setCompatibleBrands", "com.coremedia.iso.boxes.fragment.SegmentTypeBox", "java.util.List", "compatibleBrands", "", "void"), (int) Opcodes.IAND);
    }

    public SegmentTypeBox() {
        super(TYPE);
    }

    public SegmentTypeBox(String str, long j, List<String> list) {
        super(TYPE);
        this.majorBrand = str;
        this.minorVersion = j;
        this.compatibleBrands = list;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.compatibleBrands.size() * 4) + 8);
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        this.majorBrand = IsoTypeReader.read4cc(byteBuffer);
        this.minorVersion = IsoTypeReader.readUInt32(byteBuffer);
        int remaining = byteBuffer.remaining() / 4;
        this.compatibleBrands = new LinkedList();
        for (int i = 0; i < remaining; i++) {
            this.compatibleBrands.add(IsoTypeReader.read4cc(byteBuffer));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(IsoFile.fourCCtoBytes(this.majorBrand));
        IsoTypeWriter.writeUInt32(byteBuffer, this.minorVersion);
        for (String fourCCtoBytes : this.compatibleBrands) {
            byteBuffer.put(IsoFile.fourCCtoBytes(fourCCtoBytes));
        }
    }

    public String getMajorBrand() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.majorBrand;
    }

    public void setMajorBrand(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) str));
        this.majorBrand = str;
    }

    public void setMinorVersion(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, Conversions.longObject(j)));
        this.minorVersion = j;
    }

    public long getMinorVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.minorVersion;
    }

    public List<String> getCompatibleBrands() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.compatibleBrands;
    }

    public void setCompatibleBrands(List<String> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) list));
        this.compatibleBrands = list;
    }

    @DoNotParseDetail
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SegmentTypeBox[");
        sb.append("majorBrand=");
        sb.append(getMajorBrand());
        sb.append(";");
        sb.append("minorVersion=");
        sb.append(getMinorVersion());
        for (String append : this.compatibleBrands) {
            sb.append(";");
            sb.append("compatibleBrand=");
            sb.append(append);
        }
        sb.append("]");
        return sb.toString();
    }
}
