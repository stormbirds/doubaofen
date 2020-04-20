package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class EditListBox extends AbstractFullBox {
    public static final String TYPE = "elst";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private List<Entry> entries = new LinkedList();

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("EditListBox.java", EditListBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.EditListBox", "", "", "", "java.util.List"), 68);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.EditListBox", "java.util.List", "entries", "", "void"), 72);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.EditListBox", "", "", "", "java.lang.String"), 108);
    }

    public EditListBox() {
        super(TYPE);
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) list));
        this.entries = list;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        int i;
        if (getVersion() == 1) {
            i = this.entries.size() * 20;
        } else {
            i = this.entries.size() * 12;
        }
        return ((long) i) + 8;
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        int l2i = CastUtils.l2i(IsoTypeReader.readUInt32(byteBuffer));
        this.entries = new LinkedList();
        for (int i = 0; i < l2i; i++) {
            this.entries.add(new Entry(this, byteBuffer));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry content : this.entries) {
            content.getContent(byteBuffer);
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "EditListBox{entries=" + this.entries + '}';
    }

    public static class Entry {
        EditListBox editListBox;
        private double mediaRate;
        private long mediaTime;
        private long segmentDuration;

        public Entry(EditListBox editListBox2, long j, long j2, double d) {
            this.segmentDuration = j;
            this.mediaTime = j2;
            this.mediaRate = d;
            this.editListBox = editListBox2;
        }

        public Entry(EditListBox editListBox2, ByteBuffer byteBuffer) {
            if (editListBox2.getVersion() == 1) {
                this.segmentDuration = IsoTypeReader.readUInt64(byteBuffer);
                this.mediaTime = byteBuffer.getLong();
                this.mediaRate = IsoTypeReader.readFixedPoint1616(byteBuffer);
            } else {
                this.segmentDuration = IsoTypeReader.readUInt32(byteBuffer);
                this.mediaTime = (long) byteBuffer.getInt();
                this.mediaRate = IsoTypeReader.readFixedPoint1616(byteBuffer);
            }
            this.editListBox = editListBox2;
        }

        public long getSegmentDuration() {
            return this.segmentDuration;
        }

        public void setSegmentDuration(long j) {
            this.segmentDuration = j;
        }

        public long getMediaTime() {
            return this.mediaTime;
        }

        public void setMediaTime(long j) {
            this.mediaTime = j;
        }

        public double getMediaRate() {
            return this.mediaRate;
        }

        public void setMediaRate(double d) {
            this.mediaRate = d;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.mediaTime == entry.mediaTime && this.segmentDuration == entry.segmentDuration;
        }

        public int hashCode() {
            long j = this.segmentDuration;
            long j2 = this.mediaTime;
            return (((int) (j ^ (j >>> 32))) * 31) + ((int) (j2 ^ (j2 >>> 32)));
        }

        public void getContent(ByteBuffer byteBuffer) {
            if (this.editListBox.getVersion() == 1) {
                IsoTypeWriter.writeUInt64(byteBuffer, this.segmentDuration);
                byteBuffer.putLong(this.mediaTime);
            } else {
                IsoTypeWriter.writeUInt32(byteBuffer, (long) CastUtils.l2i(this.segmentDuration));
                byteBuffer.putInt(CastUtils.l2i(this.mediaTime));
            }
            IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.mediaRate);
        }

        public String toString() {
            return "Entry{segmentDuration=" + this.segmentDuration + ", mediaTime=" + this.mediaTime + ", mediaRate=" + this.mediaRate + '}';
        }
    }
}
