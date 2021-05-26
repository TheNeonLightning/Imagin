package com.app.imagin;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.imagin.server.ServiceGenerator;
import com.app.imagin.server.services.receivecolortype.ReceiveColorType;
import com.app.imagin.server.services.receivecolortype.response.ColorType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.imagin.applications.Imagin.getContext;


public class ResultActivity extends Activity {

    private static final int NUMBER_OF_SAMPLES = 7;

    private LinearLayout colorSamples;
    private TextView colorTitle;
    private LinearLayout winterSeason;
    private LinearLayout autumnSeason;
    private LinearLayout summerSeason;
    private LinearLayout springSeason;

    private LinearLayout currentSeason;
    private LinearLayout winter;
    private LinearLayout autumn;
    private LinearLayout summer;
    private LinearLayout spring;

    private Button findClothesBtn;


    private ArrayList<ColorType> colorTypes;
    private String resultColorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        findClothesBtn = findViewById(R.id.find_clothes_btn);
        findClothesBtn.setOnClickListener(click -> {
            Toast.makeText(getContext(), "To be implemented...", Toast.LENGTH_SHORT).show();
        });

        getResultColorType();

        setCurrentColorTypeTitle(resultColorType);

        winterSeason = findViewById(R.id.season0);
        autumnSeason = findViewById(R.id.season1);
        summerSeason = findViewById(R.id.season2);
        springSeason = findViewById(R.id.season3);

        getColorTypes();

        winter = findViewById(R.id.winter);
        autumn = findViewById(R.id.autumn);
        summer = findViewById(R.id.summer);
        spring = findViewById(R.id.spring);

        winter.setOnClickListener(click -> onSeasonClick("winter", winter));

        autumn.setOnClickListener(click -> onSeasonClick("autumn", autumn));

        summer.setOnClickListener(click -> onSeasonClick("summer", summer));

        spring.setOnClickListener(click -> onSeasonClick("spring", spring));
    }

    private void onSeasonClick(String season, LinearLayout sample) {
        if (currentSeason.equals(sample)) {
            return;
        }

        setCurrentColorTypeSample(season);
        setCurrentColorTypeTitle(season);
        GradientDrawable currentTypeSample = (GradientDrawable) sample.getBackground();
        currentTypeSample.setStroke(10, getResources().getColor(R.color.highlight_sample));

        GradientDrawable previousTypeSample = (GradientDrawable) currentSeason.getBackground();
        previousTypeSample.setStroke(0, getResources().getColor(R.color.highlight_sample));

        currentSeason = sample;
    }

    private void setCurrentColorTypeSample(String season) {

        if (season == null) {
            throw new NullPointerException("season is null");
        }

        ArrayList<com.app.imagin.server.services.receivecolortype.response.Color> colors = null;
        for (ColorType colorType : colorTypes) {
            if (season.equals(colorType.type)) {
                colors = colorType.colors;
            }
        }

        if (colors == null) {
            throw new NullPointerException("colors is null");
        }

        setCurrentColorTypeSample(colors);
    }

    private void getResultColorType() {
        resultColorType = getIntent().getStringExtra("colorType");

        switch (resultColorType) {
            case "0":
                resultColorType = "Winter";
                currentSeason = findViewById(R.id.winter);
                break;
            case "1":
                resultColorType = "Autumn";
                currentSeason = findViewById(R.id.autumn);
                break;
            case "2":
                resultColorType = "Summer";
                currentSeason = findViewById(R.id.summer);
                break;
            case "3":
                resultColorType = "Spring";
                currentSeason = findViewById(R.id.spring);
                break;
            default:
                throw new IllegalStateException("Unexpected value: resultType = " + resultColorType);
        }

        GradientDrawable resultTypeSample = (GradientDrawable) currentSeason.getBackground();
        resultTypeSample.setStroke(10, getResources().getColor(R.color.highlight_sample));
    }

    private void getColorTypes() {
        ReceiveColorType service =
                ServiceGenerator.createService(ReceiveColorType.class, this, null);

        Call<ArrayList<ColorType>> call
                = service.receiveColorType();

        call.enqueue(new Callback<ArrayList<ColorType>>() {
            @Override
            public void onResponse(Call<ArrayList<ColorType>> call, Response<ArrayList<ColorType>> response) {
                colorTypes = response.body();

                if (colorTypes == null) {
                    throw new NullPointerException("Received colorTypes is null");
                }

                ArrayList<com.app.imagin.server.services.receivecolortype.response.Color> colors = null;
                for (ColorType colorType : colorTypes) {
                    if (resultColorType.toLowerCase().equals(colorType.type)) {
                        colors = colorType.colors;
                    }
                }

                if (colors == null) {
                    throw new NullPointerException("colors is null");
                }

                setCurrentColorTypeSample(colors);
                setSamples();

                Log.v("Receive color types", "success");
            }

            @Override
            public void onFailure(Call<ArrayList<ColorType>> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void setCurrentColorTypeTitle(String colorType) {
        colorTitle = findViewById(R.id.color_type);

        if (colorType == null) {
            throw new NullPointerException("LinearLayout for color samples is null");
        }

        String title = colorType.substring(0, 1).toUpperCase() + colorType.substring(1).toLowerCase();
        title += '\n' + getResources().getString(R.string.color_hint);
        SpannableString colorTitleText = new SpannableString(title);
        colorTitleText.setSpan(new RelativeSizeSpan(1.5f), 0, colorType.length(), 0);
        colorTitle.setText(colorTitleText);
    }

    private void setCurrentColorTypeSample(
            ArrayList<com.app.imagin.server.services.receivecolortype.response.Color> colors) {

        colorSamples = findViewById(R.id.color_samples);

        if (colorSamples == null) {
            throw new NullPointerException("TextView for color title is null");
        }

        for (int index = 0; index < colors.size() && index < colorSamples.getChildCount(); ++index) {
            View child = colorSamples.getChildAt(index);
            if (child instanceof ImageView) {
                String currentColor = colors.get(index).hexValues.get(0);

                ImageView sample = (ImageView) child;
                sample.getDrawable().mutate().setTint(Color.parseColor(currentColor));
            }
        }

        if (colors.size() < NUMBER_OF_SAMPLES) {
            View child = colorSamples.getChildAt(NUMBER_OF_SAMPLES - 1);
            if (child instanceof ImageView) {
                ImageView sample = (ImageView) child;
                sample.getDrawable().mutate().setTint(getResources().getColor(R.color.background));
            }
        }
    }

    private void setColorTypeSample(LinearLayout season,
                                          ArrayList<com.app.imagin.server.services.receivecolortype.response.Color>
                                   possibleColors) {

        for (int index = 0; index < possibleColors.size() && index < season.getChildCount(); ++index) {
            View child = season.getChildAt(index);
            if (child instanceof ImageView) {
                String currentColor = possibleColors.get(index).hexValues.get(0);

                ImageView sample = (ImageView) child;
                sample.getDrawable().mutate().setTint(Color.parseColor(currentColor));
            }
        }

    }

    private void setSamples() {
        for (ColorType colorType : colorTypes) {
            LinearLayout season;

            if (colorType.type == null) {
                throw new NullPointerException("Received colorType type is null");
            }

            switch (colorType.type) {
                case "summer":
                    season = summerSeason;
                    break;
                case "spring":
                    season = springSeason;
                    break;
                case "winter":
                    season = winterSeason;
                    break;
                case "autumn":
                    season = autumnSeason;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + colorType.type);
            }
            setColorTypeSample(season, colorType.colors);
        }
    }
}
