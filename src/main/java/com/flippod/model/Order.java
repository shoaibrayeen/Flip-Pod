package com.flippod.model;

import com.flippod.enums.Size;
import com.flippod.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private String orderId;

  private String customerId;

  private String customerName;

  private String pod;

  private String lockerId;

  private Size orderSize;

  private Status orderStatus;
}
