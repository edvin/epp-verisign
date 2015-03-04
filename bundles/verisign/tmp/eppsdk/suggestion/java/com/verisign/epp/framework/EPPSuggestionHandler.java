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

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPMessage;
import com.verisign.epp.codec.suggestion.EPPSuggestionInfoCmd;
import com.verisign.epp.codec.suggestion.EPPSuggestionMapFactory;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>EPPSuggestionHandler</code> class provides an interface for
 * handling EPP Suggestion Commands in a Server implementation. EPPEvents are
 * handled by the handleEvent() method here and routed to the appropriate
 * abstract member function. Subclasses should override the abstract methods to
 * define the desired behavior of a particular command when it is received. A
 * subclassed instance of <code>EPPSuggestionHandler</code> should be
 * registered with the <code>EPPDispatcher</code> so that EEPEvents related to
 * the Suggestion Mapping will be handled there. <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 * @see EPPEventHandler
 * @see EPPEvent
 */
public abstract class EPPSuggestionHandler implements EPPEventHandler {
	/** The Namespace that this handler supports. In this case, Suggestion. */
	private static final String NS = EPPSuggestionMapFactory.NS;

	/** Log4j category for logging */
	private static final Logger cat = Logger.getLogger(EPPSuggestionHandler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Construct an instance of <code>EPPSuggestionHandler</code>
	 */
	/**
	 * Whenever an EPPSuggestionHandler instance is created load the
	 * corresponsding Map Factory into the Codec
	 */
	public EPPSuggestionHandler() {
		try {
			EPPFactory.getInstance().addMapFactory(
					EPPSuggestionMapFactory.class.getName());
		} catch (EPPCodecException e) {
			cat.error("Couldn't load the Map Factory for the Suggestion Mapping", e);
			System.exit(1);
		}
	}

	/**
	 * Returns the Namespace that this handler supports. In this case,
	 * Suggestion.
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
	 * @param aEvent
	 *            An <code>EPPEvent</code> that contains the
	 *            <code>EPPCommand</code>
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPContactHandler</code> instance
	 * @return DOCUMENT ME!
	 * @exception EPPEventException
	 *                Thrown if an unrecognized <code>EPPEvent</code> is
	 *                received
	 */
	public final EPPEventResponse handleEvent(EPPEvent aEvent, Object aData)
			throws EPPEventException {
		try {
			this.preHandleEvent(aEvent, aData);
		} catch (EPPHandleEventException e) {
			return new EPPEventResponse(e.getResponse());
		}

		EPPMessage message = aEvent.getMessage();
		EPPEventResponse response;

		if (message instanceof EPPSuggestionInfoCmd) {
			response = doSuggestionInfo(aEvent, aData);
		} else {
			throw new EPPEventException(
					"In EPPSuggestionHandler an unsupported event was found");
		}

		try {
			this.postHandleEvent(aEvent, aData);
		} catch (EPPHandleEventException e) {
			return new EPPEventResponse(e.getResponse());
		}

		return response;
	}

	/**
	 * Handles any common behavior that all Suggestion commands need to execute
	 * before they execute their command specific behavior. The default
	 * implementation does nothing.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPSuggestionHandler</code>
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
			throws EPPHandleEventException {}

	/**
	 * Handles any common behavior that all Suggestion commands need to execute
	 * after they execute their command specific behavior. The default
	 * implementation does nothing
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPSuggestionHandler</code>
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
			throws EPPHandleEventException {}

	/**
	 * Invoked when a Suggestion Info command is received. Subclasses should
	 * define the behavior when a Suggestion Info command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPSuggestiondHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected abstract EPPEventResponse doSuggestionInfo(EPPEvent aEvent, Object aData);

}