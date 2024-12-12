package com.harusora.longcn.apporderfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.activity.CartActivity;
import com.harusora.longcn.apporderfood.activity.LoginActivity;
import com.harusora.longcn.apporderfood.adapter.CommentAdapter;
import com.harusora.longcn.apporderfood.database.DatabaseHelper;
import com.harusora.longcn.apporderfood.database.table.CartTable;
import com.harusora.longcn.apporderfood.database.table.CommentTable;
import com.harusora.longcn.apporderfood.database.table.ProductTable;
import com.harusora.longcn.apporderfood.model.Cart;
import com.harusora.longcn.apporderfood.model.Comment;
import com.harusora.longcn.apporderfood.model.Product;
import com.harusora.longcn.apporderfood.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDetailFragment extends Fragment {

    private FloatingActionButton btnAddToCart;

    private Button btnSendComment;

    private RecyclerView recyclerView;


    private Product product;

    private List<Comment> commentList;

    private CommentAdapter commentAdapter;

    private SharedPreferences mPreferences;

    private DatabaseHelper db;

    private ImageView ivProductImage;

    private TextView tvProductName, tvProductDescription, tvProductPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        initView(view);
        loadProduct();
        loadComment();
        bindingData();
        commentAdapter.addComments(commentList);
        return view;
    }

    private void bindingData() {
        tvProductName.setText(product.getName());
        tvProductPrice.setText(String.valueOf(product.getPrice()));
        tvProductDescription.setText(product.getDescription());
        ivProductImage.setImageResource(Utils.getResourceIdFromDrawable(requireContext(), product.getImage()));
    }

    private void loadComment() {
        assert getArguments() != null;
        Cursor cursor = db.getCommentByProductId(getArguments().getInt("productId"));
        if (cursor.moveToFirst()) {
            do {
                Comment comment = new Comment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(CommentTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CommentTable.COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CommentTable.COLUMN_DETAIL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CommentTable.COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CommentTable.COLUMN_DATE))
                );
                commentList.add(comment);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void loadProduct() {
        assert getArguments() != null;
        Cursor cursor = db.getProductById(getArguments().getInt("productId"));
        if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_IMAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_CATEGORY_ID))
            );
        }
        cursor.close();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBtnAddToCartClick();
    }

    private void OnBtnAddToCartClick() {
        btnAddToCart.setOnClickListener(v -> {
            if (mPreferences.getBoolean("isLoggedIn", false)) {
                assert getArguments() != null;
                Cursor cursor = db.getAllCartByUserIdAndProductId(mPreferences.getInt("userId", 1), getArguments().getInt("productId"));
                if (cursor == null || !cursor.moveToFirst()) {
                    db.insertCart(mPreferences.getInt("userId", 1), 1, getArguments().getInt("productId"));
                } else {
                    Cart cart = new Cart(
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_USER_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_QUANTITY)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_NAME)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_PRICE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(CartTable.COLUMN_PRODUCT_IMAGE))
                    );
                    db.updateCart(cart.getId(), getArguments().getInt("productId"), cart.getQuantity() + 1, mPreferences.getInt("userId", 1));
                }
                Intent intent = new Intent(getContext(), CartActivity.class);
                assert getArguments() != null;
                intent.putExtra("productId", getArguments().getInt("productId"));
                startActivity(intent);
            } else {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }


    public void btnAddToCartClicked() {

    }


    private void initView(View view) {
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        btnSendComment = view.findViewById(R.id.btnSendComment);
        recyclerView = view.findViewById(R.id.commentRecycleView);
        product = new Product();
        commentList = new ArrayList<>();
        db = new DatabaseHelper(getContext());
        tvProductName = view.findViewById(R.id.tvProductName);
        tvProductDescription = view.findViewById(R.id.tvProductDescription);
        tvProductPrice = view.findViewById(R.id.tvProductPrice);
        ivProductImage = view.findViewById(R.id.ivProductImage);
        commentAdapter = new CommentAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentAdapter);
    }
}