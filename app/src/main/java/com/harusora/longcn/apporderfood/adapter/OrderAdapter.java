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
import com.harusora.longcn.apporderfood.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderAdapter(Context context) {
        this.orders = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_checkout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bindData(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void addOrders(List<Order> orderList) {
        if (orderList != null && !orderList.isEmpty()) {
            orders.addAll(orderList);
            notifyDataSetChanged();
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView cartProductNameCO;

        private ImageView imageViewCO;
        private TextView tvQuantityCO;
        private TextView productOrderPriceCO;
        private TextView productPriceCO;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductNameCO = itemView.findViewById(R.id.cartProductNameCO);
            imageViewCO = itemView.findViewById(R.id.imageViewCO);
            tvQuantityCO = itemView.findViewById(R.id.tvQuantityCO);
            productOrderPriceCO = itemView.findViewById(R.id.productOrderPriceCO);
            productPriceCO = itemView.findViewById(R.id.productPriceCO);

        }

        public void bindData(Order order) {
            cartProductNameCO.setText(String.valueOf(order.getOrderCode()));
            tvQuantityCO.setText(String.valueOf(order.getTotalAmount()));
            productOrderPriceCO.setText(String.valueOf(order.getStatus()));
            productPriceCO.setText(String.valueOf(order.getOrderDate()));
        }
    }
}

