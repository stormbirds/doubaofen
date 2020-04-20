package com.coremedia.iso.boxes.mdat;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class MediaDataBox implements Box {
    public static final String TYPE = "mdat";
    private DataSource dataSource;
    private long offset;
    Container parent;
    private long size;

    public String getType() {
        return TYPE;
    }

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    private static void transfer(DataSource dataSource2, long j, long j2, WritableByteChannel writableByteChannel) throws IOException {
        long j3 = 0;
        while (j3 < j2) {
            j3 += dataSource2.transferTo(j + j3, Math.min(67076096, j2 - j3), writableByteChannel);
        }
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        transfer(this.dataSource, this.offset, this.size, writableByteChannel);
    }

    public long getSize() {
        return this.size;
    }

    public long getOffset() {
        return this.offset;
    }

    public void parse(DataSource dataSource2, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        this.offset = dataSource2.position() - ((long) byteBuffer.remaining());
        this.dataSource = dataSource2;
        this.size = ((long) byteBuffer.remaining()) + j;
        dataSource2.position(dataSource2.position() + j);
    }

    public String toString() {
        return "MediaDataBox{size=" + this.size + '}';
    }
}
