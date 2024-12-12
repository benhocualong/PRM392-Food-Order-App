package com.harusora.longcn.apporderfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.adapter.CartCheckoutAdapter;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;
import com.harusora.longcn.apporderfood.database.table.CartTable;
import com.harusora.longcn.apporderfood.model.Cart;
import com.harusora.longcn.apporderfood.model.Order;
import com.harusora.longcn.apporderfood.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class CheckoutActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private List<Cart> cartList;

    private Button btnCheckout;

    CartCheckoutAdapter cartCheckoutAdapter;

    private TextView tvTotalPrice, etAddress, edtNote;

    private RadioGroup radioGroupPaymentMethod;

    private RecyclerView recyclerCartView;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.checkout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        loadData();
        bindingData();
        initRecyclerCartView();
        handleCheckout();
    }

    private void handleCheckout() {
        btnCheckout.setOnClickListener(v -> {
            Cursor cursor = databaseHelper.getAllCartByUserId(pref.getInt("userId", 1));
            List<Cart> carts = new ArrayList<>();
            if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
                do {
                    Cart cart = new Cart(
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_USER_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_QUANTITY)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_NAME)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_PRICE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_IMAGE))
                    );
                    carts.add(cart);
                } while (cursor.moveToNext());
            }
            AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
            carts.forEach(cart -> totalAmount.set(totalAmount.get() + cart.getQuantity() * cart.getProductPrice()));
            Order order = new Order("PRM392" + new Random().nextInt(100000), pref.getInt("userId", 1), totalAmount.get(), 1, radioGroupPaymentMethod.getCheckedRadioButtonId(), carts, etAddress.getText().toString(), edtNote.getText().toString());
            databaseHelper.insertOrder(order);
            databaseHelper.deleteCartByUserId(pref.getInt("userId", 1));
            Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initView() {
        btnCheckout = findViewById(R.id.btnConfirmOrder);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        databaseHelper = new DatabaseHelper(this);
        pref = getSharedPreferences("login", MODE_PRIVATE);
        recyclerCartView = findViewById(R.id.cartCheckoutView);
        cartList = new ArrayList<>();
        etAddress = findViewById(R.id.etAddress);
        edtNote = findViewById(R.id.edtNote);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
    }

    private void loadData() {
        Cursor cursor = databaseHelper.getAllCartByUserId(pref.getInt("userId", 1));
        if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
            do {
                Cart cart = new Cart(
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_QUANTITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_IMAGE))
                );
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void bindingData() {
        AtomicReference<Double> calTotalOrder = new AtomicReference<>((double) 0);
        cartList.forEach(cart -> {
            calTotalOrder.set(calTotalOrder.get() + cart.getQuantity() * cart.getProductPrice());
        });
        tvTotalPrice.setText("Tổng giá trị: " + calTotalOrder.get() + " " + Constant.VND);


    }

    private void initRecyclerCartView() {
        cartCheckoutAdapter = new CartCheckoutAdapter(this);
        cartCheckoutAdapter.addCart(cartList);
        recyclerCartView.setAdapter(cartCheckoutAdapter);
        recyclerCartView.setLayoutManager(new LinearLayoutManager(this));
    }
}