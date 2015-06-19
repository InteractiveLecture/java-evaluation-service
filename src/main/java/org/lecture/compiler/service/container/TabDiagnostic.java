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

import javax.tools.Diagnostic;
import java.util.Locale;

/**
 *
 * @author rene
 */
public class TabDiagnostic implements Diagnostic
{
    
    private Diagnostic dia;
    private int tabId;
    
    public TabDiagnostic(Diagnostic dia, int tabId)
    {
        this.dia = dia;
        this.tabId = tabId;
    }

    public int getTabId() {
        return tabId;
    }
    
    

    @Override
    public Kind getKind() 
    {
        return dia.getKind();
    }

    @Override
    public Object getSource() {
        return dia.getSource();
    }

    @Override
    public long getPosition() {
        return dia.getPosition();
    }

    @Override
    public long getStartPosition() {
        return dia.getStartPosition();
    }

    @Override
    public long getEndPosition() {
        return dia.getEndPosition();
    }

    @Override
    public long getLineNumber() {
        return dia.getLineNumber();
    }

    @Override
    public long getColumnNumber() {
        return dia.getColumnNumber();
    }

    @Override
    public String getCode() {
        return dia.getCode();
    }

    @Override
    public String getMessage(Locale locale) {
        return dia.getMessage(locale);
    }
    
}
