package org.lecture.model;

/*
* Copyright (c) 2015 .
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

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity that represents CodeSubmissions.
 * @author Rene Richter
 */
@Document
public class CodeSubmission extends BaseEntity {

  private String username;
  // k=classname, v=source
  private Map<String,String> sources;
  private long exerciseId;

  private LocalDateTime submissionDate;


  public CodeSubmission(){
    this.sources = new HashMap<>();
  }




  /**
   * a convenience constructor.
   */
  public CodeSubmission(String username, String classname, String source, long exerciseId) {
    this.username = username;
    Map<String,String> map = new HashMap<>();
    map.put(classname,source);
    this.sources = map;
    this.exerciseId = exerciseId;
  }

  public void addSubmission(String className,String sourceCode) {
    this.sources.put(className,sourceCode);

  }

  public LocalDateTime getSubmissionDate() {
    return submissionDate;
  }

  public void setSubmissionDate(LocalDateTime submissionDate) {
    this.submissionDate = submissionDate;
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
}
