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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.task.ErrorCodeIOException;
import com.sun.star.ucb.XFileIdentifierConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;

/**
 * Default file-based {@link DocumentConverter} implementation.
 * <p>
 * This implementation passes document data to and from the OpenOffice.org
 * service as file URLs.
 * <p>
 * File-based conversions are faster than stream-based ones (provided by
 * {@link StreamOpenOfficeDocumentConverter}) but they require the
 * OpenOffice.org service to be running locally and have the correct
 * permissions to the files.
 * 
 * @see StreamOpenOfficeDocumentConverter
 */
public class OpenOfficeDocumentConverter extends AbstractOpenOfficeDocumentConverter {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenOfficeDocumentConverter.class);

	public OpenOfficeDocumentConverter(OpenOfficeConnection connection) {
		super(connection);
	}

	public OpenOfficeDocumentConverter(OpenOfficeConnection connection, DocumentFormatRegistry formatRegistry) {
		super(connection, formatRegistry);
	}

	/**
	 * In this file-based implementation, streams are emulated using temporary files.
	 */
	protected void convertInternal(InputStream inputStream, DocumentFormat inputFormat, OutputStream outputStream, DocumentFormat outputFormat) {
		File inputFile = null;
		File outputFile = null;
		try {
			inputFile = File.createTempFile("document", "." + inputFormat.getFileExtension());
			OutputStream inputFileStream = null;
			try {
				inputFileStream = new FileOutputStream(inputFile);
				IOUtils.copy(inputStream, inputFileStream);
			} finally {
				IOUtils.closeQuietly(inputFileStream);
			}
			
			outputFile = File.createTempFile("document", "." + outputFormat.getFileExtension());
			convert(inputFile, inputFormat, outputFile, outputFormat);
			InputStream outputFileStream = null;
			try {
				outputFileStream = new FileInputStream(outputFile);
				IOUtils.copy(outputFileStream, outputStream);
			} finally {
				IOUtils.closeQuietly(outputFileStream);
			}
		} catch (IOException ioException) {
			throw new OpenOfficeException("conversion failed", ioException);
		} finally {
			if (inputFile != null) {
				inputFile.delete();
			}
			if (outputFile != null) {
				outputFile.delete();
			}
		}
	}

	protected void convertInternal(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat) {
        Map/*<String,Object>*/ loadProperties = new HashMap();
        loadProperties.putAll(getDefaultLoadProperties());
        loadProperties.putAll(inputFormat.getImportOptions());

        Map/*<String,Object>*/ storeProperties = outputFormat.getExportOptions(inputFormat.getFamily());

        synchronized (openOfficeConnection) {
			XFileIdentifierConverter fileContentProvider = openOfficeConnection.getFileContentProvider();
			String inputUrl = fileContentProvider.getFileURLFromSystemPath("", inputFile.getAbsolutePath());
			String outputUrl = fileContentProvider.getFileURLFromSystemPath("", outputFile.getAbsolutePath());
            
			loadAndExport(inputUrl, loadProperties, outputUrl, storeProperties);
		}
	}

	private void loadAndExport(String inputUrl, Map/*<String,Object>*/ loadProperties, String outputUrl, Map/*<String,Object>*/ storeProperties) throws OpenOfficeException {
		XComponent document;
		try {
		    document = loadDocument(inputUrl, loadProperties);
		} catch (ErrorCodeIOException errorCodeIOException) {
		    throw new OpenOfficeException("conversion failed: could not load input document; OOo errorCode: " + errorCodeIOException.ErrCode, errorCodeIOException);
		} catch (Exception otherException) {
            throw new OpenOfficeException("conversion failed: could not load input document", otherException);
        }
		if (document == null) {
		    throw new OpenOfficeException("conversion failed: could not load input document");
		}
		
		refreshDocument(document);
		
		try {
		    storeDocument(document, outputUrl, storeProperties);
        } catch (ErrorCodeIOException errorCodeIOException) {
            throw new OpenOfficeException("conversion failed: could not save output document; OOo errorCode: " + errorCodeIOException.ErrCode, errorCodeIOException);
        } catch (Exception otherException) {
            throw new OpenOfficeException("conversion failed: could not save output document", otherException);
        }
	}

    private XComponent loadDocument(String inputUrl, Map loadProperties) throws com.sun.star.io.IOException, IllegalArgumentException {
        XComponentLoader desktop = openOfficeConnection.getDesktop();
        return desktop.loadComponentFromURL(inputUrl, "_blank", 0, toPropertyValues(loadProperties));
    }

    private void storeDocument(XComponent document, String outputUrl, Map storeProperties) throws com.sun.star.io.IOException {
        try {
			XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
			storable.storeToURL(outputUrl, toPropertyValues(storeProperties));
		} finally {
		    XCloseable closeable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, document);
		    if (closeable != null) {
    		    try {
                    closeable.close(true);
                } catch (CloseVetoException closeVetoException) {
                    logger.warn("document.close() vetoed");
                }
		    } else {
		        document.dispose();
		    }
		}
    }

}
