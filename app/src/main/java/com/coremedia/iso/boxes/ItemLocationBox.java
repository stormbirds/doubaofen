package com.coremedia.iso.boxes;

import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoTypeWriterVariable;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ItemLocationBox extends AbstractFullBox {
    public static final String TYPE = "iloc";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9 = null;
    public int baseOffsetSize = 8;
    public int indexSize = 0;
    public List<Item> items = new LinkedList();
    public int lengthSize = 8;
    public int offsetSize = 8;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("ItemLocationBox.java", ItemLocationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 119);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "offsetSize", "", "void"), 123);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "createItem", "com.coremedia.iso.boxes.ItemLocationBox", "int:int:int:long:java.util.List", "itemId:constructionMethod:dataReferenceIndex:baseOffset:extents", "", "com.coremedia.iso.boxes.ItemLocationBox$Item"), 160);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "createExtent", "com.coremedia.iso.boxes.ItemLocationBox", "long:long:long", "extentOffset:extentLength:extentIndex", "", "com.coremedia.iso.boxes.ItemLocationBox$Extent"), 285);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getLengthSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 127);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setLengthSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "lengthSize", "", "void"), 131);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getBaseOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 135);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setBaseOffsetSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "baseOffsetSize", "", "void"), 139);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getIndexSize", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "int"), 143);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setIndexSize", "com.coremedia.iso.boxes.ItemLocationBox", "int", "indexSize", "", "void"), 147);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getItems", "com.coremedia.iso.boxes.ItemLocationBox", "", "", "", "java.util.List"), (int) Opcodes.DCMPL);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setItems", "com.coremedia.iso.boxes.ItemLocationBox", "java.util.List", "items", "", "void"), 155);
    }

    public ItemLocationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long j = 8;
        for (Item size : this.items) {
            j += (long) size.getSize();
        }
        return j;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.offsetSize << 4) | this.lengthSize);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt8(byteBuffer, (this.baseOffsetSize << 4) | this.indexSize);
        } else {
            IsoTypeWriter.writeUInt8(byteBuffer, this.baseOffsetSize << 4);
        }
        IsoTypeWriter.writeUInt16(byteBuffer, this.items.size());
        for (Item content : this.items) {
            content.getContent(byteBuffer);
        }
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        this.offsetSize = readUInt8 >>> 4;
        this.lengthSize = readUInt8 & 15;
        int readUInt82 = IsoTypeReader.readUInt8(byteBuffer);
        this.baseOffsetSize = readUInt82 >>> 4;
        if (getVersion() == 1) {
            this.indexSize = readUInt82 & 15;
        }
        int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
        for (int i = 0; i < readUInt16; i++) {
            this.items.add(new Item(byteBuffer));
        }
    }

    public int getOffsetSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.offsetSize;
    }

    public void setOffsetSize(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(i)));
        this.offsetSize = i;
    }

    public int getLengthSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.lengthSize;
    }

    public void setLengthSize(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(i)));
        this.lengthSize = i;
    }

    public int getBaseOffsetSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.baseOffsetSize;
    }

    public void setBaseOffsetSize(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(i)));
        this.baseOffsetSize = i;
    }

    public int getIndexSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.indexSize;
    }

    public void setIndexSize(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(i)));
        this.indexSize = i;
    }

    public List<Item> getItems() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.items;
    }

    public void setItems(List<Item> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, (Object) list));
        this.items = list;
    }

    public Item createItem(int i, int i2, int i3, long j, List<Extent> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, new Object[]{Conversions.intObject(i), Conversions.intObject(i2), Conversions.intObject(i3), Conversions.longObject(j), list}));
        return new Item(i, i2, i3, j, list);
    }

    /* access modifiers changed from: package-private */
    public Item createItem(ByteBuffer byteBuffer) {
        return new Item(byteBuffer);
    }

    public class Item {
        public long baseOffset;
        public int constructionMethod;
        public int dataReferenceIndex;
        public List<Extent> extents = new LinkedList();
        public int itemId;

        public Item(ByteBuffer byteBuffer) {
            this.itemId = IsoTypeReader.readUInt16(byteBuffer);
            if (ItemLocationBox.this.getVersion() == 1) {
                this.constructionMethod = IsoTypeReader.readUInt16(byteBuffer) & 15;
            }
            this.dataReferenceIndex = IsoTypeReader.readUInt16(byteBuffer);
            if (ItemLocationBox.this.baseOffsetSize > 0) {
                this.baseOffset = IsoTypeReaderVariable.read(byteBuffer, ItemLocationBox.this.baseOffsetSize);
            } else {
                this.baseOffset = 0;
            }
            int readUInt16 = IsoTypeReader.readUInt16(byteBuffer);
            for (int i = 0; i < readUInt16; i++) {
                this.extents.add(new Extent(byteBuffer));
            }
        }

        public Item(int i, int i2, int i3, long j, List<Extent> list) {
            this.itemId = i;
            this.constructionMethod = i2;
            this.dataReferenceIndex = i3;
            this.baseOffset = j;
            this.extents = list;
        }

        public int getSize() {
            int i = (ItemLocationBox.this.getVersion() == 1 ? 4 : 2) + 2 + ItemLocationBox.this.baseOffsetSize + 2;
            for (Extent size : this.extents) {
                i += size.getSize();
            }
            return i;
        }

        public void setBaseOffset(long j) {
            this.baseOffset = j;
        }

        public void getContent(ByteBuffer byteBuffer) {
            IsoTypeWriter.writeUInt16(byteBuffer, this.itemId);
            if (ItemLocationBox.this.getVersion() == 1) {
                IsoTypeWriter.writeUInt16(byteBuffer, this.constructionMethod);
            }
            IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
            if (ItemLocationBox.this.baseOffsetSize > 0) {
                IsoTypeWriterVariable.write(this.baseOffset, byteBuffer, ItemLocationBox.this.baseOffsetSize);
            }
            IsoTypeWriter.writeUInt16(byteBuffer, this.extents.size());
            for (Extent content : this.extents) {
                content.getContent(byteBuffer);
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Item item = (Item) obj;
            if (this.baseOffset != item.baseOffset || this.constructionMethod != item.constructionMethod || this.dataReferenceIndex != item.dataReferenceIndex || this.itemId != item.itemId) {
                return false;
            }
            List<Extent> list = this.extents;
            List<Extent> list2 = item.extents;
            return list == null ? list2 == null : list.equals(list2);
        }

        public int hashCode() {
            long j = this.baseOffset;
            int i = ((((((this.itemId * 31) + this.constructionMethod) * 31) + this.dataReferenceIndex) * 31) + ((int) (j ^ (j >>> 32)))) * 31;
            List<Extent> list = this.extents;
            return i + (list != null ? list.hashCode() : 0);
        }

        public String toString() {
            return "Item{baseOffset=" + this.baseOffset + ", itemId=" + this.itemId + ", constructionMethod=" + this.constructionMethod + ", dataReferenceIndex=" + this.dataReferenceIndex + ", extents=" + this.extents + '}';
        }
    }

    public Extent createExtent(long j, long j2, long j3) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, new Object[]{Conversions.longObject(j), Conversions.longObject(j2), Conversions.longObject(j3)}));
        return new Extent(j, j2, j3);
    }

    /* access modifiers changed from: package-private */
    public Extent createExtent(ByteBuffer byteBuffer) {
        return new Extent(byteBuffer);
    }

    public class Extent {
        public long extentIndex;
        public long extentLength;
        public long extentOffset;

        public Extent(long j, long j2, long j3) {
            this.extentOffset = j;
            this.extentLength = j2;
            this.extentIndex = j3;
        }

        public Extent(ByteBuffer byteBuffer) {
            if (ItemLocationBox.this.getVersion() == 1 && ItemLocationBox.this.indexSize > 0) {
                this.extentIndex = IsoTypeReaderVariable.read(byteBuffer, ItemLocationBox.this.indexSize);
            }
            this.extentOffset = IsoTypeReaderVariable.read(byteBuffer, ItemLocationBox.this.offsetSize);
            this.extentLength = IsoTypeReaderVariable.read(byteBuffer, ItemLocationBox.this.lengthSize);
        }

        public void getContent(ByteBuffer byteBuffer) {
            if (ItemLocationBox.this.getVersion() == 1 && ItemLocationBox.this.indexSize > 0) {
                IsoTypeWriterVariable.write(this.extentIndex, byteBuffer, ItemLocationBox.this.indexSize);
            }
            IsoTypeWriterVariable.write(this.extentOffset, byteBuffer, ItemLocationBox.this.offsetSize);
            IsoTypeWriterVariable.write(this.extentLength, byteBuffer, ItemLocationBox.this.lengthSize);
        }

        public int getSize() {
            return (ItemLocationBox.this.indexSize > 0 ? ItemLocationBox.this.indexSize : 0) + ItemLocationBox.this.offsetSize + ItemLocationBox.this.lengthSize;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Extent extent = (Extent) obj;
            return this.extentIndex == extent.extentIndex && this.extentLength == extent.extentLength && this.extentOffset == extent.extentOffset;
        }

        public int hashCode() {
            long j = this.extentOffset;
            long j2 = this.extentLength;
            long j3 = this.extentIndex;
            return (((((int) (j ^ (j >>> 32))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + ((int) (j3 ^ (j3 >>> 32)));
        }

        public String toString() {
            return "Extent" + "{extentOffset=" + this.extentOffset + ", extentLength=" + this.extentLength + ", extentIndex=" + this.extentIndex + '}';
        }
    }
}
