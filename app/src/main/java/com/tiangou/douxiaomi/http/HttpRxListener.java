package com.tiangou.douxiaomi.http;

public interface HttpRxListener<T> {
    void httpResponse(T t, boolean z, int i);
}
