package com.example.kmanager.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private Double price;

    @ColumnInfo(name = "roomId")
    private int roomId;

    @ColumnInfo(name = "orderTime")
    private long orderTime;

    // 0 chưa checkout
    // 1 đã checkout
    @ColumnInfo(name = "checkout")
    private int checkout;

    public OrderEntity(String name, Double price, int roomId) {
        this.name = name;
        this.price = price;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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