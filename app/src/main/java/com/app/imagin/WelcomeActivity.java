package com.app.imagin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.app.imagin.intro.PreferencesManager;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.github.paolorotolo.appintro.model.SliderPagerBuilder;

import static com.app.imagin.applications.Imagin.getContext;

public class WelcomeActivity extends AppIntro2 {

    PreferencesManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new PreferencesManager(this);

        if (preferences.isFirstLaunch()) {

            setFadeAnimation();

            showIntroSlides();
        } else {
            goToMain();
        }
    }

    private void showIntroSlides() {

        preferences.setIsFirstLaunch(false);

        int backgroundColor;
        int textColor;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            backgroundColor = getColor(R.color.background);
            textColor = getColor(R.color.intro_text);
        } else {
            backgroundColor = getResources().getColor(R.color.background);
            textColor = getResources().getColor(R.color.intro_text);
        }

        SliderPage intro1 = new SliderPagerBuilder()
                .title(getString(R.string.intro1_title))
                .titleColor(textColor)
                .description(getString(R.string.intro1_text))
                .descColor(textColor)
                .imageDrawable(R.drawable.cluster)
                .bgColor(backgroundColor)
                .build();

        SliderPage intro2 = new SliderPagerBuilder()
                .title(getString(R.string.intro2_title))
                .titleColor(textColor)
                .description(getString(R.string.intro2_text))
                .descColor(textColor)
                .imageDrawable(R.drawable.cluster)
                .bgColor(backgroundColor)
                .build();

        SliderPage intro3 = new SliderPagerBuilder()
                .title(getString(R.string.intro3_title))
                .titleColor(textColor)
                .description(getString(R.string.intro3_text))
                .descColor(textColor)
                .imageDrawable(R.drawable.cluster)
                .bgColor(backgroundColor)
                .build();

        addSlide(AppIntro2Fragment.newInstance(intro1));
        addSlide(AppIntro2Fragment.newInstance(intro2));
        addSlide(AppIntro2Fragment.newInstance(intro3));
    }

    private void goToMain() {
        Intent imageActivityScreen = new Intent(getContext(), ImageActivity.class);
        startActivity(imageActivityScreen);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        goToMain();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        goToMain();
    }
}
