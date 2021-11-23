package com.example.kmanager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.DatePicker;

import com.example.kmanager.databinding.ActivityStatisticsBinding;
import com.example.kmanager.db.repo.OrdersRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private ActivityStatisticsBinding binding;

    private OrdersRepository ordersRepository;

    private Calendar currentCalendar;
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentCalendar = Calendar.getInstance();
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String today = sdf.format(currentCalendar.getTime());
        binding.tvFromDate.setText("Từ ngày: " + today);
        binding.tvToDate.setText("Đến ngày: " + today);

        initActions();

        ordersRepository = new OrdersRepository(getApplication());

//        new Thread(() -> {
//            Calendar calendar = Calendar.getInstance();
//            Date dateOfMonth = calendar.getTime();
//            List<OrderEntity> orders = ordersRepository.getOrderInMonth(dateOfMonth);
//            Log.e("TAG", "onCreate: " + orders.size());
//            if (!orders.isEmpty()) {
//                runOnUiThread(() -> {
//
//                });
//            }
//        }).start();

    }

    private void initActions() {
        binding.imgBack.setOnClickListener(view -> onBackPressed());

        binding.tvFromDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        binding.tvFromDate.setText("Từ ngày: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, currentCalendar.get(Calendar.YEAR),
                    currentCalendar.get(Calendar.MONTH) + 1,
                    currentCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(toDateCalendar.getTimeInMillis());
            datePickerDialog.show();
        });

        binding.tvToDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        binding.tvToDate.setText("Đến ngày: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, currentCalendar.get(Calendar.YEAR),
                    currentCalendar.get(Calendar.MONTH) + 1,
                    currentCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(currentCalendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

}