/***********************************************************
 Copyright (C) 2013 VeriSign, Inc.

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class for a Trademark that can be included in the list of trademarks contained
 * in the {@link EPPMark}.
 * 
 * @see EPPMark
 */
public class EPPTrademark implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPTrademark.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the trademark local name
	 */
	public static final String ELM_LOCALNAME = "trademark";

	/**
	 * Constant for the trademark tag
	 */
	public static final String ELM_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for identifier of the trademark
	 */
	private static final String ELM_ID = "id";

	/**
	 * Element local name for the registered trademark text string
	 */
	private static final String ELM_MARK_NAME = "markName";

	/**
	 * Element local name for the jurisdiction.
	 */
	private static final String ELM_JURISDICTION = "jurisdiction";

	/**
	 * Element local name for the Nice Classification class number
	 */
	private static final String ELM_CLASS = "class";

	/**
	 * Element local name for domain name A-label for the trademark
	 */
	private static final String ELM_LABEL = "label";

	/**
	 * Element local name for name of the goods and services
	 */
	private static final String ELM_GOODS_AND_SERVICES = "goodsAndServices";

	/**
	 * Element local name for the trademark application ID.
	 */
	private static final String ELM_AP_ID = "apId";

	/**
	 * Element local name of the date the trademark was applied for.
	 */
	private static final String ELM_AP_DATE = "apDate";

	/**
	 * Element local name for the trademark registration ID.
	 */
	private static final String ELM_REG_NUM = "regNum";

	/**
	 * Element local name the date of registration / application of the trademark
	 */
	private static final String ELM_REG_DATE = "regDate";

	/**
	 * Element local name for the date of expiration of the trademark
	 */
	private static final String ELM_EX_DATE = "exDate";

	/**
	 * Identifier for the trademark
	 */
	private String id;

	/**
	 * The registered trademark text string.
	 */
	private String name;

	/**
	 * One or more trademark holders that contain information of the holder of the
	 * trademark. An "entitlement" attribute attribute is used to identify the
	 * entitlement of the holder, possible values are: owner, assignee and
	 * licensee.
	 */
	private List<EPPMarkContact> holders = new ArrayList<EPPMarkContact>();

	/**
	 * Zero or more contacts that contains the information of the representation
	 * of the trademark registration. A "type" attribute is used to identify the type
	 * of contact, possible values are: owner, agent or thirdparty.
	 */
	private List<EPPMarkContact> contacts = new ArrayList<EPPMarkContact>();

	/**
	 * The two-character code of the jurisdiction where the trademark was
	 * registered. This is a two-character code from [WIPO.ST3].
	 */
	private String jurisdiction;

	/**
	 * Nice Classification class numbers as defined in the Nice List of Classes.
	 */
	private List<String> classes = new ArrayList<String>();

	/**
	 * Zero or more domain name A-labels that corresponds to the trademark name.
	 */
	private List<String> labels = new ArrayList<String>();

	/**
	 * Contains the full description of the goods and services mentioned in the
	 * trademark registration document.
	 */
	private String goodsAndServices;

	/**
	 * Trademark application ID registered in the trademark office.
	 */
	private String apId;

	/**
	 * The date the trademark was applied for.
	 */
	private Date apDate;

	/**
	 * Trademark registration ID (number).
	 */
	private String regNum;

	/**
	 * the date the trademark was registered.
	 */
	private Date regDate;

	/**
	 * the date of expiration of the trademark.
	 */
	private Date exDate;

	/**
	 * Create an <code>EPPTrademark</code> instance. Use the setter methods to
	 * set the attributes of the instance.
	 */
	public EPPTrademark() {
	}

	/**
	 * Clone <code>EPPTrademark</code>.
	 * 
	 * @return clone of <code>EPPTrademark</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPTrademark clone = (EPPTrademark) super.clone();

		return clone;
	}

	/**
	 * Encode the trademark to a <code>byte[]</code>.
	 * 
	 * @return Encoded trademark
	 * @throws EPPEncodeException
	 *             Error encoding the trademark
	 */
	public byte[] encode() throws EPPEncodeException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			Document doc = new DocumentImpl();
			Element root = this.encode(doc);
			doc.appendChild(root);

			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();

			trans.transform(new DOMSource(root), new StreamResult(os));
		}
		catch (EPPEncodeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			cat.error("Error encoding trademark to byte[]: " + ex);
			throw new EPPEncodeException("Error encoding trademark to byte[]");
		}

		return os.toByteArray();
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
					+ " on in EPPTrademark.encode(Document)");
		}

		// Validated required attributes
		if (this.id == null) {
			throw new EPPEncodeException("id is required for trademark");
		}
		if (this.name == null) {
			throw new EPPEncodeException("name is required for trademark");
		}
		if (this.holders == null || this.holders.isEmpty()) {
			throw new EPPEncodeException("one holder is required for trademark");
		}
		if (this.jurisdiction == null) {
			throw new EPPEncodeException("jurisdiction is required for trademark");
		}
		if (this.goodsAndServices == null) {
			throw new EPPEncodeException("goodsAndServices is required for trademark");
		}
		if (this.regNum == null) {
			throw new EPPEncodeException("regNum is required for trademark");
		}
		if (this.regDate == null) {
			throw new EPPEncodeException("regDate is required for trademark");
		}
		
		
		Element root = aDocument.createElementNS(EPPMark.NS, ELM_NAME);

		// Id
		EPPUtil.encodeString(aDocument, root, this.id, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_ID);

		// Name
		EPPUtil.encodeString(aDocument, root, this.name, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_MARK_NAME);

		// Holders
		EPPUtil.encodeCompList(aDocument, root, this.holders);

		// Contacts
		EPPUtil.encodeCompList(aDocument, root, this.contacts);

		// Jurisdiction
		EPPUtil.encodeString(aDocument, root, this.jurisdiction, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_JURISDICTION);

		// Classes
		EPPUtil.encodeList(aDocument, root, this.classes, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_CLASS);

		// Labels
		EPPUtil.encodeList(aDocument, root, this.labels, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_LABEL);

		// Goods and Services
		EPPUtil.encodeString(aDocument, root, this.goodsAndServices,
				EPPMark.NS, EPPMark.NS_PREFIX + ":" + ELM_GOODS_AND_SERVICES);

		// Application ID
		EPPUtil.encodeString(aDocument, root, this.apId, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_AP_ID);

		// Application Date
		EPPUtil.encodeTimeInstant(aDocument, root, this.apDate, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_AP_DATE);

		// Registration Number
		EPPUtil.encodeString(aDocument, root, this.regNum, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_REG_NUM);

		// Registration Date
		EPPUtil.encodeTimeInstant(aDocument, root, this.regDate, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_REG_DATE);

		// Expiration Date
		EPPUtil.encodeTimeInstant(aDocument, root, this.exDate, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_EX_DATE);

		return root;
	}

	/**
	 * Decode the <code>EPPTrademark</code> attributes from the input
	 * <code>byte[]</code>.
	 * 
	 * @param aMarkArray
	 *            <code>byte[]</code> to decode the attribute values
	 * @throws EPPDecodeException
	 *             Error decoding the <code>byte[]</code>.
	 */
	public void decode(byte[] aMarkArray) throws EPPDecodeException {

		try {
			ByteArrayInputStream is = new ByteArrayInputStream(aMarkArray);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			docFactory.setNamespaceAware(true);
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(is);

			this.decode(doc.getDocumentElement());
		}
		catch (Exception ex) {
			throw new EPPDecodeException("Error decoding trademark array: " + ex);
		}
	}

	/**
	 * Decode the <code>EPPTrademark</code> component
	 * 
	 * @param aElement
	 *            Root element of the <code>EPPTrademark</code>
	 * @throws EPPDecodeException
	 *             Error decoding the <code>EPPTrademark</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Id
		this.id = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_ID);

		// Name
		this.name = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_MARK_NAME);

		// Holders
		this.holders = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPMarkContact.ELM_HOLDER_LOCALNAME, EPPMarkContact.class);

		// Contacts
		this.contacts = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPMarkContact.ELM_CONTACT_LOCALNAME, EPPMarkContact.class);

		// Jurisdiction
		this.jurisdiction = EPPUtil.decodeString(aElement, EPPMark.NS,
				ELM_JURISDICTION);

		// Classes
		this.classes = EPPUtil.decodeList(aElement, EPPMark.NS, ELM_CLASS);

		// Labels
		this.labels = EPPUtil.decodeList(aElement, EPPMark.NS, ELM_LABEL);

		// Goods and Services
		this.goodsAndServices = EPPUtil.decodeString(aElement, EPPMark.NS,
				ELM_GOODS_AND_SERVICES);

		// Application ID
		this.apId = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_AP_ID);

		// Application Date
		this.apDate = EPPUtil.decodeTimeInstant(aElement, EPPMark.NS,
				ELM_AP_DATE);

		// Registration Number
		this.regNum = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_REG_NUM);

		// Registration Date
		this.regDate = EPPUtil.decodeTimeInstant(aElement, EPPMark.NS,
				ELM_REG_DATE);

		// Expiration Date
		this.exDate = EPPUtil.decodeTimeInstant(aElement, EPPMark.NS,
				ELM_EX_DATE);
	}

	/**
	 * implements a deep <code>EPPTrademark</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPTrademark</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPTrademark)) {
			cat.error("EPPTrademark.equals(): aObject is not an EPPTrademark");
			return false;
		}

		EPPTrademark other = (EPPTrademark) aObject;

		// Id
		if (!EqualityUtil.equals(this.id, other.id)) {
			cat.error("EPPTrademark.equals(): id not equal");
			return false;
		}

		// Name
		if (!EqualityUtil.equals(this.name, other.name)) {
			cat.error("EPPTrademark.equals(): name not equal");
			return false;
		}

		// Holders
		if (!EqualityUtil.equals(this.holders, other.holders)) {
			cat.error("EPPTrademark.equals(): holders not equal");
			return false;
		}

		// Contacts
		if (!EqualityUtil.equals(this.contacts, other.contacts)) {
			cat.error("EPPTrademark.equals(): contacts not equal");
			return false;
		}

		// Jurisdiction
		if (!EqualityUtil.equals(this.jurisdiction, other.jurisdiction)) {
			cat.error("EPPTrademark.equals(): jurisdiction not equal");
			return false;
		}

		// Classes
		if (!EqualityUtil.equals(this.classes, other.classes)) {
			cat.error("EPPTrademark.equals(): classes not equal");
			return false;
		}

		// Labels
		if (!EqualityUtil.equals(this.labels, other.labels)) {
			cat.error("EPPTrademark.equals(): labels not equal");
			return false;
		}

		// Goods and Services
		if (!EqualityUtil.equals(this.goodsAndServices, other.goodsAndServices)) {
			cat.error("EPPTrademark.equals(): goodsAndServices not equal");
			return false;
		}

		// Application ID
		if (!EqualityUtil.equals(this.apId, other.apId)) {
			cat.error("EPPTrademark.equals(): apId not equal");
			return false;
		}

		// Application Date
		if (!EqualityUtil.equals(this.apDate, other.apDate)) {
			cat.error("EPPTrademark.equals(): apDate not equal");
			return false;
		}

		// Registration Number
		if (!EqualityUtil.equals(this.regNum, other.regNum)) {
			cat.error("EPPTrademark.equals(): regNum not equal");
			return false;
		}

		// Registration Date
		if (!EqualityUtil.equals(this.regDate, other.regDate)) {
			cat.error("EPPTrademark.equals(): regDate not equal");
			return false;
		}

		// Expiration Date
		if (!EqualityUtil.equals(this.exDate, other.exDate)) {
			cat.error("EPPTrademark.equals(): exDate not equal");
			return false;
		}

		return true;
	}

	/**
	 * Gets the identifier for the trademark.
	 * 
	 * @return The identifier for the trademark if set; <code>null</code> otherwise.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the identifier for the trademark.
	 * 
	 * @param aId
	 *            Identifier for the trademark
	 */
	public void setId(String aId) {
		this.id = aId;
	}

	/**
	 * Gets the registered trademark text string.
	 * 
	 * @return The registered trademark text string if set; <code>null</code>
	 *         otherwise.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the registered trademark text string.
	 * 
	 * @param aName
	 *            the registered trademark text string
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Gets holders of the trademark.
	 * 
	 * @return the holders of the trademark if set; Empty <code>List</code>
	 *         otherwise.
	 */
	public List<EPPMarkContact> getHolders() {
		return this.holders;
	}

	/**
	 * Sets holders of the trademark.
	 * 
	 * @param aHolders
	 *            the holders of the trademark
	 */
	public void setHolders(List<EPPMarkContact> aHolders) {
		this.holders = aHolders;
		if (this.holders == null) {
			this.holders = new ArrayList<EPPMarkContact>();
		}

		// Ensure to set the local name
		for (EPPMarkContact holder : this.holders) {
			holder.setLocalName(EPPMarkContact.ELM_HOLDER_LOCALNAME);
		}
	}

	/**
	 * Adds a holder to the list of holders of the trademark.
	 * 
	 * @param aHolder
	 *            Holder to add to list of holders of the trademark.
	 */
	public void addHolder(EPPMarkContact aHolder) {
		if (aHolder == null) {
			return;
		}

		aHolder.setLocalName(EPPMarkContact.ELM_HOLDER_LOCALNAME);

		if (this.holders == null) {
			this.holders = new ArrayList<EPPMarkContact>();
		}

		this.holders.add(aHolder);
	}

	/**
	 * Gets contacts of the trademark.
	 * 
	 * @return the contacts of the trademark if set; Empty <code>List</code>
	 *         otherwise.
	 */
	public List<EPPMarkContact> getContacts() {
		return this.contacts;
	}

	/**
	 * Sets contacts of the trademark.
	 * 
	 * @param aContacts
	 *            the contacts of the trademark
	 */
	public void setContacts(List<EPPMarkContact> aContacts) {
		this.contacts = aContacts;
		if (this.contacts == null) {
			this.contacts = new ArrayList<EPPMarkContact>();
		}

		// Ensure to set the local name
		for (EPPMarkContact contact : this.contacts) {
			contact.setLocalName(EPPMarkContact.ELM_CONTACT_LOCALNAME);
		}
	}

	/**
	 * Adds a contact to the list of contacts of the trademark.
	 * 
	 * @param aContact
	 *            Contact to add to list of contacts of the trademark.
	 */
	public void addContact(EPPMarkContact aContact) {
		if (aContact == null) {
			return;
		}

		aContact.setLocalName(EPPMarkContact.ELM_CONTACT_LOCALNAME);

		if (this.contacts == null) {
			this.contacts = new ArrayList<EPPMarkContact>();
		}

		this.contacts.add(aContact);
	}

	/**
	 * Gets the two-character code of the jurisdiction where the trademark was
	 * registered. This is a two-character code from [WIPO.ST3].
	 * 
	 * @return Jurisdiction if set; <code>null</code> otherwise.
	 */
	public String getJurisdiction() {
		return this.jurisdiction;
	}

	/**
	 * Sets the two-character code of the jurisdiction where the trademark was
	 * registered. This is a two-character code from [WIPO.ST3].
	 * 
	 * @param aJurisdiction
	 *            Jurisdiction where trademark was registered.
	 */
	public void setJurisdiction(String aJurisdiction) {
		this.jurisdiction = aJurisdiction;
	}

	/**
	 * Gets list of Nice Classification class numbers.
	 * 
	 * @return list of Nice Classification class numbers if defined; empty list
	 *         otherwise.
	 */
	public List<String> getClasses() {
		return this.classes;
	}

	/**
	 * Sets list of Nice Classification class numbers.
	 * 
	 * @param aClasses
	 *            list of Nice Classification class numbers
	 */
	public void setClasses(List<String> aClasses) {
		this.classes = aClasses;

		if (this.classes == null) {
			this.classes = new ArrayList<String>();
		}
	}

	/**
	 * Adds a Nice Classification class number to the list of classes.
	 * 
	 * @param aClass
	 *            Nice Classification class number to add
	 */
	public void addClass(String aClass) {
		if (this.classes == null) {
			this.classes = new ArrayList<String>();
		}

		this.classes.add(aClass);
	}

	/**
	 * Gets the domain name labels that corresponds to the trademark.
	 * 
	 * @return the domain name labels that corresponds to the trademark if set; Empty
	 *         <code>List</code> otherwise.
	 */
	public List<String> getLabels() {
		return this.labels;
	}

	/**
	 * Sets the domain name labels that corresponds to the trademark.
	 * 
	 * @param aLabels
	 *            the domain name labels that corresponds to the trademark
	 */
	public void setLabels(List<String> aLabels) {
		this.labels = aLabels;
		if (this.labels == null) {
			this.labels = new ArrayList<String>();
		}
	}

	/**
	 * Adds a domain name label to the domain name labels that corresponds to
	 * the trademark.
	 * 
	 * @param aLabel
	 *            Domain name label to add.
	 */
	public void addLabel(String aLabel) {
		if (this.labels == null) {
			this.labels = new ArrayList<String>();
		}

		this.labels.add(aLabel);
	}

	/**
	 * Gets full description of the goods and services mentioned in the trademark
	 * registration document.
	 * 
	 * @return The goods and services if set; <code>null</code> otherwise.
	 */
	public String getGoodsAndServices() {
		return this.goodsAndServices;
	}

	/**
	 * Sets full description of the goods and services mentioned in the trademark
	 * registration document.
	 * 
	 * @param aGoodsAndServices
	 *            The goods and services
	 */
	public void setGoodsAndServices(String aGoodsAndServices) {
		this.goodsAndServices = aGoodsAndServices;
	}

	/**
	 * Gets the application ID registered in the trademark office.
	 * 
	 * @return Application ID registered in the trademark office if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getApId() {
		return this.apId;
	}

	/**
	 * Sets the application ID registered in the trademark office.
	 * 
	 * @param aApId
	 *            Application ID registered in the trademark office.
	 */
	public void setApId(String aApId) {
		this.apId = aApId;
	}

	/**
	 * Gets the date the trademark was applied for.
	 * 
	 * @return The date the trademark was applied for if defined;
	 *         <code>null</code> otherwise.
	 */
	public Date getApDate() {
		return this.apDate;
	}

	/**
	 * Sets the date the trademark was applied for.
	 * 
	 * @param aApDate
	 *            The date the trademark was applied for.
	 */
	public void setApDate(Date aApDate) {
		this.apDate = aApDate;
	}

	/**
	 * Gets the trademark registration ID (number) registered in the trademark
	 * office.
	 * 
	 * @return The trademark registration ID (number) registered in the
	 *         trademark office.
	 */
	public String getRegNum() {
		return this.regNum;
	}

	/**
	 * Sets the trademark registration ID (number) registered in the trademark
	 * office.
	 * 
	 * @param aRegNum
	 *            The trademark registration ID (number) registered in the
	 *            trademark office.
	 */
	public void setRegNum(String aRegNum) {
		this.regNum = aRegNum;
	}

	/**
	 * Gets the date the trademark was registered.
	 * 
	 * @return The date the trademark was registered if set; <code>null</code>
	 *         otherwise.
	 */
	public Date getRegDate() {
		return this.regDate;
	}

	/**
	 * Sets the date the trademark was registered.
	 * 
	 * @param aRegDate
	 *            The date the trademark was registered
	 */
	public void setRegDate(Date aRegDate) {
		this.regDate = aRegDate;
	}

	/**
	 * Gets the date of expiration of the trademark.
	 * 
	 * @return the date of expiration of the trademark if set; <code>null</code>
	 *         otherwise.
	 */
	public Date getExDate() {
		return this.exDate;
	}

	/**
	 * Sets the date of expiration of the trademark.
	 * 
	 * @param aExDate
	 *            The date of expiration of the trademark
	 */
	public void setExDate(Date aExDate) {
		this.exDate = aExDate;
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