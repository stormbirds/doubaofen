package com.googlecode.mp4parser.authoring.builder;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.EditBox;
import com.coremedia.iso.boxes.EditListBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.HintMediaHeaderBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.NullMediaHeaderBox;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.SubtitleMediaHeaderBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentRandomAccessBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentRandomAccessOffsetBox;
import com.coremedia.iso.boxes.fragment.SampleFlags;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBaseMediaDecodeTimeBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.tracks.CencEncryptedTrack;
import com.googlecode.mp4parser.boxes.dece.SampleEncryptionBox;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Path;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationOffsetsBox;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import com.mp4parser.iso23001.part7.TrackEncryptionBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FragmentedMp4Builder implements Mp4Builder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Logger LOG = Logger.getLogger(FragmentedMp4Builder.class.getName());
    protected Fragmenter fragmenter;

    public Date getDate() {
        return new Date();
    }

    public Box createFtyp(Movie movie) {
        LinkedList linkedList = new LinkedList();
        linkedList.add("mp42");
        linkedList.add("iso6");
        linkedList.add(VisualSampleEntry.TYPE3);
        linkedList.add("isom");
        return new FileTypeBox("iso6", 1, linkedList);
    }

    /* access modifiers changed from: protected */
    public List<Track> sortTracksInSequence(List<Track> list, final int i, final Map<Track, long[]> map) {
        LinkedList linkedList = new LinkedList(list);
        Collections.sort(linkedList, new Comparator<Track>() {
            public int compare(Track track, Track track2) {
                long j = ((long[]) map.get(track))[i];
                long j2 = ((long[]) map.get(track2))[i];
                long[] sampleDurations = track.getSampleDurations();
                long[] sampleDurations2 = track2.getSampleDurations();
                long j3 = 0;
                for (int i = 1; ((long) i) < j; i++) {
                    j3 += sampleDurations[i - 1];
                }
                long j4 = 0;
                for (int i2 = 1; ((long) i2) < j2; i2++) {
                    j4 += sampleDurations2[i2 - 1];
                }
                return (int) (((((double) j3) / ((double) track.getTrackMetaData().getTimescale())) - (((double) j4) / ((double) track2.getTrackMetaData().getTimescale()))) * 100.0d);
            }
        });
        return linkedList;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v3, resolved type: com.googlecode.mp4parser.authoring.Track} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.coremedia.iso.boxes.Box> createMoofMdat(com.googlecode.mp4parser.authoring.Movie r27) {
        /*
            r26 = this;
            java.util.LinkedList r8 = new java.util.LinkedList
            r8.<init>()
            java.util.HashMap r9 = new java.util.HashMap
            r9.<init>()
            java.util.HashMap r10 = new java.util.HashMap
            r10.<init>()
            java.util.List r0 = r27.getTracks()
            java.util.Iterator r0 = r0.iterator()
        L_0x0017:
            boolean r1 = r0.hasNext()
            if (r1 != 0) goto L_0x00de
            r11 = 1
            r12 = 1
        L_0x001f:
            boolean r0 = r9.isEmpty()
            if (r0 == 0) goto L_0x0026
            return r8
        L_0x0026:
            r0 = 0
            r1 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            java.util.Set r3 = r10.entrySet()
            java.util.Iterator r3 = r3.iterator()
            r13 = r0
        L_0x0035:
            boolean r0 = r3.hasNext()
            if (r0 != 0) goto L_0x00b1
            java.lang.Object r0 = r9.get(r13)
            r14 = r0
            long[] r14 = (long[]) r14
            r15 = 0
            r4 = r14[r15]
            int r0 = r14.length
            if (r0 <= r11) goto L_0x004b
            r6 = r14[r11]
            goto L_0x0055
        L_0x004b:
            java.util.List r0 = r13.getSamples()
            int r0 = r0.size()
            int r0 = r0 + r11
            long r6 = (long) r0
        L_0x0055:
            long[] r0 = r13.getSampleDurations()
            com.googlecode.mp4parser.authoring.TrackMetaData r3 = r13.getTrackMetaData()
            r16 = r12
            long r11 = r3.getTimescale()
            r17 = r1
            r1 = r4
        L_0x0066:
            int r3 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r3 < 0) goto L_0x0096
            r0 = r26
            r1 = r8
            r2 = r13
            r3 = r4
            r5 = r6
            r7 = r16
            r0.createFragment(r1, r2, r3, r5, r7)
            int r0 = r14.length
            r3 = 1
            if (r0 != r3) goto L_0x0080
            r9.remove(r13)
            r10.remove(r13)
            goto L_0x0092
        L_0x0080:
            int r0 = r14.length
            int r0 = r0 - r3
            long[] r0 = new long[r0]
            int r1 = r0.length
            java.lang.System.arraycopy(r14, r3, r0, r15, r1)
            r9.put(r13, r0)
            java.lang.Double r0 = java.lang.Double.valueOf(r17)
            r10.put(r13, r0)
        L_0x0092:
            int r12 = r16 + 1
            r11 = 1
            goto L_0x001f
        L_0x0096:
            r3 = 1
            r19 = 1
            long r21 = r1 - r19
            int r21 = com.googlecode.mp4parser.util.CastUtils.l2i(r21)
            r22 = r4
            r3 = r0[r21]
            double r3 = (double) r3
            r24 = r6
            double r5 = (double) r11
            double r3 = r3 / r5
            double r17 = r17 + r3
            long r1 = r1 + r19
            r4 = r22
            r6 = r24
            goto L_0x0066
        L_0x00b1:
            r16 = r12
            r4 = 1
            java.lang.Object r0 = r3.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r5 = r0.getValue()
            java.lang.Double r5 = (java.lang.Double) r5
            double r5 = r5.doubleValue()
            int r7 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r7 >= 0) goto L_0x00d9
            java.lang.Object r1 = r0.getValue()
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            java.lang.Object r0 = r0.getKey()
            r13 = r0
            com.googlecode.mp4parser.authoring.Track r13 = (com.googlecode.mp4parser.authoring.Track) r13
        L_0x00d9:
            r12 = r16
            r11 = 1
            goto L_0x0035
        L_0x00de:
            java.lang.Object r1 = r0.next()
            com.googlecode.mp4parser.authoring.Track r1 = (com.googlecode.mp4parser.authoring.Track) r1
            r2 = r26
            com.googlecode.mp4parser.authoring.builder.Fragmenter r3 = r2.fragmenter
            long[] r3 = r3.sampleNumbers(r1)
            r9.put(r1, r3)
            r3 = 0
            java.lang.Double r3 = java.lang.Double.valueOf(r3)
            r10.put(r1, r3)
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.builder.FragmentedMp4Builder.createMoofMdat(com.googlecode.mp4parser.authoring.Movie):java.util.List");
    }

    /* access modifiers changed from: protected */
    public int createFragment(List<Box> list, Track track, long j, long j2, int i) {
        if (j != j2) {
            long j3 = j;
            long j4 = j2;
            Track track2 = track;
            int i2 = i;
            list.add(createMoof(j3, j4, track2, i2));
            list.add(createMdat(j3, j4, track2, i2));
        }
        return i;
    }

    public Container build(Movie movie) {
        Logger logger = LOG;
        logger.fine("Creating movie " + movie);
        if (this.fragmenter == null) {
            this.fragmenter = new BetterFragmenter(2.0d);
        }
        BasicContainer basicContainer = new BasicContainer();
        basicContainer.addBox(createFtyp(movie));
        basicContainer.addBox(createMoov(movie));
        for (Box addBox : createMoofMdat(movie)) {
            basicContainer.addBox(addBox);
        }
        basicContainer.addBox(createMfra(movie, basicContainer));
        return basicContainer;
    }

    /* access modifiers changed from: protected */
    public Box createMdat(long j, long j2, Track track, int i) {
        final long j3 = j;
        final long j4 = j2;
        final Track track2 = track;
        return new Box() {
            Container parent;
            long size_ = -1;

            public String getType() {
                return MediaDataBox.TYPE;
            }

            public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
            }

            public Container getParent() {
                return this.parent;
            }

            public void setParent(Container container) {
                this.parent = container;
            }

            public long getOffset() {
                throw new RuntimeException("Doesn't have any meaning for programmatically created boxes");
            }

            public long getSize() {
                long j = this.size_;
                if (j != -1) {
                    return j;
                }
                long j2 = 8;
                for (Sample size : FragmentedMp4Builder.this.getSamples(j3, j4, track2)) {
                    j2 += size.getSize();
                }
                this.size_ = j2;
                return j2;
            }

            public void getBox(WritableByteChannel writableByteChannel) throws IOException {
                ByteBuffer allocate = ByteBuffer.allocate(8);
                IsoTypeWriter.writeUInt32(allocate, (long) CastUtils.l2i(getSize()));
                allocate.put(IsoFile.fourCCtoBytes(getType()));
                allocate.rewind();
                writableByteChannel.write(allocate);
                for (Sample writeTo : FragmentedMp4Builder.this.getSamples(j3, j4, track2)) {
                    writeTo.writeTo(writableByteChannel);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void createTfhd(long j, long j2, Track track, int i, TrackFragmentBox trackFragmentBox) {
        TrackFragmentHeaderBox trackFragmentHeaderBox = new TrackFragmentHeaderBox();
        trackFragmentHeaderBox.setDefaultSampleFlags(new SampleFlags());
        trackFragmentHeaderBox.setBaseDataOffset(-1);
        trackFragmentHeaderBox.setTrackId(track.getTrackMetaData().getTrackId());
        trackFragmentHeaderBox.setDefaultBaseIsMoof(true);
        trackFragmentBox.addBox(trackFragmentHeaderBox);
    }

    /* access modifiers changed from: protected */
    public void createMfhd(long j, long j2, Track track, int i, MovieFragmentBox movieFragmentBox) {
        MovieFragmentHeaderBox movieFragmentHeaderBox = new MovieFragmentHeaderBox();
        movieFragmentHeaderBox.setSequenceNumber((long) i);
        movieFragmentBox.addBox(movieFragmentHeaderBox);
    }

    /* access modifiers changed from: protected */
    public void createTraf(long j, long j2, Track track, int i, MovieFragmentBox movieFragmentBox) {
        long j3 = j;
        Track track2 = track;
        TrackFragmentBox trackFragmentBox = new TrackFragmentBox();
        movieFragmentBox.addBox(trackFragmentBox);
        long j4 = j;
        long j5 = j2;
        Track track3 = track;
        int i2 = i;
        TrackFragmentBox trackFragmentBox2 = trackFragmentBox;
        createTfhd(j4, j5, track3, i2, trackFragmentBox2);
        createTfdt(j3, track2, trackFragmentBox);
        createTrun(j4, j5, track3, i2, trackFragmentBox2);
        if (track2 instanceof CencEncryptedTrack) {
            long j6 = j;
            long j7 = j2;
            CencEncryptedTrack cencEncryptedTrack = (CencEncryptedTrack) track2;
            int i3 = i;
            TrackFragmentBox trackFragmentBox3 = trackFragmentBox;
            createSaiz(j6, j7, cencEncryptedTrack, i3, trackFragmentBox3);
            createSenc(j6, j7, cencEncryptedTrack, i3, trackFragmentBox3);
            createSaio(j6, j7, cencEncryptedTrack, i3, trackFragmentBox3);
        }
        HashMap hashMap = new HashMap();
        for (Map.Entry next : track.getSampleGroups().entrySet()) {
            String type = ((GroupEntry) next.getKey()).getType();
            List list = (List) hashMap.get(type);
            if (list == null) {
                list = new ArrayList();
                hashMap.put(type, list);
            }
            list.add((GroupEntry) next.getKey());
        }
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            SampleGroupDescriptionBox sampleGroupDescriptionBox = new SampleGroupDescriptionBox();
            String str = (String) entry.getKey();
            sampleGroupDescriptionBox.setGroupEntries((List) entry.getValue());
            sampleGroupDescriptionBox.setGroupingType(str);
            SampleToGroupBox sampleToGroupBox = new SampleToGroupBox();
            sampleToGroupBox.setGroupingType(str);
            long j8 = 1;
            SampleToGroupBox.Entry entry2 = null;
            for (int l2i = CastUtils.l2i(j3 - 1); l2i < CastUtils.l2i(j2 - j8); l2i++) {
                int i4 = 0;
                int i5 = 0;
                while (i4 < ((List) entry.getValue()).size()) {
                    Iterator it2 = it;
                    i5 = Arrays.binarySearch(track.getSampleGroups().get((GroupEntry) ((List) entry.getValue()).get(i4)), (long) l2i) >= 0 ? 65537 + i4 : i5;
                    i4++;
                    it = it2;
                    j8 = 1;
                }
                if (entry2 == null || entry2.getGroupDescriptionIndex() != i5) {
                    SampleToGroupBox.Entry entry3 = new SampleToGroupBox.Entry(j8, i5);
                    sampleToGroupBox.getEntries().add(entry3);
                    entry2 = entry3;
                } else {
                    entry2.setSampleCount(entry2.getSampleCount() + j8);
                }
            }
            trackFragmentBox.addBox(sampleGroupDescriptionBox);
            trackFragmentBox.addBox(sampleToGroupBox);
        }
    }

    /* access modifiers changed from: protected */
    public void createSenc(long j, long j2, CencEncryptedTrack cencEncryptedTrack, int i, TrackFragmentBox trackFragmentBox) {
        SampleEncryptionBox sampleEncryptionBox = new SampleEncryptionBox();
        sampleEncryptionBox.setSubSampleEncryption(cencEncryptedTrack.hasSubSampleEncryption());
        sampleEncryptionBox.setEntries(cencEncryptedTrack.getSampleEncryptionEntries().subList(CastUtils.l2i(j - 1), CastUtils.l2i(j2 - 1)));
        trackFragmentBox.addBox(sampleEncryptionBox);
    }

    /* access modifiers changed from: protected */
    public void createSaio(long j, long j2, CencEncryptedTrack cencEncryptedTrack, int i, TrackFragmentBox trackFragmentBox) {
        Box next;
        SchemeTypeBox schemeTypeBox = (SchemeTypeBox) Path.getPath((AbstractContainerBox) cencEncryptedTrack.getSampleDescriptionBox(), "enc.[0]/sinf[0]/schm[0]");
        SampleAuxiliaryInformationOffsetsBox sampleAuxiliaryInformationOffsetsBox = new SampleAuxiliaryInformationOffsetsBox();
        trackFragmentBox.addBox(sampleAuxiliaryInformationOffsetsBox);
        sampleAuxiliaryInformationOffsetsBox.setAuxInfoType("cenc");
        sampleAuxiliaryInformationOffsetsBox.setFlags(1);
        long j3 = 8;
        Iterator<Box> it = trackFragmentBox.getBoxes().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Box next2 = it.next();
            if (next2 instanceof SampleEncryptionBox) {
                j3 += (long) ((SampleEncryptionBox) next2).getOffsetToFirstIV();
                break;
            }
            j3 += next2.getSize();
        }
        long j4 = j3 + 16;
        Iterator<Box> it2 = ((MovieFragmentBox) trackFragmentBox.getParent()).getBoxes().iterator();
        while (it2.hasNext() && (next = it2.next()) != trackFragmentBox) {
            j4 += next.getSize();
        }
        sampleAuxiliaryInformationOffsetsBox.setOffsets(new long[]{j4});
    }

    /* access modifiers changed from: protected */
    public void createSaiz(long j, long j2, CencEncryptedTrack cencEncryptedTrack, int i, TrackFragmentBox trackFragmentBox) {
        SampleDescriptionBox sampleDescriptionBox = cencEncryptedTrack.getSampleDescriptionBox();
        SchemeTypeBox schemeTypeBox = (SchemeTypeBox) Path.getPath((AbstractContainerBox) sampleDescriptionBox, "enc.[0]/sinf[0]/schm[0]");
        TrackEncryptionBox trackEncryptionBox = (TrackEncryptionBox) Path.getPath((AbstractContainerBox) sampleDescriptionBox, "enc.[0]/sinf[0]/schi[0]/tenc[0]");
        SampleAuxiliaryInformationSizesBox sampleAuxiliaryInformationSizesBox = new SampleAuxiliaryInformationSizesBox();
        sampleAuxiliaryInformationSizesBox.setAuxInfoType("cenc");
        sampleAuxiliaryInformationSizesBox.setFlags(1);
        if (cencEncryptedTrack.hasSubSampleEncryption()) {
            short[] sArr = new short[CastUtils.l2i(j2 - j)];
            List<CencSampleAuxiliaryDataFormat> subList = cencEncryptedTrack.getSampleEncryptionEntries().subList(CastUtils.l2i(j - 1), CastUtils.l2i(j2 - 1));
            for (int i2 = 0; i2 < sArr.length; i2++) {
                sArr[i2] = (short) subList.get(i2).getSize();
            }
            sampleAuxiliaryInformationSizesBox.setSampleInfoSizes(sArr);
        } else {
            sampleAuxiliaryInformationSizesBox.setDefaultSampleInfoSize(trackEncryptionBox.getDefaultIvSize());
            sampleAuxiliaryInformationSizesBox.setSampleCount(CastUtils.l2i(j2 - j));
        }
        trackFragmentBox.addBox(sampleAuxiliaryInformationSizesBox);
    }

    /* access modifiers changed from: protected */
    public List<Sample> getSamples(long j, long j2, Track track) {
        return track.getSamples().subList(CastUtils.l2i(j) - 1, CastUtils.l2i(j2) - 1);
    }

    /* access modifiers changed from: protected */
    public long[] getSampleSizes(long j, long j2, Track track, int i) {
        List<Sample> samples = getSamples(j, j2, track);
        long[] jArr = new long[samples.size()];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr[i2] = samples.get(i2).getSize();
        }
        return jArr;
    }

    /* access modifiers changed from: protected */
    public void createTfdt(long j, Track track, TrackFragmentBox trackFragmentBox) {
        TrackFragmentBaseMediaDecodeTimeBox trackFragmentBaseMediaDecodeTimeBox = new TrackFragmentBaseMediaDecodeTimeBox();
        trackFragmentBaseMediaDecodeTimeBox.setVersion(1);
        long[] sampleDurations = track.getSampleDurations();
        long j2 = 0;
        for (int i = 1; ((long) i) < j; i++) {
            j2 += sampleDurations[i - 1];
        }
        trackFragmentBaseMediaDecodeTimeBox.setBaseMediaDecodeTime(j2);
        trackFragmentBox.addBox(trackFragmentBaseMediaDecodeTimeBox);
    }

    /* access modifiers changed from: protected */
    public void createTrun(long j, long j2, Track track, int i, TrackFragmentBox trackFragmentBox) {
        long[] jArr;
        long j3;
        TrackRunBox trackRunBox = new TrackRunBox();
        trackRunBox.setVersion(1);
        long[] sampleSizes = getSampleSizes(j, j2, track, i);
        trackRunBox.setSampleDurationPresent(true);
        trackRunBox.setSampleSizePresent(true);
        ArrayList arrayList = new ArrayList(CastUtils.l2i(j2 - j));
        List<CompositionTimeToSample.Entry> compositionTimeEntries = track.getCompositionTimeEntries();
        CompositionTimeToSample.Entry[] entryArr = (compositionTimeEntries == null || compositionTimeEntries.size() <= 0) ? null : (CompositionTimeToSample.Entry[]) compositionTimeEntries.toArray(new CompositionTimeToSample.Entry[compositionTimeEntries.size()]);
        long count = (long) (entryArr != null ? entryArr[0].getCount() : -1);
        trackRunBox.setSampleCompositionTimeOffsetPresent(count > 0);
        long j4 = count;
        long j5 = 1;
        int i2 = 0;
        while (j5 < j) {
            long[] jArr2 = sampleSizes;
            if (entryArr != null) {
                j4--;
                j3 = 0;
                if (j4 == 0) {
                    if (entryArr.length - i2 > 1) {
                        i2++;
                        j4 = (long) entryArr[i2].getCount();
                    }
                    j5++;
                    long j6 = j3;
                    sampleSizes = jArr2;
                }
            } else {
                j3 = 0;
            }
            j5++;
            long j62 = j3;
            sampleSizes = jArr2;
        }
        boolean z = (track.getSampleDependencies() != null && !track.getSampleDependencies().isEmpty()) || !(track.getSyncSamples() == null || track.getSyncSamples().length == 0);
        trackRunBox.setSampleFlagsPresent(z);
        int i3 = 0;
        while (i3 < sampleSizes.length) {
            TrackFragmentBox trackFragmentBox2 = trackFragmentBox;
            TrackRunBox.Entry entry = new TrackRunBox.Entry();
            entry.setSampleSize(sampleSizes[i3]);
            if (z) {
                SampleFlags sampleFlags = new SampleFlags();
                if (track.getSampleDependencies() != null && !track.getSampleDependencies().isEmpty()) {
                    SampleDependencyTypeBox.Entry entry2 = track.getSampleDependencies().get(i3);
                    sampleFlags.setSampleDependsOn(entry2.getSampleDependsOn());
                    sampleFlags.setSampleIsDependedOn(entry2.getSampleIsDependentOn());
                    sampleFlags.setSampleHasRedundancy(entry2.getSampleHasRedundancy());
                }
                if (track.getSyncSamples() == null || track.getSyncSamples().length <= 0) {
                    jArr = sampleSizes;
                } else {
                    jArr = sampleSizes;
                    if (Arrays.binarySearch(track.getSyncSamples(), j + ((long) i3)) >= 0) {
                        sampleFlags.setSampleIsDifferenceSample(false);
                        sampleFlags.setSampleDependsOn(2);
                    } else {
                        sampleFlags.setSampleIsDifferenceSample(true);
                        sampleFlags.setSampleDependsOn(1);
                    }
                }
                entry.setSampleFlags(sampleFlags);
            } else {
                jArr = sampleSizes;
            }
            entry.setSampleDuration(track.getSampleDurations()[CastUtils.l2i((j + ((long) i3)) - 1)]);
            if (entryArr != null) {
                entry.setSampleCompositionTimeOffset(entryArr[i2].getOffset());
                j4--;
                if (j4 == 0 && entryArr.length - i2 > 1) {
                    i2++;
                    j4 = (long) entryArr[i2].getCount();
                }
            }
            arrayList.add(entry);
            i3++;
            sampleSizes = jArr;
        }
        trackRunBox.setEntries(arrayList);
        trackFragmentBox.addBox(trackRunBox);
    }

    /* access modifiers changed from: protected */
    public Box createMoof(long j, long j2, Track track, int i) {
        MovieFragmentBox movieFragmentBox = new MovieFragmentBox();
        long j3 = j;
        long j4 = j2;
        Track track2 = track;
        int i2 = i;
        MovieFragmentBox movieFragmentBox2 = movieFragmentBox;
        createMfhd(j3, j4, track2, i2, movieFragmentBox2);
        createTraf(j3, j4, track2, i2, movieFragmentBox2);
        TrackRunBox trackRunBox = movieFragmentBox.getTrackRunBoxes().get(0);
        trackRunBox.setDataOffset(1);
        trackRunBox.setDataOffset((int) (movieFragmentBox.getSize() + 8));
        return movieFragmentBox;
    }

    /* access modifiers changed from: protected */
    public Box createMvhd(Movie movie) {
        MovieHeaderBox movieHeaderBox = new MovieHeaderBox();
        movieHeaderBox.setVersion(1);
        movieHeaderBox.setCreationTime(getDate());
        movieHeaderBox.setModificationTime(getDate());
        long j = 0;
        movieHeaderBox.setDuration(0);
        movieHeaderBox.setTimescale(movie.getTimescale());
        for (Track next : movie.getTracks()) {
            if (j < next.getTrackMetaData().getTrackId()) {
                j = next.getTrackMetaData().getTrackId();
            }
        }
        movieHeaderBox.setNextTrackId(j + 1);
        return movieHeaderBox;
    }

    /* access modifiers changed from: protected */
    public Box createMoov(Movie movie) {
        MovieBox movieBox = new MovieBox();
        movieBox.addBox(createMvhd(movie));
        for (Track createTrak : movie.getTracks()) {
            movieBox.addBox(createTrak(createTrak, movie));
        }
        movieBox.addBox(createMvex(movie));
        return movieBox;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v18, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v16, resolved type: com.coremedia.iso.boxes.fragment.TrackExtendsBox} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.coremedia.iso.boxes.Box createTfra(com.googlecode.mp4parser.authoring.Track r34, com.coremedia.iso.boxes.Container r35) {
        /*
            r33 = this;
            com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox r0 = new com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox
            r0.<init>()
            r1 = 1
            r0.setVersion(r1)
            java.util.LinkedList r1 = new java.util.LinkedList
            r1.<init>()
            java.lang.String r2 = "moov/mvex/trex"
            r3 = r35
            java.util.List r2 = com.googlecode.mp4parser.util.Path.getPaths((com.coremedia.iso.boxes.Container) r3, (java.lang.String) r2)
            java.util.Iterator r2 = r2.iterator()
            r4 = 0
        L_0x001b:
            boolean r5 = r2.hasNext()
            if (r5 != 0) goto L_0x01b6
            java.util.List r2 = r35.getBoxes()
            java.util.Iterator r5 = r2.iterator()
            r2 = 0
            r6 = r2
        L_0x002c:
            boolean r8 = r5.hasNext()
            if (r8 != 0) goto L_0x0041
            r0.setEntries(r1)
            com.googlecode.mp4parser.authoring.TrackMetaData r1 = r34.getTrackMetaData()
            long r1 = r1.getTrackId()
            r0.setTrackId(r1)
            return r0
        L_0x0041:
            java.lang.Object r8 = r5.next()
            r15 = r8
            com.coremedia.iso.boxes.Box r15 = (com.coremedia.iso.boxes.Box) r15
            boolean r8 = r15 instanceof com.coremedia.iso.boxes.fragment.MovieFragmentBox
            if (r8 == 0) goto L_0x019d
            r8 = r15
            com.coremedia.iso.boxes.fragment.MovieFragmentBox r8 = (com.coremedia.iso.boxes.fragment.MovieFragmentBox) r8
            java.lang.Class<com.coremedia.iso.boxes.fragment.TrackFragmentBox> r9 = com.coremedia.iso.boxes.fragment.TrackFragmentBox.class
            java.util.List r13 = r8.getBoxes(r9)
            r14 = 0
            r11 = 0
        L_0x0057:
            int r8 = r13.size()
            if (r11 < r8) goto L_0x005f
            goto L_0x019d
        L_0x005f:
            java.lang.Object r8 = r13.get(r11)
            com.coremedia.iso.boxes.fragment.TrackFragmentBox r8 = (com.coremedia.iso.boxes.fragment.TrackFragmentBox) r8
            com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox r9 = r8.getTrackFragmentHeaderBox()
            long r9 = r9.getTrackId()
            com.googlecode.mp4parser.authoring.TrackMetaData r12 = r34.getTrackMetaData()
            long r16 = r12.getTrackId()
            int r12 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
            if (r12 != 0) goto L_0x017e
            java.lang.Class<com.coremedia.iso.boxes.fragment.TrackRunBox> r9 = com.coremedia.iso.boxes.fragment.TrackRunBox.class
            java.util.List r12 = r8.getBoxes(r9)
            r9 = 0
        L_0x0080:
            int r8 = r12.size()
            if (r9 < r8) goto L_0x0088
            goto L_0x017e
        L_0x0088:
            java.util.LinkedList r10 = new java.util.LinkedList
            r10.<init>()
            java.lang.Object r8 = r12.get(r9)
            r17 = r8
            com.coremedia.iso.boxes.fragment.TrackRunBox r17 = (com.coremedia.iso.boxes.fragment.TrackRunBox) r17
            r18 = r6
            r7 = 0
        L_0x0098:
            java.util.List r6 = r17.getEntries()
            int r6 = r6.size()
            if (r7 < r6) goto L_0x00cc
            int r6 = r10.size()
            java.util.List r7 = r17.getEntries()
            int r7 = r7.size()
            if (r6 != r7) goto L_0x00c4
            java.util.List r6 = r17.getEntries()
            int r6 = r6.size()
            if (r6 <= 0) goto L_0x00c4
            java.lang.Object r6 = r10.get(r14)
            com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox$Entry r6 = (com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox.Entry) r6
            r1.add(r6)
            goto L_0x00c7
        L_0x00c4:
            r1.addAll(r10)
        L_0x00c7:
            int r9 = r9 + 1
            r6 = r18
            goto L_0x0080
        L_0x00cc:
            java.util.List r6 = r17.getEntries()
            java.lang.Object r6 = r6.get(r7)
            r20 = r6
            com.coremedia.iso.boxes.fragment.TrackRunBox$Entry r20 = (com.coremedia.iso.boxes.fragment.TrackRunBox.Entry) r20
            if (r7 != 0) goto L_0x00e5
            boolean r6 = r17.isFirstSampleFlagsPresent()
            if (r6 == 0) goto L_0x00e5
            com.coremedia.iso.boxes.fragment.SampleFlags r6 = r17.getFirstSampleFlags()
            goto L_0x00f4
        L_0x00e5:
            boolean r6 = r17.isSampleFlagsPresent()
            if (r6 == 0) goto L_0x00f0
            com.coremedia.iso.boxes.fragment.SampleFlags r6 = r20.getSampleFlags()
            goto L_0x00f4
        L_0x00f0:
            com.coremedia.iso.boxes.fragment.SampleFlags r6 = r4.getDefaultSampleFlags()
        L_0x00f4:
            if (r6 != 0) goto L_0x010b
            java.lang.String r8 = r34.getHandler()
            java.lang.String r14 = "vide"
            boolean r8 = r8.equals(r14)
            if (r8 != 0) goto L_0x0103
            goto L_0x010b
        L_0x0103:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Cannot find SampleFlags for video track but it's required to build tfra"
            r0.<init>(r1)
            throw r0
        L_0x010b:
            if (r6 == 0) goto L_0x012c
            int r6 = r6.getSampleDependsOn()
            r8 = 2
            if (r6 != r8) goto L_0x0115
            goto L_0x012c
        L_0x0115:
            r21 = r0
            r22 = r1
            r23 = r4
            r24 = r5
            r27 = r7
            r28 = r9
            r4 = r10
            r30 = r11
            r31 = r12
            r0 = r13
            r25 = r15
            r32 = 0
            goto L_0x0161
        L_0x012c:
            com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox$Entry r14 = new com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox$Entry
            int r6 = r11 + 1
            r21 = r0
            r22 = r1
            long r0 = (long) r6
            int r6 = r9 + 1
            r23 = r4
            r24 = r5
            long r4 = (long) r6
            int r6 = r7 + 1
            r25 = r4
            long r4 = (long) r6
            r6 = r14
            r27 = r7
            r7 = r18
            r28 = r9
            r29 = r10
            r9 = r2
            r30 = r11
            r31 = r12
            r11 = r0
            r0 = r13
            r1 = r14
            r32 = 0
            r13 = r25
            r25 = r15
            r15 = r4
            r6.<init>(r7, r9, r11, r13, r15)
            r4 = r29
            r4.add(r1)
        L_0x0161:
            long r5 = r20.getSampleDuration()
            long r18 = r18 + r5
            int r7 = r27 + 1
            r13 = r0
            r10 = r4
            r0 = r21
            r1 = r22
            r4 = r23
            r5 = r24
            r15 = r25
            r9 = r28
            r11 = r30
            r12 = r31
            r14 = 0
            goto L_0x0098
        L_0x017e:
            r21 = r0
            r22 = r1
            r23 = r4
            r24 = r5
            r30 = r11
            r0 = r13
            r25 = r15
            r32 = 0
            int r11 = r30 + 1
            r13 = r0
            r0 = r21
            r1 = r22
            r4 = r23
            r5 = r24
            r15 = r25
            r14 = 0
            goto L_0x0057
        L_0x019d:
            r21 = r0
            r22 = r1
            r23 = r4
            r24 = r5
            r25 = r15
            long r0 = r25.getSize()
            long r2 = r2 + r0
            r0 = r21
            r1 = r22
            r4 = r23
            r5 = r24
            goto L_0x002c
        L_0x01b6:
            r21 = r0
            r22 = r1
            r23 = r4
            java.lang.Object r0 = r2.next()
            r4 = r0
            com.coremedia.iso.boxes.fragment.TrackExtendsBox r4 = (com.coremedia.iso.boxes.fragment.TrackExtendsBox) r4
            long r0 = r4.getTrackId()
            com.googlecode.mp4parser.authoring.TrackMetaData r5 = r34.getTrackMetaData()
            long r5 = r5.getTrackId()
            int r7 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            r0 = r21
            r1 = r22
            if (r7 != 0) goto L_0x01d9
            goto L_0x001b
        L_0x01d9:
            r4 = r23
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.builder.FragmentedMp4Builder.createTfra(com.googlecode.mp4parser.authoring.Track, com.coremedia.iso.boxes.Container):com.coremedia.iso.boxes.Box");
    }

    /* access modifiers changed from: protected */
    public Box createMfra(Movie movie, Container container) {
        MovieFragmentRandomAccessBox movieFragmentRandomAccessBox = new MovieFragmentRandomAccessBox();
        for (Track createTfra : movie.getTracks()) {
            movieFragmentRandomAccessBox.addBox(createTfra(createTfra, container));
        }
        MovieFragmentRandomAccessOffsetBox movieFragmentRandomAccessOffsetBox = new MovieFragmentRandomAccessOffsetBox();
        movieFragmentRandomAccessBox.addBox(movieFragmentRandomAccessOffsetBox);
        movieFragmentRandomAccessOffsetBox.setMfraSize(movieFragmentRandomAccessBox.getSize());
        return movieFragmentRandomAccessBox;
    }

    /* access modifiers changed from: protected */
    public Box createTrex(Movie movie, Track track) {
        TrackExtendsBox trackExtendsBox = new TrackExtendsBox();
        trackExtendsBox.setTrackId(track.getTrackMetaData().getTrackId());
        trackExtendsBox.setDefaultSampleDescriptionIndex(1);
        trackExtendsBox.setDefaultSampleDuration(0);
        trackExtendsBox.setDefaultSampleSize(0);
        SampleFlags sampleFlags = new SampleFlags();
        if ("soun".equals(track.getHandler()) || "subt".equals(track.getHandler())) {
            sampleFlags.setSampleDependsOn(2);
            sampleFlags.setSampleIsDependedOn(2);
        }
        trackExtendsBox.setDefaultSampleFlags(sampleFlags);
        return trackExtendsBox;
    }

    /* access modifiers changed from: protected */
    public Box createMvex(Movie movie) {
        MovieExtendsBox movieExtendsBox = new MovieExtendsBox();
        MovieExtendsHeaderBox movieExtendsHeaderBox = new MovieExtendsHeaderBox();
        movieExtendsHeaderBox.setVersion(1);
        for (Track trackDuration : movie.getTracks()) {
            long trackDuration2 = getTrackDuration(movie, trackDuration);
            if (movieExtendsHeaderBox.getFragmentDuration() < trackDuration2) {
                movieExtendsHeaderBox.setFragmentDuration(trackDuration2);
            }
        }
        movieExtendsBox.addBox(movieExtendsHeaderBox);
        for (Track createTrex : movie.getTracks()) {
            movieExtendsBox.addBox(createTrex(movie, createTrex));
        }
        return movieExtendsBox;
    }

    /* access modifiers changed from: protected */
    public Box createTkhd(Movie movie, Track track) {
        TrackHeaderBox trackHeaderBox = new TrackHeaderBox();
        trackHeaderBox.setVersion(1);
        trackHeaderBox.setFlags(7);
        trackHeaderBox.setAlternateGroup(track.getTrackMetaData().getGroup());
        trackHeaderBox.setCreationTime(track.getTrackMetaData().getCreationTime());
        trackHeaderBox.setDuration(0);
        trackHeaderBox.setHeight(track.getTrackMetaData().getHeight());
        trackHeaderBox.setWidth(track.getTrackMetaData().getWidth());
        trackHeaderBox.setLayer(track.getTrackMetaData().getLayer());
        trackHeaderBox.setModificationTime(getDate());
        trackHeaderBox.setTrackId(track.getTrackMetaData().getTrackId());
        trackHeaderBox.setVolume(track.getTrackMetaData().getVolume());
        return trackHeaderBox;
    }

    private long getTrackDuration(Movie movie, Track track) {
        return (track.getDuration() * movie.getTimescale()) / track.getTrackMetaData().getTimescale();
    }

    /* access modifiers changed from: protected */
    public Box createMdhd(Movie movie, Track track) {
        MediaHeaderBox mediaHeaderBox = new MediaHeaderBox();
        mediaHeaderBox.setCreationTime(track.getTrackMetaData().getCreationTime());
        mediaHeaderBox.setModificationTime(getDate());
        mediaHeaderBox.setDuration(0);
        mediaHeaderBox.setTimescale(track.getTrackMetaData().getTimescale());
        mediaHeaderBox.setLanguage(track.getTrackMetaData().getLanguage());
        return mediaHeaderBox;
    }

    /* access modifiers changed from: protected */
    public Box createStbl(Movie movie, Track track) {
        SampleTableBox sampleTableBox = new SampleTableBox();
        createStsd(track, sampleTableBox);
        sampleTableBox.addBox(new TimeToSampleBox());
        sampleTableBox.addBox(new SampleToChunkBox());
        sampleTableBox.addBox(new SampleSizeBox());
        sampleTableBox.addBox(new StaticChunkOffsetBox());
        return sampleTableBox;
    }

    /* access modifiers changed from: protected */
    public void createStsd(Track track, SampleTableBox sampleTableBox) {
        sampleTableBox.addBox(track.getSampleDescriptionBox());
    }

    /* access modifiers changed from: protected */
    public Box createMinf(Track track, Movie movie) {
        MediaInformationBox mediaInformationBox = new MediaInformationBox();
        if (track.getHandler().equals("vide")) {
            mediaInformationBox.addBox(new VideoMediaHeaderBox());
        } else if (track.getHandler().equals("soun")) {
            mediaInformationBox.addBox(new SoundMediaHeaderBox());
        } else if (track.getHandler().equals("text")) {
            mediaInformationBox.addBox(new NullMediaHeaderBox());
        } else if (track.getHandler().equals("subt")) {
            mediaInformationBox.addBox(new SubtitleMediaHeaderBox());
        } else if (track.getHandler().equals("hint")) {
            mediaInformationBox.addBox(new HintMediaHeaderBox());
        } else if (track.getHandler().equals("sbtl")) {
            mediaInformationBox.addBox(new NullMediaHeaderBox());
        }
        mediaInformationBox.addBox(createDinf(movie, track));
        mediaInformationBox.addBox(createStbl(movie, track));
        return mediaInformationBox;
    }

    /* access modifiers changed from: protected */
    public Box createMdiaHdlr(Track track, Movie movie) {
        HandlerBox handlerBox = new HandlerBox();
        handlerBox.setHandlerType(track.getHandler());
        return handlerBox;
    }

    /* access modifiers changed from: protected */
    public Box createMdia(Track track, Movie movie) {
        MediaBox mediaBox = new MediaBox();
        mediaBox.addBox(createMdhd(movie, track));
        mediaBox.addBox(createMdiaHdlr(track, movie));
        mediaBox.addBox(createMinf(track, movie));
        return mediaBox;
    }

    /* access modifiers changed from: protected */
    public Box createTrak(Track track, Movie movie) {
        Logger logger = LOG;
        logger.fine("Creating Track " + track);
        TrackBox trackBox = new TrackBox();
        trackBox.addBox(createTkhd(movie, track));
        Box createEdts = createEdts(track, movie);
        if (createEdts != null) {
            trackBox.addBox(createEdts);
        }
        trackBox.addBox(createMdia(track, movie));
        return trackBox;
    }

    /* access modifiers changed from: protected */
    public Box createEdts(Track track, Movie movie) {
        if (track.getEdits() == null || track.getEdits().size() <= 0) {
            return null;
        }
        EditListBox editListBox = new EditListBox();
        editListBox.setVersion(1);
        ArrayList arrayList = new ArrayList();
        for (Edit next : track.getEdits()) {
            arrayList.add(new EditListBox.Entry(editListBox, Math.round(next.getSegmentDuration() * ((double) movie.getTimescale())), (next.getMediaTime() * track.getTrackMetaData().getTimescale()) / next.getTimeScale(), next.getMediaRate()));
        }
        editListBox.setEntries(arrayList);
        EditBox editBox = new EditBox();
        editBox.addBox(editListBox);
        return editBox;
    }

    /* access modifiers changed from: protected */
    public DataInformationBox createDinf(Movie movie, Track track) {
        DataInformationBox dataInformationBox = new DataInformationBox();
        DataReferenceBox dataReferenceBox = new DataReferenceBox();
        dataInformationBox.addBox(dataReferenceBox);
        DataEntryUrlBox dataEntryUrlBox = new DataEntryUrlBox();
        dataEntryUrlBox.setFlags(1);
        dataReferenceBox.addBox(dataEntryUrlBox);
        return dataInformationBox;
    }

    public Fragmenter getFragmenter() {
        return this.fragmenter;
    }

    public void setFragmenter(Fragmenter fragmenter2) {
        this.fragmenter = fragmenter2;
    }
}
