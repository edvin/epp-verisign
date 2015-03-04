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

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;


/**
 * Identifies the features supported by the server, including:
 * 
 * <ul>
 * <li>
 * One or more &ltversion&gt elements that contain the protocol versions
 * supported by the server.  Use <code>getVersions</code> and
 * <code>setVersion(s)</code>     to get and set the element(s).
 * </li>
 * <li>
 * One or more &ltlang&gt elements that contain the identifiers of the text
 * response languages known by the server.  Language identifiers MUST be
 * structured as documented in <a
 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>. Only
 * language identifiers listed in [ISO639]     may be used.  Use
 * <code>getLangs</code> and <code>setLang(s)</code>     to get and set the
 * element(s).
 * </li>
 * <li>
 * One or more object-specific &ltobj:service&gt elements that identify the
 * objects that the server is capable of managing.  Use
 * <code>getObjectServices</code>     and <code>setObjectServices</code> to
 * get and set the element(s). An optional &ltsvcExtension&gt element that
 * contains one or more &ltextURI&gt elements that contains namespace URIS
 * representin object extensions supported by the server.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $ change history modified the decode method to
 * 		   accomdate the latest epp-05 protocol added service extension
 * 		   methods to accomdate extended services
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPService
 */
public class EPPServiceMenu implements EPPCodecComponent {
	/** Flag for inspecting whether the service is an  ObjectService */
	public boolean OBJ_SERVICE = false;

	/** Flag for inspecting whether the service is an ExtensionService */
	public boolean EXT_SERVICE = false;

	/**
	 * Vector of version <code>String</code> instances.  Each version String is
	 * associated with a &ltversion&gt XML element.
	 */
	private Vector versions;

	/**
	 * Vector of language <code>String</code> instances.  Each language String
	 * is     associated with a &ltlang&gt XML element.
	 */
	private Vector langs;

	/**
	 * Vector of <code>EPPService</code> instances.  Each service is associated
	 * with a &ltobjuri&gt XML element.
	 */
	private Vector services;

	/**
	 * Vector of <code>EPPExtensionService</code> instances.  Each
	 * extensionservice is     associated with a &ltexturi&gt XML element.
	 */
	private Vector extservices;

	/**
	 * Default XML root tag name for <code>EPPServiceMenu</code>.  This is the
	 * tag name used in <code>EPPGreeting</code>.
	 */
	private final String ELM_NAME = "svcMenu";

	/** XML tag name for the <code>versions</code> attribute. */
	private final String ELM_VERSION = "version";

	/** XML tag name for the <code>langs</code> attribute. */
	private final String ELM_LANG = "lang";

	/** XML tag name for the <code>objUri</code> Element. */
	private final String ELM_OBJURI = "objURI";

	/** XML tag name for the <code>SvcExtension</code> Element. */
	private final String ELM_EXT = "svcExtension";

	/** XML tag name for the <code>ExtensionUri</code> Element. */
	private final String ELM_EXTURI = "extURI";

	/**
	 * Allocates a new <code>EPPServiceMenu</code> with default attribute
	 * values. The defaults include the following: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * versions is set to a <code>Vector</code> with a single value defined by
	 * the constant EPPCodec.VERSION.
	 * </li>
	 * <li>
	 * langs is set to a <code>Vector</code> with a single "en" value.
	 * </li>
	 * <li>
	 * services is set to the services returned from <code>EPPFactory</code>
	 * </li>
	 * <li>
	 * extservices is set to the extension services returned from
	 * <code>EPPFactory</code>
	 * </li>
	 * </ul>
	 * 
	 * <br> The <code>encode</code> method can be invoked with the default
	 * values set by this constructor.
	 */
	public EPPServiceMenu() {
		versions = new Vector();
		versions.addElement(EPPCodec.VERSION);

		langs = new Vector();
		langs.addElement("en");

		services	    = EPPFactory.getInstance().getServices();
		extservices     = EPPFactory.getInstance().getExtensions();
	}

	// End EPPServiceMenu.EPPServiceMenu()

