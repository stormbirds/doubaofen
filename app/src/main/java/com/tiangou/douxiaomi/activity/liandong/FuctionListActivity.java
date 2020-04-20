package com.tiangou.douxiaomi.activity.liandong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.adapter.FunctionAdapter;
import com.tiangou.douxiaomi.bean.FunctionBean;
import java.util.ArrayList;
import java.util.List;

public class FuctionListActivity extends Activity {
    FunctionAdapter adapter;
    List<FunctionBean> functionBeans;
    RecyclerView recyData;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setListener();
        setOther();
    }

    private void initView() {
        setContentView(R.layout.activity_function_list);
        this.recyData = (RecyclerView) findViewById(R.id.recy_data);
        this.adapter = new FunctionAdapter(R.layout.recy_function, (List<FunctionBean>) null);
    }

    private void setListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FuctionListActivity.this.finish();
            }
        });
        this.recyData.setLayoutManager(new GridLayoutManager(this, 2));
        this.recyData.setAdapter(this.adapter);
        this.adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                final FunctionBean functionBean = (FunctionBean) FuctionListActivity.this.adapter.getItem(i);
                functionBean.showData(FuctionListActivity.this, new FunctionBean.OnFunctionSelectListener() {
                    public void onSelect(String str) {
                        FuctionListActivity.this.setResult(functionBean);
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void setResult(FunctionBean functionBean) {
        App.getInstance().functionList.add(functionBean);
        setResult(-1, new Intent());
        finish();
    }

    private void setOther() {
        addData();
        this.adapter.setNewData(this.functionBeans);
    }

    private void addData() {
        this.functionBeans = new ArrayList();
        this.functionBeans.add(new FunctionBean(1, "定向抖友", "私/关定向抖友", "请先打开对应列表", true));
        this.functionBeans.add(new FunctionBean(2, "首页推荐抖友", "私/关首页作品随机抖友", "请先打开首页，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(3, "关键词用户", "私/关指定用户,支持关键词/ID", "请先打开添加好友页面，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(4, "通讯录用户", "私/关通讯录用户", "请先打开通讯录页面，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(7, "评论专区", "推荐视频/同城/指定用户的作品/直播广场", ""));
        this.functionBeans.add(new FunctionBean(9, "好友列表", "好友列表互动/文字/图片", "请先打开好友列表，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(10, "消息列表", "发消息", "请先打开消息列表，再点击开始按钮"));
        this.functionBeans.add(new FunctionBean(11, "养号", "推荐专区养号", "请先打开首页，再点击开始按钮"));
    }
}
