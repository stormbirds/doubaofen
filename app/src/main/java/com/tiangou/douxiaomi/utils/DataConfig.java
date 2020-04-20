package com.tiangou.douxiaomi.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.azhon.appupdate.utils.Constant;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataConfig {
    public Boolean Disorder = true;
    public String content;
    public int durationMax = Constant.HTTP_TIME_OUT;
    public int durationMin = 2000;
    public String forwardStr;
    int i = 0;
    public int lookMax = 20000;
    public int lookMin = 20000;
    public Context mContext;
    public int maxCount = 100;
    public String[] message;
    public int messageCount = 1;
    public List<String> operaUser = null;
    public int operationMax = 10000;
    public int operationMin = 10000;
    public int runNum = 1;
    public int runSleep = 1000;
    public int runSleepMax = 1000;
    public List<Integer> userSex;
    public String users = null;
    public int yangAtten = 1;
    public int yangComment = 1;
    public int yangForward = 1;
    public int yangMax = Constant.HTTP_TIME_OUT;
    public int yangMin = 2000;
    public int yangPraise = 1;
    public int yangTotal = 100;

    public DataConfig(Context context) {
        this.mContext = context;
        this.userSex = new ArrayList();
        this.userSex.add(0);
        this.userSex.add(1);
        this.userSex.add(2);
        getData();
    }

    public void saveData() {
        SPUtils.saveInt(this.mContext, "maxCount", this.maxCount);
        SPUtils.saveInt(this.mContext, "durationMax", this.durationMax);
        SPUtils.saveInt(this.mContext, "durationMin", this.durationMin);
        SPUtils.saveInt(this.mContext, "runNum", this.runNum);
        SPUtils.saveInt(this.mContext, "runSleep", this.runSleep);
        SPUtils.saveInt(this.mContext, "runSleepMax", this.runSleepMax);
        SPUtils.saveInt(this.mContext, "yangtotal", this.yangTotal);
        SPUtils.saveInt(this.mContext, "yangpraise", this.yangPraise);
        SPUtils.saveInt(this.mContext, "yangcomment", this.yangComment);
        SPUtils.saveInt(this.mContext, "yangatten", this.yangAtten);
        SPUtils.saveInt(this.mContext, "yangforward", this.yangForward);
        SPUtils.saveInt(this.mContext, "yangMin", this.yangMin);
        SPUtils.saveInt(this.mContext, "yangMax", this.yangMax);
        SPUtils.saveInt(this.mContext, "messageCount", this.messageCount);
        SPUtils.saveString(this.mContext, "forwardStr", this.forwardStr);
        SPUtils.saveBoolean(this.mContext, "Disorder", this.Disorder.booleanValue());
        SPUtils.saveInt(this.mContext, "lookMin", this.lookMin);
        SPUtils.saveInt(this.mContext, "lookMax", this.lookMax);
        SPUtils.saveInt(this.mContext, "operationMin", this.operationMin);
        SPUtils.saveInt(this.mContext, "operationMax", this.operationMax);
    }

    public void getData() {
        this.maxCount = SPUtils.getInt(this.mContext, "maxCount", this.maxCount);
        this.durationMax = SPUtils.getInt(this.mContext, "durationMax", this.durationMax);
        this.Disorder = Boolean.valueOf(SPUtils.getBoolean(this.mContext, "Disorder", true));
        this.durationMin = SPUtils.getInt(this.mContext, "durationMin", this.durationMin);
        this.runNum = SPUtils.getInt(this.mContext, "runNum", this.runNum);
        this.messageCount = SPUtils.getInt(this.mContext, "messageCount", 1);
        this.runSleep = SPUtils.getInt(this.mContext, "runSleep", this.runSleep);
        this.runSleepMax = SPUtils.getInt(this.mContext, "runSleepMax", this.runSleepMax);
        this.yangTotal = SPUtils.getInt(this.mContext, "yangtotal", 100);
        this.yangPraise = SPUtils.getInt(this.mContext, "yangpraise", 0);
        this.yangComment = SPUtils.getInt(this.mContext, "yangcomment", 0);
        this.yangAtten = SPUtils.getInt(this.mContext, "yangatten", 0);
        this.yangForward = SPUtils.getInt(this.mContext, "yangforward", 0);
        this.yangMin = SPUtils.getInt(this.mContext, "yangMin", this.yangMin);
        this.yangMax = SPUtils.getInt(this.mContext, "yangMax", this.yangMax);
        this.forwardStr = SPUtils.getString(this.mContext, "forwardStr", "有爱转发");
        this.lookMin = SPUtils.getInt(this.mContext, "lookMin", this.lookMin);
        this.lookMax = SPUtils.getInt(this.mContext, "lookMax", this.lookMax);
        this.operationMin = SPUtils.getInt(this.mContext, "operationMin", this.operationMin);
        this.operationMax = SPUtils.getInt(this.mContext, "operationMax", this.operationMax);
        getUsers();
    }

    public String getUsers() {
        if (TextUtils.isEmpty(this.users)) {
            this.users = SPUtils.getString(this.mContext, "user", (String) null);
        }
        return this.users;
    }

    public void setUsers(String str) {
        this.users = str;
        SPUtils.saveString(this.mContext, "user", str);
        SPUtils.saveString(this.mContext, "useropera", str);
    }

    public List<String> getOperaUser() {
        if (Utils.isEmptyArray(this.operaUser)) {
            String string = SPUtils.getString(this.mContext, "useropera", (String) null);
            if (TextUtils.isEmpty(string)) {
                string = getUsers();
            }
            this.operaUser = Arrays.asList(string.split("\n"));
            this.operaUser = new ArrayList(this.operaUser);
        }
        return this.operaUser;
    }

    public void removeUser(String str) {
        if (!Utils.isEmptyArray(this.operaUser)) {
            this.operaUser.remove(str);
            String str2 = null;
            for (String next : this.operaUser) {
                if (str2 == null) {
                    str2 = next;
                } else {
                    str2 = str2 + "\n" + next;
                }
            }
            SPUtils.saveString(this.mContext, "useropera", str2);
        }
    }

    public String getMessage() {
        String message2 = getMessage(this.i);
        if (!TextUtils.isEmpty(message2)) {
            this.i++;
            if (this.i == this.message.length) {
                this.i = 0;
            }
        }
        return message2;
    }

    public String getMessage(int i2) {
        String[] strArr = this.message;
        if (strArr == null || strArr.length == 0) {
            if (this.content != null) {
                return "请输入内容";
            }
            this.content = getContent();
            this.message = this.content.split("%%");
        }
        String[] strArr2 = this.message;
        if (i2 > strArr2.length) {
            i2 = strArr2.length - 1;
        }
        if (this.Disorder.booleanValue()) {
            return this.message[new Random().nextInt(this.message.length)];
        }
        return this.message[i2];
    }

    public String getContent() {
        String str = this.content;
        if (str != null) {
            return str;
        }
        this.content = SPUtils.getString(this.mContext, "content", (String) null);
        String str2 = this.content;
        if (str2 != null) {
            this.message = str2.split("%%");
        }
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
        SPUtils.saveString(this.mContext, "content", str);
        this.message = str.split("%%");
    }

    public int getSendInterval() {
        return Utils.getNum(this.durationMin, this.durationMax);
    }

    public int getYangInterval() {
        return Utils.getNum(this.yangMin, this.yangMax);
    }

    public int getSleepTime() {
        return Utils.getNum(this.runSleep, this.runSleepMax);
    }

    public int getLookTime() {
        Log.e("qyh", "qyh== min=" + this.lookMin + "  max=" + this.lookMax);
        return Utils.getNum(this.lookMin, this.lookMax);
    }

    public int getoperationTime() {
        return Utils.getNum(this.operationMin, this.operationMax);
    }
}
