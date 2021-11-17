package com.example.kmanager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityRoomBinding;
import com.example.kmanager.db.entity.RoomEntity;

public class RoomActivity extends AppCompatActivity {

    private ActivityRoomBinding binding;
    private RoomEntity roomEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActions();

        if (getIntent() != null) {
            roomEntity = (RoomEntity) getIntent().getSerializableExtra("room");
            if (roomEntity != null) {
                binding.tvTitle.setText(roomEntity.getName());
            }
        }

    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());
    }
}