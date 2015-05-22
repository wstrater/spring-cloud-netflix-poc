package com.wstrater.commons.log;

public class ServiceMessage extends AbstractMessage {

  private static final long  serialVersionUID = 20150401;

  public final static String LOGGER_NAME      = TransactionMessage.LOGGER_NAME;

  private Boolean            serviceFailed;
  private String             serviceName;
  private String             serviceURL;
  
  public ServiceMessage() {
    super();

    serviceFailed = booleanOfMDC(LogUtil.SERVICE_FAILED);
    serviceURL = valueOfMDC(LogUtil.SERVICE_URL);
  }

  public ServiceMessage(Object message) {
    this();

    setMessage(message);
  }

  public static Builder builder() {
    return new Builder(new ServiceMessage());
  }

  public static Builder builder(Object message) {
    return new Builder(new ServiceMessage(message));
  }

  public Boolean getServiceFailed() {
    return serviceFailed;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getServiceURL() {
    return serviceURL;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();

    buf.append("ServiceMessage: ");
    super.toStringBuilder(buf);

    if (serviceFailed != null) {
      buf.append(",serviceFailed=").append(serviceFailed);
    }
    if (serviceName != null) {
      buf.append(",serviceName=").append(serviceName);
    }
    if (serviceURL != null) {
      buf.append(",serviceURL=").append(serviceURL);
    }

    return buf.toString();
  }

  public static class Builder extends AbstractMessage.Builder {

    public Builder(ServiceMessage built) {
      super(built);
    }

    public ServiceMessage build() {
      return (ServiceMessage) super.build();
    }

    public Builder serviceFailed(Boolean serviceFailed) {
      build().serviceFailed = serviceFailed;
      return this;
    }

    public Builder serviceName(String serviceName) {
      build().serviceName = serviceName;
      return this;
    }

    public Builder serviceURL(String serviceURL) {
      build().serviceURL = serviceURL;
      return this;
    }

  }

}