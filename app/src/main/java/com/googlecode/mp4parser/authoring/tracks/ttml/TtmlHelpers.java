package com.googlecode.mp4parser.authoring.tracks.ttml;

import com.bumptech.glide.load.Key;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TtmlHelpers {
    public static final NamespaceContext NAMESPACE_CONTEXT = new TextTrackNamespaceContext((TextTrackNamespaceContext) null);
    public static final String SMPTE_TT_NAMESPACE = "http://www.smpte-ra.org/schemas/2052-1/2010/smpte-tt";
    public static final String TTML_NAMESPACE = "http://www.w3.org/ns/ttml";
    static byte[] namespacesStyleSheet1 = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n    <xsl:output method=\"text\"/>\n    <xsl:key name=\"kElemByNSURI\"\n             match=\"*[namespace::*[not(. = ../../namespace::*)]]\"\n              use=\"namespace::*[not(. = ../../namespace::*)]\"/>\n    <xsl:template match=\"/\">\n        <xsl:for-each select=\n            \"//namespace::*[not(. = ../../namespace::*)]\n                           [count(..|key('kElemByNSURI',.)[1])=1]\">\n            <xsl:value-of select=\"concat(.,'&#xA;')\"/>\n        </xsl:for-each>\n    </xsl:template>\n</xsl:stylesheet>".getBytes();

    public static void main(String[] strArr) throws URISyntaxException, ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setNamespaceAware(true);
        TtmlTrackImpl ttmlTrackImpl = new TtmlTrackImpl("a.xml", TtmlSegmenter.split(newInstance.newDocumentBuilder().parse("C:\\dev\\mp4parser\\a.xml"), 60));
        Movie movie = new Movie();
        movie.addTrack(ttmlTrackImpl);
        new DefaultMp4Builder().build(movie).writeContainer(new FileOutputStream("output.mp4").getChannel());
    }

    public static String[] getAllNamespaces(Document document) {
        try {
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new ByteArrayInputStream(namespacesStyleSheet1)));
            StringWriter stringWriter = new StringWriter();
            newTransformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            ArrayList arrayList = new ArrayList(new LinkedHashSet(Arrays.asList(stringWriter.getBuffer().toString().split("\n"))));
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static String toTimeExpression(long j) {
        return toTimeExpression(j, -1);
    }

    public static String toTimeExpression(long j, int i) {
        String str = j >= 0 ? "" : "-";
        long abs = Math.abs(j);
        long j2 = ((abs / 1000) / 60) / 60;
        long j3 = abs - (((j2 * 1000) * 60) * 60);
        long j4 = (j3 / 1000) / 60;
        long j5 = j3 - ((j4 * 1000) * 60);
        long j6 = j5 / 1000;
        long j7 = j5 - (1000 * j6);
        if (i >= 0) {
            return String.format("%s%02d:%02d:%02d:%d", new Object[]{str, Long.valueOf(j2), Long.valueOf(j4), Long.valueOf(j6), Integer.valueOf(i)});
        }
        return String.format("%s%02d:%02d:%02d.%03d", new Object[]{str, Long.valueOf(j2), Long.valueOf(j4), Long.valueOf(j6), Long.valueOf(j7)});
    }

    public static long toTime(String str) {
        long j;
        Matcher matcher = Pattern.compile("(-?)([0-9][0-9]):([0-9][0-9]):([0-9][0-9])([\\.:][0-9][0-9]?[0-9]?)?").matcher(str);
        if (matcher.matches()) {
            int i = 1;
            String group = matcher.group(1);
            String group2 = matcher.group(2);
            String group3 = matcher.group(3);
            String group4 = matcher.group(4);
            String group5 = matcher.group(5);
            if (group5 == null) {
                group5 = ".000";
            }
            String replace = group5.replace(":", ".");
            long parseLong = (Long.parseLong(group2) * 60 * 60 * 1000) + (Long.parseLong(group3) * 60 * 1000) + (Long.parseLong(group4) * 1000);
            if (replace.contains(":")) {
                j = (long) (((double) parseLong) + (Double.parseDouble("0" + replace.replace(":", ".")) * 40.0d * 1000.0d));
            } else {
                j = (long) (((double) parseLong) + (Double.parseDouble("0" + replace) * 1000.0d));
            }
            if ("-".equals(group)) {
                i = -1;
            }
            return j * ((long) i);
        }
        throw new RuntimeException("Cannot match '" + str + "' to time expression");
    }

    private static class TextTrackNamespaceContext implements NamespaceContext {
        private TextTrackNamespaceContext() {
        }

        /* synthetic */ TextTrackNamespaceContext(TextTrackNamespaceContext textTrackNamespaceContext) {
            this();
        }

        public String getNamespaceURI(String str) {
            if (str.equals("ttml")) {
                return TtmlHelpers.TTML_NAMESPACE;
            }
            if (str.equals("smpte")) {
                return TtmlHelpers.SMPTE_TT_NAMESPACE;
            }
            return null;
        }

        public Iterator getPrefixes(String str) {
            return Arrays.asList(new String[]{"ttml", "smpte"}).iterator();
        }

        public String getPrefix(String str) {
            if (str.equals(TtmlHelpers.TTML_NAMESPACE)) {
                return "ttml";
            }
            if (str.equals(TtmlHelpers.SMPTE_TT_NAMESPACE)) {
                return "smpte";
            }
            return null;
        }
    }

    public static void pretty(Document document, OutputStream outputStream, int i) throws IOException {
        try {
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("encoding", Key.STRING_CHARSET_NAME);
            if (i > 0) {
                newTransformer.setOutputProperty("indent", "yes");
                newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(i));
            }
            try {
                newTransformer.transform(new DOMSource(document), new StreamResult(outputStream));
            } catch (TransformerException e) {
                throw new IOException(e);
            }
        } catch (TransformerConfigurationException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static long getStartTime(Node node) {
        long j = 0;
        Node node2 = node;
        while (true) {
            node2 = node2.getParentNode();
            if (node2 == null) {
                break;
            } else if (!(node2.getAttributes() == null || node2.getAttributes().getNamedItem("begin") == null)) {
                j += toTime(node2.getAttributes().getNamedItem("begin").getNodeValue());
            }
        }
        return (node.getAttributes() == null || node.getAttributes().getNamedItem("begin") == null) ? j : j + toTime(node.getAttributes().getNamedItem("begin").getNodeValue());
    }

    public static long getEndTime(Node node) {
        long j = 0;
        Node node2 = node;
        while (true) {
            node2 = node2.getParentNode();
            if (node2 == null) {
                break;
            } else if (!(node2.getAttributes() == null || node2.getAttributes().getNamedItem("begin") == null)) {
                j += toTime(node2.getAttributes().getNamedItem("begin").getNodeValue());
            }
        }
        return (node.getAttributes() == null || node.getAttributes().getNamedItem("end") == null) ? j : j + toTime(node.getAttributes().getNamedItem("end").getNodeValue());
    }

    public static void deepCopyDocument(Document document, File file) throws IOException {
        try {
            NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@backgroundImage").evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                URI create = URI.create(nodeList.item(i).getNodeValue());
                if (!create.isAbsolute()) {
                    copyLarge(new URI(document.getDocumentURI()).resolve(create).toURL().openStream(), new File(file.toURI().resolve(create).toURL().getFile()));
                }
            }
            copyLarge(new URI(document.getDocumentURI()).toURL().openStream(), file);
        } catch (XPathExpressionException e) {
            throw new IOException(e);
        } catch (URISyntaxException e2) {
            throw new IOException(e2);
        }
    }

    private static long copyLarge(InputStream inputStream, File file) throws IOException {
        byte[] bArr = new byte[16384];
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        long j = 0;
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (-1 == read) {
                    return j;
                }
                fileOutputStream.write(bArr, 0, read);
                j += (long) read;
            } finally {
                fileOutputStream.close();
            }
        }
    }
}
