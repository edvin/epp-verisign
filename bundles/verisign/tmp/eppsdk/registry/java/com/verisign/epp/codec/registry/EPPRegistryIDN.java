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
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents the Internationalized Domain Name (IDN) policy information. The
 * &lt;registry:idn&gt; must contain the following child elements:<br>
 * <br>
 * 
 * <ul>
 * <li>
 * &lt;registry:idnVersion&gt; - The OPTIONAL server unique version of the IDN
 * language rules. Use {@link #getIdnVersion()} and
 * {@link #setIdnVersion(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:idnaVersion&gt; - An Internationalizing Domain Names in
 * Applications (IDNA) version supported by the server. IDNA represents a
 * collection of documents that describe the protocol and usage for
 * Internationalized Domain for Applications like IDNA 2003, with value of 2003,
 * or IDNA 2008, with value of 2008. Use {@link #getIdnaVersion()} and
 * {@link #setIdnaVersion(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:unicodeVersion&gt; - The Unicode version supported by the server
 * like the value of "6.0" for Unicode 6.0. Use {@link #getUnicodeVersion()} and
 * {@link #setUnicodeVersion(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:encoding&gt; - The OPTIONAL encoding for transforming Unicode
 * characters uniquely and reversibly into DNS compatible characters with a
 * default value of "Punycode". Use {@link #getEncoding()} and
 * {@link #setEncoding(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:commingleAllowed&gt; - An OPTIONAL boolean value that indicates
 * whether commingling of scripts is allowed with a default value of "false".
 * Use {@link #getCommingleAllowed()} and {@link #setCommingleAllowed(Boolean)}
 * to get and set the element.</li>
 * <li>
 * &lt;registry:language&gt; - Zero or more &lt;registry:language&gt; elements
 * that defines the supported language codes and character code point policy.
 * Use {@link #getLanguages()} and {@link #setLanguages(List)} to get and set
 * the element. Use {@link #addLanguage(EPPRegistryLanguageType)} to add one
 * language policy to the existing list.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryLanguageType
 */
public class EPPRegistryIDN implements EPPCodecComponent {
	private static final long serialVersionUID = -8068272628369235117L;

	/** XML Element Name of <code>EPPRegistryIDN</code> root element. */
	public static final String ELM_NAME = "registry:idn";

	/** XML Element Name of <code>idnVersion</code> attribute. */
	public static final String ELM_IDN_VERSION = "registry:idnVersion";

	/** XML Element Name of <code>idnaVersion</code> attribute. */
	public static final String ELM_IDNA_VERSION = "registry:idnaVersion";

	/** XML Element Name of <code>unicodeVersion</code> attribute. */
	public static final String ELM_UNICODE_VERSION = "registry:unicodeVersion";

	/** XML Element Name of <code>encoding</code> attribute. */
	public static final String ELM_ENCODING = "registry:encoding";

	/** XML Element Name of <code>commingleAllowed</code> attribute. */
	public static final String ELM_COMMINGLE_ALLOWED = "registry:commingleAllowed";

	/** String representation of idnVersion */
	private String idnVersion = null;

	/** String representation of idnaVersion */
	private String idnaVersion = null;

	/** String representation of unicodeVersion */
	private String unicodeVersion = null;

	/** Whether commingling of scripts is allowed */
	private Boolean commingleAllowed = Boolean.FALSE;

	/**
	 * Character encoding for transforming Unicode characters uniquely and
	 * reversibly into DNS compatible characters
	 */
	private String encoding = "Punycode";

	/**
	 * {@code List} of {@link EPPRegistryLanguageType} that defines the
	 * supported language codes and character code point policy
	 */
	private List languages = new ArrayList();

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryIDN} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the {@code EPPRegistryIDN}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryIDN} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryIDN.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		if (idnVersion != null && idnVersion.trim().length() > 0) {
			EPPUtil.encodeString(aDocument, root, idnVersion,
					EPPRegistryMapFactory.NS, ELM_IDN_VERSION);
		}
		EPPUtil.encodeString(aDocument, root, idnaVersion,
				EPPRegistryMapFactory.NS, ELM_IDNA_VERSION);
		EPPUtil.encodeString(aDocument, root, unicodeVersion,
				EPPRegistryMapFactory.NS, ELM_UNICODE_VERSION);

		if (encoding == null && encoding.trim().length() == 0) {
			encoding = "Punycode";
		}
		EPPUtil.encodeString(aDocument, root, encoding,
				EPPRegistryMapFactory.NS, ELM_ENCODING);
		if (commingleAllowed == null) {
			commingleAllowed = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root, commingleAllowed.toString(),
				EPPRegistryMapFactory.NS, ELM_COMMINGLE_ALLOWED);
		if (languages != null && languages.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, languages);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryIDN} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryIDN} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		idnVersion = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_IDN_VERSION);
		idnaVersion = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_IDNA_VERSION);
		unicodeVersion = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_UNICODE_VERSION);

		encoding = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_ENCODING);
		if (encoding == null && encoding.trim().length() == 0) {
			encoding = "Punycode";
		}
		commingleAllowed = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_COMMINGLE_ALLOWED);
		if (commingleAllowed == null) {
			commingleAllowed = Boolean.FALSE;
		}
		languages = EPPUtil
				.decodeCompList(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryLanguageType.ELM_NAME,
						EPPRegistryLanguageType.class);
	}

	/**
	 * Validate the state of the <code>EPPRegistryIDN</code> instance. A valid
	 * state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (unicodeVersion == null || unicodeVersion.trim().length() == 0) {
			throw new EPPCodecException("unicodeVersion element is not set");
		}
		if (idnaVersion == null || idnaVersion.trim().length() == 0) {
			throw new EPPCodecException("idnaVersion element is not set");
		}

	}

	/**
	 * Clone <code>EPPRegistryIDN</code>.
	 * 
	 * @return clone of <code>EPPRegistryIDN</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryIDN clone = (EPPRegistryIDN) super.clone();

		if (languages != null) {
			clone.languages = (List) ((ArrayList) languages).clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryIDN</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryIDN</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryIDN)) {
			return false;
		}

		EPPRegistryIDN theComp = (EPPRegistryIDN) aObject;

		if (!((idnVersion == null) ? (theComp.idnVersion == null) : idnVersion
				.equals(theComp.idnVersion))) {
			return false;
		}
		if (!((idnaVersion == null) ? (theComp.idnaVersion == null)
				: idnaVersion.equals(theComp.idnaVersion))) {
			return false;
		}
		if (!((unicodeVersion == null) ? (theComp.unicodeVersion == null)
				: unicodeVersion.equals(theComp.unicodeVersion))) {
			return false;
		}
		if (!((encoding == null) ? (theComp.encoding == null) : encoding
				.equals(theComp.encoding))) {
			return false;
		}
		if (!((commingleAllowed == null) ? (theComp.commingleAllowed == null)
				: commingleAllowed.equals(theComp.commingleAllowed))) {
			return false;
		}
		if (!((languages == null) ? (theComp.languages == null) : EPPUtil
				.equalLists(languages, theComp.languages))) {
			return false;
		}

		return true;
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
	 * Get the idnVersion.
	 * 
	 * @return {@code String} representation of idnVersion
	 */
	public String getIdnVersion() {
		return idnVersion;
	}

	/**
	 * Set the idnVersion.
	 * 
	 * @param idnVersion
	 *            {@code String} representation of idnVersion
	 */
	public void setIdnVersion(String idnVersion) {
		this.idnVersion = idnVersion;
	}

	/**
	 * Get the unicodeVersion.
	 * 
	 * @return {@code String} representation of unicodeVersion
	 */
	public String getUnicodeVersion() {
		return unicodeVersion;
	}

	/**
	 * Set the unicodeVersion.
	 * 
	 * @param unicodeVersion
	 *            {@code String} representation of unicodeVersion
	 */
	public void setUnicodeVersion(String unicodeVersion) {
		this.unicodeVersion = unicodeVersion;
	}

	/**
	 * Get the idnaVersion.
	 * 
	 * @return {@code String} representation of idnaVersion
	 */
	public String getIdnaVersion() {
		return idnaVersion;
	}

	/**
	 * Set the idnaVersion.
	 * 
	 * @param idnaVersion
	 *            {@code String} representation of idnaVersion
	 */
	public void setIdnaVersion(String idnaVersion) {
		this.idnaVersion = idnaVersion;
	}

	/**
	 * Get the character encoding.
	 * 
	 * @return character encoding for transforming Unicode characters uniquely
	 *         and reversibly into DNS compatible characters
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Set the character encoding.
	 * 
	 * @param encoding
	 *            character encoding for transforming Unicode characters
	 *            uniquely and reversibly into DNS compatible characters
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Get whether commingling of scripts is allowed.
	 * 
	 * @return {@code true} allow commingling; {@code false} do not allow
	 *         commingling
	 */
	public Boolean getCommingleAllowed() {
		return commingleAllowed;
	}

	/**
	 * Set whether commingling of scripts is allowed.
	 * 
	 * @param commingleAllowed
	 *            {@code true} allow commingling; {@code false} do not allow
	 *            commingling
	 */
	public void setCommingleAllowed(Boolean commingleAllowed) {
		this.commingleAllowed = commingleAllowed;
	}

	/**
	 * Get the {@code List} of {@link EPPRegistryLanguageType}.
	 * 
	 * @return {@code List} of {@link EPPRegistryLanguageType} that defines the
	 *         supported language codes and character code point policy
	 */
	public List getLanguages() {
		return languages;
	}

	/**
	 * Set the {@code List} of {@link EPPRegistryLanguageType}.
	 * 
	 * @param languages
	 *            {@code List} of {@link EPPRegistryLanguageType} that defines
	 *            the supported language codes and character code point policy
	 */
	public void setLanguages(List languages) {
		this.languages = languages;
	}

	/**
	 * Append one instance of {@link EPPRegistryLanguageType} to the existing
	 * list.
	 * 
	 * @param language
	 *            instance of {@link EPPRegistryLanguageType} that defines the
	 *            supported language codes and character code point policy
	 */
	public void addLanguage(EPPRegistryLanguageType language) {
		if (this.languages == null) {
			this.languages = new ArrayList();
		}
		this.languages.add(language);
	}
}
