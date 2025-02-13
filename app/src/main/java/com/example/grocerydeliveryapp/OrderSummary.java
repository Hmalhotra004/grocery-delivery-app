package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummary extends AppCompatActivity {

  private List<ProductOrdersModel> products;
  private OrderSummaryAdapter orderSummaryAdapter;
  private FirebaseFirestore db;
  private FirebaseAuth auth;

  private double handlingCharge = 5.0;
  private double deliveryCharge = 25.0;

  private RecyclerView orderSummaryRecyclerView;
  private TextView itemsTotalTextView, deliveryChargeTextView, grandTotalTextView, orderSumItemsTextView;

  LinearLayout orderSummary;
  private ProgressBar progressBar;
  private Button repeatOrderButton;

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
    progressBar = findViewById(R.id.orderSumLoader);
    orderSummary = findViewById(R.id.orderSummary);
    orderSumItemsTextView = findViewById(R.id.orderSumItems);
    repeatOrderButton = findViewById(R.id.repeatOrderBtn);

    orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    products = new ArrayList<>();
    orderSummaryAdapter = new OrderSummaryAdapter(OrderSummary.this, products);
    orderSummaryRecyclerView.setAdapter(orderSummaryAdapter);

    fetchOrderSummary(orderId);

    repeatOrderButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        repeatOrder(orderId);
      }
    });

  }

  private void fetchOrderSummary(String orderId) {
    progressBar.setVisibility(View.VISIBLE);
    orderSummary.setVisibility(View.GONE);

    db.collection("orders")
      .document(orderId)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          DocumentSnapshot documentSnapshot = task.getResult();
          if (documentSnapshot != null && documentSnapshot.exists()) {
            List<Map<String, Object>> productsList =
              (List<Map<String, Object>>) documentSnapshot.get("products");

            if (productsList != null) {
              products.clear();
              for (Map<String, Object> productMap : productsList) {
                ProductOrdersModel product = new ProductOrdersModel(
                  (String) productMap.get("productId"),
                  (String) productMap.get("imageUrl"),
                  (String) productMap.get("description"),
                  (String) productMap.get("name"),
                  ((Number) productMap.get("quantity")).intValue(),
                  ((Number) productMap.get("price")).intValue()
                );
                products.add(product);
              }
              orderSummaryAdapter.notifyDataSetChanged();
              calculateTotals();
              if(products.size() == 1){
                orderSumItemsTextView.setText((products.size() + " Item in this order"));
              } else {
                orderSumItemsTextView.setText((products.size() + " Items in this order"));
              }
              progressBar.setVisibility(View.GONE);
              orderSummary.setVisibility(View.VISIBLE);
            }
          } else {
            Log.e("OrderSummary", "Document not found or empty");
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

  private void repeatOrder(String orderId) {
    String userId = auth.getCurrentUser().getUid();
    db.collection("orders")
      .document(orderId)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          DocumentSnapshot documentSnapshot = task.getResult();
          if (documentSnapshot != null && documentSnapshot.exists()) {
            List<Map<String, Object>> productsList =
              (List<Map<String, Object>>) documentSnapshot.get("products");

            if (productsList != null) {
              for (Map<String, Object> productMap : productsList) {
                Map<String, Object> cartProduct = new HashMap<>();
                cartProduct.put("productId", productMap.get("productId"));
                cartProduct.put("imageUrl", productMap.get("imageUrl"));
                cartProduct.put("description", productMap.get("description"));
                cartProduct.put("name", productMap.get("name"));
                cartProduct.put("quantity", productMap.get("quantity"));
                cartProduct.put("price", productMap.get("price"));
                cartProduct.put("userId", userId);

                // Add the product to the cart in Firestore
                db.collection("cart")
                  .add(cartProduct)
                  .addOnSuccessListener(documentReference ->
                    Log.d("RepeatOrder", "Product added to cart: " + documentReference.getId()))
                  .addOnFailureListener(e ->
                    Log.e("RepeatOrder", "Failed to add product to cart", e));
              }
              Toast.makeText(this,"Order added to cart",Toast.LENGTH_LONG).show();
            }
          } else {
            Log.e("OrderSummary", "Document not found or empty");
          }
        } else {
          Log.e("OrderSummary", "Error fetching order data", task.getException());
        }
      });
  }
}
