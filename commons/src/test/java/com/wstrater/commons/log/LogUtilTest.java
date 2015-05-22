package com.wstrater.commons.log;

import org.junit.Assert;
import org.junit.Test;

public class LogUtilTest {
  
  @Test
  public void testLocalHost() {
    Assert.assertNotNull("Expectign localhost", LogUtil.getLocalHost());
  }
  
  @Test
  public void testStart() {
    String[] startKeys = new String[] { LogUtil.HOST_NAME, LogUtil.START_TIME, LogUtil.THREAD_NAME, LogUtil.TRANSACTION_ID };

    LogUtil.clearMDC();
    for (String key : startKeys) {
      Assert.assertNull("Found after clear: " + key, LogUtil.getMDC(key));
    }

    LogUtil.markMDCStart();
    for (String key : startKeys) {
      Assert.assertNotNull("Expecting to find: " + key, LogUtil.getMDC(key));
    }

    LogUtil.clearMDC();
    for (String key : startKeys) {
      Assert.assertNull("Found after clear: " + key, LogUtil.getMDC(key));
    }
    
    String testKey = "XXXXX";
    LogUtil.setMDC(testKey, testKey);
    Assert.assertNotNull("Expecting to find before start: " + testKey, LogUtil.getMDC(testKey));
   
    LogUtil.markMDCStart(false);
    Assert.assertNotNull("Expecting to find after start: " + testKey, LogUtil.getMDC(testKey));
  }

  @Test
  public void testTransactionId() {
    LogUtil.clearMDC();
    Assert.assertNull("Found after clear: " + LogUtil.TRANSACTION_ID, LogUtil.getMDC(LogUtil.TRANSACTION_ID));
    
    LogUtil.getMDCTransactionId(false);
    Assert.assertNull("Found after get: " + LogUtil.TRANSACTION_ID, LogUtil.getMDC(LogUtil.TRANSACTION_ID));
    
    LogUtil.getMDCTransactionId();
    Assert.assertNotNull("Found after get: " + LogUtil.TRANSACTION_ID, LogUtil.getMDC(LogUtil.TRANSACTION_ID));
  }
  
}