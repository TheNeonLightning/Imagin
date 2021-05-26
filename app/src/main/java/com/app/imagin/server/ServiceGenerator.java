package com.app.imagin.server;

import android.content.Context;

import com.app.imagin.server.services.sendimage.TokenAuthenticator;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Builds impl for given service interface
public class ServiceGenerator {

    public static final String BASE_URL = ""; // TODO specify BASE_URL

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, Context context, Authenticator authenticator) {
        return builder.client(okHttpClient(context, authenticator)).build().create(serviceClass);
    }

    private static OkHttpClient okHttpClient(Context context, Authenticator authenticator) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context));

        // if needs JWT authentication (authenticator != null) then adding an authenticator
        if (authenticator != null) {
            clientBuilder.authenticator(authenticator);
        }

        return clientBuilder.build();
    }
}