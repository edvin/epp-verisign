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
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCreateCmd;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP Domain &lt;create&gt; command, which provides a transform
 * operation that allows a client to create a domain object. In addition to
 * the standard EPP command elements, the &lt;create&gt; command MUST contain
 * a &lt;domain:create&gt; element that identifies the domain namespace and
 * the location of the domain schema. The &lt;domain:create&gt; element MUST
 * contain the following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * of the object to be created. Use <code>getName</code> and
 * <code>setName</code> to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:period&gt; element that contains the initial
 * registration period of the domain object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * <li>
 * Zero or more &lt;domain:ns&gt; elements that contain the fully qualified
 * host name of a known host object to provide resolution services for the
 * domain. A host object MUST be known to the server before a domain can be
 * delegated to the host. A server MUST provide host object services to
 * provide domain name services. The EPP mapping for host objects is described
 * in [EPP-H]. Use <code>getServers</code> and <code>setServers</code> to get
 * and set the elements.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:registrant&gt; element that contains the identifier
 * for the human or organizational social information (contact) object to be
 * associated with the domain object as the object registrant. This object
 * identifier MUST be known to the server before the contact object can be
 * associated with the domain object. Use <code>getRegistrant</code> and
 * <code>setRegistrant</code> to get and set the elements.
 * </li>
 * <li>
 * Zero or more &lt;domain:contact&gt; elements that contain the registrant,
 * administrative, technical, and billing contact identifiers to be associated
 * with the domain. A contact identifier MUST be known to the server before
 * the contact can be associated with the domain. Only one contact identifier
 * of each type MAY be specified. A server MAY provide contact object services
 * when providing domain name object services. The EPP mapping for contact
 * objects is described in [EPP-C]. Use <code>getContacts</code> and
 * <code>setContacts</code> to get and set the elements. Contacts should only
 * be specified if the Contact Mapping is supported.
 * </li>
 * <li>
 * A &lt;domain:authInfo&gt; element that contains authorization information to
 * be associated with the domain object.
 * </li>
 * </ul>
 * 
 * <br>It is important to note that the transaction identifier associated with
 * successful creation of a domain object becomes the authorization identifier
 * required to transfer sponsorship of the domain object. A client MUST retain
 * all transaction identifiers associated with domain object creation and
 * protect them from disclosure. A client MUST also provide a copy of the
 * transaction identifier information to the domain registrant, who will need
 * this information to request a domain transfer through a different client. <br>
 * <br>
 * <code>EPPDomainCreateResp</code> is the concrete <code>EPPReponse</code>
 * associated with <code>EPPDomainCreateCmd</code>.<br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainCreateResp
 */
public class EPPDomainCreateCmd extends EPPCreateCmd {
	/** XML Element Name of <code>EPPDomainCreateCmd</code> root element. */
	final static String ELM_NAME = "domain:create";

	/** XML Element Name of <code>EPPDomainCreateCmd</code> root element. */
	final static String ELM_REGISTRANT = "domain:registrant";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML tag name for the <code>servers</code> attribute. */
	private final static String ELM_SERVER = "domain:ns";

	/** XML tag name for host object reference */
	private final static String ELM_HOST_OBJ = "domain:hostObj";

	/** XML tag name for host attribute */
	private final static String ELM_HOST_ATTR = EPPHostAttr.ELM_NAME;

	/** XML tag name for the <code>contacts</code> attribute. */
	private final static String ELM_CONTACT = "domain:contact";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPDomainCreateCmd.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Domain Name of domain to create. */
	private String name = null;

	/** Registration Period. */
	private EPPDomainPeriod period = null;

	/** Domain Name Servers */
	private Vector servers = null;

	/** Domain Contacts */
	private Vector contacts = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/** registrant. */
	private java.lang.String registrant = null;

	/**
	 * Allocates a new <code>EPPDomainCreateCmd</code> with default attribute
	 * values. the defaults include the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>UNSPEC_PERIOD</code>
	 * </li>
	 * <li>
	 * servers is set to to <code>null</code>
	 * </li>
	 * <li>
	 * contacts is set to to <code>null</code>
	 * </li>
	 * <li>
	 * transaction id is set to <code>null</code>.
	 * </li>
	 * </ul>
	 * 
	 * <br>The name must be set before invoking <code>encode</code>.
	 */
	public EPPDomainCreateCmd() {
		name		 = null;
		period		 = null;
		servers		 = null;
		contacts     = null;
	}

	// End EPPDomainCreateCmd.EPPDomainCreateCmd()

	/**
	 * Allocates a new <code>EPPDomainCreateCmd</code> with a domain name. The
	 * other attributes are initialized as follows: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * period is set to <code>UNSPEC_PERIOD</code>
	 * </li>
	 * <li>
	 * servers is set to <code>null</code>
	 * </li>
	 * <li>
	 * contacts is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name
	 * @param aAuthInfo EPPAuthInfo authorization information
	 */
	public EPPDomainCreateCmd(
							  String aTransId, String aName,
							  EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		 = aName;
		authInfo     = aAuthInfo;
		authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
		period		 = null;
		servers		 = null;
		contacts     = null;
	}

