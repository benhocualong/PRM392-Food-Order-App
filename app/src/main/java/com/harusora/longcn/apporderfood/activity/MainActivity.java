package com.harusora.longcn.apporderfood.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.databinding.ActivityMainBinding;
import com.harusora.longcn.apporderfood.fragment.AccountFragment;
import com.harusora.longcn.apporderfood.fragment.HomeFragment;
import com.harusora.longcn.apporderfood.fragment.OrderFragment;
import com.harusora.longcn.apporderfood.fragment.PromotionFragment;
import com.harusora.longcn.apporderfood.fragment.ShopFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        initView();
    }

    private void initView() {
        pref = getSharedPreferences("login", MODE_PRIVATE);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment());
            } else if(item.getItemId() == R.id.promotion){
                replaceFragment(new PromotionFragment());
            } else if(item.getItemId() == R.id.near){
                replaceFragment(new ShopFragment());
            } else if(item.getItemId() == R.id.order){
                replaceFragment(new OrderFragment());
            } else {
                replaceFragment(new AccountFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }
}