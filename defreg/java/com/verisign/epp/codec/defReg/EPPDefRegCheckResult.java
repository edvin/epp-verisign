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
package com.verisign.epp.codec.defReg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;


/**
 * <code>EPPDomainCheckResult</code> represents the result of an individual
 * defReg name ping.  The attributes of <code>EPPDomainCheckResult</code>
 * include     the defReg name and attributes namely  level which is either
 * "premium" or "standard" and  boolean value indicating if the defReg name is
 * already available.  <code>defReg reason</code> must be set before invoking
 * <code>encode</code> if the available flag is set to <code>false</code>.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegCheckResp
 */
public class EPPDefRegCheckResult implements EPPCodecComponent {
	/** XML root tag name for <code>EPPDefRegCheckResult</code>. */
	final static String ELM_NAME = "defReg:cd";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_DEFREG_NAME = "defReg:name";

	/** XML Element Name for the <code>reason</code> attribute. */
	private final static String ELM_DEFREG_REASON = "defReg:reason";

	/** XML attribute name for the <code>available</code> attribute. */
	private final static String ATTR_AVAIL = "avail";

	/** XML attribute name for the <code>level</code> attribute. */
	private final static String ATTR_LEVEL = "level";

	/** XML attribute name for the <code>defReg:reason</code> attribute. */
	private final static String ATTR_LANG = "lang";

	/** Default XML attribute value for defReg reason language. */
	private final static String VALUE_LANG = "en";

	/** XML attribute value for the <code>lang</code> attribute. */
	private String language = "en";

	/** DefRegFwd Name associated with result. */
	private String name;

	/** DefRegFwd Level associated with result. */
	private String level;

	/** Is the DefReg Name (name) available? */
	private boolean available;

	/** DefReg Reason to check.  This is a <code>String</code>. */
	private String reason;

	/**
	 * Default constructor for <code>EPPDefRegCheckResult</code>.     the
	 * defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * level is set to <code>null</code>
	 * </li>
	 * <li>
	 * available is set to <code>true</code>
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPDefRegCheckResult() {
		name     = null;

		level     = null;

		available = true;
	}

	// End EPPDefRegCheckResult()

	/**
	 * Constructor for <code>EPPDefRegCheckResult</code> that includes     the
	 * defReg name and the is available flag.
	 *
	 * @param aName DefReg name associated with result
	 * @param aLevel DefReg level associated with result, which  should be
	 * 		  either <code>EPPDefRegName.LEVEL_STANDARD</code> or
	 * 		  <code>EPPDefRegName.LEVEL_STANDARD</code>.
	 * @param aIsAvailable Is the defReg name available?
	 */
	public EPPDefRegCheckResult(String aName, String aLevel, boolean aIsAvailable) {
		name	  = aName;
		level     = aLevel;
		available     = aIsAvailable;
	}

	// EPPDefRegCheckResult(String, String ,boolean)

	/**
	 * Gets the defReg name associated with the result.
	 *
	 * @return DefReg name associated with the result if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDefRegCheckResult.getName()

	/**
	 * Sets the defReg name associated with the result.
	 *
	 * @param aName DefReg Name associated with the result.
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDefRegCheckResult.setName(String)

	/**
	 * Gets the defReg level associated with the result.  The level should be
	 * either <code>EPPDefRegName.LEVEL_STANDARD</code> or
	 * <code>EPPDefRegName.LEVEL_STANDARD</code>,
	 *
	 * @return DefReg level associated with the result if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getLevel() {
		return level;
	}

	// End EPPDefRegCheckResult.getLevel()

	/**
	 * Sets the defReg level associated with the result.
	 *
	 * @param aLevel DefReg Level associated with the result, which  should be
	 * 		  either <code>EPPDefRegName.LEVEL_STANDARD</code> or
	 * 		  <code>EPPDefRegName.LEVEL_STANDARD</code>.
	 */
	public void setLevel(String aLevel) {
		level = aLevel;
	}

	// End EPPDefRegCheckResult.setLevel(String)

	/**
	 * Gets if the defReg associated with <code>EPPDomainCheckResult</code> is
	 * available.
	 *
	 * @return Is the defReg available?
	 */
	public boolean isAvailable() {
		return available;
	}

	// End EPPDefRegCheckResult.isKnown()

