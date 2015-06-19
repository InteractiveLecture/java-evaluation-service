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
  public CompilationReport compileUserSource(CodeSubmission submission) {
    //String source, String className,
     //   String username, long exerciseId
    CodeSubmission previousSubmission = codeSubmissionRepository
        .findOneByUsernameAndExerciseIdOrderBySubmissionDate(
            submission.getUsername(),submission.getExerciseId());

    if(previousSubmission != null) {
      submission.getSources().forEach(previousSubmission::addSubmission);
      submission = previousSubmission;
    }

    StringCompiler compiler = new StringCompiler();
    submission.getSources().forEach(compiler::addCompilationTask);

    CompilationResult compilationResult = compiler.startCompilation();

    CompilationReport report = createCompilationReport(
        compilationResult,submission.getUsername(),submission.getExerciseId());

    

    //TODO if performance issues, make call async.
    codeSubmissionRepository.save(submission);

    return report;
  }

  @Override
  public TestCase compileTestCase(TestCase testCase) {

    //TODO in aspket umwandeln.
    boolean hasPermission = aclClient.hasWritePermission(
        testCase.getUsername(),testCase.getExerciseId(),"exercise");

    if(!hasPermission) {
      throw new RuntimeException(testCase.getUsername()
          + " has no permissions to write tests for exercise with id "+testCase.getExerciseId());
    }

    StringCompiler compiler = new StringCompiler();

    compiler.addCompilationTask(testCase.getClassname(),testCase.getTestCode());

    CompilationResult compilationResult = compiler.startCompilation();

    CompilationReport report = createCompilationReport(
        compilationResult,testCase.getUsername(),testCase.getExerciseId());
    testCase.setCompilationReport(report);
    testCase.setTestClass(
        compilationResult.getCompiledClasses().get(testCase.getClassname()));
    testCase.setActive(!compilationResult.hasErrors());
    return testCaseRepository.save(testCase);
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
