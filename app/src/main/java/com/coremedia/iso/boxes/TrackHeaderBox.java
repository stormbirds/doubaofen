package com.coremedia.iso.boxes;

import android.support.v7.widget.helper.ItemTouchHelper;
import com.alibaba.fastjson.asm.Opcodes;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.DateHelper;
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Matrix;
import java.nio.ByteBuffer;
import java.util.Date;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackHeaderBox extends AbstractFullBox {
    private static Logger LOG = Logger.getLogger(TrackHeaderBox.class);
    public static final String TYPE = "tkhd";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_14 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_15 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_16 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_17 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_18 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_19 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_20 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_21 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_22 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_23 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_24 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_25 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_26 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_27 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_28 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_29 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_9 = null;
    private int alternateGroup;
    private Date creationTime = new Date(0);
    private long duration;
    private double height;
    private int layer;
    private Matrix matrix = Matrix.ROTATE_0;
    private Date modificationTime = new Date(0);
    private long trackId;
    private float volume;
    private double width;

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("TrackHeaderBox.java", TrackHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 62);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 66);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getContent", "com.coremedia.iso.boxes.TrackHeaderBox", "java.nio.ByteBuffer", "byteBuffer", "", "void"), 145);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.lang.String"), 173);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "creationTime", "", "void"), (int) Opcodes.IFNONNULL);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "modificationTime", "", "void"), 206);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "trackId", "", "void"), 214);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "duration", "", "void"), 218);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "layer", "", "void"), 225);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "alternateGroup", "", "void"), 229);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "float", "volume", "", "void"), 233);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "com.googlecode.mp4parser.util.Matrix", "matrix", "", "void"), 237);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 70);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "width", "", "void"), 241);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "height", "", "void"), 245);
        ajc$tjp_22 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), (int) ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        ajc$tjp_23 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 254);
        ajc$tjp_24 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 258);
        ajc$tjp_25 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "isInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 262);
        ajc$tjp_26 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "enabled", "", "void"), 266);
        ajc$tjp_27 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inMovie", "", "void"), 274);
        ajc$tjp_28 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPreview", "", "void"), 282);
        ajc$tjp_29 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "setInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPoster", "", "void"), 290);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 74);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 78);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 82);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "float"), 86);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "com.googlecode.mp4parser.util.Matrix"), 90);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 94);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig("1", "getHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 98);
    }

    static {
        ajc$preClinit();
    }

    public TrackHeaderBox() {
        super(TYPE);
    }

    public Date getCreationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.creationTime;
    }

    public Date getModificationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.modificationTime;
    }

    public long getTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.trackId;
    }

    public long getDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.duration;
    }

    public int getLayer() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.layer;
    }

    public int getAlternateGroup() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.alternateGroup;
    }

    public float getVolume() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.volume;
    }

    public Matrix getMatrix() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.matrix;
    }

    public double getWidth() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.width;
    }

    public double getHeight() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return this.height;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (getVersion() == 1 ? 36 : 24) + 60;
    }

    public void _parseDetails(ByteBuffer byteBuffer) {
        parseVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt64(byteBuffer));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt64(byteBuffer));
            this.trackId = IsoTypeReader.readUInt32(byteBuffer);
            IsoTypeReader.readUInt32(byteBuffer);
            this.duration = byteBuffer.getLong();
        } else {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt32(byteBuffer));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt32(byteBuffer));
            this.trackId = IsoTypeReader.readUInt32(byteBuffer);
            IsoTypeReader.readUInt32(byteBuffer);
            this.duration = (long) byteBuffer.getInt();
        }
        if (this.duration < -1) {
            LOG.logWarn("tkhd duration is not in expected range");
        }
        IsoTypeReader.readUInt32(byteBuffer);
        IsoTypeReader.readUInt32(byteBuffer);
        this.layer = IsoTypeReader.readUInt16(byteBuffer);
        this.alternateGroup = IsoTypeReader.readUInt16(byteBuffer);
        this.volume = IsoTypeReader.readFixedPoint88(byteBuffer);
        IsoTypeReader.readUInt16(byteBuffer);
        this.matrix = Matrix.fromByteBuffer(byteBuffer);
        this.width = IsoTypeReader.readFixedPoint1616(byteBuffer);
        this.height = IsoTypeReader.readFixedPoint1616(byteBuffer);
    }

    public void getContent(ByteBuffer byteBuffer) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, (Object) byteBuffer));
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
            IsoTypeWriter.writeUInt32(byteBuffer, 0);
            byteBuffer.putLong(this.duration);
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
            IsoTypeWriter.writeUInt32(byteBuffer, 0);
            byteBuffer.putInt((int) this.duration);
        }
        IsoTypeWriter.writeUInt32(byteBuffer, 0);
        IsoTypeWriter.writeUInt32(byteBuffer, 0);
        IsoTypeWriter.writeUInt16(byteBuffer, this.layer);
        IsoTypeWriter.writeUInt16(byteBuffer, this.alternateGroup);
        IsoTypeWriter.writeFixedPoint88(byteBuffer, (double) this.volume);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
        this.matrix.getContent(byteBuffer);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.width);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.height);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, this, this));
        return "TrackHeaderBox[" + "creationTime=" + getCreationTime() + ";" + "modificationTime=" + getModificationTime() + ";" + "trackId=" + getTrackId() + ";" + "duration=" + getDuration() + ";" + "layer=" + getLayer() + ";" + "alternateGroup=" + getAlternateGroup() + ";" + "volume=" + getVolume() + ";" + "matrix=" + this.matrix + ";" + "width=" + getWidth() + ";" + "height=" + getHeight() + "]";
    }

    public void setCreationTime(Date date) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, (Object) date));
        this.creationTime = date;
        if (DateHelper.convert(date) >= 4294967296L) {
            setVersion(1);
        }
    }

    public void setModificationTime(Date date) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, (Object) date));
        this.modificationTime = date;
        if (DateHelper.convert(date) >= 4294967296L) {
            setVersion(1);
        }
    }

    public void setTrackId(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.longObject(j)));
        this.trackId = j;
    }

    public void setDuration(long j) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, Conversions.longObject(j)));
        this.duration = j;
        if (j >= 4294967296L) {
            setFlags(1);
        }
    }

    public void setLayer(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, (Object) this, (Object) this, Conversions.intObject(i)));
        this.layer = i;
    }

    public void setAlternateGroup(int i) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, Conversions.intObject(i)));
        this.alternateGroup = i;
    }

    public void setVolume(float f) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, (Object) this, (Object) this, Conversions.floatObject(f)));
        this.volume = f;
    }

    public void setMatrix(Matrix matrix2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, (Object) matrix2));
        this.matrix = matrix2;
    }

    public void setWidth(double d) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, (Object) this, (Object) this, Conversions.doubleObject(d)));
        this.width = d;
    }

    public void setHeight(double d) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, (Object) this, (Object) this, Conversions.doubleObject(d)));
        this.height = d;
    }

    public boolean isEnabled() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_22, this, this));
        return (getFlags() & 1) > 0;
    }

    public boolean isInMovie() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_23, this, this));
        return (getFlags() & 2) > 0;
    }

    public boolean isInPreview() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_24, this, this));
        return (getFlags() & 4) > 0;
    }

    public boolean isInPoster() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_25, this, this));
        return (getFlags() & 8) > 0;
    }

    public void setEnabled(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_26, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 1);
        } else {
            setFlags(getFlags() & -2);
        }
    }

    public void setInMovie(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_27, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 2);
        } else {
            setFlags(getFlags() & -3);
        }
    }

    public void setInPreview(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_28, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 4);
        } else {
            setFlags(getFlags() & -5);
        }
    }

    public void setInPoster(boolean z) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_29, (Object) this, (Object) this, Conversions.booleanObject(z)));
        if (z) {
            setFlags(getFlags() | 8);
        } else {
            setFlags(getFlags() & -9);
        }
    }
}
