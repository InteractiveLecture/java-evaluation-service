package org.lecture.resource;

import org.junit.runner.notification.Failure;

/**
 * Created by rene on 17.12.15.
 */
public class FailureResource {
  private String message;
  private String methodName;
  private String className;

  public FailureResource(Failure failure) {
    this.message = failure.getMessage();
    this.methodName = failure.getDescription().getMethodName();
    this.className = failure.getDescription().getClassName();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
