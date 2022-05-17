package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email_column LIKE :email AND password_column LIKE :password")
    List<User> login(String email, String password);

    @Insert
    void insertAll(User...users);

}
