package com.tiangou.douxiaomi.function.impl.base;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.jni.Jni;
import java.util.Iterator;
import java.util.List;

public abstract class BaseCommentImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public int count = 0;

    public BaseCommentImpl(R r) {
        super(r);
    }

    public void commentVideo() throws Exception {
        AccessibilityNodeInfo commentIcon;
        do {
            Thread.sleep((long) this.sleep);
            commentIcon = getCommentIcon();
            if (!App.getInstance().startRun.booleanValue()) {
                break;
            }
        } while (commentIcon == null);
        if (commentIcon != null) {
            ServiceUtils.clickView(commentIcon);
            sendComment(false);
            AccessibilityNodeInfo workerCloseIcon = getWorkerCloseIcon();
            if (workerCloseIcon != null) {
                ServiceUtils.clickView(workerCloseIcon);
                return;
            }
            return;
        }
        Log.e("qyh", "评论失败");
    }

    public AccessibilityNodeInfo getCommentIcon() {
        for (AccessibilityNodeInfo next : ServiceUtils.findViewByContentDescription(this.service, Jni.getSbObj(56))) {
            if (next != null && next.isVisibleToUser()) {
                return next;
            }
        }
        return null;
    }

    public AccessibilityNodeInfo getWorkerCloseIcon() {
        List<AccessibilityNodeInfo> findViewByContainsText = ServiceUtils.findViewByContainsText((AccessibilityService) this.service, Jni.getSbObj(80));
        if (Utils.isEmptyArray(findViewByContainsText)) {
            List<AccessibilityNodeInfo> findViewByContainsText2 = ServiceUtils.findViewByContainsText((AccessibilityService) this.service, Jni.getSbObj(81));
            if (Utils.isEmptyArray(findViewByContainsText2)) {
                findViewByContainsText2 = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, Jni.getSbObj(82));
            }
            if (Utils.isEmptyArray(findViewByContainsText2)) {
                return null;
            }
            for (AccessibilityNodeInfo parent : findViewByContainsText2) {
                AccessibilityNodeInfo parent2 = parent.getParent().getParent();
                if (parent2.getClassName().equals(Jni.getSbObj(88)) && parent2.getChildCount() > 1) {
                    for (int i = 0; i < parent2.getChildCount(); i++) {
                        if (parent2.getChild(i).getClassName().equals(Jni.getSbObj(2))) {
                            return parent2.getChild(i);
                        }
                    }
                    continue;
                }
            }
            return null;
        }
        for (AccessibilityNodeInfo parent3 : findViewByContainsText) {
            AccessibilityNodeInfo parent4 = parent3.getParent().getParent().getParent().getParent();
            if (parent4.getClassName().equals(Jni.getSbObj(57)) && parent4.getChildCount() > 1) {
                for (int i2 = 0; i2 < parent4.getChildCount(); i2++) {
                    if (parent4.getChild(i2).getClassName().equals(Jni.getSbObj(2))) {
                        return parent4.getChild(i2);
                    }
                }
                continue;
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0062  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendComment(java.lang.Boolean r8) throws java.lang.Exception {
        /*
            r7 = this;
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x000d
            return
        L_0x000d:
            r0 = 0
            r1 = 0
        L_0x000f:
            int r2 = r7.sleep
            long r2 = (long) r2
            java.lang.Thread.sleep(r2)
            com.tiangou.douxiaomi.service.AccessService r2 = r7.service
            r3 = 3
            java.lang.String r4 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r2 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r2, (java.lang.String) r4)
            r4 = 1
            int r1 = r1 + r4
            int r5 = r2.size()
            r6 = 5
            if (r5 >= r4) goto L_0x002b
            if (r1 < r6) goto L_0x000f
        L_0x002b:
            if (r1 >= r6) goto L_0x00b7
            java.lang.Object r1 = r2.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r1 = (android.view.accessibility.AccessibilityNodeInfo) r1
            boolean r8 = r8.booleanValue()
            if (r8 == 0) goto L_0x0040
            com.tiangou.douxiaomi.utils.DataConfig r8 = r7.getConfig()
            java.lang.String r8 = r8.forwardStr
            goto L_0x0048
        L_0x0040:
            com.tiangou.douxiaomi.utils.DataConfig r8 = r7.getConfig()
            java.lang.String r8 = r8.getMessage()
        L_0x0048:
            com.tiangou.douxiaomi.ServiceUtils.setText(r1, r8)
            int r8 = r7.sleep
            long r1 = (long) r8
            java.lang.Thread.sleep(r1)
            r8 = 0
        L_0x0052:
            com.tiangou.douxiaomi.service.AccessService r1 = r7.service
            java.lang.String r2 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r1 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r1, (java.lang.String) r2)
            int r2 = r1.size()
            if (r2 <= 0) goto L_0x007f
            java.lang.Object r8 = r1.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r8 = (android.view.accessibility.AccessibilityNodeInfo) r8
            com.tiangou.douxiaomi.ServiceUtils.clickView(r8)
            int r8 = r7.longSleep
            long r5 = (long) r8
            java.lang.Thread.sleep(r5)
            com.tiangou.douxiaomi.service.AccessService r8 = r7.service
            r2 = 2
            java.lang.String r2 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r2)
            java.util.List r8 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r8, (java.lang.String) r2)
            r7.removeEmo(r8)
        L_0x007f:
            boolean r2 = com.tiangou.douxiaomi.Utils.isEmptyArray(r8)
            if (r2 == 0) goto L_0x0091
            com.tiangou.douxiaomi.App r2 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r2 = r2.startRun
            boolean r2 = r2.booleanValue()
            if (r2 != 0) goto L_0x0052
        L_0x0091:
            com.tiangou.douxiaomi.ServiceUtils.recycleAccessibilityNodeInfo(r1)
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x00b7
            int r0 = r8.size()
            int r0 = r0 - r4
            java.lang.Object r0 = r8.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r0 = (android.view.accessibility.AccessibilityNodeInfo) r0
            com.tiangou.douxiaomi.ServiceUtils.clickView(r0)
            com.tiangou.douxiaomi.ServiceUtils.recycleAccessibilityNodeInfo(r8)
            int r8 = r7.sleep
            long r0 = (long) r8
            java.lang.Thread.sleep(r0)
        L_0x00b7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseCommentImpl.sendComment(java.lang.Boolean):void");
    }

    private void removeEmo(List<AccessibilityNodeInfo> list) {
        Iterator<AccessibilityNodeInfo> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getParent().getClassName().equals(Jni.getSbObj(37))) {
                it.remove();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x0115  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendLiveComment() throws java.lang.Exception {
        /*
            r10 = this;
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x000d
            return
        L_0x000d:
            r0 = 0
            r1 = 0
        L_0x000f:
            int r2 = r10.sleep
            long r2 = (long) r2
            java.lang.Thread.sleep(r2)
            r2 = 1
            int r1 = r1 + r2
            com.tiangou.douxiaomi.service.AccessService r3 = r10.service
            r4 = 22
            java.lang.String r4 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r4)
            java.util.List r3 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r3, (java.lang.String) r4)
            boolean r4 = com.tiangou.douxiaomi.Utils.isEmptyArray(r3)
            r5 = 5
            if (r4 == 0) goto L_0x0038
            if (r1 >= r5) goto L_0x0038
            com.tiangou.douxiaomi.App r4 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r4 = r4.startRun
            boolean r4 = r4.booleanValue()
            if (r4 != 0) goto L_0x000f
        L_0x0038:
            boolean r1 = com.tiangou.douxiaomi.Utils.isEmptyArray(r3)
            r4 = 3
            if (r1 != 0) goto L_0x00fa
            java.lang.Object r1 = r3.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r1 = (android.view.accessibility.AccessibilityNodeInfo) r1
            com.tiangou.douxiaomi.ServiceUtils.clickView(r1)
            r1 = 0
        L_0x0049:
            int r3 = r10.sleep
            long r6 = (long) r3
            java.lang.Thread.sleep(r6)
            com.tiangou.douxiaomi.service.AccessService r3 = r10.service
            java.lang.String r6 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r4)
            java.util.List r3 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r3, (java.lang.String) r6)
            int r1 = r1 + r2
            int r6 = r3.size()
            if (r6 >= r2) goto L_0x0062
            if (r1 < r5) goto L_0x0049
        L_0x0062:
            if (r1 >= r5) goto L_0x00f1
            com.tiangou.douxiaomi.service.AccessService r1 = r10.service
            r5 = 2
            java.lang.String r6 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r5)
            java.util.List r1 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r1, (java.lang.String) r6)
            r6 = 0
        L_0x0070:
            com.tiangou.douxiaomi.utils.DataConfig r7 = r10.getConfig()
            int r7 = r7.messageCount
            if (r6 >= r7) goto L_0x00e5
            com.tiangou.douxiaomi.App r7 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r7 = r7.startRun
            boolean r7 = r7.booleanValue()
            if (r7 == 0) goto L_0x00e2
            java.lang.Object r7 = r3.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r7 = (android.view.accessibility.AccessibilityNodeInfo) r7
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            com.tiangou.douxiaomi.utils.DataConfig r9 = r10.getConfig()
            int r9 = r9.messageCount
            if (r9 != r2) goto L_0x00a0
            com.tiangou.douxiaomi.utils.DataConfig r9 = r10.getConfig()
            java.lang.String r9 = r9.getMessage()
            goto L_0x00a8
        L_0x00a0:
            com.tiangou.douxiaomi.utils.DataConfig r9 = r10.getConfig()
            java.lang.String r9 = r9.getMessage(r6)
        L_0x00a8:
            r8.append(r9)
            int r9 = r6 % 2
            if (r9 != 0) goto L_0x00b2
            java.lang.String r9 = ""
            goto L_0x00b4
        L_0x00b2:
            java.lang.String r9 = "."
        L_0x00b4:
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.tiangou.douxiaomi.ServiceUtils.setText(r7, r8)
            java.lang.Object r7 = r3.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r7 = (android.view.accessibility.AccessibilityNodeInfo) r7
            com.tiangou.douxiaomi.ServiceUtils.clickView(r7)
            int r7 = r10.sleep
            int r7 = r7 / r5
            long r7 = (long) r7
            java.lang.Thread.sleep(r7)
            int r7 = r1.size()
            int r7 = r7 - r2
            java.lang.Object r7 = r1.get(r7)
            android.view.accessibility.AccessibilityNodeInfo r7 = (android.view.accessibility.AccessibilityNodeInfo) r7
            com.tiangou.douxiaomi.ServiceUtils.clickView(r7)
            int r7 = r10.sleep
            long r7 = (long) r7
            java.lang.Thread.sleep(r7)
        L_0x00e2:
            int r6 = r6 + 1
            goto L_0x0070
        L_0x00e5:
            com.tiangou.douxiaomi.ServiceUtils.recycleAccessibilityNodeInfo(r1)
            com.tiangou.douxiaomi.ServiceUtils.recycleAccessibilityNodeInfo(r3)
            int r1 = r10.longSleep
            long r5 = (long) r1
            java.lang.Thread.sleep(r5)
        L_0x00f1:
            r10.back()
            int r1 = r10.sleep
            long r5 = (long) r1
            java.lang.Thread.sleep(r5)
        L_0x00fa:
            r1 = 0
        L_0x00fb:
            int r1 = r1 + r2
            com.tiangou.douxiaomi.service.AccessService r3 = r10.service
            r5 = 89
            java.lang.String r5 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r5)
            java.util.List r3 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r3, (java.lang.String) r5)
            java.lang.String r5 = "qyh"
            java.lang.String r6 = "back"
            android.util.Log.e(r5, r6)
            boolean r5 = com.tiangou.douxiaomi.Utils.isEmptyArray(r3)
            if (r5 == 0) goto L_0x0118
            r10.back()
        L_0x0118:
            int r5 = r10.sleep
            long r5 = (long) r5
            java.lang.Thread.sleep(r5)
            boolean r5 = com.tiangou.douxiaomi.Utils.isEmptyArray(r3)
            if (r5 == 0) goto L_0x0132
            if (r1 >= r4) goto L_0x0132
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r5 = r5.startRun
            boolean r5 = r5.booleanValue()
            if (r5 != 0) goto L_0x00fb
        L_0x0132:
            boolean r1 = com.tiangou.douxiaomi.Utils.isEmptyArray(r3)
            if (r1 != 0) goto L_0x0141
            java.lang.Object r0 = r3.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r0 = (android.view.accessibility.AccessibilityNodeInfo) r0
            com.tiangou.douxiaomi.ServiceUtils.clickView(r0)
        L_0x0141:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseCommentImpl.sendLiveComment():void");
    }
}
