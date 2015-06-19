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

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity that represents Tests.
 * @author Rene Richter
 */
@Document
public class TestCase extends BaseEntity {

  private long exerciseId;
  private String testCode;
  private Class<?> testClass;
  private String classname;
  private String username;
  private CompilationReport compilationReport;
  

  public TestCase(){}


  /**
   * a convenience constructor.
   */
  public TestCase(long exerciseId,String classname, String testCode, Class<?> testClass,String username) {

    this.exerciseId = exerciseId;
    this.testCode = testCode;
    this.testClass = testClass;
    this.classname = classname;
    this.username = username;
    
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

  public String getTestCode() {
    return testCode;
  }

  public void setTestCode(String testCode) {
    this.testCode = testCode;
  }

  public Class<?> getTestClass() {
    return testClass;
  }

  public void setTestClass(Class<?> testClass) {
    this.testClass = testClass;
  }

  public String getClassname() {
    return classname;
  }

  public void setClassname(String classname) {
    this.classname = classname;
  }
}
