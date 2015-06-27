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


import compiler.compiler.StringJavaFileObject;

import javax.tools.Diagnostic;

/**
 * Wrapper for Diagnostic objects.
 * @author Rene Richter
 */
public class CompilationDiagnostic {

  private String classname;
  private String code;
  private long colNumber;
  private long endPosition;
  private long startPosition;
  private long lineNumber;
  private String message;
  private long position;
  private boolean noPosition;

  public CompilationDiagnostic(){}

  public CompilationDiagnostic(Diagnostic diagnostic) {
    this.classname = ((StringJavaFileObject)diagnostic.getSource()).className;
    this.code = diagnostic.getCode();
    this.colNumber = diagnostic.getColumnNumber();
    this.endPosition = diagnostic.getEndPosition();
    this.startPosition = diagnostic.getStartPosition();
    this.lineNumber = diagnostic.getLineNumber();
    this.position = diagnostic.getPosition();
    this.noPosition = diagnostic.getPosition() >= 0;
  }



  public String getClassname() {
    return classname;
  }

  public void setClassname(String classname) {
    this.classname = classname;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public long getColNumber() {
    return colNumber;
  }

  public void setColNumber(long colNumber) {
    this.colNumber = colNumber;
  }

  public long getEndPosition() {
    return endPosition;
  }

  public void setEndPosition(long endPosition) {
    this.endPosition = endPosition;
  }

  public long getStartPosition() {
    return startPosition;
  }

  public void setStartPosition(long startPosition) {
    this.startPosition = startPosition;
  }

  public long getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(long lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getPosition() {
    return position;
  }

  public void setPosition(long position) {
    this.position = position;
  }

  public boolean isNoPosition() {
    return noPosition;
  }

  public void setNoPosition(boolean noPosition) {
    this.noPosition = noPosition;
  }
}
