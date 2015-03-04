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
package com.verisign.epp.serverstub;

import com.verisign.epp.codec.gen.EPPGreeting;
import com.verisign.epp.framework.EPPConnectionHandler;
import com.verisign.epp.framework.EPPEventResponse;


/**
 * The <code>ConnectionHandler</code> class defines how a server acknowledges
 * new connections.  An EPPGreeting is sent in this case.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class ConnectionHandler implements EPPConnectionHandler {
	/**
	 * Create a new instance of ConnnectionHandler
	 */
	public ConnectionHandler() {
	}

	/**
	 * Invoked when a new connection occurs
	 *
	 * @param aData Any data that the server may need
	 *
	 * @return EPPEventResponse Returns the greeting
	 */
	public EPPEventResponse handleConnection(Object aData) {
		/**
		 * We know that param aData should just the SessionObject
		 */
		return new EPPEventResponse(((SessionData) aData).getGreeting());
	}
}
