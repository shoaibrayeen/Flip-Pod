package com.flippod.model;

import com.flippod.enums.Size;
import com.flippod.enums.State;
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
public class Locker {

  private String lockerId;

  private String pod;

  private Size lockerSize;

  private State lockerState;
}
