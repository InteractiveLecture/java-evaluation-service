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
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.lecture.compiler.compiler.StringCompiler;
import org.lecture.compiler.service.api.ExerciseContext;
import org.lecture.ExerciseContextImpl;
import org.lecture.compiler.testframework.LectureRunner;
import org.lecture.model.SourceContainer;
import org.lecture.model.TestCaseContainer;
import org.lecture.model.TestReport;
import org.lecture.model.TestResult;
import org.lecture.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rene on 21.06.15.
 *
 * @author Rene Richter
 */
@Service
public class TestServiceImpl implements TestService {

  @Autowired
  TestCaseRepository testCaseRepository;


  @Override
  public TestReport runTests(SourceContainer sourceContainer) {
    TestCaseContainer testCaseContainer =
        testCaseRepository.findByExerciseId(sourceContainer.getExerciseId());

    ExerciseContext exerciseContext = new ExerciseContextImpl();

    exerciseContext.setTestClasses(testCaseContainer.getTestClasses());

    StringCompiler compiler = new StringCompiler();
    sourceContainer.getSources().forEach(compiler::addCompilationTask);
    exerciseContext.setExerciseClasses(compiler.startCompilation().getCompiledClasses());

    JUnitCore jc = new JUnitCore();
    List<Runner> runners = testCaseContainer.getTestClasses().values()
        .stream()
        .map(clazz -> instanciateRunner(clazz, exerciseContext))
        .collect(Collectors.toList());

    List<TestResult> results = runners.stream()
        .map(jc::run)
        .map(TestResult::new)
        .collect(Collectors.toList());

    return new TestReport(results);
  }

  private LectureRunner instanciateRunner(Class<?> clazz, ExerciseContext container) {
    try {
      return new LectureRunner(clazz, container);
    } catch (InitializationError initializationError) {
      throw new RuntimeException(initializationError);
    }
  }
}
