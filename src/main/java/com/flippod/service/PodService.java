package com.flippod.service;

import com.flippod.constants.Constants;
import com.flippod.model.BaseResponse;
import com.flippod.model.Pod;
import com.flippod.util.ApplicationUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PodService {
  @Autowired
  private ApplicationUtils applicationUtils;

  List<Pod> pods = new ArrayList<Pod>();

  public BaseResponse<List<Pod>> getAllPods() {
    String message = Constants.SUCCESS_MESSAGE;
    if(applicationUtils.isEmpty(pods)) {
      message = Constants.NO_DATA_FOUND;
    }
    return BaseResponse.<List<Pod>>builder().code(Constants.SUCCESS_CODE).message(message).response(pods).build();
  }

  public <T> BaseResponse<T> addPod(Pod pod) {
    if (pod.getName().isEmpty()) {
      throw new NullPointerException(Constants.BAD_INPUT);
    }
    String message = Constants.SUCCESS_MESSAGE;
    if(isPodAvailable(pod.getName())) {
      message = Constants.POD_ALREADY_AVAILABLE;
    }
    else {
      pods.add(pod);
    }
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(message).build();
  }

  public boolean isPodAvailable(String podName) {
    for(Pod pod: pods) {
      if(pod.getName().equalsIgnoreCase(podName)) {
        return true;
      }
    }
    return false;
  }
}
