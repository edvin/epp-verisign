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
package com.verisign.epp.codec.domain;

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
 * Represents an EPP Domain &lt;transfer&gt; command. The EPP &lt;transfer&gt;
 * command provides a query operation that allows a client to determine
 * real-time status of pending and completed transfer requests.  In addition
 * to the standard EPP command elements, the &lt;transfer&gt; command MUST
 * contain an <code>op</code> attribute with value <code>query</code>, and a
 * &lt;domain:transfer&gt; element that identifies the domain namespace and
 * the location of the domain schema. The &lt;domain:transfer&gt; element
 * SHALL contain     the following child elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * of the object for which a transfer request is to be created, approved,
 * rejected, or cancelled.  Use <code>getName</code> and <code>setName</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:period&gt; element that contains the initial
 * registration period of the domain object. Use <code>getPeriod</code> and
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
 * A &lt;domain:authInfo&gt; element that contains authorization information
 * associated with the domain object or authorization information associated
 * with the domain object's registrant or associated contacts. This element is
 * REQUIRED only when a transfer is requested, and it SHALL be ignored if used
 * otherwise. Use <code>getAuthInfo</code>     and <code>setAuthInfo</code> to
 * get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 * Transfer of a domain object MUST implicitly transfer all host objects that
 * are subordinate to the domain object.  For example, if domain object
 * "example.com" is transferred and host object "ns1.example.com" exists, the
 * host object MUST be transferred as part of the "example.com" transfer
 * process.  Host objects that are subject to transfer when transferring a
 * domain object are listed in the response to an EPP &lt;info&gt; command
 * performed on the domain object. <br>
 * <br>
 * <code>EPPDomainTransferResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPDomainTransferCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainTransferResp
 */
public class EPPDomainTransferCmd extends EPPTransferCmd {
	/** XML Element Name of <code>EPPDomainTransferCmd</code> root element. */
	final static String ELM_NAME = "domain:transfer";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** Domain Name of domain to query. */
	private String name = null;

	/** Registration Period */
	private EPPDomainPeriod period = null;

	/** Authorization information. */
	private EPPAuthInfo authInfo = null;

	/**
	 * Allocates a new <code>EPPDomainTransferCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
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
	public EPPDomainTransferCmd() {
		name		 = null;
		period		 = null;
		authInfo     = null;
	}

	// End EPPDomainTransferCmd.EPPDomainTransferCmd()

	/**
	 * <code>EPPDomainTransferCmd</code> constructor that takes the required
	 * attributes as arguments.  The     period attribute is set to
	 * <code>UNSPEC_PERIOD</code> and will not be included when
	 * <code>encode</code> is invoked.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aName Domain name to create.
	 */
	public EPPDomainTransferCmd(String aTransId, String aOp, String aName) {
		super(aTransId, aOp);

		name = aName;
	}

	// End EPPDomainTransferCmd.EPPDomainTransferCmd(String, String, String)

	/**
	 * <code>EPPDomainTransferCmd</code> constructor that takes the required
	 * attributes plus the     optional attibute <code>aPeriod</code>.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aOp One of the <code>EPPCommand.OP_</code> constants associated
	 * 		  with the transfer command.
	 * @param aName Domain name to create.
	 * @param aAuthInfo Authorization Information for operating with the
	 * 		  domain.
	 * @param aPeriod Registration period to be added to the domain upon
	 * 		  transfer.
	 */
	public EPPDomainTransferCmd(
								String aTransId, String aOp, String aName,
								EPPAuthInfo aAuthInfo, EPPDomainPeriod aPeriod) {
		super(aTransId, aOp);

		name	   = aName;
		period     = aPeriod;
		setAuthInfo(aAuthInfo);
	}

	// End EPPDomainTransferCmd.EPPDomainTransferCmd(String, EPPAuthInfo, String, String, EPPDomainPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainTransferCmd</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainTransferCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPDomainTransferCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the name of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException When validation failed
	 */
	void validateState() throws EPPCodecException {
		if (super.getOp() == null) {
			throw new EPPCodecException("op required attribute is not set");
		}

		// Domain Name
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}
	}

	// End EPPDomainTransferCmd.isValid()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainTransferCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDomainTransferCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainTransferCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPDomainTransferCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);

		// Period with Attribute of Unit
		if ((period != null) && (!period.isPeriodUnspec())) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPDomainTransferCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainTransferCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainTransferCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Domain Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Period
		period =
			(EPPDomainPeriod) EPPUtil.decodeComp(
												 aElement,
												 EPPDomainMapFactory.NS,
												 EPPDomainPeriod.ELM_NAME,
												 EPPDomainPeriod.class);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDomainMapFactory.NS,
											 EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPDomainTransferCmd.doDecode(Element)

	/**
	 * Gets the domain name to query.
	 *
	 * @return Domain Name if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainTransferCmd.getName()

	/**
	 * Sets the domain name to query.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainTransferCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPDomainTransferCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainTransferCmd)) {
			return false;
		}

		// EPPTransferCmd
		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainTransferCmd theMap = (EPPDomainTransferCmd) aObject;

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

	// End EPPDomainTransferCmd.equals(Object)

	/**
	 * Clone <code>EPPDomainTransferCmd</code>.
	 *
	 * @return clone of <code>EPPDomainTransferCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainTransferCmd clone = (EPPDomainTransferCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPDomainPeriod) period.clone();
		}

		return clone;
	}

	// End EPPDomainTransferCmd.clone()

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

	// End EPPDomainTransferCmd.toString()

	/**
	 * Get authorization Information.
	 *
	 * @return com.verisign.epp.codec.domain.EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDomainTransferCmd.getAuthInfo()

	/**
	 * Gets the registration period of the transfer command in years.
	 *
	 * @return Registration Period in years if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public EPPDomainPeriod getPeriod() {
		return period;
	}

	// End EPPDomainTransferCmd.getPeriod()

	/**
	 * Set authorization information.
	 *
	 * @param newAuthInfo com.verisign.epp.codec.domain.EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
		}
	}

	// End EPPDomainTransferCmd.setAuthInfo(setAuthInfo)

	/**
	 * Sets the registration period of the transfer command.
	 *
	 * @param aPeriod Registration Period.
	 */
	public void setPeriod(EPPDomainPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPDomainTransferCmd.setPeriod(EPPDomainPeriod)
}
