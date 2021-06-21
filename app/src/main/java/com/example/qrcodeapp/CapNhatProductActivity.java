package com.example.qrcodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import model.Product;

public class CapNhatProductActivity extends AppCompatActivity {
    EditText edtId,edtTen,edtPrice,edtDescription, edtQuantity;
    ImageView imgPicture;
    ImageButton btnCapture;
    ImageButton btnChoose;
    Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_product);
        addControls();
        getProductDetail();
        addEvents();
    }
    private void getProductDetail() {
        Intent intent=getIntent();
        final String key=intent.getStringExtra("KEY");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là product (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("products");

        //truy suất và lắng nghe sự thay đổi dữ liệu
        //chỉ truy suất node được chọn trên ListView myRef.child(key)
        //addListenerForSingleValueEvent để lấy dữ liệu đơn
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Product product=dataSnapshot.getValue(Product.class);
                    product.setProductId(dataSnapshot.getKey());
                    edtQuantity.setText(product.getQuantity());
                    edtId.setText(product.getProductId());
                    edtTen.setText(product.getName());
                    edtPrice.setText(product.getPrice());
                    edtDescription.setText(product.getDescription());
                    if(product.getPicture()!=null) {
                        byte[] decodedString = Base64.decode(product.getPicture(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgPicture.setImageBitmap(decodedByte);
                    }
                }
                catch (Exception ex)
                {
                    Log.e("LOI_JSON",ex.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LOI_CHITIET", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void addControls() {
        btnCapture = (ImageButton) findViewById(R.id.btnCapture);
        btnChoose= (ImageButton) findViewById(R.id.btnChoose);
        imgPicture= (ImageView) findViewById(R.id.imgPicture);
        edtQuantity = findViewById(R.id.edtQuantity);
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
    private void choosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 200);//one can be replaced with any action code
    }

    private void capturePicture() {
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt,100);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(selectedBitmap);
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
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
    public void xuLyCapNhat(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Kết nối tới node có tên là products (node này do ta định nghĩa trong CSDL Firebase)
            DatabaseReference myRef = database.getReference("products");
            String productId=edtId.getText().toString();
            String ten = edtTen.getText().toString();
            String price = edtPrice.getText().toString();
            String description = edtDescription.getText().toString();
            myRef.child(productId).child("price").setValue(price);
            myRef.child(productId).child("description").setValue(description);
            myRef.child(productId).child("name").setValue(ten);

            //đưa bitmap về base64string:
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String imgeEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myRef.child(productId).child("picture").setValue(imgeEncoded);

            finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
}