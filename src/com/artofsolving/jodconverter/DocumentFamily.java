//
// JODConverter - Java OpenDocument Converter
// Copyright (C) 2004-2007 - Mirko Nasato <mirko@artofsolving.com>
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// http://www.gnu.org/copyleft/lesser.html
//
package com.artofsolving.jodconverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum-style class declaring the available document families (Text, Spreadsheet, Presentation).
 */
public class DocumentFamily {

	public static final DocumentFamily TEXT = new DocumentFamily("Text");
	public static final DocumentFamily SPREADSHEET = new DocumentFamily("Spreadsheet");
	public static final DocumentFamily PRESENTATION = new DocumentFamily("Presentation");
    public static final DocumentFamily DRAWING = new DocumentFamily("Drawing");

	private static Map FAMILIES = new HashMap();
	static {
		FAMILIES.put(TEXT.name, TEXT);
		FAMILIES.put(SPREADSHEET.name, SPREADSHEET);
		FAMILIES.put(PRESENTATION.name, PRESENTATION);
        FAMILIES.put(DRAWING.name, DRAWING);
	}

    private String name;

	private DocumentFamily(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    public static DocumentFamily getFamily(String name) {
    	DocumentFamily family = (DocumentFamily) FAMILIES.get(name);
    	if (family == null) {
    		throw new IllegalArgumentException("invalid DocumentFamily: " + name);
    	}
    	return family;
    }
}
