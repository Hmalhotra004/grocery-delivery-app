package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerydeliveryapp.adapters.CartAdapters;
import com.example.grocerydeliveryapp.models.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private String mParam1;
  private String mParam2;

  public CartFragment() {
    // Required empty public constructor
  }

  public static CartFragment newInstance(String param1, String param2) {
    CartFragment fragment = new CartFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  public List<CartModel> cartModelList;
  public CartAdapters cartAdapters;
  public FirebaseFirestore db;
  public FirebaseAuth auth;
  public LinearLayout billDetails;
  public TextView fallback,itemsTotalTextView,grandTotalTextView;
  public double handlingCharge = 5.0;
  public double deliveryCharge = 25.0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cart, container, false);

    db = FirebaseFirestore.getInstance();
    auth = FirebaseAuth.getInstance();

    fallback = view.findViewById(R.id.fallbackCart);
    billDetails = view.findViewById(R.id.billDetailsL);

     itemsTotalTextView = view.findViewById(R.id.cartItemsTotal);
     grandTotalTextView = view.findViewById(R.id.grandTotal);

    RecyclerView cartRecyclerView = view.findViewById(R.id.cartItemRec);
    cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    cartModelList = new ArrayList<>();
    cartAdapters = new CartAdapters(getActivity(), cartModelList);

    cartRecyclerView.setAdapter(cartAdapters);

    loadCartItems();

    return view;
  }

  private void loadCartItems() {
    String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

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

          double grandTotal = itemsTotal + handlingCharge + deliveryCharge;

          itemsTotalTextView.setText(String.format("₹"+ itemsTotal));
          grandTotalTextView.setText(String.format("₹"+ grandTotal));
        }
      });
  }
}
