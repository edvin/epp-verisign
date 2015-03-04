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
package com.verisign.epp.interfaces;

import java.util.Vector;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.host.EPPHostAddRemove;
import com.verisign.epp.codec.host.EPPHostAddress;
import com.verisign.epp.codec.host.EPPHostCheckCmd;
import com.verisign.epp.codec.host.EPPHostCheckResp;
import com.verisign.epp.codec.host.EPPHostCreateCmd;
import com.verisign.epp.codec.host.EPPHostDeleteCmd;
import com.verisign.epp.codec.host.EPPHostInfoCmd;
import com.verisign.epp.codec.host.EPPHostInfoResp;
import com.verisign.epp.codec.host.EPPHostStatus;
import com.verisign.epp.codec.host.EPPHostUpdateCmd;


/**
 * <code>EPPHost</code> is the primary client interface class used for host
 * management.  An instance of <code>EPPHost</code> is created  with an
 * initialized <code>EPPSession</code>, and can be used for more than one
 * request within a single thread. A set of setter methods are provided to set
 * the attributes before a call to one of the send action  methods.  The
 * responses returned from the send action methods  are either instances of
 * <code>EPPResponse</code> or instances of response classes in the
 * <code>com.verisign.epp.codec.host</code> package.          <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.host.EPPHostCreateResp
 * @see com.verisign.epp.codec.host.EPPHostInfoResp
 * @see com.verisign.epp.codec.host.EPPHostCheckResp
 */
public class EPPHost {
	/** DOCUMENT ME! */
	public final static java.lang.String STAT_OK = EPPHostStatus.ELM_STATUS_OK;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_PENDING_DELETE =
		EPPHostStatus.ELM_STATUS_PENDING_DELETE;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_PENDING_TRANSFER =
		EPPHostStatus.ELM_STATUS_PENDING_TRANSFER;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_PENDING_CREATE =
		EPPHostStatus.ELM_STATUS_PENDING_CREATE;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_PENDING_UPDATE =
		EPPHostStatus.ELM_STATUS_PENDING_UPDATE;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_CLIENT_DELETE_PROHIBITED =
		EPPHostStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_CLIENT_UPDATE_PROHIBITED =
		EPPHostStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_LINKED =
		EPPHostStatus.ELM_STATUS_LINKED;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_SERVER_DELETE_PROHIBITED =
		EPPHostStatus.ELM_STATUS_SERVER_DELETE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static java.lang.String STAT_SERVER_UPDATE_PROHIBITED =
		EPPHostStatus.ELM_STATUS_SERVER_UPDATE_PROHIBITED;

	/** Default language for status descriptions */
	public final static String DEFAULT_LANG = EPPHostStatus.ELM_DEFAULT_LANG;

	/** Host Name(s) */
	private Vector hosts = new Vector();

	/**
	 * IP Addresses to add as a <code>Vector</code> of
	 * <code>EPPHostAddress</code> instances.
	 */
	private Vector addAddresses = null;

	/**
	 * IP Addresses to remove as a <code>Vector</code> of
	 * <code>EPPHostAddress</code> instances.
	 */
	private Vector removeAddresses = null;

	/**
	 * Host statuses to add as a <code>Vector</code> of
	 * <code>EPPHostStatus</code> instances.
	 */
	private Vector addStatuses = null;

	/**
	 * Host statuses to remove as a <code>Vector</code> of
	 * <code>EPPHostStatus</code> instances.
	 */
	private Vector removeStatuses = null;

	/** New host name */
	private String newName = null;

	/** Authenticated session */
	private EPPSession session = null;

	/** Transaction Id provided by cliet */
	private String transId = null;

	/**
	 * Extension objects associated with the command.  This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector extensions = null;

	/**
	 * Constructs an <code>EPPHost</code> given an initialized EPP session.
	 *
	 * @param newSession Server session to use.
	 */
	public EPPHost(EPPSession newSession) {
		this.session = newSession;
	}

