package com.wstrater.commons.test;

import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class TestMatcher {

  public static Matcher<String> containsAllItems(Iterable<String> items) {
    return new ContainsAllItems(items);
  }
  
  public static Matcher<String> containsAllItems(String... items) {
    return containsAllItems(Arrays.asList(items));
  }
  
  private static class ContainsAllItems extends BaseMatcher<String> {
    
    private Iterable<String> items;
  
    private ContainsAllItems(Iterable<String> items) {
      this.items = items;
    }
  
    @Override
    public void describeMismatch(Object item, Description description) {
      description.appendText("all items not in ").appendValue(item);
    }
  
    public void describeTo(Description description) {
      description.appendValue(items);
    }
  
    @Override
    public boolean matches(Object value) {
      boolean ret = false;
  
      if (value != null && value instanceof String && items != null) {
        String test = (String)value;
        ret = true;
        for (String item : items) {
          ret = ret && test.contains(item);
        }
      }
      
      return ret;
    }
    
  }

}