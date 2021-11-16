package com.example.kmanager.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kmanager.db.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users ORDER BY username DESC")
    LiveData<List<UserEntity>> getAllUsers();

    @Query("SELECT * FROM users WHERE username=:username")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE username=:username AND password=:password")
    UserEntity getUserLogin(String username, String password);

    @Query("SELECT * FROM users WHERE username=:id")
    LiveData<UserEntity> getUsers(int id);

    @Insert
    long insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("DELETE FROM users")
    void deleteAll();
}