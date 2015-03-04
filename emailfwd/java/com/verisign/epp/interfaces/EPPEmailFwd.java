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

import java.util.Date;
import java.util.Vector;

import com.verisign.epp.codec.emailFwd.EPPEmailFwdAddRemove;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdContact;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdDeleteCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdMapFactory;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdPeriod;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdStatus;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;

/**
 * <code>EPPEmailFwd</code> is the primary client interface class used for
 * email forward management. An instance of <code>EPPEmailFwd</code> is
 * created with an initialized <code>EPPSession</code>, and can be used for
 * more than one request within a single thread. A set of setter methods are
 * provided to set the attributes before a call to one of the send action
 * methods. The responses returned from the send action methods are either
 * instances of <code>EPPResponse</code> or instances of response classes in
 * the <code>com.verisign.epp.codec.emailFwd</code> package. <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 * 
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateResp
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoResp
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResp
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferResp
 */
public class EPPEmailFwd {
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

	/**
	 * <code>ok</code> constant
	 */
	public final static String STATUS_OK = EPPEmailFwdStatus.ELM_STATUS_OK;

	/**
	 * <code>serverHold</code> constant
	 */
	public final static String STATUS_SERVER_HOLD = EPPEmailFwdStatus.ELM_STATUS_SERVER_HOLD;

	/**
	 * <code>serverRenewProhibited</code> constant
	 */
	public final static String STATUS_SERVER_RENEW_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_SERVER_RENEW_PROHIBITED;

	/**
	 * <code>serverTransferProhibited</code> constant
	 */
	public final static String STATUS_SERVER_TRANSFER_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_SERVER_TRANSFER_PROHIBITED;

	/**
	 * <code>serverUpdateProhibited</code> constant
	 */
	public final static String STATUS_SERVER_UPDATE_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_SERVER_UPDATE_PROHIBITED;

	/**
	 * <code>serverDeleteProhibited</code> constant
	 */
	public final static String STATUS_SERVER_DELETE_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_SERVER_DELETE_PROHIBITED;

	/**
	 * <code>pendingCreate</code> constant
	 */
	public final static String STATUS_PENDING_CREATE = EPPEmailFwdStatus.ELM_STATUS_PENDING_CREATE;

	/**
	 * <code>pendingDelete</code> constant
	 */
	public final static String STATUS_PENDING_DELETE = EPPEmailFwdStatus.ELM_STATUS_PENDING_DELETE;

	/**
	 * <code>pendingRenew</code> constant
	 */
	public final static String STATUS_PENDING_RENEW = EPPEmailFwdStatus.ELM_STATUS_PENDING_RENEW;

	/**
	 * <code>pendingTransfer</code> constant
	 */
	public final static String STATUS_PENDING_TRANSFER = EPPEmailFwdStatus.ELM_STATUS_PENDING_TRANSFER;

	/**
	 * <code>pendingUpdate</code> constant
	 */
	public final static String STATUS_PENDING_UPDATE = EPPEmailFwdStatus.ELM_STATUS_PENDING_UPDATE;

	/**
	 * <code>clientHold</code> constant
	 */
	public final static String STATUS_CLIENT_HOLD = EPPEmailFwdStatus.ELM_STATUS_CLIENT_HOLD;

	/**
	 * <code>clientRenewProhibited</code> constant used with
	 * 
	 * {@link #setUpdateAttrib(int, EPPEmailFwdStatus, int)}.
	 */
	public final static String STATUS_CLIENT_RENEW_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_CLIENT_RENEW_PROHIBITED;

	/**
	 * <code>clientTransferProhibited</code> constant used with
	 * 
	 * {@link #setUpdateAttrib(int, EPPEmailFwdStatus, int)}.
	 */
	public final static String STATUS_CLIENT_TRANSFER_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_CLIENT_TRANSFER_PROHIBITED;

	/**
	 * <code>clientUpdateProhibited</code> constant used with
	 * 
	 * {@link #setUpdateAttrib(int, EPPEmailFwdStatus, int)}.
	 */
	public final static String STATUS_CLIENT_UPDATE_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED;

	/**
	 * <code>clientDeleteProhibited</code> constant used with
	 * 
	 * {@link #setUpdateAttrib(int, EPPEmailFwdStatus, int)}.
	 */
	public final static String STATUS_CLIENT_DELETE_PROHIBITED = EPPEmailFwdStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED;

