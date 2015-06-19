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

import org.lecture.assembler.TestAssembler;
import org.lecture.model.CompilationReport;
import org.lecture.model.TestCase;
import org.lecture.repository.TestCaseRepository;
import org.lecture.resource.TestResource;
import org.lecture.service.CompilerService;
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


/**
 * A controller for TestCase Routes.
 * @author Rene Richter
 */
@RestController
@RequestMapping("/tests")
@ExposesResourceFor(TestCase.class)
public class TestController extends BaseController {

  @Autowired
  TestAssembler testAssembler;

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
  public PagedResources<TestCase> getAll(@PageableDefault(size = 20, page = 0)
                                         Pageable pageable,
                                         PagedResourcesAssembler assembler) {

    Page<TestCase> pageResult = this.testRepository.findAll(pageable);
    return assembler.toResource(pageResult, testAssembler);
  }

  /**
   * Creates a new TestCase
   * @param entity the test from the post-request. This test is deserialized by
   *              jackson.
   * @return A respoonse containing a link to the new resource.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody TestCase entity) {
    CompilationReport report = compilerService.compileTestSource(entity.getTestCode(),
        entity.getClassname(), entity.getUsername(), entity.getExerciseId());


    return super.createEntity(this.testRepository.save(entity));
  }

  /**
   * Returns one TestCase.
   *
   * @param id the id of the  test to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<TestResource> getOne(@PathVariable String id) {
    TestResource result
        = testAssembler.toResource(testRepository.findOne(id));
    return ResponseEntity.ok().body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {
    testRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<?> update(@PathVariable String id,
                                  @RequestBody TestCase newValues) {
    newValues.setId(id);
    testRepository.save(newValues);
    return ResponseEntity.noContent().build();
  }


}
