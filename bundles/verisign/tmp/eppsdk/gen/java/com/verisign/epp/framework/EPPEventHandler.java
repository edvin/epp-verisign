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

import com.verisign.epp.codec.gen.EPPMessage;

// EPP Imports
import com.verisign.epp.codec.gen.EPPResponse;


/**
 * The <code>EPPEventHandler</code> interface defines the interface used to be
 * notified of EPPEvents.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.exception.EPPException
 */
public interface EPPEventHandler {
	/**
	 * Invoked when an EPPMessage should be handled
	 *
	 * @param aEvent The Event to be handled
	 * @param aData Any additional data that may need to be received.
	 *
	 * @return <code>EPPEventResponse</code> The response to return
	 */
	public EPPEventResponse handleEvent(EPPEvent aEvent, Object aData)
								 throws EPPEventException;

	/**
	 * The Namespace that a handler is associated with.
	 *
	 * @return String The Namespace that the handler is associated with.
	 */
	public String getNamespace();
}
