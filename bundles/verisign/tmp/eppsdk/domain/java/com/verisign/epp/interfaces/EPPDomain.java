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


// W3C Imports
import java.util.Date;
import java.util.Vector;

import org.w3c.dom.Document;

import com.verisign.epp.codec.domain.EPPDomainAddRemove;
import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainPeriod;
import com.verisign.epp.codec.domain.EPPDomainRenewCmd;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferCmd;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.domain.EPPHostAttr;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;


/**
 * <code>EPPDomain</code> is the primary client interface class used for domain
 * management.  An instance of <code>EPPDomain</code> is created with an
 * initialized <code>EPPSession</code>, and can be used for more than one
 * request within a single thread. A set of setter methods are provided to set
 * the attributes before a call to one of the send action methods.  The
 * responses returned from the send action methods are either instances of
 * <code>EPPResponse</code> or instances of response classes in the
 * <code>com.verisign.epp.codec.domain</code> package.
 *
 * @author $Author: jim $
 * @version $Revision: 1.7 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.domain.EPPDomainCreateResp
 * @see com.verisign.epp.codec.domain.EPPDomainInfoResp
 * @see com.verisign.epp.codec.domain.EPPDomainCheckResp
 * @see com.verisign.epp.codec.domain.EPPDomainTransferResp
 */
public class EPPDomain {
	/** Used to specify a host update with <code>setUpdateAttrib</code>. */
	public static final int HOST = 0;

	/** Used to specify a contact update with <code>setUpdateAttrib</code>. */
	public static final int CONTACT = 1;

	/** Used to specify a status update with <code>setUpdateAttrib</code>. */
	public static final int STATUS = 2;

	/**
	 * Used to specify the addition of an attribute with
	 * <code>setUpdateAttrib</code>.
	 */
	public static final int ADD = 1;

	/**
	 * Used to specify the removal of an attribute with
	 * <code>setUpdateAttrib</code>.
	 */
	public static final int REMOVE = 2;

	/**
	 * Used to specify the change of an attribute with
	 * <code>setUpdateAttrib</code>.
	 */
	public static final int CHANGE = 3;

	/** <code>ok</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_OK = EPPDomainStatus.ELM_STATUS_OK;

	/** <code>serverHold</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_SERVER_HOLD =
		EPPDomainStatus.ELM_STATUS_SERVER_HOLD;

	/** <code>serverRenewProhibited</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_SERVER_RENEW_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_SERVER_RENEW_PROHIBITED;

	/** <code>serverTransferProhibited</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_SERVER_TRANSFER_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_SERVER_TRANSFER_PROHIBITED;

	/** <code>serverUpdateProhibited</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_SERVER_UPDATE_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_SERVER_UPDATE_PROHIBITED;

	/** <code>serverDeleteProhibited</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_SERVER_DELETE_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_SERVER_DELETE_PROHIBITED;

	/** <code>inactive</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_INACTIVE =
		EPPDomainStatus.ELM_STATUS_INACTIVE;

	/** <code>pendingCreate</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_PENDING_CREATE =
		EPPDomainStatus.ELM_STATUS_PENDING_CREATE;

	/** <code>pendingDelete</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_PENDING_DELETE =
		EPPDomainStatus.ELM_STATUS_PENDING_DELETE;

	/** <code>pendingRenew</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_PENDING_RENEW =
		EPPDomainStatus.ELM_STATUS_PENDING_RENEW;

	/** <code>pendingTransfer</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_PENDING_TRANSFER =
		EPPDomainStatus.ELM_STATUS_PENDING_TRANSFER;

	/** <code>pendingUpdate</code> constant 
	 * used with @link{com.verisign.epp.codec.domain.EPPDomainInfoResp.getStatuses()}.  */
	public final static String STATUS_PENDING_UPDATE =
		EPPDomainStatus.ELM_STATUS_PENDING_UPDATE;

	/** <code>clientHold</code> constant 
	 * used with @link{#setUpdateAttrib(int, EPPDomainStatus, int)}.  */
	public final static String STATUS_CLIENT_HOLD =
		EPPDomainStatus.ELM_STATUS_CLIENT_HOLD;

	/** <code>clientRenewProhibited</code> constant 
	 * used with @link{#setUpdateAttrib(int, EPPDomainStatus, int)}.  */
	public final static String STATUS_CLIENT_RENEW_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_CLIENT_RENEW_PROHIBITED;

	/** <code>clientTransferProhibited</code> constant 
	 * used with @link{#setUpdateAttrib(int, EPPDomainStatus, int)}.  */
	public final static String STATUS_CLIENT_TRANSFER_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_CLIENT_TRANSFER_PROHIBITED;

