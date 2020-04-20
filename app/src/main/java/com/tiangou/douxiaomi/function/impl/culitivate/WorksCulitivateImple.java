package com.tiangou.douxiaomi.function.impl.culitivate;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.ServiceUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.bean.ConfigBean;
import com.tiangou.douxiaomi.function.impl.base.BaseCulitivateImpl;
import com.tiangou.douxiaomi.jni.Jni;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorksCulitivateImple<R extends ConfigBean> extends BaseCulitivateImpl<R> {
    public List<Integer> attenList;
    public List<Integer> commentList;
    public List<Integer> forwardList;
    public List<Integer> praiseList;

    /* access modifiers changed from: protected */
    public void prepose() {
    }

    public WorksCulitivateImple(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            randomList();
            if (Build.VERSION.SDK_INT >= 24) {
                culitivateLike();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void randomList() {
        this.commentList = new ArrayList();
        if (App.getInstance().config.yangComment > App.getInstance().config.yangTotal) {
            App.getInstance().config.yangComment = App.getInstance().config.yangTotal;
        }
        if (App.getInstance().config.yangPraise > App.getInstance().config.yangTotal) {
            App.getInstance().config.yangPraise = App.getInstance().config.yangTotal;
        }
        if (App.getInstance().config.yangAtten > App.getInstance().config.yangTotal) {
            App.getInstance().config.yangAtten = App.getInstance().config.yangTotal;
        }
        if (App.getInstance().config.yangForward > App.getInstance().config.yangTotal) {
            App.getInstance().config.yangForward = App.getInstance().config.yangTotal;
        }
        while (this.commentList.size() < App.getInstance().config.yangComment) {
            int nextInt = new Random().nextInt(App.getInstance().config.yangTotal);
            if (!this.commentList.contains(Integer.valueOf(nextInt))) {
                this.commentList.add(Integer.valueOf(nextInt));
            }
        }
        Log.e("qyh", "评论随机数==" + this.commentList.toString());
        this.praiseList = new ArrayList();
        while (this.praiseList.size() < App.getInstance().config.yangPraise) {
            int nextInt2 = new Random().nextInt(App.getInstance().config.yangTotal);
            if (!this.praiseList.contains(Integer.valueOf(nextInt2))) {
                this.praiseList.add(Integer.valueOf(nextInt2));
            }
        }
        Log.e("qyh", "点赞随机数==" + this.praiseList.toString());
        this.attenList = new ArrayList();
        while (this.attenList.size() < App.getInstance().config.yangAtten) {
            int nextInt3 = new Random().nextInt(App.getInstance().config.yangTotal);
            if (!this.attenList.contains(Integer.valueOf(nextInt3))) {
                this.attenList.add(Integer.valueOf(nextInt3));
            }
        }
        Log.e("qyh", "关注随机数==" + this.attenList.toString());
        this.forwardList = new ArrayList();
        while (this.forwardList.size() < App.getInstance().config.yangForward) {
            int nextInt4 = new Random().nextInt(App.getInstance().config.yangTotal);
            if (!this.forwardList.contains(Integer.valueOf(nextInt4))) {
                this.forwardList.add(Integer.valueOf(nextInt4));
            }
        }
        Log.e("qyh", "转发随机数==" + this.forwardList.toString());
    }

    private void culitivateLike() throws Exception {
        this.names = new ArrayList();
        while (App.getInstance().startRun.booleanValue() && this.names.size() < getConfig().yangTotal) {
            doWorkLive("android:id/content");
            if (this.names.size() < getConfig().yangTotal) {
                upTouch();
            }
        }
    }

    private void doWorkLive(String str) throws Exception {
        AccessibilityNodeInfo commentBean;
        leftTouch();
        AccessibilityNodeInfo findViewById = ServiceUtils.findViewById(this.service, str);
        if ((findViewById == null || !findViewById.isVisibleToUser()) && App.getInstance().startRun.booleanValue() && !isAtten().booleanValue()) {
            back();
            this.names.add("1");
            Thread.sleep((long) getConfig().getLookTime());
            if (this.praiseList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue()) {
                doubleClickView(Utils.getDisplayWidth(this.service) / 2, (Utils.getDisplayHeight(this.service) / 5) * 2);
                Thread.sleep((long) getConfig().getoperationTime());
            }
            if (this.commentList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue() && (commentBean = getCommentBean(ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(87)))) != null && App.getInstance().startRun.booleanValue()) {
                clickView(commentBean);
                if (Utils.isEmptyArray(ServiceUtils.findViewByClassName((AccessibilityService) this.service, Jni.getSbObj(3)))) {
                    clickView(commentBean);
                }
                sendComment(false);
                Thread.sleep((long) getConfig().getoperationTime());
            }
            if (this.attenList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue()) {
                leftTouch();
                AccessibilityNodeInfo findViewById2 = ServiceUtils.findViewById(this.service, str);
                if (findViewById2 == null || !findViewById2.isVisibleToUser()) {
                    startAttenForColor();
                    Thread.sleep((long) this.sleep);
                } else {
                    Log.e("qyh", "进入失败");
                }
            }
            backToId(str);
            Thread.sleep((long) getConfig().getoperationTime());
        }
        backToId(str);
    }
}
