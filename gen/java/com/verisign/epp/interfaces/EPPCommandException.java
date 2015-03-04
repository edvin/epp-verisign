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
package com.verisign.epp.interfaces;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.exception.EPPException;


/**
 * Exception thrown when an error is recognized by one of the
 * <code>interfaces</code> classes.  A server error response  can be
 * associated with the exception.  <code>hasResponse</code>  is used to
 * determine if there is an associated error response, and
 * <code>getResponse</code> is used to get the error response from the
 * exception.     <br><br>
 *
 * @author P. Amiri
 * @version $Id: EPPCommandException.java,v 1.2 2004/01/26 21:21:06 jim Exp $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPCommandException extends EPPException {
	// Server response associated with exception

	/** DOCUMENT ME! */
	private EPPResponse response = null;

	/**
	 * Constructs an Exception with the specified detailed message. This
	 * message should provide information about the source, reasons and any
	 * other useful facts that could justifies the reason the exception is
	 * thrown.
	 *
	 * @param newDescription String containing a detailed message.
	 */
	public EPPCommandException(String newDescription) {
		super(newDescription);
	}

	/**
	 * Constructs an Exception with the specified detailed message and an
	 * associated server error response. This message should provide
	 * information about the source, reasons and any other useful facts that
	 * could justifies the reason the exception is thrown.
	 *
	 * @param newDescription String containing a detailed message.
	 * @param newResponse Server error response associated with the exception
	 */
	public EPPCommandException(String newDescription, EPPResponse newResponse) {
		super(newDescription);
		response = newResponse;
	}

	/**
	 * Is there a server error response associated with the exception? If
	 * <code>true</code>, <code>getResponse</code> can be  used to retrieve
	 * the response.
	 *
	 * @return <code>true</code> if there is a response; <code>false</code>
	 * 		   otherwise.
	 */
	public boolean hasResponse() {
		if (response != null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Gets the server error response associated with the exception.  If  there
	 * is no associated error response, <code>null</code> will be  returned.
	 * <code>hasResponse</code> can be used to determine if  there is a
	 * response before calling <code>getResponse</code>.
	 *
	 * @return EPPResponse Server error response if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public EPPResponse getResponse() {
		return response;
	}
}
