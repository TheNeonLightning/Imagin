package com.app.imagin.server.services.receivecolortype;
import com.app.imagin.server.services.receivecolortype.response.ColorType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReceiveColorType {
    @GET("") // TODO specify URL
    Call<ArrayList<ColorType>> receiveColorType();
}
