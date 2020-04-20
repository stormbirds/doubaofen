package com.coremedia.iso.boxes.sampleentry;

import android.support.v4.internal.view.SupportMenu;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class VisualSampleEntry extends AbstractSampleEntry implements Container {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE1 = "mp4v";
    public static final String TYPE2 = "s263";
    public static final String TYPE3 = "avc1";
    public static final String TYPE4 = "avc3";
    public static final String TYPE5 = "drmi";
    public static final String TYPE6 = "hvc1";
    public static final String TYPE7 = "hev1";
    public static final String TYPE_ENCRYPTED = "encv";
    private String compressorname = "";
    private int depth = 24;
    private int frameCount = 1;
    private int height;
    private double horizresolution = 72.0d;
    private long[] predefined = new long[3];
    private double vertresolution = 72.0d;
    private int width;

    public VisualSampleEntry() {
        super(TYPE3);
    }

    public VisualSampleEntry(String str) {
        super(str);
    }

    public void setType(String str) {
        this.type = str;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public double getHorizresolution() {
        return this.horizresolution;
    }

    public void setHorizresolution(double d) {
        this.horizresolution = d;
    }

    public double getVertresolution() {
        return this.vertresolution;
    }

    public void setVertresolution(double d) {
        this.vertresolution = d;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public void setFrameCount(int i) {
        this.frameCount = i;
    }

    public String getCompressorname() {
        return this.compressorname;
    }

    public void setCompressorname(String str) {
        this.compressorname = str;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int i) {
        this.depth = i;
    }

    public void parse(final DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        final long position = dataSource.position() + j;
        ByteBuffer allocate = ByteBuffer.allocate(78);
        dataSource.read(allocate);
        allocate.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(allocate);
        IsoTypeReader.readUInt16(allocate);
        IsoTypeReader.readUInt16(allocate);
        this.predefined[0] = IsoTypeReader.readUInt32(allocate);
        this.predefined[1] = IsoTypeReader.readUInt32(allocate);
        this.predefined[2] = IsoTypeReader.readUInt32(allocate);
        this.width = IsoTypeReader.readUInt16(allocate);
        this.height = IsoTypeReader.readUInt16(allocate);
        this.horizresolution = IsoTypeReader.readFixedPoint1616(allocate);
        this.vertresolution = IsoTypeReader.readFixedPoint1616(allocate);
        IsoTypeReader.readUInt32(allocate);
        this.frameCount = IsoTypeReader.readUInt16(allocate);
        int readUInt8 = IsoTypeReader.readUInt8(allocate);
        if (readUInt8 > 31) {
            readUInt8 = 31;
        }
        byte[] bArr = new byte[readUInt8];
        allocate.get(bArr);
        this.compressorname = Utf8.convert(bArr);
        if (readUInt8 < 31) {
            allocate.get(new byte[(31 - readUInt8)]);
        }
        this.depth = IsoTypeReader.readUInt16(allocate);
        IsoTypeReader.readUInt16(allocate);
        initContainer(new DataSource() {
            public int read(ByteBuffer byteBuffer) throws IOException {
                if (position == dataSource.position()) {
                    return -1;
                }
                if (((long) byteBuffer.remaining()) <= position - dataSource.position()) {
                    return dataSource.read(byteBuffer);
                }
                ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(position - dataSource.position()));
                dataSource.read(allocate);
                byteBuffer.put((ByteBuffer) allocate.rewind());
                return allocate.capacity();
            }

            public long size() throws IOException {
                return position;
            }

            public long position() throws IOException {
                return dataSource.position();
            }

            public void position(long j) throws IOException {
                dataSource.position(j);
            }

            public long transferTo(long j, long j2, WritableByteChannel writableByteChannel) throws IOException {
                return dataSource.transferTo(j, j2, writableByteChannel);
            }

            public ByteBuffer map(long j, long j2) throws IOException {
                return dataSource.map(j, j2);
            }

            public void close() throws IOException {
                dataSource.close();
            }
        }, j - 78, boxParser);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer allocate = ByteBuffer.allocate(78);
        allocate.position(6);
        IsoTypeWriter.writeUInt16(allocate, this.dataReferenceIndex);
        IsoTypeWriter.writeUInt16(allocate, 0);
        IsoTypeWriter.writeUInt16(allocate, 0);
        IsoTypeWriter.writeUInt32(allocate, this.predefined[0]);
        IsoTypeWriter.writeUInt32(allocate, this.predefined[1]);
        IsoTypeWriter.writeUInt32(allocate, this.predefined[2]);
        IsoTypeWriter.writeUInt16(allocate, getWidth());
        IsoTypeWriter.writeUInt16(allocate, getHeight());
        IsoTypeWriter.writeFixedPoint1616(allocate, getHorizresolution());
        IsoTypeWriter.writeFixedPoint1616(allocate, getVertresolution());
        IsoTypeWriter.writeUInt32(allocate, 0);
        IsoTypeWriter.writeUInt16(allocate, getFrameCount());
        IsoTypeWriter.writeUInt8(allocate, Utf8.utf8StringLengthInBytes(getCompressorname()));
        allocate.put(Utf8.convert(getCompressorname()));
        int utf8StringLengthInBytes = Utf8.utf8StringLengthInBytes(getCompressorname());
        while (utf8StringLengthInBytes < 31) {
            utf8StringLengthInBytes++;
            allocate.put((byte) 0);
        }
        IsoTypeWriter.writeUInt16(allocate, getDepth());
        IsoTypeWriter.writeUInt16(allocate, SupportMenu.USER_MASK);
        writableByteChannel.write((ByteBuffer) allocate.rewind());
        writeContainer(writableByteChannel);
    }

    public long getSize() {
        long containerSize = getContainerSize() + 78;
        return containerSize + ((long) ((this.largeBox || 8 + containerSize >= 4294967296L) ? 16 : 8));
    }
}
