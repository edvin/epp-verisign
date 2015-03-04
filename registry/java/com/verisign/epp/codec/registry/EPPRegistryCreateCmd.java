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

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCreateCmd;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents an EPP Registry &lt;create&gt; command, which provides a transform
 * operation that allows a client to create a registry object. In addition to
 * the standard EPP command elements, the &lt;create&gt; command MUST contain a
 * &lt;registry:create&gt; element that identifies the registry namespace and
 * the location of the registry schema. The &lt;registry:create&gt; element MUST
 * contain the following child elements: <br>
 * <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:zone&gt; element that contains the detailed registry
 * information of the object to be created. Use <code>getZone</code> and
 * <code>setZone</code> to get and set the element.</li>
 * </ul>
 * 
 * <code>EPPRegistryCreateResp</code> is the concrete <code>EPPReponse</code>
 * associated with <code>EPPRegistryCreateCmd</code>.<br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryCreateResp
 */
public class EPPRegistryCreateCmd extends EPPCreateCmd {
	private static final long serialVersionUID = 8229815168285864332L;

	private static Logger cat = Logger.getLogger(EPPRegistryCreateCmd.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** XML Element Name of <code>EPPRegistryCreateCmd</code> root element. */
	final static String ELM_NAME = "registry:create";

	/** Instance of {@link EPPRegistryZoneInfo} to create */
	private EPPRegistryZoneInfo zone = null;

	/**
	 * Creates an empty EPPRegistryCreateCmd object. {@code zone} is set to
	 * {@code null}. Use {@code setZone} to set {@code zone} attribute before
	 * sending the create command.
	 */
	public EPPRegistryCreateCmd() {
		zone = null;
	}

	/**
	 * Creates a new EPPRegistryCreateCmd object that will create a registry
	 * object based on the info in {@code zone}.
	 * 
	 * @param aTransId
	 *            Transaction Id associated with command.
	 * @param zone
	 *            Instance of {@link EPPRegistryZoneInfo} to create
	 */
	public EPPRegistryCreateCmd(String aTransId, EPPRegistryZoneInfo zone) {
		super(aTransId);
		this.zone = zone;
	}

	/**
	 * Get the EPP command Namespace associated with
	 * {@code EPPRegistryCreateCmd}.
	 * 
	 * @return {@link EPPRegistryMapFactory}.NS
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Validate the state of the <code>EPPRegistryCreateCmd</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (zone == null) {
			throw new EPPCodecException("zone required element is not set");
		}
	}

	/**
	 * Compare an instance of <code>EPPRegistryCreateCmd</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryCreateCmd)) {
			cat.error("EPPRegistryCreateCmd.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPRegistryCreateCmd instance");

			return false;
		}

		if (!super.equals(aObject)) {
			cat.error("EPPRegistryCreateCmd.equals(): super class not equal");

			return false;
		}

		EPPRegistryCreateCmd theComp = (EPPRegistryCreateCmd) aObject;

		if (!((zone == null) ? (theComp.zone == null) : zone
				.equals(theComp.zone))) {
			cat.error("EPPRegistryCreateCmd.equals(): zone not equal");

			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryCreateCmd</code>.
	 * 
	 * @return clone of <code>EPPRegistryCreateCmd</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryCreateCmd clone = (EPPRegistryCreateCmd) super.clone();

		if (this.zone != null) {
			clone.zone = (EPPRegistryZoneInfo) this.zone.clone();
		}

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
	 * {@code EPPRegistryCreateCmd} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Root DOM Element representing the {@code EPPRegistryCreateCmd}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode {@code EPPRegistryCreateCmd} instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			// Validate States
			validateState();
		} catch (EPPCodecException e) {
			cat.error("EPPRegistryCreateCmd.doEncode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException("EPPRegistryCreateCmd invalid state: "
					+ e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		EPPUtil.encodeComp(aDocument, root, zone);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryCreateCmd} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryCreateCmd} from.
	 * 
	 * @exception {@link EPPDecodeException} Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		zone = (EPPRegistryZoneInfo) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryZoneInfo.ELM_NAME,
				EPPRegistryZoneInfo.class);
	}

	/**
	 * Get zone to create
	 * 
	 * @return Instance of {@link EPPRegistryZoneInfo} to create
	 */
	public EPPRegistryZoneInfo getZone() {
		return zone;
	}

	/**
	 * Set zone to create
	 * 
	 * @param zone
	 *            Instance of {@link EPPRegistryZoneInfo} to create
	 */
	public void setZone(EPPRegistryZoneInfo zone) {
		this.zone = zone;
	}
}
