package com.example.kmanager.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityRegisterBinding;
import com.example.kmanager.db.repo.UsersRepository;
import com.example.kmanager.db.entity.UserEntity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private UsersRepository usersRepository;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActions();
        usersRepository = new UsersRepository(getApplication());
        prefs = getSharedPreferences("k_prefs", MODE_PRIVATE);
    }

    private void initActions() {
        binding.imgClose.setOnClickListener(view -> onBackPressed());

        binding.edtPosition.setOnClickListener(v -> {
            String[] options = {"Lễ tân", "Phục vụ", "Nhân viên order"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn vị trí");
            builder.setItems(options, (dialog, which) -> {
                binding.edtPosition.setText(options[which]);
            });
            builder.show();
        });

        binding.btnRegister.setOnClickListener(view -> doRegister());
    }

    private void doRegister() {
        binding.tvError.setVisibility(View.INVISIBLE);

        String username = binding.edtUsername.getText().toString().trim();
        String position = binding.edtPosition.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String rePassword = binding.edtRePassword.getText().toString().trim();

        if (!validateRegister(username, position, password, rePassword)) {
            return;
        }

        new Thread(() -> {
            UserEntity userCheck = usersRepository.getUser(username);
            if (userCheck != null) {
                runOnUiThread(() -> showErrorMsg("Tài khoản đã tồn tại"));
            } else {
                UserEntity user = new UserEntity(username, password, position);
                long id = usersRepository.insertUser(user);
                if (id > -1) {
                    prefs.edit().putString("username", username).apply();
                    prefs.edit().putString("position", position).apply();
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    showErrorMsg(getString(R.string.unknown_error));
                }
            }
        }).start();
    }

    private boolean validateRegister(String username, String position, String password, String rePassword) {
        if ("".equals(username)) {
//            showErrorMsg("Tên đăng nhập không được để trống");
            showErrorMsg("Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if ("".equals(position)) {
//            showErrorMsg("Tên đăng nhập không được để trống");
            showErrorMsg("Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if ("".equals(password)) {
//            showErrorMsg("Mật khẩu không được để trống");
            showErrorMsg("Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if ("".equals(rePassword)) {
//            showErrorMsg("Xác nhận mật khẩu không được để trống");
            showErrorMsg("Vui lòng nhập đầy đủ thông tin");
            return false;
        } else {
            if (!password.equals(rePassword)) {
                showErrorMsg("Mật khẩu không khớp");
                return false;
            }
        }
        if ("admin".equals(username)) {
            showErrorMsg("Tên đăng nhập không hợp lệ");
            return false;
        }

        return true;
    }

    private void showErrorMsg(String msg) {
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvError.setText(msg);
    }

}