package com.flippod.service;

import com.flippod.constants.Constants;
import com.flippod.model.BaseResponse;
import com.flippod.model.Locker;
import com.flippod.model.Order;
import com.flippod.util.ApplicationUtils;
import com.flippod.enums.State;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockerService {

  @Autowired
  private ApplicationUtils applicationUtils;

  @Autowired
  private PodService podService;

  LinkedList<Order> queue = new LinkedList<>();
  List<Locker> lockers = new ArrayList<Locker>();

  public BaseResponse<List<Locker>> getAllLockers() {
    String message = Constants.SUCCESS_MESSAGE;
    if(applicationUtils.isEmpty(lockers)) {
      message = Constants.NO_DATA_FOUND;
    }
    return BaseResponse.<List<Locker>>builder().code(Constants.SUCCESS_CODE).message(message).response(lockers).build();
  }

  public <T> BaseResponse<T> addLocker(Locker locker) {
    String message = Constants.SUCCESS_MESSAGE;
    if(!podService.isPodAvailable(locker.getPod())) {
      message = Constants.NO_POD_FOUND;
    }
    else {
      locker.setLockerId(applicationUtils.getUUID());
      locker.setLockerState(State.UNLOCKED);
      lockers.add(locker);
    }
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(message).build();
  }

  public BaseResponse<LinkedList<Order>> getAllQueue() {
    String message = Constants.SUCCESS_MESSAGE;
    if(queue.isEmpty()) {
      message = Constants.QUEUE_IS_EMPTY;
    }
    return BaseResponse.<LinkedList<Order>>builder().code(Constants.SUCCESS_CODE).message(message).response(queue).build();
  }

  public void addQueue(Order order) {
    queue.add(order);
  }

  public boolean isQueueEmpty() {
    return queue.isEmpty();
  }

  public Order getQueuedOrder() {
    if(queue.isEmpty()) {
      return null;
    }
    return queue.getFirst();
  }

  public void removeQueuedOrder() {
    queue.removeFirst();
  }

  public boolean isLockerFree(Order order) {
    for (Locker locker : lockers) {
      if (locker.getLockerState().getState().equalsIgnoreCase(State.UNLOCKED.getState()) &&
          locker.getLockerSize().getSize().equalsIgnoreCase(order.getOrderSize().getSize()) &&
          locker.getPod().equalsIgnoreCase(order.getPod())) {
        locker.setLockerState(State.LOCKED);
        order.setLockerId(locker.getLockerId());
        return true;
      }
    }
    return false;
  }

  public BaseResponse<List<Locker>> getAllPodLockers(String pod) {
    String message = Constants.SUCCESS_MESSAGE;
    List<Locker> podLockers = new ArrayList<Locker>();
    if (!podService.isPodAvailable(pod)) {
      message = Constants.NO_POD_FOUND;
    }
    else {
      for (Locker locker : lockers) {
        if(locker.getPod().equalsIgnoreCase(pod)) {
          podLockers.add(locker);
        }
      }
      if(applicationUtils.isEmpty(podLockers)) {
        message = Constants.NO_DATA_FOUND;
      }
    }
    return BaseResponse.<List<Locker>>builder().code(Constants.SUCCESS_CODE).message(message).response(podLockers).build();
  }

  public void freeLocker(String lockerId) {
    for (Locker locker : lockers) {
      if(locker.getLockerId().equalsIgnoreCase(lockerId)) {
        locker.setLockerState(State.UNLOCKED);
      }
    }
  }

  public BaseResponse<List<Locker>> getFreeLockers() {
    String message = Constants.SUCCESS_MESSAGE;
    List<Locker> freeLockers = new ArrayList<Locker>();
    for (Locker locker : lockers) {
      if(locker.getLockerState().getState().equalsIgnoreCase(State.UNLOCKED.getState())) {
        freeLockers.add(locker);
      }
    }
    if(applicationUtils.isEmpty(freeLockers)) {
      message = Constants.NO_DATA_FOUND;
    }
    return BaseResponse.<List<Locker>>builder().code(Constants.SUCCESS_CODE).message(message).response(freeLockers).build();
  }
}
