package com.yhao.floatwindow;

import android.view.View;

abstract class FloatView {
    /* access modifiers changed from: package-private */
    public abstract void dismiss();

    /* access modifiers changed from: package-private */
    public int getX() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getY() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public abstract void init();

    /* access modifiers changed from: package-private */
    public abstract void setGravity(int i, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void setSize(int i, int i2);

    /* access modifiers changed from: package-private */
    public abstract void setView(View view);

    /* access modifiers changed from: package-private */
    public void updateX(int i) {
    }

    /* access modifiers changed from: package-private */
    public void updateXY(int i, int i2) {
    }

    /* access modifiers changed from: package-private */
    public void updateY(int i) {
    }

    FloatView() {
    }
}
