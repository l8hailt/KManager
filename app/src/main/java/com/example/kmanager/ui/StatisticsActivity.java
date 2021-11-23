package com.example.kmanager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityStatisticsBinding;
import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.repo.OrdersRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private ActivityStatisticsBinding binding;

    private OrdersRepository ordersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActions();

        ordersRepository = new OrdersRepository(getApplication());

        new Thread(() -> {
            Calendar calendar = Calendar.getInstance();
            Date dateOfMonth = calendar.getTime();
            List<OrderEntity> orders = ordersRepository.getOrderInMonth(dateOfMonth);
            Log.e("TAG", "onCreate: " + orders.size());
            if (!orders.isEmpty()) {
                runOnUiThread(() -> {

                });
            }
        }).start();

    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());
    }

}