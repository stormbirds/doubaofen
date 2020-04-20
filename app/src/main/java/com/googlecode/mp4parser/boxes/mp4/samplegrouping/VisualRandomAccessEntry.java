package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import java.nio.ByteBuffer;
import kotlin.jvm.internal.ByteCompanionObject;

public class VisualRandomAccessEntry extends GroupEntry {
    public static final String TYPE = "rap ";
    private short numLeadingSamples;
    private boolean numLeadingSamplesKnown;

    public String getType() {
        return TYPE;
    }

    public boolean isNumLeadingSamplesKnown() {
        return this.numLeadingSamplesKnown;
    }

    public void setNumLeadingSamplesKnown(boolean z) {
        this.numLeadingSamplesKnown = z;
    }

    public short getNumLeadingSamples() {
        return this.numLeadingSamples;
    }

    public void setNumLeadingSamples(short s) {
        this.numLeadingSamples = s;
    }

    public void parse(ByteBuffer byteBuffer) {
        byte b = byteBuffer.get();
        this.numLeadingSamplesKnown = (b & ByteCompanionObject.MIN_VALUE) == 128;
        this.numLeadingSamples = (short) (b & ByteCompanionObject.MAX_VALUE);
    }

    public ByteBuffer get() {
        ByteBuffer allocate = ByteBuffer.allocate(1);
        allocate.put((byte) ((this.numLeadingSamplesKnown ? (short) 128 : 0) | (this.numLeadingSamples & 127)));
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
        VisualRandomAccessEntry visualRandomAccessEntry = (VisualRandomAccessEntry) obj;
        return this.numLeadingSamples == visualRandomAccessEntry.numLeadingSamples && this.numLeadingSamplesKnown == visualRandomAccessEntry.numLeadingSamplesKnown;
    }

    public int hashCode() {
        return ((this.numLeadingSamplesKnown ? 1 : 0) * true) + this.numLeadingSamples;
    }

    public String toString() {
        return "VisualRandomAccessEntry" + "{numLeadingSamplesKnown=" + this.numLeadingSamplesKnown + ", numLeadingSamples=" + this.numLeadingSamples + '}';
    }
}
