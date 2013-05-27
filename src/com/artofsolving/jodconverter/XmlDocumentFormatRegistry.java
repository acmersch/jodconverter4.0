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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlDocumentFormatRegistry extends BasicDocumentFormatRegistry implements DocumentFormatRegistry {

	private static final String DEFAULT_CONFIGURATION =
		"/"+ XmlDocumentFormatRegistry.class.getPackage().getName().replace('.', '/')
		+ "/document-formats.xml";

	/**
	 * @deprecated use {@link DefaultDocumentFormatRegistry} instead (since version 2.1.2)
	 */
	public XmlDocumentFormatRegistry() {
		load(getClass().getResourceAsStream(DEFAULT_CONFIGURATION));
	}

	public XmlDocumentFormatRegistry(InputStream inputStream) {
		load(inputStream);
	}

	private void load(InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		}
		XStream xstream = createXStream();
		try {
			ObjectInputStream in = xstream.createObjectInputStream(new InputStreamReader(inputStream));
			while (true) {
				try {
					addDocumentFormat((DocumentFormat) in.readObject());
				} catch (EOFException endOfFile) {
					break;
				}
			}
		} catch (Exception exception) {
			throw new RuntimeException("invalid registry configuration", exception);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private static XStream createXStream() {
		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("document-format", DocumentFormat.class);
		xstream.aliasField("mime-type", DocumentFormat.class, "mimeType");
        xstream.aliasField("file-extension", DocumentFormat.class, "fileExtension");
		xstream.aliasField("export-filters", DocumentFormat.class, "exportFilters");
		xstream.aliasField("export-options", DocumentFormat.class, "exportOptions");
        xstream.aliasField("import-options", DocumentFormat.class, "importOptions");
		xstream.alias("family", DocumentFamily.class);
		xstream.registerConverter(new AbstractSingleValueConverter() {
			public boolean canConvert(Class type) {
				return type.equals(DocumentFamily.class);
			}
			public Object fromString(String name) {
				return DocumentFamily.getFamily(name);
			}
			public String toString(Object object) {
				DocumentFamily family = (DocumentFamily) object;
				return family.getName();
			}
		});
		return xstream;
	}

	/**
	 * Prints out a document-formats.xml from the {@link DefaultDocumentFormatRegistry}
	 */
	public static void main(String[] args) throws IOException {
		DefaultDocumentFormatRegistry registry = new DefaultDocumentFormatRegistry();
		XStream xstream = createXStream();
		ObjectOutputStream outputStream = xstream.createObjectOutputStream(new OutputStreamWriter(System.out), "document-formats");
		for (Iterator iterator = registry.getDocumentFormats().iterator(); iterator.hasNext();) {
			outputStream.writeObject(iterator.next());
		}
		outputStream.close();
	}
}
