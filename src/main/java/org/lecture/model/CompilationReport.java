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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity that represents CompilationReports.
 * @author Rene Richter
 */
public class CompilationReport {

  private LocalDateTime date;
  private List<CompilationDiagnostic> errors;
  private List<CompilationDiagnostic> warnings;

  

  public CompilationReport(){}


  /**
   * a convenience constructor.
   */
  public  CompilationReport(LocalDateTime date,List<CompilationDiagnostic> errors,List<CompilationDiagnostic> warnings) {

    this.date = date;
    this.errors = errors;
    this.warnings = warnings;
    
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
}
