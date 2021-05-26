package com.app.imagin.intro;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.imagin.R;

// Controls that intro is only shown on the first launch
public class PreferencesManager {

    private final SharedPreferences preferences;
    private static final String IS_FIRST_LAUNCH = "is_first_launch";

    public PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
    }

    public void setIsFirstLaunch(boolean isFirstTimeLaunch) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstTimeLaunch);
        editor.apply();
    }

    public boolean isFirstLaunch() {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true);
    }
}

