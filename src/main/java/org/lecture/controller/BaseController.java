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

import org.apache.commons.lang3.ArrayUtils;
import org.lecture.model.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
* A basecontroller for shared functionality.
*
* @author Rene Richter
*/
@Component
public abstract class BaseController {

  @Autowired
  EntityLinks entityLinks;
  /**
   * A conviniencemethod for creating new Entities.
   *
   * @param newEntity The entity that should be created
   * @param <T>       Type of the entity that should get created
   * @param headers   Additional headers with the pattern k,v... .
   * @return The formal Response for the childcontroller.
   */
  public <T extends BaseEntity> ResponseEntity<?> createEntity(T newEntity,String... headers) {
    ResponseEntity.BodyBuilder responseEntityBuilder = ResponseEntity
        .created(entityLinks.linkForSingleResource(newEntity).toUri());
    if(headers.length > 0) {
      for(int i = 0; i < headers.length; i = i+2) {
        responseEntityBuilder.header(headers[i], headers[i + 1]);
      }
    }
    return responseEntityBuilder.build();
  }


  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound(ResourceNotFoundException ex) {
   return ex.toString();
  }
}
