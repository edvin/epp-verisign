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

import java.io.Serializable;
import java.util.Hashtable;

import com.verisign.epp.codec.gen.EPPGreeting;
import com.verisign.epp.codec.gen.EPPLoginCmd;


/**
 * The <code>SessionData</code> is a utility class that contains data related
 * to an established EPP client/server session.   
 */
public class SessionData implements Cloneable, Serializable {
	/** Should be true if a login occured */
	private boolean bLoggedIn;

	/** Should be true if a logout occured */
	private boolean bLogoutOccured;

	/** The EPPGreeting to send clients when they make a connection */
	private EPPGreeting greeting;

	/** For extra attributes */
	private Hashtable _attribs;
	
	/**
	 * {@link EPPLoginCmd} passed for the authenticated session.  This 
	 * can be used to retrieve the passed object and extension services as well 
	 * as the client id of the client.  
	 */
	private EPPLoginCmd loginCmd = null; 

	/**
	 * Constructs a new SessionData instance
	 */
	public SessionData() {
		bLoggedIn		   = false;
		bLogoutOccured     = false;
		_attribs		   = new Hashtable();
	}

	/**
	 * Makes a copy of the invoking SessionData object
	 *
	 * @return DOCUMENT ME!
	 *
	 * @exception CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		SessionData theCopy = (SessionData) super.clone();

		if (greeting != null) {
			theCopy.greeting = (EPPGreeting) greeting.clone();
		}

		return theCopy;
	}

	/**
	 * Returns the logout state of this session object.
	 *
	 * @return boolean True if a logout has occurred
	 */
	public boolean hasLogoutOccured() {
		return bLogoutOccured;
	}

	/**
	 * Sets the logout state of this session object.
	 *
	 * @param aBool The new logout state
	 */
	public void setLogoutOccured(boolean aBool) {
		bLogoutOccured = aBool;
	}

	/**
	 * Returns the login state of this session object
	 *
	 * @return boolean The login state of this session object
	 */
	public boolean isLoggedIn() {
		return bLoggedIn;
	}

	/**
	 * Gets the {@link EPPLoginCmd} used for the authenticated session.
	 * 
	 * @return {@link EPPLoginCmd} if set; <code>null</code> otherwise.
	 */
	public EPPLoginCmd getLoginCmd() {
		return this.loginCmd;
	}

	/**
	 * Sets the {@link EPPLoginCmd} used for the authenticated session.
	 * 
	 * @param aLoginCmd {@link EPPLoginCmd} passed by the user for the authenticated session.
	 */
	public void setLoginCmd(EPPLoginCmd aLoginCmd) {
		this.loginCmd = aLoginCmd;
	}

	/**
	 * Sets the login state of this session object
	 *
	 * @param aBool The new login state
	 */
	public void setLoggedIn(boolean aBool) {
		bLoggedIn = aBool;
	}
	
	

	/**
	 * Returns the EPPGreeting associated with this session
	 *
	 * @return EPPGreeting The greeting
	 */
	public EPPGreeting getGreeting() {
		return greeting;
	}

	/**
	 * Sets the greeting that will be sent to clients when they connect
	 *
	 * @param aGreeting The greeting that should be sent.
	 */
	public void setGreeting(EPPGreeting aGreeting) {
		greeting = aGreeting;
	}

	/**
	 * Sets a session attribute by use an attribute name (key) along with 
	 * an attribute value.
	 *
	 * @param aName Name of the attribute
	 * @param aResource Value of the attribute
	 */
	public void setAttribute(String aName, Object aResource) {
		_attribs.put(aName, aResource);
	}

	/**
	 * Gets a session attribute by attribute name.
	 *
	 * @param aName Name of attribute
	 *
	 * @return Value of attribute if defined; <code>null</code> otherwise.
	 */
	public Object getAttribute(String aName) {
		return _attribs.get(aName);
	}
}
