package com.example.qrcodeapp.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrcodeapp.AboutUsActivity;
import com.example.qrcodeapp.EditProfileActivity;
import com.example.qrcodeapp.HelpActivity;
import com.example.qrcodeapp.LoginActivity;
import com.example.qrcodeapp.R;
import com.example.qrcodeapp.databinding.FragmentHomeBinding;
import com.example.qrcodeapp.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import model.Product;
import model.User;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;
    TextView txtName, txtEmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uId;
    Button btnLogOut, btnEditProfile, btnHelp, btnAboutUs;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        uId = mAuth.getUid();
        txtEmail = root.findViewById(R.id.txtEmail_profile);
        txtName = root.findViewById(R.id.txtName_profile);
        btnLogOut = root.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.init(getContext());
                Paper.book().delete(User.password);
                startActivity(new Intent(getContext(), LoginActivity.class ));
            }
        });
        btnAboutUs = root.findViewById(R.id.btnAboutUs);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AboutUsActivity.class));
            }
        });
        btnHelp = root.findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HelpActivity.class));
            }
        });
        btnEditProfile = root.findViewById(R.id.btnEdit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        getProductFromFirebase();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getProductFromFirebase() {
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.child(uId).getChildren())
                {
                    txtEmail.setText("Email: "+dss.child("mail").getValue().toString());
                    txtName.setText("Name: "+dss.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}