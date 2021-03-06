package com.example.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import adapter.ProductAdapter;
import model.Product;

public class MainActivity extends AppCompatActivity {
    ListView lvProduct;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        getProductFromFirebase();
        addEvents();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuAdd:
                Intent intent=new Intent(MainActivity.this,ThemProductActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuQR:
                Intent intent2=new Intent(MainActivity.this,QRGenerate.class);
                startActivity(intent2);
                break;
            case R.id.mnuBC:
                Intent intent3=new Intent(MainActivity.this,BarcodeGenerate.class);
                startActivity(intent3);
            default:
                break;
        }
//        if(item.getItemId()==R.id.mnuAdd)
//        {
//        //m??? m??n h??nh th??m ??? ????y
//            Intent intent=new Intent(MainActivity.this,ThemProductActivity.class);
//            startActivity(intent);
//        }
//        if(item.getItemId()==R.id.mnuQR)
//        {
//            //m??? m??n h??nh QR ??? ????y
//            Intent intent=new Intent(MainActivity.this,QRGenerate.class);
//            startActivity(intent);
//        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {

        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = adapter.getItem(position);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference("products");
                myRef.child(product.getProductId()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Th??nh c??ng", Toast.LENGTH_LONG).show();

                                adapter.remove(product);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "L???i r???i :" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                return false;
            }
        });
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product data=adapter.getItem(position);
                Intent intent=new Intent(MainActivity.this,CapNhatProductActivity.class);
                intent.putExtra("KEY",data.getProductId());
                startActivity(intent);
            }
        });
    }

    private void getProductFromFirebase() {
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
                    //convert ra ?????i t?????ng Product:
                    Product product=dss.getValue(Product.class);
                    String key=dss.getKey();
                    product.setProductId(key);
                    adapter.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        lvProduct=findViewById(R.id.lvProduct);
        adapter=new ProductAdapter(this,R.layout.item);
        lvProduct.setAdapter(adapter);
    }
}