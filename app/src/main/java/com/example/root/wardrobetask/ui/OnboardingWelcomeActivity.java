package com.example.root.wardrobetask.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.root.wardrobetask.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnboardingWelcomeActivity extends AppCompatActivity
{
    final int REQUEST_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_welcome);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
    }

    @OnClick(R.id.next_fab)
    void startOnboardingShirtsActivity()
    {
        startActivity(new Intent(OnboardingWelcomeActivity.this, OnboardingAddShirtsActivity.class));
        finish();
    }


    public void checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(OnboardingWelcomeActivity.this, Manifest.permission.MANAGE_DOCUMENTS) + ContextCompat.checkSelfPermission(OnboardingWelcomeActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(OnboardingWelcomeActivity.this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(OnboardingWelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))

            {
                Toast.makeText(this, "Please Grant Permissions", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(OnboardingWelcomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        REQUEST_PERMISSIONS);
            } else
            {
                ActivityCompat.requestPermissions(OnboardingWelcomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        REQUEST_PERMISSIONS);
            }
        }
        else
        {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_PERMISSIONS:
            {
                Toast.makeText(this, "We won't misuse them - promise!", Toast.LENGTH_LONG).show();

            }
        }
    }
}
