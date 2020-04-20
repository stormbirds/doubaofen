package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.TrackExtension;

public class NameTrackExtension implements TrackExtension {
    private String name;

    public static NameTrackExtension create(String str) {
        NameTrackExtension nameTrackExtension = new NameTrackExtension();
        nameTrackExtension.name = str;
        return nameTrackExtension;
    }

    public String getName() {
        return this.name;
    }
}
