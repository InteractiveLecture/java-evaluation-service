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

import org.lecture.assembler.CompilationReportAssembler;
import org.lecture.model.CompilationReport;
import org.lecture.resource.CompilationReportResource;
import org.lecture.repository.CompilationReportRepository;
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
 * A controller for CompilationReport Routes.
 * @author Rene Richter
 */
@RestController
@RequestMapping("/compilationreports")
@ExposesResourceFor(CompilationReport.class)
public class CompilationReportController extends BaseController {

  @Autowired
  CompilationReportAssembler compilationreportAssembler;

  @Autowired
  CompilationReportRepository compilationreportRepository;


  /**
   * Returns a list of compilationreports.
   *
   * @param pageable  The number of items, gotten through the url
   * @param assembler the assembler injected by spring.
   * @return a Resource representing the page.
   */
  @RequestMapping(method = RequestMethod.GET)
  public PagedResources<CompilationReport> getAll(@PageableDefault(size = 20, page = 0)
                                         Pageable pageable,
                                         PagedResourcesAssembler assembler) {

    Page<CompilationReport> pageResult = this.compilationreportRepository.findAll(pageable);
    return assembler.toResource(pageResult, compilationreportAssembler);
  }

  /**
   * Creates a new CompilationReport
   * @param entity the compilationreport from the post-request. This compilationreport is deserialized by
   *              jackson.
   * @return A respoonse containing a link to the new resource.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody CompilationReport entity) {
    return super.createEntity(this.compilationreportRepository.save(entity));
  }

  /**
   * Returns one CompilationReport.
   *
   * @param id the id of the  compilationreport to return.
   * @return a response.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<CompilationReportResource> getOne(@PathVariable String id) {
    CompilationReportResource result
        = compilationreportAssembler.toResource(compilationreportRepository.findOne(id));
    return ResponseEntity.ok().body(result);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable String id) {
    compilationreportRepository.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<?> update(@PathVariable String id,
                                  @RequestBody CompilationReport newValues) {
    newValues.setId(id);
    compilationreportRepository.save(newValues);
    return ResponseEntity.noContent().build();
  }


}
