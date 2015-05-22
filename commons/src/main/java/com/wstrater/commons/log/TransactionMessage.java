package com.wstrater.commons.log;

public class TransactionMessage extends AbstractMessage {

  private static final long serialVersionUID = 20150401L;

  public enum OutputTypeEnum {
    CONTENT, FILE, FILLABLE_PDF, FOP_PDF, FORWARD, HTML, IMAGE, JSON, REDIRECT, REST, VIEW, XML, XSL
  };

  public final static String LOGGER_NAME = "MESSAGE.Transaction";

  private String             action;
  private OutputTypeEnum     outputType;
  private String             outputValue;

  public TransactionMessage() {
    super();

    action = valueOfMDC(LogUtil.ACTION);
    setDuration(getTimeOffset());
    Object obj = LogUtil.getMDC(LogUtil.OUTPUT_TYPE);
    if (obj instanceof OutputTypeEnum) {
      outputType = (OutputTypeEnum) obj;
    }
    outputValue = valueOfMDC(LogUtil.OUTPUT_VALUE);
  }

  public TransactionMessage(Object message) {
    this();

    setMessage(message);
  }

  public static Builder builder() {
    return new Builder(new TransactionMessage());
  }

  public static Builder builder(Object message) {
    return new Builder(new TransactionMessage(message));
  }

  public String getAction() {
    return action;
  }

  public OutputTypeEnum getOutputType() {
    return outputType;
  }

  public String getOutputValue() {
    return outputValue;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();

    buf.append("TransactionMessage: ");
    this.toStringBuilder(buf);

    return buf.toString();
  }

  @Override
  protected void toStringBuilder(StringBuilder buf) {
    super.toStringBuilder(buf);

    if (action != null) {
      buf.append(",action=").append(action);
    }
    if (outputType != null) {
      buf.append(",outputType=").append(outputType);
    }
    if (outputValue != null) {
      buf.append(",outputValue=").append(outputValue);
    }
  }

  public static class Builder extends AbstractMessage.Builder {

    protected Builder(TransactionMessage built) {
      super(built);
    }

    public TransactionMessage build() {
      return (TransactionMessage) super.build();
    }

    public Builder action(String action) {
      build().action = action;
      return this;
    }

    public Builder outputType(OutputTypeEnum outputType) {
      build().outputType = outputType;
      return this;
    }

    public Builder outputValue(String outputValue) {
      build().outputValue = outputValue;
      return this;
    }

  }

}