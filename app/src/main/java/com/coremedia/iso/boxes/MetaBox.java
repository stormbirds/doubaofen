package com.coremedia.iso.boxes;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class MetaBox extends AbstractContainerBox {
    public static final String TYPE = "meta";
    private int flags;
    private boolean isFullBox = true;
    private int version;

    public MetaBox() {
        super(TYPE);
    }

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

    public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(j));
        dataSource.read(allocate);
        allocate.position(4);
        if (HandlerBox.TYPE.equals(IsoTypeReader.read4cc(allocate))) {
            this.isFullBox = false;
            initContainer(new MemoryDataSourceImpl((ByteBuffer) allocate.rewind()), j, boxParser);
            return;
        }
        this.isFullBox = true;
        parseVersionAndFlags((ByteBuffer) allocate.rewind());
        initContainer(new MemoryDataSourceImpl(allocate), j - 4, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        if (this.isFullBox) {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            writeVersionAndFlags(allocate);
            writableByteChannel.write((ByteBuffer) allocate.rewind());
        }
        writeContainer(writableByteChannel);
    }

    public long getSize() {
        long containerSize = getContainerSize() + (this.isFullBox ? 4 : 0);
        return containerSize + ((long) ((this.largeBox || containerSize >= 4294967296L) ? 16 : 8));
    }
}
