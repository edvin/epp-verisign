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
import java.util.StringTokenizer;

// W3C Imports
// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Specifies the e-mail address to send the NameWatch reports and the frequency
 * of the reports. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPNameWatchRptTo implements EPPCodecComponent {
	/** Daily Report Frequency */
	public final static String RPTTO_FREQ_DAILY = "daily";

	/** Weekly Report Frequency */
	public final static String RPTTO_FREQ_WEEKLY = "weekly";

	/** Monthly Report Frequency */
	public final static String RPTTO_FREQ_MONTHLY = "monthly";

	/** XML Element Name of <code>EPPNameWatchRptTo</code> root element. */
	final static String ELM_NAME = "nameWatch:rptTo";

	/** XML tag name for the <code>rptTo</code> attribute. */
	private final static String ELM_RPTTO = "nameWatch:rptTo";

	/** XML attribute name for the <code>period</code> element. */
	private final static String ELM_RPTTO_FREQ = "freq";

	/** NameWatch Reply To E-mail Address */
	private String rptTo = null;

	/** NameWatch Report Frequency. */
	private String freqType = RPTTO_FREQ_DAILY;

	/**
	 * <code>EPPNameWatchRptTo</code> default constructor.  The period is
	 * initialized to <code>unspecified</code>.     The period must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPNameWatchRptTo() {
	}

	// End EPPNameWatchRptTo.EPPNameWatchRptTo()

	/**
	 * <code>EPPNameWatchRptTo</code> constructor that takes the nameWatch
	 * e-mail address to report to.  The frequency is set to
	 * <code>RPTTO_FREQ_DAILY</code>.
	 *
	 * @param aRptTo e-mail address to report to.
	 */
	public EPPNameWatchRptTo(String aRptTo) {
		rptTo		 = aRptTo;
		freqType     = RPTTO_FREQ_DAILY;
	}

	// End EPPNameWatchRptTo.EPPNameWatchRptTo(String)

	/**
	 * <code>EPPNameWatchRptTo</code> constructor that takes the nameWatch
	 * frequency and namewatch e-mail address to report to.
	 *
	 * @param aFreqType Report frequency, which should be one of the
	 * 		  <code>RPTTO_FREQ_</code> constant values.  If <code>null</code>,
	 * 		  it will be set to <code>RPTTO_FREQ_DAILY</code> by default.
	 * @param aRptTo E-mail address to report to.
	 */
	public EPPNameWatchRptTo(String aFreqType, String aRptTo) {
		freqType = aFreqType;

		if (
			(freqType == null)
				|| (
					!freqType.equals(RPTTO_FREQ_DAILY)
					&& !freqType.equals(RPTTO_FREQ_WEEKLY)
					&& !freqType.equals(RPTTO_FREQ_MONTHLY)
				)) {
			freqType = RPTTO_FREQ_DAILY;
		}

		rptTo = aRptTo;
	}

	// End EPPNameWatchRptTo.EPPNameWatchRptTo(String, int)

	/**
	 * Clone <code>EPPNameWatchRptTo</code>.
	 *
	 * @return clone of <code>EPPNameWatchRptTo</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchRptTo clone = null;

		clone = (EPPNameWatchRptTo) super.clone();

		return clone;
	}

	// End EPPNameWatchRptTo.clone()

	/**
	 * Decode the EPPNameWatchRptTo attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPNameWatchRptTo from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// RptTo
		String tempVal = null;
		tempVal		 = aElement.getFirstChild().getNodeValue();
		freqType     = aElement.getAttribute(ELM_RPTTO_FREQ);

		if (tempVal == null) {
			rptTo = null;
			throw new EPPDecodeException("EPPNameWatchRptTo: rptTo should not be null");
		}
		else {
			rptTo = tempVal;
		}
	}

	// End EPPNameWatchRptTo.doDecode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPNameWatchRptTo
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPNameWatchRptTo
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPNameWatchRptTo
	 * 			  instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// RptTo with Attribute of Unit
		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		// Check the RptTo unit
		if (freqType == null) {
			throw new EPPEncodeException("EPPNameWatchRptTo: RptTo freq should not be null");
		}
		else {
			// add attribute here
			root.setAttribute(ELM_RPTTO_FREQ, freqType);

			// add value
			Text currVal = aDocument.createTextNode(rptTo);

			// append child
			root.appendChild(currVal);
		}

		return root;
	}

	// End EPPNameWatchRptTo.encode(Document)

	/**
	 * implements a deep <code>EPPNameWatchRptTo</code> compare.
	 *
	 * @param aObject <code>EPPNameWatchRptTo</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchRptTo)) {
			return false;
		}

		EPPNameWatchRptTo theComp = (EPPNameWatchRptTo) aObject;

		// rptTo
		if (!(rptTo.equals(theComp.rptTo))) {
			return false;
		}

		// freqType
		if (
			!(
					(freqType == null) ? (theComp.freqType == null)
										   : freqType.equals(theComp.freqType)
				)) {
			return false;
		}

		return true;
	}

	// End EPPNameWatchRptTo.equals(Object)

	/**
	 * Gets the email address to report to.
	 *
	 * @return Report to e-mail address if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getRptTo() {
		return rptTo;
	}

	// End EPPNameWatchRptTo.getRptTo()

	/**
	 * Get nameWatch report frequency.
	 *
	 * @return Report frequency, which should be one of the
	 * 		   <code>RPTTO_FREQ_</code> constant values.
	 */
	public String getFreqType() {
		return freqType;
	}

	// End EPPNameWatchRptTo.getPUnit()

	/**
	 * Test whether the report to attribute is unspecified.
	 *
	 * @return <code>true</code> is unspecified and <code>false</code> is
	 * 		   specified.
	 */
	public boolean isRptToUnspec() {
		if (rptTo == null) {
			return true;
		}
		else {
			return false;
		}
	}

	// End EPPNameWatchRptTo.isRptToUnspec()

	/**
	 * Sets the namewatch e-mail address to report to.
	 *
	 * @param newRptTo e-mail address to report to
	 *
	 * @exception EPPCodecException Format error
	 */
	public void setRptTo(String newRptTo) throws EPPCodecException {
		int i = 0;

		if (rptTo != null) {
			StringTokenizer st = new StringTokenizer(newRptTo, "@");

			if ((i = (st.countTokens())) < 2) {
				throw new EPPCodecException("invalid reply to format");
			}
		}

		rptTo = newRptTo;
	}

	// End EPPNameWatchRptTo.setRptTo(String)

	/**
	 * Sets the frequency of the report.
	 *
	 * @param newFreqType Should be one of the <code>RPTTO_FREQ_</code>
	 * 		  constant values.
	 */
	public void setFreqType(String newFreqType) {
		freqType = newFreqType;

		if (
			!freqType.equals(RPTTO_FREQ_DAILY)
				&& !freqType.equals(RPTTO_FREQ_WEEKLY)
				&& !freqType.equals(RPTTO_FREQ_MONTHLY)) {
			freqType = RPTTO_FREQ_DAILY;
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

	// End EPPNameWatchRptTo.toString()
}
