package com.wstrater.commons.log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {

  private Map<String, String> nameMap = new HashMap<>();

  @Before
  public void beforeTest() {
    nameMap.put("ACTION", "action");
    nameMap.put("CLASS_NAME", "className");
    nameMap.put("CLIENT_IP_ADDRESS", "clientIP");
    nameMap.put("COOKIES", "cookies");
    nameMap.put("HOST_NAME", "hostName");
    nameMap.put("LINE_NUMBER", "lineNumber");
    nameMap.put("METHOD_NAME", "methodName");
    nameMap.put("OUTPUT_TYPE", "outputType");
    nameMap.put("OUTPUT_VALUE", "outputValue");
    nameMap.put("REFERENCE_ID", "referenceId");
    nameMap.put("REFERER", "referer");
    nameMap.put("REQUEST_ID", "requestId");
    nameMap.put("REQUEST_TYPE", "requestType");
    nameMap.put("REQUEST_URI", "requestURI");
    nameMap.put("RESPONSE_TYPE", "responseType");
    nameMap.put("SERVLET_ACTION", "servletAction");
    nameMap.put("SESSION_ID", "sessionId");
    nameMap.put("SOAP_ACTION", "soapAction");
    nameMap.put("SOAP_REQUEST_ID", "soapRequestId");
    nameMap.put("SOAP_RESPONSE_ID", "soapResponseId");
    nameMap.put("SOAP_SUCCESS", "soapSuccess");
    nameMap.put("SOAP_TIME", "soapTime");
    nameMap.put("SOAP_URL", "soapUrl");
    nameMap.put("STACK_TRACE", "stackTrace");
    nameMap.put("START_TIME", "startTime");
    nameMap.put("THREAD_NAME", "threadName");
    nameMap.put("TRANSACTION_ID", "transactionId");
    nameMap.put("USERNAME", "userName");
    nameMap.put("USER_AGENT", "userAgent");
    nameMap.put("USER_ID", "userId");
    nameMap.put("TRACKING_ID", "trackingId");
  }

  @Test
  public void testStartMessage() {
    String testValue = "testVALUE";

    String[] startKeys = new String[] { LogUtil.HOST_NAME, LogUtil.THREAD_NAME, LogUtil.TRANSACTION_ID };
    String[] setKeys = new String[] { LogUtil.CLIENT_IP_ADDRESS, LogUtil.REFERER, LogUtil.REQUEST_URI, LogUtil.REFERENCE_ID,
        LogUtil.SESSION_ID, LogUtil.TRACKING_ID, LogUtil.USER_AGENT };
    String[][] combinedKeys = new String[][] { startKeys, setKeys };

    LogUtil.markMDCStart(true);
    for (String key : setKeys) {
      LogUtil.setMDC(key, key);
    }

    for (String[] keys : combinedKeys) {
      for (String key : keys) {
        Assert.assertNotNull(String.format("MDC expecting value for: %%s", key), LogUtil.getMDC(key));
      }
    }

    String message = "This is a test StartMessage";
    StartMessage startMessage = (StartMessage)StartMessage.builder(message).putValue(testValue, testValue).build();
    Assert.assertEquals(message, startMessage.getMessage());
    Assert.assertEquals(LogUtil.REFERENCE_ID, startMessage.getReferenceId());

    String logged = startMessage.toString();
    System.out.println(logged);
    for (String[] keys : combinedKeys) {
      for (String key : keys) {
        String pattern = String.format(",%s=%s", nameMap.get(key),  
            key == LogUtil.HOST_NAME ? LogUtil.getLocalHost() : 
            key == LogUtil.START_TIME ? "\\d+/\\d+/\\d+" : 
            key == LogUtil.THREAD_NAME ? Thread.currentThread().getName() : 
            key == LogUtil.TRANSACTION_ID ? "[a-fA-E0-9-]+" : key);
        Assert.assertTrue(String.format("TransactionMessage could not find %s '%s' in '%s'", key, pattern, logged), 
            Pattern.compile(pattern).matcher(logged).find());
      }
    }
    Assert.assertTrue(String.format("TransactionMessage could not find ',%s=%s' in '%s'", testValue, testValue, logged),
        logged.contains(String.format(",%s=%s", testValue, testValue)));

    String referenceId = "This is a test referenceId";
    startMessage = StartMessage.builder(message).referenceId(referenceId).build();
    Assert.assertEquals(message, startMessage.getMessage());
    Assert.assertEquals(referenceId, startMessage.getReferenceId());
  }

  @Test
  public void testTransactionMessage() {
    String[] startKeys = new String[] { LogUtil.HOST_NAME, LogUtil.THREAD_NAME, LogUtil.TRANSACTION_ID };
    String[] setKeys = new String[] { LogUtil.ACTION, LogUtil.OUTPUT_TYPE, LogUtil.OUTPUT_VALUE };
    String[][] combinedKeys = new String[][] { startKeys, setKeys };

    LogUtil.markMDCStart(true);
    Object startTime = LogUtil.getMDC(LogUtil.START_TIME);

    try {
      Thread.sleep(2000L);
    } catch (InterruptedException ee) {
    }

    for (String key : setKeys) {
      LogUtil.setMDC(key, key);
    }

    for (String[] keys : combinedKeys) {
      for (String key : keys) {
        Assert.assertNotNull(String.format("MDC expecting value for: %%s", key), LogUtil.getMDC(key));
      }
    }

    LogUtil.setMDC(LogUtil.START_TIME, startTime);
    String message = "This is a test TransactionMessage";
    TransactionMessage startMessage = TransactionMessage.builder(message).outputType(TransactionMessage.OutputTypeEnum.CONTENT).build();
    Assert.assertEquals(message, startMessage.getMessage());
    Assert.assertEquals(TransactionMessage.OutputTypeEnum.CONTENT, startMessage.getOutputType());

    String logged = startMessage.toString();
    System.out.println(logged);
    for (String[] keys : combinedKeys) {
      for (String key : keys) {
        String pattern = String.format(",%s=%s", nameMap.get(key),
            key == LogUtil.HOST_NAME ? LogUtil.getLocalHost() : 
            key == LogUtil.THREAD_NAME ? Thread.currentThread().getName() : 
            key == LogUtil.TRANSACTION_ID ? "[a-fA-E0-9-]+" : 
            key == LogUtil.OUTPUT_TYPE ? TransactionMessage.OutputTypeEnum.CONTENT.name() : key);
        Assert.assertTrue(String.format("TransactionMessage could not find %s as '%s' in '%s'", key, pattern, logged), 
            Pattern.compile(pattern).matcher(logged).find());
      }
    }
    String match = String.format(",outputType=%s", TransactionMessage.OutputTypeEnum.CONTENT.name());
    Assert.assertTrue(String.format("TransactionMessage does not contain '%s' in '%s'", match, logged), logged.contains(match));

    String pattern = ",timeOffset=\\d+";
    Assert.assertTrue(String.format("TransactionMessage could not find '%s' in '%s'", pattern, logged), 
            Pattern.compile(pattern).matcher(logged).find());

    pattern = ",timeStamp=\\d+/\\d+/\\d+";
    Assert.assertTrue(String.format("TransactionMessage could not find '%s' in '%s'", pattern, logged), 
            Pattern.compile(pattern).matcher(logged).find());

    pattern = ",duration=\\d+";
    Assert.assertTrue(String.format("TransactionMessage could not find '%s' in '%s'", pattern, logged), 
            Pattern.compile(pattern).matcher(logged).find());

    String action = "This is a test Action";
    TransactionMessage.OutputTypeEnum outputType = TransactionMessage.OutputTypeEnum.JSON;
    startMessage = TransactionMessage.builder(message).action(action).build();
    Assert.assertEquals(message, startMessage.getMessage());
    Assert.assertEquals(action, startMessage.getAction());
  }

}