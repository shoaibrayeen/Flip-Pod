package com.flippod.constants;

public interface Endpoints {
  String FLIPPOD_BASE_URL = "/api/v1";
  String CUSTOMERS = "/customers";
  String LOCKERS = "/lockers";
  String FREE_LOCKERS = "/lockers/free";
  String POD_LOCKERS = "/lockers/{pod}";
  String GET_QUEUE_STATE = "/queue-state";
  String ALL_ORDERS = "/orders";
  String PLACE_ORDER = "/place-order";
  String RECEIVE_ORDER = "/receive-order";
  String ALL_DELIVERED_ORDERS = "/orders/delivered";
  String PODS = "/pods";
}
