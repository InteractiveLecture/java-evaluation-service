package org.lecture.integration.test;

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

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.lecture.restclient.AclRestClient;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.Principal;

/**
 * Configuration class for org.lecture integration test.
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
public class TestIntegrationTestConfig extends WebMvcConfigurerAdapter {

  @Bean
  public Mongo mongo() {
    Fongo fongo = new Fongo("test");
    return fongo.getMongo();
   }
    
  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongo(),"TestCaseContainer");
  }

  @Bean
  public AclRestClient aclRestClient() {
    return Mockito.mock(AclRestClient.class);
  }

  @Bean
  public Principal principal() {
    return new Principal() {
      @Override
      public String getName() {
        return "doent@hs-trier.de";
      }
    };
  }
}
