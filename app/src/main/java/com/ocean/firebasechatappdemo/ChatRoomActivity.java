package com.ocean.firebasechatappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ocean.firebasechatappdemo.databinding.ActivityChatRoomBinding;

public class ChatRoomActivity extends AppCompatActivity {

    ActivityChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}