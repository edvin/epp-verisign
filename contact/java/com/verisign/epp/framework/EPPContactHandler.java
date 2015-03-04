/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.framework;


// Log4j imports
import org.apache.log4j.*;

import com.verisign.epp.codec.contact.*;

//EPP imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>EPPContactHandler</code> class provides an interface for handling
 * EPP Contact Commands in a Server implementation.  EPPEvents are handled by
 * the handleEvent() method here and routed to the appropriate abstract member
 * function. Subclasses should override the abstract methods to define the
 * desired behavior of a particular command when it is received.  A subclassed
 * instance of <code>EPPContactHandler</code> should be registered with the
 * <code>EPPDispatcher</code> so that EEPEvents related to the Contact Mapping
 * will be handled there.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see EPPEventHandler
 * @see EPPEvent
 */
public abstract class EPPContactHandler implements EPPEventHandler {
	/** The Namespace that this handler supports.  In this case, contact. */
	private static final String NS = EPPContactMapFactory.NS;

	/** Log4j category for logging */
	private static final Logger cat =
		Logger.getLogger(
						 EPPContactHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Construct an instance of <code>EPPContactHandler</code>
	 */
	public EPPContactHandler() {
		try {
			EPPFactory.getInstance().addMapFactory(EPPContactMapFactory.class
												   .getName());
		}
		 catch (EPPCodecException e) {
			cat.error(
					  "Couldn't load the Map Factory associated with the Contact Mapping",
					  e);
			System.exit(1);
		}
	}

	/**
	 * Returns the Namespace that this handler supports. In this case, contact.
	 *
	 * @return String The Namespace that this handler supports
	 */
	public final String getNamespace() {
		return NS;
	}

	/**
	 * This method receives an <code>EPPEvent</code> and routes it to the
	 * appropriate abstract method.
	 *
	 * @param aEvent An <code>EPPEvent</code> that contains the
	 * 		  <code>EPPCommand</code>
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code> instance
	 *
	 * @return DOCUMENT ME!
	 *
	 * @exception EPPEventException Thrown if an unrecognized
	 * 			  <code>EPPEvent</code> is received
	 */
	public final EPPEventResponse handleEvent(EPPEvent aEvent, Object aData)
									   throws EPPEventException {
		try {
			this.preHandleEvent(aEvent, aData);
		}
		 catch (EPPHandleEventException e) {
			return new EPPEventResponse(e.getResponse());
		}

		EPPMessage		 message  = aEvent.getMessage();
		EPPEventResponse response;

		if (message instanceof EPPContactCreateCmd) {
			response = doContactCreate(aEvent, aData);
		}
		else if (message instanceof EPPContactDeleteCmd) {
			response = doContactDelete(aEvent, aData);
		}
		else if (message instanceof EPPContactInfoCmd) {
			response = doContactInfo(aEvent, aData);
		}
		else if (message instanceof EPPContactCheckCmd) {
			response = doContactCheck(aEvent, aData);
		}
		else if (message instanceof EPPContactTransferCmd) {
			response = doContactTransfer(aEvent, aData);
		}
		else if (message instanceof EPPContactUpdateCmd) {
			response = doContactUpdate(aEvent, aData);
		}
		else {
			throw new EPPEventException("In EPPDomainHandler an event was sent that is not supported");
		}

		try {
			this.postHandleEvent(aEvent, aData);
		}
		 catch (EPPHandleEventException e) {
			return new EPPEventResponse(e.getResponse());
		}

		return response;
	}

	/**
	 * Handles any common behavior that all contact commands need to execute
	 * before they execute their command specific behavior.  The default
	 * implementation does nothing.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
						   throws EPPHandleEventException {
	}

	/**
	 * Handles any common behavior that all contact commands need to execute
	 * after they execute their command specific behavior.  The default
	 * implementation does nothing
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}

	/**
	 * Invoked when a Contact Create command is received.  Subclasses should
	 * define the behavior when a Create Contact command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doContactCreate(
														EPPEvent aEvent,
														Object aData);

	/**
	 * Invoked when a Contact Delete command is received.  Subclasses should
	 * define the behavior when a Contact Delete command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doContactDelete(
														EPPEvent aEvent,
														Object aData);

	/**
	 * Invoked when a Contact Info command is received.  Subclasses should
	 * define the behavior when a Contact Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doContactInfo(
													  EPPEvent aEvent,
													  Object aData);

	/**
	 * Invoked when a Contact Ping command is received.  Subclasses should
	 * define the behavior when a Contact Ping command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doContactCheck(
													   EPPEvent aEvent,
													   Object aData);

	/**
	 * Invoked when a Contact Transfer command is received.  Subclasses should
	 * define the behavior when a Contact Transfer command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doContactTransfer(
														  EPPEvent aEvent,
														  Object aData);

	/**
	 * Invoked when a Contact Update command is received.  Subclasses should
	 * define the behavior when an Contact Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doContactUpdate(
														EPPEvent aEvent,
														Object aData);
}
