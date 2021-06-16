package com.example.qrcodeapp;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;

public class BarcodeGenerate extends AppCompatActivity {
    private EditText edtValue;
    private ImageView qrImage;
    private String inputValue;
    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generate);

        qrImage = findViewById(R.id.qr_image);
        edtValue = findViewById(R.id.edt_value);
        activity = this;

        ActivityCompat.requestPermissions(BarcodeGenerate.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(BarcodeGenerate.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        findViewById(R.id.generate_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValue = edtValue.getText().toString().trim();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(edtValue.getText().toString(), BarcodeFormat.CODE_128, qrImage.getWidth(), qrImage.getHeight());
                    Bitmap bitmap = Bitmap.createBitmap(qrImage.getWidth(), qrImage.getHeight(), Bitmap.Config.RGB_565);
                    for (int i = 0; i < qrImage.getWidth(); i++){
                        for (int j = 0; j < qrImage.getHeight(); j++){
                            bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                        }
                    }
                    qrImage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.save_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) qrImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                FileOutputStream outputStream = null;
                File file = Environment.getExternalStorageDirectory();
                File dir = new File(file.getAbsolutePath() + "/BarCode");
                dir.mkdirs();

                String filename = String.format("%d.png",System.currentTimeMillis());
                File outFile = new File(dir,filename);
                try{
                    outputStream = new FileOutputStream(outFile);
                    Toast.makeText(activity,"Image Saved", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                try{
                    outputStream.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    outputStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}