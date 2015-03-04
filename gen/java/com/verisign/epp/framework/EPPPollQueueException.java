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


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
import com.verisign.epp.exception.EPPException;


/**
 * Represents a general EPP Poll Queue Exception.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPPollQueueException extends EPPException {
	/** Default type of poll queue exception */
	public final static int TYPE_DEFAULT = 0;

	/** Poll queue is empty */
	public final static int TYPE_QUEUE_EMPTY = 1;

	/** Message id was not found */
	public final static int TYPE_MSGID_NOT_FOUND = 2;

	/** Type of exception based on one of the <code>TYPE</code> constants */
	private int type = TYPE_DEFAULT;

	/**
	 * Constructor for EPPPollQueueException that takes an info string.
	 *
	 * @param info Text description of the exception.
	 */
	public EPPPollQueueException(String info) {
		super(info);
		type = TYPE_DEFAULT;
	}

	// End EPPPollQueueException.EPPPollQueueException(String)

	/**
	 * Constructor for EPPPollQueueException that takes a type and an info
	 * string.
	 *
	 * @param aType Type of the exception.
	 * @param info Text description of the exception.
	 */
	public EPPPollQueueException(int aType, String info) {
		super(info);
		type = aType;
	}

	/**
	 * Function to get the exception type.
	 *
	 * @return DOCUMENT ME!
	 */
	public int getType() {
		return type;
	}
}