	/**
	 * Gets the list of supported/desired EPP versions.     An EPP Client uses
	 * this method to get the list     of supported EPP versions of the EPP
	 * Server.
	 *
	 * @return Vector of version <code>String</code>'s
	 */
	public Vector getVersions() {
		return versions;
	}

	// End EPPServiceMenu.getVersion()

	/**
	 * Sets the supported versions.
	 *
	 * @param someVersions Vector of version<code>String</code>'s supported by
	 * 		  the server.
	 */
	public void setVersions(Vector someVersions) {
		versions = someVersions;
	}

	// End EPPServiceMenu.setVersions(Vector)

	/**
	 * Gets the single EPP version.  A non-<code>null</code> version     will
	 * be returned only if there is one version defined.
	 *
	 * @return Single version if there is only one version; <code>null</code>
	 * 		   otherwise.
	 */
	public String getVersion() {
		String retVal = null;

		if (versions.size() == 1) {
			retVal = (String) versions.elementAt(0);
		}

		return retVal;
	}

	// End EPPServiceMenu.getVersion()

	/**
	 * Sets the EPP versions to an individual EPP version.
	 *
	 * @param aVersion Version to set versions to.
	 */
	public void setVersion(String aVersion) {
		versions = new Vector();

		versions.addElement(aVersion);
	}

	// End EPPServiceMenu.setVersion(String)

	/**
	 * Gets the list of supported/desired language(s).  Language identifiers
	 * MUST be structured as documented in [RFC1766].
	 *
	 * @return Vector of language <code>String</code>'s
	 */
	public Vector getLangs() {
		return langs;
	}

	// End EPPServiceMenu.getLang()

	/**
	 * Sets the supported languages.
	 *
	 * @param someLangs Vector of language<code>String</code>'s supported by
	 * 		  the server.
	 */
	public void setLangs(Vector someLangs) {
		langs = someLangs;
	}

	// End EPPServiceMenu.setLang(Vector)

	/**
	 * Gets the single EPP language.  A non-<code>null</code> language     will
	 * be returned only if there is one language defined.
	 *
	 * @return Single language if there is only one language; <code>null</code>
	 * 		   otherwise.
	 */
	public String getLang() {
		String retVal = null;

		if (langs.size() == 1) {
			retVal = (String) langs.elementAt(0);
		}

		return retVal;
	}

	// End EPPServiceMenu.getLang()

	/**
	 * Sets the languages to an individual language.
	 *
	 * @param aLang Language supported/desired.
	 */
	public void setLang(String aLang) {
		langs = new Vector();
		langs.addElement(aLang);
	}

	// End EPPServiceMenu.setLang(String)

	/**
	 * Gets the list of supported/desired object services.  An EPP Client will
	 * retrieve the list of object services supported by the EPP Server. An
	 * EPP Server will retrieve the list of object services desired by the EPP
	 * Client.
	 *
	 * @return Vector of <code>EPPService</code> instances.
	 */
	public Vector getObjectServices() {
		return services;
	}

	// End EPPServiceMenu.getObjectServices()

	/**
	 * Sets the list of supported/desired object services.  An EPP Client will
	 * set the list of object services desired.  An     EPP Server will set
	 * the list of supported object services.
	 *
	 * @param someObjServices Vector of<code>EPPService</code> instances.
	 */
	public void setObjectServices(Vector someObjServices) {
		services = someObjServices;
	}

	// End EPPServiceMenu.setObjectServices(Vector)

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
	 * will set the list of extension services desired.  An EPP Server will
	 * set the list of supported extension services.
	 *
	 * @param someExtServices Vector of <code>EPPService</code> instances.
	 */
	public void setExtensionServices(Vector someExtServices) {
		extservices = someExtServices;
	}

	// End EPPServiceMenu.setExtensionServices(Vector)

