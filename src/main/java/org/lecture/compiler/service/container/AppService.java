/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lecture.compiler.service.container;

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


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.lecture.compiler.compiler.CompilationResult;
import org.lecture.compiler.compiler.StringCompiler;
import org.lecture.compiler.compiler.StringJavaFileObject;
import org.lecture.compiler.service.api.ExerciseContainer;
import org.lecture.compiler.service.api.SourceContainer;
import org.lecture.compiler.service.utils.Utils;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author rene
 */
//TODO sicherstellen, dass kein systemout erfolgt, damit die testfälle auch system.out abfragen können
public class AppService 
{
    private final ExerciseContainer acc;
    
    public AppService(String exerciseId)
    {
        this.acc = ContainerRegistry.getInstance().addContainerIfAbsent(exerciseId);
        
    }
    
    public void removeContainer(String id)
    {
        ContainerRegistry.getInstance().removeContainer(id);
    }
    
    
    public CompilationResult compileExercise(int id, String source)
    {
        
        CompilationResult result = compileSource(id,source,this.acc.getUserSources());
        if(result == null) return null;
        this.acc.setExerciseClasses(result.getCompiledClasses());
        return result;
    }
    
    public CompilationResult compileTest(int id, String source)
    {
        
        CompilationResult result = compileSource(id,source,this.acc.getTestSources());
        if(result == null) return null;
        this.acc.setTestClasses(result.getCompiledClasses());
        return result;
    }
    
    
    private CompilationResult compileSource(int id,String source,Map<Integer,SourceContainer> scopedSources)
    {
       
       String className = Utils.getClassName(source);
       if(className == null) return null;
       scopedSources.put(id, new SourceContainerImpl(id,className,source));
       StringCompiler sc = new StringCompiler();
       scopedSources.forEach((cn,sourceContainer)->sc.addCompilationTask(sourceContainer.getClassName(), sourceContainer.getSource()));
       CompilationResult result = sc.startCompilation();
       if(result.hasErrors())
       {
           List<Diagnostic> decoratedErrors = decorateErrors(result.getErrors(), scopedSources);
           result.getDiagnostics().put(Kind.ERROR, decoratedErrors);
           
       }
       return result;
    }
    
    private Failure[] executeTestsForFailures()
    {
        JUnitCore jc = new JUnitCoreProxy(new AppContainerDiscoveryServiceImpl());
        Result result = jc.run(acc.getTestClasses().values().toArray(new Class<?>[1]));
        Failure[] failures = new Failure[result.getFailureCount()];
        if(!result.wasSuccessful())
            failures = result.getFailures().toArray(new Failure[result.getFailureCount()]);
        
        return failures;
    }
    
    public String executeTestsForFailureJson()
    {
        try
        {
             Failure[] failures = executeTestsForFailures();
              return Arrays.stream(failures)
            .map((fail)->Json.createObjectBuilder()
                .add("message", fail.getMessage() != null ? fail.getMessage() : "")
                .add("exception", fail.getException().toString())
                .add("method", fail.getDescription().getMethodName())
                .add("class", fail.getDescription().getClassName()))
            .collect(()->Json.createArrayBuilder(), (arrayBuilder,object)-> arrayBuilder.add(object)  , (builder,builder2)-> builder2.build().forEach((value)->builder.add(value))).build().toString();
            
        }
        catch(RuntimeException e)
        {
            return Json.createObjectBuilder()
                    .add("message","there was an unexpected error while testing your code. Please contact your dozent. It is likely he made some mistakes while writing the test :)")
                    .add("exception",e.toString())
                    .add("method", "unknown")
                    .add("class", "unknown").build().toString();
        }
       
    }
    
    
    private String compilationResultToJson(CompilationResult compilationResult)
    {
        if(compilationResult == null)
        {
            return Json.createObjectBuilder().add("errors",Json.createArrayBuilder().add(Json.createObjectBuilder()
                    .add("class", "unknown")
                    .add("row", 0)
                    .add("column",0)
                    .add("message","not a valid class definition")
                    .add("type", "error"))).build().toString();
        }
            
        
        
        if (compilationResult.hasErrors())
        {
            return Json.createObjectBuilder().add("errors",compilationResult.getErrors().stream().map((d)->
                    Json.createObjectBuilder()
                    .add("tabId", ((TabDiagnostic)d).getTabId())
                    .add("class",((StringJavaFileObject)d.getSource()).className)
                    .add("row", d.getLineNumber()-1)
                    .add("column",d.getColumnNumber()-1)
                    .add("text", d.getMessage(Locale.GERMANY))
                    .add("type", "error"))
                    .collect(()->Json.createArrayBuilder(), (arrayBuilder,object)-> arrayBuilder.add(object)  , (builder,builder2)-> builder2.build().forEach((value)->builder.add(value)))).build().toString();      
        } 
        else
            return Json.createObjectBuilder().add("errors", Json.createArrayBuilder()).build().toString();
    }
    
    public String compileExerciseSourceForErrorJson(int id,String source)
    {
        CompilationResult result = this.compileExercise(id, source);
        return compilationResultToJson(result);
        
    }
    
    public String compileTestSourceForErrorJson(int id,String source)
    {
        CompilationResult result = this.compileTest(id, source);
        return compilationResultToJson(result);
        
    }
    
    private List <Diagnostic> decorateErrors(List<Diagnostic> errors,Map<Integer,SourceContainer> sources)
    {
        Map<String,Integer> tabMap = sources.entrySet().stream().collect(Collectors.toMap((e)->e.getValue().getClassName(),(e)->e.getKey()));
        return errors.stream()
                .map((dia)-> new TabDiagnostic(dia,tabMap.get(((StringJavaFileObject)dia.getSource()).className)))
                .collect(Collectors.toList());
    }
}
