package com.googlecode.mp4parser.authoring.tracks.ttml;

import android.support.v4.internal.view.SupportMenu;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.mp4parser.iso14496.part30.XMLSubtitleSampleEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import kotlin.jvm.internal.LongCompanionObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TtmlTrackImpl extends AbstractTrack {
    SampleDescriptionBox sampleDescriptionBox = new SampleDescriptionBox();
    private long[] sampleDurations;
    List<Sample> samples = new ArrayList();
    SubSampleInformationBox subSampleInformationBox = new SubSampleInformationBox();
    TrackMetaData trackMetaData = new TrackMetaData();
    XMLSubtitleSampleEntry xmlSubtitleSampleEntry = new XMLSubtitleSampleEntry();

    public void close() throws IOException {
    }

    public String getHandler() {
        return "subt";
    }

    public TtmlTrackImpl(String str, List<Document> list) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, URISyntaxException {
        super(str);
        extractLanguage(list);
        HashSet hashSet = new HashSet();
        this.sampleDurations = new long[list.size()];
        XPathFactory.newInstance().newXPath().setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        for (int i = 0; i < list.size(); i++) {
            Document document = list.get(i);
            SubSampleInformationBox.SubSampleEntry subSampleEntry = new SubSampleInformationBox.SubSampleEntry();
            this.subSampleInformationBox.getEntries().add(subSampleEntry);
            subSampleEntry.setSampleDelta(1);
            this.sampleDurations[i] = extractDuration(document);
            List<byte[]> extractImages = extractImages(document);
            hashSet.addAll(extractMimeTypes(document));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            TtmlHelpers.pretty(document, byteArrayOutputStream, 4);
            SubSampleInformationBox.SubSampleEntry.SubsampleEntry subsampleEntry = new SubSampleInformationBox.SubSampleEntry.SubsampleEntry();
            subsampleEntry.setSubsampleSize((long) byteArrayOutputStream.size());
            subSampleEntry.getSubsampleEntries().add(subsampleEntry);
            for (byte[] next : extractImages) {
                byteArrayOutputStream.write(next);
                SubSampleInformationBox.SubSampleEntry.SubsampleEntry subsampleEntry2 = new SubSampleInformationBox.SubSampleEntry.SubsampleEntry();
                subsampleEntry2.setSubsampleSize((long) next.length);
                subSampleEntry.getSubsampleEntries().add(subsampleEntry2);
            }
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            this.samples.add(new Sample() {
                public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
                    writableByteChannel.write(ByteBuffer.wrap(byteArray));
                }

                public long getSize() {
                    return (long) byteArray.length;
                }

                public ByteBuffer asByteBuffer() {
                    return ByteBuffer.wrap(byteArray);
                }
            });
        }
        this.xmlSubtitleSampleEntry.setNamespace(join(",", TtmlHelpers.getAllNamespaces(list.get(0))));
        this.xmlSubtitleSampleEntry.setSchemaLocation("");
        this.xmlSubtitleSampleEntry.setAuxiliaryMimeTypes(join(",", (String[]) new ArrayList(hashSet).toArray(new String[hashSet.size()])));
        this.sampleDescriptionBox.addBox(this.xmlSubtitleSampleEntry);
        this.trackMetaData.setTimescale(30000);
        this.trackMetaData.setLayer(SupportMenu.USER_MASK);
    }

    public static String getLanguage(Document document) {
        return document.getDocumentElement().getAttribute("xml:lang");
    }

    protected static List<byte[]> extractImages(Document document) throws XPathExpressionException, URISyntaxException, IOException {
        NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@backgroundImage").evaluate(document, XPathConstants.NODESET);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int i = 1;
        for (int i2 = 0; i2 < nodeList.getLength(); i2++) {
            Node item = nodeList.item(i2);
            String nodeValue = item.getNodeValue();
            String substring = nodeValue.substring(nodeValue.lastIndexOf("."));
            String str = (String) linkedHashMap.get(nodeValue);
            if (str == null) {
                str = "urn:mp4parser:" + i + substring;
                linkedHashMap.put(str, nodeValue);
                i++;
            }
            item.setNodeValue(str);
        }
        ArrayList arrayList = new ArrayList();
        if (!linkedHashMap.isEmpty()) {
            for (Map.Entry value : linkedHashMap.entrySet()) {
                arrayList.add(streamToByteArray(new URI(document.getDocumentURI()).resolve((String) value.getValue()).toURL().openStream()));
            }
        }
        return arrayList;
    }

    private static String join(String str, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String append : strArr) {
            sb.append(append);
            sb.append(str);
        }
        if (sb.length() > 0) {
            i = sb.length() - 1;
        }
        sb.setLength(i);
        return sb.toString();
    }

    private static long latestTimestamp(Document document) {
        XPath newXPath = XPathFactory.newInstance().newXPath();
        newXPath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        try {
            NodeList nodeList = (NodeList) newXPath.compile("//*[name()='p']").evaluate(document, XPathConstants.NODESET);
            long j = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                j = Math.max(TtmlHelpers.getEndTime(nodeList.item(i)), j);
            }
            return j;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] streamToByteArray(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[8096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    /* access modifiers changed from: protected */
    public long firstTimestamp(Document document) {
        XPath newXPath = XPathFactory.newInstance().newXPath();
        newXPath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        try {
            NodeList nodeList = (NodeList) newXPath.compile("//*[@begin]").evaluate(document, XPathConstants.NODESET);
            long j = LongCompanionObject.MAX_VALUE;
            for (int i = 0; i < nodeList.getLength(); i++) {
                j = Math.min(TtmlHelpers.getStartTime(nodeList.item(i)), j);
            }
            return j;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public long lastTimestamp(Document document) {
        XPath newXPath = XPathFactory.newInstance().newXPath();
        newXPath.setNamespaceContext(TtmlHelpers.NAMESPACE_CONTEXT);
        try {
            NodeList nodeList = (NodeList) newXPath.compile("//*[@end]").evaluate(document, XPathConstants.NODESET);
            long j = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                j = Math.max(TtmlHelpers.getEndTime(nodeList.item(i)), j);
            }
            return j;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void extractLanguage(List<Document> list) {
        String str = null;
        for (Document language : list) {
            String language2 = getLanguage(language);
            if (str == null) {
                this.trackMetaData.setLanguage(Locale.forLanguageTag(language2).getISO3Language());
                str = language2;
            } else if (!str.equals(language2)) {
                throw new RuntimeException("Within one Track all sample documents need to have the same language");
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<String> extractMimeTypes(Document document) throws XPathExpressionException {
        NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@smpte:backgroundImage").evaluate(document, XPathConstants.NODESET);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String nodeValue = nodeList.item(i).getNodeValue();
            String substring = nodeValue.substring(nodeValue.lastIndexOf("."));
            if (substring.contains("jpg") || substring.contains("jpeg")) {
                linkedHashSet.add("image/jpeg");
            } else if (substring.contains("png")) {
                linkedHashSet.add("image/png");
            }
        }
        return new ArrayList(linkedHashSet);
    }

    /* access modifiers changed from: package-private */
    public long extractDuration(Document document) {
        return lastTimestamp(document) - firstTimestamp(document);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    public long[] getSampleDurations() {
        long[] jArr = new long[this.sampleDurations.length];
        for (int i = 0; i < jArr.length; i++) {
            jArr[i] = (this.sampleDurations[i] * this.trackMetaData.getTimescale()) / 1000;
        }
        return jArr;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.subSampleInformationBox;
    }
}
