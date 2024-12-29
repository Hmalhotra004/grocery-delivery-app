package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.grocerydeliveryapp.adapters.CartAdapters;
import com.example.grocerydeliveryapp.models.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
import java.util.ArrayList;
import java.util.List;

  private String mParam1;
  private String mParam2;
public class CartFragment extends Fragment {

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
  List<CartModel> cartModelList;
  CartAdapters cartAdapters;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }
  RecyclerView cartRecyclerView;
  TextView emptyCartMessage;

  FirebaseFirestore db;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_cart, container, false);
    View view = inflater.inflate(R.layout.fragment_cart, container, false);

    db = FirebaseFirestore.getInstance();

    cartRecyclerView = view.findViewById(R.id.cartItemRec);
//    emptyCartMessage = view.findViewById(R.id.emptyCartMessage);

    cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    cartModelList = new ArrayList<>();
    cartAdapters = new CartAdapters(getActivity(), cartModelList);

    cartRecyclerView.setAdapter(cartAdapters);


    return view;
  }
  }
}}
