package com.chad.library.adapter.base;

import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import com.chad.library.R;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import java.util.Collections;
import java.util.List;

public abstract class BaseItemDraggableAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {
    private static final String ERROR_NOT_SAME_ITEMTOUCHHELPER = "Item drag and item swipe should pass the same ItemTouchHelper";
    private static final int NO_TOGGLE_VIEW = 0;
    protected boolean itemDragEnabled = false;
    protected boolean itemSwipeEnabled = false;
    protected boolean mDragOnLongPress = true;
    protected ItemTouchHelper mItemTouchHelper;
    protected OnItemDragListener mOnItemDragListener;
    protected OnItemSwipeListener mOnItemSwipeListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;
    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected int mToggleViewId = 0;

    public BaseItemDraggableAdapter(List<T> list) {
        super(list);
    }

    public BaseItemDraggableAdapter(int i, List<T> list) {
        super(i, list);
    }

    public void onBindViewHolder(K k, int i) {
        super.onBindViewHolder(k, i);
        int itemViewType = k.getItemViewType();
        if (this.mItemTouchHelper != null && this.itemDragEnabled && itemViewType != 546 && itemViewType != 273 && itemViewType != 1365 && itemViewType != 819) {
            int i2 = this.mToggleViewId;
            if (i2 != 0) {
                View view = k.getView(i2);
                if (view != null) {
                    view.setTag(R.id.BaseQuickAdapter_viewholder_support, k);
                    if (this.mDragOnLongPress) {
                        view.setOnLongClickListener(this.mOnToggleViewLongClickListener);
                    } else {
                        view.setOnTouchListener(this.mOnToggleViewTouchListener);
                    }
                }
            } else {
                k.itemView.setTag(R.id.BaseQuickAdapter_viewholder_support, k);
                k.itemView.setOnLongClickListener(this.mOnToggleViewLongClickListener);
            }
        }
    }

    public void setToggleViewId(int i) {
        this.mToggleViewId = i;
    }

    public void setToggleDragOnLongPress(boolean z) {
        this.mDragOnLongPress = z;
        if (this.mDragOnLongPress) {
            this.mOnToggleViewTouchListener = null;
            this.mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (BaseItemDraggableAdapter.this.mItemTouchHelper == null || !BaseItemDraggableAdapter.this.itemDragEnabled) {
                        return true;
                    }
                    BaseItemDraggableAdapter.this.mItemTouchHelper.startDrag((RecyclerView.ViewHolder) view.getTag(R.id.BaseQuickAdapter_viewholder_support));
                    return true;
                }
            };
            return;
        }
        this.mOnToggleViewTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) != 0 || BaseItemDraggableAdapter.this.mDragOnLongPress) {
                    return false;
                }
                if (BaseItemDraggableAdapter.this.mItemTouchHelper == null || !BaseItemDraggableAdapter.this.itemDragEnabled) {
                    return true;
                }
                BaseItemDraggableAdapter.this.mItemTouchHelper.startDrag((RecyclerView.ViewHolder) view.getTag(R.id.BaseQuickAdapter_viewholder_support));
                return true;
            }
        };
        this.mOnToggleViewLongClickListener = null;
    }

    public void enableDragItem(ItemTouchHelper itemTouchHelper) {
        enableDragItem(itemTouchHelper, 0, true);
    }

    public void enableDragItem(ItemTouchHelper itemTouchHelper, int i, boolean z) {
        this.itemDragEnabled = true;
        this.mItemTouchHelper = itemTouchHelper;
        setToggleViewId(i);
        setToggleDragOnLongPress(z);
    }

    public void disableDragItem() {
        this.itemDragEnabled = false;
        this.mItemTouchHelper = null;
    }

    public boolean isItemDraggable() {
        return this.itemDragEnabled;
    }

    public void enableSwipeItem() {
        this.itemSwipeEnabled = true;
    }

    public void disableSwipeItem() {
        this.itemSwipeEnabled = false;
    }

    public boolean isItemSwipeEnable() {
        return this.itemSwipeEnabled;
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        this.mOnItemDragListener = onItemDragListener;
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - getHeaderLayoutCount();
    }

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        OnItemDragListener onItemDragListener = this.mOnItemDragListener;
        if (onItemDragListener != null && this.itemDragEnabled) {
            onItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemDragMoving(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        int viewHolderPosition = getViewHolderPosition(viewHolder);
        int viewHolderPosition2 = getViewHolderPosition(viewHolder2);
        if (inRange(viewHolderPosition) && inRange(viewHolderPosition2)) {
            if (viewHolderPosition < viewHolderPosition2) {
                int i = viewHolderPosition;
                while (i < viewHolderPosition2) {
                    int i2 = i + 1;
                    Collections.swap(this.mData, i, i2);
                    i = i2;
                }
            } else {
                for (int i3 = viewHolderPosition; i3 > viewHolderPosition2; i3--) {
                    Collections.swap(this.mData, i3, i3 - 1);
                }
            }
            notifyItemMoved(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
        }
        OnItemDragListener onItemDragListener = this.mOnItemDragListener;
        if (onItemDragListener != null && this.itemDragEnabled) {
            onItemDragListener.onItemDragMoving(viewHolder, viewHolderPosition, viewHolder2, viewHolderPosition2);
        }
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        OnItemDragListener onItemDragListener = this.mOnItemDragListener;
        if (onItemDragListener != null && this.itemDragEnabled) {
            onItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void setOnItemSwipeListener(OnItemSwipeListener onItemSwipeListener) {
        this.mOnItemSwipeListener = onItemSwipeListener;
    }

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        OnItemSwipeListener onItemSwipeListener = this.mOnItemSwipeListener;
        if (onItemSwipeListener != null && this.itemSwipeEnabled) {
            onItemSwipeListener.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        OnItemSwipeListener onItemSwipeListener = this.mOnItemSwipeListener;
        if (onItemSwipeListener != null && this.itemSwipeEnabled) {
            onItemSwipeListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        int viewHolderPosition = getViewHolderPosition(viewHolder);
        if (inRange(viewHolderPosition)) {
            this.mData.remove(viewHolderPosition);
            notifyItemRemoved(viewHolder.getAdapterPosition());
        }
        OnItemSwipeListener onItemSwipeListener = this.mOnItemSwipeListener;
        if (onItemSwipeListener != null && this.itemSwipeEnabled) {
            onItemSwipeListener.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, float f, float f2, boolean z) {
        OnItemSwipeListener onItemSwipeListener = this.mOnItemSwipeListener;
        if (onItemSwipeListener != null && this.itemSwipeEnabled) {
            onItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, f, f2, z);
        }
    }

    private boolean inRange(int i) {
        return i >= 0 && i < this.mData.size();
    }
}
