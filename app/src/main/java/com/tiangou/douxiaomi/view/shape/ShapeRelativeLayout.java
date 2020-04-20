package com.tiangou.douxiaomi.view.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.tiangou.douxiaomi.R;

public class ShapeRelativeLayout extends RelativeLayout {
    int bottomLeftRadius;
    int bottomRightRadius;
    int cornesRadius;
    int gradientAngle;
    int gradientCenterColor;
    GradientDrawable gradientDrawable;
    int gradientEndColor;
    int gradientOrientation;
    int gradientRadius;
    int gradientStartColor;
    int gradientType;
    boolean gradientUseLevel;
    int shapeType;
    int solidColor;
    int strokeDashGap;
    int strokeDashWidth;
    int stroke_Color;
    int stroke_Width;
    int topLeftRadius;
    int topRightRadius;
    int touchColor;

    public ShapeRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initData(context, attributeSet);
    }

    public ShapeRelativeLayout(Context context) {
        super(context);
    }

    private void initData(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeRelativeLayout);
        this.solidColor = obtainStyledAttributes.getColor(12, 0);
        this.stroke_Color = obtainStyledAttributes.getColor(15, 0);
        this.gradientStartColor = obtainStyledAttributes.getColor(8, 0);
        this.gradientEndColor = obtainStyledAttributes.getColor(5, 0);
        this.gradientCenterColor = obtainStyledAttributes.getColor(4, 0);
        this.touchColor = obtainStyledAttributes.getColor(19, 0);
        this.cornesRadius = (int) obtainStyledAttributes.getDimension(2, 0.0f);
        this.topLeftRadius = (int) obtainStyledAttributes.getDimension(17, 0.0f);
        this.topRightRadius = (int) obtainStyledAttributes.getDimension(18, 0.0f);
        this.bottomLeftRadius = (int) obtainStyledAttributes.getDimension(0, 0.0f);
        this.bottomRightRadius = (int) obtainStyledAttributes.getDimension(1, 0.0f);
        this.stroke_Width = (int) obtainStyledAttributes.getDimension(16, 0.0f);
        this.strokeDashWidth = (int) obtainStyledAttributes.getDimension(14, 0.0f);
        this.strokeDashGap = (int) obtainStyledAttributes.getDimension(13, 0.0f);
        this.gradientAngle = (int) obtainStyledAttributes.getDimension(3, 0.0f);
        this.gradientRadius = (int) obtainStyledAttributes.getDimension(7, 0.0f);
        this.gradientUseLevel = obtainStyledAttributes.getBoolean(10, false);
        this.gradientType = obtainStyledAttributes.getInt(9, -1);
        this.gradientOrientation = obtainStyledAttributes.getInt(6, -1);
        this.shapeType = obtainStyledAttributes.getInt(11, -1);
        this.gradientDrawable = new GradientDrawable();
        this.gradientDrawable.setStroke(this.stroke_Width, this.stroke_Color, (float) this.strokeDashWidth, (float) this.strokeDashGap);
        int i = this.gradientOrientation;
        if (i != -1) {
            this.gradientDrawable.setOrientation(getOrientation(i));
            this.gradientDrawable.setColors(new int[]{this.gradientStartColor, this.gradientCenterColor, this.gradientEndColor});
        } else {
            this.gradientDrawable.setColor(this.solidColor);
        }
        int i2 = this.shapeType;
        if (i2 != -1) {
            this.gradientDrawable.setShape(i2);
        }
        if (this.shapeType != 1) {
            int i3 = this.cornesRadius;
            if (i3 != 0) {
                this.gradientDrawable.setCornerRadius((float) i3);
            } else {
                GradientDrawable gradientDrawable2 = this.gradientDrawable;
                int i4 = this.topLeftRadius;
                int i5 = this.topRightRadius;
                int i6 = this.bottomRightRadius;
                int i7 = this.bottomLeftRadius;
                gradientDrawable2.setCornerRadii(new float[]{(float) i4, (float) i4, (float) i5, (float) i5, (float) i6, (float) i6, (float) i7, (float) i7});
            }
        }
        boolean z = this.gradientUseLevel;
        if (z) {
            this.gradientDrawable.setUseLevel(z);
        }
        int i8 = this.gradientType;
        if (i8 != -1) {
            this.gradientDrawable.setGradientType(i8);
        }
        this.gradientDrawable.setGradientRadius((float) this.gradientRadius);
        setBackground(this.gradientDrawable);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            int i = this.touchColor;
            if (i != 0) {
                this.gradientDrawable.setColor(i);
                setBackground(this.gradientDrawable);
                postInvalidate();
            }
        } else if ((motionEvent.getAction() == 1 || motionEvent.getAction() == 3) && this.touchColor != 0) {
            this.gradientDrawable.setColor(this.solidColor);
            setBackground(this.gradientDrawable);
        }
        return super.onTouchEvent(motionEvent);
    }

    private GradientDrawable.Orientation getOrientation(int i) {
        switch (i) {
            case 0:
                return GradientDrawable.Orientation.BL_TR;
            case 1:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 2:
                return GradientDrawable.Orientation.BR_TL;
            case 3:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 4:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 5:
                return GradientDrawable.Orientation.TL_BR;
            case 6:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 7:
                return GradientDrawable.Orientation.TR_BL;
            default:
                return null;
        }
    }
}
