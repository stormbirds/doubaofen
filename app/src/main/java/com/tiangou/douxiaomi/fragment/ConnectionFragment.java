package com.tiangou.douxiaomi.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.adapter.ConnectAdapter;
import com.tiangou.douxiaomi.bean.BaseModel;
import com.tiangou.douxiaomi.bean.ConnectBean;
import com.tiangou.douxiaomi.bean.TypeBean;
import com.tiangou.douxiaomi.bean.UserNumBean;
import com.tiangou.douxiaomi.dialog.PromptThemeDialog;
import com.tiangou.douxiaomi.http.HttpRxListener;
import com.tiangou.douxiaomi.http.RtRxOkHttp;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.pickerview.DataPickerDialog;
import com.tiangou.douxiaomi.utils.DensityUtil;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;

public class ConnectionFragment extends Fragment implements HttpRxListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    ConnectAdapter adapter;
    CheckBox cbSelect;
    View layoutView;
    UserNumBean numBean;
    RecyclerView recyConnect;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvExport;
    TextView tvRefresh;
    TextView tvSex;
    TextView tvType;
    List<TypeBean> types;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        initView();
        View findViewById = this.layoutView.findViewById(R.id.statusbar);
        if (findViewById != null) {
            ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
            layoutParams.height = DensityUtil.getStatusBarHeight(getActivity());
            findViewById.setLayoutParams(layoutParams);
        }
        setListener();
        setOthers();
        return this.layoutView;
    }

    private void initView() {
        this.layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_connect_new, (ViewGroup) null);
        this.adapter = new ConnectAdapter(R.layout.recy_connect, (List<ConnectBean>) null);
        this.tvType = (TextView) this.layoutView.findViewById(R.id.tv_type);
        this.tvSex = (TextView) this.layoutView.findViewById(R.id.tv_sex);
        this.cbSelect = (CheckBox) this.layoutView.findViewById(R.id.cb_select);
        this.tvRefresh = (TextView) this.layoutView.findViewById(R.id.tv_refresh);
        this.tvExport = (TextView) this.layoutView.findViewById(R.id.tv_export);
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.layoutView.findViewById(R.id.swipe_refresh);
        this.recyConnect = (RecyclerView) this.layoutView.findViewById(R.id.recy_connect);
    }

    private void setListener() {
        this.tvSex.setOnClickListener(this);
        this.tvType.setOnClickListener(this);
        this.cbSelect.setOnClickListener(this);
        this.tvExport.setOnClickListener(this);
        this.recyConnect.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyConnect.setAdapter(this.adapter);
        this.tvRefresh.setOnClickListener(this);
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setOthers() {
        getClassType();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_select:
                changeSelect();
                return;
            case R.id.tv_export:
                getLastNum();
                return;
            case R.id.tv_refresh:
                this.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                return;
            case R.id.tv_sex:
                showSex();
                return;
            case R.id.tv_type:
                showClassType();
                return;
            default:
                return;
        }
    }

    private void getLastNum() {
        if (Jni.getAuthStatus() != 0) {
            Toast.makeText(getActivity(), "请先授权", 0).show();
            return;
        }
        Toast.makeText(getActivity(), "正在进行数据导出工作，请稍后", 0).show();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("project_id", "100001");
        builder.add("uid", SPUtils.getAuthCode(getActivity()));
        RtRxOkHttp.getInstance().createRtRx(RtRxOkHttp.getApiService().getUserNum(builder.build()), this, 3);
    }

    private void changeSelect() {
        for (ConnectBean connectBean : this.adapter.getData()) {
            connectBean.checked = Boolean.valueOf(this.cbSelect.isChecked());
        }
        this.adapter.notifyDataSetChanged();
        if (this.cbSelect.isChecked()) {
            this.cbSelect.setText("取消全选");
        } else {
            this.cbSelect.setText("全选");
        }
    }

    private void getConnect() {
        FormBody.Builder builder = new FormBody.Builder();
        if (this.tvType.getTag() != null) {
            builder.add("typeid", this.tvType.getTag().toString());
        }
        if (this.tvSex.getTag() != null) {
            builder.add("sex", this.tvSex.getTag().toString());
        }
        builder.add("num", "30");
        builder.add("project_id", "100001");
        RtRxOkHttp.getInstance().createRtRx(RtRxOkHttp.getApiService().getConnectList(builder.build()), this, 2);
    }

    private void getClassType() {
        RtRxOkHttp.getInstance().createRtRx(RtRxOkHttp.getApiService().getClassType(), this, 1);
    }

    public void httpResponse(Object obj, boolean z, int i) {
        this.swipeRefreshLayout.setRefreshing(false);
        if (z) {
            if (i == 1) {
                BaseModel baseModel = (BaseModel) obj;
                if (baseModel.code == 0) {
                    this.types = (List) baseModel.data;
                    this.tvType.setText(this.types.get(0).title);
                    TextView textView = this.tvType;
                    textView.setTag(this.types.get(0).id + "");
                    this.swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
            } else if (i == 2) {
                BaseModel baseModel2 = (BaseModel) obj;
                if (baseModel2.code == 0) {
                    this.adapter.setNewData((List) baseModel2.data);
                }
            } else if (i == 3) {
                BaseModel baseModel3 = (BaseModel) obj;
                if (baseModel3.code == 0) {
                    this.numBean = (UserNumBean) baseModel3.data;
                    solveExport();
                }
            } else if (i == 4 && ((BaseModel) obj).code == 0) {
                exportSuccess();
            }
        } else if (i == 2) {
            this.adapter.setNewData((List) null);
        }
    }

    private void exportSuccess() {
        String str = null;
        for (ConnectBean connectBean : this.adapter.getData()) {
            if (connectBean.checked.booleanValue()) {
                if (TextUtils.isEmpty(str)) {
                    str = connectBean.phone;
                } else {
                    str = str + "\n" + connectBean.phone;
                }
            }
        }
        ((ClipboardManager) getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Label", str));
        new PromptThemeDialog(getActivity(), "数据已复制在剪切板，请自行粘贴使用", (PromptThemeDialog.PromptClickSureListener) null).show();
    }

    private void solveExport() {
        if (!Utils.isEmptyArray(this.adapter.getData())) {
            ArrayList arrayList = new ArrayList();
            for (ConnectBean connectBean : this.adapter.getData()) {
                if (connectBean.checked.booleanValue()) {
                    arrayList.add(connectBean);
                }
            }
            if (Utils.isEmptyArray(arrayList)) {
                Toast.makeText(getActivity(), "请选择需要导出的人脉", 0).show();
            } else if (arrayList.size() > this.numBean.mc_max) {
                Toast.makeText(getActivity(), "单次导出数量超过最大限制", 0).show();
            } else if (arrayList.size() > this.numBean.everyday_max - this.numBean.used_nums) {
                Toast.makeText(getActivity(), "当天导出数量超过导出上限", 0).show();
            } else {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("project_id", "100001");
                builder.add("nums", arrayList.size() + "");
                builder.add("uid", SPUtils.getAuthCode(getActivity()));
                RtRxOkHttp.getInstance().createRtRx(RtRxOkHttp.getApiService().export(builder.build()), this, 4);
            }
        }
    }

    private void showSex() {
        if (!Utils.isEmptyArray(this.types)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add("男");
            arrayList.add("女");
            arrayList.add("未知");
            new DataPickerDialog.Builder(getActivity()).setTitle("选择性别").setData(arrayList).setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                public void onDataSelected(int i, String str) {
                    ConnectionFragment.this.tvSex.setText(str);
                    TextView textView = ConnectionFragment.this.tvSex;
                    textView.setTag((i + 1) + "");
                    ConnectionFragment.this.swipeRefreshLayout.setRefreshing(true);
                    ConnectionFragment.this.onRefresh();
                }
            }).create().show();
        }
    }

    private void showClassType() {
        if (!Utils.isEmptyArray(this.types)) {
            ArrayList arrayList = new ArrayList();
            for (TypeBean typeBean : this.types) {
                arrayList.add(typeBean.title);
            }
            new DataPickerDialog.Builder(getActivity()).setTitle("选择行业").setData(arrayList).setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                public void onDataSelected(int i, String str) {
                    ConnectionFragment.this.tvType.setText(str);
                    TextView textView = ConnectionFragment.this.tvType;
                    textView.setTag(ConnectionFragment.this.types.get(i).id + "");
                    ConnectionFragment.this.swipeRefreshLayout.setRefreshing(true);
                    ConnectionFragment.this.onRefresh();
                }
            }).create().show();
        }
    }

    public void onRefresh() {
        getConnect();
    }
}
