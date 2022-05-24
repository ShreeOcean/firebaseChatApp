package com.ocean.firebasechatappdemo;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.ocean.firebasechatappdemo.model.UserModel;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class FirebaseCommonDB <T> {

    private Class<T> model;
    private CollectionReference documentReference;
    DatabaseReference databaseReference;

    public FirebaseCommonDB(Class<T> model, CollectionReference documentReference) {
        this.model = model;
        this.documentReference = documentReference;
    }

    public Task<Void> add(String key, T model, MutableLiveData<Boolean> completeLiveData){
        return documentReference.document(key).set(model).addOnCompleteListener(task -> {
            if(completeLiveData != null){
                completeLiveData.setValue(true);
            }
        });
    }

    // getting of data
    public Query get(){
        return databaseReference;
    }

    public Task<Void> update(String key, T model){
        return databaseReference.child(key).setValue(model);
    }

}
