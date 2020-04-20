package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class AbstractContainerBox extends BasicContainer implements Box {
    protected boolean largeBox;
    private long offset;
    Container parent;
    protected String type;

    public AbstractContainerBox(String str) {
        this.type = str;
    }

    public Container getParent() {
        return this.parent;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    public long getSize() {
        long containerSize = getContainerSize();
        return containerSize + ((long) ((this.largeBox || 8 + containerSize >= 4294967296L) ? 16 : 8));
    }

    public String getType() {
        return this.type;
    }

    /* access modifiers changed from: protected */
    public ByteBuffer getHeader() {
        ByteBuffer byteBuffer;
        if (this.largeBox || getSize() >= 4294967296L) {
            byte[] bArr = new byte[16];
            bArr[3] = 1;
            bArr[4] = this.type.getBytes()[0];
            bArr[5] = this.type.getBytes()[1];
            bArr[6] = this.type.getBytes()[2];
            bArr[7] = this.type.getBytes()[3];
            byteBuffer = ByteBuffer.wrap(bArr);
            byteBuffer.position(8);
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
        } else {
            byte[] bArr2 = new byte[8];
            bArr2[4] = this.type.getBytes()[0];
            bArr2[5] = this.type.getBytes()[1];
            bArr2[6] = this.type.getBytes()[2];
            bArr2[7] = this.type.getBytes()[3];
            byteBuffer = ByteBuffer.wrap(bArr2);
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
        }
        byteBuffer.rewind();
        return byteBuffer;
    }

    public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        this.offset = dataSource.position() - ((long) byteBuffer.remaining());
        this.largeBox = byteBuffer.remaining() == 16;
        initContainer(dataSource, j, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        writeContainer(writableByteChannel);
    }

    public void initContainer(DataSource dataSource, long j, BoxParser boxParser) throws IOException {
        this.dataSource = dataSource;
        this.parsePosition = dataSource.position();
        this.startPosition = this.parsePosition - ((long) ((this.largeBox || 8 + j >= 4294967296L) ? 16 : 8));
        dataSource.position(dataSource.position() + j);
        this.endPosition = dataSource.position();
        this.boxParser = boxParser;
    }
}
