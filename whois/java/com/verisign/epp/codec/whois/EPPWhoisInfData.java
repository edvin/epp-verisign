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
 * Extension to the domain info response to return 
 * additional information that is found in 
 * whois.  This will only be returned if the 
 * <code>EPPWhoisInf</code> extension is included 
 * in the domain info command with the flag value 
 * of <code>true</code>.  
 * 
 * @see com.verisign.epp.codec.whois.EPPWhoisInf
 */
public class EPPWhoisInfData implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPWhoisInfData.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the whois info extension tag
	 */
	public static final String ELM_NAME = EPPWhoisExtFactory.NS_PREFIX + ":whoisInfData";

	/**
	 * XML tag name for the registrar
	 */
	private static final String ELM_REGISTRAR = EPPWhoisExtFactory.NS_PREFIX + ":registrar";


	/**
	 * XML tag name for the whoisServer
	 */
	private static final String ELM_WHOIS_SERVER = EPPWhoisExtFactory.NS_PREFIX + ":whoisServer";
	
	/**
	 * XML tag name for the url
	 */
	private static final String ELM_URL = EPPWhoisExtFactory.NS_PREFIX + ":url";
	
	/**
	 * XML tag name for the irisServer
	 */
	private static final String ELM_IRIS_SERVER = EPPWhoisExtFactory.NS_PREFIX + ":irisServer";
	

	/**
	 * The sponsoring registrar name
	 */
	private String registrar = null;
	
	/**
	 * The spnsoring registrar whois server name 
	 */
	private String whoisServer = null;
	
	/**
	 * The sponsoring registrar referrel URL
	 */
	private String url = null;
	
	/**
	 * The sponsoring registrar IRIS server name.
	 */
	private String irisServer = null;

	/**
	 * Create an <code>EPPWhoisInfData</code>  instance
	 */
	public EPPWhoisInfData() {
	}
	
	/**
	 * Create a <code>EPPWhoisInfData</code> instance with all of the required 
	 * attributes.
	 * 
	 * @param aRegistrar Sponsoring Registrar name
	 */
	public EPPWhoisInfData(String aRegistrar) {
		this.registrar = aRegistrar;
	}
	
	
	
	/**
	 * Create a <code>EPPWhoisInfData</code> instance with the most common
	 * attributes
	 * 
	 * @param aRegistrar Sponsoring Registrar name
	 * @param aWhoisServer Sponsoring Registrar whois server name
	 * @param aURL Sponsoring Registrar referrel URL
	 */
	public EPPWhoisInfData(String aRegistrar, String aWhoisServer, String aURL) {
		this.registrar = aRegistrar;
		this.whoisServer = aWhoisServer;
		this.url = aURL;
	}
	

	/**
	 * Create a <code>EPPWhoisInfData</code> instance with all of the attributes.
	 * 
	 * @param aRegistrar Sponsoring Registrar name
	 * @param aWhoisServer Sponsoring Registrar whois server name
	 * @param aURL Sponsoring Registrar referrel URL
	 * @param aIrisServer Optional Sponsoring Registrar IRIS server
	 */
	public EPPWhoisInfData(String aRegistrar, String aWhoisServer, String aURL, String aIrisServer) {
		this(aRegistrar, aWhoisServer, aURL);
		this.irisServer = aIrisServer;
	}

	/**
	 * Clone <code>EPPWhoisInfData</code>.
	 * 
	 * @return clone of <code>EPPWhoisInfData</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPWhoisInfData clone = null;

		clone = (EPPWhoisInfData) super.clone();

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

		// Validate state 
		if (this.registrar == null) {
			throw new EPPEncodeException("EPPWhoisInfData.encode(): Required registrar attribute is null");			
		}
		
		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPWhoisInfData.encode(Document)");
		}

		Element root = aDocument.createElementNS(EPPWhoisExtFactory.NS,
				ELM_NAME);
		root.setAttribute("xmlns:whoisInf", EPPWhoisExtFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPWhoisExtFactory.NS_SCHEMA);

		// Registrar 
		EPPUtil.encodeString(aDocument, root, this.registrar, EPPWhoisExtFactory.NS, ELM_REGISTRAR);

		// Whois Server 
		EPPUtil.encodeString(aDocument, root, this.whoisServer, EPPWhoisExtFactory.NS, ELM_WHOIS_SERVER);

		// URL 
		EPPUtil.encodeString(aDocument, root, this.url, EPPWhoisExtFactory.NS, ELM_URL);
		
		// IRIS Server 
		EPPUtil.encodeString(aDocument, root, this.irisServer, EPPWhoisExtFactory.NS, ELM_IRIS_SERVER);
		
		return root;
	}

	/**
	 * Decode the EPPIdnLangExtCrete component
	 * 
	 * @param aElement
	 * @throws EPPDecodeException
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Registrar
		this.registrar = EPPUtil.decodeString(aElement, EPPWhoisExtFactory.NS,
				ELM_REGISTRAR);

		// Whois Server
		this.whoisServer = EPPUtil.decodeString(aElement, EPPWhoisExtFactory.NS,
				ELM_WHOIS_SERVER);
		
		// URL
		this.url = EPPUtil.decodeString(aElement, EPPWhoisExtFactory.NS,
				ELM_URL);
		
		// IRIS Server
		this.irisServer = EPPUtil.decodeString(aElement, EPPWhoisExtFactory.NS,
				ELM_IRIS_SERVER);
		
	}

	/**
	 * implements a deep <code>EPPWhoisInfData</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPWhoisInfData</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPWhoisInfData)) {
			return false;
		}

		EPPWhoisInfData theComp = (EPPWhoisInfData) aObject;

		// Registrar
		if (!((this.registrar == null) ? (theComp.registrar == null) : this.registrar
				.equals(theComp.registrar))) {
			cat.error("EPPWhoisInfData.equals(): registrar not equal");

			return false;
		}

		// Whois Server
		if (!((this.whoisServer == null) ? (theComp.whoisServer == null) : this.whoisServer
				.equals(theComp.whoisServer))) {
			cat.error("EPPWhoisInfData.equals(): whoisServer not equal");

			return false;
		}
		
		// URL
		if (!((this.url == null) ? (theComp.url == null) : this.url
				.equals(theComp.url))) {
			cat.error("EPPWhoisInfData.equals(): url not equal");

			return false;
		}
		
		// IRIS Server
		if (!((this.irisServer == null) ? (theComp.irisServer == null) : this.irisServer
				.equals(theComp.irisServer))) {
			cat.error("EPPWhoisInfData.equals(): irisServer not equal");

			return false;
		}
		
		
		return true;
	}

	/**
	 * Returns the registrar name
	 * 
	 * @return the registrar name if set;<code>null</code> otherwise
	 */
	public String getRegistrar() {
		return this.registrar;
	}

	/**
	 * Sets the registrar name
	 * 
	 * @param aRegistrar
	 *            Registrar full name
	 */
	public void setFlag(String aRegistrar) {
		this.registrar = aRegistrar;
	}

	
	/**
	 * Returns the registrar whois server name
	 * 
	 * @return the registrar whois server name if set;<code>null</code> otherwise
	 */
	public String getWhoisServer() {
		return this.whoisServer;
	}

	/**
	 * Sets the registrar whois server name
	 * 
	 * @param aWhoisServer
	 *            Registrar whois server name
	 */
	public void setWhoisServer(String aWhoisServer) {
		this.whoisServer = aWhoisServer;
	}
	
	
	/**
	 * Returns the registrar referral URL
	 * 
	 * @return the registrar referral URL if set;<code>null</code> otherwise
	 */
	public String getURL() {
		return this.url;
	}

	/**
	 * Sets the registrar referral URL
	 * 
	 * @param aURL
	 *            Registrar referral URL
	 */
	public void setURL(String aURL) {
		this.url = aURL;
	}
	
	
	/**
	 * Returns the optional registrar IRIS server name
	 * 
	 * @return the registrar IRIS server name if set;<code>null</code> otherwise
	 */
	public String getIrisServer() {
		return this.irisServer;
	}

	/**
	 * Sets the optional registrar IRIS server name
	 * 
	 * @param aIrisServer
	 *            Registrar IRIS server name
	 */
	public void setIrisServer(String aIrisServer) {
		this.irisServer = aIrisServer;
	}
	
	
}