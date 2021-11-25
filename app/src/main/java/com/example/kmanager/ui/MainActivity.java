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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityMainBinding;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.repo.RoomsRepository;
import com.example.kmanager.ui.adapter.RoomAdapter;
import com.example.kmanager.ui.adapter.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private RoomsRepository roomsRepository;

    private List<RoomEntity> rooms;
    private RoomAdapter roomAdapter;

    private SharedPreferences prefs;

    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); 
        setContentView(binding.getRoot());

        roomsRepository = new RoomsRepository(getApplication());
        prefs = getSharedPreferences("k_prefs", MODE_PRIVATE);

        String username = prefs.getString("username", "");
        String position = prefs.getString("position", "");
        isAdmin = "admin".equals(username);
        binding.fabAddRoom.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
//        binding.imgLogout.setVisibility(isAdmin ? View.GONE : View.VISIBLE);
//        binding.imgMenu.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        View navHeader = binding.navMain.getHeaderView(0);
        TextView tvUsername = navHeader.findViewById(R.id.tv_username);
        TextView tvPosition = navHeader.findViewById(R.id.tv_position);
        tvUsername.setText(username);
        tvPosition.setText(position);
        if (isAdmin) {

        } else {
            binding.navMain.getMenu().removeItem(R.id.menu_statistics);
        }
//        binding.drawerMain.setDrawerLockMode(isAdmin ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


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
                    doLogout();
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

        binding.imgLogout.setOnClickListener(view -> doLogout());
    }

    private void initRoomsView() {
        rooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(rooms);
        roomAdapter.setOnRoomClickListener(new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onClick(RoomEntity roomEntity) {
                Intent roomIntent = new Intent(MainActivity.this, RoomActivity.class);
                roomIntent.putExtra("room", roomEntity);
                startActivity(roomIntent);
            }

            @Override
            public void onLongClick(RoomEntity roomEntity, int position) {
                if (isAdmin) {
                    showEditRoomOptionsDialog(roomEntity, position);
                }
            }
        });
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) binding.rvRoom.getLayoutManager();
        int spanCount = gridLayoutManager != null ? gridLayoutManager.getSpanCount() : 3;
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(spanCount, spacingInPixels, true);
        binding.rvRoom.addItemDecoration(spacesItemDecoration);
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

    private void doLogout() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    prefs.edit().putString("username", "").apply();
                    prefs.edit().putString("position", "").apply();
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void showEditRoomOptionsDialog(RoomEntity roomEntity, int position) {
        String[] options = {"Sửa", "Xóa"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(roomEntity.getName());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showEditRoomDialog(roomEntity, position);
                    break;
                case 1:
                    showConfirmDeleteRoomDialog(roomEntity, position);
                    break;
            }
        });
        builder.show();
    }

    private void showConfirmDeleteRoomDialog(RoomEntity roomEntity, int position) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Bạn có chắc chắn xóa " + roomEntity.getName() + " không?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    new Thread(() -> {
                        int deleteResult = roomsRepository.deleteRoom(roomEntity);
                        runOnUiThread(() -> {
                            if (deleteResult > -1) {
                                rooms.remove(roomEntity);
                                roomAdapter.notifyItemRemoved(position);
                            } else {
                                Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void showEditRoomDialog(RoomEntity roomEntity, int position) {
        LinearLayout llContainer = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_edit_text, null);
        EditText edtRoomName = llContainer.findViewById(R.id.edt_room_name);
        edtRoomName.setText(roomEntity.getName());
        edtRoomName.setSelection(roomEntity.getName().length());
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Cập nhật")
                .setView(llContainer)
                .setCancelable(false)
                .setPositiveButton("Cập nhật", (dialogInterface, i) -> {
                    String roomName = edtRoomName.getText().toString().trim();
                    if ("".equals(roomName)) {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập tên phòng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(() -> {
                        roomEntity.setName(roomName);
                        int updateResult = roomsRepository.updateRoom(roomEntity);
                        runOnUiThread(() -> {
                            if (updateResult > -1) {
                                rooms.set(position, roomEntity);
                                roomAdapter.notifyItemChanged(position);
                            } else {
                                Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRooms();
    }
}