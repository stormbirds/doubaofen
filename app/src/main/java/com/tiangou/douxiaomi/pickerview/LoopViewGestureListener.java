package com.tiangou.douxiaomi.pickerview;

import android.view.GestureDetector;
import android.view.MotionEvent;

final class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
    final LoopView loopView;

    LoopViewGestureListener(LoopView loopView2) {
        this.loopView = loopView2;
    }

    public final boolean onDown(MotionEvent motionEvent) {
        if (this.loopView.mTimer == null) {
            return true;
        }
        this.loopView.mTimer.cancel();
        return true;
    }

    public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.loopView.b(f2);
        return true;
    }
}
