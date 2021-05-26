package com.app.imagin.server.services.sendimage.response;

import com.google.gson.annotations.SerializedName;

public class SendImageResponseMetadata {

    @SerializedName("color_type")
    public String colorType;

    @SerializedName("photo_quality")
    public int photoQuality;
}
