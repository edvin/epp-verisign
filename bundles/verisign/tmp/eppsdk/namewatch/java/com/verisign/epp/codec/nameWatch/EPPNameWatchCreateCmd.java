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

import org.w3c.dom.*;
import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.Date;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP NameWatch &lt;create&gt; command, which provides a
 * transform     operation that allows a client to create a nameWatch object.
 * In addition to the standard EPP command elements, the &lt;create&gt;
 * command MUST contain a &lt;nameWatch:create&gt; element that identifies the
 * nameWatch namespace and the location of the nameWatch schema.     The
 * &lt;nameWatch:create&gt; element MUST contain the following child
 * elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:name&gt; element that contains the fully qualified nameWatch
 * name of the object to be created.  Use <code>getName</code> and
 * <code>setName</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:period&gt; element that contains the initial
 * registration period of the nameWatch object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * <li>
 * Zero or more &lt;nameWatch:ns&gt; elements that contain the fully qualified
 * host name of a known host object to provide resolution services for the
 * nameWatch.  A host object MUST be known to the server before a nameWatch
 * can be delegated to the host.  A server MUST provide host object services
 * to provide nameWatch name services.  The EPP mapping for host objects is
 * described in [EPP-H]. Use <code>getServers</code> and
 * <code>setServers</code> to get     and set the elements.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:registrant&gt; element that contains the
 * identifier for the human or organizational social information (contact)
 * object to be associated with the nameWatch object as the object registrant.
 * This object identifier MUST be known to the server before the contact
 * object can be associated with the nameWatch object. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get     and
 * set the elements.
 * </li>
 * <li>
 * Zero or more &lt;nameWatch:contact&gt; elements that contain the registrant,
 * administrative, technical, and billing contact identifiers to be associated
 * with the nameWatch.  A contact identifier MUST be known to the server
 * before the contact can be associated with the nameWatch.  Only one contact
 * identifier of each type MAY be specified.  A server MAY provide contact
 * object services when providing nameWatch name object services.  The EPP
 * mapping for contact objects is described in [EPP-C].     Use
 * <code>getContacts</code> and <code>setContacts</code> to get     and set
 * the elements.  Contacts should only be specified if the     Contact Mapping
 * is supported.
 * </li>
 * <li>
 * A &lt;nameWatch:authInfo&gt; element that contains authorization information
 * to be associated with the nameWatch object.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the transaction identifier associated with
 * successful creation of a nameWatch object becomes the authorization
 * identifier required to transfer sponsorship of the nameWatch object.  A
 * client MUST retain all transaction identifiers associated with nameWatch
 * object creation and protect them from disclosure.  A client MUST also
 * provide a copy of the transaction identifier information to the nameWatch
 * registrant, who will need this information to request a nameWatch transfer
 * through a different client.     <br>
 * <br>
 * <code>EPPNameWatchCreateResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPNameWatchCreateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchCreateResp
 */
public class EPPNameWatchCreateCmd extends EPPCreateCmd {
	/** XML Element Name of <code>EPPNameWatchCreateCmd</code> root element. */
	final static String ELM_NAME = "nameWatch:create";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_NAMEWATCH_NAME = "nameWatch:name";

	/** XML Element Name of <code>registrant</code> attribute. */
	final static String ELM_REGISTRANT = "nameWatch:registrant";

	/** XML Element Name of <code>period</code> attribute. */
	private final static String ELM_PERIOD = "nameWatch:period";

	/** Maximum number of periods. */
	private final static int MAX_PERIOD = 99;

	/** Minimum number of periods. */
	private final static int MIN_PERIOD = 1;

	/** NameWatch Name of nameWatch to create. */
	private String name = null;

	/** NameWatch Report To. */
	private EPPNameWatchRptTo rptTo = null;

	/** NameWatch period */
	private EPPNameWatchPeriod period = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/** registrant. */
	private String registrant = null;

	/**
	 * Allocates a new <code>EPPNameWatchCreateCmd</code> with default
	 * attribute values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * registrant is set to to <code>null</code>
	 * </li>
	 * <li>
	 * rptTo is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to 0
	 * </li>
	 * <li>
	 * authInfo is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name, registrant, rptTo, and authInfo must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPNameWatchCreateCmd() {
		name		   = null;
		registrant     = null;
		rptTo		   = null;
		authInfo	   = null;
		period		   = null;
	}

	// End EPPNameWatchCreateCmd.EPPNameWatchCreateCmd()

	/**
	 * Allocates a new <code>EPPNameWatchCreateCmd</code> with default
	 * attribute values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * registrant is set to to <code>null</code>
	 * </li>
	 * <li>
	 * rptTo is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>null</code>
	 * </li>
	 * <li>
	 * authInfo is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The registrant, rptTo, and authInfo must be set before invoking
	 * <code>encode</code>.
	 *
	 * @param aTransId DOCUMENT ME!
	 * @param aName DOCUMENT ME!
	 */
	public EPPNameWatchCreateCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPNameWatchCreateCmd.EPPNameWatchCreateCmd(String, String)

	/**
	 * Allocates a new <code>EPPNameWatchCreateCmd</code> with a nameWatch
	 * name.     The other attributes are initialized as follows:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * period is default to 0
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName NameWatch name
	 * @param aRegistrant NameWatch registrant
	 * @param aRptTo EPPNameWatchRptTo rptTo
	 * @param aAuthInfo EPPAuthInfo    authorization information
	 */
	public EPPNameWatchCreateCmd(
								 String aTransId, String aName,
								 String aRegistrant, EPPNameWatchRptTo aRptTo,
								 EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		   = aName;
		registrant     = aRegistrant;
		rptTo		   = aRptTo;
		authInfo	   = aAuthInfo;
		authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
	}

