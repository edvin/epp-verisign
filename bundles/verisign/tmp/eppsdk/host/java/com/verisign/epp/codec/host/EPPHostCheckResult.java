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
package com.verisign.epp.codec.host;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;


/**
 * <code>EPPHostCheckResult</code> represents the result of an individual
 * host name check.  The attributes of <code>EPPHostCheckResult</code>
 * include     the domain name and a boolean value indicating if the domain
 * name     is available.  <code>host reason</code> must be set before
 * invoking <code>encode</code> if the available flag is set to
 * <code>false</code>.     <br><br>
 * @see com.verisign.epp.codec.host.EPPHostCheckResp
 */
public class EPPHostCheckResult implements EPPCodecComponent {
	/** XML root tag name for <code>EPPDomainPingResult</code>. */
	final static String ELM_NAME = "host:cd";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_HOST_NAME = "host:name";

	/** XML Element Name for the <code>reason</code> attribute. */
	private final static String ELM_HOST_REASON = "host:reason";

	/** XML attribute name for the <code>known</code> attribute. */
	private final static String ATTR_AVAIL = "avail";

	/** XML attribute name for the <code>host:reason</code> attribute. */
	private final static String ATTR_LANG = "lang";

	/** Default XML attribute value for host reason language. */
	private final static String VALUE_LANG = "en";

	/** XML attribute value for the <code>lang</code> attribute. */
	private String language = "en";

	/** Host Name associated with result. */
	private String name;

	/** Is the Host Name (name) available? */
	private boolean available;

	/** Host inavailable reason.*/
	private String reason;
	
	/**
	 * Default constructor for <code>EPPHostCheckResult</code>.     the
	 * defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * known is set to <code>false</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPHostCheckResult() {
		name	  = null;
		available = true;
	}

	// End EPPDomainHostResult()

	/**
	 * Constructor for <code>EPPHostCheckResult</code> that includes     the
	 * domain name and the is known flag.
	 *
	 * @param aName Domain name associated with result
	 * @param isAvailable Is the domain name available?
	 */
	public EPPHostCheckResult(String aName, boolean isAvailable) {
		name	  = aName;
		available = isAvailable;
	}

	// EPPHostCheckResult(String, boolean)

	/**
	 * Gets the domain name associated with the result.
	 *
	 * @return Domain name associated with the result if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPHostCheckResult.getName()

	/**
	 * Sets the domain name associated with the result.
	 *
	 * @param aName Domain Name associated with the result.
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostCheckResult.setName(String)

	/**
	 * Gets if the domain associated with <code>EPPHostCheckResult</code> is
	 * known.
	 *
	 * @return Is the domain known?
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #isAvailable()}.
	 */
	public boolean isKnown() {
		return !this.available;
	}
	// End EPPHostCheckResult.isKnown()

	/**
	 * Sets if the domain associated with <code>EPPHostCheckResult</code> is
	 * known.
	 *
	 * @param aIsKnown Is the domain known?
	 *
	 * @deprecated As of EPP 1.0, replaced with {@link #setIsAvailable(boolean)}.
	 */
	public void setIsKnown(boolean aIsKnown) {
		this.available = !aIsKnown;
	}
	// End EPPHostCheckResult.setIsKnown(boolean)
	
	/**
	 * Gets if the domain associated with <code>EPPHostCheckResult</code> is
	 * availability (can it be provisioned or not) at the moment the
	 * &lt;check&gt; command was completed.
	 *
	 * @return Is the host available?  If <code>false</code>, call {@link
	 * 		   #getHostReason()} for inavailability reason.
	 */
	public boolean isAvailable() {
		return this.available;
	}

	// End EPPHostCheckResult.isAvailable()

	/**
	 * Sets if the host associated with <code>EPPHostCheckResult</code> is
	 * availability (can it be provisioned or not) at the moment the
	 * &lt;check&gt; command was completed.
	 *
	 * @param aIsAvailable Is the host available?
	 */
	public void setIsAvailable(boolean aIsAvailable) {
		this.available = aIsAvailable;
	}
	
	
	/**
	 * Sets host reason.
	 *
	 * @param aReason Host Reason.
	 */
	public void setHostReason(String aReason) {
		reason = aReason;
	}

	// End EPPHostCheckResult.setHostReason(String)

	/**
	 * Gets host reason.
	 *
	 * @return String of host reason.
	 */
	public String getHostReason() {
		return reason;
	}

	// End EPPHostCheckResult.getHostReason()

	/**
	 * Sets language attribute.
	 *
	 * @param aLang Sets value of language attribute.
	 */
	public void setLanguage(String aLang) {
		language = aLang;
	}

	// End EPPHostCheckResult.setLanguage(String)

	/**
	 * Sets domain reason to check.
	 *
	 * @return String of language attribute.
	 */
	public String getLanguage() {
		return language;
	}

	// End EPPHostCheckResult.getLanguage()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostCheckResult</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPHostCheckResult</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostCheckResult</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		// Host Name
		Element nameElm = aDocument.createElementNS(EPPHostMapFactory.NS, ELM_HOST_NAME);
		root.appendChild(nameElm);

		// Available
		EPPUtil.encodeBooleanAttr(nameElm, ATTR_AVAIL, this.available);
		
		// Name
		Text textNode = aDocument.createTextNode(name);
		nameElm.appendChild(textNode);

		// Host Reason
		if (reason != null) {
			Element reasonElm =
				aDocument.createElementNS(EPPHostMapFactory.NS, ELM_HOST_REASON);
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

	// End EPPHostCheckResult.encode(Document)

	/**
	 * Decode the <code>EPPHostCheckResult</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPHostCheckResult</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Available
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPHostMapFactory.NS, ELM_HOST_NAME);
		this.available = EPPUtil.decodeBooleanAttr(currElm, ATTR_AVAIL);

		// Name
		name = currElm.getFirstChild().getNodeValue();

		// Host Reason
		currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPHostMapFactory.NS, ELM_HOST_REASON);

		if (currElm != null) {
			reason = currElm.getFirstChild().getNodeValue();

			// Language
			String lang =
				currElm.getAttribute(ATTR_LANG);

			if (lang.length() > 0) {
				if (!lang.equals(VALUE_LANG)) {
					setLanguage(lang);
				}
			}
		}
	}

	// End EPPHostCheckResult.decode(Element)

	/**
	 * Compare an instance of <code>EPPHostCheckResult</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostCheckResult)) {
			return false;
		}

		EPPHostCheckResult theComp = (EPPHostCheckResult) aObject;

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

	// End EPPHostCheckResult.equals(Object)

	/**
	 * Clone <code>EPPHostCheckResult</code>.
	 *
	 * @return clone of <code>EPPHostCheckResult</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostCheckResult clone = null;

		clone = (EPPHostCheckResult) super.clone();

		return clone;
	}

	// End EPPHostCheckResult.clone()

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

	// End EPPHostCheckResult.toString()
}
