package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

  private List<PopularModel> popularModelList;
  private PopularAdapters popularAdapters;

  private List<GroceryModel> groceryKitchens;
  private GroceryAdapters groceryKitchenAdapters;

  private List<SnackModel> snackList;
  private SnackAdapters snackAdapters;

  private RecyclerView popRecyclerView, groceryRecyclerView, snackRecyclerView;
  private ProgressBar homeLoader;
  private ScrollView homeContent;

  private FirebaseFirestore db;

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

    homeLoader = view.findViewById(R.id.homeLoader);
    homeContent = view.findViewById(R.id.homeContent);

    popRecyclerView = view.findViewById(R.id.popularRec);
    groceryRecyclerView = view.findViewById(R.id.GroceryKitchenRec);
    snackRecyclerView = view.findViewById(R.id.SnackRec);

    popRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    groceryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    snackRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

    popularModelList = new ArrayList<>();
    groceryKitchens = new ArrayList<>();
    snackList = new ArrayList<>();

    popularAdapters = new PopularAdapters(getActivity(), popularModelList);
    groceryKitchenAdapters = new GroceryAdapters(getActivity(), groceryKitchens);
    snackAdapters = new SnackAdapters(getActivity(), snackList);

    popRecyclerView.setAdapter(popularAdapters);
    groceryRecyclerView.setAdapter(groceryKitchenAdapters);
    snackRecyclerView.setAdapter(snackAdapters);

    showLoading(true);

    fetchPopularItems();
    fetchGroceryItems();
    fetchSnackItems();

    return view;
  }

  private void fetchPopularItems() {
    db.collection("popular").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          for (QueryDocumentSnapshot doc : task.getResult()) {
            PopularModel popularModel = doc.toObject(PopularModel.class);
            popularModelList.add(popularModel);
          }
          popularAdapters.notifyDataSetChanged();
        }
        checkDataFetchComplete();
      }
    });
  }

  private void fetchGroceryItems() {
    db.collection("grocery").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          for (QueryDocumentSnapshot doc : task.getResult()) {
            GroceryModel groceryModel = doc.toObject(GroceryModel.class);
            groceryKitchens.add(groceryModel);
          }
          groceryKitchenAdapters.notifyDataSetChanged();
        }
        checkDataFetchComplete();
      }
    });
  }

  private void fetchSnackItems() {
    db.collection("snacks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          for (QueryDocumentSnapshot doc : task.getResult()) {
            SnackModel snackModel = doc.toObject(SnackModel.class);
            snackList.add(snackModel);
          }
          snackAdapters.notifyDataSetChanged();
        }
        checkDataFetchComplete();
      }
    });
  }

  private void checkDataFetchComplete() {
    if (!popularModelList.isEmpty() && !groceryKitchens.isEmpty() && !snackList.isEmpty()) {
      showLoading(false);
    }
  }

  private void showLoading(boolean isLoading) {
    homeLoader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    homeContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
  }
}