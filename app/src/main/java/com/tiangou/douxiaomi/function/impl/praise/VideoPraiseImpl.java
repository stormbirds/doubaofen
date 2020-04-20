package com.tiangou.douxiaomi.function.impl.praise;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCulitivateImpl;
import java.util.List;

public class VideoPraiseImpl<R extends ConfigBean> extends BaseCulitivateImpl<R> {
    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public VideoPraiseImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        int maxCount = getMaxCount();
        int i = 0;
        while (i < maxCount) {
            try {
                if (App.getInstance().startRun.booleanValue()) {
                    List<AccessibilityNodeInfo> findAllView = ServiceUtils.findAllView(this.service);
                    List<AccessibilityNodeInfo> findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, "点击进入直播间");
                    if (findAllView == null || findAllView.size() == 2 || !canPraise(findViewByEqualsText)) {
                        Thread.sleep((long) this.sleep);
                        upTouchQuickPraise();
                        Thread.sleep((long) this.longSleep);
                    } else {
                        doubleClickView(Utils.getDisplayWidth(this.service) / 2, (Utils.getDisplayHeight(this.service) / 5) * 2);
                        int i2 = i + 1;
                        if (i2 % getConfig().maxCount == 0) {
                            Thread.sleep((long) getConfig().getSleepTime());
                        } else {
                            Thread.sleep((long) getConfig().getSendInterval());
                        }
                        if (canNext(i2).booleanValue()) {
                            upTouchQuick();
                            Thread.sleep((long) this.longSleep);
                        }
                    }
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private boolean canPraise(List<AccessibilityNodeInfo> list) {
        if (Utils.isEmptyArray(list)) {
            return true;
        }
        for (AccessibilityNodeInfo isVisibleToUser : list) {
            if (isVisibleToUser.isVisibleToUser()) {
                return false;
            }
        }
        return true;
    }

    public void upTouchQuickPraise() throws Exception {
        Thread.sleep((long) this.sleep);
        Path path = new Path();
        path.moveTo(1.0f, (float) ((Utils.getDisplayHeight(this.service) / 4) * 3));
        path.lineTo(1.0f, 0.0f);
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 100, 300)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep((long) this.longSleep);
    }
}
