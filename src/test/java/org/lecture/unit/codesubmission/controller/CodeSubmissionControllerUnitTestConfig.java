package org.lecture.unit.codesubmission.controller;

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
import org.lecture.repository.SourceContainerRepository;
import org.lecture.controller.UserSourceContainerController;
import org.lecture.service.CompilerService;
import org.lecture.service.TestService;
import org.mockito.Mockito;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
* Configuration for SourceContainer controller unit tests.
* @author Rene Richter
*/
@Configuration
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class CodeSubmissionControllerUnitTestConfig {


  @Bean
  public UserSourceContainerController codesubmissionController() {
    return new UserSourceContainerController();
  }

  @Bean
  public LoadBalancerClient loadBalancerClient() {
    return  Mockito.mock(LoadBalancerClient.class);
  }

  @Bean
  public SourceContainerRepository codesubmissionRepository() {
    return Mockito.mock(SourceContainerRepository.class);
  }

  @Bean
  public SourceContainerAssembler codesubmissionAssembler() {
    return Mockito.mock(SourceContainerAssembler.class);
  }

  @Bean
  public PagedResourcesAssembler pagedResourcesAssembler() {
    return Mockito.mock(PagedResourcesAssembler.class);
  }

  @Bean
  public CompilerService compilerService() {
    return Mockito.mock(CompilerService.class);
  }


  @Bean
  public TestService testService() {
    return Mockito.mock(TestService.class);
  }

}