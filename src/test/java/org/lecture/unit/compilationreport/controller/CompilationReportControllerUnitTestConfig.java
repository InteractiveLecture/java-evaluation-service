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

import org.mockito.Mockito;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
* Configuration for CompilationReport controller unit tests.
* @author Rene Richter
*/
@Configuration
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class CompilationReportControllerUnitTestConfig {


  @Bean
  public CompilationReportController compilationreportController() {
    return new CompilationReportController();
  }

  @Bean
  public LoadBalancerClient loadBalancerClient() {
    return  Mockito.mock(LoadBalancerClient.class);
  }

  @Bean
  public CompilationReportRepository compilationreportRepository() {
    return Mockito.mock(CompilationReportRepository.class);
  }

  @Bean
  public CompilationReportAssembler compilationreportAssembler() {
    return Mockito.mock(CompilationReportAssembler.class);
  }

  @Bean
  public PagedResourcesAssembler pagedResourcesAssembler() {
    return Mockito.mock(PagedResourcesAssembler.class);
  }


}