	/**
	 * Transfer approve operation constant used with
	 * 
	 * {@link #setTransferOpCode(String)}
	 */
	public static final String TRANSFER_APPROVE = EPPCommand.OP_APPROVE;

	/**
	 * Transfer cancel operation constant used with
	 * 
	 * {@link #setTransferOpCode(String)}
	 */
	public static final String TRANSFER_CANCEL = EPPCommand.OP_CANCEL;

	/**
	 * Transfer query operation constant used with
	 * 
	 * {@link #setTransferOpCode(String)}
	 */
	public static final String TRANSFER_QUERY = EPPCommand.OP_QUERY;

	/**
	 * Transfer reject operation constant used with
	 * 
	 * {@link #setTransferOpCode(String)}
	 */
	public static final String TRANSFER_REJECT = EPPCommand.OP_REJECT;

	/**
	 * Transfer request operation constant used with
	 * 
	 * {@link #setTransferOpCode(String)}
	 */
	public static final String TRANSFER_REQUEST = EPPCommand.OP_REQUEST;

	/**
	 * Administrative contact constant used with
	 * 
	 * {@link #addContact(String, String)}
	 */
	public static final String CONTACT_ADMINISTRATIVE = EPPEmailFwdContact.TYPE_ADMINISTRATIVE;

	/**
	 * Billing contact constant used with
	 * 
	 * {@link #addContact(String, String)}
	 */
	public static final String CONTACT_BILLING = EPPEmailFwdContact.TYPE_BILLING;

	/**
	 * Technical contact constant used with
	 * 
	 * {@link #addContact(String, String)}
	 */
	public static final String CONTACT_TECHNICAL = EPPEmailFwdContact.TYPE_TECHNICAL;

	/** Period month unit contant. */
	public static final String PERIOD_MONTH = EPPEmailFwdPeriod.PERIOD_UNIT_MONTH;

	/** Period year unit constant. This is the default unit. */
	public static final String PERIOD_YEAR = EPPEmailFwdPeriod.PERIOD_UNIT_YEAR;

	/** Intance variable for a vector of Email Forward Names */
	private Vector myEmailFwdList = new Vector();

	/** Forward to e-mail address */
	private String myForwardTo = null;

	/** Attribute contains list of upto 4 Contacts */
	private Vector myContactList = new Vector();

	/**
	 * <code>Vector</code> of <code>UpdateAttrib</code> instances, holding
	 * update list attributes (contacts, statuses).
	 */
	private Vector myUpdateAttribs = new Vector();

	/** An instance of a session. */
	private EPPSession mySession = null;

	/** Transaction Id provided by cliet */
	private String myTransId = null;

	/** The Expiration Date. */
	private Date myExpirationDate;

	/** Transfer Operation Code */
	private String myTransferOpCode;

	/**
	 * This is Attribute Contains Validity Period : duration which email forward
	 * is registered for.
	 */
	private int myPeriodLength = 1;

	/**
	 * This is Attribute Contains Validity Unit :time unit where Period Length
	 * is mussured by. ie. year / month
	 */
	private String myPeriodUnit;

	/**
	 * This is Attribute Contains Authorization String provided by client when
	 * manipulation information on the Server
	 */
	private String myAuthString;

	/**
	 * This attributes contains the roid for hte regisrant or contact object
	 * that the <code>myAuthString</code> is associated with.
	 */
	private String authRoid;

	/**
	 * Extension objects associated with the command. This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector extensions = null;

	/** Email Forward Registrant Contact Identifier. */
	private String myRegistrant = null;

	/**
	 * Constructs an <code>EPPEmailFwd</code> given an initialized EPP
	 * session.
	 * 
	 * @param newSession
	 *            Server session to use.
	 */
	public EPPEmailFwd(EPPSession newSession) {
		mySession = newSession;

		return;
	}

	/**
	 * Adds a command extension object.
	 * 
	 * @param aExtension
	 *            command extension object associated with the command
	 */
	public void addExtension(EPPCodecComponent aExtension) {
		if (this.extensions == null) {
			this.extensions = new Vector();
		}

		this.extensions.addElement(aExtension);
	}

	// End EPPEmailFwd.addExtension(EPPCodecComponent)

