package com.dhc.library.data;

import android.content.Context;
import android.text.TextUtils;

import com.dhc.library.data.cache.ICache;
import com.dhc.library.data.net.CacheInterceptor;
import com.dhc.library.data.net.CallInterceptor;
import com.dhc.library.data.net.StringConverterFactory;
import com.dhc.library.utils.AppUtil;
import com.dhc.library.utils.file.FileUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 创建者：邓浩宸
 * 时间 ：2016/11/15 15:59
 * 描述 ：网络请求的辅助类
 */
public class HttpHelper implements IDataHelper {


    private static NetConfig netConfig = new NetConfig();



    private static OkHttpClient okHttpClient = null;

    private static Retrofit retrofit = null;

    public Context context;

    private Gson gson;

    private ICache iCache;

    public HttpHelper(Context context,ICache iCache) {
        //Map used to store RetrofitService
        this.iCache=iCache;
        this.context = context;
    }

    public void initConfig(NetConfig netConfig) {
        this.netConfig = netConfig;
    }


    @SuppressWarnings("unchecked")
    @Override
    public <S> S getApi(Class<S> serviceClass) {
        if (iCache.contains(serviceClass.getName())) {
            return (S) iCache.get(serviceClass.getName());
        } else {
            Object obj = createApi(serviceClass);
            iCache.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S> S getApi(Class<S> serviceClass, OkHttpClient client) {
        if (iCache.contains(serviceClass.getName())) {
            return (S) iCache.get(serviceClass.getName());
        } else {
            Object obj = createApi(serviceClass, client);
            iCache.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    @Override
    public <S> S createApi(Class<S> serviceClass) {
        return createApi(serviceClass, getOkHttpClient());
    }


    @Override
    public <S> S createApi(Class<S> serviceClass, OkHttpClient client) {
        String baseURL = netConfig.baseURL;
        if (netConfig.isUseMultiBaseURL) {
            try {
                Field field1 = serviceClass.getField("baseURL");
                baseURL = (String) field1.get(serviceClass);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                baseURL = netConfig.baseURL;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                baseURL = netConfig.baseURL;
            }
            if (TextUtils.isEmpty(baseURL))
                throw new RuntimeException("baseUrl is null .please init by NetModule or apiService field BaseUrl");
        }
        if (retrofit != null && retrofit.baseUrl().host().equals(baseURL)) {
            return retrofit.create(serviceClass);
        } else {
            return getRetrofit(baseURL).create(serviceClass);
        }
    }

    @Override
    public OkHttpClient getClient() {
        if (okHttpClient != null) {
            return okHttpClient;
        } else {
            return getOkHttpClient();
        }
    }

    public Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().create();//Gson解析
        return gson;
    }

    /**
     * 如果是需要返回String的数据是使用
     *
     * @return Retrofit
     */
    public Retrofit getRetrofit(String host) {
        if (gson == null)
            gson = getGson();//Gson解析
        if (okHttpClient == null)
            okHttpClient = getOkHttpClient();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(host);//baseurl路径
        builder.client(okHttpClient);//添加客户端
        builder.addConverterFactory(new StringConverterFactory())//添加Gson格式化工厂
                .addConverterFactory(GsonConverterFactory.create(gson));//添加Gson格式化工厂
        if (netConfig.factories != null) {
            for (int i = 0; i < netConfig.factories.length; i++) {
                builder.addConverterFactory(netConfig.factories[i]);
            }
        }
        if (netConfig.isUseRx) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());//call 适配器
        }
        retrofit = builder.build();
        return retrofit;
    }


    public OkHttpClient getOkHttpClient() {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));//对cooke自动管理管理
        File cacheFile = new File(FileUtil.getCacheDirectory(context), "Cache");//缓存路径
        cacheFile = FileUtil.makeDirs(cacheFile);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 40);//设置缓存大小为40M
        //缓存
        CacheInterceptor cacheInterceptor = new CacheInterceptor(context);

        OkHttpClient.Builder builder =
                new OkHttpClient.Builder()
                        .cache(cache)
                        .addInterceptor(cacheInterceptor)
                        .addNetworkInterceptor(cacheInterceptor)
                        //                        .retryOnConnectionFailure(true)
                        .connectTimeout(netConfig.connectTimeoutMills != 0 ? netConfig.connectTimeoutMills : 15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)//写超时超时
                        .readTimeout(netConfig.readTimeoutMills != 0 ? netConfig.readTimeoutMills : 15, TimeUnit.SECONDS)//读超时
                        .cookieJar(netConfig.mCookieJar != null ? netConfig.mCookieJar : cookieJar);

        //设置https相关
        if (netConfig.mHttpsCall != null) {
            netConfig.mHttpsCall.configHttps(builder);
        }
        if (netConfig.call != null) {
            builder.addInterceptor(new CallInterceptor(netConfig.call));
        }
        if (netConfig.mInterceptors != null) {
            for (int i = 0; i < netConfig.mInterceptors.length; i++) {
                builder.addInterceptor(netConfig.mInterceptors[i]);
            }
        }
        if (AppUtil.isDebug()) {//如果当前是debug模式就开启日志过滤器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        //当前okHttpClient
        okHttpClient = builder.build();

        return okHttpClient;
    }

}
