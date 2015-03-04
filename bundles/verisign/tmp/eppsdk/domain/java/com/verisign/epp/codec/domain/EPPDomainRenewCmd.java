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

import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Domain &lt;renew&gt; command, which provides a transform
 * operation     that allows a client to extend the validity period of a
 * domain object. The EPP &lt;renew&gt; command provides a transform operation
 * that allows a client to extend the validity period of a domain object.  In
 * addition to the standard EPP command elements, the &lt;renew&gt; command
 * MUST contain a &lt;domain:renew&gt; element that identifies the domain
 * namespace and the location of the domain schema.     The
 * &lt;domain:renew&gt; element SHALL contain the following child elements:
 * <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * of the object whose validity period is to be extended.  Use
 * <code>getName</code>     and <code>setName</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;domain:curExpDate&gt; element that contains the date on which the
 * current validity period ends.  This value ensures that repeated
 * &lt;renew&gt; commands do not result in multiple unanticipated successful
 * renewals. Use <code>getCurExpDate</code> and     <code>setCurExpDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:period&gt; element that contains the initial
 * registration period of the domain object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainRenewResp
 */
public class EPPDomainRenewCmd extends EPPRenewCmd {
	/** XML Element Name of <code>EPPDomainRenewCmd</code> root element. */
	final static String ELM_NAME = "domain:renew";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML Element Name for the <code>currentExpirationYear</code> attribute. */
	private final static String ELM_CURRENT_EXPIRATION_DATE =
		"domain:curExpDate";

	/** Domain Name of domain to create. */
	private String name = null;

	/** Registration Period */
	private EPPDomainPeriod period = null;

	/** Current Expiration Date. */
	private java.util.Date curExpDate = null;

	/**
	 * Allocates a new <code>EPPDomainRenewCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>1 year</code>
	 * </li>
	 * <li>
	 * current expiration date to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name and current expiration year must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDomainRenewCmd() {
		name		   = null;
		period		   = new EPPDomainPeriod(1);
		curExpDate     = null;
	}

	// End EPPDomainRenewCmd.EPPDomainRenewCmd()

	/**
	 * <code>EPPDomainRenewCmd</code> constructor that takes the domain name
	 * and the current expiration year     as arguments.  The period will
	 * default to 1 year.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name to renew.
	 * @param aCurExpDate The current expiration date of the domain
	 */
	public EPPDomainRenewCmd(String aTransId, String aName, Date aCurExpDate) {
		super(aTransId);

		name		   = aName;
		curExpDate     = aCurExpDate;

		// default to 1 year
		period = new EPPDomainPeriod(1);
	}

	// End EPPDomainRenewCmd.EPPDomainRenewCmd(String, String, Date)

	/**
	 * <code>EPPDomainRenewCmd</code> constructor that takes all of the
	 * attributes of the renew     command as arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name to renew.
	 * @param aCurExpDate The current expiration date of the domain.
	 * @param aPeriod Registration period in years.
	 */
	public EPPDomainRenewCmd(
							 String aTransId, String aName, Date aCurExpDate,
							 EPPDomainPeriod aPeriod) {
		super(aTransId);

		name		   = aName;
		curExpDate     = aCurExpDate;
		period		   = aPeriod;
	}

	// End EPPDomainRenewCmd.EPPDomainRenewCmd(String, String, Date, EPPDomainPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainRenewCmd</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainRenewCmd.getNamespace()

	/**
	 * Gets the domain name to renew.
	 *
	 * @return Domain Name    if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainRenewCmd.getName()

	/**
	 * Sets the domain name to renew.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainRenewCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPDomainRenewCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainRenewCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainRenewCmd theMap = (EPPDomainRenewCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theMap.name == null)
									   : name.equals(theMap.name)
				)) {
			return false;
		}

		// Current Expiration Date
		if (
			!(
					(curExpDate == null) ? (theMap.curExpDate == null)
											 : curExpDate.equals(theMap.curExpDate)
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

		return true;
	}

	// End EPPDomainRenewCmd.equals(Object)

	/**
	 * Clone <code>EPPDomainRenewCmd</code>.
	 *
	 * @return clone of <code>EPPDomainRenewCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainRenewCmd clone = (EPPDomainRenewCmd) super.clone();

		if (period != null) {
			clone.period = (EPPDomainPeriod) period.clone();
		}

		return clone;
	}

	// End EPPDomainRenewCmd.clone()

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

	// End EPPDomainRenewCmd.toString()

	/**
	 * Get current expiration date.
	 *
	 * @return java.util.Date
	 */
	public Date getCurExpDate() {
		return curExpDate;
	}

	// End EPPDomainRenewCmd.getCurExpDate(Date)

	/**
	 * Gets the registration period of the renew command in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPDomainPeriod getPeriod() {
		return period;
	}

	// End EPPDomainRenewCmd.getPeriod()

	/**
	 * Set current expiration date.
	 *
	 * @param newCurExpDate java.util.Date
	 */
	public void setCurExpDate(Date newCurExpDate) {
		curExpDate = newCurExpDate;
	}

	// End EPPDomainRenewCmd.setCurExpDate(Date)

	/**
	 * Sets the registration period of the renew command in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPDomainPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPDomainRenewCmd.setPeriod(EPPDomainPeriod)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainRenewCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPDomainRenewCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainRenewCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPDomainRenewCmd invalid state: "
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

		// Current Expiration Date
		EPPUtil.encodeDate(
						   aDocument, root, curExpDate, EPPDomainMapFactory.NS,
						   ELM_CURRENT_EXPIRATION_DATE);

		// Period with Attribute of Unit
		if (!period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		return root;
	}

	// End EPPDomainRenewCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainRenewCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainRenewCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		String tempVal;

		// Domain Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Current Expiration Year
		curExpDate =
			EPPUtil.decodeDate(
							   aElement, EPPDomainMapFactory.NS,
							   ELM_CURRENT_EXPIRATION_DATE);

		// Period
		period =
			(EPPDomainPeriod) EPPUtil.decodeComp(
												 aElement,
												 EPPDomainMapFactory.NS,
												 EPPDomainPeriod.ELM_NAME,
												 EPPDomainPeriod.class);
	}

	// End EPPDomainRenewCmd.doDecode(Element)

	/**
	 * Validates the state of the <code>EPPDomainRenewCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the EPPCodecException will contain a
	 * description of the error.     throws     EPPCodecException    State
	 * error.  This will contain the name of the     attribute that is not
	 * valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (curExpDate == null) {
			throw new EPPCodecException("currentExpirationYear required attribute is not set");
		}
	}

	// End EPPDomainRenewCmd.isValid()
}