	/**
	 * Sets a command extension object.
	 * 
	 * @param aExtension
	 *            command extension object associated with the command
	 * 
	 * @deprecated Replaced by <code>addExtension(EPPCodecComponent)</code>. This
	 *             method will add the extension as is done in <code>addExtension(EPPCodecComponet)</code>.
	 */
	public void setExtension(EPPCodecComponent aExtension) {
		this.addExtension(aExtension);
	}

	// End EPPEmailFwd.setExtension(EPPCodecComponent)

	/**
	 * Sets the command extension objects.
	 * 
	 * @param aExtensions
	 *            command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		this.extensions = aExtensions;
	}

	// End EPPEmailFwd.setExtensions(Vector)

	/**
	 * Gets the command extensions.
	 * 
	 * @return <code>Vector</code> of concrete <code>EPPCodecComponent</code>
	 *         associated with the command if exists; <code>null</code>
	 *         otherwise.
	 */
	public Vector getExtensions() {
		return this.extensions;
	}

	// End EPPEmailFwd.getExtensions()

	/**
	 * Adds an email forward for use with a <code>send</code> method. Adding
	 * more than one email forward is only supported by <code>sendCheck</code>.
	 * 
	 * @param newEmailFwd
	 *            Email Forward name to add
	 */
	public void addEmailFwdName(String newEmailFwd) {
		myEmailFwdList.addElement(newEmailFwd);

		return;
	}

	/**
	 * Will add a new contact which includes a name and a type.
	 * 
	 * @param newName
	 *            Contact Name
	 * @param newType
	 *            Contact Type, which should be a
	 *            <code>EPPEmailFwd.CONTACT_</code> constant.
	 */
	public void addContact(String newName, String newType) {
		myContactList.addElement(new EPPEmailFwdContact(newName, newType));
	}

	/**
	 * Sets the Email Forward expiration date.
	 * 
	 * @param newExpirationDate
	 *            Email Forward expiration date
	 */
	public void setExpirationDate(Date newExpirationDate) {
		myExpirationDate = newExpirationDate;
	}

	/**
	 * Gets the Email Forward expiration date.
	 * 
	 * @return Email Forward expiration date
	 */
	public Date getExpirationDate() {
		return myExpirationDate;
	}

	/**
	 * Sets the transfer operation for a call to <code>encodeTransfer</code>.
	 * The transfer code must be set to one of the
	 * <code>EPPEmailFwd.TRANSFER_</code> constants.
	 * 
	 * @param newTransferOpCode
	 *            One of the <code>EPPEmailFwd.TRANSFER_</code> constants
	 */
	public void setTransferOpCode(String newTransferOpCode) {
		myTransferOpCode = newTransferOpCode;
	}

	/**
	 * Sets the client transaction identifier.
	 * 
	 * @param newTransId
	 *            Client transaction identifier
	 */
	public void setTransId(String newTransId) {
		myTransId = newTransId;
	}

	/**
	 * Sets the authorization string associated with an <code>sendCreate</code>
	 * and <code>sendTransfer</code>.
	 * 
	 * @param newAuthString
	 *            Authorization string
	 */
	public void setAuthString(String newAuthString) {
		myAuthString = newAuthString;
	}

	/**
	 * Sets the authorization string associated with an <code>sendCreate</code>
	 * and <code>sendTransfer</code>.
	 * 
	 * @return Authorization string if defined; <code>null</code> otherwise.
	 */
	public String getAuthString() {
		return myAuthString;
	}

	/**
	 * Sets the authorization roid that is used to identify the registrant or
	 * contact object if and only if the value of authInfo, set by
	 * <code>setAuthString(String)</code>, is associated with the registrant
	 * or contact object. This can be used with <code>sendTransfer</code>
	 * along with setting the authInfo with the
	 * <code>setAuthString(String)</code> method.
	 * 
	 * @return Roid of registrant or contact object if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getAuthRoid() {
		return this.authRoid;
	}

	/**
	 * Gets the authorization roid that is used to identify the registrant or
	 * contact object if and only if the value of authInfo, set by
	 * <code>setAuthString(String)</code>, is associated with the registrant
	 * or contact object. This can be used with <code>sendTransfer</code>
	 * along with setting the authInfo with the
	 * <code>setAuthString(String)</code> method.
	 * 
	 * @param aAuthRoid
	 *            Roid of registrant or contact object
	 */
	public void setAuthRoid(String aAuthRoid) {
		this.authRoid = aAuthRoid;
	}

