package org.lecture.resource;

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

import org.lecture.model.CompilationReport;
import org.lecture.model.TestCaseContainer;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;

/**
 * A TestCaseContainer-resource.
 *
 * @author Rene Richter
 */
public class TestCaseResource extends ResourceSupport {

  Map<String, String> sources;
  private String taskId;
  private boolean active;

  private String username;

  private CompilationReport compilationReport;


  /*
   * Reads all attributes from entity that should get serialized.
   */
  public TestCaseResource(TestCaseContainer entity) {

    this.taskId = entity.getTaskId();

    this.sources = entity.getSources();

    this.active = entity.isActive();

    this.username = entity.getUserId();

    this.compilationReport = entity.getCompilationReport();

  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }


  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public CompilationReport getCompilationReport() {
    return compilationReport;
  }

  public void setCompilationReport(CompilationReport compilationReport) {
    this.compilationReport = compilationReport;
  }

}
