package com.example.grocerydeliveryapp.models;

public class SnackModel {
  private String name;
  private int id;
  private String imageUrl1;

  public SnackModel(String name, String imageUrl1,int id) {
    this.name = name;
    this.imageUrl1 = imageUrl1;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
