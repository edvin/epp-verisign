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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents a nameWatch Period. A nameWatch name object MAY have a specified
 * validity period.  If server policy supports nameWatch object validity
 * periods, the validity period is defined when a nameWatch object is created,
 * and it MAY be extended by the EPP &lt;renew&gt; or &lt;transfer&gt;
 * commands.  As a matter of server policy, this specification does not define
 * actions to be taken upon expiration of a nameWatch object's validity
 * period. <br>
 * <br>
 * Validity periods are measured in years or months with the appropriate units
 * specified using the <code>unit</code> attribute.  Valid values for the
 * <code>unit</code> attribute are <code>y</code> for years and <code>m</code>
 * for months. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPNameWatchPeriod
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Period in Unit Month */
	public final static String PERIOD_UNIT_MONTH = "m";

	/** Period in Unit Year */
	public final static String PERIOD_UNIT_YEAR = "y";

	/** Unspecified Period */
	final static int UNSPEC_PERIOD = -1;

	/** XML Element Name of <code>EPPNameWatchPeriod</code> root element. */
	final static String ELM_NAME = "nameWatch:period";

	/** XML tag name for the <code>period</code> attribute. */
	private final static String ELM_PERIOD = "nameWatch:period";

	/** XML attribute name for the <code>period</code> element. */
	private final static String ELM_PERIOD_UNIT = "unit";

	/** Maximum number of periods. */
	private final static int MAX_PERIOD = 99;

	/** Minimum number of periods. */
	private final static int MIN_PERIOD = 1;

	/** NameWatch period. */
	private int period = 0;

	/** NameWatch period unit. */
	private String pUnit = "y";

	/**
	 * <code>EPPNameWatchPeriod</code> default constructor.  The period is
	 * initialized to <code>unspecified</code>.     The period must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPNameWatchPeriod() {
		period = UNSPEC_PERIOD;
	}

	// End EPPNameWatchPeriod.EPPNameWatchPeriod()

	/**
	 * <code>EPPNameWatchPeriod</code> constructor that takes the nameWatch
	 * period (in unit of year) as an argument
	 *
	 * @param aPeriod int
	 */
	public EPPNameWatchPeriod(int aPeriod) {
		period     = aPeriod;
		pUnit	   = "y";
	}

	// End EPPNameWatchPeriod.EPPNameWatchPeriod(int)

	/**
	 * <code>EPPNameWatchPeriod</code> constructor that takes the nameWatch
	 * period and period unit as an arguments
	 *
	 * @param aPUnit String
	 * @param aPeriod int
	 */
	public EPPNameWatchPeriod(String aPUnit, int aPeriod) {
		pUnit = aPUnit;

		if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH)) {
			pUnit = PERIOD_UNIT_YEAR;
		}

		period = aPeriod;
	}

	// End EPPNameWatchPeriod.EPPNameWatchPeriod(String, int)

	/**
	 * Clone <code>EPPNameWatchPeriod</code>.
	 *
	 * @return clone of <code>EPPNameWatchPeriod</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchPeriod clone = null;

		clone = (EPPNameWatchPeriod) super.clone();

		return clone;
	}

	// End EPPNameWatchPeriod.clone()

	/**
	 * Decode the EPPNameWatchPeriod attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPNameWatchPeriod from.
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

	// End EPPNameWatchPeriod.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPNameWatchPeriod
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the
	 * 		   EPPNameWatchPeriod instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPNameWatchPeriod
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Period with Attribute of Unit
		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		// Check the Period unit
		if (pUnit == null) {
			throw new EPPEncodeException("EPPNameWatchPeriod: Period Unit should not be null");
		}
		else {
			//if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH))
			//    throw new EPPEncodeException("EPPNameWatchPeriod: Period Unit has an invalid value");
			// add attribute here
			root.setAttribute(ELM_PERIOD_UNIT, pUnit);

			// add value
			Text currVal = aDocument.createTextNode(period + "");

			// append child
			root.appendChild(currVal);
		}

		return root;
	}

	// End EPPNameWatchPeriod.encode(Document)

	/**
	 * implements a deep <code>EPPNameWatchPeriod</code> compare.
	 *
	 * @param aObject <code>EPPNameWatchPeriod</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchPeriod)) {
			return false;
		}

		EPPNameWatchPeriod theComp = (EPPNameWatchPeriod) aObject;

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

	// End EPPNameWatchPeriod.equals(Object)

	/**
	 * Get nameWatch period.
	 *
	 * @return int
	 */
	public int getPeriod() {
		return period;
	}

	// End EPPNameWatchPeriod.getPeriod()

	/**
	 * Get nameWatch period unit.
	 *
	 * @return String
	 */
	public String getPUnit() {
		return pUnit;
	}

	// End EPPNameWatchPeriod.getPUnit()

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

	// End EPPNameWatchPeriod.isPeriodUnspec()

	/**
	 * Set nameWatch period.
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

	// End EPPNameWatchPeriod.setPeriod(int)

	/**
	 * Set nameWatch period of un. Creation date: (5/30/01 11:36:52 AM)
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

	// End EPPNameWatchPeriod.toString()
}
