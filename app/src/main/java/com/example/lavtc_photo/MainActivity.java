package com.example.lavtc_photo;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity  {

    FloatingActionButton camera;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    View header;
    ImageView imageView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.camera_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        checkForPermissions();
        setNavigationView();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDirectories();
                Intent intent = new Intent(MainActivity.this, com.example.lavtc_photo.camera.class);
                intent.putExtra("pdf",false);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)  {
                Intent intent ;
                makeDirectories();
                switch (menuItem.getItemId()){
                    case R.id.nav_JPGGallery:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this,Gallery.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_driveSync:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        try {
                            Intent i = getPackageManager().getLaunchIntentForPackage("com.ttxapps.drivesync");
                            startActivity(i);


                        } catch (NullPointerException ex) {
                            Drive_Sync drive_sync = new Drive_Sync(MainActivity.this);
                            drive_sync.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            drive_sync.show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;

                    case R.id.i_center_pdf:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this,I_Center.class);
                        startActivity(intent);
                        break;

                }

                return true;
            }
        });

    }

    private void setNavigationView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById( R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);

        imageView = header.findViewById(R.id.imageView);

        setSupportActionBar(toolbar);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





    }

    private void checkForPermissions() {
        //make directory



        String manifest_camera = Manifest.permission.CAMERA;
        String manifest_write_data = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String manifest_read_data = Manifest.permission.READ_EXTERNAL_STORAGE;


        String[]permissions = {manifest_camera,manifest_read_data,manifest_write_data};
        ActivityCompat.requestPermissions(MainActivity.this,permissions,1);



    }

    private void makeDirectories(){
        String sdCard = Environment.getExternalStorageDirectory().toString();
        final File file = new File(sdCard + "/id/");
        if (!file.mkdir()) {
            file.mkdir();
        }

        File jpg_file = new File(sdCard + "/id/JPG");
        if (!jpg_file.mkdir()) {
            jpg_file.mkdir();
        }

        File pdf_file = new File(sdCard + "/id/PDF");
        if (!pdf_file.mkdir()) {
            pdf_file.mkdir();
        }

        File file2 = new File(sdCard + "/I-Center/");
        if (!file2.mkdir()) {
            file2.mkdir();
        }
    }
}


