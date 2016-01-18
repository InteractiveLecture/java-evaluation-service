package org.lecture.controller;

/*
* Copyright (c) 2015 .
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


import org.lecture.model.CompilationReport;
import org.lecture.model.FilePatch;
import org.lecture.model.SourceContainer;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.resource.SourceContainerResource;
import org.lecture.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * A controller for TestCaseContainer Routes.
 *
 * @author Rene Richter
 */
@RestController
@RequestMapping("/tests")
public class TestCaseController extends BaseController {

  @Autowired
  SourceContainerRepository testRepository;

  @Autowired
  CompilerService compilerService;


  /**
   * Returns a single testcase container by exercise id.
   *
   * @param taskId The id of the task the testcase container belongs to.
   * @return a Resource representing the testcase container.
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<SourceContainerResource> getByExerciseId(
      @RequestParam("taskId") String taskId) {

    SourceContainer result =
        this.testRepository.findByTaskIdAndTests(taskId,true);

    return ResponseEntity.ok()
        .header("Accept-FilePatch", "application/json;charset=UTF-8")
        .body(new SourceContainerResource(result));
  }

  /**
   * Creates a new TestCaseContainer. This method should get called before the
   * code submission gets send.
   *
   * @param entity    the test from the post-request. This test is deserialized by
   *                  jackson.
   * @return A respoonse containing a compilation-report.
   */
  @RequestMapping( method = RequestMethod.POST)
  public ResponseEntity<?> create(
      @RequestBody SourceContainer entity, HttpServletRequest request) {
    String userId = request.getHeader("User-Id");
    entity.setUserId(userId);
    entity.setTests(true);
    entity = testRepository.save(entity);
    return super.createEntity(entity, "Accept-FilePatch", "application/json;charset=UTF-8");
  }

  /**
   * Returns one TestCaseContainer.
   *
   * @param id the id of the  test to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<SourceContainerResource> getOneTest(@PathVariable String id) {

    SourceContainerResource result
        = new SourceContainerResource(testRepository.findOne(id));

    return ResponseEntity.ok()
        .header("Accept-FilePatch", "application/json;charset=UTF-8")
        .body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {

    testRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH,consumes="application/json;charset=UTF-8")
  public ResponseEntity<?> update(
      @PathVariable String id, @RequestBody List<FilePatch> patches) {

    CompilationReport report =
        compilerService.patchAndCompileTestSource(id, patches);
    if (report == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(report);
  }


}
