package com.googlecode.mp4parser.authoring.builder;

import android.support.v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.ChunkOffsetBox;
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
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.SubtitleMediaHeaderBox;
import com.coremedia.iso.boxes.SyncSampleBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
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
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Math;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.googlecode.mp4parser.util.Path;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationOffsetsBox;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultMp4Builder implements Mp4Builder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    /* access modifiers changed from: private */
    public static Logger LOG = Logger.getLogger(DefaultMp4Builder.class);
    Map<Track, StaticChunkOffsetBox> chunkOffsetBoxes = new HashMap();
    private Fragmenter fragmenter;
    Set<SampleAuxiliaryInformationOffsetsBox> sampleAuxiliaryInformationOffsetsBoxes = new HashSet();
    HashMap<Track, List<Sample>> track2Sample = new HashMap<>();
    HashMap<Track, long[]> track2SampleSizes = new HashMap<>();

    /* access modifiers changed from: protected */
    public Box createUdta(Movie movie) {
        return null;
    }

    private static long sum(int[] iArr) {
        long j = 0;
        for (int i : iArr) {
            j += (long) i;
        }
        return j;
    }

    private static long sum(long[] jArr) {
        long j = 0;
        for (long j2 : jArr) {
            j += j2;
        }
        return j;
    }

    public static long gcd(long j, long j2) {
        return j2 == 0 ? j : gcd(j2, j % j2);
    }

    public void setFragmenter(Fragmenter fragmenter2) {
        this.fragmenter = fragmenter2;
    }

    public Container build(Movie movie) {
        Box next;
        if (this.fragmenter == null) {
            this.fragmenter = new BetterFragmenter(2.0d);
        }
        LOG.logDebug("Creating movie " + movie);
        Iterator<Track> it = movie.getTracks().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Track next2 = it.next();
            List<Sample> samples = next2.getSamples();
            putSamples(next2, samples);
            long[] jArr = new long[samples.size()];
            for (int i = 0; i < jArr.length; i++) {
                jArr[i] = samples.get(i).getSize();
            }
            this.track2SampleSizes.put(next2, jArr);
        }
        BasicContainer basicContainer = new BasicContainer();
        basicContainer.addBox(createFileTypeBox(movie));
        HashMap hashMap = new HashMap();
        for (Track next3 : movie.getTracks()) {
            hashMap.put(next3, getChunkSizes(next3));
        }
        MovieBox createMovieBox = createMovieBox(movie, hashMap);
        basicContainer.addBox(createMovieBox);
        long j = 0;
        for (SampleSizeBox sampleSizes : Path.getPaths((Box) createMovieBox, "trak/mdia/minf/stbl/stsz")) {
            j += sum(sampleSizes.getSampleSizes());
        }
        LOG.logDebug("About to create mdat");
        InterleaveChunkMdat interleaveChunkMdat = new InterleaveChunkMdat(this, movie, hashMap, j, (InterleaveChunkMdat) null);
        basicContainer.addBox(interleaveChunkMdat);
        LOG.logDebug("mdat crated");
        long dataOffset = interleaveChunkMdat.getDataOffset();
        for (StaticChunkOffsetBox chunkOffsets : this.chunkOffsetBoxes.values()) {
            long[] chunkOffsets2 = chunkOffsets.getChunkOffsets();
            for (int i2 = 0; i2 < chunkOffsets2.length; i2++) {
                chunkOffsets2[i2] = chunkOffsets2[i2] + dataOffset;
            }
        }
        for (SampleAuxiliaryInformationOffsetsBox next4 : this.sampleAuxiliaryInformationOffsetsBoxes) {
            long size = next4.getSize() + 44;
            Container container = next4;
            while (true) {
                Container parent = container.getParent();
                Iterator<Box> it2 = parent.getBoxes().iterator();
                while (it2.hasNext() && (next = it2.next()) != container) {
                    size += next.getSize();
                }
                if (!(parent instanceof Box)) {
                    break;
                }
                container = parent;
            }
            long[] offsets = next4.getOffsets();
            for (int i3 = 0; i3 < offsets.length; i3++) {
                offsets[i3] = offsets[i3] + size;
            }
            next4.setOffsets(offsets);
        }
        return basicContainer;
    }

    /* access modifiers changed from: protected */
    public List<Sample> putSamples(Track track, List<Sample> list) {
        return this.track2Sample.put(track, list);
    }

    /* access modifiers changed from: protected */
    public FileTypeBox createFileTypeBox(Movie movie) {
        LinkedList linkedList = new LinkedList();
        linkedList.add("mp42");
        linkedList.add("iso6");
        linkedList.add(VisualSampleEntry.TYPE3);
        linkedList.add("isom");
        return new FileTypeBox("iso6", 1, linkedList);
    }

    /* access modifiers changed from: protected */
    public MovieBox createMovieBox(Movie movie, Map<Track, int[]> map) {
        long j;
        MovieBox movieBox = new MovieBox();
        MovieHeaderBox movieHeaderBox = new MovieHeaderBox();
        movieHeaderBox.setCreationTime(new Date());
        movieHeaderBox.setModificationTime(new Date());
        movieHeaderBox.setMatrix(movie.getMatrix());
        long timescale = getTimescale(movie);
        long j2 = 0;
        for (Track next : movie.getTracks()) {
            Movie movie2 = movie;
            Map<Track, int[]> map2 = map;
            if (next.getEdits() == null || next.getEdits().isEmpty()) {
                j = (next.getDuration() * timescale) / next.getTrackMetaData().getTimescale();
            } else {
                double d = 0.0d;
                for (Edit segmentDuration : next.getEdits()) {
                    d += (double) ((long) segmentDuration.getSegmentDuration());
                }
                j = (long) (d * ((double) timescale));
            }
            if (j > j2) {
                j2 = j;
            }
        }
        movieHeaderBox.setDuration(j2);
        movieHeaderBox.setTimescale(timescale);
        long j3 = 0;
        for (Track next2 : movie.getTracks()) {
            Movie movie3 = movie;
            Map<Track, int[]> map3 = map;
            if (j3 < next2.getTrackMetaData().getTrackId()) {
                j3 = next2.getTrackMetaData().getTrackId();
            }
        }
        movieHeaderBox.setNextTrackId(j3 + 1);
        movieBox.addBox(movieHeaderBox);
        for (Track createTrackBox : movie.getTracks()) {
            movieBox.addBox(createTrackBox(createTrackBox, movie, map));
        }
        Box createUdta = createUdta(movie);
        if (createUdta != null) {
            movieBox.addBox(createUdta);
        }
        return movieBox;
    }

    /* access modifiers changed from: protected */
    public TrackBox createTrackBox(Track track, Movie movie, Map<Track, int[]> map) {
        TrackBox trackBox = new TrackBox();
        TrackHeaderBox trackHeaderBox = new TrackHeaderBox();
        trackHeaderBox.setEnabled(true);
        trackHeaderBox.setInMovie(true);
        trackHeaderBox.setMatrix(track.getTrackMetaData().getMatrix());
        trackHeaderBox.setAlternateGroup(track.getTrackMetaData().getGroup());
        trackHeaderBox.setCreationTime(track.getTrackMetaData().getCreationTime());
        if (track.getEdits() == null || track.getEdits().isEmpty()) {
            trackHeaderBox.setDuration((track.getDuration() * getTimescale(movie)) / track.getTrackMetaData().getTimescale());
        } else {
            long j = 0;
            for (Edit segmentDuration : track.getEdits()) {
                j += (long) segmentDuration.getSegmentDuration();
            }
            trackHeaderBox.setDuration(j * track.getTrackMetaData().getTimescale());
        }
        trackHeaderBox.setHeight(track.getTrackMetaData().getHeight());
        trackHeaderBox.setWidth(track.getTrackMetaData().getWidth());
        trackHeaderBox.setLayer(track.getTrackMetaData().getLayer());
        trackHeaderBox.setModificationTime(new Date());
        trackHeaderBox.setTrackId(track.getTrackMetaData().getTrackId());
        trackHeaderBox.setVolume(track.getTrackMetaData().getVolume());
        trackBox.addBox(trackHeaderBox);
        trackBox.addBox(createEdts(track, movie));
        MediaBox mediaBox = new MediaBox();
        trackBox.addBox(mediaBox);
        MediaHeaderBox mediaHeaderBox = new MediaHeaderBox();
        mediaHeaderBox.setCreationTime(track.getTrackMetaData().getCreationTime());
        mediaHeaderBox.setDuration(track.getDuration());
        mediaHeaderBox.setTimescale(track.getTrackMetaData().getTimescale());
        mediaHeaderBox.setLanguage(track.getTrackMetaData().getLanguage());
        mediaBox.addBox(mediaHeaderBox);
        HandlerBox handlerBox = new HandlerBox();
        mediaBox.addBox(handlerBox);
        handlerBox.setHandlerType(track.getHandler());
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
        DataInformationBox dataInformationBox = new DataInformationBox();
        DataReferenceBox dataReferenceBox = new DataReferenceBox();
        dataInformationBox.addBox(dataReferenceBox);
        DataEntryUrlBox dataEntryUrlBox = new DataEntryUrlBox();
        dataEntryUrlBox.setFlags(1);
        dataReferenceBox.addBox(dataEntryUrlBox);
        mediaInformationBox.addBox(dataInformationBox);
        mediaInformationBox.addBox(createStbl(track, movie, map));
        mediaBox.addBox(mediaInformationBox);
        Logger logger = LOG;
        logger.logDebug("done with trak for track_" + track.getTrackMetaData().getTrackId());
        return trackBox;
    }

    /* access modifiers changed from: protected */
    public Box createEdts(Track track, Movie movie) {
        if (track.getEdits() == null || track.getEdits().size() <= 0) {
            return null;
        }
        EditListBox editListBox = new EditListBox();
        editListBox.setVersion(0);
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
    public Box createStbl(Track track, Movie movie, Map<Track, int[]> map) {
        Track track2 = track;
        Map<Track, int[]> map2 = map;
        SampleTableBox sampleTableBox = new SampleTableBox();
        createStsd(track2, sampleTableBox);
        createStts(track2, sampleTableBox);
        createCtts(track2, sampleTableBox);
        createStss(track2, sampleTableBox);
        createSdtp(track2, sampleTableBox);
        createStsc(track2, map2, sampleTableBox);
        createStsz(track2, sampleTableBox);
        createStco(track2, movie, map2, sampleTableBox);
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
        for (Map.Entry entry : hashMap.entrySet()) {
            SampleGroupDescriptionBox sampleGroupDescriptionBox = new SampleGroupDescriptionBox();
            String str = (String) entry.getKey();
            sampleGroupDescriptionBox.setGroupingType(str);
            sampleGroupDescriptionBox.setGroupEntries((List) entry.getValue());
            SampleToGroupBox sampleToGroupBox = new SampleToGroupBox();
            sampleToGroupBox.setGroupingType(str);
            SampleToGroupBox.Entry entry2 = null;
            for (int i = 0; i < track.getSamples().size(); i++) {
                int i2 = 0;
                for (int i3 = 0; i3 < ((List) entry.getValue()).size(); i3++) {
                    if (Arrays.binarySearch(track.getSampleGroups().get((GroupEntry) ((List) entry.getValue()).get(i3)), (long) i) >= 0) {
                        i2 = i3 + 1;
                    }
                }
                if (entry2 == null || entry2.getGroupDescriptionIndex() != i2) {
                    SampleToGroupBox.Entry entry3 = new SampleToGroupBox.Entry(1, i2);
                    sampleToGroupBox.getEntries().add(entry3);
                    entry2 = entry3;
                } else {
                    entry2.setSampleCount(entry2.getSampleCount() + 1);
                }
            }
            sampleTableBox.addBox(sampleGroupDescriptionBox);
            sampleTableBox.addBox(sampleToGroupBox);
        }
        if (track2 instanceof CencEncryptedTrack) {
            createCencBoxes((CencEncryptedTrack) track2, sampleTableBox, map2.get(track2));
        }
        createSubs(track2, sampleTableBox);
        LOG.logDebug("done with stbl for track_" + track.getTrackMetaData().getTrackId());
        return sampleTableBox;
    }

    /* access modifiers changed from: protected */
    public void createSubs(Track track, SampleTableBox sampleTableBox) {
        if (track.getSubsampleInformationBox() != null) {
            sampleTableBox.addBox(track.getSubsampleInformationBox());
        }
    }

    /* access modifiers changed from: protected */
    public void createCencBoxes(CencEncryptedTrack cencEncryptedTrack, SampleTableBox sampleTableBox, int[] iArr) {
        SampleTableBox sampleTableBox2 = sampleTableBox;
        int[] iArr2 = iArr;
        SampleAuxiliaryInformationSizesBox sampleAuxiliaryInformationSizesBox = new SampleAuxiliaryInformationSizesBox();
        sampleAuxiliaryInformationSizesBox.setAuxInfoType("cenc");
        sampleAuxiliaryInformationSizesBox.setFlags(1);
        List<CencSampleAuxiliaryDataFormat> sampleEncryptionEntries = cencEncryptedTrack.getSampleEncryptionEntries();
        if (cencEncryptedTrack.hasSubSampleEncryption()) {
            short[] sArr = new short[sampleEncryptionEntries.size()];
            for (int i = 0; i < sArr.length; i++) {
                sArr[i] = (short) sampleEncryptionEntries.get(i).getSize();
            }
            sampleAuxiliaryInformationSizesBox.setSampleInfoSizes(sArr);
        } else {
            sampleAuxiliaryInformationSizesBox.setDefaultSampleInfoSize(8);
            sampleAuxiliaryInformationSizesBox.setSampleCount(cencEncryptedTrack.getSamples().size());
        }
        SampleAuxiliaryInformationOffsetsBox sampleAuxiliaryInformationOffsetsBox = new SampleAuxiliaryInformationOffsetsBox();
        SampleEncryptionBox sampleEncryptionBox = new SampleEncryptionBox();
        sampleEncryptionBox.setSubSampleEncryption(cencEncryptedTrack.hasSubSampleEncryption());
        sampleEncryptionBox.setEntries(sampleEncryptionEntries);
        long[] jArr = new long[iArr2.length];
        long offsetToFirstIV = (long) sampleEncryptionBox.getOffsetToFirstIV();
        int i2 = 0;
        int i3 = 0;
        while (i2 < iArr2.length) {
            jArr[i2] = offsetToFirstIV;
            int i4 = i3;
            int i5 = 0;
            while (i5 < iArr2[i2]) {
                offsetToFirstIV += (long) sampleEncryptionEntries.get(i4).getSize();
                i5++;
                i4++;
                sampleEncryptionBox = sampleEncryptionBox;
            }
            i2++;
            i3 = i4;
        }
        sampleAuxiliaryInformationOffsetsBox.setOffsets(jArr);
        sampleTableBox2.addBox(sampleAuxiliaryInformationSizesBox);
        sampleTableBox2.addBox(sampleAuxiliaryInformationOffsetsBox);
        sampleTableBox2.addBox(sampleEncryptionBox);
        this.sampleAuxiliaryInformationOffsetsBoxes.add(sampleAuxiliaryInformationOffsetsBox);
    }

    /* access modifiers changed from: protected */
    public void createStsd(Track track, SampleTableBox sampleTableBox) {
        sampleTableBox.addBox(track.getSampleDescriptionBox());
    }

    /* access modifiers changed from: protected */
    public void createStco(Track track, Movie movie, Map<Track, int[]> map, SampleTableBox sampleTableBox) {
        char c;
        int i;
        Track track2 = track;
        Map<Track, int[]> map2 = map;
        if (this.chunkOffsetBoxes.get(track2) == null) {
            LOG.logDebug("Calculating chunk offsets for track_" + track.getTrackMetaData().getTrackId());
            ArrayList<Track> arrayList = new ArrayList<>(map.keySet());
            Collections.sort(arrayList, new Comparator<Track>() {
                public int compare(Track track, Track track2) {
                    return CastUtils.l2i(track.getTrackMetaData().getTrackId() - track2.getTrackMetaData().getTrackId());
                }
            });
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            Iterator it = arrayList.iterator();
            while (true) {
                c = 0;
                if (!it.hasNext()) {
                    break;
                }
                ArrayList arrayList2 = arrayList;
                Track track3 = (Track) it.next();
                hashMap.put(track3, 0);
                hashMap2.put(track3, 0);
                hashMap3.put(track3, Double.valueOf(0.0d));
                this.chunkOffsetBoxes.put(track3, new StaticChunkOffsetBox());
                arrayList = arrayList2;
            }
            long j = 0;
            while (true) {
                Track track4 = null;
                for (Track track5 : arrayList) {
                    ArrayList arrayList3 = arrayList;
                    if ((track4 == null || ((Double) hashMap3.get(track5)).doubleValue() < ((Double) hashMap3.get(track4)).doubleValue()) && ((Integer) hashMap.get(track5)).intValue() < map2.get(track5).length) {
                        track4 = track5;
                    }
                    arrayList = arrayList3;
                    c = 0;
                }
                if (track4 == null) {
                    break;
                }
                ChunkOffsetBox chunkOffsetBox = this.chunkOffsetBoxes.get(track4);
                long[] chunkOffsets = chunkOffsetBox.getChunkOffsets();
                long[] jArr = new long[1];
                jArr[c] = j;
                chunkOffsetBox.setChunkOffsets(Mp4Arrays.copyOfAndAppend(chunkOffsets, jArr));
                int intValue = ((Integer) hashMap.get(track4)).intValue();
                int i2 = map2.get(track4)[intValue];
                int intValue2 = ((Integer) hashMap2.get(track4)).intValue();
                double doubleValue = ((Double) hashMap3.get(track4)).doubleValue();
                long[] sampleDurations = track4.getSampleDurations();
                int i3 = intValue2;
                while (true) {
                    i = intValue2 + i2;
                    if (i3 >= i) {
                        break;
                    }
                    long j2 = j + this.track2SampleSizes.get(track4)[i3];
                    doubleValue += ((double) sampleDurations[i3]) / ((double) track4.getTrackMetaData().getTimescale());
                    i3++;
                    intValue = intValue;
                    j = j2;
                    arrayList = arrayList;
                }
                hashMap.put(track4, Integer.valueOf(intValue + 1));
                hashMap2.put(track4, Integer.valueOf(i));
                hashMap3.put(track4, Double.valueOf(doubleValue));
                c = 0;
            }
        }
        sampleTableBox.addBox(this.chunkOffsetBoxes.get(track2));
    }

    /* access modifiers changed from: protected */
    public void createStsz(Track track, SampleTableBox sampleTableBox) {
        SampleSizeBox sampleSizeBox = new SampleSizeBox();
        sampleSizeBox.setSampleSizes(this.track2SampleSizes.get(track));
        sampleTableBox.addBox(sampleSizeBox);
    }

    /* access modifiers changed from: protected */
    public void createStsc(Track track, Map<Track, int[]> map, SampleTableBox sampleTableBox) {
        int[] iArr = map.get(track);
        SampleToChunkBox sampleToChunkBox = new SampleToChunkBox();
        sampleToChunkBox.setEntries(new LinkedList());
        long j = -2147483648L;
        for (int i = 0; i < iArr.length; i++) {
            if (j != ((long) iArr[i])) {
                sampleToChunkBox.getEntries().add(new SampleToChunkBox.Entry((long) (i + 1), (long) iArr[i], 1));
                j = (long) iArr[i];
            }
        }
        sampleTableBox.addBox(sampleToChunkBox);
    }

    /* access modifiers changed from: protected */
    public void createSdtp(Track track, SampleTableBox sampleTableBox) {
        if (track.getSampleDependencies() != null && !track.getSampleDependencies().isEmpty()) {
            SampleDependencyTypeBox sampleDependencyTypeBox = new SampleDependencyTypeBox();
            sampleDependencyTypeBox.setEntries(track.getSampleDependencies());
            sampleTableBox.addBox(sampleDependencyTypeBox);
        }
    }

    /* access modifiers changed from: protected */
    public void createStss(Track track, SampleTableBox sampleTableBox) {
        long[] syncSamples = track.getSyncSamples();
        if (syncSamples != null && syncSamples.length > 0) {
            SyncSampleBox syncSampleBox = new SyncSampleBox();
            syncSampleBox.setSampleNumber(syncSamples);
            sampleTableBox.addBox(syncSampleBox);
        }
    }

    /* access modifiers changed from: protected */
    public void createCtts(Track track, SampleTableBox sampleTableBox) {
        List<CompositionTimeToSample.Entry> compositionTimeEntries = track.getCompositionTimeEntries();
        if (compositionTimeEntries != null && !compositionTimeEntries.isEmpty()) {
            CompositionTimeToSample compositionTimeToSample = new CompositionTimeToSample();
            compositionTimeToSample.setEntries(compositionTimeEntries);
            sampleTableBox.addBox(compositionTimeToSample);
        }
    }

    /* access modifiers changed from: protected */
    public void createStts(Track track, SampleTableBox sampleTableBox) {
        ArrayList arrayList = new ArrayList();
        TimeToSampleBox.Entry entry = null;
        for (long j : track.getSampleDurations()) {
            if (entry == null || entry.getDelta() != j) {
                entry = new TimeToSampleBox.Entry(1, j);
                arrayList.add(entry);
            } else {
                entry.setCount(entry.getCount() + 1);
            }
        }
        TimeToSampleBox timeToSampleBox = new TimeToSampleBox();
        timeToSampleBox.setEntries(arrayList);
        sampleTableBox.addBox(timeToSampleBox);
    }

    /* access modifiers changed from: package-private */
    public int[] getChunkSizes(Track track) {
        long j;
        long[] sampleNumbers = this.fragmenter.sampleNumbers(track);
        int[] iArr = new int[sampleNumbers.length];
        int i = 0;
        while (i < sampleNumbers.length) {
            long j2 = sampleNumbers[i] - 1;
            int i2 = i + 1;
            if (sampleNumbers.length == i2) {
                j = (long) track.getSamples().size();
            } else {
                j = sampleNumbers[i2] - 1;
            }
            iArr[i] = CastUtils.l2i(j - j2);
            i = i2;
        }
        return iArr;
    }

    public long getTimescale(Movie movie) {
        long timescale = movie.getTracks().iterator().next().getTrackMetaData().getTimescale();
        for (Track trackMetaData : movie.getTracks()) {
            timescale = Math.lcm(timescale, trackMetaData.getTrackMetaData().getTimescale());
        }
        return timescale;
    }

    private class InterleaveChunkMdat implements Box {
        List<List<Sample>> chunkList;
        long contentSize;
        Container parent;
        final /* synthetic */ DefaultMp4Builder this$0;
        List<Track> tracks;

        private boolean isSmallBox(long j) {
            return j + 8 < 4294967296L;
        }

        public String getType() {
            return MediaDataBox.TYPE;
        }

        public void parse(DataSource dataSource, ByteBuffer byteBuffer, long j, BoxParser boxParser) throws IOException {
        }

        private InterleaveChunkMdat(DefaultMp4Builder defaultMp4Builder, Movie movie, Map<Track, int[]> map, long j) {
            int i;
            Map<Track, int[]> map2 = map;
            this.this$0 = defaultMp4Builder;
            this.chunkList = new ArrayList();
            this.contentSize = j;
            this.tracks = movie.getTracks();
            ArrayList<Track> arrayList = new ArrayList<>(map.keySet());
            Collections.sort(arrayList, new Comparator<Track>() {
                public int compare(Track track, Track track2) {
                    return CastUtils.l2i(track.getTrackMetaData().getTrackId() - track2.getTrackMetaData().getTrackId());
                }
            });
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            for (Track track : arrayList) {
                hashMap.put(track, 0);
                hashMap2.put(track, 0);
                hashMap3.put(track, Double.valueOf(0.0d));
            }
            while (true) {
                Track track2 = null;
                for (Track track3 : arrayList) {
                    if ((track2 == null || ((Double) hashMap3.get(track3)).doubleValue() < ((Double) hashMap3.get(track2)).doubleValue()) && ((Integer) hashMap.get(track3)).intValue() < map2.get(track3).length) {
                        track2 = track3;
                    }
                }
                if (track2 != null) {
                    int intValue = ((Integer) hashMap.get(track2)).intValue();
                    int i2 = map2.get(track2)[intValue];
                    int intValue2 = ((Integer) hashMap2.get(track2)).intValue();
                    double doubleValue = ((Double) hashMap3.get(track2)).doubleValue();
                    int i3 = intValue2;
                    while (true) {
                        i = intValue2 + i2;
                        if (i3 >= i) {
                            break;
                        }
                        doubleValue += ((double) track2.getSampleDurations()[i3]) / ((double) track2.getTrackMetaData().getTimescale());
                        i3++;
                        i2 = i2;
                        intValue = intValue;
                    }
                    this.chunkList.add(track2.getSamples().subList(intValue2, i));
                    hashMap.put(track2, Integer.valueOf(intValue + 1));
                    hashMap2.put(track2, Integer.valueOf(i));
                    hashMap3.put(track2, Double.valueOf(doubleValue));
                } else {
                    return;
                }
            }
        }

        /* synthetic */ InterleaveChunkMdat(DefaultMp4Builder defaultMp4Builder, Movie movie, Map map, long j, InterleaveChunkMdat interleaveChunkMdat) {
            this(defaultMp4Builder, movie, map, j);
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

        /* JADX WARNING: type inference failed for: r0v3, types: [com.coremedia.iso.boxes.Container] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long getDataOffset() {
            /*
                r7 = this;
                r0 = 16
                r1 = r0
                r0 = r7
            L_0x0004:
                boolean r3 = r0 instanceof com.coremedia.iso.boxes.Box
                if (r3 != 0) goto L_0x0009
                return r1
            L_0x0009:
                r3 = r0
                com.coremedia.iso.boxes.Box r3 = (com.coremedia.iso.boxes.Box) r3
                com.coremedia.iso.boxes.Container r4 = r3.getParent()
                java.util.List r4 = r4.getBoxes()
                java.util.Iterator r4 = r4.iterator()
            L_0x0018:
                boolean r5 = r4.hasNext()
                if (r5 != 0) goto L_0x001f
                goto L_0x0027
            L_0x001f:
                java.lang.Object r5 = r4.next()
                com.coremedia.iso.boxes.Box r5 = (com.coremedia.iso.boxes.Box) r5
                if (r0 != r5) goto L_0x002c
            L_0x0027:
                com.coremedia.iso.boxes.Container r0 = r3.getParent()
                goto L_0x0004
            L_0x002c:
                long r5 = r5.getSize()
                long r1 = r1 + r5
                goto L_0x0018
            */
            throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder.InterleaveChunkMdat.getDataOffset():long");
        }

        public long getSize() {
            return this.contentSize + 16;
        }

        public void getBox(WritableByteChannel writableByteChannel) throws IOException {
            ByteBuffer allocate = ByteBuffer.allocate(16);
            long size = getSize();
            if (isSmallBox(size)) {
                IsoTypeWriter.writeUInt32(allocate, size);
            } else {
                IsoTypeWriter.writeUInt32(allocate, 1);
            }
            allocate.put(IsoFile.fourCCtoBytes(MediaDataBox.TYPE));
            if (isSmallBox(size)) {
                allocate.put(new byte[8]);
            } else {
                IsoTypeWriter.writeUInt64(allocate, size);
            }
            allocate.rewind();
            writableByteChannel.write(allocate);
            DefaultMp4Builder.LOG.logDebug("About to write " + this.contentSize);
            long j = 0;
            long j2 = 0;
            for (List<Sample> it : this.chunkList) {
                for (Sample sample : it) {
                    sample.writeTo(writableByteChannel);
                    j2 += sample.getSize();
                    if (j2 > PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
                        j2 -= PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
                        j++;
                        DefaultMp4Builder.LOG.logDebug("Written " + j + "MB");
                    }
                }
            }
        }
    }
}
