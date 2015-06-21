/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lecture.compiler.service.api;

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

import java.util.Map;

/**
 *
 * @author rene
 */
public interface ExerciseContainer
{
  Object createObject(String className, Object... params);
  Object executeMethod(Object object, String methodName, Object... parameters);
  Map<String, Class<?>> getTestClasses();
  Map<String, Class<?>> getUserClasses();
  void setTestClasses(Map<String,Class<?>> classes);
  void setExerciseClasses(Map<String,Class<?>> exerciseClasses);
}
