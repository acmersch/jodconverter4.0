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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lib.uno.adapter.ByteArrayToXInputStreamAdapter;
import com.sun.star.lib.uno.adapter.OutputStreamToXOutputStreamAdapter;
import com.sun.star.uno.UnoRuntime;

/**
 * Alternative stream-based {@link DocumentConverter} implementation.
 * <p>
 * This implementation passes document data to and from the OpenOffice.org
 * service as streams.
 * <p>
 * Stream-based conversions are slower than the default file-based ones (provided
 * by {@link OpenOfficeDocumentConverter}) but they allow to run the OpenOffice.org
 * service on a different machine, or under a different system user on the same
 * machine without file permission problems.
 * <p>
 * <b>Warning!</b> There is a <a href="http://www.openoffice.org/issues/show_bug.cgi?id=75519">bug</a>
 * in OpenOffice.org 2.2.x that causes some input formats, including Word and Excel, not to work when
 * using stream-base conversions.
 * <p>
 * Use this implementation only if {@link OpenOfficeDocumentConverter} is not possible.
 * 
 * @see OpenOfficeDocumentConverter
 */
public class StreamOpenOfficeDocumentConverter extends AbstractOpenOfficeDocumentConverter {

	public StreamOpenOfficeDocumentConverter(OpenOfficeConnection connection) {
		super(connection);
	}

	public StreamOpenOfficeDocumentConverter(OpenOfficeConnection connection, DocumentFormatRegistry formatRegistry) {
		super(connection, formatRegistry);
	}

	protected void convertInternal(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(inputFile);
			outputStream = new FileOutputStream(outputFile);
			convert(inputStream, inputFormat, outputStream, outputFormat);
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalArgumentException(fileNotFoundException.getMessage());
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

	protected void convertInternal(InputStream inputStream, DocumentFormat inputFormat, OutputStream outputStream, DocumentFormat outputFormat) {
        Map/*<String,Object>*/ exportOptions = outputFormat.getExportOptions(inputFormat.getFamily());
        
		try {
			synchronized (openOfficeConnection) {
				loadAndExport(inputStream, inputFormat.getImportOptions(), outputStream, exportOptions);
			}
        } catch (OpenOfficeException openOfficeException) {
            throw openOfficeException;
		} catch (Throwable throwable) {
			throw new OpenOfficeException("conversion failed", throwable);
		}
	}

	private void loadAndExport(InputStream inputStream, Map/*<String,Object>*/ importOptions, OutputStream outputStream, Map/*<String,Object>*/ exportOptions) throws Exception {
		XComponentLoader desktop = openOfficeConnection.getDesktop();
        
        Map/*<String,Object>*/ loadProperties = new HashMap();
        loadProperties.putAll(getDefaultLoadProperties());
        loadProperties.putAll(importOptions);
        // doesn't work using InputStreamToXInputStreamAdapter; probably because it's not XSeekable 
        //property("InputStream", new InputStreamToXInputStreamAdapter(inputStream))
        loadProperties.put("InputStream", new ByteArrayToXInputStreamAdapter(IOUtils.toByteArray(inputStream)));
        
		XComponent document = desktop.loadComponentFromURL("private:stream", "_blank", 0, toPropertyValues(loadProperties));
        if (document == null) {
            throw new OpenOfficeException("conversion failed: input document is null after loading");
        }

		refreshDocument(document);
		
        Map/*<String,Object>*/ storeProperties = new HashMap();
        storeProperties.putAll(exportOptions);
        storeProperties.put("OutputStream", new OutputStreamToXOutputStreamAdapter(outputStream));
        
		try {
			XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
			storable.storeToURL("private:stream", toPropertyValues(storeProperties));
		} finally {
			document.dispose();
		}
	}
}
