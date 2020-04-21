package com.tiangou.douxiaomi;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tiangou.douxiaomi.activity.BaseActivity;
import com.tiangou.douxiaomi.bean.FunctionBean;
import com.tiangou.douxiaomi.event.SolveFinishEvent;
import com.tiangou.douxiaomi.function.impl.base.BaseImpl;
import com.tiangou.douxiaomi.service.AccessService;
import com.tiangou.douxiaomi.utils.DataConfig;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    static App instance;
    CheckBox checkBoxFloat;
    public DataConfig config;
    /* access modifiers changed from: private */
    public Activity currentActivity = null;
    public List<FunctionBean> functionList = new ArrayList();
    public boolean isFans = false;
    public AccessService service;
    public Boolean startRun = false;
    public TaskUtils taskUtils;
    TextView txtBack;
    TextView txtInfoFloat;

    public void onCreate() {
        super.onCreate();
        instance = this;
        EventBus.getDefault().register(this);
        AccessService.get().init(this);
        this.config = new DataConfig(this);
        initFloatWindow();
        initGlobeActivity();
    }

    public static App getInstance() {
        return instance;
    }

    public Boolean getStartRun() {
        return this.startRun;
    }

    public void setStartRun(Boolean bool) {
        this.startRun = bool;
    }

    public AccessService getService() {
        return this.service;
    }

    public void setService(AccessService accessService) {
        this.service = accessService;
    }

    private void initFloatWindow() {
        if (FloatWindow.get() == null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.other_float_view, (ViewGroup) null);
            this.checkBoxFloat = (CheckBox) inflate.findViewById(R.id.checkbox_float);
            this.txtBack = (TextView) inflate.findViewById(R.id.txt_back);
            this.txtInfoFloat = (TextView) inflate.findViewById(R.id.txt_float_info);
            this.checkBoxFloat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (!z) {
                        App.this.txtInfoFloat.setText("开始");
                        App.getInstance().startRun = false;
                        if (App.this.taskUtils != null) {
                            App.this.taskUtils.finish();
                        }
                    } else if (!AccessService.get().checkAccessibilityEnabled(Constants.douyinService)) {
                        AccessService.get().goAccess();
                        compoundButton.setChecked(false);
                    } else if (App.this.taskUtils == null) {
                        compoundButton.setChecked(false);
                    } else {
                        try {
                            App.this.txtInfoFloat.setText("停止");
                            App.getInstance().startRun = true;
                            App.this.taskUtils.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            this.txtBack.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                public void onClick(View view) {
                    if (App.this.taskUtils != null) {
                        App.getInstance().startRun = false;
                        if (App.this.taskUtils != null) {
                            App.this.taskUtils.finish();
                        }
                        App.this.resetStartUI();
                    }
                    if (App.this.getCurrentActivity() != null) {
                        App.this.getCurrentActivity().moveTaskToFront();
                    }
                }
            });
            FloatWindow.with(getApplicationContext()).setView(inflate)
                    .setWidth(Screen.width , 0.2f)
                    .setHeight(Screen.height, 0.2f)
                    .setX(Screen.width, 0.8f)
                    .setY(Screen.height, 0.3f)
                    .setMoveType(MoveType.active, 100, -100)
                    .setMoveStyle(500, new BounceInterpolator())
                    .setPermissionListener(new PermissionListener() {
                public void onSuccess() {
                    Toast.makeText(App.this.getApplicationContext(), "成功授权悬浮窗权限", Toast.LENGTH_SHORT).show();
                }

                public void onFail() {
                    Toast.makeText(App.this.getApplicationContext(), "需要授权悬浮窗权限", Toast.LENGTH_SHORT).show();
                }
            }).setDesktopShow(true).build();
        }
    }

    public void showFloatWindows() {
        if (FloatWindow.get().getView().getVisibility() == View.INVISIBLE) {
            FloatWindow.get().getView().setVisibility(View.VISIBLE);
        }
    }

    public void hideFloatWindows() {
        if (FloatWindow.get().getView().getVisibility() == View.VISIBLE) {
            FloatWindow.get().getView().setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void runNext(SolveFinishEvent solveFinishEvent) {
        this.taskUtils.onNext();
    }

    public void resetStartUI() {
        try {
            this.checkBoxFloat.setChecked(false);
            this.txtInfoFloat.setText("开始");
            if (this.taskUtils != null) {
                getInstance().startRun = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void holdApp(String str) {
        showFloatWindows();
        startActivity(getPackageManager().getLaunchIntentForPackage(str));
    }

    public void initTaskUtils(List<BaseImpl> list) {
        this.taskUtils = new TaskUtils(list);
    }

    public void initTaskUtils(BaseImpl baseImpl) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(baseImpl);
        this.taskUtils = new TaskUtils(arrayList);
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                Activity unused = App.this.currentActivity = activity;
            }

            public void onActivityDestroyed(Activity activity) {
                Activity unused = App.this.currentActivity = activity;
            }

            public void onActivityStarted(Activity activity) {
                Activity unused = App.this.currentActivity = activity;
            }

            public void onActivityResumed(Activity activity) {
                Activity unused = App.this.currentActivity = activity;
            }

            public void onActivityPaused(Activity activity) {
                Activity unused = App.this.currentActivity = activity;
            }

            public void onActivityStopped(Activity activity) {
                Activity unused = App.this.currentActivity = activity;
            }
        });
    }

    public BaseActivity getCurrentActivity() {
        Activity activity = this.currentActivity;
        if (activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        return null;
    }
}
