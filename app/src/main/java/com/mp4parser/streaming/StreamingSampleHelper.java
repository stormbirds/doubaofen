package com.mp4parser.streaming;

public class StreamingSampleHelper {
    static boolean hasSampleExtension(StreamingSample streamingSample, Class<? extends SampleExtension> cls) {
        for (SampleExtension sampleExtension : streamingSample.getExtensions()) {
            if (cls.isAssignableFrom(sampleExtension.getClass())) {
                return true;
            }
        }
        return false;
    }

    static <B extends SampleExtension> B getSampleExtension(StreamingSample streamingSample, Class<B> cls) {
        for (B b : streamingSample.getExtensions()) {
            if (cls.isAssignableFrom(b.getClass())) {
                return b;
            }
        }
        return null;
    }
}
