package com.tiangou.douxiaomi.function.impl.person;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;
import com.tiangou.douxiaomi.jni.Jni;
import java.util.ArrayList;
import java.util.List;

public class HomeRandomFriendImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public HomeRandomFriendImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        this.isPrepose = enterHomeRecommand();
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
        while (App.getInstance().startRun.booleanValue() && next().booleanValue()) {
            Thread.sleep((long) this.sleep);
            AccessibilityNodeInfo findViewById = ServiceUtils.findViewById(this.service, Jni.getSbObj(26));
            if (findViewById != null) {
                ServiceUtils.clickView(findViewById);
            }
            if (enterUserFriend(App.getInstance().isFans ? 1 : 0).booleanValue()) {
                this.names.add("1");
                solveRecycleView(getTitle());
            }
            backToMain(0);
            if (next().booleanValue() && App.getInstance().startRun.booleanValue()) {
                List<AccessibilityNodeInfo> findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, Jni.getSbObj(1));
                Thread.sleep((long) this.sleep);
                if (!Utils.isEmptyArray(findViewByEqualsText) && App.getInstance().startRun.booleanValue()) {
                    ServiceUtils.clickView(findViewByEqualsText.get(0));
                }
                Thread.sleep((long) (this.longSleep + this.sleep));
            }
        }
    }

    public Boolean next() {
        ConfigBean configBean = (ConfigBean) this.params;
        boolean z = true;
        if (configBean == null) {
            return true;
        }
        if (configBean.type == 1) {
            if (this.names.size() >= configBean.count) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        if (Utils.getTime() >= this.startTime + configBean.count) {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
