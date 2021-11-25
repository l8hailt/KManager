package com.example.kmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kmanager.db.entity.RoomEntity;

import java.util.List;

@Dao
public interface RoomDAO {

    @Query("SELECT * FROM rooms ORDER BY id ASC")
    List<RoomEntity> getAllRooms();

    @Query("SELECT * FROM rooms WHERE id=:id")
    RoomEntity getRoomById(int id);

    @Insert
    long insert(RoomEntity room);

    @Update
    int update(RoomEntity room);

    @Delete
    int delete(RoomEntity room);

    @Query("DELETE FROM rooms")
    void deleteAll();

}
