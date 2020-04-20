package com.tiangou.douxiaomi.function.impl.praise;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCulitivateImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.List;

public class CommenterPraiseImpl<R extends ConfigBean> extends BaseCulitivateImpl<R> {
    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public CommenterPraiseImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveCommenter();
    }

    private void solveCommenter() {
        List<AccessibilityNodeInfo> findViewByClassName = ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(6));
        List<AccessibilityNodeInfo> findViewByClassName2 = getCommentRecy(findViewByClassName) != null ? ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(3)) : null;
        if (Utils.isEmptyArray(findViewByClassName) || Utils.isEmptyArray(findViewByClassName2)) {
            Log.e("qyh", "列表0");
        } else {
            solveRecyclerViewPraise(findViewByClassName2.get(0).getText().toString());
        }
    }

    public void solveRecyclerViewPraise(String str) {
        try {
            RecyclerViewClickUtils recyclerViewClickUtils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
                public void next() throws Exception {
                }

                public AccessibilityNodeInfo getParentInfo() {
                    return CommenterPraiseImpl.this.getCommentRecy(ServiceUtils.findViewByClassName((AccessibilityService) CommenterPraiseImpl.this.service, Jni.getSbObj(6)));
                }

                public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                    AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(0);
                    if (child != null && child.getClassName().equals("android.view.ViewGroup") && !child.getChild(child.getChildCount() - 1).isSelected()) {
                        List<AccessibilityNodeInfo> findViewByClassNameInGroup = ServiceUtils.findViewByClassNameInGroup(child, "android.view.View");
                        if (!Utils.isEmptyArray(findViewByClassNameInGroup)) {
                            return new Object[]{findViewByClassNameInGroup.get(0), list.size() + ""};
                        }
                    }
                    return new Object[]{null, "0"};
                }

                public Boolean runNext(int i) throws Exception {
                    return CommenterPraiseImpl.this.canNext(i);
                }
            }, getMaxCount());
            recyclerViewClickUtils.setService(this.service);
            recyclerViewClickUtils.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
