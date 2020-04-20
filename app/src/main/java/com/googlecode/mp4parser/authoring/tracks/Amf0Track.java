package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.adobe.ActionMessageFormat0SampleEntryBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Amf0Track extends AbstractTrack {
    SortedMap<Long, byte[]> rawSamples = new TreeMap<Long, byte[]>() {
    };
    private TrackMetaData trackMetaData = new TrackMetaData();

    public void close() throws IOException {
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public String getHandler() {
        return "data";
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

    public Amf0Track(Map<Long, byte[]> map) {
        super(ActionMessageFormat0SampleEntryBox.TYPE);
        this.rawSamples = new TreeMap(map);
        this.trackMetaData.setCreationTime(new Date());
        this.trackMetaData.setModificationTime(new Date());
        this.trackMetaData.setTimescale(1000);
        this.trackMetaData.setLanguage("eng");
    }

    public List<Sample> getSamples() {
        LinkedList linkedList = new LinkedList();
        for (byte[] wrap : this.rawSamples.values()) {
            linkedList.add(new SampleImpl(ByteBuffer.wrap(wrap)));
        }
        return linkedList;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        SampleDescriptionBox sampleDescriptionBox = new SampleDescriptionBox();
        ActionMessageFormat0SampleEntryBox actionMessageFormat0SampleEntryBox = new ActionMessageFormat0SampleEntryBox();
        actionMessageFormat0SampleEntryBox.setDataReferenceIndex(1);
        sampleDescriptionBox.addBox(actionMessageFormat0SampleEntryBox);
        return sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        LinkedList linkedList = new LinkedList(this.rawSamples.keySet());
        Collections.sort(linkedList);
        long[] jArr = new long[linkedList.size()];
        for (int i = 0; i < linkedList.size(); i++) {
            jArr[i] = ((Long) linkedList.get(i)).longValue() - 0;
        }
        return jArr;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }
}
