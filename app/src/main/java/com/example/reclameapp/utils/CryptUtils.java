package com.example.reclameapp.utils;

import android.util.Base64;
import android.util.Log;

public class CryptUtils {
    public String cryptMD5(String text) {
        if (text == "" || text == null)
            return text;

        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public String decryptMD5(String text) {
        if (!(text == "" || text == null))
            text = new String(Base64.decode(text, Base64.DEFAULT));

        return text;
    }
}
