package com.tiangou.douxiaomi.http;

import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersIntercetor implements Interceptor {
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response proceed;
        synchronized (HeadersIntercetor.class) {
            Request request = chain.request();
            HttpUrl url = request.url();
            Request.Builder newBuilder = request.newBuilder();
            List<String> headers = request.headers("urlname");
            if (headers != null && headers.size() > 0) {
                newBuilder.removeHeader("urlname");
                String str = headers.get(0);
                HttpUrl httpUrl = null;
                if ("host1".equals(str)) {
                    httpUrl = HttpUrl.parse(Constant.HTTP1);
                } else if ("host2".equals(str)) {
                    httpUrl = HttpUrl.parse(Constant.HTTP2);
                }
                newBuilder.url(url.newBuilder().scheme(httpUrl.scheme()).host(httpUrl.host()).port(httpUrl.port()).build()).build();
            }
            proceed = chain.proceed(newBuilder.build());
        }
        return proceed;
    }
}
