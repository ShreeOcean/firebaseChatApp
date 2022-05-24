package com.ocean.firebasechatappdemo.model;

import lombok.Data;

@Data
public class ChatModel {

    private String name, message;
    private Long timeStamp;
}
