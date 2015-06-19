package org.lecture.restclient;

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

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by rene on 18.06.15.
 */
@FeignClient("acl-service")
public interface AclRestClient {

  //TODO anpassen an den tatsächlichen acl-client.
  @RequestMapping(
      method = RequestMethod.GET, value = "/permissions/{classname}/{oid}/write")
  boolean hasWritePermission(@RequestParam("sid") String email,
                             @PathVariable("oid") long id,
                             @PathVariable("classname") String className);

  @RequestMapping(
      method = RequestMethod.GET, value = "/permissions/{classname}/{oid}/read")
  boolean hasReadPermission(@RequestParam("sid") String email,
                            @PathVariable("oid") long id,
                            @PathVariable("classname") String className);

  @RequestMapping(
      method = RequestMethod.GET, value = "/permissions/{classname}/{oid}/delete")
  boolean hasDeletePermission(@RequestParam("sid") String email,
                              @PathVariable("oid") long id,
                              @PathVariable("classname") String className);
}