	/**
	 * Sets the forward to e-mail address associated with
	 * <code>sendCreate</code> and <code>sendUpdate</code>.
	 * 
	 * @param newForwardTo
	 *            Forward to e-mail address
	 */
	public void setForwardTo(String newForwardTo) {
		myForwardTo = newForwardTo;
	}

	/**
	 * Gets the forward to e-mail address associated with
	 * <code>sendCreate</code> and <code>sendUpdate</code>.
	 * 
	 * @return Forward to e-mail address
	 */
	public String getForwardTo() {
		return myForwardTo;
	}

	/**
	 * Gets the Email Forward Registrant Contact Identifier.
	 * 
	 * @return Email Forward Registrant Contact Identifier if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getRegistrant() {
		return myRegistrant;
	}

	/**
	 * Sets the Email Forward Registrant Contact Identifier.
	 * 
	 * @param aRegistrant
	 *            Email Forward Registrant Contact Identifier.
	 */
	public void setRegistrant(String aRegistrant) {
		myRegistrant = aRegistrant;
	}

	/**
	 * Sends an Email Forward Create Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Sets the email forward to create.
	 * Only one email forward is valid. </li>
	 * <li> <code>setForwardTo</code> - Sets the forward to e-mail address
	 * </li>
	 * <li> <code>setAuthString</code> - Sets the email forward authorization
	 * string. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * <li> <code>setPeriodLength</code> - Sets the registration period
	 * (default = 1) </li>
	 * <li> <code>setPeriodUnit</code> - Sets the registration period unit
	 * (default = <code>PERIOD_YEAR</code>) </li>
	 * <li> <code>setRegistrant</code> - Sets the Registrant for the email
	 * forward. This is required for thick registries. </li>
	 * <li> <code>addContact</code> - Add email forward contact </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPEmailFwdCreateResp</code> containing the Email Forward
	 *         create result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the create command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPEmailFwdCreateResp sendCreate() throws EPPCommandException {
		// Invalid number of Email Forward Names?
		if (myEmailFwdList.size() != 1) {
			throw new EPPCommandException(
					"One Email Forward Name is required for sendCreate()");
		}

		// Is the Contact Mapping supported?
		if (EPPFactory.getInstance().hasService(
				EPPEmailFwdMapFactory.NS_CONTACT)) {
			// No contacts specified?
			if (myContactList.size() == 0) {
				myContactList = null;
			}
		}
		else {
			myContactList = null;
		}

		EPPEmailFwdPeriod thePeriod = null;

		// registration period specified?
		if (myPeriodLength >= 0) {
			// period specified?
			if (myPeriodUnit != null) {
				thePeriod = new EPPEmailFwdPeriod(myPeriodUnit, myPeriodLength);
			}
			else {
				thePeriod = new EPPEmailFwdPeriod(myPeriodLength);
			}
		}

		// Create the command
		EPPEmailFwdCreateCmd theCommand = new EPPEmailFwdCreateCmd(myTransId,
				(String) myEmailFwdList.firstElement(), myForwardTo,
				myContactList, thePeriod, new EPPAuthInfo(myAuthString));

		// Set registrant
		if (myRegistrant != null) {
			theCommand.setRegistrant(myRegistrant);
		}

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return (EPPEmailFwdCreateResp) this.mySession.processDocument(theCommand, EPPEmailFwdCreateResp.class);
	}

	// End EPPEmailFwd.sendCreate()

	/**
	 * Sends an Email Forward Update Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Sets the email forward to update.
	 * Only one email forward is valid. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * <li> <code>setForwardTo</code> - Sets the forward to e-mail address
	 * </li>
	 * <li> <code>setUpdateAttrib(int,EmailFwdStatus,int)</code> -
	 * Adds/removes statuses </li>
	 * <li> <code>setUpdateAttrib(int,String,String,int)</code> - Adds/removes
	 * contacts </li>
	 * <li> <code>setRegistrant</code> - Sets the Registrant for the email
	 * forward. </li>
	 * <li> <code>setAuthString</code> - Sets the email forward authorization
	 * string. </li>
	 * </ul>
	 * 
	 * At least one update attribute needs to be set.
	 * 
	 * @return <code>EPPResponse</code> containing the Email Forward update
	 *         result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the update command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPResponse sendUpdate() throws EPPCommandException {
		// Invalid number of Email Forward Names?
		if (myEmailFwdList.size() != 1) {
			throw new EPPCommandException(
					"One Email Forward Name is required for sendUpdate()");
		}

		// Update vectors
		Vector contactUpdate = null;
		Vector statusUpdate = null;

		// ADD Elements
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

		// Attributes to add
		EPPEmailFwdAddRemove addItems = null;

		// Any add updates?
		if ((contactUpdate != null) || (statusUpdate != null)) {
			addItems = new EPPEmailFwdAddRemove(contactUpdate, statusUpdate);
		}

		// REMOVE Elements
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

		// Attributes to remove
		EPPEmailFwdAddRemove removeItems = null;

		// Any remove updates?
		if ((contactUpdate != null) || (statusUpdate != null)) {
			removeItems = new EPPEmailFwdAddRemove(contactUpdate, statusUpdate);
		}

		// Attributes to change
		EPPEmailFwdAddRemove changeItems = null;

		// Any change updates?
		if ((myRegistrant != null) || (myForwardTo != null)
				|| (myAuthString != null)) {
			changeItems = new EPPEmailFwdAddRemove();
			
			// Registrant changed?
			if (myRegistrant != null) {
				changeItems.setRegistrant(myRegistrant);
			}
			
			// Forward to address changed?
			if (myForwardTo != null) {
				changeItems.setForwardTo(myForwardTo);
			}
			
			// Auth info changed?
			if (myAuthString != null) {
				changeItems.setAuthInfo(new EPPAuthInfo(myAuthString));
			}
		}

		// Create the command
		EPPEmailFwdUpdateCmd theCommand = new EPPEmailFwdUpdateCmd(myTransId,
				(String) myEmailFwdList.firstElement(), addItems, removeItems,
				changeItems);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return this.mySession.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPEmailFwd.sendUpdate()

	/**
	 * Sends an Email Forward Transfer Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Sets the email forward for transfer
	 * command. Only one email forward is valid. </li>
	 * <li> <code>setTransferOpCode</code> - Sets the email forward transfer
	 * operation. </li>
	 * </ul>
	 * 
	 * 
	 * <ul>
	 * <li> <code>setAuthString</code> - Sets the email forward authorization
	 * string. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * <li> <code>setPeriodLength</code> - Sets the registration period
	 * (default = 1) </li>
	 * <li> <code>setPeriodUnit</code> - Sets the registration period unit
	 * (default = <code>PERIOD_YEAR</code>) </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPEmailFwdTransferResp</code> containing the Email
	 *         Forward transfer result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the create command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPEmailFwdTransferResp sendTransfer() throws EPPCommandException {
		// Invalid number of Email Forward Names?
		if (myEmailFwdList.size() != 1) {
			throw new EPPCommandException(
					"One Email Forward Name is required for sendTransfer()");
		}

		// Transfer Operation Code not specified?
		if (myTransferOpCode == null) {
			throw new EPPCommandException(
					"Transfer Operation Code is required.");
		}

		// Create the command
		EPPEmailFwdTransferCmd theCommand = new EPPEmailFwdTransferCmd(
				myTransId, myTransferOpCode, (String) myEmailFwdList
						.firstElement());

		// Authorization Info String specified?
		if (this.myAuthString != null) {
			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.myAuthString);
			
			// Authorization Info ROID specified?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}
			
			theCommand.setAuthInfo(theAuthInfo);				
		}

		// Transfer Request?
		if (myTransferOpCode.equals(TRANSFER_REQUEST)) {
			// Set Transfer Request specific attributes
			if (myAuthString == null) {
				throw new EPPCommandException(
						"Auth Info must be set on a Transfer Request");
			}

			// Period not specified?
			EPPEmailFwdPeriod thePeriod = null;

			if (myPeriodLength < 0) {
				thePeriod = null;
			}
			else if (myPeriodUnit == null) {
				thePeriod = new EPPEmailFwdPeriod(myPeriodLength);
			}
			else {
				thePeriod = new EPPEmailFwdPeriod(myPeriodUnit, myPeriodLength);
			}

			theCommand.setPeriod(thePeriod);
		}

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return (EPPEmailFwdTransferResp) this.mySession.processDocument(theCommand, EPPEmailFwdTransferResp.class);
	}

	// End EPPEmailFwd.sendTransfer()

	/**
	 * Sends an Email Forward Renew Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Sets the email forward to renew.
	 * Only one email forward is valid. </li>
	 * <li> <code>setExpirationDate</code> - Sets current expiration date.
	 * </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * <li> <code>setPeriodLength</code> - Sets the registration period
	 * (default = 1) </li>
	 * <li> <code>setPeriodUnit</code> - Sets the registration period unit
	 * (default = <code>PERIOD_YEAR</code>) </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPEmailFwdRenewResp</code> containing the Email Forward
	 *         renew result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the renew command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPEmailFwdRenewResp sendRenew() throws EPPCommandException {
		// Invalid number of Email Forward Names?
		if (myEmailFwdList.size() != 1) {
			throw new EPPCommandException(
					"One Email Forward Name is required for sendCreate()");
		}

		EPPEmailFwdPeriod thePeriod = null;

		// registration period specified?
		if (myPeriodLength >= 0) {
			// period specified?
			if (myPeriodUnit != null) {
				thePeriod = new EPPEmailFwdPeriod(myPeriodUnit, myPeriodLength);
			}
			else {
				thePeriod = new EPPEmailFwdPeriod(myPeriodLength);
			}
		}

		// Create the command
		EPPEmailFwdRenewCmd theCommand = new EPPEmailFwdRenewCmd(myTransId,
				(String) myEmailFwdList.firstElement(), myExpirationDate,
				thePeriod);

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return (EPPEmailFwdRenewResp) this.mySession.processDocument(theCommand, EPPEmailFwdRenewResp.class);
	}

	// End EPPEmailFwd.sendRenew()

	/**
	 * Sends an Email Forward Info Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Sets the email forward to get info
	 * for. Only one email forward is valid. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPEmailFwdInfoResp</code> containing the Email Forward
	 *         information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the info command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPEmailFwdInfoResp sendInfo() throws EPPCommandException {
		// Invalid number of Email Forward Names?
		if (myEmailFwdList.size() != 1) {
			throw new EPPCommandException(
					"One Email Forward Name is required for sendInfo()");
		}

		// Create the command
		EPPEmailFwdInfoCmd theCommand = new EPPEmailFwdInfoCmd(myTransId,
				(String) myEmailFwdList.firstElement());

		// Authorization string was provided?
		if (this.myAuthString != null) {

			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.myAuthString);

			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}

			theCommand.setAuthInfo(theAuthInfo);
		}

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return (EPPEmailFwdInfoResp) this.mySession.processDocument(theCommand, EPPEmailFwdInfoResp.class);
	}

	// End EPPEmailFwd.sendInfo()

	/**
	 * Sends an Email Forward Check Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Adds an email forward to check.
	 * More than one email forward can be checked in <code>sendCheck</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPEmailFwdCheckResp</code> containing the Email Forward
	 *         check information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the check command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPEmailFwdCheckResp sendCheck() throws EPPCommandException {
		// Create the command
		EPPEmailFwdCheckCmd theCommand = new EPPEmailFwdCheckCmd(myTransId,
				myEmailFwdList);

		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return (EPPEmailFwdCheckResp) this.mySession.processDocument(theCommand, EPPEmailFwdCheckResp.class);
	}

	// End EPPEmailFwd.sendCheck()

	/**
	 * Sends an Email Forward Delete Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addEmailFwdName</code> - Sets the email forward to delete.
	 * Only one email forward is valid. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPResponse</code> containing the delete result
	 *         information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the delete command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPResponse sendDelete() throws EPPCommandException {
		// Invalid number of Email Forward Names?
		if (myEmailFwdList.size() != 1) {
			throw new EPPCommandException(
					"One Email Forward Name is required for sendDelete()");
		}

		// Create the command
		EPPEmailFwdDeleteCmd theCommand = new EPPEmailFwdDeleteCmd(myTransId,
				(String) myEmailFwdList.firstElement());

		// Set command extension
		theCommand.setExtensions(this.extensions);

		// Reset email forward attributes
		resetEmailFwd();

		// process the command and response
		return this.mySession.processDocument(theCommand, EPPResponse.class);
	}

	// End EPPEmailFwd.sendDelete()

	/**
	 * Resets the email forward instance to its initial state.
	 */
	private void resetEmailFwd() {
		this.myEmailFwdList = new Vector();
		this.myContactList = new Vector();
		this.myUpdateAttribs = new Vector();
		this.myForwardTo = null;
		this.myTransId = null;
		this.myExpirationDate = null;
		this.myTransferOpCode = null;
		this.myPeriodLength = 1;
		this.myPeriodUnit = null;
		this.myAuthString = null;
		this.authRoid = null;
		this.extensions = null;
		this.myRegistrant = null;
	}

