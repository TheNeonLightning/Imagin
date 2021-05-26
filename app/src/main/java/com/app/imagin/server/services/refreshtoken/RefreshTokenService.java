package com.app.imagin.server.services.refreshtoken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

// currently unused service
public interface RefreshTokenService {
    @POST("") // TODO specify URL
    Call<String> refreshToken(@Field("refreshToken") String refreshToken);
}
