package com.tiangou.douxiaomi.pickerview;

import java.util.Timer;
import java.util.TimerTask;

final class LoopTimerTask extends TimerTask {
    float a = 2.1474839E9f;
    final float b;
    final LoopView loopView;
    final Timer timer;

    LoopTimerTask(LoopView loopView2, float f, Timer timer2) {
        this.loopView = loopView2;
        this.b = f;
        this.timer = timer2;
    }

    public final void run() {
        if (this.a == 2.1474839E9f) {
            if (Math.abs(this.b) <= 2000.0f) {
                this.a = this.b;
            } else if (this.b > 0.0f) {
                this.a = 2000.0f;
            } else {
                this.a = -2000.0f;
            }
        }
        if (Math.abs(this.a) < 0.0f || Math.abs(this.a) > 20.0f) {
            this.loopView.totalScrollY -= (int) ((this.a * 10.0f) / 1000.0f);
            if (!this.loopView.isLoop) {
                if (this.loopView.totalScrollY <= ((int) (((float) (-this.loopView.positon)) * this.loopView.l * ((float) this.loopView.h)))) {
                    this.a = 40.0f;
                    LoopView loopView2 = this.loopView;
                    loopView2.totalScrollY = (int) (((float) (-loopView2.positon)) * this.loopView.l * ((float) this.loopView.h));
                } else if (this.loopView.totalScrollY >= ((int) (((float) ((this.loopView.arrayList.size() - 1) - this.loopView.positon)) * this.loopView.l * ((float) this.loopView.h)))) {
                    LoopView loopView3 = this.loopView;
                    loopView3.totalScrollY = (int) (((float) ((loopView3.arrayList.size() - 1) - this.loopView.positon)) * this.loopView.l * ((float) this.loopView.h));
                    this.a = -40.0f;
                }
            }
            float f = this.a;
            if (f < 0.0f) {
                this.a = f + 20.0f;
            } else {
                this.a = f - 20.0f;
            }
            this.loopView.handler.sendEmptyMessage(1000);
            return;
        }
        this.timer.cancel();
        this.loopView.handler.sendEmptyMessage(2000);
    }
}
