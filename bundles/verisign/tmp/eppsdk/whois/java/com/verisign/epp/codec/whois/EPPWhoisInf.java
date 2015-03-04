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

package com.verisign.epp.codec.whois;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Extension to the domain info command to specify whether 
 * or not the whois info response data defined 
 * in <code>EPPWhoisInfData</code> is desired.  There 
 * is a single flag attribute that specifies the 
 * preference.  A flag attribute value of <code>false</code>
 * has the same result of not including the 
 * <code>EPPWhoisInfo</code> extension.
 * 
 * @see com.verisign.epp.codec.whois.EPPWhoisInfData
 */
public class EPPWhoisInf implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPWhoisInf.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the whois info extension tag
	 */
	public static final String ELM_NAME = EPPWhoisExtFactory.NS_PREFIX + ":whoisInf";

	/**
	 * XML tag name for the flag
	 */
	private static final String ELM_FLAG = EPPWhoisExtFactory.NS_PREFIX + ":flag";

	/**
	 * whois info flag, where a <code>true</code> value will have the
	 * <code>EPPWhoisInfData</code> extension added to a successful response.
	 * A value of <code>false</code> will have the same result as not adding
	 * the <code>EPPWhoisInf</code> extension to the command.
	 */
	private Boolean flag = new Boolean(false);

	/**
	 * Create an EPPWhoisInf instance
	 */
	public EPPWhoisInf() {
	}
	
	/**
	 * Create a EPPWhoisInf intance with the flag value
	 * 
	 * @param aFlag
	 *            <code>true</code> to get the <code>EPPWhoisInfData</code>
	 *            extension in the response;<code>false</code> otherwise
	 */
	public EPPWhoisInf(boolean aFlag) {
		this.flag = new Boolean(aFlag);
	}
	

	/**
	 * Create a EPPWhoisInf intance with the flag value
	 * 
	 * @param aFlag
	 *            <code>true</code> to get the <code>EPPWhoisInfData</code>
	 *            extension in the response;<code>false</code> otherwise
	 */
	public EPPWhoisInf(Boolean aFlag) {
		this.flag = aFlag;
	}

	/**
	 * Clone <code>EPPWhoisInf</code>.
	 * 
	 * @return clone of <code>EPPWhoisInf</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPWhoisInf clone = null;

		clone = (EPPWhoisInf) super.clone();

		return clone;
	}

	/**
	 * Sets all this instance's data in the given XML document
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPWhoisInf.encode(Document)");
		}

		Element root = aDocument.createElementNS(EPPWhoisExtFactory.NS,
				ELM_NAME);
		root.setAttribute("xmlns:whoisInf", EPPWhoisExtFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPWhoisExtFactory.NS_SCHEMA);

		EPPUtil.encodeBoolean(aDocument, root, this.flag,
				EPPWhoisExtFactory.NS, ELM_FLAG);

		return root;
	}

	/**
	 * Decode the EPPIdnLangExtCrete component
	 * 
	 * @param aElement
	 * @throws EPPDecodeException
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		this.flag = EPPUtil.decodeBoolean(aElement, EPPWhoisExtFactory.NS,
				ELM_FLAG);

		if (this.flag == null) {
			throw new EPPDecodeException(
					"EPPWhoisInf.decode(): Could not find the " + ELM_FLAG
							+ " element");
		}
	}

	/**
	 * implements a deep <code>EPPWhoisInf</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPWhoisInf</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPWhoisInf)) {
			return false;
		}

		EPPWhoisInf theComp = (EPPWhoisInf) aObject;

		// Flag
		if (!((this.flag == null) ? (theComp.flag == null) : this.flag
				.equals(theComp.flag))) {
			cat.error("EPPWhoisInf.equals(): flag not equal");

			return false;
		}

		return true;
	}

	/**
	 * Returns the flag value
	 * 
	 * @return the flag value if set;<code>null</code> otherwise
	 */
	public Boolean getFlag() {
		return this.flag;
	}

	/**
	 * Sets the flag Code
	 * 
	 * @param aFlag
	 *            The flag value
	 */
	public void setFlag(Boolean aFlag) {
		this.flag = aFlag;
	}

}