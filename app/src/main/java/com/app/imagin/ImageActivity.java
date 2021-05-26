package com.app.imagin;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.imagin.applications.Imagin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.app.imagin.applications.Imagin.getContext;


public class ImageActivity extends Activity {

    private static final int IMAGE_CAPTURE_PERMISSION_CODE = 1;
    private static final int IMAGE_LOAD_PERMISSION_CODE = 2;
    private static final int IMAGE_CAPTURE_CODE = 1;
    private static final int IMAGE_LOAD_CODE = 2;

    private ImageView imageView;

    private Uri imageUri;

    private Button captureImageBtn;
    private Button loadImageBtn;
    private Button sendImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        imageView = findViewById(R.id.image_view);

        captureImageBtn = findViewById(R.id.capture_image_btn);
        loadImageBtn = findViewById(R.id.load_image_btn);
        sendImageBtn = findViewById(R.id.send_image_btn);

        sendImageBtn.setEnabled(false);
        sendImageBtn.setAlpha(0.1f);
        sendImageBtn.setPaintFlags(sendImageBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        captureImageBtn.setOnClickListener(click -> requestPermissionsAndOpenCamera());

        loadImageBtn.setOnClickListener(click -> requestPermissionsAndOpenStorage());

        sendImageBtn.setOnClickListener(click -> startSendImageActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case IMAGE_LOAD_CODE:
                    // TODO bitmap was used here, check if it is better
                    imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                    sendImageBtn.setAlpha(0.5f);
                    sendImageBtn.setEnabled(true);
                    break;

                case IMAGE_CAPTURE_CODE:
                    imageView.setImageURI(imageUri);
                    sendImageBtn.setAlpha(0.5f);
                    sendImageBtn.setEnabled(true);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == IMAGE_CAPTURE_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == IMAGE_LOAD_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openStorage();
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermissionsAndOpenCamera() {
        // if the android version is below Marshmallow (6.0) then permissions
        // are already granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ((checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED) ||
                    (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED)) {

                // if one of permissions was already granted then only needed one would be
                // requested by the android system
                String[] permission = {Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permission, IMAGE_CAPTURE_PERMISSION_CODE);

            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void requestPermissionsAndOpenStorage() {
        // if the android version is below Marshmallow (6.0) then permissions
        // are already granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED)) {

                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permission, IMAGE_LOAD_PERMISSION_CODE);

            } else {
                openStorage();
            }
        } else {
            openStorage();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image for Imagin app");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");

        imageUri = getContentResolver().
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void openStorage() {
        // TODO is the second option more general? or are they equal?
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, IMAGE_LOAD_CODE);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, IMAGE_LOAD_CODE);
    }

    private void startSendImageActivity() {
        Intent sendImageActivityScreen = new Intent(getContext(), SendImageActivity.class);

        sendImageActivityScreen.putExtra("imageUri", imageUri.toString());

        startActivity(sendImageActivityScreen);
    }
}
