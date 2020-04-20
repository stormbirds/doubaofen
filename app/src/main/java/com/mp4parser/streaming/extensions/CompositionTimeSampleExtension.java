package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.SampleExtension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CompositionTimeSampleExtension implements SampleExtension {
    public static Map<Integer, CompositionTimeSampleExtension> pool = Collections.synchronizedMap(new HashMap());
    private int ctts;

    public static CompositionTimeSampleExtension create(int i) {
        CompositionTimeSampleExtension compositionTimeSampleExtension = pool.get(Integer.valueOf(i));
        if (compositionTimeSampleExtension != null) {
            return compositionTimeSampleExtension;
        }
        CompositionTimeSampleExtension compositionTimeSampleExtension2 = new CompositionTimeSampleExtension();
        compositionTimeSampleExtension2.ctts = i;
        pool.put(Integer.valueOf(i), compositionTimeSampleExtension2);
        return compositionTimeSampleExtension2;
    }

    public int getCompositionTimeOffset() {
        return this.ctts;
    }
}
