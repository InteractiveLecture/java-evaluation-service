package org.lecture.config;

/*
* Copyright (c) 2015 Rene Richter.
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
import nats.client.NatsConnector;
import org.lecture.controller.UserSourceContainerController;
import org.lecture.controller.UserSourceHandler;
import org.lecture.patchservice.PatchService;
import org.lecture.patchservice.dmp.DmpPatchService;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Application config-class.
 *
 * @author Rene Richter
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.lecture"})
@EnableMongoRepositories(basePackages = "org.lecture.repository")
@EntityScan(basePackages = "org.lecture.repository")
@EnableSpringDataWebSupport
@EnableFeignClients
@EnableWebSocket
public class AppConfig  implements WebSocketConfigurer {
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public PatchService patchService() {
    return new DmpPatchService();
  }


  @Bean
  public UserSourceHandler userSourceHandler() {
    return new UserSourceHandler();
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(userSourceHandler(),"/user-compiler");
  }

  @Bean
  public Nats nats(){
    return new NatsConnector().addHost("nats://nats:4222").connect();
  }
}
