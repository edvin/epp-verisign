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

import org.w3c.dom.Document;

import com.verisign.epp.codec.defReg.EPPDefRegAddRemove;
import com.verisign.epp.codec.defReg.EPPDefRegCheckCmd;
import com.verisign.epp.codec.defReg.EPPDefRegCheckResp;
import com.verisign.epp.codec.defReg.EPPDefRegCreateCmd;
import com.verisign.epp.codec.defReg.EPPDefRegCreateResp;
import com.verisign.epp.codec.defReg.EPPDefRegDeleteCmd;
import com.verisign.epp.codec.defReg.EPPDefRegInfoCmd;
import com.verisign.epp.codec.defReg.EPPDefRegInfoResp;
import com.verisign.epp.codec.defReg.EPPDefRegName;
import com.verisign.epp.codec.defReg.EPPDefRegPeriod;
import com.verisign.epp.codec.defReg.EPPDefRegRenewCmd;
import com.verisign.epp.codec.defReg.EPPDefRegRenewResp;
import com.verisign.epp.codec.defReg.EPPDefRegStatus;
import com.verisign.epp.codec.defReg.EPPDefRegTransferCmd;
import com.verisign.epp.codec.defReg.EPPDefRegTransferResp;
import com.verisign.epp.codec.defReg.EPPDefRegUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;

/**
 * <code>EPPDefReg</code> is the primary client interface class used for
 * defReg management. An instance of <code>EPPDefReg</code> is created with an
 * initialized <code>EPPSession</code>, and can be used for more than one
 * request within a single thread. A set of setter methods are provided to set
 * the attributes before a call to one of the send action methods. The responses
 * returned from the send action methods are either instances of
 * <code>EPPResponse</code> or instances of response classes in the
 * <code>com.verisign.epp.codec.defReg</code> package. <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 * 
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.defReg.EPPDefRegCreateResp
 * @see com.verisign.epp.codec.defReg.EPPDefRegInfoResp
 * @see com.verisign.epp.codec.defReg.EPPDefRegCheckResp
 * @see com.verisign.epp.codec.defReg.EPPDefRegTransferResp
 */
public class EPPDefReg {
	/** The Following are add to encapsulate the Update Command */
	public static final int CONTACT = 1;

	/** DOCUMENT ME! */
	public static final int STATUS = 2;

	/** DOCUMENT ME! */
	public static final int ADD = 1;

	/** DOCUMENT ME! */
	public static final int REMOVE = 2;

	/** DOCUMENT ME! */
	public static final int CHANGE = 2;

	/** Status constants */
	public final static String STATUS_OK = EPPDefRegStatus.ELM_STATUS_OK;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_RENEW_PROHIBITED = EPPDefRegStatus.ELM_STATUS_SERVER_RENEW_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_TRANSFER_PROHIBITED = EPPDefRegStatus.ELM_STATUS_SERVER_TRANSFER_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_UPDATE_PROHIBITED = EPPDefRegStatus.ELM_STATUS_SERVER_UPDATE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_DELETE_PROHIBITED = EPPDefRegStatus.ELM_STATUS_SERVER_DELETE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_RENEW_PROHIBITED = EPPDefRegStatus.ELM_STATUS_CLIENT_RENEW_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_TRANSFER_PROHIBITED = EPPDefRegStatus.ELM_STATUS_CLIENT_TRANSFER_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_UPDATE_PROHIBITED = EPPDefRegStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_DELETE_PROHIBITED = EPPDefRegStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_PENDING_DELETE = EPPDefRegStatus.ELM_STATUS_PENDING_DELETE;

	/** DOCUMENT ME! */
	public final static String STATUS_PENDING_TRANSFER = EPPDefRegStatus.ELM_STATUS_PENDING_TRANSFER;

	/** DOCUMENT ME! */
	public final static String STATUS_PENDING_VERIFICATION = EPPDefRegStatus.ELM_STATUS_PENDING_VERIFICATION;

	/** Transfer Operation constants */
	public static final String TRANSFER_APPROVE = EPPCommand.OP_APPROVE;

	/** DOCUMENT ME! */
	public static final String TRANSFER_CANCEL = EPPCommand.OP_CANCEL;

	/** DOCUMENT ME! */
	public static final String TRANSFER_QUERY = EPPCommand.OP_QUERY;

