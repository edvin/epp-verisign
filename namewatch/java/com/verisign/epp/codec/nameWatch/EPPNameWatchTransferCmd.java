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
package com.verisign.epp.codec.nameWatch;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP NameWatch &lt;transfer&gt; command. The EPP
 * &lt;transfer&gt; command provides a query operation that allows a client to
 * determine real-time status of pending and completed transfer requests.  In
 * addition to the standard EPP command elements, the &lt;transfer&gt; command
 * MUST contain an <code>op</code> attribute with value <code>query</code>,
 * and a &lt;nameWatch:transfer&gt; element that identifies the nameWatch
 * namespace and the location of the nameWatch schema. The
 * &lt;nameWatch:transfer&gt; element SHALL contain     the following child
 * elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:name&gt; element that contains the fully qualified nameWatch
 * name of the object for which a transfer request is to be created, approved,
 * rejected, or cancelled.  Use <code>getName</code> and <code>setName</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:period&gt; element that contains the initial
 * registration period of the nameWatch object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * <li>
 * An "op" attribute that identifies the transfer operation to be performed.
 * Valid values, definitions, and authorizations for all attribute values are
 * defined in [EPP].  Use <code>getOp</code> and <code>setOp</code> to get and
 * set the element.  One of the <code>EPPCommand.OP_</code> constants need to
 * be specified.
 * </li>
 * <li>
 * A &lt;nameWatch:authInfo&gt; element that contains authorization information
 * associated with the nameWatch object or authorization information
 * associated with the nameWatch object's registrant or associated contacts.
 * This element is REQUIRED only when a transfer is requested, and it SHALL be
 * ignored if used otherwise. Use <code>getAuthInfo</code>     and
 * <code>setAuthInfo</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 * Transfer of a nameWatch object MUST implicitly transfer all host objects
 * that are subordinate to the nameWatch object.  For example, if nameWatch
 * object "example.com" is transferred and host object "ns1.example.com"
 * exists, the host object MUST be transferred as part of the "example.com"
 * transfer process.  Host objects that are subject to transfer when
 * transferring a nameWatch object are listed in the response to an EPP
 * &lt;info&gt; command performed on the nameWatch object. <br>
 * <br>
 * <code>EPPNameWatchTransferResp</code> is the concrete
 * <code>EPPReponse</code> associated     with
 * <code>EPPNameWatchTransferCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchTransferResp
 */
public class EPPNameWatchTransferCmd extends EPPTransferCmd {
	/** XML Element Name of <code>EPPNameWatchTransferCmd</code> root element. */
	final static String ELM_NAME = "nameWatch:transfer";

	/** XML Element Name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** XML Element Name of <code>period</code> root element. */

	//private final static String ELM_PERIOD      = "nameWatch:period";

	/** NameWatch Roid of nameWatch to query. */
	private String roid = null;

	/** Registration Period */
	private EPPNameWatchPeriod period = null;

	/** Authorization information. */
	private EPPAuthInfo authInfo = null;

	/**
	 * Allocates a new <code>EPPNameWatchTransferCmd</code> with default
	 * attribute values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * roid is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>null</code>
	 * </li>
	 * <li>
	 * authInfo  is set to to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The transaction ID, operation, and roid must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPNameWatchTransferCmd() {
		roid		 = null;
		period		 = null;
		authInfo     = null;
	}

	// End EPPNameWatchTransferCmd.EPPNameWatchTransferCmd()

	/**
	 * <code>EPPNameWatchTransferCmd</code> constructor that takes the required
	 * attributes as arguments.  The     period attribute is set to "0" and
	 * will not be included when     <code>encode</code> is invoked.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aRoid NameWatch roid to create.
	 */
	public EPPNameWatchTransferCmd(String aTransId, String aOp, String aRoid) {
		super(aTransId, aOp);

		roid = aRoid;
	}

	// End EPPNameWatchTransferCmd.EPPNameWatchTransferCmd(String, String, String)

	/**
	 * <code>EPPNameWatchTransferCmd</code> constructor that takes the required
	 * attributes as arguments.  The     period attribute is set to "0" and
	 * will not be included when     <code>encode</code> is invoked.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aRoid NameWatch roid to create.
	 * @param aAuthInfo Authorization Information for operating with the
	 * 		  nameWatch.
	 */
	public EPPNameWatchTransferCmd(
								   String aTransId, String aOp, String aRoid,
								   EPPAuthInfo aAuthInfo) {
		super(aTransId, aOp);

		roid = aRoid;
		setAuthInfo(aAuthInfo);
	}

