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
package com.verisign.epp.framework;

import com.verisign.epp.codec.gen.EPPResponse;

// EPP Imports
import com.verisign.epp.exception.EPPException;


/**
 * The <code>EPPHandleEventException</code> class is used by
 * <code>EPPEventHandlers</code> to hold <code>EPPResponses</code> when errors
 * occur when handling EPP Commmands.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see EPPEventHandler
 * @see EPPEvent
 */
public class EPPHandleEventException extends EPPException {
	/** The EPPResponse to contain */
	private EPPResponse response;

	/**
	 * Construct an <code>EPPHandleEventException</code>
	 *
	 * @param description Any debug information
	 * @param aResponse The EPPResponse to contain
	 */
	public EPPHandleEventException(String description, EPPResponse aResponse) {
		super(description);
		response = aResponse;
	}

	/**
	 * Get the <code>EPPResponse</code> for this
	 * <code>EPPHandleEventExcetion</code> instance
	 *
	 * @return EPPResponse The response to return
	 */
	public EPPResponse getResponse() {
		return response;
	}
}
