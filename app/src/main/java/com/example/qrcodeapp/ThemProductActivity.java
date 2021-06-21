package com.example.qrcodeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Category;
import model.Product;
import model.Vacxin;

public class ThemProductActivity extends AppCompatActivity {
    ImageButton btnCapture;
    ImageButton btnChoose;
    ImageView imgPicture;
    Bitmap selectedBitmap;
    EditText edtId,edtTen,edtPrice,edtDescription, edtQuantity, edtExpectedQuantity;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    List<Category> categoryList = new ArrayList<Category>();
    List<Vacxin> vacxinList = new ArrayList<Vacxin>();
    Spinner categorySpinner, vacxinSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_product);
        addControls();
        addEvents();
    }
    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference myRef= firebaseDatabase.getReference("category");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
                    String name = dss.child("name").getValue().toString();
                    String key = dss.getKey();
                    Category g = new Category(key,name);
                    categoryList.add(g);
                }
                categorySpinner = findViewById(R.id.spnCategory_add_product);
                List<String> galleryNameList = new ArrayList<String>();
                galleryNameList.add("Chọn danh mục-----");
                for (Category gallery:categoryList) {
                    galleryNameList.add(gallery.getName());
                }
                ArrayAdapter<String> galleryArrayAdapter = new ArrayAdapter<String>(ThemProductActivity.this, android.R.layout.simple_spinner_item, galleryNameList);
                galleryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(galleryArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Get vacxin
        DatabaseReference rff= firebaseDatabase.getReference("vacxin");
        rff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vacxinList.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
                    String name = dss.child("name").getValue().toString();
                    String key = dss.getKey();
                    Vacxin g = new Vacxin(key,name);
                    vacxinList.add(g);
                }
                vacxinSpinner = findViewById(R.id.spnVacxin_add_product);
                List<String> vacxinNameList = new ArrayList<String>();
                vacxinNameList.add("Chọn vacxin-----");
                for (Vacxin vacxin:vacxinList) {
                    vacxinNameList.add(vacxin.getName());
                }
                ArrayAdapter<String> vacxinArrayAdapter = new ArrayAdapter<String>(ThemProductActivity.this, android.R.layout.simple_spinner_item, vacxinNameList);
                vacxinArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vacxinSpinner.setAdapter(vacxinArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        edtQuantity = findViewById(R.id.edtQuantity_add_product);
        edtExpectedQuantity = findViewById(R.id.edtExpectedQuantity_add_product);

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
    public void xuLyThemMoi(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("products");

            String ten = edtTen.getText().toString();
            String productId= ten+java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            String price = edtPrice.getText().toString();
            String description = edtDescription.getText().toString();
            String quantity = edtQuantity.getText().toString();
            String category = categoryList.get(categorySpinner.getSelectedItemPosition() - 1).getId();
            myRef.child(productId).child("price").setValue(price);
            myRef.child(productId).child("description").setValue(description);
            myRef.child(productId).child("name").setValue(ten);
            myRef.child(productId).child("category").setValue(category);
            myRef.child(productId).child("quantity").setValue(quantity);

            //đưa bitmap về base64string:
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream .toByteArray();
//            String imgeEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myRef.child(productId).child("picture").setValue("imgeEncoded");
            Intent intent = new Intent(ThemProductActivity.this, QRGenerate.class);
            intent.putExtra("id", productId);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
}