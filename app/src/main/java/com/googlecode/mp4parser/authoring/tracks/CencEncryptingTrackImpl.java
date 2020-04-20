package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.OriginalFormatBox;
import com.coremedia.iso.boxes.ProtectionSchemeInformationBox;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SchemeInformationBox;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.authoring.tracks.h264.H264NalUnitHeader;
import com.googlecode.mp4parser.authoring.tracks.h264.H264TrackImpl;
import com.googlecode.mp4parser.authoring.tracks.h265.H265NalUnitHeader;
import com.googlecode.mp4parser.authoring.tracks.h265.H265TrackImpl;
import com.googlecode.mp4parser.boxes.cenc.CencEncryptingSampleList;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.CencSampleEncryptionInformationGroupEntry;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.RangeStartMap;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;
import com.mp4parser.iso14496.part15.HevcConfigurationBox;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import com.mp4parser.iso23001.part7.TrackEncryptionBox;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

public class CencEncryptingTrackImpl implements CencEncryptedTrack {
    List<CencSampleAuxiliaryDataFormat> cencSampleAuxiliaryData;
    Object configurationBox;
    UUID defaultKeyId;
    boolean dummyIvs;
    private final String encryptionAlgo;
    RangeStartMap<Integer, SecretKey> indexToKey;
    Map<UUID, SecretKey> keys;
    Map<GroupEntry, long[]> sampleGroups;
    List<Sample> samples;
    Track source;
    SampleDescriptionBox stsd;
    boolean subSampleEncryption;

    public CencEncryptingTrackImpl(Track track, UUID uuid, SecretKey secretKey, boolean z) {
        this(track, uuid, Collections.singletonMap(uuid, secretKey), (Map<CencSampleEncryptionInformationGroupEntry, long[]>) null, "cenc", z);
    }

    public CencEncryptingTrackImpl(Track track, UUID uuid, Map<UUID, SecretKey> map, Map<CencSampleEncryptionInformationGroupEntry, long[]> map2, String str, boolean z) {
        this(track, uuid, map, map2, str, z, false);
    }

