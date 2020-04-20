package com.tiangou.douxiaomi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.adapter.DialogContentAdapter;
import com.tiangou.douxiaomi.bean.BaseContentBean;
import com.tiangou.douxiaomi.view.shape.ShapeTextView;
import java.io.Serializable;
import java.util.List;

public class CustomContentDialog extends Dialog implements Serializable {
    DialogContentAdapter adapter;
    String confirmStr;
    ImageView ivClose;
    OnDialogConfimListener listener;
    private Context mContext;
    List<BaseContentBean> mDatas;
    RecyclerView recyData;
    String titleStr;
    ShapeTextView tvConfirm;
    TextView tvTitle;

    public interface OnDialogConfimListener {
        void onDataConfirm();
    }

    public CustomContentDialog(Context context, String str, String str2, List<BaseContentBean> list) {
        super(context, R.style.Theme_Light_NoTitle_Dialog);
        this.mContext = context;
        this.titleStr = str;
        this.confirmStr = str2;
        this.mDatas = list;
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
        window.setGravity(17);
    }

    public void setOnDataSelectListener(OnDialogConfimListener onDialogConfimListener) {
        this.listener = onDialogConfimListener;
    }

    public void refreshData() {
        this.adapter.notifyDataSetChanged();
    }

    private void initView() {
        setContentView(R.layout.dialog_take_over);
        this.recyData = (RecyclerView) findViewById(R.id.recy_data);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.tvConfirm = (ShapeTextView) findViewById(R.id.tv_confirm);
        this.ivClose = (ImageView) findViewById(R.id.iv_close);
        this.adapter = new DialogContentAdapter(this.mDatas);
    }

    private void setListener() {
        this.recyData.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.recyData.setAdapter(this.adapter);
        this.ivClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CustomContentDialog.this.dismiss();
            }
        });
        this.tvConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Boolean access$000 = CustomContentDialog.this.isSuccess();
                if (CustomContentDialog.this.listener != null && access$000.booleanValue()) {
                    CustomContentDialog.this.listener.onDataConfirm();
                    CustomContentDialog.this.dismiss();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public Boolean isSuccess() {
        for (BaseContentBean next : this.mDatas) {
            if (next.notNull.booleanValue() && TextUtils.isEmpty(next.content)) {
                Context context = this.mContext;
                Toast.makeText(context, next.msg + "不能为空", 0).show();
                return false;
            }
        }
        return true;
    }

    private void setOthers() {
        this.tvTitle.setText(this.titleStr);
        this.tvConfirm.setText(this.confirmStr);
    }
}
