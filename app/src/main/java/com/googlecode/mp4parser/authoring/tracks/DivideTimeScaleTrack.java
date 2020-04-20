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
import java.util.List;
import java.util.Map;

public class DivideTimeScaleTrack implements Track {
    Track source;
    private int timeScaleDivisor;

    public DivideTimeScaleTrack(Track track, int i) {
        this.source = track;
        this.timeScaleDivisor = i;
    }

    public void close() throws IOException {
        this.source.close();
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.source.getSampleDescriptionBox();
    }

    public long[] getSampleDurations() {
        long[] jArr = new long[this.source.getSampleDurations().length];
        for (int i = 0; i < this.source.getSampleDurations().length; i++) {
            jArr[i] = this.source.getSampleDurations()[i] / ((long) this.timeScaleDivisor);
        }
        return jArr;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return adjustCtts();
    }

    public long[] getSyncSamples() {
        return this.source.getSyncSamples();
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.source.getSampleDependencies();
    }

    public TrackMetaData getTrackMetaData() {
        TrackMetaData trackMetaData = (TrackMetaData) this.source.getTrackMetaData().clone();
        trackMetaData.setTimescale(this.source.getTrackMetaData().getTimescale() / ((long) this.timeScaleDivisor));
        return trackMetaData;
    }

    public String getHandler() {
        return this.source.getHandler();
    }

    public List<Sample> getSamples() {
        return this.source.getSamples();
    }

    /* access modifiers changed from: package-private */
    public List<CompositionTimeToSample.Entry> adjustCtts() {
        List<CompositionTimeToSample.Entry> compositionTimeEntries = this.source.getCompositionTimeEntries();
        if (compositionTimeEntries == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(compositionTimeEntries.size());
        for (CompositionTimeToSample.Entry next : compositionTimeEntries) {
            arrayList.add(new CompositionTimeToSample.Entry(next.getCount(), next.getOffset() / this.timeScaleDivisor));
        }
        return arrayList;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.source.getSubsampleInformationBox();
    }

    public long getDuration() {
        long j = 0;
        for (long j2 : getSampleDurations()) {
            j += j2;
        }
        return j;
    }

    public String toString() {
        return "MultiplyTimeScaleTrack{source=" + this.source + '}';
    }

    public String getName() {
        return "timscale(" + this.source.getName() + ")";
    }

    public List<Edit> getEdits() {
        return this.source.getEdits();
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.source.getSampleGroups();
    }
}
