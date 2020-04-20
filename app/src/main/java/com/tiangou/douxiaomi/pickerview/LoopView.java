package com.tiangou.douxiaomi.pickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.tiangou.douxiaomi.Utils;
import java.util.List;
import java.util.Timer;

public class LoopView extends View {
    List arrayList;
    int colorBlack;
    int colorGray;
    int colorGrayLight;
    Context context;
    int g;
    private GestureDetector gestureDetector;
    int h;
    Handler handler;
    boolean isLoop;
    float l;
    LoopListener loopListener;
    int mCurrentItem;
    private int mSelectItem;
    Timer mTimer;
    int n;
    int o;
    Paint paintA;
    Paint paintB;
    Paint paintC;
    int positon;
    int r;
    int s;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener;
    int t;
    int textSize;
    int totalScrollY;
    int u;
    int v;
    int w;
    float x;
    float y;
    float z;

    public LoopView(Context context2) {
        super(context2);
        initLoopView(context2);
    }

    public LoopView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        initLoopView(context2);
    }

    public LoopView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        initLoopView(context2);
    }

    private void initLoopView(Context context2) {
        this.textSize = 0;
        this.colorGray = -5789785;
        this.colorBlack = ViewCompat.MEASURED_STATE_MASK;
        this.colorGrayLight = -2302756;
        this.l = 2.0f;
        this.isLoop = true;
        this.positon = -1;
        this.r = 9;
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.totalScrollY = 0;
        this.simpleOnGestureListener = new LoopViewGestureListener(this);
        this.handler = new MessageHandler(this);
        this.context = context2;
        setTextSize(16.0f);
    }

    static int getSelectItem(LoopView loopView) {
        return loopView.getCurrentItem();
    }

    static void b(LoopView loopView) {
        loopView.f();
    }

    private void d() {
        if (this.arrayList != null) {
            Typeface create = Typeface.create(Typeface.MONOSPACE, 1);
            this.paintA = new Paint();
            this.paintA.setColor(this.colorGray);
            this.paintA.setAntiAlias(true);
            this.paintA.setTypeface(Typeface.MONOSPACE);
            this.paintA.setTextSize((float) this.textSize);
            this.paintB = new Paint();
            this.paintB.setColor(this.colorBlack);
            this.paintB.setAntiAlias(true);
            this.paintB.setTextScaleX(1.05f);
            this.paintB.setTypeface(Typeface.MONOSPACE);
            this.paintB.setTextSize((float) this.textSize);
            this.paintB.setTypeface(create);
            this.paintC = new Paint();
            this.paintC.setColor(this.colorGrayLight);
            this.paintC.setAntiAlias(true);
            this.paintC.setTypeface(Typeface.MONOSPACE);
            this.paintC.setTextSize((float) this.textSize);
            if (Build.VERSION.SDK_INT >= 11) {
                setLayerType(1, (Paint) null);
            }
            this.gestureDetector = new GestureDetector(this.context, this.simpleOnGestureListener);
            this.gestureDetector.setIsLongpressEnabled(false);
            e();
            int i = this.h;
            float f = this.l;
            this.t = (int) (((float) i) * f * ((float) (this.r - 1)));
            int i2 = this.t;
            this.s = (int) (((double) (i2 * 2)) / 3.141592653589793d);
            this.u = (int) (((double) i2) / 3.141592653589793d);
            this.v = this.g + this.textSize;
            int i3 = this.s;
            this.n = (int) ((((float) i3) - (((float) i) * f)) / 2.0f);
            this.o = (int) ((((float) i3) + (f * ((float) i))) / 2.0f);
            if (this.positon == -1) {
                if (this.isLoop) {
                    this.positon = (this.arrayList.size() + 1) / 2;
                } else {
                    this.positon = 0;
                }
            }
            this.mCurrentItem = this.positon;
        }
    }

    private void e() {
        Rect rect = new Rect();
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.paintB.getTextBounds("00000000", 0, 8, rect);
            int width = (int) (((float) rect.width()) * 4.0f);
            if (width > this.g) {
                this.g = width;
            }
            this.paintB.getTextBounds("星期", 0, 2, rect);
            int height = rect.height();
            if (height > this.h) {
                this.h = height;
            }
        }
    }

    private void f() {
        Timer timer = new Timer();
        this.mTimer = timer;
        timer.schedule(new MTimer(this, (int) (((float) this.totalScrollY) % (this.l * ((float) this.h))), timer), 0, 10);
    }

    public final void setNotLoop() {
        this.isLoop = false;
    }

    public final void setCyclic(boolean z2) {
        this.isLoop = z2;
    }

    public final void setTextSize(float f) {
        if (f > 0.0f) {
            this.textSize = (int) (this.context.getResources().getDisplayMetrics().density * f);
        }
    }

    public final void setCurrentItem(int i) {
        this.positon = i;
        this.totalScrollY = 0;
        f();
        invalidate();
    }

    public final void setListener(LoopListener loopListener2) {
        this.loopListener = loopListener2;
    }

    public final void setArrayList(List list) {
        this.arrayList = list;
        d();
        invalidate();
    }

    public final int getCurrentItem() {
        int i = this.mCurrentItem;
        if (i > 0 && i <= this.arrayList.size() - 1) {
            return this.mCurrentItem;
        }
        return 0;
    }

    public final String getCurrentItemValue() {
        return String.valueOf(this.arrayList.get(getCurrentItem())).trim();
    }

    /* access modifiers changed from: protected */
    public final void b(float f) {
        Timer timer = new Timer();
        this.mTimer = timer;
        timer.schedule(new LoopTimerTask(this, f, timer), 0, 20);
    }

    /* access modifiers changed from: protected */
    public final void b(int i) {
        Timer timer = new Timer();
        this.mTimer = timer;
        timer.schedule(new MyTimerTask(this, i, timer), 0, 20);
    }

    /* access modifiers changed from: protected */
    public final void c() {
        if (this.loopListener != null) {
            new Handler().postDelayed(new LoopRunnable(this), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        List list = this.arrayList;
        if (list == null) {
            super.onDraw(canvas);
            return;
        }
        String[] strArr = new String[this.r];
        this.w = (int) (((float) this.totalScrollY) / (this.l * ((float) this.h)));
        int size = this.positon + (this.w % list.size());
        if (this.mCurrentItem != size) {
            c();
        }
        this.mCurrentItem = size;
        if (!this.isLoop) {
            if (this.mCurrentItem < 0) {
                this.mCurrentItem = 0;
            }
            if (this.mCurrentItem > this.arrayList.size() - 1) {
                this.mCurrentItem = this.arrayList.size() - 1;
            }
        } else {
            if (this.mCurrentItem < 0) {
                this.mCurrentItem = this.arrayList.size() + this.mCurrentItem;
            }
            if (this.mCurrentItem > this.arrayList.size() - 1) {
                this.mCurrentItem -= this.arrayList.size();
            }
        }
        int i = (int) (((float) this.totalScrollY) % (this.l * ((float) this.h)));
        for (int i2 = 0; i2 < this.r; i2++) {
            int i3 = this.mCurrentItem - (4 - i2);
            if (this.isLoop) {
                if (i3 < 0) {
                    i3 += this.arrayList.size();
                }
                if (i3 > this.arrayList.size() - 1) {
                    i3 -= this.arrayList.size();
                }
                strArr[i2] = (String) this.arrayList.get(i3);
            } else if (i3 < 0) {
                strArr[i2] = "";
            } else if (i3 > this.arrayList.size() - 1) {
                strArr[i2] = "";
            } else {
                strArr[i2] = (String) this.arrayList.get(i3);
            }
        }
        int i4 = this.v;
        int i5 = (i4 - this.g) / 2;
        int i6 = this.n;
        canvas.drawLine(0.0f, (float) i6, (float) i4, (float) i6, this.paintC);
        int i7 = this.o;
        canvas.drawLine(0.0f, (float) i7, (float) this.v, (float) i7, this.paintC);
        for (int i8 = 0; i8 < this.r; i8++) {
            canvas.save();
            double d = (((double) ((((float) (this.h * i8)) * this.l) - ((float) i))) * 3.141592653589793d) / ((double) this.t);
            float f = (float) (90.0d - ((d / 3.141592653589793d) * 180.0d));
            if (f >= 90.0f || f <= -90.0f) {
                canvas.restore();
            } else {
                int cos = (int) ((((double) this.u) - (Math.cos(d) * ((double) this.u))) - ((Math.sin(d) * ((double) this.h)) / 2.0d));
                canvas2.translate(0.0f, (float) cos);
                canvas2.scale(1.0f, (float) Math.sin(d));
                String str = strArr[i8];
                int i9 = this.textSize;
                int length = (int) (((double) i9) * ((((double) this.textSize) - ((double) (str.length() * 2))) / ((double) i9)) * 1.2d);
                this.paintA.setTextSize((float) Utils.dp2Px(this.context, 14.0f));
                this.paintB.setTextSize((float) Utils.dp2Px(this.context, 14.0f));
                int left = (int) (((double) this.n) + (((double) getLeft()) * 0.5d));
                Rect rect = new Rect();
                this.paintB.getTextBounds(str, 0, str.length(), rect);
                int width = rect.width();
                int width2 = getWidth() - (left * 2);
                if (width > 0) {
                    left = (int) (((double) left) + (((double) (width2 - width)) * 0.5d));
                }
                int i10 = this.n;
                if (cos > i10 || this.h + cos < i10) {
                    int i11 = this.o;
                    if (cos > i11 || this.h + cos < i11) {
                        if (cos >= this.n) {
                            int i12 = this.h;
                            if (cos + i12 <= this.o) {
                                canvas2.clipRect(0, 0, this.v, (int) (((float) i12) * this.l));
                                canvas2.drawText(strArr[i8], (float) left, (float) this.h, this.paintB);
                                this.mSelectItem = this.arrayList.indexOf(strArr[i8]);
                            }
                        }
                        canvas2.clipRect(0, 0, this.v, (int) (((float) this.h) * this.l));
                        canvas2.drawText(strArr[i8], (float) left, (float) this.h, this.paintA);
                    } else {
                        canvas.save();
                        canvas2.clipRect(0, 0, this.v, this.o - cos);
                        float f2 = (float) left;
                        canvas2.drawText(strArr[i8], f2, (float) this.h, this.paintB);
                        canvas.restore();
                        canvas.save();
                        canvas2.clipRect(0, this.o - cos, this.v, (int) (((float) this.h) * this.l));
                        canvas2.drawText(strArr[i8], f2, (float) this.h, this.paintA);
                        canvas.restore();
                    }
                } else {
                    canvas.save();
                    canvas2.clipRect(0, 0, this.v, this.n - cos);
                    float f3 = (float) left;
                    canvas2.drawText(strArr[i8], f3, (float) this.h, this.paintA);
                    canvas.restore();
                    canvas.save();
                    canvas2.clipRect(0, this.n - cos, this.v, (int) (((float) this.h) * this.l));
                    canvas2.drawText(strArr[i8], f3, (float) this.h, this.paintB);
                    canvas.restore();
                }
                canvas.restore();
            }
        }
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        d();
        setMeasuredDimension(this.v, this.s);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.x = motionEvent.getRawY();
        } else if (action != 2) {
            if (!this.gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1) {
                f();
            }
            return true;
        } else {
            this.y = motionEvent.getRawY();
            float f = this.x;
            float f2 = this.y;
            this.z = f - f2;
            this.x = f2;
            this.totalScrollY = (int) (((float) this.totalScrollY) + this.z);
            if (!this.isLoop) {
                int i = this.totalScrollY;
                int i2 = this.positon;
                float f3 = this.l;
                int i3 = this.h;
                if (i <= ((int) (((float) (-i2)) * ((float) i3) * f3))) {
                    this.totalScrollY = (int) (((float) (-i2)) * f3 * ((float) i3));
                }
            }
        }
        if (this.totalScrollY < ((int) (((float) ((this.arrayList.size() - 1) - this.positon)) * this.l * ((float) this.h)))) {
            invalidate();
        } else {
            this.totalScrollY = (int) (((float) ((this.arrayList.size() - 1) - this.positon)) * this.l * ((float) this.h));
            invalidate();
        }
        if (!this.gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1) {
            f();
        }
        return true;
    }
}
