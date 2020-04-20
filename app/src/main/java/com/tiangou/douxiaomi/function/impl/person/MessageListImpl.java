package com.tiangou.douxiaomi.function.impl.person;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BasePersonImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.RecyclerViewClickUtils;
import java.util.ArrayList;
import java.util.List;

public class MessageListImpl<R extends ConfigBean> extends BasePersonImpl<R> {
    String id = "com.ss.android.ugc.aweme:id/dnk";

    public MessageListImpl(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        try {
            this.isPrepose = backToMain(3);
            Thread.sleep((long) this.longSleep);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void on() {
        solveMessage();
    }

    private void solveMessage() {
        this.utils = new RecyclerViewClickUtils(new RecyclerViewClickUtils.RunListener() {
            public AccessibilityNodeInfo getParentInfo() {
                AccessibilityNodeInfo recyclerView = MessageListImpl.this.getRecyclerView(ServiceUtils.findViewByClassName((AccessibilityService) MessageListImpl.this.service, Jni.getSbObj(6)));
                return recyclerView == null ? MessageListImpl.this.service.findViewByID(MessageListImpl.this.id) : recyclerView;
            }

            public Object[] solveTag(AccessibilityNodeInfo accessibilityNodeInfo, List<String> list) throws Exception {
                Object[] access$000 = MessageListImpl.this.getNextChild(list);
                if (access$000 != null && !Utils.isEmptyArray(list)) {
                    MessageListImpl.this.runSleep(list.size());
                }
                return access$000;
            }

            public void next() throws Exception {
                MessageListImpl.this.editAndBack();
            }

            public Boolean runNext(int i) throws Exception {
                return MessageListImpl.this.canNext(i);
            }
        }, getMaxCount());
        try {
            this.utils.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public Object[] getNextChild(List<String> list) {
        new ArrayList();
        AccessibilityNodeInfo recyclerView = getRecyclerView(ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(6)));
        if (recyclerView == null) {
            recyclerView = this.service.findViewByID(this.id);
        }
        if (recyclerView == null) {
            return null;
        }
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            if (recyclerView.getChild(i).isClickable()) {
                ArrayList arrayList = new ArrayList();
                ServiceUtils.findViewByClassName(arrayList, recyclerView.getChild(i), Jni.getSbObj(7));
                if (arrayList.size() > 2 && !list.contains(((AccessibilityNodeInfo) arrayList.get(0)).getText())) {
                    return new Object[]{recyclerView.getChild(i), ((AccessibilityNodeInfo) arrayList.get(0)).getText()};
                }
            }
        }
        return null;
    }
}
