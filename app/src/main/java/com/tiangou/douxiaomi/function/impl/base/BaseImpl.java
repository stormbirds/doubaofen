package com.tiangou.douxiaomi.function.impl.base;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.event.SolveFinishEvent;
import com.tiangou.douxiaomi.function.TxtType;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.AccessService;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import com.tiangou.douxiaomi.utils.DataConfig;
import com.tiangou.douxiaomi.utils.GBData;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseImpl<R> {
    public Boolean changeData = true;
    public int functionType = 0;
    public Boolean isPrepose = false;
    public int longSleep = 2000;
    public List<String> names;
    protected R params;
    public int runNum = 0;
    public AccessService service = null;
    public int sleep = 1000;
    public int startTime = 0;
    public TxtType txtType = TxtType.TXT;
    public RecyclerViewClickUtils utils;

    /* access modifiers changed from: protected */
    public abstract void on();

    /* access modifiers changed from: protected */
    public abstract void prepose();

    public BaseImpl(R r) {
        this.params = r;
        this.names = new ArrayList();
    }

    public DataConfig getConfig() {
        return App.getInstance().config;
    }

    public R getParams() {
        return this.params;
    }

    public void setParams(R r) {
        this.params = r;
    }

    public void start() {
        if (App.getInstance().getService() == null) {
            Log.e("qyh", "service=null");
            finish();
            return;
        }
        this.startTime = Utils.getTime();
        new Thread() {
            public void run() {
                super.run();
                BaseImpl.this.service = App.getInstance().getService();
                if (BaseImpl.this.params != null) {
                    BaseImpl.this.prepose();
                    if (BaseImpl.this.isPrepose.booleanValue()) {
                        BaseImpl.this.on();
                        BaseImpl.this.finish();
                        return;
                    }
                    Log.e("qyh", "联动运行失败");
                    return;
                }
                Log.e("qyh", "开始运行单功能");
                BaseImpl.this.on();
                BaseImpl.this.finish();
            }
        }.start();
    }

    public Boolean canNext(int i) {
        ConfigBean configBean = (ConfigBean) getParams();
        boolean z = false;
        if (configBean != null && configBean.type == 0 && Utils.getTime() >= this.startTime + configBean.count) {
            return false;
        }
        int maxCount = getMaxCount();
        Log.e("qyh", "最大数量" + maxCount);
        if (i < maxCount) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public int getMaxCount() {
        int i = this.runNum;
        if (i != 0) {
            return i;
        }
        R r = this.params;
        if (r == null) {
            return getConfig().maxCount * getConfig().runNum;
        }
        ConfigBean configBean = (ConfigBean) r;
        if (configBean.type == 1) {
            return configBean.count;
        }
        return Integer.MAX_VALUE;
    }

    public void runSleep(int i) throws InterruptedException {
        if (this.params != null) {
            Thread.sleep((long) getConfig().getSendInterval());
        } else if (i % getConfig().maxCount == 0) {
            Thread.sleep((long) getConfig().getSleepTime());
        } else {
            Thread.sleep((long) getConfig().getSendInterval());
        }
    }

    public void finish() {
        EventBus.getDefault().post(new SolveFinishEvent());
    }

    public AccessibilityNodeInfo getRecyclerView(List<AccessibilityNodeInfo> list) {
        if (Utils.isEmptyArray(list)) {
            return null;
        }
        for (AccessibilityNodeInfo next : list) {
            Rect rect = new Rect();
            next.getBoundsInScreen(rect);
            if (rect.left != rect.right && next.isVisibleToUser()) {
                return next;
            }
        }
        return null;
    }

    public Boolean isUserMainPage() {
        List<AccessibilityNodeInfo> list = null;
        int i = 0;
        while (App.getInstance().startRun.booleanValue() && i < 2) {
            try {
                Thread.sleep((long) this.sleep);
                i++;
                list = ServiceUtils.filterErrData(ServiceUtils.findViewByIdList(this.service, Jni.getSbObj(92)));
                if (!Utils.isEmptyArray(list)) {
                    List<AccessibilityNodeInfo> findViewByContainsText = ServiceUtils.findViewByContainsText((AccessibilityService) this.service, "抖音号");
                    List<AccessibilityNodeInfo> findViewByContentDescription = ServiceUtils.findViewByContentDescription(this.service, "更多");
                    for (AccessibilityNodeInfo isVisibleToUser : list) {
                        if (isVisibleToUser.isVisibleToUser() && !Utils.isEmptyArray(findViewByContainsText) && !Utils.isEmptyArray(findViewByContentDescription)) {
                            return true;
                        }
                    }
                    continue;
                } else {
                    Log.e("qyh", "test==null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        ServiceUtils.recycleAccessibilityNodeInfo(list);
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void backToList(java.lang.String r6) throws java.lang.Exception {
        /*
            r5 = this;
            r0 = 0
            r1 = 0
        L_0x0002:
            r2 = 1
            int r1 = r1 + r2
            com.tiangou.douxiaomi.App r3 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r3 = r3.startRun
            boolean r3 = r3.booleanValue()
            if (r3 == 0) goto L_0x005a
            com.tiangou.douxiaomi.service.AccessService r0 = r5.service
            r0.performGlobalAction(r2)
            int r0 = r5.sleep
            long r2 = (long) r0
            java.lang.Thread.sleep(r2)
            com.tiangou.douxiaomi.service.AccessService r0 = r5.service
            r2 = 7
            java.lang.String r2 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r2)
            java.util.List r0 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText(r0, r6, r2)
            boolean r2 = com.tiangou.douxiaomi.Utils.isEmptyArray(r0)
            if (r2 == 0) goto L_0x005a
            com.tiangou.douxiaomi.service.AccessService r2 = r5.service
            r3 = 3
            java.lang.String r3 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r2 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r2, (java.lang.String) r3)
            java.util.Iterator r2 = r2.iterator()
        L_0x003b:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x005a
            java.lang.Object r3 = r2.next()
            android.view.accessibility.AccessibilityNodeInfo r3 = (android.view.accessibility.AccessibilityNodeInfo) r3
            java.lang.CharSequence r4 = r3.getText()
            boolean r4 = r4.equals(r6)
            if (r4 == 0) goto L_0x003b
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r0.add(r3)
            goto L_0x003b
        L_0x005a:
            boolean r2 = com.tiangou.douxiaomi.Utils.isEmptyArray(r0)
            if (r2 == 0) goto L_0x0070
            com.tiangou.douxiaomi.App r2 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r2 = r2.startRun
            boolean r2 = r2.booleanValue()
            if (r2 == 0) goto L_0x0070
            r2 = 8
            if (r1 < r2) goto L_0x0002
        L_0x0070:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseImpl.backToList(java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0012  */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void backToId(java.lang.String r6) throws java.lang.Exception {
        /*
            r5 = this;
            r0 = 0
        L_0x0001:
            r1 = 1
            int r0 = r0 + r1
            com.tiangou.douxiaomi.service.AccessService r2 = r5.service
            android.view.accessibility.AccessibilityNodeInfo r2 = com.tiangou.douxiaomi.ServiceUtils.findViewById(r2, r6)
            if (r2 == 0) goto L_0x0014
            boolean r3 = r2.isVisibleToUser()
            if (r3 != 0) goto L_0x0012
            goto L_0x0014
        L_0x0012:
            r2 = 0
            goto L_0x001f
        L_0x0014:
            com.tiangou.douxiaomi.service.AccessService r3 = r5.service
            r3.performGlobalAction(r1)
            int r1 = r5.sleep
            long r3 = (long) r1
            java.lang.Thread.sleep(r3)
        L_0x001f:
            if (r2 == 0) goto L_0x0030
            com.tiangou.douxiaomi.App r1 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r1 = r1.startRun
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0030
            r1 = 5
            if (r0 < r1) goto L_0x0001
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseImpl.backToId(java.lang.String):void");
    }

    public void editAndBack() throws Exception {
        if (App.getInstance().startRun.booleanValue()) {
            Thread.sleep((long) this.longSleep);
            int i = AnonymousClass3.$SwitchMap$com$tiangou$douxiaomi$function$TxtType[this.txtType.ordinal()];
            if (i == 1) {
                sendText();
            } else if (i == 2) {
                sendImg();
            } else if (i == 3) {
                sendImg();
                sendText();
            }
            back();
            Thread.sleep((long) this.sleep);
        }
    }

    /* renamed from: com.tiangou.douxiaomi.function.impl.base.BaseImpl$3  reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$tiangou$douxiaomi$function$TxtType = new int[TxtType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                com.tiangou.douxiaomi.function.TxtType[] r0 = com.tiangou.douxiaomi.function.TxtType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$tiangou$douxiaomi$function$TxtType = r0
                int[] r0 = $SwitchMap$com$tiangou$douxiaomi$function$TxtType     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.tiangou.douxiaomi.function.TxtType r1 = com.tiangou.douxiaomi.function.TxtType.TXT     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$tiangou$douxiaomi$function$TxtType     // Catch:{ NoSuchFieldError -> 0x001f }
                com.tiangou.douxiaomi.function.TxtType r1 = com.tiangou.douxiaomi.function.TxtType.IMG     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$tiangou$douxiaomi$function$TxtType     // Catch:{ NoSuchFieldError -> 0x002a }
                com.tiangou.douxiaomi.function.TxtType r1 = com.tiangou.douxiaomi.function.TxtType.TXT_IMG     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseImpl.AnonymousClass3.<clinit>():void");
        }
    }

    private void sendImg() throws Exception {
        List<AccessibilityNodeInfo> findViewByContentDescription = ServiceUtils.findViewByContentDescription(this.service, Jni.getSbObj(93));
        if (!Utils.isEmptyArray(findViewByContentDescription)) {
            ServiceUtils.clickView(findViewByContentDescription.get(0));
            Thread.sleep((long) this.sleep);
            AccessibilityNodeInfo findViewByFirstContainsContentDescription = ServiceUtils.findViewByFirstContainsContentDescription((AccessibilityService) this.service, "照片");
            if (findViewByFirstContainsContentDescription != null) {
                findViewByContentDescription = ServiceUtils.findViewByClassNameInGroup(findViewByFirstContainsContentDescription.getParent(), Jni.getSbObj(2));
                if (!Utils.isEmptyArray(findViewByContentDescription) && findViewByContentDescription.size() == 2) {
                    ServiceUtils.clickView(findViewByContentDescription.get(1));
                    Thread.sleep((long) this.sleep);
                    findViewByContentDescription = ServiceUtils.findViewByContainsText((AccessibilityService) this.service, "发送(1)");
                    if (!Utils.isEmptyArray(findViewByContentDescription)) {
                        ServiceUtils.clickView(findViewByContentDescription.get(0));
                    }
                }
            }
        }
        ServiceUtils.recycleAccessibilityNodeInfo(findViewByContentDescription);
        Thread.sleep((long) this.longSleep);
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendText() throws java.lang.Exception {
        /*
            r11 = this;
            r0 = 0
            r1 = 0
        L_0x0002:
            com.tiangou.douxiaomi.service.AccessService r2 = r11.service
            r3 = 3
            java.lang.String r4 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r2 = com.tiangou.douxiaomi.ServiceUtils.findViewByClassName((android.accessibilityservice.AccessibilityService) r2, (java.lang.String) r4)
            int r4 = r11.sleep
            long r4 = (long) r4
            java.lang.Thread.sleep(r4)
            r4 = 1
            int r1 = r1 + r4
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r5 = r5.startRun
            boolean r5 = r5.booleanValue()
            r6 = 5
            if (r5 == 0) goto L_0x002a
            int r5 = r2.size()
            if (r5 >= r4) goto L_0x002a
            if (r1 < r6) goto L_0x0002
        L_0x002a:
            if (r1 >= r6) goto L_0x0111
            r1 = 0
        L_0x002d:
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            com.tiangou.douxiaomi.utils.DataConfig r5 = r5.config
            int r5 = r5.messageCount
            if (r1 >= r5) goto L_0x010e
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r5 = r5.startRun
            boolean r5 = r5.booleanValue()
            if (r5 == 0) goto L_0x010a
            boolean r5 = com.tiangou.douxiaomi.Utils.isEmptyArray(r2)
            if (r5 != 0) goto L_0x010a
            java.lang.Object r5 = r2.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r5 = (android.view.accessibility.AccessibilityNodeInfo) r5
            com.tiangou.douxiaomi.App r7 = com.tiangou.douxiaomi.App.getInstance()
            com.tiangou.douxiaomi.utils.DataConfig r7 = r7.config
            java.lang.String r7 = r7.getMessage(r1)
            com.tiangou.douxiaomi.ServiceUtils.setText(r5, r7)
            int r5 = r11.sleep
            long r7 = (long) r5
            java.lang.Thread.sleep(r7)
            r5 = 0
        L_0x0063:
            int r7 = r11.sleep
            int r7 = r7 / 2
            long r7 = (long) r7
            java.lang.Thread.sleep(r7)
            com.tiangou.douxiaomi.service.AccessService r7 = r11.service
            r8 = 83
            java.lang.String r8 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r8)
            java.util.List r7 = com.tiangou.douxiaomi.ServiceUtils.findViewByContentDescription(r7, r8)
            int r5 = r5 + r4
            boolean r8 = com.tiangou.douxiaomi.Utils.isEmptyArray(r7)
            if (r8 == 0) goto L_0x008c
            if (r5 >= r6) goto L_0x008c
            com.tiangou.douxiaomi.App r8 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r8 = r8.startRun
            boolean r8 = r8.booleanValue()
            if (r8 != 0) goto L_0x0063
        L_0x008c:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r8 = "final img size=="
            r5.append(r8)
            int r8 = r7.size()
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            java.lang.String r8 = "qyh"
            android.util.Log.e(r8, r5)
            boolean r5 = com.tiangou.douxiaomi.Utils.isEmptyArray(r7)
            if (r5 != 0) goto L_0x00c2
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r5 = r5.startRun
            boolean r5 = r5.booleanValue()
            if (r5 == 0) goto L_0x00c2
            java.lang.Object r5 = r7.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r5 = (android.view.accessibility.AccessibilityNodeInfo) r5
            com.tiangou.douxiaomi.ServiceUtils.clickView(r5)
            goto L_0x0101
        L_0x00c2:
            java.lang.Object r5 = r2.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r5 = (android.view.accessibility.AccessibilityNodeInfo) r5
            android.view.accessibility.AccessibilityNodeInfo r5 = r5.getParent()
            android.view.accessibility.AccessibilityNodeInfo r5 = r5.getParent()
            if (r5 == 0) goto L_0x0101
            java.lang.CharSequence r9 = r5.getClassName()
            java.lang.String r10 = "android.widget.LinearLayout"
            boolean r9 = r9.equals(r10)
            if (r9 == 0) goto L_0x0101
            int r9 = r5.getChildCount()
            r10 = 4
            if (r9 != r10) goto L_0x0101
            android.view.accessibility.AccessibilityNodeInfo r9 = r5.getChild(r3)
            boolean r9 = com.tiangou.douxiaomi.ServiceUtils.clickView(r9)
            if (r9 != 0) goto L_0x0101
            java.lang.String r9 = "send should be touch"
            android.util.Log.e(r8, r9)
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 24
            if (r8 < r9) goto L_0x0101
            android.view.accessibility.AccessibilityNodeInfo r5 = r5.getChild(r3)
            r11.clickView(r5)
        L_0x0101:
            com.tiangou.douxiaomi.ServiceUtils.recycleAccessibilityNodeInfo(r7)
            int r5 = r11.longSleep
            long r7 = (long) r5
            java.lang.Thread.sleep(r7)
        L_0x010a:
            int r1 = r1 + 1
            goto L_0x002d
        L_0x010e:
            com.tiangou.douxiaomi.ServiceUtils.recycleAccessibilityNodeInfo(r2)
        L_0x0111:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseImpl.sendText():void");
    }

    public void back() throws Exception {
        if (App.getInstance().startRun.booleanValue()) {
            Thread.sleep((long) this.sleep);
            this.service.performGlobalAction(1);
        }
    }

    public int distingSex() {
        List<AccessibilityNodeInfo> filterErrData = ServiceUtils.filterErrData(ServiceUtils.findViewByEqualsText(this.service, "获赞", Jni.getSbObj(7)));
        AccessibilityNodeInfo parent = !Utils.isEmptyArray(filterErrData) ? filterErrData.get(0).getParent().getParent().getParent() : null;
        if (parent != null && parent.getChildCount() > 1) {
            parent = parent.getChild(parent.getChild(parent.getChildCount() - 1).getClassName().equals(Jni.getSbObj(37)) ? parent.getChildCount() - 3 : parent.getChildCount() - 2);
        }
        if (parent == null || parent.getChildCount() <= 0) {
            return 2;
        }
        AccessibilityNodeInfo child = parent.getChild(0);
        if (!child.getClassName().equals(Jni.getSbObj(7))) {
            return 2;
        }
        if (child.getText().equals("男")) {
            return 0;
        }
        if (child.getText().equals("女")) {
            return 1;
        }
        if (!child.getText().toString().contains("岁")) {
            return 2;
        }
        Rect rect = new Rect();
        child.getBoundsInScreen(rect);
        int i = (rect.bottom - ((rect.bottom - rect.top) / 2)) - 2;
        for (int i2 = 5; i2 < 20; i2++) {
            int color = GBData.getColor(rect.left + Utils.dp2Px(this.service, (float) (i2 + 2)), i);
            if (Boolean.valueOf(GBData.ColorComp(color, 2602393)).booleanValue()) {
                return 0;
            }
            if (Boolean.valueOf(GBData.ColorComp(color, 16408398)).booleanValue()) {
                return 1;
            }
        }
        return 2;
    }

    public String getRectDesc(AccessibilityNodeInfo accessibilityNodeInfo) {
        String str = (("可见性:" + accessibilityNodeInfo.isVisibleToUser()) + "className:" + accessibilityNodeInfo.getClassName()) + "title:" + accessibilityNodeInfo.getText();
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        return str + "坐标:上" + rect.top + " 下" + rect.bottom + " 左" + rect.left + " 右" + rect.right;
    }

    public void upTouch() throws Exception {
        Thread.sleep((long) this.sleep);
        Path path = new Path();
        path.moveTo((float) (Utils.getDisplayWidth(this.service) / 2), (float) ((Utils.getDisplayHeight(this.service) / 5) * 4));
        path.lineTo((float) (Utils.getDisplayWidth(this.service) / 2), (float) (Utils.getDisplayHeight(this.service) / 5));
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 10, 400)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep((long) this.longSleep);
    }

    public void leftTouch() throws Exception {
        Thread.sleep((long) this.sleep);
        Path path = new Path();
        path.moveTo((float) ((Utils.getDisplayWidth(this.service) / 5) * 4), (float) ((Utils.getDisplayHeight(this.service) / 4) * 1));
        path.lineTo(0.0f, (float) ((Utils.getDisplayHeight(this.service) / 5) * 2));
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 10, 400)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep((long) this.longSleep);
    }

    public void doubleClickView(int i, int i2) throws Exception {
        Thread.sleep((long) this.sleep);
        final Path path = new Path();
        path.moveTo((float) i, (float) i2);
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 0, 100)).build(), new AccessibilityService.GestureResultCallback() {
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                BaseImpl.this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 150, 100)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
            }
        }, (Handler) null);
    }

    public void upTouchQuick() throws Exception {
        Thread.sleep((long) this.sleep);
        Path path = new Path();
        path.moveTo((float) (Utils.getDisplayWidth(this.service) / 2), (float) ((Utils.getDisplayHeight(this.service) / 5) * 4));
        path.lineTo((float) (Utils.getDisplayWidth(this.service) / 2), (float) (Utils.getDisplayHeight(this.service) / 4));
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 10, 100)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep((long) this.longSleep);
    }

    public void clickView(AccessibilityNodeInfo accessibilityNodeInfo) throws Exception {
        Thread.sleep((long) this.sleep);
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
        path.moveTo((float) ((rect.left + rect.right) / 2), (float) ((rect.top + rect.bottom) / 2));
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 10, 350)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep((long) this.longSleep);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Boolean backToMain(int r8) throws java.lang.Exception {
        /*
            r7 = this;
            r0 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
            java.lang.Boolean.valueOf(r0)
            r2 = 0
        L_0x0009:
            r3 = 1
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r3)
            r5 = 1500(0x5dc, double:7.41E-321)
            java.lang.Thread.sleep(r5)
            com.tiangou.douxiaomi.service.AccessService r5 = r7.service
            java.lang.String r6 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r3)
            java.util.List r5 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r5, (java.lang.String) r6)
            boolean r6 = com.tiangou.douxiaomi.Utils.isEmptyArray(r5)
            if (r6 != 0) goto L_0x002f
            java.lang.Object r5 = r5.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r5 = (android.view.accessibility.AccessibilityNodeInfo) r5
            boolean r5 = r5.isVisibleToUser()
            if (r5 != 0) goto L_0x0030
        L_0x002f:
            r4 = r1
        L_0x0030:
            com.tiangou.douxiaomi.service.AccessService r5 = r7.service
            r6 = 18
            java.lang.String r6 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r6)
            java.util.List r5 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r5, (java.lang.String) r6)
            boolean r6 = com.tiangou.douxiaomi.Utils.isEmptyArray(r5)
            if (r6 != 0) goto L_0x004e
            java.lang.Object r5 = r5.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r5 = (android.view.accessibility.AccessibilityNodeInfo) r5
            boolean r5 = r5.isVisibleToUser()
            if (r5 != 0) goto L_0x004f
        L_0x004e:
            r4 = r1
        L_0x004f:
            com.tiangou.douxiaomi.service.AccessService r5 = r7.service
            r6 = 17
            java.lang.String r6 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r6)
            java.util.List r5 = com.tiangou.douxiaomi.ServiceUtils.findViewByEqualsText((android.accessibilityservice.AccessibilityService) r5, (java.lang.String) r6)
            boolean r6 = com.tiangou.douxiaomi.Utils.isEmptyArray(r5)
            if (r6 != 0) goto L_0x006d
            java.lang.Object r5 = r5.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r5 = (android.view.accessibility.AccessibilityNodeInfo) r5
            boolean r5 = r5.isVisibleToUser()
            if (r5 != 0) goto L_0x006e
        L_0x006d:
            r4 = r1
        L_0x006e:
            boolean r5 = r4.booleanValue()
            if (r5 != 0) goto L_0x0079
            com.tiangou.douxiaomi.service.AccessService r5 = r7.service
            r5.performGlobalAction(r3)
        L_0x0079:
            int r2 = r2 + r3
            boolean r5 = r4.booleanValue()
            if (r5 != 0) goto L_0x0090
            com.tiangou.douxiaomi.App r5 = com.tiangou.douxiaomi.App.getInstance()
            java.lang.Boolean r5 = r5.startRun
            boolean r5 = r5.booleanValue()
            if (r5 == 0) goto L_0x0090
            r5 = 12
            if (r2 < r5) goto L_0x0009
        L_0x0090:
            boolean r2 = r4.booleanValue()
            if (r2 == 0) goto L_0x00ed
            com.tiangou.douxiaomi.service.AccessService r2 = r7.service
            java.lang.String r4 = "拍摄，按钮"
            java.util.List r2 = com.tiangou.douxiaomi.ServiceUtils.findViewByContentDescription(r2, r4)
            boolean r4 = com.tiangou.douxiaomi.Utils.isEmptyArray(r2)
            if (r4 != 0) goto L_0x00e6
            java.lang.Object r0 = r2.get(r0)
            android.view.accessibility.AccessibilityNodeInfo r0 = (android.view.accessibility.AccessibilityNodeInfo) r0
            android.view.accessibility.AccessibilityNodeInfo r0 = r0.getParent()
            android.view.accessibility.AccessibilityNodeInfo r0 = r0.getParent()
            android.view.accessibility.AccessibilityNodeInfo r0 = r0.getParent()
            android.view.accessibility.AccessibilityNodeInfo r0 = r0.getParent()
            java.lang.CharSequence r2 = r0.getClassName()
            r4 = 87
            java.lang.String r4 = com.tiangou.douxiaomi.jni.Jni.getSbObj(r4)
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x00ed
            android.view.accessibility.AccessibilityNodeInfo r1 = r0.getChild(r8)
            boolean r1 = r1.isSelected()
            if (r1 != 0) goto L_0x00e1
            android.view.accessibility.AccessibilityNodeInfo r8 = r0.getChild(r8)
            com.tiangou.douxiaomi.ServiceUtils.clickView(r8)
            int r8 = r7.sleep
            long r0 = (long) r8
            java.lang.Thread.sleep(r0)
        L_0x00e1:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r3)
            return r8
        L_0x00e6:
            java.lang.String r8 = "qyh"
            java.lang.String r0 = "拍摄标识没有找到"
            android.util.Log.e(r8, r0)
        L_0x00ed:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.function.impl.base.BaseImpl.backToMain(int):java.lang.Boolean");
    }

    public void clickView(int i, int i2) throws Exception {
        Thread.sleep((long) this.sleep);
        Path path = new Path();
        path.moveTo((float) i, (float) i2);
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 10, 350)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep((long) this.longSleep);
    }
}
