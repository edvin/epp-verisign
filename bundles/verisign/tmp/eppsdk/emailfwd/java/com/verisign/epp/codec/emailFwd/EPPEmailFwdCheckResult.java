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
package com.verisign.epp.codec.emailFwd;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;


/**
 * <code>EPPEmailFwdCheckResult</code> represents the result of an individual
 * emailFwd name ping.  The attributes of <code>EPPEmailFwdCheckResult</code>
 * include     the emailFwd name and a boolean value indicating if the
 * emailFwd name     is already available. <code>emailFwd reason</code> must be
 * set before invoking <code>encode</code> if the available flag is set to
 * <code>false</code>. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResp
 */
public class EPPEmailFwdCheckResult implements EPPCodecComponent {
	/** XML root tag name for <code>EPPEmailFwdCheckResult</code>. */
	final static String ELM_NAME = "emailFwd:cd";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** XML Element Name for the <code>reason</code> attribute. */
	private final static String ELM_EMAILFWD_REASON = "emailFwd:reason";

	/** XML attribute name for the <code>available</code> attribute. */
	private final static String ATTR_AVAIL = "avail";
	
	/** XML attribute name for the <code>emailFwd:reason</code> attribute. */
	private final static String ATTR_LANG = "lang";

	/** Default XML attribute value for emailFwd reason language. */
	private final static String VALUE_LANG = "en";

	/** XML attribute value for the <code>lang</code> attribute. */
	private String language = "en";

	/** EmailFwd Name associated with result. */
	private String name;

	/** Is the EmailFwd Name (name) available? */
	private boolean available;

	/** EmailFwd Reason to check.  This is a <code>String</code>. */
	private String reason;

	/**
	 * Default constructor for <code>EPPEmailFwdCheckResult</code>.     the
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
	public EPPEmailFwdCheckResult() {
		name     = null;

		available = true;
	}

	// End EPPEmailFwdCheckResult()

	/**
	 * Constructor for <code>EPPEmailFwdCheckResult</code> that includes the
	 * emailFwd name and the is available flag.
	 *
	 * @param aName EmailFwd name associated with result
	 * @param aIsAvailable Is the emailFwd name available?
	 */
	public EPPEmailFwdCheckResult(String aName, boolean aIsAvailable) {
		name	  = aName;
		available     = aIsAvailable;
	}

	// EPPEmailFwdCheckResult(String, boolean)

	/**
	 * Gets the emailFwd name associated with the result.
	 *
	 * @return EmailFwd name associated with the result if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdCheckResult.getName()

	/**
	 * Sets the emailFwd name associated with the result.
	 *
	 * @param aName EmailFwd Name associated with the result.
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdCheckResult.setName(String)

	/**
	 * Gets if the emailFwd associated with <code>EPPEmailFwdCheckResult</code>
	 * is available.
	 *
	 * @return Is the emailFwd available?
	 */
	public boolean isAvailable() {
		return available;
	}

	// End EPPEmailFwdCheckResult.isAvailable()

	/**
	 * Sets if the emailFwd associated with <code>EPPEmailFwdCheckResult</code>
	 * is available.
	 *
	 * @param aIsAvailable Is the emailFwd available?
	 */
	public void setIsAvailable(boolean aIsAvailable) {
		available = aIsAvailable;
	}

	// End EPPEmailFwdCheckResult.setIsAvailable(boolean)

	/**
	 * Sets emailFwd reason.
	 *
	 * @param aReason EmailFwd Reason.
	 */
	public void setEmailFwdReason(String aReason) {
		reason = aReason;
	}

	// End EPPEmailFwdCheckResult.setEmailFwdReason(String)

	/**
	 * Gets emailFwd reason.
	 *
	 * @return String of emailFwd reason.
	 */
	public String getEmailFwdReason() {
		return reason;
	}

	// End EPPEmailFwdCheckResult.getEmailFwdReason()

	/**
	 * Sets language attribute.
	 *
	 * @param aLang Sets emailFwd reason language attribute.
	 */
	public void setLanguage(String aLang) {
		language = aLang;
	}

	// End EPPEmailFwdCheckResult.setLanguage(String)

	/**
	 * Sets emailFwd reason to check.
	 *
	 * @return String of emailFwd reason language.
	 */
	public String getLanguage() {
		return language;
	}

	// End EPPEmailFwdCheckResult.getLanguage()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdCheckResult</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPEmailFwdCheckResult</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdCheckResult</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		// Name
		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		// EmailFwd Name
		Element nameElm =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_NAME);
		root.appendChild(nameElm);

		// Available
		EPPUtil.encodeBooleanAttr(nameElm, ATTR_AVAIL, this.available);

		// Name
		Text textNode = aDocument.createTextNode(name);
		nameElm.appendChild(textNode);

		// EmailFwd Reason
		if (reason != null) {
			Element reasonElm =
				aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_REASON);
			root.appendChild(reasonElm);

			// Language
			if (!language.equals(VALUE_LANG)) {
				reasonElm.setAttribute(ATTR_LANG, language);
			}

			// EmailFwd Reason
			Text aReason = aDocument.createTextNode(reason);
			reasonElm.appendChild(aReason);
		}

		return root;
	}

	// End EPPEmailFwdCheckResult.encode(Document)

	/**
	 * Decode the <code>EPPEmailFwdCheckResult</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdCheckResult</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Available
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_NAME);
		this.available = EPPUtil.decodeBooleanAttr(currElm, ATTR_AVAIL);
		
		// EmailFwd Name
		name = currElm.getFirstChild().getNodeValue();

		// EmailFwd Reason
		currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPEmailFwdMapFactory.NS, ELM_EMAILFWD_NAME);

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

	// End EPPEmailFwdCheckResult.decode(Element)

	/**
	 * Compare an instance of <code>EPPEmailFwdPingResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdCheckResult)) {
			return false;
		}

		EPPEmailFwdCheckResult theComp = (EPPEmailFwdCheckResult) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Available
		if (available != theComp.available) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdCheckResult.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdCheckResult</code>.
	 *
	 * @return clone of <code>EPPEmailFwdCheckResult</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdCheckResult clone = null;

		clone = (EPPEmailFwdCheckResult) super.clone();

		return clone;
	}

	// End EPPEmailFwdCheckResult.clone()

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

	// End EPPEmailFwdCheckResult.toString()
}
