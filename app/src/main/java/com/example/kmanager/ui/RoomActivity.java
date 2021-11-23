package com.example.kmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityRoomBinding;
import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.repo.OrdersRepository;
import com.example.kmanager.db.repo.RoomsRepository;
import com.example.kmanager.ui.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private ActivityRoomBinding binding;
    private RoomEntity roomEntity;

    private RoomsRepository roomsRepository;
    private OrdersRepository ordersRepository;

    private List<OrderEntity> orders;
    private OrderAdapter orderAdapter;

    private Double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        roomsRepository = new RoomsRepository(getApplication());
        ordersRepository = new OrdersRepository(getApplication());

        initActions();

        if (getIntent() != null) {
            roomEntity = (RoomEntity) getIntent().getSerializableExtra("room");
            if (roomEntity != null) {
                binding.tvTitle.setText(roomEntity.getName());
                binding.btnSubmit.setText(roomEntity.isUse() ? "Trả phòng" : "Đặt phòng");
                binding.fabAddRoom.setVisibility(roomEntity.isUse() ? View.VISIBLE : View.GONE);

                initOrdersView();
                new Thread(() -> {
                    List<OrderEntity> ordersOfRoom = ordersRepository.getOrderOfRoom(roomEntity.getId());
                    if (ordersOfRoom != null && !ordersOfRoom.isEmpty()) {
                        for (OrderEntity orderEntity : ordersOfRoom) {
                            total += orderEntity.getPrice();
                        }
                        orders.addAll(ordersOfRoom);
                        runOnUiThread(() -> {
                            orderAdapter.notifyDataSetChanged();
                            binding.tvTotal.setText(String.valueOf(total));
                        });
                    }
                }).start();
            } else {
                binding.fabAddRoom.setVisibility(View.GONE);
            }
        }

    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());

        binding.btnSubmit.setOnClickListener(v -> checkInOrCheckOutRoom());

        binding.fabAddRoom.setOnClickListener(v -> showAddOrderDialog());
    }

    private void initOrdersView() {
        orders = new ArrayList<>();
        orderAdapter = new OrderAdapter(orders);
        orderAdapter.setOnOrderClickListener(new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onEdit(OrderEntity orderEntity, int position) {

            }

            @Override
            public void onRemove(OrderEntity orderEntity, int position) {
                new Thread(() -> {
                    int result = ordersRepository.deleteOrder(orderEntity);
                    runOnUiThread(() -> {
                        if (result > -1) {
                            total -= orderEntity.getPrice();
                            binding.tvTotal.setText(String.valueOf(total));
                            orders.remove(orderEntity);
                            orderAdapter.notifyItemRemoved(position);
                        } else {
                            Toast.makeText(RoomActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });
        binding.rvOrders.setAdapter(orderAdapter);
    }

    private void showAddOrderDialog() {
        LinearLayout llContainer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_add_order, null);
        EditText edtOrderName = llContainer.findViewById(R.id.edt_order_name);
        EditText edtPrice = llContainer.findViewById(R.id.edt_price);
        new AlertDialog.Builder(this)
                .setTitle("Thêm sản phẩm, dịch vụ")
                .setView(llContainer)
                .setCancelable(false)
                .setPositiveButton("Thêm", (dialogInterface, i) -> {
                    String orderName = edtOrderName.getText().toString().trim();
                    String price = edtPrice.getText().toString().trim();
                    if ("".equals(orderName) && "".equals(price)) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(() -> {
                        try {
                            OrderEntity order = new OrderEntity(orderName,
                                    Double.parseDouble(price),
                                    roomEntity.getId());
                            long id = ordersRepository.insertOrder(order);
                            runOnUiThread(() -> {
                                if (id > -1) {
                                    total += order.getPrice();
                                    binding.tvTotal.setText(String.valueOf(total));
                                    order.setId(id);
                                    orders.add(order);
                                    orderAdapter.notifyItemInserted(orders.size() - 1);
                                } else {
                                    Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (NumberFormatException nfe) {
                            Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void checkInOrCheckOutRoom() {
        if (roomEntity != null) {
            if (roomEntity.isUse()) {
                new AlertDialog.Builder(RoomActivity.this)
                        .setMessage("Bạn có chắc chắn muốn trả phòng không?")
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            new Thread(() -> {
                                roomEntity.setUse(!roomEntity.isUse());
                                int result = roomsRepository.updateRoom(roomEntity);
                                runOnUiThread(() -> {
                                    if (result > -1) {
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
                    List<Long> ids = new ArrayList<>();
                    for (OrderEntity order : orders) {
                        ids.add(order.getId());
                    }
                    ordersRepository.checkoutOrders(ids);
                    runOnUiThread(() -> {
                        if (result > -1) {
                            binding.btnSubmit.setText(roomEntity.isUse() ? "Trả phòng" : "Đặt phòng");
                            binding.fabAddRoom.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(RoomActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        }
    }

}