package com.example.qrcodeapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrcodeapp.CapNhatProductActivity;
import com.example.qrcodeapp.MainActivity;
import com.example.qrcodeapp.R;
import com.example.qrcodeapp.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import adapter.ProductAdapter;
import model.Product;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ListView lvProduct;
    ProductAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lvProduct= root.findViewById(R.id.lvProduct);
        adapter=new ProductAdapter(getActivity(),R.layout.item);
        lvProduct.setAdapter(adapter);
        getProductFromFirebase();
//        addEvents();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//    private void addEvents() {
//
//        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Product product = adapter.getItem(position);
//                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = firebaseDatabase.getReference("products");
//                myRef.child(product.getProductId()).removeValue()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getContext(), "Thành công", Toast.LENGTH_LONG).show();
//
//                                adapter.remove(product);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getContext(), "Lỗi rồi :" + e.toString(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//                return false;
//            }
//        });
//        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Product data=adapter.getItem(position);
//                Intent intent=new Intent(getContext(), CapNhatProductActivity.class);
//                intent.putExtra("KEY",data.getProductId());
//                startActivity(intent);
//            }
//        });
//    }

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

}