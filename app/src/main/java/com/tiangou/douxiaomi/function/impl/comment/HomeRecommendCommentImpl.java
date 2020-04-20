package com.tiangou.douxiaomi.function.impl.comment;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCommentImpl;
import com.tiangou.douxiaomi.jni.Jni;
import java.util.ArrayList;
import java.util.List;

public class HomeRecommendCommentImpl<R extends ConfigBean> extends BaseCommentImpl<R> {
    public HomeRecommendCommentImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        this.isPrepose = enterHomeRecommand();
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            commentCommend();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commentCommend() throws Exception {
        List<AccessibilityNodeInfo> findViewByEqualsText;
        this.names = new ArrayList();
        while (canNext(this.names.size()).booleanValue() && App.getInstance().startRun.booleanValue()) {
            commentVideo();
            this.names.add("1");
            if (canNext(this.names.size()).booleanValue()) {
                runSleep(this.names.size());
                do {
                    findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, Jni.getSbObj(1));
                } while (Utils.isEmptyArray(findViewByEqualsText));
                ServiceUtils.clickView(findViewByEqualsText.get(0));
            }
        }
    }
}
