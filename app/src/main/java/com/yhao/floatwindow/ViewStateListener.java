package com.yhao.floatwindow;

public interface ViewStateListener {
    void onBackToDesktop();

    void onDismiss();

    void onHide();

    void onMoveAnimEnd();

    void onMoveAnimStart();

    void onPositionUpdate(int i, int i2);

    void onShow();
}
