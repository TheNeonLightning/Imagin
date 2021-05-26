package com.app.imagin.server.services.login.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status_code")
    int statusCode;

    @SerializedName("auth_token")
    String authToken;

    @SerializedName("user")
    User user;

    public int getStatusCode() {
        return statusCode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public User getUser() {
        return user;
    }
}
