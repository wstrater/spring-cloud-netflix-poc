package com.wstrater.commons.log;

import java.util.Hashtable;
import java.net.InetAddress;

import org.apache.log4j.MDC;

import com.wstrater.commons.util.Compare;
import com.wstrater.commons.util.GUID;

public abstract class LogUtil {

  public static final String ACTION            = "ACTION";
  public static final String CLASS_NAME        = "CLASS_NAME";
  public static final String CLIENT_IP_ADDRESS = "CLIENT_IP_ADDRESS";
  public static final String COOKIES           = "COOKIES";
  public static final String HOST_NAME         = "HOST_NAME";
  public static final String LINE_NUMBER       = "LINE_NUMBER";
  public static final String METHOD_NAME       = "METHOD_NAME";
  public static final String OUTPUT_TYPE       = "OUTPUT_TYPE";
  public static final String OUTPUT_VALUE      = "OUTPUT_VALUE";
  public static final String PID               = "PID";
  public static final String REFERENCE_ID      = "REFERENCE_ID";
  public static final String REFERER           = "REFERER";
  public static final String REQUEST_ID        = "REQUEST_ID";
  public static final String REQUEST_TYPE      = "REQUEST_TYPE";
  public static final String REQUEST_URI       = "REQUEST_URI";
  public static final String REQUEST_URL       = "REQUEST_URL";
  public static final String RESPONSE_TYPE     = "RESPONSE_TYPE";
  public static final String SERVICE_FAILED    = "SERVICE_FAILED";
  public static final String SERVICE_NAME      = "SERVICE_NAME";
  public static final String SERVICE_URL       = "SERVICE_URL";
  public static final String SERVLET_ACTION    = "SERVLET_ACTION";
  public static final String SESSION_ID        = "SESSION_ID";
  public static final String SOAP_ACTION       = "SOAP_ACTION";
  public static final String SOAP_REQUEST_ID   = "SOAP_REQUEST_ID";
  public static final String SOAP_RESPONSE_ID  = "SOAP_RESPONSE_ID";
  public static final String SOAP_SUCCESS      = "SOAP_SUCCESS";
  public static final String SOAP_TIME         = "SOAP_TIME";
  public static final String SOAP_URL          = "SOAP_URL";
  public static final String STACK_TRACE       = "STACK_TRACE";
  public static final String START_TIME        = "START_TIME";
  public static final String THREAD_NAME       = "THREAD_NAME";
  public static final String TRANSACTION_ID    = "TRANSACTION_ID";
  public static final String USERNAME          = "USERNAME";
  public static final String USER_AGENT        = "USER_AGENT";
  public static final String USER_ID           = "USER_ID";
  public static final String TRACKING_ID       = "TRACKING_ID";

  public static final String REFERER_HEADER    = "Referer";
  public static final String USER_AGENT_HEADER = "User-Agent";
  public static final String X_FOWARDED_FOR_HEADER = "X-Forwarded-For";

  private static String      localHost;

  public static void clearMDC() {
    Hashtable context = MDC.getContext();
    if (context != null) {
      context.clear();
    }
  }

  public static String getLocalHost() {
    if (localHost == null) {
      synchronized (LogUtil.class) {
        if (localHost == null) {
          localHost = "localHost";
          try {
            InetAddress addr = InetAddress.getLocalHost();
            localHost = addr.getHostName();
            if (localHost == null) {
              localHost = addr.getHostAddress();
            }
          } catch (Exception ee) {
            localHost = "localHost";
            System.err.println(String.format("Can not get localHost: %s", ee.toString()));
          }
        }
      }
    }

    return localHost;
  }

  public static Object getMDC(String name) {
    Object ret = null;

    if (name != null) {
      ret = MDC.get(name);
    }

    return ret;
  }

  public static String getMDCString(String name) {
    String ret = null;

    Object obj = getMDC(name);
    if (obj != null) {
      ret = String.valueOf(obj);
    }

    return ret;
  }

  public static String getMDCTransactionId() {
    return getMDCTransactionId(true);
  }

  public static String getMDCTransactionId(boolean createIfMissing) {
    String ret = null;

    Object value = getMDC(TRANSACTION_ID);
    if (value instanceof String) {
      ret = (String) value;
    } else if (createIfMissing) {
      ret = GUID.getGUID();
      setMDC(TRANSACTION_ID, ret);
    }

    return ret;
  }

  public static void markMDCStart() {
    markMDCStart(true);
  }

  public static void markMDCStart(boolean clear) {
    if (clear) {
      clearMDC();
    }

    setMDC(START_TIME, System.currentTimeMillis());
    setMDC(HOST_NAME, getLocalHost());
    setMDC(PID, System.getProperty("PID"));
    setMDC(THREAD_NAME, Thread.currentThread().getName());
    getMDCTransactionId();
  }

  public static void setMDC(String name, Object value) {
    setMDC(name, value, true);
  }

  public static void setMDC(String name, Object value, boolean overWriteIfExists) {
    if (Compare.isNotBlank(name)) {
      if (value == null || (value instanceof String && Compare.isBlank((String) value))) {
        MDC.remove(name);
      } else if (overWriteIfExists) {
        MDC.put(name, value);
      } else if (MDC.getContext() == null || !MDC.getContext().contains(name)) {
        MDC.put(name, value);
      }
    }
  }

  public static void removeMDC(String name) {
    if (Compare.isNotBlank(name)) {
      MDC.remove(name);
    }
  }

  public static String toStringMDC() {
    String ret = "[]";
    
    Hashtable context = MDC.getContext();
    if (context != null) {
      ret = context.toString();
    }

    return ret;
  }

}