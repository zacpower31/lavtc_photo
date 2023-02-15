package com.example.lavtc_photo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;

import com.google.zxing.aztec.decoder.Decoder;
import com.google.zxing.qrcode.QRCodeReader;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class image_viewer extends AppCompatActivity implements View.OnClickListener {

    ImageView img_view ;
    TextInputLayout file_name_editText;
    FloatingActionButton save_btn,barcode_btn,change;
    Switch pdf_switch;
    Toolbar toolbar;
    CoordinatorLayout layout;
    boolean filename_bool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_viewer);


        file_name_editText = findViewById(R.id.filename);
        save_btn = findViewById(R.id.save);
        barcode_btn = findViewById(R.id.barcode);
        change = findViewById(R.id.change);
        layout = findViewById(R.id.layout);
        toolbar = findViewById(R.id.toolbar);

        barcode_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        change.setOnClickListener(this);

        pdf_switch = findViewById(R.id.pdf_switch);


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        SetImage();

        file_name_editText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int text_length = file_name_editText.getEditText().getText().length();
                if (text_length == 9 || text_length == 12){
                    filename_bool = true;
                    barcode_btn.setVisibility(View.INVISIBLE);
                    save_btn.setVisibility(View.VISIBLE);
                }
                else {
                    filename_bool = false;
                    barcode_btn.setVisibility(View.VISIBLE);
                    save_btn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pdf_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && filename_bool){
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
            }
        });

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Lavtc Photo App");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDirectories clear = new ClearDirectories(image_viewer.this,getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
                finish();
            }
        });
    }




    private String getFilePath() throws IOException {
        File  file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/path_image.txt");
        Scanner scanner  = new Scanner(file);
        return scanner.nextLine();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view)  {
        switch (view.getId()){
            case R.id.save:
                try {
                    saveImages saveImages = new saveImages(getFilePath(),file_name_editText.getEditText().getText().toString(),this,false);
                }catch (Exception ex){}
                break;

            case R.id.barcode:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
                break;

            case R.id.change:
                Intent intent = new Intent(image_viewer.this,camera.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                file_name_editText.getEditText().setText(intentResult.getContents().toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetImage();
    }

    private void SetImage(){
        img_view = findViewById(R.id.image_view);
        try {
            img_view.setImageBitmap(BitmapFactory.decodeFile(getFilePath()));

            }catch (Exception ex){}
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClearDirectories clear = new ClearDirectories(image_viewer.this,getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
        finish();
    }


}