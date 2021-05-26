package com.app.imagin.server;

import android.content.Context;
import android.content.SharedPreferences;
import com.app.imagin.R;

// Controls the JWT token used in interaction with server
public class SessionManager {

    private final SharedPreferences preferences;
    private static final String USER_TOKEN = "user_token";

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
    }

    public void  setAuthToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String fetchAuthToken() {
        return preferences.getString(USER_TOKEN, null);
    }
}






