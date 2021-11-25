package com.example.kmanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.kmanager.db.dao.OrderDAO;
import com.example.kmanager.db.dao.OrderDetailDAO;
import com.example.kmanager.db.dao.RoomDAO;
import com.example.kmanager.db.dao.UserDAO;
import com.example.kmanager.db.entity.OrderDetailEntity;
import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.entity.UserEntity;

@Database(entities = {
        UserEntity.class,
        RoomEntity.class,
        OrderEntity.class,
        OrderDetailEntity.class
}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO userDAO();
    public abstract RoomDAO roomDAO();
    public abstract OrderDAO orderDAO();
    public abstract OrderDetailDAO orderDetailDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "k_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}