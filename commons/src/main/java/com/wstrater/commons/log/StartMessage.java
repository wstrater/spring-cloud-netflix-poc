package com.wstrater.commons.log;

public class StartMessage extends AbstractMessage {

  private static final long  serialVersionUID = 20150401;

  public final static String LOGGER_NAME      = TransactionMessage.LOGGER_NAME;

  private String             clientIP;
  private String             referenceId;
  private String             referer;
  private String             requestId;
  private String             requestURI;
  private String             sessionId;
  private String             trackingId;
  private String             userAgent;

  public StartMessage() {
    super();

    clientIP = valueOfMDC(LogUtil.CLIENT_IP_ADDRESS);
    referer = valueOfMDC(LogUtil.REFERER);
    requestId = valueOfMDC(LogUtil.REQUEST_ID);
    requestURI = valueOfMDC(LogUtil.REQUEST_URI);
    referenceId = valueOfMDC(LogUtil.REFERENCE_ID);
    sessionId = valueOfMDC(LogUtil.SESSION_ID);
    trackingId = valueOfMDC(LogUtil.TRACKING_ID);
    userAgent = valueOfMDC(LogUtil.USER_AGENT);
  }

  public StartMessage(Object message) {
    this();

    setMessage(message);
  }

  public static Builder builder() {
    return new Builder(new StartMessage());
  }

  public static Builder builder(Object message) {
    return new Builder(new StartMessage(message));
  }

  public String getClientIP() {
    return clientIP;
  }

  /*
   * The referenceId is the first transactionId for the user request. 
   * It is set to the transactionId if there is no referenceId.
   */
  public String getReferenceId() {
    return referenceId;
  }

  public String getReferer() {
    return referer;
  }

  /*
   * The requestId is the transactionId from the calling system.
   * The transactionId is added to the request header as the requestId.
   */
  public String getRequestId() {
    return requestId;
  }

  public String getRequestURI() {
    return requestURI;
  }

  public String getSessionId() {
    return sessionId;
  }

  /*
   * The trackingId is common to multiple user request.
   */
  public String getTrackingId() {
    return trackingId;
  }

  public String getUserAgent() {
    return userAgent;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();

    buf.append("StartMessage: ");
    this.toStringBuilder(buf);

    return buf.toString();
  }

  @Override
  protected void toStringBuilder(StringBuilder buf) {
    super.toStringBuilder(buf);

    if (clientIP != null) {
      buf.append(",clientIP=").append(clientIP);
    }
    if (referenceId != null) {
      buf.append(",referenceId=").append(referenceId);
    }
    if (referer != null) {
      buf.append(",referer=").append(encode(referer));
    }
    if (requestId != null) {
      buf.append(",requestId=").append(requestId);
    }
    if (requestURI != null) {
      buf.append(",requestURI=").append(requestURI);
    }
    if (sessionId != null) {
      buf.append(",sessionId=").append(sessionId);
    }
    if (trackingId != null) {
      buf.append(",trackingId=").append(trackingId);
    }
    if (userAgent != null) {
      buf.append(",userAgent=").append(userAgent);
    }
  }

  public static class Builder extends AbstractMessage.Builder {

    protected Builder(StartMessage built) {
      super(built);
    }

    public StartMessage build() {
      return (StartMessage) super.build();
    }

    public Builder clientIP(String clientIP) {
      build().clientIP = clientIP;
      return this;
    }

    public Builder referenceId(String referenceId) {
      build().referenceId = referenceId;
      return this;
    }

    public Builder referer(String referer) {
      build().referer = referer;
      return this;
    }

    public Builder requestId(String requestId) {
      build().requestId = requestId;
      return this;
    }

    public Builder requestURI(String requestURI) {
      build().requestURI = requestURI;
      return this;
    }

    public Builder sessionId(String sessionId) {
      build().sessionId = sessionId;
      return this;
    }

    public Builder trackingId(String trackingId) {
      build().trackingId = trackingId;
      return this;
    }

    public Builder userAgent(String userAgent) {
      build().userAgent = userAgent;
      return this;
    }

  }

}