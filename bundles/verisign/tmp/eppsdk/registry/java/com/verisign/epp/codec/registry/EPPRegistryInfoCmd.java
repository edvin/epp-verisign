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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPInfoCmd;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents an EPP Registry &lt;info&gt; command that is used to retrieve
 * information associated with a registry. The &lt;registry:info&gt; element
 * MUST contain one of the following child elements:<br>
 * <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:name&gt; element that contains the fully qualified zone object
 * name for which information is requested. Use <code>getName</code> and
 * <code>setName</code> to get and set the element.
 * </li>
 * <li>
 * A &lt;registry:all&gt; empty element that specifies whether or not to query a list
 * of all supported zone objects by the server. Use <code>isAll</code> and
 * <code>setAll</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * A valid {@code EPPRegistryInfoCmd} must contains one and only one of the above
 * elements.
 * <br>
 * 
 * <br>
 * <code>EPPRegistryInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated with <code>EPPRegistryInfoResp</code>. <br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryInfoResp
 */
public class EPPRegistryInfoCmd extends EPPInfoCmd {
	private static final long serialVersionUID = 7204018220215077843L;

	private static Logger cat = Logger.getLogger(EPPRegistryInfoCmd.class);

	/** XML Element Name of <code>EPPRegistryInfoCmd</code> root element. */
	final static String ELM_NAME = "registry:info";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_REGISTRY_NAME = "registry:name";
	/** XML Element Name for the <code>all</code> attribute. */
	private final static String ELM_REGISTRY_ALL = "registry:all";

	/**
	 * Setting this attribute to {@code true} to query a list of all of the
	 * supported zone objects from the server. This CANNOT be used together with
	 * {@code name}. If {@code name} is not empty, then this attribute must be
	 * set to {@code false}.
	 */
	private boolean all = false;

	/**
	 * Fully qualified name of zone object to get information on. Cannot be used
	 * together with {@code all}. If {@code all} is set to {@code true}, this
	 * attribute must be empty.
	 * */
	private String name;

	// TODO: permanently remove authInfo
	/** authorization information. */
	// private EPPAuthInfo authInfo = null;

	/**
	 * <code>EPPRegistryInfoCmd</code> default constructor. The {@code all} is
	 * set to {@code false} and the {@code name} is set to {@code null}. Either
	 * {@code all} or {@code name} must be set before invoking
	 * <code>encode</code>.
	 */
	public EPPRegistryInfoCmd() {
	}

	/**
	 * <code>EPPRegistryInfoCmd</code> constructor that takes the qualified zone
	 * object name as an argument. Attribute {@code all} is set to {@code false}
	 * .
	 * 
	 * @param aTransId
	 *            transaction Id associated with command
	 * @param aName
	 *            fully qualified zone object name to get information on
	 */
	public EPPRegistryInfoCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
		all = false;
	}

	/**
	 * <code>EPPRegistryInfoCmd</code> constructor that tries to query a list of
	 * all supported zone objects from the server. Attribute {@code name} is set
	 * to {@code nulL}.
	 * 
	 * @param aTransId
	 *            transaction Id associated with command
	 * @param all
	 *            fully qualified zone object name to get information on
	 */
	public EPPRegistryInfoCmd(String aTransId, boolean all) {
		super(aTransId);

		name = null;
		this.all = all;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryInfoCmd</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Root DOM Element representing the <code>EPPRegistryInfoCmd</code>
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryInfoCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if ((name == null && !all) || (name != null && all)) {
			throw new EPPEncodeException(
					"Name is required, unless \"all\" is set.");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		Element nameElm = null;
		if (!all) {
			nameElm = aDocument.createElementNS(EPPRegistryMapFactory.NS,
					ELM_REGISTRY_NAME);
			Text nameVal = aDocument.createTextNode(name);
			nameElm.appendChild(nameVal);
		} else {
			nameElm = aDocument.createElementNS(EPPRegistryMapFactory.NS,
					ELM_REGISTRY_ALL);
		}

		root.appendChild(nameElm);

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryInfoCmd</code> attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryInfoCmd</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		this.name = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_REGISTRY_NAME);
		this.all = EPPUtil.getElementByTagNameNS(aElement,
				EPPRegistryMapFactory.NS, ELM_REGISTRY_ALL) != null;
		return;
	}

	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Compare an instance of <code>EPPRegistryInfoCmd</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryInfoCmd)) {
			cat.error("EPPRegistryInfoCmd.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPRegistryInfoCmd instance");

			return false;
		}

		if (!super.equals(aObject)) {
			cat.error("EPPRegistryInfoCmd.equals(): super class not equal");

			return false;
		}

		EPPRegistryInfoCmd theComp = (EPPRegistryInfoCmd) aObject;

		if (all != theComp.all) {
			cat.error("EPPRegistryInfoCmd.equals(): \"all\" not equal");

			return false;
		}
		if (!((name == null) ? (theComp.name == null) : name
				.equals(theComp.name))) {
			cat.error("EPPRegistryInfoCmd.equals(): name not equal");

			return false;
		}

		return true;
	}

	/**
	 * Get the name of zone object to get information on.
	 * 
	 * @return fully qualified zone object name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of zone object to get information on. If {@code name}
	 * is {@code null}, {@code all} must be true; and vice versa.
	 * 
	 * @param name
	 *            fully qualified zone object name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the flag to query all supported zone objects.
	 * 
	 * @return {@code true} if client intends to query a list of all supported
	 *         zone object ({@code name} must be set to {@code null}). {@code false} if
	 *         client intends to query detailed info of one zone object. (
	 *         {@code name} must be non-blank)
	 */
	public boolean isAll() {
		return all;
	}

	/**
	 * Set the flag to query all supported zone objects.
	 * 
	 * @param all
	 *            {@code true} if client intends to query a list of all
	 *            supported zone object ({@code name} must be set to {@code null}).
	 *            {@code false} if client intends to query detailed info of one
	 *            zone object. ({@code name} must be non-blank)
	 */
	public void setAll(boolean all) {
		this.all = all;
	}
}
