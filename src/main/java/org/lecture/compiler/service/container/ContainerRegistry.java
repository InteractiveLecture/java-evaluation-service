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


import org.lecture.compiler.service.api.ExerciseContainer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Hello world!
 *
 */
public class ContainerRegistry 
{
    private ConcurrentMap<String,ExerciseContainer> containers;
    private static ContainerRegistry instance = new ContainerRegistry();   
    private ContainerRegistry()
    {
        this.containers = new ConcurrentHashMap<>();
    }
    
    
    public static ContainerRegistry getInstance()
    {
        return instance;
    }
    
    //TODO mögliche nebenläufigkeitsprobleme beachten
    /**
     * @param id
     * @return 
     */
    public ExerciseContainer addContainerIfAbsent(String id)
    {
       AppContainerImpl acc = new AppContainerImpl();
       this.containers.putIfAbsent(id, acc);
       return this.containers.get(id);
    }
    
    public ExerciseContainer getContainer(String excerciseName)
    {
        return this.containers.get(excerciseName);
    }
    
    public void removeContainer(String excerciseName)
    {
        this.containers.remove(excerciseName);
    }
    
    public ConcurrentMap<String,ExerciseContainer> getContainers()
    {
        return this.containers;
    }
}
