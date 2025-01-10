package com.example.grocerydeliveryapp.models;

import java.util.List;

public class OrderSummaryModel {

  public String totalPrice, orderId;
    public List<ProductOrdersModel> products;

  public OrderSummaryModel() {}

  public List<ProductOrdersModel> getProducts() {
    return products;
  }

  public void setProducts(List<ProductOrdersModel> products) {
    this.products = products;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
