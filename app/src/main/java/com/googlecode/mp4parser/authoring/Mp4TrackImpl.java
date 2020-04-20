package com.googlecode.mp4parser.authoring;

import android.support.v4.internal.view.SupportMenu;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.EditListBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.SampleFlags;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.coremedia.iso.boxes.mdat.SampleList;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Mp4TrackImpl extends AbstractTrack {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private List<CompositionTimeToSample.Entry> compositionTimeEntries;
    private long[] decodingTimes;
    IsoFile[] fragments;
    private String handler;
    private List<SampleDependencyTypeBox.Entry> sampleDependencies;
    private SampleDescriptionBox sampleDescriptionBox;
    private List<Sample> samples;
    private SubSampleInformationBox subSampleInformationBox;
    private long[] syncSamples = null;
    TrackBox trackBox;
    private TrackMetaData trackMetaData;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Mp4TrackImpl(String str, TrackBox trackBox2, IsoFile... isoFileArr) {
        super(str);
        ArrayList arrayList;
        String str2;
        SampleFlags sampleFlags;
        Mp4TrackImpl mp4TrackImpl = this;
        TrackBox trackBox3 = trackBox2;
        IsoFile[] isoFileArr2 = isoFileArr;
        mp4TrackImpl.trackMetaData = new TrackMetaData();
        mp4TrackImpl.subSampleInformationBox = null;
        mp4TrackImpl.trackBox = trackBox3;
        long trackId = trackBox2.getTrackHeaderBox().getTrackId();
        mp4TrackImpl.samples = new SampleList(trackBox3, isoFileArr2);
        SampleTableBox sampleTableBox = trackBox2.getMediaBox().getMediaInformationBox().getSampleTableBox();
        mp4TrackImpl.handler = trackBox2.getMediaBox().getHandlerBox().getHandlerType();
        ArrayList arrayList2 = new ArrayList();
        mp4TrackImpl.compositionTimeEntries = new ArrayList();
        mp4TrackImpl.sampleDependencies = new ArrayList();
        arrayList2.addAll(sampleTableBox.getTimeToSampleBox().getEntries());
        if (sampleTableBox.getCompositionTimeToSample() != null) {
            mp4TrackImpl.compositionTimeEntries.addAll(sampleTableBox.getCompositionTimeToSample().getEntries());
        }
        if (sampleTableBox.getSampleDependencyTypeBox() != null) {
            mp4TrackImpl.sampleDependencies.addAll(sampleTableBox.getSampleDependencyTypeBox().getEntries());
        }
        if (sampleTableBox.getSyncSampleBox() != null) {
            mp4TrackImpl.syncSamples = sampleTableBox.getSyncSampleBox().getSampleNumber();
        }
        String str3 = SubSampleInformationBox.TYPE;
        mp4TrackImpl.subSampleInformationBox = (SubSampleInformationBox) Path.getPath((AbstractContainerBox) sampleTableBox, str3);
        ArrayList<MovieFragmentBox> arrayList3 = new ArrayList<>();
        arrayList3.addAll(((Box) trackBox2.getParent()).getParent().getBoxes(MovieFragmentBox.class));
        int length = isoFileArr2.length;
        int i = 0;
        while (i < length) {
            long j = trackId;
            String str4 = str3;
            ArrayList arrayList4 = arrayList3;
            arrayList4.addAll(isoFileArr2[i].getBoxes(MovieFragmentBox.class));
            i++;
            mp4TrackImpl = this;
            arrayList3 = arrayList4;
        }
        mp4TrackImpl.sampleDescriptionBox = sampleTableBox.getSampleDescriptionBox();
        List<MovieExtendsBox> boxes = trackBox2.getParent().getBoxes(MovieExtendsBox.class);
        if (boxes.size() > 0) {
            for (MovieExtendsBox boxes2 : boxes) {
                for (TrackExtendsBox next : boxes2.getBoxes(TrackExtendsBox.class)) {
                    if (next.getTrackId() == trackId) {
                        if (Path.getPaths(((Box) trackBox2.getParent()).getParent(), "/moof/traf/subs").size() > 0) {
                            mp4TrackImpl.subSampleInformationBox = new SubSampleInformationBox();
                        }
                        long j2 = 1;
                        long j3 = 1;
                        for (MovieFragmentBox boxes3 : arrayList3) {
                            long j4 = j3;
                            for (TrackFragmentBox next2 : boxes3.getBoxes(TrackFragmentBox.class)) {
                                if (next2.getTrackFragmentHeaderBox().getTrackId() == trackId) {
                                    TrackFragmentBox trackFragmentBox = next2;
                                    long j5 = trackId;
                                    long j6 = j2;
                                    mp4TrackImpl.sampleGroups = getSampleGroups(sampleTableBox.getBoxes(SampleGroupDescriptionBox.class), Path.getPaths((Container) next2, SampleGroupDescriptionBox.TYPE), Path.getPaths((Container) next2, SampleToGroupBox.TYPE), mp4TrackImpl.sampleGroups, j4 - j2);
                                    SubSampleInformationBox subSampleInformationBox2 = (SubSampleInformationBox) Path.getPath((AbstractContainerBox) trackFragmentBox, str3);
                                    if (subSampleInformationBox2 != null) {
                                        long j7 = (j4 - ((long) 0)) - j6;
                                        for (SubSampleInformationBox.SubSampleEntry next3 : subSampleInformationBox2.getEntries()) {
                                            SubSampleInformationBox.SubSampleEntry subSampleEntry = new SubSampleInformationBox.SubSampleEntry();
                                            subSampleEntry.getSubsampleEntries().addAll(next3.getSubsampleEntries());
                                            if (j7 != 0) {
                                                subSampleEntry.setSampleDelta(j7 + next3.getSampleDelta());
                                                j7 = 0;
                                            } else {
                                                subSampleEntry.setSampleDelta(next3.getSampleDelta());
                                            }
                                            mp4TrackImpl.subSampleInformationBox.getEntries().add(subSampleEntry);
                                        }
                                    }
                                    for (TrackRunBox next4 : trackFragmentBox.getBoxes(TrackRunBox.class)) {
                                        TrackFragmentHeaderBox trackFragmentHeaderBox = ((TrackFragmentBox) next4.getParent()).getTrackFragmentHeaderBox();
                                        boolean z = true;
                                        for (TrackRunBox.Entry next5 : next4.getEntries()) {
                                            if (!next4.isSampleDurationPresent()) {
                                                str2 = str3;
                                                arrayList = arrayList3;
                                                if (trackFragmentHeaderBox.hasDefaultSampleDuration()) {
                                                    arrayList2.add(new TimeToSampleBox.Entry(j6, trackFragmentHeaderBox.getDefaultSampleDuration()));
                                                } else {
                                                    arrayList2.add(new TimeToSampleBox.Entry(j6, next.getDefaultSampleDuration()));
                                                }
                                            } else if (arrayList2.size() == 0 || ((TimeToSampleBox.Entry) arrayList2.get(arrayList2.size() - 1)).getDelta() != next5.getSampleDuration()) {
                                                str2 = str3;
                                                arrayList = arrayList3;
                                                arrayList2.add(new TimeToSampleBox.Entry(j6, next5.getSampleDuration()));
                                            } else {
                                                TimeToSampleBox.Entry entry = (TimeToSampleBox.Entry) arrayList2.get(arrayList2.size() - 1);
                                                str2 = str3;
                                                arrayList = arrayList3;
                                                entry.setCount(entry.getCount() + j6);
                                            }
                                            if (next4.isSampleCompositionTimeOffsetPresent()) {
                                                if (mp4TrackImpl.compositionTimeEntries.size() != 0) {
                                                    List<CompositionTimeToSample.Entry> list = mp4TrackImpl.compositionTimeEntries;
                                                    if (((long) list.get(list.size() - 1).getOffset()) == next5.getSampleCompositionTimeOffset()) {
                                                        List<CompositionTimeToSample.Entry> list2 = mp4TrackImpl.compositionTimeEntries;
                                                        CompositionTimeToSample.Entry entry2 = list2.get(list2.size() - 1);
                                                        entry2.setCount(entry2.getCount() + 1);
                                                    }
                                                }
                                                mp4TrackImpl.compositionTimeEntries.add(new CompositionTimeToSample.Entry(1, CastUtils.l2i(next5.getSampleCompositionTimeOffset())));
                                            }
                                            if (next4.isSampleFlagsPresent()) {
                                                sampleFlags = next5.getSampleFlags();
                                            } else if (z && next4.isFirstSampleFlagsPresent()) {
                                                sampleFlags = next4.getFirstSampleFlags();
                                            } else if (trackFragmentHeaderBox.hasDefaultSampleFlags()) {
                                                sampleFlags = trackFragmentHeaderBox.getDefaultSampleFlags();
                                            } else {
                                                sampleFlags = next.getDefaultSampleFlags();
                                            }
                                            if (sampleFlags != null && !sampleFlags.isSampleIsDifferenceSample()) {
                                                mp4TrackImpl.syncSamples = Mp4Arrays.copyOfAndAppend(mp4TrackImpl.syncSamples, j4);
                                            }
                                            j4 += j6;
                                            str3 = str2;
                                            arrayList3 = arrayList;
                                            z = false;
                                        }
                                    }
                                    j2 = j6;
                                    trackId = j5;
                                }
                            }
                            j3 = j4;
                        }
                    }
                }
            }
        } else {
            mp4TrackImpl.sampleGroups = getSampleGroups(sampleTableBox.getBoxes(SampleGroupDescriptionBox.class), (List<SampleGroupDescriptionBox>) null, sampleTableBox.getBoxes(SampleToGroupBox.class), mp4TrackImpl.sampleGroups, 0);
        }
        mp4TrackImpl.decodingTimes = TimeToSampleBox.blowupTimeToSamples(arrayList2);
        MediaHeaderBox mediaHeaderBox = trackBox2.getMediaBox().getMediaHeaderBox();
        TrackHeaderBox trackHeaderBox = trackBox2.getTrackHeaderBox();
        mp4TrackImpl.trackMetaData.setTrackId(trackHeaderBox.getTrackId());
        mp4TrackImpl.trackMetaData.setCreationTime(mediaHeaderBox.getCreationTime());
        mp4TrackImpl.trackMetaData.setLanguage(mediaHeaderBox.getLanguage());
        mp4TrackImpl.trackMetaData.setModificationTime(mediaHeaderBox.getModificationTime());
        mp4TrackImpl.trackMetaData.setTimescale(mediaHeaderBox.getTimescale());
        mp4TrackImpl.trackMetaData.setHeight(trackHeaderBox.getHeight());
        mp4TrackImpl.trackMetaData.setWidth(trackHeaderBox.getWidth());
        mp4TrackImpl.trackMetaData.setLayer(trackHeaderBox.getLayer());
        mp4TrackImpl.trackMetaData.setMatrix(trackHeaderBox.getMatrix());
        mp4TrackImpl.trackMetaData.setVolume(trackHeaderBox.getVolume());
        EditListBox editListBox = (EditListBox) Path.getPath((AbstractContainerBox) trackBox3, "edts/elst");
        MovieHeaderBox movieHeaderBox = (MovieHeaderBox) Path.getPath((AbstractContainerBox) trackBox3, "../mvhd");
        if (editListBox != null) {
            for (Iterator<EditListBox.Entry> it = editListBox.getEntries().iterator(); it.hasNext(); it = it) {
                EditListBox.Entry next6 = it.next();
                mp4TrackImpl.edits.add(new Edit(next6.getMediaTime(), mediaHeaderBox.getTimescale(), next6.getMediaRate(), ((double) next6.getSegmentDuration()) / ((double) movieHeaderBox.getTimescale())));
                mp4TrackImpl = this;
                mediaHeaderBox = mediaHeaderBox;
            }
        }
    }

    private Map<GroupEntry, long[]> getSampleGroups(List<SampleGroupDescriptionBox> list, List<SampleGroupDescriptionBox> list2, List<SampleToGroupBox> list3, Map<GroupEntry, long[]> map, long j) {
        Map<GroupEntry, long[]> map2 = map;
        for (SampleToGroupBox next : list3) {
            int i = 0;
            for (SampleToGroupBox.Entry next2 : next.getEntries()) {
                if (next2.getGroupDescriptionIndex() > 0) {
                    GroupEntry groupEntry = null;
                    if (next2.getGroupDescriptionIndex() > 65535) {
                        for (SampleGroupDescriptionBox next3 : list2) {
                            if (next3.getGroupingType().equals(next.getGroupingType())) {
                                groupEntry = next3.getGroupEntries().get((next2.getGroupDescriptionIndex() - 1) & SupportMenu.USER_MASK);
                            }
                        }
                    } else {
                        for (SampleGroupDescriptionBox next4 : list) {
                            if (next4.getGroupingType().equals(next.getGroupingType())) {
                                groupEntry = next4.getGroupEntries().get(next2.getGroupDescriptionIndex() - 1);
                            }
                        }
                    }
                    GroupEntry groupEntry2 = groupEntry;
                    long[] jArr = map2.get(groupEntry2);
                    if (jArr == null) {
                        jArr = new long[0];
                    }
                    long[] jArr2 = jArr;
                    long[] jArr3 = new long[(CastUtils.l2i(next2.getSampleCount()) + jArr2.length)];
                    System.arraycopy(jArr2, 0, jArr3, 0, jArr2.length);
                    int i2 = 0;
                    while (true) {
                        long j2 = (long) i2;
                        if (j2 >= next2.getSampleCount()) {
                            break;
                        }
                        jArr3[jArr2.length + i2] = j + ((long) i) + j2;
                        i2++;
                    }
                    map2.put(groupEntry2, jArr3);
                }
                i = (int) (((long) i) + next2.getSampleCount());
            }
        }
        return map2;
    }

    public void close() throws IOException {
        Container parent = this.trackBox.getParent();
        if (parent instanceof BasicContainer) {
            ((BasicContainer) parent).close();
        }
        IsoFile[] isoFileArr = this.fragments;
        if (isoFileArr != null) {
            for (IsoFile close : isoFileArr) {
                close.close();
            }
        }
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public synchronized long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return this.compositionTimeEntries;
    }

    public long[] getSyncSamples() {
        long[] jArr = this.syncSamples;
        if (jArr == null || jArr.length == this.samples.size()) {
            return null;
        }
        return this.syncSamples;
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.sampleDependencies;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public String getHandler() {
        return this.handler;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.subSampleInformationBox;
    }
}
