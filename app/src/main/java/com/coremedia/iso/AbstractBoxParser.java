package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.mp4parser.DataSource;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public abstract class AbstractBoxParser implements BoxParser {
    private static Logger LOG = Logger.getLogger(AbstractBoxParser.class.getName());
    ThreadLocal<ByteBuffer> header = new ThreadLocal<ByteBuffer>() {
        /* access modifiers changed from: protected */
        public ByteBuffer initialValue() {
            return ByteBuffer.allocate(32);
        }
    };

    public abstract Box createBox(String str, byte[] bArr, String str2);

    public Box parseBox(DataSource dataSource, Container container) throws IOException {
        int read;
        long j;
        long j2;
        DataSource dataSource2 = dataSource;
        Container container2 = container;
        long position = dataSource.position();
        this.header.get().rewind().limit(8);
        do {
            read = dataSource2.read(this.header.get());
            if (read == 8) {
                this.header.get().rewind();
                long readUInt32 = IsoTypeReader.readUInt32(this.header.get());
                byte[] bArr = null;
                if (readUInt32 >= 8 || readUInt32 <= 1) {
                    String read4cc = IsoTypeReader.read4cc(this.header.get());
                    if (readUInt32 == 1) {
                        this.header.get().limit(16);
                        dataSource2.read(this.header.get());
                        this.header.get().position(8);
                        j = IsoTypeReader.readUInt64(this.header.get()) - 16;
                    } else {
                        j = readUInt32 == 0 ? dataSource.size() - dataSource.position() : readUInt32 - 8;
                    }
                    if (UserBox.TYPE.equals(read4cc)) {
                        this.header.get().limit(this.header.get().limit() + 16);
                        dataSource2.read(this.header.get());
                        byte[] bArr2 = new byte[16];
                        for (int position2 = this.header.get().position() - 16; position2 < this.header.get().position(); position2++) {
                            bArr2[position2 - (this.header.get().position() - 16)] = this.header.get().get(position2);
                        }
                        j2 = j - 16;
                        bArr = bArr2;
                    } else {
                        j2 = j;
                    }
                    Box createBox = createBox(read4cc, bArr, container2 instanceof Box ? ((Box) container2).getType() : "");
                    createBox.setParent(container2);
                    this.header.get().rewind();
                    createBox.parse(dataSource, this.header.get(), j2, this);
                    return createBox;
                }
                Logger logger = LOG;
                logger.severe("Plausibility check failed: size < 8 (size = " + readUInt32 + "). Stop parsing!");
                return null;
            }
        } while (read >= 0);
        dataSource2.position(position);
        throw new EOFException();
    }
}
