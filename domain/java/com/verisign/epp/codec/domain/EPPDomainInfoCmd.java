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


// Log4j Imports
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPInfoCmd;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP Domain &lt;info&gt; command that is used to retrieve
 * information associated with  a domain.  The &lt;domain:info&gt; element
 * MUST     contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * for which information is requested.  An OPTIONAL "hosts" attribute is
 * available to control return of information describing hosts related to the
 * domain object. A value of "all" (the default, which MAY be absent) returns
 * information describing both subordinate and delegated hosts.  A value of
 * "del" returns information describing only delegated hosts.  A value of
 * "sub" returns information describing only subordinate hosts.  A value of
 * "none" returns no information describing delegated or subordinate hosts.
 * Use <code>getName</code> and <code>setName</code> to get and set the
 * element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPDomainInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPDomainInfoCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainInfoResp
 */
public class EPPDomainInfoCmd extends EPPInfoCmd {
	/**
	 * Constant on a call to <code>setHosts</code> to  get information on all
	 * hosts (delegated and subordinate). This is the default settings.
	 */
	public static final String HOSTS_ALL = "all";

	/**
	 * Constant on a call to <code>setHosts</code> to  get information on just
	 * the delegated hosts.
	 */
	public static final String HOSTS_DELEGATED = "del";

	/**
	 * Constant on a call to <code>setHosts</code> to  get information on just
	 * the subordinate hosts.
	 */
	public static final String HOSTS_SUBORDINATE = "sub";

	/**
	 * Constant on a call to <code>setHosts</code> to  get no information
	 * describing delegated or subordinate hosts.
	 */
	public static final String HOSTS_NONE = "none";

	/** XML Element Name of <code>EPPDomainInfoCmd</code> root element. */
	final static String ELM_NAME = "domain:info";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML attribute name for the <code>domain:name</code> attribute. */
	private final static String ATTR_HOSTS = "hosts";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPDomainInfoCmd.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Domain Name to get information on. */
	private String name;

	/** XML attribute value for the <code>hosts</code> attribute. */
	private String hosts = HOSTS_ALL;
	
	/** authorization information. */
	private EPPAuthInfo authInfo = null;


	/**
	 * <code>EPPDomainInfoCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDomainInfoCmd() {
		name = null;
	}


	/**
	 * <code>EPPDomainInfoCmd</code> constructor that takes the domain name as
	 * an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name to get information on.
	 */
	public EPPDomainInfoCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}


	
	/**
	 * <code>EPPDomainInfoCmd</code> constructor that takes the domain name as
	 * an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name to get information on.
	 */
	public EPPDomainInfoCmd(String aTransId, String aName, EPPAuthInfo aAuthInfo) {
		this(aTransId, aName);

		authInfo = aAuthInfo;
		authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
	}
	
	
	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainInfoCmd</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}


	/**
	 * Sets the desired level of host information.  The default is
	 * <code>HOSTS_ALL</code>.
	 *
	 * @param aHost Should be one of the <code>HOSTS_</code> constants.
	 */
	public void setHosts(String aHost) {
		hosts = aHost;
	}


	/**
	 * Sets the desired level of host information.
	 *
	 * @return Should be one of the <code>HOSTS_</code> constants.
	 */
	public String getHosts() {
		return this.hosts;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainInfoCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPDomainInfoCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainInfoCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("required attribute name is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

//		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		Element nameElm =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_DOMAIN_NAME);
		Text    nameVal = aDocument.createTextNode(name);

		nameElm.appendChild(nameVal);
		root.appendChild(nameElm);

		// Non-default hosts option specified?
		if (hosts != null) {
			nameElm.setAttribute(ATTR_HOSTS, hosts);
		}

		// Authorization Info
		if (authInfo != null) {
			EPPUtil.encodeComp(aDocument, root, authInfo);
		}
		
		return root;
	}


	/**
	 * Decode the <code>EPPDomainInfoCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPDomainInfoCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Domain Name
		this.name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Host
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPDomainMapFactory.NS, ELM_DOMAIN_NAME);

		if (currElm != null) {
			this.hosts = currElm.getAttribute(ATTR_HOSTS);
			if (this.hosts != null && this.hosts.length() == 0) {
				this.hosts = HOSTS_ALL;
			}
		}
		
		// Authorization Info
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDomainMapFactory.NS,
											 EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO,
											 EPPAuthInfo.class);
	}


	/**
	 * Gets the domain name to get information on.
	 *
	 * @return Domain Name    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the domain name to get information on.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}


	/**
	 * Get authorization information
	 *
	 * @return Authorization information if defined; <code>null</code>
	 * 		   otherwise;
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	
	
	/**
	 * Set authorization information
	 *
	 * @param aAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo aAuthInfo) {
		if (aAuthInfo != null) {
			authInfo = aAuthInfo;
			authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
		}
	}

	
	/**
	 * Compare an instance of <code>EPPDomainInfoCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainInfoCmd)) {
			cat.error("EPPDomainInfoCmd.equals(): "
					  + aObject.getClass().getName()
					  + " not EPPDomainInfoCmd instance");

			return false;
		}

		if (!super.equals(aObject)) {
			cat.error("EPPDomainInfoCmd.equals(): super class not equal");

			return false;
		}

		EPPDomainInfoCmd theComp = (EPPDomainInfoCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			cat.error("EPPDomainInfoCmd.equals(): name not equal");

			return false;
		}

		// Hosts
		if (
			!(
					(hosts == null) ? (theComp.hosts == null)
									   : hosts.equals(theComp.hosts)
				)) {
			cat.error("EPPDomainInfoCmd.equals(): hosts not equal");

			return false;
		}

		// Authorization Info
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			cat.error("EPPDomainInfoCmd.equals(): authInfo not equal");
			return false;
		}
		
		
		return true;
	}


	/**
	 * Clone <code>EPPDomainInfoCmd</code>.
	 *
	 * @return clone of <code>EPPDomainInfoCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainInfoCmd clone = (EPPDomainInfoCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		return clone;
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

}
