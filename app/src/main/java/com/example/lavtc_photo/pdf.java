package com.example.lavtc_photo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.w3c.dom.Text;


public class pdf extends AppCompatActivity {

    FloatingActionButton add_picture;
    MaterialButton save_pdf;
    androidx.appcompat.widget.Toolbar toolbar;
    pdf.filename file = new filename();
    CoordinatorLayout layout;

    ArrayList<String> images_paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        add_picture = findViewById(R.id.add_pdf_image);
        save_pdf = findViewById(R.id.save_pdf);
        save_pdf.setVisibility(View.VISIBLE);

        try{refresh();} catch (Exception ex){}
        file.setFilename(getIntent().getStringExtra("filename"));
        images_paths = new ArrayList<String>();
        images_paths.clear();

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Lavtc Photo App");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ClearDirectories clear = new ClearDirectories(pdf.this,getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");
            }
        });
        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pdf.this,camera.class);
                intent.putExtra("pdf",true);
                startActivity(intent);
            }
        });

        save_pdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                Snackbar.make(pdf.this,findViewById(R.id.ref_layer),"Saving pdf",Snackbar.LENGTH_SHORT).show();
                Toast.makeText(pdf.this, "Saving", Toast.LENGTH_SHORT).show();
                save_pdf.setVisibility(View.INVISIBLE);
                try {
                    SavePDF save = new SavePDF(pdf.this, getData(), file.getFilename(),false);

                }catch (Exception exception){
                    Toast.makeText(pdf.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(pdf.this,""+ images_paths.size()+"", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private ArrayList<Image> getData() throws Exception{
        ArrayList<Image> images = new ArrayList<>();
        File downloadsFolder = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");

        Image s;

        if (downloadsFolder.exists()){

            /*File[] files = downloadsFolder.listFiles();

            for (int i = 0;i<files.length;i++){
                File file = files[i];
                s = new Image();
                s.setFilename(file.getName());
                s.setUri(Uri.fromFile(file));
                images.add(s);
            }*/

            for (int i = 0;i<images_paths.size();i++){
                File file = new File(images_paths.get(i));
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
            View v = LayoutInflater.from(c).inflate(R.layout.recycleview_layout,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Image image = images.get(position);
            holder.filename.setText(image.getFilename());
            Glide.with(c).load(image.getUri()).into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        MaterialButton delete;
        TextView filename;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_holder);
            delete = itemView.findViewById(R.id.delete);
            filename = itemView.findViewById(R.id.uri);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteFile(filename.getText().toString());

                }
            });

        }


    }

    private void DeleteFile(String filename) {
        File file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf/"+filename);
        if (file.exists()){
            //Toast.makeText(this, "exist", Toast.LENGTH_SHORT).show();
            file.delete();
            images_paths.remove(images_paths.indexOf(file.getAbsolutePath()));
            //as();
            try{refresh();} catch (Exception ex){}
        }
        else {
            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, _savefile.getFilename(), Toast.LENGTH_SHORT).show();
       try {
            images_paths.add(getFilePath());
            Toast.makeText(this, getFilePath().toString(), Toast.LENGTH_SHORT).show();
           // as();
        }catch (Exception ex){}

        try{refresh();} catch (Exception ex){}
    }


    private void refresh() throws Exception{
        RecyclerView recyclerView = findViewById(R.id.view_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecycleView_Adapter(pdf.this, getData()));

        if(images_paths.size() > 0) {save_pdf.setVisibility(View.VISIBLE);}
        else {save_pdf.setVisibility(View.INVISIBLE);}

    }

    private void as(){
        Toast.makeText(this, images_paths.size(), Toast.LENGTH_SHORT).show();
    }

    public class  filename{
        private String filename;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClearDirectories clear = new ClearDirectories(pdf.this,getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");
        finish();
    }


    private String getFilePath() throws IOException {
        File  file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/path_image_pdf.txt");
        Scanner scanner  = new Scanner(file);
        file.delete();
        return scanner.nextLine();
    }
}

