package com.flippod.controller;

import com.flippod.constants.Endpoints;
import com.flippod.model.BaseResponse;
import com.flippod.model.Locker;
import com.flippod.model.Order;
import com.flippod.service.LockerService;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoints.FLIPPOD_BASE_URL)
public class LockerController {

  @Autowired
  private LockerService lockerService;

  @GetMapping(Endpoints.LOCKERS)
  public BaseResponse<List<Locker>> getAllLockers() {
    return lockerService.getAllLockers();
  }

  @PostMapping(Endpoints.LOCKERS)
  public <T> BaseResponse<T> addLocker(@RequestBody Locker locker) {
    return lockerService.addLocker(locker);
  }

  @GetMapping(Endpoints.GET_QUEUE_STATE)
  public BaseResponse<LinkedList<Order>> getQueue() {
    return lockerService.getAllQueue();
  }

  @GetMapping(Endpoints.POD_LOCKERS)
  public BaseResponse<List<Locker>> getAllPodLockers(@PathVariable String pod) {
    return lockerService.getAllPodLockers(pod);
  }

  @GetMapping(Endpoints.FREE_LOCKERS)
  public BaseResponse<List<Locker>> getFreeLockers() {
    return lockerService.getFreeLockers();
  }
}
