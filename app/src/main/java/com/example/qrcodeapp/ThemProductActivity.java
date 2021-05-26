package com.example.qrcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ThemProductActivity extends AppCompatActivity {
    ImageButton btnCapture;
    ImageButton btnChoose;
    ImageView imgPicture;
    Bitmap selectedBitmap;
    EditText edtId,edtTen,edtPrice,edtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_product);
        addControls();
        addEvents();
    }
    public void addControls()
    {
        btnCapture = findViewById(R.id.btnCapture);
        btnChoose= findViewById(R.id.btnChoose);
        imgPicture=findViewById(R.id.imgPicture);
        edtId=findViewById(R.id.edtProductId);
        edtTen=findViewById(R.id.edtTen);
        edtPrice=findViewById(R.id.edtPrice);
        edtDescription=findViewById(R.id.edtDescription);
    }
    public void addEvents() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }
    //xử lý chọn hình
    private void choosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 200);//one can be replaced with any action code
    }
    //xử lý chụp hình
    private void capturePicture() {
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt,100);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100&& resultCode == RESULT_OK) {
    //xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(selectedBitmap);
        }
        else if(requestCode == 200&& resultCode == RESULT_OK) {
            try {
    //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPicture.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void xuLyThemMoi(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Kết nối tới node có tên là products (node này do ta định nghĩa trong CSDL Firebase)
            DatabaseReference myRef = database.getReference("products");
            String contactId=edtId.getText().toString();
            String ten = edtTen.getText().toString();
            String price = edtPrice.getText().toString();
            String description = edtDescription.getText().toString();
            myRef.child(contactId).child("price").setValue(price);
            myRef.child(contactId).child("description").setValue(description);
            myRef.child(contactId).child("name").setValue(ten);

            //đưa bitmap về base64string:
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String imgeEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myRef.child(contactId).child("picture").setValue(imgeEncoded);

            finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
}