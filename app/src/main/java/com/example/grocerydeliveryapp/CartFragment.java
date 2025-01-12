package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerydeliveryapp.adapters.CartAdapters;
import com.example.grocerydeliveryapp.models.CartModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CartFragment extends Fragment {

  public CartFragment() {
    // Required empty public constructor
  }

  public List<CartModel> cartModelList;
  public CartAdapters cartAdapters;
  public FirebaseFirestore db;
  public FirebaseAuth auth;
  public LinearLayout billDetails, cartMain;
  public ProgressBar loader;
  public TextView fallback,itemsTotalTextView,grandTotalTextView;
  public Button placeOrder;
  public double handlingCharge = 5.0;
  public double deliveryCharge = 25.0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    db = FirebaseFirestore.getInstance();
    auth = FirebaseAuth.getInstance();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cart, container, false);

    fallback = view.findViewById(R.id.fallbackCart);
    billDetails = view.findViewById(R.id.billDetailsL);
    cartMain = view.findViewById(R.id.cartMainL);
    loader = view.findViewById(R.id.cartLoader);

    placeOrder = view.findViewById(R.id.placeOrderBtn);

     itemsTotalTextView = view.findViewById(R.id.cartItemsTotal);
     grandTotalTextView = view.findViewById(R.id.grandTotal);

    RecyclerView cartRecyclerView = view.findViewById(R.id.cartItemRec);
    cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    cartModelList = new ArrayList<>();
    cartAdapters = new CartAdapters(getActivity(), cartModelList);

    cartRecyclerView.setAdapter(cartAdapters);

    loadCartItems();

    placeOrder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
          Intent intent = new Intent(getActivity(), LoginActivity.class);
          startActivity(intent);
          return;
        }

        if (cartModelList.isEmpty()) {
          Toast.makeText(getContext(), "Cart is empty.", Toast.LENGTH_SHORT).show();
          return;
        }

        List<Map<String, Object>> products = new ArrayList<>();
        double totalPrice = 0.0;

        // Prepare product data
        for (CartModel cartItem : cartModelList) {
          Map<String, Object> product = new HashMap<>();
          product.put("productId", cartItem.getProductId());
          product.put("name", cartItem.getName());
          product.put("description", cartItem.getDescription());
          product.put("price", cartItem.getPrice());
          product.put("imageUrl", cartItem.getImageUrl());
          product.put("quantity", cartItem.getQuantity());
          products.add(product);

          totalPrice += cartItem.getPrice() * cartItem.getQuantity();
        }

        double grandTotal = totalPrice + handlingCharge + deliveryCharge;

        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        String orderId = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(new Date());

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("sortDate",timestamp);
        order.put("date", currentDate);
        order.put("products", products);
        order.put("time", currentTime);
        order.put("totalPrice", String.valueOf(grandTotal));
        order.put("userId", userId);

        db.collection("orders").document(orderId)
          .set(order)
          .addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

            clearCart(userId);
          })
          .addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to place the order.", Toast.LENGTH_SHORT).show();
          });
      }
    });

    return view;
  }

  private void loadCartItems() {
    String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

    loader.setVisibility(View.VISIBLE);
    cartMain.setVisibility(View.GONE);

    if (userId == null) {
      Intent intent = new Intent(getActivity(), LoginActivity.class);
      startActivity(intent);
      return;
    }

    db.collection("cart")
      .whereEqualTo("userId", userId)
      .addSnapshotListener((snapshots, error) -> {
        if (error != null) {
          Toast.makeText(getContext(), "Failed to load cart items.", Toast.LENGTH_SHORT).show();
          return;
        }

        if (snapshots != null) {
          cartModelList.clear();
          double itemsTotal = 0.0;

          for (QueryDocumentSnapshot document : snapshots) {
            CartModel cartItem = document.toObject(CartModel.class);
            cartItem.setProductId(document.getId());
            cartModelList.add(cartItem);

            double price = cartItem.getPrice();
            int quantity = cartItem.getQuantity();
            itemsTotal += price * quantity;
          }

          cartAdapters.notifyDataSetChanged();
          loader.setVisibility(View.GONE);
          cartMain.setVisibility(View.VISIBLE);

          if (cartModelList.isEmpty()) {
            // Show fallback message and hide bill details
            fallback.setVisibility(View.VISIBLE);
            fallback.setText("No items in your cart.");
            billDetails.setVisibility(View.GONE);
          } else {
            // Hide fallback message and show bill details
            fallback.setVisibility(View.GONE);
            billDetails.setVisibility(View.VISIBLE);

            double grandTotal = itemsTotal + handlingCharge + deliveryCharge;
            itemsTotalTextView.setText(String.format("₹%.2f", itemsTotal));
            grandTotalTextView.setText(String.format("₹%.2f", grandTotal));
          }
        }
      });
  }

  private void clearCart(String userId) {
    db.collection("cart")
      .whereEqualTo("userId", userId)
      .get()
      .addOnSuccessListener(querySnapshot -> {
        for (QueryDocumentSnapshot document : querySnapshot) {
          document.getReference().delete();
        }

        cartModelList.clear();
        cartAdapters.notifyDataSetChanged();
        fallback.setVisibility(View.VISIBLE);
        billDetails.setVisibility(View.GONE);
      })
      .addOnFailureListener(e -> {
        Toast.makeText(getContext(), "Failed to clear the cart.", Toast.LENGTH_SHORT).show();
      });
  }
}
