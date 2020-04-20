package com.googlecode.mp4parser.boxes;

import android.support.v7.widget.helper.ItemTouchHelper;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public abstract class AbstractSampleEncryptionBox extends AbstractFullBox {
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    protected int algorithmId = -1;
    List<CencSampleAuxiliaryDataFormat> entries = Collections.emptyList();
    protected int ivSize = -1;
    protected byte[] kid = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("AbstractSampleEncryptionBox.java", AbstractSampleEncryptionBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getOffsetToFirstIV", "com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox", "", "", "", "int"), 29);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox", "", "", "", "java.util.List"), 89);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox", "java.util.List", "entries", "", "void"), 93);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "equals", "com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox", "java.lang.Object", "o", "", "boolean"), 173);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "hashCode", "com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox", "", "", "", "int"), (int) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntrySizes", "com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox", "", "", "", "java.util.List"), 208);
    }

    protected AbstractSampleEncryptionBox(String str) {
        super(str);
    }

    public int getOffsetToFirstIV() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return (getSize() > 4294967296L ? 16 : 8) + (isOverrideTrackEncryptionBoxParameters() ? this.kid.length + 4 : 0) + 4;
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        if ((getFlags() & 1) > 0) {
            this.algorithmId = IsoTypeReader.readUInt24(byteBuffer);
            this.ivSize = IsoTypeReader.readUInt8(byteBuffer);
            this.kid = new byte[16];
            byteBuffer.get(this.kid);
        }
        long readUInt32 = IsoTypeReader.readUInt32(byteBuffer);
        ByteBuffer duplicate = byteBuffer.duplicate();
        ByteBuffer duplicate2 = byteBuffer.duplicate();
        this.entries = parseEntries(duplicate, readUInt32, 8);
        if (this.entries == null) {
            this.entries = parseEntries(duplicate2, readUInt32, 16);
            byteBuffer.position((byteBuffer.position() + byteBuffer.remaining()) - duplicate2.remaining());
        } else {
            byteBuffer.position((byteBuffer.position() + byteBuffer.remaining()) - duplicate.remaining());
        }
        if (this.entries == null) {
            throw new RuntimeException("Cannot parse SampleEncryptionBox");
        }
    }

    private List<CencSampleAuxiliaryDataFormat> parseEntries(ByteBuffer byteBuffer, long j, int i) {
        ArrayList arrayList = new ArrayList();
        while (true) {
            long j2 = j - 1;
            if (j <= 0) {
                return arrayList;
            }
            try {
                CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat = new CencSampleAuxiliaryDataFormat();
                cencSampleAuxiliaryDataFormat.iv = new byte[i];
                byteBuffer.get(cencSampleAuxiliaryDataFormat.iv);
                if ((getFlags() & 2) > 0) {
                    cencSampleAuxiliaryDataFormat.pairs = new CencSampleAuxiliaryDataFormat.Pair[IsoTypeReader.readUInt16(byteBuffer)];
                    for (int i2 = 0; i2 < cencSampleAuxiliaryDataFormat.pairs.length; i2++) {
                        cencSampleAuxiliaryDataFormat.pairs[i2] = cencSampleAuxiliaryDataFormat.createPair(IsoTypeReader.readUInt16(byteBuffer), IsoTypeReader.readUInt32(byteBuffer));
                    }
                }
                arrayList.add(cencSampleAuxiliaryDataFormat);
                j = j2;
            } catch (BufferUnderflowException unused) {
                return null;
            }
        }
    }

    public List<CencSampleAuxiliaryDataFormat> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.entries;
    }

    public void setEntries(List<CencSampleAuxiliaryDataFormat> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    @DoNotParseDetail
    public boolean isSubSampleEncryption() {
        return (getFlags() & 2) > 0;
    }

    @DoNotParseDetail
    public void setSubSampleEncryption(boolean z) {
        if (z) {
            setFlags(getFlags() | 2);
        } else {
            setFlags(getFlags() & 16777213);
        }
    }

    /* access modifiers changed from: protected */
    @DoNotParseDetail
    public boolean isOverrideTrackEncryptionBoxParameters() {
        return (getFlags() & 1) > 0;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if (isOverrideTrackEncryptionBoxParameters()) {
            IsoTypeWriter.writeUInt24(byteBuffer, this.algorithmId);
            IsoTypeWriter.writeUInt8(byteBuffer, this.ivSize);
            byteBuffer.put(this.kid);
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) getNonEmptyEntriesNum());
        for (CencSampleAuxiliaryDataFormat next : this.entries) {
            if (next.getSize() > 0) {
                if (next.iv.length == 8 || next.iv.length == 16) {
                    byteBuffer.put(next.iv);
                    if (isSubSampleEncryption()) {
                        IsoTypeWriter.writeUInt16(byteBuffer, next.pairs.length);
                        for (CencSampleAuxiliaryDataFormat.Pair pair : next.pairs) {
                            IsoTypeWriter.writeUInt16(byteBuffer, pair.clear());
                            IsoTypeWriter.writeUInt32(byteBuffer, pair.encrypted());
                        }
                    }
                } else {
                    throw new RuntimeException("IV must be either 8 or 16 bytes");
                }
            }
        }
    }

    private int getNonEmptyEntriesNum() {
        int i = 0;
        for (CencSampleAuxiliaryDataFormat size : this.entries) {
            if (size.getSize() > 0) {
                i++;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long length = (isOverrideTrackEncryptionBoxParameters() ? 8 + ((long) this.kid.length) : 4) + 4;
        for (CencSampleAuxiliaryDataFormat size : this.entries) {
            length += (long) size.getSize();
        }
        return length;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        super.getBox(writableByteChannel);
    }

    public boolean equals(Object obj) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, obj));
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractSampleEncryptionBox abstractSampleEncryptionBox = (AbstractSampleEncryptionBox) obj;
        if (this.algorithmId != abstractSampleEncryptionBox.algorithmId || this.ivSize != abstractSampleEncryptionBox.ivSize) {
            return false;
        }
        List<CencSampleAuxiliaryDataFormat> list = this.entries;
        if (list == null ? abstractSampleEncryptionBox.entries == null : list.equals(abstractSampleEncryptionBox.entries)) {
            return Arrays.equals(this.kid, abstractSampleEncryptionBox.kid);
        }
        return false;
    }

    public int hashCode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        int i = ((this.algorithmId * 31) + this.ivSize) * 31;
        byte[] bArr = this.kid;
        int i2 = 0;
        int hashCode = (i + (bArr != null ? Arrays.hashCode(bArr) : 0)) * 31;
        List<CencSampleAuxiliaryDataFormat> list = this.entries;
        if (list != null) {
            i2 = list.hashCode();
        }
        return hashCode + i2;
    }

    public List<Short> getEntrySizes() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        ArrayList arrayList = new ArrayList(this.entries.size());
        for (CencSampleAuxiliaryDataFormat next : this.entries) {
            short length = (short) next.iv.length;
            if (isSubSampleEncryption()) {
                length = (short) (((short) (length + 2)) + (next.pairs.length * 6));
            }
            arrayList.add(Short.valueOf(length));
        }
        return arrayList;
    }
}
