/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.util;


// W3C Imports
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Core Java Imports
import java.io.OutputStream;
import java.io.PrintWriter;


/**
 * Implementation of the SAX <code>ErrorHandler</code> to report errors parsing
 * an XML document and to throw a SAXException so that the parsing stops on
 * any errors or warnings.     <br><br>
 */
public class TestErrorHandler implements ErrorHandler {
	/** Stream to write error messages to */
	private PrintWriter out;

	/**
	 * Default <code>TestErrorHandler</code> constructor, which sets the
	 * default output     to <code>System.err</code>.
	 */
	public TestErrorHandler() {
		out = new PrintWriter(System.err);
	}

	/**
	 * Allocate a <code>TestErrorHandler</code> with a destination
	 * <code>OutputStream</code>     for the error messages.
	 *
	 * @param aOut Stream to write error message to
	 */
	public TestErrorHandler(OutputStream aOut) {
		out = new PrintWriter(aOut);
	}

	/**
	 * Handle parser error.  The error will be written to either the default
	 * output stream     of <code>System.err</code> or the output stream
	 * specified in <code>TestErrorHandler(OutputStream)</code>.     A
	 * <code>SAXException</code> is thrown to ensure that the processing of
	 * the XML document stops.
	 *
	 * @param aException parser error exception
	 *
	 * @exception SAXException Exception containing a formatted message of the
	 * 			  error
	 */
	public void error(SAXParseException aException) throws SAXException {
		String str = getErrorString("Error", aException);

		out.println(str);

		throw new SAXException(str);
	}

	/**
	 * Handle parser warning.  The warning will be written to either the
	 * default output stream     of <code>System.err</code> or the output
	 * stream specified in <code>TestErrorHandler(OutputStream)</code>.     A
	 * SAXException is thrown to ensure that the processing of the XML
	 * document stops.
	 *
	 * @param aException parser warning exception
	 *
	 * @exception SAXException Exception containing a formatted message of the
	 * 			  warning
	 */
	public void warning(SAXParseException aException) throws SAXException {
		String str = getErrorString("Warning", aException);

		out.println(str);

		throw new SAXException(str);
	}

	/**
	 * Handle parser fatal error.  The fatal error will be written to either
	 * the default output stream     of <code>System.err</code> or the output
	 * stream specified in <code>TestErrorHandler(OutputStream)</code>.     A
	 * <code>SAXException</code> is thrown to ensure that the processing of
	 * the XML document stops.
	 *
	 * @param aException parser fatal error exception
	 *
	 * @exception SAXException Exception containing a formatted message of the
	 * 			  fatal error
	 */
	public void fatalError(SAXParseException aException)
					throws SAXException {
		String str = getErrorString("Fatal Error", aException);

		out.println(str);

		throw new SAXException(str);
	}

	/**
	 * Format a <code>String</code> message that includes the level of the
	 * error,     the system id, the line number of the error, the column
	 * number of the error, and the error message.
	 *
	 * @param aLevel Level of the error ("Warning", "Error", or "Fatal Error").
	 * @param aException parser error exception
	 *
	 * @return DOCUMENT ME!
	 */
	public String getErrorString(String aLevel, SAXParseException aException) {
		StringBuffer ret = new StringBuffer();

		// Level
		ret.append("Level      : " + aLevel);

		// System Id
		String systemId = aException.getSystemId();

		if (systemId != null) {
			int index = systemId.lastIndexOf('/');

			if (index != -1) {
				systemId = systemId.substring(index + 1);
			}

			ret.append(systemId);
		}

		// Line Number
		ret.append("\nLine    : " + aException.getLineNumber());

		// Column Number
		ret.append("\nColumn  : " + aException.getColumnNumber());

		// Message
		ret.append("\nMessage : " + aException.getMessage());

		return ret.toString();
	}

	// End TestErrorHandler.getErrorString();
}


// End class TestErrorHandler
