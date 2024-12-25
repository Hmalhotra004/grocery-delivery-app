package com.example.grocerydeliveryapp.models;

public class PopularModel {
  String name;
  String imgUrl1,imgUrl2,imgUrl3,imgUrl4;

  public PopularModel(String name, String imgUrl1, String imgUrl2, String imgUrl3, String imgUrl4) {
    this.name = name;
    this.imgUrl1 = imgUrl1;
    this.imgUrl2 = imgUrl2;
    this.imgUrl3 = imgUrl3;
    this.imgUrl4 = imgUrl4;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImgUrl1() {
    return imgUrl1;
  }

  public void setImgUrl1(String imgUrl1) {
    this.imgUrl1 = imgUrl1;
  }

  public String getImgUrl2() {
    return imgUrl2;
  }

  public void setImgUrl2(String imgUrl2) {
    this.imgUrl2 = imgUrl2;
  }

  public String getImgUrl3() {
    return imgUrl3;
  }

  public void setImgUrl3(String imgUrl3) {
    this.imgUrl3 = imgUrl3;
  }

  public String getImgUrl4() {
    return imgUrl4;
  }

  public void setImgUrl4(String imgUrl4) {
    this.imgUrl4 = imgUrl4;
  }
}
