package android.support.v7.widget;

import android.os.Build;
import android.view.View;

public class TooltipCompat {
    private static final ViewCompatImpl IMPL;

    private interface ViewCompatImpl {
        void setTooltipText(View view, CharSequence charSequence);
    }

    private static class BaseViewCompatImpl implements ViewCompatImpl {
        private BaseViewCompatImpl() {
        }

        public void setTooltipText(View view, CharSequence charSequence) {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }
    }

    private static class Api26ViewCompatImpl implements ViewCompatImpl {
        private Api26ViewCompatImpl() {
        }

        public void setTooltipText(View view, CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 26) {
            IMPL = new Api26ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static void setTooltipText(View view, CharSequence charSequence) {
        IMPL.setTooltipText(view, charSequence);
    }

    private TooltipCompat() {
    }
}
