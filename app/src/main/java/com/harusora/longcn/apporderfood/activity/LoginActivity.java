package com.harusora.longcn.apporderfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;
import com.harusora.longcn.apporderfood.database.table.UserTable;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Button btnLogin;
    EditText etUsername, etPwd;

    private TextView btnForgotPassword, btnSignup;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initView();
        handleLoginBtn();
        handleForgotPasswordBtn();
        handleSignupBtn();
    }

    private void handleSignupBtn() {
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void handleForgotPasswordBtn() {
        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLoginBtn() {
        btnLogin.setOnClickListener(this::handleClick);
    }

    private void handleClick(View view) {
        String username = etUsername.getText().toString();
        String password = etPwd.getText().toString();
        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if(username.equals("admin") && password.equals("admin")){
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        } else {
            Cursor cursor = dbHelper.checkUsernamePassword(etUsername.getText().toString(), etPwd.getText().toString());
            if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putInt("userId", cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)));
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPwd = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogIn);
        pref = getSharedPreferences("login", MODE_PRIVATE);
        btnForgotPassword = findViewById(R.id.forgotPassword);
        btnSignup = findViewById(R.id.btnSignup);
    }
}