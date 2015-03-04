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
import com.verisign.epp.codec.launch.EPPLaunchCodeMark;
import com.verisign.epp.codec.launch.EPPLaunchExtFactory;
import com.verisign.epp.codec.signedMark.EPPSignedMark;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class for a Mark that can contain a list of trademarks, treaty or statutes,
 * and courts. The Mark includes a set of attributes that are used to verify the
 * eligibility for the registration of a domain name.
 */
public class EPPMark implements EPPCodecComponent {

	/** Namespace URI associated with EPPLaunchExtFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:mark-1.0";

	/** Namespace prefix associated with EPPLaunchExtFactory. */
	public static final String NS_PREFIX = "mark";

	/** XML Schema definition for EPPLaunchExtFactory */
	public static final String NS_SCHEMA = "urn:ietf:params:xml:ns:mark-1.0 mark-1.0.xsd";

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPMark.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the mark local name
	 */
	public static final String ELM_LOCALNAME = "mark";

	/**
	 * Constant for the mark tag
	 */
	public static final String ELM_NAME = NS_PREFIX + ":" + ELM_LOCALNAME;

	/**
	 * List of trademarks
	 */
	List<EPPTrademark> trademarks;

	/**
	 * List of treaty or statutes
	 */
	List<EPPTreatyOrStatute> treatyOrStatutes;

	/**
	 * List of courts
	 */
	List<EPPCourt> courts;

	/**
	 * Create an <code>EPPMark</code> instance. Use the setter methods to set
	 * the attributes of the instance.
	 */
	public EPPMark() {
	}

	/**
	 * Create an <code>EPPMark</code> instance with each of the supported lists
	 * including trademarks, treaty and statutes, and courts.
	 * 
	 * @param aTrademarks
	 *            List of trademarks if defined; <code>null</code> otherwise.
	 * @param aTreatyOrStatutes
	 *            List of treaty or statutes if defined; <code>null</code>
	 *            otherwise.
	 * @param aCourts
	 *            List of courts if defined; <code>null</code> otherwise.
	 */
	public EPPMark(List<EPPTrademark> aTrademarks,
			List<EPPTreatyOrStatute> aTreatyOrStatutes, List<EPPCourt> aCourts) {
		this.trademarks = aTrademarks;
		this.treatyOrStatutes = aTreatyOrStatutes;
		this.courts = aCourts;
	}

	/**
	 * Clone <code>EPPMark</code>.
	 * 
	 * @return clone of <code>EPPMark</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPMark clone = (EPPMark) super.clone();
		return clone;
	}

	/**
	 * Encode the mark to a <code>byte[]</code>.
	 * 
	 * @return Encoded mark
	 * @throws EPPEncodeException
	 *             Error encoding the mark
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
			cat.error("Error encoding mark to byte[]: " + ex);
			throw new EPPEncodeException("Error encoding mark to byte[]");
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
					+ " on in EPPMark.encode(Document)");
		}

		Element root = aDocument.createElementNS(NS, ELM_NAME);

		// Trademarks
		EPPUtil.encodeCompList(aDocument, root, this.trademarks);

		// Treaty or Statutes
		EPPUtil.encodeCompList(aDocument, root, this.treatyOrStatutes);

		// Courts
		EPPUtil.encodeCompList(aDocument, root, this.courts);
		return root;
	}

	/**
	 * Decode the <code>EPPMark</code> attributes from the input
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
			throw new EPPDecodeException("Error decoding mark array: " + ex);
		}
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

		// Trademarks
		this.trademarks = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPTrademark.ELM_NAME, EPPTrademark.class);
		if (this.trademarks != null && this.trademarks.isEmpty()) {
			this.trademarks = null;
		}

		// Treaty of Statutes
		this.treatyOrStatutes = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPTreatyOrStatute.ELM_NAME, EPPTreatyOrStatute.class);
		if (this.treatyOrStatutes != null && this.treatyOrStatutes.isEmpty()) {
			this.treatyOrStatutes = null;
		}

		// Courts
		this.courts = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPCourt.ELM_NAME, EPPCourt.class);
		if (this.courts != null && this.courts.isEmpty()) {
			this.courts = null;
		}
	}

	/**
	 * implements a deep <code>EPPMark</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPMark</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPMark)) {
			cat.error("EPPMark.equals(): aObject is not an EPPMark");
			return false;
		}

		EPPMark other = (EPPMark) aObject;

		// Trademarks
		if (!EqualityUtil.equals(this.trademarks, other.trademarks)) {
			cat.error("EPPMark.equals(): trademarks not equal");
			return false;
		}

		// Treaty or Statutes
		if (!EqualityUtil.equals(this.treatyOrStatutes, other.treatyOrStatutes)) {
			cat.error("EPPMark.equals(): treatyOrStatutes not equal");
			return false;
		}

		// Courts
		if (!EqualityUtil.equals(this.courts, other.courts)) {
			cat.error("EPPMark.equals(): courts not equal");
			return false;
		}

		return true;
	}

	/**
	 * Are trademarks defined?
	 * 
	 * @return <code>true</code> if trademarks are defined; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasTrademarks() {
		return (this.trademarks != null ? true : false);
	}

	/**
	 * Gets the list of trademarks.
	 * 
	 * @return List of trademarks if set; <code>null</code> otherwise.
	 */
	public List<EPPTrademark> getTrademarks() {
		return this.trademarks;
	}

