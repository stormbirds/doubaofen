package com.tiangou.douxiaomi.bean;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.dialog.CustomContentDialog;
import com.tiangou.douxiaomi.dialog.DataSelectDialog;
import com.tiangou.douxiaomi.function.TxtType;
import com.tiangou.douxiaomi.function.impl.BatchFriendImpl;
import com.tiangou.douxiaomi.function.impl.base.BaseImpl;
import com.tiangou.douxiaomi.function.impl.comment.HomeRecommendCommentImpl;
import com.tiangou.douxiaomi.function.impl.comment.LiveSquareImpl;
import com.tiangou.douxiaomi.function.impl.comment.SameCityImpl;
import com.tiangou.douxiaomi.function.impl.comment.UserWorksCommentImpl;
import com.tiangou.douxiaomi.function.impl.culitivate.RecommentCulitivateImple;
import com.tiangou.douxiaomi.function.impl.culitivate.WorksCulitivateImple;
import com.tiangou.douxiaomi.function.impl.person.AddressFriendImpl;
import com.tiangou.douxiaomi.function.impl.person.BatchUserImpl;
import com.tiangou.douxiaomi.function.impl.person.DirectionFriendImpl;
import com.tiangou.douxiaomi.function.impl.person.FriendsListImpl;
import com.tiangou.douxiaomi.function.impl.person.HomeRandomFriendImpl;
import com.tiangou.douxiaomi.function.impl.person.HomeRecommendFriendImpl;
import com.tiangou.douxiaomi.function.impl.person.LiveMemberImpl;
import com.tiangou.douxiaomi.function.impl.person.MessageListImpl;
import com.tiangou.douxiaomi.function.impl.person.SameCityPrivateImpl;
import com.tiangou.douxiaomi.function.impl.person.WorkCommenterImpl;
import com.tiangou.douxiaomi.function.impl.praise.CommenterPraiseImpl;
import com.tiangou.douxiaomi.function.impl.praise.VideoPraiseImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JSONPOJOBuilder
public class FunctionBean implements Serializable {
    ConfigBean configBean;
    List<BaseContentBean> contentBeanList = new ArrayList();
    public transient CustomContentDialog contentDialog = null;
    public String desc;
    public Boolean direction = false;
    public Boolean isLink = false;
    List<BaseContentBean> settingBeanList = new ArrayList();
    public String tips;
    public String title;
    public int type;

    public interface OnFunctionSelectListener {
        void onSelect(String str);
    }

    public FunctionBean(int i, String str, String str2, String str3) {
        this.type = i;
        this.title = str;
        this.desc = str2;
        this.tips = str3;
    }

    public FunctionBean(int i, String str, String str2, String str3, Boolean bool) {
        this.type = i;
        this.title = str;
        this.isLink = bool;
        this.desc = str2;
        this.tips = str3;
    }

