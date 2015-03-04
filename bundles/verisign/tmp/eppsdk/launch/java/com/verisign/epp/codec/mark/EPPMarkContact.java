/***********************************************************
 Copyright (C) 2012 VeriSign, Inc.

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
 ***********************************************************/
package com.verisign.epp.codec.mark;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class for a Trademark, Mark for short, contact.
 */
public class EPPMarkContact implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPMarkContact.class.getName(), EPPCatFactory.getInstance()
					.getFactory());

	/**
	 * Owner entitlement for a holder contact.
	 */
	public static final String ENTITLEMENT_OWNER = "owner";

	/**
	 * Assignee entitlement for a holder contact.
	 */
	public static final String ENTITLEMENT_ASSIGNEE = "assignee";

	/**
	 * Licensee entitlement for a holder contact.
	 */
	public static final String ENTITLEMENT_LICENSEE = "licensee";

	/**
	 * Owner type for a (non-holder) contact.
	 */
	public static final String TYPE_OWNER = "owner";

	/**
	 * Agent type for a (non-holder) contact.
	 */
	public static final String TYPE_AGENT = "agent";

	/**
	 * Third party type for a (non-holder) contact.
	 */
	public static final String TYPE_THIRD_PARTY = "thirdparty";

	/**
	 * Constant for the owner local name
	 */
	public static final String ELM_HOLDER_LOCALNAME = "holder";

	/**
	 * Constant for the contact local name
	 */
	public static final String ELM_CONTACT_LOCALNAME = "contact";

	/**
	 * Constant for the owner tag name
	 */
	public static final String ELM_HOLDER_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_HOLDER_LOCALNAME;

	/**
	 * Constant for the contact tag name
	 */
	public static final String ELM_CONTACT_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_CONTACT_LOCALNAME;

	/**
	 * Element local name for name of the individual or role represented by the
	 * contact.
	 */
	private final static String ELM_NAME = "name";

	/**
	 * Element local name for name of the organization with which the contact is
	 * affiliated.
	 */
	private final static String ELM_ORG = "org";

	/** Element local name for contact's voice telephone number. */
	private final static String ELM_VOICE = "voice";

	/** Element local name for contact's facsimile telephone number. */
	private final static String ELM_FAX = "fax";

	/** Element local name for contact's email address. */
	private final static String ELM_EMAIL = "email";

	/**
	 * XML Attribute Name for a phone extension, which applies to fax and voice
	 * numbers.
	 */
	private final static String ATTR_EXT = "x";

	/**
	 * XML Attribute Name for holder contact entitlement.
	 */
	private final static String ATTR_ENTITLEMENT = "entitlement";

	/**
	 * XML Attribute Name for non-holder contact.
	 */
	private final static String ATTR_TYPE = "type";

	/**
	 * XML local name for the root element
	 */
	private String localName = ELM_CONTACT_LOCALNAME;

	/**
	 * Entitlement for a holder contact using one of the
	 * <code>ENTITLEMENT</code> constants.
	 */
	private String entitlement;

	/**
	 * Type for a (non-holder) contact using one of the <code>TYPE</code>
	 * constants.
	 */
	private String type;

	/**
	 * Name of the individual or role represented by the contact.
	 */
	private String name;

	/**
	 * Name of the organization with which the contact is affiliated.
	 */
	private String org;

	/**
	 * Address information associated with the contact
	 */
	private EPPMarkAddress address;

	/**
	 * contact's voice telephone number.
	 */
	private String voice;

	/**
	 * contact's voice telephone number extension.
	 */
	private String voiceExt;

	/**
	 * contact's facsimile telephone number.
	 */
	private String fax;

	/**
	 * contact's facsimile telephone number extension.
	 */
	private String faxExt;

	/**
	 * contact's email address
	 */
	private String email;

	/**
	 * Creates empty <code>EPPMarkContact</code> instance.
	 */
	public EPPMarkContact() {
	}

	/**
	 * Creates a mark contact with the name, organization, address, voice, fax,
	 * and email attributes.
	 * 
	 * @param aName
	 *            name of the individual or role represented by the contact.
	 * @param aOrg
	 *            name of the organization with which the contact is affiliated.
	 * @param aAddress
	 *            address information associated with the contact.
	 * @param aVoice
	 *            contact's voice telephone number
	 * @param aFax
	 *            contact's facsimile telephone number
	 * @param aEmail
	 *            contact's email address
	 */
	public EPPMarkContact(String aName, String aOrg, EPPMarkAddress aAddress,
			String aVoice, String aFax, String aEmail) {
		this.name = aName;
		this.org = aOrg;
		this.address = aAddress;
		this.voice = aVoice;
		this.fax = aFax;
		this.email = aEmail;
	}

	/**
	 * Clone <code>EPPMark</code>.
	 * 
	 * @return clone of <code>EPPMark</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPMarkContact clone = (EPPMarkContact) super.clone();

		if (this.address != null) {
			clone.address = (EPPMarkAddress) this.address.clone();
		}

		return clone;
	}

	/**
	 * Sets all this instance's data in the given XML document
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPMark.encode(Document)");
		}

		// Validate attribute independent of type of contact
		if (this.address == null) {
			throw new EPPEncodeException("address is required for contact");
		}

		// Is contact (non-holder contact)?
		if (this.isContact()) {

			if (this.name == null) {
				throw new EPPEncodeException("name is required for contact");
			}

			if (this.voice == null) {
				throw new EPPEncodeException("voice is required for contact");
			}

			if (this.email == null) {
				throw new EPPEncodeException("email is required for contact");
			}
		}

		Element root = aDocument.createElementNS(EPPMark.NS, EPPMark.NS_PREFIX
				+ ":" + this.localName);

		// Is contact (non-holder contact)?
		if (this.isContact()) {

			// Type attribute
			if (this.type != null) {
				root.setAttribute(ATTR_TYPE, this.type);
			}
		}
		else { // Is holder contact

			// Entitlement attribute
			if (this.entitlement != null) {
				root.setAttribute(ATTR_ENTITLEMENT, this.entitlement);
			}
		}

		// Name
		EPPUtil.encodeString(aDocument, root, this.name, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_NAME);

		// Org
		EPPUtil.encodeString(aDocument, root, this.org, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_ORG);

		// Address
		EPPUtil.encodeComp(aDocument, root, this.address);

		// Voice
		if (this.voice != null) {
			Element currElm = aDocument.createElementNS(EPPMark.NS,
					EPPMark.NS_PREFIX + ":" + ELM_VOICE);
			Text currVal = aDocument.createTextNode(this.voice);

			if (this.voiceExt != null) {
				currElm.setAttribute(ATTR_EXT, this.voiceExt);
			}

			currElm.appendChild(currVal);
			root.appendChild(currElm);
		}

		// Fax
		if (this.fax != null) {
			Element currElm = aDocument.createElementNS(EPPMark.NS,
					EPPMark.NS_PREFIX + ":" + ELM_FAX);
			Text currVal = aDocument.createTextNode(this.fax);

			if (this.faxExt != null) {
				currElm.setAttribute(ATTR_EXT, this.faxExt);
			}

			currElm.appendChild(currVal);
			root.appendChild(currElm);
		}

		// Email
		EPPUtil.encodeString(aDocument, root, this.email, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_EMAIL);

		return root;
	}

	/**
	 * Decode the <code>EPPMark</code> component
	 * 
	 * @param aElement
	 *            Root element of the <code>EPPMark</code>
	 * @throws EPPDecodeException
	 *             Error decoding the <code>EPPMark</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Local name
		this.localName = aElement.getLocalName();

		// Is contact (non-holder contact)?
		if (this.isContact()) {

			// Type attribute
			this.type = aElement.getAttribute(ATTR_TYPE);

			if (this.type.isEmpty()) {
				this.type = null;
			}
		}
		else { // Is holder contact

			// Entitlement attribute
			this.entitlement = aElement.getAttribute(ATTR_ENTITLEMENT);

			if (this.entitlement.isEmpty()) {
				this.entitlement = null;
			}
		}

		// Name
		this.name = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_NAME);

		// Org
		this.org = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_ORG);

		// Address
		this.address = (EPPMarkAddress) EPPUtil.decodeComp(aElement,
				EPPMark.NS, EPPMarkAddress.ELM_NAME, EPPMarkAddress.class);

		// Voice
		this.voice = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_VOICE);

		if (this.voice != null) {
			Element currElm = EPPUtil.getElementByTagNameNS(aElement,
					EPPMark.NS, ELM_VOICE);
			this.voiceExt = currElm.getAttribute(ATTR_EXT);

			if (this.voiceExt.isEmpty()) {
				this.voiceExt = null;
			}
		}
		else {
			this.voiceExt = null;
		}

		// Fax
		this.fax = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_FAX);

		if (this.fax != null) {
			Element currElm = EPPUtil.getElementByTagNameNS(aElement,
					EPPMark.NS, ELM_FAX);
			this.faxExt = currElm.getAttribute(ATTR_EXT);

			if (this.faxExt.isEmpty()) {
				this.faxExt = null;
			}
		}
		else {
			this.faxExt = null;
		}

		// Email
		this.email = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_EMAIL);

	}

	/**
	 * implements a deep <code>EPPMarkContact</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPMarkContact</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPMarkContact)) {
			cat.error("EPPMarkContact.equals(): aObject is not an EPPMarkContact");
			return false;
		}

		EPPMarkContact other = (EPPMarkContact) aObject;

		// Type
		if (!EqualityUtil.equals(this.type, other.type)) {
			cat.error("EPPMarkContact.equals(): type not equal");
			return false;
		}

		// Entitlement
		if (!EqualityUtil.equals(this.entitlement, other.entitlement)) {
			cat.error("EPPMarkContact.equals(): entitlement not equal");
			return false;
		}

		// Name
		if (!EqualityUtil.equals(this.name, other.name)) {
			cat.error("EPPMarkContact.equals(): name not equal");
			return false;
		}

		// Org
		if (!EqualityUtil.equals(this.org, other.org)) {
			cat.error("EPPMarkContact.equals(): org not equal");
			return false;
		}

		// Address
		if (!EqualityUtil.equals(this.address, other.address)) {
			cat.error("EPPMarkContact.equals(): address not equal");
			return false;
		}

		// Voice
		if (!EqualityUtil.equals(this.voice, other.voice)) {
			cat.error("EPPMarkContact.equals(): voice not equal");
			return false;
		}

		// VoiceExt
		if (!EqualityUtil.equals(this.voiceExt, other.voiceExt)) {
			cat.error("EPPMarkContact.equals(): voiceExt not equal");
			return false;
		}

		// Fax
		if (!EqualityUtil.equals(this.fax, other.fax)) {
			cat.error("EPPMarkContact.equals(): fax not equal");
			return false;
		}

		// FaxExt
		if (!EqualityUtil.equals(this.faxExt, other.faxExt)) {
			cat.error("EPPMarkContact.equals(): faxExt not equal");
			return false;
		}

		// Email
		if (!EqualityUtil.equals(this.email, other.email)) {
			cat.error("EPPMarkContact.equals(): email not equal");
			return false;
		}

		return true;
	}

	/**
	 * Is the <code>EPPMarkContact</code> a holder contact?
	 * 
	 * @return <code>true</code> if the <code>EPPMarkContact</code> is a holder
	 *         contact; <code>false</code> otherwise.
	 */
	public boolean isHolderContact() {
		return (this.localName.equals(ELM_HOLDER_LOCALNAME) ? true : false);
	}

	/**
	 * Is the <code>EPPMarkContact</code> a (non-holder) contact?
	 * 
	 * @return <code>true</code> if the <code>EPPMarkContact</code> is a
	 *         (non-holder) contact; <code>false</code> otherwise.
	 */
	public boolean isContact() {
		return (this.localName.equals(ELM_CONTACT_LOCALNAME) ? true : false);
	}

	/**
	 * Sets the XML local name of the mark contact. This should be set to either
	 * <code>ELM_HOLDER_LOCALNAME</code> or <code>ELM_CONTACT_LOCALNAME</code>.
	 * 
	 * @param aLocalName
	 *            XML local name of the mark contact
	 */
	public void setLocalName(String aLocalName) {
		this.localName = aLocalName;
	}

	/**
	 * Gets the XML local name of the mark contact.
	 * 
	 * @return The XML local name of the mark contact, which should be either
	 *         <code>ELM_HOLDER_LOCALNAME</code> or
	 *         <code>ELM_CONTACT_LOCALNAME</code>.
	 */
	public String getLocalName() {
		return this.localName;
	}

	/**
	 * Gets the entitlement of the holder contact.
	 * 
	 * @return Entitlement using one of the <code>ENTITLEMENT</code> constants
	 *         if defined; <code>null</code> otherwise.
	 */
	public String getEntitlement() {
		return this.entitlement;
	}

	/**
	 * Sets the entitlement of the holder contact.
	 * 
	 * @param aEntitlement
	 *            Entitlement of the holder contact using one of the
	 *            <code>ENTITLEMENT</code> constants.
	 */
	public void setEntitlement(String aEntitlement) {
		this.entitlement = aEntitlement;
	}

	/**
	 * Gets the type of the (non-holder) contact.
	 * 
	 * @return Type using one of the <code>TYPE</code> constants.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Sets the type of the (non-holder) contact.
	 * 
	 * @param aType
	 *            Type of the contact using one of the <code>TYPE</code>
	 *            constants.
	 */
	public void setType(String aType) {
		this.type = aType;
	}

	/**
	 * Gets name of the individual or role represented by the contact.
	 * 
	 * @return Name of the individual or role represented by the contact if
	 *         defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets name of the individual or role represented by the contact.
	 * 
	 * @param aName
	 *            Name of the individual or role represented by the contact.
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Gets the name of the organization with which the contact is affiliated.
	 * 
	 * @return The name of the organization with which the contact is affiliated
	 *         if defined; <code>null</code> otherwise.
	 */
	public String getOrg() {
		return this.org;
	}

	/**
	 * Sets the name of the organization with which the contact is affiliated.
	 * 
	 * @param aOrg
	 *            The name of the organization with which the contact is
	 *            affiliated.
	 */
	public void setOrg(String aOrg) {
		this.org = aOrg;
	}

	/**
	 * Gets the address information associated with the contact.
	 * 
	 * @return address information associated with the contact if defined;
	 *         <code>null</code> otherwise.
	 */
	public EPPMarkAddress getAddress() {
		return this.address;
	}

	/**
	 * Sets the address information associated with the contact.
	 * 
	 * @param aAddress
	 *            address information associated with the contact.
	 */
	public void setAddress(EPPMarkAddress aAddress) {
		this.address = aAddress;
	}

	/**
	 * Gets the contact's voice telephone number.
	 * 
	 * @return The contact's voice telephone number if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getVoice() {
		return this.voice;
	}

	/**
	 * Sets the contact's voice telephone number.
	 * 
	 * @param aVoice
	 *            The contact's voice telephone number.
	 */
	public void setVoice(String aVoice) {
		this.voice = aVoice;
	}

	/**
	 * Gets the contact's voice telephone number extension.
	 * 
	 * @return The contact's voice telephone number extension if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getVoiceExt() {
		return this.voiceExt;
	}

	/**
	 * Sets the contact's voice telephone extension number.
	 * 
	 * @param aVoiceExt
	 *            The contact's voice telephone extension number.
	 */
	public void setVoiceExt(String aVoiceExt) {
		this.voiceExt = aVoiceExt;
	}

	/**
	 * Gets the contact's facsimile telephone number.
	 * 
	 * @return The contact's facsimile telephone number if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getFax() {
		return this.fax;
	}

	/**
	 * Sets the contact's facsimile telephone number.
	 * 
	 * @param aFax
	 *            The contact's facsimile telephone number.
	 */
	public void setFax(String aFax) {
		this.fax = aFax;
	}

	/**
	 * Gets the contact's facsimile telephone number extension.
	 * 
	 * @return The contact's facsimile telephone number extension if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getFaxExt() {
		return this.faxExt;
	}

	/**
	 * Sets the contact's facsimile telephone extension number.
	 * 
	 * @param aFaxExt
	 *            The contact's facsimile telephone extension number.
	 */
	public void setFaxExt(String aFaxExt) {
		this.faxExt = aFaxExt;
	}

	/**
	 * Gets the contact's email address.
	 * 
	 * @return The contact's email address if defined; <code>null</code>
	 *         otherwise.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the contact's email address.
	 * 
	 * @param aEmail
	 *            The contact's email address.
	 */
	public void setEmail(String aEmail) {
		this.email = aEmail;
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
}
