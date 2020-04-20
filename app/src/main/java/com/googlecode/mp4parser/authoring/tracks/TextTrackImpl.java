package com.googlecode.mp4parser.authoring.tracks;

import com.bumptech.glide.load.Key;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.TextSampleEntry;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.threegpp26245.FontTableBox;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TextTrackImpl extends AbstractTrack {
    SampleDescriptionBox sampleDescriptionBox = new SampleDescriptionBox();
    List<Sample> samples;
    List<Line> subs = new LinkedList();
    TrackMetaData trackMetaData = new TrackMetaData();

    public void close() throws IOException {
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public String getHandler() {
        return "sbtl";
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

    public TextTrackImpl() {
        super("subtitles");
        TextSampleEntry textSampleEntry = new TextSampleEntry(TextSampleEntry.TYPE1);
        textSampleEntry.setDataReferenceIndex(1);
        textSampleEntry.setStyleRecord(new TextSampleEntry.StyleRecord());
        textSampleEntry.setBoxRecord(new TextSampleEntry.BoxRecord());
        this.sampleDescriptionBox.addBox(textSampleEntry);
        FontTableBox fontTableBox = new FontTableBox();
        fontTableBox.setEntries(Collections.singletonList(new FontTableBox.FontRecord(1, "Serif")));
        textSampleEntry.addBox(fontTableBox);
        this.trackMetaData.setCreationTime(new Date());
        this.trackMetaData.setModificationTime(new Date());
        this.trackMetaData.setTimescale(1000);
    }

    public List<Line> getSubs() {
        return this.subs;
    }

    public synchronized List<Sample> getSamples() {
        if (this.samples == null) {
            this.samples = new ArrayList();
            long j = 0;
            for (Line next : this.subs) {
                int i = ((next.from - j) > 0 ? 1 : ((next.from - j) == 0 ? 0 : -1));
                if (i > 0) {
                    this.samples.add(new SampleImpl(ByteBuffer.wrap(new byte[2])));
                } else if (i < 0) {
                    throw new Error("Subtitle display times may not intersect");
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                try {
                    dataOutputStream.writeShort(next.text.getBytes(Key.STRING_CHARSET_NAME).length);
                    dataOutputStream.write(next.text.getBytes(Key.STRING_CHARSET_NAME));
                    dataOutputStream.close();
                    this.samples.add(new SampleImpl(ByteBuffer.wrap(byteArrayOutputStream.toByteArray())));
                    j = next.to;
                } catch (IOException unused) {
                    throw new Error("VM is broken. Does not support UTF-8");
                }
            }
        }
        return this.samples;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        ArrayList<Long> arrayList = new ArrayList<>();
        long j = 0;
        for (Line next : this.subs) {
            long j2 = next.from - j;
            int i = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
            if (i > 0) {
                arrayList.add(Long.valueOf(j2));
            } else if (i < 0) {
                throw new Error("Subtitle display times may not intersect");
            }
            arrayList.add(Long.valueOf(next.to - next.from));
            j = next.to;
        }
        long[] jArr = new long[arrayList.size()];
        int i2 = 0;
        for (Long longValue : arrayList) {
            jArr[i2] = longValue.longValue();
            i2++;
        }
        return jArr;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public static class Line {
        long from;
        String text;
        long to;

        public Line(long j, long j2, String str) {
            this.from = j;
            this.to = j2;
            this.text = str;
        }

        public long getFrom() {
            return this.from;
        }

        public String getText() {
            return this.text;
        }

        public long getTo() {
            return this.to;
        }
    }
}
