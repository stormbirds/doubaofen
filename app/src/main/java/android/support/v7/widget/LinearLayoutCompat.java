package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    /* access modifiers changed from: package-private */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, (AttributeSet) null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i5 = 0; i5 < virtualChildCount; i5++) {
            View virtualChildAt = getVirtualChildAt(i5);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i5))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    i4 = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    i4 = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, i4);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i3 = virtualChildAt2.getLeft() - layoutParams2.leftMargin;
                    i2 = this.mDividerWidth;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                    drawVerticalDivider(canvas, i);
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
                drawVerticalDivider(canvas, i);
            } else {
                i3 = getWidth() - getPaddingRight();
                i2 = this.mDividerWidth;
            }
            i = i3 - i2;
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: package-private */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        int i;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i2 = this.mBaselineAlignedChildIndex;
        if (childCount > i2) {
            View childAt = getChildAt(i2);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i3 = this.mBaselineChildTop;
                if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
                    if (i == 16) {
                        i3 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                    } else if (i == 80) {
                        i3 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                    }
                }
                return i3 + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: package-private */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: package-private */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        if (i == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                return true;
            }
            return false;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            for (int i2 = i - 1; i2 >= 0; i2--) {
                if (getChildAt(i2).getVisibility() != 8) {
                    return true;
                }
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x0335  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x0340  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x0342  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureVertical(int r34, int r35) {
        /*
            r33 = this;
            r7 = r33
            r8 = r34
            r9 = r35
            r10 = 0
            r7.mTotalLength = r10
            int r11 = r33.getVirtualChildCount()
            int r12 = android.view.View.MeasureSpec.getMode(r34)
            int r13 = android.view.View.MeasureSpec.getMode(r35)
            int r14 = r7.mBaselineAlignedChildIndex
            boolean r15 = r7.mUseLargestChild
            r16 = 0
            r17 = 1
            r0 = 0
            r1 = 0
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = 0
            r5 = 0
            r18 = 0
            r19 = 1
            r20 = 0
            r21 = 0
        L_0x002b:
            if (r5 >= r11) goto L_0x01a2
            android.view.View r4 = r7.getVirtualChildAt(r5)
            if (r4 != 0) goto L_0x0047
            int r4 = r7.mTotalLength
            int r22 = r7.measureNullChild(r5)
            int r4 = r4 + r22
            r7.mTotalLength = r4
            r22 = r11
            r28 = r21
        L_0x0041:
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r21 = r13
            goto L_0x0194
        L_0x0047:
            int r6 = r4.getVisibility()
            r26 = r1
            r1 = 8
            if (r6 != r1) goto L_0x005d
            int r1 = r7.getChildrenSkipCount(r4, r5)
            int r5 = r5 + r1
            r22 = r11
            r28 = r21
            r1 = r26
            goto L_0x0041
        L_0x005d:
            boolean r1 = r7.hasDividerBeforeChildAt(r5)
            if (r1 == 0) goto L_0x006a
            int r1 = r7.mTotalLength
            int r6 = r7.mDividerHeight
            int r1 = r1 + r6
            r7.mTotalLength = r1
        L_0x006a:
            android.view.ViewGroup$LayoutParams r1 = r4.getLayoutParams()
            r6 = r1
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r6 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r6
            float r1 = r6.weight
            float r23 = r0 + r1
            r1 = 1073741824(0x40000000, float:2.0)
            if (r13 != r1) goto L_0x00a8
            int r0 = r6.height
            if (r0 != 0) goto L_0x00a8
            float r0 = r6.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x00a8
            int r0 = r7.mTotalLength
            int r1 = r6.topMargin
            int r1 = r1 + r0
            r27 = r2
            int r2 = r6.bottomMargin
            int r1 = r1 + r2
            int r0 = java.lang.Math.max(r0, r1)
            r7.mTotalLength = r0
            r25 = r3
            r3 = r4
            r22 = r11
            r28 = r21
            r8 = r26
            r2 = r27
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r18 = 1
            r11 = r5
            r21 = r13
            r13 = r6
            goto L_0x011f
        L_0x00a8:
            r27 = r2
            int r0 = r6.height
            if (r0 != 0) goto L_0x00b9
            float r0 = r6.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x00b9
            r0 = -2
            r6.height = r0
            r2 = 0
            goto L_0x00bb
        L_0x00b9:
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x00bb:
            r28 = 0
            int r0 = (r23 > r16 ? 1 : (r23 == r16 ? 0 : -1))
            if (r0 != 0) goto L_0x00c6
            int r0 = r7.mTotalLength
            r29 = r0
            goto L_0x00c8
        L_0x00c6:
            r29 = 0
        L_0x00c8:
            r0 = r33
            r8 = r26
            r24 = 1073741824(0x40000000, float:2.0)
            r1 = r4
            r31 = r2
            r30 = r27
            r2 = r5
            r9 = r3
            r3 = r34
            r26 = r4
            r22 = r11
            r11 = 1073741824(0x40000000, float:2.0)
            r32 = r21
            r21 = r13
            r13 = r32
            r4 = r28
            r11 = r5
            r5 = r35
            r25 = r9
            r28 = r13
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r13 = r6
            r6 = r29
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            r0 = r31
            if (r0 == r9) goto L_0x00fa
            r13.height = r0
        L_0x00fa:
            int r0 = r26.getMeasuredHeight()
            int r1 = r7.mTotalLength
            int r2 = r1 + r0
            int r3 = r13.topMargin
            int r2 = r2 + r3
            int r3 = r13.bottomMargin
            int r2 = r2 + r3
            r3 = r26
            int r4 = r7.getNextLocationOffset(r3)
            int r2 = r2 + r4
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
            r6 = r30
            if (r15 == 0) goto L_0x011e
            int r2 = java.lang.Math.max(r0, r6)
            goto L_0x011f
        L_0x011e:
            r2 = r6
        L_0x011f:
            if (r14 < 0) goto L_0x0129
            int r5 = r11 + 1
            if (r14 != r5) goto L_0x0129
            int r0 = r7.mTotalLength
            r7.mBaselineChildTop = r0
        L_0x0129:
            if (r11 >= r14) goto L_0x013a
            float r0 = r13.weight
            int r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r0 > 0) goto L_0x0132
            goto L_0x013a
        L_0x0132:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex."
            r0.<init>(r1)
            throw r0
        L_0x013a:
            r0 = 1073741824(0x40000000, float:2.0)
            if (r12 == r0) goto L_0x0147
            int r0 = r13.width
            r1 = -1
            if (r0 != r1) goto L_0x0147
            r0 = 1
            r20 = 1
            goto L_0x0148
        L_0x0147:
            r0 = 0
        L_0x0148:
            int r1 = r13.leftMargin
            int r4 = r13.rightMargin
            int r1 = r1 + r4
            int r4 = r3.getMeasuredWidth()
            int r4 = r4 + r1
            int r5 = java.lang.Math.max(r8, r4)
            int r6 = r3.getMeasuredState()
            int r6 = android.view.View.combineMeasuredStates(r10, r6)
            if (r19 == 0) goto L_0x0167
            int r8 = r13.width
            r10 = -1
            if (r8 != r10) goto L_0x0167
            r8 = 1
            goto L_0x0168
        L_0x0167:
            r8 = 0
        L_0x0168:
            float r10 = r13.weight
            int r10 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1))
            if (r10 <= 0) goto L_0x017a
            if (r0 == 0) goto L_0x0171
            goto L_0x0172
        L_0x0171:
            r1 = r4
        L_0x0172:
            r13 = r25
            int r0 = java.lang.Math.max(r13, r1)
            r13 = r0
            goto L_0x0187
        L_0x017a:
            r13 = r25
            if (r0 == 0) goto L_0x017f
            r4 = r1
        L_0x017f:
            r1 = r28
            int r0 = java.lang.Math.max(r1, r4)
            r28 = r0
        L_0x0187:
            int r0 = r7.getChildrenSkipCount(r3, r11)
            int r0 = r0 + r11
            r1 = r5
            r10 = r6
            r19 = r8
            r3 = r13
            r5 = r0
            r0 = r23
        L_0x0194:
            int r5 = r5 + 1
            r8 = r34
            r9 = r35
            r13 = r21
            r11 = r22
            r21 = r28
            goto L_0x002b
        L_0x01a2:
            r8 = r1
            r6 = r2
            r22 = r11
            r1 = r21
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            r21 = r13
            r13 = r3
            int r2 = r7.mTotalLength
            if (r2 <= 0) goto L_0x01c1
            r2 = r22
            boolean r3 = r7.hasDividerBeforeChildAt(r2)
            if (r3 == 0) goto L_0x01c3
            int r3 = r7.mTotalLength
            int r4 = r7.mDividerHeight
            int r3 = r3 + r4
            r7.mTotalLength = r3
            goto L_0x01c3
        L_0x01c1:
            r2 = r22
        L_0x01c3:
            r3 = r21
            if (r15 == 0) goto L_0x0213
            if (r3 == r9) goto L_0x01cb
            if (r3 != 0) goto L_0x0213
        L_0x01cb:
            r4 = 0
            r7.mTotalLength = r4
            r4 = 0
        L_0x01cf:
            if (r4 >= r2) goto L_0x0213
            android.view.View r5 = r7.getVirtualChildAt(r4)
            if (r5 != 0) goto L_0x01e3
            int r5 = r7.mTotalLength
            int r9 = r7.measureNullChild(r4)
            int r5 = r5 + r9
            r7.mTotalLength = r5
            r21 = r4
            goto L_0x020e
        L_0x01e3:
            int r9 = r5.getVisibility()
            r11 = 8
            if (r9 != r11) goto L_0x01f1
            int r5 = r7.getChildrenSkipCount(r5, r4)
            int r4 = r4 + r5
            goto L_0x0210
        L_0x01f1:
            android.view.ViewGroup$LayoutParams r9 = r5.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r9 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r9
            int r11 = r7.mTotalLength
            int r14 = r11 + r6
            r21 = r4
            int r4 = r9.topMargin
            int r14 = r14 + r4
            int r4 = r9.bottomMargin
            int r14 = r14 + r4
            int r4 = r7.getNextLocationOffset(r5)
            int r14 = r14 + r4
            int r4 = java.lang.Math.max(r11, r14)
            r7.mTotalLength = r4
        L_0x020e:
            r4 = r21
        L_0x0210:
            int r4 = r4 + 1
            goto L_0x01cf
        L_0x0213:
            int r4 = r7.mTotalLength
            int r5 = r33.getPaddingTop()
            int r9 = r33.getPaddingBottom()
            int r5 = r5 + r9
            int r4 = r4 + r5
            r7.mTotalLength = r4
            int r4 = r7.mTotalLength
            int r5 = r33.getSuggestedMinimumHeight()
            int r4 = java.lang.Math.max(r4, r5)
            r5 = r35
            r9 = r13
            r11 = 0
            int r4 = android.view.View.resolveSizeAndState(r4, r5, r11)
            r11 = 16777215(0xffffff, float:2.3509886E-38)
            r11 = r11 & r4
            int r13 = r7.mTotalLength
            int r11 = r11 - r13
            if (r18 != 0) goto L_0x0284
            if (r11 == 0) goto L_0x0243
            int r13 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1))
            if (r13 <= 0) goto L_0x0243
            goto L_0x0284
        L_0x0243:
            int r0 = java.lang.Math.max(r1, r9)
            if (r15 == 0) goto L_0x027f
            r1 = 1073741824(0x40000000, float:2.0)
            if (r3 == r1) goto L_0x027f
            r1 = 0
        L_0x024e:
            if (r1 >= r2) goto L_0x027f
            android.view.View r3 = r7.getVirtualChildAt(r1)
            if (r3 == 0) goto L_0x027c
            int r9 = r3.getVisibility()
            r11 = 8
            if (r9 != r11) goto L_0x025f
            goto L_0x027c
        L_0x025f:
            android.view.ViewGroup$LayoutParams r9 = r3.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r9 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r9
            float r9 = r9.weight
            int r9 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
            if (r9 <= 0) goto L_0x027c
            int r9 = r3.getMeasuredWidth()
            r11 = 1073741824(0x40000000, float:2.0)
            int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r11)
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r11)
            r3.measure(r9, r13)
        L_0x027c:
            int r1 = r1 + 1
            goto L_0x024e
        L_0x027f:
            r11 = r34
            r1 = r8
            goto L_0x037b
        L_0x0284:
            float r6 = r7.mWeightSum
            int r9 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r9 <= 0) goto L_0x028b
            r0 = r6
        L_0x028b:
            r6 = 0
            r7.mTotalLength = r6
            r9 = r0
            r0 = 0
            r32 = r8
            r8 = r1
            r1 = r32
        L_0x0295:
            if (r0 >= r2) goto L_0x036a
            android.view.View r13 = r7.getVirtualChildAt(r0)
            int r14 = r13.getVisibility()
            r15 = 8
            if (r14 != r15) goto L_0x02aa
            r22 = r3
            r6 = r11
            r11 = r34
            goto L_0x0362
        L_0x02aa:
            android.view.ViewGroup$LayoutParams r14 = r13.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r14 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r14
            float r6 = r14.weight
            int r18 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1))
            if (r18 <= 0) goto L_0x030d
            float r15 = (float) r11
            float r15 = r15 * r6
            float r15 = r15 / r9
            int r15 = (int) r15
            float r9 = r9 - r6
            int r11 = r11 - r15
            int r6 = r33.getPaddingLeft()
            int r18 = r33.getPaddingRight()
            int r6 = r6 + r18
            r18 = r9
            int r9 = r14.leftMargin
            int r6 = r6 + r9
            int r9 = r14.rightMargin
            int r6 = r6 + r9
            int r9 = r14.width
            r21 = r11
            r11 = r34
            int r6 = getChildMeasureSpec(r11, r6, r9)
            int r9 = r14.height
            if (r9 != 0) goto L_0x02ee
            r9 = 1073741824(0x40000000, float:2.0)
            if (r3 == r9) goto L_0x02e2
            goto L_0x02f0
        L_0x02e2:
            if (r15 <= 0) goto L_0x02e5
            goto L_0x02e6
        L_0x02e5:
            r15 = 0
        L_0x02e6:
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r9)
            r13.measure(r6, r15)
            goto L_0x0300
        L_0x02ee:
            r9 = 1073741824(0x40000000, float:2.0)
        L_0x02f0:
            int r22 = r13.getMeasuredHeight()
            int r15 = r22 + r15
            if (r15 >= 0) goto L_0x02f9
            r15 = 0
        L_0x02f9:
            int r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r9)
            r13.measure(r6, r15)
        L_0x0300:
            int r6 = r13.getMeasuredState()
            r6 = r6 & -256(0xffffffffffffff00, float:NaN)
            int r10 = android.view.View.combineMeasuredStates(r10, r6)
            r6 = r21
            goto L_0x0312
        L_0x030d:
            r6 = r11
            r11 = r34
            r18 = r9
        L_0x0312:
            int r9 = r14.leftMargin
            int r15 = r14.rightMargin
            int r9 = r9 + r15
            int r15 = r13.getMeasuredWidth()
            int r15 = r15 + r9
            int r1 = java.lang.Math.max(r1, r15)
            r21 = r1
            r1 = 1073741824(0x40000000, float:2.0)
            if (r12 == r1) goto L_0x032f
            int r1 = r14.width
            r22 = r3
            r3 = -1
            if (r1 != r3) goto L_0x0332
            r1 = 1
            goto L_0x0333
        L_0x032f:
            r22 = r3
            r3 = -1
        L_0x0332:
            r1 = 0
        L_0x0333:
            if (r1 == 0) goto L_0x0336
            r15 = r9
        L_0x0336:
            int r1 = java.lang.Math.max(r8, r15)
            if (r19 == 0) goto L_0x0342
            int r8 = r14.width
            if (r8 != r3) goto L_0x0342
            r8 = 1
            goto L_0x0343
        L_0x0342:
            r8 = 0
        L_0x0343:
            int r9 = r7.mTotalLength
            int r15 = r13.getMeasuredHeight()
            int r15 = r15 + r9
            int r3 = r14.topMargin
            int r15 = r15 + r3
            int r3 = r14.bottomMargin
            int r15 = r15 + r3
            int r3 = r7.getNextLocationOffset(r13)
            int r15 = r15 + r3
            int r3 = java.lang.Math.max(r9, r15)
            r7.mTotalLength = r3
            r19 = r8
            r9 = r18
            r8 = r1
            r1 = r21
        L_0x0362:
            int r0 = r0 + 1
            r11 = r6
            r3 = r22
            r6 = 0
            goto L_0x0295
        L_0x036a:
            r11 = r34
            int r0 = r7.mTotalLength
            int r3 = r33.getPaddingTop()
            int r6 = r33.getPaddingBottom()
            int r3 = r3 + r6
            int r0 = r0 + r3
            r7.mTotalLength = r0
            r0 = r8
        L_0x037b:
            if (r19 != 0) goto L_0x0382
            r3 = 1073741824(0x40000000, float:2.0)
            if (r12 == r3) goto L_0x0382
            goto L_0x0383
        L_0x0382:
            r0 = r1
        L_0x0383:
            int r1 = r33.getPaddingLeft()
            int r3 = r33.getPaddingRight()
            int r1 = r1 + r3
            int r0 = r0 + r1
            int r1 = r33.getSuggestedMinimumWidth()
            int r0 = java.lang.Math.max(r0, r1)
            int r0 = android.view.View.resolveSizeAndState(r0, r11, r10)
            r7.setMeasuredDimension(r0, r4)
            if (r20 == 0) goto L_0x03a1
            r7.forceUniformWidth(r2, r5)
        L_0x03a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.LinearLayoutCompat.measureVertical(int, int):void");
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0441  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x0466  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0175  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x019a  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x019d  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01c7  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01d0  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01db  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureHorizontal(int r38, int r39) {
        /*
            r37 = this;
            r7 = r37
            r8 = r38
            r9 = r39
            r10 = 0
            r7.mTotalLength = r10
            int r11 = r37.getVirtualChildCount()
            int r12 = android.view.View.MeasureSpec.getMode(r38)
            int r13 = android.view.View.MeasureSpec.getMode(r39)
            int[] r0 = r7.mMaxAscent
            r14 = 4
            if (r0 == 0) goto L_0x001e
            int[] r0 = r7.mMaxDescent
            if (r0 != 0) goto L_0x0026
        L_0x001e:
            int[] r0 = new int[r14]
            r7.mMaxAscent = r0
            int[] r0 = new int[r14]
            r7.mMaxDescent = r0
        L_0x0026:
            int[] r15 = r7.mMaxAscent
            int[] r6 = r7.mMaxDescent
            r16 = 3
            r5 = -1
            r15[r16] = r5
            r17 = 2
            r15[r17] = r5
            r18 = 1
            r15[r18] = r5
            r15[r10] = r5
            r6[r16] = r5
            r6[r17] = r5
            r6[r18] = r5
            r6[r10] = r5
            boolean r4 = r7.mBaselineAligned
            boolean r3 = r7.mUseLargestChild
            r2 = 1073741824(0x40000000, float:2.0)
            if (r12 != r2) goto L_0x004c
            r19 = 1
            goto L_0x004e
        L_0x004c:
            r19 = 0
        L_0x004e:
            r20 = 0
            r0 = 0
            r14 = -2147483648(0xffffffff80000000, float:-0.0)
            r21 = 0
            r22 = 0
            r23 = 0
            r24 = 0
            r25 = 0
            r27 = 0
            r28 = 1
            r29 = 0
        L_0x0063:
            r30 = r6
            r1 = 8
            if (r0 >= r11) goto L_0x0208
            android.view.View r6 = r7.getVirtualChildAt(r0)
            if (r6 != 0) goto L_0x007f
            int r1 = r7.mTotalLength
            int r6 = r7.measureNullChild(r0)
            int r1 = r1 + r6
            r7.mTotalLength = r1
        L_0x0078:
            r33 = r3
            r36 = r4
            r8 = -1
            goto L_0x01f6
        L_0x007f:
            int r5 = r6.getVisibility()
            if (r5 != r1) goto L_0x008b
            int r1 = r7.getChildrenSkipCount(r6, r0)
            int r0 = r0 + r1
            goto L_0x0078
        L_0x008b:
            boolean r1 = r7.hasDividerBeforeChildAt(r0)
            if (r1 == 0) goto L_0x0098
            int r1 = r7.mTotalLength
            int r5 = r7.mDividerWidth
            int r1 = r1 + r5
            r7.mTotalLength = r1
        L_0x0098:
            android.view.ViewGroup$LayoutParams r1 = r6.getLayoutParams()
            r5 = r1
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r5 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r5
            float r1 = r5.weight
            float r21 = r21 + r1
            if (r12 != r2) goto L_0x00f2
            int r1 = r5.width
            if (r1 != 0) goto L_0x00f2
            float r1 = r5.weight
            int r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1))
            if (r1 <= 0) goto L_0x00f2
            if (r19 == 0) goto L_0x00bc
            int r1 = r7.mTotalLength
            int r2 = r5.leftMargin
            int r10 = r5.rightMargin
            int r2 = r2 + r10
            int r1 = r1 + r2
            r7.mTotalLength = r1
            goto L_0x00ca
        L_0x00bc:
            int r1 = r7.mTotalLength
            int r2 = r5.leftMargin
            int r2 = r2 + r1
            int r10 = r5.rightMargin
            int r2 = r2 + r10
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
        L_0x00ca:
            if (r4 == 0) goto L_0x00e1
            r1 = 0
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r1)
            r6.measure(r2, r2)
            r35 = r0
            r33 = r3
            r36 = r4
            r9 = r5
            r3 = r6
            r8 = -1
            r32 = -2
            goto L_0x016d
        L_0x00e1:
            r35 = r0
            r33 = r3
            r36 = r4
            r9 = r5
            r3 = r6
            r0 = 1073741824(0x40000000, float:2.0)
            r8 = -1
            r25 = 1
            r32 = -2
            goto L_0x016f
        L_0x00f2:
            int r1 = r5.width
            if (r1 != 0) goto L_0x0101
            float r1 = r5.weight
            int r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1))
            if (r1 <= 0) goto L_0x0101
            r10 = -2
            r5.width = r10
            r2 = 0
            goto L_0x0104
        L_0x0101:
            r10 = -2
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x0104:
            int r1 = (r21 > r20 ? 1 : (r21 == r20 ? 0 : -1))
            if (r1 != 0) goto L_0x010d
            int r1 = r7.mTotalLength
            r32 = r1
            goto L_0x010f
        L_0x010d:
            r32 = 0
        L_0x010f:
            r34 = 0
            r1 = r0
            r0 = r37
            r35 = r1
            r10 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r6
            r10 = r2
            r2 = r35
            r33 = r3
            r3 = r38
            r36 = r4
            r4 = r32
            r9 = r5
            r8 = -1
            r5 = r39
            r31 = r6
            r32 = -2
            r6 = r34
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            r0 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r10 == r0) goto L_0x0137
            r9.width = r10
        L_0x0137:
            int r0 = r31.getMeasuredWidth()
            if (r19 == 0) goto L_0x0150
            int r1 = r7.mTotalLength
            int r2 = r9.leftMargin
            int r2 = r2 + r0
            int r3 = r9.rightMargin
            int r2 = r2 + r3
            r3 = r31
            int r4 = r7.getNextLocationOffset(r3)
            int r2 = r2 + r4
            int r1 = r1 + r2
            r7.mTotalLength = r1
            goto L_0x0167
        L_0x0150:
            r3 = r31
            int r1 = r7.mTotalLength
            int r2 = r1 + r0
            int r4 = r9.leftMargin
            int r2 = r2 + r4
            int r4 = r9.rightMargin
            int r2 = r2 + r4
            int r4 = r7.getNextLocationOffset(r3)
            int r2 = r2 + r4
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
        L_0x0167:
            if (r33 == 0) goto L_0x016d
            int r14 = java.lang.Math.max(r0, r14)
        L_0x016d:
            r0 = 1073741824(0x40000000, float:2.0)
        L_0x016f:
            if (r13 == r0) goto L_0x0179
            int r1 = r9.height
            if (r1 != r8) goto L_0x0179
            r1 = 1
            r29 = 1
            goto L_0x017a
        L_0x0179:
            r1 = 0
        L_0x017a:
            int r2 = r9.topMargin
            int r4 = r9.bottomMargin
            int r2 = r2 + r4
            int r4 = r3.getMeasuredHeight()
            int r4 = r4 + r2
            int r5 = r3.getMeasuredState()
            r10 = r27
            int r5 = android.view.View.combineMeasuredStates(r10, r5)
            if (r36 == 0) goto L_0x01bb
            int r6 = r3.getBaseline()
            if (r6 == r8) goto L_0x01bb
            int r10 = r9.gravity
            if (r10 >= 0) goto L_0x019d
            int r10 = r7.mGravity
            goto L_0x019f
        L_0x019d:
            int r10 = r9.gravity
        L_0x019f:
            r10 = r10 & 112(0x70, float:1.57E-43)
            r26 = 4
            int r10 = r10 >> 4
            r10 = r10 & -2
            int r10 = r10 >> 1
            r0 = r15[r10]
            int r0 = java.lang.Math.max(r0, r6)
            r15[r10] = r0
            r0 = r30[r10]
            int r6 = r4 - r6
            int r0 = java.lang.Math.max(r0, r6)
            r30[r10] = r0
        L_0x01bb:
            r0 = r22
            int r0 = java.lang.Math.max(r0, r4)
            if (r28 == 0) goto L_0x01c9
            int r6 = r9.height
            if (r6 != r8) goto L_0x01c9
            r6 = 1
            goto L_0x01ca
        L_0x01c9:
            r6 = 0
        L_0x01ca:
            float r9 = r9.weight
            int r9 = (r9 > r20 ? 1 : (r9 == r20 ? 0 : -1))
            if (r9 <= 0) goto L_0x01db
            if (r1 == 0) goto L_0x01d3
            goto L_0x01d4
        L_0x01d3:
            r2 = r4
        L_0x01d4:
            r9 = r24
            int r24 = java.lang.Math.max(r9, r2)
            goto L_0x01e8
        L_0x01db:
            r9 = r24
            if (r1 == 0) goto L_0x01e0
            r4 = r2
        L_0x01e0:
            r2 = r23
            int r23 = java.lang.Math.max(r2, r4)
            r24 = r9
        L_0x01e8:
            r10 = r35
            int r1 = r7.getChildrenSkipCount(r3, r10)
            int r1 = r1 + r10
            r22 = r0
            r0 = r1
            r27 = r5
            r28 = r6
        L_0x01f6:
            int r0 = r0 + 1
            r8 = r38
            r9 = r39
            r6 = r30
            r3 = r33
            r4 = r36
            r2 = 1073741824(0x40000000, float:2.0)
            r5 = -1
            r10 = 0
            goto L_0x0063
        L_0x0208:
            r33 = r3
            r36 = r4
            r0 = r22
            r2 = r23
            r9 = r24
            r10 = r27
            r8 = -1
            r32 = -2
            int r3 = r7.mTotalLength
            if (r3 <= 0) goto L_0x0228
            boolean r3 = r7.hasDividerBeforeChildAt(r11)
            if (r3 == 0) goto L_0x0228
            int r3 = r7.mTotalLength
            int r4 = r7.mDividerWidth
            int r3 = r3 + r4
            r7.mTotalLength = r3
        L_0x0228:
            r3 = r15[r18]
            if (r3 != r8) goto L_0x0239
            r3 = 0
            r4 = r15[r3]
            if (r4 != r8) goto L_0x0239
            r3 = r15[r17]
            if (r3 != r8) goto L_0x0239
            r3 = r15[r16]
            if (r3 == r8) goto L_0x0269
        L_0x0239:
            r3 = r15[r16]
            r4 = 0
            r5 = r15[r4]
            r6 = r15[r18]
            r8 = r15[r17]
            int r6 = java.lang.Math.max(r6, r8)
            int r5 = java.lang.Math.max(r5, r6)
            int r3 = java.lang.Math.max(r3, r5)
            r5 = r30[r16]
            r6 = r30[r4]
            r4 = r30[r18]
            r8 = r30[r17]
            int r4 = java.lang.Math.max(r4, r8)
            int r4 = java.lang.Math.max(r6, r4)
            int r4 = java.lang.Math.max(r5, r4)
            int r3 = r3 + r4
            int r22 = java.lang.Math.max(r0, r3)
            r0 = r22
        L_0x0269:
            if (r33 == 0) goto L_0x02c6
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r12 == r3) goto L_0x0271
            if (r12 != 0) goto L_0x02c6
        L_0x0271:
            r3 = 0
            r7.mTotalLength = r3
            r3 = 0
        L_0x0275:
            if (r3 >= r11) goto L_0x02c6
            android.view.View r4 = r7.getVirtualChildAt(r3)
            if (r4 != 0) goto L_0x0287
            int r4 = r7.mTotalLength
            int r5 = r7.measureNullChild(r3)
            int r4 = r4 + r5
            r7.mTotalLength = r4
            goto L_0x02c1
        L_0x0287:
            int r5 = r4.getVisibility()
            if (r5 != r1) goto L_0x0293
            int r4 = r7.getChildrenSkipCount(r4, r3)
            int r3 = r3 + r4
            goto L_0x02c1
        L_0x0293:
            android.view.ViewGroup$LayoutParams r5 = r4.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r5 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r5
            if (r19 == 0) goto L_0x02ac
            int r6 = r7.mTotalLength
            int r8 = r5.leftMargin
            int r8 = r8 + r14
            int r5 = r5.rightMargin
            int r8 = r8 + r5
            int r4 = r7.getNextLocationOffset(r4)
            int r8 = r8 + r4
            int r6 = r6 + r8
            r7.mTotalLength = r6
            goto L_0x02c1
        L_0x02ac:
            int r6 = r7.mTotalLength
            int r8 = r6 + r14
            int r1 = r5.leftMargin
            int r8 = r8 + r1
            int r1 = r5.rightMargin
            int r8 = r8 + r1
            int r1 = r7.getNextLocationOffset(r4)
            int r8 = r8 + r1
            int r1 = java.lang.Math.max(r6, r8)
            r7.mTotalLength = r1
        L_0x02c1:
            int r3 = r3 + 1
            r1 = 8
            goto L_0x0275
        L_0x02c6:
            int r1 = r7.mTotalLength
            int r3 = r37.getPaddingLeft()
            int r4 = r37.getPaddingRight()
            int r3 = r3 + r4
            int r1 = r1 + r3
            r7.mTotalLength = r1
            int r1 = r7.mTotalLength
            int r3 = r37.getSuggestedMinimumWidth()
            int r1 = java.lang.Math.max(r1, r3)
            r3 = r38
            r4 = 0
            r8 = -1
            int r1 = android.view.View.resolveSizeAndState(r1, r3, r4)
            r4 = 16777215(0xffffff, float:2.3509886E-38)
            r4 = r4 & r1
            int r5 = r7.mTotalLength
            int r4 = r4 - r5
            if (r25 != 0) goto L_0x0336
            if (r4 == 0) goto L_0x02f6
            int r6 = (r21 > r20 ? 1 : (r21 == r20 ? 0 : -1))
            if (r6 <= 0) goto L_0x02f6
            goto L_0x0336
        L_0x02f6:
            int r2 = java.lang.Math.max(r2, r9)
            if (r33 == 0) goto L_0x0332
            r4 = 1073741824(0x40000000, float:2.0)
            if (r12 == r4) goto L_0x0332
            r4 = 0
        L_0x0301:
            if (r4 >= r11) goto L_0x0332
            android.view.View r6 = r7.getVirtualChildAt(r4)
            if (r6 == 0) goto L_0x032f
            int r8 = r6.getVisibility()
            r9 = 8
            if (r8 != r9) goto L_0x0312
            goto L_0x032f
        L_0x0312:
            android.view.ViewGroup$LayoutParams r8 = r6.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r8 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r8
            float r8 = r8.weight
            int r8 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1))
            if (r8 <= 0) goto L_0x032f
            r8 = 1073741824(0x40000000, float:2.0)
            int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r8)
            int r12 = r6.getMeasuredHeight()
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r12, r8)
            r6.measure(r9, r12)
        L_0x032f:
            int r4 = r4 + 1
            goto L_0x0301
        L_0x0332:
            r4 = r39
            goto L_0x04d1
        L_0x0336:
            float r0 = r7.mWeightSum
            int r6 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1))
            if (r6 <= 0) goto L_0x033d
            goto L_0x033f
        L_0x033d:
            r0 = r21
        L_0x033f:
            r15[r16] = r8
            r15[r17] = r8
            r15[r18] = r8
            r6 = 0
            r15[r6] = r8
            r30[r16] = r8
            r30[r17] = r8
            r30[r18] = r8
            r30[r6] = r8
            r7.mTotalLength = r6
            r9 = r2
            r6 = -1
            r2 = r0
            r0 = 0
        L_0x0356:
            if (r0 >= r11) goto L_0x047d
            android.view.View r14 = r7.getVirtualChildAt(r0)
            if (r14 == 0) goto L_0x0470
            int r8 = r14.getVisibility()
            r5 = 8
            if (r8 != r5) goto L_0x0368
            goto L_0x0470
        L_0x0368:
            android.view.ViewGroup$LayoutParams r8 = r14.getLayoutParams()
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r8 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r8
            float r5 = r8.weight
            int r23 = (r5 > r20 ? 1 : (r5 == r20 ? 0 : -1))
            if (r23 <= 0) goto L_0x03ce
            float r3 = (float) r4
            float r3 = r3 * r5
            float r3 = r3 / r2
            int r3 = (int) r3
            float r2 = r2 - r5
            int r4 = r4 - r3
            int r5 = r37.getPaddingTop()
            int r23 = r37.getPaddingBottom()
            int r5 = r5 + r23
            r23 = r2
            int r2 = r8.topMargin
            int r5 = r5 + r2
            int r2 = r8.bottomMargin
            int r5 = r5 + r2
            int r2 = r8.height
            r24 = r4
            r4 = r39
            int r2 = getChildMeasureSpec(r4, r5, r2)
            int r5 = r8.width
            if (r5 != 0) goto L_0x03ac
            r5 = 1073741824(0x40000000, float:2.0)
            if (r12 == r5) goto L_0x03a0
            goto L_0x03ae
        L_0x03a0:
            if (r3 <= 0) goto L_0x03a3
            goto L_0x03a4
        L_0x03a3:
            r3 = 0
        L_0x03a4:
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r5)
            r14.measure(r3, r2)
            goto L_0x03be
        L_0x03ac:
            r5 = 1073741824(0x40000000, float:2.0)
        L_0x03ae:
            int r25 = r14.getMeasuredWidth()
            int r3 = r25 + r3
            if (r3 >= 0) goto L_0x03b7
            r3 = 0
        L_0x03b7:
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r5)
            r14.measure(r3, r2)
        L_0x03be:
            int r2 = r14.getMeasuredState()
            r3 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r2 = r2 & r3
            int r10 = android.view.View.combineMeasuredStates(r10, r2)
            r2 = r23
            r3 = r24
            goto L_0x03d1
        L_0x03ce:
            r3 = r4
            r4 = r39
        L_0x03d1:
            if (r19 == 0) goto L_0x03f0
            int r5 = r7.mTotalLength
            int r23 = r14.getMeasuredWidth()
            r24 = r2
            int r2 = r8.leftMargin
            int r23 = r23 + r2
            int r2 = r8.rightMargin
            int r23 = r23 + r2
            int r2 = r7.getNextLocationOffset(r14)
            int r23 = r23 + r2
            int r5 = r5 + r23
            r7.mTotalLength = r5
            r23 = r3
            goto L_0x040c
        L_0x03f0:
            r24 = r2
            int r2 = r7.mTotalLength
            int r5 = r14.getMeasuredWidth()
            int r5 = r5 + r2
            r23 = r3
            int r3 = r8.leftMargin
            int r5 = r5 + r3
            int r3 = r8.rightMargin
            int r5 = r5 + r3
            int r3 = r7.getNextLocationOffset(r14)
            int r5 = r5 + r3
            int r2 = java.lang.Math.max(r2, r5)
            r7.mTotalLength = r2
        L_0x040c:
            r2 = 1073741824(0x40000000, float:2.0)
            if (r13 == r2) goto L_0x0417
            int r2 = r8.height
            r3 = -1
            if (r2 != r3) goto L_0x0417
            r2 = 1
            goto L_0x0418
        L_0x0417:
            r2 = 0
        L_0x0418:
            int r3 = r8.topMargin
            int r5 = r8.bottomMargin
            int r3 = r3 + r5
            int r5 = r14.getMeasuredHeight()
            int r5 = r5 + r3
            int r6 = java.lang.Math.max(r6, r5)
            if (r2 == 0) goto L_0x0429
            goto L_0x042a
        L_0x0429:
            r3 = r5
        L_0x042a:
            int r2 = java.lang.Math.max(r9, r3)
            if (r28 == 0) goto L_0x0437
            int r3 = r8.height
            r9 = -1
            if (r3 != r9) goto L_0x0438
            r3 = 1
            goto L_0x0439
        L_0x0437:
            r9 = -1
        L_0x0438:
            r3 = 0
        L_0x0439:
            if (r36 == 0) goto L_0x0466
            int r14 = r14.getBaseline()
            if (r14 == r9) goto L_0x0466
            int r9 = r8.gravity
            if (r9 >= 0) goto L_0x0448
            int r8 = r7.mGravity
            goto L_0x044a
        L_0x0448:
            int r8 = r8.gravity
        L_0x044a:
            r8 = r8 & 112(0x70, float:1.57E-43)
            r25 = 4
            int r8 = r8 >> 4
            r8 = r8 & -2
            int r8 = r8 >> 1
            r9 = r15[r8]
            int r9 = java.lang.Math.max(r9, r14)
            r15[r8] = r9
            r9 = r30[r8]
            int r5 = r5 - r14
            int r5 = java.lang.Math.max(r9, r5)
            r30[r8] = r5
            goto L_0x0468
        L_0x0466:
            r25 = 4
        L_0x0468:
            r9 = r2
            r28 = r3
            r3 = r23
            r2 = r24
            goto L_0x0475
        L_0x0470:
            r3 = r4
            r25 = 4
            r4 = r39
        L_0x0475:
            int r0 = r0 + 1
            r4 = r3
            r8 = -1
            r3 = r38
            goto L_0x0356
        L_0x047d:
            r4 = r39
            int r0 = r7.mTotalLength
            int r2 = r37.getPaddingLeft()
            int r3 = r37.getPaddingRight()
            int r2 = r2 + r3
            int r0 = r0 + r2
            r7.mTotalLength = r0
            r0 = r15[r18]
            r2 = -1
            if (r0 != r2) goto L_0x04a2
            r0 = 0
            r3 = r15[r0]
            if (r3 != r2) goto L_0x04a2
            r0 = r15[r17]
            if (r0 != r2) goto L_0x04a2
            r0 = r15[r16]
            if (r0 == r2) goto L_0x04a0
            goto L_0x04a2
        L_0x04a0:
            r0 = r6
            goto L_0x04d0
        L_0x04a2:
            r0 = r15[r16]
            r2 = 0
            r3 = r15[r2]
            r5 = r15[r18]
            r8 = r15[r17]
            int r5 = java.lang.Math.max(r5, r8)
            int r3 = java.lang.Math.max(r3, r5)
            int r0 = java.lang.Math.max(r0, r3)
            r3 = r30[r16]
            r2 = r30[r2]
            r5 = r30[r18]
            r8 = r30[r17]
            int r5 = java.lang.Math.max(r5, r8)
            int r2 = java.lang.Math.max(r2, r5)
            int r2 = java.lang.Math.max(r3, r2)
            int r0 = r0 + r2
            int r0 = java.lang.Math.max(r6, r0)
        L_0x04d0:
            r2 = r9
        L_0x04d1:
            if (r28 != 0) goto L_0x04d8
            r3 = 1073741824(0x40000000, float:2.0)
            if (r13 == r3) goto L_0x04d8
            r0 = r2
        L_0x04d8:
            int r2 = r37.getPaddingTop()
            int r3 = r37.getPaddingBottom()
            int r2 = r2 + r3
            int r0 = r0 + r2
            int r2 = r37.getSuggestedMinimumHeight()
            int r0 = java.lang.Math.max(r0, r2)
            r2 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r2 = r2 & r10
            r1 = r1 | r2
            int r2 = r10 << 16
            int r0 = android.view.View.resolveSizeAndState(r0, r4, r2)
            r7.setMeasuredDimension(r1, r0)
            if (r29 == 0) goto L_0x04fe
            r0 = r38
            r7.forceUniformHeight(r11, r0)
        L_0x04fe:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.LinearLayoutCompat.measureHorizontal(int, int):void");
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: package-private */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int paddingLeft = getPaddingLeft();
        int i9 = i3 - i;
        int paddingRight = i9 - getPaddingRight();
        int paddingRight2 = (i9 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i10 = this.mGravity;
        int i11 = i10 & 112;
        int i12 = i10 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i11 == 16) {
            i5 = getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        } else if (i11 != 80) {
            i5 = getPaddingTop();
        } else {
            i5 = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        }
        int i13 = 0;
        while (i13 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i13);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i13);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i14 = layoutParams.gravity;
                if (i14 < 0) {
                    i14 = i12;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i14, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i7 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                    i6 = layoutParams.rightMargin;
                    i8 = i7 - i6;
                } else if (absoluteGravity != 5) {
                    i8 = layoutParams.leftMargin + paddingLeft;
                } else {
                    i7 = paddingRight - measuredWidth;
                    i6 = layoutParams.rightMargin;
                    i8 = i7 - i6;
                }
                int i15 = i8;
                if (hasDividerBeforeChildAt(i13)) {
                    i5 += this.mDividerHeight;
                }
                int i16 = i5 + layoutParams.topMargin;
                setChildFrame(virtualChildAt, i15, i16 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i13 += getChildrenSkipCount(virtualChildAt, i13);
                i5 = i16 + measuredHeight + layoutParams.bottomMargin + getNextLocationOffset(virtualChildAt);
            }
            i13++;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00f8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutHorizontal(int r25, int r26, int r27, int r28) {
        /*
            r24 = this;
            r6 = r24
            boolean r0 = android.support.v7.widget.ViewUtils.isLayoutRtl(r24)
            int r7 = r24.getPaddingTop()
            int r1 = r28 - r26
            int r2 = r24.getPaddingBottom()
            int r8 = r1 - r2
            int r1 = r1 - r7
            int r2 = r24.getPaddingBottom()
            int r9 = r1 - r2
            int r10 = r24.getVirtualChildCount()
            int r1 = r6.mGravity
            r2 = 8388615(0x800007, float:1.1754953E-38)
            r2 = r2 & r1
            r11 = r1 & 112(0x70, float:1.57E-43)
            boolean r12 = r6.mBaselineAligned
            int[] r13 = r6.mMaxAscent
            int[] r14 = r6.mMaxDescent
            int r1 = android.support.v4.view.ViewCompat.getLayoutDirection(r24)
            int r1 = android.support.v4.view.GravityCompat.getAbsoluteGravity(r2, r1)
            r15 = 2
            r5 = 1
            if (r1 == r5) goto L_0x004b
            r2 = 5
            if (r1 == r2) goto L_0x003f
            int r1 = r24.getPaddingLeft()
            goto L_0x0056
        L_0x003f:
            int r1 = r24.getPaddingLeft()
            int r1 = r1 + r27
            int r1 = r1 - r25
            int r2 = r6.mTotalLength
            int r1 = r1 - r2
            goto L_0x0056
        L_0x004b:
            int r1 = r24.getPaddingLeft()
            int r2 = r27 - r25
            int r3 = r6.mTotalLength
            int r2 = r2 - r3
            int r2 = r2 / r15
            int r1 = r1 + r2
        L_0x0056:
            r2 = 0
            if (r0 == 0) goto L_0x0060
            int r0 = r10 + -1
            r16 = r0
            r17 = -1
            goto L_0x0064
        L_0x0060:
            r16 = 0
            r17 = 1
        L_0x0064:
            r3 = 0
        L_0x0065:
            if (r3 >= r10) goto L_0x0140
            int r0 = r17 * r3
            int r2 = r16 + r0
            android.view.View r0 = r6.getVirtualChildAt(r2)
            if (r0 != 0) goto L_0x0078
            int r0 = r6.measureNullChild(r2)
            int r1 = r1 + r0
            goto L_0x012a
        L_0x0078:
            int r5 = r0.getVisibility()
            r15 = 8
            if (r5 == r15) goto L_0x0128
            int r15 = r0.getMeasuredWidth()
            int r5 = r0.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r18 = r0.getLayoutParams()
            r4 = r18
            android.support.v7.widget.LinearLayoutCompat$LayoutParams r4 = (android.support.v7.widget.LinearLayoutCompat.LayoutParams) r4
            r18 = r3
            if (r12 == 0) goto L_0x00a0
            int r3 = r4.height
            r19 = r10
            r10 = -1
            if (r3 == r10) goto L_0x00a2
            int r3 = r0.getBaseline()
            goto L_0x00a3
        L_0x00a0:
            r19 = r10
        L_0x00a2:
            r3 = -1
        L_0x00a3:
            int r10 = r4.gravity
            if (r10 >= 0) goto L_0x00a8
            r10 = r11
        L_0x00a8:
            r10 = r10 & 112(0x70, float:1.57E-43)
            r20 = r11
            r11 = 16
            if (r10 == r11) goto L_0x00e4
            r11 = 48
            if (r10 == r11) goto L_0x00d4
            r11 = 80
            if (r10 == r11) goto L_0x00bd
            r3 = r7
            r11 = -1
        L_0x00ba:
            r21 = 1
            goto L_0x00f2
        L_0x00bd:
            int r10 = r8 - r5
            int r11 = r4.bottomMargin
            int r10 = r10 - r11
            r11 = -1
            if (r3 == r11) goto L_0x00d2
            int r21 = r0.getMeasuredHeight()
            int r21 = r21 - r3
            r3 = 2
            r22 = r14[r3]
            int r22 = r22 - r21
            int r10 = r10 - r22
        L_0x00d2:
            r3 = r10
            goto L_0x00ba
        L_0x00d4:
            r11 = -1
            int r10 = r4.topMargin
            int r10 = r10 + r7
            r21 = 1
            if (r3 == r11) goto L_0x00e2
            r22 = r13[r21]
            int r22 = r22 - r3
            int r10 = r10 + r22
        L_0x00e2:
            r3 = r10
            goto L_0x00f2
        L_0x00e4:
            r11 = -1
            r21 = 1
            int r3 = r9 - r5
            r10 = 2
            int r3 = r3 / r10
            int r3 = r3 + r7
            int r10 = r4.topMargin
            int r3 = r3 + r10
            int r10 = r4.bottomMargin
            int r3 = r3 - r10
        L_0x00f2:
            boolean r10 = r6.hasDividerBeforeChildAt(r2)
            if (r10 == 0) goto L_0x00fb
            int r10 = r6.mDividerWidth
            int r1 = r1 + r10
        L_0x00fb:
            int r10 = r4.leftMargin
            int r10 = r10 + r1
            int r1 = r6.getLocationOffset(r0)
            int r22 = r10 + r1
            r1 = r0
            r0 = r24
            r25 = r1
            r11 = r2
            r2 = r22
            r22 = r7
            r23 = -1
            r7 = r4
            r4 = r15
            r0.setChildFrame(r1, r2, r3, r4, r5)
            int r0 = r7.rightMargin
            int r15 = r15 + r0
            r0 = r25
            int r1 = r6.getNextLocationOffset(r0)
            int r15 = r15 + r1
            int r10 = r10 + r15
            int r0 = r6.getChildrenSkipCount(r0, r11)
            int r3 = r18 + r0
            r1 = r10
            goto L_0x0134
        L_0x0128:
            r18 = r3
        L_0x012a:
            r22 = r7
            r19 = r10
            r20 = r11
            r21 = 1
            r23 = -1
        L_0x0134:
            int r3 = r3 + 1
            r10 = r19
            r11 = r20
            r7 = r22
            r5 = 1
            r15 = 2
            goto L_0x0065
        L_0x0140:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.LinearLayoutCompat.layoutHorizontal(int, int, int, int):void");
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | (-8388616 & i3);
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }
}
