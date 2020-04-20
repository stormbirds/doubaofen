package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriterVariable;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.WrappingTrack;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Path;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class Avc1ToAvc3TrackImpl extends WrappingTrack {
    AvcConfigurationBox avcC;
    List<Sample> samples;
    SampleDescriptionBox stsd;

    public Avc1ToAvc3TrackImpl(Track track) throws IOException {
        super(track);
        if (VisualSampleEntry.TYPE3.equals(track.getSampleDescriptionBox().getSampleEntry().getType())) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            track.getSampleDescriptionBox().getBox(Channels.newChannel(byteArrayOutputStream));
            this.stsd = (SampleDescriptionBox) Path.getPath((Container) new IsoFile((DataSource) new MemoryDataSourceImpl(byteArrayOutputStream.toByteArray())), SampleDescriptionBox.TYPE);
            ((VisualSampleEntry) this.stsd.getSampleEntry()).setType(VisualSampleEntry.TYPE4);
            this.avcC = (AvcConfigurationBox) Path.getPath((AbstractContainerBox) this.stsd, "avc./avcC");
            this.samples = new ReplaceSyncSamplesList(track.getSamples());
            return;
        }
        throw new RuntimeException("Only avc1 tracks can be converted to avc3 tracks");
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    private class ReplaceSyncSamplesList extends AbstractList<Sample> {
        List<Sample> parentSamples;

        public ReplaceSyncSamplesList(List<Sample> list) {
            this.parentSamples = list;
        }

        public Sample get(int i) {
            if (Arrays.binarySearch(Avc1ToAvc3TrackImpl.this.getSyncSamples(), (long) (i + 1)) < 0) {
                return this.parentSamples.get(i);
            }
            final int lengthSizeMinusOne = Avc1ToAvc3TrackImpl.this.avcC.getLengthSizeMinusOne() + 1;
            final ByteBuffer allocate = ByteBuffer.allocate(lengthSizeMinusOne);
            final Sample sample = this.parentSamples.get(i);
            return new Sample() {
                public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                    for (byte[] next : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSets()) {
                        IsoTypeWriterVariable.write((long) next.length, (ByteBuffer) allocate.rewind(), lengthSizeMinusOne);
                        writableByteChannel.write((ByteBuffer) allocate.rewind());
                        writableByteChannel.write(ByteBuffer.wrap(next));
                    }
                    for (byte[] next2 : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSetExts()) {
                        IsoTypeWriterVariable.write((long) next2.length, (ByteBuffer) allocate.rewind(), lengthSizeMinusOne);
                        writableByteChannel.write((ByteBuffer) allocate.rewind());
                        writableByteChannel.write(ByteBuffer.wrap(next2));
                    }
                    for (byte[] next3 : Avc1ToAvc3TrackImpl.this.avcC.getPictureParameterSets()) {
                        IsoTypeWriterVariable.write((long) next3.length, (ByteBuffer) allocate.rewind(), lengthSizeMinusOne);
                        writableByteChannel.write((ByteBuffer) allocate.rewind());
                        writableByteChannel.write(ByteBuffer.wrap(next3));
                    }
                    sample.writeTo(writableByteChannel);
                }

                public long getSize() {
                    int i = 0;
                    for (byte[] length : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSets()) {
                        i += lengthSizeMinusOne + length.length;
                    }
                    for (byte[] length2 : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSetExts()) {
                        i += lengthSizeMinusOne + length2.length;
                    }
                    for (byte[] length3 : Avc1ToAvc3TrackImpl.this.avcC.getPictureParameterSets()) {
                        i += lengthSizeMinusOne + length3.length;
                    }
                    return sample.getSize() + ((long) i);
                }

                public ByteBuffer asByteBuffer() {
                    int i = 0;
                    for (byte[] length : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSets()) {
                        i += lengthSizeMinusOne + length.length;
                    }
                    for (byte[] length2 : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSetExts()) {
                        i += lengthSizeMinusOne + length2.length;
                    }
                    for (byte[] length3 : Avc1ToAvc3TrackImpl.this.avcC.getPictureParameterSets()) {
                        i += lengthSizeMinusOne + length3.length;
                    }
                    ByteBuffer allocate = ByteBuffer.allocate(CastUtils.l2i(sample.getSize()) + i);
                    for (byte[] next : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSets()) {
                        IsoTypeWriterVariable.write((long) next.length, allocate, lengthSizeMinusOne);
                        allocate.put(next);
                    }
                    for (byte[] next2 : Avc1ToAvc3TrackImpl.this.avcC.getSequenceParameterSetExts()) {
                        IsoTypeWriterVariable.write((long) next2.length, allocate, lengthSizeMinusOne);
                        allocate.put(next2);
                    }
                    for (byte[] next3 : Avc1ToAvc3TrackImpl.this.avcC.getPictureParameterSets()) {
                        IsoTypeWriterVariable.write((long) next3.length, allocate, lengthSizeMinusOne);
                        allocate.put(next3);
                    }
                    allocate.put(sample.asByteBuffer());
                    return (ByteBuffer) allocate.rewind();
                }
            };
        }

        public int size() {
            return this.parentSamples.size();
        }
    }
}
