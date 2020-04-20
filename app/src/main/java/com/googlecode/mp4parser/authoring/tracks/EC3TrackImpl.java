package com.googlecode.mp4parser.authoring.tracks;

import android.support.v7.widget.helper.ItemTouchHelper;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.EC3SpecificBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EC3TrackImpl extends AbstractTrack {
    private static final long MAX_FRAMES_PER_MMAP = 20;
    private List<BitStreamInfo> bitStreamInfos = new LinkedList();
    private int bitrate;
    /* access modifiers changed from: private */
    public final DataSource dataSource;
    private long[] decodingTimes;
    /* access modifiers changed from: private */
    public int frameSize;
    SampleDescriptionBox sampleDescriptionBox;
    private List<Sample> samples;
    TrackMetaData trackMetaData = new TrackMetaData();

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public String getHandler() {
        return "soun";
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

    public EC3TrackImpl(DataSource dataSource2) throws IOException {
        super(dataSource2.toString());
        this.dataSource = dataSource2;
        boolean z = false;
        while (!z) {
            BitStreamInfo readVariables = readVariables();
            if (readVariables != null) {
                for (BitStreamInfo next : this.bitStreamInfos) {
                    if (readVariables.strmtyp != 1 && next.substreamid == readVariables.substreamid) {
                        z = true;
                    }
                }
                if (!z) {
                    this.bitStreamInfos.add(readVariables);
                }
            } else {
                throw new IOException();
            }
        }
        if (this.bitStreamInfos.size() != 0) {
            int i = this.bitStreamInfos.get(0).samplerate;
            this.sampleDescriptionBox = new SampleDescriptionBox();
            AudioSampleEntry audioSampleEntry = new AudioSampleEntry(AudioSampleEntry.TYPE9);
            audioSampleEntry.setChannelCount(2);
            long j = (long) i;
            audioSampleEntry.setSampleRate(j);
            audioSampleEntry.setDataReferenceIndex(1);
            audioSampleEntry.setSampleSize(16);
            EC3SpecificBox eC3SpecificBox = new EC3SpecificBox();
            int[] iArr = new int[this.bitStreamInfos.size()];
            int[] iArr2 = new int[this.bitStreamInfos.size()];
            for (BitStreamInfo next2 : this.bitStreamInfos) {
                if (next2.strmtyp == 1) {
                    int i2 = next2.substreamid;
                    iArr[i2] = iArr[i2] + 1;
                    iArr2[next2.substreamid] = ((next2.chanmap >> 5) & 255) | ((next2.chanmap >> 6) & 256);
                }
            }
            for (BitStreamInfo next3 : this.bitStreamInfos) {
                if (next3.strmtyp != 1) {
                    EC3SpecificBox.Entry entry = new EC3SpecificBox.Entry();
                    entry.fscod = next3.fscod;
                    entry.bsid = next3.bsid;
                    entry.bsmod = next3.bsmod;
                    entry.acmod = next3.acmod;
                    entry.lfeon = next3.lfeon;
                    entry.reserved = 0;
                    entry.num_dep_sub = iArr[next3.substreamid];
                    entry.chan_loc = iArr2[next3.substreamid];
                    entry.reserved2 = 0;
                    eC3SpecificBox.addEntry(entry);
                }
                this.bitrate += next3.bitrate;
                this.frameSize += next3.frameSize;
            }
            eC3SpecificBox.setDataRate(this.bitrate / 1000);
            audioSampleEntry.addBox(eC3SpecificBox);
            this.sampleDescriptionBox.addBox(audioSampleEntry);
            this.trackMetaData.setCreationTime(new Date());
            this.trackMetaData.setModificationTime(new Date());
            this.trackMetaData.setTimescale(j);
            this.trackMetaData.setVolume(1.0f);
            dataSource2.position(0);
            this.samples = readSamples();
            this.decodingTimes = new long[this.samples.size()];
            Arrays.fill(this.decodingTimes, 1536);
            return;
        }
        throw new IOException();
    }

    public void close() throws IOException {
        this.dataSource.close();
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    private BitStreamInfo readVariables() throws IOException {
        int i;
        long position = this.dataSource.position();
        ByteBuffer allocate = ByteBuffer.allocate(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.dataSource.read(allocate);
        allocate.rewind();
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(allocate);
        if (bitReaderBuffer.readBits(16) != 2935) {
            return null;
        }
        BitStreamInfo bitStreamInfo = new BitStreamInfo();
        bitStreamInfo.strmtyp = bitReaderBuffer.readBits(2);
        bitStreamInfo.substreamid = bitReaderBuffer.readBits(3);
        bitStreamInfo.frameSize = (bitReaderBuffer.readBits(11) + 1) * 2;
        bitStreamInfo.fscod = bitReaderBuffer.readBits(2);
        int i2 = -1;
        if (bitStreamInfo.fscod == 3) {
            i2 = bitReaderBuffer.readBits(2);
            i = 3;
        } else {
            i = bitReaderBuffer.readBits(2);
        }
        int i3 = i != 0 ? i != 1 ? i != 2 ? i != 3 ? 0 : 6 : 3 : 2 : 1;
        bitStreamInfo.frameSize *= 6 / i3;
        bitStreamInfo.acmod = bitReaderBuffer.readBits(3);
        bitStreamInfo.lfeon = bitReaderBuffer.readBits(1);
        bitStreamInfo.bsid = bitReaderBuffer.readBits(5);
        bitReaderBuffer.readBits(5);
        if (1 == bitReaderBuffer.readBits(1)) {
            bitReaderBuffer.readBits(8);
        }
        if (bitStreamInfo.acmod == 0) {
            bitReaderBuffer.readBits(5);
            if (1 == bitReaderBuffer.readBits(1)) {
                bitReaderBuffer.readBits(8);
            }
        }
        if (1 == bitStreamInfo.strmtyp && 1 == bitReaderBuffer.readBits(1)) {
            bitStreamInfo.chanmap = bitReaderBuffer.readBits(16);
        }
        if (1 == bitReaderBuffer.readBits(1)) {
            if (bitStreamInfo.acmod > 2) {
                bitReaderBuffer.readBits(2);
            }
            if (1 == (bitStreamInfo.acmod & 1) && bitStreamInfo.acmod > 2) {
                bitReaderBuffer.readBits(3);
                bitReaderBuffer.readBits(3);
            }
            if ((bitStreamInfo.acmod & 4) > 0) {
                bitReaderBuffer.readBits(3);
                bitReaderBuffer.readBits(3);
            }
            if (1 == bitStreamInfo.lfeon && 1 == bitReaderBuffer.readBits(1)) {
                bitReaderBuffer.readBits(5);
            }
            if (bitStreamInfo.strmtyp == 0) {
                if (1 == bitReaderBuffer.readBits(1)) {
                    bitReaderBuffer.readBits(6);
                }
                if (bitStreamInfo.acmod == 0 && 1 == bitReaderBuffer.readBits(1)) {
                    bitReaderBuffer.readBits(6);
                }
                if (1 == bitReaderBuffer.readBits(1)) {
                    bitReaderBuffer.readBits(6);
                }
                int readBits = bitReaderBuffer.readBits(2);
                if (1 == readBits) {
                    bitReaderBuffer.readBits(5);
                } else if (2 == readBits) {
                    bitReaderBuffer.readBits(12);
                } else if (3 == readBits) {
                    int readBits2 = bitReaderBuffer.readBits(5);
                    if (1 == bitReaderBuffer.readBits(1)) {
                        bitReaderBuffer.readBits(5);
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(4);
                        }
                        if (1 == bitReaderBuffer.readBits(1)) {
                            if (1 == bitReaderBuffer.readBits(1)) {
                                bitReaderBuffer.readBits(4);
                            }
                            if (1 == bitReaderBuffer.readBits(1)) {
                                bitReaderBuffer.readBits(4);
                            }
                        }
                    }
                    if (1 == bitReaderBuffer.readBits(1)) {
                        bitReaderBuffer.readBits(5);
                        if (1 == bitReaderBuffer.readBits(1)) {
                            bitReaderBuffer.readBits(7);
                            if (1 == bitReaderBuffer.readBits(1)) {
                                bitReaderBuffer.readBits(8);
                            }
                        }
                    }
                    for (int i4 = 0; i4 < readBits2 + 2; i4++) {
                        bitReaderBuffer.readBits(8);
                    }
                    bitReaderBuffer.byteSync();
                }
                if (bitStreamInfo.acmod < 2) {
                    if (1 == bitReaderBuffer.readBits(1)) {
                        bitReaderBuffer.readBits(14);
                    }
                    if (bitStreamInfo.acmod == 0 && 1 == bitReaderBuffer.readBits(1)) {
                        bitReaderBuffer.readBits(14);
                    }
                    if (1 == bitReaderBuffer.readBits(1)) {
                        if (i == 0) {
                            bitReaderBuffer.readBits(5);
                        } else {
                            for (int i5 = 0; i5 < i3; i5++) {
                                if (1 == bitReaderBuffer.readBits(1)) {
                                    bitReaderBuffer.readBits(5);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (1 == bitReaderBuffer.readBits(1)) {
            bitStreamInfo.bsmod = bitReaderBuffer.readBits(3);
        }
        int i6 = bitStreamInfo.fscod;
        if (i6 == 0) {
            bitStreamInfo.samplerate = 48000;
        } else if (i6 == 1) {
            bitStreamInfo.samplerate = 44100;
        } else if (i6 == 2) {
            bitStreamInfo.samplerate = 32000;
        } else if (i6 == 3) {
            if (i2 == 0) {
                bitStreamInfo.samplerate = 24000;
            } else if (i2 == 1) {
                bitStreamInfo.samplerate = 22050;
            } else if (i2 == 2) {
                bitStreamInfo.samplerate = 16000;
            } else if (i2 == 3) {
                bitStreamInfo.samplerate = 0;
            }
        }
        if (bitStreamInfo.samplerate == 0) {
            return null;
        }
        bitStreamInfo.bitrate = (int) ((((double) bitStreamInfo.samplerate) / 1536.0d) * ((double) bitStreamInfo.frameSize) * 8.0d);
        this.dataSource.position(position + ((long) bitStreamInfo.frameSize));
        return bitStreamInfo;
    }

    private List<Sample> readSamples() throws IOException {
        int l2i = CastUtils.l2i((this.dataSource.size() - this.dataSource.position()) / ((long) this.frameSize));
        ArrayList arrayList = new ArrayList(l2i);
        for (int i = 0; i < l2i; i++) {
            final int i2 = this.frameSize * i;
            arrayList.add(new Sample() {
                public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                    EC3TrackImpl.this.dataSource.transferTo((long) i2, (long) EC3TrackImpl.this.frameSize, writableByteChannel);
                }

                public long getSize() {
                    return (long) EC3TrackImpl.this.frameSize;
                }

                public ByteBuffer asByteBuffer() {
                    try {
                        return EC3TrackImpl.this.dataSource.map((long) i2, (long) EC3TrackImpl.this.frameSize);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return arrayList;
    }

    public static class BitStreamInfo extends EC3SpecificBox.Entry {
        public int bitrate;
        public int chanmap;
        public int frameSize;
        public int samplerate;
        public int strmtyp;
        public int substreamid;

        public String toString() {
            return "BitStreamInfo{frameSize=" + this.frameSize + ", substreamid=" + this.substreamid + ", bitrate=" + this.bitrate + ", samplerate=" + this.samplerate + ", strmtyp=" + this.strmtyp + ", chanmap=" + this.chanmap + '}';
        }
    }

    public String toString() {
        return "EC3TrackImpl{bitrate=" + this.bitrate + ", bitStreamInfos=" + this.bitStreamInfos + '}';
    }
}
