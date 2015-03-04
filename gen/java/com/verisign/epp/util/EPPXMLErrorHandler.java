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


// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.verisign.epp.util.EPPCatFactory;


/**
 * This is a helper class provided to intercept Errors occurred internal to SAX
 * / Dom Parser. When an Error / fatal Error or a warning occurs this methods
 * are invoked. $Id: EPPXMLErrorHandler.java,v 1.1.1.1 2003/12/05 17:36:18 jim
 * Exp $
 *
 * @author P. Amiri
 * @version $Id: EPPXMLErrorHandler.java,v 1.2 2004/01/26 21:21:07 jim Exp $
 *
 * @since JDK1.0
 */
public class EPPXMLErrorHandler implements ErrorHandler {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPXMLErrorHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Receive notification of a recoverable error. This method will first log
	 * the message and later it will throw the SAXEception.
	 *
	 * @param newException which is a SAXParseException
	 *
	 * @exception SAXException According to the W3C Recommendation this is
	 * 			  called if there is a violation in the rules of the
	 * 			  specifiaction. ie the document was not valid
	 */
	public void error(SAXParseException newException) throws SAXException {
		cat.error(
				  "EPP XML Error : " + getErrorString(newException) + " : "
				  + newException.getMessage(), newException);

		throw new SAXException("EPPXMLErrorHandler.error() : "
							   + getErrorString(newException) + " : "
							   + newException.getMessage());
	}

	/**
	 * Receive notification of a non-recoverable error. This method will first
	 * log the message and later it will throw the SAXEception.
	 *
	 * @param newException which is a SAXParseException
	 *
	 * @exception SAXException This is called if the document wasn't well
	 * 			  formed.
	 */
	public void fatalError(SAXParseException newException)
					throws SAXException {
		cat.error(
				  "EPP XML Fatal Error : " + getErrorString(newException)
				  + " : " + newException.getMessage(), newException);
		throw new SAXException("EPPXMLErrorHandler.fatalError() : "
							   + getErrorString(newException) + " : "
							   + newException.getMessage());
	}

	/**
	 * Receive notification of a warning. This method will first log the
	 * message and later it will throw the SAXEception.
	 *
	 * @param newException which is a SAXParseException
	 *
	 * @exception SAXException
	 */
	public void warning(SAXParseException newException)
				 throws SAXException {
		cat.error(
				  "EPP XML Warining : " + getErrorString(newException) + " : "
				  + newException.getMessage(), newException);
		throw new SAXException("EPPXMLErrorHandler.fatalError() : "
							   + getErrorString(newException) + " : "
							   + newException.getMessage());
	}

	/**
	 * Returns a more descriptive Error Message.
	 *
	 * @param newException which is a SAXParseException
	 *
	 * @return DOCUMENT ME!
	 */
	private String getErrorString(SAXParseException newException) {
		StringBuffer myStringBuffer = new StringBuffer();

		String		 systemId = newException.getSystemId();

		if (systemId != null) {
			int index = systemId.lastIndexOf('/');

			if (index != -1) {
				systemId = systemId.substring(index + 1);
			}

			myStringBuffer.append(systemId);
		}

		return myStringBuffer.toString() + "\nLine....: "
			   + newException.getLineNumber() + "\nColumn..: "
			   + newException.getColumnNumber() + "\nMessage.:";
	}
}
