/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lecture.compiler.testframework;

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

import org.junit.Rule;
import org.junit.rules.Timeout;
import org.lecture.compiler.service.api.ExerciseContainer;
import org.lecture.compiler.service.api.ExerciseContainerDiscoveryService;


/**
 *
 * @author rene
 */
// TODO anpassungen an neuerungen
public abstract class AbstractTest 
{
    static ExerciseContainerDiscoveryService acds;
    private ExerciseContainer tcc;
    
    /**
     * Legt fest, wie lange die Tests laufen dürfen bis ein Statuscode 508 (loop detected) gesendet wird.
     */
    @Rule
    public Timeout globalTimeout = new Timeout(10000);
    
    /**
     * Parameterloser Konstruktor für JUnit 4. 
     * Ruft die abstrakte Methode {@link #getExcerciseName()} auf um den entsprechenden
     * Appcontainer zu erhalten.
     * @see com.masterarbeit.app.AppContainer
     */
    public AbstractTest()
    {
        String exercise = getExcerciseName();
        this.tcc = acds.find(exercise);
    }
    
    /**
     * Creates an object of a submitted exercise-class.
     * You can use this method to create an instance of an exercise-class.
     * By the time the concrete test is written, the implementation is not yet present. Therefore, you can't
     * isntanciate or otherwise access any exercise-class directly (in a traditional manner) within your testcase.
     * This method uses reflection and ensures, that you operate on the correct exercise context with the correct classloader.
     * Do not use this method for classes outside the context of the given exercise. 
     * 
     * <p><strong> example </strong></p>
     *<pre>
        <code>
       {@literal @}Test
        public void testFooBar() 
        {
           Object target = createObject("Foo");
           Object result = executeMethod(foo,"bar");
           assertEquals("baz",(String)result);
        }
        </code>
        </pre>
     * 
     * Erzeugt das Objekt einer Klasse aus der Übungsaufgabe.
     * Da während der Kompilierung des Tests die zu testende Klasse noch nicht vorhanden ist, können in den 
     * konkreten Testklassen davon keine Objekte auf dem herkömmlichen Weg erzeugt werden.
     * Diese Methode nutzt reflection um auf die entsprechende Methode aufzurufen. Sie stellt sicher, dass der
     * richtige Classloader für den Aufgabenkontext genutzt wird.
     * @param className The name of the class you want to instanciate.
     * @param params The constructor arguments.
     * @return An instance of the given class.
     */
    
    protected Object createObject(String className,Object...params)
    {
        return tcc.createObject(className, params);
    }
    
    /**
     * Executes the method {@code  method} on the given Object {@code target}.
     * This method allows your concrete testclasses to execute a method on the given object.
     * The object has to be an instance of the submitted classes of the specified exercise.
     * By the time the concrete test is written, the implementation is not yet present. Therefore, you can't
     * isntanciate or otherwise access any exercise-class directly (in a traditional manner) within your testcase. 
     * This method uses reflection and ensures, that you operate on the correct exercise context with the correct classloader.
     * 
     * <p> <strong>example</strong> </p>
     * 
     * <pre>
        <code>
       {@literal @}Test
        public void testFoo() 
        {
           Object target = createObject("Foo");
           Object result = executeMethod(foo,"bar");
           assertEquals("baz",(String)result);
        }
        </code>
        </pre>
     * @param target The object you want to execute the method on. Vgl {@link #createObject(String, Object...) }
     * @param method The name of the method you want to execute. Case sensitive
     * @param params The arguments the method requires.
     * @return The result of the method call.
     */
    protected Object executeMethod( Object target, String method, Object ... params)
    {
        return tcc.executeMethod(target, method, params);
    }
    
    /**
     * Factory method to spezify the exercisename.
     * this method forces every concrete testclass to specify the exercisename, which serves as a 
     * context identifier. The constructor uses this method to lookup the corresponding appcontainer.
     * <p> <strong> example </strong></p>
     * imagine you write a testclass for the Excercise "Objectoriented Programming - getter and setter methods".
     * This testclass would override {@code getExcerciseName()} as follows
     * <pre>
        <code>
       {@literal @}Override
        public String getExcerciseName() 
        {
            return "Objectoriented Programming - getter and setter methods";
        }
        </code>
        </pre>
     * @return Name of the exercise.
     */
    public abstract String getExcerciseName();
    
}
