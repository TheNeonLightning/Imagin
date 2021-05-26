package com.app.imagin.server.services.sendimage.request;

import com.google.gson.annotations.SerializedName;

public class SendImageRequest {

    @SerializedName("file")
    String file;

    public SendImageRequest(String file) {
        this.file = file;
    }
}
