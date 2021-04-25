package com.flippod.controller;

import com.flippod.constants.Endpoints;
import com.flippod.model.BaseResponse;
import com.flippod.model.Customer;
import com.flippod.service.CustomerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoints.FLIPPOD_BASE_URL)
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @GetMapping(Endpoints.CUSTOMERS)
  public BaseResponse<List<Customer>> getAllCustomers() {
    return customerService.getAllCustomers();
  }

  @PostMapping(Endpoints.CUSTOMERS)
  public <T> BaseResponse<T> addCustomer(@RequestBody Customer customer) {
    return customerService.addCustomers(customer);
  }
}
