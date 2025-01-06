package com.example.grocerydeliveryapp.models;

import java.util.List;

public class OrderModel {
  public String orderId, totalPrice, date, time;
  private List<ProductOrdersModel> products;

  public OrderModel() {}

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public List<ProductOrdersModel> getProducts() {
    return products;
  }

  public void setProducts(List<ProductOrdersModel> products) {
    this.products = products;
  }
}
