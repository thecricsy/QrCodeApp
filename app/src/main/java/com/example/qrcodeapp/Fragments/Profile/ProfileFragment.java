package com.example.qrcodeapp.Fragments.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qrcodeapp.MainActivity2;
import com.example.qrcodeapp.NoteActivity;
import com.example.qrcodeapp.UserProfile;
import com.example.qrcodeapp.AccountSettingsActivity;
import com.example.qrcodeapp.LoginActivity;
import com.example.qrcodeapp.NotificationActivity;
import com.example.qrcodeapp.R;
import com.example.qrcodeapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import model.User;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uId;
    Button btnLogOut, btnBackHome;
    TextView txtName, txtEmail, txtEditProfile, txtNote, txtAccountSettings, txtNotify;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        uId = mAuth.getUid();
        txtEmail = root.findViewById(R.id.txtEmail_profile);
        txtName = root.findViewById(R.id.txtName_profile);
        btnBackHome = root.findViewById(R.id.btnBack_Home);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity2.class ));
            }
        });
        btnLogOut = root.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.init(getContext());
                Paper.book().delete(User.password);
                startActivity(new Intent(getContext(), LoginActivity.class ));
            }
        });
        txtNote = root.findViewById(R.id.txtNote);
        txtNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NoteActivity.class));
            }
        });
        txtAccountSettings = root.findViewById(R.id.txtSettings);
        txtAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AccountSettingsActivity.class));
            }
        });
        txtNotify = root.findViewById(R.id.txtNotify);
        txtNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });
        txtEditProfile = root.findViewById(R.id.txtEdit_profile);
        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserProfile.class));
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