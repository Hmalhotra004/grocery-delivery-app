package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.ProductOrdersModel;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

  private final Context context;
  private final List<ProductOrdersModel> productList;

  // Constructor
  public OrderSummaryAdapter(Context context, List<ProductOrdersModel> productList) {
    this.context = context;
    this.productList = productList;
  }

  @NonNull
  @Override
  public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate the item layout for each product in the order summary
    View view = LayoutInflater.from(context).inflate(R.layout.order_summary_item, parent, false);
    return new OrderSummaryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
    ProductOrdersModel product = productList.get(position);

    if (product != null) {
      // Set product details
      holder.productNameTextView.setText(product.getName());
//      holder.productPriceTextView.setText("₹" + product.getPrice());
//      holder.productQuantityTextView.setText(product.getDescription() + product.getQuantity());
//      holder.orderSumImg.setImageResource(getImageResource(product.getImageUrl()));
    }
  }

  private int getImageResource(String imageName) {
    return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
  }

  @Override
  public int getItemCount() {
    return (productList != null) ? productList.size() : 0;
  }

  // ViewHolder class for holding item views
  public static class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
    TextView productNameTextView, productPriceTextView, productQuantityTextView;
    ImageView orderSumImg;

    public OrderSummaryViewHolder(@NonNull View itemView) {
      super(itemView);
      orderSumImg = itemView.findViewById(R.id.orderSumImg);
      productNameTextView = itemView.findViewById(R.id.orderSumName);
      productPriceTextView = itemView.findViewById(R.id.orderSumPrice);
      productQuantityTextView = itemView.findViewById(R.id.orderSumDesp);
    }
  }
}
