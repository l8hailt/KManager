package com.example.kmanager.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityRoomBinding;
import com.example.kmanager.db.entity.OrderDetailEntity;
import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.entity.RoomEntity;
import com.example.kmanager.db.repo.OrdersRepository;
import com.example.kmanager.db.repo.RoomsRepository;
import com.example.kmanager.ui.adapter.OrderAdapter;
import com.example.kmanager.ui.adapter.OrderDetailAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomActivity extends AppCompatActivity {

    private ActivityRoomBinding binding;

    private SharedPreferences prefs;

    private RoomEntity roomEntity;
    private OrderEntity orderEntity;

    private RoomsRepository roomsRepository;
    private OrdersRepository ordersRepository;

    private List<OrderDetailEntity> orderDetails;
    private OrderDetailAdapter orderDetailAdapter;

    private NumberFormat nf;

    private long total = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        roomsRepository = new RoomsRepository(getApplication());
        ordersRepository = new OrdersRepository(getApplication());

        prefs = getSharedPreferences("k_prefs", MODE_PRIVATE);

        nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initActions();

        if (getIntent() != null) {
            roomEntity = (RoomEntity) getIntent().getSerializableExtra("room");
            if (roomEntity != null) {
                binding.tvTitle.setText(roomEntity.getName());
                binding.btnSubmit.setText(roomEntity.isUse() ? "Trả phòng" : "Đặt phòng");
                binding.fabAddRoom.setVisibility(roomEntity.isUse() ? View.VISIBLE : View.GONE);

                initOrdersView();
                new Thread(() -> {
                    orderEntity = ordersRepository.getOrderByRoomId(roomEntity.getId());
                    if (orderEntity != null) {
                        Log.e("TAG", "onCreate: " + orderEntity.getId());

                        List<OrderDetailEntity> orderDetails = ordersRepository.getDetailsByOrderId(orderEntity.getId());
                        Log.e("TAG", "onCreate: " + orderDetails.size());
                        for (OrderDetailEntity detailEntity : orderDetails) {
                            total += detailEntity.getPrice();
                        }
                        this.orderDetails.addAll(orderDetails);
                        runOnUiThread(() -> {
                            orderDetailAdapter.notifyDataSetChanged();
                            binding.tvTotal.setText(nf.format(total));
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
        String username = prefs.getString("username", "");
        boolean isAdmin = "admin".equals(username);
        orderDetails = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(orderDetails, isAdmin, nf);
        orderDetailAdapter.setOnOrderClickListener(new OrderDetailAdapter.OnOrderDetailClickListener() {
            @Override
            public void onEdit(OrderDetailEntity orderEntity, int position) {

            }

            @Override
            public void onRemove(OrderDetailEntity detailEntity, int position) {
                new Thread(() -> {
                    int result = ordersRepository.deleteOrderDetail(detailEntity);
                    runOnUiThread(() -> {
                        if (result > -1) {
                            total -= detailEntity.getPrice();
                            binding.tvTotal.setText(nf.format(total));
                            orderDetails.remove(detailEntity);
                            orderDetailAdapter.notifyItemRemoved(position);
                        } else {
                            Toast.makeText(RoomActivity.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });
        binding.rvOrderDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvOrderDetails.setAdapter(orderDetailAdapter);
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
                            OrderDetailEntity detail = new OrderDetailEntity(orderName,
                                    Long.parseLong(price),
                                    orderEntity.getId());
                            long id = ordersRepository.insertOrderDetail(detail);
                            runOnUiThread(() -> {
                                if (id > -1) {
                                    total += detail.getPrice();
                                    binding.tvTotal.setText(nf.format(total));
                                    detail.setId(id);
                                    orderDetails.add(detail);
                                    orderDetailAdapter.notifyItemInserted(orderDetails.size() - 1);
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

                                orderEntity.setCheckout(true);
                                orderEntity.setTimeEnd(System.currentTimeMillis());
                                orderEntity.setTotal(total);
                                int checkOutResult = ordersRepository.updateOrder(orderEntity);

                                runOnUiThread(() -> {
                                    if (result > -1 && checkOutResult > -1) {
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
                    orderEntity = new OrderEntity(roomEntity.getId());
                    long orderId = ordersRepository.insertOrder(orderEntity);
                    orderEntity.setId(orderId);
                    runOnUiThread(() -> {
                        if (result > -1 && orderId > -1) {
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