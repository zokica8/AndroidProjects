package com.example.zesiumapplication.retrofit.instance;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// create a singleton of a retrofit instance
public class RetrofitInstance {

    private static final String BASE_URL = "http://192.168.43.117:8098/";
    private static Retrofit retrofit = null;
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build();

    private RetrofitInstance() {

    }

    public static Retrofit getRetrofitInstance() {
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(retrofit == null) {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
