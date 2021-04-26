package com.flippod.service;

import com.flippod.constants.Constants;
import com.flippod.enums.Status;
import com.flippod.model.BaseResponse;
import com.flippod.model.Order;
import com.flippod.model.ReceiveOrder;
import com.flippod.util.ApplicationUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired
  private ApplicationUtils applicationUtils;

  @Autowired
  private LockerService lockerService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private PodService podService;

  List<Order> orders = new ArrayList<Order>();

  public BaseResponse<List<Order>> getAllOrders() {
    String message = returnResponseMessage(orders);
    return BaseResponse.<List<Order>>builder().code(Constants.SUCCESS_CODE).message(message).response(orders).build();
  }

  public <T> BaseResponse<T> addOrder(Order order) {
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(placeOrder(order)).build();
  }

  public <T> BaseResponse<T> getOrder(ReceiveOrder order) {
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(receiveOrder(order)).build();
  }

  public BaseResponse<List<Order>> getAllPlacedOrders() {
    List<Order> placedOrders = getOrders(Status.PLACED.getStatus());
    String message = returnResponseMessage(placedOrders);
    return BaseResponse.<List<Order>>builder().code(Constants.SUCCESS_CODE).message(message).response(placedOrders).build();
  }

  public BaseResponse<List<Order>> getAllDeliveredOrders() {
    List<Order> deliveredOrders = getOrders(Status.DELIVERED.getStatus());
    String message = returnResponseMessage(deliveredOrders);
    return BaseResponse.<List<Order>>builder().code(Constants.SUCCESS_CODE).message(message).response(deliveredOrders).build();
  }

  private List<Order> getOrders(String status) {
    List<Order> orderList = new ArrayList<Order>();
    for (Order order : orders) {
      if(order.getOrderStatus().getStatus().equalsIgnoreCase(status)) {
        orderList.add(order);
      }
    }
    return orderList;
  }

  private String receiveOrder(ReceiveOrder receiveOrder) {
    String customerId = customerService.returnCustomerId(receiveOrder.getCustomerId());
    if(customerId.isEmpty()) {
      return Constants.BAD_CUSTOMER;
    }
    String pod = receiveOrder.getPod();
    if (podService.isPodAvailable(pod)) {
      if(isOrderFound(pod, customerId)) {
        freeLocker(pod, customerId);
      }
      else {
        return Constants.NO_ORDER;
      }
    }
    else {
      return Constants.NO_POD_FOUND;
    }
    placeQueuedOrders();
    return Constants.RECEIVED_ORDER + pod;
  }

  private String placeOrder(Order order) {
    String customerId = customerService.returnCustomerId(order.getCustomerName());
    if(customerId.isEmpty()) {
      return Constants.BAD_CUSTOMER;
    }
    if(!podService.isPodAvailable(order.getPod())) {
      return Constants.NO_POD_FOUND;
    }
    order.setCustomerId(customerId);
    order.setOrderId(applicationUtils.getUUID());
    if(lockerService.isLockerFree(order)) {
      order.setOrderStatus(Status.PLACED);
      orders.add(order);
    }
    else {
      order.setOrderStatus(Status.PENDING);
      lockerService.addQueue(order);
      return Constants.PENDING_MESSAGE;
    }
    return Constants.SUCCESS_MESSAGE;
  }

  private boolean isOrderFound(String pod, String customerId) {
    if(orders.isEmpty()) {
      return false;
    }
    for (Order order : orders) {
      if(order.getPod().equalsIgnoreCase(pod) && order.getCustomerId().equalsIgnoreCase(customerId)
          && order.getOrderStatus().getStatus().equalsIgnoreCase(Status.PLACED.getStatus())) {
        return true;
      }
    }
    return false;
  }

  private void freeLocker(String pod, String customerId) {
    for (Order order : orders) {
      if(order.getPod().equalsIgnoreCase(pod) && order.getCustomerId().equalsIgnoreCase(customerId)
          && order.getOrderStatus().getStatus().equalsIgnoreCase(Status.PLACED.getStatus())) {
        lockerService.freeLocker(order.getLockerId());
        order.setOrderStatus(Status.DELIVERED);
      }
    }
  }

  private String returnResponseMessage(List list) {
    if(applicationUtils.isEmpty(list)) {
      return Constants.NO_DATA_FOUND;
    }
    return Constants.SUCCESS_MESSAGE;
  }

  private void placeQueuedOrders() {
    if(lockerService.isQueueEmpty()) {
      return;
    }
    Order order = lockerService.getFirstQueue();
    while(placeOrder(order).equalsIgnoreCase(Constants.SUCCESS_MESSAGE)) {
     lockerService.removeFirstQueue();
     order = lockerService.getFirstQueue();
    }
    order = lockerService.getLastQueue();
    lockerService.removeLastQueue();
    lockerService.addFirstQueue(order);
  }
}
