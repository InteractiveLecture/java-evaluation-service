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
import org.lecture.model.*;
import org.lecture.patchservice.PatchService;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Implementation of the CompilerService interface.
 * @author Rene Richter
 */
@Service
public class CompilerServiceImpl implements CompilerService {

  private Map<String,SourceContainer> cache = new HashMap<>();

  @Autowired
  private SourceContainerRepository codeSubmissionRepository;

  @Autowired
  private TestCaseRepository testCaseRepository;

  @Autowired
  private PatchService patchService;


  @Override
  public CompilationReport patchAndCompileUserSource(String id, List<FilePatch> patches) {
    SourceContainer submission = cache.get(id);
    CompilationResult compilationResult = patchAndCompile(patches, submission);
    CompilationReport report = createCompilationReport(compilationResult);
    submission.setCompilationReport(report);
    saveAsync(submission);
    return submission.getCompilationReport();
  }

  public void addToCache(SourceContainer container) {
    this.cache.put(container.getId(),container);
  }

  public void removeFromCache(String id) {
    this.cache.remove(id);
  }

  @Override
  public CompilationReport patchAndCompileTestSource(String id, List<FilePatch> patches) {
    TestCaseContainer testContainer = testCaseRepository.findOne(id);
    CompilationResult compilationResult = patchAndCompile(patches, testContainer);
    testContainer.setTestClasses(compilationResult.getCompiledClasses());
    checkTestValidity(testContainer);
    saveAsync(testContainer);
    return testContainer.getCompilationReport();
  }

  private <T extends SourceContainer> CompilationResult patchAndCompile(List<FilePatch> patches,
                                                                        T entity) {
    for (FilePatch patch : patches) {
      String oldSource = entity.getSources().get(patch.getFileName());
      String newSource = patchService.applyPatch(oldSource, patch.getContent());
      entity.addSource(patch.getFileName(), newSource);
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


  private void checkTestValidity(TestCaseContainer container) {
    container.getTestClasses().forEach((className, classObject) -> {
        try {
          Runner runner = new BlockJUnit4ClassRunner(classObject);
        } catch (InitializationError initializationError) {
          CompilationDiagnostic malformedError = new CompilationDiagnostic();
          malformedError.setClassname(className);
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
