package com.ocean.firebasechatappdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Util {

    public static String convertImageToString(ImageView capturedImage) { //TODO: convertBitmaptoBase64
        capturedImage.buildDrawingCache();
        Bitmap bitmap = capturedImage.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static String convertBitmaptoBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap convertStringTOBitmap(String stringImage){ //TODO: convertBase64ToBitmap
        byte[] decodedString = Base64.decode(stringImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
    }

    public static String uuid(String key){
        String result = UUID.nameUUIDFromBytes(key.getBytes()).toString();
        result=result.replace("-","").substring(0,15).toUpperCase();
        return result;
    }

    public static void showShortToast(Context context, String msg){
        Toast.makeText(context,  msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
