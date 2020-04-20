package com.googlecode.mp4parser.boxes.dece;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AssetInformationBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE = "ainf";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    String apid = "";
    String profileVersion = "0000";

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("AssetInformationBox.java", AssetInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getApid", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "", "", "", "java.lang.String"), 131);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setApid", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "java.lang.String", "apid", "", "void"), 135);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getProfileVersion", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "", "", "", "java.lang.String"), 139);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setProfileVersion", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "java.lang.String", "profileVersion", "", "void"), 143);
    }

    public static class Entry {
        public String assetId;
        public String namespace;
        public String profileLevelIdc;

        public Entry(String str, String str2, String str3) {
            this.namespace = str;
            this.profileLevelIdc = str2;
            this.assetId = str3;
        }

        public String toString() {
            return "{namespace='" + this.namespace + '\'' + ", profileLevelIdc='" + this.profileLevelIdc + '\'' + ", assetId='" + this.assetId + '\'' + '}';
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.assetId.equals(entry.assetId) && this.namespace.equals(entry.namespace) && this.profileLevelIdc.equals(entry.profileLevelIdc);
        }

        public int hashCode() {
            return (((this.namespace.hashCode() * 31) + this.profileLevelIdc.hashCode()) * 31) + this.assetId.hashCode();
        }

        public int getSize() {
            return Utf8.utf8StringLengthInBytes(this.namespace) + 3 + Utf8.utf8StringLengthInBytes(this.profileLevelIdc) + Utf8.utf8StringLengthInBytes(this.assetId);
        }
    }

    public AssetInformationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.apid) + 9);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 0) {
            byteBuffer.put(Utf8.convert(this.profileVersion), 0, 4);
            byteBuffer.put(Utf8.convert(this.apid));
            byteBuffer.put((byte) 0);
            return;
        }
        throw new RuntimeException("Unknown ainf version " + getVersion());
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.profileVersion = IsoTypeReader.readString(byteBuffer, 4);
        this.apid = IsoTypeReader.readString(byteBuffer);
    }

    public String getApid() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.apid;
    }

    public void setApid(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) str));
        this.apid = str;
    }

    public String getProfileVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.profileVersion;
    }

    public void setProfileVersion(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) str));
        this.profileVersion = str;
    }

    @DoNotParseDetail
    public boolean isHidden() {
        return (getFlags() & 1) == 1;
    }

    @DoNotParseDetail
    public void setHidden(boolean z) {
        int flags = getFlags();
        if (!(isHidden() ^ z)) {
            return;
        }
        if (z) {
            setFlags(flags | 1);
        } else {
            setFlags(16777214 & flags);
        }
    }
}
