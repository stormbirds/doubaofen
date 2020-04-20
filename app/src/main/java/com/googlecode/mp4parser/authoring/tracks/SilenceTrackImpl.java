package com.googlecode.mp4parser.authoring.tracks;

import android.support.v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SilenceTrackImpl implements Track {
    long[] decodingTimes;
    String name;
    List<Sample> samples = new LinkedList();
    Track source;

    public void close() throws IOException {
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public List<Edit> getEdits() {
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

    public SilenceTrackImpl(Track track, long j) {
        this.source = track;
        this.name = j + "ms silence";
        if (AudioSampleEntry.TYPE3.equals(track.getSampleDescriptionBox().getSampleEntry().getType())) {
            int l2i = CastUtils.l2i(((getTrackMetaData().getTimescale() * j) / 1000) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            this.decodingTimes = new long[l2i];
            Arrays.fill(this.decodingTimes, ((getTrackMetaData().getTimescale() * j) / ((long) l2i)) / 1000);
            while (true) {
                int i = l2i - 1;
                if (l2i > 0) {
                    this.samples.add(new SampleImpl((ByteBuffer) ByteBuffer.wrap(new byte[]{33, 16, 4, 96, -116, 28}).rewind()));
                    l2i = i;
                } else {
                    return;
                }
            }
        } else {
            throw new RuntimeException("Tracks of type " + track.getClass().getSimpleName() + " are not supported");
        }
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.source.getSampleDescriptionBox();
    }

    public long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public long getDuration() {
        long j = 0;
        for (long j2 : this.decodingTimes) {
            j += j2;
        }
        return j;
    }

    public TrackMetaData getTrackMetaData() {
        return this.source.getTrackMetaData();
    }

    public String getHandler() {
        return this.source.getHandler();
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public String getName() {
        return this.name;
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.source.getSampleGroups();
    }
}
