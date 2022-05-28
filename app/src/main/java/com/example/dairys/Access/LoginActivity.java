package com.example.dairys.Access;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DreamDiary;
import com.example.dairys.Database.User;
import com.example.dairys.MainActivity;
import com.example.dairys.R;
import com.example.dairys.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.List;

public class LoginActivity extends AppCompatActivity{

    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutEmail;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayoutPassword = findViewById(R.id.passwordLogin);
        textInputLayoutEmail = findViewById(R.id.emailLogin);

        db = AppDatabase.getInstance(LoginActivity.this);

        db.userDao().setAllNotLogged();
    }

    public void goToRegistration(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View v){
        String password = textInputLayoutPassword.getEditText().getText().toString();
        String email = textInputLayoutEmail.getEditText().getText().toString();

        List<User> checkLogin;
        if(Access.isPasswordCorrect(password, textInputLayoutPassword)
                && Access.isEmailCorrect(email, textInputLayoutEmail)){
            checkLogin = db.userDao().login(email, password);
            if(!checkLogin.isEmpty()){
                db.userDao().updateLogged(checkLogin.get(0).getUsername());
                goToHome(checkLogin.get(0).getUsername());
            }else{
                textInputLayoutEmail.setError("Email can be wrong");
                textInputLayoutPassword.setError("Password can be wrong");
            }
        }
    }

    public void goToHome(String username){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
