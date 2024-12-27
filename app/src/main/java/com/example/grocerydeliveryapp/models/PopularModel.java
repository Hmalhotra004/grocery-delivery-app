package com.example.grocerydeliveryapp.models;

public class PopularModel {
  private String name;
  private int id;
  private String imageUrl1, imageUrl2, imageUrl3, imageUrl4;

  public PopularModel(String name, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4,int id) {
    this.name = name;
    this.imageUrl1 = imageUrl1;
    this.imageUrl2 = imageUrl2;
    this.imageUrl3 = imageUrl3;
    this.imageUrl4 = imageUrl4;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  // Getters and Setters
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

  public String getImageUrl2() {
    return imageUrl2;
  }

  public void setImageUrl2(String imageUrl2) {
    this.imageUrl2 = imageUrl2;
  }

  public String getImageUrl3() {
    return imageUrl3;
  }

  public void setImageUrl3(String imageUrl3) {
    this.imageUrl3 = imageUrl3;
  }

  public String getImageUrl4() {
    return imageUrl4;
  }

  public void setImageUrl4(String imageUrl4) {
    this.imageUrl4 = imageUrl4;
  }
}
