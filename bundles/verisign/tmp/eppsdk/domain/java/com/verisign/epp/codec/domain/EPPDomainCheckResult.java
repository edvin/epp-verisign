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

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * <code>EPPDomainCheckResult</code> represents the result of an individual
 * domain name check.  The attributes of <code>EPPDomainCheckResult</code>
 * include     the domain name and a boolean value indicating if the domain
 * name     is available.  <code>domain reason</code> must be set before
 * invoking <code>encode</code> if the available flag is set to
 * <code>false</code>.
 * @see com.verisign.epp.codec.domain.EPPDomainCheckResp
 */
public class EPPDomainCheckResult implements EPPCodecComponent {
	/** XML root tag name for <code>EPPDomainCheckResult</code>. */
	final static String ELM_NAME = "domain:cd";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** XML Element Name for the <code>reason</code> attribute. */
	private final static String ELM_DOMAIN_REASON = "domain:reason";

	/** XML attribute name for the <code>known</code> attribute. */
	private final static String ATTR_AVAIL = "avail";

	/** XML attribute name for the <code>domain:reason</code> attribute. */
	private final static String ATTR_LANG = "lang";

	/** Default XML attribute value for domain reason language. */
	private final static String VALUE_LANG = "en";

	/** XML attribute value for the <code>lang</code> attribute. */
	private String language = "en";

	/** Domain Name associated with result. */
	private String name;

	/** Is the Domain Name (name) available? */
	private boolean available;

	/** Domain inavailable reason.*/
	private String reason;

	/**
	 * Default constructor for <code>EPPDomainCheckResult</code>.     the
	 * defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * available is set to <code>true</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPDomainCheckResult() {
		this.name		   = null;
		this.available     = true;
	}

	// End EPPDomainCheckResult()

	/**
	 * Constructor for <code>EPPDomainCheckResult</code> that includes     the
	 * domain name and the available flag.
	 *
	 * @param aName Domain name associated with result
	 * @param aIsAvailable Is the domain available?
	 */
	public EPPDomainCheckResult(String aName, boolean aIsAvailable) {
		this.name		   = aName;
		this.available     = aIsAvailable;
	}

	// EPPDomainCheckResult(String, boolean)

	/**
	 * Gets the domain name associated with the result.
	 *
	 * @return Domain name associated with the result if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return this.name;
	}

	// End EPPDomainCheckResult.getName()

	/**
	 * Sets the domain name associated with the result.
	 *
	 * @param aName Domain Name associated with the result.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	// End EPPDomainCheckResult.setName(String)

	/**
	 * Gets if the domain associated with <code>EPPDomainCheckResult</code> is
	 * known.
	 *
	 * @return Is the domain known?
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #isAvailable()}.
	 */
	public boolean isKnown() {
		return !this.available;
	}

	// End EPPDomainCheckResult.isKnown()

	/**
	 * Sets if the domain associated with <code>EPPDomainCheckResult</code> is
	 * known.
	 *
	 * @param aIsKnown Is the domain known?
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #setIsAvailable(boolean)}.
	 */
	public void setIsKnown(boolean aIsKnown) {
		this.available = !aIsKnown;
	}

	// End EPPDomainCheckResult.setIsKnown(boolean)

	/**
	 * Gets if the domain associated with <code>EPPDomainCheckResult</code> is
	 * availability (can it be provisioned or not) at the moment the
	 * &lt;check&gt; command was completed.
	 *
	 * @return Is the domain available?  If <code>false</code>, call {@link
	 * 		   #getDomainReason()} for inavailability reason.
	 */
	public boolean isAvailable() {
		return this.available;
	}

	// End EPPDomainCheckResult.isAvailable()

	/**
	 * Sets if the domain associated with <code>EPPDomainCheckResult</code> is
	 * availability (can it be provisioned or not) at the moment the
	 * &lt;check&gt; command was completed.
	 *
	 * @param aIsAvailable Is the domain available?
	 */
	public void setIsAvailable(boolean aIsAvailable) {
		this.available = aIsAvailable;
	}

	// End EPPDomainCheckResult.setIsAvailable(boolean)

	/**
	 * Sets domain reason.  This should be set if the available flag is set to
	 * <code>false</code>.
	 *
	 * @param aReason Domain Reason.
	 */
	public void setDomainReason(String aReason) {
		this.reason = aReason;
	}

	// End EPPDomainCheckResult.setDomainReason(String)

	/**
	 * Gets domain reason.  This should be set if the available flag is set to
	 * <code>false</code>.
	 *
	 * @return String of domain reason.
	 */
	public String getDomainReason() {
		return reason;
	}

	// End EPPDomainCheckResult.getDomainReason()

	/**
	 * Sets language attribute.
	 *
	 * @param aLang Sets domain reason language attribute.
	 */
	public void setLanguage(String aLang) {
		language = aLang;
	}

	// End EPPDomainCheckResult.setLanguage(String)

	/**
	 * Sets domain reason to check.
	 *
	 * @return String of domain reason language.
	 */
	public String getLanguage() {
		return language;
	}

	// End EPPDomainCheckResult.getLanguage()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainCheckResult</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDomainCheckResult</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainCheckResult</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		// Domain Name
		Element nameElm =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_DOMAIN_NAME);
		root.appendChild(nameElm);

		// Available
		EPPUtil.encodeBooleanAttr(nameElm, ATTR_AVAIL, this.available);

		// Name
		Text textNode = aDocument.createTextNode(name);
		nameElm.appendChild(textNode);

		// Domain Reason
		if (reason != null) {
			Element reasonElm =
				aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_DOMAIN_REASON);
			root.appendChild(reasonElm);

			// Language
			if (!language.equals(VALUE_LANG)) {
				reasonElm.setAttribute(ATTR_LANG, language);
			}

			// Domain Reason
			Text aReason = aDocument.createTextNode(reason);
			reasonElm.appendChild(aReason);
		}

		return root;
	}

	// End EPPDomainCheckResult.encode(Document)

	/**
	 * Decode the <code>EPPDomainCheckResult</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainCheckResult</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Available
		Element theNameElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPDomainMapFactory.NS, ELM_DOMAIN_NAME);
		this.name = theNameElm.getFirstChild().getNodeValue();
		this.available = EPPUtil.decodeBooleanAttr(theNameElm, ATTR_AVAIL);

		// Domain Reason
		Element theReasonElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPDomainMapFactory.NS, ELM_DOMAIN_REASON);

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

	// End EPPDomainCheckResult.decode(Element)

	/**
	 * Compare an instance of <code>EPPDomainPingResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainCheckResult)) {
			return false;
		}

		EPPDomainCheckResult theComp = (EPPDomainCheckResult) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Available
		if (this.available != theComp.available) {
			return false;
		}

		return true;
	}

	// End EPPDomainCheckResult.equals(Object)

	/**
	 * Clone <code>EPPDomainCheckResult</code>.
	 *
	 * @return clone of <code>EPPDomainCheckResult</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainCheckResult clone = null;

		clone = (EPPDomainCheckResult) super.clone();

		return clone;
	}

	// End EPPDomainCheckResult.clone()

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

	// End EPPDomainCheckResult.toString()
}


// End class EPPDomainCheckResult
