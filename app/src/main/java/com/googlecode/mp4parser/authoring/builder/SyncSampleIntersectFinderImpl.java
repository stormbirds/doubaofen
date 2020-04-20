package com.googlecode.mp4parser.authoring.builder;

import com.coremedia.iso.boxes.OriginalFormatBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.util.Math;
import com.googlecode.mp4parser.util.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class SyncSampleIntersectFinderImpl implements Fragmenter {
    private static Logger LOG = Logger.getLogger(SyncSampleIntersectFinderImpl.class.getName());
    private final int minFragmentDurationSeconds;
    private Movie movie;
    private Track referenceTrack;

    public SyncSampleIntersectFinderImpl(Movie movie2, Track track, int i) {
        this.movie = movie2;
        this.referenceTrack = track;
        this.minFragmentDurationSeconds = i;
    }

    static String getFormat(Track track) {
        SampleDescriptionBox sampleDescriptionBox = track.getSampleDescriptionBox();
        OriginalFormatBox originalFormatBox = (OriginalFormatBox) Path.getPath((AbstractContainerBox) sampleDescriptionBox, "enc./sinf/frma");
        if (originalFormatBox != null) {
            return originalFormatBox.getDataFormat();
        }
        return sampleDescriptionBox.getSampleEntry().getType();
    }

    public long[] sampleNumbers(Track track) {
        Track track2 = track;
        if (!"vide".equals(track.getHandler())) {
            long j = 1;
            int i = 0;
            if ("soun".equals(track.getHandler())) {
                if (this.referenceTrack == null) {
                    for (Track next : this.movie.getTracks()) {
                        if (next.getSyncSamples() != null && "vide".equals(next.getHandler()) && next.getSyncSamples().length > 0) {
                            this.referenceTrack = next;
                        }
                    }
                }
                Track track3 = this.referenceTrack;
                if (track3 != null) {
                    long[] sampleNumbers = sampleNumbers(track3);
                    int size = this.referenceTrack.getSamples().size();
                    long[] jArr = new long[sampleNumbers.length];
                    long j2 = 192000;
                    Iterator<Track> it = this.movie.getTracks().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Track next2 = it.next();
                        if (getFormat(track).equals(getFormat(next2))) {
                            AudioSampleEntry audioSampleEntry = (AudioSampleEntry) next2.getSampleDescriptionBox().getSampleEntry();
                            if (audioSampleEntry.getSampleRate() < 192000) {
                                long sampleRate = audioSampleEntry.getSampleRate();
                                double size2 = ((double) ((long) next2.getSamples().size())) / ((double) size);
                                long j3 = next2.getSampleDurations()[0];
                                int i2 = 0;
                                while (i2 < jArr.length) {
                                    long[] jArr2 = jArr;
                                    jArr2[i2] = (long) Math.ceil(((double) (sampleNumbers[i2] - j)) * size2 * ((double) j3));
                                    i2++;
                                    jArr = jArr2;
                                    j = 1;
                                    i = 0;
                                }
                                j2 = sampleRate;
                            }
                        }
                    }
                    long j4 = track.getSampleDurations()[i];
                    double sampleRate2 = ((double) ((AudioSampleEntry) track.getSampleDescriptionBox().getSampleEntry()).getSampleRate()) / ((double) j2);
                    if (sampleRate2 == Math.rint(sampleRate2)) {
                        while (i < jArr.length) {
                            jArr[i] = (long) (((((double) jArr[i]) * sampleRate2) / ((double) j4)) + 1.0d);
                            i++;
                        }
                        return jArr;
                    }
                    throw new RuntimeException("Sample rates must be a multiple of the lowest sample rate to create a correct file!");
                }
                throw new RuntimeException("There was absolutely no Track with sync samples. I can't work with that!");
            }
            for (Track next3 : this.movie.getTracks()) {
                if (next3.getSyncSamples() != null && next3.getSyncSamples().length > 0) {
                    long[] sampleNumbers2 = sampleNumbers(next3);
                    int size3 = next3.getSamples().size();
                    long[] jArr3 = new long[sampleNumbers2.length];
                    double size4 = ((double) ((long) track.getSamples().size())) / ((double) size3);
                    for (int i3 = 0; i3 < jArr3.length; i3++) {
                        jArr3[i3] = ((long) Math.ceil(((double) (sampleNumbers2[i3] - 1)) * size4)) + 1;
                    }
                    return jArr3;
                }
            }
            throw new RuntimeException("There was absolutely no Track with sync samples. I can't work with that!");
        } else if (track.getSyncSamples() == null || track.getSyncSamples().length <= 0) {
            throw new RuntimeException("Video Tracks need sync samples. Only tracks other than video may have no sync samples.");
        } else {
            List<long[]> syncSamplesTimestamps = getSyncSamplesTimestamps(this.movie, track2);
            return getCommonIndices(track.getSyncSamples(), getTimes(track2, this.movie), track.getTrackMetaData().getTimescale(), (long[][]) syncSamplesTimestamps.toArray(new long[syncSamplesTimestamps.size()][]));
        }
    }

    public static List<long[]> getSyncSamplesTimestamps(Movie movie2, Track track) {
        long[] syncSamples;
        LinkedList linkedList = new LinkedList();
        for (Track next : movie2.getTracks()) {
            if (next.getHandler().equals(track.getHandler()) && (syncSamples = next.getSyncSamples()) != null && syncSamples.length > 0) {
                linkedList.add(getTimes(next, movie2));
            }
        }
        return linkedList;
    }

    public long[] getCommonIndices(long[] jArr, long[] jArr2, long j, long[]... jArr3) {
        LinkedList linkedList;
        long[] jArr4 = jArr;
        long[] jArr5 = jArr2;
        long[][] jArr6 = jArr3;
        LinkedList<Long> linkedList2 = new LinkedList<>();
        LinkedList linkedList3 = new LinkedList();
        for (int i = 0; i < jArr5.length; i++) {
            int length = jArr6.length;
            boolean z = true;
            for (int i2 = 0; i2 < length; i2++) {
                z &= Arrays.binarySearch(jArr6[i2], jArr5[i]) >= 0;
            }
            if (z) {
                linkedList2.add(Long.valueOf(jArr4[i]));
                linkedList3.add(Long.valueOf(jArr5[i]));
            }
        }
        if (((double) linkedList2.size()) < ((double) jArr4.length) * 0.25d) {
            String str = "" + String.format("%5d - Common:  [", new Object[]{Integer.valueOf(linkedList2.size())});
            for (Long longValue : linkedList2) {
                long longValue2 = longValue.longValue();
                str = String.valueOf(str) + String.format("%10d,", new Object[]{Long.valueOf(longValue2)});
            }
            LOG.warning(String.valueOf(str) + "]");
            String str2 = "" + String.format("%5d - In    :  [", new Object[]{Integer.valueOf(jArr4.length)});
            for (long j2 : jArr4) {
                str2 = String.valueOf(str2) + String.format("%10d,", new Object[]{Long.valueOf(j2)});
            }
            LOG.warning(String.valueOf(str2) + "]");
            LOG.warning("There are less than 25% of common sync samples in the given track.");
            throw new RuntimeException("There are less than 25% of common sync samples in the given track.");
        }
        if (((double) linkedList2.size()) < ((double) jArr4.length) * 0.5d) {
            LOG.fine("There are less than 50% of common sync samples in the given track. This is implausible but I'm ok to continue.");
        } else if (linkedList2.size() < jArr4.length) {
            LOG.finest("Common SyncSample positions vs. this tracks SyncSample positions: " + linkedList2.size() + " vs. " + jArr4.length);
        }
        LinkedList linkedList4 = new LinkedList();
        if (this.minFragmentDurationSeconds > 0) {
            Iterator it = linkedList2.iterator();
            Iterator it2 = linkedList3.iterator();
            long j3 = -1;
            long j4 = -1;
            while (it.hasNext() && it2.hasNext()) {
                long longValue3 = ((Long) it.next()).longValue();
                long longValue4 = ((Long) it2.next()).longValue();
                if (j4 == j3 || (longValue4 - j4) / j >= ((long) this.minFragmentDurationSeconds)) {
                    linkedList4.add(Long.valueOf(longValue3));
                    j4 = longValue4;
                }
                j3 = -1;
            }
            linkedList = linkedList4;
        } else {
            linkedList = linkedList2;
        }
        long[] jArr7 = new long[linkedList.size()];
        for (int i3 = 0; i3 < jArr7.length; i3++) {
            jArr7[i3] = ((Long) linkedList.get(i3)).longValue();
        }
        return jArr7;
    }

    private static long[] getTimes(Track track, Movie movie2) {
        long[] syncSamples = track.getSyncSamples();
        long[] jArr = new long[syncSamples.length];
        long calculateTracktimesScalingFactor = calculateTracktimesScalingFactor(movie2, track);
        int i = 0;
        long j = 0;
        int i2 = 1;
        while (true) {
            long j2 = (long) i2;
            if (j2 > syncSamples[syncSamples.length - 1]) {
                return jArr;
            }
            if (j2 == syncSamples[i]) {
                jArr[i] = j * calculateTracktimesScalingFactor;
                i++;
            }
            j += track.getSampleDurations()[i2 - 1];
            i2++;
        }
    }

    private static long calculateTracktimesScalingFactor(Movie movie2, Track track) {
        long j = 1;
        for (Track next : movie2.getTracks()) {
            if (next.getHandler().equals(track.getHandler()) && next.getTrackMetaData().getTimescale() != track.getTrackMetaData().getTimescale()) {
                j = Math.lcm(j, next.getTrackMetaData().getTimescale());
            }
        }
        return j;
    }
}
