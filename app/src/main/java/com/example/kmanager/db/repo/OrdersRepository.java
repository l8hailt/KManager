package com.example.kmanager.db.repo;

import android.app.Application;

import com.example.kmanager.db.AppDatabase;
import com.example.kmanager.db.dao.OrderDAO;
import com.example.kmanager.db.entity.OrderEntity;

import java.util.List;

public class OrdersRepository {

    private OrderDAO orderDAO;

    public OrdersRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDAO = db.orderDAO();
    }

    public List<OrderEntity> getOrderOfRoom(int roomId) {
        return orderDAO.getOrdersByRoomId(roomId);
    }

    public long insertOrder(OrderEntity order) {
        return orderDAO.insert(order);
    }

    public int updateOrder(OrderEntity order) {
        return orderDAO.update(order);
    }

    public int deleteOrder(OrderEntity order) {
        return orderDAO.delete(order);
    }

}