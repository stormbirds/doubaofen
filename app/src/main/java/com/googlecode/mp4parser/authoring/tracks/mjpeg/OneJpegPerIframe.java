package com.googlecode.mp4parser.authoring.tracks.mjpeg;

import com.coremedia.iso.Hex;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ObjectDescriptorFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class OneJpegPerIframe extends AbstractTrack {
    File[] jpegs;
    long[] sampleDurations;
    SampleDescriptionBox stsd;
    long[] syncSamples;
    TrackMetaData trackMetaData = new TrackMetaData();

    public void close() throws IOException {
    }

    public String getHandler() {
        return "vide";
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OneJpegPerIframe(String str, File[] fileArr, Track track) throws IOException {
        super(str);
        File[] fileArr2 = fileArr;
        this.jpegs = fileArr2;
        if (track.getSyncSamples().length == fileArr2.length) {
            BufferedImage read = ImageIO.read(fileArr2[0]);
            this.trackMetaData.setWidth((double) read.getWidth());
            this.trackMetaData.setHeight((double) read.getHeight());
            this.trackMetaData.setTimescale(track.getTrackMetaData().getTimescale());
            long[] sampleDurations2 = track.getSampleDurations();
            long[] syncSamples2 = track.getSyncSamples();
            this.sampleDurations = new long[syncSamples2.length];
            long j = 0;
            boolean z = true;
            long j2 = 0;
            int i = 1;
            for (int i2 = 1; i2 < sampleDurations2.length; i2++) {
                if (i < syncSamples2.length && ((long) i2) == syncSamples2[i]) {
                    this.sampleDurations[i - 1] = j2;
                    i++;
                    j2 = 0;
                }
                j2 += sampleDurations2[i2];
            }
            long[] jArr = this.sampleDurations;
            jArr[jArr.length - 1] = j2;
            this.stsd = new SampleDescriptionBox();
            VisualSampleEntry visualSampleEntry = new VisualSampleEntry(VisualSampleEntry.TYPE1);
            this.stsd.addBox(visualSampleEntry);
            ESDescriptorBox eSDescriptorBox = new ESDescriptorBox();
            eSDescriptorBox.setData(ByteBuffer.wrap(Hex.decodeHex("038080801B000100048080800D6C11000000000A1CB4000A1CB4068080800102")));
            eSDescriptorBox.setEsDescriptor((ESDescriptor) ObjectDescriptorFactory.createFrom(-1, ByteBuffer.wrap(Hex.decodeHex("038080801B000100048080800D6C11000000000A1CB4000A1CB4068080800102"))));
            visualSampleEntry.addBox(eSDescriptorBox);
            this.syncSamples = new long[fileArr2.length];
            int i3 = 0;
            while (true) {
                long[] jArr2 = this.syncSamples;
                if (i3 >= jArr2.length) {
                    break;
                }
                int i4 = i3 + 1;
                jArr2[i3] = (long) i4;
                i3 = i4;
            }
            double d = 0.0d;
            boolean z2 = true;
            for (Edit next : track.getEdits()) {
                if (next.getMediaTime() == -1 && !z) {
                    throw new RuntimeException("Cannot accept edit list for processing (1)");
                } else if (next.getMediaTime() >= 0 && !z2) {
                    throw new RuntimeException("Cannot accept edit list for processing (2)");
                } else if (next.getMediaTime() == -1) {
                    d += next.getSegmentDuration();
                } else {
                    d -= ((double) next.getMediaTime()) / ((double) next.getTimeScale());
                    z2 = false;
                    z = false;
                }
            }
            if (track.getCompositionTimeEntries() != null && track.getCompositionTimeEntries().size() > 0) {
                int[] blowupCompositionTimes = CompositionTimeToSample.blowupCompositionTimes(track.getCompositionTimeEntries());
                int i5 = 0;
                while (i5 < blowupCompositionTimes.length && i5 < 50) {
                    blowupCompositionTimes[i5] = (int) (((long) blowupCompositionTimes[i5]) + j);
                    j += track.getSampleDurations()[i5];
                    i5++;
                }
                Arrays.sort(blowupCompositionTimes);
                d += ((double) blowupCompositionTimes[0]) / ((double) track.getTrackMetaData().getTimescale());
            }
            if (d < 0.0d) {
                getEdits().add(new Edit((long) ((-d) * ((double) getTrackMetaData().getTimescale())), getTrackMetaData().getTimescale(), 1.0d, ((double) getDuration()) / ((double) getTrackMetaData().getTimescale())));
            } else if (d > 0.0d) {
                getEdits().add(new Edit(-1, getTrackMetaData().getTimescale(), 1.0d, d));
                getEdits().add(new Edit(0, getTrackMetaData().getTimescale(), 1.0d, ((double) getDuration()) / ((double) getTrackMetaData().getTimescale())));
            }
        } else {
            throw new RuntimeException("Number of sync samples doesn't match the number of stills (" + track.getSyncSamples().length + " vs. " + fileArr2.length + ")");
        }
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public long[] getSampleDurations() {
        return this.sampleDurations;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public long[] getSyncSamples() {
        return this.syncSamples;
    }

    public List<Sample> getSamples() {
        return new AbstractList<Sample>() {
            public int size() {
                return OneJpegPerIframe.this.jpegs.length;
            }

            public Sample get(final int i) {
                return new Sample() {
                    ByteBuffer sample = null;

                    public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(OneJpegPerIframe.this.jpegs[i], "r");
                        randomAccessFile.getChannel().transferTo(0, randomAccessFile.length(), writableByteChannel);
                        randomAccessFile.close();
                    }

                    public long getSize() {
                        return OneJpegPerIframe.this.jpegs[i].length();
                    }

                    public ByteBuffer asByteBuffer() {
                        if (this.sample == null) {
                            try {
                                RandomAccessFile randomAccessFile = new RandomAccessFile(OneJpegPerIframe.this.jpegs[i], "r");
                                this.sample = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return this.sample;
                    }
                };
            }
        };
    }
}
