package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class RateShareEntry extends GroupEntry {
    public static final String TYPE = "rash";
    private short discardPriority;
    private List<Entry> entries = new LinkedList();
    private int maximumBitrate;
    private int minimumBitrate;
    private short operationPointCut;
    private short targetRateShare;

    public String getType() {
        return TYPE;
    }

    /* JADX WARNING: type inference failed for: r1v1, types: [int] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parse(java.nio.ByteBuffer r6) {
        /*
            r5 = this;
            short r0 = r6.getShort()
            r5.operationPointCut = r0
            short r0 = r5.operationPointCut
            r1 = 1
            if (r0 != r1) goto L_0x0012
            short r0 = r6.getShort()
            r5.targetRateShare = r0
            goto L_0x0016
        L_0x0012:
            int r1 = r0 + -1
            if (r0 > 0) goto L_0x0032
        L_0x0016:
            long r0 = com.coremedia.iso.IsoTypeReader.readUInt32(r6)
            int r0 = com.googlecode.mp4parser.util.CastUtils.l2i(r0)
            r5.maximumBitrate = r0
            long r0 = com.coremedia.iso.IsoTypeReader.readUInt32(r6)
            int r0 = com.googlecode.mp4parser.util.CastUtils.l2i(r0)
            r5.minimumBitrate = r0
            int r6 = com.coremedia.iso.IsoTypeReader.readUInt8(r6)
            short r6 = (short) r6
            r5.discardPriority = r6
            return
        L_0x0032:
            java.util.List<com.googlecode.mp4parser.boxes.mp4.samplegrouping.RateShareEntry$Entry> r0 = r5.entries
            com.googlecode.mp4parser.boxes.mp4.samplegrouping.RateShareEntry$Entry r2 = new com.googlecode.mp4parser.boxes.mp4.samplegrouping.RateShareEntry$Entry
            long r3 = com.coremedia.iso.IsoTypeReader.readUInt32(r6)
            int r3 = com.googlecode.mp4parser.util.CastUtils.l2i(r3)
            short r4 = r6.getShort()
            r2.<init>(r3, r4)
            r0.add(r2)
            r0 = r1
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.boxes.mp4.samplegrouping.RateShareEntry.parse(java.nio.ByteBuffer):void");
    }

    public ByteBuffer get() {
        short s = this.operationPointCut;
        ByteBuffer allocate = ByteBuffer.allocate(s == 1 ? 13 : (s * 6) + 11);
        allocate.putShort(this.operationPointCut);
        if (this.operationPointCut == 1) {
            allocate.putShort(this.targetRateShare);
        } else {
            for (Entry next : this.entries) {
                allocate.putInt(next.getAvailableBitrate());
                allocate.putShort(next.getTargetRateShare());
            }
        }
        allocate.putInt(this.maximumBitrate);
        allocate.putInt(this.minimumBitrate);
        IsoTypeWriter.writeUInt8(allocate, this.discardPriority);
        allocate.rewind();
        return allocate;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RateShareEntry rateShareEntry = (RateShareEntry) obj;
        if (this.discardPriority != rateShareEntry.discardPriority || this.maximumBitrate != rateShareEntry.maximumBitrate || this.minimumBitrate != rateShareEntry.minimumBitrate || this.operationPointCut != rateShareEntry.operationPointCut || this.targetRateShare != rateShareEntry.targetRateShare) {
            return false;
        }
        List<Entry> list = this.entries;
        List<Entry> list2 = rateShareEntry.entries;
        return list == null ? list2 == null : list.equals(list2);
    }

    public int hashCode() {
        int i = ((this.operationPointCut * 31) + this.targetRateShare) * 31;
        List<Entry> list = this.entries;
        return ((((((i + (list != null ? list.hashCode() : 0)) * 31) + this.maximumBitrate) * 31) + this.minimumBitrate) * 31) + this.discardPriority;
    }

    public short getOperationPointCut() {
        return this.operationPointCut;
    }

    public void setOperationPointCut(short s) {
        this.operationPointCut = s;
    }

    public short getTargetRateShare() {
        return this.targetRateShare;
    }

    public void setTargetRateShare(short s) {
        this.targetRateShare = s;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        this.entries = list;
    }

    public int getMaximumBitrate() {
        return this.maximumBitrate;
    }

    public void setMaximumBitrate(int i) {
        this.maximumBitrate = i;
    }

    public int getMinimumBitrate() {
        return this.minimumBitrate;
    }

    public void setMinimumBitrate(int i) {
        this.minimumBitrate = i;
    }

    public short getDiscardPriority() {
        return this.discardPriority;
    }

    public void setDiscardPriority(short s) {
        this.discardPriority = s;
    }

    public static class Entry {
        int availableBitrate;
        short targetRateShare;

        public Entry(int i, short s) {
            this.availableBitrate = i;
            this.targetRateShare = s;
        }

        public String toString() {
            return "{availableBitrate=" + this.availableBitrate + ", targetRateShare=" + this.targetRateShare + '}';
        }

        public int getAvailableBitrate() {
            return this.availableBitrate;
        }

        public void setAvailableBitrate(int i) {
            this.availableBitrate = i;
        }

        public short getTargetRateShare() {
            return this.targetRateShare;
        }

        public void setTargetRateShare(short s) {
            this.targetRateShare = s;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.availableBitrate == entry.availableBitrate && this.targetRateShare == entry.targetRateShare;
        }

        public int hashCode() {
            return (this.availableBitrate * 31) + this.targetRateShare;
        }
    }
}
