package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.ProductOrdersModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

  private Context context;
  private List<ProductOrdersModel> productList;

  // Constructor
  public ProductAdapter(Context context, List<ProductOrdersModel> productList) {
    this.context = context;
    this.productList = productList;
  }

  @NonNull
  @Override
  public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.order_product_image, parent, false);
    return new ProductViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
    // Get the current product
    ProductOrdersModel product = productList.get(position);

    // Here you can load product images
    // For example, using Glide or Picasso to load the product image
    // Glide.with(context).load(product.getImageUrl()).into(holder.productImageView);

    // Placeholder for product image
    holder.productImageView.setImageResource(R.drawable.placeholder);
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  // ViewHolder for Product
  public static class ProductViewHolder extends RecyclerView.ViewHolder {
    ImageView productImageView;

    public ProductViewHolder(@NonNull View itemView) {
      super(itemView);
      productImageView = itemView.findViewById(R.id.ivProductImage);
    }
  }
}
