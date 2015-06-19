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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lecture.model.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import util.TestUtil;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static util.TestUtil.toJson;


/**
 * A integration test for Tests
 * @author Rene Richter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestIntegrationTestConfig.class})
@WebAppConfiguration
//TODO add custom sample-data in testSampleData.java
public class TestIntegrationTest {

  private MockMvc mockMvc;


  @Autowired
  private WebApplicationContext webApplicationContext;

  /**
   * sets up the test.
   */
  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }


  @Test
  public void testGetTests() throws Exception {

    mockMvc.perform(get("/tests"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON));
  }

  @Test
  public void testGetAll()
      throws Exception {
    mockMvc.perform(get("/tests/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(
            jsonPath("$._links.self.href", is("http://localhost/tests/1")));
  }

  @Test
  public void testCreateTest() throws Exception {
    mockMvc.perform(post("/tests")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(toJson(new TestCase())))
        .andExpect(status().isCreated());
  }
}