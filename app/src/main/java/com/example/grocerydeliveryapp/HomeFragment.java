package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.adapters.GroceryAdapters;
import com.example.grocerydeliveryapp.adapters.PopularAdapters;
import com.example.grocerydeliveryapp.adapters.SnackAdapters;
import com.example.grocerydeliveryapp.models.GroceryModel;
import com.example.grocerydeliveryapp.models.PopularModel;
import com.example.grocerydeliveryapp.models.SnackModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

  List<PopularModel> popularModelList;
  PopularAdapters popularAdapters;

  List<GroceryModel> groceryKitchens;
  GroceryAdapters groceryKitchenAdapters;

  List<SnackModel> snackList;
  SnackAdapters snackAdapters;

  RecyclerView PopRecyclerView, GroceryRecyclerView, SnackRecyclerView;

  FirebaseFirestore db;

  public HomeFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);

    db = FirebaseFirestore.getInstance();

    PopRecyclerView = view.findViewById(R.id.popularRec);
    GroceryRecyclerView = view.findViewById(R.id.GroceryKitchenRec);
    SnackRecyclerView = view.findViewById(R.id.SnackRec);

    PopRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    GroceryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    SnackRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

    popularModelList = new ArrayList<>();
    groceryKitchens = new ArrayList<>();
    snackList = new ArrayList<>();

    popularAdapters = new PopularAdapters(getActivity(), popularModelList);
    groceryKitchenAdapters = new GroceryAdapters(getActivity(), groceryKitchens);
    snackAdapters = new SnackAdapters(getActivity(), snackList);

    PopRecyclerView.setAdapter(popularAdapters);
    GroceryRecyclerView.setAdapter(groceryKitchenAdapters);
    SnackRecyclerView.setAdapter(snackAdapters);


    fetchPopularItems();
    fetchGroceryItems();
//    fetchSnackItems();

    return view;
  }

  private void fetchPopularItems() {
    db.collection("popular").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          try {
            for (QueryDocumentSnapshot doc : task.getResult()) {
              PopularModel popularModel = doc.toObject(PopularModel.class);
              popularModelList.add(popularModel);
            }
            popularAdapters.notifyDataSetChanged();
          } catch (Exception e) {
            Log.e("Firestore Error", "Error parsing data", e);
          }
        } else {
          Log.e("Firestore Error", "Failed to fetch data", task.getException());
        }
      }
    });
  }

  private void fetchGroceryItems() {
    db.collection("grocery").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          try {
            for (QueryDocumentSnapshot doc : task.getResult()) {
              GroceryModel groceryModel = doc.toObject(GroceryModel.class);
              groceryKitchens.add(groceryModel);
            }
            groceryKitchenAdapters.notifyDataSetChanged();
          } catch (Exception e) {
            Log.e("Firestore Error", "Error parsing data", e);
          }
        } else {
          Log.e("Firestore Error", "Failed to fetch data", task.getException());
        }
      }
    });
  }

  private void fetchSnackItems() {
    db.collection("snacks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          try {
            for (QueryDocumentSnapshot doc : task.getResult()) {
              SnackModel snackModel = doc.toObject(SnackModel.class);
              snackList.add(snackModel);
            }
            snackAdapters.notifyDataSetChanged();
          } catch (Exception e) {
            Log.e("Firestore Error", "Error parsing data", e);
          }
        } else {
          Log.e("Firestore Error", "Failed to fetch data", task.getException());
        }
      }
    });
  }
}
