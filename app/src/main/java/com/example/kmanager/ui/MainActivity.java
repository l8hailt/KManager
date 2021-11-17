package com.example.kmanager.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityMainBinding;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.ui.adapter.RoomAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<RoomEntity> rooms;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActions();
        initRoomsView();
    }

    private void initActions() {
        binding.imgLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                    .setPositiveButton("Có", (dialogInterface, i) -> {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        binding.fabAddRoom.setOnClickListener(view -> {
            EditText editText = (EditText) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_edit_text, null);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Thêm phòng")
                    .setView(editText)
                    .setPositiveButton("Thêm", (dialogInterface, i) -> {

                    })
                    .setNegativeButton("Hủy", (dialogInterface, i) -> {

                    })
                    .show();
        });
    }

    private void initRoomsView() {
        rooms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rooms.add(new RoomEntity("Room " + (i + 1)));
        }
        roomAdapter = new RoomAdapter(rooms);
        roomAdapter.setOnRoomClickListener(roomEntity -> {
            Intent roomIntent = new Intent(MainActivity.this, RoomActivity.class);
            roomIntent.putExtra("room", (Serializable) roomEntity);
            startActivity(roomIntent);
        });
        binding.rvRoom.setAdapter(roomAdapter);
    }

}