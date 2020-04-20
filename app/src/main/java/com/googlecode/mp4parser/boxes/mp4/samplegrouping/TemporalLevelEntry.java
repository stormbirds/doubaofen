package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import java.nio.ByteBuffer;
import kotlin.jvm.internal.ByteCompanionObject;

public class TemporalLevelEntry extends GroupEntry {
    public static final String TYPE = "tele";
    private boolean levelIndependentlyDecodable;
    private short reserved;

    public String getType() {
        return TYPE;
    }

    public boolean isLevelIndependentlyDecodable() {
        return this.levelIndependentlyDecodable;
    }

    public void setLevelIndependentlyDecodable(boolean z) {
        this.levelIndependentlyDecodable = z;
    }

    public void parse(ByteBuffer byteBuffer) {
        this.levelIndependentlyDecodable = (byteBuffer.get() & ByteCompanionObject.MIN_VALUE) == 128;
    }

    public ByteBuffer get() {
        ByteBuffer allocate = ByteBuffer.allocate(1);
        allocate.put((byte) (this.levelIndependentlyDecodable ? 128 : 0));
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
        TemporalLevelEntry temporalLevelEntry = (TemporalLevelEntry) obj;
        return this.levelIndependentlyDecodable == temporalLevelEntry.levelIndependentlyDecodable && this.reserved == temporalLevelEntry.reserved;
    }

    public int hashCode() {
        return ((this.levelIndependentlyDecodable ? 1 : 0) * true) + this.reserved;
    }

    public String toString() {
        return "TemporalLevelEntry" + "{levelIndependentlyDecodable=" + this.levelIndependentlyDecodable + '}';
    }
}
