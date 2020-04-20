package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.util.UUIDConverter;
import java.nio.ByteBuffer;
import java.util.UUID;

public class CencSampleEncryptionInformationGroupEntry extends GroupEntry {
    public static final String TYPE = "seig";
    private boolean isEncrypted;
    private byte ivSize;
    private UUID kid;

    public String getType() {
        return TYPE;
    }

    public void parse(ByteBuffer byteBuffer) {
        boolean z = true;
        if (IsoTypeReader.readUInt24(byteBuffer) != 1) {
            z = false;
        }
        this.isEncrypted = z;
        this.ivSize = (byte) IsoTypeReader.readUInt8(byteBuffer);
        byte[] bArr = new byte[16];
        byteBuffer.get(bArr);
        this.kid = UUIDConverter.convert(bArr);
    }

    public ByteBuffer get() {
        ByteBuffer allocate = ByteBuffer.allocate(20);
        IsoTypeWriter.writeUInt24(allocate, this.isEncrypted ? 1 : 0);
        if (this.isEncrypted) {
            IsoTypeWriter.writeUInt8(allocate, this.ivSize);
            allocate.put(UUIDConverter.convert(this.kid));
        } else {
            allocate.put(new byte[17]);
        }
        allocate.rewind();
        return allocate;
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public void setEncrypted(boolean z) {
        this.isEncrypted = z;
    }

    public byte getIvSize() {
        return this.ivSize;
    }

    public void setIvSize(int i) {
        this.ivSize = (byte) i;
    }

    public UUID getKid() {
        return this.kid;
    }

    public void setKid(UUID uuid) {
        this.kid = uuid;
    }

    public String toString() {
        return "CencSampleEncryptionInformationGroupEntry{isEncrypted=" + this.isEncrypted + ", ivSize=" + this.ivSize + ", kid=" + this.kid + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CencSampleEncryptionInformationGroupEntry cencSampleEncryptionInformationGroupEntry = (CencSampleEncryptionInformationGroupEntry) obj;
        if (this.isEncrypted != cencSampleEncryptionInformationGroupEntry.isEncrypted || this.ivSize != cencSampleEncryptionInformationGroupEntry.ivSize) {
            return false;
        }
        UUID uuid = this.kid;
        UUID uuid2 = cencSampleEncryptionInformationGroupEntry.kid;
        return uuid == null ? uuid2 == null : uuid.equals(uuid2);
    }

    public int hashCode() {
        int i = (((this.isEncrypted ? 7 : 19) * 31) + this.ivSize) * 31;
        UUID uuid = this.kid;
        return i + (uuid != null ? uuid.hashCode() : 0);
    }
}