	// End EPPEmailFwd.resetEmailFwd()

	/**
	 * Sets an update to an email forward status. Email Forward statuses can be
	 * either added or removed.
	 * 
	 * @param newType
	 *            Must be set to <code>STATUS</code>
	 * @param newName
	 *            Name of status, which can use one of the <code>STATUS_</code>
	 *            constants
	 * @param newOp
	 *            Either <code>ADD</code> or <code>REMOVE</code>
	 * 
	 * @exception EPPCommandException
	 *                Invalid type or operation
	 */
	public void setUpdateAttrib(int newType, EPPEmailFwdStatus newName,
			int newOp) throws EPPCommandException {
		if (newType != STATUS) {
			throw new EPPCommandException(
					"setUpdateAttrib(int, EPPEmailFwdStatus, int): " + newType
							+ " is an invalid type");
		}

		if (newOp == CHANGE) {
			throw new EPPCommandException(
					"setUpdateAttrib(int, EPPEmailFwdStatus, int): "
							+ "CHANGE is an invalid operation");
		}

		myUpdateAttribs.addElement(new UpdateAttrib(newType, newName, newOp));
	}

	// End EPPEmailFwd.setUpdateAttrib(int, EPPEmailFwdStatus, int)

	/**
	 * Sets an update to an email forward contact. Email Forward contacts can be
	 * either added or removed.
	 * 
	 * @param newType
	 *            Must be set to <code>CONTACT</code>
	 * @param newVal
	 *            Contact Identifier
	 * @param newValType
	 *            Contact Type, which should be one of the <code>CONTACT_</code>
	 *            constants.
	 * @param newOp
	 *            Either <code>ADD</code> or <code>REMOVE</code>
	 * 
	 * @exception EPPCommandException
	 *                Invalid type or operation
	 */
	public void setUpdateAttrib(int newType, String newVal, String newValType,
			int newOp) throws EPPCommandException {
		if (newType != CONTACT) {
			throw new EPPCommandException(
					"setUpdateAttrib(int, String, String, int): " + newType
							+ " is an invalid type");
		}

		if (newOp == CHANGE) {
			throw new EPPCommandException(
					"setUpdateAttrib(int, String, String, int): "
							+ "CHANGE is an invalid operation");
		}

		myUpdateAttribs.addElement(new UpdateAttrib(newType,
				new EPPEmailFwdContact(newVal, newValType), newOp));
	}

