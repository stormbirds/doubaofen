package com.tiangou.douxiaomi.activity;

import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.tiangou.douxiaomi.Constants;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.fragment.AssiistantFragment;
import com.tiangou.douxiaomi.fragment.AssiistantFragment1;
import com.tiangou.douxiaomi.fragment.ConnectionFragment;
import com.tiangou.douxiaomi.fragment.HomeNewFragment;
import com.tiangou.douxiaomi.fragment.MineFragment;
import com.tiangou.douxiaomi.permission.PermissionHelper;
import com.tiangou.douxiaomi.permission.PermissionInterface;
import com.tiangou.douxiaomi.service.AccessService;
import com.tiangou.douxiaomi.tasks.BeathAuthTask;
import com.tiangou.douxiaomi.tasks.InitTask;
import com.tiangou.douxiaomi.utils.GBData;
import com.tiangou.douxiaomi.utils.StatusbarUtil;

public class TabActivity extends BaseActivity implements View.OnClickListener, PermissionInterface {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    Fragment[] fragments;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;
    private PermissionHelper mPermissionHelper;
    int position = 0;
    TextView tvAss;
    TextView tvAss1;
    TextView tvChat;
    TextView tvConnect;
    TextView tvHome;
    TextView tvMine;

    public int getPermissionsRequestCode() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setListener();
        setOthers();
        this.mPermissionHelper = new PermissionHelper(this, this);
        this.mPermissionHelper.requestPermissions();
        StatusbarUtil.setBgTransparent(this);
    }

    private void initView() {
        setContentView((int) R.layout.activity_tab);
        this.tvHome = (TextView) findViewById(R.id.tv_home);
        this.tvAss = (TextView) findViewById(R.id.tv_ass);
        this.tvAss1 = (TextView) findViewById(R.id.tv_ass_1);
        this.tvMine = (TextView) findViewById(R.id.tv_mine);
        this.tvChat = (TextView) findViewById(R.id.tv_chat);
        this.tvConnect = (TextView) findViewById(R.id.tv_connect);
        this.fragments = new Fragment[6];
    }

    private void setListener() {
        this.tvMine.setOnClickListener(this);
        this.tvHome.setOnClickListener(this);
        this.tvAss.setOnClickListener(this);
        this.tvAss1.setOnClickListener(this);
        this.tvConnect.setOnClickListener(this);
        this.tvChat.setOnClickListener(this);
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
    }

    private void setOthers() {
        setTabSelection(0);
    }

    public void startRecord() {
        Log.e("qyh", "开始录制");
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        startActivityForResult(this.mMediaProjectionManager.createScreenCaptureIntent(), 1);
    }

    public void stopRecord() {
        Log.e("qyh", "结束录制");
        if (this.mMediaProjectionManager != null) {
            this.mMediaProjectionManager = null;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ass:
                if (!AccessService.get().checkAccessibilityEnabled(Constants.douyinService)) {
                    AccessService.get().goAccess();
                    return;
                } else {
                    setTabSelection(1);
                    return;
                }
            case R.id.tv_ass_1:
                setTabSelection(5);
                return;
            case R.id.tv_chat:
                setTabSelection(4);
                return;
            case R.id.tv_connect:
                setTabSelection(3);
                return;
            case R.id.tv_home:
                setTabSelection(0);
                return;
            case R.id.tv_mine:
                setTabSelection(2);
                return;
            default:
                return;
        }
    }

    public void setTabSelection(int i) {
        try {
            this.position = i;
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            hideFragment(beginTransaction);
            if (i == 0) {
                if (this.tvHome != null) {
                    this.tvHome.setEnabled(false);
                }
                if (this.fragments[0] == null) {
                    this.fragments[0] = new HomeNewFragment();
                    beginTransaction.add(R.id.fl_home_container, this.fragments[0], "home").commitAllowingStateLoss();
                    return;
                }
                beginTransaction.show(this.fragments[0]).commitAllowingStateLoss();
            } else if (i == 1) {
                if (this.tvAss != null) {
                    this.tvAss.setEnabled(false);
                }
                if (this.fragments[1] == null) {
                    this.fragments[1] = new AssiistantFragment();
                    beginTransaction.add(R.id.fl_home_container, this.fragments[1], "ass").commitAllowingStateLoss();
                    return;
                }
                beginTransaction.show(this.fragments[1]).commitAllowingStateLoss();
            } else if (i == 2) {
                if (this.tvMine != null) {
                    this.tvMine.setEnabled(false);
                }
                if (this.fragments[2] == null) {
                    this.fragments[2] = new MineFragment();
                    beginTransaction.add(R.id.fl_home_container, this.fragments[2], "mine").commitAllowingStateLoss();
                    return;
                }
                beginTransaction.show(this.fragments[2]).commitAllowingStateLoss();
            } else if (i == 3) {
                if (this.tvConnect != null) {
                    this.tvConnect.setEnabled(false);
                }
                if (this.fragments[3] == null) {
                    this.fragments[3] = new ConnectionFragment();
                    beginTransaction.add(R.id.fl_home_container, this.fragments[3], "connect").commitAllowingStateLoss();
                    return;
                }
                beginTransaction.show(this.fragments[3]).commitAllowingStateLoss();
            } else if (i == 5) {
                if (this.tvAss1 != null) {
                    this.tvAss1.setEnabled(false);
                }
                if (this.fragments[5] == null) {
                    this.fragments[5] = new AssiistantFragment1();
                    beginTransaction.add(R.id.fl_home_container, this.fragments[5], "ass1").commitAllowingStateLoss();
                    return;
                }
                beginTransaction.show(this.fragments[5]).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 1) {
            return;
        }
        if (i2 != -1) {
            Toast.makeText(this, "User cancelled!", 0).show();
            return;
        }
        Log.i("qyh", "Starting screen capture");
        this.mMediaProjection = this.mMediaProjectionManager.getMediaProjection(i2, intent);
        setUpVirtualDisplay();
    }

    private void setUpVirtualDisplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        ImageReader newInstance = ImageReader.newInstance(displayMetrics.widthPixels, displayMetrics.heightPixels, 1, 1);
        this.mMediaProjection.createVirtualDisplay("ScreenCapture", displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi, 16, newInstance.getSurface(), (VirtualDisplay.Callback) null, (Handler) null);
        GBData.reader = newInstance;
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        Fragment[] fragmentArr = this.fragments;
        if (fragmentArr[0] != null) {
            fragmentTransaction.hide(fragmentArr[0]);
        }
        Fragment[] fragmentArr2 = this.fragments;
        if (fragmentArr2[1] != null) {
            fragmentTransaction.hide(fragmentArr2[1]);
        }
        Fragment[] fragmentArr3 = this.fragments;
        if (fragmentArr3[2] != null) {
            fragmentTransaction.hide(fragmentArr3[2]);
        }
        Fragment[] fragmentArr4 = this.fragments;
        if (fragmentArr4[3] != null) {
            fragmentTransaction.hide(fragmentArr4[3]);
        }
        Fragment[] fragmentArr5 = this.fragments;
        if (fragmentArr5[4] != null) {
            fragmentTransaction.hide(fragmentArr5[4]);
        }
        Fragment[] fragmentArr6 = this.fragments;
        if (fragmentArr6[5] != null) {
            fragmentTransaction.hide(fragmentArr6[5]);
        }
        TextView textView = this.tvHome;
        if (textView != null) {
            textView.setEnabled(true);
        }
        TextView textView2 = this.tvAss;
        if (textView2 != null) {
            textView2.setEnabled(true);
        }
        TextView textView3 = this.tvMine;
        if (textView3 != null) {
            textView3.setEnabled(true);
        }
        TextView textView4 = this.tvConnect;
        if (textView4 != null) {
            textView4.setEnabled(true);
        }
        TextView textView5 = this.tvChat;
        if (textView5 != null) {
            textView5.setEnabled(true);
        }
        TextView textView6 = this.tvAss1;
        if (textView6 != null) {
            textView6.setEnabled(true);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        this.mPermissionHelper.requestPermissionsResult(i, strArr, iArr);
    }

    public String[] getPermissions() {
        return new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.READ_EXTERNAL_STORAGE"};
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        String authCode = SPUtils.getAuthCode(this);
        if (!TextUtils.isEmpty(authCode)) {
            new BeathAuthTask(authCode, this).execute(new Object[0]);
        }
    }

    public void requestPermissionsSuccess() {
        new InitTask(this).execute(new Object[0]);
    }

    public void requestPermissionsFail() {
        Toast.makeText(this, "Android系统授权失败，请点击同意", 0).show();
    }
}
