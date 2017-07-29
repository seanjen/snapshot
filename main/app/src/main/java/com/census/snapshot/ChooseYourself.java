package com.census.snapshot;

import android.view.Window;
import com.beyondar.android.fragment.BeyondarFragmentSupport;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.screenshot.OnScreenshotListener;
import com.beyondar.image.ImageDialog;

public class ChooseYourself extends FragmentActivity implements View.OnClickListener, OnScreenshotListener {

    private BeyondarFragmentSupport mbeyondarFragment;
    private Button mTakePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.choose_yourself);

        mbeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);

        mTakePicture = (Button) findViewById(R.id.takeSelfie);
        mTakePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTakePicture) {
            mbeyondarFragment.takeScreenshot(this);
        }
    }

    @Override
    public void onScreenshot(Bitmap screenshot) {
        ImageDialog dialog = new ImageDialog();
        dialog.setImage(screenshot);

        dialog.show(getSupportFragmentManager(), "ImageDialog");
    }
}
