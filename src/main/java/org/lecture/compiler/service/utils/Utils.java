/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lecture.compiler.service.utils;

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

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rene
 */
public class Utils 
{
    public static String getClassName(String code)
    {
       Pattern p = Pattern.compile("(?<=class|interface|@interface|enum)(.*?)(?=extends|implements|$|\\{)",Pattern.MULTILINE);
       Matcher m = p.matcher(code);
       String result = null;
       if(m.find())
       {
           result = StringUtils.strip(m.group()).isEmpty() ? null : StringUtils.strip(m.group());
       }
       return result;
    }
}
