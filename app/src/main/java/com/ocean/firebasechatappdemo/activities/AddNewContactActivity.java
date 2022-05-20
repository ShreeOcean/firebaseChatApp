package com.ocean.firebasechatappdemo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.ocean.firebasechatappdemo.databinding.ActivityAddNewContactBinding;

public class AddNewContactActivity extends AppCompatActivity {

    ActivityAddNewContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createContactProfileImage.setOnClickListener(view -> {
            Toast.makeText(this, "Profile Image will be shown after saving the contact...", Toast.LENGTH_SHORT).show();
        });
    }
}