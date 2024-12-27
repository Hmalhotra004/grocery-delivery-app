package com.example.grocerydeliveryapp.utils;

public class CheckItem {
  private int id;

  public CheckItem(int name) {
    this.id = name;
  }

  public static String getFile(int id) {
    String file;

    switch (id) {
      case 1:
        file = "fruits.json";
        break;

      case 2:
        file = "aata.json";
        break;

      case 3:
        file = "dairy.json";
        break;

      case 4:
        file = "bakery.json";
        break;

      case 5:
        file = "dry.json";
        break;

      case 6:
        file = "chick.json";
        break;

      case 7:
        file = "chips.json";
        break;

      case 8:
        file = "choco.json";
        break;

      case 9:
        file = "drinks.json";
        break;

      case 10:
        file = "tea.json";
        break;

      case 11:
        file = "instant.json";
        break;

      case 12:
        file = "ice.json";
        break;

      default:
        file = "Default.json";
        break;
    }

    return file;
  }
}
