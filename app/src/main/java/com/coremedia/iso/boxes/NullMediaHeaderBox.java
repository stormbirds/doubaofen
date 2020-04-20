package com.coremedia.iso.boxes;

import java.nio.ByteBuffer;

public class NullMediaHeaderBox extends AbstractMediaHeaderBox {
    public static final String TYPE = "nmhd";

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 4;
    }

    public NullMediaHeaderBox() {
        super(TYPE);
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
    }
}