    public BaseImpl getImpl() {
        BaseImpl baseImpl;
        char c = 65535;
        BaseImpl baseImpl2 = null;
        switch (this.type) {
            case 1:
                DirectionFriendImpl directionFriendImpl = new DirectionFriendImpl(null);
                directionFriendImpl.privateType = this.contentBeanList.get(2).tag.toString();
                directionFriendImpl.isFans = Boolean.valueOf(this.contentBeanList.get(1).tag.equals("1"));
                return directionFriendImpl;
            case 2:
                HomeRandomFriendImpl homeRandomFriendImpl = new HomeRandomFriendImpl(null);
                homeRandomFriendImpl.privateType = this.contentBeanList.get(1).tag.toString();
                homeRandomFriendImpl.runNum = Integer.parseInt(this.contentBeanList.get(2).tag.toString());
                return homeRandomFriendImpl;
            case 3:
                BatchUserImpl batchUserImpl = new BatchUserImpl(null);
                batchUserImpl.runNum = Integer.parseInt(this.contentBeanList.get(0).content);
                batchUserImpl.privateType = this.contentBeanList.get(1).tag.toString();
                return batchUserImpl;
            case 4:
                AddressFriendImpl addressFriendImpl = new AddressFriendImpl(null);
                addressFriendImpl.privateType = this.contentBeanList.get(0).tag.toString();
                return addressFriendImpl;
            case 5:
                WorkCommenterImpl workCommenterImpl = new WorkCommenterImpl(null);
                workCommenterImpl.privateType = this.contentBeanList.get(0).tag.toString();
                return workCommenterImpl;
            case 6:
                LiveMemberImpl liveMemberImpl = new LiveMemberImpl(null);
                liveMemberImpl.privateType = this.contentBeanList.get(0).tag.toString();
                return liveMemberImpl;
            case 7:
                if (this.contentBeanList.get(0).tag.equals("0")) {
                    return new HomeRecommendCommentImpl(null);
                }
                if (this.contentBeanList.get(0).tag.equals("1")) {
                    return new UserWorksCommentImpl(null);
                }
                if (this.contentBeanList.get(0).tag.equals("2")) {
                    return new LiveSquareImpl(null);
                }
                return new SameCityImpl(null);
            case 9:
                FriendsListImpl friendsListImpl = new FriendsListImpl(null);
                friendsListImpl.privateType = "2";
                String str = this.contentBeanList.get(0).tag;
                switch (str.hashCode()) {
                    case 48:
                        if (str.equals("0")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 49:
                        if (str.equals("1")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 50:
                        if (str.equals("2")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    friendsListImpl.txtType = TxtType.TXT;
                    return friendsListImpl;
                } else if (c == 1) {
                    friendsListImpl.txtType = TxtType.IMG;
                    return friendsListImpl;
                } else if (c != 2) {
                    return friendsListImpl;
                } else {
                    friendsListImpl.txtType = TxtType.TXT_IMG;
                    return friendsListImpl;
                }
            case 10:
                return new MessageListImpl(null);
            case 11:
                return new RecommentCulitivateImple(null);
            case 12:
                return new WorksCulitivateImple(null);
            case 13:
                String str2 = this.contentBeanList.get(0).tag;
                switch (str2.hashCode()) {
                    case 48:
                        if (str2.equals("0")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 49:
                        if (str2.equals("1")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 50:
                        if (str2.equals("2")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 51:
                        if (str2.equals("3")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    baseImpl = new VideoPraiseImpl(null);
                } else if (c == 1) {
                    baseImpl = new VideoPraiseImpl(null);
                } else if (c == 2) {
                    baseImpl = new VideoPraiseImpl(null);
                } else if (c == 3) {
                    baseImpl = new CommenterPraiseImpl(null);
                }
                baseImpl2 = baseImpl;
                break;
            case 14:
                return new HomeRecommendFriendImpl(null);
            case 15:
                SameCityPrivateImpl sameCityPrivateImpl = new SameCityPrivateImpl(null);
                sameCityPrivateImpl.privateType = this.contentBeanList.get(0).tag.toString();
                return sameCityPrivateImpl;
            case 16:
                BatchFriendImpl batchFriendImpl = new BatchFriendImpl(null);
                batchFriendImpl.atten = Boolean.valueOf(this.contentBeanList.get(0).tag.equals("1"));
                return batchFriendImpl;
        }
        return baseImpl2;
    }

    public BaseImpl getLinkImpl() {
        ConfigBean configBean2;
        if (!this.direction.booleanValue() && (configBean2 = this.configBean) != null) {
            configBean2.id = null;
        }
        if (this.configBean == null) {
            this.configBean = new ConfigBean(1, 1);
        }
        BaseImpl impl = getImpl();
        impl.setParams(this.configBean);
        return impl;
    }

    public void showSetting(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        String str = "";
        if (Utils.isEmptyArray(this.settingBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            BaseContentBean baseContentBean2 = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "结束条件";
            baseContentBean.hint = "请选择结束条件";
            baseContentBean.content = str;
            baseContentBean2.type = 1;
            baseContentBean2.msg = "参数";
            baseContentBean2.hint = "请输入结束参数";
            baseContentBean2.content = str;
            baseContentBean2.inputType = 2;
            this.settingBeanList.add(baseContentBean);
            this.settingBeanList.add(baseContentBean2);
        }
        if (this.settingBeanList.size() == 2 && this.direction.booleanValue()) {
            BaseContentBean baseContentBean3 = new BaseContentBean();
            baseContentBean3.type = 1;
            baseContentBean3.msg = "指定用户id";
            baseContentBean3.hint = "请输入指定用户id";
            ConfigBean configBean2 = this.configBean;
            baseContentBean3.content = configBean2 == null ? str : configBean2.id;
            ConfigBean configBean3 = this.configBean;
            if (configBean3 != null) {
                str = configBean3.id;
            }
            baseContentBean3.tag = str;
            this.settingBeanList.add(baseContentBean3);
        }
        if (this.settingBeanList.size() == 3 && !this.direction.booleanValue()) {
            this.settingBeanList.remove(2);
        }
        this.settingBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("最大运行时间");
                arrayList.add("最多操作数量");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "结束条件", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.settingBeanList.get(0).content = (String) arrayList.get(i);
                        FunctionBean.this.settingBeanList.get(0).tag = i + "";
                        if (i == 0) {
                            FunctionBean.this.settingBeanList.get(1).msg = "最大运行时间，单位秒";
                        } else {
                            FunctionBean.this.settingBeanList.get(1).msg = "最大操作数量，单位个";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, "修改运行配置", "确认", this.settingBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                if (FunctionBean.this.settingBeanList.size() == 2) {
                    FunctionBean functionBean = FunctionBean.this;
                    functionBean.configBean = new ConfigBean(Integer.parseInt(functionBean.settingBeanList.get(0).tag.toString()), Integer.parseInt(FunctionBean.this.settingBeanList.get(1).tag.toString()));
                } else {
                    FunctionBean functionBean2 = FunctionBean.this;
                    functionBean2.configBean = new ConfigBean(Integer.parseInt(functionBean2.settingBeanList.get(0).tag.toString()), Integer.parseInt(FunctionBean.this.settingBeanList.get(1).tag.toString()), FunctionBean.this.settingBeanList.get(2).tag);
                }
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    public void showData(Context context, OnFunctionSelectListener onFunctionSelectListener) {
        switch (this.type) {
            case 1:
                showFunction1(context, onFunctionSelectListener);
                return;
            case 2:
                showFunction2(context, onFunctionSelectListener);
                return;
            case 3:
                if (Build.VERSION.SDK_INT < 24) {
                    Toast.makeText(App.getInstance(), "该功能需安卓7.0以上支持", 0).show();
                    return;
                } else {
                    showBatchUserDialog(context, onFunctionSelectListener);
                    return;
                }
            case 4:
                showFunction4(context, onFunctionSelectListener);
                return;
            case 5:
                if (Build.VERSION.SDK_INT < 24) {
                    Toast.makeText(App.getInstance(), "该功能需安卓7.0以上支持", 0).show();
                    return;
                } else {
                    showFunction5(context, onFunctionSelectListener);
                    return;
                }
            case 6:
                showFunction6(context, onFunctionSelectListener);
                return;
            case 7:
                showFunction7(context, onFunctionSelectListener);
                return;
            case 9:
                showFunction9(context, onFunctionSelectListener);
                return;
            case 10:
                showFunction10(onFunctionSelectListener);
                return;
            case 11:
                showFunction11(onFunctionSelectListener);
                return;
            case 12:
                if (Build.VERSION.SDK_INT < 24) {
                    Toast.makeText(context, "该功能需安卓7.0以上支持", 0).show();
                    return;
                } else {
                    showFunction12(onFunctionSelectListener);
                    return;
                }
            case 13:
                if (Build.VERSION.SDK_INT < 24) {
                    Toast.makeText(context, "该功能需安卓7.0以上支持", 0).show();
                    return;
                } else {
                    showFunction13(context, onFunctionSelectListener);
                    return;
                }
            case 14:
                showFunction14(onFunctionSelectListener);
                return;
            case 15:
                showFunction15(context, onFunctionSelectListener);
                return;
            case 16:
                showFunction16(context, onFunctionSelectListener);
                return;
            default:
                return;
        }
    }

    private void showFunction16(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("批量关注");
                arrayList.add("批量取消关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(0).tag = "1";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(0).tag = "2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction15(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(0).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(0).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(0).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction11(OnFunctionSelectListener onFunctionSelectListener) {
        onFunctionSelectListener.onSelect(this.tips);
    }

    private void showFunction12(OnFunctionSelectListener onFunctionSelectListener) {
        onFunctionSelectListener.onSelect(this.tips);
    }

    private void showFunction10(OnFunctionSelectListener onFunctionSelectListener) {
        onFunctionSelectListener.onSelect(this.tips);
    }

    private void showFunction13(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("首页推荐视频");
                arrayList.add("个人所有视频");
                arrayList.add("同城视频");
                arrayList.add("当前作品评论者");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(0).tag = "0";
                            FunctionBean.this.tips = "请先打开首页推荐，再点击开始按钮";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(0).tag = "1";
                            FunctionBean.this.tips = "请先打开个人主页第一个作品，再点击开始按钮";
                        } else if (i == 2) {
                            FunctionBean.this.contentBeanList.get(0).tag = "2";
                            FunctionBean.this.tips = "请先打开同城第一个视频，再点击开始按钮";
                        } else {
                            FunctionBean.this.contentBeanList.get(0).tag = "3";
                            FunctionBean.this.tips = "请先打开当前作品评论列表，再点击开始按钮";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction14(OnFunctionSelectListener onFunctionSelectListener) {
        onFunctionSelectListener.onSelect(this.tips);
    }

    private void showFunction4(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(0).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(0).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(0).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction5(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(0).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(0).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(0).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction6(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(0).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(0).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(0).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction7(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作对象";
            baseContentBean.hint = "请选择操作对象";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("推荐视频");
                arrayList.add("指定用户的所有作品");
                arrayList.add("直播广场");
                arrayList.add("同城列表");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作范围", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        if ((i == 1 || i == 3) && Build.VERSION.SDK_INT < 24) {
                            Toast.makeText(context, "该功能需安卓7.0以上支持", 0).show();
                            return;
                        }
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        FunctionBean.this.contentBeanList.get(0).tag = i + "";
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                FunctionBean functionBean = FunctionBean.this;
                functionBean.direction = Boolean.valueOf(functionBean.contentBeanList.get(0).tag.equals("1"));
                OnFunctionSelectListener onFunctionSelectListener = onFunctionSelectListener;
                onFunctionSelectListener.onSelect("请先打开" + FunctionBean.this.contentBeanList.get(0).content + ",再点击开始按钮");
            }
        });
        this.contentDialog.show();
    }

    private void showFunction9(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作类型";
            baseContentBean.hint = "请选择操作类型";
            baseContentBean.content = "";
            this.contentBeanList.add(baseContentBean);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("发文字");
                arrayList.add("发图片");
                arrayList.add("发图文");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        FunctionBean.this.contentBeanList.get(0).tag = i + "";
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, this.title, "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    private void showFunction2(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "操作范围";
            baseContentBean.hint = "请选择操作范围";
            baseContentBean.content = "";
            BaseContentBean baseContentBean2 = new BaseContentBean();
            baseContentBean2.type = 0;
            baseContentBean2.msg = "操作类型";
            baseContentBean2.hint = "请选择操作类型";
            baseContentBean2.content = "";
            BaseContentBean baseContentBean3 = new BaseContentBean();
            baseContentBean3.type = 1;
            baseContentBean3.msg = "单用户操作数量";
            baseContentBean3.hint = "请输入单个首页用户可操作抖友数量";
            baseContentBean3.content = "";
            baseContentBean3.inputType = 2;
            this.contentBeanList.add(baseContentBean);
            this.contentBeanList.add(baseContentBean2);
            this.contentBeanList.add(baseContentBean3);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("关注列表");
                arrayList.add("粉丝列表");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作范围", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                        FunctionBean.this.contentBeanList.get(0).tag = i + "";
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentBeanList.get(1).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(1).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(1).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(1).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(1).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, "首页随机用户的抖友", "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                App.getInstance().isFans = FunctionBean.this.contentBeanList.get(0).tag.equals("1");
                onFunctionSelectListener.onSelect("请先打开首页，再点击开始按钮");
            }
        });
        this.contentDialog.show();
    }

    private void showFunction1(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 0;
            baseContentBean.msg = "目标对象";
            baseContentBean.hint = "目标对象";
            baseContentBean.content = "";
            BaseContentBean baseContentBean2 = new BaseContentBean();
            baseContentBean2.type = 0;
            baseContentBean2.msg = "操作范围";
            baseContentBean2.hint = "请选择操作范围";
            baseContentBean2.content = "";
            BaseContentBean baseContentBean3 = new BaseContentBean();
            baseContentBean3.type = 0;
            baseContentBean3.msg = "操作类型";
            baseContentBean3.hint = "请选择操作类型";
            baseContentBean3.content = "";
            this.contentBeanList.add(baseContentBean);
            this.contentBeanList.add(baseContentBean2);
            this.contentBeanList.add(baseContentBean3);
        }
        this.contentBeanList.get(0).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("我的");
                arrayList.add("指定用户");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "目标对象", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        if (i != 1 || !FunctionBean.this.isLink.booleanValue() || Build.VERSION.SDK_INT >= 24) {
                            FunctionBean.this.contentBeanList.get(0).content = (String) arrayList.get(i);
                            FunctionBean.this.contentBeanList.get(0).tag = i + "";
                            FunctionBean.this.contentDialog.refreshData();
                            return;
                        }
                        Toast.makeText(App.getInstance(), "该功能需安卓7.0以上支持", 0).show();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentBeanList.get(1).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("关注的用户");
                arrayList.add("粉丝列表");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作范围", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(1).content = (String) arrayList.get(i);
                        FunctionBean.this.contentBeanList.get(1).tag = i + "";
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentBeanList.get(2).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(2).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(2).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(2).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(2).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, "操作抖友", "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                FunctionBean functionBean = FunctionBean.this;
                functionBean.direction = Boolean.valueOf(functionBean.contentBeanList.get(0).tag.equals("1"));
                Object[] objArr = new Object[2];
                objArr[0] = FunctionBean.this.contentBeanList.get(0).content;
                objArr[1] = FunctionBean.this.contentBeanList.get(1).tag.equals("0") ? "关注" : "粉丝";
                onFunctionSelectListener.onSelect(String.format("请先打开%s%s列表，再点击开始按钮", objArr));
            }
        });
        this.contentDialog.show();
    }

    private void showBatchUserDialog(final Context context, final OnFunctionSelectListener onFunctionSelectListener) {
        if (Utils.isEmptyArray(this.contentBeanList)) {
            BaseContentBean baseContentBean = new BaseContentBean();
            baseContentBean.type = 1;
            baseContentBean.msg = "操作数量";
            baseContentBean.inputType = 2;
            baseContentBean.hint = "单关键词操作数量";
            baseContentBean.content = "1";
            BaseContentBean baseContentBean2 = new BaseContentBean();
            baseContentBean2.type = 0;
            baseContentBean2.msg = "操作类型";
            baseContentBean2.hint = "请选择操作类型";
            baseContentBean2.content = "";
            this.contentBeanList.add(baseContentBean);
            this.contentBeanList.add(baseContentBean2);
        }
        this.contentBeanList.get(1).clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add("私信");
                arrayList.add("关注");
                arrayList.add("私信+关注");
                DataSelectDialog dataSelectDialog = new DataSelectDialog(context, "操作类型", arrayList);
                dataSelectDialog.setOnDataSelectListener(new DataSelectDialog.OnDataSelectListener() {
                    public void onDataSelect(int i) {
                        FunctionBean.this.contentBeanList.get(1).content = (String) arrayList.get(i);
                        if (i == 0) {
                            FunctionBean.this.contentBeanList.get(1).tag = "2";
                        } else if (i == 1) {
                            FunctionBean.this.contentBeanList.get(1).tag = "1";
                        } else {
                            FunctionBean.this.contentBeanList.get(1).tag = "1,2";
                        }
                        FunctionBean.this.contentDialog.refreshData();
                    }
                });
                dataSelectDialog.show();
            }
        };
        this.contentDialog = new CustomContentDialog(context, "单关键词操作数量", "确认", this.contentBeanList);
        this.contentDialog.setOnDataSelectListener(new CustomContentDialog.OnDialogConfimListener() {
            public void onDataConfirm() {
                onFunctionSelectListener.onSelect(FunctionBean.this.tips);
            }
        });
        this.contentDialog.show();
    }

    public Boolean configSuccess() {
        return this.configBean != null ? !this.direction.booleanValue() || !TextUtils.isEmpty(this.configBean.id) : this.type == 11;
    }
}
