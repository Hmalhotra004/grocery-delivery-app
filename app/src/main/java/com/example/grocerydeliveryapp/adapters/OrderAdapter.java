package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.OrderSummary;
import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.OrderModel;
import com.example.grocerydeliveryapp.models.ProductOrdersModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

  private final Context context;
  private final List<OrderModel> orderList;

  // Constructor
  public OrderAdapter(Context context, List<OrderModel> orderList) {
    this.context = context;
    this.orderList = orderList;
  }

  @NonNull
  @Override
  public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate the item layout for each order
    View view = LayoutInflater.from(context).inflate(R.layout.orders_card, parent, false);
    return new OrderViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
    // Get the current order
    OrderModel order = orderList.get(position);

    if (order != null) {
      // Set order details
      holder.dateTextView.setText(order.getDate()+",");
      holder.timeTextView.setText(order.getTime());
      holder.totalPriceTextView.setText("₹" + order.getTotalPrice());

      // Set up nested RecyclerView for products
//      List<ProductOrdersModel> productList = order.getProducts();
//      ProductAdapter productAdapter = new ProductAdapter(context, productList);
//
//      if (holder.productsRecyclerView.getLayoutManager() == null) {
//        holder.productsRecyclerView.setLayoutManager(
//          new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        );
//      }
//      holder.productsRecyclerView.setAdapter(productAdapter);

      holder.orderCard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(context, OrderSummary.class);
          intent.putExtra("orderId", order.getOrderId());
          context.startActivity(intent);
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return (orderList != null) ? orderList.size() : 0;
  }

  // ViewHolder class for holding item views
  public static class OrderViewHolder extends RecyclerView.ViewHolder {
    TextView dateTextView, timeTextView, totalPriceTextView;
    RecyclerView productsRecyclerView;
    CardView orderCard;

    public OrderViewHolder(@NonNull View itemView) {
      super(itemView);
      dateTextView = itemView.findViewById(R.id.orderDate);
      timeTextView = itemView.findViewById(R.id.orderTime);
      totalPriceTextView = itemView.findViewById(R.id.orderTotal);
      orderCard = itemView.findViewById(R.id.orderCard);
//      productsRecyclerView = itemView.findViewById(R.id.orderProductsImg);
    }
  }
}