	// End EPPEmailFwd.setUpdateAttrib(int, String, String, int)

	/**
	 * Gets the response associated with the last command. This method can be
	 * used to retrieve the server error response in the catch block of
	 * EPPCommandException.
	 * 
	 * @return Response associated with the last command
	 */
	public EPPResponse getResponse() {
		return this.mySession.getResponse();
	}

	/**
	 * Gets the registration period.
	 * 
	 * @return Registration period; <code>null</code> otherwise.
	 */
	public int getPeriodLength() {
		return myPeriodLength;
	}

	/**
	 * Gets the registration period.
	 * 
	 * @param newPeriodLength
	 *            Registration period
	 */
	public void setPeriodLength(int newPeriodLength) {
		myPeriodLength = newPeriodLength;
	}

	/**
	 * Gets the registration period unit.
	 * 
	 * @return Registration period unit if defined; <code>null</code>
	 *         otherwise.
	 */
	public String getPeriodUnit() {
		return myPeriodUnit;
	}

	/**
	 * Sets the registration period unit to either <code>PERIOD_MONTH</code>
	 * or <code>PERIOD_YEAR</code>.
	 * 
	 * @param newPeriodUnit
	 *            Registration period unit
	 */
	public void setPeriodUnit(String newPeriodUnit) {
		myPeriodUnit = newPeriodUnit;
	}

