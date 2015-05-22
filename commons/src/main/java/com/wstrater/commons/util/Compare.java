package com.wstrater.commons.util;

public abstract class Compare {

  /*
   * Convenience method for maps that contain Strings but return Object.
   */
  public static boolean isBlank(Object value) {
    return value == null || (value instanceof String && isBlank((String)value));
  }

  public static boolean isBlank(String value) {
    return value == null || value.trim().length() < 1;
  }

  /*
   * Convenience method for maps that contain Strings but return Object.
   */
  public static boolean isNotBlank(Object value) {
    return value != null && value instanceof String && isNotBlank((String)value);
  }

  public static boolean isNotBlank(String value) {
    return !isBlank(value);
  }

}