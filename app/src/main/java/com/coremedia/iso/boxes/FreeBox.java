package com.coremedia.iso.boxes;

import android.support.v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.List;

public class FreeBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE = "free";
    ByteBuffer data;
    private long offset;
    private Container parent;
    List<Box> replacers;

    public String getType() {
        return TYPE;
    }

    public FreeBox() {
        this.replacers = new LinkedList();
        this.data = ByteBuffer.wrap(new byte[0]);
    }

    public FreeBox(int i) {
        this.replacers = new LinkedList();
        this.data = ByteBuffer.allocate(i);
    }

    public long getOffset() {
        return this.offset;
    }

    public ByteBuffer getData() {
        ByteBuffer byteBuffer = this.data;
        if (byteBuffer != null) {
            return (ByteBuffer) byteBuffer.duplicate().rewind();
        }
        return null;
    }

    public void setData(ByteBuffer byteBuffer) {
        this.data = byteBuffer;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        for (Box box : this.replacers) {
            box.getBox(writableByteChannel);
        }
        ByteBuffer allocate = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt32(allocate, (long) (this.data.limit() + 8));
        allocate.put(TYPE.getBytes());
        allocate.rewind();
        writableByteChannel.write(allocate);
        allocate.rewind();
        this.data.rewind();
        writableByteChannel.write(this.data);
        this.data.rewind();
    }

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    public long getSize() {
        long j = 8;
        for (Box size : this.replacers) {
            j += size.getSize();
        }
        return j + ((long) this.data.limit());
    }

    public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        this.offset = dataSource.position() - ((long) byteBuffer.remaining());
        if (j > PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            this.data = dataSource.map(dataSource.position(), j);
            dataSource.position(dataSource.position() + j);
            return;
        }
        this.data = ByteBuffer.allocate(CastUtils.l2i(j));
        dataSource.read(this.data);
    }

    public void addAndReplace(Box box) {
        this.data.position(CastUtils.l2i(box.getSize()));
        this.data = this.data.slice();
        this.replacers.add(box);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FreeBox freeBox = (FreeBox) obj;
        return getData() == null ? freeBox.getData() == null : getData().equals(freeBox.getData());
    }

    public int hashCode() {
        ByteBuffer byteBuffer = this.data;
        if (byteBuffer != null) {
            return byteBuffer.hashCode();
        }
        return 0;
    }
}
