package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.FullBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.logging.Logger;

public abstract class FullContainerBox extends AbstractContainerBox implements FullBox {
    private static Logger LOG = Logger.getLogger(FullContainerBox.class.getName());
    private int flags;
    private int version;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int i) {
        this.flags = i;
    }

    public <T extends Box> List<T> getBoxes(Class<T> cls) {
        return getBoxes(cls, false);
    }

    public FullContainerBox(String str) {
        super(str);
    }

    public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        dataSource.read(allocate);
        parseVersionAndFlags((ByteBuffer) allocate.rewind());
        super.parse(dataSource, byteBuffer, j, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        super.getBox(writableByteChannel);
    }

    public String toString() {
        return String.valueOf(getClass().getSimpleName()) + "[childBoxes]";
    }

    /* access modifiers changed from: protected */
    public final long parseVersionAndFlags(ByteBuffer byteBuffer) {
        this.version = IsoTypeReader.readUInt8(byteBuffer);
        this.flags = IsoTypeReader.readUInt24(byteBuffer);
        return 4;
    }

    /* access modifiers changed from: protected */
    public final void writeVersionAndFlags(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, this.version);
        IsoTypeWriter.writeUInt24(byteBuffer, this.flags);
    }

    /* access modifiers changed from: protected */
    public ByteBuffer getHeader() {
        ByteBuffer byteBuffer;
        if (this.largeBox || getSize() >= 4294967296L) {
            byte[] bArr = new byte[20];
            bArr[3] = 1;
            bArr[4] = this.type.getBytes()[0];
            bArr[5] = this.type.getBytes()[1];
            bArr[6] = this.type.getBytes()[2];
            bArr[7] = this.type.getBytes()[3];
            byteBuffer = ByteBuffer.wrap(bArr);
            byteBuffer.position(8);
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
            writeVersionAndFlags(byteBuffer);
        } else {
            byte[] bArr2 = new byte[12];
            bArr2[4] = this.type.getBytes()[0];
            bArr2[5] = this.type.getBytes()[1];
            bArr2[6] = this.type.getBytes()[2];
            bArr2[7] = this.type.getBytes()[3];
            byteBuffer = ByteBuffer.wrap(bArr2);
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
            byteBuffer.position(8);
            writeVersionAndFlags(byteBuffer);
        }
        byteBuffer.rewind();
        return byteBuffer;
    }
}
