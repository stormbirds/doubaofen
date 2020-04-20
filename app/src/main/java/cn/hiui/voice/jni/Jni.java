package cn.hiui.voice.jni;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Jni {
    public static String getSbObj(int i) {
        try {
            Class<?> cls = Class.forName("helper.zhouxiaodong.wechat.jni.Jni");
            return (String) cls.getDeclaredMethod("getSbObj", new Class[]{Integer.TYPE}).invoke(cls, new Object[]{Integer.valueOf(i)});
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            Log.v("xx", stringWriter.toString());
            return "";
        }
    }
}