	/**
	 * Adds a command extension object.
	 *
	 * @param aExtension command extension object associated with the command
	 */
	public void addExtension(EPPCodecComponent aExtension) {
		if (this.extensions == null) {
			this.extensions = new Vector();
		}

		this.extensions.addElement(aExtension);
	}

	// End EPPHost.addExtension(EPPCodecComponent)

	/**
	 * Sets a command extension object.
	 *
	 * @param aExtension command extension object associated with the command
	 *
	 * @deprecated Replaced by {@link #addExtension(EPPCodecComponent)}.  This
	 * 			   method will add the extension as is done in {@link
	 * 			   #addExtension(EPPCodecComponent)}.
	 */
	public void setExtension(EPPCodecComponent aExtension) {
		this.addExtension(aExtension);
	}

	// End EPPHost.setExtension(EPPCodecComponent)

	/**
	 * Sets the command extension objects.
	 *
	 * @param aExtensions command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		this.extensions = aExtensions;
	}

	// End EPPHost.setExtensions(Vector)

	/**
	 * Gets the command extensions.
	 *
	 * @return <code>Vector</code> of concrete <code>EPPCodecComponent</code>
	 * 		   associated with the command if exists; <code>null</code>
	 * 		   otherwise.
	 */
	public Vector getExtensions() {
		return this.extensions;
	}

	// End EPPHost.getExtensions()

	/**
	 * Adds a host name for use with a <code>send</code> method. Adding more
	 * than one host name is only supported by <code>sendCheck</code>.
	 *
	 * @param newHostName Host name to add
	 */
	public void addHostName(String newHostName) {
		this.hosts.addElement(newHostName);
	}

	/**
	 * Gets the new name for the host.
	 *
	 * @return New host name if defined; <code>null</code> otherwise.
	 */
	public String getNewName() {
		return this.newName;
	}

	/**
	 * Sets the new name for the host.
	 *
	 * @param aNewName New host name
	 */
	public void setNewName(String aNewName) {
		this.newName = aNewName;
	}

	/**
	 * Adds an IPV4 IP Address to the host.
	 *
	 * @param newIPV4Address IPV4 IP Address
	 */
	public void addIPV4Address(String newIPV4Address) {
		if (this.addAddresses == null) {
			this.addAddresses = new Vector();
		}

		this.addAddresses.addElement(new EPPHostAddress(newIPV4Address));
	}

	/**
	 * Removes an IPV4 IP Address from the host.
	 *
	 * @param newIPV4Address IPV4 IP Address
	 */
	public void removeIPV4Address(String newIPV4Address) {
		if (this.removeAddresses == null) {
			this.removeAddresses = new Vector();
		}

		this.removeAddresses.addElement(new EPPHostAddress(newIPV4Address));
	}

	/**
	 * Adds an IPV6 address to the host
	 *
	 * @param newIPV6Address IPV6 Address
	 */
	public void addIPV6Address(String newIPV6Address) {
		if (this.addAddresses == null) {
			this.addAddresses = new Vector();
		}

		this.addAddresses.addElement(new EPPHostAddress(
														newIPV6Address,
														EPPHostAddress.IPV6));
	}

	/**
	 * Removes an IPV6 address from the host
	 *
	 * @param newIPV6Address IPV6 Address
	 */
	public void removeIPV6Address(String newIPV6Address) {
		if (this.removeAddresses == null) {
			this.removeAddresses = new Vector();
		}

		this.removeAddresses.addElement(new EPPHostAddress(
														   newIPV6Address,
														   EPPHostAddress.IPV6));
	}

	/**
	 * Adds a status to the host.
	 *
	 * @param aStatus One of the <code>STAT_</code> constants
	 */
	public void addStatus(String aStatus) {
		if (this.addStatuses == null) {
			this.addStatuses = new Vector();
		}

		this.addStatuses.addElement(new EPPHostStatus(aStatus));
	}

