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
 * Class for a Treaty or Statute that can be included in the list of treaty or
 * statutes contained in the {@link EPPMark}.
 * 
 * @see EPPMark
 */
public class EPPTreatyOrStatute implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPTreatyOrStatute.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the treaty or statute local name
	 */
	public static final String ELM_LOCALNAME = "treatyOrStatute";

	/**
	 * Constant for the treaty or statute tag
	 */
	public static final String ELM_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for identifier of the treaty or statute
	 */
	private static final String ELM_ID = "id";

	/**
	 * Element local name for the registered treaty or statute text string
	 */
	private static final String ELM_MARK_NAME = "markName";

	/**
	 * Element local name for domain name A-label for the treaty or statute
	 */
	private static final String ELM_LABEL = "label";

	/**
	 * Element local name for name of the goods and services
	 */
	private static final String ELM_GOODS_AND_SERVICES = "goodsAndServices";

	/**
	 * Element local the number of the mark of the treaty or statute.
	 */
	private static final String ELM_REF_NUM = "refNum";

	/**
	 * Element local name of the date of protection of the mark.
	 */
	private static final String ELM_PRO_DATE = "proDate";

	/**
	 * Element local name for the title of the treaty or statute.
	 */
	private static final String ELM_TITLE = "title";

	/**
	 * Element local name execution date of the treaty or statute.
	 */
	private static final String ELM_EXEC_DATE = "execDate";

	/**
	 * Identifier for the treaty or statute
	 */
	private String id;

	/**
	 * The registered treaty or statute text string.
	 */
	private String name;

	/**
	 * One or more treaty or statute holders that contain information of the
	 * holder of the mark. An "entitlement" attribute attribute is
	 * used to identify the entitlement of the holder, possible values are:
	 * owner, assignee and licensee.
	 */
	private List<EPPMarkContact> holders = new ArrayList<EPPMarkContact>();

	/**
	 * Zero or more contacts that contains the information of the representation
	 * of the mark registration. A "type" attribute is used to
	 * identify the type of contact, possible values are: owner, agent or
	 * thirdparty.
	 */
	private List<EPPMarkContact> contacts = new ArrayList<EPPMarkContact>();

	/**
	 * One or more &lt;mark:protection&gt; elements that contain the countries and
	 * region of the country where the mark is protected.
	 */
	private List<EPPProtection> protections = new ArrayList<EPPProtection>();

	/**
	 * Zero or more domain name A-labels that corresponds to the treaty or
	 * statute name.
	 */
	private List<String> labels = new ArrayList<String>();

	/**
	 * Contains the full description of the goods and services mentioned in the
	 * treaty or statute registration document.
	 */
	private String goodsAndServices;

	/**
	 * The number of the mark of the treaty or statute.
	 */
	private String refNum;

	/**
	 * The date of protection of the mark.
	 */
	private Date proDate;

	/**
	 * The title of the treaty or statute.
	 */
	private String title;

	/**
	 * the execution date of the treaty or statute.
	 */
	private Date execDate;

	/**
	 * Create an <code>EPPTreatyOrStatute</code> instance. Use the setter
	 * methods to set the attributes of the instance.
	 */
	public EPPTreatyOrStatute() {
	}

	/**
	 * Clone <code>EPPTreatyOrStatute</code>.
	 * 
	 * @return clone of <code>EPPTreatyOrStatute</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPTreatyOrStatute clone = (EPPTreatyOrStatute) super.clone();

		return clone;
	}

	/**
	 * Encode the treaty or statute to a <code>byte[]</code>.
	 * 
	 * @return Encoded treaty or statute
	 * @throws EPPEncodeException
	 *             Error encoding the treaty or statute
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
			cat.error("Error encoding treaty or statute to byte[]: " + ex);
			throw new EPPEncodeException(
					"Error encoding treaty or statute to byte[]");
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
					+ " on in EPPTreatyOrStatute.encode(Document)");
		}
		
		// Validated required attributes
		if (this.id == null) {
			throw new EPPEncodeException("id is required for treaty or statute");
		}
		if (this.name == null) {
			throw new EPPEncodeException("name is required for treaty or statute");
		}
		if (this.holders == null || this.holders.isEmpty()) {
			throw new EPPEncodeException("one holder is required for treaty or statute");
		}
		if (this.protections == null || this.protections.isEmpty()) {
			throw new EPPEncodeException("one protection is required for treaty or statute");
		}
		if (this.goodsAndServices == null) {
			throw new EPPEncodeException("goodsAndServices is required for treaty or statute");
		}
		if (this.refNum == null) {
			throw new EPPEncodeException("refNum is required for treaty or statute");
		}
		if (this.proDate == null) {
			throw new EPPEncodeException("proDate is required for treaty or statute");
		}
		if (this.title == null) {
			throw new EPPEncodeException("title is required for treaty or statute");
		}
		if (this.proDate == null) {
			throw new EPPEncodeException("proDate is required for treaty or statute");
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

		// Protections
		EPPUtil.encodeCompList(aDocument, root, this.protections);

		// Labels
		EPPUtil.encodeList(aDocument, root, this.labels, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_LABEL);

		// Goods and Services
		EPPUtil.encodeString(aDocument, root, this.goodsAndServices,
				EPPMark.NS, EPPMark.NS_PREFIX + ":" + ELM_GOODS_AND_SERVICES);
		
		// Reference Number
		EPPUtil.encodeString(aDocument, root, this.refNum, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_REF_NUM);

		// Protection Date
		EPPUtil.encodeTimeInstant(aDocument, root, this.proDate, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_PRO_DATE);

		// Title
		EPPUtil.encodeString(aDocument, root, this.title, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_TITLE);

		// Execution Date
		EPPUtil.encodeTimeInstant(aDocument, root, this.execDate, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_EXEC_DATE);

		return root;
	}

	/**
	 * Decode the <code>EPPTreatyOrStatute</code> attributes from the input
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
			throw new EPPDecodeException(
					"Error decoding treaty or statute array: " + ex);
		}
	}

	/**
	 * Decode the <code>EPPTreatyOrStatute</code> component
	 * 
	 * @param aElement
	 *            Root element of the <code>EPPTreatyOrStatute</code>
	 * @throws EPPDecodeException
	 *             Error decoding the <code>EPPTreatyOrStatute</code>
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

		// Protections
		this.protections = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPProtection.ELM_LOCALNAME, EPPProtection.class);

		// Labels
		this.labels = EPPUtil.decodeList(aElement, EPPMark.NS, ELM_LABEL);

		// Goods and Services
		this.goodsAndServices = EPPUtil.decodeString(aElement, EPPMark.NS,
				ELM_GOODS_AND_SERVICES);

		// Reference Number
		this.refNum = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_REF_NUM);

		// Protection Date
		this.proDate = EPPUtil.decodeTimeInstant(aElement, EPPMark.NS,
				ELM_PRO_DATE);

		// Title
		this.title = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_TITLE);

		// Execution Date
		this.execDate = EPPUtil.decodeTimeInstant(aElement, EPPMark.NS,
				ELM_EXEC_DATE);
	}

	/**
	 * implements a deep <code>EPPTreatyOrStatute</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPTreatyOrStatute</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPTreatyOrStatute)) {
			cat.error("EPPTreatyOrStatute.equals(): aObject is not an EPPTreatyOrStatute");
			return false;
		}

		EPPTreatyOrStatute other = (EPPTreatyOrStatute) aObject;

		// Id
		if (!EqualityUtil.equals(this.id, other.id)) {
			cat.error("EPPTreatyOrStatute.equals(): id not equal");
			return false;
		}

		// Name
		if (!EqualityUtil.equals(this.name, other.name)) {
			cat.error("EPPTreatyOrStatute.equals(): name not equal");
			return false;
		}

		// Holders
		if (!EqualityUtil.equals(this.holders, other.holders)) {
			cat.error("EPPTreatyOrStatute.equals(): holders not equal");
			return false;
		}

		// Contacts
		if (!EqualityUtil.equals(this.contacts, other.contacts)) {
			cat.error("EPPTreatyOrStatute.equals(): contacts not equal");
			return false;
		}

		// Protections
		if (!EqualityUtil.equals(this.protections, other.protections)) {
			cat.error("EPPTreatyOrStatute.equals(): protections not equal");
			return false;
		}

		// Labels
		if (!EqualityUtil.equals(this.labels, other.labels)) {
			cat.error("EPPTreatyOrStatute.equals(): labels not equal");
			return false;
		}

		// Goods and Services
		if (!EqualityUtil.equals(this.goodsAndServices, other.goodsAndServices)) {
			cat.error("EPPTreatyOrStatute.equals(): goodsAndServices not equal");
			return false;
		}

		// Reference Number
		if (!EqualityUtil.equals(this.refNum, other.refNum)) {
			cat.error("EPPTreatyOrStatute.equals(): refNum not equal");
			return false;
		}

		// Protection Date
		if (!EqualityUtil.equals(this.proDate, other.proDate)) {
			cat.error("EPPTreatyOrStatute.equals(): proDate not equal");
			return false;
		}

		// Title
		if (!EqualityUtil.equals(this.title, other.title)) {
			cat.error("EPPTreatyOrStatute.equals(): title not equal");
			return false;
		}

		// Execution Date
		if (!EqualityUtil.equals(this.execDate, other.execDate)) {
			cat.error("EPPTreatyOrStatute.equals(): execDate not equal");
			return false;
		}

		return true;
	}

	/**
	 * Gets the identifier for the treaty or statute.
	 * 
	 * @return The identifier for the treaty or statute if set;
	 *         <code>null</code> otherwise.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the identifier for the treaty or statute.
	 * 
	 * @param aId
	 *            Identifier for the treaty or statute
	 */
	public void setId(String aId) {
		this.id = aId;
	}

	/**
	 * Gets the registered treaty or statute text string.
	 * 
	 * @return The registered treaty or statute text string if set;
	 *         <code>null</code> otherwise.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the registered treaty or statute text string.
	 * 
	 * @param aName
	 *            the registered treaty or statute text string
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Gets holders of the treaty or statute.
	 * 
	 * @return the holders of the treaty or statute if set; Empty
	 *         <code>List</code> otherwise.
	 */
	public List<EPPMarkContact> getHolders() {
		return this.holders;
	}

	/**
	 * Sets holders of the treaty or statute.
	 * 
	 * @param aHolders
	 *            the holders of the treaty or statute
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
	 * Adds a holder to the list of holders of the treaty or statute.
	 * 
	 * @param aHolder
	 *            Holder to add to list of holders of the treaty or statute.
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
	 * Gets contacts of the treaty or statute.
	 * 
	 * @return the contacts of the treaty or statute if set; Empty
	 *         <code>List</code> otherwise.
	 */
	public List<EPPMarkContact> getContacts() {
		return this.contacts;
	}

	/**
	 * Sets contacts of the treaty or statute.
	 * 
	 * @param aContacts
	 *            the contacts of the treaty or statute
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
	 * Adds a contact to the list of contacts of the treaty or statute.
	 * 
	 * @param aContact
	 *            Contact to add to list of contacts of the treaty or statute.
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
	 * Gets protections of the treaty or statute.
	 * 
	 * @return the protections of the treaty or statute if set; Empty
	 *         <code>List</code> otherwise.
	 */
	public List<EPPProtection> getProtections() {
		return this.protections;
	}

	/**
	 * Sets protections of the treaty or statute.
	 * 
	 * @param aProtections
	 *            the protections of the treaty or statute
	 */
	public void setProtections(List<EPPProtection> aProtections) {
		this.protections = aProtections;
		if (this.protections == null) {
			this.protections = new ArrayList<EPPProtection>();
		}
	}

	/**
	 * Adds a protection to the list of protections of the treaty or statute.
	 * 
	 * @param aProtection
	 *            Protection to add to list of protections of the treaty or
	 *            statute.
	 */
	public void addProtection(EPPProtection aProtection) {
		if (aProtection == null) {
			return;
		}

		if (this.protections == null) {
			this.protections = new ArrayList<EPPProtection>();
		}

		this.protections.add(aProtection);
	}

	/**
	 * Gets the domain name labels that corresponds to the treaty or statute.
	 * 
	 * @return the domain name labels that corresponds to the treaty or statute
	 *         if set; Empty <code>List</code> otherwise.
	 */
	public List<String> getLabels() {
		return this.labels;
	}

	/**
	 * Sets the domain name labels that corresponds to the treaty or statute.
	 * 
	 * @param aLabels
	 *            the domain name labels that corresponds to the treaty or
	 *            statute
	 */
	public void setLabels(List<String> aLabels) {
		this.labels = aLabels;
		if (this.labels == null) {
			this.labels = new ArrayList<String>();
		}
	}

	/**
	 * Adds a domain name label to the domain name labels that corresponds to
	 * the treaty or statute.
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
	 * Gets full description of the goods and services mentioned in the treaty
	 * or statute registration document.
	 * 
	 * @return The goods and services if set; <code>null</code> otherwise.
	 */
	public String getGoodsAndServices() {
		return this.goodsAndServices;
	}

	/**
	 * Sets full description of the goods and services mentioned in the treaty
	 * or statute registration document.
	 * 
	 * @param aGoodsAndServices
	 *            The goods and services
	 */
	public void setGoodsAndServices(String aGoodsAndServices) {
		this.goodsAndServices = aGoodsAndServices;
	}

	/**
	 * Gets the reference number of the mark of the treaty or statute.
	 * 
	 * @return Reference number of the mark of the treaty or statute if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getRefNum() {
		return this.refNum;
	}

	/**
	 * Sets the reference number of the mark of the treaty or statute.
	 * 
	 * @param aRefNum
	 *            Reference number of the mark of the treaty or statute.
	 */
	public void setRefNum(String aRefNum) {
		this.refNum = aRefNum;
	}

	/**
	 * Gets the date of protection of the mark.
	 * 
	 * @return The date of protection of the mark if defined; <code>null</code>
	 *         otherwise.
	 */
	public Date getProDate() {
		return this.proDate;
	}

	/**
	 * Sets the date of protection of the mark.
	 * 
	 * @param aProDate
	 *            The date of protection of the mark.
	 */
	public void setProDate(Date aProDate) {
		this.proDate = aProDate;
	}

	/**
	 * Gets the title of the treaty or statute.
	 * 
	 * @return The title of the treaty or statute.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title of the treaty or statute.
	 * 
	 * @param aTitle
	 *            The title of the treaty or statute.
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	/**
	 * Gets the execution date of the treaty or statute.
	 * 
	 * @return The execution date of the treaty or statute if set;
	 *         <code>null</code> otherwise.
	 */
	public Date getExecDate() {
		return this.execDate;
	}

	/**
	 * Sets the execution date of the treaty or statute.
	 * 
	 * @param aExecDate
	 *            The execution date of the treaty or statute
	 */
	public void setExecDate(Date aExecDate) {
		this.execDate = aExecDate;
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