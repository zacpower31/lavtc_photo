package com.example.lavtc_photo;

import android.app.Activity;

import java.io.File;

public class ClearDirectories {

    public ClearDirectories(Activity c , String filepath){

            File fileList =new File(filepath);
            if (fileList != null){
                File[] filenames = fileList.listFiles();
                for (File temp:filenames){
                    temp.delete();
                }
            }
        }
}