	/**
	 * Sets the list of trademarks.
	 * 
	 * @param aTrademarks
	 *            List of trademarks
	 */
	public void setTrademarks(List<EPPTrademark> aTrademarks) {
		this.trademarks = aTrademarks;
	}

	/**
	 * Adds a trademark to the list of trademarks.
	 * 
	 * @param aTrademark
	 *            Trademark to add to the list of trademarks.
	 */
	public void addTrademark(EPPTrademark aTrademark) {
		if (this.trademarks == null) {
			this.trademarks = new ArrayList<EPPTrademark>();
		}

		this.trademarks.add(aTrademark);
	}

	/**
	 * Are treaty or statutes defined?
	 * 
	 * @return <code>true</code> if treaty or statutes are defined;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasTreatyOrStatutes() {
		return (this.treatyOrStatutes != null ? true : false);
	}

	/**
	 * Gets the list of treaty or statutes.
	 * 
	 * @return List of treaty or statutes if set; <code>null</code> otherwise.
	 */
	public List<EPPTreatyOrStatute> getTreatyOrStatutes() {
		return this.treatyOrStatutes;
	}

	/**
	 * Sets the list of treaty or statutes.
	 * 
	 * @param aTreatyOrStatutes
	 *            List of trademarks
	 */
	public void setTreatyOrStatutes(List<EPPTreatyOrStatute> aTreatyOrStatutes) {
		this.treatyOrStatutes = aTreatyOrStatutes;
	}

	/**
	 * Adds a treaty or statute to the list of treaty or statutes.
	 * 
	 * @param aTreatyOrStatute
	 *            Treaty or statute to add to the list of treaty or statutes.
	 */
	public void addTreatyOrStatute(EPPTreatyOrStatute aTreatyOrStatute) {
		if (this.treatyOrStatutes == null) {
			this.treatyOrStatutes = new ArrayList<EPPTreatyOrStatute>();
		}

		this.treatyOrStatutes.add(aTreatyOrStatute);
	}

	/**
	 * Are courts defined?
	 * 
	 * @return <code>true</code> if courts are defined; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasCourts() {
		return (this.courts != null ? true : false);
	}

	/**
	 * Gets the list of courts.
	 * 
	 * @return List of courts if set; <code>null</code> otherwise.
	 */
	public List<EPPCourt> getCourts() {
		return this.courts;
	}

	/**
	 * Sets the list of courts.
	 * 
	 * @param aCourts
	 *            List of courts
	 */
	public void setCourts(List<EPPCourt> aCourts) {
		this.courts = aCourts;
	}

	/**
	 * Adds a court to the list of courts.
	 * 
	 * @param aCourt
	 *            Court to add to the list of courts.
	 */
	public void addCourt(EPPCourt aCourt) {
		if (this.courts == null) {
			this.courts = new ArrayList<EPPCourt>();
		}

		this.courts.add(aCourt);
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