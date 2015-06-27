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

import java.util.Map;

/**
 * Entity that represents Tests.
 *
 * @author Rene Richter
 */
@Document
public class TestCaseContainer extends SourceContainer {


  private Map<String, Class<?>> testClasses;
  private boolean active;

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Map<String, Class<?>> getTestClasses() {
    return testClasses;
  }

  public void setTestClasses(Map<String, Class<?>> testClasses) {
    this.testClasses = testClasses;
  }
}
