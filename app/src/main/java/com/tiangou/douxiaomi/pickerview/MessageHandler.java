package com.tiangou.douxiaomi.pickerview;

import android.os.Handler;
import android.os.Message;

final class MessageHandler extends Handler {
    final LoopView a;

    MessageHandler(LoopView loopView) {
        this.a = loopView;
    }

    public final void handleMessage(Message message) {
        if (message.what == 1000) {
            this.a.invalidate();
        }
        if (message.what == 2000) {
            LoopView.b(this.a);
        } else {
            int i = message.what;
        }
        super.handleMessage(message);
    }
}
