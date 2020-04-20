package com.tiangou.douxiaomi.function.impl.base;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.utils.GBData;
import java.util.List;

public abstract class BaseCulitivateImpl<R extends ConfigBean> extends BaseCommentImpl<R> {
    public BaseCulitivateImpl(R r) {
        super(r);
    }

    public Boolean isAdvertise(List<AccessibilityNodeInfo> list) {
        if (!Utils.isEmptyArray(list)) {
            for (AccessibilityNodeInfo isVisibleToUser : list) {
                if (isVisibleToUser.isVisibleToUser()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void startPraise() throws Exception {
        Thread.sleep((long) this.sleep);
        List<AccessibilityNodeInfo> findViewByContentDescription = ServiceUtils.findViewByContentDescription(this.service, "选中，喜欢");
        if (!Utils.isEmptyArray(findViewByContentDescription)) {
            Log.e("qyh", "获取到数量" + findViewByContentDescription.size());
            for (AccessibilityNodeInfo next : findViewByContentDescription) {
                Log.e("qyh", "className==" + next.getClassName());
                if (next.getClassName().equals(Jni.getSbObj(87)) && next.getContentDescription().toString().contains("未选中")) {
                    ServiceUtils.clickView(next);
                }
            }
        }
    }

    public void startForward() throws Exception {
        Thread.sleep((long) this.sleep);
        AccessibilityNodeInfo forwardFragment = getForwardFragment(ServiceUtils.findViewByContentDescription(this.service, "分享，按钮"));
        if (forwardFragment != null) {
            ServiceUtils.clickView(forwardFragment);
            Thread.sleep((long) this.sleep);
            AccessibilityNodeInfo forwardLinearLayout = getForwardLinearLayout(ServiceUtils.findViewByEqualsText(this.service, "转发", Jni.getSbObj(7)));
            if (forwardLinearLayout != null) {
                ServiceUtils.clickView(forwardLinearLayout);
                Thread.sleep((long) this.sleep);
                sendComment(true);
                return;
            }
            back();
        }
    }

    public AccessibilityNodeInfo getForwardFragment(List<AccessibilityNodeInfo> list) {
        if (Utils.isEmptyArray(list)) {
            return null;
        }
        for (AccessibilityNodeInfo next : list) {
            if (next.getClassName().equals(Jni.getSbObj(87))) {
                return next.getChild(0);
            }
        }
        return null;
    }

    public AccessibilityNodeInfo getForwardLinearLayout(List<AccessibilityNodeInfo> list) {
        if (Utils.isEmptyArray(list)) {
            return null;
        }
        for (AccessibilityNodeInfo next : list) {
            if (next.getParent().getClassName().equals(Jni.getSbObj(87)) && next.getParent().isClickable()) {
                return next.getParent();
            }
        }
        return null;
    }

    public Boolean isAtten() throws Exception {
        int displayWidth = (Utils.getDisplayWidth(this.service) / 2) - 20;
        Boolean.valueOf(false);
        String str = "";
        int i = 0;
        char c = 0;
        int i2 = 0;
        while (i < 100) {
            Log.e("qyh", "Color验证中" + i);
            String colorStr = GBData.getColorStr(displayWidth, Utils.dp2Px(this.service, (float) (i + 100)));
            if (c == 0) {
                if (GBData.isSame(colorStr, Jni.getSbObj(90)).booleanValue()) {
                    if (i2 == 0) {
                        i2 = 0;
                    }
                    i2++;
                    if (i2 >= 2) {
                        str = colorStr;
                        c = 1;
                    }
                } else {
                    i += 2;
                }
                i2 = 0;
            } else if (c == 1) {
                Log.e("qyh", "Color验证结果 进入第二阶段" + colorStr);
                if (!GBData.isSame(colorStr, Jni.getSbObj(90)).booleanValue()) {
                    c = 2;
                } else {
                    i += 3;
                }
            } else if (c != 2) {
                continue;
            } else {
                Log.e("qyh", "Color验证结果 进入第三阶段" + colorStr);
                if (colorStr != str) {
                    i2++;
                }
                if (i2 > 2) {
                    return Boolean.valueOf(!GBData.isSame(colorStr, Jni.getSbObj(91)).booleanValue());
                }
            }
            i++;
        }
        return true;
    }

    public AccessibilityNodeInfo getCommentBean(List<AccessibilityNodeInfo> list) {
        for (AccessibilityNodeInfo next : list) {
            if (next.isVisibleToUser()) {
                return next;
            }
        }
        return null;
    }

    public void startAttenForColor() throws Exception {
        int displayWidth = (Utils.getDisplayWidth(this.service) / 2) - 20;
        Boolean.valueOf(false);
        String str = "";
        int i = 0;
        char c = 0;
        int i2 = 0;
        while (i < 100) {
            Log.e("qyh", "Color验证中" + i);
            int dp2Px = Utils.dp2Px(this.service, (float) (i + 100));
            String colorStr = GBData.getColorStr(displayWidth, dp2Px);
            if (c == 0) {
                if (GBData.isSame(colorStr, Jni.getSbObj(90)).booleanValue()) {
                    if (i2 == 0) {
                        i2 = 0;
                    }
                    i2++;
                    if (i2 >= 2) {
                        str = colorStr;
                        c = 1;
                    }
                } else {
                    i += 2;
                }
                i2 = 0;
            } else if (c == 1) {
                Log.e("qyh", "Color验证结果 进入第二阶段" + colorStr);
                if (!GBData.isSame(colorStr, Jni.getSbObj(90)).booleanValue()) {
                    c = 2;
                } else {
                    i += 3;
                }
            } else if (c != 2) {
                continue;
            } else {
                Log.e("qyh", "Color验证结果 进入第三阶段" + colorStr);
                if (colorStr != str) {
                    i2++;
                }
                if (i2 > 2) {
                    if (GBData.isSame(colorStr, Jni.getSbObj(91)).booleanValue()) {
                        clickView(displayWidth, dp2Px);
                        return;
                    }
                    return;
                }
            }
            i++;
        }
    }
}
