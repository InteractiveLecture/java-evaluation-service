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

import org.lecture.assembler.CodeSubmissionAssembler;
import org.lecture.model.CodeSubmission;
import org.lecture.resource.CodeSubmissionResource;
import org.lecture.repository.CodeSubmissionRepository;
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

import java.security.Principal;


/**
 * A controller for CodeSubmission Routes.
 * @author Rene Richter
 */
@RestController
@RequestMapping("/codesubmissions")
@ExposesResourceFor(CodeSubmission.class)
public class CodeSubmissionController extends BaseController {

  @Autowired
  CodeSubmissionAssembler codesubmissionAssembler;

  @Autowired
  CodeSubmissionRepository codesubmissionRepository;

  @Autowired
  CompilerService compilerService;


  /**
   * Returns a list of codesubmissions.
   *
   * @param pageable  The number of items, gotten through the url
   * @param assembler the assembler injected by spring.
   * @return a Resource representing the page.
   */
  @RequestMapping(method = RequestMethod.GET)
  public PagedResources<CodeSubmission> getAll(@PageableDefault(size = 20, page = 0)
                                         Pageable pageable,
                                         PagedResourcesAssembler assembler) {

    Page<CodeSubmission> pageResult = this.codesubmissionRepository.findAll(pageable);
    return assembler.toResource(pageResult, codesubmissionAssembler);
  }

  /**
   * Creates a new CodeSubmission
   * @param entity the codesubmission from the post-request. This codesubmission is deserialized by
   *              jackson.
   * @return A respoonse containing a link to the new resource.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody CodeSubmission entity,Principal principal) {
    entity.setUsername(principal.getName());

    return super.createEntity(this.codesubmissionRepository.save(entity));
  }

  /**
   * Returns one CodeSubmission.
   *
   * @param id the id of the  codesubmission to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<CodeSubmissionResource> getOne(@PathVariable String id) {
    CodeSubmissionResource result
        = codesubmissionAssembler.toResource(codesubmissionRepository.findOne(id));
    return ResponseEntity.ok().body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {
    codesubmissionRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<?> update(@PathVariable String id,
                                  @RequestBody CodeSubmission newValues) {
    newValues.setId(id);
    codesubmissionRepository.save(newValues);
    return ResponseEntity.noContent().build();
  }


}
