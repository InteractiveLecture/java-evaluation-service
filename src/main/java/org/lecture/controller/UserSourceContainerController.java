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

import nats.client.Nats;
import org.lecture.model.SourceContainer;
import org.lecture.model.TestReport;
import org.lecture.repository.SourceContainerRepository;
import org.lecture.resource.TestReportResource;
import org.lecture.service.CompilerService;
import org.lecture.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * A controller for SourceContainer Routes.
 *
 * @author Rene Richter
 */
@RestController
@RequestMapping("/codesubmissions")
@ExposesResourceFor(SourceContainer.class)
public class UserSourceContainerController extends BaseController {


  @Autowired
  SourceContainerRepository codesubmissionRepository;

  @Autowired
  Nats nats;

  @Autowired
  TestService testService;

  @RequestMapping(value = "/{id}/test-report", method = RequestMethod.GET)
  public ResponseEntity<TestReportResource> getTestReport(@PathVariable String id) {
    SourceContainer container = codesubmissionRepository.findOne(id);
    TestReport report = testService.runTests(container);
    if (report.isAllPassed()) {
      nats.publish("finish-task","{\"userId\":\""+ container.getTaskId()+"\",\"taskId\":\""+container.getTaskId()+"\"}");
    }
    return ResponseEntity.ok().body(new TestReportResource(testService.runTests(container)));
  }

}
