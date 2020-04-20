package com.googlecode.mp4parser.boxes.dece;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ContentInformationBox extends AbstractFullBox {
    public static final String TYPE = "cinf";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9 = null;
    Map<String, String> brandEntries = new LinkedHashMap();
    String codecs;
    Map<String, String> idEntries = new LinkedHashMap();
    String languages;
    String mimeSubtypeName;
    String profileLevelIdc;
    String protection;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("ContentInformationBox.java", ContentInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getMimeSubtypeName", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 144);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setMimeSubtypeName", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "mimeSubtypeName", "", "void"), (int) Opcodes.LCMP);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getBrandEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.util.Map"), (int) Opcodes.INVOKESTATIC);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setBrandEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.util.Map", "brandEntries", "", "void"), 188);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getIdEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.util.Map"), (int) Opcodes.CHECKCAST);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setIdEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.util.Map", "idEntries", "", "void"), 196);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getProfileLevelIdc", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 152);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setProfileLevelIdc", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "profileLevelIdc", "", "void"), 156);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getCodecs", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 160);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setCodecs", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "codecs", "", "void"), 164);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getProtection", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 168);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setProtection", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "protection", "", "void"), 172);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getLanguages", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), (int) Opcodes.ARETURN);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setLanguages", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "languages", "", "void"), (int) Opcodes.GETFIELD);
    }

    public ContentInformationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long utf8StringLengthInBytes = ((long) (Utf8.utf8StringLengthInBytes(this.mimeSubtypeName) + 1)) + 4 + ((long) (Utf8.utf8StringLengthInBytes(this.profileLevelIdc) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.codecs) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.protection) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.languages) + 1)) + 1;
        for (Map.Entry next : this.brandEntries.entrySet()) {
            utf8StringLengthInBytes = utf8StringLengthInBytes + ((long) (Utf8.utf8StringLengthInBytes((String) next.getKey()) + 1)) + ((long) (Utf8.utf8StringLengthInBytes((String) next.getValue()) + 1));
        }
        long j = utf8StringLengthInBytes + 1;
        for (Map.Entry next2 : this.idEntries.entrySet()) {
            j = j + ((long) (Utf8.utf8StringLengthInBytes((String) next2.getKey()) + 1)) + ((long) (Utf8.utf8StringLengthInBytes((String) next2.getValue()) + 1));
        }
        return j;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.mimeSubtypeName);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.profileLevelIdc);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.codecs);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.protection);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.languages);
        IsoTypeWriter.writeUInt8(byteBuffer, this.brandEntries.size());
        for (Map.Entry next : this.brandEntries.entrySet()) {
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) next.getKey());
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) next.getValue());
        }
        IsoTypeWriter.writeUInt8(byteBuffer, this.idEntries.size());
        for (Map.Entry next2 : this.idEntries.entrySet()) {
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) next2.getKey());
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) next2.getValue());
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.mimeSubtypeName = IsoTypeReader.readString(byteBuffer);
        this.profileLevelIdc = IsoTypeReader.readString(byteBuffer);
        this.codecs = IsoTypeReader.readString(byteBuffer);
        this.protection = IsoTypeReader.readString(byteBuffer);
        this.languages = IsoTypeReader.readString(byteBuffer);
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        while (true) {
            int i = readUInt8 - 1;
            if (readUInt8 <= 0) {
                break;
            }
            this.brandEntries.put(IsoTypeReader.readString(byteBuffer), IsoTypeReader.readString(byteBuffer));
            readUInt8 = i;
        }
        int readUInt82 = IsoTypeReader.readUInt8(byteBuffer);
        while (true) {
            int i2 = readUInt82 - 1;
            if (readUInt82 > 0) {
                this.idEntries.put(IsoTypeReader.readString(byteBuffer), IsoTypeReader.readString(byteBuffer));
                readUInt82 = i2;
            } else {
                return;
            }
        }
    }

    public static class BrandEntry {
        String iso_brand;
        String version;

        public BrandEntry(String str, String str2) {
            this.iso_brand = str;
            this.version = str2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            BrandEntry brandEntry = (BrandEntry) obj;
            String str = this.iso_brand;
            if (str == null ? brandEntry.iso_brand != null : !str.equals(brandEntry.iso_brand)) {
                return false;
            }
            String str2 = this.version;
            String str3 = brandEntry.version;
            return str2 == null ? str3 == null : str2.equals(str3);
        }

        public int hashCode() {
            String str = this.iso_brand;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            String str2 = this.version;
            if (str2 != null) {
                i = str2.hashCode();
            }
            return hashCode + i;
        }
    }

    public String getMimeSubtypeName() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.mimeSubtypeName;
    }

    public void setMimeSubtypeName(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) str));
        this.mimeSubtypeName = str;
    }

    public String getProfileLevelIdc() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.profileLevelIdc;
    }

    public void setProfileLevelIdc(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) str));
        this.profileLevelIdc = str;
    }

    public String getCodecs() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.codecs;
    }

    public void setCodecs(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) str));
        this.codecs = str;
    }

    public String getProtection() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.protection;
    }

    public void setProtection(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, (Object) str));
        this.protection = str;
    }

    public String getLanguages() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.languages;
    }

    public void setLanguages(String str) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, (Object) str));
        this.languages = str;
    }

    public Map<String, String> getBrandEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.brandEntries;
    }

    public void setBrandEntries(Map<String, String> map) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, (Object) map));
        this.brandEntries = map;
    }

    public Map<String, String> getIdEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.idEntries;
    }

    public void setIdEntries(Map<String, String> map) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, (Object) map));
        this.idEntries = map;
    }
}
