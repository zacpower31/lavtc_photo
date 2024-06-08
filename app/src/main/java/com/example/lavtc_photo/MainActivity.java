package com.example.lavtc_photo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import  com.example.lavtc_photo.DataReference.*;
public class MainActivity extends AppCompatActivity  {

    FloatingActionButton camera;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    View header;
    ImageView imageView;

    String[]pending_permissions;

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                CheckDIRStatus();
                Data data = new Data();
                data.setProcess(Data.Process.MAIN_MENU);
                data.setImageType(Data.Image_type.PROFILE);
                DataReference.setData(data);
                Intent intent = new Intent(MainActivity.this, com.example.lavtc_photo.camera.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)  {
                Intent intent ;
                CheckDIRStatus();
                switch (menuItem.getItemId()){
                    case R.id.nav_JPGGallery:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this,Gallery.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_driveSync:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        try {
                            Intent i = getPackageManager().getLaunchIntentForPackage("com.synology.dsdrive");
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
        String MANIFEST_CAMERA = Manifest.permission.CAMERA;
        String MANIFEST_WRITE_DATA = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String MANIFEST_READ_DATA = Manifest.permission.READ_EXTERNAL_STORAGE;
        String MANIFEST_MANAGE_DATA = Manifest.permission.MANAGE_EXTERNAL_STORAGE;

        if(Build.VERSION.SDK_INT >= 30){
            pending_permissions = new String[]{MANIFEST_CAMERA, MANIFEST_WRITE_DATA, MANIFEST_MANAGE_DATA};
        }
        else {
            pending_permissions = new String[]{MANIFEST_CAMERA, MANIFEST_READ_DATA, MANIFEST_WRITE_DATA};
        }
        requestPermissionLauncher.launch(pending_permissions);

    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted ->{});

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CheckDIRStatus(){
        check("/id/");
        check("/id/JPG");
        check("/id/PDF");
        check("/I-Center/");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void check(String path){
        String ROOT = Environment.getExternalStorageDirectory().toString();
        File temp = new File(ROOT+path);
        if(!temp.exists()){
            temp.mkdir();
        }

    }

}


