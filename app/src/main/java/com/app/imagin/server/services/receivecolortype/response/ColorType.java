package com.app.imagin.server.services.receivecolortype.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ColorType {

    @SerializedName("type")
    public String type;

    @SerializedName("possible_colors")
    public ArrayList<Color> colors;
}
