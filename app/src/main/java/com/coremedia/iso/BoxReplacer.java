package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.util.Path;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class BoxReplacer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static void replace(Map<String, Box> map, File file) throws IOException {
        IsoFile isoFile = new IsoFile((DataSource) new FileDataSourceImpl(new RandomAccessFile(file, "r").getChannel()));
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            Box path = Path.getPath((Container) isoFile, (String) next.getKey());
            hashMap.put(Path.createPath(path), (Box) next.getValue());
            hashMap2.put(Path.createPath(path), Long.valueOf(path.getOffset()));
        }
        isoFile.close();
        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
        for (String str : hashMap.keySet()) {
            channel.position(((Long) hashMap2.get(str)).longValue());
            ((Box) hashMap.get(str)).getBox(channel);
        }
        channel.close();
    }
}
