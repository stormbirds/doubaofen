package com.tiangou.douxiaomi.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.bean.FunctionBean;
import java.util.List;

public class FunctionAdapter extends BaseQuickAdapter<FunctionBean, BaseViewHolder> {
    private int getResourse(int i) {
        switch (i) {
            case 1:
                return R.drawable.bg_14;
            case 2:
                return R.drawable.bg_7;
            case 3:
                return R.drawable.bg_9;
            case 4:
                return R.drawable.bg_8;
            case 5:
                return R.drawable.bg_10;
            case 6:
                return R.drawable.bg_12;
            case 7:
                return R.drawable.bg_13;
            case 9:
                return R.drawable.bg_18;
            case 10:
                return R.drawable.bg_4;
            case 11:
                return R.drawable.bg_5;
            case 13:
                return R.drawable.bg_7;
            case 14:
                return R.drawable.bg_13;
            case 15:
                return R.drawable.bg_14;
            case 16:
                return R.drawable.bg_18;
            default:
                return R.drawable.bg_6;
        }
    }

    public FunctionAdapter(int i, List<FunctionBean> list) {
        super(i, list);
    }

    /* access modifiers changed from: protected */
    public void convert(BaseViewHolder baseViewHolder, FunctionBean functionBean) {
        baseViewHolder.setText((int) R.id.tv_title, (CharSequence) functionBean.title).setText((int) R.id.tv_desc, (CharSequence) functionBean.desc);
        baseViewHolder.setImageResource(R.id.iv_icon, getResourse(functionBean.type));
    }
}
