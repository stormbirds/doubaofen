package com.googlecode.mp4parser.authoring;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.ChunkOffsetBox;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.authoring.tracks.CencEncryptedTrack;
import com.googlecode.mp4parser.util.Path;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationOffsetsBox;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import com.mp4parser.iso23001.part7.TrackEncryptionBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class CencMp4TrackImplImpl extends Mp4TrackImpl implements CencEncryptedTrack {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private UUID defaultKeyId;
    private List<CencSampleAuxiliaryDataFormat> sampleEncryptionEntries = new ArrayList();

    public boolean hasSubSampleEncryption() {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CencMp4TrackImplImpl(String str, TrackBox trackBox, IsoFile... isoFileArr) throws IOException {
        super(str, trackBox, isoFileArr);
        long j;
        int i;
        long j2;
        Container container;
        int i2;
        TrackBox trackBox2 = trackBox;
        SchemeTypeBox schemeTypeBox = (SchemeTypeBox) Path.getPath((AbstractContainerBox) trackBox2, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schm[0]");
        long trackId = trackBox.getTrackHeaderBox().getTrackId();
        if (trackBox.getParent().getBoxes(MovieExtendsBox.class).size() > 0) {
            Iterator<MovieFragmentBox> it = ((Box) trackBox.getParent()).getParent().getBoxes(MovieFragmentBox.class).iterator();
            while (it.hasNext()) {
                MovieFragmentBox next = it.next();
                Iterator<TrackFragmentBox> it2 = next.getBoxes(TrackFragmentBox.class).iterator();
                while (it2.hasNext()) {
                    TrackFragmentBox next2 = it2.next();
                    if (next2.getTrackFragmentHeaderBox().getTrackId() == trackId) {
                        TrackEncryptionBox trackEncryptionBox = (TrackEncryptionBox) Path.getPath((AbstractContainerBox) trackBox2, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schi[0]/tenc[0]");
                        this.defaultKeyId = trackEncryptionBox.getDefault_KID();
                        if (next2.getTrackFragmentHeaderBox().hasBaseDataOffset()) {
                            container = ((Box) trackBox.getParent()).getParent();
                            j2 = next2.getTrackFragmentHeaderBox().getBaseDataOffset();
                        } else {
                            container = next;
                            j2 = 0;
                        }
                        FindSaioSaizPair invoke = new FindSaioSaizPair(next2).invoke();
                        SampleAuxiliaryInformationOffsetsBox saio = invoke.getSaio();
                        SampleAuxiliaryInformationSizesBox saiz = invoke.getSaiz();
                        long[] offsets = saio.getOffsets();
                        List<TrackRunBox> boxes = next2.getBoxes(TrackRunBox.class);
                        long j3 = trackId;
                        int i3 = 0;
                        int i4 = 0;
                        while (i3 < offsets.length) {
                            int size = boxes.get(i3).getEntries().size();
                            long j4 = offsets[i3];
                            Iterator<MovieFragmentBox> it3 = it;
                            long[] jArr = offsets;
                            List<TrackRunBox> list = boxes;
                            int i5 = i4;
                            long j5 = 0;
                            while (true) {
                                i2 = i4 + size;
                                if (i5 >= i2) {
                                    break;
                                }
                                j5 += (long) saiz.getSize(i5);
                                i5++;
                                next = next;
                                it2 = it2;
                            }
                            ByteBuffer byteBuffer = container.getByteBuffer(j2 + j4, j5);
                            int i6 = i4;
                            while (i6 < i2) {
                                this.sampleEncryptionEntries.add(parseCencAuxDataFormat(trackEncryptionBox.getDefaultIvSize(), byteBuffer, (long) saiz.getSize(i6)));
                                i6++;
                                i2 = i2;
                                next = next;
                                it2 = it2;
                            }
                            i3++;
                            offsets = jArr;
                            i4 = i2;
                            boxes = list;
                            it = it3;
                        }
                        trackId = j3;
                    }
                }
            }
            return;
        }
        TrackEncryptionBox trackEncryptionBox2 = (TrackEncryptionBox) Path.getPath((AbstractContainerBox) trackBox2, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schi[0]/tenc[0]");
        this.defaultKeyId = trackEncryptionBox2.getDefault_KID();
        ChunkOffsetBox chunkOffsetBox = (ChunkOffsetBox) Path.getPath((AbstractContainerBox) trackBox2, "mdia[0]/minf[0]/stbl[0]/stco[0]");
        long[] blowup = trackBox.getSampleTableBox().getSampleToChunkBox().blowup((chunkOffsetBox == null ? (ChunkOffsetBox) Path.getPath((AbstractContainerBox) trackBox2, "mdia[0]/minf[0]/stbl[0]/co64[0]") : chunkOffsetBox).getChunkOffsets().length);
        FindSaioSaizPair invoke2 = new FindSaioSaizPair((Container) Path.getPath((AbstractContainerBox) trackBox2, "mdia[0]/minf[0]/stbl[0]")).invoke();
        SampleAuxiliaryInformationOffsetsBox access$0 = invoke2.saio;
        SampleAuxiliaryInformationSizesBox access$1 = invoke2.saiz;
        Container parent = ((MovieBox) trackBox.getParent()).getParent();
        if (access$0.getOffsets().length == 1) {
            long j6 = access$0.getOffsets()[0];
            if (access$1.getDefaultSampleInfoSize() > 0) {
                i = (access$1.getSampleCount() * access$1.getDefaultSampleInfoSize()) + 0;
            } else {
                int i7 = 0;
                for (int i8 = 0; i8 < access$1.getSampleCount(); i8++) {
                    i7 += access$1.getSampleInfoSizes()[i8];
                }
                i = i7;
            }
            ByteBuffer byteBuffer2 = parent.getByteBuffer(j6, (long) i);
            for (int i9 = 0; i9 < access$1.getSampleCount(); i9++) {
                this.sampleEncryptionEntries.add(parseCencAuxDataFormat(trackEncryptionBox2.getDefaultIvSize(), byteBuffer2, (long) access$1.getSize(i9)));
            }
        } else if (access$0.getOffsets().length == blowup.length) {
            int i10 = 0;
            for (int i11 = 0; i11 < blowup.length; i11++) {
                long j7 = access$0.getOffsets()[i11];
                if (access$1.getDefaultSampleInfoSize() > 0) {
                    j = (((long) access$1.getSampleCount()) * blowup[i11]) + 0;
                } else {
                    j = 0;
                    for (int i12 = 0; ((long) i12) < blowup[i11]; i12++) {
                        j += (long) access$1.getSize(i10 + i12);
                    }
                }
                ByteBuffer byteBuffer3 = parent.getByteBuffer(j7, j);
                for (int i13 = 0; ((long) i13) < blowup[i11]; i13++) {
                    this.sampleEncryptionEntries.add(parseCencAuxDataFormat(trackEncryptionBox2.getDefaultIvSize(), byteBuffer3, (long) access$1.getSize(i10 + i13)));
                }
                i10 = (int) (((long) i10) + blowup[i11]);
            }
        } else {
            throw new RuntimeException("Number of saio offsets must be either 1 or number of chunks");
        }
    }

    private CencSampleAuxiliaryDataFormat parseCencAuxDataFormat(int i, ByteBuffer byteBuffer, long j) {
        CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat = new CencSampleAuxiliaryDataFormat();
        if (j > 0) {
            cencSampleAuxiliaryDataFormat.iv = new byte[i];
            byteBuffer.get(cencSampleAuxiliaryDataFormat.iv);
            if (j > ((long) i)) {
                cencSampleAuxiliaryDataFormat.pairs = new CencSampleAuxiliaryDataFormat.Pair[IsoTypeReader.readUInt16(byteBuffer)];
                for (int i2 = 0; i2 < cencSampleAuxiliaryDataFormat.pairs.length; i2++) {
                    cencSampleAuxiliaryDataFormat.pairs[i2] = cencSampleAuxiliaryDataFormat.createPair(IsoTypeReader.readUInt16(byteBuffer), IsoTypeReader.readUInt32(byteBuffer));
                }
            }
        }
        return cencSampleAuxiliaryDataFormat;
    }

    public UUID getDefaultKeyId() {
        return this.defaultKeyId;
    }

    public List<CencSampleAuxiliaryDataFormat> getSampleEncryptionEntries() {
        return this.sampleEncryptionEntries;
    }

    public String toString() {
        return "CencMp4TrackImpl{handler='" + getHandler() + '\'' + '}';
    }

    public String getName() {
        return "enc(" + super.getName() + ")";
    }

    private class FindSaioSaizPair {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private Container container;
        /* access modifiers changed from: private */
        public SampleAuxiliaryInformationOffsetsBox saio;
        /* access modifiers changed from: private */
        public SampleAuxiliaryInformationSizesBox saiz;

        static {
            Class<CencMp4TrackImplImpl> cls = CencMp4TrackImplImpl.class;
        }

        public FindSaioSaizPair(Container container2) {
            this.container = container2;
        }

        public SampleAuxiliaryInformationSizesBox getSaiz() {
            return this.saiz;
        }

        public SampleAuxiliaryInformationOffsetsBox getSaio() {
            return this.saio;
        }

        public FindSaioSaizPair invoke() {
            List<SampleAuxiliaryInformationSizesBox> boxes = this.container.getBoxes(SampleAuxiliaryInformationSizesBox.class);
            List<SampleAuxiliaryInformationOffsetsBox> boxes2 = this.container.getBoxes(SampleAuxiliaryInformationOffsetsBox.class);
            this.saiz = null;
            this.saio = null;
            for (int i = 0; i < boxes.size(); i++) {
                if (!(this.saiz == null && boxes.get(i).getAuxInfoType() == null) && !"cenc".equals(boxes.get(i).getAuxInfoType())) {
                    SampleAuxiliaryInformationSizesBox sampleAuxiliaryInformationSizesBox = this.saiz;
                    if (sampleAuxiliaryInformationSizesBox == null || sampleAuxiliaryInformationSizesBox.getAuxInfoType() != null || !"cenc".equals(boxes.get(i).getAuxInfoType())) {
                        throw new RuntimeException("Are there two cenc labeled saiz?");
                    }
                    this.saiz = boxes.get(i);
                } else {
                    this.saiz = boxes.get(i);
                }
                if (!(this.saio == null && boxes2.get(i).getAuxInfoType() == null) && !"cenc".equals(boxes2.get(i).getAuxInfoType())) {
                    SampleAuxiliaryInformationOffsetsBox sampleAuxiliaryInformationOffsetsBox = this.saio;
                    if (sampleAuxiliaryInformationOffsetsBox == null || sampleAuxiliaryInformationOffsetsBox.getAuxInfoType() != null || !"cenc".equals(boxes2.get(i).getAuxInfoType())) {
                        throw new RuntimeException("Are there two cenc labeled saio?");
                    }
                    this.saio = boxes2.get(i);
                } else {
                    this.saio = boxes2.get(i);
                }
            }
            return this;
        }
    }
}
