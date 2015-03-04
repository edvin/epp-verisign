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
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class to hold the related zone information that include the list of related
 * zone members and an optional definition of the fields that are shared or that
 * are required to be synchronized.
 */
public class EPPRegistryRelated implements EPPCodecComponent {

	private static Logger cat = Logger.getLogger(EPPRegistryRelated.class);

	/**
	 * Constant for the related root element local name.
	 */
	public static final String ELM_LOCALNAME = "related";

	/**
	 * Constant for the related root element name.
	 */
	public final static String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Definition of how the related zone fields are managed.
	 */
	private EPPRegistryFields fields;

	/**
	 * Definition of one or more related zones.
	 */
	private List<EPPRegistryZoneMember> members;

	/**
	 * Default constructor with an empty list of related zone members.
	 */
	public EPPRegistryRelated() {
		this.members = new ArrayList<EPPRegistryZoneMember>();
	}

	/**
	 * Constructor for <code>EPPRegistryRelated</code> that takes the required
	 * members field.
	 * 
	 * @param aMembers
	 *            Zone members list.
	 */
	public EPPRegistryRelated(List<EPPRegistryZoneMember> aMembers) {
		this.members = aMembers;
	}

	/**
	 * Constructor for <code>EPPRegistryRelated</code> that takes the required
	 * members field and the optional fields field.
	 * 
	 * @param aMembers
	 *            Zone members list.
	 * @param aFields
	 *            Definition of how the related zone fields are managed.
	 */
	public EPPRegistryRelated(List<EPPRegistryZoneMember> aMembers,
			EPPRegistryFields aFields) {
		this.members = aMembers;
	}

	/**
	 * Encode the <code>EPPRegistryRelated</code> to a DOM Element
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryRelated.encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		// Fields
		EPPUtil.encodeComp(aDocument, root, this.fields);

		// Members
		EPPUtil.encodeCompList(aDocument, root, this.members);

		return root;
	}

	/**
	 * Decode the DOM element to the <code>EPPRegistryRelated</code>.
	 * 
	 * @param aElement
	 *            DOM Element to decode the attribute values
	 * @throws EPPDecodeException
	 *             Error decoding the DOM Element
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Fields
		this.fields = (EPPRegistryFields) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryFields.ELM_NAME,
				EPPRegistryFields.class);

		// Members
		this.members = EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryZoneMember.ELM_NAME,
				EPPRegistryZoneMember.class);
	}

	/**
	 * Clone <code>EPPRegistryRelated</code>.
	 * 
	 * @return clone of <code>EPPRegistryRelated</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryRelated clone = (EPPRegistryRelated) super.clone();

		// Fields
		if (this.fields != null) {
			clone.fields = (EPPRegistryFields) this.fields.clone();
		}

		// Members
		if (members != null) {
			clone.members = (List) ((ArrayList) members).clone();
		}
		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryRelated</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryRelated</code> instance to compare with
	 * 
	 * @return <code>true</code> if equal <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryRelated)) {
			return false;
		}

		EPPRegistryRelated theComp = (EPPRegistryRelated) aObject;

		// Fields
		if (!EqualityUtil.equals(this.fields, theComp.fields)) {
			cat.error("EPPRegistryRelated.equals(): fields not equal");
			return false;
		}

		// Members
		if (!EPPUtil.equalLists(this.members, theComp.members)) {
			cat.error("EPPRegistryRelated.equals(): members not equal");
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPRegistryRelated</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPRegistryRelated will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *             If there is an invalid state
	 */
	void validateState() throws EPPCodecException {
		if (members == null || members.size() == 0) {
			throw new EPPCodecException(
					"There should have at least one zone member in the zone members");
		}
	}

	
	
	/**
	 * Gets the related fields definition.
	 * 
	 * @return Related fields if defined; <code>null</code> otherwise.
	 */
	public EPPRegistryFields getFields() {
		return this.fields;
	}

	/**
	 * Sets the related fields definition.
	 * 
	 * @param aFields Related fields
	 */
	public void setFields(EPPRegistryFields aFields) {
		this.fields = aFields;
	}
	
	/**
	 * Are fields defined?
	 * 
	 * @return <code>true</code> if fields are defined; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasFields() {
		return (this.fields != null ? true : false);
	}

	/**
	 * Gets the related zone members.
	 * 
	 * @return Related zone members if defined; <code>null</code> otherwise.
	 */
	public List<EPPRegistryZoneMember> getMembers() {
		return this.members;
	}

	/**
	 * Sets the related zone members.
	 * 
	 * @param aMembers
	 *            Related zone members
	 */
	public void setMembers(List<EPPRegistryZoneMember> aMembers) {
		this.members = aMembers;
	}

	/**
	 * Adds a zone member to the list of related zone members.
	 * 
	 * @param aMember
	 *            Zone member to add to the list of related zone members.
	 */
	public void addMember(EPPRegistryZoneMember aMember) {
		if (this.members == null) {
			this.members = new ArrayList<EPPRegistryZoneMember>();
		}
		this.members.add(aMember);
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