	/**
	 * Gets the count of update attributes of a given type (<code>CONTACT</code>
	 * or <code>CONTACT</code>) and a given operation (<code>ADD</code>,
	 * <code>REMOVE</code>, or <code>CHANGE</code>).
	 * 
	 * @param newType
	 *            Type of attribute, which should be either <code>CONTACT</code>
	 *            or <code>CONTACT</code>.
	 * @param newOp
	 *            Update operation, which should be either <code>ADD</code>,
	 *            <code>REMOVE</code>, or <code>CHANGE</code>
	 * 
	 * @return Number of update attributes
	 */
	private int getUpdateAttribCnt(int newType, int newOp) {
		int cnt = 0;

		for (int i = 0; i < myUpdateAttribs.size(); i++) {
			UpdateAttrib currAttrib = (UpdateAttrib) myUpdateAttribs
					.elementAt(i);

			// Desired type?
			if (currAttrib.getType(newType, newOp)) {
				cnt++;
			}
		}

		return cnt;
	}

	// end EPPEmailFwd.getUpdateAttribCnt(int, int)

	/**
	 * Gets the update <code>Vector</code> given a type and operation.
	 * 
	 * @param newType
	 *            Type of attribute, which should be either <code>CONTACT</code>
	 *            or <code>STATUS</code>.
	 * @param newOp
	 *            Update operation, which should be either <code>ADD</code>,
	 *            <code>REMOVE</code>, or <code>CHANGE</code>
	 * 
	 * @return <code>Vector</code> of matching <code>UpdateAttrib</code>
	 *         instances.
	 */
	private Vector getUpdateAttribVector(int newType, int newOp) {
		int cnt = getUpdateAttribCnt(newType, newOp);

		if (cnt == 0) {
			return null;
		}

		Vector theAttribs = new Vector(cnt);

		for (int i = 0; i < myUpdateAttribs.size(); i++) {
			UpdateAttrib currAttr = (UpdateAttrib) myUpdateAttribs.elementAt(i);

			if (currAttr.getType(newType, newOp)) {
				theAttribs.addElement(currAttr.getVal());
			}
		}

		return theAttribs;
	}

