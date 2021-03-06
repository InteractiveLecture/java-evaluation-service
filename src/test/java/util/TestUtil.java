package util;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lecture.model.BaseEntity;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * Utility class for testing.
 *
 * @author Rene Richter
 */
public class TestUtil {

  public static final MediaType APPLICATION_JSON_UTF8 =
      new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  /**
   * Converts an object to json (as bytes).
   *
   * @param object the object to convert
   * @return JSON as bytes.
   * @throws JsonProcessingException thrown by jackson objectmapper
   */
  public static byte[] toJson(BaseEntity object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsBytes(object);
  }

}