	/**
	 * encode <code>EPPServiceMenu</code> into a DOM element tree.  The
	 * "service-menu"     or "services" element is created and the attribute
	 * nodes are     appended as children.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return service menu root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Versions
		EPPUtil.encodeVector(
							 aDocument, root, versions, EPPCodec.NS, ELM_VERSION);

		// Languages
		EPPUtil.encodeVector(aDocument, root, langs, EPPCodec.NS, ELM_LANG);

		// Object Services
		EPPUtil.encodeCompVector(aDocument, root, services);

		// Extension Services
		if ((extservices != null) && extservices.elements().hasMoreElements()) {
			Element svcExtension =
				aDocument.createElementNS(EPPCodec.NS, ELM_EXT);
			EPPUtil.encodeCompVector(aDocument, svcExtension, extservices);
			root.appendChild(svcExtension);
		}

		return root;
	}

	// End EPPServiceMenu.encode(Document)

	/**
	 * decode <code>EPPServiceMenu</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the &ltservice-menu&gt
	 * element     for a <code>EPPGreeting</code> and &ltservices&gt element
	 * for a     <code>EPPLoginCmd</code>.
	 *
	 * @param aElement root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Versions
		versions     = EPPUtil.decodeVector(aElement, EPPCodec.NS, ELM_VERSION);

		// Languages
		langs     = EPPUtil.decodeVector(aElement, EPPCodec.NS, ELM_LANG);

		// Services
		services     = new Vector();

		//extension services
		extservices = new Vector();

		NodeList serviceElms = aElement.getElementsByTagNameNS(EPPCodec.NS,
				EPPUtil.getLocalName(ELM_OBJURI));

		for (int i = 0; i < serviceElms.getLength(); i++) {
			EPPService objService = new EPPService(); //dummy eppservice object to set the type of service

			objService.setServiceType(EPPService.OBJ_SERVICE);

			objService.decode((Element) serviceElms.item(i));

			services.addElement(objService);
		}

		//decode the extension services now if any exist
		NodeList svcExtensionNodeList = aElement.getElementsByTagNameNS(
				EPPCodec.NS, EPPUtil.getLocalName(ELM_EXT));

		if (svcExtensionNodeList.getLength() != 0) {
			Element svcExtension = null;

			for (int i = 0; i < svcExtensionNodeList.getLength(); i++) {
				svcExtension = (Element) svcExtensionNodeList.item(i);

				NodeList extensionElms = svcExtension.getElementsByTagNameNS(
						EPPCodec.NS, EPPUtil.getLocalName(ELM_EXTURI));

				for (int k = 0; k < extensionElms.getLength(); k++) {
					EPPService extService = new EPPService(); //dummy eppservice object to set the type of service
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

	// End EPPServiceMenu.decode(Element)

	/**
	 * implements a deep <code>EPPServiceMenu</code> compare.
	 *
	 * @param aObject <code>EPPServiceMenu</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPServiceMenu)) {
			return false;
		}

		EPPServiceMenu theServiceMenu = (EPPServiceMenu) aObject;

		// versions
		if (!EPPUtil.equalVectors(versions, theServiceMenu.versions)) {
			return false;
		}

		// langs
		if (!EPPUtil.equalVectors(langs, theServiceMenu.langs)) {
			return false;
		}

		// services
		if (!EPPUtil.equalVectors(services, theServiceMenu.services)) {
			return false;
		}

		// extservices
		if (!EPPUtil.equalVectors(extservices, theServiceMenu.extservices)) {
			return false;
		}

		return true;
	}

	// End EPPServiceMenu.equals(Object)

	/**
	 * Clone <code>EPPServiceMenu</code>.
	 *
	 * @return clone of <code>EPPServiceMenu</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPServiceMenu clone = null;

		clone     = (EPPServiceMenu) super.clone();

		clone.versions     = (Vector) versions.clone();

		clone.langs     = (Vector) langs.clone();

		// Services
		clone.services = (Vector) services.clone();

		for (int i = 0; i < services.size(); i++)
			clone.services.setElementAt(
										((EPPService) services.elementAt(i))
										.clone(), i);

		//extservices
		if ((extservices != null) && extservices.elements().hasMoreElements()) {
			clone.extservices = (Vector) extservices.clone();

			for (int i = 0; i < extservices.size(); i++) {
				clone.extservices.setElementAt(
											   ((EPPService) extservices
												.elementAt(i)).clone(), i);
			}
		}

		return clone;
	}

	// End EPPServiceMenu.clone()

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

	// End EPPServiceMenu.toString()
}
