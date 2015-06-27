package org.lecture.integration;

/*
 * Copyright (c) 2015 Rene Richter
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

import org.lecture.patchservice.PatchService;
import org.lecture.restclient.AclRestClient;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration class for org.lecture integration test.
 *
 * @author Rene Richter
 */
@Configuration
@ComponentScan(basePackages = {"org.lecture.repository",
    "org.lecture.controller",
    "org.lecture.assembler",
    "org.lecture.resource",
    "org.lecture.integration",
    "org.lecture.service"})

@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
@EnableSpringDataWebSupport
@EnableWebMvc
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "org.lecture.repository")
@EnableTransactionManagement
@Profile("test-server")
public class IntegrationTestConfigBuildServer extends WebMvcConfigurerAdapter {

  @Bean
  public AclRestClient aclRestClient() {
    return Mockito.mock(AclRestClient.class);
  }

  @Bean
  public PatchService patchService() {
    return new PatchService();
  }


}
