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


// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.io.InputStream;

// Java Core Imports
import java.io.OutputStream;
import java.util.Hashtable;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * A Singleton class that delegates message assembly to an EPPAssembler then
 * routes messages to the appropriate EPPEventHandler.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see EPPEventHandler
 * @see EPPEvent
 */
public class EPPDispatcher {
	/** The one and only instance of the EPPDispatcher */
	private static final EPPDispatcher _instance = new EPPDispatcher();

	/** Class logger */
	private static Logger cat =
		Logger.getLogger(
						 EPPDispatcher.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * eventHandlers is a Hashtable where the key is a string that defines the
	 * Namespace and value is the EPPEventHandler defined for that Namespace.
	 */
	private Hashtable eventHandlers;

	/**
	 * theAssembler makes Message-to-Stream/Stream-to-Message transformation
	 * possible.
	 */
	private EPPAssembler theAssembler;

	/** connectionHandler receives notifications of new connections. */
	private EPPConnectionHandler connectionHandler;

	/**
	 * Creates the EPPDispatcher.
	 */
	private EPPDispatcher() {
		eventHandlers = new Hashtable();
	}

	/**
	 * Gets the one and only instance of the EPPDispatcher
	 *
	 * @return Dispatcher The dispatcher
	 */
	public static EPPDispatcher getInstance() {
		return _instance;
	}

	/**
	 * Returns the <code>EPPEventResponse</code> that new connecting clients
	 * should receive.  In this case the EPPGreeting.
	 *
	 * @param aInputStream The InputStream of the new connection.
	 * @param aOutputStream The OutputStream to send the response on.
	 * @param aData Any extra data that the connection handler may need.
	 */
	public void processConnection(
								  InputStream aInputStream,
								  OutputStream aOutputStream, Object aData) {
		cat.debug("processConnection(InputStream, OutputStream, Object): Enter");

		try {
			EPPEventResponse greeting =
				connectionHandler.handleConnection(aData);
			theAssembler.toStream(greeting, aOutputStream, aData);
		}
		 catch (EPPAssemblerException e) {
			cat.error(
					  "processConnection(InputStream, OutputStream, Object)", e);
		}
		 catch (NullPointerException e) {
			cat.error(
					  "processConnection(InputStream, OutputStream, Object)", e);
		}

		cat.debug("processConnection(InputStream, OutputStream, Object): Return");
	}

	/**
	 * Sets the Assembler.  The format of the Input/Output streams determines
	 * the type of Assembler that should be used.
	 *
	 * @param aAssembler The type of Assembler to use.
	 */
	public void setAssembler(EPPAssembler aAssembler) {
		theAssembler = aAssembler;
	}

	/**
	 * Creates an <code>EPPMessage</code>, sends the message to the appropriate
	 * <code>EPPEventHandler</code>, and then sends the response on the
	 * OutputStream.
	 *
	 * @param aInputStream The InputStream used to create the EPPMessage
	 * @param aOutputStream The OutputStream to send the EPPResponse on
	 * @param aData Any additional data that may be required by the
	 * 		  EPPEventHandler
	 *
	 * @exception EPPEventException Exception related to the handling of an
	 * 			  event
	 * @exception EPPAssemblerException Exception related to the
	 * 			  assembling/de-assembling of messages.
	 * 			  <code>EPPAssemberException.isFatal</code> can be called to
	 * 			  determine if the exception is a fatal exception for the
	 * 			  client session.
	 */
	public void processMessage(
							   InputStream aInputStream,
							   OutputStream aOutputStream, Object aData)
						throws EPPEventException, EPPAssemblerException {
		cat.debug("processMessage(InputStream, OutputStream, Object): Enter");

		if (theAssembler == null) {
			throw new EPPAssemblerException(
											"No Assembler registered with"
											+ "EPPDispatcher",
											EPPAssemblerException.FATAL);
		}

		EPPEvent event = theAssembler.toEvent(aInputStream, aData);

		/** Send the event to the appropriate handler */
		EPPMessage message   = event.getMessage();
		String     namespace = message.getNamespace();
		cat.debug("Sending event for Namespace " + namespace);

		EPPEventHandler handler =
			(EPPEventHandler) eventHandlers.get(namespace);

		if (handler == null) {
			cat.error("processMessage(InputStream, OutputStream, Object): Handler not found for Namespace "
					  + namespace);
			throw new EPPEventException("Handler not found for Namespace "
										+ namespace);
		}

		EPPEventResponse eventResponse = handler.handleEvent(event, aData);

		if ((eventResponse != null) && (eventResponse.getResponse() != null)) {
			cat.debug("processMessage(InputStream, OutputStream, Object): Sending response to Assembler");
			theAssembler.toStream(eventResponse, aOutputStream, aData);
		}
		else {
			cat.debug("processMessage(InputStream, OutputStream, Object): No response to send to Assembler");
		}

		cat.debug("processMessage(InputStream, OutputStream, Object): Return");
	}

	/**
	 * Registers an <code>EPPEvenHandler</code> for notification of Events.
	 *
	 * @param aHandler The <code>EPPEventhandler</code> to register
	 */
	public void registerHandler(EPPEventHandler aHandler) {
		eventHandlers.put(aHandler.getNamespace(), aHandler);
	}

	/**
	 * Registers a <code>EPPConnectionHandler</code> for notification of
	 * connection events
	 *
	 * @param aConnectionHandler The connection handler to registier
	 */
	public void registerConnectionHandler(EPPConnectionHandler aConnectionHandler) {
		connectionHandler = aConnectionHandler;
	}

	/**
	 * Sends an EPP message to the client.
	 *
	 * @param aMessage EPP Message to send to the client
	 * @param aOutputStream Output stream to write the EPP Message
	 *
	 * @exception EPPAssemblerException Error writing message to output stream
	 */
	public void send(EPPMessage aMessage, OutputStream aOutputStream)
			  throws EPPAssemblerException {
		theAssembler.toStream(
							  new EPPEventResponse(aMessage), aOutputStream,
							  null);
	}

	/**
	 * Sends an EPP message to the client.
	 *
	 * @param aMessage EPP Message to send to the client
	 * @param aOutputStream Output stream to write the EPP Message
	 * @param aData whatever data should be passed to the Assembler
	 *
	 * @exception EPPAssemblerException Error writing message to output stream
	 */
	public void send(
					 EPPMessage aMessage, OutputStream aOutputStream,
					 Object aData) throws EPPAssemblerException {
		theAssembler.toStream(
							  new EPPEventResponse(aMessage), aOutputStream,
							  aData);
	}
}
