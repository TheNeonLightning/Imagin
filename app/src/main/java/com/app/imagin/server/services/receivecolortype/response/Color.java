package com.app.imagin.server.services.receivecolortype.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Color {

    @SerializedName("color")
    public String color;

    @SerializedName("hex_values")
    public ArrayList<String> hexValues;
}
