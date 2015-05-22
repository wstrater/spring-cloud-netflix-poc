package com.wstrater.commons.log;

import java.io.UnsupportedEncodingException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.log4j.MDC;
import org.apache.log4j.spi.LocationInfo;

import com.wstrater.commons.util.GUID;

@SuppressWarnings("serial")
public abstract class AbstractMessage implements Serializable {

  private static final boolean        USE_URL_ENCODER = true;

  private transient static DateFormat dateFmt;
  private transient static Pattern    whiteSpaceEncoder;

  private String                      clientIP;
  private String                      codeLocation;
  private long                        duration;
  private String                      hostName;
  private Object                      message;
  private String                      threadName;
  private long                        timeOffset;
  private String                      transactionId;
  private long                        timeStamp;
  private int                         userId;
  private String                      userName;
  private Map<String, String>         values;

  public AbstractMessage() {
    this.timeStamp = System.currentTimeMillis();

    clientIP = valueOfMDC(LogUtil.CLIENT_IP_ADDRESS);
    hostName = valueOfMDC(LogUtil.HOST_NAME);
    threadName = Thread.currentThread().getName();
    long startTime = longOfMDC(LogUtil.START_TIME);
    if (startTime > 0) {
      timeOffset = System.currentTimeMillis() - startTime;
    }
    transactionId = valueOfMDC(LogUtil.TRANSACTION_ID);
    if (transactionId == null) {
      transactionId = GUID.getGUID();
    }
    MDC.put(LogUtil.TRANSACTION_ID, transactionId);
    userId = intOfMDC(LogUtil.USER_ID);
    userName = valueOfMDC(LogUtil.USERNAME);
  }

  public AbstractMessage(Object message) {
    this();

    setMessage(message);
  }

  protected static boolean booleanOf(Object obj) {
    boolean ret = false;

    if (obj instanceof Boolean) {
      ret = ((Boolean) obj).booleanValue();
    } else if (obj instanceof Number) {
      ret = ((Number) obj).longValue() != 0;
    } else if (obj != null) {
      String temp = String.valueOf(obj).trim().toLowerCase();
      if (temp.startsWith("y") || temp.startsWith("t") || temp.startsWith("on")) {
        ret = true;
      } else if (temp.startsWith("n") || temp.startsWith("f") || temp.startsWith("of")) {
        ret = false;
      } else {
        ret = Boolean.parseBoolean(temp);
      }
    }

    return ret;
  }

  protected static boolean booleanOfMDC(String key) {
    boolean ret = booleanOf(LogUtil.getMDC(key));

    return ret;
  }

  protected String encode(Object in) {
    String ret = null;

    if (in != null) {
      ret = encode(String.valueOf(in));
    }

    return ret;
  }

  protected String encode(String in) {
    String ret = in;

    if (in != null) {
      if (USE_URL_ENCODER) {
        try {
          ret = URLEncoder.encode(in, "UTF-8");
        } catch (UnsupportedEncodingException ee) {
          ret = "UNABLE+TO+ENCODE";
        }
      } else {
        if (whiteSpaceEncoder == null) {
          whiteSpaceEncoder = Pattern.compile("\\s");
        }
        ret = whiteSpaceEncoder.matcher(in).replaceAll("+");
      }
    }

    return ret;
  }

  public String getClientIP() {
    return clientIP;
  }

  public String getCodeLocation() {
    return codeLocation;
  }

  protected DateFormat getDateFormat() {
    if (dateFmt == null) {
      dateFmt = new SimpleDateFormat("yyyy/MM/dd.HH:mm:ss.SSS.z");
    }

    return dateFmt;
  }

  public long getDuration() {
    return duration;
  }

  public String getHostName() {
    return hostName;
  }

  public Object getMessage() {
    return message;
  }

  public String getThreadName() {
    return threadName;
  }

