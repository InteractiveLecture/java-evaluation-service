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
import org.lecture.model.TestCaseContainer;
import org.lecture.repository.TestCaseRepository;
import org.lecture.resource.TestCaseResource;
import org.lecture.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


/**
 * A controller for TestCaseContainer Routes.
 *
 * @author Rene Richter
 */
@RestController
@RequestMapping("/tests")
@ExposesResourceFor(TestCaseContainer.class)
public class TestCaseController extends BaseController {

  @Autowired
  TestCaseRepository testRepository;

  @Autowired
  CompilerService compilerService;


  /**
   * Returns a single testcase container by exercise id.
   *
   * @param taskId The id of the task the testcase container belongs to.
   * @return a Resource representing the testcase container.
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<TestCaseResource> getByExerciseId(
      @RequestParam("taskId") String taskId) {

    TestCaseContainer result =
        this.testRepository.findByTaskId(taskId);

    return ResponseEntity.ok()
        .header("Accept-Patch", "text/mdmp")
        .body(new TestCaseResource(result));
  }

  /**
   * Creates a new TestCaseContainer. This method should get called before the
   * code submission gets send.
   *
   * @param entity    the test from the post-request. This test is deserialized by
   *                  jackson.
   * @param principal The current user injected by spring.
   * @return A respoonse containing a compilation-report.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(
      @RequestBody TestCaseContainer entity, Principal principal) {

    entity.setUserId(principal.getName());
    entity = testRepository.save(entity);
    return super.createEntity(entity, "Accept-Patch", "application/mdmp");
  }

  /**
   * Returns one TestCaseContainer.
   *
   * @param id the id of the  test to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<TestCaseResource> getOneTest(@PathVariable String id) {

    TestCaseResource result
        = new TestCaseResource(testRepository.findOne(id));
    return ResponseEntity.ok()
        .header("Accept-Patch", "application/mdmp")
        .body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {

    testRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public ResponseEntity<CompilationReport> update(
      @PathVariable String id, @RequestBody String rawPatch) {

    String[] patchParts = rawPatch.split("\n\\+\\+\\+\n");
    CompilationReport report =
        compilerService.patchAndCompileTestSource(id, patchParts);

    return ResponseEntity.ok().body(report);
  }


  @RequestMapping(value = "/{id}/active", method = RequestMethod.GET)
  public ResponseEntity<Boolean> isActive(String id) {

    TestCaseContainer container = this.testRepository.findOne(id);
    return ResponseEntity.ok(container.isActive());
  }

  @RequestMapping(value = "/{id}/active", method = RequestMethod.PUT)
  public ResponseEntity<?> setActive(String id, @RequestBody boolean active) {

    TestCaseContainer container = this.testRepository.findOne(id);
    container.setActive(active);
    this.testRepository.save(container);
    return ResponseEntity.noContent().build();
  }


}
