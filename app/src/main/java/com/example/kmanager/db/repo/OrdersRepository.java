package com.example.kmanager.db.repo;

import android.app.Application;

import com.example.kmanager.db.AppDatabase;
import com.example.kmanager.db.dao.OrderDAO;
import com.example.kmanager.db.dao.OrderDetailDAO;
import com.example.kmanager.db.entity.OrderDetailEntity;
import com.example.kmanager.db.entity.OrderEntity;

import java.util.Date;
import java.util.List;

public class OrdersRepository {

    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;

    public OrdersRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        orderDAO = db.orderDAO();
        orderDetailDAO = db.orderDetailDAO();
    }

    public List<OrderEntity> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public List<OrderEntity> getOrdersInTime(long fromTime, long toTime) {
        return orderDAO.getOrdersInTime(fromTime, toTime);
    }

    public List<OrderEntity> getOrderOfRoom(int roomId) {
        return orderDAO.getOrdersByRoomId(roomId);
    }

    public OrderEntity getOrderByRoomId(int roomId) {
        return orderDAO.getOrderByRoomId(roomId);
    }

//    public List<OrderEntity> getOrderInMonth(Date dateOfMonth) {
//        return orderDAO.getOrdersByMonth(dateOfMonth);
//    }

    public long insertOrder(OrderEntity order) {
        return orderDAO.insert(order);
    }

    public int updateOrder(OrderEntity order) {
        return orderDAO.update(order);
    }

    public int deleteOrder(OrderEntity order) {
        return orderDAO.delete(order);
    }

    public long insertOrderDetail(OrderDetailEntity detail) {
        return orderDetailDAO.insert(detail);
    }

    public List<OrderDetailEntity> getDetailsByOrderId(long orderId) {
        return orderDetailDAO.getDetailsByOrderId(orderId);
    }

    public int deleteOrderDetail(OrderDetailEntity order) {
        return orderDetailDAO.delete(order);
    }

}