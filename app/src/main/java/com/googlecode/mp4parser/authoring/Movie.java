package com.googlecode.mp4parser.authoring;

import com.googlecode.mp4parser.util.Matrix;
import java.util.LinkedList;
import java.util.List;

public class Movie {
    Matrix matrix = Matrix.ROTATE_0;
    List<Track> tracks = new LinkedList();

    public Movie() {
    }

    public Movie(List<Track> list) {
        this.tracks = list;
    }

    public List<Track> getTracks() {
        return this.tracks;
    }

    public void setTracks(List<Track> list) {
        this.tracks = list;
    }

    public void addTrack(Track track) {
        if (getTrackByTrackId(track.getTrackMetaData().getTrackId()) != null) {
            track.getTrackMetaData().setTrackId(getNextTrackId());
        }
        this.tracks.add(track);
    }

    public String toString() {
        String str = "Movie{ ";
        for (Track next : this.tracks) {
            str = String.valueOf(str) + "track_" + next.getTrackMetaData().getTrackId() + " (" + next.getHandler() + ") ";
        }
        return String.valueOf(str) + '}';
    }

    public long getNextTrackId() {
        long j = 0;
        for (Track next : this.tracks) {
            if (j < next.getTrackMetaData().getTrackId()) {
                j = next.getTrackMetaData().getTrackId();
            }
        }
        return j + 1;
    }

    public Track getTrackByTrackId(long j) {
        for (Track next : this.tracks) {
            if (next.getTrackMetaData().getTrackId() == j) {
                return next;
            }
        }
        return null;
    }

    public long getTimescale() {
        long timescale = getTracks().iterator().next().getTrackMetaData().getTimescale();
        for (Track trackMetaData : getTracks()) {
            timescale = gcd(trackMetaData.getTrackMetaData().getTimescale(), timescale);
        }
        return timescale;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setMatrix(Matrix matrix2) {
        this.matrix = matrix2;
    }

    public static long gcd(long j, long j2) {
        return j2 == 0 ? j : gcd(j2, j % j2);
    }
}
