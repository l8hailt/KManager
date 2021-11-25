package com.example.kmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.entity.RoomEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface OrderDAO {

    @Query("SELECT * FROM orders ORDER BY id DESC")
    List<OrderEntity> getAllOrders();

    @Query("SELECT * FROM orders WHERE timeEnd BETWEEN :fromTime AND :toTime")
    List<OrderEntity> getOrdersInTime(long fromTime, long toTime);

    @Query("SELECT * FROM orders WHERE roomId=:roomId AND checkout=0 ORDER BY id DESC")
    List<OrderEntity> getOrdersByRoomId(int roomId);

    @Query("SELECT * FROM orders WHERE roomId=:roomId AND checkout=0 ORDER BY id DESC")
    OrderEntity getOrderByRoomId(int roomId);

    @Insert
    long insert(OrderEntity order);

    @Update
    int update(OrderEntity order);

    @Delete
    int delete(OrderEntity order);

    @Query("DELETE FROM orders")
    void deleteAll();


}
