package com.tiangou.douxiaomi.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiangou.douxiaomi.R;
import java.util.List;

public class DataSelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DataSelectAdapter(List<String> list) {
        super(R.layout.recy_dialog_select, list);
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder baseViewHolder, String str) {
        baseViewHolder.setText((int) R.id.tv_title, (CharSequence) str);
    }
}
