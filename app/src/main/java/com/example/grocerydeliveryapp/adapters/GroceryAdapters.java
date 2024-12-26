package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.GroceryModel;

import java.util.List;

public class GroceryAdapters extends RecyclerView.Adapter<GroceryAdapters.ViewHolder> {

  private Context context;
  private List<GroceryModel> groceryKitchenList;

  public GroceryAdapters(Context context, List<GroceryModel> groceryKitchenList) {
    this.context = context;
    this.groceryKitchenList = groceryKitchenList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate the item layout for the RecyclerView
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_card, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Get the current item at the given position
    GroceryModel currentItem = groceryKitchenList.get(position);

    // Set the image resource and name for the current item
    holder.kitchenImg.setImageResource(getImageResource(currentItem.getImageUrl1()));
    holder.name.setText(currentItem.getName());
  }

  private int getImageResource(String imageName) {
    // Get the resource ID by the image name
    return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
  }

  @Override
  public int getItemCount() {
    // Return the number of items in the list
    return groceryKitchenList.size();
  }

  // ViewHolder class to hold references to the views for each item
  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView kitchenImg;
    TextView name;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      // Initialize the ImageView and TextView references
      kitchenImg = itemView.findViewById(R.id.categoryImg);
      name = itemView.findViewById(R.id.categoryName);
    }
  }
}
