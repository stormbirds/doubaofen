package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static Logger LOG = Logger.getLogger(AbstractBox.class);
    private ByteBuffer content;
    DataSource dataSource;
    private ByteBuffer deadBytes = null;
    boolean isParsed;
    long offset;
    private Container parent;
    protected String type;
    private byte[] userType;

    /* access modifiers changed from: protected */
    public abstract void _parseDetails(ByteBuffer byteBuffer);

    /* access modifiers changed from: protected */
    public abstract void getContent(ByteBuffer byteBuffer);

    /* access modifiers changed from: protected */
    public abstract long getContentSize();

    public long getOffset() {
        return this.offset;
    }

    protected AbstractBox(String str) {
        this.type = str;
        this.isParsed = true;
    }

    protected AbstractBox(String str, byte[] bArr) {
        this.type = str;
        this.userType = bArr;
        this.isParsed = true;
    }

    @DoNotParseDetail
    public void parse(DataSource dataSource2, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        this.offset = dataSource2.position() - ((long) byteBuffer.remaining());
        this.dataSource = dataSource2;
        this.content = ByteBuffer.allocate(CastUtils.l2i(j));
        while (this.content.remaining() > 0) {
            dataSource2.read(this.content);
        }
        this.content.position(0);
        this.isParsed = false;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        if (this.isParsed) {
            ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(getSize()));
            getHeader(allocate);
            getContent(allocate);
            ByteBuffer byteBuffer = this.deadBytes;
            if (byteBuffer != null) {
                byteBuffer.rewind();
                while (this.deadBytes.remaining() > 0) {
                    allocate.put(this.deadBytes);
                }
            }
            writableByteChannel.write((ByteBuffer) allocate.rewind());
            return;
        }
        int i = 16;
        int i2 = isSmallBox() ? 8 : 16;
        if (!UserBox.TYPE.equals(getType())) {
            i = 0;
        }
        ByteBuffer allocate2 = ByteBuffer.allocate(i2 + i);
        getHeader(allocate2);
        writableByteChannel.write((ByteBuffer) allocate2.rewind());
        writableByteChannel.write((ByteBuffer) this.content.position(0));
    }

    public final synchronized void parseDetails() {
        Logger logger = LOG;
        logger.logDebug("parsing details of " + getType());
        if (this.content != null) {
            ByteBuffer byteBuffer = this.content;
            this.isParsed = true;
            byteBuffer.rewind();
            _parseDetails(byteBuffer);
            if (byteBuffer.remaining() > 0) {
                this.deadBytes = byteBuffer.slice();
            }
            this.content = null;
        }
    }

    public long getSize() {
        long j;
        int i = 0;
        if (this.isParsed) {
            j = getContentSize();
        } else {
            ByteBuffer byteBuffer = this.content;
            j = (long) (byteBuffer != null ? byteBuffer.limit() : 0);
        }
        long j2 = j + ((long) ((j >= 4294967288L ? 8 : 0) + 8 + (UserBox.TYPE.equals(getType()) ? 16 : 0)));
        ByteBuffer byteBuffer2 = this.deadBytes;
        if (byteBuffer2 != null) {
            i = byteBuffer2.limit();
        }
        return j2 + ((long) i);
    }

    @DoNotParseDetail
    public String getType() {
        return this.type;
    }

    @DoNotParseDetail
    public byte[] getUserType() {
        return this.userType;
    }

    @DoNotParseDetail
    public Container getParent() {
        return this.parent;
    }

    @DoNotParseDetail
    public void setParent(Container container) {
        this.parent = container;
    }

    public boolean isParsed() {
        return this.isParsed;
    }

    private boolean verify(ByteBuffer byteBuffer) {
        long contentSize = getContentSize();
        ByteBuffer byteBuffer2 = this.deadBytes;
        ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(contentSize + ((long) (byteBuffer2 != null ? byteBuffer2.limit() : 0))));
        getContent(allocate);
        ByteBuffer byteBuffer3 = this.deadBytes;
        if (byteBuffer3 != null) {
            byteBuffer3.rewind();
            while (this.deadBytes.remaining() > 0) {
                allocate.put(this.deadBytes);
            }
        }
        byteBuffer.rewind();
        allocate.rewind();
        if (byteBuffer.remaining() != allocate.remaining()) {
            System.err.print(String.valueOf(getType()) + ": remaining differs " + byteBuffer.remaining() + " vs. " + allocate.remaining());
            LOG.logError(String.valueOf(getType()) + ": remaining differs " + byteBuffer.remaining() + " vs. " + allocate.remaining());
            return false;
        }
        int position = byteBuffer.position();
        int limit = byteBuffer.limit() - 1;
        int limit2 = allocate.limit() - 1;
        while (limit >= position) {
            byte b = byteBuffer.get(limit);
            byte b2 = allocate.get(limit2);
            if (b != b2) {
                LOG.logError(String.format("%s: buffers differ at %d: %2X/%2X", new Object[]{getType(), Integer.valueOf(limit), Byte.valueOf(b), Byte.valueOf(b2)}));
                byte[] bArr = new byte[byteBuffer.remaining()];
                byte[] bArr2 = new byte[allocate.remaining()];
                byteBuffer.get(bArr);
                allocate.get(bArr2);
                System.err.println("original      : " + Hex.encodeHex(bArr, 4));
                System.err.println("reconstructed : " + Hex.encodeHex(bArr2, 4));
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    private boolean isSmallBox() {
        int i = UserBox.TYPE.equals(getType()) ? 24 : 8;
        if (!this.isParsed) {
            return ((long) (this.content.limit() + i)) < 4294967296L;
        }
        long contentSize = getContentSize();
        ByteBuffer byteBuffer = this.deadBytes;
        return (contentSize + ((long) (byteBuffer != null ? byteBuffer.limit() : 0))) + ((long) i) < 4294967296L;
    }

    private void getHeader(ByteBuffer byteBuffer) {
        if (isSmallBox()) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, 1);
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
        }
        if (UserBox.TYPE.equals(getType())) {
            byteBuffer.put(getUserType());
        }
    }

    @DoNotParseDetail
    public String getPath() {
        return Path.createPath(this);
    }
}
