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

import org.lecture.model.SourceContainer;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;

/**
 * A SourceContainer-resource.
 * @author Rene Richter
 */
public class SourceContainerResource extends ResourceSupport {
  
  private Map<String,String> sources;
  
  private String classname;
  
  private String username;
  
  private long exerciseId;
  

  /**
   * Reads all attributes from entity that should get serialized.
   */
  public SourceContainerResource(SourceContainer entity) {
    
    this.sources = entity.getSources();

    this.username = entity.getUsername();
    
    this.exerciseId = entity.getExerciseId();
    
  }

  
  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
  
  public void setClassname(String classname) {
    this.classname = classname;
  }

  public String getClassname() {
    return this.classname;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }
  
  public void setExerciseid(long exerciseId) {
    this.exerciseId = exerciseId;
  }

  public long getExerciseid() {
    return this.exerciseId;
  }
  
}