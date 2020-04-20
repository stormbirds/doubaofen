package com.chad.library.adapter.base;

import android.util.SparseArray;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chad.library.adapter.base.util.ProviderDelegate;
import java.util.List;

public abstract class MultipleItemRvAdapter<T, V extends BaseViewHolder> extends BaseQuickAdapter<T, V> {
    private SparseArray<BaseItemProvider> mItemProviders;
    protected ProviderDelegate mProviderDelegate;

    /* access modifiers changed from: protected */
    public abstract int getViewType(T t);

    public abstract void registerItemProvider();

    public MultipleItemRvAdapter(List<T> list) {
        super(list);
    }

    public void finishInitialize() {
        this.mProviderDelegate = new ProviderDelegate();
        setMultiTypeDelegate(new MultiTypeDelegate<T>() {
            /* access modifiers changed from: protected */
            public int getItemType(T t) {
                return MultipleItemRvAdapter.this.getViewType(t);
            }
        });
        registerItemProvider();
        this.mItemProviders = this.mProviderDelegate.getItemProviders();
        for (int i = 0; i < this.mItemProviders.size(); i++) {
            int keyAt = this.mItemProviders.keyAt(i);
            BaseItemProvider baseItemProvider = this.mItemProviders.get(keyAt);
            baseItemProvider.mData = this.mData;
            getMultiTypeDelegate().registerItemType(keyAt, baseItemProvider.layout());
        }
    }

    /* access modifiers changed from: protected */
    public void convert(V v, T t) {
        BaseItemProvider baseItemProvider = this.mItemProviders.get(v.getItemViewType());
        baseItemProvider.mContext = v.itemView.getContext();
        int layoutPosition = v.getLayoutPosition() - getHeaderLayoutCount();
        baseItemProvider.convert(v, t, layoutPosition);
        bindClick(v, t, layoutPosition, baseItemProvider);
    }

    private void bindClick(V v, T t, int i, BaseItemProvider baseItemProvider) {
        BaseQuickAdapter.OnItemClickListener onItemClickListener = getOnItemClickListener();
        BaseQuickAdapter.OnItemLongClickListener onItemLongClickListener = getOnItemLongClickListener();
        if (onItemClickListener == null || onItemLongClickListener == null) {
            View view = v.itemView;
            if (onItemClickListener == null) {
                final BaseItemProvider baseItemProvider2 = baseItemProvider;
                final V v2 = v;
                final T t2 = t;
                final int i2 = i;
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        baseItemProvider2.onClick(v2, t2, i2);
                    }
                });
            }
            if (onItemLongClickListener == null) {
                final BaseItemProvider baseItemProvider3 = baseItemProvider;
                final V v3 = v;
                final T t3 = t;
                final int i3 = i;
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        return baseItemProvider3.onLongClick(v3, t3, i3);
                    }
                });
            }
        }
    }
}
