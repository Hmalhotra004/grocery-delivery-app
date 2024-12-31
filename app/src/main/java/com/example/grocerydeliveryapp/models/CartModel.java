package com.example.grocerydeliveryapp.models;

public class CartModel {
  public String name, description,productId;
  public double price;
  public int quantity;
  public String imageUrl;

  public CartModel() {}
  // Constructor

//  public CartModel(String name, double price, int quantity, String imageUrl,String description, String productId) {
//    this.name = name;
//    this.price = price;
//    this.quantity = quantity;
//    this.imageUrl = imageUrl;
//    this.description = description;
//    this.productId = productId;
//  }


  // Getters and setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }
}
