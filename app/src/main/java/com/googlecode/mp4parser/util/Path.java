package com.googlecode.mp4parser.util;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.AbstractContainerBox;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static Pattern component = Pattern.compile("(....|\\.\\.)(\\[(.*)\\])?");

    private Path() {
    }

    public static String createPath(Box box) {
        return createPath(box, "");
    }

    private static String createPath(Box box, String str) {
        Container parent = box.getParent();
        int i = 0;
        for (Box next : parent.getBoxes()) {
            if (next.getType().equals(box.getType())) {
                if (next == box) {
                    break;
                }
                i++;
            }
        }
        String str2 = String.valueOf(String.format("/%s[%d]", new Object[]{box.getType(), Integer.valueOf(i)})) + str;
        return parent instanceof Box ? createPath((Box) parent, str2) : str2;
    }

    public static <T extends Box> T getPath(Box box, String str) {
        List paths = getPaths(box, str, true);
        if (paths.isEmpty()) {
            return null;
        }
        return (Box) paths.get(0);
    }

    public static <T extends Box> T getPath(Container container, String str) {
        List paths = getPaths(container, str, true);
        if (paths.isEmpty()) {
            return null;
        }
        return (Box) paths.get(0);
    }

    public static <T extends Box> T getPath(AbstractContainerBox abstractContainerBox, String str) {
        List paths = getPaths(abstractContainerBox, str, true);
        if (paths.isEmpty()) {
            return null;
        }
        return (Box) paths.get(0);
    }

    public static <T extends Box> List<T> getPaths(Box box, String str) {
        return getPaths(box, str, false);
    }

    public static <T extends Box> List<T> getPaths(Container container, String str) {
        return getPaths(container, str, false);
    }

    private static <T extends Box> List<T> getPaths(AbstractContainerBox abstractContainerBox, String str, boolean z) {
        return getPaths((Object) abstractContainerBox, str, z);
    }

    private static <T extends Box> List<T> getPaths(Container container, String str, boolean z) {
        return getPaths((Object) container, str, z);
    }

    private static <T extends Box> List<T> getPaths(Box box, String str, boolean z) {
        return getPaths((Object) box, str, z);
    }

    private static <T extends Box> List<T> getPaths(Object obj, String str, boolean z) {
        String str2;
        if (str.startsWith("/")) {
            String substring = str.substring(1);
            while (obj instanceof Box) {
                obj = ((Box) obj).getParent();
            }
            str = substring;
        }
        if (str.length() != 0) {
            int i = 0;
            if (str.contains("/")) {
                str2 = str.substring(str.indexOf(47) + 1);
                str = str.substring(0, str.indexOf(47));
            } else {
                str2 = "";
            }
            Matcher matcher = component.matcher(str);
            if (matcher.matches()) {
                String group = matcher.group(1);
                if ("..".equals(group)) {
                    if (obj instanceof Box) {
                        return getPaths(((Box) obj).getParent(), str2, z);
                    }
                    return Collections.emptyList();
                } else if (!(obj instanceof Container)) {
                    return Collections.emptyList();
                } else {
                    int parseInt = matcher.group(2) != null ? Integer.parseInt(matcher.group(3)) : -1;
                    LinkedList linkedList = new LinkedList();
                    for (Box next : ((Container) obj).getBoxes()) {
                        if (next.getType().matches(group)) {
                            if (parseInt == -1 || parseInt == i) {
                                linkedList.addAll(getPaths(next, str2, z));
                            }
                            i++;
                        }
                        if ((z || parseInt >= 0) && !linkedList.isEmpty()) {
                            return linkedList;
                        }
                    }
                    return linkedList;
                }
            } else {
                throw new RuntimeException(String.valueOf(str) + " is invalid path.");
            }
        } else if (obj instanceof Box) {
            return Collections.singletonList((Box) obj);
        } else {
            throw new RuntimeException("Result of path expression seems to be the root container. This is not allowed!");
        }
    }

    public static boolean isContained(Box box, String str) {
        return getPaths(box, str).contains(box);
    }
}
