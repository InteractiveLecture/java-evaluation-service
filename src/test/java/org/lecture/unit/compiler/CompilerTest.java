
package org.lecture.unit.compiler;

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

import org.junit.Before;
import org.junit.Test;
import org.lecture.compiler.compiler.CompilationResult;
import org.lecture.compiler.compiler.StringCompiler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompilerTest 
{


    
    StringCompiler sc;
    String code;

    @Before
    public void reset()
    {
        System.out.println("preparing test...");
        this.sc = new StringCompiler();
        code = "import java.util.function.Function; public class Hugo{ public static int bla(){Function<Integer,Integer>f = (x)->x*x; return f.apply(5);}}";
    }
    
    @Test
    public void compileNewClassFromString() 
    {    
        sc.addCompilationTask("Hugo", code);
        CompilationResult cr = sc.startCompilation();
        assertNotNull("the class should get loaded", cr.getCompiledClasses().get("Hugo"));
    }
    
    
    @Test
    public void testHasErrors()
    {
        code=code+"adfasdfasdf";
        sc.addCompilationTask("Hugo", code);
        CompilationResult result = sc.startCompilation();
        assertTrue("The CompilationResult-Object should record errors if incorrect code was compiled",result.hasErrors());
    }
}
    
