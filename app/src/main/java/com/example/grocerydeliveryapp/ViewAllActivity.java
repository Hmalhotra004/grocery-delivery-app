package com.example.grocerydeliveryapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.adapters.ViewAllAdapters;
import com.example.grocerydeliveryapp.models.ViewAllModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewAllActivity extends AppCompatActivity {

  List<ViewAllModel> viewAllModelList;
  ViewAllAdapters viewAllAdapters;

  RecyclerView viewAllRec;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_all);

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    viewAllRec = findViewById(R.id.viewAllRec);
    viewAllRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    // Load items from JSON file
    viewAllModelList = loadItemsFromJson("Fruits.json", new TypeToken<List<ViewAllModel>>() {}.getType());

    // Set up the RecyclerView adapter
    viewAllAdapters = new ViewAllAdapters(this, viewAllModelList);
    viewAllRec.setAdapter(viewAllAdapters);
  }

  // Load items from a JSON file in the assets folder
  private <T> List<T> loadItemsFromJson(String fileName, Type typeOfT) {
    List<T> items = new ArrayList<>();
    try {
      // Open the JSON file from the assets folder
      InputStream inputStream = getAssets().open(fileName);
      String json = convertStreamToString(inputStream);

      // Parse the JSON into a list of the specified type
      Gson gson = new Gson();
      items = gson.fromJson(json, typeOfT);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return items;
  }

  // Convert InputStream to a String
  private String convertStreamToString(InputStream is) {
    Scanner scanner = new Scanner(is);
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNext()) {
      stringBuilder.append(scanner.nextLine());
    }
    return stringBuilder.toString();
  }
}
