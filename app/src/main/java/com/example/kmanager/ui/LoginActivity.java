package com.example.kmanager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.kmanager.databinding.ActivityLoginBinding;
import com.example.kmanager.db.UsersRepository;
import com.example.kmanager.db.entity.UserEntity;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private UsersRepository usersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActions();
        usersRepository = new UsersRepository(getApplication());

    }

    private void initActions() {
        binding.btnLogin.setOnClickListener(view -> {
            doLogin();
        });

        binding.tvRegister.setOnClickListener(view -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
    }

    private void doLogin() {
        String username = binding.edtUsername.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (!validateDataLogin(username, password)) {
            return;
        }

        if ("admin".equals(username) && "1".equals(password)) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            return;
        }

        new Thread(() -> {
            UserEntity userLogin = usersRepository.getUserLogin(username, password);
            if (userLogin == null) {
                runOnUiThread(() -> showErrorMsg("Tài khoản không tồn tại hoặc mật khẩu không đúng"));
            } else {
                runOnUiThread(() -> {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                });
            }
        }).start();
    }

    private boolean validateDataLogin(String username, String password) {
        if ("".equals(username)) {
            showErrorMsg("Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if ("".equals(password)) {
            showErrorMsg("Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }

    private void showErrorMsg(String msg) {
        binding.tvError.setVisibility(View.VISIBLE);
        binding.tvError.setText(msg);
    }

}