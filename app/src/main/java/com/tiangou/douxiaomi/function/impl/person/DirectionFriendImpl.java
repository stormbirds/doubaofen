package com.tiangou.douxiaomi.function.impl.person;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;

public class DirectionFriendImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public DirectionFriendImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            int i = 1;
            if (!TextUtils.isEmpty(((ConfigBean) getParams()).id)) {
                if (App.getInstance().startRun.booleanValue()) {
                    this.isPrepose = backToMain(4);
                    Log.e("qyh", "prepare1" + this.isPrepose);
                    if (this.isPrepose.booleanValue()) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            this.isPrepose = enterCustomUserPersonHome(((ConfigBean) this.params).id);
                            Log.e("qyh", "prepare2" + this.isPrepose);
                            if (this.isPrepose.booleanValue()) {
                                if (!this.isFans.booleanValue()) {
                                    i = 0;
                                }
                                this.isPrepose = enterUserFriend(i);
                                Log.e("qyh", "prepare3" + this.isPrepose);
                            }
                        }
                        Log.e("qyh", "prepare4" + this.isPrepose);
                        return;
                    }
                    return;
                }
            }
            this.isPrepose = backToMain(4);
            Log.e("qyh", "prepare5" + this.isPrepose);
            if (this.isPrepose.booleanValue()) {
                if (!this.isFans.booleanValue()) {
                    i = 0;
                }
                this.isPrepose = enterUserFriend(i);
                Log.e("qyh", "prepare6" + this.isPrepose);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveRecycleView(getTitle());
    }
}
