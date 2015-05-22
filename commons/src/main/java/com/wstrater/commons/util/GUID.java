package com.wstrater.commons.util;

import java.util.UUID;

public abstract class GUID {

  public static String getGUID() {
    return UUID.randomUUID().toString();
  }

}