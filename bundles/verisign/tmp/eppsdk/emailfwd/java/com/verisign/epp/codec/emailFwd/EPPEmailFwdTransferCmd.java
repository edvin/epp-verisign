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
package com.verisign.epp.codec.emailFwd;

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
 * Represents an EPP EmailFwd &lt;transfer&gt; command. The EPP
 * &lt;transfer&gt; command provides a query operation that allows a client to
 * determine real-time status of pending and completed transfer requests.  In
 * addition to the standard
 * EPPhttp://www.niscom.nic.in/NISTXT/services/Ser1AA.htm command elements,
 * the &lt;transfer&gt; command MUST contain an <code>op</code> attribute with
 * value <code>query</code>, and a &lt;emailFwd:transfer&gt; element that
 * identifies the emailFwd namespace and the location of the emailFwd schema.
 * The &lt;emailFwd:transfer&gt; element SHALL contain     the following child
 * elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name of the object for which a transfer request is to be created, approved,
 * rejected, or cancelled.  Use <code>getName</code> and <code>setName</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:period&gt; element that contains the initial
 * registration period of the emailFwd object. Use <code>getPeriod</code> and
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
 * A &lt;emailFwd:authInfo&gt; element that contains authorization information
 * associated with the emailFwd object or authorization information associated
 * with the emailFwd object's registrant or associated contacts. This element
 * is REQUIRED only when a transfer is requested, and it SHALL be ignored if
 * used otherwise. Use <code>getAuthInfo</code>     and
 * <code>setAuthInfo</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 * <code>EPPEmailFwdTransferResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPEmailFwdTransferCmd</code>.
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferResp
 */
public class EPPEmailFwdTransferCmd extends EPPTransferCmd {
	/** XML Element Name of <code>EPPEmailFwdTransferCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:transfer";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** EmailFwd Name of emailFwd to query. */
	private String name = null;

	/** Registration Period */
	private EPPEmailFwdPeriod period = null;

	/** Authorization information. */
	private EPPAuthInfo authInfo = null;

	/**
	 * Allocates a new <code>EPPEmailFwdTransferCmd</code> with default
	 * attribute values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>null</code>
	 * </li>
	 * <li>
	 * authInfo  is set to to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The transaction ID, operation, name, and authInfo must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPEmailFwdTransferCmd() {
		name		 = null;
		period		 = null;
		authInfo     = null;
	}

	// End EPPEmailFwdTransferCmd.EPPEmailFwdTransferCmd()

	/**
	 * <code>EPPEmailFwdTransferCmd</code> constructor that takes the required
	 * attributes as arguments.  The     period attribute is set to
	 * <code>UNSPEC_PERIOD</code> and will not be included when
	 * <code>encode</code> is invoked.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aName EmailFwd name to create.
	 */
	public EPPEmailFwdTransferCmd(String aTransId, String aOp, String aName) {
		super(aTransId, aOp);

		name = aName;
	}

	// End EPPEmailFwdTransferCmd.EPPEmailFwdTransferCmd(String, String, String)

	/**
	 * <code>EPPEmailFwdTransferCmd</code> constructor that takes the required
	 * attributes plus the     optional attibute <code>aPeriod</code>.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aName EmailFwd name to create.
	 * @param aAuthInfo Authorization Information for operating with the
	 * 		  emailFwd.
	 * @param aPeriod Registration period to be added to the emailFwd upon
	 * 		  transfer.
	 */
	public EPPEmailFwdTransferCmd(
								  String aTransId, String aOp, String aName,
								  EPPAuthInfo aAuthInfo,
								  EPPEmailFwdPeriod aPeriod) {
		super(aTransId, aOp);

		name	   = aName;
		period     = aPeriod;
		setAuthInfo(aAuthInfo);
	}

	// End EPPEmailFwdTransferCmd.EPPEmailFwdTransferCmd(String, EPPAuthInfo, String, String, EPPEmailFwdPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdTransferCmd</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdTransferCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPEmailFwdTransferCmd</code> instance.
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

		// EmailFwd Name
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		// AuthInfo
		if (super.getOp().equals(EPPCommand.OP_REQUEST) && (authInfo == null)) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPEmailFwdTransferCmd.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdTransferCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPEmailFwdTransferCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdTransferCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPEmailFwdTransferCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);

		// Period with Attribute of Unit
		if ((period != null) && (!period.isPeriodUnspec())) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPEmailFwdTransferCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdTransferCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdTransferCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// EmailFwd Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		// Period
		period =
			(EPPEmailFwdPeriod) EPPUtil.decodeComp(
												   aElement,
												   EPPEmailFwdMapFactory.NS,
												   EPPEmailFwdPeriod.ELM_NAME,
												   EPPEmailFwdPeriod.class);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPEmailFwdMapFactory.NS,
											 EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPEmailFwdTransferCmd.doDecode(Element)

	/**
	 * Gets the emailFwd name to query.
	 *
	 * @return EmailFwd Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdTransferCmd.getName()

	/**
	 * Sets the emailFwd name to query.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdTransferCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPEmailFwdTransferCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdTransferCmd)) {
			return false;
		}

		// EPPTransferCmd
		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdTransferCmd theMap = (EPPEmailFwdTransferCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theMap.name == null)
									   : name.equals(theMap.name)
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

	// End EPPEmailFwdTransferCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdTransferCmd</code>.
	 *
	 * @return clone of <code>EPPEmailFwdTransferCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdTransferCmd clone = (EPPEmailFwdTransferCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPEmailFwdPeriod) period.clone();
		}

		return clone;
	}

	// End EPPEmailFwdTransferCmd.clone()

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

	// End EPPEmailFwdTransferCmd.toString()

	/**
	 * Get authorization Information.
	 *
	 * @return com.verisign.epp.codec.emailFwd.EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPEmailFwdTransferCmd.getAuthInfo()

	/**
	 * Gets the registration period of the transfer command in years.
	 *
	 * @return Registration Period in years if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public EPPEmailFwdPeriod getPeriod() {
		return period;
	}

	// End EPPEmailFwdTransferCmd.getPeriod()

	/**
	 * Set authorization information.
	 *
	 * @param newAuthInfo com.verisign.epp.codec.emailFwd.EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
		}
	}

	// End EPPEmailFwdTransferCmd.setAuthInfo(setAuthInfo)

	/**
	 * Sets the registration period of the transfer command.
	 *
	 * @param aPeriod Registration Period.
	 */
	public void setPeriod(EPPEmailFwdPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPEmailFwdTransferCmd.setPeriod(EPPEmailFwdPeriod)
}
