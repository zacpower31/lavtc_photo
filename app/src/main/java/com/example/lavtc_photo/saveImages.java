package com.example.lavtc_photo;

import  static  com.example.lavtc_photo.DataReference.*;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class saveImages {
    Activity activity;

    public static void save_Images(Activity c) throws Exception{
        String ROOT = Environment.getExternalStorageDirectory().toString();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File temp_dir = new File(ROOT+ "/id/JPG/"+date);

        if(!temp_dir.mkdir()){ temp_dir.mkdir();}

        File source = new File(getData().FILE_PATH);
        if(!source.exists()) return;
        File target = new File(ROOT+ "/id/JPG/"+date+"/"+getData().FILE_NAME+".jpg");

        InputStream  inputStream = null;
        OutputStream outputStream = null;

        inputStream = new FileInputStream(source);
        outputStream = new FileOutputStream(target);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();

        exit(c);

    }
    private static  void exit(Activity c){
        ClearDirectories.Clear(c,c.getExternalFilesDir(Environment.MEDIA_BAD_REMOVAL).getAbsolutePath());
        c.finish();
    }

}
