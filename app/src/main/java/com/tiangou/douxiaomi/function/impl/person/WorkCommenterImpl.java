package com.tiangou.douxiaomi.function.impl.person;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;
import com.tiangou.douxiaomi.jni.Jni;
import java.util.List;

public class WorkCommenterImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public WorkCommenterImpl(R r) {
        super(r);
        this.functionType = 1;
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveCommenter();
    }

    private void solveCommenter() {
        List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(6));
        List<AccessibilityNodeInfo> findViewByClassName2 = getCommentRecy(findViewByClassName) != null ? ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(3)) : null;
        if (Utils.isEmptyArray(findViewByClassName) || Utils.isEmptyArray(findViewByClassName2)) {
            Log.e("qyh", "列表0");
        } else {
            solveRecyclerView(findViewByClassName2.get(0).getText().toString());
        }
    }
}
