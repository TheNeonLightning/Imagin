package com.app.imagin.server.services.login.response;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    String id;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("email")
    String email;
}
