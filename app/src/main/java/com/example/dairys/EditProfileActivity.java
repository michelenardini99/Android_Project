package com.example.dairys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    AppDatabase db;
    CircleImageView profileImage;
    TextView welcome;
    TextView username;
    TextView email;
    TextView birthday;
    TextView number;
    ExtendedFloatingActionButton save;

    private List<User> user;
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
                db.userDao().updateBirthday(birthdayInserted, username.getText().toString());
                db.userDao().updateNumber(numberInserted, username.getText().toString());
            }
        });

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber();
            }
        });

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