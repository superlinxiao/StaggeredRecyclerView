package com.wuxiaolong.pullloadmorerecyclerviewsample.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/** Description Author lizheng Create Data 2018\4\12 0012 */
public interface HomeService {

  /** 获取用户信息 */
  @Headers({"Content-Type: application/json", "Accept: application/json"})
  @GET("pic.php")
  Observable<PicBean> getPic();

}
