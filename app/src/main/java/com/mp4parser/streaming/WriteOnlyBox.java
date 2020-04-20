package com.mp4parser.streaming;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class WriteOnlyBox implements Box {
    private Container parent;
    private final String type;

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    public long getOffset() {
        throw new RuntimeException("It's a´write only box");
    }

    public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        throw new RuntimeException("It's a´write only box");
    }

    public WriteOnlyBox(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }
}
