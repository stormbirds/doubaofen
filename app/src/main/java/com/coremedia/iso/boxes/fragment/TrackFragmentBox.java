package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.boxes.Box;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;

public class TrackFragmentBox extends AbstractContainerBox {
    public static final String TYPE = "traf";

    public TrackFragmentBox() {
        super(TYPE);
    }

    @DoNotParseDetail
    public TrackFragmentHeaderBox getTrackFragmentHeaderBox() {
        for (Box next : getBoxes()) {
            if (next instanceof TrackFragmentHeaderBox) {
                return (TrackFragmentHeaderBox) next;
            }
        }
        return null;
    }
}
