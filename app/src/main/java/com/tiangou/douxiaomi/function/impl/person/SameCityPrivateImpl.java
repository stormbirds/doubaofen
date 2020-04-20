package com.tiangou.douxiaomi.function.impl.person;

import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCulitivateImpl;
import com.tiangou.douxiaomi.utils.DensityUtil;
import java.util.ArrayList;

public class SameCityPrivateImpl<R extends ConfigBean> extends BaseCulitivateImpl<R> {
    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public SameCityPrivateImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                culitivateLike();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void culitivateLike() throws Exception {
        this.names = new ArrayList();
        while (App.getInstance().startRun.booleanValue() && canNext(this.names.size()).booleanValue()) {
            doWorkLive("android:id/content");
            if (App.getInstance().startRun.booleanValue() && canNext(this.names.size()).booleanValue()) {
                upTouch();
            }
        }
    }

    private void doWorkLive(String str) throws Exception {
        AccessibilityNodeInfo findViewById = ServiceUtils.findViewById(this.service, str);
        if (findViewById != null && findViewById.isVisibleToUser()) {
            leftTouch();
            AccessibilityNodeInfo findViewById2 = ServiceUtils.findViewById(this.service, str);
            if ((findViewById2 == null || !findViewById2.isVisibleToUser()) && App.getInstance().startRun.booleanValue()) {
                if (this.privateType.contains("1")) {
                    startAttenForColor();
                    Thread.sleep((long) this.sleep);
                }
                if (this.privateType.contains("2")) {
                    int statusBarHeight = DensityUtil.getStatusBarHeight(this.service) + Utils.dp2Px(this.service, 29.0f);
                    int displayWidth = Utils.getDisplayWidth(this.service) - Utils.dp2Px(this.service, 31.0f);
                    Log.e("qyh", "宽度==" + displayWidth + "  高度" + statusBarHeight);
                    clickView(displayWidth, statusBarHeight);
                    Thread.sleep((long) this.sleep);
                    sendMessage();
                }
                this.names.add("1");
            }
            backToId(str);
            Thread.sleep((long) App.getInstance().config.getSendInterval());
        }
    }
}
