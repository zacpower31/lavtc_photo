package com.example.lavtc_photo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Drive_Sync extends Dialog implements View.OnClickListener {

    Activity activity;
    MaterialButton download;
    FloatingActionButton back;

    public Drive_Sync(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_sync);
        download = findViewById(R.id.download);
        back = findViewById(R.id.back);

        download.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.download:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.synology.dsdrive"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
                break;

            case R.id.back:
               hide();
        }
    }
}