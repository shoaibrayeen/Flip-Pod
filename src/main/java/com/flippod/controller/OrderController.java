package com.flippod.controller;

import com.flippod.constants.Endpoints;
import com.flippod.model.BaseResponse;
import com.flippod.model.Order;
import com.flippod.model.ReceiveOrder;
import com.flippod.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoints.FLIPPOD_BASE_URL)
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping(Endpoints.ALL_ORDERS)
  public BaseResponse<List<Order>> getAllOrders() {
    return orderService.getAllOrders();
  }

  @PostMapping(Endpoints.PLACE_ORDER)
  public <T> BaseResponse<T> addOrder(@RequestBody Order order) {
    return orderService.addOrder(order);
  }

  @PostMapping(Endpoints.RECEIVE_ORDER)
  public <T> BaseResponse<T> getOrder(@RequestBody ReceiveOrder receiveOrder) {
    return  orderService.getOrder(receiveOrder);
  }

  @GetMapping(Endpoints.ALL_DELIVERED_ORDERS)
  public BaseResponse<List<Order>> getAllDeliveredOrders() {
    return orderService.getAllDeliveredOrders();
  }

  @GetMapping(Endpoints.ALL_PLACED_ORDERS)
  public BaseResponse<List<Order>> getAllPlacedOrders() {
    return orderService.getAllPlacedOrders();
  }
}
