package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.ViewAllActivity;
import com.example.grocerydeliveryapp.models.PopularModel;

import java.util.List;

public class PopularAdapters extends RecyclerView.Adapter<PopularAdapters.ViewHolder> {

  private Context context;
  private List<PopularModel> popularModelList;

  public PopularAdapters(Context context, List<PopularModel> popularModelList) {
    this.context = context;
    this.popularModelList = popularModelList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate the item layout for the RecyclerView
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Get the current item at the given position
    PopularModel currentItem = popularModelList.get(position);

    // Set the image resources using the current item
    holder.popImg1.setImageResource(getImageResource(currentItem.getImageUrl1()));
    holder.popImg2.setImageResource(getImageResource(currentItem.getImageUrl2()));
    holder.popImg3.setImageResource(getImageResource(currentItem.getImageUrl3()));
    holder.popImg4.setImageResource(getImageResource(currentItem.getImageUrl4()));

    // Set the name of the item
    holder.name.setText(currentItem.getName());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String CurrentName = currentItem.getName();
        String file = checkItem(CurrentName);
        Intent intent = new Intent(context, ViewAllActivity.class);
        intent.putExtra("file", file);
        intent.putExtra("title", currentItem.getName());
        context.startActivity(intent);
      }
    });
  }

  private int getImageResource(String imageName) {
    // Get the resource ID by the image name
    return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
  }

  @Override
  public int getItemCount() {
    // Return the number of items in the list
    return popularModelList.size();
  }

  // ViewHolder class to hold references to the views for each item
  public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView popImg1, popImg2, popImg3, popImg4;
    TextView name;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      // Initialize the ImageView and TextView references
      popImg1 = itemView.findViewById(R.id.popImg1);
      popImg2 = itemView.findViewById(R.id.popImg2);
      popImg3 = itemView.findViewById(R.id.popImg3);
      popImg4 = itemView.findViewById(R.id.popImg4);
      name = itemView.findViewById(R.id.popName);
    }
  }

  private String checkItem(String name) {
    String file;

    switch (name) {
      case "Drinks & Juices":
        file = "drinks.json";
        break;

      case "Chips & Namkeen":
        file = "chips.json";
        break;

      case "Dairy, Bread & Eggs":
        file = "dairy.json";
        break;

      case "Bakery & Biscuits":
        file = "bakery.json";
        break;


      case "Vegetables & Fruits":
        file = "fruits.json";
        break;

      case "Sweets & Chocolates":
        file = "choco.json";
        break;

      default:
        file = "Default.json";
        break;
    }

    return file;
  }

}
