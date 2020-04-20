package com.tiangou.douxiaomi.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.tiangou.douxiaomi.R;
import java.util.ArrayList;
import java.util.List;

public class DataPickerDialog extends Dialog {
    private Params params;

    public interface OnDataSelectedListener {
        void onDataSelected(int i, String str);
    }

    public DataPickerDialog(Context context, int i) {
        super(context, i);
    }

    /* access modifiers changed from: private */
    public void setParams(Params params2) {
        this.params = params2;
    }

    public void setSelection(String str) {
        int indexOf;
        if (this.params.dataList.size() > 0 && (indexOf = this.params.dataList.indexOf(str)) >= 0) {
            int unused = this.params.initSelection = indexOf;
            this.params.loopData.setCurrentItem(this.params.initSelection);
        }
    }

    private static final class Params {
        /* access modifiers changed from: private */
        public OnDataSelectedListener callback;
        /* access modifiers changed from: private */
        public boolean canCancel;
        /* access modifiers changed from: private */
        public final List<String> dataList;
        /* access modifiers changed from: private */
        public int initSelection;
        /* access modifiers changed from: private */
        public LoopView loopData;
        /* access modifiers changed from: private */
        public boolean shadow;
        /* access modifiers changed from: private */
        public String title;
        /* access modifiers changed from: private */
        public String unit;

        private Params() {
            this.shadow = true;
            this.canCancel = true;
            this.initSelection = 3;
            this.dataList = new ArrayList();
        }
    }

    public static class Builder {
        private final Context context;
        /* access modifiers changed from: private */
        public final Params params = new Params();

        public Builder(Context context2) {
            this.context = context2;
        }

        /* access modifiers changed from: private */
        public final String getCurrDateValue() {
            return this.params.loopData.getCurrentItemValue();
        }

        /* access modifiers changed from: private */
        public final int getCurrDateIndex() {
            return this.params.loopData.getCurrentItem();
        }

        public Builder setData(List<String> list) {
            this.params.dataList.clear();
            this.params.dataList.addAll(list);
            return this;
        }

        public Builder setTitle(String str) {
            String unused = this.params.title = str;
            return this;
        }

        public Builder setUnit(String str) {
            String unused = this.params.unit = str;
            return this;
        }

        public Builder setSelection(int i) {
            int unused = this.params.initSelection = i;
            return this;
        }

        public Builder setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
            OnDataSelectedListener unused = this.params.callback = onDataSelectedListener;
            return this;
        }

        public DataPickerDialog create() {
            final DataPickerDialog dataPickerDialog = new DataPickerDialog(this.context, this.params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_picker_data, (ViewGroup) null);
            if (!TextUtils.isEmpty(this.params.title)) {
                ((TextView) inflate.findViewById(R.id.tx_title)).setText(this.params.title);
            }
            if (!TextUtils.isEmpty(this.params.unit)) {
                ((TextView) inflate.findViewById(R.id.tx_unit)).setText(this.params.unit);
            }
            LoopView loopView = (LoopView) inflate.findViewById(R.id.loop_data);
            loopView.setArrayList(this.params.dataList);
            loopView.setNotLoop();
            if (this.params.dataList.size() > this.params.initSelection) {
                loopView.setCurrentItem(this.params.initSelection);
            }
            inflate.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dataPickerDialog.dismiss();
                    Builder.this.params.callback.onDataSelected(Builder.this.getCurrDateIndex(), Builder.this.getCurrDateValue());
                }
            });
            inflate.findViewById(R.id.tx_cancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dataPickerDialog.dismiss();
                }
            });
            Window window = dataPickerDialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            attributes.height = -2;
            window.setAttributes(attributes);
            window.setGravity(80);
            window.setWindowAnimations(R.style.Animation_Bottom_Rising);
            dataPickerDialog.setContentView(inflate);
            dataPickerDialog.setCanceledOnTouchOutside(this.params.canCancel);
            dataPickerDialog.setCancelable(this.params.canCancel);
            LoopView unused = this.params.loopData = loopView;
            dataPickerDialog.setParams(this.params);
            return dataPickerDialog;
        }
    }
}
