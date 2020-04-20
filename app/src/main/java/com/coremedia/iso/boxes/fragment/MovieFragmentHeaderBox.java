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

public class MovieFragmentHeaderBox extends AbstractFullBox {
    public static final String TYPE = "mfhd";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private long sequenceNumber;

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("MovieFragmentHeaderBox.java", MovieFragmentHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getSequenceNumber", "com.coremedia.iso.boxes.fragment.MovieFragmentHeaderBox", "", "", "", "long"), 59);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setSequenceNumber", "com.coremedia.iso.boxes.fragment.MovieFragmentHeaderBox", "long", "sequenceNumber", "", "void"), 63);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.fragment.MovieFragmentHeaderBox", "", "", "", "java.lang.String"), 68);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 8;
    }

    public MovieFragmentHeaderBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.sequenceNumber);
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        this.sequenceNumber = IsoTypeReader.readUInt32(byteBuffer);
    }

    public long getSequenceNumber() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.sequenceNumber;
    }

    public void setSequenceNumber(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.longObject(j)));
        this.sequenceNumber = j;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "MovieFragmentHeaderBox{sequenceNumber=" + this.sequenceNumber + '}';
    }
}
