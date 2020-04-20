package com.googlecode.mp4parser;

import android.support.v4.os.EnvironmentCompat;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.logging.Logger;

public class Version {
    private static final Logger LOG;
    public static final String VERSION;

    static {
        String str;
        Class<Version> cls = Version.class;
        LOG = Logger.getLogger(cls.getName());
        try {
            str = new LineNumberReader(new InputStreamReader(cls.getResourceAsStream("/version.txt"))).readLine();
        } catch (IOException e) {
            LOG.warning(e.getMessage());
            str = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        VERSION = str;
    }
}
