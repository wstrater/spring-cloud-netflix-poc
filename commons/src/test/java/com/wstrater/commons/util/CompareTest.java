package com.wstrater.commons.util;

import org.junit.Assert;
import org.junit.Test;

public class CompareTest {
  
  public final static String[] BLANKS = new String[] {null, "", "  ", "\t"};
  public final static String[] NOT_BLANKS = new String[] {"Text", " Padded "};

  @Test
  public void testBlank() {
    for (String test : BLANKS) {
      Assert.assertTrue(String.format("Should be isBlank: %s", test), Compare.isBlank(test));
    }
    for (String test : NOT_BLANKS) {
      Assert.assertFalse(String.format("Shouldn't be isBlank: %s", test), Compare.isBlank(test));
    }
  }
  
  @Test
  public void testNotBlank() {
    for (String test : BLANKS) {
      Assert.assertFalse(String.format("Shouldn't be isNotBlank: %s", test), Compare.isNotBlank(test));
    }
    for (String test : NOT_BLANKS) {
      Assert.assertTrue(String.format("Should be isNotBlank: %s", test), Compare.isNotBlank(test));
    }
  }

}