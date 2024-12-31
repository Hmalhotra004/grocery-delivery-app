package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.CartModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapters extends RecyclerView.Adapter<CartAdapters.ViewHolder> {

  private Context context;
  private List<CartModel> cartItems;
  private FirebaseFirestore db;

  public CartAdapters(Context context, List<CartModel> cartItems) {
    this.context = context;
    this.cartItems = cartItems;
    this.db = FirebaseFirestore.getInstance();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    CartModel currentItem = cartItems.get(position);

    // Bind data to the views
    holder.name.setText(currentItem.getName());
    holder.qty.setText(String.valueOf(currentItem.getQuantity()));
    holder.description.setText(currentItem.getDescription());

    // Calculate total price for the current item
    double totalPrice = currentItem.getPrice() * currentItem.getQuantity();
    holder.price.setText("₹" + totalPrice);

    holder.img.setImageResource(getImageResource(currentItem.getImageUrl()));

    holder.cartPLus.setOnClickListener(v -> {
      int newQuantity = currentItem.getQuantity() + 1;
      currentItem.setQuantity(newQuantity);
      updateCartItem(currentItem, position);
    });

    holder.cartMinus.setOnClickListener(v -> {
      int newQuantity = currentItem.getQuantity() - 1;
      if (newQuantity > 0) {
        currentItem.setQuantity(newQuantity);
        updateCartItem(currentItem, position);
      } else {
        removeCartItem(currentItem, position);
      }
    });
  }

  private int getImageResource(String imageName) {
    return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
  }

  private void updateCartItem(CartModel cartItem, int position) {
    db.collection("cart").document(cartItem.getProductId()) // Ensure `cartItem` has a unique `id` field
      .update("quantity", cartItem.getQuantity())
      .addOnSuccessListener(aVoid -> {
        notifyItemChanged(position);
      })
      .addOnFailureListener(e -> Toast.makeText(context, "Failed to update cart.", Toast.LENGTH_SHORT).show());
  }

  private void removeCartItem(CartModel cartItem, int position) {
    db.collection("cart").document(cartItem.getProductId())
      .delete()
      .addOnSuccessListener(aVoid -> {
        cartItems.remove(position);
        notifyItemRemoved(position);
      })
      .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove item from cart.", Toast.LENGTH_SHORT).show());
  }

  @Override
  public int getItemCount() {
    return cartItems.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView img;
    TextView name, price, qty, description;
    ImageButton cartPLus, cartMinus;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      img = itemView.findViewById(R.id.cartImg);
      name = itemView.findViewById(R.id.cartName);
      price = itemView.findViewById(R.id.cartPrice);
      qty = itemView.findViewById(R.id.cartQty);
      description = itemView.findViewById(R.id.cartDesp);
      cartPLus = itemView.findViewById(R.id.cartPLus);
      cartMinus = itemView.findViewById(R.id.cartMinus);
    }
  }
}
