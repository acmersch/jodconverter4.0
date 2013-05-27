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
package com.artofsolving.jodconverter.openoffice.converter;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.sun.star.beans.PropertyValue;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XRefreshable;

public abstract class AbstractOpenOfficeDocumentConverter implements DocumentConverter {

    private final Map/*<String,Object>*/ defaultLoadProperties;

	protected OpenOfficeConnection openOfficeConnection;
	private DocumentFormatRegistry documentFormatRegistry;

	public AbstractOpenOfficeDocumentConverter(OpenOfficeConnection connection) {
		this(connection, new DefaultDocumentFormatRegistry());
	}

	public AbstractOpenOfficeDocumentConverter(OpenOfficeConnection openOfficeConnection, DocumentFormatRegistry documentFormatRegistry) {
		this.openOfficeConnection = openOfficeConnection;
		this.documentFormatRegistry = documentFormatRegistry;
        
        defaultLoadProperties = new HashMap();
        defaultLoadProperties.put("Hidden", Boolean.TRUE);
        defaultLoadProperties.put("ReadOnly", Boolean.TRUE);
	}

    public void setDefaultLoadProperty(String name, Object value) {
        defaultLoadProperties.put(name, value);
    }

    protected Map getDefaultLoadProperties() {
        return defaultLoadProperties;
    }

	protected DocumentFormatRegistry getDocumentFormatRegistry() {
		return documentFormatRegistry;
	}

	public void convert(File inputFile, File outputFile) {
		convert(inputFile, outputFile, null);
	}

	public void convert(File inputFile, File outputFile, DocumentFormat outputFormat) {
		convert(inputFile, null, outputFile, outputFormat);
	}

	public void convert(InputStream inputStream, DocumentFormat inputFormat, OutputStream outputStream, DocumentFormat outputFormat) {
		ensureNotNull("inputStream", inputStream);
		ensureNotNull("inputFormat", inputFormat);
		ensureNotNull("outputStream", outputStream);
		ensureNotNull("outputFormat", outputFormat);
		convertInternal(inputStream, inputFormat, outputStream, outputFormat);
	}

	public void convert(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat) {
		ensureNotNull("inputFile", inputFile);
		ensureNotNull("outputFile", outputFile);
		
		if (!inputFile.exists()) {
			throw new IllegalArgumentException("inputFile doesn't exist: " + inputFile);
		}
		if (inputFormat == null) {
			inputFormat = guessDocumentFormat(inputFile);
		}
		if (outputFormat == null) {
			outputFormat = guessDocumentFormat(outputFile);
		}
		if (!inputFormat.isImportable()) {
			throw new IllegalArgumentException("unsupported input format: " + inputFormat.getName());
		}
		if (!inputFormat.isExportableTo(outputFormat)) {
			throw new IllegalArgumentException("unsupported conversion: from " + inputFormat.getName() + " to " + outputFormat.getName());
		}
		convertInternal(inputFile, inputFormat, outputFile, outputFormat);
	}

	protected abstract void convertInternal(InputStream inputStream, DocumentFormat inputFormat, OutputStream outputStream, DocumentFormat outputFormat);
	
	protected abstract void convertInternal(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat);
	
	private void ensureNotNull(String argumentName, Object argumentValue) {
		if (argumentValue == null) {
			throw new IllegalArgumentException(argumentName + " is null");
		}
	}

	private DocumentFormat guessDocumentFormat(File file) {
		String extension = FilenameUtils.getExtension(file.getName());
		DocumentFormat format = getDocumentFormatRegistry().getFormatByFileExtension(extension);
		if (format == null) {
			throw new IllegalArgumentException("unknown document format for file: " + file);
		}
		return format;
	}

    protected void refreshDocument(XComponent document) {
		XRefreshable refreshable = (XRefreshable) UnoRuntime.queryInterface(XRefreshable.class, document);
		if (refreshable != null) {
			refreshable.refresh();
		}
	}

	protected static PropertyValue property(String name, Object value) {
    	PropertyValue property = new PropertyValue();
    	property.Name = name;
    	property.Value = value;
    	return property;
    }

	protected static PropertyValue[] toPropertyValues(Map/*<String,Object>*/ properties) {
		PropertyValue[] propertyValues = new PropertyValue[properties.size()];
		int i = 0;
		for (Iterator iter = properties.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
            Object value = entry.getValue();
            if (value instanceof Map) {
                // recursively convert nested Map to PropertyValue[]
                Map subProperties = (Map) value;
                value = toPropertyValues(subProperties);
            }
			propertyValues[i++] = property((String) entry.getKey(), value);
		}
		return propertyValues;
	}
}
