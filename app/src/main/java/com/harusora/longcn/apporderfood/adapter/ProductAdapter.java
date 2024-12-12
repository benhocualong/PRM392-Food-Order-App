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
import com.harusora.longcn.apporderfood.model.Product;
import com.harusora.longcn.apporderfood.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    ArrayList<Product> productList;
    Context context;

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product model);
    }


    public ProductAdapter(Context context, OnItemClickListener listener) {
        this.listener = listener;
        this.productList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getName());
        holder.productPrice.setText(String.valueOf(productList.get(position).getPrice()));
        holder.productDescription.setText(productList.get(position).getDescription());
        holder.productCategory.setText(productList.get(position).getCategoryName());
        holder.productImage.setImageResource(Utils.getResourceIdFromDrawable(context.getApplicationContext(), productList.get(position).getImage()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(productList.get(position)));

    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void addProducts(List<Product> newProducts) {
        if (newProducts != null) {
            productList.addAll(newProducts);
            notifyDataSetChanged();
        }
    }

    public void updateList(List<Product> newList) {
        this.productList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productDescription, productCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgvProduct);
            productName = itemView.findViewById(R.id.tvName);
            productPrice = itemView.findViewById(R.id.tvPrice);
            productDescription = itemView.findViewById(R.id.tvDescription);
            productCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
