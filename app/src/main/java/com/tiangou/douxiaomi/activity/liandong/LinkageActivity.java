package com.tiangou.douxiaomi.activity.liandong;

import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.tiangou.douxiaomi.activity.BaseActivity;
import com.tiangou.douxiaomi.adapter.FunctionLinkAdapter;
import com.tiangou.douxiaomi.bean.FunctionBean;
import com.tiangou.douxiaomi.function.impl.base.BaseImpl;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.AccessService;
import com.tiangou.douxiaomi.utils.GBData;
import com.tiangou.douxiaomi.utils.JsonPraise;
import com.tiangou.douxiaomi.utils.StatusbarUtil;
import java.util.ArrayList;
import java.util.List;

public class LinkageActivity extends BaseActivity implements View.OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int REQUEST_MEDIA_PROJECTION = 111;
    FunctionLinkAdapter adapter;
    ImageView ivBack;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;
    RecyclerView recyData;
    TextView tvAdd;
    TextView tvStart;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setListener();
        setOther();
    }

    private void initView() {
        setContentView((int) R.layout.activity_linkage);
        StatusbarUtil.setBgTransparent(this);
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.tvAdd = (TextView) findViewById(R.id.tv_add);
        this.tvStart = (TextView) findViewById(R.id.tv_start);
        this.recyData = (RecyclerView) findViewById(R.id.recy_data);
        this.adapter = new FunctionLinkAdapter(R.layout.recy_function_linkage, App.getInstance().functionList);
        this.ivBack.setOnClickListener(this);
        this.tvAdd.setOnClickListener(this);
        this.tvStart.setOnClickListener(this);
    }

    private void setListener() {
        this.recyData.setLayoutManager(new LinearLayoutManager(this));
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
                LinkageActivity.this.adapter.notifyDataSetChanged();
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
                LinkageActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                FunctionBean functionBean = (FunctionBean) LinkageActivity.this.adapter.getItem(i);
                int id = view.getId();
                if (id == R.id.iv_change) {
                    functionBean.showData(LinkageActivity.this, new FunctionBean.OnFunctionSelectListener() {
                        public void onSelect(String str) {
                            LinkageActivity.this.adapter.notifyDataSetChanged();
                        }
                    });
                } else if (id == R.id.iv_setting) {
                    functionBean.showSetting(LinkageActivity.this, new FunctionBean.OnFunctionSelectListener() {
                        public void onSelect(String str) {
                            LinkageActivity.this.adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
    }

    private void setOther() {
        reload();
    }

    public void startRecord() {
        Log.e("qyh", "开始录制");
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        startActivityForResult(this.mMediaProjectionManager.createScreenCaptureIntent(), 111);
    }

    public void stopRecord() {
        Log.e("qyh", "结束录制");
        if (this.mMediaProjectionManager != null) {
            this.mMediaProjectionManager = null;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_add) {
            startActivityForResult(new Intent(this, FuctionListActivity.class), 1);
        } else if (id == R.id.tv_start) {
            if (!AccessService.get().checkAccessibilityEnabled(Constants.douyinService)) {
                AccessService.get().goAccess();
            } else {
                startLink();
            }
        }
    }

    private void setUpVirtualDisplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        ImageReader newInstance = ImageReader.newInstance(displayMetrics.widthPixels, displayMetrics.heightPixels, 1, 1);
        this.mMediaProjection.createVirtualDisplay("ScreenCapture", displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi, 16, newInstance.getSurface(), (VirtualDisplay.Callback) null, (Handler) null);
        GBData.reader = newInstance;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        save();
    }

    public void save() {
        String objToJson = !Utils.isEmptyArray(App.getInstance().functionList) ? JsonPraise.objToJson(App.getInstance().functionList) : null;
        Log.e("qyh", "str==" + objToJson);
        SPUtils.saveString(this, "function", objToJson);
    }

    public void reload() {
        String string = SPUtils.getString(this, "function", "");
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

    private void startLink() {
        if (Utils.isEmptyArray(this.adapter.getData())) {
            Toast.makeText(this, "请先添加功能", 0).show();
        } else if (!isSuccess().booleanValue()) {
            Toast.makeText(this, "功能未配置完毕，请检查", 0).show();
        } else {
            ArrayList arrayList = new ArrayList();
            for (FunctionBean linkImpl : this.adapter.getData()) {
                arrayList.add(linkImpl.getLinkImpl());
            }
            if (Jni.getAuthStatus() == 0) {
                startRecord();
                App.getInstance().initTaskUtils((List<BaseImpl>) arrayList);
                App.getInstance().showFloatWindows();
                App.getInstance().holdApp(Constants.douyin);
                return;
            }
            Toast.makeText(this, "请先前往用户中心进行授权", 0).show();
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

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1) {
            this.adapter.setNewData(App.getInstance().functionList);
            save();
        }
        if (i != 111) {
            return;
        }
        if (i2 != -1) {
            Toast.makeText(this, "User cancelled!", 0).show();
            return;
        }
        Log.i("qyh", "Starting screen capture");
        this.mMediaProjection = this.mMediaProjectionManager.getMediaProjection(i2, intent);
        setUpVirtualDisplay();
    }
}
