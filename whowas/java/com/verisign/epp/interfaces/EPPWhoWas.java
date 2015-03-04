/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

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
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.whowas.EPPWhoWasConstants;
import com.verisign.epp.codec.whowas.EPPWhoWasInfoCmd;
import com.verisign.epp.codec.whowas.EPPWhoWasInfoResp;

/**
 * This class is the primary client interface class used for WhoWas management.
 * An instance of this class is created with an initialized {@link EPPSession}
 * and can be used for more than one request within a single thread. A set of
 * setter methods are provided to set the attributes before a call to one of the
 * send action methods. The responses returned from the send action methods are
 * either instances of {@link EPPResponse} or instances of response classes in
 * the <code>com.verisign.epp.codec.whowas</code> package. <br>
 * <br>
 * 
 * @author Deepak Deshpande
 * @version 1.0
 * @see EPPResponse
 * @see EPPWhoWasInfoResp
 */
public class EPPWhoWas {

	/**
	 * Extension objects associated with the command. This is a {@link Vector} of
	 * {@link EPPCodecComponent} objects.
	 */
	private Vector extensions = null;

	/** Response of the last operation. */
	private EPPResponse response = null;

	/** An instance of a session. */
	private EPPSession session = null;

	/** The WhoWas entity type with default of <code>EPPWhoWasConstants.TYPE_DOMAIN</code>. */
	private String type = EPPWhoWasConstants.TYPE_DOMAIN;

	/** The {@link EPPWhoWasInfoCmd} name. */
	private String name = null;

	/** The {@link EPPWhoWasInfoCmd} type. */
	private String roid = null;

	/** The {@link EPPWhoWasInfoCmd} client transaction id. */
	private String transId = null;


	/**
	 * Constructs an {@link EPPWhoWas} given an initialized EPP session.
	 * 
	 * @param aSession
	 *        Server session to use.
	 */
	public EPPWhoWas ( EPPSession aSession ) {
		this.session = aSession;
	}


	/**
	 * Adds a command extension object.
	 * 
	 * @param aExtension
	 *        command extension object associated with the command
	 */
	public void addExtension ( EPPCodecComponent aExtension ) {
		if ( this.extensions == null ) {
			this.extensions = new Vector();
		}
		this.extensions.addElement( aExtension );
	}


	/**
	 * Returns {@link Vector} of concrete {@link EPPCodecComponent} associated
	 * with the command if exists; <code>null</code> otherwise.
	 * 
	 * @return {@link Vector} of concrete {@link EPPCodecComponent} associated
	 *         with the command if exists; <code>null</code> otherwise.
	 */
	public Vector getExtensions () {
		return this.extensions;
	}


	/**
	 * Returns the {@link EPPResponse} associated with the last command. This
	 * method can be used to retrieve the server error response in the catch block
	 * of {@link EPPCommandException}.
	 * 
	 * @return the {@link EPPResponse} associated with the last command.
	 */
	public EPPResponse getResponse () {
		return this.response;
	}


	/**
	 * Returns the {@link EPPSession} associated with this {@link EPPWhoWas}.
	 * 
	 * @return the {@link EPPSession} associated with this {@link EPPWhoWas}.
	 */
	public EPPSession getSession () {
		return this.session;
	}


	/**
	 * Returns {@link EPPWhoWasInfoResp} received after sending WhoWas Info
	 * Command to the server.<br>
	 * 
	 * @return {@link EPPWhoWasInfoResp} received after sending WhoWas Info
	 *         Command to the server.<br>
	 * @exception EPPCommandException
	 *            Error executing the info command. Use <code>getResponse</code>
	 *            to get the associated server error response.
	 */
	public EPPWhoWasInfoResp sendInfo () throws EPPCommandException {

		EPPWhoWasInfoCmd theCmd = new EPPWhoWasInfoCmd( this.transId, this.type );

		theCmd.setName( this.name );
		theCmd.setRoid( this.roid );

		theCmd.setExtensions( this.extensions );

		this.response = this.session.processDocument( theCmd );

		if ( !(this.response instanceof EPPWhoWasInfoResp) ) {
			throw new EPPCommandException( "Unexpected response type of "
					+ this.response.getClass().getName() + ", expecting "
					+ EPPWhoWasInfoResp.class.getName() );
		}

		resetWhoWas();

		return (EPPWhoWasInfoResp) this.response;
	}


	/**
	 * Resets the instance to its initial state by setting <code>command</code>
	 * and <code>extensions</code> to null.
	 */
	private void resetWhoWas () {
		this.type = EPPWhoWasConstants.TYPE_DOMAIN;
		this.name = null;
		this.roid = null;
		this.extensions = null;
	}


	/**
	 * Sets extensions value to aExtensions
	 * 
	 * @param aExtensions
	 *        the extensions to set
	 */
	public void setExtensions ( Vector aExtensions ) {
		this.extensions = aExtensions;
	}


	/**
	 * Sets session value to aSession
	 * 
	 * @param aSession
	 *        the session to set
	 */
	public void setSession ( EPPSession aSession ) {
		this.session = aSession;
	}


	/**
	 * Returns the type
	 * 
	 * @return the type
	 */
	public String getType () {
		return this.type;
	}


	/**
	 * Sets type value to <code>aType</code>.  The default value 
	 * is <code>EPPWhoWasConstants.TYPE_DOMAIN</code>.
	 * 
	 * @param aType
	 *        the type to set
	 */
	public void setType ( String aType ) {
		this.type = aType;
	}


	/**
	 * Returns the name
	 * 
	 * @return the name
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Sets name value to <code>aName</code>.
	 * 
	 * @param aName
	 *        the name to set
	 */
	public void setName ( String aName ) {
		this.name = aName;
	}


	/**
	 * Returns the Registry Object IDentifier (roid).
	 * 
	 * @return the roid
	 */
	public String getRoid () {
		return this.roid;
	}


	/**
	 * Sets Registry Object IDentifier (roid) value to <code>aRoid</code>.
	 * 
	 * @param aRoid
	 *        the roid to set
	 */
	public void setRoid ( String aRoid ) {
		this.roid = aRoid;
	}


	/**
	 * Returns the transId
	 * 
	 * @return the transId
	 */
	public String getTransId () {
		return this.transId;
	}


	/**
	 * Sets transId value to <code>aTransId</code>.
	 * 
	 * @param aTransId
	 *        the transId to set
	 */
	public void setTransId ( String aTransId ) {
		this.transId = aTransId;
	}
}
