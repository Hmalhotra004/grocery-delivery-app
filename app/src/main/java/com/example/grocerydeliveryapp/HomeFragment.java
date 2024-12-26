package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.adapters.PopularAdapters;
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
  RecyclerView recyclerView;

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
    recyclerView = view.findViewById(R.id.popularRec);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

    // Load data and set the adapter
    popularModelList = loadItemsFromJson();
    popularAdapters = new PopularAdapters(getActivity(),popularModelList);
    recyclerView.setAdapter(popularAdapters);

    return view;
  }

  private List<PopularModel> loadItemsFromJson() {
    List<PopularModel> items = new ArrayList<>();
    try {
      InputStream inputStream = requireContext().getAssets().open("popularItems.json");
      String json = convertStreamToString(inputStream);

      Gson gson = new Gson();
      Type listType = new TypeToken<List<PopularModel>>() {}.getType();
      items = gson.fromJson(json, listType);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return items;
  }


  private String convertStreamToString(InputStream is) {
    Scanner scanner = new Scanner(is);
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNext()) {
      stringBuilder.append(scanner.nextLine());
    }
    return stringBuilder.toString();
  }
}
