package com.ocean.firebasechatappdemo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ocean.firebasechatappdemo.R;
import com.ocean.firebasechatappdemo.SessionManager;
import com.ocean.firebasechatappdemo.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity  :::";
    private FirebaseAuth firebaseAuth;
    private String verificationId;
    private String phone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvGotoRegister.setOnClickListener(this);
        binding.btnGetOtp.setOnClickListener(this);
        binding.btnVerifyOtp.setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();

//        if (firebaseAuth.getCurrentUser() != null){ //TODO: inbuilt firebase logged in session
//
//            startActivity(new Intent(LoginActivity.this, SubmitDetailsActivity.class));
//            finish();
//        }
        if (sessionManager.notLoggedIn()){
            sessionManager.loggedIn();
            startActivity(new Intent(LoginActivity.this, SubmitDetailsActivity.class));
            finish();
        }

        mCallBack =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "onCodeSent:" + verificationId + "/---/" +forceResendingToken);

                verificationId = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
                if(code != null){
                    binding.inputTextOtp.setText(code);
                    verifyCode(code);
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Log.d(TAG, "onVerificationFailed: " + e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(LoginActivity.this, "Invalid Request: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(LoginActivity.this, "The SMS quota for the project has been exceeded... " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                //Todo: Show a message and update the UI
            }
        };
    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT ).show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    sessionManager.setUserPhoneNum(phone);
                    startActivity(new Intent(LoginActivity.this, SubmitDetailsActivity.class));
                    finish();

                }else {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_get_otp:

                if (TextUtils.isEmpty(binding.inputTextPhoneNum.getText().toString())){
                    binding.inputTextPhoneNum.setError("This feild can not be empty !!!");
                }else if (binding.inputTextPhoneNum.getText().length() != 10){
                    binding.inputTextPhoneNum.setError("Input a valid phone number !!!");
                }else {
                    phone = "+91" + binding.inputTextPhoneNum.getText().toString();
                    sendVerificationCode(phone);//signIn With Phone Auth Credential

                    binding.outlinedTextFieldPhNum.setVisibility(View.GONE);
                    binding.btnGetOtp.setVisibility(View.GONE);

                    binding.outlinedTextFieldOTP.setVisibility(View.VISIBLE);
                    binding.btnVerifyOtp.setVisibility(View.VISIBLE);

                }

                break;
            case R.id.btn_verify_otp:

                if (TextUtils.isEmpty(binding.inputTextOtp.getText().toString())){
                    binding.inputTextOtp.setError("OTP is empty !!!");
                }else if (binding.inputTextOtp.getText().length() != 6){
                    binding.inputTextOtp.setError("OTP is of 6 digit !!!");
                }else {
                    String code = binding.inputTextOtp.getText().toString();
                    verifyCode(code);
                }

                break;
            case R.id.tv_goto_register:
                startActivity(new Intent(LoginActivity.this, SubmitDetailsActivity.class));
                break;
        }
    }

    private void sendVerificationCode(String phone) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                                                        .setPhoneNumber(phone)
                                                        .setTimeout(60L, TimeUnit.SECONDS)
                                                        .setActivity(this)
                                                        .setCallbacks(mCallBack)
                                                        .build();

        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }
}