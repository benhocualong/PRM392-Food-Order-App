package com.harusora.longcn.apporderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.model.Cart;
import com.harusora.longcn.apporderfood.model.Product;
import com.harusora.longcn.apporderfood.util.Constant;
import com.harusora.longcn.apporderfood.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;

    private OnChangeQuantityListener listener;

    public interface OnChangeQuantityListener {
        void onChangeQuantity(Cart cart);
    }

    public CartAdapter(Context context, OnChangeQuantityListener listener) {
        this.cartList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.setCart(cart);
        holder.cartQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.productOrderPrice.setText(String.valueOf(cart.getProductPrice() * cart.getQuantity()));
        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = cart.getQuantity() + 1;
            cart.setQuantity(newQuantity);
            updateCartItem(position, cart);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (cart.getQuantity() > 1) {
                int newQuantity = cart.getQuantity() - 1;
                cart.setQuantity(newQuantity);
                updateCartItem(position, cart);

            }
        });
    }

    private void updateCartItem(int position, Cart cartItem) {
        cartList.set(position, cartItem);
        notifyItemChanged(position);
        if (listener != null) {
            listener.onChangeQuantity(cartItem);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void addCart(List<Cart> cartListDb) {
        if (cartListDb != null) {
            cartList.addAll(cartListDb);
            notifyDataSetChanged();
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView cartProductName;
        private TextView productPrice;
        private TextView productOrderPrice;
        private TextView cartQuantity;
        private ImageView imageView;
        private Button btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();
        }

        private void bindingView() {
            cartProductName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.productPrice);
            imageView = itemView.findViewById(R.id.imageView);
            productOrderPrice = itemView.findViewById(R.id.productOrderPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            cartQuantity = itemView.findViewById(R.id.tvQuantity);
        }

        public void setCart(Cart cart) {
            cartProductName.setText(cart.getProductName());
            productPrice.setText("Giá: " + cart.getProductPrice() + " " + Constant.VND);
            productOrderPrice.setText(cart.getProductPrice() * cart.getQuantity() + " " + Constant.VND);
            imageView.setImageResource(Utils.getResourceIdFromDrawable(itemView.getContext(), cart.getProductImage()));
        }
    }
}
