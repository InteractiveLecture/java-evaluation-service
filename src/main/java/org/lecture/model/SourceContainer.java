package org.lecture.model;

/*
 * Copyright (c) 2015 Rene Richter
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rene on 20.06.15.
 */
public class SourceContainer extends BaseEntity {

  private String userId;
  // k=classname, v=source
  private Map<String, String> sources;
  private String taskId;

  @JsonIgnore
  @Transient
  private Map<String, Class<?>> testClasses;

  private LocalDateTime submissionDate;

  private CompilationReport compilationReport;

  private boolean tests;

  public SourceContainer(String userId, String taskId) {
    this.userId = userId;
    this.taskId = taskId;
    this.sources = new HashMap<>();
    this.submissionDate = LocalDateTime.now();
    this.compilationReport = new CompilationReport();
  }

  public SourceContainer(){
    this.sources = new HashMap<>();
    this.compilationReport = new CompilationReport();
  }

  public boolean isTests() {
    return tests;
  }

  public void setTests(boolean tests) {
    this.tests = tests;
  }

  public Map<String, Class<?>> getTestClasses() {
    return testClasses;
  }

  public void setTestClasses(Map<String, Class<?>> testClasses) {
    this.testClasses = testClasses;
  }

  public void addSource(String classname, String source) {
    if (sources == null) {
      sources = new HashMap<>();
    }
    this.sources.put(classname, source);
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Map<String, String> getSources() {
    return sources;
  }

  public void setSources(Map<String, String> sources) {
    this.sources = sources;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public LocalDateTime getSubmissionDate() {
    return submissionDate;
  }

  public void setSubmissionDate(LocalDateTime submissionDate) {
    this.submissionDate = submissionDate;
  }

  public CompilationReport getCompilationReport() {
    return compilationReport;
  }

  public void setCompilationReport(CompilationReport compilationReport) {
    this.compilationReport = compilationReport;
  }
}
