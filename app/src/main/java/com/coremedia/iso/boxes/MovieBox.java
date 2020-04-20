package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractContainerBox;
import java.util.List;

public class MovieBox extends AbstractContainerBox {
    public static final String TYPE = "moov";

    public MovieBox() {
        super(TYPE);
    }

    public int getTrackCount() {
        return getBoxes(TrackBox.class).size();
    }

    public long[] getTrackNumbers() {
        List<TrackBox> boxes = getBoxes(TrackBox.class);
        long[] jArr = new long[boxes.size()];
        for (int i = 0; i < boxes.size(); i++) {
            jArr[i] = boxes.get(i).getTrackHeaderBox().getTrackId();
        }
        return jArr;
    }

    public MovieHeaderBox getMovieHeaderBox() {
        for (Box next : getBoxes()) {
            if (next instanceof MovieHeaderBox) {
                return (MovieHeaderBox) next;
            }
        }
        return null;
    }
}
