package com.ocean.firebasechatappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ocean.firebasechatappdemo.databinding.ActivitySelectContactBinding;

public class SelectContactActivity extends AppCompatActivity {

    ActivitySelectContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.linearLayoutAddNewContact.setOnClickListener(view -> {

        });
    }
}