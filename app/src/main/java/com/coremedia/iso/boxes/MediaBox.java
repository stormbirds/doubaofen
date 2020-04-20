package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractContainerBox;

public class MediaBox extends AbstractContainerBox {
    public static final String TYPE = "mdia";

    public MediaBox() {
        super(TYPE);
    }

    public MediaInformationBox getMediaInformationBox() {
        for (Box next : getBoxes()) {
            if (next instanceof MediaInformationBox) {
                return (MediaInformationBox) next;
            }
        }
        return null;
    }

    public MediaHeaderBox getMediaHeaderBox() {
        for (Box next : getBoxes()) {
            if (next instanceof MediaHeaderBox) {
                return (MediaHeaderBox) next;
            }
        }
        return null;
    }

    public HandlerBox getHandlerBox() {
        for (Box next : getBoxes()) {
            if (next instanceof HandlerBox) {
                return (HandlerBox) next;
            }
        }
        return null;
    }
}
