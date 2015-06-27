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

import compiler.compiler.CompilationResult;
import compiler.compiler.StringCompiler;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.lecture.model.CompilationDiagnostic;
import org.lecture.model.CompilationReport;
import org.lecture.model.SourceContainer;
import org.lecture.model.TestCaseContainer;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.repository.TestCaseRepository;
import org.lecture.restclient.AclRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


/**
 * @author Rene Richter
 */
@Service
public class CompilerServiceImpl implements CompilerService {

  @Autowired
  private SourceContainerRepository codeSubmissionRepository;

  @Autowired
  private TestCaseRepository testCaseRepository;

  @Autowired
  private PatchService patchService;

  @Autowired
  private AclRestClient aclClient;

  @Override
  public CompilationReport patchAndCompileUserSource(String id, String[] patches) {
    SourceContainer submission = codeSubmissionRepository.findOne(id);
    CompilationResult compilationResult = patchAndCompile(patches,submission);
    CompilationReport report = createCompilationReport(compilationResult);
    submission.setCompilationReport(report);
    saveAsync(submission);
    return submission.getCompilationReport();
  }

  @Override
  public CompilationReport patchAndCompileTestSource(String id, String[] patches) {
    TestCaseContainer testContainer = testCaseRepository.findOne(id);
    CompilationResult compilationResult = patchAndCompile(patches,testContainer);
    testContainer.setTestClasses(compilationResult.getCompiledClasses());
    checkTestValidity(testContainer);
    saveAsync(testContainer);
    return testContainer.getCompilationReport();
  }

  private <T extends SourceContainer> CompilationResult patchAndCompile(String[] patches,T entity) {
    for (String patch : patches) {
      String[] parsedPatch = parsePatch(patch);
      String oldSource = entity.getSources().get(parsedPatch[0]);
      String newSource = patchService.patch(oldSource,parsedPatch[1]);
      entity.addSource(parsedPatch[0], newSource);
    }
    StringCompiler compiler = new StringCompiler();
    entity.getSources().forEach(compiler::addCompilationTask);
    return compiler.startCompilation();

  }

  private CompilationReport createCompilationReport(CompilationResult result) {

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

    return compilationReport;

  }

  // 0 = classname, 1 = patch
  private String[] parsePatch(String patch) {
    BufferedReader br = new BufferedReader(new StringReader(patch));
    String[] parsedPatch = new String[2];
    try {
      parsedPatch[0] = br.readLine();
      parsedPatch[1] = br.lines().collect(Collectors.joining());
    } catch (IOException e) {
      throw new RuntimeException(
          "Well... you are the first person who got an IOException from a "
              + "StringReader. Congratulations!");
    }
    return parsedPatch;
  }

  private void checkTestValidity(TestCaseContainer container) {
    container.getTestClasses().forEach((k, v) -> {
      try {
        Runner runner = new BlockJUnit4ClassRunner(v);
      } catch (InitializationError initializationError) {
        CompilationDiagnostic malformedError = new CompilationDiagnostic();
        malformedError.setClassname(k);
        malformedError.setMessage("Not a valid JUnit test.");
        container.getCompilationReport().addError(malformedError);
      }
    });

  }

  @Async
  private void saveAsync(SourceContainer submission) {
    codeSubmissionRepository.save(submission);
  }

  @Async
  private void saveAsync(TestCaseContainer testCase) {
    testCaseRepository.save(testCase);
  }







}
