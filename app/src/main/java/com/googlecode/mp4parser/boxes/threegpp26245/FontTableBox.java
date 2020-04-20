package com.googlecode.mp4parser.boxes.threegpp26245;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class FontTableBox extends AbstractBox {
    public static final String TYPE = "ftab";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    List<FontRecord> entries = new LinkedList();

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("FontTableBox.java", FontTableBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.googlecode.mp4parser.boxes.threegpp26245.FontTableBox", "", "", "", "java.util.List"), 52);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.googlecode.mp4parser.boxes.threegpp26245.FontTableBox", "java.util.List", "entries", "", "void"), 56);
    }

    public FontTableBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        int i = 2;
        for (FontRecord size : this.entries) {
            i += size.getSize();
        }
        return (long) i;
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
        for (int i = 0; i < readUInt16; i++) {
            FontRecord fontRecord = new FontRecord();
            fontRecord.parse(byteBuffer);
            this.entries.add(fontRecord);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt16(byteBuffer, this.entries.size());
        for (FontRecord content : this.entries) {
            content.getContent(byteBuffer);
        }
    }

    public List<FontRecord> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<FontRecord> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    public static class FontRecord {
        int fontId;
        String fontname;

        public FontRecord() {
        }

        public FontRecord(int i, String str) {
            this.fontId = i;
            this.fontname = str;
        }

        public void parse(ByteBuffer byteBuffer) {
            this.fontId = IsoTypeReader.readUInt16(byteBuffer);
            this.fontname = IsoTypeReader.readString(byteBuffer, IsoTypeReader.readUInt8(byteBuffer));
        }

        public void getContent(ByteBuffer byteBuffer) {
            IsoTypeWriter.writeUInt16(byteBuffer, this.fontId);
            IsoTypeWriter.writeUInt8(byteBuffer, this.fontname.length());
            byteBuffer.put(Utf8.convert(this.fontname));
        }

        public int getSize() {
            return Utf8.utf8StringLengthInBytes(this.fontname) + 3;
        }

        public String toString() {
            return "FontRecord{fontId=" + this.fontId + ", fontname='" + this.fontname + '\'' + '}';
        }
    }
}
