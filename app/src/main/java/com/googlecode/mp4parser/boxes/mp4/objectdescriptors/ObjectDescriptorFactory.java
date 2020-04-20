package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectDescriptorFactory {
    protected static Map<Integer, Map<Integer, Class<? extends BaseDescriptor>>> descriptorRegistry = new HashMap();
    protected static Logger log = Logger.getLogger(ObjectDescriptorFactory.class.getName());

    static {
        HashSet<Class> hashSet = new HashSet<>();
        hashSet.add(DecoderSpecificInfo.class);
        hashSet.add(SLConfigDescriptor.class);
        hashSet.add(BaseDescriptor.class);
        hashSet.add(ExtensionDescriptor.class);
        hashSet.add(ObjectDescriptorBase.class);
        hashSet.add(ProfileLevelIndicationDescriptor.class);
        hashSet.add(AudioSpecificConfig.class);
        hashSet.add(ExtensionProfileLevelDescriptor.class);
        hashSet.add(ESDescriptor.class);
        hashSet.add(DecoderConfigDescriptor.class);
        for (Class cls : hashSet) {
            Descriptor descriptor = (Descriptor) cls.getAnnotation(Descriptor.class);
            int[] tags = descriptor.tags();
            int objectTypeIndication = descriptor.objectTypeIndication();
            Map map = descriptorRegistry.get(Integer.valueOf(objectTypeIndication));
            if (map == null) {
                map = new HashMap();
            }
            for (int valueOf : tags) {
                map.put(Integer.valueOf(valueOf), cls);
            }
            descriptorRegistry.put(Integer.valueOf(objectTypeIndication), map);
        }
    }

    public static BaseDescriptor createFrom(int i, ByteBuffer byteBuffer) throws IOException {
        BaseDescriptor baseDescriptor;
        int readUInt8 = IsoTypeReader.readUInt8(byteBuffer);
        Map map = descriptorRegistry.get(Integer.valueOf(i));
        if (map == null) {
            map = descriptorRegistry.get(-1);
        }
        Class cls = (Class) map.get(Integer.valueOf(readUInt8));
        if (cls == null || cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
            Logger logger = log;
            logger.warning("No ObjectDescriptor found for objectTypeIndication " + Integer.toHexString(i) + " and tag " + Integer.toHexString(readUInt8) + " found: " + cls);
            baseDescriptor = new UnknownDescriptor();
        } else {
            try {
                baseDescriptor = (BaseDescriptor) cls.newInstance();
            } catch (Exception e) {
                Logger logger2 = log;
                Level level = Level.SEVERE;
                logger2.log(level, "Couldn't instantiate BaseDescriptor class " + cls + " for objectTypeIndication " + i + " and tag " + readUInt8, e);
                throw new RuntimeException(e);
            }
        }
        baseDescriptor.parse(readUInt8, byteBuffer);
        return baseDescriptor;
    }
}
