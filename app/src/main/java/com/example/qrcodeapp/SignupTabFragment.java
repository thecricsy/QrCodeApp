package com.example.qrcodeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {

    EditText email,pass, confirmPass;
    Button signUp;
    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        email = root.findViewById(R.id.txtMail_signUpForm);
        pass = root.findViewById(R.id.txtPwd_signUpForm);
        confirmPass = root.findViewById(R.id.txtConfirmPwd_signUpForm);
        signUp = root.findViewById(R.id.btnSignUp_signUpForm);

//        email.setTranslationX(800);
//        pass.setTranslationX(800);
//        confirmPass.setTranslationX(800);
//        signUp.setTranslationX(800);
//
//        email.setAlpha(v);
//        pass.setAlpha(v);
//        confirmPass.setAlpha(v);
//        signUp.setAlpha(v);
//
//        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
//        pass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
//        confirmPass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
//        signUp.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();

        return root;
    }
}
