package com.example.grocerydeliveryapp.models;

public class ProductOrdersModel {
  public String productId, imageUrl;
  public int quantity;

  public ProductOrdersModel() {}

  public ProductOrdersModel(String productId, int quantity,String imageUrl) {
    this.productId = productId;
    this.quantity = quantity;
    this.imageUrl = imageUrl;
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

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
