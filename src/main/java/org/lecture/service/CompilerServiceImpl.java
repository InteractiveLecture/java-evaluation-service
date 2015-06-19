package org.lecture.service;

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

import org.lecture.compiler.compiler.CompilationResult;
import org.lecture.compiler.compiler.StringCompiler;
import org.lecture.model.CodeSubmission;
import org.lecture.model.CompilationDiagnostic;
import org.lecture.model.CompilationReport;
import org.lecture.model.TestCase;
import org.lecture.repository.CodeSubmissionRepository;
import org.lecture.repository.TestCaseRepository;
import org.lecture.restclient.AclRestClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


/**
 * @author Rene Richter
 */
public class CompilerServiceImpl implements CompilerService {

  @Autowired
  private CodeSubmissionRepository codeSubmissionRepository;

  @Autowired
  private TestCaseRepository testCaseRepository;

  @Autowired
  private AclRestClient aclClient;


  @Override
  public CompilationReport compileUserSource(String source, String className,
                                             String username, long exerciseId) {

    CodeSubmission submission = codeSubmissionRepository
        .findOneByUsernameAndExerciseIdOrderBySubmissionDate(username,exerciseId);

    if(submission == null) {
      submission = new CodeSubmission(username,className,source,exerciseId);
    } else {
      submission.addSubmission(className, source);
    }

    StringCompiler compiler = new StringCompiler();
    submission.getSources().forEach(compiler::addCompilationTask);

    CompilationResult compilationResult = compiler.startCompilation();

    CompilationReport report = createCompilationReport(
        compilationResult,username,exerciseId);

    //TODO if performance issues, make call async.
    codeSubmissionRepository.save(submission);

    return report;
  }

  @Override
  public CompilationReport compileTestSource(String source, String className,
                                             String username, long exerciseId) {

    //TODO in aspket umwandeln.
    boolean hasPermission = aclClient.hasWritePermission(username,exerciseId,"exercise");

    if(!hasPermission) {
      throw new RuntimeException(username
          + " has no permissions to write tests for exercise with id "+exerciseId);
    }

    StringCompiler compiler = new StringCompiler();

    compiler.addCompilationTask(className,source);

    CompilationResult compilationResult = compiler.startCompilation();

    CompilationReport report = createCompilationReport(
        compilationResult,username,exerciseId);

    if(!compilationResult.hasErrors()) {
      Class<?> testClass = compilationResult.getCompiledClasses().get(className);
      TestCase testCase = new TestCase(exerciseId,className,source,testClass,username);
      testCaseRepository.save(testCase);
    }
    return report;
  }


  private CompilationReport createCompilationReport(CompilationResult result,
                                                    String username,
                                                    long exerciseId) {

    CompilationReport compilationReport = new CompilationReport();
    compilationReport.setDate(LocalDateTime.now());
    compilationReport.setErrors(result.getErrors()
        .stream()
        .map(CompilationDiagnostic::new)
        .collect(Collectors.toList()));
    compilationReport.setWarnings(result.getWarnings()
        .stream()
        .map(CompilationDiagnostic::new)
        .collect(Collectors.toList()));
    compilationReport.setUsername(username);
    compilationReport.setExerciseId(exerciseId);

    return compilationReport;

  }






}