    public CencEncryptingTrackImpl(Track track, UUID uuid, Map<UUID, SecretKey> map, Map<CencSampleEncryptionInformationGroupEntry, long[]> map2, String str, boolean z, boolean z2) {
        UUID uuid2 = uuid;
        Map<UUID, SecretKey> map3 = map;
        boolean z3 = z;
        this.keys = new HashMap();
        char c = 0;
        this.dummyIvs = false;
        this.subSampleEncryption = false;
        Object obj = null;
        this.stsd = null;
        this.source = track;
        this.keys = map3;
        this.defaultKeyId = uuid2;
        this.dummyIvs = z3;
        this.encryptionAlgo = str;
        this.sampleGroups = new HashMap();
        for (Map.Entry next : track.getSampleGroups().entrySet()) {
            if (!(next.getKey() instanceof CencSampleEncryptionInformationGroupEntry)) {
                this.sampleGroups.put((GroupEntry) next.getKey(), (long[]) next.getValue());
            }
            c = 0;
            obj = null;
        }
        if (map2 != null) {
            for (Map.Entry next2 : map2.entrySet()) {
                this.sampleGroups.put((GroupEntry) next2.getKey(), (long[]) next2.getValue());
            }
        }
        this.sampleGroups = new HashMap<GroupEntry, long[]>(this.sampleGroups) {
            public long[] put(GroupEntry groupEntry, long[] jArr) {
                if (!(groupEntry instanceof CencSampleEncryptionInformationGroupEntry)) {
                    return (long[]) super.put(groupEntry, jArr);
                }
                throw new RuntimeException("Please supply CencSampleEncryptionInformationGroupEntries in the constructor");
            }
        };
        this.samples = track.getSamples();
        this.cencSampleAuxiliaryData = new ArrayList();
        BigInteger bigInteger = new BigInteger("1");
        int i = 8;
        byte[] bArr = new byte[8];
        if (!z3) {
            new SecureRandom().nextBytes(bArr);
        }
        BigInteger bigInteger2 = new BigInteger(1, bArr);
        ArrayList arrayList = new ArrayList();
        if (map2 != null) {
            arrayList.addAll(map2.keySet());
        }
        this.indexToKey = new RangeStartMap<>();
        int i2 = -1;
        int i3 = 0;
        int i4 = -1;
        while (i3 < track.getSamples().size()) {
            int i5 = 0;
            int i6 = 0;
            while (i5 < arrayList.size()) {
                BigInteger bigInteger3 = bigInteger2;
                if (Arrays.binarySearch(getSampleGroups().get((GroupEntry) arrayList.get(i5)), (long) i3) >= 0) {
                    i6 = i5 + 1;
                }
                i5++;
                bigInteger2 = bigInteger3;
                obj = null;
                i = 8;
            }
            if (i4 != i6) {
                if (i6 == 0) {
                    this.indexToKey.put(Integer.valueOf(i3), map3.get(uuid2));
                } else {
                    int i7 = i6 - 1;
                    if (((CencSampleEncryptionInformationGroupEntry) arrayList.get(i7)).getKid() != null) {
                        SecretKey secretKey = map3.get(((CencSampleEncryptionInformationGroupEntry) arrayList.get(i7)).getKid());
                        if (secretKey != null) {
                            this.indexToKey.put(Integer.valueOf(i3), secretKey);
                        } else {
                            throw new RuntimeException("Key " + ((CencSampleEncryptionInformationGroupEntry) arrayList.get(i7)).getKid() + " was not supplied for decryption");
                        }
                    } else {
                        this.indexToKey.put(Integer.valueOf(i3), obj);
                    }
                }
                i4 = i6;
            }
            i3++;
            c = 0;
        }
        for (Box next3 : track.getSampleDescriptionBox().getSampleEntry().getBoxes()) {
            if (next3 instanceof AvcConfigurationBox) {
                this.configurationBox = next3;
                this.subSampleEncryption = true;
                i2 = ((AvcConfigurationBox) next3).getLengthSizeMinusOne() + 1;
            }
            if (next3 instanceof HevcConfigurationBox) {
                this.configurationBox = next3;
                this.subSampleEncryption = true;
                i2 = ((HevcConfigurationBox) next3).getLengthSizeMinusOne() + 1;
            }
        }
        for (int i8 = 0; i8 < this.samples.size(); i8++) {
            Sample sample = this.samples.get(i8);
            CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat = new CencSampleAuxiliaryDataFormat();
            this.cencSampleAuxiliaryData.add(cencSampleAuxiliaryDataFormat);
            if (this.indexToKey.get(Integer.valueOf(i8)) != null) {
                byte[] byteArray = bigInteger2.toByteArray();
                byte[] bArr2 = new byte[i];
                System.arraycopy(byteArray, byteArray.length - i > 0 ? byteArray.length - i : 0, bArr2, 8 - byteArray.length < 0 ? 0 : 8 - byteArray.length, byteArray.length > i ? 8 : byteArray.length);
                cencSampleAuxiliaryDataFormat.iv = bArr2;
                ByteBuffer byteBuffer = (ByteBuffer) sample.asByteBuffer().rewind();
                if (this.subSampleEncryption) {
                    if (z2) {
                        CencSampleAuxiliaryDataFormat.Pair[] pairArr = new CencSampleAuxiliaryDataFormat.Pair[1];
                        pairArr[c] = cencSampleAuxiliaryDataFormat.createPair(byteBuffer.remaining(), 0);
                        cencSampleAuxiliaryDataFormat.pairs = pairArr;
                    } else {
                        ArrayList arrayList2 = new ArrayList(5);
                        while (byteBuffer.remaining() > 0) {
                            int l2i = CastUtils.l2i(IsoTypeReaderVariable.read(byteBuffer, i2));
                            int i9 = l2i + i2;
                            int i10 = (i9 < 112 || isClearNal(byteBuffer.duplicate())) ? i9 : (i9 % 16) + 96;
                            arrayList2.add(cencSampleAuxiliaryDataFormat.createPair(i10, (long) (i9 - i10)));
                            byteBuffer.position(byteBuffer.position() + l2i);
                        }
                        cencSampleAuxiliaryDataFormat.pairs = (CencSampleAuxiliaryDataFormat.Pair[]) arrayList2.toArray(new CencSampleAuxiliaryDataFormat.Pair[arrayList2.size()]);
                    }
                }
                bigInteger2 = bigInteger2.add(bigInteger);
            }
        }
    }

    public UUID getDefaultKeyId() {
        return this.defaultKeyId;
    }

    public boolean hasSubSampleEncryption() {
        return this.subSampleEncryption;
    }

    public List<CencSampleAuxiliaryDataFormat> getSampleEncryptionEntries() {
        return this.cencSampleAuxiliaryData;
    }

