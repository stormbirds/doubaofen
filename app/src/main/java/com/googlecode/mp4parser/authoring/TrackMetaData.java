package com.googlecode.mp4parser.authoring;

import com.googlecode.mp4parser.util.Matrix;
import java.util.Date;

public class TrackMetaData implements Cloneable {
    private Date creationTime = new Date();
    private int group = 0;
    private double height;
    private String language = "eng";
    int layer;
    private Matrix matrix = Matrix.ROTATE_0;
    private Date modificationTime = new Date();
    private long timescale;
    private long trackId = 1;
    private float volume;
    private double width;

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public long getTimescale() {
        return this.timescale;
    }

    public void setTimescale(long j) {
        this.timescale = j;
    }

    public Date getModificationTime() {
        return this.modificationTime;
    }

    public void setModificationTime(Date date) {
        this.modificationTime = date;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date date) {
        this.creationTime = date;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double d) {
        this.width = d;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double d) {
        this.height = d;
    }

    public long getTrackId() {
        return this.trackId;
    }

    public void setTrackId(long j) {
        this.trackId = j;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int i) {
        this.layer = i;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float f) {
        this.volume = f;
    }

    public int getGroup() {
        return this.group;
    }

    public void setGroup(int i) {
        this.group = i;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setMatrix(Matrix matrix2) {
        this.matrix = matrix2;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }
}
