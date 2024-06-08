package com.example.lavtc_photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import com.example.lavtc_photo.DataReference.*;


public class image_viewer extends AppCompatActivity{

    ImageView img_view ;
    TextInputLayout file_name_editText;
    FloatingActionButton save_btn,barcode_btn,change;
    Switch pdf_switch;
    Toolbar toolbar;
    CoordinatorLayout layout;

    boolean hide_menu_items = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_viewer);


        file_name_editText = findViewById(R.id.filename);
        save_btn = findViewById(R.id.save);
        barcode_btn = findViewById(R.id.barcode);
        change = findViewById(R.id.change);
        layout = findViewById(R.id.layout);
        toolbar = findViewById(R.id.toolbar);

        pdf_switch = findViewById(R.id.pdf_switch);

        pdf_switch.setVisibility(View.INVISIBLE);
        save_btn.setVisibility(View.INVISIBLE);


        if(DataReference.getData().process == DataReference.Data.Process.PDF){
            ClearDirectories.Clear(image_viewer.this,getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
            finish();
        }
        else {
            SetImage();
        }


        SetImage();

        file_name_editText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int text_length = file_name_editText.getEditText().getText().length();

                if(hide_menu_items) { hide_menu_items = false; ToggleVisibility();}

                if (text_length == 9){
                    hide_menu_items = true;
                    ToggleVisibility();

                }
                else if(text_length == 12){
                    hideKeyboard(true);
                    hide_menu_items = true;
                    ToggleVisibility();
                }

            }

            private void ToggleVisibility(){
                barcode_btn.setVisibility((barcode_btn.getVisibility()== View.VISIBLE)?View.INVISIBLE:View.VISIBLE);
                save_btn.setVisibility((save_btn.getVisibility() == View.VISIBLE)?View.INVISIBLE:View.VISIBLE);
                pdf_switch.setVisibility((pdf_switch.getVisibility() == View.VISIBLE)?View.INVISIBLE:View.VISIBLE);
            }
            private void hideKeyboard(boolean hide) {
                View view = image_viewer.this.getCurrentFocus();
                if(view == null)return;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hide) { imm.hideSoftInputFromWindow(view.getWindowToken(), 0); }
                else { imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT); }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pdf_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int text_length = file_name_editText.getEditText().getText().length();

                if(b && (text_length == 9 || text_length == 12)){
                   DataReference.getData().setFILE_NAME((file_name_editText.getEditText().getText().toString()));
                   Intent intent = new Intent(image_viewer.this,pdf.class);
                   startActivity(intent);
                   finish();
                }
                else {
                    pdf_switch.setChecked(false);
                }

                /*
                if (b && (text_length == 9 || text_length == 12)){
                    try {
                        saveImages saveImages = new saveImages(getFilePath(), file_name_editText.getEditText().getText().toString(), image_viewer.this, false);
                    }
                    catch (Exception ex){

                    }
                    DataReference.getData().setFILE_NAME(file_name_editText.getEditText().getText().toString());
                    Intent intent = new Intent(image_viewer.this, pdf.class);
                    intent.putExtra("filename",file_name_editText.getEditText().getText().toString());
                    startActivity(intent);
                }
                else {
                    pdf_switch.setChecked(false);
                    if (b) {
                        Snackbar.make(image_viewer.this, layout, "Please enter a filename", Snackbar.LENGTH_SHORT).show();
                    }
                }

                 */
            }
        });

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Lavtc Photo App");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDirectories.Clear(image_viewer.this,getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
               finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                    DataReference.getData().setFILE_NAME(file_name_editText.getEditText().getText().toString());
                    saveImages.save_Images(image_viewer.this);
                }catch (Exception ex){
                    Toast.makeText(image_viewer.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
                options.setPrompt("Scan a barcode");
                options.setCameraId(0);
                options.setBeepEnabled(false);
                options.setOrientationLocked(false);
                barcodeLauncher.launch(options);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataReference.getData().setImageType(DataReference.Data.Image_type.PROFILE);
                DataReference.getData().setFILE_NAME(file_name_editText.getEditText().getText().toString());
                Intent intent = new Intent(image_viewer.this,camera.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getFilePath() { return DataReference.getData().FILE_PATH;}

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Snackbar.make(image_viewer.this,layout,"Cancelled",Snackbar.LENGTH_SHORT).show();
                } else {
                    file_name_editText.getEditText().setText(result.getContents().toString());
                    Snackbar.make(image_viewer.this,layout,"Scanned",Snackbar.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onResume() {
        Toast.makeText(this,"Resume"+DataReference.getData().FILE_NAME,Toast.LENGTH_SHORT).show();
        super.onResume();
        SetImage();
        file_name_editText.getEditText().setText(DataReference.getData().FILE_NAME);
    }

    private void SetImage(){
        img_view = findViewById(R.id.image_view);
        try {
            Glide.with(this).load(BitmapDrawable.createFromPath(getFilePath())).into(img_view);
        }
        catch (Exception ex){}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClearDirectories.Clear(image_viewer.this,getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
        finish();
    }


}