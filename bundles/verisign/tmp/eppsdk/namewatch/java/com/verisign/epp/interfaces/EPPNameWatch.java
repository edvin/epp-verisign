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

import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.nameWatch.EPPNameWatchAddRemove;
import com.verisign.epp.codec.nameWatch.EPPNameWatchCreateCmd;
import com.verisign.epp.codec.nameWatch.EPPNameWatchCreateResp;
import com.verisign.epp.codec.nameWatch.EPPNameWatchDeleteCmd;
import com.verisign.epp.codec.nameWatch.EPPNameWatchInfoCmd;
import com.verisign.epp.codec.nameWatch.EPPNameWatchInfoResp;
import com.verisign.epp.codec.nameWatch.EPPNameWatchPeriod;
import com.verisign.epp.codec.nameWatch.EPPNameWatchRenewCmd;
import com.verisign.epp.codec.nameWatch.EPPNameWatchRenewResp;
import com.verisign.epp.codec.nameWatch.EPPNameWatchRptTo;
import com.verisign.epp.codec.nameWatch.EPPNameWatchStatus;
import com.verisign.epp.codec.nameWatch.EPPNameWatchTransferCmd;
import com.verisign.epp.codec.nameWatch.EPPNameWatchTransferResp;
import com.verisign.epp.codec.nameWatch.EPPNameWatchUpdateCmd;


/**
 * <code>EPPNameWatch</code> is the primary client interface class used for
 * nameWatch management. An instance of <code>EPPNameWatch</code> is created
 * with an initialized <code>EPPSession</code>, and can be used for more than
 * one request within a single thread. A set of setter methods are provided to
 * set the attributes before a call to one of the send action methods. The
 * responses returned from the send action methods are either instances of
 * <code>EPPResponse</code> or instances of response classes in the
 * <code>com.verisign.epp.codec.nameWatch</code> package. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchCreateResp
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchInfoResp
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchTransferResp
 */
public class EPPNameWatch {
	/** Status constants */
	public final static String STATUS_OK = EPPNameWatchStatus.ELM_STATUS_OK;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_HOLD =
		EPPNameWatchStatus.ELM_STATUS_SERVER_HOLD;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_RENEW_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_SERVER_RENEW_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_TRANSFER_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_SERVER_TRANSFER_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_UPDATE_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_SERVER_UPDATE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_SERVER_DELETE_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_SERVER_DELETE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_HOLD =
		EPPNameWatchStatus.ELM_STATUS_CLIENT_HOLD;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_RENEW_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_CLIENT_RENEW_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_TRANSFER_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_CLIENT_TRANSFER_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_UPDATE_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_CLIENT_DELETE_PROHIBITED =
		EPPNameWatchStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED;

	/** DOCUMENT ME! */
	public final static String STATUS_PENDING_DELETE =
		EPPNameWatchStatus.ELM_STATUS_PENDING_DELETE;

	/** DOCUMENT ME! */
	public final static String STATUS_PENDING_TRANSFER =
		EPPNameWatchStatus.ELM_STATUS_PENDING_TRANSFER;

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

	/** Frequency type constants */
	public static final String FREQ_DAILY = EPPNameWatchRptTo.RPTTO_FREQ_DAILY;

	/** DOCUMENT ME! */
	public static final String FREQ_WEEKLY =
		EPPNameWatchRptTo.RPTTO_FREQ_WEEKLY;

	/** DOCUMENT ME! */
	public static final String FREQ_MONTHLY =
		EPPNameWatchRptTo.RPTTO_FREQ_MONTHLY;

	/** Period in Unit Month */
	public final static String PERIOD_MONTH =
		EPPNameWatchPeriod.PERIOD_UNIT_MONTH;

	/** Period in Unit Year */
	public final static String PERIOD_YEAR =
		EPPNameWatchPeriod.PERIOD_UNIT_YEAR;

	/** The NameWatch Name */
	private String myName = null;

	/** The Email address to report to. */
	private String myRptTo = null;

	/**
	 * The frequency of the report. Should be one of the
	 * <code>FREQ_DAILY</code> constant values.
	 */
	private String myFreq = null;

	/** The NameWatch Roid */
	private String myRoid = null;

	/** Registrant name */
	private String myRegistrant = null;

	/** An instance of a session. */
	private EPPSession mySession = null;

	/** Transaction Id provided by cliet */
	private String myTransId = null;

	/** The Expiration Date. */
	private Date myExpirationDate;

	/** Transfer Operation Code */
	private String myTransferOpCode;

