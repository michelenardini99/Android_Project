package com.example.dairys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 1;
    AppDatabase db;
    CircleImageView profileImage;
    TextView welcome;
    TextView username;
    TextView email;
    TextView birthday;
    TextView number;
    ExtendedFloatingActionButton save;

    private List<User> user;
    private Uri selectedImage = null;
    private String birthdayInserted = null;
    private String numberInserted = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImage = findViewById(R.id.editImageProfile);
        welcome = findViewById(R.id.textView_show_welcome);
        username = findViewById(R.id.usernameProfile);
        email = findViewById(R.id.emailProfile);
        birthday = findViewById(R.id.birthdayProfile);
        number = findViewById(R.id.numberProfile);
        save = findViewById(R.id.saveProfileChanges);

        db = AppDatabase.getInstance(this);
        user = db.userDao().userLogged();

        try {
            setData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(birthdayInserted != null) db.userDao().updateBirthday(birthdayInserted, username.getText().toString());
                if(numberInserted != null) db.userDao().updateNumber(numberInserted, username.getText().toString());
                if(selectedImage != null) db.userDao().updateImageProfile(selectedImage.toString(), username.getText().toString());
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            }
        });

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , REQUEST_CODE_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_IMAGE){
                selectedImage = data.getData();
                profileImage.setImageURI(selectedImage);
            }
        }
    }

    private void insertNumber() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Insert number");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                numberInserted = input.getText().toString();
                number.setText(numberInserted);
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void setData() throws ParseException {
        welcome.setText("Welcome " + user.get(0).getUsername() + "!");
        username.setText(user.get(0).getUsername());
        email.setText(user.get(0).getEmail());
        if(user.get(0).getBirthday() != null){
            setDataPicker(user.get(0).getBirthday());
        }else{
            setDataPicker(null);
        }
        if(user.get(0).getNumber() != null){
            numberInserted = user.get(0).getNumber();
            number.setText(numberInserted);
        }
        if(user.get(0).getImageProfile() != null){
            selectedImage = Uri.parse(user.get(0).getImageProfile());
            profileImage.setImageURI(selectedImage);
        }
    }

    private void setDataPicker(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if(date != null){
            birthdayInserted = date;
            birthday.setText(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateLimit = simpleDateFormat.parse(date);
            calendar.setTime(dateLimit);
        }

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = day + "/" + (month+1) + "/" + year;
                        birthdayInserted = date;
                        birthday.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }
}