	/**
	 * Removes a status from the host.
	 *
	 * @param aStatus One of the <code>STAT_</code> constants
	 */
	public void removeStatus(String aStatus) {
		if (this.removeStatuses == null) {
			this.removeStatuses = new Vector();
		}

		this.removeStatuses.addElement(new EPPHostStatus(aStatus));
	}

	/**
	 * Adds a status to the host with a description.
	 *
	 * @param aStatus One of the <code>STAT_</code> constants
	 * @param aDesc Description of the rationale for the status change
	 * @param aLang Language of <code>aDesc</code> Use
	 * 		  <code>DEFAULT_LANG</code> for the default language ("us").
	 */
	public void addStatus(String aStatus, String aDesc, String aLang) {
		if (this.addStatuses == null) {
			this.addStatuses = new Vector();
		}

		this.addStatuses.addElement(new EPPHostStatus(aStatus, aDesc, aLang));
	}

	/**
	 * Removes a status from the host with a description.
	 *
	 * @param aStatus One of the <code>STAT_</code> constants
	 * @param aDesc Description of the rationale for the status change
	 * @param aLang Language of <code>aDesc</code> Use
	 * 		  <code>DEFAULT_LANG</code> for the default language ("us").
	 */
	public void removeStatus(String aStatus, String aDesc, String aLang) {
		if (this.removeStatuses == null) {
			this.removeStatuses = new Vector();
		}

		this.removeStatuses.addElement(new EPPHostStatus(aStatus, aDesc, aLang));
	}

	/**
	 * Sets the client transaction identifier.
	 *
	 * @param newTransId Client transaction identifier
	 */
	public void setTransId(String newTransId) {
		this.transId = newTransId;
	}

	/**
	 * Gets the response associated with the last command.  This method     can
	 * be used to retrieve the server error response in the     catch block of
	 * EPPCommandException.
	 *
	 * @return Response associated with the last command
	 */
	public EPPResponse getResponse() {
		return this.session.getResponse();
	}

