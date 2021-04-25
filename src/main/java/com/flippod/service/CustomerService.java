package com.flippod.service;

import com.flippod.constants.Constants;
import com.flippod.model.BaseResponse;
import com.flippod.model.Customer;
import com.flippod.util.ApplicationUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  @Autowired
  private ApplicationUtils applicationUtils;

  List<Customer> customerList = new ArrayList<Customer>();

  public BaseResponse<List<Customer>> getAllCustomers() {
    String message = Constants.SUCCESS_MESSAGE;
    if(applicationUtils.isEmpty(customerList)) {
      message = Constants.NO_DATA_FOUND;
    }
    return BaseResponse.<List<Customer>>builder().code(Constants.SUCCESS_CODE).message(message).response(customerList).build();
  }

  public <T> BaseResponse<T> addCustomers(Customer customer) {
    customer.setCustomerId(applicationUtils.getUUID());
    customerList.add(customer);
    return BaseResponse.<T>builder().code(Constants.SUCCESS_CODE).message(Constants.SUCCESS_MESSAGE).build();
  }

  public String returnCustomerId(String customerName) {
    for (Customer customer : customerList) {
      if (customer.getName().equalsIgnoreCase(customerName)) {
        return customer.getCustomerId();
      }
    }
    return "";
  }
}
