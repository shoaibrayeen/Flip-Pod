package com.flippod.enums;

public enum Status {
  PLACED("placed"),
  DELIVERED("delivered"),
  PENDING("pending");


  String status;

  Status(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
