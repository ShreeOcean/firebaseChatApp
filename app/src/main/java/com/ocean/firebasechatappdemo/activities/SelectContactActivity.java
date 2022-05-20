package com.ocean.firebasechatappdemo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
            startActivity(new Intent(SelectContactActivity.this, AddNewContactActivity.class));
        });
    }
}