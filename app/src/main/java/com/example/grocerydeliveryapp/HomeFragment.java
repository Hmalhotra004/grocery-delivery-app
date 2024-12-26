package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.adapters.GroceryKitchenAdapters;
import com.example.grocerydeliveryapp.adapters.PopularAdapters;
import com.example.grocerydeliveryapp.models.GroceryKitchen;
import com.example.grocerydeliveryapp.models.PopularModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  List<PopularModel> popularModelList;
  PopularAdapters popularAdapters;

  List<GroceryKitchen> groceryKitchens;
  GroceryKitchenAdapters groceryKitchenAdapters;
  RecyclerView PopRecyclerView, GroceryRecyclerView;

  public HomeFragment() {
    // Required empty public constructor
  }

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
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);

    // Initialize RecyclerView
    PopRecyclerView = view.findViewById(R.id.popularRec);
    GroceryRecyclerView = view.findViewById(R.id.GroceryKitchenRec);

    PopRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    GroceryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

    // Load data and set the adapter
    popularModelList = loadItemsFromJson("popularItems.json", new TypeToken<List<PopularModel>>() {}.getType());
    groceryKitchens = loadItemsFromJson("groceryItems.json", new TypeToken<List<GroceryKitchen>>() {}.getType());

    popularAdapters = new PopularAdapters(getActivity(), popularModelList);
    groceryKitchenAdapters = new GroceryKitchenAdapters(getActivity(), groceryKitchens);

    PopRecyclerView.setAdapter(popularAdapters);
    GroceryRecyclerView.setAdapter(groceryKitchenAdapters);

    return view;
  }

  // Generic method to load items from JSON based on the model type
  private <T> List<T> loadItemsFromJson(String fileName, Type typeOfT) {
    List<T> items = new ArrayList<>();
    try {
      // Open the JSON file from the assets
      InputStream inputStream = requireContext().getAssets().open(fileName);
      String json = convertStreamToString(inputStream);

      // Use Gson to parse the JSON into a list of the provided type
      Gson gson = new Gson();
      items = gson.fromJson(json, typeOfT);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return items;
  }

  // Convert InputStream to String
  private String convertStreamToString(InputStream is) {
    Scanner scanner = new Scanner(is);
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNext()) {
      stringBuilder.append(scanner.nextLine());
    }
    return stringBuilder.toString();
  }
}
