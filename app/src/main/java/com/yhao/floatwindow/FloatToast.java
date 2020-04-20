package com.yhao.floatwindow;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class FloatToast extends FloatView {
    private Method hide;
    private int mHeight;
    private Object mTN;
    private int mWidth;
    private Method show;
    private Toast toast;

    FloatToast(Context context) {
        this.toast = new Toast(context);
    }

    public void setSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public void setView(View view) {
        this.toast.setView(view);
        initTN();
    }

    public void setGravity(int i, int i2, int i3) {
        this.toast.setGravity(i, i2, i3);
    }

    public void init() {
        try {
            this.show.invoke(this.mTN, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            this.hide.invoke(this.mTN, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTN() {
        try {
            Field declaredField = this.toast.getClass().getDeclaredField("mTN");
            declaredField.setAccessible(true);
            this.mTN = declaredField.get(this.toast);
            this.show = this.mTN.getClass().getMethod("show", new Class[0]);
            this.hide = this.mTN.getClass().getMethod("hide", new Class[0]);
            Field declaredField2 = this.mTN.getClass().getDeclaredField("mParams");
            declaredField2.setAccessible(true);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) declaredField2.get(this.mTN);
            layoutParams.flags = 40;
            layoutParams.width = this.mWidth;
            layoutParams.height = this.mHeight;
            layoutParams.windowAnimations = 0;
            Field declaredField3 = this.mTN.getClass().getDeclaredField("mNextView");
            declaredField3.setAccessible(true);
            declaredField3.set(this.mTN, this.toast.getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
