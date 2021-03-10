package com.example.fratnav.houses;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.fratnav.R;
import com.example.fratnav.houses.HousePage;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HouseTourActivity extends AppCompatActivity {

    private VrPanoramaView mVRPanoramaView;
    public VrPanoramaView.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //connects XML
        setContentView(R.layout.activity_house_tour);

        //gets the Panorama View
        mVRPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);

        // gets the url for the panorama view from s3
        String url = getIntent().getStringExtra(HousePage.URL_KEY);

        //runs a thread to start and display the panorama view
        new Thread() {
            @Override
            public void run() {
                Bitmap bp = getBitmapFromURL(url);
                options = new VrPanoramaView.Options();
                options.inputType = VrPanoramaView.Options.TYPE_MONO;
                options = new VrPanoramaView.Options();
                options.inputType = VrPanoramaView.Options.TYPE_MONO;
                mVRPanoramaView.loadImageFromBitmap(bp, options);
            }
        }.start();






    }

    public static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVRPanoramaView.resumeRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRPanoramaView.pauseRendering();
    }

    @Override
    protected void onDestroy() {
        mVRPanoramaView.shutdown();
        super.onDestroy();
    }
}