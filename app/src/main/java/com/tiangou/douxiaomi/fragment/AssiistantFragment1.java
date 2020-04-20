package com.tiangou.douxiaomi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.StringSignature;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.WebActivity;
import com.tiangou.douxiaomi.event.AdGetSuccessEvent;
import com.tiangou.douxiaomi.event.AdverBean;
import com.tiangou.douxiaomi.tasks.GetAdvertisementTask;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class AssiistantFragment1 extends Fragment {
    AdverBean adverBean;
    ImageView ivAss;
    View layoutView;

    private void setOthers() {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        EventBus.getDefault().register(this);
        initView();
        setListener();
        setOthers();
        return this.layoutView;
    }

    private void initView() {
        this.layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ass_1, (ViewGroup) null);
        this.ivAss = (ImageView) this.layoutView.findViewById(R.id.iv_ass);
    }

    private void setListener() {
        this.ivAss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AssiistantFragment1.this.adverBean != null) {
                    Intent intent = new Intent(AssiistantFragment1.this.getActivity(), WebActivity.class);
                    intent.putExtra("title", "详情");
                    intent.putExtra("url", AssiistantFragment1.this.adverBean.ad_url2);
                    AssiistantFragment1.this.startActivity(intent);
                }
            }
        });
    }

    public void test() {
        new GetAdvertisementTask(getActivity()).execute(new Object[0]);
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            test();
        }
    }

    public void onResume() {
        super.onResume();
        test();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void startRun(AdGetSuccessEvent adGetSuccessEvent) {
        this.adverBean = (AdverBean) JSONObject.parseObject(adGetSuccessEvent.content, AdverBean.class);
        AdverBean adverBean2 = this.adverBean;
        if (adverBean2 != null && !TextUtils.isEmpty(adverBean2.img2)) {
            String valueOf = String.valueOf(System.currentTimeMillis());
            RequestManager with = Glide.with(getActivity());
            with.load("http://yz.yipinanzhuo.com" + this.adverBean.img2).signature((Key) new StringSignature(valueOf)).into(this.ivAss);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
