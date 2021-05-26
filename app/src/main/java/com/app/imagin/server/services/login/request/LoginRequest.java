package com.app.imagin.server.services.login.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;
}
