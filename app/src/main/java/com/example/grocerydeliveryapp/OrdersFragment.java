package com.example.grocerydeliveryapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    db = FirebaseFirestore.getInstance();
    auth = FirebaseAuth.getInstance();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_orders, container, false);
    progressBar = view.findViewById(R.id.ordersLoader);

    RecyclerView ordersRecyclerView = view.findViewById(R.id.ordersRec);
    ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    orderModelList = new ArrayList<>();
    orderAdapter = new OrderAdapter(getActivity(), orderModelList);
    ordersRecyclerView.setAdapter(orderAdapter);

    fetchOrders();

    return view;
  }

  private void fetchOrders() {
    if (auth.getCurrentUser() == null) {
      Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
      return;
    }

    String userId = auth.getCurrentUser().getUid();
    progressBar.setVisibility(View.VISIBLE);

    db.collection("orders")
      .whereEqualTo("userId", userId)
      .get()
      .addOnSuccessListener(queryDocumentSnapshots -> {
        orderModelList.clear();

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
          OrderModel order = new OrderModel();

          order.setOrderId(document.getId());
          order.setTotalPrice(document.getString("totalPrice"));
          order.setDate(document.getString("date"));
          order.setSortDate(document.getTimestamp("sortDate"));
          order.setTime(document.getString("time"));

          List<Map<String, Object>> productsList = (List<Map<String, Object>>) document.get("products");

          if (productsList != null) {
            List<ProductOrdersModel> orderProducts = new ArrayList<>();
            for (Map<String, Object> productMap : productsList) {
              String productId = (String) productMap.get("productId");
              String imageUrl = (String) productMap.get("imageUrl");
              String description = (String) productMap.get("description");
              String name = (String) productMap.get("name");

              int quantity = (productMap.get("quantity") != null) ? ((Number) productMap.get("quantity")).intValue() : 0;
              int price = (productMap.get("price") != null) ? ((Number) productMap.get("price")).intValue() : 0;

              ProductOrdersModel product = new ProductOrdersModel(productId, imageUrl, description, name, quantity, price);
              orderProducts.add(product);
            }
            order.setProducts(orderProducts);
          }
          orderModelList.add(order);
        }

        Collections.sort(orderModelList, (order1, order2) -> {
          if (order2.getSortDate() == null || order1.getSortDate() == null) {
            return 0;
          }
          return order2.getSortDate().compareTo(order1.getSortDate());
        });

        orderAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
      })
      .addOnFailureListener(e -> {
        Toast.makeText(getContext(), "Failed to fetch orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
      });
  }
}