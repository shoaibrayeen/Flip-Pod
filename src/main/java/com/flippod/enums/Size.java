package com.flippod.enums;

public enum Size {
  LARGE("large"),
  SMALL("small");

  String size;

  Size(String size) {
    this.size = size;
  }

  public String getSize() {
    return size;
  }
}
