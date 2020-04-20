package com.tiangou.douxiaomi.bean;

public class ConfigBean {
    public int count = 100;
    public String id;
    public int type = 0;

    public ConfigBean(int i, int i2) {
        this.type = i;
        this.count = i2;
        this.id = null;
    }

    public ConfigBean(int i, int i2, String str) {
        this.type = i;
        this.count = i2;
        this.id = str;
    }
}
