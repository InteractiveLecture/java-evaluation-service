package org.lecture.unit.test.assembler;

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

import org.lecture.assembler.TestCaseAssembler;
import org.mockito.Mockito;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
* Configuration class for TestCase unit tests.
* @author Rene Richter
*/
@Configuration
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class TestAssemblerUnitTestConfig {

  @Bean
  public LoadBalancerClient loadBalancerClient() {
    return  Mockito.mock(LoadBalancerClient.class);
  }

  @Bean
  public TestCaseAssembler testAssembler() {
    return new TestCaseAssembler();
  }
}