	/** DOCUMENT ME! */
	public static final String TRANSFER_REJECT = EPPCommand.OP_REJECT;

	/** DOCUMENT ME! */
	public static final String TRANSFER_REQUEST = EPPCommand.OP_REQUEST;

	/**
	 * Standard level of Defensive Registration. For example, use this level
	 * when checking or creating name "john.smith".
	 */
	public static final String LEVEL_STANDARD = EPPDefRegName.LEVEL_STANDARD;

	/**
	 * Premium level of Defensive Registration. For example, use this level when
	 * checking or creating name "smith".
	 */
	public static final String LEVEL_PREMIUM = EPPDefRegName.LEVEL_PREMIUM;

	/** Currently Only two Priod Type is supported. */
	public static final String PERIOD_MONTH = EPPDefRegPeriod.PERIOD_UNIT_MONTH;

	/** DOCUMENT ME! */
	public static final String PERIOD_YEAR = EPPDefRegPeriod.PERIOD_UNIT_YEAR;

	/** Intance variable for a vector of DefReg Name */
	private Vector myDefRegList = new Vector();

	/** Attribute contains list of upto 4 Contacts */
	private Vector myContactList = new Vector();

	/** An instance of a session. */
	private EPPSession mySession = null;

	/** Transaction Id provided by cliet */
	private String myTransId = null;

	/** Roid provided by cliet */
	private String roid = null;

	/** Registrant provided by cliet */
	private String registrant = null;

	/** DefReg TradeMark of defReg to create . */
	private String tm = null;

	/** DefReg TradeMarkCountry of defReg to create . */
	private String tmcountry = null;

	/** DefReg TradeMarkDate of defReg to create . */
	private Date tmdate = null;

	/** DefReg AdminContact of defReg to create . */
	private String admincontact = null;

	/** The Expiration Date. */
	private Date myExpirationDate;

	/** Transfer Operation Code */
	private String myTransferOpCode;

	/**
	 * This is Attribute Contains Validity Period : duration which defReg is
	 * registered for.
	 */
	private int thePeriodLength = 1;

	/**
	 * This is Attribute Contains Validity Unit :time unit where Period Length
	 * is mussured by. ie. year / month
	 */
	private String thePeriodUnit;

	/**
	 * This is Attribute Contains Authorization String provided by client when
	 * manipulation information on the Server
	 */
	private String theAuthString;

	/**
	 * This attributes contains the roid for hte regisrant or contact object
	 * that the <code>theAuthString</code> is associated with.
	 */
	private String authRoid;

	/**
	 * Extension objects associated with the command. This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector extensions = null;

	/** Statuses to add on call to <code>sendUpdate</code>. */
	private Vector myAddStatuses = null;

	/** Statuses to remove on call to <code>sendUpdate</code>. */
	private Vector myRemoveStatuses = null;

