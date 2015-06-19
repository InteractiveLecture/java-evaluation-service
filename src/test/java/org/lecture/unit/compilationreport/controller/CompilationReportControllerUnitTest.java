package org.lecture.unit.compilationreport.controller;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.lecture.resource.CompilationReportResource;
import org.lecture.model.CompilationReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

/**
* Unit test for CompilationReport controllers.
* @author Rene Richter
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CompilationReportControllerUnitTestConfig.class})
public class CompilationReportControllerUnitTest {

  @Autowired
  private CompilationReportRepository compilationreportRepository;

  @Autowired
  private CompilationReportAssembler compilationreportAssembler;

  @Autowired
  private PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired
  private CompilationReportController testInstance;


  /**
   * sets up the test.
   */
  @Before
  public void setUp() {
    reset(compilationreportRepository,compilationreportAssembler,pagedResourcesAssembler);
  }


  @Test
  public void getAllShouldReturnAPageOfCompilationReport() throws Exception {

    List<CompilationReport> sampleData = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      CompilationReport instance = new CompilationReport();
      instance.setId(String.valueOf(i));
      sampleData.add(instance);
    }
    Page<CompilationReport> page = new PageImpl<>(sampleData);
    when(compilationreportRepository.findAll(any(Pageable.class))).thenReturn(page);
    when(pagedResourcesAssembler.toResource(page,compilationreportAssembler))
      .thenReturn(new PagedResources(sampleData,null));

    Pageable pageable = new PageRequest(2,2);
    PagedResources result = testInstance.getAll(pageable,pagedResourcesAssembler);
    assertEquals(10,result.getContent().size());
    verify(compilationreportRepository, times(1)).findAll(any(Pageable.class));
    verify(pagedResourcesAssembler,times(1)).toResource(eq(page),eq(compilationreportAssembler));
    verifyNoMoreInteractions(compilationreportRepository);
    verifyNoMoreInteractions(compilationreportAssembler);
    verifyNoMoreInteractions(pagedResourcesAssembler);
  }

  @Test
  public void getOneShouldReturnResponseContainingTheDataOfOneCompilationReportAsJson() throws Exception {
    CompilationReport instance = new CompilationReport();
    instance.setId(String.valueOf(1));
    CompilationReportResource testResource = new CompilationReportResource(instance);
    when(compilationreportRepository.findOne(String.valueOf(1))).thenReturn(instance);
    when(compilationreportAssembler.toResource(instance)).thenReturn(testResource);
    ResponseEntity response = testInstance.getOne(String.valueOf(1));
    assertEquals(200,response.getStatusCode().value());
    verify(compilationreportRepository, times(1)).findOne(String.valueOf(1));
    verify(compilationreportAssembler, times(1)).toResource(instance);
  }
}