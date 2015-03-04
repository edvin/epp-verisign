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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPDeleteCmd;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents an EPP Registry &lt;delete&gt; command that allows a client to
 * delete a registry object. The EPP &lt;delete&gt; command provides a transform
 * operation that allows a client to delete a registry object. In addition to
 * the standard EPP command elements, the &lt;delete&gt; command MUST contain a
 * "registry:delete" element that identifies the registry namespace and the
 * location of the registry schema. <br>
 * <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:name&gt; element that contains the fully qualified registry
 * name of the object to be deleted. Use <code>getName</code> and
 * <code>setName</code> to get and set the element.</li>
 * </ul>
 * 
 * <br>
 * {@code EPPReponse} is the response associated with
 * <code>EPPRegistryDeleteCmd</code>. <br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPRegistryDeleteCmd extends EPPDeleteCmd {
	private static final long serialVersionUID = -7461061298359182434L;

	/** XML Element Name of <code>EPPRegistryDeleteCmd</code> root element. */
	final static String ELM_NAME = "registry:delete";

	/**
	 * XML Element Name of a registry name in a
	 * <code>EPPRegistryDeleteCmd</code>.
	 */
	private final static String ELM_ZONE_NAME = "registry:name";

	/** Registry Name of zone to delete. */
	private String name;

	/**
	 * <code>EPPRegistryDeleteCmd</code> default constructor. The name is
	 * initialized {@code name} to <code>null</code>. The name must be set
	 * before invoking <code>encode</code>.
	 */
	public EPPRegistryDeleteCmd() {
		name = null;
	}

	/**
	 * <code>EPPRegistryDeleteCmd</code> constructor that takes the registry
	 * name as an argument.
	 * 
	 * @param aTransId
	 *            transaction Id associated with command.
	 * @param aName
	 *            fully qualified name of zone object to delete.
	 */
	public EPPRegistryDeleteCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPRegistryDeleteCmd</code>.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Gets the registry name to delete.
	 * 
	 * @return Registry Name <code>String</code> instance if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the registry name to delete.
	 * 
	 * @param aName
	 *            Registry Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	/**
	 * Compare an instance of <code>EPPRegistryDeleteCmd</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryDeleteCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPRegistryDeleteCmd theComp = (EPPRegistryDeleteCmd) aObject;

		// Name
		if (!((name == null) ? (theComp.name == null) : name
				.equals(theComp.name))) {
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryDeleteCmd</code>.
	 * 
	 * @return clone of <code>EPPRegistryDeleteCmd</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryDeleteCmd clone = (EPPRegistryDeleteCmd) super.clone();

		return clone;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryDeleteCmd</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Root DOM Element representing the <code>EPPRegistryDeleteCmd</code>
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryDeleteCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(aDocument, root, name, EPPRegistryMapFactory.NS,
				ELM_ZONE_NAME);

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryDeleteCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPRegistryDeleteCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		name = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_ZONE_NAME);
	}
}
