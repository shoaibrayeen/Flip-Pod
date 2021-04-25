package com.flippod.controller;

import com.flippod.constants.Endpoints;
import com.flippod.model.BaseResponse;
import com.flippod.model.Pod;
import com.flippod.service.PodService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoints.FLIPPOD_BASE_URL)
public class PodController {

  @Autowired
  private PodService podService;

  @GetMapping(Endpoints.PODS)
  public BaseResponse<List<Pod>> getAllPods() {
    return podService.getAllPods();
  }

  @PostMapping(Endpoints.PODS)
  public <T> BaseResponse<T> addPod(@RequestBody Pod pod) {
    return podService.addPod(pod);
  }
}