	/** <code>clientUpdateProhibited</code> constant 
	 * used with @link{#setUpdateAttrib(int, EPPDomainStatus, int)}.  */
	public final static String STATUS_CLIENT_UPDATE_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED;

	/** <code>clientDeleteProhibited</code> constant 
	 * used with @link{#setUpdateAttrib(int, EPPDomainStatus, int)}.  */
	public final static String STATUS_CLIENT_DELETE_PROHIBITED =
		EPPDomainStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED;

	/** Transfer approve operation constant used with @link{#setTransferOpCode(String)} */
	public static final String TRANSFER_APPROVE = EPPCommand.OP_APPROVE;

	/** Transfer cancel operation constant used with @link{#setTransferOpCode(String)} */
	public static final String TRANSFER_CANCEL = EPPCommand.OP_CANCEL;

	/** Transfer query operation constant used with @link{#setTransferOpCode(String)} */
	public static final String TRANSFER_QUERY = EPPCommand.OP_QUERY;

	/** Transfer reject operation constant used with @link{#setTransferOpCode(String)} */
	public static final String TRANSFER_REJECT = EPPCommand.OP_REJECT;

	/** Transfer request operation constant used with @link{#setTransferOpCode(String)} */
	public static final String TRANSFER_REQUEST = EPPCommand.OP_REQUEST;

	/** Administrative contact constant used with @link{#addContact(String, String)} */
	public static final String CONTACT_ADMINISTRATIVE =
		EPPDomainContact.TYPE_ADMINISTRATIVE;

	/** Billing contact constant used with @link{#addContact(String, String)} */
	public static final String CONTACT_BILLING = EPPDomainContact.TYPE_BILLING;

	/** Technical contact constant used with @link{#addContact(String, String)} */
	public static final String CONTACT_TECHNICAL =
		EPPDomainContact.TYPE_TECHNICAL;

	/** Period month unit contant. */
	public static final String PERIOD_MONTH = EPPDomainPeriod.PERIOD_UNIT_MONTH;

	/** Period year unit constant.  This is the default unit. */
	public static final String PERIOD_YEAR = EPPDomainPeriod.PERIOD_UNIT_YEAR;

	/**
	 * Constant on a call to <code>setHosts</code> to  get information on all
	 * hosts (delegated and subordinate). This is the default settings.
	 */
	public static final String HOSTS_ALL = EPPDomainInfoCmd.HOSTS_ALL;

	/**
	 * Constant on a call to <code>setHosts</code> to  get information on just
	 * the delegated hosts.
	 */
	public static final String HOSTS_DELEGATED = EPPDomainInfoCmd.HOSTS_DELEGATED;

	/**
	 * Constant on a call to <code>setHosts</code> to  get information on just
	 * the subordinate hosts.
	 */
	public static final String HOSTS_SUBORDINATE = EPPDomainInfoCmd.HOSTS_SUBORDINATE;
	
	
	/** Intance variable for a vector of Domain Name */
	private Vector domainList = new Vector();

	/** This attribute contains a list of upto 13 name server */
	private Vector hostList = new Vector();

	/** Attribute contains list of upto 4 Contacts */
	private Vector contactList = new Vector();

	/**
	 * <code>Vector</code> of <code>UpdateAttrib</code> instances, holding
	 * update list attributes (hosts, contacts, statuses).
	 */
	private Vector updateAttribs = new Vector();

	/** An instance of a session. */
	private EPPSession session = null;

	/** Transaction Id provided by cliet */
	private String transId = null;

	/** The Expiration Date. */
	private Date expirationDate;

	/** Transfer Operation Code */
	private String transferOpCode;

	/**
	 * This Attribute Contains Validity Period : duration which domain is
	 * registered for.  The default is <code>1</code>.
	 */
	private int periodLength = 1;

	/**
	 * This Attribute Contains Validity Unit :time unit where Period Length
	 * is mussured by. ie. year / month
	 */
	private String periodUnit;

	/**
	 * This Attribute Contains Authorization String provided by client when
	 * manipulation information on the Server
	 */
	private String authString;
	
	/**
	 * This attributes contains the roid for hte regisrant or contact object 
	 * that the <code>authString</code> is associated with.
	 */
	private String authRoid;

	/**
	 * Extension objects associated with the command.  This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector extensions = null;

	/** Domain Registrant Contact Identifier. */
	private String registrant = null;

	/** XML attribute value for the <code>hosts</code> attribute. */
	private String hosts = HOSTS_ALL;
	
