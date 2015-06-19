package org.lecture.unit.compilationreport.assembler;

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

import static org.junit.Assert.assertEquals;

import org.lecture.assembler.CompilationReportAssembler;
import org.lecture.model.CompilationReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Unit test for CompilationReport assemblers.
 * @author Rene Richter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CompilationReportAssemblerUnitTestConfig.class})
@WebAppConfiguration
public class CompilationReportAssemblerUnitTest {


  @Autowired
  private CompilationReportAssembler testInstance;


  @Test
  public void testToResource() throws Exception {
    CompilationReport instance = new CompilationReport();
    instance.setId(String.valueOf(1));
    ResourceSupport resourceSupport = testInstance.toResource(instance);
    assertEquals("self",resourceSupport.getId().getRel());
  }
}