package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractContainerBox;
import java.util.List;

public class TrackBox extends AbstractContainerBox {
    public static final String TYPE = "trak";
    private SampleTableBox sampleTableBox;

    public TrackBox() {
        super(TYPE);
    }

    public TrackHeaderBox getTrackHeaderBox() {
        for (Box next : getBoxes()) {
            if (next instanceof TrackHeaderBox) {
                return (TrackHeaderBox) next;
            }
        }
        return null;
    }

    public SampleTableBox getSampleTableBox() {
        MediaInformationBox mediaInformationBox;
        SampleTableBox sampleTableBox2 = this.sampleTableBox;
        if (sampleTableBox2 != null) {
            return sampleTableBox2;
        }
        MediaBox mediaBox = getMediaBox();
        if (mediaBox == null || (mediaInformationBox = mediaBox.getMediaInformationBox()) == null) {
            return null;
        }
        this.sampleTableBox = mediaInformationBox.getSampleTableBox();
        return this.sampleTableBox;
    }

    public MediaBox getMediaBox() {
        for (Box next : getBoxes()) {
            if (next instanceof MediaBox) {
                return (MediaBox) next;
            }
        }
        return null;
    }

    public void setBoxes(List<Box> list) {
        super.setBoxes(list);
        this.sampleTableBox = null;
    }
}
