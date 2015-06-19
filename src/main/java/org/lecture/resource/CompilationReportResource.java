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

import org.lecture.model.CompilationDiagnostic;
import org.lecture.model.CompilationReport;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A CompilationReport-resource.
 * @author Rene Richter
 */
public class CompilationReportResource extends ResourceSupport {

  private LocalDateTime date;
  private List<CompilationDiagnostic> errors;
  private List<CompilationDiagnostic> warnings;

  private String username;
  private long exerciseId;
  

  /**
   * Reads all attributes from entity that should get serialized.
   */
  public  CompilationReportResource(CompilationReport entity) {
    this.date = entity.getDate();
    this.errors = entity.getErrors();
    this.warnings = entity.getWarnings();
    this.username = entity.getUsername();
    this.exerciseId = entity.getExerciseId();
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public List<CompilationDiagnostic> getErrors() {
    return errors;
  }

  public void setErrors(List<CompilationDiagnostic> errors) {
    this.errors = errors;
  }

  public List<CompilationDiagnostic> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<CompilationDiagnostic> warnings) {
    this.warnings = warnings;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public long getExerciseId() {
    return exerciseId;
  }

  public void setExerciseId(long exerciseId) {
    this.exerciseId = exerciseId;
  }
}
