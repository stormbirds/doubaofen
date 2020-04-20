package com.tiangou.douxiaomi.pickerview;

import android.support.graphics.drawable.PathInterpolatorCompat;
import java.util.Timer;
import java.util.TimerTask;

final class MTimer extends TimerTask {
    int a = Integer.MAX_VALUE;
    int b = 0;
    final int c;
    final LoopView loopView;
    final Timer timer;

    MTimer(LoopView loopView2, int i, Timer timer2) {
        this.loopView = loopView2;
        this.c = i;
        this.timer = timer2;
    }

    public final void run() {
        if (this.a == Integer.MAX_VALUE) {
            int i = this.c;
            if (i < 0) {
                if (((float) (-i)) > (this.loopView.l * ((float) this.loopView.h)) / 2.0f) {
                    this.a = (int) (((-this.loopView.l) * ((float) this.loopView.h)) - ((float) this.c));
                } else {
                    this.a = -this.c;
                }
            } else if (((float) i) > (this.loopView.l * ((float) this.loopView.h)) / 2.0f) {
                this.a = (int) ((this.loopView.l * ((float) this.loopView.h)) - ((float) this.c));
            } else {
                this.a = -this.c;
            }
        }
        int i2 = this.a;
        this.b = (int) (((float) i2) * 0.1f);
        if (this.b == 0) {
            if (i2 < 0) {
                this.b = -1;
            } else {
                this.b = 1;
            }
        }
        if (Math.abs(this.a) <= 0) {
            this.timer.cancel();
            this.loopView.handler.sendEmptyMessage(PathInterpolatorCompat.MAX_NUM_POINTS);
            return;
        }
        this.loopView.totalScrollY += this.b;
        this.loopView.handler.sendEmptyMessage(1000);
        this.a -= this.b;
    }
}