	/**
	 * Constructs an <code>EPPDomain</code> given an initialized EPP session.
	 *
	 * @param aSession Server session to use.
	 */
	public EPPDomain(EPPSession aSession) {
		this.session = aSession;

		return;
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

	// End EPPDomain.addExtension(EPPCodecComponent)

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

	// End EPPDomain.setExtension(EPPCodecComponent)

	/**
	 * Sets the command extension objects.
	 *
	 * @param aExtensions command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		this.extensions = aExtensions;
	}

	// End EPPDomain.setExtensions(Vector)

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

	// End EPPDomain.getExtensions()

	/**
	 * Adds a domain name for use with a <code>send</code> method. Adding more
	 * than one domain name is only supported by <code>sendCheck</code>.
	 *
	 * @param aDomain Domain name to add
	 */
	public void addDomainName(String aDomain) {
		this.domainList.addElement(aDomain);
	}

	/**
	 * Adds a host name for use as a Domain delegating host.
	 *
	 * @param aHost Host name to add
	 */
	public void addHostName(String aHost) {
		this.hostList.addElement(aHost);
	}

	/**
	 * Adds a host attribute name for use as a Domain delegating host.  
	 * Calls to <code>addHostAttr</code> can not be mixed with calls 
	 * to <code>addHostName</code>.  
	 *
	 * @param aHostAttr aHostAttr Host attribute 
	 */
	public void addHostAttr(EPPHostAttr aHostAttr) {
		this.hostList.addElement(aHostAttr);
	}
	
	/**
	 * Will add a new contact which includes a name and a type.
	 *
	 * @param aName Contact Name
	 * @param aType Contact Type, which should be a
	 * 		  <code>EPPDomain.CONTACT_</code> constant.
	 */
	public void addContact(String aName, String aType) {
		this.contactList.addElement(new EPPDomainContact(aName, aType));
	}

	/**
	 * Sets the Domain expiration date.
	 *
	 * @param aExpirationDate Domain expiration date
	 */
	public void setExpirationDate(Date aExpirationDate) {
		this.expirationDate = aExpirationDate;
	}

	/**
	 * Gets the Domain expiration date.
	 *
	 * @return Domain expiration date
	 */
	public Date getExpirationDate() {
		return this.expirationDate;
	}

	/**
	 * Sets the transfer operation for a call to <code>encodeTransfer</code>.
	 * The transfer code must be set to one of the
	 * <code>EPPDomain.TRANSFER_</code> constants.
	 *
	 * @param aTransferOpCode One of the <code>EPPDomain.TRANSFER_</code>
	 * 		  constants
	 */
	public void setTransferOpCode(String aTransferOpCode) {
		this.transferOpCode = aTransferOpCode;
	}

	/**
	 * Sets the client transaction identifier.
	 *
	 * @param aTransId Client transaction identifier
	 */
	public void setTransId(String aTransId) {
		this.transId = aTransId;
	}

	/**
	 * Sets the authorization string associated with an <code>sendCreate</code>
	 * and <code>sendTransfer</code> and optionally <code>sendInfo</code>.
	 *
	 * @param aAuthString Authorization string
	 */
	public void setAuthString(String aAuthString) {
		this.authString = aAuthString;
	}

	/**
	 * Sets the authorization roid that is used to identify the registrant or 
	 * contact object if and only if the value of authInfo, set by <code>setAuthString(String)</code>, 
	 * is associated with the registrant or contact object.  This can be used 
	 * with <code>sendTransfer</code> and <code>sendInfo</code> along with setting
	 * the authInfo with the <code>setAuthString(String)</code> method.
	 *
	 * @return Roid of registrant or contact object if defined; <code>null</code> otherwise.
	 */
	public String getAuthRoid() {
		return this.authRoid;
	}

	/**
	 * Gets the authorization roid that is used to identify the registrant or 
	 * contact object if and only if the value of authInfo, set by <code>setAuthString(String)</code>, 
	 * is associated with the registrant or contact object.  This can be used 
	 * with <code>sendTransfer</code> and <code>sendInfo</code> along with setting
	 * the authInfo with the <code>setAuthString(String)</code> method.
	 *
	 * @param aAuthRoid Roid of registrant or contact object
	 */
	public void setAuthRoid(String aAuthRoid) {
		this.authRoid = aAuthRoid;
	}

	/**
	 * Sets the authorization string associated with an <code>sendCreate</code>
	 * and <code>sendTransfer</code>.
	 *
	 * @return Authorization string if defined; <code>null</code> otherwise.
	 */
	public String getAuthString() {
		return this.authString;
	}
	
	/**
	 * Sets the desired level of host information.  The default is
	 * <code>HOSTS_ALL</code>.
	 *
	 * @param aHosts Should be one of the <code>HOSTS_</code> constants.
	 */
	public void setHosts(String aHosts) {
		this.hosts = aHosts;
	}

	/**
	 * Sets the desired level of host information.
	 *
	 * @return Should be one of the <code>HOSTS_</code> constants.
	 */
	public String getHosts() {
		return this.hosts;
	}

	// End EPPDomainInfoCmd.getHosts()
	
	
	/**
	 * Gets the Domain Registrant Contact Identifier.
	 *
	 * @return Domain Registrant Contact Identifier if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRegistrant() {
		return this.registrant;
	}

	/**
	 * Sets the Domain Registrant Contact Identifier.
	 *
	 * @param aRegistrant Domain Registrant Contact Identifier.
	 */
	public void setRegistrant(String aRegistrant) {
		this.registrant = aRegistrant;
	}

	/**
	 * Sends a Domain Name Create Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name to create.  Only one
	 * domain name is valid.
	 * </li>
	 * <li>
	 * <code>setAuthString</code> - Sets the domain name authorization string.
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
	 * <code>setPeriodLength</code> - Sets the registration period (default =
	 * 1)
	 * </li>
	 * <li>
	 * <code>setPeriodUnit</code> - Sets the registration period unit (default
	 * = <code>PERIOD_YEAR</code>)
	 * </li>
	 * <li>
	 * <code>setRegistrant</code> - Sets the Registrant for the domain.  This
	 * is required for thick registries.
	 * </li>
	 * <li>
	 * <code>addContact</code> - Add domain contact
	 * </li>
	 * <li>
	 * <code>addHostName</code> - Add domain delegation host
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPDomainCreateResp</code> containing the Domain create
	 * 		   result.
	 *
	 * @exception EPPCommandException Error executing the create command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPDomainCreateResp sendCreate() throws EPPCommandException {
		// Invalid number of Domain Names?
		if (this.domainList.size() != 1) {
			throw new EPPCommandException("One Domain Name is required for sendCreate()");
		}

		// Is the Contact Mapping supported?
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			// No contacts specified?
			if (this.contactList.size() == 0) {
				this.contactList = null;
			}
		}
		else {
			this.contactList = null;
		}

		// No hosts specified?
		if (this.hostList.size() == 0) {
			this.hostList = null;
		}

		EPPDomainPeriod thePeriod = null;

		// registration period specified?
		if (this.periodLength >= 0) {
			// period specified?
			if (this.periodUnit != null) {
				thePeriod =
					new EPPDomainPeriod(this.periodUnit, this.periodLength);
			}
			else {
				thePeriod = new EPPDomainPeriod(this.periodLength);
			}
		}

		// Create the command
		EPPDomainCreateCmd theCommand =
			new EPPDomainCreateCmd(
								   this.transId,
								   (String) this.domainList.firstElement(),
								   this.hostList, this.contactList, thePeriod,
								   new EPPAuthInfo(this.authString));

		// Set registrant
		if (this.registrant != null) {
			theCommand.setRegistrant(this.registrant);

			// Set command extension
		}

		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return (EPPDomainCreateResp) this.session.processDocument(theCommand, EPPDomainCreateResp.class);
	}

	// End EPPDomain.sendCreate()

	/**
	 * Sends a Domain Name Update Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name to update.  Only one
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
	 * <code>setUpdateAttrib(int,String,int)</code> - Adds/removes hosts
	 * </li>
	 * <li>
	 * <code>setUpdateAttrib(int,DomainStatus,int)</code> - Adds/removes
	 * statuses
	 * </li>
	 * <li>
	 * <code>setUpdateAttrib(int,String,String,int)</code> - Adds/removes
	 * contacts
	 * </li>
	 * <li>
	 * <code>setRegistrant</code> - Sets the Registrant for the domain.
	 * </li>
	 * <li>
	 * <code>setAuthString</code> - Sets the domain name authorization string.
	 * </li>
	 * </ul>
	 * 
	 * At least one update attribute needs to be set.
	 *
	 * @return <code>EPPResponse</code> containing the Domain update result.
	 *
	 * @exception EPPCommandException Error executing the update command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendUpdate() throws EPPCommandException {
		// Invalid number of Domain Names?
		if (this.domainList.size() != 1) {
			throw new EPPCommandException("One Domain Name is required for sendUpdate()");
		}

		// Update vectors
		Vector hostUpdate    = null;
		Vector contactUpdate = null;
		Vector statusUpdate  = null;

		// ADD Elements
		if (getUpdateAttribCnt(HOST, ADD) == 0) {
			hostUpdate = null;
		}
		else {
			hostUpdate = getUpdateAttribVector(HOST, ADD);
		}

		if (getUpdateAttribCnt(CONTACT, ADD) == 0) {
			contactUpdate = null;
		}
		else {
			contactUpdate = getUpdateAttribVector(CONTACT, ADD);
		}

		if (getUpdateAttribCnt(STATUS, ADD) == 0) {
			statusUpdate = null;
		}
		else {
			statusUpdate = getUpdateAttribVector(STATUS, ADD);
		}

		EPPDomainAddRemove addItems = null;

		// Any add updates?
		if (
			(hostUpdate != null) || (contactUpdate != null)
				|| (statusUpdate != null)) {
			addItems =
				new EPPDomainAddRemove(hostUpdate, contactUpdate, statusUpdate);
		}

		// REMOVE Elements
		if (getUpdateAttribCnt(HOST, REMOVE) == 0) {
			hostUpdate = null;
		}
		else {
			hostUpdate = getUpdateAttribVector(HOST, REMOVE);
		}

		if (getUpdateAttribCnt(CONTACT, REMOVE) == 0) {
			contactUpdate = null;
		}
		else {
			contactUpdate = getUpdateAttribVector(CONTACT, REMOVE);
		}

		if (getUpdateAttribCnt(STATUS, REMOVE) == 0) {
			statusUpdate = null;
		}
		else {
			statusUpdate = getUpdateAttribVector(STATUS, REMOVE);
		}

		EPPDomainAddRemove removeItems = null;

		// Any remove updates?
		if (
			(hostUpdate != null) || (contactUpdate != null)
				|| (statusUpdate != null)) {
			removeItems =
				new EPPDomainAddRemove(hostUpdate, contactUpdate, statusUpdate);
		}

		// Attributes to change
		EPPDomainAddRemove changeItems = null;

		// Any change updates?
		if ((this.registrant != null) || (this.authString != null)) {
			changeItems = new EPPDomainAddRemove();

			if (this.registrant != null) {
				changeItems.setRegistrant(this.registrant);
			}

			if (this.authString != null) {
				changeItems.setAuthInfo(new EPPAuthInfo(this.authString));
			}
		}

		// Create the command
		EPPDomainUpdateCmd theCommand =
			new EPPDomainUpdateCmd(
								   this.transId,
								   (String) this.domainList.firstElement(),
								   addItems, removeItems, changeItems);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return this.session.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPDomain.sendUpdate()

	/**
	 * Sends a Domain Name Transfer Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name for transfer command.
	 * Only one domain name is valid.
	 * </li>
	 * <li>
	 * <code>setTransferOpCode</code> - Sets the domain transfer operation.
	 * </li>
	 * <li>
	 * <code>setAuthString</code> - Sets the domain name authorization string.
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
	 * <code>setPeriodLength</code> - Sets the registration period (default =
	 * 1)
	 * </li>
	 * <li>
	 * <code>setPeriodUnit</code> - Sets the registration period unit (default
	 * = <code>PERIOD_YEAR</code>)
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPDomainTransferResp</code> containing the Domain
	 * 		   transfer result.
	 *
	 * @exception EPPCommandException Error executing the create command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPDomainTransferResp sendTransfer() throws EPPCommandException {
		// Invalid number of Domain Names?
		if (this.domainList.size() != 1) {
			throw new EPPCommandException("One Domain Name is required for sendTransfer()");
		}

		// Transfer Operation Code not specified?
		if (this.transferOpCode == null) {
			throw new EPPCommandException("Transfer Operation Code is required.");
		}

		// Create the command
		EPPDomainTransferCmd theCommand =
			new EPPDomainTransferCmd(
									 this.transId, this.transferOpCode,
									 (String) this.domainList.firstElement());

		// Authorization Info String specified?
		if (this.authString != null) {
			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.authString);
			
			// Authorization Info ROID specified?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}
			
			theCommand.setAuthInfo(theAuthInfo);				
		}
		
		// Transfer Request?
		if (this.transferOpCode.equals(TRANSFER_REQUEST)) {

			// Period not specified?
			EPPDomainPeriod thePeriod = null;

			if (this.periodLength < 0) {
				thePeriod = null;
			}
			else if (this.periodUnit == null) {
				thePeriod = new EPPDomainPeriod(this.periodLength);
			}
			else {
				thePeriod =
					new EPPDomainPeriod(this.periodUnit, this.periodLength);
			}

			theCommand.setPeriod(thePeriod);
		}

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return (EPPDomainTransferResp) this.session.processDocument(theCommand, EPPDomainTransferResp.class);
	}

	// End EPPDomain.sendTransfer()

	/**
	 * Sends a Domain Name Renew Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name to renew.  Only one
	 * domain name is valid.
	 * </li>
	 * <li>
	 * <code>setExpirationDate</code> - Sets current expiration date.
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
	 * <code>setPeriodLength</code> - Sets the registration period (default =
	 * 1)
	 * </li>
	 * <li>
	 * <code>setPeriodUnit</code> - Sets the registration period unit (default
	 * = <code>PERIOD_YEAR</code>)
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPDomainRenewResp</code> containing the Domain renew
	 * 		   result.
	 *
	 * @exception EPPCommandException Error executing the renew command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPDomainRenewResp sendRenew() throws EPPCommandException {
		// Invalid number of Domain Names?
		if (this.domainList.size() != 1) {
			throw new EPPCommandException("One Domain Name is required for sendCreate()");
		}

		EPPDomainPeriod thePeriod = null;

		// registration period specified?
		if (this.periodLength >= 0) {
			// period specified?
			if (this.periodUnit != null) {
				thePeriod =
					new EPPDomainPeriod(this.periodUnit, this.periodLength);
			}
			else {
				thePeriod = new EPPDomainPeriod(this.periodLength);
			}
		}

		// Create the command
		EPPDomainRenewCmd theCommand =
			new EPPDomainRenewCmd(
								  this.transId,
								  (String) this.domainList.firstElement(),
								  this.expirationDate, thePeriod);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return (EPPDomainRenewResp) this.session.processDocument(theCommand, EPPDomainRenewResp.class);
	}

	// End EPPDomain.sendRenew()

	/**
	 * Sends a Domain Name Info Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name to get info for.  Only
	 * one domain name is valid.
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
	 * <code>setAuthString</code> - Sets the Authorization string
	 * </li>
	 * <li>
	 * <code>setHosts</code> - Sets the desired hosts to one of 
	 * the <code>HOSTS_</code> constants <code>HOSTS_ALL</code>, 
	 * <code>HOSTS_DELEGATED</code>, or <code>HOSTS_SUBORDINATE</code>.  
	 * <code>HOSTS_ALL</code> is the default.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPDomainInfoResp</code> containing the Domain
	 * 		   information.
	 *
	 * @exception EPPCommandException Error executing the info command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPDomainInfoResp sendInfo() throws EPPCommandException {
		// Invalid number of Domain Names?
		if (this.domainList.size() != 1) {
			throw new EPPCommandException("One Domain Name is required for sendInfo()");
		}

		// Create the command
		EPPDomainInfoCmd theCommand =
			new EPPDomainInfoCmd(
								 this.transId,
								 (String) this.domainList.firstElement()
								 );
		
		// Authorization string was provided?
		if (this.authString != null) {
			
			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.authString);
			
			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid); 				
			}
			
			theCommand.setAuthInfo(theAuthInfo);
		}

		// Set the hosts desired (HOSTS_ALL, HOSTS_DELEGATED, or HOSTS_SUBORDINATE)_
		theCommand.setHosts(this.hosts);
		
		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return (EPPDomainInfoResp) this.session.processDocument(theCommand, EPPDomainInfoResp.class);
	}

	// End EPPDomain.sendInfo()

	/**
	 * Sends a Domain Name Check Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Adds a domain name to check.  More than one
	 * domain name can be checked in <code>sendCheck</code>
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
	 * @return <code>EPPDomainCheckResp</code> containing the Domain check
	 * 		   information.
	 *
	 * @exception EPPCommandException Error executing the check command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPDomainCheckResp sendCheck() throws EPPCommandException {
		Document myDoc = null;

		// Create the command
		EPPDomainCheckCmd theCommand =
			new EPPDomainCheckCmd(this.transId, this.domainList);

		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return (EPPDomainCheckResp) this.session.processDocument(theCommand, EPPDomainCheckResp.class);
	}

	// End EPPDomain.sendCheck()

	/**
	 * Sends a Domain Name Delete Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name to delete.  Only one
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
		if (this.domainList.size() != 1) {
			throw new EPPCommandException("One Domain Name is required for sendDelete()");
		}

		// Create the command
		EPPDomainDeleteCmd theCommand =
			new EPPDomainDeleteCmd(
								   this.transId,
								   (String) this.domainList.firstElement());

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return this.session.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPDomain.sendDelete()

	/**
	 * Resets the domain instance to its initial state.
	 */
	protected void resetDomain() {
		this.domainList		    = new Vector();
		this.hostList		    = new Vector();
		this.contactList	    = new Vector();
		this.updateAttribs	    = new Vector();
		this.transId		    = null;
		this.expirationDate     = null;
		this.transferOpCode     = null;
		this.periodLength	    = 1;
		this.periodUnit		    = null;
		this.authString		    = null;
		this.authRoid		    = null;
		this.extensions		    = null;
		this.registrant		    = null;
		this.hosts				= HOSTS_ALL;
	}

	// End EPPDomain.resetDomain()

	/**
	 * Sets an update to a string list value, which currently can only be used
	 * for domain hosts.  Domain hosts can only be added or removed.
	 *
	 * @param aType Must be set to <code>HOST</code>
	 * @param aName Name of host
	 * @param aOp Either <code>ADD</code> or <code>REMOVE</code>
	 *
	 * @exception EPPCommandException Invalid type or operation
	 */
	public void setUpdateAttrib(int aType, String aName, int aOp)
						 throws EPPCommandException {
		if (aType != HOST) {
			throw new EPPCommandException("setUpdateAttrib(int, String, int): "
										  + aType + " is an invalid type");
		}

		if (aOp == CHANGE) {
			throw new EPPCommandException("setUpdateAttrib(int, String, int): "
										  + "CHANGE is an invalid operation");
		}

		this.updateAttribs.addElement(new UpdateAttrib(aType, aName, aOp));
	}

	// End EPPDomain.setUpdateAttrib(int, String, int)

	/**
	 * Sets an update to a domain status. Domain statuses can be either added
	 * or removed.
	 *
	 * @param aType Must be set to <code>STATUS</code>
	 * @param aName Name of status, which can use one of the
	 * 		  <code>STATUS_</code> constants
	 * @param aOp Either <code>ADD</code> or <code>REMOVE</code>
	 *
	 * @exception EPPCommandException Invalid type or operation
	 */
	public void setUpdateAttrib(int aType, EPPDomainStatus aName, int aOp)
						 throws EPPCommandException {
		if (aType != STATUS) {
			throw new EPPCommandException("setUpdateAttrib(int, EPPDomainStatus, int): "
										  + aType + " is an invalid type");
		}

		if (aOp == CHANGE) {
			throw new EPPCommandException("setUpdateAttrib(int, EPPDomainStatus, int): "
										  + "CHANGE is an invalid operation");
		}

		this.updateAttribs.addElement(new UpdateAttrib(aType, aName, aOp));
	}

	// End EPPDomain.setUpdateAttrib(int, EPPDomainStatus, int)

	/**
	 * Sets an update to a domain contact. Domain contacts can be either added
	 * or removed.
	 *
	 * @param aType Must be set to <code>CONTACT</code>
	 * @param aVal Contact Identifier
	 * @param aValType Contact Type, which should be one of the
	 * 		  <code>CONTACT_</code> constants.
	 * @param aOp Either <code>ADD</code> or <code>REMOVE</code>
	 *
	 * @exception EPPCommandException Invalid type or operation
	 */
	public void setUpdateAttrib(
								int aType, String aVal, String aValType, int aOp)
						 throws EPPCommandException {
		if (aType != CONTACT) {
			throw new EPPCommandException("setUpdateAttrib(int, String, String, int): "
										  + aType + " is an invalid type");
		}

		if (aOp == CHANGE) {
			throw new EPPCommandException("setUpdateAttrib(int, String, String, int): "
										  + "CHANGE is an invalid operation");
		}

		this.updateAttribs.addElement(new UpdateAttrib(
													   aType,
													   new EPPDomainContact(
																			aVal,
																			aValType),
													   aOp));
	}

	// End EPPDomain.setUpdateAttrib(int, String, String, int)

	/**
	 * Gets the response associated with the last command.  This method can be
	 * used to retrieve the server error response in the catch block of
	 * EPPCommandException.
	 *
	 * @return Response associated with the last command
	 */
	public EPPResponse getResponse() {
		return this.session.getResponse();
	}

	/**
	 * Gets the registration period.
	 *
	 * @return Registration period; <code>null</code> otherwise.
	 */
	public int getPeriodLength() {
		return this.periodLength;
	}

	/**
	 * Sets the registration period.
	 *
	 * @param aPeriodLength Registration period
	 */
	public void setPeriodLength(int aPeriodLength) {
		this.periodLength = aPeriodLength;
	}

	/**
	 * Gets the registration period unit.
	 *
	 * @return Registration period unit if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getPeriodUnit() {
		return this.periodUnit;
	}

	/**
	 * Sets the registration period unit to either <code>PERIOD_MONTH</code> or
	 * <code>PERIOD_YEAR</code>.
	 *
	 * @param aPeriodUnit Registration period unit
	 */
	public void setPeriodUnit(String aPeriodUnit) {
		this.periodUnit = aPeriodUnit;
	}

	/**
	 * Gets the count of update attributes of a given type
	 * (<code>CONTACT</code>, <code>HOST</code>, or <code>CONTACT</code>) and
	 * a given operation (<code>ADD</code>, <code>REMOVE</code>, or
	 * <code>CHANGE</code>).
	 *
	 * @param aType Type of attribute, which should be either
	 * 		  <code>CONTACT</code>, <code>HOST</code>, or
	 * 		  <code>CONTACT</code>.
	 * @param aOp Update operation, which should be either <code>ADD</code>,
	 * 		  <code>REMOVE</code>, or <code>CHANGE</code>
	 *
	 * @return Number of update attributes
	 */
	private int getUpdateAttribCnt(int aType, int aOp) {
		int cnt = 0;

		for (int i = 0; i < this.updateAttribs.size(); i++) {
			UpdateAttrib currAttrib =
				(UpdateAttrib) this.updateAttribs.elementAt(i);

			// Desired type?
			if (currAttrib.getType(aType, aOp)) {
				cnt++;
			}
		}

		return cnt;
	}

	// end EPPDomain.getUpdateAttribCnt(int, int)

	/**
	 * Gets the update <code>Vector</code> given a type and operation.
	 *
	 * @param aType Type of attribute, which should be either
	 * 		  <code>CONTACT</code>, <code>HOST</code>, or
	 * 		  <code>CONTACT</code>.
	 * @param aOp Update operation, which should be either <code>ADD</code>,
	 * 		  <code>REMOVE</code>, or <code>CHANGE</code>
	 *
	 * @return <code>Vector</code> of matching <code>UpdateAttrib</code>
	 * 		   instances.
	 */
	private Vector getUpdateAttribVector(int aType, int aOp) {
		int cnt = getUpdateAttribCnt(aType, aOp);

		if (cnt == 0) {
			return null;
		}

		Vector theAttribs = new Vector(cnt);

		for (int i = 0; i < this.updateAttribs.size(); i++) {
			UpdateAttrib currAttr =
				(UpdateAttrib) this.updateAttribs.elementAt(i);

			if (currAttr.getType(aType, aOp)) {
				theAttribs.addElement(currAttr.getVal());
			}
		}

		return theAttribs;
	}

	/**
	 * Inner utility class for holding a domain update attribute setting.
	 */
	private static class UpdateAttrib {
		/**
		 * Update type (<code>EPPDomain.HOST</code>,
		 * <code>EPPDomain.CONTACT</code>, or <code>EPPDomain.STATUS</code>.
		 */
		private int type = 0;

		/** Update value. */
		private Object val = null;

		/**
		 * Update operation (<code>EPPDomain.ADD</code>,
		 * <code>EPPDomain.REMOVE</code>, or <code>EPPDomain.CHANGE</code>.
		 */
		private int op = 0;

		/**
		 * Creates a new UpdateAttrib object.
		 *
		 * @param aType Type of attribute to update.  Should be either <code>EPPDomain.HOST</code>, 
		 * <code>EPPDomain.CONTACT</code>, or <code>EPPDomain.STATUS</code>
		 * @param aVal Value to set.  For example, this could be a host name when <code>aType</code> 
		 * is set to <code>EPPDomain.HOST</code> 
		 * @param aOp Operation to do to attribute.  Should be either <code>EPPDomain.ADD</code>, 
		 * or <code>EPPDomain.REMOVE</code>, or <code>EPPDomain.CHANGE</code>
		 *
		 * @throws EPPCommandException Invalid attribute update
		 */
		public UpdateAttrib(int aType, Object aVal, int aOp)
					 throws EPPCommandException {
			// Invalid update type?
			if (
				(aType != EPPDomain.HOST) && (aType != EPPDomain.CONTACT)
					&& (aType != EPPDomain.STATUS)) {
				throw new EPPCommandException("UpdateAttrib.UpdateAttrib() : Invalid Type of "
											  + aType);
			}

			// Invalid update operation?
			if (
				(aOp != EPPDomain.ADD) && (aOp != EPPDomain.REMOVE)
					&& (aOp != EPPDomain.CHANGE)) {
				throw new EPPCommandException("UpdateAttrib.UpdateAttrib() : Invalid Operation of "
											  + aOp);
			}

			// Invalid update value?
			if (aVal == null) {
				throw new EPPCommandException("UpdateAttrib.UpdateAttrib() : Invalid Value (null)");
			}

			this.type     = aType;
			this.val	  = aVal;
			this.op		  = aOp;
		}

		/**
		 * Is the specific attribute type and operation specified.  For example,
		 * is there a host add specified, where <code>aType</code> is <code>EPPDomain.HOST</code>
		 * and <code>aOp</code> is <code>EPPDomain.ADD</code>.
		 *
		 * @param aType Type of attribute to update.  Should be either <code>EPPDomain.HOST</code>, 
		 * <code>EPPDomain.CONTACT</code>, or <code>EPPDomain.STATUS</code>
		 * @param aVal Value to set.  For example, this could be a host name when <code>aType</code> 
		 * is set to <code>EPPDomain.HOST</code> 
		 *
		 * @return <code>true</code> if attribute type and operation is defined; 
		 * <code>false</code> otherwise.
		 */
		public boolean getType(int aType, int aOp) {
			if ((aType == this.type) && (aOp == this.op)) {
				return true;
			}

			return false;
		}

		/**
		 * Gets the update value.
		 *
		 * @return update value
		 */
		public Object getVal() {
			return this.val;
		}
	}

	// End static class UpdateAttrib
}


// End class EPPDomain
