package com.example.kmanager.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.kmanager.databinding.ActivityStatisticsDetailBinding;
import com.example.kmanager.db.entity.OrderDetailEntity;
import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.repo.OrdersRepository;
import com.example.kmanager.ui.adapter.OrderDetailAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatisticsDetailActivity extends AppCompatActivity {

    private ActivityStatisticsDetailBinding binding;

    private OrdersRepository ordersRepository;

    private OrderEntity orderEntity;

    private NumberFormat nf;

    private long total;
    private ArrayList<OrderDetailEntity> orderDetails;
    private OrderDetailAdapter orderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ordersRepository = new OrdersRepository(getApplication());

        nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initActions();

        if (getIntent() != null) {
            orderEntity = (OrderEntity) getIntent().getSerializableExtra("order");
            if (orderEntity != null) {
                binding.tvTitle.setText("HD" + orderEntity.getId());

                initOrderDetailsView();
                new Thread(() -> {
                    List<OrderDetailEntity> orderDetails = ordersRepository.getDetailsByOrderId(orderEntity.getId());
                    Log.e("TAG", "onCreate: " + orderDetails.size());
                    for (OrderDetailEntity detailEntity : orderDetails) {
                        total += detailEntity.getPrice();
                    }
                    this.orderDetails.clear();
                    this.orderDetails.addAll(orderDetails);
                    runOnUiThread(() -> {
                        orderDetailAdapter.notifyDataSetChanged();
                        binding.tvTotal.setText(nf.format(total));
                    });
                }).start();
            }
        }

    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());
    }

    private void initOrderDetailsView() {
        orderDetails = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(orderDetails, false, nf);
        binding.rvOrderDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvOrderDetails.setAdapter(orderDetailAdapter);
    }

}