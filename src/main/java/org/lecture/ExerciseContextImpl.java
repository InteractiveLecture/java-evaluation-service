package org.lecture;

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


import org.lecture.compiler.api.ExerciseContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author rene
 */
public class ExerciseContextImpl implements ExerciseContext {
  private Map<String, Class<?>> excerciseClasses;
  private Map<String, Class<?>> testClasses;

  @Override
  public void setExerciseClasses(Map<String, Class<?>> exerciseClasses) {
    this.excerciseClasses = exerciseClasses;
  }

  @Override
  public Object createObject(String className, Object... params) {
    try {
      Constructor c = this.excerciseClasses.get(className).getConstructor(getParameterTypes(params));
      return c.newInstance(params);
    } catch (NoSuchMethodException | SecurityException
        | InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException ex) {

      throw new RuntimeException(ex);
    }
  }

  @Override
  public Map<String, Class<?>> getUserClasses() {
    return excerciseClasses;
  }

  @Override
  public Map<String, Class<?>> getTestClasses() {
    return testClasses;
  }

  @Override
  public void setTestClasses(Map<String, Class<?>> classes) {
    this.testClasses = classes;
  }

  @Override
  public Object executeMethod(Object object, String methodName, Object... parameters) {
    try {
      return object.getClass()
          .getMethod(methodName, (Class<?>[]) getParameterTypes(parameters))
          .invoke(object, parameters);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private Class<?>[] getParameterTypes(Object[] parameters) {
    if (parameters.length == 0) return new Class<?>[0];
    return Arrays.stream(parameters).map(Object::getClass).toArray(Class<?>[]::new);
  }
}
