package com.example.kmanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.kmanager.databinding.ActivityStatisticsBinding;
import com.example.kmanager.db.entity.OrderEntity;
import com.example.kmanager.db.repo.OrdersRepository;
import com.example.kmanager.ui.adapter.OrderAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private ActivityStatisticsBinding binding;

    private OrdersRepository ordersRepository;

    private Calendar currentCalendar;
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private SimpleDateFormat sdf;
    private NumberFormat nf;

    private List<OrderEntity> orders;
    private OrderAdapter orderAdapter;


    private long totalRevenue = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentCalendar = Calendar.getInstance();
        fromDateCalendar = Calendar.getInstance();
        fromDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromDateCalendar.set(Calendar.MINUTE, 0);
        fromDateCalendar.set(Calendar.SECOND, 0);
        toDateCalendar = Calendar.getInstance();
        toDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        toDateCalendar.set(Calendar.MINUTE, 59);
        toDateCalendar.set(Calendar.SECOND, 59);
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        String today = sdf.format(currentCalendar.getTime());
        binding.tvFromDate.setText("Từ ngày: " + today);
        binding.tvToDate.setText("Đến ngày: " + today);

        initActions();

        ordersRepository = new OrdersRepository(getApplication());

        orders = new ArrayList<>();
        orderAdapter = new OrderAdapter(orders);
        orderAdapter.setOnOrderClickListener(orderEntity -> {
            Intent statisticsDetailIntent = new Intent(StatisticsActivity.this, StatisticsDetailActivity.class);
            statisticsDetailIntent.putExtra("order", orderEntity);
            startActivity(statisticsDetailIntent);
        });
        binding.rvOrders.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvOrders.setAdapter(orderAdapter);

        initOrdersStatistics();
    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());

        binding.tvFromDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        setAndDisplayFromDate(year, month, dayOfMonth);
                    }, currentCalendar.get(Calendar.YEAR),
                    currentCalendar.get(Calendar.MONTH) + 1,
                    currentCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(toDateCalendar.getTimeInMillis());
            datePickerDialog.show();
        });

        binding.tvToDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        setAndDisplayToDate(year, month, dayOfMonth);
                        if (toDateCalendar.getTimeInMillis() < fromDateCalendar.getTimeInMillis()) {
                            setAndDisplayFromDate(year, month, dayOfMonth);
                        }
                    }, currentCalendar.get(Calendar.YEAR),
                    currentCalendar.get(Calendar.MONTH) + 1,
                    currentCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(currentCalendar.getTimeInMillis());
            datePickerDialog.show();
        });

        binding.btnSearch.setOnClickListener(view -> {
            initOrdersStatistics();
        });
    }

    private void initOrdersStatistics() {
        new Thread(() -> {
            List<OrderEntity> orders = ordersRepository.getOrdersInTime(fromDateCalendar.getTimeInMillis(), toDateCalendar.getTimeInMillis());
            Log.e("TAG", "onCreate: " + orders.size());
            this.orders.clear();
            this.orders.addAll(orders);
            for (OrderEntity order : this.orders) {
                totalRevenue += order.getTotal();
            }
            runOnUiThread(() -> {
                orderAdapter.notifyDataSetChanged();
                binding.tvTotalRevenue.setText(nf.format(totalRevenue));
            });
        }).start();
    }

    private void setAndDisplayFromDate(int year, int month, int dayOfMonth) {
        binding.tvFromDate.setText("Từ ngày: " + dayOfMonth + "/" + (month + 1) + "/" + year);
        fromDateCalendar.set(Calendar.YEAR, year);
        fromDateCalendar.set(Calendar.MONTH, month);
        fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        fromDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        fromDateCalendar.set(Calendar.MINUTE, 0);
//        fromDateCalendar.set(Calendar.SECOND, 0);
    }

    private void setAndDisplayToDate(int year, int month, int dayOfMonth) {
        binding.tvToDate.setText("Đến ngày: " + dayOfMonth + "/" + (month + 1) + "/" + year);
        toDateCalendar.set(Calendar.YEAR, year);
        toDateCalendar.set(Calendar.MONTH, month);
        toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        toDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
//        toDateCalendar.set(Calendar.MINUTE, 59);
//        toDateCalendar.set(Calendar.SECOND, 59);
    }

}