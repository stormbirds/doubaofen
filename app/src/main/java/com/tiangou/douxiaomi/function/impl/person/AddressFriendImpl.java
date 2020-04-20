package com.tiangou.douxiaomi.function.impl.person;

import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;

public class AddressFriendImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public AddressFriendImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            if (getParams() != null && backToMain(4).booleanValue()) {
                this.isPrepose = enterMyAddressList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveRecycleView("手机通讯录");
    }
}
