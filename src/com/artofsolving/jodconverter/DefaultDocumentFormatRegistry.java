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

public class DefaultDocumentFormatRegistry extends BasicDocumentFormatRegistry {

	public DefaultDocumentFormatRegistry() {
		final DocumentFormat pdf = new DocumentFormat("Portable Document Format", "application/pdf", "pdf");
        pdf.setExportFilter(DocumentFamily.DRAWING, "draw_pdf_Export");
		pdf.setExportFilter(DocumentFamily.PRESENTATION, "impress_pdf_Export");
		pdf.setExportFilter(DocumentFamily.SPREADSHEET, "calc_pdf_Export");
		pdf.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");
		addDocumentFormat(pdf);
		
		final DocumentFormat swf = new DocumentFormat("Macromedia Flash", "application/x-shockwave-flash", "swf");
        swf.setExportFilter(DocumentFamily.DRAWING, "draw_flash_Export");
		swf.setExportFilter(DocumentFamily.PRESENTATION, "impress_flash_Export");
		addDocumentFormat(swf);
		
		final DocumentFormat xhtml = new DocumentFormat("XHTML", "application/xhtml+xml", "xhtml");
		xhtml.setExportFilter(DocumentFamily.PRESENTATION, "XHTML Impress File");
		xhtml.setExportFilter(DocumentFamily.SPREADSHEET, "XHTML Calc File");
		xhtml.setExportFilter(DocumentFamily.TEXT, "XHTML Writer File");
		addDocumentFormat(xhtml);

		// HTML is treated as Text when supplied as input, but as an output it is also
		// available for exporting Spreadsheet and Presentation formats
		final DocumentFormat html = new DocumentFormat("HTML", DocumentFamily.TEXT, "text/html", "html");
		html.setExportFilter(DocumentFamily.PRESENTATION, "impress_html_Export");
		html.setExportFilter(DocumentFamily.SPREADSHEET, "HTML (StarCalc)");
		html.setExportFilter(DocumentFamily.TEXT, "HTML (StarWriter)");
		addDocumentFormat(html);
		
		final DocumentFormat odt = new DocumentFormat("OpenDocument Text", DocumentFamily.TEXT, "application/vnd.oasis.opendocument.text", "odt");
		odt.setExportFilter(DocumentFamily.TEXT, "writer8");
		addDocumentFormat(odt);

		final DocumentFormat sxw = new DocumentFormat("OpenOffice.org 1.0 Text Document", DocumentFamily.TEXT, "application/vnd.sun.xml.writer", "sxw");
		sxw.setExportFilter(DocumentFamily.TEXT, "StarOffice XML (Writer)");
		addDocumentFormat(sxw);

		final DocumentFormat doc = new DocumentFormat("Microsoft Word", DocumentFamily.TEXT, "application/msword", "doc");
		doc.setExportFilter(DocumentFamily.TEXT, "MS Word 97");
		addDocumentFormat(doc);

		final DocumentFormat docx = new DocumentFormat("Microsoft Word 2007 XML", DocumentFamily.TEXT, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
		addDocumentFormat(docx);
		
		final DocumentFormat rtf = new DocumentFormat("Rich Text Format", DocumentFamily.TEXT, "text/rtf", "rtf");
		rtf.setExportFilter(DocumentFamily.TEXT, "Rich Text Format");
		addDocumentFormat(rtf);

		final DocumentFormat wpd = new DocumentFormat("WordPerfect", DocumentFamily.TEXT, "application/wordperfect", "wpd");
		addDocumentFormat(wpd);

		final DocumentFormat txt = new DocumentFormat("Plain Text", DocumentFamily.TEXT, "text/plain", "txt");
        // default to "Text (encoded)" UTF8,CRLF to prevent OOo from trying to display the "ASCII Filter Options" dialog
        txt.setImportOption("FilterName", "Text (encoded)");
        txt.setImportOption("FilterOptions", "UTF8,CRLF");
		txt.setExportFilter(DocumentFamily.TEXT, "Text (encoded)");
		txt.setExportOption(DocumentFamily.TEXT, "FilterOptions", "UTF8,CRLF");
		addDocumentFormat(txt);

		final DocumentFormat wikitext = new DocumentFormat("MediaWiki wikitext", "text/x-wiki", "wiki");
		wikitext.setExportFilter(DocumentFamily.TEXT, "MediaWiki");
        addDocumentFormat(wikitext);

		final DocumentFormat ods = new DocumentFormat("OpenDocument Spreadsheet", DocumentFamily.SPREADSHEET, "application/vnd.oasis.opendocument.spreadsheet", "ods");
		ods.setExportFilter(DocumentFamily.SPREADSHEET, "calc8");
		addDocumentFormat(ods);

		final DocumentFormat sxc = new DocumentFormat("OpenOffice.org 1.0 Spreadsheet", DocumentFamily.SPREADSHEET, "application/vnd.sun.xml.calc", "sxc");
		sxc.setExportFilter(DocumentFamily.SPREADSHEET, "StarOffice XML (Calc)");
		addDocumentFormat(sxc);

		final DocumentFormat xls = new DocumentFormat("Microsoft Excel", DocumentFamily.SPREADSHEET, "application/vnd.ms-excel", "xls");
		xls.setExportFilter(DocumentFamily.SPREADSHEET, "MS Excel 97");
		addDocumentFormat(xls);

		final DocumentFormat xlsx = new DocumentFormat("Microsoft Excel 2007 XML", DocumentFamily.SPREADSHEET, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
		addDocumentFormat(xlsx);

        final DocumentFormat csv = new DocumentFormat("CSV", DocumentFamily.SPREADSHEET, "text/csv", "csv");
        csv.setImportOption("FilterName", "Text - txt - csv (StarCalc)");
        csv.setImportOption("FilterOptions", "44,34,0");  // Field Separator: ','; Text Delimiter: '"'  
        csv.setExportFilter(DocumentFamily.SPREADSHEET, "Text - txt - csv (StarCalc)");
        csv.setExportOption(DocumentFamily.SPREADSHEET, "FilterOptions", "44,34,0");
        addDocumentFormat(csv);

        final DocumentFormat tsv = new DocumentFormat("Tab-separated Values", DocumentFamily.SPREADSHEET, "text/tab-separated-values", "tsv");
        tsv.setImportOption("FilterName", "Text - txt - csv (StarCalc)");
        tsv.setImportOption("FilterOptions", "9,34,0");  // Field Separator: '\t'; Text Delimiter: '"'
        tsv.setExportFilter(DocumentFamily.SPREADSHEET, "Text - txt - csv (StarCalc)");
        tsv.setExportOption(DocumentFamily.SPREADSHEET, "FilterOptions", "9,34,0");
        addDocumentFormat(tsv);

		final DocumentFormat odp = new DocumentFormat("OpenDocument Presentation", DocumentFamily.PRESENTATION, "application/vnd.oasis.opendocument.presentation", "odp");
		odp.setExportFilter(DocumentFamily.PRESENTATION, "impress8");
		addDocumentFormat(odp);

		final DocumentFormat sxi = new DocumentFormat("OpenOffice.org 1.0 Presentation", DocumentFamily.PRESENTATION, "application/vnd.sun.xml.impress", "sxi");
		sxi.setExportFilter(DocumentFamily.PRESENTATION, "StarOffice XML (Impress)");
		addDocumentFormat(sxi);

		final DocumentFormat ppt = new DocumentFormat("Microsoft PowerPoint", DocumentFamily.PRESENTATION, "application/vnd.ms-powerpoint", "ppt");
		ppt.setExportFilter(DocumentFamily.PRESENTATION, "MS PowerPoint 97");
		addDocumentFormat(ppt);

		final DocumentFormat pptx = new DocumentFormat("Microsoft PowerPoint 2007 XML", DocumentFamily.PRESENTATION, "application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
		addDocumentFormat(pptx);

        final DocumentFormat odg = new DocumentFormat("OpenDocument Drawing", DocumentFamily.DRAWING, "application/vnd.oasis.opendocument.graphics", "odg");
        odg.setExportFilter(DocumentFamily.DRAWING, "draw8");
        addDocumentFormat(odg);
        
        final DocumentFormat svg = new DocumentFormat("Scalable Vector Graphics", "image/svg+xml", "svg");
        svg.setExportFilter(DocumentFamily.DRAWING, "draw_svg_Export");
        addDocumentFormat(svg);
        
        final DocumentFormat vsd = new DocumentFormat("Microsoft Visio", DocumentFamily.DRAWING, "application/visio", "vsd");
        svg.setExportFilter(DocumentFamily.DRAWING, "draw_visio_Export");
        addDocumentFormat(vsd);
        
        final DocumentFormat dps = new DocumentFormat("Data Processing System", DocumentFamily.PRESENTATION, "application/ksdps", "dps");
        svg.setExportFilter(DocumentFamily.PRESENTATION, "draw_dps_Export");
        addDocumentFormat(dps);
        
        final DocumentFormat et = new DocumentFormat("Microsoft Excel", DocumentFamily.SPREADSHEET, "application/kset", "et");
        svg.setExportFilter(DocumentFamily.SPREADSHEET, "et");
        addDocumentFormat(et);
        
        final DocumentFormat wps = new DocumentFormat("Word Processing System", DocumentFamily.TEXT, "application/kswps", "wps");
        svg.setExportFilter(DocumentFamily.TEXT, "wps");
        addDocumentFormat(wps);
  	}
}
