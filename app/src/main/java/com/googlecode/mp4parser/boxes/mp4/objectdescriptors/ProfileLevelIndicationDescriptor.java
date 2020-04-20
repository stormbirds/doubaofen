package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {20})
public class ProfileLevelIndicationDescriptor extends BaseDescriptor {
    int profileLevelIndicationIndex;

    public int getContentSize() {
        return 1;
    }

    public ProfileLevelIndicationDescriptor() {
        this.tag = 20;
    }

    public void parseDetail(ByteBuffer byteBuffer) throws IOException {
        this.profileLevelIndicationIndex = IsoTypeReader.readUInt8(byteBuffer);
    }

    public ByteBuffer serialize() {
        ByteBuffer allocate = ByteBuffer.allocate(getSize());
        IsoTypeWriter.writeUInt8(allocate, 20);
        writeSize(allocate, getContentSize());
        IsoTypeWriter.writeUInt8(allocate, this.profileLevelIndicationIndex);
        return allocate;
    }

    public String toString() {
        return "ProfileLevelIndicationDescriptor" + "{profileLevelIndicationIndex=" + Integer.toHexString(this.profileLevelIndicationIndex) + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.profileLevelIndicationIndex == ((ProfileLevelIndicationDescriptor) obj).profileLevelIndicationIndex;
    }

    public int hashCode() {
        return this.profileLevelIndicationIndex;
    }
}
