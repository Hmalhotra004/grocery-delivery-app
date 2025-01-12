package com.example.grocerydeliveryapp.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class OrderModel {
  public String orderId, totalPrice, date, time;
  public Timestamp sortDate;
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

  public Timestamp getSortDate() {
    return sortDate;
  }

  public void setSortDate(Timestamp sortDate) {
    this.sortDate = sortDate;
  }

    public List<ProductOrdersModel> getProducts() {
    return products;
  }

  public void setProducts(List<ProductOrdersModel> products) {
    this.products = products;
  }
}
