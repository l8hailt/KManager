package com.example.kmanager.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityRoomBinding;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.repo.RoomsRepository;

public class RoomActivity extends AppCompatActivity {

    private ActivityRoomBinding binding;
    private RoomEntity roomEntity;

    private RoomsRepository roomsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        roomsRepository = new RoomsRepository(getApplication());

        initActions();

        if (getIntent() != null) {
            roomEntity = (RoomEntity) getIntent().getSerializableExtra("room");
            if (roomEntity != null) {
                binding.tvTitle.setText(roomEntity.getName());
                binding.btnSubmit.setText(roomEntity.isUse() ? "Trả phòng" : "Đặt phòng");
            }
        }

    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());

        binding.btnSubmit.setOnClickListener(v -> {
            if (roomEntity != null) {
                if (roomEntity.isUse()) {
                    new AlertDialog.Builder(RoomActivity.this)
                            .setMessage("Bạn có chắc chắn muốn trả phòng không?")
                            .setPositiveButton("Có", (dialogInterface, i) -> {
                                new Thread(() -> {
                                    roomEntity.setUse(!roomEntity.isUse());
                                    int result = roomsRepository.updateRoom(roomEntity);
                                    runOnUiThread(() -> {
                                        if (result > 0) {
                                            finish();
                                        } else {
                                            Toast.makeText(RoomActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }).start();
                            })
                            .setNegativeButton("Không", null)
                            .show();
                } else {
                    new Thread(() -> {
                        roomEntity.setUse(!roomEntity.isUse());
                        int result = roomsRepository.updateRoom(roomEntity);
                        runOnUiThread(() -> {
                            if (result > 0) {
                                binding.btnSubmit.setText(roomEntity.isUse() ? "Trả phòng" : "Đặt phòng");
                            } else {
                                Toast.makeText(RoomActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            }
        });
    }
}