  public long getTimeOffset() {
    return timeOffset;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public Date getTimeStampDate() {
    return new Date(timeStamp);
  }

  /*
   * The transactionId is created each time a user or system request is recieved.
   */
  public String getTransactionId() {
    return transactionId;
  }

  public int getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getValue(String key) {
    String ret = null;

    if (values != null && values.containsKey(key)) {
      ret = values.get(key);
    }

    return ret;
  }

  protected static int intOf(Object obj) {
    int ret = 0;

    if (obj instanceof Number) {
      ret = ((Number) obj).intValue();
    } else if (obj != null) {
      try {
        ret = Integer.parseInt(String.valueOf(obj));
      } catch (NumberFormatException ee) {
        ret = 0;
      }
    }

    return ret;
  }

  protected static int intOfMDC(String key) {
    int ret = intOf(LogUtil.getMDC(key));

    return ret;
  }

  protected static long longOf(Object obj) {
    long ret = 0L;

    if (obj instanceof Number) {
      ret = ((Number) obj).longValue();
    } else if (obj != null) {
      try {
        ret = Long.parseLong(String.valueOf(obj));
      } catch (NumberFormatException ee) {
        ret = 0L;
      }
    }

    return ret;
  }

  protected static long longOfMDC(String key) {
    long ret = longOf(LogUtil.getMDC(key));

    return ret;
  }

  protected void setDuration(long duration) {
    this.duration = duration;
  }

  protected void setMessage(Object message) {
    this.message = message;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();

    this.toStringBuilder(buf);

    return buf.toString();
  }

  protected void toStringBuilder(StringBuilder buf) {
    if (clientIP != null) {
      buf.append(",clientIP=").append(clientIP);
    }
    if (codeLocation != null) {
      buf.append(",codeLocation=").append(codeLocation);
    }
    if (duration > 0) {
      buf.append(",duration=").append(duration);
    }
    if (hostName != null) {
      buf.append(",hostName=").append(hostName);
    }
    if (message != null) {
      buf.append(",message=").append(encode(message));
    }
    if (threadName != null) {
      buf.append(",threadName=").append(threadName);
    }
    if (timeOffset > 0) {
      buf.append(",timeOffset=").append(timeOffset);
    }
    if (transactionId != null) {
      buf.append(",transactionId=").append(transactionId);
    }
    if (userId != 0) {
      buf.append(",userId=").append(userId);
    }
    if (userName != null) {
      buf.append(",userName=").append(userName);
    }
    if (values != null) {
      for (String key : new TreeSet<String>(valuesKeySet())) {
        buf.append(',').append(key).append('=').append(values.get(key));
      }
    }
    buf.append(",timeStamp=").append(getDateFormat().format(getTimeStampDate()));
  }

  public Set<Entry<String, String>> valuesEntrySet() {
    Set<Entry<String, String>> ret = Collections.emptySet();

    if (values != null) {
      ret = Collections.unmodifiableSet(values.entrySet());
    }

    return ret;
  }

  public Set<String> valuesKeySet() {
    Set<String> ret = Collections.emptySet();

    if (values != null) {
      ret = Collections.unmodifiableSet(values.keySet());
    }

    return ret;
  }

  protected static String valueOf(Object obj) {
    String ret = null;

    if (obj != null) {
      ret = String.valueOf(obj);
    }

    return ret;
  }

  protected static String valueOfMDC(String key) {
    String ret = valueOf(LogUtil.getMDC(key));

    return ret;
  }

  public static class Builder {

    private AbstractMessage built;

    protected Builder(AbstractMessage built) {
      this.built = built;
    }

    public AbstractMessage build() {
      return built;
    }

    @Override
    public String toString() {
      return built.toString();
    }

    public Builder clientIP(String clientIP) {
      build().clientIP = clientIP;
      return this;
    }

    public Builder duration(long duration) {
      built.duration = duration;
      return this;
    }

    public Builder hostName(String hostName) {
      build().hostName = hostName;
      return this;
    }

    public Builder markCodeLocation() {
      Throwable thrown = new Exception("EventMessage.Builder.markLocation");
      String fqn = getClass().getName();
      LocationInfo info = new LocationInfo(thrown, fqn);
      if (LocationInfo.NA.equals(info.getClassName())) {
        // Try the abstract message builder.
        fqn = Builder.class.getName();
        info = new LocationInfo(thrown, fqn);
      }
      build().codeLocation = String.format("%s.%s(%s:%s)", info.getClassName(), info.getMethodName(), info.getFileName(),
          info.getLineNumber());
      return this;
    }

    protected Builder message(Object message) {
      built.setMessage(message);
      return this;
    }

    public Builder putValue(String key, Object value) {
      if (value == null) {
        return putValue(key, null);
      } else {
        return putValue(key, String.valueOf(value));
      }
    }

    public Builder putValue(String key, String value) {
      if (key != null) {
        if (value != null) {
          if (build().values == null) {
            build().values = new HashMap<>();
          }
          build().values.put(key, value);
        } else if (build().values != null && build().values.containsKey(key)) {
          build().values.remove(key);
        }
      }
      return this;
    }

    public Builder threadName(String threadName) {
      built.threadName = threadName;
      return this;
    }

    public Builder transactionId(String transactionId) {
      built.transactionId = transactionId;
      return this;
    }

    public Builder userId(int userId) {
      build().userId = userId;
      return this;
    }

    public Builder userId(String userId) {
      return userId(intOf(userId));
    }

    public Builder userName(String userName) {
      build().userName = userName;
      return this;
    }

  }

}