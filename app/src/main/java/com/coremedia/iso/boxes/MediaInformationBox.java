package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractContainerBox;

public class MediaInformationBox extends AbstractContainerBox {
    public static final String TYPE = "minf";

    public MediaInformationBox() {
        super(TYPE);
    }

    public SampleTableBox getSampleTableBox() {
        for (Box next : getBoxes()) {
            if (next instanceof SampleTableBox) {
                return (SampleTableBox) next;
            }
        }
        return null;
    }

    public AbstractMediaHeaderBox getMediaHeaderBox() {
        for (Box next : getBoxes()) {
            if (next instanceof AbstractMediaHeaderBox) {
                return (AbstractMediaHeaderBox) next;
            }
        }
        return null;
    }
}
