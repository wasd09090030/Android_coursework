package com.example.coursework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 登录活动：提供登录和注册功能。
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;

    // SharedPreferences 用于存储用户名和密码
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // 获取 SharedPreferences 实例
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // 登录按钮逻辑
        loginButton.setOnClickListener(v -> login());

        // 注册按钮逻辑
        registerButton.setOnClickListener(v -> register());
    }

    /**
     * 用户登录逻辑。
     */
    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 从 SharedPreferences 中获取存储的用户名和密码
        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);

        // 判断用户名和密码是否匹配
        if (savedUsername != null && savedPassword != null && savedUsername.equals(username) && savedPassword.equals(password)) {
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户注册逻辑。
     */
    private void register() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            // 将用户名和密码保存到 SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();

            Toast.makeText(this, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
        }
    }
}
