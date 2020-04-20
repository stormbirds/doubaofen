package com.tiangou.douxiaomi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.adapter.DataSelectAdapter;
import java.util.List;

public class DataSelectDialog extends Dialog {
    DataSelectAdapter adapter;
    OnDescClickListener descListener;
    OnDataSelectListener listener;
    private Context mContext;
    List<String> mData;
    RecyclerView recyData;
    String title;
    TextView tvTitle;

    public interface OnDataSelectListener {
        void onDataSelect(int i);
    }

    public interface OnDescClickListener {
        void onDescClick();
    }

    public DataSelectDialog(Context context, String str, List<String> list) {
        super(context, R.style.Theme_Light_NoTitle_Dialog);
        this.mData = list;
        this.title = str;
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setListener();
        setOthers();
        Window window = getWindow();
        window.getDecorView().setPadding(Utils.dp2Px(this.mContext, 10.0f), Utils.dp2Px(this.mContext, 10.0f), Utils.dp2Px(this.mContext, 10.0f), Utils.dp2Px(this.mContext, 10.0f));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -2;
        window.setAttributes(attributes);
        window.setGravity(80);
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);
    }

    public void setOnDataSelectListener(OnDataSelectListener onDataSelectListener) {
        this.listener = onDataSelectListener;
    }

    public void setDescListener(OnDescClickListener onDescClickListener) {
        this.descListener = onDescClickListener;
    }

    private void initView() {
        setContentView(R.layout.dialog_select);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.recyData = (RecyclerView) findViewById(R.id.recy_data);
    }

    private void setListener() {
        this.adapter = new DataSelectAdapter(this.mData);
        this.recyData.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.recyData.setAdapter(this.adapter);
        this.adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (DataSelectDialog.this.listener != null) {
                    DataSelectDialog.this.listener.onDataSelect(i);
                    DataSelectDialog.this.dismiss();
                }
            }
        });
    }

    private void setOthers() {
        this.tvTitle.setText(this.title);
    }
}
