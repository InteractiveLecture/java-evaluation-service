package org.lecture.service;

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

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;
import org.lecture.compiler.compiler.StringCompiler;
import org.lecture.compiler.service.api.ExerciseContainer;
import org.lecture.compiler.service.container.AppContainerImpl;
import org.lecture.compiler.testframework.LectureRunner;
import org.lecture.model.SourceContainer;
import org.lecture.model.TestCaseContainer;
import org.lecture.model.TestReport;
import org.lecture.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rene on 21.06.15.
 *
 * @author Rene Richter
 */
public class TestServiceImpl {

  @Autowired
  TestCaseRepository testCaseRepository;


  public TestReport runTests(SourceContainer sourceContainer) {
    TestCaseContainer testCaseContainer =
        testCaseRepository.findByExerciseId(sourceContainer.getExerciseId());

    ExerciseContainer exerciseContainer = new AppContainerImpl();

    exerciseContainer.setTestClasses(testCaseContainer.getTestClasses());

    StringCompiler compiler = new StringCompiler();
    sourceContainer.getSources().forEach(compiler::addCompilationTask);
    exerciseContainer.setExerciseClasses(compiler.startCompilation().getCompiledClasses());

    JUnitCore jc = new JUnitCore();
    List<Runner> runners = testCaseContainer.getTestClasses().values()
        .stream()
        .map(clazz -> instanciateRunner(clazz,exerciseContainer))
        .collect(Collectors.toList());

    List<Result> results = runners.stream()
        .map(jc::run)
        .collect(Collectors.toList());





    Result result = jc.run((Class<?>[])testCaseContainer.getTestClasses().values().toArray());
    Failure[] failures = new Failure[result.getFailureCount()];
    if(!result.wasSuccessful())
      failures = result.getFailures().toArray(new Failure[result.getFailureCount()]);


  }

  private LectureRunner instanciateRunner(Class<?> clazz, ExerciseContainer container) {
    try {
      return new LectureRunner(clazz, container);
    } catch (InitializationError initializationError) {
      throw new RuntimeException(initializationError);
    }
  }
}
