package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.LazyList;
import com.googlecode.mp4parser.util.Logger;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BasicContainer implements Container, Iterator<Box>, Closeable {
    private static final Box EOF = new AbstractBox("eof ") {
        /* access modifiers changed from: protected */
        public void _parseDetails(ByteBuffer byteBuffer) {
        }

        /* access modifiers changed from: protected */
        public void getContent(ByteBuffer byteBuffer) {
        }

        /* access modifiers changed from: protected */
        public long getContentSize() {
            return 0;
        }
    };
    private static Logger LOG = Logger.getLogger(BasicContainer.class);
    protected BoxParser boxParser;
    private List<Box> boxes = new ArrayList();
    protected DataSource dataSource;
    long endPosition = 0;
    Box lookahead = null;
    long parsePosition = 0;
    long startPosition = 0;

    public List<Box> getBoxes() {
        if (this.dataSource == null || this.lookahead == EOF) {
            return this.boxes;
        }
        return new LazyList(this.boxes, this);
    }

    public void setBoxes(List<Box> list) {
        this.boxes = new ArrayList(list);
        this.lookahead = EOF;
        this.dataSource = null;
    }

    /* access modifiers changed from: protected */
    public long getContainerSize() {
        long j = 0;
        for (int i = 0; i < getBoxes().size(); i++) {
            j += this.boxes.get(i).getSize();
        }
        return j;
    }

    public <T extends Box> List<T> getBoxes(Class<T> cls) {
        List<Box> boxes2 = getBoxes();
        ArrayList arrayList = null;
        Box box = null;
        for (int i = 0; i < boxes2.size(); i++) {
            Box box2 = boxes2.get(i);
            if (cls.isInstance(box2)) {
                if (box == null) {
                    box = box2;
                } else {
                    if (arrayList == null) {
                        arrayList = new ArrayList(2);
                        arrayList.add(box);
                    }
                    arrayList.add(box2);
                }
            }
        }
        if (arrayList != null) {
            return arrayList;
        }
        if (box != null) {
            return Collections.singletonList(box);
        }
        return Collections.emptyList();
    }

    public <T extends Box> List<T> getBoxes(Class<T> cls, boolean z) {
        ArrayList arrayList = new ArrayList(2);
        List<Box> boxes2 = getBoxes();
        for (int i = 0; i < boxes2.size(); i++) {
            Box box = boxes2.get(i);
            if (cls.isInstance(box)) {
                arrayList.add(box);
            }
            if (z && (box instanceof Container)) {
                arrayList.addAll(((Container) box).getBoxes(cls, z));
            }
        }
        return arrayList;
    }

    public void addBox(Box box) {
        if (box != null) {
            this.boxes = new ArrayList(getBoxes());
            box.setParent(this);
            this.boxes.add(box);
        }
    }

    public void initContainer(DataSource dataSource2, long j, BoxParser boxParser2) throws IOException {
        this.dataSource = dataSource2;
        long position = dataSource2.position();
        this.startPosition = position;
        this.parsePosition = position;
        dataSource2.position(dataSource2.position() + j);
        this.endPosition = dataSource2.position();
        this.boxParser = boxParser2;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        Box box = this.lookahead;
        if (box == EOF) {
            return false;
        }
        if (box != null) {
            return true;
        }
        try {
            this.lookahead = next();
            return true;
        } catch (NoSuchElementException unused) {
            this.lookahead = EOF;
            return false;
        }
    }

    public Box next() {
        Box parseBox;
        Box box = this.lookahead;
        if (box == null || box == EOF) {
            DataSource dataSource2 = this.dataSource;
            if (dataSource2 == null || this.parsePosition >= this.endPosition) {
                this.lookahead = EOF;
                throw new NoSuchElementException();
            }
            try {
                synchronized (dataSource2) {
                    this.dataSource.position(this.parsePosition);
                    parseBox = this.boxParser.parseBox(this.dataSource, this);
                    this.parsePosition = this.dataSource.position();
                }
                return parseBox;
            } catch (EOFException unused) {
                throw new NoSuchElementException();
            } catch (IOException unused2) {
                throw new NoSuchElementException();
            }
        } else {
            this.lookahead = null;
            return box;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("[");
        for (int i = 0; i < this.boxes.size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(this.boxes.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public final void writeContainer(WritableByteChannel writableByteChannel) throws IOException {
        for (Box box : getBoxes()) {
            box.getBox(writableByteChannel);
        }
    }

    public ByteBuffer getByteBuffer(long j, long j2) throws IOException {
        ByteBuffer map;
        long j3 = j2;
        DataSource dataSource2 = this.dataSource;
        if (dataSource2 != null) {
            synchronized (dataSource2) {
                map = this.dataSource.map(this.startPosition + j, j3);
            }
            return map;
        }
        ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(j2));
        long j4 = j + j3;
        long j5 = 0;
        for (Box next : this.boxes) {
            long size = next.getSize() + j5;
            if (size > j && j5 < j4) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                WritableByteChannel newChannel = Channels.newChannel(byteArrayOutputStream);
                next.getBox(newChannel);
                newChannel.close();
                int i = (j5 > j ? 1 : (j5 == j ? 0 : -1));
                if (i >= 0 && size <= j4) {
                    allocate.put(byteArrayOutputStream.toByteArray());
                } else if (i < 0 && size > j4) {
                    long j6 = j - j5;
                    allocate.put(byteArrayOutputStream.toByteArray(), CastUtils.l2i(j6), CastUtils.l2i((next.getSize() - j6) - (size - j4)));
                } else if (i < 0 && size <= j4) {
                    long j7 = j - j5;
                    allocate.put(byteArrayOutputStream.toByteArray(), CastUtils.l2i(j7), CastUtils.l2i(next.getSize() - j7));
                } else if (i >= 0 && size > j4) {
                    allocate.put(byteArrayOutputStream.toByteArray(), 0, CastUtils.l2i(next.getSize() - (size - j4)));
                }
            }
            j5 = size;
        }
        return (ByteBuffer) allocate.rewind();
    }

    public void close() throws IOException {
        this.dataSource.close();
    }
}
