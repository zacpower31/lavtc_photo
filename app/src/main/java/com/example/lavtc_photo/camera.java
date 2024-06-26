package com.example.lavtc_photo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Size;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.xml.transform.Transformer;

public class camera extends AppCompatActivity {

     FloatingActionButton bTakePicture;
    PreviewView previewView;

    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    CoordinatorLayout layout;
    Camera camera;
    CameraControl cControl;
    CameraInfo cInfo;

    SeekBar zoom;



    pdf pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        bTakePicture = findViewById(R.id.take_picture);
        previewView = findViewById(R.id.camera_view);
        layout = findViewById(R.id.layer);
        zoom = findViewById(R.id.zoomBar);

        bTakePicture.setVisibility(View.VISIBLE);
        zoom.setVisibility(View.VISIBLE);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, getExecutor());
        zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float mat = (float) (i) / (100);
                cControl.setLinearZoom(mat);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        bTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoom.setVisibility(View.INVISIBLE);
                bTakePicture.setVisibility(View.INVISIBLE);
                capturePhoto();
            }
        });
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder()
                .build();


        preview.setSurfaceProvider(previewView.getSurfaceProvider());



        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();


         camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
         cControl = camera.getCameraControl();
         cInfo = camera.getCameraInfo();


    }

    private void capturePhoto() {
        Snackbar.make(camera.this, findViewById(R.id.layer),"Processing....",Snackbar.LENGTH_SHORT).show();
        boolean pdf_bool = getIntent().getBooleanExtra("pdf",false);
        DataReference.Data.Image_type type = DataReference.getData().type;;

        File directory;

        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (type == DataReference.Data.Image_type.PROFILE) {
           directory = getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL);
        }
        else {
            directory = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()+"/pdf");
        }
        File image = new File(directory,filename+".jpg");
        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(image).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                if (type == DataReference.Data.Image_type.PROFILE) {
                    try {
                        DataReference.getData().setFILE_PATH(image.getAbsolutePath());
                        Intent intent = new Intent(camera.this, image_viewer.class);
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception ex){}

                }
                else {
                   // Intent message = new Intent("PDF_SAVE");
                   // message.putExtra("path",image.getAbsolutePath());
                    //sendBroadcast(message);
                    try {
                        DataReference.getData().setPDF_PATH(image.getAbsolutePath());
                        finish();
                    }catch (Exception ex) {
                        Toast.makeText(pdf, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });

    }

    private void saveImageLocation(String path,boolean pdf) throws IOException {
        File file;
        if(!pdf){
            file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/path_image.txt");
        }
        else {
            file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/path_image_pdf.txt");
        }
        FileWriter writer = new FileWriter(file.getAbsolutePath());
        writer.write(path);
        writer.close();
    }



}