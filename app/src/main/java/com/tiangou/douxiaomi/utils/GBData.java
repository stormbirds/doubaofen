package com.tiangou.douxiaomi.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import java.nio.ByteBuffer;

public class GBData {
    private static final String TAG = "GBData";
    private static Bitmap bitmap;
    public static ImageReader reader;

    public static String getColorStr(int i, int i2) {
        if (reader == null) {
            return null;
        }
        int color = getColor(i, i2);
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        String hexString = Integer.toHexString(alpha);
        if (hexString.length() < 2) {
            "0" + hexString;
        }
        String hexString2 = Integer.toHexString(red);
        if (hexString2.length() < 2) {
            hexString2 = "0" + hexString2;
        }
        String hexString3 = Integer.toHexString(green);
        if (hexString3.length() < 2) {
            hexString3 = "0" + hexString3;
        }
        String hexString4 = Integer.toHexString(blue);
        if (hexString4.length() < 2) {
            hexString4 = "0" + hexString4;
        }
        return hexString2 + hexString3 + hexString4;
    }

    public static boolean ColorComp(int i, int i2) {
        int red = Color.red(i) - Color.red(i2);
        int green = Color.green(i) - Color.green(i2);
        int blue = Color.blue(i) - Color.blue(i2);
        double sqrt = Math.sqrt((double) ((red * red) + (green * green) + (blue * blue)));
        Math.abs(red);
        Math.abs(green);
        Math.abs(blue);
        return sqrt < 70.0d;
    }

    public static Boolean isSame(String str, String str2) {
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            try {
                if (str.charAt(i) == str2.charAt(i)) {
                    i2++;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (i2 >= 4) {
            return true;
        }
        return false;
    }

    public static int getColor(int i, int i2) {
        ImageReader imageReader = reader;
        if (imageReader == null) {
            Log.w(TAG, "getColor: reader is null");
            return -1;
        }
        Image acquireLatestImage = imageReader.acquireLatestImage();
        if (acquireLatestImage == null) {
            Bitmap bitmap2 = bitmap;
            if (bitmap2 != null) {
                return bitmap2.getPixel(i, i2);
            }
            Log.w(TAG, "getColor: image is null");
            return -1;
        }
        int width = acquireLatestImage.getWidth();
        int height = acquireLatestImage.getHeight();
        Image.Plane[] planes = acquireLatestImage.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride() - (pixelStride * width);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width + (rowStride / pixelStride), height, Bitmap.Config.ARGB_8888);
        }
        bitmap.copyPixelsFromBuffer(buffer);
        acquireLatestImage.close();
        return bitmap.getPixel(i, i2);
    }
}
