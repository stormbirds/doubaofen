package com.tiangou.douxiaomi.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.hiui.voice.MainActivity;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.WebActivity;
import com.tiangou.douxiaomi.activity.AuthActivity;
import com.tiangou.douxiaomi.activity.VideoActivity;
import com.tiangou.douxiaomi.activity.WaterMarkActivity;
import com.tiangou.douxiaomi.jni.Jni;

public class MineFragment extends Fragment implements View.OnClickListener {
    View layoutView;
    TextView tvStatus;
    TextView tvVersion;
    TextView tv_auth;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        initView();
        setListener();
        setOthers();
        return this.layoutView;
    }

    private void initView() {
        this.layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, (ViewGroup) null);
        this.tvVersion = (TextView) this.layoutView.findViewById(R.id.tv_version);
        this.tv_auth = (TextView) this.layoutView.findViewById(R.id.tv_auth);
        this.tvStatus = (TextView) this.layoutView.findViewById(R.id.tv_status);
        this.tv_auth.setOnClickListener(this);
    }

    private void setListener() {
        this.layoutView.findViewById(R.id.tv_video).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MineFragment.this.getActivity(), WebActivity.class);
                intent.putExtra("title", "抖音教程");
                intent.putExtra("url", "http://jc.xuerenwx.com/jiaocheng/");
                MineFragment.this.startActivity(intent);
            }
        });
        this.layoutView.findViewById(R.id.tv_douyin_download).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://www.lanzous.com/s/100000"));
                MineFragment.this.startActivity(intent);
            }
        });
        this.layoutView.findViewById(R.id.tv_water).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MineFragment mineFragment = MineFragment.this;
                mineFragment.startActivity(new Intent(mineFragment.getActivity(), WaterMarkActivity.class));
            }
        });
        this.layoutView.findViewById(R.id.tv_you).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Jni.getAuthStatus() != 0) {
                    Toast.makeText(view.getContext(), "没授权", 1).show();
                    return;
                }
                MineFragment mineFragment = MineFragment.this;
                mineFragment.startActivity(new Intent(mineFragment.getActivity(), MainActivity.class));
            }
        });
        this.layoutView.findViewById(R.id.tv_video_change).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Jni.getAuthStatus() != 0) {
                    Toast.makeText(view.getContext(), "没授权", 1).show();
                    return;
                }
                MineFragment mineFragment = MineFragment.this;
                mineFragment.startActivity(new Intent(mineFragment.getActivity(), VideoActivity.class));
            }
        });
    }

    private void setOthers() {
        this.tvVersion.setText(Utils.getVersionName(getActivity()));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.tv_auth) {
            click_auth(view);
        }
    }

    /* access modifiers changed from: package-private */
    public void click_auth(View view) {
        Context context = view.getContext();
        context.startActivity(new Intent(context, AuthActivity.class));
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            solve();
        }
    }

    public void onResume() {
        super.onResume();
        solve();
    }

    public void solve() {
        if (Jni.getAuthStatus() == 0) {
            this.tvStatus.setText("已授权");
            this.tvStatus.setTextColor(Color.parseColor("#ff669900"));
            return;
        }
        this.tvStatus.setText("未授权");
        this.tvStatus.setTextColor(Color.parseColor("#ff0000"));
    }
}
