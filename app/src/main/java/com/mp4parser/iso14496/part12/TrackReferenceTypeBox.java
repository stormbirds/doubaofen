package com.mp4parser.iso14496.part12;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.Mp4Arrays;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackReferenceTypeBox extends AbstractBox {
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    long[] trackIds = new long[0];

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("TrackReferenceTypeBox.java", TrackReferenceTypeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getTrackIds", "com.mp4parser.iso14496.part12.TrackReferenceTypeBox", "", "", "", "[J"), 58);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setTrackIds", "com.mp4parser.iso14496.part12.TrackReferenceTypeBox", "[J", "trackIds", "", "void"), 62);
    }

    public TrackReferenceTypeBox(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (this.trackIds.length * 4);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        for (long writeUInt32 : this.trackIds) {
            IsoTypeWriter.writeUInt32(byteBuffer, writeUInt32);
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer byteBuffer) {
        while (byteBuffer.remaining() >= 4) {
            this.trackIds = Mp4Arrays.copyOfAndAppend(this.trackIds, IsoTypeReader.readUInt32(byteBuffer));
        }
    }

    public long[] getTrackIds() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.trackIds;
    }

    public void setTrackIds(long[] jArr) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) jArr));
        this.trackIds = jArr;
    }
}
