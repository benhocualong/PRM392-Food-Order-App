package com.harusora.longcn.apporderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.model.Cart;
import com.harusora.longcn.apporderfood.util.Constant;
import com.harusora.longcn.apporderfood.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CartCheckoutAdapter extends RecyclerView.Adapter<CartCheckoutAdapter.CartCheckoutViewHolder>{

    private List<Cart> cartList;
    public CartCheckoutAdapter(Context context) {
        this.cartList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartCheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_cart_checkout, parent, false);
        return new CartCheckoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartCheckoutViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.setCart(cart);
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

    public static class CartCheckoutViewHolder extends RecyclerView.ViewHolder {

        private TextView cartProductName;
        private TextView productPrice;
        private TextView productOrderPrice;
        private TextView quantity;
        private ImageView imageView;

        public CartCheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();
        }

        private void bindingView() {
            cartProductName = itemView.findViewById(R.id.cartProductNameCO);
            productPrice = itemView.findViewById(R.id.productPriceCO);
            imageView = itemView.findViewById(R.id.imageViewCO);
            productOrderPrice = itemView.findViewById(R.id.productOrderPriceCO);
            quantity = itemView.findViewById(R.id.tvQuantityCO);
        }

        public void setCart(Cart cart) {
            cartProductName.setText(cart.getProductName());
            productPrice.setText(cart.getProductPrice() + " " + Constant.VND);
            productOrderPrice.setText(cart.getProductPrice() * cart.getQuantity() + " " + Constant.VND);
            imageView.setImageResource(Utils.getResourceIdFromDrawable(itemView.getContext(), cart.getProductImage()));
            quantity.setText("Số lượng: " + cart.getQuantity());
        }
    }
}
