package com.example.dairys.Access;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Access {

    public static final int PASSWORD_MIN_LENGTH = 6;

    public static boolean isPasswordCorrect(String password, TextInputLayout inputLayout){
        if(TextUtils.isEmpty(password) || password.length() < PASSWORD_MIN_LENGTH)
        {
            inputLayout.setError("Not enough character");
            return false;
        }
        return true;
    }

    public static boolean isEmailCorrect(String email, TextInputLayout inputLayout){
        if(TextUtils.isEmpty(email))
        {
            inputLayout.setError("Insert email");
            return false;
        }
        return true;
    }

    public static boolean isUsernameCorrect(String username, TextInputLayout inputLayout){
        if(TextUtils.isEmpty(username))
        {
            inputLayout.setError("Insert username");
            return false;
        }
        return true;
    }

}
