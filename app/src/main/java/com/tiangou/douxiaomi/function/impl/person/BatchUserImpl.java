package com.tiangou.douxiaomi.function.impl.person;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.List;

public class BatchUserImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    public BatchUserImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            this.isPrepose = backToMain(4);
            if (!this.isPrepose.booleanValue() || !App.getInstance().startRun.booleanValue()) {
                this.isPrepose = false;
                return;
            }
            this.isPrepose = enterFindFriend();
            Thread.sleep((long) this.sleep);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            doBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00dc A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doBatch() throws java.lang.Exception {
        /*
            r8 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r8.names = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            com.tiangou.douxiaomi.utils.DataConfig r1 = r8.getConfig()
            java.util.List r1 = r1.getOperaUser()
            r0.<init>(r1)
            java.util.Iterator r0 = r0.iterator()
        L_0x0018:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x00e5
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            com.tiangou.douxiaomi.App r2 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r2 = r2.startRun
            boolean r2 = r2.booleanValue()
            if (r2 == 0) goto L_0x00e5
            java.lang.Boolean r2 = r8.next()
            boolean r2 = r2.booleanValue()
            if (r2 != 0) goto L_0x003c
            goto L_0x00e5
        L_0x003c:
            java.util.List r2 = r8.names
            java.lang.String r3 = "1"
            r2.add(r3)
            r2 = 0
            r3 = 0
        L_0x0045:
            int r4 = r8.sleep
            int r4 = r4 / 2
            long r4 = (long) r4
            java.lang.Thread.sleep(r4)
            com.tiangou.douxiaomi.service.AccessService r4 = r8.service
            r5 = 3
            java.lang.String r5 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r5)
            java.util.List r4 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r4, (java.lang.String) r5)
            r5 = 1
            int r3 = r3 + r5
            int r6 = r4.size()
            r7 = 5
            if (r6 >= r5) goto L_0x006d
            if (r3 >= r7) goto L_0x006d
            java.lang.Boolean r5 = r8.next()
            boolean r5 = r5.booleanValue()
            if (r5 != 0) goto L_0x0045
        L_0x006d:
            if (r3 >= r7) goto L_0x00dc
            int r3 = android.os.Build.VERSION.SDK_INT
            r5 = 24
            if (r3 < r5) goto L_0x007e
            java.lang.Object r3 = r4.get(r2)
            android.view.accessibility.AccessibilityNodeInfo r3 = (android.view.accessibility.AccessibilityNodeInfo) r3
            r8.clickView(r3)
        L_0x007e:
            java.lang.Object r3 = r4.get(r2)
            android.view.accessibility.AccessibilityNodeInfo r3 = (android.view.accessibility.AccessibilityNodeInfo) r3
            com.tiangou.douxiaomi.ServiceUtils.setText(r3, r1)
            int r3 = r8.sleep
            long r3 = (long) r3
            java.lang.Thread.sleep(r3)
        L_0x008d:
            com.tiangou.douxiaomi.service.AccessService r3 = r8.service
            r4 = 4
            java.lang.String r5 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r4)
            java.util.List r3 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r3, (java.lang.String) r5)
            boolean r5 = com.tiangou.douxiaomi.Utils.isEmptyArray(r3)
            if (r5 == 0) goto L_0x00b4
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r5 = r5.startRun
            boolean r5 = r5.booleanValue()
            if (r5 == 0) goto L_0x00b4
            java.lang.Boolean r5 = r8.next()
            boolean r5 = r5.booleanValue()
            if (r5 != 0) goto L_0x008d
        L_0x00b4:
            java.lang.Object r2 = r3.get(r2)
            android.view.accessibility.AccessibilityNodeInfo r2 = (android.view.accessibility.AccessibilityNodeInfo) r2
            com.tiangou.douxiaomi.ServiceUtils.clickView(r2)
            int r2 = r8.longSleep
            long r2 = (long) r2
            java.lang.Thread.sleep(r2)
            com.tiangou.douxiaomi.service.AccessService r2 = r8.service
            r3 = 97
            java.lang.String r3 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r2 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r2, (java.lang.String) r3)
            boolean r2 = com.tiangou.douxiaomi.Utils.isEmptyArray(r2)
            if (r2 == 0) goto L_0x00dc
            java.lang.String r2 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r4)
            r8.operSearchList(r2)
        L_0x00dc:
            com.tiangou.douxiaomi.utils.DataConfig r2 = r8.getConfig()
            r2.removeUser(r1)
            goto L_0x0018
        L_0x00e5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.person.BatchUserImpl.doBatch():void");
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

    private void operSearchList(final String str) throws Exception {
        this.utils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
            public AccessibilityNodeInfo getParentInfo() {
                Log.e("qyh", "hhhh===" + Jni.getSbObj(6));
                List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) BatchUserImpl.this.service, Jni.getSbObj(6));
                AccessibilityNodeInfo recyclerView = BatchUserImpl.this.getRecyclerView(findViewByClassName);
                if (Utils.isEmptyArray(findViewByClassName) || recyclerView.getChildCount() <= 0) {
                    return null;
                }
                return recyclerView;
            }

            public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                if (!accessibilityNodeInfo.getClassName().equals(Jni.getSbObj(87))) {
                    return null;
                }
                List<AccessibilityNodeInfo> findViewByClassNameInGroup = ServiceUtils.findViewByClassNameInGroup(accessibilityNodeInfo, Jni.getSbObj(7));
                if (Utils.isEmptyArray(findViewByClassNameInGroup) || findViewByClassNameInGroup.size() <= 2) {
                    Log.e("qyh", list.size() + "");
                    return null;
                }
                if (!Utils.isEmptyArray(list)) {
                    BatchUserImpl.this.runSleep(list.size());
                }
                return new Object[]{accessibilityNodeInfo, findViewByClassNameInGroup.get(0).getText().toString() + findViewByClassNameInGroup.get(1).getText().toString()};
            }

            public void next() throws Exception {
                if (!BatchUserImpl.this.sendOrAttenUser(str).booleanValue()) {
                    BatchUserImpl.this.utils.solveError();
                }
            }

            public Boolean runNext(int i) {
                return BatchUserImpl.this.canNext(i);
            }
        }, getMaxCount());
        this.utils.start();
    }
}
