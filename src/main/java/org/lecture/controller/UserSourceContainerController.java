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

import org.lecture.assembler.SourceContainerAssembler;
import org.lecture.model.CompilationReport;
import org.lecture.model.SourceContainer;
import org.lecture.model.TestReport;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.resource.SourceContainerResource;
import org.lecture.service.CompilerService;
import org.lecture.service.TestService;
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
 * A controller for SourceContainer Routes.
 * @author Rene Richter
 */
@RestController
@RequestMapping("/codesubmissions")
@ExposesResourceFor(SourceContainer.class)
public class UserSourceContainerController extends BaseController {

  @Autowired
  SourceContainerAssembler codesubmissionAssembler;

  @Autowired
  SourceContainerRepository codesubmissionRepository;

  @Autowired
  CompilerService compilerService;

  @Autowired
  TestService testService;

  /**
   * Returns a single source container by exercise id.
   *
   * @param exerciseId  The id of the exercise the source container belongs to.
   * @param principal  the logged in user injected by spring.
   * @return a Resource representing the source container.
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<SourceContainerResource> getAll(
      @RequestParam("exerciseId")long exerciseId, Principal principal) {

    String username = principal.getName();
    SourceContainer result =
        this.codesubmissionRepository.findByUsernameAndExerciseId(username, exerciseId);

    return ResponseEntity.ok()
        .header("Accept-Patch", "text/mdmp")
        .body(codesubmissionAssembler.toResource(result));
  }

  /**
   * Creates a new SourceContainer
   * @param entity the codesubmission from the post-request. This codesubmission is deserialized by
   *              jackson.
   * @param principal the logged in user injected by spring.
   * @return A respoonse containing a link to the new resource.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody SourceContainer entity,Principal principal) {
    entity.setUsername(principal.getName());
    return super.createEntity(this.codesubmissionRepository.save(entity));
  }

  /**
   * Returns one SourceContainer.
   *
   * @param id the id of the  codesubmission to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<SourceContainerResource> getOne(@PathVariable String id) {
    SourceContainerResource result
        = codesubmissionAssembler.toResource(codesubmissionRepository.findOne(id));
    return ResponseEntity.ok().body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {
    codesubmissionRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  public ResponseEntity<?> update(@PathVariable String id,
                                  @RequestBody String rawPatch) {

    String [] patchParts = rawPatch.split("\\+\\+\\+\n");
    CompilationReport report = compilerService.patchAndCompileUserSource(id, patchParts);

    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}/test-report", method = RequestMethod.GET)
  public ResponseEntity<TestReport> getTestReport(@PathVariable String id) {
    SourceContainer container = codesubmissionRepository.findOne(id);
    return ResponseEntity.ok().body(testService.runTests(container));
  }




}
