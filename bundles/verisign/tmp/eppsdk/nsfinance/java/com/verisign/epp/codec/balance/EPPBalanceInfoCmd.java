/***********************************************************
 Copyright (C) 2011 VeriSign, Inc.

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

package com.verisign.epp.codec.balance;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPInfoCmd;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents an EPP Balance &lt;info&gt; command that is used to retrieve
 * account balance and other finance information defined in the
 * {@link EPPBalanceInfoResp}.
 */
public class EPPBalanceInfoCmd extends EPPInfoCmd {

	/** XML Element Name of {@link EPPBalanceInfoCmd} root element. */
	static final String ELM_NAME = "balance:info";

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPBalanceInfoCmd.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Default constructor
	 */
	public EPPBalanceInfoCmd() {
		// default constructor
	}

	/**
	 * This constructor calls the super <code>EPPInfoCmd(String)</code> method
	 * to set the transaction id for the command.
	 * 
	 * @param aTransId
	 *            Client Transaction Id associated with command.
	 */
	public EPPBalanceInfoCmd(String aTransId) {
		super(aTransId);
	}

	/**
	 * Clone {@link EPPBalanceInfoCmd}.
	 * 
	 * @return clone of {@link EPPBalanceInfoCmd}.
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Compare an instance of {@link EPPBalanceInfoCmd} with this instance.
	 */
	public boolean equals(Object o) {
		if ((o != null) && (o.getClass().equals(this.getClass()))) {
			if (!super.equals(o)) {
				cat.error("EPPBalanceInfoCmd.equals(): super class not equal");
				return false;
			}

			return true;
		}
		else {
			cat
					.error("EPPBalanceInfoCmd.equals(): not EPPBalanceInfoCmd instance");
			return false;
		}
	}

	/**
	 * Gets the EPP command Namespace associated with {@link EPPBalanceInfoCmd}.
	 * 
	 * @return <code>EPPBalanceMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPBalanceMapFactory.NS;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * {@link com.verisign.epp.codec.gen.EPPCodecComponent}.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Decode the {@link EPPBalanceInfoCmd} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@link EPPBalanceInfoCmd} from.
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Empty element
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@link EPPBalanceInfoCmd} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * @return Root DOM Element representing the {@link EPPBalanceInfoCmd}
	 *         instance.
	 * @exception EPPEncodeException
	 *                Unable to encode {@link EPPBalanceInfoCmd} instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {

		Element root = aDocument.createElementNS(EPPBalanceMapFactory.NS,
				ELM_NAME);
		root.setAttribute("xmlns:balance", EPPBalanceMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPBalanceMapFactory.NS_SCHEMA);

		// Empty element

		return root;
	}

}