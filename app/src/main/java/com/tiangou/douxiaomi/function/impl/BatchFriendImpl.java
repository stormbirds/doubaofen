package com.tiangou.douxiaomi.function.impl;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.ArrayList;
import java.util.List;

public class BatchFriendImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public Boolean atten = true;

    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public BatchFriendImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveRecycleViewBatch(getTitle());
    }

    public void solveRecycleViewBatch(String str) {
        try {
            this.utils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public void next() throws Exception {
                }

                public AccessibilityNodeInfo getParentInfo() {
                    return BatchFriendImpl.this.getRecyclerView(ServiceUtils.findViewByClassName((AccessibilityService) BatchFriendImpl.this.service, Jni.getSbObj(6)));
                }

                public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                    Rect rect = new Rect();
                    accessibilityNodeInfo.getBoundsInScreen(rect);
                    if (rect.top == rect.bottom) {
                        return new Object[]{null, null};
                    }
                    ArrayList arrayList = new ArrayList();
                    ServiceUtils.findViewByClassName(arrayList, accessibilityNodeInfo, Jni.getSbObj(7));
                    if (arrayList.size() == 0 || (arrayList.size() == 1 && ((AccessibilityNodeInfo) arrayList.get(0)).equals(Jni.getSbObj(21)))) {
                        return new Object[]{null, null};
                    }
                    String charSequence = ((AccessibilityNodeInfo) arrayList.get(0)).getText().toString();
                    String str = "";
                    if (Utils.isEmptyArray(list) && !Utils.isEmptyArray(ServiceUtils.findViewByEqualsText((AccessibilityService) BatchFriendImpl.this.service, "粉丝影响力"))) {
                        str = Jni.getSbObj(87);
                    }
                    AccessibilityNodeInfo access$000 = BatchFriendImpl.this.getAttenBtn(accessibilityNodeInfo);
                    if (access$000 != null && !accessibilityNodeInfo.getClassName().equals(str) && !charSequence.contains("好友加入抖音") && !list.contains(charSequence) && !accessibilityNodeInfo.getClassName().equals(Jni.getSbObj(37))) {
                        if (!BatchFriendImpl.this.atten.booleanValue() ? access$000.getText().equals("关注") : !access$000.getText().equals("关注")) {
                            access$000 = null;
                        }
                        if (access$000 != null) {
                            if (access$000.isClickable()) {
                                return new Object[]{access$000, charSequence};
                            }
                            return new Object[]{arrayList.get(0), charSequence};
                        }
                    }
                    return new Object[]{null, null};
                }

                public Boolean runNext(int i) throws Exception {
                    return BatchFriendImpl.this.canNext(i);
                }
            }, getMaxCount());
            this.utils.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public AccessibilityNodeInfo getAttenBtn(AccessibilityNodeInfo accessibilityNodeInfo) {
        List<AccessibilityNodeInfo> findViewByClassNameInGroup = ServiceUtils.findViewByClassNameInGroup(accessibilityNodeInfo, "android.widget.TextView");
        if (Utils.isEmptyArray(findViewByClassNameInGroup)) {
            return null;
        }
        for (AccessibilityNodeInfo next : findViewByClassNameInGroup) {
            if (next.getParent().getClassName().equals("android.widget.RelativeLayout")) {
                return next;
            }
        }
        return null;
    }
}
