package com.ocean.firebasechatappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ocean.firebasechatappdemo.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButtonHomepage.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, SelectContactActivity.class));
        });
    }
}