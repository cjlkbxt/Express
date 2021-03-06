package express.tutu.com.commonsdk.network;

import java.util.concurrent.TimeUnit;

import express.tutu.com.commonsdk.BuildConfig;
import express.tutu.com.commonsdk.network.configs.NetworkConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cjlkbxt on 2018/7/7/007.
 * 静态内部类单例
 */

public class RetrofitManager {
    private Retrofit mRetrofit;

    private RetrofitManager(){
        initRetrofit();
    }

    public static RetrofitManager getInstance(){
        return SingletonHolder.sInstance;
    }

    //静态内部类
    private static class SingletonHolder{
        private static final RetrofitManager sInstance = new RetrofitManager();
    }

    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor();
        LoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG){
            //添加retrofit日志打印
            builder.addInterceptor(LoginInterceptor);
        }
        builder.connectTimeout(NetworkConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetworkConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public <T> T getService(Class<T> reqServer){
        return mRetrofit.create(reqServer);
    }

}
