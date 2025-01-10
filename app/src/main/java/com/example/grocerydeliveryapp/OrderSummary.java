package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.adapters.OrderSummaryAdapter;
import com.example.grocerydeliveryapp.models.OrderSummaryModel;
import com.example.grocerydeliveryapp.models.ProductOrdersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderSummary extends AppCompatActivity {

  private List<ProductOrdersModel> products;
  private OrderSummaryAdapter orderSummaryAdapter;
  private FirebaseFirestore db;
  private FirebaseAuth auth;

  private double handlingCharge = 5.0;
  private double deliveryCharge = 25.0;

  private RecyclerView orderSummaryRecyclerView;
  private TextView itemsTotalTextView, deliveryChargeTextView, grandTotalTextView;
//  private Button repeatOrderButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_summary);

    db = FirebaseFirestore.getInstance();
    auth = FirebaseAuth.getInstance();

    String orderId = getIntent().getStringExtra("orderId");

    orderSummaryRecyclerView = findViewById(R.id.orderSumRec);
    itemsTotalTextView = findViewById(R.id.cartItemsTotal);
    deliveryChargeTextView = findViewById(R.id.deliveryCharge);
    grandTotalTextView = findViewById(R.id.grandTotal);
//    repeatOrderButton = findViewById(R.id.repeatOrderBtn);

    orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    products = new ArrayList<>();
    orderSummaryAdapter = new OrderSummaryAdapter(OrderSummary.this, products);
    orderSummaryRecyclerView.setAdapter(orderSummaryAdapter);

    fetchOrderSummary(orderId);

  }

  private void fetchOrderSummary(String orderId) {
    db.collection("orders")
      .document(orderId)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          DocumentSnapshot documentSnapshot = task.getResult();
          if (documentSnapshot != null && documentSnapshot.exists()) {

            List<ProductOrdersModel> productsList = (List<ProductOrdersModel>) documentSnapshot.get("products");
            if (productsList != null) {
              products.clear();
              products.addAll(productsList);

              orderSummaryAdapter.notifyDataSetChanged();

              calculateTotals();
            }
          }
        } else {
          Log.e("OrderSummary", "Error fetching order data", task.getException());
        }
      });
  }

  private void calculateTotals() {
    double itemsTotal = 0.0;

    for (ProductOrdersModel product : products) {
      itemsTotal += product.getPrice() * product.getQuantity();
    }

    itemsTotalTextView.setText("₹" + itemsTotal);
    deliveryChargeTextView.setText("₹" + deliveryCharge);

    double grandTotal = itemsTotal + handlingCharge + deliveryCharge;
    grandTotalTextView.setText("₹" + grandTotal);
  }
}
