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
package com.verisign.epp.codec.gen;


// Log4J Imports
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.verisign.epp.util.EPPCatFactory;


/**
 * The EPP &lt;login&gt; command is used to establish a session with an EPP
 * server in response to a greeting issued by the server.  A &lt;login&gt;
 * command MUST be sent to a server before any other EPP command to establish
 * an ongoing session.  A server operator MAY limit the number of failed login
 * attempts N, 1 &lt= N &lt= infinity, after which a login failure results in
 * the connection to the server (if a connection exists) being closed. <br>
 * A client identifier and initial password MUST be created on the server
 * before a client can successfully complete a &lt;login&gt; command.  The
 * client identifier and initial password MUST be delivered to the client
 * using an out-of-band method that protects the identifier and password from
 * inadvertent disclosure. <br>
 * In addition to the standard EPP command elements, the &lt;login&gt; command
 * contains the following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &ltclID&gt element that contains the client identifier assigned to the
 * client by the server.
 * </li>
 * <li>
 * A &ltpw&gt element that contains the client's plain text password.  The
 * value of this element is case sensitive.
 * </li>
 * <li>
 * An OPTIONAL &ltnewPW&gt element that contains a new plain text password to
 * be assigned to the client for use with subsequent &ltlogin&gt commands. The
 * value of this element is case sensitive.
 * </li>
 * </ul>
 * 
 * <br>
 * 
 * <ul>
 * <li>
 * An &ltoptions&gt element that contains the following child elements:
 * </li>
 * <li>
 * A &ltversion&gt element that contains the protocol version to be used for
 * the command or ongoing server session.
 * </li>
 * <li>
 * A &ltlang&gt element that contains the text response language to be used for
 * the command or ongoing server session commands.
 * </li>
 * </ul>
 * 
 * <br> The values of the &ltversion&gt and &ltlang&gt elements MUST exactly
 * match one of the values presented in the EPP greeting. <br>
 * 
 * <ul>
 * <li>
 * A &ltsvcs&gt element that contains one or more &ltobjURI&gt elements that
 * contain namespace URIs representing the objects to be managed during the
 * session.  The &ltsvcs&gt element MAY contain an OPTIONAL &ltsvcExtension&gt
 * element that contains one or more &ltextURI&gt elements that identify
 * object extensions to be used during the session.
 * </li>
 * </ul>
 * 
 * <br> The PLAIN SASL mechanism presented in [RFC2595] describes a format for
 * providing a user identifier, an authorization identifier, and a password as
 * part of a single plain text string.  The EPP authentication mechanism is
 * similar, though EPP does not require a session-level authorization
 * identifier and the user identifier and password are separated into distinct
 * XML elements.  Additional identification and authorization schemes MUST be
 * provided at other protocol layers to provide more robust security services.
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 */
public class EPPLoginCmd extends EPPCommand {
	/** XML root tag name for <code>EPPLoginCmd</code>. */
	final static String ELM_NAME = "login";

	/** XML tag name for the <code>clientId</code> attribute. */
	private final static String ELM_CLIENT_ID = "clID";

	/** XML tag name for the <code>password</code> attribute. */
	private final static String ELM_PASSWORD = "pw";

	/** XML tag name for the <code>newPassword</code> attribute. */
	private final static String ELM_NEW_PASSWORD = "newPW";

	/** XML tag name for the credential options. */
	private final static String ELM_OPTIONS = "options";

	/** XML tag name for the <code>services</code> attribute. */
	private final static String ELM_SERVICES = "svcs";

	/** XML tag name for the <code>services</code> attribute. */
	private final static String ELM_EXT_SERVICES = "svcExtension";

	/** XML tag name for the <code>extservices</code> attribute. */
	private final static String ELM_EXT_URI = "extURI";

