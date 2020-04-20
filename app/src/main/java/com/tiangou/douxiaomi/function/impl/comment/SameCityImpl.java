package com.tiangou.douxiaomi.function.impl.comment;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCommentImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.List;

public class SameCityImpl<R extends ConfigBean> extends BaseCommentImpl<R> {
    public SameCityImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            this.isPrepose = backToMain(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            cityService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cityService() {
        try {
            new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public AccessibilityNodeInfo getParentInfo() {
                    List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) SameCityImpl.this.service, Jni.getSbObj(6));
                    if (!Utils.isEmptyArray(findViewByClassName)) {
                        return findViewByClassName.get(0);
                    }
                    return null;
                }

                public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                    if (accessibilityNodeInfo.getClassName().equals(Jni.getSbObj(88)) || !Utils.isEmptyArray(ServiceUtils.findViewByEqualsText(accessibilityNodeInfo, Jni.getSbObj(96)))) {
                        return new Object[]{null, null};
                    }
                    if (!Utils.isEmptyArray(list)) {
                        SameCityImpl.this.runSleep(list.size());
                    }
                    return new Object[]{accessibilityNodeInfo, accessibilityNodeInfo.getContentDescription().toString()};
                }

                public void next() throws Exception {
                    if (!Utils.isEmptyArray(ServiceUtils.findViewByEqualsText((AccessibilityService) SameCityImpl.this.service, Jni.getSbObj(22)))) {
                        Log.e("qyh", "直播");
                    } else if (Utils.isEmptyArray(ServiceUtils.findViewByEqualsText((AccessibilityService) SameCityImpl.this.service, "直播已结束"))) {
                        List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) SameCityImpl.this.service, Jni.getSbObj(87));
                        if (!Utils.isEmptyArray(findViewByClassName)) {
                            if (Build.VERSION.SDK_INT >= 24) {
                                SameCityImpl.this.clickView(findViewByClassName.get(0));
                            }
                            SameCityImpl.this.sendComment(false);
                        }
                    }
                    SameCityImpl.this.backToList(Jni.getSbObj(-1));
                }

                public Boolean runNext(int i) throws Exception {
                    return SameCityImpl.this.canNext(i);
                }
            }, getMaxCount()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
