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
 * Class for a Court that can be included in the list of courts contained in the
 * {@link EPPMark}.
 * 
 * @see EPPMark
 */
public class EPPCourt implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPCourt.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the court local name
	 */
	public static final String ELM_LOCALNAME = "court";

	/**
	 * Constant for the court tag
	 */
	public static final String ELM_NAME = EPPMark.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for identifier of the court
	 */
	private static final String ELM_ID = "id";

	/**
	 * Element local name for the registered court text string
	 */
	private static final String ELM_MARK_NAME = "markName";

	/**
	 * Element local name for domain name A-label for the court
	 */
	private static final String ELM_LABEL = "label";

	/**
	 * Element local name for name of the goods and services
	 */
	private static final String ELM_GOODS_AND_SERVICES = "goodsAndServices";

	/**
	 * Element local the number of the mark of the court's opinion.
	 */
	private static final String ELM_REF_NUM = "refNum";

	/**
	 * Element local name of the date of protection of the mark.
	 */
	private static final String ELM_PRO_DATE = "proDate";

	/**
	 * Element local name for the two-character code of the country in which the
	 * court is located.
	 */
	private static final String ELM_CC = "cc";

	/**
	 * Element local name for the name of the city, state, province or other
	 * geographic region in which the mark is protected
	 */
	private static final String ELM_REGION = "region";

	/**
	 * Element local name for the name of the court
	 */
	private static final String ELM_COURT_NAME = "courtName";

	/**
	 * Identifier for the court
	 */
	private String id;

	/**
	 * The registered text string.
	 */
	private String name;

	/**
	 * One or more court holders that contain information of the holder of the
	 * mark. An "entitlement" attribute attribute is used to identify the
	 * entitlement of the holder, possible values are: owner, assignee and
	 * licensee.
	 */
	private List<EPPMarkContact> holders = new ArrayList<EPPMarkContact>();

	/**
	 * Zero or more contacts that contains the information of the representation
	 * of the mark registration. A "type" attribute is used to identify the type
	 * of contact, possible values are: owner, agent or thirdparty.
	 */
	private List<EPPMarkContact> contacts = new ArrayList<EPPMarkContact>();

	/**
	 * Zero or more domain name A-labels that corresponds to the treaty or
	 * statute name.
	 */
	private List<String> labels = new ArrayList<String>();

	/**
	 * Contains the full description of the goods and services mentioned in the
	 * court registration document.
	 */
	private String goodsAndServices;

	/**
	 * The number of the mark of the court's opinion.
	 */
	private String refNum;

	/**
	 * The date of protection of the mark.
	 */
	private Date proDate;

	/**
	 * The two-character code of the country in which the court is located.
	 */
	private String cc;

	/**
	 * Zero or more regions that contain the name of a city, state, province or
	 * other geographic region in which the mark is protected.
	 */
	private List<String> regions = new ArrayList<String>();

	/**
	 * The name of the court.
	 */
	private String courtName;

	/**
	 * Create an <code>EPPCourt</code> instance. Use the setter methods to set
	 * the attributes of the instance.
	 */
	public EPPCourt() {
	}

	/**
	 * Clone <code>EPPCourt</code>.
	 * 
	 * @return clone of <code>EPPCourt</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPCourt clone = (EPPCourt) super.clone();

		return clone;
	}

	/**
	 * Encode the court to a <code>byte[]</code>.
	 * 
	 * @return Encoded court
	 * @throws EPPEncodeException
	 *             Error encoding the court
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
			cat.error("Error encoding court to byte[]: " + ex);
			throw new EPPEncodeException("Error encoding court to byte[]");
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
					+ " on in EPPCourt.encode(Document)");
		}

		// Validated required attributes
		if (this.id == null) {
			throw new EPPEncodeException("id is required for court");
		}
		if (this.name == null) {
			throw new EPPEncodeException("name is required for court");
		}
		if (this.holders == null || this.holders.isEmpty()) {
			throw new EPPEncodeException("one holder is required for court");
		}
		if (this.goodsAndServices == null) {
			throw new EPPEncodeException(
					"goodsAndServices is required for court");
		}
		if (this.refNum == null) {
			throw new EPPEncodeException("refNum is required for court");
		}
		if (this.proDate == null) {
			throw new EPPEncodeException("proDate is required for court");
		}
		if (this.proDate == null) {
			throw new EPPEncodeException("proDate is required for court");
		}
		if (this.cc == null) {
			throw new EPPEncodeException("cc is required for court");
		}
		if (this.courtName == null) {
			throw new EPPEncodeException("courtName is required for court");
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

		// Country Code
		EPPUtil.encodeString(aDocument, root, this.cc, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_CC);

		// Regions
		EPPUtil.encodeList(aDocument, root, this.regions, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_REGION);

		// Court Name
		EPPUtil.encodeString(aDocument, root, this.courtName, EPPMark.NS,
				EPPMark.NS_PREFIX + ":" + ELM_COURT_NAME);

		return root;
	}

	/**
	 * Decode the <code>EPPCourt</code> attributes from the input
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
			throw new EPPDecodeException("Error decoding court array: " + ex);
		}
	}

	/**
	 * Decode the <code>EPPCourt</code> component
	 * 
	 * @param aElement
	 *            Root element of the <code>EPPCourt</code>
	 * @throws EPPDecodeException
	 *             Error decoding the <code>EPPCourt</code>
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

		// Country Code
		this.cc = EPPUtil.decodeString(aElement, EPPMark.NS, ELM_CC);

		// Regions
		this.regions = EPPUtil.decodeList(aElement, EPPMark.NS, ELM_REGION);

		// Court Name
		this.courtName = EPPUtil.decodeString(aElement, EPPMark.NS,
				ELM_COURT_NAME);
	}

	/**
	 * implements a deep <code>EPPCourt</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPCourt</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPCourt)) {
			cat.error("EPPCourt.equals(): aObject is not an EPPCourt");
			return false;
		}

		EPPCourt other = (EPPCourt) aObject;

		// Id
		if (!EqualityUtil.equals(this.id, other.id)) {
			cat.error("EPPCourt.equals(): id not equal");
			return false;
		}

		// Name
		if (!EqualityUtil.equals(this.name, other.name)) {
			cat.error("EPPCourt.equals(): name not equal");
			return false;
		}

		// Holders
		if (!EqualityUtil.equals(this.holders, other.holders)) {
			cat.error("EPPCourt.equals(): holders not equal");
			return false;
		}

		// Contacts
		if (!EqualityUtil.equals(this.contacts, other.contacts)) {
			cat.error("EPPCourt.equals(): contacts not equal");
			return false;
		}

		// Labels
		if (!EqualityUtil.equals(this.labels, other.labels)) {
			cat.error("EPPCourt.equals(): labels not equal");
			return false;
		}

		// Goods and Services
		if (!EqualityUtil.equals(this.goodsAndServices, other.goodsAndServices)) {
			cat.error("EPPCourt.equals(): goodsAndServices not equal");
			return false;
		}

		// Reference Number
		if (!EqualityUtil.equals(this.refNum, other.refNum)) {
			cat.error("EPPCourt.equals(): refNum not equal");
			return false;
		}

		// Protection Date
		if (!EqualityUtil.equals(this.proDate, other.proDate)) {
			cat.error("EPPCourt.equals(): proDate not equal");
			return false;
		}

		// Country Code
		if (!EqualityUtil.equals(this.cc, other.cc)) {
			cat.error("EPPCourt.equals(): cc not equal");
			return false;
		}

		// Regions
		if (!EqualityUtil.equals(this.regions, other.regions)) {
			cat.error("EPPCourt.equals(): regions not equal");
			return false;
		}

		// Court Name
		if (!EqualityUtil.equals(this.courtName, other.courtName)) {
			cat.error("EPPCourt.equals(): courtName not equal");
			return false;
		}

		return true;
	}

	/**
	 * Gets the identifier for the court.
	 * 
	 * @return The identifier for the court if set; <code>null</code> otherwise.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the identifier for the court.
	 * 
	 * @param aId
	 *            Identifier for the court
	 */
	public void setId(String aId) {
		this.id = aId;
	}

	/**
	 * Gets the registered court text string.
	 * 
	 * @return The registered court text string if set; <code>null</code>
	 *         otherwise.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the registered court text string.
	 * 
	 * @param aName
	 *            the registered court text string
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Gets holders of the court.
	 * 
	 * @return the holders of the court if set; Empty <code>List</code>
	 *         otherwise.
	 */
	public List<EPPMarkContact> getHolders() {
		return this.holders;
	}

	/**
	 * Sets holders of the court.
	 * 
	 * @param aHolders
	 *            the holders of the court
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
	 * Adds a holder to the list of holders of the court.
	 * 
	 * @param aHolder
	 *            Holder to add to list of holders of the court.
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
	 * Gets contacts of the court.
	 * 
	 * @return the contacts of the court if set; Empty <code>List</code>
	 *         otherwise.
	 */
	public List<EPPMarkContact> getContacts() {
		return this.contacts;
	}

	/**
	 * Sets contacts of the court.
	 * 
	 * @param aContacts
	 *            the contacts of the court
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
	 * Adds a contact to the list of contacts of the court.
	 * 
	 * @param aContact
	 *            Contact to add to list of contacts of the court.
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
	 * Gets the domain name labels that corresponds to the court.
	 * 
	 * @return the domain name labels that corresponds to the court if set;
	 *         Empty <code>List</code> otherwise.
	 */
	public List<String> getLabels() {
		return this.labels;
	}

	/**
	 * Sets the domain name labels that corresponds to the court.
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
	 * the court.
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
	 * Gets the reference number of the mark of the court's opinion.
	 * 
	 * @return Reference number of the mark of the court's opinion if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getRefNum() {
		return this.refNum;
	}

	/**
	 * Sets the reference number of the mark of the court's opinion.
	 * 
	 * @param aRefNum
	 *            Reference number of the mark of the court's opinion.
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
	 * Gets two-character code of the country where the court is located from
	 * [ISO3166-2].
	 * 
	 * @return Two-character code of the country where the court is located if
	 *         set; <code>null</code> otherwise.
	 */
	public String getCc() {
		return this.cc;
	}

	/**
	 * Sets two-character code of the country where the court is located from
	 * [ISO3166-2].
	 * 
	 * @param aCc
	 *            Two-character code of the country where the court is located
	 *            from [ISO3166-2].
	 */
	public void setCc(String aCc) {
		this.cc = aCc;
	}

	/**
	 * Gets the regions where the mark is protected. A region is the name of a
	 * city, state, province or other geographic location.
	 * 
	 * @return The regions where the mark is protected if set; Empty
	 *         <code>List</code> otherwise.
	 */
	public List<String> getRegions() {
		return this.regions;
	}

	/**
	 * Sets the regions where the mark is protected. A region is the name of a
	 * city, state, province or other geographic location.
	 * 
	 * @param aRegions
	 *            The regions where the mark is protected.
	 */
	public void setRegions(List<String> aRegions) {
		this.regions = aRegions;
		if (this.regions == null) {
			this.regions = new ArrayList<String>();
		}
	}

	/**
	 * Adds a region to the list or regions where the mark is protected.
	 * 
	 * @param aRegion
	 *            Region where the mark is protected.
	 */
	public void addRegions(String aRegion) {
		if (this.regions == null) {
			this.regions = new ArrayList<String>();
		}

		this.regions.add(aRegion);
	}
	
	
    /**
     * Gets the name of the court.
     * 
     * @return The name of the court if set; <code>null</code> otherwise.
     */
	public String getCourtName() {
		return this.courtName;
	}

	/**
	 * Sets the name of the court.
	 * 
	 * @param aCourtName The name of the court.
	 */
	public void setCourtName(String aCourtName) {
		this.courtName = aCourtName;
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