	/** Class logger */
	private static final Logger cat =
		Logger.getLogger(
						 EPPLoginCmd.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Client login id */
	private String clientId = null;

	/** Client password */
	private String password = null;

	/** New client password */
	private String newPassword = null;

	/** Desired EPP protocol version */
	private String version = EPPCodec.VERSION;

	/** Desired language for error result messages. */
	private String lang = "en";

	/** Object Services desired by the client. */
	private Vector services;

	/**
	 * Extension (Protocol and Command-Response) Services desired by the
	 * client.
	 */
	private Vector extservices;

	/** XML tag name for the <code>version</code> attribute. */
	private final String ELM_VERSION = "version";

	/** XML tag name for the <code>lang</code> attribute. */
	private final String ELM_LANG = "lang";

	/**
	 * Allocates a new <code>EPPLoginCmd</code> with default attribute values.
	 * the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * transaction id is set to <code>null</code>.
	 * </li>
	 * <li>
	 * client id is set to <code>null</code>
	 * </li>
	 * <li>
	 * password is set to <code>null</code>
	 * </li>
	 * <li>
	 * new password is set to <code>null</code>
	 * </li>
	 * <li>
	 * services is initialized based on the <code>EPPFactory</code>
	 * configuration.
	 * </li>
	 * </ul>
	 * 
	 * <br>     The client id, password, and transaction id must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPLoginCmd() {
		services = EPPFactory.getInstance().getServices();
		this.extservices = EPPFactory.getInstance().getExtensions();
	}

	// End EPPLoginCmd.EPPLoginCmd()

	/**
	 * Allocates a new <code>EPPLoginCmd</code> with the required attributes.
	 * The     other attributes are initialized as follows:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * new password is set to <code>null</code>
	 * </li>
	 * <li>
	 * services is initialized based on the <code>EPPFactory</code>
	 * configuration.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aTransId transaction id of the command.
	 * @param aClientId Client login id
	 * @param aPassword Client password
	 */
	public EPPLoginCmd(String aTransId, String aClientId, String aPassword) {
		super(aTransId);

		services     = EPPFactory.getInstance().getServices();
		this.extservices = EPPFactory.getInstance().getExtensions();
		clientId     = aClientId;
		password     = aPassword;
	}

	// End EPPLoginCmd.EPPLoginCmd(String, String, String)

	/**
	 * Allocates a new <code>EPPLoginCmd</code> with the required attributes
	 * and the     optional new password attribute.  The services is
	 * initialized based on the     <code>EPPFactory</code> configuration.
	 *
	 * @param aTransId transaction id of the command.
	 * @param aClientId Client login id
	 * @param aPassword Client password
	 * @param aNewPassword New client password
	 */
	public EPPLoginCmd(
					   String aTransId, String aClientId, String aPassword,
					   String aNewPassword) {
		super(aTransId);

		services	    = EPPFactory.getInstance().getServices();
		this.extservices = EPPFactory.getInstance().getExtensions();
		clientId	    = aClientId;
		password	    = aPassword;
		newPassword     = aNewPassword;
	}

	// End EPPLoginCmd.EPPLoginCmd(String, String, String, String)

	/**
	 * Gets the EPP command Namespace associated with <code>EPPLoginCmd</code>.
	 *
	 * @return EPPCodec.NS
	 */
	public String getNamespace() {
		return EPPCodec.NS;
	}

	// End EPPLoginCmd.getNamespace()

	/**
	 * Gets the EPP command type associated with <code>EPPLoginCmd</code>.
	 *
	 * @return <code>EPPCommand.TYPE_LOGIN</code>
	 */
	public String getType() {
		return EPPCommand.TYPE_LOGIN;
	}

	// End EPPLoginCmd.getType()

	/**
	 * Gets the client login identifier.
	 *
	 * @return Client login identifier if defined; <code>null</code> otherwise.
	 */
	public String getClientId() {
		return clientId;
	}

	// End EPPLoginCmd.getClientId()

	/**
	 * Sets the client login identifier.
	 *
	 * @param aClientId Client login identifier.
	 */
	public void setClientId(String aClientId) {
		clientId = aClientId;
	}

	// End EPPLoginCmd.setClientId(String)

	/**
	 * Gets the client password.
	 *
	 * @return Client password if defined; <code>null</code> otherwise.
	 */
	public String getPassword() {
		return password;
	}

	// End EPPLoginCmd.getPassword()

	/**
	 * Sets the client password.
	 *
	 * @param aPassword Client password.
	 */
	public void setPassword(String aPassword) {
		password = aPassword;
	}

	// End EPPLoginCmd.setPassword(String)

	/**
	 * Gets the new client password.
	 *
	 * @return New client password if defined; <code>null</code> otherwise.
	 */
	public String getNewPassword() {
		return newPassword;
	}

	// End EPPLoginCmd.getNewPassword()

	/**
	 * Sets the new client password.
	 *
	 * @param aNewPassword New client password.
	 */
	public void setNewPassword(String aNewPassword) {
		newPassword = aNewPassword;
	}

	// End EPPLoginCmd.setNewPassword(String)

	/**
	 * Is a new password defined?
	 *
	 * @return <code>true</code> if the new password is defined;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasNewPassword() {
		if (newPassword != null) {
			return true;
		}
		else {
			return false;
		}
	}

	// End EPPLoginCmd.hasNewPassword()

	/**
	 * Gets the desired EPP version. The default version is set to
	 * <code>EPPCodec.VERSION</code>.
	 *
	 * @return EPP version identifier if defined; <code>null</code> otherwise.
	 */
	public String getVersion() {
		return version;
	}

	// End EPPLoginCmd.getVersion()

	/**
	 * Sets the desired EPP version.  The default version is set to
	 * <code>EPPCodec.VERSION</code>.
	 *
	 * @param aVersion EPP version identifier
	 */
	public void setVersion(String aVersion) {
		version = aVersion;
	}

	// End EPPLoginCmd.setVersion(String)

	/**
	 * Gets the desired EPP language.  The EPP language determines the language
	 * of the error description strings and should be one of the supported
	 * languages of the <code>EPPGreeting</code>.  The default language is
	 * "en".
	 *
	 * @return The desired EPP language if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getLang() {
		return lang;
	}

	// End EPPLoginCmd.getLang()

	/**
	 * Sets the desired EPP language.  The EPP language determines the language
	 * of the error description strings and should be one of the supported
	 * languages of the <code>EPPGreeting</code>.  The default language is
	 * "en".
	 *
	 * @param aLang The desired EPP language
	 */
	public void setLang(String aLang) {
		lang = aLang;
	}

	// End EPPLoginCmd.setLang(String)

	/**
	 * Gets the login services.
	 *
	 * @return <code>Vector</code> of <code>EPPService</code> instances
	 */
	public Vector getServices() {
		return services;
	}

	// End EPPLoginCmd.getServices()

	/**
	 * Sets the login services.  The default services are retrieved from
	 * <code>EPPFactory.getServices</code>.
	 *
	 * @param someServices <code>Vector</code> of desired
	 * 		  <code>EPPService</code> instances
	 */
	public void setServices(Vector someServices) {
		services = someServices;
	}

	// End EPPLoginCmd.setServices(Vector)

	/**
	 * Gets the list of supported/desired extension services.  An EPP Client
	 * will retrieve the list of extension services supported by the EPP
	 * Server.  An     EPP Server will retrieve the list of extension services
	 * desired by the EPP Client.
	 *
	 * @return Vector of <code>EPPService</code> instances.
	 */
	public Vector getExtensionServices() {
		return extservices;
	}

	// End EPPServiceMenu.getExtensionServices()

	/**
	 * Sets the list of supported/desired extension services.  An EPP Client
	 * will set the list of extension services desired.  An     EPP Server
	 * will set the list of supported extension services.
	 *
	 * @param someExtServices Vector of <code>EPPService</code> instances.
	 */
	public void setExtensionServices(Vector someExtServices) {
		extservices = someExtServices;
	}

	// End EPPServiceMenu.setExtensionServices(Vector)

	/**
	 * encode <code>EPPLoginCmd</code> into a DOM element tree.  The
	 * &ltlogin&gt element     is created and the attribute nodes are appended
	 * as children.  This method is part     of the Template Design Pattern,
	 * where <code>EPPCommand</code> provides the public <code>encode</code>
	 * and calls the abstract <code>doGenEncode</code>.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &ltlogin&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	protected Element doGenEncode(Document aDocument) throws EPPEncodeException {
		//login
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Client Id
		EPPUtil.encodeString(
							 aDocument, root, clientId, EPPCodec.NS,
							 ELM_CLIENT_ID);

		// Password
		EPPUtil.encodeString(
							 aDocument, root, password, EPPCodec.NS,
							 ELM_PASSWORD);

		// New Password
		EPPUtil.encodeString(
							 aDocument, root, newPassword, EPPCodec.NS,
							 ELM_NEW_PASSWORD);

		// Options
		Element optionsElm =
			aDocument.createElementNS(EPPCodec.NS, ELM_OPTIONS);
		root.appendChild(optionsElm);

		// Version
		EPPUtil.encodeString(
							 aDocument, optionsElm, version, EPPCodec.NS,
							 ELM_VERSION);

		// Language
		EPPUtil.encodeString(
							 aDocument, optionsElm, lang, EPPCodec.NS, ELM_LANG);

		// Services svcs element
		Element servicesElm =
			aDocument.createElementNS(EPPCodec.NS, ELM_SERVICES);
		root.appendChild(servicesElm);
		EPPUtil.encodeCompVector(aDocument, servicesElm, services);

		if ((extservices != null) && extservices.elements().hasMoreElements()) {
			//svcExtension element
			Element svcExtension = aDocument.createElementNS(EPPCodec.NS, ELM_EXT_SERVICES);

			EPPUtil.encodeCompVector(aDocument, svcExtension, extservices);
			servicesElm.appendChild(svcExtension);
		}

		root.appendChild(servicesElm);

		return root;
	}

	// End EPPLoginCmd.doGenEncode(Document)

	/**
	 * decode <code>EPPLoginCmd</code> from a DOM element tree.  The "login"
	 * element needs to be the value of the <code>aElement</code> argument.
	 * This method is part     of the Template Design Pattern, where
	 * <code>EPPCommand</code> provides the public     <code>decode</code> and
	 * calls the abstract <code>doGenDecode</code>.
	 *
	 * @param aElement &ltlogin&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	protected void doGenDecode(Element aElement) throws EPPDecodeException {
		// Client Id
		clientId     = EPPUtil.decodeString(
											aElement, EPPCodec.NS, ELM_CLIENT_ID);

		// Password
		password     = EPPUtil.decodeString(
											aElement, EPPCodec.NS, ELM_PASSWORD);

		// New Password
		newPassword =
			EPPUtil.decodeString(aElement, EPPCodec.NS, ELM_NEW_PASSWORD);

		// Options
		NodeList elms = aElement.getElementsByTagNameNS(EPPCodec.NS, ELM_OPTIONS);

		if (elms.getLength() == 1) {
			Element optionsElm = (Element) elms.item(0);

			// Version
			version =
				EPPUtil.decodeString(optionsElm, EPPCodec.NS, ELM_VERSION);

			// Language
			lang = EPPUtil.decodeString(optionsElm, EPPCodec.NS, ELM_LANG);
		}
		else {
			version     = null;
			lang	    = null;
		}

		//-- Services
		// Default to empty services Vector
		services     = new Vector();

		// Default to empty extension services Vector
		extservices     = new Vector();

		// Get Services root node
		Element servicesElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPCodec.NS, ELM_SERVICES);

		if (servicesElm == null) {
			throw new EPPDecodeException("EPPLoginCmd.doGenDecode did not find a "
										 + ELM_SERVICES + " element");
		}

		// Get the list of services
		NodeList serviceElms = servicesElm.getElementsByTagNameNS(EPPCodec.NS,
				EPPUtil.getLocalName(EPPService.ELM_OBJ_URI));

		for (int i = 0; i < serviceElms.getLength(); i++) {
			EPPService currService = new EPPService();
			currService.setServiceType(EPPService.OBJ_SERVICE);

			currService.decode((Element) serviceElms.item(i));

			services.addElement(currService);
		}

		NodeList svcExtensionNodeList = aElement.getElementsByTagNameNS(
				EPPCodec.NS, EPPUtil.getLocalName(ELM_EXT_SERVICES));

		if (svcExtensionNodeList.getLength() != 0) {
			Element svcExtension = null;

			for (int i = 0; i < svcExtensionNodeList.getLength(); i++) {
				svcExtension = (Element) svcExtensionNodeList.item(i);

				NodeList extensionElms = svcExtension.getElementsByTagNameNS(
						EPPCodec.NS, EPPUtil.getLocalName(ELM_EXT_URI));

				for (int k = 0; k < extensionElms.getLength(); k++) {
					EPPService extService = new EPPService();
					extService.setServiceType(EPPService.EXT_SERVICE);
					extService.decode((Element) extensionElms.item(k));
					extservices.addElement(extService);
				}

				//end inner for loop
			}

			//end outer for loop
		}

		//end if
	}

	// End EPPLoginCmd.doGenDecode(Element)

	/**
	 * implements a deep <code>EPPLoginCmd</code> compare.
	 *
	 * @param aObject <code>EPPLoginCmd</code> instance to compare with
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPLoginCmd)) {
			return false;
		}

		EPPLoginCmd theLogin = (EPPLoginCmd) aObject;

		// EPPCommand superclass.
		if (!super.equals(aObject)) {
			return false;
		}

		// clientId
		if (
			!(
					(clientId == null) ? (theLogin.clientId == null)
										   : clientId.equals(theLogin.clientId)
				)) {
			return false;
		}

		// password
		if (
			!(
					(password == null) ? (theLogin.password == null)
										   : password.equals(theLogin.password)
				)) {
			return false;
		}

		// newPassword
		if (
			!(
					(newPassword == null) ? (theLogin.newPassword == null)
											  : newPassword.equals(theLogin.newPassword)
				)) {
			return false;
		}

		// version
		if (
			!(
					(version == null) ? (theLogin.version == null)
										  : version.equals(theLogin.version)
				)) {
			return false;
		}

		// lang
		if (
			!(
					(lang == null) ? (theLogin.lang == null)
									   : lang.equals(theLogin.lang)
				)) {
			return false;
		}

		// services
		if (!EPPUtil.equalVectors(services, theLogin.services)) {
			return false;
		}

		return true;
	}

	// End EPPLoginCmd.equals(Object)

	/**
	 * Clone <code>EPPLoginCmd</code>.
	 *
	 * @return clone of <code>EPPLoginCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPLoginCmd clone = null;

		clone     = (EPPLoginCmd) super.clone();

		// Services
		clone.services = (Vector) services.clone();

		for (int i = 0; i < services.size(); i++)
			clone.services.setElementAt(
										((EPPService) services.elementAt(i))
										.clone(), i);

		return clone;
	}

	// End EPPLoginCmd.clone()

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

	// End EPPLoginCmd.toString()

	/**
	 * Is the <code>EPPLoginCmd</code> services settings valid as compared with
	 * the     services specified in the <code>EPPGreeting</code>?  The
	 * services attributes defined     in <code>EPPLoginCmd</code> must be a
	 * subset of the available services specified in     the
	 * <code>EPPGreeting</code>.
	 *
	 * @param aGreeting Greeting to compare services with
	 *
	 * @return <code>true</code> if the service settings are valid;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean isValidServices(EPPGreeting aGreeting) {
		cat.debug("isValidServices(): enter");

		EPPServiceMenu greetingServices = aGreeting.getServiceMenu();

		// Language
		if ((lang == null) || (!greetingServices.getLangs().contains(lang))) {
			cat.error("lang mismatch " + lang + " not in "
					  + greetingServices.getLangs());

			return false;
		}

		// Version
		if (
			(version == null)
				|| (!greetingServices.getVersions().contains(version))) {
			cat.error("version mismatch " + version + " not in "
					  + greetingServices.getVersions());

			return false;
		}

		// Services
		if (
			!EPPUtil.vectorSubset(
									  services,
										  greetingServices.getObjectServices())) {
			cat.debug("services mismatch " + services + " not in "
					  + greetingServices.getObjectServices());

			return false;
		}

		cat.debug("isValidServices(): exit (true)");

		return true;
	} // End EPPLoginCmd.isValidServices(EPPGreeting)
	
	/**
	 * Merge the services defined automatically in the EPP SDK configuration
	 * with services and extension services defined in the EPP Greeting, so
	 * that the login services are sent to only be a subset of the services
	 * defined in the EPP Greeting.
	 * 
	 * @param aGreeting EPP Greeting to merge the services in the EPP Login.
	 */
	public void mergeServicesAndExtensionServices(EPPGreeting aGreeting) {
		this.services.retainAll(aGreeting.getServiceMenu().getObjectServices());
		this.extservices.retainAll(aGreeting.getServiceMenu().getExtensionServices());		
	}
}


// End class EPPLoginCmd
