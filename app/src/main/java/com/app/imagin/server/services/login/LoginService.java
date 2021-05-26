package com.app.imagin.server.services.login;

import com.app.imagin.server.services.login.request.LoginRequest;
import com.app.imagin.server.services.login.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("") // TODO specify URL
    Call<LoginResponse> login(@Body LoginRequest request);
}
