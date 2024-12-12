package com.harusora.longcn.apporderfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.adapter.CartAdapter;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;
import com.harusora.longcn.apporderfood.database.table.CartTable;
import com.harusora.longcn.apporderfood.model.Cart;
import com.harusora.longcn.apporderfood.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerDiscountView;
    private RecyclerView recyclerCartView;
    private TextView totalOrder, discountAddText, totalShip, totalDiscount, totalAmount;

    private PopupWindow popupWindow;

    Button addDiscountBtn, btnCheckout;

    CartAdapter cartAdapter;

    private DatabaseHelper databaseHelper;

    private List<Cart> cartList;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        loadData();
        bindingData();
        initRecyclerCartView();
        handleBtnCheckout();
    }

    private void handleBtnCheckout() {
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });
    }


    private void bindingData() {
        AtomicReference<Double> calTotalOrder = new AtomicReference<>((double) 0);
        cartList.forEach(cart -> {
            calTotalOrder.set(calTotalOrder.get() + cart.getQuantity() * cart.getProductPrice());
        });
        totalOrder.setText(calTotalOrder.get() + " " + Constant.VND);
        totalAmount.setText(calTotalOrder.get() + " " + Constant.VND);
    }

    private void initView() {
        totalOrder = findViewById(R.id.totalOrder);
        discountAddText = findViewById(R.id.discountAddText);
        btnCheckout = findViewById(R.id.btnCheckout);
        addDiscountBtn = findViewById(R.id.btnDiscountPopup);
//        discountAdapter = new DiscountAdapter(discountCodes, this::onDiscountSelected);
        recyclerCartView = findViewById(R.id.cartView);
        totalShip = findViewById(R.id.totalShip);
        totalDiscount = findViewById(R.id.totaDiscount);
        totalAmount = findViewById(R.id.totalAmount);
        cartList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        pref = getSharedPreferences("login", MODE_PRIVATE);
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

    private void initRecyclerCartView() {
        cartAdapter = new CartAdapter(this, this::onChangeQuantity);
        cartAdapter.addCart(cartList);
        recyclerCartView.setAdapter(cartAdapter);
        recyclerCartView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onChangeQuantity(Cart cart) {
        databaseHelper.updateCart(cart.getId(), cart.getProductId(), cart.getQuantity(), cart.getUserId());
        bindingData();
    }
}