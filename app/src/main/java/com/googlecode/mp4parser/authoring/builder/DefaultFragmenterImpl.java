package com.googlecode.mp4parser.authoring.builder;

import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.util.Mp4Arrays;
import java.util.Arrays;

public class DefaultFragmenterImpl implements Fragmenter {
    private double fragmentLength = 2.0d;

    public DefaultFragmenterImpl(double d) {
        this.fragmentLength = d;
    }

    public long[] sampleNumbers(Track track) {
        long[] sampleDurations = track.getSampleDurations();
        long[] syncSamples = track.getSyncSamples();
        long timescale = track.getTrackMetaData().getTimescale();
        long[] jArr = {1};
        double d = 0.0d;
        for (int i = 0; i < sampleDurations.length; i++) {
            d += ((double) sampleDurations[i]) / ((double) timescale);
            if (d >= this.fragmentLength && (syncSamples == null || Arrays.binarySearch(syncSamples, (long) (i + 1)) >= 0)) {
                if (i > 0) {
                    jArr = Mp4Arrays.copyOfAndAppend(jArr, (long) (i + 1));
                }
                d = 0.0d;
            }
        }
        if (d >= this.fragmentLength || jArr.length <= 1) {
            return jArr;
        }
        long[] jArr2 = new long[(jArr.length - 1)];
        System.arraycopy(jArr, 0, jArr2, 0, jArr.length - 1);
        return jArr2;
    }
}
