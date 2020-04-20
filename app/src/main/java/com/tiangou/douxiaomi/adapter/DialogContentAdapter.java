package com.tiangou.douxiaomi.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.bean.BaseContentBean;
import java.util.List;

public class DialogContentAdapter extends BaseQuickAdapter<BaseContentBean, BaseViewHolder> {
    public DialogContentAdapter(List<BaseContentBean> list) {
        super(R.layout.recy_dialog_base, list);
    }

    /* access modifiers changed from: protected */
    public void convert(final BaseViewHolder baseViewHolder, BaseContentBean baseContentBean) {
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_content);
        final EditText editText = (EditText) baseViewHolder.getView(R.id.et_content);
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.iv_right);
        baseViewHolder.setText((int) R.id.tv_msg, (CharSequence) baseContentBean.msg);
        if (baseContentBean.resId != 0) {
            imageView.setVisibility(0);
            imageView.setImageResource(baseContentBean.resId);
        } else {
            imageView.setVisibility(8);
        }
        int i = baseContentBean.type;
        if (i == 0) {
            textView.setVisibility(0);
            editText.setVisibility(8);
            if (baseContentBean.tag != null) {
                textView.setTag(baseContentBean.tag);
            } else {
                textView.setTag((Object) null);
            }
            textView.setHint(baseContentBean.hint);
            textView.setText(baseContentBean.content);
        } else if (i == 1) {
            textView.setVisibility(8);
            editText.setVisibility(0);
            if (baseContentBean.tag != null) {
                editText.setTag(baseContentBean.tag);
            } else {
                editText.setTag((Object) null);
            }
            editText.setText(baseContentBean.content);
            editText.setHint(baseContentBean.hint);
            if (baseContentBean.inputType != -1) {
                editText.setInputType(baseContentBean.inputType);
            } else {
                editText.setInputType(144);
            }
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    ((BaseContentBean) DialogContentAdapter.this.mData.get(baseViewHolder.getAdapterPosition())).content = editText.getText().toString();
                    ((BaseContentBean) DialogContentAdapter.this.mData.get(baseViewHolder.getAdapterPosition())).tag = editText.getText().toString();
                }
            });
        }
        baseViewHolder.setOnClickListener(R.id.ll_view, baseContentBean.clickListener);
    }
}
