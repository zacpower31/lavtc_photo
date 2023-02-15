package com.example.lavtc_photo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class pdf_save_dialogbox extends Dialog implements View.OnClickListener {


    Activity c;
    String fileName;
    public Dialog d;
    public MaterialButton save;
    public TextInputLayout filename;
    public ArrayList<Image> images;
    FloatingActionButton back;
    public pdf_save_dialogbox(Activity activity,ArrayList<Image> images) {
        super(activity);
        this.c = activity;
        this.images = images;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_save_dialogbox);

        save = findViewById(R.id.save);
        filename = findViewById(R.id.filename);
        back = findViewById(R.id.back);

        save.setOnClickListener(this);
        back.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                try {
                    SavePDF save = new SavePDF(c, images, filename.getEditText().getText().toString(),true);
                } catch (Exception ex) {
                }
                break;

            case R.id.back:
                hide();
                break;

        }
    }
}