	// End EPPNameWatchTransferCmd.EPPNameWatchTransferCmd(String, String, String, EPPAuthInfo)

	/**
	 * <code>EPPNameWatchTransferCmd</code> constructor that takes the required
	 * attributes plus the     optional attibute <code>aPeriod</code>.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aRoid NameWatch roid to create.
	 * @param aAuthInfo Authorization Information for operating with the
	 * 		  nameWatch.
	 * @param aPeriod Registration period to be added to the nameWatch upon
	 * 		  transfer.
	 */
	public EPPNameWatchTransferCmd(
								   String aTransId, String aOp, String aRoid,
								   EPPAuthInfo aAuthInfo,
								   EPPNameWatchPeriod aPeriod) {
		super(aTransId, aOp);

		roid	   = aRoid;
		period     = aPeriod;
		setAuthInfo(aAuthInfo);
	}

	// End EPPNameWatchTransferCmd.EPPNameWatchTransferCmd(String, String, String, EPPAuthInfo, EPPNameWatchPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchTransferCmd</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchTransferCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPNameWatchTransferCmd</code> instance.
	 * A valid state means that all of the required attributes have been set.
	 * If validateState     returns without an exception, the state is valid.
	 * If the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the name of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (super.getOp() == null) {
			throw new EPPCodecException("op required attribute is not set");
		}

		// NameWatch Roid
		if (roid == null) {
			throw new EPPCodecException("roid required attribute is not set");
		}
	}

	// End EPPNameWatchTransferCmd.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchTransferCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchTransferCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchTransferCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPNameWatchTransferCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// Roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Period
		EPPUtil.encodeComp(aDocument, root, period);

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPNameWatchTransferCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchTransferCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchTransferCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// NameWatch Roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		// Period
		period =
			(EPPNameWatchPeriod) EPPUtil.decodeComp(
													aElement,
													EPPNameWatchMapFactory.NS,
													EPPNameWatchPeriod.ELM_NAME,
													EPPNameWatchPeriod.class);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPNameWatchMapFactory.NS,
											 EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO,
											 EPPAuthInfo.class);

		//Validate States
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPNameWatchTransferCmd.decode: "
										 + e);
		}
	}

	// End EPPNameWatchTransferCmd.doDecode(Element)

	/**
	 * Gets the nameWatch roid to query.
	 *
	 * @return NameWatch Roid
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchTransferCmd.getRoid()

	/**
	 * Sets the nameWatch roid to query.
	 *
	 * @param aRoid NameWatch Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchTransferCmd.setRoid(String)

	/**
	 * Compare an instance of <code>EPPNameWatchTransferCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchTransferCmd)) {
			return false;
		}

		// EPPTransferCmd
		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchTransferCmd theMap = (EPPNameWatchTransferCmd) aObject;

		// Roid
		if (
			!(
					(roid == null) ? (theMap.roid == null)
									   : roid.equals(theMap.roid)
				)) {
			return false;
		}

		// Period
		if (
			!(
					(period == null) ? (theMap.period == null)
										 : period.equals(theMap.period)
				)) {
			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theMap.authInfo == null)
										   : authInfo.equals(theMap.authInfo)
				)) {
			return false;
		}

		return true;
	}

	// End EPPNameWatchTransferCmd.equals(Object)

	/**
	 * Clone <code>EPPNameWatchTransferCmd</code>.
	 *
	 * @return clone of <code>EPPNameWatchTransferCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchTransferCmd clone = (EPPNameWatchTransferCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPNameWatchPeriod) period.clone();
		}

		return clone;
	}

	// End EPPNameWatchTransferCmd.clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPNameWatchTransferCmd.toString()

	/**
	 * Get authorization Information.
	 *
	 * @return EPPAuthInfo if defined; <code>null</code> otherwise.
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPNameWatchTransferCmd.getAuthInfo()

	/**
	 * Gets the registration period of the transfer command in years.
	 *
	 * @return Registration Period in years if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public EPPNameWatchPeriod getPeriod() {
		return period;
	}

	// End EPPNameWatchTransferCmd.getPeriod()

	/**
	 * Set authorization information.
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
		}
	}

	// End EPPNameWatchTransferCmd.setAuthInfo(setAuthInfo)

	/**
	 * Sets the registration period of the transfer command.
	 *
	 * @param aPeriod Registration Period.
	 */
	public void setPeriod(EPPNameWatchPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPNameWatchTransferCmd.setPeriod(EPPNameWatchPeriod)
}
