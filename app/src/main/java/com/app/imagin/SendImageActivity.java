package com.app.imagin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.imagin.server.services.login.request.LoginRequest;
import com.app.imagin.server.services.login.response.LoginResponse;
import com.app.imagin.server.services.sendimage.SendImageServiceHolder;
import com.app.imagin.server.services.sendimage.TokenAuthenticator;
import com.app.imagin.server.services.sendimage.request.SendImageRequest;
import com.app.imagin.server.services.sendimage.response.SendImageResponse;
import com.app.imagin.server.ServiceGenerator;
import com.app.imagin.server.services.login.LoginService;
import com.app.imagin.server.services.sendimage.SendImageServiceBase64;
import com.app.imagin.server.SessionManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.app.imagin.applications.Imagin.getContext;


public class SendImageActivity extends Activity {

    private ImageView imageView;
    private Uri imageUri;

    private Button retryBtn;
    private Button changeImageBtn;
    private Button resultBtn;
    private ProgressBar loadingImageSpinner;

    private SessionManager sessionManager;

    private String colorType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_image_activity);

        String imageUriStr = getIntent().getStringExtra("imageUri");
        imageUri = Uri.parse(imageUriStr);

        sessionManager = new SessionManager(this);
        imageView = findViewById(R.id.image_view);

        changeImageBtn = findViewById(R.id.change_image_btn);
        changeImageBtn.setPaintFlags(changeImageBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        resultBtn = findViewById(R.id.result_btn);

        changeImageBtn.setOnClickListener(click -> finish());

        resultBtn.setEnabled(false);
        resultBtn.setOnClickListener(click -> startResultActivity());

        loadingImageSpinner = findViewById(R.id.loading_image_spinner);
        loadingImageSpinner.setVisibility(View.VISIBLE);

        //login();
        sendImageBase64();
        //sendImage();
    }

/// REQUESTS ///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void login() {
        LoginService service = ServiceGenerator.createService(LoginService.class, this, null);

        LoginRequest request = new LoginRequest();

        Call<LoginResponse> call = service.login(request);
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                int statusCode = loginResponse.getStatusCode();

                if (statusCode == 200 && loginResponse.getUser() != null) {
                    sessionManager.setAuthToken(loginResponse.getAuthToken());

                } else if (statusCode != 200) {
                    Log.v("Login failed:", "status code " + statusCode);
                } else {
                    Log.v("Login failed:", "user data is null");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Login error:", t.getMessage());
            }
        });
    }

    private void sendImageBase64() {
        SendImageServiceBase64 service =
                ServiceGenerator.createService(SendImageServiceBase64.class, this, null);

        String image = convertImageToBase64();

        if (image == null) {
            return; // TODO file not converted handling
        }

        Call<SendImageResponse> call
                = service.sendImage(new SendImageRequest(image));

        call.enqueue(new Callback<SendImageResponse>() {

            @Override
            public void onResponse(Call<SendImageResponse> call, Response<SendImageResponse> response) {
                SendImageResponse sendImageResponse = response.body();
                colorType = sendImageResponse.getMetadata().colorType;

                if (sendImageResponse.getMetadata().photoQuality == 0) {
                    Toast.makeText(getContext(), "Low photo quality, please make another one",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    writeImageToDisk(sendImageResponse.getImage());
                }

                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<SendImageResponse> call, Throwable t) {
                // Network connection issue
                if (t instanceof IOException) {
                    Toast.makeText(SendImageActivity.this,
                            R.string.network_failure,
                            Toast.LENGTH_LONG).show();

                    // Conversion error: mismatch of the response payload and app Java models
                } else {
                    Toast.makeText(SendImageActivity.this,
                            R.string.conversion_error,
                            Toast.LENGTH_LONG).show();

                    // TODO important issue, should log in some bug tracking service
                }
                Log.e("Upload error:", t.getMessage());
                sendImageFailure();
            }
        });
    }

    private void sendImage() {
        SendImageServiceHolder serviceHolder = new SendImageServiceHolder();
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(getContext(), serviceHolder);

        SendImageServiceBase64 service
                = ServiceGenerator.createService(SendImageServiceBase64.class, getContext(), tokenAuthenticator);

        serviceHolder.set(service);


        String image = convertImageToBase64();

        if (image == null) {
            return; // TODO file not converted handling
        }

        Call<SendImageResponse> call
                = service.sendImage(new SendImageRequest(image));

        call.enqueue(new Callback<SendImageResponse>() {
            @Override
            public void onResponse(Call<SendImageResponse> call, Response<SendImageResponse> response) {
                SendImageResponse sendImageResponse = response.body();

                if (sendImageResponse == null) {
                    return;
                }

                colorType = sendImageResponse.getMetadata().colorType;
                writeImageToDisk(sendImageResponse.getImage());

                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<SendImageResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

    public void startResultActivity() {
        Intent resultActivityScreen = new Intent(getContext(), ResultActivity.class);

        if (colorType == null) {
            throw new NullPointerException("colorType is still null");
        }
        resultActivityScreen.putExtra("colorType", colorType);

        startActivity(resultActivityScreen);
    }

    private void sendImageFailure() {
        setContentView(R.layout.send_image_activity_failure);

        imageView = findViewById(R.id.image_view);

        retryBtn = findViewById(R.id.retry_btn);
        changeImageBtn = findViewById(R.id.change_image_btn);
        changeImageBtn.setPaintFlags(changeImageBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resultBtn = findViewById(R.id.result_btn);

        retryBtn.setOnClickListener(click -> retrySendImage());
        changeImageBtn.setOnClickListener(click -> finish());
        resultBtn.setOnClickListener(click -> startResultActivity());
    }

    private void retrySendImage() {
        setContentView(R.layout.send_image_activity);

        imageView = findViewById(R.id.image_view);

        changeImageBtn = findViewById(R.id.change_image_btn);
        changeImageBtn.setPaintFlags(changeImageBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resultBtn = findViewById(R.id.result_btn);

        changeImageBtn.setOnClickListener(click -> finish());
        resultBtn.setOnClickListener(click -> startResultActivity());

        loadingImageSpinner = findViewById(R.id.loading_image_spinner);
        loadingImageSpinner.setVisibility(View.VISIBLE);
        sendImageBase64();
    }

    private String convertImageToBase64() {
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;

        try (InputStream is = getContentResolver().openInputStream(imageUri);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            bytes = os.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (FileNotFoundException e) {
            Log.e("File not found error", e.getMessage());

            // TODO when sending the same file - change to proper handling
            Toast.makeText(getContext(), "Low photo quality, please make another one",
                    Toast.LENGTH_LONG).show();
            finish();
        } catch (IOException e) {
            Log.e("File read error", e.getMessage());
        }

        return null;
    }

    private void writeImageToDisk(String image) {

        String pathname = getExternalFilesDir(null) +
                File.separator + "result_image.png";

        File file = new File(pathname);

        Log.d(TAG, pathname);

        try (InputStream is = new ByteArrayInputStream(Base64.decode(image, Base64.DEFAULT));
             OutputStream os = new FileOutputStream(file)) {

            byte[] fileReader = new byte[4096];

            while (true) {
                int read = is.read(fileReader);

                if (read == -1) {
                    break;
                }

                os.write(fileReader, 0, read);
            }
            os.flush();

        } catch (IOException e) {
            Log.e("Write image error:", e.getMessage());
        } finally {
            imageUri = Uri.fromFile(file);
            imageView.setImageURI(imageUri);
            loadingImageSpinner.setVisibility(View.GONE);
            resultBtn.setEnabled(true);
        }
    }
}
