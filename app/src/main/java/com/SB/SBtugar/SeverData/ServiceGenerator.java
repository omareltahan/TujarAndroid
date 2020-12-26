package com.SB.SBtugar.SeverData;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/1/2018.
 */

public class ServiceGenerator {
    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
    public static <S> S createService(Class<S> serviceClass, String baseUrl, String userToken, TokenType type) {
        Retrofit.Builder builder = new Retrofit.Builder();

        final String basic;
        if (userToken != null ) {
            if (type == TokenType.FIREBATOKEN_TYPE) {
                basic = "Bekala " + userToken;
            } else {
                basic = "Token " + userToken;
            }
        } else {
            basic = "";
        }
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Authorization", basic)
                        .addHeader("Accept", "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        clientBuilder.connectTimeout(50, TimeUnit.SECONDS);
        clientBuilder.readTimeout(50, TimeUnit.SECONDS);

        clientBuilder.addInterceptor(logging);

        OkHttpClient client = clientBuilder.build();

        builder.baseUrl(baseUrl);
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit adapter = builder.build();
        return adapter.create(serviceClass);
    }



    public static <S> S createService(Class<S> serviceClass, String baseUrl){

    Retrofit.Builder builder = new Retrofit.Builder();
    final String authToken = Credentials.basic("api_access", "$uper12345");

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder
                .addInterceptor(new BasicAuthInterceptor("api_access", "$uper12345"));

        clientBuilder.connectTimeout(50, TimeUnit.SECONDS);
    clientBuilder.readTimeout(50, TimeUnit.SECONDS);
    OkHttpClient client = clientBuilder.build();
    builder.baseUrl(baseUrl);
    builder.client(client);
    builder.addConverterFactory(GsonConverterFactory.create());
    Retrofit adapter = builder.build();
    return adapter.create(serviceClass);
}

}
class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor(String user, String password) {
        this.credentials = Credentials.basic(user, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }

}


