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

/**
 * Created by rene on 19.06.15.
 */

import org.lecture.patch.diff_match_patch;

import java.util.LinkedList;

public class PatchServiceImpl implements PatchService {

  @Override
  public String patch(String source, String patch) {
    diff_match_patch dmp = new diff_match_patch();
    LinkedList<diff_match_patch.Patch> patches =
        (LinkedList<diff_match_patch.Patch>)dmp.patch_fromText(patch);
    return (String)dmp.patch_apply(patches,source)[0];
  }

}
