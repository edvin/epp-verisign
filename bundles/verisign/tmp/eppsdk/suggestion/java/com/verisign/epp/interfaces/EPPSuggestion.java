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
package com.verisign.epp.interfaces;

import java.util.Vector;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.suggestion.EPPSuggestionInfoResp;

/**
 * <code>EPPSuggestion</code> is the primary client interface class used for
 * Suggestion management. An instance of <code>EPPSuggestion</code> is created
 * with an initialized <code>EPPSession</code>, and can be used for more than
 * one request within a single thread. A set of setter methods are provided to
 * set the attributes before a call to one of the send action methods. The
 * responses returned from the send action methods are either instances of
 * <code>EPPResponse</code> or instances of response classes in the
 * <code>com.verisign.epp.codec.suggestion</code> package. <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.suggestion.EPPSuggestionInfoResp
 */
public class EPPSuggestion {

	/** The command to send */
	private EPPCommand command = null;

	/**
	 * Extension objects associated with the command. This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector<EPPCodecComponent> extensions = null;

	/** Response of the last operation. */
	private EPPResponse response = null;

	/** An instance of a session. */
	private EPPSession session = null;

	/**
	 * Constructs an <code>EPPSuggestion</code> given an initialized EPP
	 * session.
	 * 
	 * @param newSession
	 *            Server session to use.
	 */
	public EPPSuggestion( final EPPSession newSession) {
		session = newSession;
	}

	/**
	 * Adds a command extension object.
	 * 
	 * @param aExtension
	 *            command extension object associated with the command
	 */
	public void addExtension( final EPPCodecComponent aExtension) {
		if (this.extensions == null) {
			this.extensions = new Vector<EPPCodecComponent>();
		}
		this.extensions.addElement(aExtension);
	}

	/**
	 * Retrieve the <code>EPPCommand</code> object to send
	 * 
	 * @return <code>EPPCommand</code> to send to the server.
	 */
	public EPPCommand getCommand() {
		return command;
	}

	/**
	 * Gets the command extensions.
	 * 
	 * @return <code>Vector</code> of concrete <code>EPPCodecComponent</code>
	 *         associated with the command if exists; <code>null</code>
	 *         otherwise.
	 */
	public Vector<EPPCodecComponent> getExtensions() {
		return this.extensions;
	}

	/**
	 * Get the <code>EPPResponse</code> associated with the last command. This
	 * method can be used to retrieve the server error response in the catch
	 * block of EPPCommandException.
	 * 
	 * @return <code>EPPResponse</code> associated with the last command
	 */
	public EPPResponse getResponse() {
		return response;
	}

	/**
	 * Retrieve the <code>EPPSession</code> associated with this
	 * EPPSuggestion.
	 * 
	 * @return <code>EPPSession</code>
	 */
	public EPPSession getSession() {
		return session;
	}

	/**
	 * Reset the Suggestion instance to its initial state.
	 */
	private void resetSuggestion() {
		command = null;
		extensions = null;
	}

	public EPPSuggestionInfoResp sendInfo() throws EPPCommandException {
		command.setExtensions(this.extensions);
		response = session.processDocument(command);
		if (!(response instanceof EPPSuggestionInfoResp)) { throw new EPPCommandException(
				"Unexpected response type of " + response.getClass().getName()
						+ ", expecting " + EPPSuggestionInfoResp.class.getName()); }
		resetSuggestion();
		return (EPPSuggestionInfoResp) response;
	}

	public void setCommand( final EPPCommand command) {
		this.command = command;
	}

	public void setExtensions(final Vector<EPPCodecComponent> aExtensions) {
		this.extensions = aExtensions;
	}

	public void setSession(final EPPSession aSession) {
		this.session = aSession;
	}

}