package com.googlecode.mp4parser.boxes.piff;

import com.coremedia.iso.Hex;
import com.googlecode.mp4parser.contentprotection.GenericHeader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ProtectionSpecificHeader {
    protected static Map<UUID, Class<? extends ProtectionSpecificHeader>> uuidRegistry = new HashMap();

    public abstract ByteBuffer getData();

    public abstract UUID getSystemId();

    public abstract void parse(ByteBuffer byteBuffer);

    public boolean equals(Object obj) {
        throw new RuntimeException("somebody called equals on me but that's not supposed to happen.");
    }

    public static ProtectionSpecificHeader createFor(UUID uuid, ByteBuffer byteBuffer) {
        ProtectionSpecificHeader protectionSpecificHeader;
        Class cls = uuidRegistry.get(uuid);
        if (cls != null) {
            try {
                protectionSpecificHeader = (ProtectionSpecificHeader) cls.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        } else {
            protectionSpecificHeader = null;
        }
        if (protectionSpecificHeader == null) {
            protectionSpecificHeader = new GenericHeader();
        }
        protectionSpecificHeader.parse(byteBuffer);
        return protectionSpecificHeader;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProtectionSpecificHeader");
        sb.append("{data=");
        ByteBuffer duplicate = getData().duplicate();
        duplicate.rewind();
        byte[] bArr = new byte[duplicate.limit()];
        duplicate.get(bArr);
        sb.append(Hex.encodeHex(bArr));
        sb.append('}');
        return sb.toString();
    }
}
