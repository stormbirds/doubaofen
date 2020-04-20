package com.tiangou.douxiaomi.adapter;

import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.bean.ConnectBean;
import java.util.List;

public class ConnectAdapter extends BaseQuickAdapter<ConnectBean, BaseViewHolder> {
    public ConnectAdapter(int i, List<ConnectBean> list) {
        super(i, list);
    }

    /* access modifiers changed from: protected */
    public void convert(final BaseViewHolder baseViewHolder, final ConnectBean connectBean) {
        baseViewHolder.setText((int) R.id.tv_type, (CharSequence) connectBean.type_title).setText((int) R.id.tv_phone, (CharSequence) connectBean.phone);
        baseViewHolder.setImageResource(R.id.iv_check, connectBean.checked.booleanValue() ? R.drawable.renmai_check_select : R.drawable.renmai_check_normal);
        baseViewHolder.setOnClickListener(R.id.ll_view, new View.OnClickListener() {
            public void onClick(View view) {
                ConnectBean connectBean = connectBean;
                connectBean.checked = Boolean.valueOf(!connectBean.checked.booleanValue());
                ConnectAdapter.this.notifyItemChanged(baseViewHolder.getAdapterPosition());
            }
        });
    }
}
