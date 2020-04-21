package com.tiangou.douxiaomi;

import android.util.Log;
import com.tiangou.douxiaomi.function.impl.base.BaseImpl;
import java.util.List;

public class TaskUtils {
    int current = 0;
    public List<BaseImpl> currentTasks;

    public TaskUtils(List<BaseImpl> list) {
        this.currentTasks = list;
    }

    public void start() {
        Log.e("qyh", "start");
        App.getInstance().startRun = true;
        this.current = 0;
        onNext();
    }

    public void onNext() {
        if (App.getInstance().startRun) {
            if (this.current >= this.currentTasks.size()) {
                App.getInstance().startRun = false;
                App.getInstance().resetStartUI();
                return;
            }
            this.currentTasks.get(this.current).start();
            this.current++;
        }
    }

    public void finish() {
        App.getInstance().startRun = false;
    }
}
