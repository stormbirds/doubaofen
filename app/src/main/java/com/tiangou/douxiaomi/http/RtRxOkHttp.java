package com.tiangou.douxiaomi.http;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RtRxOkHttp<T> {
    private static BaseApi api;
    private static RtRxOkHttp instance;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    public void toastMsg(String str) {
    }

    public static RtRxOkHttp getInstance() {
        if (instance == null) {
            instance = new RtRxOkHttp();
        }
        return instance;
    }

    public static void init() {
        generateOkhttpClient();
        retrofit = new Retrofit.Builder().baseUrl(Constant.HTTP1).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        api = (BaseApi) retrofit.create(BaseApi.class);
    }

    public static BaseApi getApiService(String str) {
        generateOkhttpClient();
        return (BaseApi) new Retrofit.Builder().baseUrl(str).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(BaseApi.class);
    }

    public static BaseApi reCreateApi() {
        api = null;
        okHttpClient = null;
        init();
        return api;
    }

    public static BaseApi getApiService() {
        if (api == null) {
            init();
        }
        return api;
    }

    public void createRtRx(Observable<T> observable, final HttpRxListener httpRxListener, final int i) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(T t) {
                try {
                    if (httpRxListener != null && t != null) {
                        httpRxListener.httpResponse(t, true, i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onError(Throwable th) {
                HttpRxListener httpRxListener = httpRxListener;
                if (httpRxListener != null) {
                    httpRxListener.httpResponse(null, false, i);
                }
                if (th instanceof UnknownHostException) {
                    RtRxOkHttp.this.toastMsg("网络连接失败，请检查您的网络");
                } else if (th instanceof TimeoutException) {
                    RtRxOkHttp.this.toastMsg("网络连接超时，请检查您的网络");
                } else {
                    RtRxOkHttp.this.toastMsg("服务器异常,请稍后重试");
                }
                Log.e("qyh", "服务器down" + th.getMessage());
            }
        });
    }

    public static void generateOkhttpClient() {
        if (okHttpClient == null) {
            synchronized (RtRxOkHttp.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder overlockCard = overlockCard(new OkHttpClient.Builder());
                    overlockCard.retryOnConnectionFailure(true);
                    overlockCard.addInterceptor(new HeadersIntercetor());
                    overlockCard.connectTimeout(30, TimeUnit.SECONDS);
                    overlockCard.readTimeout(30, TimeUnit.SECONDS);
                    overlockCard.writeTimeout(30, TimeUnit.SECONDS);
                    okHttpClient = overlockCard.build();
                    okHttpClient.dispatcher().setMaxRequestsPerHost(1);
                    okHttpClient.dispatcher().setMaxRequests(1);
                }
            }
        }
    }

    private static OkHttpClient.Builder overlockCard(OkHttpClient.Builder builder) {
        SSLContext sSLContext;
        TrustManager[] trustManagerArr = {new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try {
            sSLContext = SSLContext.getInstance("SSL");
            try {
                sSLContext.init((KeyManager[]) null, trustManagerArr, new SecureRandom());
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            sSLContext = null;
            e.printStackTrace();
            builder.sslSocketFactory(sSLContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String str, SSLSession sSLSession) {
                    return true;
                }
            });
            return builder;
        }
        builder.sslSocketFactory(sSLContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }
        });
        return builder;
    }
}
