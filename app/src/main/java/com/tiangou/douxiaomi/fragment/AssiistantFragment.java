package com.tiangou.douxiaomi.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.gson.reflect.TypeToken;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.Constants;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.activity.TabActivity;
import com.tiangou.douxiaomi.activity.liandong.FuctionListActivity;
import com.tiangou.douxiaomi.adapter.FunctionLinkAdapter;
import com.tiangou.douxiaomi.bean.FunctionBean;
import com.tiangou.douxiaomi.function.impl.base.BaseImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.AccessService;
import com.tiangou.douxiaomi.utils.JsonPraise;
import java.util.ArrayList;
import java.util.List;

public class AssiistantFragment extends Fragment implements View.OnClickListener {
    FunctionLinkAdapter adapter;
    View layoutView;
    RecyclerView recyData;
    TextView tvAdd;
    TextView tvStart;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        initView();
        setListener();
        setOthers();
        return this.layoutView;
    }

    private void initView() {
        this.layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ass, (ViewGroup) null);
        this.tvAdd = (TextView) this.layoutView.findViewById(R.id.tv_add);
        this.tvStart = (TextView) this.layoutView.findViewById(R.id.tv_start);
        this.recyData = (RecyclerView) this.layoutView.findViewById(R.id.recy_data);
        this.adapter = new FunctionLinkAdapter(R.layout.recy_function_linkage, App.getInstance().functionList);
        this.tvAdd.setOnClickListener(this);
        this.tvStart.setOnClickListener(this);
    }

    private void setListener() {
        this.recyData.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyData.setAdapter(this.adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(this.adapter));
        itemTouchHelper.attachToRecyclerView(this.recyData);
        this.adapter.enableDragItem(itemTouchHelper, R.id.ll_view, true);
        this.adapter.setOnItemDragListener(new OnItemDragListener() {
            public void onItemDragMoving(RecyclerView.ViewHolder viewHolder, int i, RecyclerView.ViewHolder viewHolder2, int i2) {
            }

            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int i) {
            }

            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int i) {
                AssiistantFragment.this.adapter.notifyDataSetChanged();
            }
        });
        this.adapter.enableSwipeItem();
        this.adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {
            }

            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float f, float f2, boolean z) {
            }

            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {
            }

            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                AssiistantFragment.this.adapter.notifyDataSetChanged();
            }
        });
        this.adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                FunctionBean functionBean = (FunctionBean) AssiistantFragment.this.adapter.getItem(i);
                int id = view.getId();
                if (id == R.id.iv_change) {
                    functionBean.showData(AssiistantFragment.this.getActivity(), new FunctionBean.OnFunctionSelectListener() {
                        public void onSelect(String str) {
                            AssiistantFragment.this.adapter.notifyDataSetChanged();
                        }
                    });
                } else if (id == R.id.iv_setting) {
                    functionBean.showSetting(AssiistantFragment.this.getActivity(), new FunctionBean.OnFunctionSelectListener() {
                        public void onSelect(String str) {
                            AssiistantFragment.this.adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private void setOthers() {
        reload();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_add) {
            startActivityForResult(new Intent(getActivity(), FuctionListActivity.class), 1);
        } else if (id == R.id.tv_start) {
            if (!AccessService.get().checkAccessibilityEnabled(Constants.douyinService)) {
                AccessService.get().goAccess();
            } else {
                startLink();
            }
        }
    }

    private void startLink() {
        if (Utils.isEmptyArray(this.adapter.getData())) {
            Toast.makeText(getActivity(), "请先添加功能", 0).show();
        } else if (!isSuccess().booleanValue()) {
            Toast.makeText(getActivity(), "功能未配置完毕，请检查", 0).show();
        } else {
            ArrayList arrayList = new ArrayList();
            for (FunctionBean linkImpl : this.adapter.getData()) {
                arrayList.add(linkImpl.getLinkImpl());
            }
            if (Jni.getAuthStatus() == 0) {
                ((TabActivity) getActivity()).startRecord();
                App.getInstance().initTaskUtils((List<BaseImpl>) arrayList);
                App.getInstance().showFloatWindows();
                App.getInstance().holdApp(Constants.douyin);
                return;
            }
            Toast.makeText(getActivity(), "请先前往用户中心进行授权", 0).show();
        }
    }

    public void reload() {
        String string = SPUtils.getString(getActivity(), "function", "");
        if (!TextUtils.isEmpty(string)) {
            Log.e("qyh", "str==" + string);
            List<FunctionBean> list = (List) JsonPraise.opt001ListData(string, new TypeToken<List<FunctionBean>>() {
            }.getType());
            if (!Utils.isEmptyArray(list)) {
                App.getInstance().functionList = list;
            } else {
                App.getInstance().functionList = new ArrayList();
            }
            this.adapter.setNewData(App.getInstance().functionList);
        }
    }

    private Boolean isSuccess() {
        for (FunctionBean configSuccess : this.adapter.getData()) {
            if (!configSuccess.configSuccess().booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        this.adapter.setNewData(App.getInstance().functionList);
        save();
    }

    public void onPause() {
        super.onPause();
        save();
    }

    public void save() {
        String objToJson = !Utils.isEmptyArray(App.getInstance().functionList) ? JsonPraise.objToJson(App.getInstance().functionList) : null;
        Log.e("qyh", "str==" + objToJson);
        SPUtils.saveString(getActivity(), "function", objToJson);
    }
}
