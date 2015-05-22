package com.wstrater.commons.util;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class GUIDTest {
  
  @Test
  public void testGUID() {
    Set<String> guids = new HashSet<>();
    for (int xx = 0; xx < 100; xx++) {
      String guid = GUID.getGUID();
      Assert.assertNotNull("No GUID generated", guid);
      Assert.assertFalse("Duplicate GUID", guids.contains(guid));
      guids.add(guid);
    }
  }

}