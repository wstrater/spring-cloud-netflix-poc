package com.wstrater.commons.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class TestMatcherTest {
  
  @Test
  public void testContainsAllItems() {
    String[] items = new String[] {"Abc", "Def", "Ghi", "Jkl"};
    StringBuilder buf = new StringBuilder();    
    for (String item : items) {
      buf.append("Hello ").append(item).append("! ");
    }

    String test = buf.toString();
    Assert.assertTrue(String.format("'%s' does not containsAllItems: '%s'", test, Arrays.toString(items)), 
      TestMatcher.containsAllItems(items).matches(test));

    test = test.replaceAll(items[0], "XXX");
    Assert.assertFalse(String.format("'%s' does containsAllItems: '%s'", test, Arrays.toString(items)), 
      TestMatcher.containsAllItems(items).matches(test));
  }

}