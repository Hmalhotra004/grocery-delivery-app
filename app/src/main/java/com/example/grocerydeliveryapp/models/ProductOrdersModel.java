package com.example.grocerydeliveryapp.models;

public class ProductOrdersModel {
  public String productId, imageUrl,description, name;
  public int quantity, price;

  public ProductOrdersModel() {}

//  public ProductOrdersModel(String productId, int quantity,String imageUrl) {
//    this.productId = productId;
//    this.quantity = quantity;
//    this.imageUrl = imageUrl;
//  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
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
