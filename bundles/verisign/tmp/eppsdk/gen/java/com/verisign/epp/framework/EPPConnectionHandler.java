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

/**
 * The EPPConnectionHandler interface defines an interface for classes that
 * need to be notified of connection events from a Server.  A subclassed
 * instance of <code>EPPConnectionHandler</code> should be registered with the
 * <code>EPPDispatcher</code> so that connection events are handled there.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.framework.EPPDispatcher
 */
public interface EPPConnectionHandler {
	/**
	 * The method that subclasses will implement when they wish to be notified
	 * of new connections
	 *
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPConnectionHandler</code> instance.
	 *
	 * @return EPPEventResponse A reponse that an
	 * 		   <code>EPPConnectionHandler</code> can send when notified of a
	 * 		   connection.
	 */
	public EPPEventResponse handleConnection(Object aData);
}
