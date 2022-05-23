package com.example.dairys.Access;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.User;
import com.example.dairys.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textInputLayoutPassword = findViewById(R.id.passwordRegistration);
        textInputLayoutEmail = findViewById(R.id.emailRegistration);
        textInputLayoutUsername = findViewById(R.id.userNameRegistration);
    }

    public void onSignInClicked(View v)
    {
        String password = textInputLayoutPassword.getEditText().getText().toString();
        String email = textInputLayoutEmail.getEditText().getText().toString();
        String username = textInputLayoutUsername.getEditText().getText().toString();

        if(Access.isPasswordCorrect(password, textInputLayoutPassword)
            && Access.isEmailCorrect(email, textInputLayoutEmail)
            && Access.isUsernameCorrect(username, textInputLayoutUsername)){
            db = AppDatabase.getInstance(RegistrationActivity.this);
            db.userDao().insertAll(new User(username, email, password));
            goToLogin();
        }
    }

    public void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}