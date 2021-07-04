package com.example.qrcodeapp.Fragments.Action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.qrcodeapp.R;
import com.example.qrcodeapp.Scanner;
import com.example.qrcodeapp.ThemProductActivity;
import com.example.qrcodeapp.databinding.FragmentActionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import model.Category;

public class ActionFragment extends Fragment {
    TextView txtDevName, txtDevStatus;
    private FragmentActionBinding binding;
    Button btnAddProduct, btnAddCategory, btnAddVacxin, btnStart;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txtDevName = root.findViewById(R.id.txtDevice_action);
        txtDevStatus = root.findViewById(R.id.txtStatus_action);
        btnAddCategory = root.findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.add_category_layout, null);
                final EditText edtName = (EditText) alertLayout.findViewById(R.id.edtName_add_category);
                final EditText edtExpectedQuantity = (EditText) alertLayout.findViewById(R.id.edtExpectedQuantity_add_category);
                final EditText edtQuantity = (EditText) alertLayout.findViewById(R.id.edtQuantity_add_category);
                final EditText edtPendingQuantity = (EditText) alertLayout.findViewById(R.id.edtPendingQuantity_add_category);
                final EditText edtDescription = (EditText) alertLayout.findViewById(R.id.edtDescription_add_category);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("THÊM DANH MỤC");
                alert.setView(alertLayout);
                alert.setCancelable(true);
                alert.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edtName.getText().toString();
                        String expectedQuantity  = edtExpectedQuantity.getText().toString();
                        String quantity = edtQuantity.getText().toString();
                        String pending = edtPendingQuantity.getText().toString();
                        String des = edtDescription.getText().toString();
                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        Category category = new Category(name+mydate, name, quantity, expectedQuantity, pending, des);
                        DatabaseReference rff= firebaseDatabase.getReference();
                        rff.child("category").child(name+mydate).setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
        btnAddProduct = root.findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ThemProductActivity.class));
            }
        });
        btnAddVacxin = root.findViewById(R.id.btnAddVacxin);
        btnAddVacxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.add_category_layout, null);
                final EditText edtName = (EditText) alertLayout.findViewById(R.id.edtName_add_category);
                final EditText edtExpectedQuantity = (EditText) alertLayout.findViewById(R.id.edtExpectedQuantity_add_category);
                final EditText edtQuantity = (EditText) alertLayout.findViewById(R.id.edtQuantity_add_category);
                final EditText edtPendingQuantity = (EditText) alertLayout.findViewById(R.id.edtPendingQuantity_add_category);
                final EditText edtDescription = (EditText) alertLayout.findViewById(R.id.edtDescription_add_category);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(alertLayout);
                alert.setTitle("THÊM VACXIN");
                alert.setCancelable(true);
                alert.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edtName.getText().toString();
                        String expectedQuantity  = edtExpectedQuantity.getText().toString();
                        String quantity = edtQuantity.getText().toString();
                        String pending = edtPendingQuantity.getText().toString();
                        String des = edtDescription.getText().toString();
                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        Category category = new Category(name+mydate, name, quantity, expectedQuantity, pending, des);
                        DatabaseReference rff= firebaseDatabase.getReference();
                        rff.child("vacxin").child(name+mydate).setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
        btnStart = root.findViewById(R.id.btnStart_action);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Scanner.class));
            }
        });
        getDevice();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getDevice() {
        DatabaseReference myRef= firebaseDatabase.getReference("device");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
                    txtDevName.setText("Thiết bị: "+ dss.child("name").getValue().toString());
                    txtDevStatus.setText("Trạng thái: "+ dss.child("status").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}