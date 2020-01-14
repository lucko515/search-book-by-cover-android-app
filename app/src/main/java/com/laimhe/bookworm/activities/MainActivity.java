package com.laimhe.bookworm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import com.laimhe.bookworm.R;
import com.laimhe.bookworm.activities.ImageCropActivity;
import com.laimhe.bookworm.helpers.MyConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.laimhe.bookworm.helpers.ImageUtils.handleSamplingAndRotationBitmap;


public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 1;

    private String pictureImagePath;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        findViewById(R.id.scanImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Start the Camera activity on the Scan button click
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                outputFileUri = Uri.fromFile(file);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            File imgFile = new  File(pictureImagePath);

            //If we made a image with the camera, convert it to Bitmap and send it to the ImageCropActivity
            if(imgFile.exists()){
                Bitmap photo = null;

                //Make sure that the image is always in the portrait mode
                try {
                    photo = handleSamplingAndRotationBitmap(getApplicationContext(), outputFileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MyConstants.selectedImageBitmap = photo;
                startActivity(new Intent(getApplicationContext(), ImageCropActivity.class));
            }
        }
    }
}
