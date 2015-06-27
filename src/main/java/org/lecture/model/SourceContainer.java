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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rene on 20.06.15.
 */
public class SourceContainer extends BaseEntity {

  private String username;
  // k=classname, v=source
  private Map<String, String> sources;
  private long exerciseId;

  private LocalDateTime submissionDate;

  private CompilationReport compilationReport;

  public void addSource(String classname, String source) {
    if (sources == null) {
      sources = new HashMap<>();
    }
    this.sources.put(classname, source);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Map<String, String> getSources() {
    return sources;
  }

  public void setSources(Map<String, String> sources) {
    this.sources = sources;
  }

  public long getExerciseId() {
    return exerciseId;
  }

  public void setExerciseId(long exerciseId) {
    this.exerciseId = exerciseId;
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
