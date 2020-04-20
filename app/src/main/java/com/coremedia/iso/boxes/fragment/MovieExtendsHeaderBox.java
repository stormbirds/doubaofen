package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class MovieExtendsHeaderBox extends AbstractFullBox {
    public static final String TYPE = "mehd";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private long fragmentDuration;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("MovieExtendsHeaderBox.java", MovieExtendsHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getFragmentDuration", "com.coremedia.iso.boxes.fragment.MovieExtendsHeaderBox", "", "", "", "long"), 65);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setFragmentDuration", "com.coremedia.iso.boxes.fragment.MovieExtendsHeaderBox", "long", "fragmentDuration", "", "void"), 69);
    }

    public MovieExtendsHeaderBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (getVersion() == 1 ? 12 : 8);
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.fragmentDuration = getVersion() == 1 ? IsoTypeReader.readUInt64(byteBuffer) : IsoTypeReader.readUInt32(byteBuffer);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, this.fragmentDuration);
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, this.fragmentDuration);
        }
    }

    public long getFragmentDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.fragmentDuration;
    }

    public void setFragmentDuration(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.longObject(j)));
        this.fragmentDuration = j;
    }
}
