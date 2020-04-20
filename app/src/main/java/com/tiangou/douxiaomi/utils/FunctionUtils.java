package com.tiangou.douxiaomi.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.Constants;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.activity.TabActivity;
import com.tiangou.douxiaomi.bean.FunctionBean;
import com.tiangou.douxiaomi.dialog.CustomContentDialog;
import com.tiangou.douxiaomi.dialog.PromptThemeDialog;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.service.AccessService;

public class FunctionUtils {
    CustomContentDialog contentDialog;
    public Context context;
    Button susStart = null;

    public FunctionUtils(Context context2) {
        this.context = context2;
    }

    public void click(final FunctionBean functionBean) {
        if (!Utils.isSettingOpen(AccessService.class, this.context) || (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this.context))) {
            Toast.makeText(this.context, "请先开启辅助权限", 0).show();
        } else {
            functionBean.showData(this.context, new FunctionBean.OnFunctionSelectListener() {
                public void onSelect(String str) {
                    App.getInstance().initTaskUtils(functionBean.getImpl());
                    FunctionUtils.this.startRun(str, functionBean.type);
                }
            });
        }
    }

    public void startRun(String str, int i) {
        if (App.getInstance().config.userSex.size() < 3 || i == 12 || i == 15) {
            ((TabActivity) this.context).startRecord();
        }
        new PromptThemeDialog(this.context, str, new PromptThemeDialog.PromptClickSureListener() {
            public void onClose() {
            }

            public void onSure() {
                if (Jni.getAuthStatus() == 0) {
                    App.getInstance().showFloatWindows();
                    App.getInstance().holdApp(Constants.douyin);
                    return;
                }
                Toast.makeText(FunctionUtils.this.context, "请先前往用户中心进行授权", 0).show();
            }
        }).show();
    }

    public void onResume() {
        if (this.susStart != null) {
            ((WindowManager) this.context.getSystemService("window")).removeView(this.susStart);
            this.susStart = null;
        }
    }
}
