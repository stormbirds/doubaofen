package com.tiangou.douxiaomi.http;

import com.tiangou.douxiaomi.bean.AnalysisBean;
import com.tiangou.douxiaomi.bean.BaseModel;
import com.tiangou.douxiaomi.bean.ConnectBean;
import com.tiangou.douxiaomi.bean.TypeBean;
import com.tiangou.douxiaomi.bean.UserNumBean;
import io.reactivex.Observable;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaseApi {
    @GET("/shipin.php")
    @Headers({"urlname:host2"})
    Observable<BaseModel<AnalysisBean>> analysis(@Query("url") String str);

    @POST("/api/daochu/save")
    @Headers({"urlname:host1"})
    Observable<BaseModel> export(@Body RequestBody requestBody);

    @POST("/api/category/index?project_id=100001")
    @Headers({"urlname:host1"})
    Observable<BaseModel<List<TypeBean>>> getClassType();

    @POST("/api/phonebook/find")
    @Headers({"urlname:host1"})
    Observable<BaseModel<List<ConnectBean>>> getConnectList(@Body RequestBody requestBody);

    @POST("/api/daochu/check")
    @Headers({"urlname:host1"})
    Observable<BaseModel<UserNumBean>> getUserNum(@Body RequestBody requestBody);
}
