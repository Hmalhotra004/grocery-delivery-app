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
import com.example.grocerydeliveryapp.models.ViewAllModel;

import java.util.List;

public class ViewAllAdapters extends RecyclerView.Adapter<ViewAllAdapters.ViewHolder> {

  private Context context;
  private List<ViewAllModel> viewAllModelList;

  public ViewAllAdapters(Context context, List<ViewAllModel> viewAllModelList) {
    this.context = context;
    this.viewAllModelList = viewAllModelList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ViewAllModel currentItem = viewAllModelList.get(position);

    holder.name.setText(currentItem.getName());
    holder.price.setText(String.format("₹%d", currentItem.getPrice()));
    holder.amt.setText(currentItem.getAmt());
    holder.imageView.setImageResource(getImageResource(currentItem.getImageUrl()));
  }

  private int getImageResource(String imageName) {
    // Get the resource ID by the image name
    return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
  }

  @Override
  public int getItemCount() {
    return viewAllModelList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView name, price ,amt;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.productImg);
      name = itemView.findViewById(R.id.productName);
      price = itemView.findViewById(R.id.productPrice);
      amt = itemView.findViewById(R.id.productAmt);
    }
  }
}