    public synchronized SampleDescriptionBox getSampleDescriptionBox() {
        if (this.stsd == null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                this.source.getSampleDescriptionBox().getBox(Channels.newChannel(byteArrayOutputStream));
                int i = 0;
                this.stsd = (SampleDescriptionBox) new IsoFile((DataSource) new MemoryDataSourceImpl(byteArrayOutputStream.toByteArray())).getBoxes().get(0);
                OriginalFormatBox originalFormatBox = new OriginalFormatBox();
                originalFormatBox.setDataFormat(this.stsd.getSampleEntry().getType());
                if (this.stsd.getSampleEntry() instanceof AudioSampleEntry) {
                    ((AudioSampleEntry) this.stsd.getSampleEntry()).setType(AudioSampleEntry.TYPE_ENCRYPTED);
                } else if (this.stsd.getSampleEntry() instanceof VisualSampleEntry) {
                    ((VisualSampleEntry) this.stsd.getSampleEntry()).setType(VisualSampleEntry.TYPE_ENCRYPTED);
                } else {
                    throw new RuntimeException("I don't know how to cenc " + this.stsd.getSampleEntry().getType());
                }
                ProtectionSchemeInformationBox protectionSchemeInformationBox = new ProtectionSchemeInformationBox();
                protectionSchemeInformationBox.addBox(originalFormatBox);
                SchemeTypeBox schemeTypeBox = new SchemeTypeBox();
                schemeTypeBox.setSchemeType(this.encryptionAlgo);
                schemeTypeBox.setSchemeVersion(65536);
                protectionSchemeInformationBox.addBox(schemeTypeBox);
                SchemeInformationBox schemeInformationBox = new SchemeInformationBox();
                TrackEncryptionBox trackEncryptionBox = new TrackEncryptionBox();
                trackEncryptionBox.setDefaultIvSize(this.defaultKeyId == null ? 0 : 8);
                if (this.defaultKeyId != null) {
                    i = 1;
                }
                trackEncryptionBox.setDefaultAlgorithmId(i);
                trackEncryptionBox.setDefault_KID(this.defaultKeyId == null ? new UUID(0, 0) : this.defaultKeyId);
                schemeInformationBox.addBox(trackEncryptionBox);
                protectionSchemeInformationBox.addBox(schemeInformationBox);
                this.stsd.getSampleEntry().addBox(protectionSchemeInformationBox);
            } catch (IOException unused) {
                throw new RuntimeException("Dumping stsd to memory failed");
            }
        }
        return this.stsd;
    }

    public long[] getSampleDurations() {
        return this.source.getSampleDurations();
    }

    public long getDuration() {
        return this.source.getDuration();
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return this.source.getCompositionTimeEntries();
    }

    public long[] getSyncSamples() {
        return this.source.getSyncSamples();
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.source.getSampleDependencies();
    }

    public TrackMetaData getTrackMetaData() {
        return this.source.getTrackMetaData();
    }

    public String getHandler() {
        return this.source.getHandler();
    }

    public List<Sample> getSamples() {
        return new CencEncryptingSampleList(this.indexToKey, this.source.getSamples(), this.cencSampleAuxiliaryData, this.encryptionAlgo);
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.source.getSubsampleInformationBox();
    }

    public void close() throws IOException {
        this.source.close();
    }

    public String getName() {
        return "enc(" + this.source.getName() + ")";
    }

    public List<Edit> getEdits() {
        return this.source.getEdits();
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.sampleGroups;
    }

    public boolean isClearNal(ByteBuffer byteBuffer) {
        Object obj = this.configurationBox;
        if (obj instanceof HevcConfigurationBox) {
            H265NalUnitHeader nalUnitHeader = H265TrackImpl.getNalUnitHeader(byteBuffer.slice());
            if (nalUnitHeader.nalUnitType >= 0 && nalUnitHeader.nalUnitType <= 9) {
                return false;
            }
            if (nalUnitHeader.nalUnitType >= 16 && nalUnitHeader.nalUnitType <= 21) {
                return false;
            }
            if (nalUnitHeader.nalUnitType < 16 || nalUnitHeader.nalUnitType > 21) {
                return true;
            }
            return false;
        } else if (obj instanceof AvcConfigurationBox) {
            H264NalUnitHeader nalUnitHeader2 = H264TrackImpl.getNalUnitHeader(byteBuffer.slice());
            if (nalUnitHeader2.nal_unit_type == 19 || nalUnitHeader2.nal_unit_type == 2 || nalUnitHeader2.nal_unit_type == 3 || nalUnitHeader2.nal_unit_type == 4 || nalUnitHeader2.nal_unit_type == 20 || nalUnitHeader2.nal_unit_type == 5 || nalUnitHeader2.nal_unit_type == 1) {
                return false;
            }
            return true;
        } else {
            throw new RuntimeException("Subsample encryption is activated but the CencEncryptingTrackImpl can't say if this sample is to be encrypted or not!");
        }
    }
}