	// End EPPDomainCreateCmd.EPPDomainCreateCmd(String, String, EPPAuthInfo)

	/**
	 * Allocates a new <code>EPPDomainCreateCmd</code> with all attributes
	 * specified by the arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName Domain name
	 * @param someServers Domain name servers
	 * @param someContacts Domain contacts. Should be <code>null</code> if the
	 * 		  Contact Mapping is not supported.
	 * @param aPeriod Value greater than or equal to <code>MIN_PERIOD</code> or
	 * 		  less than or equal to <code>MAX_PERIOD</code>.
	 * @param aAuthInfo EPPAuthInfo authorization information
	 */
	public EPPDomainCreateCmd(
							  String aTransId, String aName, Vector someServers,
							  Vector someContacts, EPPDomainPeriod aPeriod,
							  EPPAuthInfo aAuthInfo) {
		super(aTransId);

		name		 = aName;
		period		 = aPeriod;
		servers		 = someServers;
		contacts     = someContacts;
		authInfo     = aAuthInfo;
		authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
	}

	// End EPPDomainCreateCmd.EPPDomainCreateCmd(String, String, Vector, Vector, EPPDomainPeriod, EPPAuthInfo)

	/**
	 * Get the EPP command Namespace associated with EPPDomainCreateCmd.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainCreateCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPDomainCreateCmd</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error.  throws EPPCodecException State error. This
	 * will contain the name of the attribute that is not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		//Domain name
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		//AuthInfo
		if (authInfo == null) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPDomainCreateCmd.isValid()

	/**
	 * Get the domain name to create.
	 *
	 * @return Domain Name
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainCreateCmd.getName()

	/**
	 * Set the domain name to create.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainCreateCmd.setName(String)

	/**
	 * Gets the name servers. The name servers can either be
	 * <code>String</code> instances containing the fully qualified name of a
	 * known name server host object, or <code>EPPHostAttr</code> instances
	 * containing the fully qualified name of a host and optionally the host
	 * IP addresses.
	 *
	 * @return <code>Vector</code> of name server <code>String</code> instances
	 * 		   for host object references or <code>EPPHostAttr</code>
	 * 		   instances for host attribute values if exists;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getServers() {
		return servers;
	}

	// End EPPDomainCreateCmd.getServers()

	/**
	 * Sets the name servers. The name servers can either be
	 * <code>String</code> instances containing the fully qualified name of a
	 * known name server host object, or <code>EPPHostAttr</code> instances
	 * containing the fully qualified name of a host and optionally the host
	 * IP addresses.
	 *
	 * @param aServers <code>Vector</code> of name server <code>String</code>
	 * 		  instances for host object references or <code>EPPHostAttr</code>
	 * 		  instances for host attribute values.
	 */
	public void setServers(Vector aServers) {
		servers = aServers;
	}

	// End EPPDomainCreateCmd.setServers(Vector)

	/**
	 * Gets the contacts.
	 *
	 * @return Vector of <code>EPPDomainContact</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getContacts() {
		return contacts;
	}

	// End EPPDomainCreateCmd.getContacts()

	/**
	 * Sets the contacts.
	 *
	 * @param aContacts DOCUMENT ME!
	 */
	public void setContacts(Vector aContacts) {
		contacts = aContacts;
	}

	// End EPPDomainCreateCmd.setContacts(Vector)

