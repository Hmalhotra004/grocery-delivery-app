package com.example.grocerydeliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerydeliveryapp.R;
import com.example.grocerydeliveryapp.models.CartModel;

import java.util.List;

public class CartAdapters extends RecyclerView.Adapter<CartAdapters.ViewHolder> {

  private Context context;
  private List<CartModel> cartItems;

  public CartAdapters(Context context, List<CartModel> cartItems) {
    this.context = context;
    this.cartItems = cartItems;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate the layout for cart items
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Get the current cart item
    CartModel currentItem = cartItems.get(position);

    // Bind data to the views
    holder.name.setText(currentItem.getName());
    holder.price.setText("₹" + currentItem.getPrice());
    holder.qty.setText(String.valueOf(currentItem.getQuantity()));
    holder.description.setText(currentItem.getDescription());

    holder.img.setImageResource(getImageResource(currentItem.getImageUrl()));

    holder.cartPLus.setOnClickListener(v -> {
      currentItem.setQuantity(currentItem.getQuantity() + 1);
      notifyItemChanged(position);
    });

    holder.cartMinus.setOnClickListener(v -> {
      if (currentItem.getQuantity() > 1) {
        currentItem.setQuantity(currentItem.getQuantity() - 1);
        notifyItemChanged(position);
      }
    });
  }

  private int getImageResource(String imageName) {
    // Get the resource ID by the image name
    return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
  }

  @Override
  public int getItemCount() {
    // Return the size of the cart item list
    return cartItems.size();
  }

  // ViewHolder class to hold the views for each cart item
  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView img;
    TextView name, price, qty, description;
    ImageButton cartPLus, cartMinus;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      // Initialize the views
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