	/**
	 * This is Attribute Contains Validity Period : duration which nameWatch is
	 * registered for.
	 */
	private int myPeriodLength = 1;

	/**
	 * Unit of <code>myPeriodLength</code>, which should be either
	 * <code>PERIOD_MONTH</code> or <code>PERIOD_YEAR</code>.
	 */
	private String myPeriodUnit = null;

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
	 * Extension objects associated with the command.  This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector extensions = null;

	/** Statuses to add on call to <code>sendUpdate</code>. */
	private Vector myAddStatuses = null;

	/** Statuses to remove on call to <code>sendUpdate</code>. */
	private Vector myRemoveStatuses = null;

	/**
	 * Constructs an <code>EPPNameWatch</code> given an initialized EPP
	 * session.
	 *
	 * @param newSession Server session to use.
	 */
	public EPPNameWatch(EPPSession newSession) {
		mySession = newSession;

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

	// End EPPNameWatch.addExtension(EPPCodecComponent)

	/**
	 * Sets a command extension object.
	 *
	 * @param aExtension command extension object associated with the command
	 *
	 * @deprecated Replaced by <code>addExtension(EPPCodecComponent)</code>.  This
	 * 			   method will add the extension as is done in <code>addExtension(EPPCodecComponet)</code>.
	 */
	public void setExtension(EPPCodecComponent aExtension) {
		this.addExtension(aExtension);
	}

	// End EPPNameWatch.setExtension(EPPCodecComponent)

	/**
	 * Sets the command extension objects.
	 *
	 * @param aExtensions command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		this.extensions = aExtensions;
	}

	// End EPPNameWatch.setExtensions(Vector)

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

	// End EPPNameWatch.getExtensions()

	/**
	 * Sets the NameWatch roid
	 *
	 * @param newRoid NameWatch roid
	 */
	public void setRoid(String newRoid) {
		myRoid = newRoid;

		//myNameWatchList.addElement(newRoid);
	}

	/**
	 * Gets the NameWatch Roid
	 *
	 * @return NameWatch Roid
	 */
	public String getRoid() {
		return myRoid;
	}

	/**
	 * Sets the NameWatch expiration date.
	 *
	 * @param newExpirationDate NameWatch expiration date
	 */
	public void setExpirationDate(Date newExpirationDate) {
		myExpirationDate = newExpirationDate;
	}

	/**
	 * Gets the NameWatch expiration date.
	 *
	 * @return NameWatch expiration date
	 */
	public Date getExpirationDate() {
		return myExpirationDate;
	}

	/**
	 * Gets the NameWatch Name
	 *
	 * @return NameWatch Name
	 */
	public String getName() {
		return myName;
	}

	/**
	 * Sets the NameWatch Name
	 *
	 * @param newName NameWatch Name
	 */
	public void setName(String newName) {
		myName = newName;
	}

	/**
	 * Sets the transfer operation for a call to <code>encodeTransfer</code>.
	 * The transfer code must be set to one of the
	 * <code>EPPNameWatch.TRANSFER_</code> constants.
	 *
	 * @param newTransferOpCode One of the <code>EPPNameWatch.TRANSFER_</code>
	 * 		  constants
	 */
	public void setTransferOpCode(String newTransferOpCode) {
		myTransferOpCode = newTransferOpCode;
	}

	/**
	 * Sets the client transaction identifier.
	 *
	 * @param newTransId Client transaction identifier
	 */
	public void setTransId(String newTransId) {
		myTransId = newTransId;
	}

	/**
	 * Sets the authorization string associated with an <code>sendCreate</code>
	 * and <code>sendTransfer</code>.
	 *
	 * @param newAuthString Authorization string
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
	 * contact object if and only if the value of authInfo, set by <code>setAuthString(String)</code>, 
	 * is associated with the registrant or contact object.  This can be used 
	 * with <code>sendTransfer</code> along with setting
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
	 * with <code>sendTransfer</code> along with setting
	 * the authInfo with the <code>setAuthString(String)</code> method.
	 *
	 * @param aAuthRoid Roid of registrant or contact object
	 */
	public void setAuthRoid(String aAuthRoid) {
		this.authRoid = aAuthRoid;
	}
	
	

	/**
	 * Gets the registrant.
	 *
	 * @return registrant if defined; <code>null</code> otherwise.
	 */
	public String getRegistrant() {
		return myRegistrant;
	}

	/**
	 * Sets the registrant.
	 *
	 * @param newRegistrant NameWatch registrant
	 */
	public void setRegistrant(String newRegistrant) {
		myRegistrant = newRegistrant;
	}

	/**
	 * Gets NameWatch e-mail address to report to.
	 *
	 * @return E-mail address if defined; <code>null</code> otherwise.
	 */
	public String getRptTo() {
		return myRptTo;
	}

	/**
	 * Sets NameWatch e-mail address to report to.
	 *
	 * @param aRptTo address<code>String</code>.
	 */
	public void setRptTo(String aRptTo) {
		myRptTo = aRptTo;
	}

	/**
	 * Gets NameWatch report frequency, which should be one of the
	 * <code>FREQ_</code> constant values.
	 *
	 * @return Report frequency if defined; <code>null</code> otherwise.
	 */
	public String getFreq() {
		return myFreq;
	}

	/**
	 * Sets the NameWatch report frequency, which should be one of the
	 * <code>FREQ_</code> constant values.
	 *
	 * @param aFreq One of the <code>FREQ_</code> constant values.
	 */
	public void setFreq(String aFreq) {
		myFreq = aFreq;
	}

	/**
	 * Sends a NameWatch Create Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setName</code>- Sets the namewatch name to create.
	 * </li>
	 * <li>
	 * <code>setAuthString</code>- Sets the namewatch authorization
	 * </li>
	 * <li>
	 * <code>setRegistrant</code>- Sets the Registrant for the namewatch.
	 * string.
	 * </li>
	 * <li>
	 * <code>setRptTo</code>- Sets e-mail address to report to.
	 * </li>
	 * <li>
	 * <code>setFreq</code>- Sets the frequency of the report
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li>
	 * <code>setPeriodLength</code>- Sets the registration period (default = 1)
	 * </li>
	 * <li>
	 * <code>setPeriodUnit</code>- Sets the registration period unit (default
	 * =<code>PERIOD_YEAR</code>)
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPNameWatchCreateResp</code> containing the namewatch
	 * 		   create result. Use <code>EPPNameWatchCreateResp.getRoid</code>
	 * 		   to get the ROID required for the rest of the
	 * 		   <code>EPPNameWatch</code> operations.
	 *
	 * @exception EPPCommandException Error executing the create command. Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPNameWatchCreateResp sendCreate() throws EPPCommandException {
		// Create the command
		EPPNameWatchCreateCmd myCommand =
			new EPPNameWatchCreateCmd(
									  myTransId, myName, myRegistrant,
									  new EPPNameWatchRptTo(myFreq, myRptTo),
									  new EPPAuthInfo(myAuthString));

		EPPNameWatchPeriod    myPeriod = null;

		// Period not specified?
		if (myPeriodLength >= 0) {
			if (myPeriodUnit == null) {
				myCommand.setPeriod(new EPPNameWatchPeriod(myPeriodLength));
			}

			else {
				myCommand.setPeriod(new EPPNameWatchPeriod(
														   myPeriodUnit,
														   myPeriodLength));
			}
		}

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset namewatch attributes
		resetNameWatch();

		// process the command and response
		return (EPPNameWatchCreateResp) this.mySession.processDocument(myCommand, EPPNameWatchCreateResp.class);
	}

	// End EPPNameWatch.sendCreate()

	/**
	 * Sends a NameWatch Update Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setRoid</code>- Sets the namewatch ROID. string.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li>
	 * <code>addStatus</code>- Adds a status to the namewatch. More than one
	 * status can be added.
	 * </li>
	 * <li>
	 * <code>removeStatus</code>- Removes a status from the namewatch. More
	 * than one status can be removed.
	 * </li>
	 * <li>
	 * <code>setRegistrant</code>- Sets the Registrant for the namewatch.
	 * </li>
	 * <li>
	 * <code>setAuthString</code>- Sets the namewatch authorization
	 * </li>
	 * <li>
	 * <code>setRptTo</code>- Sets e-mail address to report to.
	 * </li>
	 * <li>
	 * <code>setFreq</code>- Sets the frequency of the report
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPNameWatchCreateResp</code> containing the namewatch
	 * 		   create result. Use <code>EPPNameWatchCreateResp.getRoid</code>
	 * 		   to get the ROID required for the rest of the
	 * 		   <code>EPPNameWatch</code> operations.
	 *
	 * @exception EPPCommandException Error executing the create command. Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendUpdate() throws EPPCommandException {
		// Add Attributes
		EPPNameWatchAddRemove addItems = null;

		if (myAddStatuses != null) {
			addItems = new EPPNameWatchAddRemove(myAddStatuses);
		}

		else {
			addItems = new EPPNameWatchAddRemove();
		}

		// Remove Attributes
		EPPNameWatchAddRemove removeItems = null;

		if (myRemoveStatuses != null) {
			removeItems = new EPPNameWatchAddRemove(myRemoveStatuses);
		}

		else {
			removeItems = new EPPNameWatchAddRemove();
		}

		EPPAuthInfo authInfo = null;

		// Change Attributes
		if (myAuthString != null) {
			authInfo = new EPPAuthInfo(myAuthString);
		}

		EPPNameWatchRptTo rptTo = null;

		if ((myRptTo != null) && (myFreq != null)) {
			rptTo = new EPPNameWatchRptTo(myFreq, myRptTo);
		}

		EPPNameWatchAddRemove changeItems =
			new EPPNameWatchAddRemove(myRegistrant, rptTo, authInfo);

		// Create the command
		EPPNameWatchUpdateCmd myCommand =
			new EPPNameWatchUpdateCmd(
									  myTransId, myRoid, addItems, removeItems,
									  changeItems);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset namewatch attributes
		resetNameWatch();

		// process the command and response
		return this.mySession.processDocument(myCommand, EPPResponse.class);
	}

	// End EPPNameWatch.sendUpdate()

	/**
	 * Sends a NameWatch Transfer Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setRoid</code>- Sets the namewatch ROID.
	 * </li>
	 * <li>
	 * <code>setTransferOpCode</code>- Sets the namewatch transfer operation.
	 * </li>
	 * <li>
	 * <code>setAuthString</code>- Sets the namewatch authorization string. <br>
	 * <br>
	 * The optional attributes have been set with the following: <br><br>
	 * </li>
	 * <li>
	 * <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li>
	 * <code>setPeriodLength</code>- Sets the registration period (default = 1)
	 * </li>
	 * <li>
	 * <code>setPeriodUnit</code>- Sets the registration period unit (default
	 * =<code>PERIOD_YEAR</code>)
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPNameWatchTransferResp</code> containing the namewatch
	 * 		   transfer result.
	 *
	 * @exception EPPCommandException Error executing the create command. Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPNameWatchTransferResp sendTransfer() throws EPPCommandException {
		// Create the command
		EPPNameWatchTransferCmd myCommand =
			new EPPNameWatchTransferCmd(myTransId, myTransferOpCode, myRoid);

		// Authorization Info String specified?
		if (this.myAuthString != null) {
			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.myAuthString);
			
			// Authorization Info ROID specified?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}
			
			myCommand.setAuthInfo(theAuthInfo);				
		}

		// Transfer Request?
		if (myTransferOpCode.equals(TRANSFER_REQUEST)) {
			// Set Transfer Request specific attributes
			if (myAuthString == null) {
				throw new EPPCommandException("Auth Info must be set on a Transfer Request");
			}

			// Period not specified?
			EPPNameWatchPeriod thePeriod = null;

			if (myPeriodLength < 0) {
				thePeriod = null;
			}

			else if (myPeriodUnit == null) {
				thePeriod = new EPPNameWatchPeriod(myPeriodLength);
			}

			else {
				thePeriod =
					new EPPNameWatchPeriod(myPeriodUnit, myPeriodLength);
			}

			myCommand.setPeriod(thePeriod);
		}

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset namewatch attributes
		resetNameWatch();

		// process the command and response
		return (EPPNameWatchTransferResp) this.mySession.processDocument(myCommand, EPPNameWatchTransferResp.class);
	}

	// End EPPNameWatch.sendTransfer()

	/**
	 * Sends a NameWatch Renew Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setRoid</code>- Sets the namewatch ROID.
	 * </li>
	 * <li>
	 * <code>setExpirationDate</code>- Sets current expiration date.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * <li>
	 * <code>setPeriodLength</code>- Sets the registration period (default = 1)
	 * </li>
	 * <li>
	 * <code>setPeriodUnit</code>- Sets the registration period unit (default
	 * =<code>PERIOD_YEAR</code>)
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPNameWatchRenewResp</code> containing the namewatch
	 * 		   renew result.
	 *
	 * @exception EPPCommandException Error executing the renew command. Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPNameWatchRenewResp sendRenew() throws EPPCommandException {
		EPPNameWatchPeriod myPeriod = null;

		// Period not specified?
		if (myPeriodUnit != null) {
			myPeriod = new EPPNameWatchPeriod(myPeriodUnit, myPeriodLength);
		}
		else {
			myPeriod = new EPPNameWatchPeriod(myPeriodLength);
		}

		// Create the command
		EPPNameWatchRenewCmd myCommand =
			new EPPNameWatchRenewCmd(
									 myTransId, myRoid, myExpirationDate,
									 myPeriod);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset namewatch attributes
		resetNameWatch();

		// process the command and response
		return (EPPNameWatchRenewResp) this.mySession.processDocument(myCommand, EPPNameWatchRenewResp.class);
	}

	// End EPPNameWatch.sendRenew()

	/**
	 * Sends a NameWatch Info Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setRoid</code>- Sets the namewatch ROID.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPNameWatchInfoResp</code> containing the namewatch
	 * 		   information.
	 *
	 * @exception EPPCommandException Error executing the info command. Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPNameWatchInfoResp sendInfo() throws EPPCommandException {
		// Create the command
		EPPNameWatchInfoCmd myCommand =
			new EPPNameWatchInfoCmd(myTransId, myRoid);
		
		// Authorization string was provided?
		if (this.myAuthString != null) {
			
			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.myAuthString);
			
			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid); 				
			}
			
			myCommand.setAuthInfo(theAuthInfo);
		}

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset namewatch attributes
		resetNameWatch();

		// process the command and response
		return (EPPNameWatchInfoResp) this.mySession.processDocument(myCommand, EPPNameWatchInfoResp.class);
	}

	// End EPPNameWatch.sendInfo()

	/**
	 * Sends a NameWatch Delete Command to the server. <br>
	 * <br>
	 * The required attributes have been set with the following methods:
	 * <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setRoid</code>- Sets the namewatch ROID.
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code>- Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return <code>EPPResponse</code> containing the delete result
	 * 		   information.
	 *
	 * @exception EPPCommandException Error executing the delete command. Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendDelete() throws EPPCommandException {
		// Create the command
		EPPNameWatchDeleteCmd myCommand =
			new EPPNameWatchDeleteCmd(myTransId, myRoid);

		// Set command extension
		myCommand.setExtensions(this.extensions);

		// Reset namewatch attributes
		resetNameWatch();

		// process the command and response
		return this.mySession.processDocument(myCommand, EPPResponse.class);
	}

	// End EPPNameWatch.sendDelete()

	/**
	 * Resets the nameWatch instance to its initial state.
	 */
	private void resetNameWatch() {
		myRoid     = null;

		myTransId     = null;

		myExpirationDate     = null;

		myTransferOpCode     = null;

		myPeriodLength     = 1;

		myAuthString     = null;
		
		this.authRoid		 = null;

		this.extensions     = null;

		myAddStatuses     = null;

		myRemoveStatuses     = null;

		myRptTo     = null;

		myFreq     = null;

		myRegistrant = null;
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
	 * Gets the registration period. The unit can be retrieved by calling
	 * <code>getPeriodUnit</code>.
	 *
	 * @return registration period if defined; <code>0</code> otherwise
	 */
	public int getPeriodLength() {
		return myPeriodLength;
	}

	/**
	 * Sets the registration period. The default unit is years, but this can be
	 * overriden by calling <code>setPeriodUnit</code>.
	 *
	 * @param newPeriodLength DOCUMENT ME!
	 */
	public void setPeriodLength(int newPeriodLength) {
		myPeriodLength = newPeriodLength;
	}

	/**
	 * Gets the registration period unit. If defined, this should be either
	 * <code>PERIOD_MONTH</code> or <code>PERIOD_YEAR</code>.
	 *
	 * @return registration period unit if defined; <code>0</code> otherwise
	 */
	public String getPeriodUnit() {
		return myPeriodUnit;
	}

	/**
	 * Sets the registration period unit.
	 *
	 * @param newPeriodUnit DOCUMENT ME!
	 */
	public void setPeriodUnit(String newPeriodUnit) {
		myPeriodUnit = newPeriodUnit;
	}

	/**
	 * Adds a status for use in calling <code>sendUpdate</code>.
	 *
	 * @param aStatus Status to add to namewatch.
	 */
	public void addStatus(EPPNameWatchStatus aStatus) {
		if (myAddStatuses == null) {
			myAddStatuses = new Vector();
		}

		myAddStatuses.addElement(aStatus);
	}

	// End EPPNameWatch.addStatus(EPPNameWatchStatus)

	/**
	 * Removes a status for use in calling <code>sendUpdate</code>.
	 *
	 * @param aStatus Status to add to namewatch.
	 */
	public void removeStatus(EPPNameWatchStatus aStatus) {
		if (myRemoveStatuses == null) {
			myRemoveStatuses = new Vector();
		}

		myRemoveStatuses.addElement(aStatus);
	}

	// End EPPNameWatch.removeStatus(EPPNameWatchStatus)
}


// End class EPPNameWatch
