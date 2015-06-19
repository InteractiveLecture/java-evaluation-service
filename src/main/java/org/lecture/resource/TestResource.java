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

import org.lecture.model.TestCase;
import org.springframework.hateoas.ResourceSupport;

/**
 * A TestCase-resource.
 * @author Rene Richter
 */
public class TestResource extends ResourceSupport {
  
  private long exerciseId;
  
  private String testCode;
  
  private Class<?> testClass;
  

  /**
   * Reads all attributes from entity that should get serialized.
   */
  public  TestResource( TestCase entity) {
    
    this.exerciseId = entity.getExerciseid();
    
    this.testCode = entity.getTestcode();
    
    this.testClass = entity.getTestclass();
    
  }

  
  public void setExerciseid(long exerciseId) {
    this.exerciseId = exerciseId;
  }

  public long getExerciseid() {
    return this.exerciseId;
  }
  
  public void setTestcode(String testCode) {
    this.testCode = testCode;
  }

  public String getTestcode() {
    return this.testCode;
  }
  
  public void setTestclass(Class<?> testClass) {
    this.testClass = testClass;
  }

  public Class<?> getTestclass() {
    return this.testClass;
  }
  
}
