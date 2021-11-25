package com.example.kmanager.db.repo;

import android.app.Application;

import com.example.kmanager.db.AppDatabase;
import com.example.kmanager.db.dao.RoomDAO;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.entity.UserEntity;

import java.util.List;

public class RoomsRepository {

    private RoomDAO roomDAO;

    public RoomsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        roomDAO = db.roomDAO();
    }

    public List<RoomEntity> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public RoomEntity getRoom(int id) {
        return roomDAO.getRoomById(id);
    }

    public long insertRoom(RoomEntity room) {
        return roomDAO.insert(room);
    }

    public int updateRoom(RoomEntity room) {
        return roomDAO.update(room);
    }

    public int deleteRoom(RoomEntity room) {
        return roomDAO.delete(room);
    }

}