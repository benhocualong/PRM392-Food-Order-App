package com.harusora.longcn.apporderfood.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.adapter.OrderAdapter;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;
import com.harusora.longcn.apporderfood.database.table.OrderTable;
import com.harusora.longcn.apporderfood.database.table.ProductTable;
import com.harusora.longcn.apporderfood.model.Order;
import com.harusora.longcn.apporderfood.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private DatabaseHelper db;

    private RecyclerView recyclerView;

    private OrderAdapter adapter;

    private SharedPreferences mPreferences;

    private List<Order> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        loadData();
        adapter.addOrders(orderList);
        return view;
    }

    private void loadData() {

        Cursor cursor = db.getAllOrdersByUserId(mPreferences.getInt("userId", 1));
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_ORDER_CODE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_USER_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_TOTAL_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(OrderTable.COLUMN_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initView(View view) {
        db = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.orderRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(getContext());
        recyclerView.setAdapter(adapter);
        mPreferences = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        orderList = new ArrayList<>();
    }
}