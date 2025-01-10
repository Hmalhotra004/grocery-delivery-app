package com.example.grocerydeliveryapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.grocerydeliveryapp.adapters.OrderAdapter;
import com.example.grocerydeliveryapp.models.OrderModel;
import com.example.grocerydeliveryapp.models.ProductOrdersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdersFragment extends Fragment {

  private List<OrderModel> orderModelList;
  private OrderAdapter orderAdapter;
  private FirebaseFirestore db;
  private FirebaseAuth auth;
  private ProgressBar progressBar;

  public OrdersFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize Firebase
    db = FirebaseFirestore.getInstance();
    auth = FirebaseAuth.getInstance();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_orders, container, false);

    progressBar = view.findViewById(R.id.ordersLoader);

    RecyclerView ordersRecyclerView = view.findViewById(R.id.ordersRec);
    ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    orderModelList = new ArrayList<>();
    orderAdapter = new OrderAdapter(getActivity(), orderModelList);
    ordersRecyclerView.setAdapter(orderAdapter);

    // Fetch orders
    fetchOrders();

    return view;
  }

  private void fetchOrders() {
    String userId = auth.getCurrentUser().getUid();

    progressBar.setVisibility(View.VISIBLE);

    // Query orders for the current user
    db.collection("orders")
      .whereEqualTo("userId", userId)
      .get()
      .addOnSuccessListener(queryDocumentSnapshots -> {
        orderModelList.clear();

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
          OrderModel order = new OrderModel();

          // Parse order details
          order.setOrderId(document.getId());
          order.setTotalPrice(document.getString("totalPrice"));
          order.setDate(document.getString("date"));
          order.setTime(document.getString("time"));

          // Parse products
//          List<ProductOrdersModel> products = new ArrayList<>();
//          List<Object> productsList = (List<Object>) document.get("products");
//
//          if (productsList != null) {
//            for (Object productObject : productsList) {
//              if (productObject instanceof HashMap) {
//                @SuppressWarnings("unchecked")
//                HashMap<String, Object> productMap = (HashMap<String, Object>) productObject;
//
//                ProductOrdersModel product = new ProductOrdersModel();
//                product.setProductId((String) productMap.get("productId"));
//                product.setQuantity(((Long) productMap.get("quantity")).intValue());
//
//                products.add(product);
//              }
//            }
//          }
//
//          order.setProducts(products);

          // Add order to the list
          orderModelList.add(order);
        }

        // Notify the adapter of the changes
        orderAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
      })
      .addOnFailureListener(e -> {
        Toast.makeText(getContext(), "Failed to fetch orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
      });
  }
}