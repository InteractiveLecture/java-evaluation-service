package org.lecture.unit.test.controller;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lecture.assembler.TestCaseAssembler;
import org.lecture.controller.TestCaseController;
import org.lecture.model.TestCaseContainer;
import org.lecture.repository.TestCaseRepository;
import org.lecture.resource.TestCaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
* Unit test for TestCaseContainer controllers.
* @author Rene Richter
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestControllerUnitTestConfig.class})
public class TestControllerUnitTest {

  @Autowired
  private TestCaseRepository testRepository;

  @Autowired
  private TestCaseAssembler testAssembler;

  @Autowired
  private PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired
  private TestCaseController testInstance;


  /**
   * sets up the test.
   */
  @Before
  public void setUp() {
    reset(testRepository,testAssembler,pagedResourcesAssembler);
  }


  @Test
  public void getOneShouldReturnResponseContainingTheDataOfOneTestAsJson() throws Exception {
    TestCaseContainer instance = new TestCaseContainer();
    instance.setId("1");
    TestCaseResource testResource = new TestCaseResource(instance);
    when(testRepository.findOne("1")).thenReturn(instance);
    when(testAssembler.toResource(instance)).thenReturn(testResource);
    ResponseEntity response = testInstance.getOneTest("1");
    assertEquals(200,response.getStatusCode().value());
    verify(testRepository, times(1)).findOne("1");
    verify(testAssembler, times(1)).toResource(instance);
  }
}