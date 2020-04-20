package com.tiangou.douxiaomi.function.impl.person;

import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;

public class FriendsListImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public FriendsListImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            this.isPrepose = backToMain(4);
            this.isPrepose = enterMyFriend();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveRecycleView("好友列表");
    }
}
