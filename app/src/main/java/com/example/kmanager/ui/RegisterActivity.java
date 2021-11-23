package com.example.kmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ActivityRegisterBinding;
import com.example.kmanager.db.repo.UsersRepository;
import com.example.kmanager.db.entity.UserEntity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private UsersRepository usersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActions();
        usersRepository = new UsersRepository(getApplication());
    }

    private void initActions() {
        binding.imgClose.setOnClickListener(view -> onBackPressed());

        binding.btnRegister.setOnClickListener(view -> doRegister());
    }

    private void doRegister() {
        binding.tvError.setVisibility(View.INVISIBLE);

        String username = binding.edtUsername.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String rePassword = binding.edtRePassword.getText().toString().trim();

        if (!validateRegister(username, password, rePassword)) {
            return;
        }

        new Thread(() -> {
            UserEntity userCheck = usersRepository.getUser(username);
            if (userCheck != null) {
                runOnUiThread(() -> showErrorMsg("Tài khoản đã tồn tại"));
            } else {
                UserEntity user = new UserEntity(username, password);
                long id = usersRepository.insertUser(user);
                if (id > -1) {
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else {
                    showErrorMsg(getString(R.string.unknown_error));
                }
            }
        }).start();
    }

    private boolean validateRegister(String username, String password, String rePassword) {
        if ("".equals(username)) {
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

        return true;
    }

    private void showErrorMsg(String msg) {
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvError.setText(msg);
    }

}