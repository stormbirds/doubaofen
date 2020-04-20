package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ChangeTimeScaleTrack implements Track {
    private static final Logger LOG = Logger.getLogger(ChangeTimeScaleTrack.class.getName());
    List<CompositionTimeToSample.Entry> ctts;
    long[] decodingTimes;
    Track source;
    long timeScale;

    public ChangeTimeScaleTrack(Track track, long j, long[] jArr) {
        this.source = track;
        this.timeScale = j;
        double timescale = ((double) j) / ((double) track.getTrackMetaData().getTimescale());
        this.ctts = adjustCtts(track.getCompositionTimeEntries(), timescale);
        this.decodingTimes = adjustTts(track.getSampleDurations(), timescale, jArr, getTimes(track, jArr, j));
    }

    private static long[] getTimes(Track track, long[] jArr, long j) {
        long[] jArr2 = new long[jArr.length];
        int i = 0;
        long j2 = 0;
        int i2 = 1;
        while (true) {
            long j3 = (long) i2;
            if (j3 > jArr[jArr.length - 1]) {
                return jArr2;
            }
            if (j3 == jArr[i]) {
                jArr2[i] = (j2 * j) / track.getTrackMetaData().getTimescale();
                i++;
            }
            j2 += track.getSampleDurations()[i2 - 1];
            i2++;
        }
    }

    static List<CompositionTimeToSample.Entry> adjustCtts(List<CompositionTimeToSample.Entry> list, double d) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (CompositionTimeToSample.Entry next : list) {
            arrayList.add(new CompositionTimeToSample.Entry(next.getCount(), (int) Math.round(((double) next.getOffset()) * d)));
        }
        return arrayList;
    }

    static long[] adjustTts(long[] jArr, double d, long[] jArr2, long[] jArr3) {
        long[] jArr4 = jArr;
        long[] jArr5 = new long[jArr4.length];
        long j = 0;
        int i = 1;
        while (i <= jArr4.length) {
            int i2 = i - 1;
            long round = Math.round(((double) jArr4[i2]) * d);
            int i3 = i + 1;
            int binarySearch = Arrays.binarySearch(jArr2, (long) i3);
            if (binarySearch >= 0 && jArr3[binarySearch] != j) {
                long j2 = jArr3[binarySearch] - (j + round);
                LOG.finest(String.format("Sample %d %d / %d - correct by %d", new Object[]{Integer.valueOf(i), Long.valueOf(j), Long.valueOf(jArr3[binarySearch]), Long.valueOf(j2)}));
                round += j2;
            }
            j += round;
            jArr5[i2] = round;
            i = i3;
        }
        return jArr5;
    }

    public void close() throws IOException {
        this.source.close();
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.source.getSampleDescriptionBox();
    }

    public long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return this.ctts;
    }

    public long[] getSyncSamples() {
        return this.source.getSyncSamples();
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.source.getSampleDependencies();
    }

    public TrackMetaData getTrackMetaData() {
        TrackMetaData trackMetaData = (TrackMetaData) this.source.getTrackMetaData().clone();
        trackMetaData.setTimescale(this.timeScale);
        return trackMetaData;
    }

    public String getHandler() {
        return this.source.getHandler();
    }

    public List<Sample> getSamples() {
        return this.source.getSamples();
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.source.getSubsampleInformationBox();
    }

    public long getDuration() {
        long j = 0;
        for (long j2 : this.decodingTimes) {
            j += j2;
        }
        return j;
    }

    public String toString() {
        return "ChangeTimeScaleTrack{source=" + this.source + '}';
    }

    public String getName() {
        return "timeScale(" + this.source.getName() + ")";
    }

    public List<Edit> getEdits() {
        return this.source.getEdits();
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.source.getSampleGroups();
    }
}
