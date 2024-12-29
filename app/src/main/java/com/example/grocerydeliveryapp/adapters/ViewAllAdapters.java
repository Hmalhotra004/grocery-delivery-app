package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.ViewAllModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllAdapters extends RecyclerView.Adapter<ViewAllAdapters.ViewHolder> {

  private Context context;
  private List<ViewAllModel> viewAllModelList;

  // Firestore instance
  private FirebaseFirestore db;

  public ViewAllAdapters(Context context, List<ViewAllModel> viewAllModelList) {
    this.context = context;
    this.viewAllModelList = viewAllModelList;
    this.db = FirebaseFirestore.getInstance(); // Initialize Firestore
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

    holder.add.setOnClickListener(view -> {
      // Get the current user's ID
      String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

      if (userId != null) {
        // Create a map for the cart item
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("userId", userId);
//        cartItem.put("productId", currentItem.getProductId());
        cartItem.put("name",currentItem.getName());
        cartItem.put("imageUrl",currentItem.getImageUrl());
        cartItem.put("desp",currentItem.getAmt());
        cartItem.put("quantity", 1); // Default quantity
        cartItem.put("price", currentItem.getPrice());

        // Add to Firestore
        db.collection("cart")
          .add(cartItem)
          .addOnSuccessListener(documentReference ->
            Toast.makeText(context, "Item added to cart!", Toast.LENGTH_SHORT).show())
          .addOnFailureListener(e ->
            Toast.makeText(context, "Failed to add item to cart.", Toast.LENGTH_SHORT).show());
      } else {
        Toast.makeText(context, "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show();
      }
    });
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
    TextView name, price, amt;
    Button add;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.productImg);
      name = itemView.findViewById(R.id.productName);
      price = itemView.findViewById(R.id.productPrice);
      amt = itemView.findViewById(R.id.productAmt);
      add = itemView.findViewById(R.id.addToCartBtn);
    }
  }
}
