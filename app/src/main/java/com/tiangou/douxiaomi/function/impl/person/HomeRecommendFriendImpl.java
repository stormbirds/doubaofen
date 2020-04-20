package com.tiangou.douxiaomi.function.impl.person;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;
import com.tiangou.douxiaomi.jni.Jni;
import java.util.ArrayList;
import java.util.List;

public class HomeRecommendFriendImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public HomeRecommendFriendImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            this.names = new ArrayList();
            sendRandomPerson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRandomPerson() throws Exception {
        while (App.getInstance().startRun.booleanValue() && canNext(this.names.size()).booleanValue()) {
            Thread.sleep((long) this.sleep);
            AccessibilityNodeInfo findViewById = ServiceUtils.findViewById(this.service, Jni.getSbObj(26));
            if (findViewById != null) {
                ServiceUtils.clickView(findViewById);
            }
            this.privateType = "2";
            sendOrAttenUser("推荐");
            this.names.add("1");
            backToMain(0);
            if (canNext(this.names.size()).booleanValue() && App.getInstance().startRun.booleanValue()) {
                List<AccessibilityNodeInfo> findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, Jni.getSbObj(1));
                if (!Utils.isEmptyArray(findViewByEqualsText)) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        upTouch();
                    } else {
                        ServiceUtils.clickView(findViewByEqualsText.get(0));
                    }
                }
                Thread.sleep((long) (this.longSleep + this.sleep));
            }
        }
    }
}
