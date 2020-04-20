package com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.mp4parser.streaming.WriteOnlyBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class VTTEmptyCueBox extends WriteOnlyBox {
    public long getSize() {
        return 8;
    }

    public VTTEmptyCueBox() {
        super("vtte");
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt32(allocate, getSize());
        allocate.put(IsoFile.fourCCtoBytes(getType()));
        writableByteChannel.write((ByteBuffer) allocate.rewind());
    }
}
