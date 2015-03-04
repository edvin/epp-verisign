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

package com.verisign.epp.codec.relateddomainext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
// EPP Imports

/**
 * Represents a domain Period. A domain name object MAY have a specified
 * validity period. If server policy supports domain object validity periods,
 * the validity period is defined when a domain object is created, and it MAY be
 * extended by the EPP &lt;renew&gt; or &lt;transfer&gt; commands. As a matter
 * of server policy, this specification does not define actions to be taken upon
 * expiration of a domain object's validity period. <br>
 * <br>
 * Validity periods are measured in years or months with the appropriate units
 * specified using the <code>unit</code> attribute. Valid values for the
 * <code>unit</code> attribute are <code>y</code> for years and <code>m</code>
 * for months.
 * 
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPRelatedDomainExtPeriod implements
		com.verisign.epp.codec.gen.EPPCodecComponent {
	/** Period in Unit Month */
	public final static java.lang.String PERIOD_UNIT_MONTH = "m";

	/** Period in Unit Year */
	public final static java.lang.String PERIOD_UNIT_YEAR = "y";

	/** Unspecified Period */
	final static int UNSPEC_PERIOD = -1;

	/** XML Element Name of <code>EPPRelatedDomainExtPeriod</code> root element. */
	final static java.lang.String ELM_NAME = "relDom:period";

	/** XML tag name for the <code>period</code> attribute. */
	private final static java.lang.String ELM_PERIOD = "relDom:period";

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
	 * <code>EPPRelatedDomainExtPeriod</code> default constructor. The period is
	 * initialized to <code>unspecified</code>. The period must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPRelatedDomainExtPeriod () {
		this.period = UNSPEC_PERIOD;
	}


	// End EPPRelatedDomainExtPeriod.EPPRelatedDomainExtPeriod()

	/**
	 * <code>EPPRelatedDomainExtPeriod</code> constructor that takes the domain
	 * period (in unit of year) as an argument
	 * 
	 * @param aPeriod
	 *        int
	 */
	public EPPRelatedDomainExtPeriod ( final int aPeriod ) {
		this.period = aPeriod;
		this.pUnit = "y";
	}


	// End EPPRelatedDomainExtPeriod.EPPRelatedDomainExtPeriod(int)

	/**
	 * <code>EPPRelatedDomainExtPeriod</code> constructor that takes the domain
	 * period and period unit as an arguments
	 * 
	 * @param aPUnit
	 *        String
	 * @param aPeriod
	 *        int
	 */
	public EPPRelatedDomainExtPeriod ( final String aPUnit, final int aPeriod ) {
		this.pUnit = aPUnit;

		if ( !this.pUnit.equals( PERIOD_UNIT_YEAR )
				&& !this.pUnit.equals( PERIOD_UNIT_MONTH ) ) {
			this.pUnit = PERIOD_UNIT_YEAR;
		}

		this.period = aPeriod;
	}


	// End EPPRelatedDomainExtPeriod.EPPRelatedDomainExtPeriod(String, int)

	/**
	 * Clone <code>EPPRelatedDomainExtPeriod</code>.
	 * 
	 * @return clone of <code>EPPRelatedDomainExtPeriod</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	@Override
	public Object clone () throws CloneNotSupportedException {
		EPPRelatedDomainExtPeriod clone = null;

		clone = (EPPRelatedDomainExtPeriod) super.clone();

		return clone;
	}
	// End EPPRelatedDomainExtPeriod.clone()

	/**
	 * Decode the EPPRelatedDomainExtPeriod attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *        - Root DOM Element to decode EPPRelatedDomainExtPeriod from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {
		// Period
		String tempVal = null;
		tempVal = aElement.getFirstChild().getNodeValue();
		this.pUnit = aElement.getAttribute( ELM_PERIOD_UNIT );

		if ( tempVal == null ) {
			this.period = UNSPEC_PERIOD;
		}
		else {
			this.period = Integer.parseInt( tempVal );
		}
	}
	// End EPPRelatedDomainExtPeriod.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPRelatedDomainExtPeriod instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return Element - Root DOM Element representing the
	 *         EPPRelatedDomainExtPeriod instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode EPPRelatedDomainExtPeriod instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		// Period with Attribute of Unit
		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		// Check the Period unit
		if ( this.pUnit == null ) {
			throw new EPPEncodeException(
					"EPPRelatedDomainExtPeriod: Period Unit should not be null" );
		}
		else {
			// if (!pUnit.equals(PERIOD_UNIT_YEAR) &&
			// !pUnit.equals(PERIOD_UNIT_MONTH))
			// throw new
			// EPPEncodeException("EPPRelatedDomainExtPeriod: Period Unit has an invalid value");
			// add attribute here
			root.setAttribute( ELM_PERIOD_UNIT, this.pUnit );

			// add value
			final Text currVal = aDocument.createTextNode( this.period + "" );

			// append child
			root.appendChild( currVal );
		}

		return root;
	}
	// End EPPRelatedDomainExtPeriod.encode(Document)

	/**
	 * implements a deep <code>EPPRelatedDomainExtPeriod</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPRelatedDomainExtPeriod</code> instance to compare with
	 * @return DOCUMENT ME!
	 */
	@Override
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPRelatedDomainExtPeriod) ) {
			return false;
		}

		final EPPRelatedDomainExtPeriod theComp =
				(EPPRelatedDomainExtPeriod) aObject;

		// period
		if ( this.period != theComp.period ) {
			return false;
		}

		// pUnit
		if ( !((this.pUnit == null) ? (theComp.pUnit == null) : this.pUnit
				.equals( theComp.pUnit )) ) {
			return false;
		}

		return true;
	}
	// End EPPRelatedDomainExtPeriod.equals(Object)

	/**
	 * Get domain period.
	 * 
	 * @return int
	 */
	public int getPeriod () {
		return this.period;
	}
	// End EPPRelatedDomainExtPeriod.getPeriod()

	/**
	 * Get domain period unit.
	 * 
	 * @return String
	 */
	public String getPUnit () {
		return this.pUnit;
	}
	// End EPPRelatedDomainExtPeriod.getPUnit()

	/**
	 * Test whether the period has been specfied: <code>true</code> is unspecified
	 * and <code>false</code> is specified.
	 * 
	 * @return boolean
	 */
	public boolean isPeriodUnspec () {
		if ( this.period == UNSPEC_PERIOD ) {
			return true;
		}
		else {
			return false;
		}
	}
	// End EPPRelatedDomainExtPeriod.isPeriodUnspec()

	/**
	 * Set domain period.
	 * 
	 * @param newPeriod
	 *        int
	 * @throws EPPCodecException
	 *         DOCUMENT ME!
	 */
	public void setPeriod ( final int newPeriod ) throws EPPCodecException {

		if ( newPeriod != UNSPEC_PERIOD ) {
			if ( (newPeriod < MIN_PERIOD) && (newPeriod > MAX_PERIOD) ) {
				throw new EPPCodecException( "period of " + newPeriod
						+ " is out of range, must be between " + MIN_PERIOD + " and "
						+ MAX_PERIOD );
			}
		}

		this.period = newPeriod;

	}
	// End EPPRelatedDomainExtPeriod.setPeriod(int)

	/**
	 * Set domain period of un. Creation date: (5/30/01 11:36:52 AM)
	 * 
	 * @param newPUnit
	 *        java.lang.String
	 */
	public void setPUnit ( final java.lang.String newPUnit ) {
		this.pUnit = newPUnit;

		if ( !this.pUnit.equals( PERIOD_UNIT_YEAR )
				&& !this.pUnit.equals( PERIOD_UNIT_MONTH ) ) {
			this.pUnit = PERIOD_UNIT_YEAR;
		}
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString( this );
	}
	// End EPPRelatedDomainExtPeriod.toString()
}
