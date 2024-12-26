package com.example.grocerydeliveryapp.models;

public class SnackModel {
  private String name;
  private String imageUrl1;

  public SnackModel(String name, String imageUrl1) {
    this.name = name;
    this.imageUrl1 = imageUrl1;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageUrl1() {
    return imageUrl1;
  }

  public void setImageUrl1(String imageUrl1) {
    this.imageUrl1 = imageUrl1;
  }
}
