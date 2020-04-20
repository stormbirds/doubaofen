package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {6})
public class SLConfigDescriptor extends BaseDescriptor {
    int predefined;

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        return 1;
    }

    public SLConfigDescriptor() {
        this.tag = 6;
    }

    public int getPredefined() {
        return this.predefined;
    }

    public void setPredefined(int i) {
        this.predefined = i;
    }

    public void parseDetail(ByteBuffer byteBuffer) throws IOException {
        this.predefined = IsoTypeReader.readUInt8(byteBuffer);
    }

    public ByteBuffer serialize() {
        ByteBuffer allocate = ByteBuffer.allocate(getSize());
        IsoTypeWriter.writeUInt8(allocate, 6);
        writeSize(allocate, getContentSize());
        IsoTypeWriter.writeUInt8(allocate, this.predefined);
        return allocate;
    }

    public String toString() {
        return "SLConfigDescriptor" + "{predefined=" + this.predefined + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.predefined == ((SLConfigDescriptor) obj).predefined;
    }

    public int hashCode() {
        return this.predefined;
    }
}
