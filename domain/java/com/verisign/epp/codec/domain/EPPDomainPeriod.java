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
 * Represents a domain Period. A domain name object MAY have a specified
 * validity period.  If server policy supports domain object validity periods,
 * the validity period is defined when a domain object is created, and it MAY
 * be extended by the EPP &lt;renew&gt; or &lt;transfer&gt; commands.  As a
 * matter of server policy, this specification does not define actions to be
 * taken upon expiration of a domain object's validity period. <br>
 * <br>
 * Validity periods are measured in years or months with the appropriate units
 * specified using the <code>unit</code> attribute.  Valid values for the
 * <code>unit</code> attribute are <code>y</code> for years and <code>m</code>
 * for months.
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPDomainPeriod
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Period in Unit Month */
	public final static java.lang.String PERIOD_UNIT_MONTH = "m";

	/** Period in Unit Year */
	public final static java.lang.String PERIOD_UNIT_YEAR = "y";

	/** Unspecified Period */
	final static int UNSPEC_PERIOD = -1;

	/** XML Element Name of <code>EPPDomainPeriod</code> root element. */
	final static java.lang.String ELM_NAME = "domain:period";

	/** XML tag name for the <code>period</code> attribute. */
	private final static java.lang.String ELM_PERIOD = "domain:period";

	/** XML attribute name for the <code>period</code> element. */
	private final static java.lang.String ELM_PERIOD_UNIT = "unit";

	/** Maximum number of periods. */
	private final static int MAX_PERIOD = 99;

	/** Minimum number of periods. */
	private final static int MIN_PERIOD = 1;

	/** Domain period. */
	private int period = 0;

	/** Domain period unit. */
	private java.lang.String pUnit = "y";

	/**
	 * <code>EPPDomainPeriod</code> default constructor.  The period is
	 * initialized to <code>unspecified</code>.     The period must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPDomainPeriod() {
		period = UNSPEC_PERIOD;
	}

	// End EPPDomainPeriod.EPPDomainPeriod()

	/**
	 * <code>EPPDomainPeriod</code> constructor that takes the domain period
	 * (in unit of year) as an argument
	 *
	 * @param aPeriod int
	 */
	public EPPDomainPeriod(int aPeriod) {
		period     = aPeriod;
		pUnit	   = "y";
	}

	// End EPPDomainPeriod.EPPDomainPeriod(int)

	/**
	 * <code>EPPDomainPeriod</code> constructor that takes the domain period
	 * and period unit as an arguments
	 *
	 * @param aPUnit String
	 * @param aPeriod int
	 */
	public EPPDomainPeriod(String aPUnit, int aPeriod) {
		pUnit = aPUnit;

		if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH)) {
			pUnit = PERIOD_UNIT_YEAR;
		}

		period = aPeriod;
	}

	// End EPPDomainPeriod.EPPDomainPeriod(String, int)

	/**
	 * Clone <code>EPPDomainPeriod</code>.
	 *
	 * @return clone of <code>EPPDomainPeriod</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainPeriod clone = null;

		clone = (EPPDomainPeriod) super.clone();

		return clone;
	}

	// End EPPDomainPeriod.clone()

	/**
	 * Decode the EPPDomainPeriod attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPDomainPeriod from.
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

	// End EPPDomainPeriod.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPDomainPeriod
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPDomainPeriod
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPDomainPeriod
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Period with Attribute of Unit
		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		// Check the Period unit
		if (pUnit == null) {
			throw new EPPEncodeException("EPPDomainPeriod: Period Unit should not be null");
		}
		else {
			//if (!pUnit.equals(PERIOD_UNIT_YEAR) && !pUnit.equals(PERIOD_UNIT_MONTH))
			//    throw new EPPEncodeException("EPPDomainPeriod: Period Unit has an invalid value");
			// add attribute here
			root.setAttribute(ELM_PERIOD_UNIT, pUnit);

			// add value
			Text currVal = aDocument.createTextNode(period + "");

			// append child
			root.appendChild(currVal);
		}

		return root;
	}

	// End EPPDomainPeriod.encode(Document)

	/**
	 * implements a deep <code>EPPDomainPeriod</code> compare.
	 *
	 * @param aObject <code>EPPDomainPeriod</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainPeriod)) {
			return false;
		}

		EPPDomainPeriod theComp = (EPPDomainPeriod) aObject;

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

	// End EPPDomainPeriod.equals(Object)

	/**
	 * Get domain period.
	 *
	 * @return int
	 */
	public int getPeriod() {
		return period;
	}

	// End EPPDomainPeriod.getPeriod()

	/**
	 * Get domain period unit.
	 *
	 * @return String
	 */
	public String getPUnit() {
		return pUnit;
	}

	// End EPPDomainPeriod.getPUnit()

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

	// End EPPDomainPeriod.isPeriodUnspec()

	/**
	 * Set domain period.
	 *
	 * @param newPeriod int
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	public void setPeriod(int newPeriod) throws EPPCodecException {
		
		if (newPeriod != UNSPEC_PERIOD) {
			if ((newPeriod < MIN_PERIOD) && (newPeriod > MAX_PERIOD)) {
				throw new EPPCodecException("period of " + newPeriod
											+ " is out of range, must be between "
											+ MIN_PERIOD + " and " + MAX_PERIOD);
			}
		}

		period = newPeriod;
		
	}

	// End EPPDomainPeriod.setPeriod(int)

	/**
	 * Set domain period of un. Creation date: (5/30/01 11:36:52 AM)
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

	// End EPPDomainPeriod.toString()
}
