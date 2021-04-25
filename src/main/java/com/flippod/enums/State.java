package com.flippod.enums;

public enum State {
  LOCKED("locked"),
  UNLOCKED("unlocked");

  String state;

  State(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }
}