	// End EPPNameWatchCreateCmd.EPPNameWatchCreateCmd(String, String, String EPPNameWatchRptTo, EPPAuthInfo)

	/**
	 * Get the EPP command Namespace associated with EPPNameWatchCreateCmd.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchCreateCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPNameWatchCreateCmd</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the <code>EPPCodecException</code> will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		//NameWatch name
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		// Registrant
		if (registrant == null) {
			throw new EPPCodecException("registrant required attribute is not set");
		}

		// rptTo
		if (rptTo == null) {
			throw new EPPCodecException("rptTo required attribute is not set");
		}

		//AuthInfo
		if (authInfo == null) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPNameWatchCreateCmd.validateState()

	/**
	 * Gets NameWatch name.
	 *
	 * @return NameWatch Name
	 */
	public String getName() {
		return name;
	}

	// End EPPNameWatchCreateCmd.getName()

	/**
	 * Sets NameWatch name.
	 *
	 * @param aName NameWatch Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPNameWatchCreateCmd.setName(String)

	/**
	 * Gets NameWatch report to specification, which includes the e-mail
	 * address and the frequency.
	 *
	 * @return Report to specification if defined; <code>null</code> otherwise.
	 */
	public EPPNameWatchRptTo getRptTo() {
		return rptTo;
	}

	// End EPPNameWatchCreateCmd.getRptTo()

	/**
	 * Sets NameWatch Report to specification, which includes the e-mail
	 * address and frequency.
	 *
	 * @param aRptTo to specification.
	 */
	public void setRptTo(EPPNameWatchRptTo aRptTo) {
		rptTo = aRptTo;
	}

	// End EPPNameWatchCreateCmd.setRptTo(EPPNameWatchRptTo)

	/**
	 * Get NameWatch period.
	 *
	 * @return period if defined; <code>null</code> otherwise.
	 */
	public EPPNameWatchPeriod getPeriod() {
		return period;
	}

	// End EPPNameWatchCreateCmd.getPeriod()

	/**
	 * Set NameWatch period.
	 *
	 * @param newPeriod NameWatch period
	 */
	public void setPeriod(EPPNameWatchPeriod newPeriod) {
		period = newPeriod;
	}

	// End EPPNameWatchCreateCmd.setPeriod(int)

	/**
	 * Compare an instance of <code>EPPNameWatchCreateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchCreateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchCreateCmd theComp = (EPPNameWatchCreateCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
				)) {
			return false;
		}

		// rptTo
		if (
			!(
					(rptTo == null) ? (theComp.rptTo == null)
										: rptTo.equals(theComp.rptTo)
				)) {
			return false;
		}

		// Period
		if (
			!(
					(period == null) ? (theComp.period == null)
										 : period.equals(theComp.period)
				)) {
			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			return false;
		}

		return true;
	}

	// End EPPNameWatchCreateCmd.equals(Object)

	/**
	 * Clone <code>EPPNameWatchCreateCmd</code>.
	 *
	 * @return clone of <code>EPPNameWatchCreateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchCreateCmd clone = (EPPNameWatchCreateCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (rptTo != null) {
			clone.rptTo = (EPPNameWatchRptTo) rptTo.clone();
		}

		if (period != null) {
			clone.period = (EPPNameWatchPeriod) period.clone();
		}

		return clone;
	}

	// End EPPNameWatchCreateCmd.clone()

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

	// End EPPNameWatchCreateCmd.toString()

	/**
	 * Get authorization information
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPNameWatchCreateCmd.getAuthInfo()

	/**
	 * Get registrant.
	 *
	 * @return registrant
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPNameWatchCreateCmd.getRegistrant()

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
		}
	}

	// End EPPNameWatchCreateCmd.setAuthInfo(EPPAuthInfo)

	/**
	 * Set registrant.
	 *
	 * @param newRegistrant NameWatch registrant
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPNameWatchCreateCmd.setRegistrant(String)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPNameWatchCreateCmd instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPNameWatchCreateCmd
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPNameWatchCreateCmd
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		//Validate States
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on EPPNameWatchCreateCmd.encode: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPNameWatchMapFactory.NS,
							 ELM_NAMEWATCH_NAME);

		// Registrant
		EPPUtil.encodeString(
							 aDocument, root, registrant,
							 EPPNameWatchMapFactory.NS, ELM_REGISTRANT);

		// rptTo
		if (!rptTo.isRptToUnspec()) {
			EPPUtil.encodeComp(aDocument, root, rptTo);
		}

		// Period with Attribute of Unit
		if ((period != null) && !period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPNameWatchCreateCmd.doEncode(Document)

	/**
	 * Decode the EPPNameWatchCreateCmd attributes from the aElement DOM
	 * Element tree.
	 *
	 * @param aElement Root DOM Element to decode EPPNameWatchCreateCmd from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_NAMEWATCH_NAME);

		// registrant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPNameWatchMapFactory.NS,
								 ELM_REGISTRANT);

		// rptTo
		rptTo =
			(EPPNameWatchRptTo) EPPUtil.decodeComp(
												   aElement,
												   EPPNameWatchMapFactory.NS,
												   EPPNameWatchRptTo.ELM_NAME,
												   EPPNameWatchRptTo.class);

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
			throw new EPPDecodeException("Invalid state on EPPNameWatchCreateCmd.decode: "
										 + e);
		}
	}

	// End EPPNameWatchCreateCmd.doDecode(Element)
}
