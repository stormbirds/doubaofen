package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractH26XTrack extends AbstractTrack {
    public static int BUFFER = 67107840;
    protected List<CompositionTimeToSample.Entry> ctts;
    private DataSource dataSource;
    protected long[] decodingTimes;
    protected List<SampleDependencyTypeBox.Entry> sdtp;
    protected List<Integer> stss;
    protected TrackMetaData trackMetaData;
    boolean tripleZeroIsEndOfSequence;

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public AbstractH26XTrack(DataSource dataSource2, boolean z) {
        super(dataSource2.toString());
        this.ctts = new ArrayList();
        this.sdtp = new ArrayList();
        this.stss = new ArrayList();
        this.trackMetaData = new TrackMetaData();
        this.tripleZeroIsEndOfSequence = true;
        this.dataSource = dataSource2;
        this.tripleZeroIsEndOfSequence = z;
    }

    public AbstractH26XTrack(DataSource dataSource2) {
        this(dataSource2, true);
    }

    public static class LookAhead {
        ByteBuffer buffer;
        long bufferStartPos = 0;
        DataSource dataSource;
        int inBufferPos = 0;
        long start;

        public void fillBuffer() throws IOException {
            DataSource dataSource2 = this.dataSource;
            this.buffer = dataSource2.map(this.bufferStartPos, Math.min(dataSource2.size() - this.bufferStartPos, (long) AbstractH26XTrack.BUFFER));
        }

        public LookAhead(DataSource dataSource2) throws IOException {
            this.dataSource = dataSource2;
            fillBuffer();
        }

        public boolean nextThreeEquals001() throws IOException {
            int limit = this.buffer.limit();
            int i = this.inBufferPos;
            if (limit - i >= 3) {
                if (this.buffer.get(i) == 0 && this.buffer.get(this.inBufferPos + 1) == 0 && this.buffer.get(this.inBufferPos + 2) == 1) {
                    return true;
                }
                return false;
            } else if (this.bufferStartPos + ((long) i) + 3 < this.dataSource.size()) {
                return false;
            } else {
                throw new EOFException();
            }
        }

        public boolean nextThreeEquals000or001orEof(boolean z) throws IOException {
            int limit = this.buffer.limit();
            int i = this.inBufferPos;
            if (limit - i >= 3) {
                return this.buffer.get(i) == 0 && this.buffer.get(this.inBufferPos + 1) == 0 && ((this.buffer.get(this.inBufferPos + 2) == 0 && z) || this.buffer.get(this.inBufferPos + 2) == 1);
            }
            if (this.bufferStartPos + ((long) i) + 3 > this.dataSource.size()) {
                return this.bufferStartPos + ((long) this.inBufferPos) == this.dataSource.size();
            }
            this.bufferStartPos = this.start;
            this.inBufferPos = 0;
            fillBuffer();
            return nextThreeEquals000or001orEof(z);
        }

        public void discardByte() {
            this.inBufferPos++;
        }

        public void discardNext3AndMarkStart() {
            this.inBufferPos += 3;
            this.start = this.bufferStartPos + ((long) this.inBufferPos);
        }

        public ByteBuffer getNal() {
            long j = this.start;
            long j2 = this.bufferStartPos;
            if (j >= j2) {
                this.buffer.position((int) (j - j2));
                ByteBuffer slice = this.buffer.slice();
                slice.limit((int) (((long) this.inBufferPos) - (this.start - this.bufferStartPos)));
                return slice;
            }
            throw new RuntimeException("damn! NAL exceeds buffer");
        }
    }

    /* access modifiers changed from: protected */
    public ByteBuffer findNextNal(LookAhead lookAhead) throws IOException {
        while (!lookAhead.nextThreeEquals001()) {
            try {
                lookAhead.discardByte();
            } catch (EOFException unused) {
                return null;
            }
        }
        lookAhead.discardNext3AndMarkStart();
        while (!lookAhead.nextThreeEquals000or001orEof(this.tripleZeroIsEndOfSequence)) {
            lookAhead.discardByte();
        }
        return lookAhead.getNal();
    }

    /* access modifiers changed from: protected */
    public Sample createSampleObject(List<? extends ByteBuffer> list) {
        byte[] bArr = new byte[(list.size() * 4)];
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        for (ByteBuffer remaining : list) {
            wrap.putInt(remaining.remaining());
        }
        ByteBuffer[] byteBufferArr = new ByteBuffer[(list.size() * 2)];
        for (int i = 0; i < list.size(); i++) {
            int i2 = i * 2;
            byteBufferArr[i2] = ByteBuffer.wrap(bArr, i * 4, 4);
            byteBufferArr[i2 + 1] = (ByteBuffer) list.get(i);
        }
        return new SampleImpl(byteBufferArr);
    }

    public long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return this.ctts;
    }

    public long[] getSyncSamples() {
        long[] jArr = new long[this.stss.size()];
        for (int i = 0; i < this.stss.size(); i++) {
            jArr[i] = (long) this.stss.get(i).intValue();
        }
        return jArr;
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.sdtp;
    }

    protected static InputStream cleanBuffer(InputStream inputStream) {
        return new CleanInputStream(inputStream);
    }

    protected static byte[] toArray(ByteBuffer byteBuffer) {
        ByteBuffer duplicate = byteBuffer.duplicate();
        byte[] bArr = new byte[duplicate.remaining()];
        duplicate.get(bArr, 0, bArr.length);
        return bArr;
    }

    public void close() throws IOException {
        this.dataSource.close();
    }
}
