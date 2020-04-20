package com.tiangou.douxiaomi.event;

import com.tiangou.douxiaomi.callback.SexGetSuccessListener;

public class GetScreenEvent {
    public SexGetSuccessListener listener;
    public int x;
    public int y;

    public GetScreenEvent(SexGetSuccessListener sexGetSuccessListener, int i, int i2) {
        this.listener = sexGetSuccessListener;
        this.x = i;
        this.y = i2;
    }
}
