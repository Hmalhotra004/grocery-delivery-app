package com.example.grocerydeliveryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.adapters.ViewAllAdapters;
import com.example.grocerydeliveryapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

  List<ViewAllModel> viewAllModelList;
  ViewAllAdapters viewAllAdapters;

  RecyclerView viewAllRec;
  Toolbar title;

  FirebaseFirestore db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_all);

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    String productType = getIntent().getStringExtra("type");
    String pageTitle = getIntent().getStringExtra("title");

    title = findViewById(R.id.viewAlltoolbar);
    setSupportActionBar(title);
    getSupportActionBar().setTitle(pageTitle);

    viewAllRec = findViewById(R.id.viewAllRec);
    viewAllRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    db = FirebaseFirestore.getInstance();

    fetchProductsByType(productType);
  }

  private void fetchProductsByType(String productType) {
    viewAllModelList = new ArrayList<>();

    db.collection("products")
      .whereEqualTo("type", productType)
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              ViewAllModel product = document.toObject(ViewAllModel.class);

              // Set the productId to the Firestore document ID
              product.setProductId(document.getId());

              viewAllModelList.add(product);
            }

            viewAllAdapters = new ViewAllAdapters(ViewAllActivity.this, viewAllModelList);
            viewAllRec.setAdapter(viewAllAdapters);
          } else {
            task.getException().printStackTrace();
          }
        }
      });
  }
}
