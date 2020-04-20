package com.tiangou.douxiaomi.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.Utils;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewClickUtils {
    int curromMax;
    public List<AccessibilityNodeInfo> items;
    Boolean next;
    public AccessibilityNodeInfo parent;
    RunListener runListener;
    public AccessibilityService service;
    String tag;
    List<String> tags;

    public interface RunListener {
        AccessibilityNodeInfo getParentInfo();

        void next() throws Exception;

        Boolean runNext(int i) throws Exception;

        Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception;
    }

    public void setService(AccessibilityService accessibilityService) {
        this.service = accessibilityService;
    }

    public RecyclerViewClickUtils(RunListener runListener2, int i) {
        this.parent = null;
        this.items = null;
        this.tags = new ArrayList();
        this.curromMax = 1;
        this.next = true;
        this.items = null;
        this.curromMax = i;
        this.runListener = runListener2;
        this.parent = runListener2.getParentInfo();
    }

    public void start() throws Exception {
        if (App.getInstance().startRun.booleanValue() && this.tags.size() < this.curromMax && this.parent != null) {
            this.next = true;
            if (this.items != null) {
                this.next = Boolean.valueOf(this.parent.performAction(4096));
                if (!this.next.booleanValue()) {
                    Thread.sleep(500);
                    this.parent = this.runListener.getParentInfo();
                    this.next = Boolean.valueOf(this.parent.performAction(4096));
                }
                Thread.sleep(1500);
            }
            this.parent = this.runListener.getParentInfo();
            this.items = getChildParent(this.parent);
            Log.e("qyh", "tag==start==parent count==" + this.items.size());
            if (Utils.isEmptyArray(this.items) || !this.next.booleanValue()) {
                if (!this.runListener.runNext(this.tags.size()).booleanValue()) {
                }
            } else {
                doBatch();
            }
        }
    }

    public RunListener getRunListener() {
        return this.runListener;
    }

    public void solveError() {
        this.curromMax++;
    }

    private void doBatch() throws Exception {
        AccessibilityNodeInfo accessibilityNodeInfo;
        if (Utils.isEmptyArray(this.items) || !App.getInstance().startRun.booleanValue() || this.tags.size() >= this.curromMax) {
            start();
            return;
        }
        boolean z = false;
        AccessibilityNodeInfo remove = this.items.remove(0);
        if (remove != null) {
            Object[] solveTag = this.runListener.solveTag(remove, this.tags);
            if (solveTag != null) {
                this.tag = (String) solveTag[1];
            } else {
                this.tag = null;
            }
            this.next = Boolean.valueOf(!TextUtils.isEmpty(this.tag) && !this.tags.contains(this.tag));
            if (this.next.booleanValue() && (accessibilityNodeInfo = (AccessibilityNodeInfo) solveTag[0]) != null) {
                if (accessibilityNodeInfo.isClickable()) {
                    if (this.next.booleanValue() && accessibilityNodeInfo.performAction(16)) {
                        z = true;
                    }
                    this.next = Boolean.valueOf(z);
                } else if (Build.VERSION.SDK_INT < 24) {
                    return;
                } else {
                    if (accessibilityNodeInfo.isVisibleToUser()) {
                        clickView(accessibilityNodeInfo);
                    } else {
                        this.next = false;
                    }
                }
            }
            if (this.next.booleanValue()) {
                String str = this.tag;
                if (str != null) {
                    this.tags.add(str);
                }
                Thread.sleep(1000);
                this.runListener.next();
                Thread.sleep((long) App.getInstance().config.getSendInterval());
            }
        }
        if (this.runListener.runNext(this.tags.size()).booleanValue()) {
            doBatch();
        }
    }

    private void clickView(AccessibilityNodeInfo accessibilityNodeInfo) throws Exception {
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
        path.moveTo((float) ((rect.left + rect.right) / 2), (float) ((rect.top + rect.bottom) / 2));
        this.service.dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription(path, 10, 350)).build(), (AccessibilityService.GestureResultCallback) null, (Handler) null);
        Thread.sleep(1000);
    }

    private List<AccessibilityNodeInfo> getChildParent(AccessibilityNodeInfo accessibilityNodeInfo) {
        ArrayList arrayList = new ArrayList();
        if (accessibilityNodeInfo != null && accessibilityNodeInfo.getChildCount() > 0) {
            arrayList = new ArrayList();
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                if (accessibilityNodeInfo.getChild(i) != null) {
                    arrayList.add(accessibilityNodeInfo.getChild(i));
                }
            }
        }
        return arrayList;
    }
}
