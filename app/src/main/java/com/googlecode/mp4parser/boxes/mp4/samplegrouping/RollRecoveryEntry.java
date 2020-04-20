package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import java.nio.ByteBuffer;

public class RollRecoveryEntry extends GroupEntry {
    public static final String TYPE = "roll";
    private short rollDistance;

    public String getType() {
        return TYPE;
    }

    public short getRollDistance() {
        return this.rollDistance;
    }

    public void setRollDistance(short s) {
        this.rollDistance = s;
    }

    public void parse(ByteBuffer byteBuffer) {
        this.rollDistance = byteBuffer.getShort();
    }

    public ByteBuffer get() {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.putShort(this.rollDistance);
        allocate.rewind();
        return allocate;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.rollDistance == ((RollRecoveryEntry) obj).rollDistance;
    }

    public int hashCode() {
        return this.rollDistance;
    }
}
