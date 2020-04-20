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

public class RecommentCulitivateImple<R extends ConfigBean> extends BaseCulitivateImpl<R> {
    public List<Integer> attenList;
    public List<Integer> commentList;
    public List<Integer> forwardList;
    public List<Integer> praiseList;

    public RecommentCulitivateImple(R r) {
        super(r);
    }

    /* access modifiers changed from: protected */
    public void prepose() {
        this.isPrepose = enterHomeRecommand();
    }

    /* access modifiers changed from: protected */
    public void on() {
        try {
            randomList();
            culitivateRecommend();
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

    private void culitivateRecommend() throws Exception {
        this.names = new ArrayList();
        while (App.getInstance().startRun.booleanValue() && this.names.size() < App.getInstance().config.yangTotal) {
            List<AccessibilityNodeInfo> findViewByEqualsText = ServiceUtils.findViewByEqualsText((AccessibilityService) this.service, Jni.getSbObj(1));
            if (!Utils.isEmptyArray(findViewByEqualsText)) {
                if (Build.VERSION.SDK_INT >= 24) {
                    upTouch();
                } else {
                    ServiceUtils.clickView(findViewByEqualsText.get(0));
                }
            }
            Thread.sleep((long) this.longSleep);
            if (!isAdvertise(ServiceUtils.findViewByContainsText((AccessibilityService) this.service, " [t]")).booleanValue()) {
                this.names.add("1");
                if (this.commentList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue()) {
                    Log.e("qyh", "需要评论");
                    commentVideo();
                    Thread.sleep((long) this.sleep);
                }
                if (this.attenList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue()) {
                    Log.e("qyh", "需要关注");
                    Thread.sleep((long) this.sleep);
                    AccessibilityNodeInfo findViewByFirstEqualsContentDescription = ServiceUtils.findViewByFirstEqualsContentDescription((AccessibilityService) this.service, Jni.getSbObj(12));
                    if (findViewByFirstEqualsContentDescription != null) {
                        ServiceUtils.clickView(findViewByFirstEqualsContentDescription);
                    } else {
                        Log.e("qyh", "关注失败");
                    }
                    Thread.sleep((long) this.sleep);
                }
                if (this.praiseList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue()) {
                    Log.e("qyh", "需要点赞");
                    startPraise();
                    Thread.sleep((long) this.sleep);
                }
                if (this.forwardList.contains(Integer.valueOf(this.names.size() - 1)) && App.getInstance().startRun.booleanValue()) {
                    Log.e("qyh", "需要转发");
                    startForward();
                    Thread.sleep((long) this.sleep);
                }
                Thread.sleep((long) getConfig().getYangInterval());
            } else {
                Log.e("qyh", "这是个广告");
            }
        }
    }
}
