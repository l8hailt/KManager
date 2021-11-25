package com.example.kmanager.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kmanager.db.entity.OrderDetailEntity;
import com.example.kmanager.db.entity.OrderEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface OrderDetailDAO {

    @Query("SELECT * FROM order_details ORDER BY id DESC")
    List<OrderDetailEntity> getAllOrders();

    @Query("SELECT * FROM order_details WHERE orderId=:orderId ORDER BY id DESC")
    List<OrderDetailEntity> getDetailsByOrderId(long orderId);

    @Insert
    long insert(OrderDetailEntity order);

    @Update
    int update(OrderDetailEntity order);

    @Delete
    int delete(OrderDetailEntity order);

    @Query("DELETE FROM orders")
    void deleteAll();

    @Query("UPDATE orders SET checkout=1 WHERE id IN (:ids)")
    void updateCheckoutOrders(List<Long> ids);

}
