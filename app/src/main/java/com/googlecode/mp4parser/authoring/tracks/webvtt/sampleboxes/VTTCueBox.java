package com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.mp4parser.streaming.WriteOnlyBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class VTTCueBox extends WriteOnlyBox {
    CueIDBox cueIDBox;
    CuePayloadBox cuePayloadBox;
    CueSettingsBox cueSettingsBox;
    CueSourceIDBox cueSourceIDBox;
    CueTimeBox cueTimeBox;

    public VTTCueBox() {
        super("vtcc");
    }

    public long getSize() {
        CueSourceIDBox cueSourceIDBox2 = this.cueSourceIDBox;
        long j = 0;
        long size = (cueSourceIDBox2 != null ? cueSourceIDBox2.getSize() : 0) + 8;
        CueIDBox cueIDBox2 = this.cueIDBox;
        long size2 = size + (cueIDBox2 != null ? cueIDBox2.getSize() : 0);
        CueTimeBox cueTimeBox2 = this.cueTimeBox;
        long size3 = size2 + (cueTimeBox2 != null ? cueTimeBox2.getSize() : 0);
        CueSettingsBox cueSettingsBox2 = this.cueSettingsBox;
        long size4 = size3 + (cueSettingsBox2 != null ? cueSettingsBox2.getSize() : 0);
        CuePayloadBox cuePayloadBox2 = this.cuePayloadBox;
        if (cuePayloadBox2 != null) {
            j = cuePayloadBox2.getSize();
        }
        return size4 + j;
    }

    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt32(allocate, getSize());
        allocate.put(IsoFile.fourCCtoBytes(getType()));
        writableByteChannel.write((ByteBuffer) allocate.rewind());
        CueSourceIDBox cueSourceIDBox2 = this.cueSourceIDBox;
        if (cueSourceIDBox2 != null) {
            cueSourceIDBox2.getBox(writableByteChannel);
        }
        CueIDBox cueIDBox2 = this.cueIDBox;
        if (cueIDBox2 != null) {
            cueIDBox2.getBox(writableByteChannel);
        }
        CueTimeBox cueTimeBox2 = this.cueTimeBox;
        if (cueTimeBox2 != null) {
            cueTimeBox2.getBox(writableByteChannel);
        }
        CueSettingsBox cueSettingsBox2 = this.cueSettingsBox;
        if (cueSettingsBox2 != null) {
            cueSettingsBox2.getBox(writableByteChannel);
        }
        CuePayloadBox cuePayloadBox2 = this.cuePayloadBox;
        if (cuePayloadBox2 != null) {
            cuePayloadBox2.getBox(writableByteChannel);
        }
    }

    public CueSourceIDBox getCueSourceIDBox() {
        return this.cueSourceIDBox;
    }

    public void setCueSourceIDBox(CueSourceIDBox cueSourceIDBox2) {
        this.cueSourceIDBox = cueSourceIDBox2;
    }

    public CueIDBox getCueIDBox() {
        return this.cueIDBox;
    }

    public void setCueIDBox(CueIDBox cueIDBox2) {
        this.cueIDBox = cueIDBox2;
    }

    public CueTimeBox getCueTimeBox() {
        return this.cueTimeBox;
    }

    public void setCueTimeBox(CueTimeBox cueTimeBox2) {
        this.cueTimeBox = cueTimeBox2;
    }

    public CueSettingsBox getCueSettingsBox() {
        return this.cueSettingsBox;
    }

    public void setCueSettingsBox(CueSettingsBox cueSettingsBox2) {
        this.cueSettingsBox = cueSettingsBox2;
    }

    public CuePayloadBox getCuePayloadBox() {
        return this.cuePayloadBox;
    }

    public void setCuePayloadBox(CuePayloadBox cuePayloadBox2) {
        this.cuePayloadBox = cuePayloadBox2;
    }
}
