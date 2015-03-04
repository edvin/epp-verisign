/***********************************************************
 Copyright (C) 2011 VeriSign, Inc.

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

import com.verisign.epp.codec.balance.EPPBalanceInfoCmd;
import com.verisign.epp.codec.balance.EPPBalanceInfoResp;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPResponse;

/**
 * This class is the primary client interface class used for sending the 
 * EPP balance info command to get the account balance and other finance information.
 * An instance of this class is created with an initialized {@link EPPSession}
 * and can be used for more than one request within a single thread. A set of
 * setter methods are provided to set the attributes before a call to one of the
 * send action methods. The responses returned from the send action methods are
 * either instances of {@link EPPResponse} or instances of response classes in
 * the <code>com.verisign.epp.codec.balance</code> package. <br>
 * @see EPPResponse
 * @see EPPBalanceInfoResp
 */
public class EPPBalance {

	/**
	 * Extension objects associated with the command. This is a {@link Vector} of
	 * {@link EPPCodecComponent} objects.
	 */
	private Vector extensions = null;

	/** Response of the last operation. */
	private EPPResponse response = null;

	/** An instance of a session. */
	private EPPSession session = null;

	/** The {@link EPPBalanceInfoCmd} client transaction id. */
	private String transId = null;


	/**
	 * Constructs an {@link EPPBalance} given an initialized EPP session.
	 * 
	 * @param aSession
	 *        Server session to use.
	 */
	public EPPBalance ( EPPSession aSession ) {
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
	 * Returns the {@link EPPSession} associated with this {@link EPPBalance}.
	 * 
	 * @return the {@link EPPSession} associated with this {@link EPPBalance}.
	 */
	public EPPSession getSession () {
		return this.session;
	}


	/**
	 * Returns {@link EPPBalanceInfoResp} received after sending Balance Info
	 * Command to the server.<br>
	 * 
	 * @return {@link EPPBalanceInfoResp} received after sending Balance Info
	 *         Command to the server.<br>
	 * @exception EPPCommandException
	 *            Error executing the info command. Use <code>getResponse</code>
	 *            to get the associated server error response.
	 */
	public EPPBalanceInfoResp sendInfo () throws EPPCommandException {

		EPPBalanceInfoCmd theCmd = new EPPBalanceInfoCmd( this.transId );

		theCmd.setExtensions( this.extensions );

		this.response = this.session.processDocument( theCmd );

		if ( !(this.response instanceof EPPBalanceInfoResp) ) {
			throw new EPPCommandException( "Unexpected response type of "
					+ this.response.getClass().getName() + ", expecting "
					+ EPPBalanceInfoResp.class.getName() );
		}

		resetBalance();

		return (EPPBalanceInfoResp) this.response;
	}


	/**
	 * Resets the instance to its initial state by setting <code>command</code>
	 * and <code>extensions</code> to null.
	 */
	private void resetBalance () {
		this.transId = null;
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
