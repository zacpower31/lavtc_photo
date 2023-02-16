package com.example.lavtc_photo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    View header;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setNavigationView();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.nav_MainActivity:
                        finish();
                        break;
                    case R.id.i_center_pdf:
                        intent = new Intent(Gallery.this,I_Center.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_driveSync:
                        try {
                            Intent i = getPackageManager().getLaunchIntentForPackage("com.ttxapps.drivesync");
                            startActivity(i);
                            finish();

                        } catch (NullPointerException ex) {
                            Drive_Sync drive_sync = new Drive_Sync(Gallery.this);
                            drive_sync.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            drive_sync.show();
                        }
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        RecyclerView recyclerView = findViewById(R.id.view_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecycleView_Adapter(Gallery.this,getData()));
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


    private ArrayList<Image> getData() {
        ArrayList<Image> images = new ArrayList<>();
        File downloadsFolder = new File(Environment.getExternalStorageDirectory().toString()+"/id/JPG");

        Image s;

        if (downloadsFolder.exists()){

            File[] files = downloadsFolder.listFiles();

            for (int i = 0;i<files.length;i++){
                File file = files[i];
                s = new Image();
                s.setFilename(file.getName());
                s.setUri(Uri.fromFile(file));
                images.add(s);
            }

        }
        return images;
    }


    public class RecycleView_Adapter extends RecyclerView.Adapter<MyViewHolder>{


        Context c;
        ArrayList<Image> images;

        public   RecycleView_Adapter(Context c,ArrayList<Image> images){
            this.c = c;
            this.images = images;

        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(c).inflate(R.layout.galley_frame,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Image image = images.get(position);
            holder.filePath.setText(image.getFilename());
            Glide.with(c).load(image.getUri()).into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        MaterialButton change;
        TextView filePath;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_holder);
            filePath = itemView.findViewById(R.id.uri);


        }


    }
}