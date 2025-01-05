package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    this.db = FirebaseFirestore.getInstance();
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

    String userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

    if (userId != null) {
      db.collection("cart")
        .whereEqualTo("userId", userId)
        .whereEqualTo("productId", currentItem.getProductId())
        .addSnapshotListener((value, error) -> {
          if (error != null) {
            Toast.makeText(context, "Error fetching cart data.", Toast.LENGTH_SHORT).show();
            return;
          }

          if (value != null && !value.isEmpty()) {
            holder.add.setVisibility(View.GONE);
            holder.quantityAdjuster.setVisibility(View.VISIBLE);

            DocumentSnapshot document = value.getDocuments().get(0);
            int currentQuantity = document.getLong("quantity").intValue();
            holder.qty.setText(String.valueOf(currentQuantity));
          } else {
            holder.add.setVisibility(View.VISIBLE);
            holder.quantityAdjuster.setVisibility(View.GONE);
          }
        });
    }

    holder.add.setOnClickListener(view -> {
      if (userId != null) {
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("userId", userId);
        cartItem.put("productId", currentItem.getProductId());
        cartItem.put("name", currentItem.getName());
        cartItem.put("imageUrl", currentItem.getImageUrl());
        cartItem.put("description", currentItem.getAmt());
        cartItem.put("quantity", 1);
        cartItem.put("price", currentItem.getPrice());

        db.collection("cart")
          .add(cartItem)
          .addOnFailureListener(e ->
            Toast.makeText(context, "Failed to add item to cart.", Toast.LENGTH_SHORT).show());
      } else {
        Toast.makeText(context, "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show();
      }
    });

    holder.minusBtn.setOnClickListener(v -> updateQuantityInCart(holder, currentItem, userId, -1));
    holder.plusBtn.setOnClickListener(v -> updateQuantityInCart(holder, currentItem, userId, 1));
  }

  private void updateQuantityInCart(ViewHolder holder, ViewAllModel currentItem, String userId, int quantityChange) {
    db.collection("cart")
      .whereEqualTo("userId", userId)
      .whereEqualTo("productId", currentItem.getProductId())
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful() && !task.getResult().isEmpty()) {
          DocumentSnapshot document = task.getResult().getDocuments().get(0);
          int currentQuantity = document.getLong("quantity").intValue();
          int newQuantity = currentQuantity + quantityChange;

          if (newQuantity <= 0) {
            db.collection("cart").document(document.getId())
              .delete()
              .addOnFailureListener(e -> {
                Toast.makeText(context, "Failed to remove item from cart.", Toast.LENGTH_SHORT).show();
              });
          } else {
            // Update Firestore with the new quantity if quantity is greater than zero
            Map<String, Object> updatedCartItem = new HashMap<>();
            updatedCartItem.put("quantity", newQuantity);

            db.collection("cart").document(document.getId())
              .update(updatedCartItem)
              .addOnSuccessListener(aVoid -> {
                holder.qty.setText(String.valueOf(newQuantity));
              })
              .addOnFailureListener(e -> {
                Toast.makeText(context, "Failed to update quantity.", Toast.LENGTH_SHORT).show();
              });
          }
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
    TextView name, price, amt,qty;
    Button add;
    Button minusBtn, plusBtn;
    LinearLayout quantityAdjuster;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.productImg);
      name = itemView.findViewById(R.id.productName);
      price = itemView.findViewById(R.id.productPrice);
      amt = itemView.findViewById(R.id.productAmt);
      add = itemView.findViewById(R.id.addToCartBtn);
      minusBtn = itemView.findViewById(R.id.minusBtn);
      plusBtn = itemView.findViewById(R.id.plusBtn);
      qty= itemView.findViewById(R.id.quantity);
      quantityAdjuster = itemView.findViewById(R.id.quantityAdjuster);
    }
  }
}
