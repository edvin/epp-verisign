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
package com.verisign.epp.codec.registry;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class EPPRegistryCheckResult implements EPPCodecComponent {
	/** DOCUMENT ME! */
	private static final long serialVersionUID = -3349624534103691414L;
	
	private static Logger cat = Logger.getLogger(EPPRegistryCheckResult.class);

	/** XML root tag name for <code>EPPRegistryCheckResult</code>. */
	final static String ELM_NAME = "registry:cd";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_REGISTRY_NAME = "registry:name";

	/** XML Element Name for the <code>reason</code> attribute. */
	private final static String ELM_REGISTRY_REASON = "registry:reason";

	/** XML attribute name for the <code>known</code> attribute. */
	private final static String ATTR_AVAIL = "avail";

	/** XML attribute name for the <code>registry:reason</code> attribute. */
	private final static String ATTR_LANG = "lang";

	/** Default XML attribute value for registry reason language. */
	private final static String VALUE_LANG = "en";

	/** XML attribute value for the <code>lang</code> attribute. */
	private String language = "en";

	/** zone Name associated with result. */
	private String name;

	/** Is the zone Name (name) available? */
	private Boolean available;

	/** zone inavailable reason. */
	private String reason;

	/**
	 * Creates a new EPPRegistryCheckResult object.
	 */
	public EPPRegistryCheckResult() {
		this.name = null;
		this.available = Boolean.TRUE;
	}

	/**
	 * Creates a new EPPRegistryCheckResult object.
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * @param available
	 *            DOCUMENT ME!
	 */
	public EPPRegistryCheckResult(String name, Boolean available) {
		this.name = name;
		this.available = available;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aDocument
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws EPPEncodeException
	 *             DOCUMENT ME!
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required element is not set");
		}
		if (available == null) {
			throw new EPPEncodeException(
					"available required attribute is not set");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		// zone Name
		Element nameElm = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_REGISTRY_NAME);
		root.appendChild(nameElm);

		// Available
		EPPUtil.encodeBooleanAttr(nameElm, ATTR_AVAIL,
				this.available.booleanValue());

		// Name
		Text textNode = aDocument.createTextNode(name);
		nameElm.appendChild(textNode);

		// zone Reason
		if (reason != null) {
			Element reasonElm = aDocument.createElementNS(
					EPPRegistryMapFactory.NS, ELM_REGISTRY_REASON);
			root.appendChild(reasonElm);

			// Language
			if (!language.equals(VALUE_LANG)) {
				reasonElm.setAttribute(ATTR_LANG, language);
			}

			// zone Reason
			Text aReason = aDocument.createTextNode(reason);
			reasonElm.appendChild(aReason);
		}

		return root;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aElement
	 *            DOCUMENT ME!
	 * 
	 * @throws EPPDecodeException
	 *             DOCUMENT ME!
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Available
		Element theNameElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPRegistryMapFactory.NS, ELM_REGISTRY_NAME);
		this.name = theNameElm.getFirstChild().getNodeValue();
		this.available = Boolean.valueOf(EPPUtil.decodeBooleanAttr(theNameElm,
				ATTR_AVAIL));

		// TLD Reason
		Element theReasonElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPRegistryMapFactory.NS, ELM_REGISTRY_REASON);

		if (theReasonElm != null) {
			this.reason = theReasonElm.getFirstChild().getNodeValue();

			String theLang = theReasonElm.getAttribute(ATTR_LANG);

			if (theLang.length() > 0) {
				if (!theLang.equals(VALUE_LANG)) {
					setLanguage(theLang);
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws CloneNotSupportedException
	 *             DOCUMENT ME!
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryCheckResult clone = null;

		clone = (EPPRegistryCheckResult) super.clone();

		return clone;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aObject
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryCheckResult)) {
			return false;
		}

		EPPRegistryCheckResult theComp = (EPPRegistryCheckResult) aObject;

		// Name
		if (!((name == null) ? (theComp.name == null) : name
				.equals(theComp.name))) {
			cat.error("EPPRegistryCheckResult: name not equal");
			return false;
		}

		// Available
		if (!((available == null) ? (theComp.available == null)
				: this.available.booleanValue() == theComp.available
						.booleanValue())) {
			cat.error("EPPRegistryCheckResult: available not equal");
			return false;
		}

		if (!((language == null) ? (theComp.language == null) : language
				.equals(theComp.language))) {
			cat.error("EPPRegistryCheckResult: language not equal");
			return false;
		}

		if (!((reason == null) ? (theComp.reason == null) : reason
				.equals(theComp.reason))) {
			cat.error("EPPRegistryCheckResult: reason not equal");
			return false;
		}

		return true;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param language
	 *            DOCUMENT ME!
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Boolean isAvailable() {
		return available;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param available
	 *            DOCUMENT ME!
	 */
	public void setAvailable(Boolean available) {
		this.available = available;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param reason
	 *            DOCUMENT ME!
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
