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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lecture.assembler.TestCaseAssembler;
import org.lecture.model.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for TestCase assemblers.
 * @author Rene Richter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAssemblerUnitTestConfig.class})
@WebAppConfiguration
public class TestAssemblerUnitTest {


  @Autowired
  private TestCaseAssembler testInstance;


  @Test
  public void testToResource() throws Exception {
    TestCase instance = new TestCase();
    instance.setId("1");
    ResourceSupport resourceSupport = testInstance.toResource(instance);
    assertEquals("self",resourceSupport.getId().getRel());
  }
}