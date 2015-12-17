package org.lecture.resource;

import org.lecture.model.TestReport;
import org.lecture.model.TestResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rene on 17.12.15.
 */
public class TestResultResource {

  List<FailureResource> failures;
  private boolean successful;

  public TestResultResource(TestResult result) {
    this.successful = result.isSuccessful();
    this.failures = result.getFailures().stream().map(FailureResource::new).collect(Collectors.toList());
  }

  public List<FailureResource> getFailures() {
    return failures;
  }

  public void setFailures(List<FailureResource> failures) {
    this.failures = failures;
  }

  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }
}