	/**
	 * Compare an instance of <code>EPPDomainCreateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainCreateCmd)) {
			cat.error("EPPDomainCreateCmd.equals(): "
					  + aObject.getClass().getName()
					  + " not EPPDomainCreateCmd instance");

			return false;
		}

		if (!super.equals(aObject)) {
			cat.error("EPPDomainCreateCmd.equals(): super class not equal");

			return false;
		}

		EPPDomainCreateCmd theComp = (EPPDomainCreateCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			cat.error("EPPDomainCreateCmd.equals(): name not equal");

			return false;
		}

		// Period
		if (
			!(
					(period == null) ? (theComp.period == null)
										 : period.equals(theComp.period)
				)) {
			cat.error("EPPDomainCreateCmd.equals(): period not equal");

			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			cat.error("EPPDomainCreateCmd.equals(): authInfo not equal");

			return false;
		}

		// Domain Name Server
		if (!EPPUtil.equalVectors(servers, theComp.servers)) {
			cat.error("EPPDomainCreateCmd.equals(): servers not equal");

			return false;
		}

		// Domain Contacts
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			if (!EPPUtil.equalVectors(contacts, theComp.contacts)) {
				cat.error("EPPDomainCreateCmd.equals(): contacts not equal");

				return false;
			}
		}

		// registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
				)) {
			cat.error("EPPDomainCreateCmd.equals(): registrant not equal");

			return false;
		}

		return true;
	}

	// End EPPDomainCreateCmd.equals(Object)

	/**
	 * Clone <code>EPPDomainCreateCmd</code>.
	 *
	 * @return clone of <code>EPPDomainCreateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainCreateCmd clone = (EPPDomainCreateCmd) super.clone();

		if (servers != null) {
			clone.servers = (Vector) servers.clone();
		}

		if (contacts != null) {
			clone.contacts = (Vector) contacts.clone();

			for (int i = 0; i < contacts.size(); i++)
				clone.contacts.setElementAt(
											((EPPDomainContact) contacts
											 .elementAt(i)).clone(), i);
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPDomainPeriod) period.clone();
		}

		return clone;
	}

	// End EPPDomainCreateCmd.clone()

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

	// End EPPDomainCreateCmd.toString()

	/**
	 * Get authorization information
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDomainCreateCmd.getAuthInfo()

	/**
	 * Gets the registration period in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPDomainPeriod getPeriod() {
		return period;
	}

	// End EPPDomainCreateCmd.getPeriod()

	/**
	 * Get registrant.
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getRegistrant() {
		return registrant;
	}

	// End EPPDomainCreateCmd.getRegistrant()

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo java.lang.String
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDomainMapFactory.NS, EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO);
		}
	}

	// End EPPDomainCreateCmd.setAuthInfo(EPPAuthInfo)

	/**
	 * Sets the registration period in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPDomainPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPDomainCreateCmd.setPeriod(EPPDomainPeriod)

	/**
	 * Set registrant.
	 *
	 * @param newRegistrant java.lang.String
	 */
	public void setRegistrant(java.lang.String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPDomainCreateCmd.setPeriod(EPPDomainPeriod)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPDomainCreateCmd
	 * instance.
	 *
	 * @param aDocument DOM Document that is being built. Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPDomainCreateCmd instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPDomainCreateCmd
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			//Validate States
			validateState();
		}
		 catch (EPPCodecException e) {
			cat.error("EPPDomainCreateCmd.doEncode(): Invalid state on encode: "
					  + e);
			throw new EPPEncodeException("EPPDomainCreateCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);

		// Period with Attribute of Unit
		if ((period != null) && !period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// Domain Name Servers
		if ((this.servers != null) && (this.servers.size() > 0)) {
			Element theServersElm =
				aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_SERVER);
			root.appendChild(theServersElm);

			Object theNS = this.servers.get(0);

			// Name Server Host objects?
			if (theNS instanceof String) {
				EPPUtil.encodeVector(
									 aDocument, theServersElm, this.servers,
									 EPPDomainMapFactory.NS, ELM_HOST_OBJ);
			}

			// Name Server Host attributes?
			else if (theNS instanceof EPPHostAttr) {
				EPPUtil.encodeCompVector(
										 aDocument, theServersElm, this.servers);
			}
			else {
				throw new EPPEncodeException("EPPDomainCreateCmd.encode: Invalid NS server class "
											 + theNS.getClass().getName());
			}
		}

		// end if (this.servers != null) && (this.servers.size()) > 0)
		// Registrant
		if (registrant != null) {
			EPPUtil.encodeString(
								 aDocument, root, registrant,
								 EPPDomainMapFactory.NS, ELM_REGISTRANT);
		}

		// Contacts
		if (contacts != null) {
			if (
				EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
				EPPUtil.encodeCompVector(aDocument, root, contacts);
			}
			else {
				throw new EPPEncodeException("Contacts specified when the Contact Mapping is not supported");
			}
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPDomainCreateCmd.doEncode(Document)

	/**
	 * Decode the EPPDomainCreateCmd attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement Root DOM Element to decode EPPDomainCreateCmd from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Domain Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Period
		period =
			(EPPDomainPeriod) EPPUtil.decodeComp(
												 aElement,
												 EPPDomainMapFactory.NS,
												 EPPDomainPeriod.ELM_NAME,
												 EPPDomainPeriod.class);

		// Servers
		Element theServersElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPDomainMapFactory.NS, ELM_SERVER);

		if (theServersElm != null) {
			Element theServerElm = EPPUtil.getFirstElementChild(theServersElm);

			if (theServerElm != null) {
				if (theServerElm.getLocalName().equals(
						EPPUtil.getLocalName(ELM_HOST_OBJ))) {
					servers = EPPUtil.decodeVector(theServersElm,
							EPPDomainMapFactory.NS, ELM_HOST_OBJ);
				}
				else if (theServerElm.getLocalName().equals(
						EPPUtil.getLocalName(ELM_HOST_ATTR))) {
					servers = EPPUtil.decodeCompVector(theServersElm,
							EPPDomainMapFactory.NS, ELM_HOST_ATTR,
							EPPHostAttr.class);
				}
				else {
					throw new EPPDecodeException(
							"EPPDomainCreateCmd.doDecode: Invalid host child element "
									+ theServersElm.getLocalName());
				}

				if (servers.size() == 0) {
					servers = null;
				}
			}

			// end if (theServerElm != null) 
		}

		// end if (theServersElm != null)
		// Registrant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_REGISTRANT);

		// Contacts
		contacts =
			EPPUtil.decodeCompVector(
									 aElement, EPPDomainMapFactory.NS,
									 ELM_CONTACT, EPPDomainContact.class);

		if (contacts.size() == 0) {
			contacts = null;
		}

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDomainMapFactory.NS,
											 EPPDomainMapFactory.ELM_DOMAIN_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPDomainCreateCmd.doDecode(Element)
}
