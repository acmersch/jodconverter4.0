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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a document format ("OpenDocument Text" or "PDF").
 * Also contains its available export filters.
 */
public class DocumentFormat {

    private static final String FILTER_NAME = "FilterName";
    
	private String name;
	private DocumentFamily family;
	private String mimeType;
	private String fileExtension;
	private Map/*<DocumentFamily,Map<String,Object>>*/ exportOptions = new HashMap();
    private Map/*<String,Object>*/ importOptions = new HashMap();

    public DocumentFormat() {
    	// empty constructor needed for XStream deserialization
    }

	public DocumentFormat(String name, String mimeType, String extension) {
		this.name = name;
		this.mimeType = mimeType;
		this.fileExtension = extension;
	}

	public DocumentFormat(String name, DocumentFamily family, String mimeType, String extension) {
		this.name = name;
		this.family = family;
		this.mimeType = mimeType;
		this.fileExtension = extension;
	}

	public String getName() {
		return name;
	}

	public DocumentFamily getFamily() {
		return family;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	private String getExportFilter(DocumentFamily family) {
        return (String) getExportOptions(family).get(FILTER_NAME);
	}

	public boolean isImportable() {
		return family != null;
	}

	public boolean isExportOnly() {
		return !isImportable();
	}

	public boolean isExportableTo(DocumentFormat otherFormat) {
		return otherFormat.isExportableFrom(this.family);
	}

	public boolean isExportableFrom(DocumentFamily family) {
        return getExportFilter(family) != null;
	}

	public void setExportFilter(DocumentFamily family, String filter) {
		getExportOptions(family).put(FILTER_NAME, filter);
	}

	public void setExportOption(DocumentFamily family, String name, Object value) {
        Map/*<String,Object>*/ options = (Map) exportOptions.get(family);
        if (options == null) {
            options = new HashMap();
            exportOptions.put(family, options);
        }
		options.put(name, value);
	}

	public Map/*<String,Object>*/ getExportOptions(DocumentFamily family) {
        Map/*<String,Object>*/ options = (Map) exportOptions.get(family);
        if (options == null) {
            options = new HashMap();
            exportOptions.put(family, options);
        }
        return options;
	}

    public void setImportOption(String name, Object value) {
        importOptions.put(name, value);
    }

    public Map/*<String,Object>*/ getImportOptions() {
        if (importOptions != null) {
            return importOptions;
        } else {
            return Collections.EMPTY_MAP;
        }
    }    
}
