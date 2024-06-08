package com.example.lavtc_photo;

import static com.example.lavtc_photo.DataReference.Data.Process.*;
import static  com.example.lavtc_photo.DataReference.*;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.camera.core.processing.SurfaceProcessorNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.lavtc_photo.DataReference.Data.Process;
public class SavePDF {

    public static void save_pdf(Activity c,DataReference.Data.Process process,ArrayList<Image> list) throws  Exception{
        Log.d("CUSTOM",""+process+"");

        String ROOT = Environment.getExternalStorageDirectory().toString();
        String date  = new SimpleDateFormat("yyyyMMdd").format(new Date());
        PdfDocument document = new PdfDocument();
        File temp_dir = new File(ROOT+ "/id/PDF/"+date);
        if(!temp_dir.mkdir()){ temp_dir.mkdir();}

        File target = new File( (process == ICENTER)? ROOT+"/I-Center/" + getData().FILE_NAME + ".pdf":
                                (process == PDF)? ROOT+"/id/PDF/"+date+"/"+ getData().FILE_NAME + ".pdf": "");

        FileOutputStream fos = new FileOutputStream(target);

        for (int n = 0 ; n < list.size() ; n++){
            Bitmap bitmap = BitmapFactory.decodeFile(list.get(n).getUri().getPath());
            Bitmap resized_bitmap = Bitmap.createScaledBitmap(bitmap,793,1122,true);
            // Bitmap rotated_bitmap = rotateBitmap(resized_bitmap,images.get(n).getUri());
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(resized_bitmap.getWidth(), resized_bitmap.getHeight(), (n + 1)).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            canvas.drawBitmap(resized_bitmap, 0f, 0f, null);
            document.finishPage(page);
            bitmap.recycle();
        }

        document.writeTo(fos);
        document.close();

        exit(c,process);
    }
    private static  void exit(Activity c,Process process){
        ClearDirectories.Clear(c,c.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf");
        if(process == ICENTER){
            pdf_save_dialogbox dialog_box = new pdf_save_dialogbox(c,null);
            dialog_box.hide();
            Intent intent = new Intent(c,MainActivity.class);
            c.startActivity(intent);
            c.finish();
        }
        else {
            c.finish();
        }
    }


}

