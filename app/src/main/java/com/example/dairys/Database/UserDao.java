package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email LIKE :email AND password LIKE :password")
    List<User> login(String email, String password);

    @Insert
    void insertAll(User...users);

    @Query("SELECT * FROM user WHERE is_logged LIKE 1")
    List<User> userLogged();

    @Query("SELECT * FROM user WHERE is_logged LIKE 1 AND id LIKE :id")
    List<User> isUserLogged(int id);

    @Query("SELECT * FROM user WHERE id LIKE :idUser")
    List<User> userFromId(int idUser);

    @Query("UPDATE user SET is_logged = 0")
    void setAllNotLogged();

    @Query("UPDATE user SET is_logged = 1 WHERE username = :username")
    void updateLogged(String username);

    @Query("UPDATE user SET birthday = :birthday WHERE username = :username")
    void updateBirthday(String birthday, String username);

    @Query("UPDATE user SET number = :number WHERE username = :username")
    void updateNumber(String number, String username);

    @Query("UPDATE user SET image_profile = :image WHERE username = :username")
    void updateImageProfile(String image, String username);

    @Transaction
    @Query("SELECT * FROM User WHERE id LIKE :id")
    public List<UserWithDreamDiary> getUsersWithDreamDiary(int id);

}
