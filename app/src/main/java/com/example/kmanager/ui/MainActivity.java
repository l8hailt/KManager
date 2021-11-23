package com.example.kmanager.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityMainBinding;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.repo.RoomsRepository;
import com.example.kmanager.ui.adapter.RoomAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private RoomsRepository roomsRepository;

    private List<RoomEntity> rooms;
    private RoomAdapter roomAdapter;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        roomsRepository = new RoomsRepository(getApplication());
        prefs = getSharedPreferences("k_prefs", MODE_PRIVATE);

        String username = prefs.getString("username", "");
        boolean isAdmin = "admin".equals(username);
        binding.fabAddRoom.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        View navHeader = binding.navMain.getHeaderView(0);
        TextView tvUsername = navHeader.findViewById(R.id.tv_username);
        tvUsername.setText(username);

        initActions();
        initRoomsView();

        initRooms();
    }

    private void initActions() {
        binding.imgMenu.setOnClickListener(view -> {
            binding.drawerMain.openDrawer(GravityCompat.START);
        });

        binding.navMain.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.menu_statistics:
                    binding.drawerMain.closeDrawer(GravityCompat.START);
                    Intent statisticsIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                    startActivity(statisticsIntent);
                    break;

                case R.id.menu_logout:
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                            .setPositiveButton("Có", (dialogInterface, i) -> {
                                prefs.edit().putString("username", "").apply();
                                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                            })
                            .setNegativeButton("Không", null)
                            .show();
                    break;
            }
            return true;
        });

        binding.fabAddRoom.setOnClickListener(view -> {
            LinearLayout llContainer = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_edit_text, null);
            EditText edtRoomName = llContainer.findViewById(R.id.edt_room_name);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Thêm phòng")
                    .setView(llContainer)
                    .setCancelable(false)
                    .setPositiveButton("Thêm", (dialogInterface, i) -> {
                        String roomName = edtRoomName.getText().toString().trim();
                        if ("".equals(roomName)) {
                            Toast.makeText(MainActivity.this, "Vui lòng nhập tên phòng", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        new Thread(() -> {
                            RoomEntity room = new RoomEntity(roomName);
                            long id = roomsRepository.insertRoom(room);
                            runOnUiThread(() -> {
                                if (id > -1) {
                                    rooms.add(room);
                                    roomAdapter.notifyItemInserted(rooms.size() - 1);
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }).start();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void initRoomsView() {
        rooms = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            rooms.add(new RoomEntity("Room " + (i + 1)));
//        }
        roomAdapter = new RoomAdapter(rooms);
        roomAdapter.setOnRoomClickListener(roomEntity -> {
            Intent roomIntent = new Intent(MainActivity.this, RoomActivity.class);
            roomIntent.putExtra("room", (Serializable) roomEntity);
            startActivity(roomIntent);
        });
        binding.rvRoom.setAdapter(roomAdapter);
    }

    private void initRooms() {
        new Thread(() -> {
            List<RoomEntity> rooms = roomsRepository.getAllRooms();
            if (rooms != null && !rooms.isEmpty()) {
                this.rooms.clear();
                this.rooms.addAll(rooms);
                runOnUiThread(() -> roomAdapter.notifyDataSetChanged());
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRooms();
    }
}