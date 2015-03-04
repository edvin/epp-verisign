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
package com.verisign.epp.codec.defReg;

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
 * Represents an EPP DefReg &lt;transfer&gt; command. The EPP &lt;transfer&gt;
 * command provides a query operation that allows a client to determine
 * real-time status of pending and completed transfer requests.  In addition
 * to the standard EPPhttp://www.niscom.nic.in/NISTXT/services/Ser1AA.htm
 * command elements, the &lt;transfer&gt; command MUST contain an
 * <code>op</code> attribute with value <code>query</code>, and a
 * &lt;defReg:transfer&gt; element that identifies the defReg namespace and
 * the location of the defReg schema. The &lt;defReg:transfer&gt; element
 * SHALL contain     the following child elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:roid&gt; element that contains  that contains the roid of the
 * object to be transferred
 * </li>
 * </ul>
 * 
 * <br><br>
 * 
 * <ul>
 * <li>
 * Use <code>getRoid</code> and     <code>setRoid</code> to get and set the
 * element.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:period&gt; element that contains the initial
 * registration period of the defReg object. Use <code>getPeriod</code> and
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
 * A &lt;defReg:authInfo&gt; element that contains authorization information
 * associated with the defReg object or authorization information associated
 * with the defReg object's registrant or associated contacts. This element is
 * REQUIRED only when a transfer is requested, and it SHALL be ignored if used
 * otherwise. Use <code>getAuthInfo</code>     and <code>setAuthInfo</code> to
 * get and set the element.     <code>EPPDefRegTransferResp</code> is the
 * concrete <code>EPPReponse</code> associated     with
 * <code>EPPDefRegTransferCmd</code>.
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegTransferResp
 */
public class EPPDefRegTransferCmd extends EPPTransferCmd {
	/** XML Element Name of <code>EPPDefRegTransferCmd</code> root element. */
	final static String ELM_NAME = "defReg:transfer";

	/** DefReg roid of defReg to query. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** XML Element Name of <code>period</code> root element. */
	private final static String ELM_DEFREG_PERIOD = "defReg:period";

	/** DefReg Roid of defReg to update. */
	private String roid = null;

	/** Registration Period */
	private EPPDefRegPeriod period = null;

	/** Authorization information. */
	private EPPAuthInfo authInfo = null;

	/**
	 * Allocates a new <code>EPPDefRegTransferCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
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
	 * <br>     The transaction ID, operation, name, and authInfo must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPDefRegTransferCmd() {
		roid		 = null;
		period		 = null;
		authInfo     = null;
	}

	// End EPPDefRegTransferCmd.EPPDefRegTransferCmd()

	/**
	 * <code>EPPDefRegTransferCmd</code> constructor that takes the required
	 * attributes as arguments.  The     period attribute is set to
	 * <code>UNSPEC_PERIOD</code> and will not be included when
	 * <code>encode</code> is invoked.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aRoid DefReg roid to create.
	 */
	public EPPDefRegTransferCmd(String aTransId, String aOp, String aRoid) {
		super(aTransId, aOp);

		roid		 = aRoid;
		authInfo     = null;
		period		 = null;
	}

	// End EPPDefRegTransferCmd.EPPDefRegTransferCmd(String, EPPAuthInfo, String, String)

	/**
	 * <code>EPPDefRegTransferCmd</code> constructor that takes the required
	 * attributes plus the     optional attibute <code>aPeriod</code>.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aRoid DefReg roid to create.
	 * @param aAuthInfo Authorization Information for operating with the
	 * 		  defReg.
	 * @param aPeriod Registration period to be added to the defReg upon
	 * 		  transfer.
	 */
	public EPPDefRegTransferCmd(
								String aTransId, String aOp, String aRoid,
								EPPAuthInfo aAuthInfo, EPPDefRegPeriod aPeriod) {
		super(aTransId, aOp);

		roid	   = aRoid;
		period     = aPeriod;
		setAuthInfo(aAuthInfo);
	}

	// End EPPDefRegTransferCmd.EPPDefRegTransferCmd(String, EPPAuthInfo, String, String, EPPDefRegPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegTransferCmd</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegTransferCmd.getNamespace()

	/**
	 * Get authorization information
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDefRegTransferCmd.getAuthInfo()

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo java.lang.String
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
		}
	}

	// End EPPDefRegTransferCmd.setAuthInfo(EPPAuthInfo)

	/**
	 * Validate the state of the <code>EPPDefRegTransferCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the name of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 * @throws EPPEncodeException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (super.getOp() == null) {
			throw new EPPCodecException("op required attribute is not set");
		}

		if (roid == null) {
			throw new EPPEncodeException("roid required attribute is not set");
		}

		if (super.getOp().equals(EPPCommand.OP_REQUEST) && (authInfo == null)) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPDefRegTransferCmd.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegTransferCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDefRegTransferCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegTransferCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPDefRegTransferCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_ROID);

		// Period with Attribute of Unit
		if ((period != null) && !period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPDefRegTransferCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegTransferCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegTransferCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		//
		// DefReg Name
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

		// Period
		period =
			(EPPDefRegPeriod) EPPUtil.decodeComp(
												 aElement,
												 EPPDefRegMapFactory.NS,
												 EPPDefRegPeriod.ELM_NAME,
												 EPPDefRegPeriod.class);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDefRegMapFactory.NS,
											 EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPDefRegTransferCmd.doDecode(Element)

	/**
	 * Gets the defReg name to query.
	 *
	 * @return DefReg Name if defined; <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegTransferCmd.getName()

	/**
	 * Sets the defReg name to query.
	 *
	 * @param aRoid DefReg Name
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegTransferCmd.setName(String)

	/**
	 * Gets the registration period of the transfer command in years.
	 *
	 * @return Registration Period in years if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public EPPDefRegPeriod getPeriod() {
		return period;
	}

	// End EPPDefRegTransferCmd.getPeriod()

	/**
	 * Sets the registration period of the transfer command.
	 *
	 * @param aPeriod Registration Period.
	 */
	public void setPeriod(EPPDefRegPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPDefRegTransferCmd.setPeriod(EPPDefRegPeriod)

	/**
	 * Compare an instance of <code>EPPDefRegTransferCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegTransferCmd)) {
			return false;
		}

		// EPPTransferCmd
		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegTransferCmd theMap = (EPPDefRegTransferCmd) aObject;

		//Roid
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

	// End EPPDefRegTransferCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegTransferCmd</code>.
	 *
	 * @return clone of <code>EPPDefRegTransferCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegTransferCmd clone = (EPPDefRegTransferCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPDefRegPeriod) period.clone();
		}

		return clone;
	}

	// End EPPDefRegTransferCmd.clone()

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

	// End EPPDefRegTransferCmd.toString()
}
