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
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP NameWatch &lt;renew&gt; command, which provides a
 * transform operation     that allows a client to extend the validity period
 * of a nameWatch object. The EPP &lt;renew&gt; command provides a transform
 * operation that allows a client to extend the validity period of a nameWatch
 * object.  In addition to the standard EPP command elements, the
 * &lt;renew&gt; command MUST contain a &lt;nameWatch:renew&gt; element that
 * identifies the nameWatch namespace and the location of the nameWatch
 * schema.     The &lt;nameWatch:renew&gt; element SHALL contain the following
 * child elements:     <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the fully qualified nameWatch
 * roid of the object whose validity period is to be extended.  Use
 * <code>getRoid</code>     and <code>setRoid</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &lt;nameWatch:curExpDate&gt; element that contains the date on which the
 * current validity period ends.  This value ensures that repeated
 * &lt;renew&gt; commands do not result in multiple unanticipated successful
 * renewals. Use <code>getCurExpDate</code> and <code>setCurExpDate</code> to
 * get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;nameWatch:period&gt; element that contains the initial
 * registration period of the nameWatch object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchRenewResp
 */
public class EPPNameWatchRenewCmd extends EPPRenewCmd {
	/** XML Element Name of <code>EPPNameWatchRenewCmd</code> root element. */
	final static String ELM_NAME = "nameWatch:renew";

	/** XML Element Name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** XML Element Name for the <code>curExpDate</code> attribute. */
	private final static String ELM_CURRENT_EXPIRATION_DATE =
		"nameWatch:curExpDate";

	/** NameWatch Roid of nameWatch to create. */
	private String roid = null;

	/** Registration Period */
	private EPPNameWatchPeriod period = null;

	/** Current Expiration Date. */
	private java.util.Date curExpDate = null;

	/**
	 * Allocates a new <code>EPPNameWatchRenewCmd</code> with default attribute
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
	 * <br>     The roid and current expiration year must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPNameWatchRenewCmd() {
		roid     = null;

		curExpDate     = null;

		period = new EPPNameWatchPeriod(1);
	}

	// End EPPNameWatchRenewCmd.EPPNameWatchRenewCmd()

	/**
	 * <code>EPPNameWatchRenewCmd</code> constructor that takes the nameWatch
	 * roid and the current expiration year     as arguments.  The period will
	 * default to 1 year.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid NameWatch roid to renew.
	 * @param aCurExpDate The current expiration date of the nameWatch
	 */
	public EPPNameWatchRenewCmd(
								String aTransId, String aRoid, Date aCurExpDate) {
		super(aTransId);

		roid     = aRoid;

		curExpDate     = aCurExpDate;

		period = new EPPNameWatchPeriod(1);
	}

	// End EPPNameWatchRenewCmd.EPPNameWatchRenewCmd(String, String, Date)

	/**
	 * <code>EPPNameWatchRenewCmd</code> constructor that takes all of the
	 * attributes of the renew     command as arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid NameWatch roid to renew.
	 * @param aCurExpDate The current expiration date of the nameWatch.
	 * @param aPeriod Registration period in years.
	 */
	public EPPNameWatchRenewCmd(
								String aTransId, String aRoid, Date aCurExpDate,
								EPPNameWatchPeriod aPeriod) {
		super(aTransId);

		roid     = aRoid;

		curExpDate     = aCurExpDate;

		period = aPeriod;
	}

	// End EPPNameWatchRenewCmd.EPPNameWatchRenewCmd(String, String, Date, EPPNameWatchPeriod)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchRenewCmd</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchRenewCmd.getNamespace()

	/**
	 * Gets the nameWatch roid to renew.
	 *
	 * @return NameWatch Roid
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchRenewCmd.getRoid()

	/**
	 * Sets the nameWatch roid to renew.
	 *
	 * @param aRoid NameWatch Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchRenewCmd.setRoid(String)

	/**
	 * Compare an instance of <code>EPPNameWatchRenewCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchRenewCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchRenewCmd theMap = (EPPNameWatchRenewCmd) aObject;

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

	// End EPPNameWatchRenewCmd.equals(Object)

	/**
	 * Clone <code>EPPNameWatchRenewCmd</code>.
	 *
	 * @return clone of <code>EPPNameWatchRenewCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchRenewCmd clone = (EPPNameWatchRenewCmd) super.clone();

		if (period != null) {
			clone.period = (EPPNameWatchPeriod) period.clone();
		}

		return clone;
	}

	// End EPPNameWatchRenewCmd.clone()

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

	// End EPPNameWatchRenewCmd.toString()

	/**
	 * Get current expiration date.
	 *
	 * @return curExpDate
	 */
	public Date getCurExpDate() {
		return curExpDate;
	}

	// End EPPNameWatchRenewCmd.getCurExpDate()

	/**
	 * Gets the registration period of the renew command in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPNameWatchPeriod getPeriod() {
		return period;
	}

	// End EPPNameWatchRenewCmd.getPeriod()

	/**
	 * Set current expiration date.
	 *
	 * @param newCurExpDate Current Expiration Date
	 */
	public void setCurExpDate(Date newCurExpDate) {
		curExpDate = newCurExpDate;
	}

	// End EPPNameWatchRenewCmd.setCurExpDate(Date)

	/**
	 * Sets the registration period of the renew command in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPNameWatchPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPNameWatchRenewCmd.setPeriod(EPPNameWatchPeriod)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchRenewCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchRenewCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchRenewCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}

		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPNameWatchRenewCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);

		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Current Expiration Date
		EPPUtil.encodeDate(
						   aDocument, root, curExpDate,
						   EPPNameWatchMapFactory.NS,
						   ELM_CURRENT_EXPIRATION_DATE);

		// Period with Attribute of Unit
		if (!period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		return root;
	}

	// End EPPNameWatchRenewCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchRenewCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchRenewCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		String tempVal;

		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		// Current Expiration Date
		curExpDate =
			EPPUtil.decodeDate(
							   aElement, EPPNameWatchMapFactory.NS,
							   ELM_CURRENT_EXPIRATION_DATE);

		// Period
		period =
			(EPPNameWatchPeriod) EPPUtil.decodeComp(
													aElement,
													EPPNameWatchMapFactory.NS,
													EPPNameWatchPeriod.ELM_NAME,
													EPPNameWatchPeriod.class);

		//Validate States
		try {
			validateState();
		}

		 catch (EPPCodecException e) {
			throw new EPPDecodeException("Invalid state on EPPNameWatchRenewCmd.decode: "
										 + e);
		}
	}

	// End EPPNameWatchRenewCmd.doDecode(Element)

	/**
	 * Validates the state of the <code>EPPNameWatchRenewCmd</code> instance. A
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
			throw new EPPCodecException("roid required attribute is not set");
		}

		if (curExpDate == null) {
			throw new EPPCodecException("curExpDate required attribute is not set");
		}
	}

	// End EPPNameWatchRenewCmd.isValid()
}
