package com.harusora.longcn.apporderfood.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.adapter.ProductAdapter;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;
import com.harusora.longcn.apporderfood.database.table.ProductTable;
import com.harusora.longcn.apporderfood.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    TextInputLayout tilSearch;
    TextInputEditText etSearch;

    List<Product> products;

    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        loadProduct();
        adapter.addProducts(products);

        return view;
    }



    private void loadProduct() {
        Cursor cursor = db.getAllProducts();
        if (cursor.moveToFirst()) {
            do {
                Product user = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_CATEGORY_ID))
                );
                products.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initView(View view) {
        db = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.rvFoodList);
        adapter = new ProductAdapter(getContext(), this::onProductClick);
        products = new ArrayList<>();
        tilSearch = view.findViewById(R.id.tilSearch);
        etSearch = view.findViewById(R.id.etSearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void onProductClick(Product product) {

        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("productId", product.getId());
        fragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }


    private List<Product> fetchProductsFromDatabase() {
        // Implement logic to fetch products from database
        // Return list of Product objects
        return java.util.Collections.emptyList();
    }
}