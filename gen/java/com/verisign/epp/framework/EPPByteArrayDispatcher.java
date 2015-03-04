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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPMessage;
import com.verisign.epp.util.EPPCatFactory;

/**
 * A Singleton class that delegates message assembly to an EPPAssembler then
 * routes messages to the appropriate EPPEventHandler. <br>
 * <br>
 * 
 * @author Srikanth Veeramachaneni
 * @version 1.0 Dec 04, 2006
 * @see EPPEventHandler
 * @see EPPEvent
 */
public class EPPByteArrayDispatcher {

	/** The one and only instance of the EPPByteArrayDispatcher */
	private static final EPPByteArrayDispatcher _instance = new EPPByteArrayDispatcher();

	/** Class logger */
	private static Logger LOG = Logger.getLogger( EPPByteArrayDispatcher.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * eventHandlers is a Hashtable where the key is a string that defines the
	 * Namespace and value is the EPPEventHandler defined for that Namespace.
	 */
	private Map eventHandlers;

	/**
	 * theAssembler makes Message-to-Byte-Array/Byte-Array-to-Message
	 * transformation possible.
	 */
	private EPPByteArrayAssembler theAssembler;


	/**
	 * Creates the EPPByteArrayDispatcher.
	 */
	private EPPByteArrayDispatcher () {
		this.eventHandlers = new HashMap();
	}


	/**
	 * Gets the one and only instance of the EPPByteArrayDispatcher
	 * 
	 * @return Dispatcher The dispatcher
	 */
	public static EPPByteArrayDispatcher getInstance () {
		return _instance;
	}


	/**
	 * Sets the ByteArrayAssembler.
	 * 
	 * @param aByteArrayAssembler
	 *        The ByteArrayAssembler to use.
	 */
	public void setAssembler ( EPPByteArrayAssembler aByteArrayAssembler ) {
		this.theAssembler = aByteArrayAssembler;
	}


	/**
	 * Creates an <code>EPPMessage</code>, sends the message to the appropriate
	 * <code>EPPEventHandler</code>, and then returns the response as a
	 * <code>byte</code> array.
	 * 
	 * @param aInputBytes
	 *        The byte array containing the xml input
	 * @param aData
	 *        Any additional data that may be required by the EPPEventHandler
	 * @exception EPPEventException
	 *            Exception related to the handling of an event
	 * @exception EPPAssemblerException
	 *            Exception related to the assembling/de-assembling of messages.
	 *            <code>EPPAssemberException.isFatal</code> can be called to
	 *            determine if the exception is a fatal exception for the client
	 *            session.
	 */
	public byte[] processMessage ( byte[] aInputBytes, Object aData )
			throws EPPEventException, EPPAssemblerException {
		LOG.debug( "processMessage(byte[], Object): Enter" );

		if ( this.theAssembler == null ) {
			throw new EPPAssemblerException(
					"No Assembler registered with EPPByteArrayDispatcher",
					EPPAssemblerException.FATAL );
		}

		EPPEvent event = this.theAssembler.decode( aInputBytes, aData );

		/** Send the event to the appropriate handler */
		EPPMessage message = event.getMessage();
		String namespace = message.getNamespace();
		LOG.debug( "Sending event for Namespace " + namespace );

		EPPEventHandler handler = (EPPEventHandler) this.eventHandlers
				.get( namespace );
		if ( handler == null ) {
			LOG.error( "processMessage(): Handler not found for Namespace "
					+ namespace );
			throw new EPPEventException( "Handler not found for Namespace "
					+ namespace );
		}

		byte[] responseBytes = null;
		EPPEventResponse eventResponse = handler.handleEvent( event, aData );
		if ( eventResponse != null && eventResponse.getResponse() != null ) {
			LOG.debug( "processMessage(): Sending response to Assembler" );
			responseBytes = theAssembler.encode( eventResponse, aData );
		}
		else {
			LOG.debug( "processMessage(): No response to send to Assembler" );
		}

		LOG.debug( "processMessage(): Return" );

		return responseBytes;
	}


	/**
	 * Registers an <code>EPPEvenHandler</code> for notification of Events.
	 * 
	 * @param aHandler
	 *        The <code>EPPEventhandler</code> to register
	 */
	public void registerHandler ( EPPEventHandler aHandler ) {
		this.eventHandlers.put( aHandler.getNamespace(), aHandler );
	}


	/**
	 * Encodes an EPP message to a <code>byte</code> array.
	 * 
	 * @param aMessage
	 *        EPP Message to send to the client
	 * @exception EPPAssemblerException
	 *            Error encoding the EPP message
	 */
	public byte[] toBytes ( EPPMessage aMessage ) throws EPPAssemblerException {
		return this.theAssembler.encode( new EPPEventResponse( aMessage ), null );
	}


	/**
	 * Encodes an EPP message to a <code>byte</code> array.
	 * 
	 * @param aMessage
	 *        EPP Message to send to the client
	 * @exception EPPAssemblerException
	 *            Error encoding the EPP message
	 */
	public byte[] toBytes ( EPPMessage aMessage, Object aData )
			throws EPPAssemblerException {
		return this.theAssembler.encode( new EPPEventResponse( aMessage ), aData );
	}
}
