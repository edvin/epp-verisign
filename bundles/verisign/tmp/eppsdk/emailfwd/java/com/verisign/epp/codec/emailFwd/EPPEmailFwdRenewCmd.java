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

import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP EmailFwd &lt;renew&gt; command, which provides a transform
 * operation     that allows a client to extend the validity period of a
 * emailFwd object. The EPP &lt;renew&gt; command provides a transform
 * operation that allows a client to extend the validity period of a emailFwd
 * object.  In addition to the standard EPP command elements, the
 * &lt;renew&gt; command MUST contain a &lt;emailFwd:renew&gt; element that
 * identifies the emailFwd namespace and the location of the emailFwd schema.
 * The &lt;emailFwd:renew&gt; element SHALL contain the following child
 * elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name of the object whose validity period is to be extended.  Use
 * <code>getName</code>     and <code>setName</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;emailFwd:curExpDate&gt; element that contains the date on which the
 * current validity period ends.  This value ensures that repeated
 * &lt;renew&gt; commands do not result in multiple unanticipated successful
 * renewals. Use <code>getCurExpDate</code> and     <code>setCurExpDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:period&gt; element that contains the initial
 * registration period of the emailFwd object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewResp
 */
public class EPPEmailFwdRenewCmd extends EPPRenewCmd {
	/** XML Element Name of <code>EPPEmailFwdRenewCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:renew";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** XML Element Name for the <code>currentExpirationYear</code> attribute. */
	private final static String ELM_CURRENT_EXPIRATION_DATE =
		"emailFwd:curExpDate";

	/** EmailFwd Name of emailFwd to create. */
	private String name = null;

	/** Registration Period */
	private EPPEmailFwdPeriod period = null;

	/** Current Expiration Date. */
	private java.util.Date curExpDate = null;

	/**
	 * Allocates a new <code>EPPEmailFwdRenewCmd</code> with default attribute
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
	public EPPEmailFwdRenewCmd() {
		name		   = null;
		period		   = new EPPEmailFwdPeriod(1);
		curExpDate     = null;
	}

	// End EPPEmailFwdRenewCmd.EPPEmailFwdRenewCmd()

	/**
	 * <code>EPPEmailFwdRenewCmd</code> constructor that takes the emailFwd
	 * name and the current expiration year     as arguments.  The period will
	 * default to 1 year.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name to renew.
	 * @param aCurExpDate The current expiration date of the emailFwd
	 */
	public EPPEmailFwdRenewCmd(String aTransId, String aName, Date aCurExpDate) {
		super(aTransId);

		name		   = aName;
		curExpDate     = aCurExpDate;

		// default to 1 year
		period = new EPPEmailFwdPeriod(1);
	}

	// End EPPEmailFwdRenewCmd.EPPEmailFwdRenewCmd(String, String, Date)

	/**
	 * <code>EPPEmailFwdRenewCmd</code> constructor that takes all of the
	 * attributes of the renew     command as arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name to renew.
	 * @param aCurExpDate The current expiration date of the emailFwd.
	 * @param aPeriod Registration period in years.
	 */
	public EPPEmailFwdRenewCmd(
							   String aTransId, String aName, Date aCurExpDate,
							   EPPEmailFwdPeriod aPeriod) {
		super(aTransId);

		name		   = aName;
		curExpDate     = aCurExpDate;
		period		   = aPeriod;
	}

	// End EPPEmailFwdRenewCmd.EPPEmailFwdRenewCmd(String, String, Date, EPPEmailFwdPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdRenewCmd</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdRenewCmd.getNamespace()

	/**
	 * Gets the emailFwd name to renew.
	 *
	 * @return EmailFwd Name    if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdRenewCmd.getName()

	/**
	 * Sets the emailFwd name to renew.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdRenewCmd.setName(String)

	/**
	 * Compare an instance of <code>EPPEmailFwdRenewCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdRenewCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdRenewCmd theMap = (EPPEmailFwdRenewCmd) aObject;

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

	// End EPPEmailFwdRenewCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdRenewCmd</code>.
	 *
	 * @return clone of <code>EPPEmailFwdRenewCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdRenewCmd clone = (EPPEmailFwdRenewCmd) super.clone();

		if (period != null) {
			clone.period = (EPPEmailFwdPeriod) period.clone();
		}

		return clone;
	}

	// End EPPEmailFwdRenewCmd.clone()

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

	// End EPPEmailFwdRenewCmd.toString()

	/**
	 * Get current expiration date.
	 *
	 * @return java.util.Date
	 */
	public Date getCurExpDate() {
		return curExpDate;
	}

	// End EPPEmailFwdRenewCmd.getCurExpDate(Date)

	/**
	 * Gets the registration period of the renew command in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPEmailFwdPeriod getPeriod() {
		return period;
	}

	// End EPPEmailFwdRenewCmd.getPeriod()

	/**
	 * Set current expiration date.
	 *
	 * @param newCurExpDate java.util.Date
	 */
	public void setCurExpDate(Date newCurExpDate) {
		curExpDate = newCurExpDate;
	}

	// End EPPEmailFwdRenewCmd.setCurExpDate(Date)

	/**
	 * Sets the registration period of the renew command in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPEmailFwdPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPEmailFwdRenewCmd.setPeriod(EPPEmailFwdPeriod)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdRenewCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPEmailFwdRenewCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdRenewCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPEmailFwdRenewCmd invalid state: "
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

		// Current Expiration Date
		EPPUtil.encodeDate(
						   aDocument, root, curExpDate, EPPEmailFwdMapFactory.NS,
						   ELM_CURRENT_EXPIRATION_DATE);

		// Period with Attribute of Unit
		if (!period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		return root;
	}

	// End EPPEmailFwdRenewCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdRenewCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdRenewCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		String tempVal;

		// EmailFwd Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		// Current Expiration Year
		curExpDate =
			EPPUtil.decodeDate(
							   aElement, EPPEmailFwdMapFactory.NS,
							   ELM_CURRENT_EXPIRATION_DATE);

		// Period
		period =
			(EPPEmailFwdPeriod) EPPUtil.decodeComp(
												   aElement,
												   EPPEmailFwdMapFactory.NS,
												   EPPEmailFwdPeriod.ELM_NAME,
												   EPPEmailFwdPeriod.class);
	}

	// End EPPEmailFwdRenewCmd.doDecode(Element)

	/**
	 * Validates the state of the <code>EPPEmailFwdRenewCmd</code> instance.  A
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

	// End EPPEmailFwdRenewCmd.isValid()
}
