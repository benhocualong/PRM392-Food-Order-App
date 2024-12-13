package com.harusora.longcn.apporderfood.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.databinding.ActivityAdminBinding;
import com.harusora.longcn.apporderfood.fragment.admin.ProductFrament;
import com.harusora.longcn.apporderfood.fragment.admin.ShopFragment;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ProductFrament());
        initView();
    }

    private void initView() {
        binding.bottomAdmin.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.product) {
                replaceFragment(new ProductFrament());
            } else {
                replaceFragment(new ShopFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, fragment).commit();
    }
}