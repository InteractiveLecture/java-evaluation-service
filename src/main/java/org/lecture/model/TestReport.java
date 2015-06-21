package org.lecture.model;

/*
 * Copyright (c) 2015 Rene Richter.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

import java.util.List;

/**
 * Created by rene on 21.06.15.
 *
 * @author Rene Richter
 */
public class TestReport {

  private List<TestResult> testResults;
  private boolean allPassed;

  public TestReport(){}

  public TestReport(List<TestResult> testResults) {
    this.testResults = testResults;
    this.allPassed = !testResults.stream()
        .filter(testResult -> !testResult.isSuccessful())
        .findAny()
        .isPresent();
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public void setTestResults(List<TestResult> testResults) {
    this.testResults = testResults;
  }

  public boolean isAllPassed() {
    return allPassed;
  }

  public void setAllPassed(boolean allPassed) {
    this.allPassed = allPassed;
  }
}
