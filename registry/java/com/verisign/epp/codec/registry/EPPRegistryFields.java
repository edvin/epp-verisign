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
package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class that defines the management of related fields.   
 */
public class EPPRegistryFields implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPRegistryFields.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the related zone fields being shared, where there is one set
	 * of related fields. Updating a field in one related zone results in
	 * updating the field value across all of the related zones.
	 */
	public final static String TYPE_SHARED = "shared";

	/**
	 * Constant for the related zone fields required to be synchronized by
	 * registry policy. Updating a field in one related zone does not result in
	 * updating the field across all of the related zones.
	 */
	public final static String TYPE_SYNC = "sync";

	/**
	 * Constant for the phase local name
	 */
	public static final String ELM_LOCALNAME = "fields";

	/**
	 * Constant for the qualified name (prefix and local name)
	 */
	public static final String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for a field
	 */
	private static final String ELM_FIELD = "field";

	/**
	 * Required attribute type name that should have a value of either
	 * {@link #TYPE_SHARED} or {@link #TYPE_SYNC}.
	 */
	public static final String ATTR_TYPE = "type";

	/**
	 * Required attribute type that should have a value of either
	 * {@link #TYPE_SHARED} or {@link #TYPE_SYNC}.
	 */
	private String type;

	/**
	 * List of field names.
	 */
	private List<String> fields = new ArrayList<String>();

	/**
	 * Default constructor. The type value MUST be set using the
	 * {@link #setType(String)} method and the fields MUST be set using 
	 * {@link #setFields(List)} or 
	 * {@link #addField(String)}.
	 */
	public EPPRegistryFields() {
	}

	/**
	 * Create <code>EPPRegistryFields</code> instance with required attributes.
	 * 
	 * @param aType
	 *            Type of related fields with either {@link #TYPE_SHARED} or
	 *            {@link #TYPE_SYNC}.
	 * @param aFields
	 *            List of related fields.
	 */
	public EPPRegistryFields(String aType, List<String> aFields) {
		this.type = aType;
		this.fields = aFields;
	}

	/**
	 * Gets the related fields type, which should be either {@link #TYPE_SHARED}
	 * or {@link #TYPE_SYNC}.
	 * 
	 * @return {@link #TYPE_SHARED} or {@link #TYPE_SYNC} if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Sets the related fields type, which should be either {@link #TYPE_SHARED}
	 * or {@link #TYPE_SYNC}.
	 * 
	 * @param aType
	 *            the type, which should be either {@link #TYPE_SHARED} or
	 *            {@link #TYPE_SYNC}
	 */
	public void setType(String aType) {
		this.type = aType;
	}

	/**
	 * Gets the related field names.
	 * 
	 * @return List of related field names.
	 */
	public List<String> getFields() {
		return this.fields;
	}

	/**
	 * Sets the related field names.
	 * 
	 * @param aFields
	 *            The list of related field names.
	 */
	public void setFields(List<String> aFields) {
		this.fields = aFields;
	}
	
	
	/**
	 * Adds a field to the list of fields.
	 * 
	 * @param aField Field to add to the list of fields.
	 */
	public void addField(String aField) {
		if (this.fields == null) {
			this.fields = new ArrayList<String>();
		}
		
		this.fields.add(aField);
	}

	/**
	 * Clone <code>EPPRegistryFields</code>.
	 * 
	 * @return clone of <code>EPPRegistryFields</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryFields clone = null;

		clone = (EPPRegistryFields) super.clone();

		return clone;
	}

	/**
	 * Decode the <code>EPPRegistryFields</code> element aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *            - Root DOM Element to decode <code>EPPRegistryFields</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Type
		String theType = aElement.getAttribute(ATTR_TYPE);
		if (theType != null && (theType.length() != 0)) {
			this.type = theType;
		}
		else {
			this.type = null;
		}

		// Fields
		this.fields = EPPUtil.decodeList(aElement, EPPRegistryMapFactory.NS,
				ELM_FIELD);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryFields</code> instance.
	 * 
	 * @param aDocument
	 *            - DOM Document that is being built. Used as an Element
	 *            factory.
	 * 
	 * @return Element - Root DOM Element representing the
	 *         <code>EPPRegistryFields</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode <code>EPPRegistryFields</code>
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " in EPPRegistryFields.encode(Document)");
		}
		if (this.type == null) {
			throw new EPPEncodeException("type is null"
					+ " in EPPRegistryFields.encode(Document)");
		}
		if (this.fields == null || this.fields.isEmpty()) {
			throw new EPPEncodeException("fields is null or empty"
					+ " in EPPRegistryFields.encode(Document)");
		}

		// Status with Attributes
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		// Type
		root.setAttribute(ATTR_TYPE, this.type);

		// Fields
		EPPUtil.encodeList(aDocument, root, this.fields,
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_FIELD);

		return root;
	}

	/**
	 * implements a deep <code>EPPRegistryFields</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryFields</code> instance to compare with
	 * 
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryFields)) {
			return false;
		}

		EPPRegistryFields other = (EPPRegistryFields) aObject;

		// Type
		if (!EqualityUtil.equals(this.type, other.type)) {
			cat.error("EPPRegistryFields.equals(): type not equal");
			return false;
		}

		// Fields
		if (!EqualityUtil.equals(this.fields, other.fields)) {
			cat.error("EPPRegistryFields.equals(): fields not equal");
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

}
