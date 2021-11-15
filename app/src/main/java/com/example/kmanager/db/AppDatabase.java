package com.example.kmanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.kmanager.db.dao.NoteDao;
import com.example.kmanager.db.entity.NoteEntity;

@Database(entities = {NoteEntity.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "notes_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}