package com.tiangou.douxiaomi.function.impl.base;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePersonImpl<R extends ConfigBean> extends BaseImpl<R> {
    public Boolean isFans = false;
    public String privateType;

    public BasePersonImpl(R r) {
        super(r);
    }

    public Boolean enterHomeRecommand() {
        boolean z = false;
        try {
            Boolean backToMain = backToMain(0);
            try {
                if (!backToMain.booleanValue()) {
                    return false;
                }
                List<AccessibilityNodeInfo> findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, "推荐");
                Thread.sleep((long) this.sleep);
                if (Utils.isEmptyArray(findViewByEqualsText) || !App.getInstance().startRun.booleanValue()) {
                    return false;
                }
                ServiceUtils.clickView(findViewByEqualsText.get(0));
                Thread.sleep((long) (this.longSleep + this.sleep));
                return 1;
            } catch (Exception e) {
                e = e;
                z = backToMain;
                e.printStackTrace();
                return z;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return z;
        }
    }

    public void solveRecycleView(final String str) {
        try {
            this.utils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public AccessibilityNodeInfo getParentInfo() {
                    return BasePersonImpl.this.getRecyclerView(ServiceUtils.findViewByClassName((AccessibilityService) BasePersonImpl.this.service, Jni.getSbObj(6)));
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
                    if (Utils.isEmptyArray(list) && !Utils.isEmptyArray(ServiceUtils.findViewByEqualsText((AccessibilityService) BasePersonImpl.this.service, "粉丝影响力"))) {
                        str = Jni.getSbObj(87);
                    }
                    if (accessibilityNodeInfo.getClassName().equals(str) || charSequence.contains("好友加入抖音") || list.contains(charSequence) || accessibilityNodeInfo.getClassName().equals(Jni.getSbObj(37))) {
                        return new Object[]{null, null};
                    }
                    if (!Utils.isEmptyArray(list)) {
                        BasePersonImpl.this.runSleep(list.size());
                    }
                    if (accessibilityNodeInfo.isClickable()) {
                        return new Object[]{accessibilityNodeInfo, charSequence};
                    }
                    return new Object[]{arrayList.get(0), charSequence};
                }

                public void next() throws Exception {
                    BasePersonImpl.this.sendOrAttenUser(str);
                }

                public Boolean runNext(int i) throws Exception {
                    Boolean canNext = BasePersonImpl.this.canNext(i);
                    Log.e("qyh", "canNext==" + canNext);
                    return BasePersonImpl.this.canNext(i);
                }
            }, getMaxCount());
            this.utils.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean sendOrAttenUser(String str) throws Exception {
        boolean z = false;
        Thread.sleep((long) this.longSleep);
        int distingSex = App.getInstance().config.userSex.size() < 3 ? distingSex() : 0;
        Log.e("qyh", "sex==" + distingSex);
        if (App.getInstance().config.userSex.contains(Integer.valueOf(distingSex))) {
            if (this.privateType.contains("1")) {
                Boolean bool = false;
                int i = 0;
                do {
                    i++;
                    Thread.sleep((long) this.sleep);
                    List<AccessibilityNodeInfo> findViewByEqualsText = Utils.isEmptyArray(ServiceUtils.findViewByEqualsText(this.service, "取消关注", Jni.getSbObj(7))) ? ServiceUtils.findViewByEqualsText(this.service, Jni.getSbObj(12), Jni.getSbObj(7)) : null;
                    if (!Utils.isEmptyArray(findViewByEqualsText)) {
                        bool = true;
                        ServiceUtils.clickView(findViewByEqualsText.get(0));
                        Thread.sleep((long) this.longSleep);
                        if (!Utils.isEmptyArray(ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, "快捷设置备注名"))) {
                            Log.e("qyh", "包含");
                            List<AccessibilityNodeInfo> findViewByEqualsText2 = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, "取消");
                            if (!Utils.isEmptyArray(findViewByEqualsText2)) {
                                Log.e("qyh", "点击");
                                ServiceUtils.clickView(findViewByEqualsText2.get(0));
                                Thread.sleep((long) this.longSleep);
                            }
                        }
                    }
                    if (!App.getInstance().startRun.booleanValue()) {
                        break;
                    } else if (i >= 2) {
                        break;
                    }
                } while (bool.booleanValue());
            }
            if (this.privateType.contains("2")) {
                List<AccessibilityNodeInfo> findViewByContentDescription = ServiceUtils.findViewByContentDescription(this.service, Jni.getSbObj(29));
                if (!Utils.isEmptyArray(findViewByContentDescription) && App.getInstance().startRun.booleanValue()) {
                    ServiceUtils.clickView(findViewByContentDescription.get(findViewByContentDescription.size() - 1));
                    Thread.sleep((long) this.sleep);
                    ServiceUtils.recycleAccessibilityNodeInfo(findViewByContentDescription);
                    sendMessage();
                }
            }
            z = true;
        }
        backToList(str);
        return z;
    }

    public void sendMessage() throws Exception {
        List<AccessibilityNodeInfo> findViewByContentDescription = ServiceUtils.findViewByContentDescription(this.service, Jni.getSbObj(30));
        if (Utils.isEmptyArray(findViewByContentDescription)) {
            findViewByContentDescription = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, "发私信");
        }
        if (App.getInstance().startRun.booleanValue() && !Utils.isEmptyArray(findViewByContentDescription)) {
            ServiceUtils.clickView(findViewByContentDescription.get(0));
            ServiceUtils.recycleAccessibilityNodeInfo(findViewByContentDescription);
            editAndBack();
        }
    }

    public Boolean enterMyAddressList() throws Exception {
        if (enterFindFriend().booleanValue() && App.getInstance().startRun.booleanValue()) {
            Thread.sleep((long) this.sleep);
            AccessibilityNodeInfo findViewByFirstContainsContentDescription = ServiceUtils.findViewByFirstContainsContentDescription((AccessibilityService) this.service, "通讯录");
            if (findViewByFirstContainsContentDescription != null && App.getInstance().startRun.booleanValue()) {
                ServiceUtils.clickView(findViewByFirstContainsContentDescription);
                Thread.sleep((long) this.sleep);
                return true;
            }
        }
        return false;
    }

    public Boolean enterFindFriend() throws InterruptedException {
        AccessibilityNodeInfo findViewByFirstContainsContentDescription;
        do {
            Thread.sleep((long) this.sleep);
            findViewByFirstContainsContentDescription = ServiceUtils.findViewByFirstContainsContentDescription((AccessibilityService) this.service, "发现好友");
        } while (findViewByFirstContainsContentDescription == null);
        if (findViewByFirstContainsContentDescription == null) {
            return false;
        }
        ServiceUtils.clickView(findViewByFirstContainsContentDescription);
        Thread.sleep((long) this.sleep);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00f5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Boolean enterCustomUserPersonHome(java.lang.String r9) throws java.lang.Exception {
        /*
            r8 = this;
            java.lang.Boolean r0 = r8.enterFindFriend()
            boolean r0 = r0.booleanValue()
            r1 = 0
            if (r0 == 0) goto L_0x0107
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0107
            r0 = 0
        L_0x0018:
            int r2 = r8.sleep
            int r2 = r2 / 2
            long r2 = (long) r2
            java.lang.Thread.sleep(r2)
            com.tiangou.douxiaomi.service.AccessService r2 = r8.service
            r3 = 3
            java.lang.String r3 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r2 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r2, (java.lang.String) r3)
            r3 = 1
            int r0 = r0 + r3
            int r4 = r2.size()
            r5 = 5
            if (r4 >= r3) goto L_0x0042
            if (r0 >= r5) goto L_0x0042
            com.tiangou.douxiaomi.App r4 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r4 = r4.startRun
            boolean r4 = r4.booleanValue()
            if (r4 != 0) goto L_0x0018
        L_0x0042:
            if (r0 >= r5) goto L_0x0107
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0107
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 24
            if (r0 < r4) goto L_0x006b
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x006b
            java.lang.Object r0 = r2.get(r1)
            android.view.accessibility.AccessibilityNodeInfo r0 = (android.view.accessibility.AccessibilityNodeInfo) r0
            r8.clickView(r0)
        L_0x006b:
            java.lang.Object r0 = r2.get(r1)
            android.view.accessibility.AccessibilityNodeInfo r0 = (android.view.accessibility.AccessibilityNodeInfo) r0
            com.tiangou.douxiaomi.ServiceUtils.setText(r0, r9)
            int r9 = r8.sleep
            long r6 = (long) r9
            java.lang.Thread.sleep(r6)
        L_0x007a:
            com.tiangou.douxiaomi.service.AccessService r9 = r8.service
            r0 = 4
            java.lang.String r0 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r0)
            java.util.List r9 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r9, (java.lang.String) r0)
            boolean r0 = com.tiangou.douxiaomi.Utils.isEmptyArray(r9)
            if (r0 == 0) goto L_0x0097
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x007a
        L_0x0097:
            java.lang.Object r9 = r9.get(r1)
            android.view.accessibility.AccessibilityNodeInfo r9 = (android.view.accessibility.AccessibilityNodeInfo) r9
            com.tiangou.douxiaomi.ServiceUtils.clickView(r9)
            int r9 = r8.longSleep
            long r6 = (long) r9
            java.lang.Thread.sleep(r6)
            com.tiangou.douxiaomi.service.AccessService r9 = r8.service
            java.lang.String r0 = "搜索结果为空"
            java.util.List r9 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r9, (java.lang.String) r0)
            boolean r9 = com.tiangou.douxiaomi.Utils.isEmptyArray(r9)
            if (r9 == 0) goto L_0x0107
            com.tiangou.douxiaomi.App r9 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r9 = r9.startRun
            boolean r9 = r9.booleanValue()
            if (r9 == 0) goto L_0x0107
            r9 = 0
        L_0x00c1:
            int r9 = r9 + r3
            com.tiangou.douxiaomi.service.AccessService r0 = r8.service
            r2 = 6
            java.lang.String r2 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r2)
            java.util.List r0 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r0, (java.lang.String) r2)
            android.view.accessibility.AccessibilityNodeInfo r2 = r8.getRecyclerView(r0)
            int r4 = r8.sleep
            long r6 = (long) r4
            java.lang.Thread.sleep(r6)
            boolean r0 = com.tiangou.douxiaomi.Utils.isEmptyArray(r0)
            if (r0 != 0) goto L_0x00f3
            if (r2 == 0) goto L_0x00f3
            int r0 = r2.getChildCount()
            if (r0 <= 0) goto L_0x00f3
            com.tiangou.douxiaomi.App r0 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r0 = r0.startRun
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x00f3
            if (r9 < r5) goto L_0x00c1
        L_0x00f3:
            if (r2 == 0) goto L_0x0107
            android.view.accessibility.AccessibilityNodeInfo r9 = r2.getChild(r1)
            com.tiangou.douxiaomi.ServiceUtils.clickView(r9)
            int r9 = r8.sleep
            long r0 = (long) r9
            java.lang.Thread.sleep(r0)
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r3)
            return r9
        L_0x0107:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r1)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BasePersonImpl.enterCustomUserPersonHome(java.lang.String):java.lang.Boolean");
    }

    public Boolean enterMyFriend() throws Exception {
        if (enterFindFriend().booleanValue() && App.getInstance().startRun.booleanValue()) {
            Thread.sleep((long) this.sleep);
            List<AccessibilityNodeInfo> findViewByContainsText = ServiceUtils.findViewByContainsText((AccessibilityService) this.service, "好友列表");
            if (!Utils.isEmptyArray(findViewByContainsText)) {
                ServiceUtils.clickView(findViewByContainsText.get(0));
                Thread.sleep((long) this.sleep);
                return true;
            }
        }
        return false;
    }

    public void changeUserTab(String str) {
        List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(94));
        if (!Utils.isEmptyArray(findViewByClassName)) {
            for (AccessibilityNodeInfo next : findViewByClassName) {
                List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = next.findAccessibilityNodeInfosByViewId(Jni.getSbObj(92));
                if (!Utils.isEmptyArray(findAccessibilityNodeInfosByViewId) && findAccessibilityNodeInfosByViewId.get(0).getText().toString().contains(str)) {
                    ServiceUtils.clickView(next);
                    return;
                }
            }
        }
    }

    public String getTitle() {
        for (AccessibilityNodeInfo next : ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(7))) {
            Rect rect = new Rect();
            next.getBoundsInScreen(rect);
            boolean z = true;
            Boolean valueOf = Boolean.valueOf(rect.top < Utils.getDisplayHeight(this.service) / 10);
            if (Math.abs(((rect.left + rect.right) / 2) - (Utils.getDisplayWidth(this.service) / 2)) >= 2) {
                z = false;
            }
            Boolean valueOf2 = Boolean.valueOf(z);
            if (valueOf.booleanValue() && valueOf2.booleanValue()) {
                return next.getText().toString();
            }
        }
        return null;
    }

    public Boolean enterUserFriend(int i) throws Exception {
        AccessibilityNodeInfo recyclerView;
        int i2 = 0;
        Boolean bool = false;
        if (!App.getInstance().startRun.booleanValue()) {
            return bool;
        }
        Log.e("qyh", "douyin==========验证用户主页");
        if (!isUserMainPage().booleanValue()) {
            Log.e("qyh", "douyin==========验证用户主页失败，继续下一步");
            return bool;
        }
        Thread.sleep((long) this.longSleep);
        Log.e("qyh", "douyin==========验证用户主页成功");
        List<AccessibilityNodeInfo> filterErrData = ServiceUtils.filterErrData(ServiceUtils.findViewByEqualsText(this.service, "获赞", Jni.getSbObj(7)));
        AccessibilityNodeInfo parent = getParent(filterErrData);
        if (parent != null) {
            ServiceUtils.clickView(parent.getChild(i == 1 ? 2 : 1));
        }
        ServiceUtils.recycleAccessibilityNodeInfo(filterErrData);
        Thread.sleep((long) this.longSleep);
        if (Utils.isEmptyArray(ServiceUtils.findViewByContentDescription(this.service, "修改主页背景图"))) {
            do {
                i2++;
                List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(6));
                if (!Utils.isEmptyArray(findViewByClassName) && (recyclerView = getRecyclerView(findViewByClassName)) != null && recyclerView.getParent().getParent().getChildCount() < 2) {
                    bool = true;
                }
                Thread.sleep((long) this.sleep);
                if (bool.booleanValue() || i2 >= 4 || !App.getInstance().startRun.booleanValue()) {
                }
                i2++;
                List<AccessibilityNodeInfo> findViewByClassName2 = ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(6));
                bool = true;
                Thread.sleep((long) this.sleep);
                break;
            } while (!App.getInstance().startRun.booleanValue());
            return true;
        }
        Log.e("qyh", "隐私账户，无法查看");
        return bool;
    }

    public AccessibilityNodeInfo getParent(List<AccessibilityNodeInfo> list) {
        for (AccessibilityNodeInfo parent : list) {
            AccessibilityNodeInfo parent2 = parent.getParent().getParent();
            Log.e("qyh", "parentchild =" + parent2.getChildCount() + "desc" + parent2);
            if (parent2.getClassName().equals(Jni.getSbObj(87)) && parent2.getChildCount() >= 3) {
                return parent2;
            }
        }
        return null;
    }

    public void solveRecyclerView(final String str) {
        try {
            RecyclerViewClickUtils recyclerViewClickUtils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public AccessibilityNodeInfo getParentInfo() {
                    return BasePersonImpl.this.getCommentRecy(ServiceUtils.findViewByClassName((AccessibilityService) BasePersonImpl.this.service, Jni.getSbObj(6)));
                }

                public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                    ArrayList arrayList = new ArrayList();
                    if (!accessibilityNodeInfo.getClassName().equals(Jni.getSbObj(7))) {
                        ServiceUtils.findViewByClassName(arrayList, accessibilityNodeInfo, Jni.getSbObj(7));
                    } else {
                        arrayList.add(accessibilityNodeInfo);
                    }
                    String charSequence = !Utils.isEmptyArray(arrayList) ? ((AccessibilityNodeInfo) arrayList.get(0)).getText().toString() : null;
                    if (!Utils.isEmptyArray(list)) {
                        if (list.size() % BasePersonImpl.this.getConfig().maxCount == 0) {
                            Thread.sleep((long) BasePersonImpl.this.getConfig().getSleepTime());
                        } else {
                            Thread.sleep((long) BasePersonImpl.this.getConfig().getSendInterval());
                        }
                    }
                    Log.e("qyh", "tag==" + charSequence);
                    if (BasePersonImpl.this.functionType != 1) {
                        if (!list.contains(charSequence)) {
                            ServiceUtils.clickView((AccessibilityNodeInfo) arrayList.get(0));
                            Thread.sleep((long) BasePersonImpl.this.sleep);
                            List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) BasePersonImpl.this.service, Jni.getSbObj(2));
                            if (!Utils.isEmptyArray(findViewByClassName) && findViewByClassName.size() > 1) {
                                accessibilityNodeInfo = findViewByClassName.get(1);
                            }
                        } else {
                            return new Object[]{null, null};
                        }
                    } else if (accessibilityNodeInfo == null || accessibilityNodeInfo.getChildCount() != 1 || !accessibilityNodeInfo.getChild(0).getClassName().equals(Jni.getSbObj(87))) {
                        accessibilityNodeInfo = (AccessibilityNodeInfo) arrayList.get(0);
                    } else {
                        return new Object[]{null, null};
                    }
                    return new Object[]{accessibilityNodeInfo, charSequence};
                }

                public void next() throws Exception {
                    BasePersonImpl.this.sendOrAttenUser(str);
                }

                public Boolean runNext(int i) throws Exception {
                    return BasePersonImpl.this.canNext(i);
                }
            }, getMaxCount());
            recyclerViewClickUtils.setService(this.service);
            recyclerViewClickUtils.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AccessibilityNodeInfo getCommentRecy(List<AccessibilityNodeInfo> list) {
        for (AccessibilityNodeInfo next : list) {
            Log.e("qyh", "info==" + next);
            Rect rect = new Rect();
            next.getBoundsInScreen(rect);
            if (rect.left >= 0 && rect.top > 10 && rect.top < Utils.getDisplayHeight(this.service) - 10 && next.isVisibleToUser()) {
                return next;
            }
        }
        return null;
    }
}
