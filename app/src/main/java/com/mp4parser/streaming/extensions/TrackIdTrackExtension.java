package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.TrackExtension;

public class TrackIdTrackExtension implements TrackExtension {
    private long trackId = 1;

    public TrackIdTrackExtension(long j) {
        this.trackId = j;
    }

    public long getTrackId() {
        return this.trackId;
    }
}
