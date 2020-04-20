package com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.util.CastUtils;
import com.mp4parser.streaming.WriteOnlyBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractCueBox extends WriteOnlyBox {
    String content = "";

    public AbstractCueBox(String str) {
        super(str);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public long getSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.content) + 8);
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(getSize()));
        IsoTypeWriter.writeUInt32(allocate, getSize());
        allocate.put(IsoFile.fourCCtoBytes(getType()));
        allocate.put(Utf8.convert(this.content));
        writableByteChannel.write((ByteBuffer) allocate.rewind());
    }
}