	/**
	 * Constructs an <code>EPPDefReg</code> given an initialized EPP session.
	 * 
	 * @param newSession
	 *            Server session to use.
	 */
	public EPPDefReg(EPPSession newSession) {
		mySession = newSession;
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

	// End EPPDefREg.addExtension(EPPCodecComponent)

	/**
	 * Sets a command extension object.
	 * 
	 * @param aExtension
	 *            command extension object associated with the command
	 * 
	 * @deprecated Replaced by {@link #addExtension(EPPCodecComponent)}. This
	 *             method will add the extension as is done in {@link #addExtension(EPPCodecComponent)}.
	 */
	public void setExtension(EPPCodecComponent aExtension) {
		this.addExtension(aExtension);
	}

	// End EPPDefREg.setExtension(EPPCodecComponent)

	/**
	 * Sets the command extension objects.
	 * 
	 * @param aExtensions
	 *            command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		this.extensions = aExtensions;
	}

	// End EPPDefREg.setExtensions(Vector)

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

	// End EPPDefREg.getExtensions()

	/**
	 * Adds a defReg name for use with a <code>send</code> method. Adding more
	 * than one defReg names is only supported by <code>sendCheck</code>.
	 * 
	 * @param newLevel
	 *            Either <code>LEVEL_STANDARD</code> or
	 *            <code>LEVEL_PREMIUM</code>
	 * @param newName
	 *            Defensive Registration Name
	 */
	public void addDefRegName(String newLevel, String newName) {
		EPPDefRegName theName = new EPPDefRegName(newLevel, newName);

		myDefRegList.addElement(theName);
	}

	/**
	 * Sets the DefReg Roid.
	 * 
	 * @param aRoid
	 *            DefReg expiration date
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	/**
	 * Gets the DefReg Roid.
	 * 
	 * @return DefReg Roid
	 */
	public String getRoid() {
		return roid;
	}

	/**
	 * Get the trademark informnation.
	 * 
	 * @return DefReg tradeMark
	 */
	public String getTm() {
		return tm;
	}

	// End EPPDefRegInfoCmd.getTm()

	/**
	 * Set the trademark informnation.
	 * 
	 * @param aTm
	 *            DefReg registrant
	 */
	public void setTm(String aTm) {
		tm = aTm;
	}

	// End EPPDefRegInfoCmd.setTm(String)

	/**
	 * Get the trademark country informnation.
	 * 
	 * @return DefReg trademark country
	 */
	public String getTmCountry() {
		return tmcountry;
	}

	// End EPPDefRegInfoCmd.getTmCountry()

	/**
	 * Set the trademark country informnation.
	 * 
	 * @param aTmCountry
	 *            DefReg trademark country
	 */
	public void setTmCountry(String aTmCountry) {
		tmcountry = aTmCountry;
	}

	// End EPPDefRegInfoCmd.setTmCountry(String)

	/**
	 * Get the trademark date informnation.
	 * 
	 * @return DefReg trademark date
	 */
	public Date getTmDate() {
		return tmdate;
	}

	// End EPPDefRegInfoCmd.getTmDate()

	/**
	 * Set the trademark date informnation.
	 * 
	 * @param aTmDate
	 *            DefReg trademark date
	 */
	public void setTmDate(Date aTmDate) {
		tmdate = aTmDate;
	}

	// End EPPDefRegInfoCmd.setTmDate(String)

	/**
	 * Get the AdminContact informnation.
	 * 
	 * @return DefReg AdminContact
	 */
	public String getAdminContact() {
		return admincontact;
	}

	// End EPPDefRegInfoCmd.getAdminContact()

	/**
	 * Sets the DefReg Registrant.
	 * 
	 * @param aRegistrant
	 *            DefReg Registrant.
	 */
	public void setRegistrant(String aRegistrant) {
		registrant = aRegistrant;
	}

	/**
	 * Sets the DefReg Admin Contact.
	 * 
	 * @param aAdminContact
	 *            DefReg Admin Contact.
	 */
	public void setAdminContact(String aAdminContact) {
		admincontact = aAdminContact;
	}

	/**
	 * Gets the DefReg Registrant.
	 * 
	 * @return DefReg Registrant
	 */
	public String getRegistrant() {
		return registrant;
	}

	/**
	 * Sets the DefReg expiration date.
	 * 
	 * @param newExpirationDate
	 *            DefReg expiration date
	 */
	public void setExpirationDate(Date newExpirationDate) {
		myExpirationDate = newExpirationDate;
	}

	/**
	 * Gets the DefReg expiration date.
	 * 
	 * @return DefReg expiration date
	 */
	public Date getExpirationDate() {
		return myExpirationDate;
	}

	/**
	 * Sets the transfer operation for a call to <code>encodeTransfer</code>.
	 * The transfer code must be set to one of the
	 * <code>EPPDefReg.TRANSFER_</code> constants.
	 * 
	 * @param newTransferOpCode
	 *            One of the <code>EPPDefReg.TRANSFER_</code> constants
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
		theAuthString = newAuthString;
	}

	/**
	 * Sets the authorization string associated with an <code>sendCreate</code>
	 * and <code>sendTransfer</code>.
	 * 
	 * @return Authorization string if defined; <code>null</code> otherwise.
	 */
	public String getAuthString() {
		return theAuthString;
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
	 * Sends a Defensive Registration Create Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addDefRegName</code>- Sets the defensive registration name
	 * to create. Only one defensive registration name is valid. </li>
	 * <li> <code>setAuthString</code>- Sets the defensive registration
	 * authorization string. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li> <code>setPeriodLength</code>- Sets the registration period
	 * (default = 1) </li>
	 * <li> <code>setPeriodUnit</code>- Sets the registration period unit
	 * (default =<code>PERIOD_YEAR</code>) </li>
	 * <li> <code>setRegistrant</code>- Sets the Registrant for the defensive
	 * registration. This is required for thick registries. </li>
	 * <li> <code>setAdminContact</code>- Sets the adminitrator contact </li>
	 * <li> <code>setTm</code>- Sets the trademark </li>
	 * <li> <code>setTmCountry</code>- Sets the trademark country </li>
	 * <li> <code>setTmDate</code>- Sets the trademark date </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPDefRegCreateResp</code> containing the defensive
	 *         registration create result. Use
	 *         <code>EPPDefRegCreateResp.getRoid</code> to get the ROID
	 *         required for the rest of the <code>EPPDefReg</code> operations.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the create command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPDefRegCreateResp sendCreate() throws EPPCommandException {
		// Invalid number of defensive registration names?
		if (myDefRegList.size() != 1) {
			throw new EPPCommandException(
					"One Defensive Registration Name is required for sendCreate()");
		}

		EPPDefRegPeriod myPeriod = null;

		// Period not specified?
		if (thePeriodLength < 0) {
			myPeriod = null;
		}

		else if (thePeriodUnit == null) {
			myPeriod = new EPPDefRegPeriod(thePeriodLength);
		}

		else {
			myPeriod = new EPPDefRegPeriod(thePeriodUnit, thePeriodLength);
		}

		// Create the command
		EPPDefRegCreateCmd myCommand = new EPPDefRegCreateCmd(myTransId,
				(EPPDefRegName) myDefRegList.firstElement(), registrant, tm,
				tmcountry, tmdate, admincontact, myPeriod, new EPPAuthInfo(
						theAuthString));

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset defensive registration attributes
		resetDefReg();

		// process the command and response
		return (EPPDefRegCreateResp) this.mySession.processDocument(myCommand, EPPDefRegCreateResp.class);
	}

	// End EPPDefReg.sendCreate()

	/**
	 * Sends a Defensive Registration Update Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setRoid</code>- Sets the defensive registration ROID. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li> <code>addStatus</code>- Adds a status to the defensive
	 * registration. More than one status can be added. </li>
	 * <li> <code>removeStatus</code>- Removes a status from the defensive
	 * registration. More than one status can be removed. </li>
	 * <li> <code>setRegistrant</code>- Sets the Registrant for the defensive
	 * registration. </li>
	 * <li> <code>setAuthString</code>- Sets the defensive registration name
	 * authorization </li>
	 * <li> <code>setRegistrant</code>- Sets the Registrant for the defensive
	 * registration. </li>
	 * <li> <code>setAdminContact</code>- Sets the adminitrator contact </li>
	 * <li> <code>setTm</code>- Sets the trademark </li>
	 * <li> <code>setTmCountry</code>- Sets the trademark country </li>
	 * <li> <code>setTmDate</code>- Sets the trademark date string. </li>
	 * </ul>
	 * 
	 * At least one update attribute needs to be set.
	 * 
	 * @return <code>EPPResponse</code> containing the defensive registration
	 *         update result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the update command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPResponse sendUpdate() throws EPPCommandException {
		// Add Attributes
		EPPDefRegAddRemove addItems = null;

		if (myAddStatuses != null) {
			addItems = new EPPDefRegAddRemove(myAddStatuses);
		}

		else {
			addItems = new EPPDefRegAddRemove();
		}

		// Remove Attributes
		EPPDefRegAddRemove removeItems = null;

		if (myRemoveStatuses != null) {
			removeItems = new EPPDefRegAddRemove(myRemoveStatuses);
		}

		else {
			removeItems = new EPPDefRegAddRemove();
		}

		EPPAuthInfo authInfo = null;

		// Change Attributes
		if (theAuthString != null) {
			authInfo = new EPPAuthInfo(theAuthString);
		}

		EPPDefRegAddRemove changeItems = new EPPDefRegAddRemove(registrant, tm,
				tmcountry, tmdate, admincontact, authInfo);

		// Create the command
		EPPDefRegUpdateCmd myCommand = new EPPDefRegUpdateCmd(myTransId, roid,
				addItems, removeItems, changeItems);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset defensive registration attributes
		resetDefReg();

		// process the command and response
		return this.mySession.processDocument(myCommand, EPPResponse.class);
	}

	// End EPPDefReg.sendUpdate()

	/**
	 * Sends a Defensive Registration Transfer Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setRoid</code>- Sets the defensive registration ROID. </li>
	 * <li> <code>setTransferOpCode</code>- Sets the defensive registration
	 * transfer operation. </li>
	 * </ul>
	 * 
	 * 
	 * <ul>
	 * <li> <code>setAuthString</code>- Sets the defensive registration
	 * authorization string. <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * </li>
	 * </ul>
	 * 
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li> <code>setPeriodLength</code>- Sets the registration period
	 * (default = 1) </li>
	 * <li> <code>setPeriodUnit</code>- Sets the registration period unit
	 * (default =<code>PERIOD_YEAR</code>) </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPDefRegTransferResp</code> containing the defensive
	 *         registration transfer result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the create command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPDefRegTransferResp sendTransfer() throws EPPCommandException {
		// Create the command
		EPPDefRegTransferCmd myCommand = new EPPDefRegTransferCmd(myTransId,
				myTransferOpCode, roid);

		// Authorization Info String specified?
		if (this.theAuthString != null) {
			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.theAuthString);
			
			// Authorization Info ROID specified?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}
			
			myCommand.setAuthInfo(theAuthInfo);				
		}
		

		// Transfer Request?
		if (myTransferOpCode.equals(TRANSFER_REQUEST)) {
			// Set Transfer Request specific attributes
			if (theAuthString == null) {
				throw new EPPCommandException(
						"Auth Info must be set on a Transfer Request");
			}

			// Period not specified?
			EPPDefRegPeriod myPeriod = null;

			if (thePeriodLength < 0) {
				myPeriod = null;
			}

			else if (thePeriodUnit == null) {
				myPeriod = new EPPDefRegPeriod(thePeriodLength);
			}

			else {
				myPeriod = new EPPDefRegPeriod(thePeriodUnit, thePeriodLength);
			}

			myCommand.setPeriod(myPeriod);
		}

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset defensive registration attributes
		resetDefReg();

		// process the command and response
		return (EPPDefRegTransferResp) this.mySession.processDocument(myCommand, EPPDefRegTransferResp.class);
	}

	// End EPPDefReg.sendTransfer()

	/**
	 * Sends a Defensive Registration Renew Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setRoid</code>- Sets the defensive registration ROID. </li>
	 * <li> <code>setExpirationDate</code>- Sets current expiration date.
	 * </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li> <code>setPeriodLength</code>- Sets the registration period
	 * (default = 1) </li>
	 * <li> <code>setPeriodUnit</code>- Sets the registration period unit
	 * (default =<code>PERIOD_YEAR</code>) </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPDefRegRenewResp</code> containing the defensive
	 *         registration renew result.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the renew command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPDefRegRenewResp sendRenew() throws EPPCommandException {
		EPPDefRegPeriod myPeriod = null;

		if (thePeriodLength < 0) {
			myPeriod = null;
		}
		else if (thePeriodUnit == null) {
			myPeriod = new EPPDefRegPeriod(thePeriodLength);
		}
		else {
			myPeriod = new EPPDefRegPeriod(thePeriodUnit, thePeriodLength);
		}
		
		// Create the command
		EPPDefRegRenewCmd myCommand = new EPPDefRegRenewCmd(myTransId, roid,
				myExpirationDate, myPeriod);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset defensive registration attributes
		resetDefReg();

		// process the command and response
		return (EPPDefRegRenewResp) this.mySession.processDocument(myCommand, EPPDefRegRenewResp.class);
	}

	// End EPPDefReg.sendRenew()

	/**
	 * Sends a Defensive Registration Info Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setRoid</code>- Sets the defensive registration ROID. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPDefRegInfoResp</code> containing the defensive
	 *         registration information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the info command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPDefRegInfoResp sendInfo() throws EPPCommandException {
		// Create the command
		EPPDefRegInfoCmd myCommand = new EPPDefRegInfoCmd(myTransId, roid);

		// Authorization string was provided?
		if (this.theAuthString != null) {

			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.theAuthString);

			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}

			myCommand.setAuthInfo(theAuthInfo);
		}

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset defensive registration attributes
		resetDefReg();

		// process the command and response
		return (EPPDefRegInfoResp) this.mySession.processDocument(myCommand, EPPDefRegInfoResp.class);
	}

	// End EPPDefReg.sendInfo()

	/**
	 * Sends a Defensive Registration Check Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>addDefRegName</code>- Adds a defensive registration name to
	 * check. More than one defensive registration name can be checked in
	 * <code>sendCheck</code> </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPDefRegCheckResp</code> containing the DefReg check
	 *         information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the check command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPDefRegCheckResp sendCheck() throws EPPCommandException {
		Document myDoc = null;

		// Create the command
		EPPDefRegCheckCmd myCommand = new EPPDefRegCheckCmd(myTransId,
				myDefRegList);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset domain attributes
		resetDefReg();

		// process the command and response
		return (EPPDefRegCheckResp) this.mySession.processDocument(myCommand, EPPDefRegCheckResp.class);
	}

	// End EPPDefReg.sendCheck()

	/**
	 * Sends a Defensive Registration Delete Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setRoid</code>- Sets the defensive registration ROID. </li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following: <br>
	 * <br>
	 * 
	 * <ul>
	 * <li> <code>setTransId</code>- Sets the client transaction identifier
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
		// Create the command
		EPPDefRegDeleteCmd myCommand = new EPPDefRegDeleteCmd(myTransId, roid);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset defensive registration attributes
		resetDefReg();

		// process the command and response
		return this.mySession.processDocument(myCommand, EPPResponse.class);
	}

	// End EPPDefReg.sendDelete()

	/**
	 * Adds a status for use in calling <code>sendUpdate</code>.
	 * 
	 * @param aStatus
	 *            Status to add to defensive registration.
	 */
	public void addStatus(EPPDefRegStatus aStatus) {
		if (myAddStatuses == null) {
			myAddStatuses = new Vector();
		}

		myAddStatuses.addElement(aStatus);
	}

	// End EPPDefReg.addStatus(EPPDefRegStatus)

	/**
	 * Removes a status for use in calling <code>sendUpdate</code>.
	 * 
	 * @param aStatus
	 *            Status to add to defensive registration.
	 */
	public void removeStatus(EPPDefRegStatus aStatus) {
		if (myRemoveStatuses == null) {
			myRemoveStatuses = new Vector();
		}

		myRemoveStatuses.addElement(aStatus);
	}

	// End EPPDefReg.removeStatus(EPPDefRegStatus)

	/**
	 * Resets the defReg instance to its initial state.
	 */
	private void resetDefReg() {
		myDefRegList = new Vector();

		myContactList = new Vector();

		myAddStatuses = null;

		myRemoveStatuses = null;

		myTransId = null;

		roid = null;

		registrant = null;

		myExpirationDate = null;

		myTransferOpCode = null;

		thePeriodLength = 1;

		thePeriodUnit = null;

		theAuthString = null;

		this.authRoid = null;

		this.extensions = null;
	}

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
	 * This is a Setter Method for PriodLength private Attribute This Attribute
	 * maintains Validity Period : duration which defReg is registered for.
	 * 
	 * @param newPeriodLength
	 *            DOCUMENT ME!
	 */
	public void setPeriodLength(int newPeriodLength) {
		thePeriodLength = newPeriodLength;
	}

	/**
	 * This is a Setter Method for PeriodUnit private Attribute This Attribute
	 * maintains Validity Unit :time unit where Period Length is mussured by.
	 * ie. year / month
	 * 
	 * @param newPeriodUnit
	 *            DOCUMENT ME!
	 */
	public void setPeriodUnit(String newPeriodUnit) {
		thePeriodUnit = newPeriodUnit;
	}

	/**
	 * This is a Getter Method for PriodLength private Attribute This Attribute
	 * maintains Validity Period : duration which defReg is registered for.
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getPeriodLength() {
		return thePeriodLength;
	}

	/**
	 * This is a Getter Method for PeriodUnit private Attribute This Attribute
	 * maintains Validity Unit :time unit where Period Length is mussured by.
	 * ie. year / month
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getPeriodUnit() {
		return thePeriodUnit;
	}
}
// End class EPPDefReg
