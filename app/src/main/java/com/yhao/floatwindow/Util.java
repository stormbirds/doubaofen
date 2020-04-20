package com.yhao.floatwindow;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

class Util {
    private static Point sPoint;

    Util() {
    }

    static View inflate(Context context, int i) {
        return ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i, (ViewGroup) null);
    }

    static int getScreenWidth(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getSize(sPoint);
        }
        return sPoint.x;
    }

    static int getScreenHeight(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getSize(sPoint);
        }
        return sPoint.y;
    }

    static boolean isViewVisible(View view) {
        return view.getGlobalVisibleRect(new Rect());
    }
}
