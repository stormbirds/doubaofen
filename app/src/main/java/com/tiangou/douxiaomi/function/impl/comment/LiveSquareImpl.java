package com.tiangou.douxiaomi.function.impl.comment;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCommentImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.List;

public class LiveSquareImpl<R extends ConfigBean> extends BaseCommentImpl<R> {
    public LiveSquareImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            this.isPrepose = backToMain(0);
            Thread.sleep((long) this.sleep);
            AccessibilityNodeInfo findViewByFirstContainsContentDescription = ServiceUtils.findViewByFirstContainsContentDescription((AccessibilityService) this.service, "live");
            this.isPrepose = false;
            if (findViewByFirstContainsContentDescription != null) {
                ServiceUtils.clickView(findViewByFirstContainsContentDescription);
                Thread.sleep((long) (this.sleep + this.longSleep));
                List<AccessibilityNodeInfo> findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, "更多直播");
                if (!Utils.isEmptyArray(findViewByEqualsText)) {
                    ServiceUtils.clickView(findViewByEqualsText.get(0));
                    Thread.sleep((long) (this.sleep + this.longSleep));
                    this.isPrepose = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            live();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void live() {
        try {
            this.utils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public AccessibilityNodeInfo getParentInfo() {
                    AccessibilityNodeInfo access$000 = LiveSquareImpl.this.getLiveRecyclerView(ServiceUtils.findViewByClassName((AccessibilityService) LiveSquareImpl.this.service, Jni.getSbObj(6)));
                    if (access$000 == null || access$000.getChildCount() <= 0) {
                        return null;
                    }
                    return access$000;
                }

                public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                    AccessibilityNodeInfo parentInfo = getParentInfo();
                    int childCount = (parentInfo.getChildCount() - LiveSquareImpl.this.utils.items.size()) - 1;
                    AccessibilityNodeInfo child = (childCount < 0 || childCount >= parentInfo.getChildCount() - 1) ? null : parentInfo.getChild(childCount);
                    if (child != null) {
                        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = child.findAccessibilityNodeInfosByViewId(Jni.getSbObj(95));
                        if (!Utils.isEmptyArray(findAccessibilityNodeInfosByViewId)) {
                            if (!Utils.isEmptyArray(list)) {
                                LiveSquareImpl.this.runSleep(list.size());
                            }
                            return new Object[]{child, findAccessibilityNodeInfosByViewId.get(0).getText()};
                        }
                    }
                    return new Object[]{child, null};
                }

                public void next() throws Exception {
                    Thread.sleep((long) LiveSquareImpl.this.longSleep);
                    LiveSquareImpl.this.sendLiveComment();
                }

                public Boolean runNext(int i) throws Exception {
                    return LiveSquareImpl.this.canNext(i);
                }
            }, getMaxCount());
            this.utils.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public AccessibilityNodeInfo getLiveRecyclerView(List<AccessibilityNodeInfo> list) {
        for (AccessibilityNodeInfo next : list) {
            Rect rect = new Rect();
            next.getBoundsInScreen(rect);
            if (rect.left != 0 && rect.top > 10 && rect.right == Utils.getDisplayWidth(this.service)) {
                return next;
            }
        }
        return null;
    }
}
