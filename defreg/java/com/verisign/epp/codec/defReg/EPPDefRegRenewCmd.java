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

import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP DefReg &lt;renew&gt; command, which provides a transform
 * operation     that allows a client to extend the validity period of a
 * defReg object.In addition to the standard EPP command elements, the
 * &lt;renew&gt; command MUST contain a &lt;defReg:renew&gt; element that
 * identifies the defReg namespace and the location of the defReg schema. The
 * &lt;defReg:renew&gt; element SHALL contain the following child elements:
 * <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:roid&gt; element that contains the fully  defReg roid of the
 * object whose validity period is to be extended.  Use <code>getRoid</code>
 * and <code>setRoid</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;defReg:curExpDate&gt; element that contains the date on which the
 * current validity period ends.  This value ensures that repeated
 * &lt;renew&gt; commands do not result in multiple unanticipated successful
 * renewals. Use <code>getCurExpDate</code> and     <code>setCurExpDate</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:period&gt; element that contains the initial
 * registration period of the defReg object. Use <code>getPeriod</code> and
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
 * @see com.verisign.epp.codec.defReg.EPPDefRegRenewResp
 */
public class EPPDefRegRenewCmd extends EPPRenewCmd {
	/** XML Element Name of <code>EPPDefRegRenewCmd</code> root element. */
	final static String ELM_NAME = "defReg:renew";

	/** XML Element Roid for the <code>name</code> attribute. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** XML Element Name for the <code>currentExpirationYear</code> attribute. */
	private final static String ELM_CURRENT_EXPIRATION_DATE =
		"defReg:curExpDate";

	/** DefReg Roid of defReg to create. */
	private String roid = null;

	/** Registration Period */
	private EPPDefRegPeriod period = null;

	/** Current Expiration Date. */
	private java.util.Date curExpDate = null;

	/**
	 * Allocates a new <code>EPPDefRegRenewCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * roid is set to <code>null</code>
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
	public EPPDefRegRenewCmd() {
		roid		   = null;
		period		   = new EPPDefRegPeriod(1);
		curExpDate     = null;
	}

	// End EPPDefRegRenewCmd.EPPDefRegRenewCmd()

	/**
	 * <code>EPPDefRegRenewCmd</code> constructor that takes the defReg name
	 * and the current expiration year     as arguments.  The period will
	 * default to 1 year.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid DefReg name to renew.
	 * @param aCurExpDate The current expiration date of the defReg
	 */
	public EPPDefRegRenewCmd(String aTransId, String aRoid, Date aCurExpDate) {
		super(aTransId);

		roid		   = aRoid;
		curExpDate     = aCurExpDate;

		// default to 1 year
		period = new EPPDefRegPeriod(1);
	}

	// End EPPDefRegRenewCmd.EPPDefRegRenewCmd(String, String, Date)

	/**
	 * <code>EPPDefRegRenewCmd</code> constructor that takes all of the
	 * attributes of the renew     command as arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid DefReg name to renew.
	 * @param aCurExpDate The current expiration date of the defReg.
	 * @param aPeriod Registration period in years.
	 */
	public EPPDefRegRenewCmd(
							 String aTransId, String aRoid, Date aCurExpDate,
							 EPPDefRegPeriod aPeriod) {
		super(aTransId);

		roid		   = aRoid;
		curExpDate     = aCurExpDate;
		period		   = aPeriod;
	}

	// End EPPDefRegRenewCmd.EPPDefRegRenewCmd(String, String, Date, EPPDefRegPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegRenewCmd</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegRenewCmd.getNamespace()

	/**
	 * Gets the defReg roid to renew.
	 *
	 * @return DefReg Roid    if defined; <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegRenewCmd.getRoid()

	/**
	 * Sets the defReg roid to renew.
	 *
	 * @param aRoid DefReg Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegRenewCmd.setRoid(String)

	/**
	 * Compare an instance of <code>EPPDefRegRenewCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegRenewCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegRenewCmd theMap = (EPPDefRegRenewCmd) aObject;

		// Roid
		if (
			!(
					(roid == null) ? (theMap.roid == null)
									   : roid.equals(theMap.roid)
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

	// End EPPDefRegRenewCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegRenewCmd</code>.
	 *
	 * @return clone of <code>EPPDefRegRenewCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegRenewCmd clone = (EPPDefRegRenewCmd) super.clone();

		if (period != null) {
			clone.period = (EPPDefRegPeriod) period.clone();
		}

		return clone;
	}

	// End EPPDefRegRenewCmd.clone()

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

	// End EPPDefRegRenewCmd.toString()

	/**
	 * Get current expiration date.
	 *
	 * @return java.util.Date
	 */
	public Date getCurExpDate() {
		return curExpDate;
	}

	// End EPPDefRegRenewCmd.getCurExpDate(Date)

	/**
	 * Gets the registration period of the renew command in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPDefRegPeriod getPeriod() {
		return period;
	}

	// End EPPDefRegRenewCmd.getPeriod()

	/**
	 * Set current expiration date.
	 *
	 * @param newCurExpDate java.util.Date
	 */
	public void setCurExpDate(Date newCurExpDate) {
		curExpDate = newCurExpDate;
	}

	// End EPPDefRegRenewCmd.setCurExpDate(Date)

	/**
	 * Sets the registration period of the renew command in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPDefRegPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPDefRegRenewCmd.setPeriod(EPPDefRegPeriod)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegRenewCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPDefRegRenewCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegRenewCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPDefRegRenewCmd invalid state: "
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

		// Current Expiration Date
		EPPUtil.encodeDate(
						   aDocument, root, curExpDate, EPPDefRegMapFactory.NS,
						   ELM_CURRENT_EXPIRATION_DATE);

		// Period with Attribute of Unit
		if (!period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		return root;
	}

	// End EPPDefRegRenewCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegRenewCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegRenewCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		String tempVal;

		// DefReg Roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

		// Current Expiration Year
		curExpDate =
			EPPUtil.decodeDate(
							   aElement, EPPDefRegMapFactory.NS,
							   ELM_CURRENT_EXPIRATION_DATE);

		// Period
		period =
			(EPPDefRegPeriod) EPPUtil.decodeComp(
												 aElement,
												 EPPDefRegMapFactory.NS,
												 EPPDefRegPeriod.ELM_NAME,
												 EPPDefRegPeriod.class);
	}

	// End EPPDefRegRenewCmd.doDecode(Element)

	/**
	 * Validates the state of the <code>EPPDefRegRenewCmd</code> instance.  A
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
		if (roid == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (curExpDate == null) {
			throw new EPPCodecException("currentExpirationYear required attribute is not set");
		}
	}

	// End EPPDefRegRenewCmd.isValid()
}
