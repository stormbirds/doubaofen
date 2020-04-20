package com.tiangou.douxiaomi.pickerview;

final class LoopRunnable implements Runnable {
    final LoopView loopView;

    LoopRunnable(LoopView loopView2) {
        this.loopView = loopView2;
    }

    public final void run() {
        this.loopView.loopListener.onItemSelect(LoopView.getSelectItem(this.loopView));
    }
}
