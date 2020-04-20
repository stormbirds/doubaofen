package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {3})
public class ESDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ESDescriptor.class.getName());
    int URLFlag;
    int URLLength = 0;
    String URLString;
    DecoderConfigDescriptor decoderConfigDescriptor;
    int dependsOnEsId;
    int esId;
    int oCREsId;
    int oCRstreamFlag;
    List<BaseDescriptor> otherDescriptors = new ArrayList();
    int remoteODFlag;
    SLConfigDescriptor slConfigDescriptor;
    int streamDependenceFlag;
    int streamPriority;

    public ESDescriptor() {
        this.tag = 3;
    }

    public void parseDetail(ByteBuffer byteBuffer) throws IOException {
        this.esId = IsoTypeReader.readUInt16(byteBuffer);
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        this.streamDependenceFlag = readUInt8 >>> 7;
        this.URLFlag = (readUInt8 >>> 6) & 1;
        this.oCRstreamFlag = (readUInt8 >>> 5) & 1;
        this.streamPriority = readUInt8 & 31;
        if (this.streamDependenceFlag == 1) {
            this.dependsOnEsId = IsoTypeReader.readUInt16(byteBuffer);
        }
        if (this.URLFlag == 1) {
            this.URLLength = IsoTypeReader.readUInt8(byteBuffer);
            this.URLString = IsoTypeReader.readString(byteBuffer, this.URLLength);
        }
        if (this.oCRstreamFlag == 1) {
            this.oCREsId = IsoTypeReader.readUInt16(byteBuffer);
        }
        while (byteBuffer.remaining() > 1) {
            BaseDescriptor createFrom = ObjectDescriptorFactory.createFrom(-1, byteBuffer);
            if (createFrom instanceof DecoderConfigDescriptor) {
                this.decoderConfigDescriptor = (DecoderConfigDescriptor) createFrom;
            } else if (createFrom instanceof SLConfigDescriptor) {
                this.slConfigDescriptor = (SLConfigDescriptor) createFrom;
            } else {
                this.otherDescriptors.add(createFrom);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        int i = this.streamDependenceFlag > 0 ? 5 : 3;
        if (this.URLFlag > 0) {
            i += this.URLLength + 1;
        }
        if (this.oCRstreamFlag > 0) {
            i += 2;
        }
        int size = i + this.decoderConfigDescriptor.getSize() + this.slConfigDescriptor.getSize();
        if (this.otherDescriptors.size() <= 0) {
            return size;
        }
        throw new RuntimeException(" Doesn't handle other descriptors yet");
    }

    public ByteBuffer serialize() {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[getSize()]);
        IsoTypeWriter.writeUInt8(wrap, 3);
        writeSize(wrap, getContentSize());
        IsoTypeWriter.writeUInt16(wrap, this.esId);
        IsoTypeWriter.writeUInt8(wrap, (this.streamDependenceFlag << 7) | (this.URLFlag << 6) | (this.oCRstreamFlag << 5) | (this.streamPriority & 31));
        if (this.streamDependenceFlag > 0) {
            IsoTypeWriter.writeUInt16(wrap, this.dependsOnEsId);
        }
        if (this.URLFlag > 0) {
            IsoTypeWriter.writeUInt8(wrap, this.URLLength);
            IsoTypeWriter.writeUtf8String(wrap, this.URLString);
        }
        if (this.oCRstreamFlag > 0) {
            IsoTypeWriter.writeUInt16(wrap, this.oCREsId);
        }
        ByteBuffer serialize = this.decoderConfigDescriptor.serialize();
        ByteBuffer serialize2 = this.slConfigDescriptor.serialize();
        wrap.put(serialize.array());
        wrap.put(serialize2.array());
        return wrap;
    }

    public DecoderConfigDescriptor getDecoderConfigDescriptor() {
        return this.decoderConfigDescriptor;
    }

    public void setDecoderConfigDescriptor(DecoderConfigDescriptor decoderConfigDescriptor2) {
        this.decoderConfigDescriptor = decoderConfigDescriptor2;
    }

    public SLConfigDescriptor getSlConfigDescriptor() {
        return this.slConfigDescriptor;
    }

    public void setSlConfigDescriptor(SLConfigDescriptor sLConfigDescriptor) {
        this.slConfigDescriptor = sLConfigDescriptor;
    }

    public List<BaseDescriptor> getOtherDescriptors() {
        return this.otherDescriptors;
    }

    public int getoCREsId() {
        return this.oCREsId;
    }

    public void setoCREsId(int i) {
        this.oCREsId = i;
    }

    public int getEsId() {
        return this.esId;
    }

    public void setEsId(int i) {
        this.esId = i;
    }

    public int getStreamDependenceFlag() {
        return this.streamDependenceFlag;
    }

    public void setStreamDependenceFlag(int i) {
        this.streamDependenceFlag = i;
    }

    public int getURLFlag() {
        return this.URLFlag;
    }

    public void setURLFlag(int i) {
        this.URLFlag = i;
    }

    public int getoCRstreamFlag() {
        return this.oCRstreamFlag;
    }

    public void setoCRstreamFlag(int i) {
        this.oCRstreamFlag = i;
    }

    public int getStreamPriority() {
        return this.streamPriority;
    }

    public void setStreamPriority(int i) {
        this.streamPriority = i;
    }

    public int getURLLength() {
        return this.URLLength;
    }

    public void setURLLength(int i) {
        this.URLLength = i;
    }

    public String getURLString() {
        return this.URLString;
    }

    public void setURLString(String str) {
        this.URLString = str;
    }

    public int getRemoteODFlag() {
        return this.remoteODFlag;
    }

    public void setRemoteODFlag(int i) {
        this.remoteODFlag = i;
    }

    public int getDependsOnEsId() {
        return this.dependsOnEsId;
    }

    public void setDependsOnEsId(int i) {
        this.dependsOnEsId = i;
    }

    public String toString() {
        return "ESDescriptor" + "{esId=" + this.esId + ", streamDependenceFlag=" + this.streamDependenceFlag + ", URLFlag=" + this.URLFlag + ", oCRstreamFlag=" + this.oCRstreamFlag + ", streamPriority=" + this.streamPriority + ", URLLength=" + this.URLLength + ", URLString='" + this.URLString + '\'' + ", remoteODFlag=" + this.remoteODFlag + ", dependsOnEsId=" + this.dependsOnEsId + ", oCREsId=" + this.oCREsId + ", decoderConfigDescriptor=" + this.decoderConfigDescriptor + ", slConfigDescriptor=" + this.slConfigDescriptor + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ESDescriptor eSDescriptor = (ESDescriptor) obj;
        if (this.URLFlag != eSDescriptor.URLFlag || this.URLLength != eSDescriptor.URLLength || this.dependsOnEsId != eSDescriptor.dependsOnEsId || this.esId != eSDescriptor.esId || this.oCREsId != eSDescriptor.oCREsId || this.oCRstreamFlag != eSDescriptor.oCRstreamFlag || this.remoteODFlag != eSDescriptor.remoteODFlag || this.streamDependenceFlag != eSDescriptor.streamDependenceFlag || this.streamPriority != eSDescriptor.streamPriority) {
            return false;
        }
        String str = this.URLString;
        if (str == null ? eSDescriptor.URLString != null : !str.equals(eSDescriptor.URLString)) {
            return false;
        }
        DecoderConfigDescriptor decoderConfigDescriptor2 = this.decoderConfigDescriptor;
        if (decoderConfigDescriptor2 == null ? eSDescriptor.decoderConfigDescriptor != null : !decoderConfigDescriptor2.equals(eSDescriptor.decoderConfigDescriptor)) {
            return false;
        }
        List<BaseDescriptor> list = this.otherDescriptors;
        if (list == null ? eSDescriptor.otherDescriptors != null : !list.equals(eSDescriptor.otherDescriptors)) {
            return false;
        }
        SLConfigDescriptor sLConfigDescriptor = this.slConfigDescriptor;
        SLConfigDescriptor sLConfigDescriptor2 = eSDescriptor.slConfigDescriptor;
        return sLConfigDescriptor == null ? sLConfigDescriptor2 == null : sLConfigDescriptor.equals(sLConfigDescriptor2);
    }

    public int hashCode() {
        int i = ((((((((((this.esId * 31) + this.streamDependenceFlag) * 31) + this.URLFlag) * 31) + this.oCRstreamFlag) * 31) + this.streamPriority) * 31) + this.URLLength) * 31;
        String str = this.URLString;
        int i2 = 0;
        int hashCode = (((((((i + (str != null ? str.hashCode() : 0)) * 31) + this.remoteODFlag) * 31) + this.dependsOnEsId) * 31) + this.oCREsId) * 31;
        DecoderConfigDescriptor decoderConfigDescriptor2 = this.decoderConfigDescriptor;
        int hashCode2 = (hashCode + (decoderConfigDescriptor2 != null ? decoderConfigDescriptor2.hashCode() : 0)) * 31;
        SLConfigDescriptor sLConfigDescriptor = this.slConfigDescriptor;
        int hashCode3 = (hashCode2 + (sLConfigDescriptor != null ? sLConfigDescriptor.hashCode() : 0)) * 31;
        List<BaseDescriptor> list = this.otherDescriptors;
        if (list != null) {
            i2 = list.hashCode();
        }
        return hashCode3 + i2;
    }
}
