package com.example.grocerydeliveryapp.models;

public class ProductOrdersModel {
  public String productId;
  public int quantity;

  public ProductOrdersModel() {}

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
