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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicDocumentFormatRegistry implements DocumentFormatRegistry {

	private List/*<DocumentFormat>*/ documentFormats = new ArrayList();

	public void addDocumentFormat(DocumentFormat documentFormat) {
		documentFormats.add(documentFormat);
	}

	protected List/*<DocumentFormat>*/ getDocumentFormats() {
		return documentFormats;
	}

	/**
	 * @param extension the file extension
	 * @return the DocumentFormat for this extension, or null if the extension is not mapped
	 */
	public DocumentFormat getFormatByFileExtension(String extension) {
        if (extension == null) {
            return null;
        }
        String lowerExtension = extension.toLowerCase();
		for (Iterator it = documentFormats.iterator(); it.hasNext();) {
			DocumentFormat format = (DocumentFormat) it.next();		
			if (format.getFileExtension().equals(lowerExtension)) {
				return format;
			}
		}
		return null;
	}

	public DocumentFormat getFormatByMimeType(String mimeType) {
		for (Iterator it = documentFormats.iterator(); it.hasNext();) {
			DocumentFormat format = (DocumentFormat) it.next();		
			if (format.getMimeType().equals(mimeType)) {
				return format;
			}
		}
		return null;
	}
}
