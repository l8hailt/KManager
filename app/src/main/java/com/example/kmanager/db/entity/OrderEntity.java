package com.example.kmanager.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "total")
    private Double total;

    @ColumnInfo(name = "roomId")
    private int roomId;

    @ColumnInfo(name = "orderTime")
    private long orderTime;

    // 0 chưa checkout
    // 1 đã checkout
    @ColumnInfo(name = "checkout")
    private int checkout;

    public OrderEntity(Double total, int roomId) {
        this.total = total;
        this.roomId = roomId;
        this.orderTime = System.currentTimeMillis();
        this.checkout = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getCheckout() {
        return checkout;
    }

    public void setCheckout(int checkout) {
        this.checkout = checkout;
    }
}