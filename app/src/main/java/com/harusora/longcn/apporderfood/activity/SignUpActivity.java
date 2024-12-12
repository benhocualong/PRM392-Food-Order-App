package com.harusora.longcn.apporderfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {

    EditText etUser, etPad, etRepad;
    Button btnSignUp;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        handleSignUpBtn();
    }

    private void handleSignUpBtn() {
        btnSignUp.setOnClickListener(v -> {
            String username = etUser.getText().toString();
            String password = etPad.getText().toString();
            String repassword = etRepad.getText().toString();
            if (username.isBlank() || password.isBlank() || repassword.isBlank()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(repassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.checkUsername(username)) {
                    Toast.makeText(this, "User already exist", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean registeredSuccess = dbHelper.insertData(username, password);
                if (registeredSuccess) {
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "User Registration failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initView() {
        etUser = findViewById(R.id.etUsername);
        etPad = findViewById(R.id.etPassword);
        etRepad = findViewById(R.id.etRepassword);
        btnSignUp = findViewById(R.id.btnSignup);
        dbHelper = new DatabaseHelper(this);
    }
}