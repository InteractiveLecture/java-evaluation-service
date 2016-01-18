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

  public CompilationResult compileSources(Map<String,String> sources) {
    StringCompiler compiler = new StringCompiler();
    sources.forEach(compiler::addCompilationTask);
    return compiler.startCompilation();
  }

  @Override
  public CompilationReport patchAndCompileTestSource(String id, List<FilePatch> patches) {
    SourceContainer testContainer = codeSubmissionRepository.findOne(id);
    CompilationResult compilationResult = patchAndCompile(patches, testContainer);
    if (compilationResult == null){
      deleteAsync(id);
      return null;
    }
    testContainer.setTestClasses(compilationResult.getCompiledClasses());
    testContainer.setCompilationReport(createCompilationReport(compilationResult));
    checkTestValidity(testContainer);
    saveAsync(testContainer);
    return testContainer.getCompilationReport();
  }

  private void cleanEmptySources(SourceContainer container) {
    container.setSources(container.getSources().entrySet().stream()
        .filter(entry -> !entry.getValue().equals(""))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  private <T extends SourceContainer> CompilationResult patchAndCompile(List<FilePatch> patches,
                                                                        T entity) {
    for (FilePatch patch : patches) {
      String oldSource = entity.getSources().get(patch.getFileName()) != null
          ? entity.getSources().get(patch.getFileName())
          : "";
      String newSource = patchService.applyPatch(oldSource, patch.getContent());
      entity.addSource(patch.getFileName(), newSource);
    }
    cleanEmptySources(entity);

    //return if nothing left to compile
    if (entity.getSources().isEmpty())
      return null;
    else
      return this.compileSources(entity.getSources());
  }

  private CompilationReport createCompilationReport(CompilationResult result) {

    CompilationReport compilationReport = new CompilationReport();
    compilationReport.setDate(LocalDateTime.now());
    if (result.hasErrors())
      compilationReport.setErrors(result.getErrors()
        .stream()
        .map(CompilationDiagnostic::new)
        .collect(Collectors.toList()));
    if (result.hasWarnings())
      compilationReport.setWarnings(result.getWarnings()
        .stream()
        .map(CompilationDiagnostic::new)
        .collect(Collectors.toList()));

    return compilationReport;

  }


  private void checkTestValidity(SourceContainer container) {
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
  private void deleteAsync(String id) {
    System.out.println("deleting: "+id);
    codeSubmissionRepository.delete(id);
  }


}