	/**
	 * Sets if the defReg associated with <code>EPPDefRegCheckResult</code> is
	 * available.
	 *
	 * @param aIsAvailable Is the defReg available?
	 */
	public void setIsAvailable(boolean aIsAvailable) {
		available = aIsAvailable;
	}

	// End EPPDefRegCheckResult.setIsKnown(boolean)

	/**
	 * Sets defReg reason.
	 *
	 * @param aReason DefReg Reason.
	 */
	public void setDefRegReason(String aReason) {
		reason = aReason;
	}

	// End EPPDefRegCheckResult.setDefRegReason(String)

	/**
	 * Gets defReg reason.
	 *
	 * @return String of defReg reason.
	 */
	public String getDefRegReason() {
		return reason;
	}

	// End EPPDefRegCheckResult.getDefRegReason()

	/**
	 * Sets language attribute.
	 *
	 * @param aLang Sets defReg reason language attribute.
	 */
	public void setLanguage(String aLang) {
		language = aLang;
	}

	// End EPPDefRegCheckResult.setLanguage(String)

	/**
	 * Sets defReg reason to check.
	 *
	 * @return String of defReg reason language.
	 */
	public String getLanguage() {
		return language;
	}

	// End EPPDefRegCheckResult.getLanguage()

	/**
	 * Validate the state of the <code>EPPDefRegCreateCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the <code>EPPCodecException</code> will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPEncodeException DOCUMENT ME!
	 */
	public void validate() throws EPPEncodeException {
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		if (level == null) {
			throw new EPPEncodeException("level required attribute is not set");
		}

		if (
			!level.equals(EPPDefRegName.LEVEL_STANDARD)
				&& !level.equals(EPPDefRegName.LEVEL_PREMIUM)) {
			throw new EPPEncodeException("Invalid level of " + level + " set");
		}
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegCheckResult</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDefRegCheckResult</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegCheckResult</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate state
		try {
			validate();
		}
		 catch (EPPEncodeException ex) {
			throw new EPPEncodeException("Invalid state on EPPDefRegCheckResult.encode: "
										 + ex);
		}

		// Name
		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		// DefReg Name
		Element nameElm =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_DEFREG_NAME);
		root.appendChild(nameElm);

		nameElm.setAttribute(ATTR_LEVEL, level);

		// Available
		EPPUtil.encodeBooleanAttr(nameElm, ATTR_AVAIL, this.available);

		// Name
		Text textNode = aDocument.createTextNode(name);
		nameElm.appendChild(textNode);

		// DefReg Reason element
		if (reason != null) {
			Element reasonElm =
				aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_DEFREG_REASON);
			root.appendChild(reasonElm);

			// Language
			if (!language.equals(VALUE_LANG)) {
				reasonElm.setAttribute(ATTR_LANG, language);
			}

			// DefReg Reason value
			Text aReason = aDocument.createTextNode(reason);
			reasonElm.appendChild(aReason);
		}

		return root;
	}

	// End EPPDefRegCheckResult.encode(Document)

	/**
	 * Decode the <code>EPPDefRegCheckResult</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegCheckResult</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Available
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPDefRegMapFactory.NS, ELM_DEFREG_NAME);
		level = currElm.getAttribute(ATTR_LEVEL);
		this.available = EPPUtil.decodeBooleanAttr(currElm, ATTR_AVAIL);

		// DefReg Name
		name = currElm.getFirstChild().getNodeValue();

		// DefReg Reason
		currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPDefRegMapFactory.NS, ELM_DEFREG_REASON);

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

	// End EPPDefRegCheckResult.decode(Element)

	/**
	 * Compare an instance of <code>EPPDefRegPingResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegCheckResult)) {
			return false;
		}

		EPPDefRegCheckResult theComp = (EPPDefRegCheckResult) aObject;

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

	// End EPPDefRegCheckResult.equals(Object)

	/**
	 * Clone <code>EPPDefRegCheckResult</code>.
	 *
	 * @return clone of <code>EPPDefRegCheckResult</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegCheckResult clone = null;

		clone = (EPPDefRegCheckResult) super.clone();

		return clone;
	}

	// End EPPDefRegCheckResult.clone()

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

	// End EPPDefRegCheckResult.toString()
}
