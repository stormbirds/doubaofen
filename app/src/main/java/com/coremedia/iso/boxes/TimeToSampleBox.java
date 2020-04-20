package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TimeToSampleBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE = "stts";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    static Map<List<Entry>, SoftReference<long[]>> cache = new WeakHashMap();
    List<Entry> entries = Collections.emptyList();

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("TimeToSampleBox.java", TimeToSampleBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.util.List"), 79);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "java.util.List", "entries", "", "void"), 83);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.lang.String"), 87);
    }

    public TimeToSampleBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.entries.size() * 8) + 8);
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.entries = new ArrayList(l2i);
        for (int i = 0; i < l2i; i++) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(byteBuffer), IsoTypeReader.readUInt32(byteBuffer)));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry next : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, next.getCount());
            IsoTypeWriter.writeUInt32(byteBuffer, next.getDelta());
        }
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "TimeToSampleBox[entryCount=" + this.entries.size() + "]";
    }

    public static class Entry {
        long count;
        long delta;

        public Entry(long j, long j2) {
            this.count = j;
            this.delta = j2;
        }

        public long getCount() {
            return this.count;
        }

        public long getDelta() {
            return this.delta;
        }

        public void setCount(long j) {
            this.count = j;
        }

        public void setDelta(long j) {
            this.delta = j;
        }

        public String toString() {
            return "Entry{count=" + this.count + ", delta=" + this.delta + '}';
        }
    }

    static {
        ajc$preClinit();
    }

    public static synchronized long[] blowupTimeToSamples(List<Entry> list) {
        long[] jArr;
        synchronized (TimeToSampleBox.class) {
            SoftReference softReference = cache.get(list);
            if (softReference != null && (jArr = (long[]) softReference.get()) != null) {
                return jArr;
            }
            long j = 0;
            for (Entry count : list) {
                j += count.getCount();
            }
            long[] jArr2 = new long[((int) j)];
            int i = 0;
            for (Entry next : list) {
                int i2 = i;
                int i3 = 0;
                while (((long) i3) < next.getCount()) {
                    jArr2[i2] = next.getDelta();
                    i3++;
                    i2++;
                }
                i = i2;
            }
            cache.put(list, new SoftReference(jArr2));
            return jArr2;
        }
    }
}
