package com.wstrater.commons.log;

public class ErrorMessage extends AbstractMessage {

  private static final long  serialVersionUID = 20150401;

  public final static String LOGGER_NAME      = TransactionMessage.LOGGER_NAME;

  private String             className;
  private int                lineNumber;
  private String             methodName;
  private Throwable          thrown;

  public ErrorMessage() {
    super();
  }

  public ErrorMessage(Object message) {
    this(message, null);
  }

  public ErrorMessage(Object message, Throwable thrown) {
    this();

    if (thrown == null && message instanceof Throwable) {
      setMessage(null);
      this.thrown = (Throwable) message;
    } else {
      setMessage(message);
      this.thrown = thrown;
    }
  }

  public static Builder builder() {
    return new Builder(new ErrorMessage());
  }

  public static Builder builder(Object message) {
    return new Builder(new ErrorMessage(message, null));
  }

  public static Builder builder(Object message, Throwable thrown) {
    return new Builder(new ErrorMessage(message, thrown));
  }

  public String getClassName() {
    return className;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getMethodName() {
    return methodName;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();

    buf.append("ErrorMessage: ");
    super.toStringBuilder(buf);

    if (className != null) {
      buf.append(",className=").append(className);
    }
    if (lineNumber != 0) {
      buf.append(",lineNumber=").append(lineNumber);
    }
    if (methodName != null) {
      buf.append(",methodName=").append(methodName);
    }
    if (thrown != null) {
      buf.append(",thrown=").append(encode(thrown.toString()));
    }

    return buf.toString();
  }

  public static class Builder extends AbstractMessage.Builder {

    public Builder(ErrorMessage built) {
      super(built);
    }

    public ErrorMessage build() {
      return (ErrorMessage) super.build();
    }

    public Builder className(String className) {
      build().className = className;
      return this;
    }

    public Builder message(Object message) {
      if (build().thrown == null && message instanceof Throwable) {
        build().thrown = (Throwable) message;
      } else {
        super.message(message);
      }
      return this;
    }

    public Builder methodName(String methodName) {
      build().methodName = methodName;
      return this;
    }

    public Builder lineNumber(int lineNumber) {
      build().lineNumber = lineNumber;
      return this;
    }

    public Builder thrown(Throwable thrown) {
      build().thrown = thrown;
      return this;
    }

  }

}