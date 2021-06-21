package com.example.qrcodeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import adapter.ProductAdapter;
import model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    TextView edtId,edtTen,edtPrice,edtDescription, edtQuantity, edtExpectedQuantity, edtPendingQuantity,edtVacxin;
    ImageView imgPicture;
    Button btnStart;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        edtId = findViewById(R.id.txtID_product_detail);
        edtTen = findViewById(R.id.txtName_product_detail);
        edtPrice = findViewById(R.id.txtPrice_product_detail);
        edtQuantity = findViewById(R.id.txtQuantity_product_detail);
        edtDescription = findViewById(R.id.txtDescription_product_detail);
        imgPicture = findViewById(R.id.imgPicture_product_detail);
        edtExpectedQuantity = findViewById(R.id.txtExpectedQuantity_product_detail);
        edtPendingQuantity = findViewById(R.id.txtPending_product_detail);
        edtVacxin = findViewById(R.id.txtVacxin_product_detail);
        btnStart = findViewById(R.id.btnStart_product_detail);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rff= database.getReference();
                rff.child("device").child("1").child("productId").setValue(edtId.getText().toString().trim());
                rff.child("device").child("1").child("count").setValue(edtQuantity.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(ProductDetailActivity.this, "Đã bắt đầu dây chuyền.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        getProductDetail();
    }
    private void getProductDetail() {
        Intent intent=getIntent();
        final String key=intent.getStringExtra("id");
        DatabaseReference myRef = database.getReference("products");
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Product product=dataSnapshot.getValue(Product.class);
                    product.setProductId(dataSnapshot.getKey());
                    edtQuantity.setText("Số lượng: "+product.getQuantity());
                    edtId.setText("ID: "+product.getProductId());
                    edtTen.setText("Tên: "+product.getName());
                    edtPrice.setText("Price: "+product.getPrice());
                    edtDescription.setText("Mô tả: "+product.getDescription());
                    edtExpectedQuantity.setText("Số lượng dự định: "+product.getExpectedQuantity());
                    edtPendingQuantity.setText("Số lượng tạm hoãn: "+product.getPendingQuantity());
                    edtVacxin.setText("Vacxin: "+product.getVacxin());
                    Picasso.get().load(product.getPicture()).into(imgPicture);
//                    if(product.getPicture()!=null) {
//                        byte[] decodedString = Base64.decode(product.getPicture(), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        imgPicture.setImageBitmap(decodedByte);
//                    }
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
}