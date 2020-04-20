package com.tiangou.douxiaomi.pickerview;

import java.util.Timer;
import java.util.TimerTask;

final class MyTimerTask extends TimerTask {
    float a = 2.1474839E9f;
    float b = 0.0f;
    final int c;
    final LoopView loopView;
    final Timer timer;

    MyTimerTask(LoopView loopView2, int i, Timer timer2) {
        this.loopView = loopView2;
        this.c = i;
        this.timer = timer2;
    }

    public final void run() {
        if (this.a == 2.1474839E9f) {
            this.a = ((float) (this.c - LoopView.getSelectItem(this.loopView))) * this.loopView.l * ((float) this.loopView.h);
            if (this.c > LoopView.getSelectItem(this.loopView)) {
                this.b = -1000.0f;
            } else {
                this.b = 1000.0f;
            }
        }
        if (Math.abs(this.a) < 1.0f) {
            this.timer.cancel();
            this.loopView.handler.sendEmptyMessage(2000);
            return;
        }
        int i = (int) ((this.b * 10.0f) / 1000.0f);
        if (Math.abs(this.a) < ((float) Math.abs(i))) {
            i = (int) (-this.a);
        }
        this.loopView.totalScrollY -= i;
        this.a = ((float) i) + this.a;
        this.loopView.handler.sendEmptyMessage(1000);
    }
}
