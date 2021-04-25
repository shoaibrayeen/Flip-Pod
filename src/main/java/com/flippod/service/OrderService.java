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

  List<Order> placedOrders = new ArrayList<Order>();
  List<Order> deliveredOrders = new ArrayList<Order>();

  public BaseResponse<List<Order>> getAllOrders() {
    String message = Constants.SUCCESS_MESSAGE;
    if(applicationUtils.isEmpty(placedOrders)) {
      message = Constants.NO_DATA_FOUND;
    }
    return BaseResponse.<List<Order>>builder().code(Constants.SUCCESS_CODE).message(message).response(placedOrders).build();
  }

  public <T> BaseResponse<T> addOrder(Order order) {
    String customerId = customerService.returnCustomerId(order.getCustomerName());
    if(customerId.isEmpty()) {
      return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(Constants.BAD_CUSTOMER).build();
    }
    if(!podService.isPodAvailable(order.getPod())) {
      return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(Constants.NO_POD_FOUND).build();
    }
    String message = Constants.SUCCESS_MESSAGE;
    order.setCustomerId(customerId);
    order.setOrderId(applicationUtils.getUUID());
    if(lockerService.isLockerFree(order)) {
      order.setOrderStatus(Status.PLACED);
      placedOrders.add(order);
    }
    else {
      order.setOrderStatus(Status.PENDING);
      lockerService.addQueue(order);
      message = Constants.PENDING_MESSAGE;
    }
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(message).build();
  }

  public <T> BaseResponse<T> getOrder(ReceiveOrder receiveOrder) {
    String pod = receiveOrder.getPod();
    String message = Constants.RECEIVED_ORDER + pod;
    String customerId = customerService.returnCustomerId(receiveOrder.getCustomerId());
    if(customerId.isEmpty()) {
      message = Constants.BAD_CUSTOMER;
    }
    else {
      if (podService.isPodAvailable(pod)) {
        if(isOrderFound(pod, customerId)) {
          freeAllLockersPodCustomerOrders(pod, customerId);
        }
        else {
          message = Constants.NO_ORDER;
        }
      }
      else {
        message = Constants.NO_POD_FOUND;
      }
    }
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(message).build();
  }

  public boolean isOrderFound(String pod, String customerId) {
    if(placedOrders.isEmpty()) {
      return false;
    }
    for (Order order : placedOrders) {
      if(order.getPod().equalsIgnoreCase(pod) && order.getCustomerId().equalsIgnoreCase(customerId)) {
        return true;
      }
    }
    return false;
  }

  public void freeAllLockersPodCustomerOrders(String pod, String customerId) {
    for (Order order : placedOrders) {
      if(order.getPod().equalsIgnoreCase(pod) && order.getCustomerId().equalsIgnoreCase(customerId)) {
        lockerService.freeAllLockers(order.getLockerId());
        order.setOrderStatus(Status.DELIVERED);
        placedOrders.remove(order);
        deliveredOrders.add(order);
      }
    }
  }

  public BaseResponse<List<Order>> getAllDeliveredOrders() {
    String message = Constants.SUCCESS_MESSAGE;
    if(applicationUtils.isEmpty(deliveredOrders)) {
      message = Constants.NO_DATA_FOUND;
    }
    return BaseResponse.<List<Order>>builder().code(Constants.SUCCESS_CODE).message(message).response(deliveredOrders).build();
  }
}
