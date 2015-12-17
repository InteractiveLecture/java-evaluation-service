package org.lecture.resource;

import org.lecture.model.TestReport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rene on 17.12.15.
 */
public class TestReportResource {
  private List<TestResultResource> testResults;
  private boolean allPassed;

  public TestReportResource(TestReport testReport) {
    this.allPassed = testReport.isAllPassed();
    this.testResults = testReport.getTestResults().stream().map(TestResultResource::new).collect(Collectors.toList());
  }

  public List<TestResultResource> getTestResults() {
    return testResults;
  }

  public void setTestResults(List<TestResultResource> testResults) {
    this.testResults = testResults;
  }

  public boolean isAllPassed() {
    return allPassed;
  }

  public void setAllPassed(boolean allPassed) {
    this.allPassed = allPassed;
  }
}
