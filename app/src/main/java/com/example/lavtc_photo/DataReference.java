package com.example.lavtc_photo;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class DataReference {

    public static Data getData() {
        return data;
    }

    public static void setData(Data data) {
        DataReference.data = data;
    }

    static Data data;

    public static class  Data{

        public String FILE_NAME;
        public String FILE_PATH;
        public  String PDF_PATH;
        public enum  Process {MAIN_MENU,PDF,ICENTER}
        public enum Image_type{PROFILE,PDF}
        public Process process;
        public Image_type type;
        public HashMap<String,String> PDF_Paths_List = new HashMap<String,String>();

        public void setFILE_NAME(String FILE_NAME) {
            this.FILE_NAME = FILE_NAME;
        }
        public void setFILE_PATH(String FILE_PATH) {
            this.FILE_PATH = FILE_PATH;
        }
        public void setPDF_PATH(String PDF_PATH) {
            this.PDF_PATH = PDF_PATH;
        }
        public void setProcess(Process process) { this.process = process; }
        public void setImageType(Image_type imageType) {
            this.type = imageType;
        }
        public void setPDF_Path( String PDF_Path) { this.PDF_Paths_List.put(String.valueOf(new Random().nextInt(1000)),PDF_Path); }

    }
}
