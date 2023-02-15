package com.example.lavtc_photo;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.journeyapps.barcodescanner.Util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class saveImages {
    Activity activity;
    boolean jpg_instance_finalize,photo_instance_finalize;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public saveImages(String file_path,String filename,Activity c,boolean pdf) throws  IOException{
        this.activity = c;

         File source = new File(file_path);
         saveImageInPhoto(source,filename,activity,pdf);
         saveImageInLavtc(source,filename,activity,pdf);

         jpg_instance_finalize =false;
         photo_instance_finalize = false;


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void saveImageInLavtc(File source, String filename ,Activity c,boolean pdf) throws IOException {


        File target = new File(Environment.getExternalStorageDirectory().toString()+ "/id/JPG/"+filename+".jpg");
        InputStream  inputStream = null;
        OutputStream outputStream = null;
        if (source.exists()) {
            try {
                inputStream = new FileInputStream(source);
                outputStream = new FileOutputStream(target);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } finally {
                inputStream.close();
                outputStream.close();
                jpg_instance_finalize =true;
            }

        }

        finilize(c,pdf);
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void saveImageInPhoto(File source, String filename,Activity c,boolean pdf) throws IOException {
        File target = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+filename+".jpg");
        InputStream  inputStream = null;
        OutputStream outputStream = null;
        if (source.exists()) {
            try {
              inputStream = new FileInputStream(source);
                outputStream = new FileOutputStream(target);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } finally {
                inputStream.close();
                outputStream.close();
                photo_instance_finalize =true;
            }

        }

        finilize(c,pdf);
    }


    private void finilize(Activity c,boolean pdf) {
        if (photo_instance_finalize&&jpg_instance_finalize&&!pdf){
            ClearDirectories clear = new ClearDirectories(c,c.getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
            c.finish();
        }
        else {

        }
    }
}
