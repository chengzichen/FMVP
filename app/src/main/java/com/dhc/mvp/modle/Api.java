package com.dhc.mvp.modle;

import com.dhc.mvp.App.SampleApiResponse;
import com.dhc.mvp.modle.bean.GankItemBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/22 下午2:22
 * @description：TODO 请描述该类职责
 */
public interface Api {

    String baseURL= "http://gank.io/api/";
    /**
     * 随机妹纸图
     */
    @GET("random/data/福利/{num}")
    Flowable<SampleApiResponse<List<GankItemBean>>> getRandomGirl(@Path("num") int num);



}
