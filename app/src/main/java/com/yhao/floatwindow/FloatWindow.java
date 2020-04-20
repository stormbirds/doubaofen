package com.yhao.floatwindow;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

public class FloatWindow {
    private static B mBuilder = null;
    private static final String mDefaultTag = "default_float_window_tag";
    /* access modifiers changed from: private */
    public static Map<String, IFloatWindow> mFloatWindowMap;

    private FloatWindow() {
    }

    public static IFloatWindow get() {
        return get(mDefaultTag);
    }

    public static IFloatWindow get(String str) {
        Map<String, IFloatWindow> map = mFloatWindowMap;
        if (map == null) {
            return null;
        }
        return map.get(str);
    }

    public static B with(Context context) {
        B b = new B(context);
        mBuilder = b;
        return b;
    }

    public static void destroy() {
        destroy(mDefaultTag);
    }

    public static void destroy(String str) {
        Map<String, IFloatWindow> map = mFloatWindowMap;
        if (map != null && map.containsKey(str)) {
            mFloatWindowMap.get(str).dismiss();
            mFloatWindowMap.remove(str);
        }
    }

    public static class B {
        int gravity = 8388659;
        Class[] mActivities;
        Context mApplicationContext;
        boolean mDesktopShow;
        long mDuration = 300;
        int mHeight = -2;
        TimeInterpolator mInterpolator;
        private int mLayoutId;
        int mMoveType = 3;
        PermissionListener mPermissionListener;
        boolean mShow = true;
        int mSlideLeftMargin;
        int mSlideRightMargin;
        private String mTag = FloatWindow.mDefaultTag;
        View mView;
        ViewStateListener mViewStateListener;
        int mWidth = -2;
        int xOffset;
        int yOffset;

        private B() {
        }

        B(Context context) {
            this.mApplicationContext = context;
        }

        public B setView(View view) {
            this.mView = view;
            return this;
        }

        public B setView(int i) {
            this.mLayoutId = i;
            return this;
        }

        public B setWidth(int i) {
            this.mWidth = i;
            return this;
        }

        public B setHeight(int i) {
            this.mHeight = i;
            return this;
        }

        public B setWidth(int i, float f) {
            int i2;
            if (i == 0) {
                i2 = Util.getScreenWidth(this.mApplicationContext);
            } else {
                i2 = Util.getScreenHeight(this.mApplicationContext);
            }
            this.mWidth = (int) (((float) i2) * f);
            return this;
        }

        public B setHeight(int i, float f) {
            int i2;
            if (i == 0) {
                i2 = Util.getScreenWidth(this.mApplicationContext);
            } else {
                i2 = Util.getScreenHeight(this.mApplicationContext);
            }
            this.mHeight = (int) (((float) i2) * f);
            return this;
        }

        public B setX(int i) {
            this.xOffset = i;
            return this;
        }

        public B setY(int i) {
            this.yOffset = i;
            return this;
        }

        public B setX(int i, float f) {
            int i2;
            if (i == 0) {
                i2 = Util.getScreenWidth(this.mApplicationContext);
            } else {
                i2 = Util.getScreenHeight(this.mApplicationContext);
            }
            this.xOffset = (int) (((float) i2) * f);
            return this;
        }

        public B setY(int i, float f) {
            int i2;
            if (i == 0) {
                i2 = Util.getScreenWidth(this.mApplicationContext);
            } else {
                i2 = Util.getScreenHeight(this.mApplicationContext);
            }
            this.yOffset = (int) (((float) i2) * f);
            return this;
        }

        public B setFilter(boolean z, Class... clsArr) {
            this.mShow = z;
            this.mActivities = clsArr;
            return this;
        }

        public B setMoveType(int i) {
            return setMoveType(i, 0, 0);
        }

        public B setMoveType(int i, int i2, int i3) {
            this.mMoveType = i;
            this.mSlideLeftMargin = i2;
            this.mSlideRightMargin = i3;
            return this;
        }

        public B setMoveStyle(long j, TimeInterpolator timeInterpolator) {
            this.mDuration = j;
            this.mInterpolator = timeInterpolator;
            return this;
        }

        public B setTag(String str) {
            this.mTag = str;
            return this;
        }

        public B setDesktopShow(boolean z) {
            this.mDesktopShow = z;
            return this;
        }

        public B setPermissionListener(PermissionListener permissionListener) {
            this.mPermissionListener = permissionListener;
            return this;
        }

        public B setViewStateListener(ViewStateListener viewStateListener) {
            this.mViewStateListener = viewStateListener;
            return this;
        }

        public void build() {
            if (FloatWindow.mFloatWindowMap == null) {
                Map unused = FloatWindow.mFloatWindowMap = new HashMap();
            }
            if (FloatWindow.mFloatWindowMap.containsKey(this.mTag)) {
                throw new IllegalArgumentException("FloatWindow of this tag has been added, Please set a new tag for the new FloatWindow");
            } else if (this.mView == null && this.mLayoutId == 0) {
                throw new IllegalArgumentException("View has not been set!");
            } else {
                if (this.mView == null) {
                    this.mView = Util.inflate(this.mApplicationContext, this.mLayoutId);
                }
                FloatWindow.mFloatWindowMap.put(this.mTag, new IFloatWindowImpl(this));
            }
        }
    }
}
