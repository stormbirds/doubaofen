package com.coremedia.iso.boxes;

import com.googlecode.mp4parser.AbstractContainerBox;

public class SampleTableBox extends AbstractContainerBox {
    public static final String TYPE = "stbl";
    private SampleToChunkBox sampleToChunkBox;

    public SampleTableBox() {
        super(TYPE);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        for (Box next : getBoxes()) {
            if (next instanceof SampleDescriptionBox) {
                return (SampleDescriptionBox) next;
            }
        }
        return null;
    }

    public SampleSizeBox getSampleSizeBox() {
        for (Box next : getBoxes()) {
            if (next instanceof SampleSizeBox) {
                return (SampleSizeBox) next;
            }
        }
        return null;
    }

    public SampleToChunkBox getSampleToChunkBox() {
        SampleToChunkBox sampleToChunkBox2 = this.sampleToChunkBox;
        if (sampleToChunkBox2 != null) {
            return sampleToChunkBox2;
        }
        for (Box next : getBoxes()) {
            if (next instanceof SampleToChunkBox) {
                this.sampleToChunkBox = (SampleToChunkBox) next;
                return this.sampleToChunkBox;
            }
        }
        return null;
    }

    public ChunkOffsetBox getChunkOffsetBox() {
        for (Box next : getBoxes()) {
            if (next instanceof ChunkOffsetBox) {
                return (ChunkOffsetBox) next;
            }
        }
        return null;
    }

    public TimeToSampleBox getTimeToSampleBox() {
        for (Box next : getBoxes()) {
            if (next instanceof TimeToSampleBox) {
                return (TimeToSampleBox) next;
            }
        }
        return null;
    }

    public SyncSampleBox getSyncSampleBox() {
        for (Box next : getBoxes()) {
            if (next instanceof SyncSampleBox) {
                return (SyncSampleBox) next;
            }
        }
        return null;
    }

    public CompositionTimeToSample getCompositionTimeToSample() {
        for (Box next : getBoxes()) {
            if (next instanceof CompositionTimeToSample) {
                return (CompositionTimeToSample) next;
            }
        }
        return null;
    }

    public SampleDependencyTypeBox getSampleDependencyTypeBox() {
        for (Box next : getBoxes()) {
            if (next instanceof SampleDependencyTypeBox) {
                return (SampleDependencyTypeBox) next;
            }
        }
        return null;
    }
}
