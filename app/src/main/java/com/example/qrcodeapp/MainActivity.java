package com.example.qrcodeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        if(item.getItemId()==R.id.mnuAdd)
        {
        //mở màn hình thêm ở đây
            Intent intent=new Intent(MainActivity.this,ThemProductActivity.class);
            startActivity(intent);
        }
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
                                Toast.makeText(MainActivity.this, "Thành công", Toast.LENGTH_LONG).show();

                                adapter.remove(product);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Lỗi rồi :" + e.toString(), Toast.LENGTH_LONG).show();
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
                    //convert ra đối tượng Product:
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