	/**
	 * Inner utility class for holding an email forward update attribute
	 * setting.
	 */
	private static class UpdateAttrib {
		/**
		 * Update type (<code>EPPEmailFwd.CONTACT</code> or
		 * <code>EPPEmailFwd.STATUS</code>.
		 */
		private int myType = 0;

		/** Update value. */
		private Object myVal = null;

		/**
		 * Update operation (<code>EPPEmailFwd.ADD</code>,
		 * <code>EPPEmailFwd.REMOVE</code>, or
		 * <code>EPPEmailFwd.CHANGE</code>.
		 */
		private int myOp = 0;

		/**
		 * Creates a new UpdateAttrib object.
		 * 
		 * @param newType
		 *            DOCUMENT ME!
		 * @param newVal
		 *            DOCUMENT ME!
		 * @param newOp
		 *            DOCUMENT ME!
		 * 
		 * @throws EPPCommandException
		 *             DOCUMENT ME!
		 */
		public UpdateAttrib(int newType, Object newVal, int newOp)
				throws EPPCommandException {
			// Invalid update type?
			if ((newType != EPPEmailFwd.CONTACT)
					&& (newType != EPPEmailFwd.STATUS)) {
				throw new EPPCommandException(
						"UpdateAttrib.UpdateAttrib() : Invalid Type of "
								+ newType);
			}

			// Invalid update operation?
			if ((newOp != EPPEmailFwd.ADD) && (newOp != EPPEmailFwd.REMOVE)
					&& (newOp != EPPEmailFwd.CHANGE)) {
				throw new EPPCommandException(
						"UpdateAttrib.UpdateAttrib() : Invalid Operation of "
								+ newOp);
			}

			// Invalid update value?
			if (newVal == null) {
				throw new EPPCommandException(
						"UpdateAttrib.UpdateAttrib() : Invalid Value (null)");
			}

			myType = newType;
			myVal = newVal;
			myOp = newOp;
		}

		/**
		 * DOCUMENT ME!
		 * 
		 * @param newType
		 *            DOCUMENT ME!
		 * @param newOp
		 *            DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
		public boolean getType(int newType, int newOp) {
			if ((newType == myType) && (newOp == myOp)) {
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
			return myVal;
		}
	}
	// End static class UpdateAttrib
}
// End class EPPEmailFwd
