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
import java.util.ArrayList;
import java.util.List;

public class UserWorksCommentImpl<R extends ConfigBean> extends BaseCommentImpl<R> {
    public UserWorksCommentImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            this.isPrepose = false;
            if (Build.VERSION.SDK_INT >= 24) {
                this.isPrepose = backToMain(4);
                if (this.isPrepose.booleanValue()) {
                    this.isPrepose = enterCustomUserPersonHome(((ConfigBean) this.params).id);
                    Log.e("qyh", "isPrepose==" + this.isPrepose);
                    if (this.isPrepose.booleanValue()) {
                        changeUserTab("作品");
                        Thread.sleep((long) this.sleep);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            commentUserWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commentUserWork() throws Exception {
        if (isUserMainPage().booleanValue()) {
            if (Build.VERSION.SDK_INT >= 24) {
                upTouch();
            }
            this.names = new ArrayList();
            List<AccessibilityNodeInfo> filterErrData = ServiceUtils.filterErrData(ServiceUtils.findViewByIdList(this.service, Jni.getSbObj(33)));
            ServiceUtils.clickView(filterErrData.get(0));
            final String charSequence = filterErrData.get(0).getText().toString();
            this.utils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public AccessibilityNodeInfo getParentInfo() {
                    List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) UserWorksCommentImpl.this.service, Jni.getSbObj(6));
                    if (Utils.isEmptyArray(findViewByClassName)) {
                        return null;
                    }
                    for (AccessibilityNodeInfo next : findViewByClassName) {
                        if (next != null && next.getChildCount() > 0 && next.isVisibleToUser()) {
                            return next;
                        }
                    }
                    return null;
                }

                /* JADX WARNING: Removed duplicated region for block: B:14:0x0060  */
                /* JADX WARNING: Removed duplicated region for block: B:15:0x0067  */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public java.lang.Object[] solveTag(android.view.accessibility.AccessibilityNodeInfo r8, java.util.List<java.lang.String> r9) throws java.lang.Exception {
                    /*
                        r7 = this;
                        android.view.accessibility.AccessibilityNodeInfo r8 = r7.getParentInfo()
                        r0 = 0
                        r1 = 0
                        r2 = 1
                        if (r8 == 0) goto L_0x0032
                        int r3 = r8.getChildCount()
                        if (r3 <= 0) goto L_0x0032
                        int r3 = r8.getChildCount()
                        com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl r4 = com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl.this
                        com.tiangou.douxiaomi.service.RecyclerViewClickUtils r4 = r4.utils
                        java.util.List<android.view.accessibility.AccessibilityNodeInfo> r4 = r4.items
                        int r4 = r4.size()
                        int r3 = r3 - r4
                        int r3 = r3 - r2
                        if (r3 < 0) goto L_0x0031
                        int r4 = r8.getChildCount()
                        int r4 = r4 - r2
                        if (r3 >= r4) goto L_0x0031
                        android.view.accessibility.AccessibilityNodeInfo r8 = r8.getChild(r3)
                        android.view.accessibility.AccessibilityNodeInfo r8 = r8.getChild(r1)
                        goto L_0x0032
                    L_0x0031:
                        r8 = r0
                    L_0x0032:
                        if (r8 != 0) goto L_0x007e
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder
                        r3.<init>()
                        java.lang.String r4 = "视频"
                        r3.append(r4)
                        int r4 = r9.size()
                        int r4 = r4 + r2
                        r3.append(r4)
                        java.lang.String r3 = r3.toString()
                        java.util.ArrayList r4 = new java.util.ArrayList
                        r4.<init>()
                        r4 = r8
                        r8 = 0
                    L_0x0051:
                        int r8 = r8 + r2
                        com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl r5 = com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl.this
                        com.tiangou.douxiaomi.service.AccessService r5 = r5.service
                        java.util.List r5 = com.tiangou.douxiaomi.ServiceUtils.findViewByContentDescription(r5, r3)
                        boolean r6 = com.tiangou.douxiaomi.Utils.isEmptyArray(r5)
                        if (r6 != 0) goto L_0x0067
                        java.lang.Object r4 = r5.get(r1)
                        android.view.accessibility.AccessibilityNodeInfo r4 = (android.view.accessibility.AccessibilityNodeInfo) r4
                        goto L_0x006c
                    L_0x0067:
                        r5 = 500(0x1f4, double:2.47E-321)
                        java.lang.Thread.sleep(r5)
                    L_0x006c:
                        com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
                        java.lang.Boolean r5 = r5.startRun
                        boolean r5 = r5.booleanValue()
                        if (r5 == 0) goto L_0x007d
                        if (r4 != 0) goto L_0x007d
                        r5 = 3
                        if (r8 < r5) goto L_0x0051
                    L_0x007d:
                        r8 = r4
                    L_0x007e:
                        if (r8 != 0) goto L_0x0088
                        java.lang.String r9 = "qyh"
                        java.lang.String r3 = "tag==异常"
                        android.util.Log.e(r9, r3)
                        goto L_0x0097
                    L_0x0088:
                        boolean r3 = com.tiangou.douxiaomi.Utils.isEmptyArray(r9)
                        if (r3 != 0) goto L_0x0097
                        com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl r3 = com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl.this
                        int r9 = r9.size()
                        r3.runSleep(r9)
                    L_0x0097:
                        r9 = 2
                        java.lang.Object[] r9 = new java.lang.Object[r9]
                        r9[r1] = r8
                        if (r8 != 0) goto L_0x009f
                        goto L_0x00a3
                    L_0x009f:
                        java.lang.CharSequence r0 = r8.getContentDescription()
                    L_0x00a3:
                        r9[r2] = r0
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl.AnonymousClass1.solveTag(android.view.accessibility.AccessibilityNodeInfo, java.util.List):java.lang.Object[]");
                }

                public void next() throws Exception {
                    List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) UserWorksCommentImpl.this.service, Jni.getSbObj(87));
                    if (!Utils.isEmptyArray(findViewByClassName)) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            UserWorksCommentImpl.this.clickView(findViewByClassName.get(0));
                        }
                        UserWorksCommentImpl.this.sendComment(false);
                        UserWorksCommentImpl.this.backToList(charSequence);
                    }
                }

                public Boolean runNext(int i) throws Exception {
                    Boolean canNext = UserWorksCommentImpl.this.canNext(i);
                    Log.e("qyh", "next==" + canNext);
                    return canNext;
                }
            }, getMaxCount());
            this.utils.setService(this.service);
            this.utils.start();
        }
    }
}
