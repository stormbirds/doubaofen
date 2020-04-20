package com.tiangou.douxiaomi.function.impl.person;

import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;

public class LiveMemberImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public LiveMemberImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveMember();
    }

    private void solveMember() {
        solveRecyclerView("本场榜");
    }
}
