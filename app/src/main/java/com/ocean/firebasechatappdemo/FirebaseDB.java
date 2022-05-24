package com.ocean.firebasechatappdemo;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ocean.firebasechatappdemo.model.ChatModel;
import com.ocean.firebasechatappdemo.model.UserModel;

import lombok.Data;

@Data
public class FirebaseDB {

    private CollectionReference databaseReference;
    private static FirebaseDB instance;
    private FirebaseCommonDB<UserModel> userModel;
    private FirebaseCommonDB<ChatModel> chatModel;

    public static FirebaseDB getInstance() {
        if(instance == null)
            instance = new FirebaseDB();
        return instance;
    }

    private FirebaseDB(){
        databaseReference = FirebaseFirestore.getInstance().collection("UserList");
        userModel = new FirebaseCommonDB<>(UserModel.class,databaseReference);

        databaseReference = FirebaseFirestore.getInstance().collection("ChatList");
        chatModel = new FirebaseCommonDB<>(ChatModel.class, databaseReference);
    }

    public FirebaseCommonDB<UserModel> getUserModel(){
        return userModel;
    }

    public FirebaseCommonDB<ChatModel> getChatModel(){
        return chatModel;
    }

}
