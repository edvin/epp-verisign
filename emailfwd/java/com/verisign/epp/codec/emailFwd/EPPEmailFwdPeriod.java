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

import org.w3c.dom.*;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a emailFwd Period. A emailFwd name object MAY have a specified
 * validity period.  If server policy supports emailFwd object validity
 * periods, the validity period is defined when a emailFwd object is created,
 * and it MAY be extended by the EPP &lt;renew&gt; or &lt;transfer&gt;
 * commands.  As a matter of server policy, this specification does not define
 * actions to be taken upon expiration of a emailFwd object's validity period. <br>
 * <br>
 * Validity periods are measured in years or months with the appropriate units
 * specified using the <code>unit</code> attribute.  Valid values for the
 * <code>unit</code> attribute are <code>y</code> for years and <code>m</code>
 * for months.
 */
public class EPPEmailFwdPeriod
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Period in Unit Month */
	public final static java.lang.String PERIOD_UNIT_MONTH = "m";

	/** Period in Unit Year */
	public final static java.lang.String PERIOD_UNIT_YEAR = "y";

	/** Unspecified Period */
	final static int UNSPEC_PERIOD = -1;

	/** XML Element Name of <code>EPPEmailFwdPeriod</code> root element. */
	final static java.lang.String ELM_NAME = "emailFwd:period";

	/** XML tag name for the <code>period</code> attribute. */
	private final static java.lang.String ELM_PERIOD = "emailFwd:period";

	/** XML attribute name for the <code>period</code> element. */
	private final static java.lang.String ELM_PERIOD_UNIT = "unit";

	/** Maximum number of periods. */
	private final static int MAX_PERIOD = 99;

	/** Minimum number of periods. */
	private final static int MIN_PERIOD = 1;

	/** EmailFwd period. */
	private int period = 0;

	/** EmailFwd period unit. */
	private java.lang.String pUnit = "y";

	/**
	 * <code>EPPEmailFwdPeriod</code> default constructor.  The period is
	 * initialized to <code>unspecified</code>.     The period must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPEmailFwdPeriod() {
		period = UNSPEC_PERIOD;
	}

	// End EPPEmailFwdPeriod.EPPEmailFwdPeriod()

	/**
	 * <code>EPPEmailFwdPeriod</code> constructor that takes the emailFwd
	 * period (in unit of year) as an argument
	 *
	 * @param aPeriod int
	 */
	public EPPEmailFwdPeriod(int aPeriod) {
		period     = aPeriod;
		pUnit	   = "y";
	}

	// End EPPEmailFwdPeriod.EPPEmailFwdPeriod(int)

	/**
	 * <code>EPPEmailFwdPeriod</code> constructor that takes the emailFwd
	 * period and period unit as an arguments
	 *
	 * @param aPUnit String
	 * @param aPeriod int
	 */
	public EPPEmailFwdPeriod(String aPUnit, int aPeriod) {
		pUnit = aPUnit;

		if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH)) {
			pUnit = PERIOD_UNIT_YEAR;
		}

		period = aPeriod;
	}

	// End EPPEmailFwdPeriod.EPPEmailFwdPeriod(String, int)

	/**
	 * Clone <code>EPPEmailFwdPeriod</code>.
	 *
	 * @return clone of <code>EPPEmailFwdPeriod</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdPeriod clone = null;

		clone = (EPPEmailFwdPeriod) super.clone();

		return clone;
	}

	// End EPPEmailFwdPeriod.clone()

	/**
	 * Decode the EPPEmailFwdPeriod attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPEmailFwdPeriod from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Period
		String tempVal = null;
		tempVal     = aElement.getFirstChild().getNodeValue();
		pUnit	    = aElement.getAttribute(ELM_PERIOD_UNIT);

		if (tempVal == null) {
			period = UNSPEC_PERIOD;
		}
		else {
			period = Integer.parseInt(tempVal);
		}
	}

	// End EPPEmailFwdPeriod.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPEmailFwdPeriod
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPEmailFwdPeriod
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPEmailFwdPeriod
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Period with Attribute of Unit
		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		// Check the Period unit
		if (pUnit == null) {
			throw new EPPEncodeException("EPPEmailFwdPeriod: Period Unit should not be null");
		}
		else {
			//if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH))
			//    throw new EPPEncodeException("EPPEmailFwdPeriod: Period Unit has an invalid value");
			// add attribute here
			root.setAttribute(ELM_PERIOD_UNIT, pUnit);

			// add value
			Text currVal = aDocument.createTextNode(period + "");

			// append child
			root.appendChild(currVal);
		}

		return root;
	}

	// End EPPEmailFwdPeriod.encode(Document)

	/**
	 * implements a deep <code>EPPEmailFwdPeriod</code> compare.
	 *
	 * @param aObject <code>EPPEmailFwdPeriod</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdPeriod)) {
			return false;
		}

		EPPEmailFwdPeriod theComp = (EPPEmailFwdPeriod) aObject;

		// period
		if (period != theComp.period) {
			return false;
		}

		// pUnit
		if (
			!(
					(pUnit == null) ? (theComp.pUnit == null)
										: pUnit.equals(theComp.pUnit)
				)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdPeriod.equals(Object)

	/**
	 * Get emailFwd period.
	 *
	 * @return int
	 */
	public int getPeriod() {
		return period;
	}

	// End EPPEmailFwdPeriod.getPeriod()

	/**
	 * Get emailFwd period unit.
	 *
	 * @return String
	 */
	public String getPUnit() {
		return pUnit;
	}

	// End EPPEmailFwdPeriod.getPUnit()

	/**
	 * Test whether the period has been specfied: <code>true</code> is
	 * unspecified and <code>false</code> is specified.
	 *
	 * @return boolean
	 */
	public boolean isPeriodUnspec() {
		if (period == UNSPEC_PERIOD) {
			return true;
		}
		else {
			return false;
		}
	}

	// End EPPEmailFwdPeriod.isPeriodUnspec()

	/**
	 * Set emailFwd period.
	 *
	 * @param newPeriod int
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	public void setPeriod(int newPeriod) throws EPPCodecException {
		if (period != UNSPEC_PERIOD) {
			if ((period < MIN_PERIOD) && (period > MAX_PERIOD)) {
				throw new EPPCodecException("period of " + period
											+ " is out of range, must be between "
											+ MIN_PERIOD + " and " + MAX_PERIOD);
			}
		}

		period = newPeriod;
	}

	// End EPPEmailFwdPeriod.setPeriod(int)

	/**
	 * Set emailFwd period of un. Creation date: (5/30/01 11:36:52 AM)
	 *
	 * @param newPUnit java.lang.String
	 */
	public void setPUnit(java.lang.String newPUnit) {
		pUnit = newPUnit;

		if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH)) {
			pUnit = PERIOD_UNIT_YEAR;
		}
	}

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

	// End EPPEmailFwdPeriod.toString()
}
