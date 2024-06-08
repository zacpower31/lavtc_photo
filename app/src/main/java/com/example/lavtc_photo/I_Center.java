package com.example.lavtc_photo;

import static com.example.lavtc_photo.DataReference.Data;
import static com.example.lavtc_photo.DataReference.getData;
import static com.example.lavtc_photo.DataReference.setData;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class I_Center extends AppCompatActivity {

    FloatingActionButton add_picture;
    MaterialButton save_pdf;
    androidx.appcompat.widget.Toolbar toolbar;
    Image temp_image_file;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        add_picture = findViewById(R.id.add_pdf_image);
        save_pdf = findViewById(R.id.save_pdf);

        Data data = new Data();
        setData(data);

        getData().PDF_Paths_List.clear();
        reloadView();

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData().setImageType(Data.Image_type.PDF);
                Intent intent = new Intent(I_Center.this,camera.class);
                startActivity(intent);
            }
        });

        save_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdf_save_dialogbox dialogbox = new pdf_save_dialogbox(I_Center.this,getViewList());
                dialogbox.show();
            }
        });


        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Lavtc Photo App");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDirectories.Clear(I_Center.this,getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");
                finish();
            }
        });

    }

    private ArrayList<Image> getViewList() {
        ArrayList<Image> temp_list = new ArrayList<>();
        for (Map.Entry<String,String> data: getData().PDF_Paths_List.entrySet()) {
            File temp = new File(data.getValue());
            temp_image_file = new Image();
            temp_image_file.setFilename(temp.getName());
            temp_image_file.setIndex(data.getKey());
            temp_image_file.setUri(Uri.fromFile(temp));
            temp_list.add(temp_image_file);
        }

        return  (!temp_list.isEmpty())?temp_list:new ArrayList<>();
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
            holder.index.setText(image.getIndex());
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
        TextView index;
        public My_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_holder);
            delete = itemView.findViewById(R.id.delete);
            index = itemView.findViewById(R.id.uri);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteFile(index.getText().toString());
                }
            });



        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadView();
    }


    private void reloadView(){
        if (!getData().PDF_Paths_List.isEmpty()) { save_pdf.setVisibility(View.VISIBLE); }
        else { save_pdf.setVisibility(View.INVISIBLE); }

        if(getData().PDF_PATH != null) { getData().setPDF_Path(getData().PDF_PATH);}
        if(getData().PDF_Paths_List.isEmpty()) { return; }
        getData().setPDF_PATH(null);

        RecyclerView recyclerView = findViewById(R.id.view_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecycleView_Adapter(I_Center.this,getViewList()));

    }

    private void DeleteFile(String index) {
        CoordinatorLayout layout = findViewById(R.id.layout);
        File file = new File(getData().PDF_Paths_List.get(index));

        if(file.exists()){
            file.delete();
            getData().PDF_Paths_List.remove(index);
            reloadView();
        }
    }

}