	/**
	 * Sends a Host Create Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addHostName</code> - Sets the host name to create.  Only  one host
	 * name is valid.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * <li>
	 * <code>addIPV4Address</code> - Add an IPV4 Address
	 * </li>
	 * <li>
	 * <code>addIPV6Address</code> - Add an IPV6 Address
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPResponse</code> containing the Host create result.
	 *
	 * @exception EPPCommandException Error executing the create command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendCreate() throws EPPCommandException {
		// Invalid number of Host Names?
		if (this.hosts.size() != 1) {
			throw new EPPCommandException("One Host Name is required for sendCreate()");
		}

		// Create the command
		EPPHostCreateCmd theCommand =
			new EPPHostCreateCmd(
								 this.transId,
								 (String) this.hosts.firstElement(),
								 this.addAddresses);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset host attributes 
		resetHost();

		// process the command and response
		return this.session.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPHost.sendCreate()

	/**
	 * Sends a Host Check Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addHostName</code> - Adds a host name to check.  More than one
	 * host name can be checked in <code>sendCheck</code>
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPHostCheckResp</code> containing the Host check
	 * 		   information.
	 *
	 * @exception EPPCommandException Error executing the check command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPHostCheckResp sendCheck() throws EPPCommandException {
		// Invalid number of Host Names?
		if (this.hosts.size() == 0) {
			throw new EPPCommandException("At least One Host Name is required for sendCheck()");
		}

		// Create the command
		EPPHostCheckCmd theCommand =
			new EPPHostCheckCmd(this.transId, this.hosts);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset host attributes 
		resetHost();

		// process the command and response
		return (EPPHostCheckResp) this.session.processDocument(theCommand, EPPHostCheckResp.class);
	}

	// End EPPHost.sendCheck()

	/**
	 * Sends a Host Info Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addHostName</code> - Sets the host name to get info for.  Only one
	 * host name is valid.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPHostInfoResp</code> containing the Host information.
	 *
	 * @exception EPPCommandException Error executing the info command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPHostInfoResp sendInfo() throws EPPCommandException {
		// Invalid number of Host Names?
		if (this.hosts.size() != 1) {
			throw new EPPCommandException("One Host Name is required for sendInfo()");
		}

		// Create the command
		EPPHostInfoCmd theCommand =
			new EPPHostInfoCmd(
							   this.transId, (String) this.hosts.firstElement());

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset contact attributes 
		resetHost();

		// process the command and response
		return (EPPHostInfoResp) this.session.processDocument(theCommand, EPPHostInfoResp.class);
	}

	// End EPPHost.sendInfo()

	/**
	 * Sends a Host Update Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addHostName</code> - Sets the domain name to update.  Only  one
	 * domain name is valid.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * <li>
	 * <code>addIPV4Address</code> - Adds IPV4 Address
	 * </li>
	 * <li>
	 * <code>addIPV6Address</code> - Adds IPV6 Address
	 * </li>
	 * <li>
	 * <code>removeIPV4Address</code> - Removes IPV4 Address
	 * </li>
	 * <li>
	 * <code>removeIPV6Address</code> - Removes IPV6 Address
	 * </li>
	 * <li>
	 * <code>addStatus</code> - Add status
	 * </li>
	 * <li>
	 * <code>removeStatus</code> - Remove status
	 * </li>
	 * <li>
	 * <code>setNewName</code> - Renames the host
	 * </li>
	 * </ul>
	 * 
	 * At least one update attribute needs to be set.
	 *
	 * @return <code>EPPResponse</code> containing the Host update result.
	 *
	 * @exception EPPCommandException Error executing the update command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendUpdate() throws EPPCommandException {
		// Invalid number of Host Names?
		if (this.hosts.size() != 1) {
			throw new EPPCommandException("One Host Name is required for sendUpdate()");
		}

		// Add attributes specified?
		EPPHostAddRemove addItems = null;

		if ((this.addAddresses != null) || (this.addStatuses != null)) {
			addItems =
				new EPPHostAddRemove(this.addAddresses, this.addStatuses);
		}

		// Remove attributes specified?
		EPPHostAddRemove removeItems = null;

		if ((this.removeAddresses != null) || (this.removeStatuses != null)) {
			removeItems =
				new EPPHostAddRemove(this.removeAddresses, this.removeStatuses);
		}

		// Change attributes specified?
		EPPHostAddRemove changeItems = null;

		if (this.newName != null) {
			changeItems = new EPPHostAddRemove(this.newName);
		}

		// Create the command
		EPPHostUpdateCmd theCommand =
			new EPPHostUpdateCmd(
								 this.transId,
								 (String) this.hosts.firstElement(), addItems,
								 removeItems, changeItems);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset host attributes 
		resetHost();

		// process the command and response
		return this.session.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPHost.sendUpdate()

	/**
	 * Sends a Host Delete Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addHostName</code> - Sets the host name to delete.  Only  one host
	 * name is valid.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPResponse</code> containing the delete result
	 * 		   information.
	 *
	 * @exception EPPCommandException Error executing the delete command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendDelete() throws EPPCommandException {
		// Invalid number of Domain Names?
		if (this.hosts.size() != 1) {
			throw new EPPCommandException("One Host Name is required for sendDelete()");
		}

		// Create the command
		EPPHostDeleteCmd theCommand =
			new EPPHostDeleteCmd(
								 this.transId,
								 (String) this.hosts.firstElement());

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset host attributes 
		resetHost();

		// process the command and response
		return this.session.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPHost.sendDelete()

	/**
	 * Resets the host instance to its initial state.
	 */
	protected void resetHost() {
		this.hosts				 = new Vector();
		this.addAddresses		 = null;
		this.removeAddresses     = null;
		this.addStatuses		 = null;
		this.removeStatuses		 = null;
		this.transId			 = null;
		this.extensions			 = null;
		this.newName			 = null;
	}

	// End EPPHost.resetHost()
}


// End class EPPHost
