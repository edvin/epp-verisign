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
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents the postal-address information policy information. The
 * &lt;registry:postalInfo&gt; element contains the following child elements: <br>
 * <ul>
 * <li>
 * &lt;registry:name&gt; - The minimum and maximum length of
 * &lt;contact:name&gt; element defined RFC 5733 using the
 * &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child elements,
 * respectively.</li>
 * <li>
 * &lt;registry:org&gt; - The minimum and maximum length of the
 * &lt;contact:org&gt; element defined in RFC 5733 using the
 * &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child elements,
 * respectively.</li>
 * <li>
 * &lt;registry:address&gt; - The address information policy information.</li>
 * <li>
 * &lt;registry:voiceRequired&gt; - An OPTIONAL boolean flag indicating whether
 * the server requires the &lt;contact:voice&gt; element to be defined, with a
 * default value of "false".</li>
 * <li>
 * &lt;registry:voiceExt&gt; - The OPTIONAL minimum and maximum length of the
 * &lt;contact:voice&gt; extension "x" attribute defined in RFC 5733 using the
 * &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child elements,
 * respectively.</li>
 * <li>
 * &lt;registry:faxExt&gt; - The OPTIONAL minimum and maximum length of the
 * &lt;contact:fax&gt; extension "x" attribute defined in RFC 5733 [RFC5733]
 * using the &lt;registry:minLength&gt; and &lt;registry:maxLength&gt; child
 * elements, respectively.</li>
 * <li>
 * &lt;registry:emailRegex&gt; - Zero or more &lt;registry:emailRegex&gt;
 * elements that define the regular expressions used to validate the
 * &lt;contact:email&gt; element defined in RFC 5733</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryContact
 * @see com.verisign.epp.codec.registry.EPPRegistryContactName
 * @see com.verisign.epp.codec.registry.EPPRegistryContactOrg
 * @see com.verisign.epp.codec.registry.EPPRegistryContactAddress
 * @see com.verisign.epp.codec.registry.EPPRegistryMinMaxLength
 * @see com.verisign.epp.codec.registry.EPPRegistryRegex
 */
