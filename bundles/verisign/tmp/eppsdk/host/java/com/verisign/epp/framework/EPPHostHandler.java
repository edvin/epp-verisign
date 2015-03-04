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


// Log4j imports
import org.apache.log4j.*;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.host.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>EPPHostHandler</code> class provides an interface for handling
 * Host EPP Commands in a Server implementation.  EPPEvents are handled by the
 * handleEvent() method here and routed to the appropriate abstract member
 * function. Subclasses should override the abstract methods to define the
 * desired behavior of a particular command when it is received.  A subclassed
 * instance of <code>EPPHostHandler</code> should be registered with the
 * <code>EPPDispatcher</code> so that EEPEvents related to the Host Mapping
 * will be handled there.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see EPPEventHandler
 * @see EPPEvent
 */
public abstract class EPPHostHandler implements EPPEventHandler {
	/** The Namespace that this handler supports.  In this case, Host. */
	private static final String NS = EPPHostMapFactory.NS;

	/** Log4j category for logging */
	private static final Logger cat =
		Logger.getLogger(
						 EPPHostHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Construct an instance of <code>EPPHostHandler</code>
	 */
	public EPPHostHandler() {
		try {
			EPPFactory.getInstance().addMapFactory(EPPHostMapFactory.class
												   .getName());
		}
		 catch (EPPCodecException e) {
			cat.error(
					  "Couldn't load the Map Factory associated with the Host Mapping",
					  e);
			System.exit(1);
		}
	}

	/**
	 * Returns the Namespace that this handler supports. In this case, host.
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

		if (message instanceof EPPHostCreateCmd) {
			response = doHostCreate(aEvent, aData);
		}
		else if (message instanceof EPPHostDeleteCmd) {
			response = doHostDelete(aEvent, aData);
		}
		else if (message instanceof EPPHostInfoCmd) {
			response = doHostInfo(aEvent, aData);
		}
		else if (message instanceof EPPHostCheckCmd) {
			response = doHostCheck(aEvent, aData);
		}
		else if (message instanceof EPPHostUpdateCmd) {
			response = doHostUpdate(aEvent, aData);
		}
		else {
			throw new EPPEventException("In EPPHostHandler an event was sent that is not supported");
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
	 * Handles any common behavior that all host commands need to execute
	 * before they execute their command specific behavior.  The default
	 * implementation does nothing.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
						   throws EPPHandleEventException {
	}

	/**
	 * Handles any common behavior that all gen commands need to execute after
	 * they execute their command specific behavior.  The default
	 * implementation does nothing
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}

	/**
	 * Invoked when a Host Create command is received.  Subclasses should
	 * define the behavior when a Host Create command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doHostCreate(
													 EPPEvent aEvent,
													 Object aData);

	/**
	 * Invoked when a Host Info command is received.  Subclasses should define
	 * the behavior when a Host Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doHostDelete(
													 EPPEvent aEvent,
													 Object aData);

	/**
	 * Invoked when a Host Info command is received.  Subclasses should define
	 * the behavior when a Host Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doHostInfo(
												   EPPEvent aEvent, Object aData);

	/**
	 * Invoked when a Host Check command is received.  Subclasses should define
	 * the behavior when a Host Check command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doHostCheck(
													EPPEvent aEvent,
													Object aData);

	/**
	 * Invoked when a Host Update command is received.  Subclasses should
	 * define the behavior when a Host Update command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected abstract EPPEventResponse doHostUpdate(
													 EPPEvent aEvent,
													 Object aData);
}
