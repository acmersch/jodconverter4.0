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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface DocumentConverter {

	/**
	 * Convert a document.
	 * <p>
	 * Note that this method does not close <tt>inputStream</tt> and <tt>outputStream</tt>.
	 * 
	 * @param inputStream
	 * @param inputFormat
	 * @param outputStream
	 * @param outputFormat
	 */
	public void convert(InputStream inputStream, DocumentFormat inputFormat, OutputStream outputStream, DocumentFormat outputFormat);

    /**
     * Convert a document.
     * 
     * @param inputFile
     * @param inputFormat
     * @param outputFile
     * @param outputFormat
     */
    public void convert(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat);


    /**
     * Convert a document. The input format is guessed from the file extension.
     * 
     * @param inputDocument
     * @param outputDocument
     * @param outputFormat
     */
    public void convert(File inputDocument, File outputDocument, DocumentFormat outputFormat);

    /**
     * Convert a document. Both input and output formats are guessed from the file extension.
     * 
     * @param inputDocument
     * @param outputDocument
     */
    public void convert(File inputDocument, File outputDocument);

}
