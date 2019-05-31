# 目录
- [前言](#前言)
- [特点](#特点)
- [快速使用](#快速使用)
- [使用依赖](#使用依赖)
- [代码结构](#代码结构)
- [具体使用](#具体使用)


# 前言


     欢迎使用Flyabbit全家桶


  🔜 FMVP: 快速开发的 MVP 框架,开箱即用

### 最新版本

模块|FMVP|FMVP  AS插件
---|---|---
最新版本|[ ![Download](https://api.bintray.com/packages/chengzichen/maven/mvp/images/download.svg) ](https://bintray.com/chengzichen/maven/mvp/_latestVersion) |![](https://img.shields.io/github/release/chengzichen/component.svg)


- 版本 1.1.x为已经迁移了AndroidX
- 版本 1.0.x为之前的版本

# 特点:

- ` MVP`更加简单实用,网络,缓存,数据库开箱即用
-  `degger2` ,`util`....每个Modle中提供了`XXDiHelper` 快速使用AOP依赖注入,一键注入你想要的
-  `rxjava`,`retrofit`让网络请求线程切换赢在起跑线上
-  使用`ARouter`路由解耦跳转更加灵活
-  `base`中封装了懒加载的`BaseFragment`,`BaseActivity`,满足你日常开发的各种动作和姿势
- `room`,`MemoryCache`,`SPHelper`和` RxCache `提供了强大的网络请求和缓存功能


# 快速使用

### 使用方法一:IDEA 插件(推荐)

- **IDE插件安装:**
	1. File->Setting->Plugins->搜索[componentPlugin](https://github.com/chengzichen/component)(或者[下载](https://github.com/chengzichen/component/blob/master/component.jar))
,安装完后重启Andriod Studio

	2. 在对应的目录下 -> new -> FMVP-File,填写模块名称 

		<div >   
		 <img src="https://github.com/chengzichen/Photo/raw/master/gif/newMVPFile2.gif" width = "500" height = "300" alt="图片名称" align=cente/>
		</div>
	**注意**:考虑到mvp模板路径的正确性，对模板生成的路径有限制（只有在Moudle下的java路径下才能生效），更好的引导大家使用

- **初始化Appliction并在AndroidManifest中注册为启动类**
	
  ```
    public class SampleApp extends BaseApplication {

	    @Override
	    public void onCreate() {
	        super.onCreate();
	             //todo
	   		 }
		}

  ```


- **填写 M-V-P 代码逻辑代码**


	- 定义 Api: 
	
	```
	public interface Api {
		
		    String baseURL= "http://gank.io/api/";//配置 baseurl也可以在NetConfit 中配置
		    @GET("random/data/福利/{num}")//还有不懂Retrofit的同学请看这里 https://blog.csdn.net/chengzichen_/article/details/77840996
		    Flowable<SampleApiResponse<List<GankItemBean>>> 	getRandomGirl(@Path("num") int num);
		
	}
	```
	- 定义Contract接口: 

	```
	public interface INetTestContract {

    interface IView<T> extends IBaseView {

        void success(T data);

        void failure(String code, String msg);
    }

    interface IPresenter extends IBasePresenter<IView> {

        void getRandomGirl();//or  todo   自己填写的逻辑

    }

    interface IModel extends IBaseModel {

        Flowable<SampleApiResponse<List<GankItemBean>>> getRandomGirl();//or  todo  自己填写的逻辑

   		 }
	}
	
	```	
	- 分别实现model和 presenter

	moudel示例:
	
	```
	public class NetTestRemoteDataService  implements INetTestContract.IModel {

    private HttpHelper mHttpHelper;

    @Inject
    public NetTestRemoteDataService(HttpHelper httpHelper) {
        this.mHttpHelper = httpHelper;
    }


    @Override
    public Flowable<SampleApiResponse<List<GankItemBean>>> getRandomGirl() {
        return mHttpHelper.createApi(Api.class).getRandomGirl(1).compose(RxUtil.<SampleApiResponse<List<GankItemBean>>>rxSchedulerHelper());
    }

	}
	
	```	
	
	presenter示例
	
	```
		public class NetTestPresenter extends XPresenter<INetTestContract.IView> implements INetTestContract.IPresenter {

    private NetTestRemoteDataService mNetTestRemoteDataService;

    @Inject
    public NetTestPresenter(NetTestRemoteDataService NetTestRemoteDataService) {
        mNetTestRemoteDataService = NetTestRemoteDataService;
    }

	
    @Override
    public void getRandomGirl() {
        mNetTestRemoteDataService.getRandomGirl()
                .compose(getV().<SampleApiResponse<List<GankItemBean>>>bindLifecycle())
                .subscribe(new SampleSubscriber<SampleApiResponse<List<GankItemBean>>>(new SampleSubscriberListener<List<GankItemBean>>() {
                    @Override
                    public void onSuccess(List<GankItemBean> response) {
                        getV().success(response);
                    }

                    @Override
                    public void onFail(NetError errorMsg) {
                        super.onFail(errorMsg);
                        getV().failure("-1", errorMsg.getMessage());
                    }
                }));
	   	 }
	}
    ```

- **使用 Dagger2注入对象**

	在ActivityComponent或者FragmentComponent中(/di/component/)定义需要注入的对象和方法:

	`void  inject(XXXXActivity xxxxActivity);
	`	

-  **在 Fragment 或者 Activity 中使用**

	``` 
	public class NetSampleActivity extends XDaggerActivity<NetTestPresenter> implements INetTestContract.IView<List<GankItemBean>> {
	
	
	    @Override
	    public int getLayoutId() {
	        return R.layout.activity_net_sample;
	    }
	
	    @Override
	    public void initEventAndData(Bundle savedInstanceState) {
	       mPresenter.getRandomGirl();//调用方法请求接口
	    }
	
	   
	    @Override
	    public void initInject(Bundle savedInstanceState) {
	        DiHelper.getActivityComponent(getActivityModule()).inject(this);
	    }
	
	    @Override
	    public void success(List<GankItemBean> data) {
	        //todo
	    }
	
	    @Override
	    public void failure(String code, String msg) {
			//todo
	    }
	}
	```
	**注意: 上面所列出的代码基本都有生成,都只需填充逻辑代码就可以了.**
	
	
	
### 使用方法二: 不使用插件,单独使用

- **Step 1. 在根目录的gradle文件中配置**

	```
	allprojects {
			repositories {
				...
				 jcenter()
			}
		}
	```

- **step2 添加依赖(By 3.0):**

	```
	dependencies {
	       api 'com.dhc.lib:mvp:1.0.7'
	       annotationProcessor "com.google.dagger:dagger-compiler:2.13"//dagger2注解
   			annotationProcessor "com.alibaba:arouter-compiler:1.1.2.1"// arouter 注解
	}
	```

- **step3 项目中使用**
		


	**步骤与上述一样,只不过需要手动实现代码,而IDE插件见过了这一过程.**




	


# 使用依赖:

-  [`fragmentation`](https://github.com/YoKeyword/Fragmentation) :**强大的Framgment支持库,如同Activity一样使用**
-  [`rxjava`](https://github.com/ReactiveX/RxJava),[`rxandroid`](https://github.com/ReactiveX/RxAndroid)	       :**这个不用说了,强大的链式编程线程调度库**
-  [`rxlifecycle` ](https://github.com/trello/RxLifecycle) :   **Lifecycle handling APIs for Android apps using RxJava**
- `gson`,`retrofit`,`okhttp3`,`okhttp3-logging-interceptor`...	  **网络请求框架整合**
-  [`rxpermissions `](https://github.com/tbruyelle/RxPermissions)    **Android runtime permissions powered by RxJava2**
-  [`RxCache`](https://github.com/VictorAlbertos/RxCache)      **Reactive caching library for Android and Java**
-  [`PersistentCookieJar`](https://github.com/franmontiel/PersistentCookieJar) **A persistent CookieJar implementation for OkHttp 3 based on SharedPreferences.**
-  [`dagger2`](https://github.com/google/dagger) **A fast dependency injector for Android and Java.**
-  [`arouter`](https://github.com/alibaba/ARouter)   **Android平台中对页面、服务提供路由功能的中间件**
-  [`room`](https://developer.android.com/topic/libraries/architecture/room.html) **Room是谷歌官方的数据库ORM框架**

# 代码结构:

    src
    └─com
       └─dhc
      	 └─library
			└─base
      	 		│  BaseApplication.java 		定义Application基类
       			│  BaseActivity.java  			无MVP的activity基类
       			│  BaseBean.java 				数据类的基类
       			│  BaseChildApplication.java  	该Application只能放在子moudle中使用,用于moudle隔离
       			│  BaseFragment.java 			无MVP的Fragment基类
       			│  BaseSubscriber.java     		调用者自己对请求数据进行处理
       			│  IBaseModle.java   			Modle请求数据的基类
       			│  IBasePresenter.java     		Presenter基类
				│  IBaseView.java     			View的基类
				│  XDaggerActivity.java   		MVP activity基类
				│  XDaggerFragment.java 		MVP Fragment基类
				│  XDataBindingActivity.java    Databinding和Dagger2使用的Activity//1.0.6以后已经剔除
				│  XDataBindingFragment.java   	Databinding和Dagger2使用的Fragment//1.0.6以后已经剔除
				│  XPresenter.java       		用于绑定view和解绑view
				│	other....
       		 └─data
       			├─account
       			│  AccountManager.java   		登陆账号管理类
       			├─Cache
       			│  MemoryCache.java				内存缓存
       			├─net
       			│  ApiResponse.java				Api响应结果的封装类
				│  TokenInterceptor.java		Token管理
				│  SubscriberListener.java 		业务异常干货统一处理
				│  CallInterceptor.java			做请求前和请求后的操作
       			├─DBHelper.java	  				room数据库帮助类
       			├─HttpHelper.java  				网络请求的辅助类
				├─SPHelper.java 				SharedPreferences统一管理类
       		└─di
			  ├─component
			  │	AppComponent.java 				App的注入使用
			  ├─module	....					注入对象提供者
			  │  other...
       		└─util
       			├─file 							文件处理工具
       			├─media							图文视频处理工具
				├─rx							rxjava处理工具
       			├─storage						储存文件夹处理工具
       			├─string						String处理工具
       			├─sys							系统类处理工具
       			├─AppContext					保存全局的Application
       			├─ApplicationLike				作为接口，方便主工程调度子模块的声明周期
       			├─AppManager					Activity进栈出栈统一管理
				.....



## 具体使用:



### 初始化拓展
- 初始化

	*可以让自己的主Application直接继承BaseApplication,如果有特殊的要求也可以把相对的代码复制到自己主Applic
	ation中,在 Appliction 初始化也提供了网络请求相关,以及Dagger2注入对象相关的配置*
	
		  public class   SampleApp extends BaseApplication{
			  public void onCreate() {
					//todo
				}
				...
			}



- **网络相关配置**

	- NetConfig配置

		配置网络请求BaseUrl,也可以配置多个BaseUrl,以及网络相关的配置,具体配置可参考[NetConfig ](https://github.com/chengzichen/Flyabbit/blob/28e53032bd051748c59daa85e1e8b64dd78107b4/baselib/library/src/main/java/com/dhc/library/data/IDataHelper.java#L29)
	
		
		```
	    public class SampleApp extends BaseApplication {
	
	    @Override
	    public void onCreate() {
	        super.onCreate();
	    }
	
	    /**
	     * 必须重新设置BaseUrl
	     *
	     * @return
	     */
	    @Override
	    public IDataHelper.NetConfig getNetConfig() {
	         ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new
	                SharedPrefsCookiePersistor(this));
	        return new IDataHelper.NetConfig()
	                .configBaseURL("www.desoce.com")//配置 baseUrl
	                .configConnectTimeoutMills(3)//配置当前(默认读写都是15)超时时间
	                .configConverterFactory( new Converter.Factory[]{ProtoConverterFactory.create()})//配置多个解析器
	                .configInterceptors(new Interceptor[]{new TokenInterceptor()})//配置多个拦截器
	                .configLogEnable(true)//是否打印日志
	                .configCookieJar(cookieJar)// 配置 cookie 缓存,如果没有配置就默认使用 PersistentCookieJar.
	                .configisUseMultiBaseURL(true)//是否开启对个 baserlU
	                .configIsUseRx(true) //是否使用rxjava适配器(默认是开启)
	                .configCall(new IDataHelper.RequestCall() {
	                    @Override
	                    public Request onBeforeRequest(Request request, Interceptor.Chain chain) {//请求之前
	                        return null;
	                    }
	
	                    @Override
	                    public Response onAfterRequest(Response response, ResponseBody result, Interceptor.Chain chain) {//数据返回之前
	                        return null;
	                    }
	                })//请求拦截器.
	                .configHttps(new IDataHelper.HttpsCall() {
	                    @Override
	                    public void configHttps(OkHttpClient.Builder builder) {
	                        
	                    }
	                })//okhttp 相关的配置处理.比如配置 https
	                ;
	     }
	    }
	    ```

	- 配置单个请求重连次数和时长

	    ```
		//在IModel中使用compose()方法,默认重连三次每次等待时间3秒,有特殊情况可以重新设置
	    RxUtil. <T extends ApiResponse>rxSchedulerHelper(int count, final long delay)
	    ```
		
- **Dagger2注入对象相关的配置**

	```
    public class SampleApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public AppComponent getAppComponent() {
        return 
                getAppComponentBuilder()
                .appModule(new AppModule(this))//默认添加
                .otherModule(new therModule())//添加注入,具体使用请参考 Dagger2
                ;
   		 }
	}
    ```


### 缓存技术
	
	
- **LruCache**

        ```
        //获取 当前 Appliction 对象就可以使用 LruCache(内存缓存),提供了 CRUD 方法

        ICache iCache=((BaseApplication)AppContext.get()).getAppComponent().memoryCache();

        iCache.put("key",object);//添加

        iCache.remove("key");//移除

        iCache.get("key")//获取

        iCache.contains("key");//是否包含

        iCache.clear();//清空
        ```

- **SharedPreferences**

	```	
	SPHelper.put(this,"key","object")// 添加

	SPHelper.get(context,"key",defaultType)//获取 defaltType must be
	
	SPHelper.contains(context,"key");//是否包含
	
	SPHelper.remove(context);//移除
	```	
	
- **数据库 Room缓存**

	- 添加注解
	
		```	
	     dependencies {
			android.arch.persistence.room:compiler:1.1.0-alpha1
		}
		```	

	- 字段与实体类的映射关系

		```	
		@Entity(tableName = "gankitem")
		public class GankItemBean {
		    @PrimaryKey
		    @ColumnInfo(name="id")
		    @NonNull
		    private String _id;
		    private String url;
		   
		    public String get_id() {
		        return _id;
		    }	
		    public void set_id(String _id) {
		        this._id = _id;
		    }
		
		   public String getUrl() {
		        return url;
		    }
		
		    public void setUrl(String url) {
		        this.url = url;
		    }
		}
		```	
	- 添加实体与表映射关系	
		
		```	
	    @Database(entities = {GankItemBean.class}, version = 1)//对应映射实体类,修改了字段必需升级版本号
		public abstract class AppDatabase extends RoomDatabase {
		
		    public abstract GankDao gankDao();
		
		}
		```	
	- 操作数据库	
	
		```	
		//获取 当前 Appliction 对象就可以使用Room
		
		DBHelper db=((BaseApplication)AppContext.get()).getAppComponent().dtabaseHelper();
		AppDatabase appDatabase=   ((BaseApplication)AppContext.get()).getAppComponent().dtabaseHelper().getApi(AppDatabase.class,"gankitem");
        GankItemBean gankItemBean=    new GankItemBean();
        gankItemBean.set_id("1");
        gankItemBean.setUrl("的设计费估计是");
        appDatabase.gankDao().insertAll(gankItemBean);//插入数据
        
        List<GankItemBean> gankItemBeans= appDatabase.gankDao().getAll();//查询所有数据
		
		```	
- **RxCache**
	- 定义 CacheApi
	
		```	
		public  interface CacheApi {
		    @LifeCache(duration = 2,timeUnit = TimeUnit.MINUTES)//两分钟失效
		    Flowable<SampleApiResponse<List<GankItemBean>>> getRandomGirl(Flowable<SampleApiResponse<List<GankItemBean>>> sampleApiResponseFlowable);
		}
		```	
	- 修改Presenter

		```	
		public class RxCacheTestPresenter extends XPresenter<INetTestContract.IView> implements INetTestContract.IPresenter {
		
		    private NetTestRemoteDataService mNetTestRemoteDataService;
		    private CacheApi cacheApi;
		
		    @Inject
		    public RxCacheTestPresenter(NetTestRemoteDataService NetTestRemoteDataService, RxCache rxCache) {
		        mNetTestRemoteDataService = NetTestRemoteDataService;
		        cacheApi= rxCache.using(CacheApi.class);//获取 Cache
		    }
		
		
		    @Override
		    public void getRandomGirl() {
		        cacheApi.getRandomGirl(mNetTestRemoteDataService.getRandomGirl())// 使用缓存
		                .compose(getV().<SampleApiResponse<List<GankItemBean>>>bindLifecycle())
		                .subscribe(new SampleSubscriber<SampleApiResponse<List<GankItemBean>>>(new SampleSubscriberListener<List<GankItemBean>>() {
		                    @Override
		                    public void onSuccess(List<GankItemBean> response) {
		                        getV().success(response);
		                    }
		
		                    @Override
		                    public void onFail(NetError errorMsg) {
		                        super.onFail(errorMsg);
		                        getV().failure("-1", errorMsg.getMessage());
		                    }
		                }));
		    }
		}
        ```

### 数据处理
- **ApiResponse**

参考自己具体业务进行处理
示例 :
ApiResponse

	public class SampleApiResponse <T> implements ApiResponse<T> {
    private boolean error;
    public T results;
    @Override
    public T getData() {...}
    public void setData(T data) {...}
    @Override
    public boolean isSuccess() {...}
    @Override
    public boolean checkReLogin() {
    return false;
    	}
	}


- **文件上传,下载**
	
	
	[网络请求的封装](https://blog.csdn.net/chengzichen_/article/details/77659318)
	
	
	


### 相关文章

- 关于FMVP:[https://github.com/chengzichen/Flyabbit/Fmvp](https://github.com/chengzichen/Flyabbit/blob/master/Fmvp%E4%BB%8B%E7%BB%8D.md)

第一篇-网络篇:

 - [[从零开始系列]AndroidApp研发之路(一) 网络请求的封装(一)](http://blog.csdn.net/chengzichen_/article/details/77659318)

第二篇-Retrofit源码解析

  - [[从零开始系列]AndroidApp研发之路-<楼外篇>Retrofit的刨根问底篇](http://blog.csdn.net/chengzichen_/article/details/77840996)
  
  更新中....
  
## 关于个人
     
   
  
  Github:[https://github.com/chengzichen](https://github.com/chengzichen)
  
  CSDN : [http://blog.csdn.net/chengzichen_](http://blog.csdn.net/chengzichen_)


<div  align="center"> 
本人一直都致力于组件化和插件化的研究如果大家有更好的想法可以联系我一起成长
</div>

<div  align="center">   
 <img src="https://i.imgur.com/J1LpBum.jpg" width = "200" height = "300" alt="图片名称" align=center />
</div>

