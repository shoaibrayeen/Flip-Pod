package com.flippod.util;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUtils {

  public static String getUUID() {
    UUID uniqueKey = UUID.randomUUID();
    String uniqueID = uniqueKey.toString().replace("-", "");
    return uniqueID;
  }

  public static boolean isEmpty(List object) {
    return object.isEmpty();
  }
}
