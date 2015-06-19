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
import org.lecture.model.TestCase;
import org.lecture.repository.TestCaseRepository;
import org.lecture.resource.TestCaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
* Unit test for TestCase controllers.
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
  public void getAllShouldReturnAPageOfTest() throws Exception {

    List<TestCase> sampleData = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      TestCase instance = new TestCase();
      instance.setId(""+i);
      sampleData.add(instance);
    }
    Page<TestCase> page = new PageImpl<>(sampleData);
    when(testRepository.findAll(any(Pageable.class))).thenReturn(page);
    when(pagedResourcesAssembler.toResource(page,testAssembler))
      .thenReturn(new PagedResources(sampleData,null));

    Pageable pageable = new PageRequest(2,2);
    PagedResources result = testInstance.getAll(pageable,pagedResourcesAssembler);
    assertEquals(10,result.getContent().size());
    verify(testRepository, times(1)).findAll(any(Pageable.class));
    verify(pagedResourcesAssembler,times(1)).toResource(eq(page),eq(testAssembler));
    verifyNoMoreInteractions(testRepository);
    verifyNoMoreInteractions(testAssembler);
    verifyNoMoreInteractions(pagedResourcesAssembler);
  }

  @Test
  public void getOneShouldReturnResponseContainingTheDataOfOneTestAsJson() throws Exception {
    TestCase instance = new TestCase();
    instance.setId("1");
    TestCaseResource testResource = new TestCaseResource(instance);
    when(testRepository.findOne("1")).thenReturn(instance);
    when(testAssembler.toResource(instance)).thenReturn(testResource);
    ResponseEntity response = testInstance.getOne("1");
    assertEquals(200,response.getStatusCode().value());
    verify(testRepository, times(1)).findOne("1");
    verify(testAssembler, times(1)).toResource(instance);
  }
}