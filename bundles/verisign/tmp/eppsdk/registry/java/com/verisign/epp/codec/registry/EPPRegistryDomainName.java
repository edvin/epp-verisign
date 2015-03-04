/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * This class is encoded to the &lt;registry:domainName&gt; element inside the
 * &lt;registry:domain&gt; element. It represents policies for a domain name
 * label for a specific level, defined with the "level" attribute, with a
 * minimum value of "2" for the second level domain name label level. The
 * &lt;registry:domainName&gt; element contains the following child elements <br>
 * <br>
 * 
 * <ul>
 * <li>
 * &lt;registry:minLength&gt; - An OPTIONAL minimum length of the domain name
 * label. Use {@link #getMinLength()} and {@link #setMinLength(Integer)} to get
 * and set the element.</li>
 * <li>
 * &lt;registry:maxLength&gt; - An OPTIONAL maximum length of the domain name
 * label. Use {@link #getMaxLength()} and {@link #setMaxLength(Integer)} to get
 * and set the element.</li>
 * <li>
 * &lt;registry:alphaNumStart&gt; - An OPTIONAL flag indicating whether the
 * label must start with an alphanumeric character with a default of "false".
 * Use {@link #getAlphaNumStart()} and {@link #setAlphaNumStart(Boolean)} to get
 * and set the element.</li>
 * <li>
 * &lt;registry:alphaNumEnd&gt; - An OPTIONAL flag indicating whether the label
 * must end with an alphanumeric character with a default value of "false". Use
 * {@link #getAlphaNumEnd()} and {@link #setAlphaNumEnd(Boolean)} to get and set
 * the element.</li>
 * <li>
 * &lt;registry:onlyDnsChars&gt; - An OPTIONAL flag indicating whether the label
 * MUST only contain valid DNS characters (alphanumeric and '-') with a default
 * value of "true". Use {@link #getOnlyDnsChars()} and
 * {@link #setOnlyDnsChars(Boolean)} to get and set the element.</li>
 * <li>
 * &lt;registry:regex&gt; - Zero or more &lt;registry:regex&gt; elements that
 * contain a &lt;registry:expression&gt; child element that defines the regular
 * expression to apply to domain name label along with an OPTIONAL
 * &lt;registry:explanation&gt; child element that describes the regular
 * expression with an OPTIONAL "lang" attribute that defines the language of the
 * explanation with a default value of "en". Use {@link #getRegex()} and
 * {@link #setRegex(List)} to get and set the element.</li>
 * <li>
 * &lt;registry:reservedNames&gt; - An OPTIONAL element that defines the set of
 * reserved domain names starting from that label level. The reserved names can
 * refer to values with more than one level which is relative to the level of
 * the parent &lt;registry:domainName&gt; element. Use
 * {@link #getReservedNames()} and
 * {@link #setReservedNames(EPPRegistryReservedNames)} to get and set the
 * element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryReservedNames
 */
public class EPPRegistryDomainName implements EPPCodecComponent {
	private static final long serialVersionUID = -3575272010031382087L;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryDomainName.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** XML Element Name of <code>EPPRegistryDomainName</code> root element. */
	final static String ELM_NAME = "registry:domainName";

	/** XML tag name for the <code>minLength</code> attribute. */
	final static String ELM_MIN_LENGTH = "registry:minLength";

	/** XML tag name for the <code>maxLength</code> attribute. */
	final static String ELM_MAX_LENGTH = "registry:maxLength";

	/** XML tag name for the <code>alphaNumStart</code> attribute. */
	final static String ELM_ALPHA_NUM_START = "registry:alphaNumStart";

	/** XML tag name for the <code>alphaNumEnd</code> attribute. */
	final static String ELM_ALPHA_NUM_END = "registry:alphaNumEnd";

	/** XML tag name for the <code>onlyDnsChars</code> attribute. */
	final static String ELM_ONLY_DNS_CHARS = "registry:onlyDnsChars";

	/** XML tag name for the <code>regex</code> attribute. */
	final static String ELM_REGEX = "regex";

	/** XML attribute name for the <code>level</code> attribute. */
	final static String ATTR_LEVEL = "level";

	/** Level of domain name. Value should be >= 2 */
	private Integer level = null;

	/** Minimum number of characters in a domain name */
	private Integer minLength = null;

	/** Maximum number of characters in a domain name */
	private Integer maxLength = null;

	/** Whether or not to allow domain name start with an alphanumeric character */
	private Boolean alphaNumStart = Boolean.FALSE;

	/** Whether or not to allow domain name end with an alphanumeric character */
	private Boolean alphaNumEnd = Boolean.FALSE;

	/** Limit only DNS characters in the domain name */
	private Boolean onlyDnsChars = Boolean.TRUE;

	/**
	 * {@code List} of {@link EPPRegistryRegex} regular expression that domain
	 * name must follow
	 */
	private List regex = new ArrayList();

	/** Defines a set of reserved domain names */
	private EPPRegistryReservedNames reservedNames = null;

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryDomainName} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryDomainName} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryDomainName} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryDomainName.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		root.setAttribute(ATTR_LEVEL, level.toString());
		if (minLength != null) {
			EPPUtil.encodeString(aDocument, root, minLength.toString(),
					EPPRegistryMapFactory.NS, ELM_MIN_LENGTH);
		}
		if (maxLength != null) {
			EPPUtil.encodeString(aDocument, root, maxLength.toString(),
					EPPRegistryMapFactory.NS, ELM_MAX_LENGTH);
		}
		if (alphaNumStart == null) {
			alphaNumStart = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root, alphaNumStart.toString(),
				EPPRegistryMapFactory.NS, ELM_ALPHA_NUM_START);
		if (alphaNumEnd == null) {
			alphaNumEnd = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root, alphaNumEnd.toString(),
				EPPRegistryMapFactory.NS, ELM_ALPHA_NUM_END);
		if (onlyDnsChars == null) {
			onlyDnsChars = Boolean.TRUE;
		}
		EPPUtil.encodeString(aDocument, root, onlyDnsChars.toString(),
				EPPRegistryMapFactory.NS, ELM_ONLY_DNS_CHARS);
		if (regex != null && regex.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, regex);
		}
		if (reservedNames != null) {
			EPPUtil.encodeComp(aDocument, root, reservedNames);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryDomainName} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryDomainName} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		String levelStr = null;
		try {
			levelStr = aElement.getAttribute(ATTR_LEVEL);
			level = Integer.valueOf(levelStr);
		} catch (NumberFormatException e) {
			throw new EPPDecodeException("Cannot decode level: " + levelStr
					+ "." + e);
		}
		minLength = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS,
				ELM_MIN_LENGTH);
		maxLength = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS,
				ELM_MAX_LENGTH);
		alphaNumStart = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_ALPHA_NUM_START);
		if (alphaNumStart == null) {
			alphaNumStart = Boolean.FALSE;
		}
		alphaNumEnd = EPPUtil.decodeBoolean(aElement, EPPRegistryMapFactory.NS,
				ELM_ALPHA_NUM_END);
		if (alphaNumEnd == null) {
			alphaNumEnd = Boolean.FALSE;
		}
		onlyDnsChars = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_ONLY_DNS_CHARS);
		if (onlyDnsChars == null) {
			onlyDnsChars = Boolean.TRUE;
		}
		this.setRegex(EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, ELM_REGEX, EPPRegistryRegex.class));
		reservedNames = (EPPRegistryReservedNames) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryReservedNames.ELM_NAME,
				EPPRegistryReservedNames.class);

		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPDecodeException(
					"Invalid state on EPPRegistryDomainName.decode: " + e);
		}
	}

	/**
	 * implements a deep <code>EPPRegistryDomainName</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryDomainName</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryDomainName)) {
			return false;
		}

		EPPRegistryDomainName theComp = (EPPRegistryDomainName) aObject;
		if (!((level == null) ? (theComp.level == null) : level
				.equals(theComp.level))) {
			cat.error("EPPRegistryDomainName.equals(): level not equal");
			return false;
		}
		if (!((minLength == null) ? (theComp.minLength == null) : minLength
				.equals(theComp.minLength))) {
			cat.error("EPPRegistryDomainName.equals(): minLength not equal");
			return false;
		}
		if (!((maxLength == null) ? (theComp.maxLength == null) : maxLength
				.equals(theComp.maxLength))) {
			cat.error("EPPRegistryDomainName.equals(): maxLength not equal");
			return false;
		}
		if (!((alphaNumStart == null) ? (theComp.alphaNumStart == null)
				: alphaNumStart.equals(theComp.alphaNumStart))) {
			cat.error("EPPRegistryDomainName.equals(): alphaNumStart not equal");
			return false;
		}
		if (!((alphaNumEnd == null) ? (theComp.alphaNumEnd == null)
				: alphaNumEnd.equals(theComp.alphaNumEnd))) {
			cat.error("EPPRegistryDomainName.equals(): alphaNumEnd not equal");
			return false;
		}
		if (!((onlyDnsChars == null) ? (theComp.onlyDnsChars == null)
				: onlyDnsChars.equals(theComp.onlyDnsChars))) {
			cat.error("EPPRegistryDomainName.equals(): onlyDnsChars not equal");
			return false;
		}
		if (!((regex == null) ? (theComp.regex == null) : EPPUtil.equalLists(
				regex, theComp.regex))) {
			cat.error("EPPRegistryDomainName.equals(): regex not equal");
			return false;
		}
		if (!((reservedNames == null) ? (theComp.reservedNames == null)
				: reservedNames.equals(theComp.reservedNames))) {
			cat.error("EPPRegistryDomainName.equals(): reservedNames not equal");
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPRegistryDomainName</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (level == null || level.intValue() < 2) {
			throw new EPPCodecException(
					"level should exist and be greater than or equal to 2");
		}
		if (minLength != null && minLength.intValue() < 1) {
			throw new EPPCodecException(
					"minLength, if specified, should be greater than 0");
		}
		if (maxLength != null) {
			if (minLength != null) {
				if (maxLength.intValue() < minLength.intValue()) {
					throw new EPPCodecException(
							"maxLength, if specified, should be no less than minLength");
				}
			} else if (maxLength.intValue() < 1) {
				throw new EPPCodecException(
						"maxLength, if specified, should be greater than 0");
			}
		}
	}

	/**
	 * Clone <code>EPPRegistryDomainName</code>.
	 * 
	 * @return clone of <code>EPPRegistryDomainName</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryDomainName clone = (EPPRegistryDomainName) super.clone();

		if (regex != null) {
			clone.regex = (List) ((ArrayList) regex).clone();
		}

		if (reservedNames != null) {
			clone.reservedNames = (EPPRegistryReservedNames) reservedNames
					.clone();
		}

		return clone;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Get the level of domain name.
	 * 
	 * @return level of domain name. Must be >= 2
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * Set the level of domain name.
	 * 
	 * @param level
	 *            level of domain name. Must be >= 2
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * Get minimum number of characters in a domain name.
	 * 
	 * @return minimum number of characters in a domain name
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * Set minimum number of characters in a domain name.
	 * 
	 * @param minLength
	 *            minimum number of characters in a domain name
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * Get maximum number of characters in a domain name.
	 * 
	 * @return maximum number of characters in a domain name
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * Set maximum number of characters in a domain name.
	 * 
	 * @param maxLength
	 *            maximum number of characters in a domain name
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Get whether or not to allow domain name start with an alphanumeric
	 * character.
	 * 
	 * @return {@code true} allow domain name start with an alphanumeric
	 *         character. {@code false} do not allow domain name start with an
	 *         alphanumeric character
	 */
	public Boolean getAlphaNumStart() {
		return alphaNumStart;
	}

	/**
	 * Set whether or not to allow domain name start with an alphanumeric
	 * character.
	 * 
	 * @param alphaNumStart
	 *            {@code true} allow domain name start with an alphanumeric
	 *            character. {@code false} do not allow domain name start with
	 *            an alphanumeric character
	 */
	public void setAlphaNumStart(Boolean alphaNumStart) {
		this.alphaNumStart = alphaNumStart;
	}

	/**
	 * Get whether or not to allow domain name end with an alphanumeric
	 * character.
	 * 
	 * @return {@code true} allow domain name end with an alphanumeric
	 *         character. {@code false} do not allow domain name end with an
	 *         alphanumeric character
	 */
	public Boolean getAlphaNumEnd() {
		return alphaNumEnd;
	}

	/**
	 * Set whether or not to allow domain name end with an alphanumeric
	 * character.
	 * 
	 * @param alphaNumEnd
	 *            {@code true} allow domain name end with an alphanumeric
	 *            character. {@code false} do not allow domain name end with an
	 *            alphanumeric character
	 */
	public void setAlphaNumEnd(Boolean alphaNumEnd) {
		this.alphaNumEnd = alphaNumEnd;
	}

	/**
	 * Get whether to limit only DNS characters in the domain name.
	 * 
	 * @return {@code true} allow only DNS characters (alphanumeric and '-') in
	 *         the domain name. {@code false} allow non-DNS characters in the
	 *         domain name
	 */
	public Boolean getOnlyDnsChars() {
		return onlyDnsChars;
	}

	/**
	 * Set whether to limit only DNS characters in the domain name.
	 * 
	 * @param onlyDnsChars
	 *            {@code true} allow only DNS characters (alphanumeric and '-')
	 *            in the domain name. {@code false} allow non-DNS characters in
	 *            the domain name
	 */
	public void setOnlyDnsChars(Boolean onlyDnsChars) {
		this.onlyDnsChars = onlyDnsChars;
	}

	/**
	 * Get the {@code List} of {@link EPPRegistryRegex} regular expressions that
	 * domain name must follow.
	 * 
	 * @return {@code List} of {@link EPPRegistryRegex} regular expressions
	 */
	public List getRegex() {
		return regex;
	}

	/**
	 * Append one instance of {@link EPPRegistryRegex} to the existing
	 * {@code List}.
	 * 
	 * @param re
	 *            instance of {@link EPPRegistryRegex}
	 */
	public void addRegex(EPPRegistryRegex re) {
		if (re != null) {
			re.setRootName(ELM_REGEX);
			if (regex == null) {
				regex = new ArrayList();
			}
			regex.add(re);
		}
	}

	/**
	 * Set the {@code List} of {@link EPPRegistryRegex} regular expressions that
	 * domain name must follow.
	 * 
	 * @param regex
	 *            {@code List} of {@link EPPRegistryRegex} regular expressions
	 */
	public void setRegex(List regex) {
		if (regex != null) {
			for (Iterator it = regex.iterator(); it.hasNext();) {
				EPPRegistryRegex re = (EPPRegistryRegex) it.next();
				re.setRootName(ELM_REGEX);
			}
		}
		this.regex = regex;
	}

	/**
	 * Get the instance of {@link EPPRegistryReservedNames} that defines a set
	 * of reserved domain names.
	 * 
	 * @return instance of {@link EPPRegistryReservedNames}
	 */
	public EPPRegistryReservedNames getReservedNames() {
		return reservedNames;
	}

	/**
	 * Set the instance of {@link EPPRegistryReservedNames} that defines a set
	 * of reserved domain names.
	 * 
	 * @param reservedNames
	 *            instance of {@link EPPRegistryReservedNames}
	 */
	public void setReservedNames(EPPRegistryReservedNames reservedNames) {
		this.reservedNames = reservedNames;
	}
}
