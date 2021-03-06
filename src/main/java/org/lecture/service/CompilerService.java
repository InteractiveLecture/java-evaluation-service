package org.lecture.service;

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

import compiler.compiler.CompilationResult;
import org.lecture.model.CompilationReport;
import org.lecture.model.FilePatch;
import org.lecture.model.SourceContainer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by rene on 21.06.15.
 */
@Service
public interface CompilerService {

  void removeFromCache(String id);

  void addToCache(SourceContainer container);

  CompilationReport patchAndCompileUserSource(String id, List<FilePatch> patches);

  CompilationReport patchAndCompileTestSource(String id, List<FilePatch> patches);

  CompilationResult compileSources(Map<String,String> sources);
}
