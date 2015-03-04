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

import java.util.ArrayList;
import java.util.List;

import com.verisign.epp.codec.gen.EPPCheckCmd;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents an EPP Registry &lt;check&gt; command, which is used to determine
 * if a zone name is known to the server. The &lt;registry:check&gt; element
 * MUST contain the following child elements:<br>
 * <br>
 * 
 * <ul>
 * <li>
 * One or more (up to a maximum of <code>MAX_ZONES</code>) &lt;registry:name&gt;
 * elements that contain the fully qualified name of the queried zones. Use
 * <code>getNames</code> and <code>setNames</code> to get and set the elements.
 * Use <code>addName</code> to add a name to existing list or use
 * <code>setName</code> to set an individual name.</li>
 * </ul>
 * 
 * <br>
 * <code>EPPRegistryCheckResp</code> is the concrete <code>EPPReponse</code>
 * associated with <code>EPPRegistryCheckCmd</code>. <br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryCheckResp
 */
public class EPPRegistryCheckCmd extends EPPCheckCmd {
	private static final long serialVersionUID = -5430412031466266011L;

	/** Maximum number of zones to check at once. */
	public static final int MAX_ZONES = 99;

	/** XML Element Name of <code>EPPRegistryCheckCmd</code> root element. */
	final static String ELM_NAME = "registry:check";

	/** XML Element Name for the <code>names</code> attribute. */
	private final static String ELM_ZONE_NAME = "registry:name";

	/**
	 * zone Names to check. This is a <code>List</code> of <code>String</code>
	 * instances.
	 */
	private List names;

	/**
	 * <code>EPPRegistryCheckCmd</code> default constructor. It will set the
	 * names attribute to an empty <code>List</code>.
	 */
	public EPPRegistryCheckCmd() {
		this.names = new ArrayList();
	}

	/**
	 * <code>EPPRegistryCheckCmd</code> constructor that will check an
	 * individual zone name.
	 * 
	 * @param aTransId
	 *            Transaction Id associated with command.
	 * @param aName
	 *            zone name to check
	 */
	public EPPRegistryCheckCmd(String aTransId, String aName) {
		super(aTransId);

		names = new ArrayList();
		names.add(aName);
	}

	/**
	 * <code>EPPRegistryCheckCmd</code> constructor that will check a list of
	 * zone names.
	 * 
	 * @param aTransId
	 *            Transaction Id associated with command.
	 * @param names
	 *            <code>List</code> of zone name <code>String</code> instances.
	 */
	public EPPRegistryCheckCmd(String aTransId, List names) {
		super(aTransId);

		this.names = names;
	}

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPRegistryCheckCmd</code>.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Compare an instance of <code>EPPRegistryCheckCmd</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise.
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryCheckCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPRegistryCheckCmd theMap = (EPPRegistryCheckCmd) aObject;

		// zone Names
		if (!EPPUtil.equalLists(names, theMap.names)) {
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryCheckCmd</code>.
	 * 
	 * @return Deep copy clone of <code>EPPRegistryCheckCmd</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryCheckCmd clone = (EPPRegistryCheckCmd) super.clone();

		clone.names = (List) ((ArrayList) names).clone();

		return clone;
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
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryCheckCmd</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         <code>EPPRegistryCheckCmd</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryCheckCmd</code>
	 *                instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if ((names == null) || (names.size() == 0)) {
			throw new EPPEncodeException(
					"No zone names specified in EPPRegistryCheckCmd");
		}

		if (names.size() > MAX_ZONES) {
			throw new EPPEncodeException(names.size()
					+ " zone names is greater than the maximum of " + MAX_ZONES
					+ " in EPPRegistryCheckCmd");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		EPPUtil.encodeList(aDocument, root, names, EPPRegistryMapFactory.NS,
				ELM_ZONE_NAME);

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryCheckCmd</code> attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryCheckCmd</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		this.names = EPPUtil.decodeList(aElement, EPPRegistryMapFactory.NS,
				ELM_ZONE_NAME);

		if (this.names == null) {
			this.names = new ArrayList();
		}
	}

	/**
	 * Get zone names to check
	 * 
	 * @return {@code List} of zone name {@code String}'s
	 */
	public List getNames() {
		return names;
	}

	/**
	 * Set zone names to check
	 * 
	 * @param names
	 *            {@code List} of zone name {@code String}'s
	 */
	public void setNames(List names) {
		this.names = names;
	}

	/**
	 * Set an individual zone name to check. This method clears existing zone
	 * name {@code List}.
	 * 
	 * @param name
	 *            zone name to check
	 */
	public void setName(String name) {
		this.names = new ArrayList();
		this.names.add(name);
	}

	/**
	 * Append a zone name to the name {@code List} to check. This method does
	 * NOT clear existing zone name {@code List}.
	 * 
	 * @param name zone name to append
	 */
	public void addName(String name) {
		if (this.names == null) {
			this.names = new ArrayList();
		}

		this.names.add(name);
	}
}
