package com.googlecode.mp4parser.authoring;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTrack implements Track {
    List<Edit> edits = new ArrayList();
    String name;
    Map<GroupEntry, long[]> sampleGroups = new HashMap();

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return null;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return null;
    }

    public long[] getSyncSamples() {
        return null;
    }

    public AbstractTrack(String str) {
        this.name = str;
    }

    public long getDuration() {
        long j = 0;
        for (long j2 : getSampleDurations()) {
            j += j2;
        }
        return j;
    }

    public String getName() {
        return this.name;
    }

    public List<Edit> getEdits() {
        return this.edits;
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.sampleGroups;
    }
}
