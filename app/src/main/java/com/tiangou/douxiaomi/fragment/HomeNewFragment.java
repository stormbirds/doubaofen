package com.tiangou.douxiaomi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.StringSignature;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.WebActivity;
import com.tiangou.douxiaomi.activity.SettingActivity;
import com.tiangou.douxiaomi.activity.TabActivity;
import com.tiangou.douxiaomi.adapter.FunctionAdapter;
import com.tiangou.douxiaomi.bean.FunctionBean;
import com.tiangou.douxiaomi.event.AdGetSuccessEvent;
import com.tiangou.douxiaomi.event.AdverBean;
import com.tiangou.douxiaomi.tasks.GetAdvertisementTask;
import com.tiangou.douxiaomi.utils.DensityUtil;
import com.tiangou.douxiaomi.utils.FunctionUtils;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import java.util.ArrayList;
import java.util.List;

public class HomeNewFragment extends Fragment implements View.OnClickListener {
    FunctionAdapter adapter;
    AdverBean adverBean;
    List<FunctionBean> functionBeans;
    ImageView ivMain;
    ImageView ivSetting;
    View layoutView;
    RecyclerView recyData;
    Button susStart = null;
    TextView tvLiandong;
    FunctionUtils utils;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        initView();
        View findViewById = this.layoutView.findViewById(R.id.statusbar);
        if (findViewById != null) {
            ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
            layoutParams.height = DensityUtil.getStatusBarHeight(getActivity());
            findViewById.setLayoutParams(layoutParams);
        }
        setListener();
        setOthers();
        return this.layoutView;
    }

    private void initView() {
        this.layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, (ViewGroup) null);
        this.utils = new FunctionUtils(getActivity());
        this.adapter = new FunctionAdapter(R.layout.recy_function, (List<FunctionBean>) null);
        this.tvLiandong = (TextView) this.layoutView.findViewById(R.id.tv_liandong);
        this.ivSetting = (ImageView) this.layoutView.findViewById(R.id.iv_setting);
        this.ivMain = (ImageView) this.layoutView.findViewById(R.id.iv_main);
        this.recyData = (RecyclerView) this.layoutView.findViewById(R.id.recy_data);
        EventBus.getDefault().register(this);
    }

    private void setListener() {
        this.recyData.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        this.recyData.setAdapter(this.adapter);
        this.ivSetting.setOnClickListener(this);
        this.ivMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomeNewFragment.this.getActivity(), WebActivity.class);
                intent.putExtra("title", "详情");
                intent.putExtra("url", HomeNewFragment.this.adverBean.ad_url1);
                HomeNewFragment.this.startActivity(intent);
            }
        });
        this.adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                HomeNewFragment.this.utils.click((FunctionBean) HomeNewFragment.this.adapter.getItem(i));
            }
        });
    }

    public void test() {
        new GetAdvertisementTask(getActivity()).execute(new Object[0]);
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
    }

    private void setOthers() {
        addData();
        this.adapter.setNewData(this.functionBeans);
    }

    private void addData() {
        this.functionBeans = new ArrayList();
        this.functionBeans.add(new FunctionBean(1, "定向抖友", "私/关定向抖友", "请先打开对应列表"));
        this.functionBeans.add(new FunctionBean(2, "首页推荐抖友", "私/关首页作品随机抖友", "请先打开首页，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(3, "关键词用户", "私/关指定用户,支持关键词/ID", "请先打开添加好友页面，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(4, "通讯录用户", "私/关通讯录用户", "请先打开通讯录页面，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(5, "评论用户", "私/关当前作品评论者", "请先打开对应作品评论页面，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(6, "直播间用户", "私/关直播间本场榜/在线用户", "请先打开直播间指定榜单，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(7, "评论专区", "推荐视频/同城/指定用户的作品/直播广场", ""));
        this.functionBeans.add(new FunctionBean(9, "好友列表", "好友列表互动/文字/图片", "请先打开好友列表，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(10, "消息列表", "发消息", "请先打开消息列表，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(11, "养号", "推荐专区养号", "请先打开首页，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(13, "点赞", "视频点赞", ""));
        this.functionBeans.add(new FunctionBean(14, "私信", "私信首页推荐视频", "请先打开首页推荐视频"));
        this.functionBeans.add(new FunctionBean(15, "同城用户", "私/关通讯录用户", "同城页面第一个视频"));
        this.functionBeans.add(new FunctionBean(16, "批量取关/回关", "我的抖友，批量关注及取消关注", "请先打开我的粉丝列表或我的关注列表"));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.iv_setting) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        }
    }

    public void onResume() {
        super.onResume();
        this.utils.onResume();
        ((TabActivity) getActivity()).stopRecord();
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void banner(AdGetSuccessEvent adGetSuccessEvent) {
        try {
            this.adverBean = (AdverBean) JSONObject.parseObject(adGetSuccessEvent.content, AdverBean.class);
            if (this.adverBean == null || TextUtils.isEmpty(this.adverBean.img1)) {
                this.ivMain.setVisibility(8);
                return;
            }
            this.ivMain.setVisibility(0);
            String valueOf = String.valueOf(System.currentTimeMillis());
            RequestManager with = Glide.with(getActivity());
            with.load("http://yz.yipinanzhuo.com" + this.adverBean.img1).signature((Key) new StringSignature(valueOf)).into(this.ivMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
