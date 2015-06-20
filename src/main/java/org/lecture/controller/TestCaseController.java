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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.lecture.assembler.TestCaseAssembler;
import org.lecture.model.CompilationReport;
import org.lecture.model.TestCaseContainer;
import org.lecture.repository.TestCaseRepository;
import org.lecture.resource.TestCaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


/**
 * A controller for TestCaseContainer Routes.
 * @author Rene Richter
 */
@RestController
@RequestMapping("/tests")
@ExposesResourceFor(TestCaseContainer.class)
public class TestCaseController extends BaseController {

  @Autowired
  TestCaseAssembler testAssembler;

  @Autowired
  TestCaseRepository testRepository;

  @Autowired
  CompilerService compilerService;


  /**
   * Returns a list of tests.
   *
   * @param pageable  The number of items, gotten through the url
   * @param assembler the assembler injected by spring.
   * @return a Resource representing the page.
   */
  @RequestMapping(method = RequestMethod.GET)
  public PagedResources<TestCaseContainer> getAll(@PageableDefault(size = 20, page = 0)
                                         Pageable pageable,
                                         PagedResourcesAssembler assembler) {

    Page<TestCaseContainer> pageResult = this.testRepository.findAll(pageable);
    return assembler.toResource(pageResult, testAssembler);
  }

  /**
   * Creates a new TestCaseContainer
   * @param entity the test from the post-request. This test is deserialized by
   *              jackson.
   * @return A respoonse containing a compilation-report.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<CompilationReport> create(@RequestBody TestCaseContainer entity,
                                                  Principal principal) {
    entity.setUsername(principal.getName());
    TestCaseContainer testCase = compilerService.compileTestCase(entity);
    return ResponseEntity.created(
        linkTo(TestCaseController.class).slash(testCase.getId()).toUri())
        .body(testCase.getCompilationReport());
  }

  /**
   * Returns one TestCaseContainer.
   *
   * @param id the id of the  test to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<TestCaseResource> getOne(@PathVariable String id) {
    TestCaseResource result
        = testAssembler.toResource(testRepository.findOne(id));
    return ResponseEntity.ok().body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {
    testRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<CompilationReport> update(@PathVariable String id,
                                  @RequestBody TestCaseContainer newValues) {
    newValues.setId(id);
    TestCaseContainer testCase = compilerService.compileTestCase(newValues);
    return ResponseEntity.ok().body(testCase.getCompilationReport());
  }


}
