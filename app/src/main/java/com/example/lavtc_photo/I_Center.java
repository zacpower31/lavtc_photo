package com.example.lavtc_photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class I_Center extends AppCompatActivity {

    FloatingActionButton add_picture,save_pdf;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        add_picture = findViewById(R.id.add_pdf_image);
        save_pdf = findViewById(R.id.save_pdf);


        refresh();

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(I_Center.this,camera.class);
                intent.putExtra("pdf",true);
                startActivity(intent);
            }
        });

        save_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdf_save_dialogbox dialogbox = new pdf_save_dialogbox(I_Center.this,getData());
                dialogbox.show();
            }
        });


        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Lavtc Photo App");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDirectories clear = new ClearDirectories(I_Center.this,getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");
                finish();
            }
        });

    }

    private ArrayList<Image> getData() {
        ArrayList<Image> images = new ArrayList<>();
        File downloadsFolder = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");

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


    public class RecycleView_Adapter extends RecyclerView.Adapter<My_ViewHolder>{

        Context c;
        ArrayList<Image> images;

        public   RecycleView_Adapter(Context c,ArrayList<Image> images){
            this.c = c;
            this.images = images;

        }
        @NonNull
        @Override
        public My_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(c).inflate(R.layout.recycleview_layout,parent,false);
            return new My_ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull My_ViewHolder holder, int position) {
            Image image = images.get(position);
            holder.filePath.setText(image.getFilename());
            Glide.with(c).load(image.getUri()).into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }

    private class My_ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        MaterialButton delete;
        TextView filePath;
        public My_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_holder);
            delete = itemView.findViewById(R.id.delete);
            filePath = itemView.findViewById(R.id.uri);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteFile(filePath.getText().toString());
                }
            });



        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    private void refresh(){
        RecyclerView recyclerView = findViewById(R.id.view_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecycleView_Adapter(I_Center.this,getData()));

    }

    private void DeleteFile(String filename) {
        CoordinatorLayout layout = findViewById(R.id.layout);
        File file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf/"+filename);
        if (file.exists()){
            Snackbar.make(I_Center.this, layout, "File Deleted", Snackbar.LENGTH_SHORT).show();
            file.delete();
            refresh();
        }
        else {
            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
        }
    }

}
