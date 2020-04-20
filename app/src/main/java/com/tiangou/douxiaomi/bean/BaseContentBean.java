package com.tiangou.douxiaomi.bean;

import android.view.View;
import java.io.Serializable;

public class BaseContentBean implements Serializable {
    public transient View.OnClickListener clickListener = null;
    public String content;
    public String hint = "请输入内容";
    public int inputType = -1;
    public String msg = "";
    public Boolean notNull = true;
    public int resId;
    public String tag = null;
    public int type = 0;
}
