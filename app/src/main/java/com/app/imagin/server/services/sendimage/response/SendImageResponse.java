package com.app.imagin.server.services.sendimage.response;

import com.google.gson.annotations.SerializedName;

public class SendImageResponse {

    @SerializedName("file")
    String file;

    @SerializedName("metadata")
    SendImageResponseMetadata metadata;

    public String getImage() {
        return file;
    }

    public SendImageResponseMetadata getMetadata() {
        return metadata;
    }
}
