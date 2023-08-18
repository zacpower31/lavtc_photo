package com.example.lavtc_photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Switch;
import android.widget.Toast;

import androidx.camera.core.processing.SurfaceProcessorNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SavePDF {

    public SavePDF (Activity activity , ArrayList<Image> images,String filename,boolean i_center) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File pdf_file = new File(Environment.getExternalStorageDirectory().toString() + "/id/PDF/"+date);
        if (!pdf_file.mkdir()) {
            pdf_file.mkdir();
        }
        SavePDF(activity,images,filename,i_center,date);
    }




    private void SavePDF(Activity c,ArrayList<Image> images,String fileName,boolean i_center,String date) throws IOException {
        File target;
        PdfDocument document = new PdfDocument();
        String sdCard = c.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

        if (i_center) {
            target = new File(Environment.getExternalStorageDirectory().toString() + "/I-Center/" + fileName + ".pdf");
        }
        else {
            target = new File(Environment.getExternalStorageDirectory().toString() + "/id/PDF/"+date+"/"+ fileName + ".pdf");
        }
        FileOutputStream fos = new FileOutputStream(target);

        for (int n = 0 ; n < images.size() ; n++){

            Bitmap bitmap = BitmapFactory.decodeFile(images.get(n).getUri().getPath());
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

        String downloadsFolder = c.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+"/pdf";
        ClearDirectories clear = new ClearDirectories(c,downloadsFolder);

        returnToactivity(i_center,c);

    }

    private void returnToactivity(boolean i_center,Activity c) {
        if (i_center){
            pdf_save_dialogbox dialogbox = new pdf_save_dialogbox(c,null);
            dialogbox.hide();
            Intent intent = new Intent(c,MainActivity.class);
            c.startActivity(intent);
            c.finish();

        }
        else {
            Intent intent = new Intent(c,MainActivity.class);
            Intent intent_broadcast = new Intent("finish_activity");
            c.sendBroadcast(intent_broadcast);
            c.startActivity(intent);
            c.finish();

        }

    }

}

