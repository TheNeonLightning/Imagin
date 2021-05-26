package com.app.imagin.server.services.sendimage;

import com.app.imagin.server.services.sendimage.request.SendImageRequest;
import com.app.imagin.server.services.sendimage.response.SendImageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SendImageServiceBase64 {

    @POST("") // TODO specify URL
    Call<SendImageResponse> sendImage(@Body SendImageRequest request);

    @POST("") // TODO specify URL
    Call<String> refreshToken(@Field("refreshToken") String refreshToken);
}
