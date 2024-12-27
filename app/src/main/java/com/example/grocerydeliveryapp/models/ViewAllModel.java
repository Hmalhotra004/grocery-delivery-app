package com.example.grocerydeliveryapp.models;

public class ViewAllModel {
  private String name;
  private int price;
  private String imageUrl;
  private String amt;


  public ViewAllModel(String name, int price, String imageUrl,String amt) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.amt = amt;
  }

  public String getAmt() {
    return amt;
  }

  public void setAmt(String amt) {
    this.amt = amt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
