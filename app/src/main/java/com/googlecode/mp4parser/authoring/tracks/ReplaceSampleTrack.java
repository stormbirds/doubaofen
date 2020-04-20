package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.List;

public class ReplaceSampleTrack extends AbstractTrack {
    Track origTrack;
    /* access modifiers changed from: private */
    public Sample sampleContent;
    /* access modifiers changed from: private */
    public long sampleNumber;
    private List<Sample> samples = new ReplaceASingleEntryList(this, (ReplaceASingleEntryList) null);

    public ReplaceSampleTrack(Track track, long j, ByteBuffer byteBuffer) {
        super("replace(" + track.getName() + ")");
        this.origTrack = track;
        this.sampleNumber = j;
        this.sampleContent = new SampleImpl(byteBuffer);
    }

    public void close() throws IOException {
        this.origTrack.close();
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.origTrack.getSampleDescriptionBox();
    }

    public synchronized long[] getSampleDurations() {
        return this.origTrack.getSampleDurations();
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return this.origTrack.getCompositionTimeEntries();
    }

    public synchronized long[] getSyncSamples() {
        return this.origTrack.getSyncSamples();
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.origTrack.getSampleDependencies();
    }

    public TrackMetaData getTrackMetaData() {
        return this.origTrack.getTrackMetaData();
    }

    public String getHandler() {
        return this.origTrack.getHandler();
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.origTrack.getSubsampleInformationBox();
    }

    private class ReplaceASingleEntryList extends AbstractList<Sample> {
        private ReplaceASingleEntryList() {
        }

        /* synthetic */ ReplaceASingleEntryList(ReplaceSampleTrack replaceSampleTrack, ReplaceASingleEntryList replaceASingleEntryList) {
            this();
        }

        public Sample get(int i) {
            if (ReplaceSampleTrack.this.sampleNumber == ((long) i)) {
                return ReplaceSampleTrack.this.sampleContent;
            }
            return ReplaceSampleTrack.this.origTrack.getSamples().get(i);
        }

        public int size() {
            return ReplaceSampleTrack.this.origTrack.getSamples().size();
        }
    }
}