public class EPPRegistryPostal implements EPPCodecComponent {
	private static final long serialVersionUID = 7798346996535004684L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPRegistryPostal.class);

	/**
	 * Constant for the local name
	 */
	public static final String ELM_LOCALNAME = "postalInfo";

	/**
	 * Constant for the prefix and local name
	 */
	public static final String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	public static final String ELM_VOICE_REQUIRED = "voiceRequired";

	public static final String ELM_EMAIL_REGEX = "emailRegex";

	/**
	 * Constant for the voiceExt local name
	 */
	public static final String ELM_VOICE_EXT = "voiceExt";

	/**
	 * Constant for the faxExt local name
	 */
	public static final String ELM_FAX_EXT = "faxExt";

	/**
	 * Contact name
	 */
	private EPPRegistryContactName name = null;

	/**
	 * Contact organization
	 */
	private EPPRegistryContactOrg org = null;

	/**
	 * Contact address
	 */
	private EPPRegistryContactAddress address = null;

	/**
	 * Is the contact voice required?
	 */
	private Boolean voiceRequired = Boolean.FALSE;

	/**
	 * The OPTIONAL minimum and maximum length of the &lt;contact:voice&gt;
	 * extension "x" attribute.
	 */
	private EPPRegistryMinMaxLength voiceExt = null;

	/**
	 * The OPTIONAL minimum and maximum length of the &lt;contact:fax&gt;
	 * extension "x" attribute.
	 */
	private EPPRegistryMinMaxLength faxExt = null;

	/**
	 * Email regular expressions
	 */
	private List emailRegex = new ArrayList();

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryPostal} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryPostal} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryPostal} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryPostal.encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		// name
		EPPUtil.encodeComp(aDocument, root, name);

		// org
		EPPUtil.encodeComp(aDocument, root, org);

		// address
		EPPUtil.encodeComp(aDocument, root, address);
		if (voiceRequired == null) {
			voiceRequired = Boolean.FALSE;
		}

		// voiceRequired
		EPPUtil.encodeString(aDocument, root, voiceRequired.toString(),
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_VOICE_REQUIRED);

		// voiceExt
		EPPUtil.encodeComp(aDocument, root, this.voiceExt);

		// faxExt
		EPPUtil.encodeComp(aDocument, root, this.faxExt);

		if (emailRegex != null && emailRegex.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, emailRegex);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryPostal} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryPostal} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// name
		this.setName((EPPRegistryContactName) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryContactName.ELM_NAME,
				EPPRegistryContactName.class));

		// org
		this.setOrg((EPPRegistryContactOrg) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryContactOrg.ELM_NAME,
				EPPRegistryContactOrg.class));

		// address
		address = (EPPRegistryContactAddress) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryContactAddress.ELM_NAME,
				EPPRegistryContactAddress.class);

		// voiceRequired
		voiceRequired = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_VOICE_REQUIRED);
		if (voiceRequired == null) {
			voiceRequired = Boolean.FALSE;
		}

		// voiceExt
		this.setVoiceExt((EPPRegistryMinMaxLength) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, ELM_VOICE_EXT,
				EPPRegistryMinMaxLength.class));

		// faxExt
		this.setFaxExt((EPPRegistryMinMaxLength) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, ELM_FAX_EXT,
				EPPRegistryMinMaxLength.class));

		this.setEmailRegex((EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, ELM_EMAIL_REGEX,
				EPPRegistryRegex.class)));
	}

	/**
	 * Validate the state of the <code>EPPRegistryPostal</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (name == null) {
			throw new EPPCodecException("name required element is not set");
		}
		if (org == null) {
			throw new EPPCodecException("org required element is not set");
		}
		if (address == null) {
			throw new EPPCodecException("address required element is not set");
		}
	}

	/**
	 * Clone <code>EPPRegistryPostal</code>.
	 * 
	 * @return clone of <code>EPPRegistryPostal</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryPostal clone = (EPPRegistryPostal) super.clone();

		// name
		if (name != null) {
			clone.name = (EPPRegistryContactName) name.clone();
		}

		// org
		if (org != null) {
			clone.org = (EPPRegistryContactOrg) org.clone();
		}

		// address
		if (address != null) {
			clone.address = (EPPRegistryContactAddress) address.clone();
		}

		// voiceExt
		if (this.voiceExt != null) {
			clone.voiceExt = (EPPRegistryMinMaxLength) voiceExt.clone();
		}

		// faxExt
		if (this.faxExt != null) {
			clone.faxExt = (EPPRegistryMinMaxLength) faxExt.clone();
		}

		// emailRegex
		if (emailRegex != null) {
			clone.emailRegex = (List) ((ArrayList) emailRegex).clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryPostal</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryPostal</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryPostal)) {
			return false;
		}

		EPPRegistryPostal theComp = (EPPRegistryPostal) aObject;

		// name
		if (!EqualityUtil.equals(this.name, theComp.name)) {
			cat.error("EPPRegistryPostal.equals(): name not equal");
			return false;
		}

		// org
		if (!EqualityUtil.equals(this.org, theComp.org)) {
			cat.error("EPPRegistryPostal.equals(): org not equal");
			return false;
		}

		// address
		if (!EqualityUtil.equals(this.address, theComp.address)) {
			cat.error("EPPRegistryPostal.equals(): address not equal");
			return false;
		}

		// voiceRequired
		if (!EqualityUtil.equals(this.voiceRequired, theComp.voiceRequired)) {
			cat.error("EPPRegistryPostal.equals(): voiceRequired not equal");
			return false;
		}

		// voiceExt
		if (!EqualityUtil.equals(this.voiceExt, theComp.voiceExt)) {
			cat.error("EPPRegistryPostal.equals(): voiceExt not equal");
			return false;
		}

		// faxExt
		if (!EqualityUtil.equals(this.faxExt, theComp.faxExt)) {
			cat.error("EPPRegistryPostal.equals(): faxExt not equal");
			return false;
		}

		if (!((emailRegex == null) ? (theComp.emailRegex == null) : EPPUtil
				.equalLists(emailRegex, theComp.emailRegex))) {
			cat.error("EPPRegistryPostal.equals(): emailRegex not equal");
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
	 * Gets the minimum and maximum length of name.
	 * 
	 * @return {@code EPPRegistryContactName} instance that contains min/max
	 *         length of contact name
	 */
	public EPPRegistryContactName getName() {
		return name;
	}

	/**
	 * Sets the minimum and maximum length of name.
	 * 
	 * @param name
	 *            {@code EPPRegistryContactName} instance that contains min/max
	 *            length of contact name
	 */
	public void setName(EPPRegistryContactName name) {
		this.name = name;
	}

	/**
	 * Gets the minimum and maximum length of organization.
	 * 
	 * @return {@code EPPRegistryContactOrg} instance that contains min/max
	 *         length of contact organization
	 */
	public EPPRegistryContactOrg getOrg() {
		return org;
	}

	/**
	 * Sets the minimum and maximum length of organization.
	 * 
	 * @param org
	 *            {@code EPPRegistryContactOrg} instance that contains min/max
	 *            length of contact organization
	 */
	public void setOrg(EPPRegistryContactOrg org) {
		this.org = org;
	}

	/**
	 * Gets address.
	 * 
	 * @return {@code EPPRegistryContactAddress} instance that contains contact
	 *         address attributes
	 */
	public EPPRegistryContactAddress getAddress() {
		return address;
	}

	/**
	 * Sets address.
	 * 
	 * @param address
	 *            {@code EPPRegistryContactAddress} instance that contains
	 *            contact address attributes
	 */
	public void setAddress(EPPRegistryContactAddress address) {
		this.address = address;
	}

	/**
	 * Gets voice required flag.
	 * 
	 * @return {@code true} if voice is required. {@code false} otherwise.
	 */
	public Boolean getVoiceRequired() {
		return voiceRequired;
	}

	/**
	 * Sets voice required flag.
	 * 
	 * @param voiceRequired
	 *            {@code true} if voice is required. {@code false} otherwise.
	 */
	public void setVoiceRequired(Boolean voiceRequired) {
		this.voiceRequired = voiceRequired;
	}

	/**
	 * Gets the optional voice extension minimum and maximum length.
	 * 
	 * @return <code>EPPRegistryMinMaxLength</code> instance containing the
	 *         minimum and maximum length if defined; <code>null</code>
	 *         otherise.
	 */
	public EPPRegistryMinMaxLength getVoiceExt() {
		return this.voiceExt;
	}

	/**
	 * Sets the optional voice extension minimum and maximum length.
	 * 
	 * @param aVoiceExt
	 *            <code>EPPRegistryMinMaxLength</code> instance containing the
	 *            minimum and maximum length.
	 */
	public void setVoiceExt(EPPRegistryMinMaxLength aVoiceExt) {
		if (aVoiceExt != null) {
			aVoiceExt.setRootName(EPPRegistryMapFactory.NS_PREFIX + ":"
					+ ELM_VOICE_EXT);
		}
		this.voiceExt = aVoiceExt;
	}

	/**
	 * Gets the optional fax extension minimum and maximum length.
	 * 
	 * @return <code>EPPRegistryMinMaxLength</code> instance containing the
	 *         minimum and maximum length if defined; <code>null</code>
	 *         otherise.
	 */
	public EPPRegistryMinMaxLength getFaxExt() {
		return this.faxExt;
	}

	/**
	 * Sets the optional fax extension minimum and maximum length.
	 * 
	 * @param aFaxExt
	 *            <code>EPPRegistryMinMaxLength</code> instance containing the
	 *            minimum and maximum length.
	 */
	public void setFaxExt(EPPRegistryMinMaxLength aFaxExt) {
		if (aFaxExt != null) {
			aFaxExt.setRootName(EPPRegistryMapFactory.NS_PREFIX + ":"
					+ ELM_FAX_EXT);
		}
		this.faxExt = aFaxExt;
	}

	/**
	 * Gets email regular expression.
	 * 
	 * @return regular expression used to validate &lt;contact:email&gt; element
	 *         defined in RFC 5733
	 */
	public List getEmailRegex() {
		return emailRegex;
	}

	/**
	 * Adds one email regular expression to an existing list.
	 * 
	 * @param re
	 *            regular expression used to validate &lt;contact:email&gt;
	 *            element defined in RFC 5733
	 */
	public void addEmailRegex(EPPRegistryRegex re) {
		if (re != null) {
			re.setRootName(ELM_EMAIL_REGEX);
			if (emailRegex == null) {
				emailRegex = new ArrayList();
			}
			emailRegex.add(re);
		}
	}

	/**
	 * Sets email regular expression.
	 * 
	 * @param emailRegex
	 *            regular expression used to validate &lt;contact:email&gt;
	 *            element defined in RFC 5733
	 */
	public void setEmailRegex(List emailRegex) {
		if (emailRegex != null) {
			for (Iterator it = emailRegex.iterator(); it.hasNext();) {
				EPPRegistryRegex re = (EPPRegistryRegex) it.next();
				re.setRootName(ELM_EMAIL_REGEX);
			}
		}
		this.emailRegex = emailRegex;
	}
}