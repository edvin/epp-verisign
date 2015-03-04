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
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUpdateCmd;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents an EPP Registry &lt;update&gt; command. The EPP &lt;update&gt;
 * command provides a transform operation that allows a client to modify the
 * attributes of a zone object.  In addition to the standard EPP command
 * elements, the &lt;update&gt; command MUST contain a &lt;domain:update&gt;
 * element that identifies the domain namespace and the location of the domain
 * schema. In addition to  The &lt;registry:update&gt; element SHALL contain the
 * following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:zone&gt; element that contains the detailed registry
 * information of the object to be updated. Use <code>getZone</code> and
 * <code>setZone</code> to get and set the element.</li>
 * </ul>
 * 
 * <code>EPPRegistryUpdateResp</code> is the concrete <code>EPPReponse</code>
 * associated with <code>EPPRegistryUpdateCmd</code>.<br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryUpdateResp
 */
public class EPPRegistryUpdateCmd extends EPPUpdateCmd {
	private static final long serialVersionUID = -7884717270975504713L;

	/** XML Element Name of <code>EPPRegistryUpdateCmd</code> root element. */
	final static String ELM_NAME = "registry:update";

	/** Instance of {@link EPPRegistryZoneInfo} to update */
	private EPPRegistryZoneInfo zone = null;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryUpdateCmd.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Creates an empty EPPRegistryUpdateCmd object. {@code zone} is set to
	 * {@code null}. Use {@code setZone} to set {@code zone} attribute before
	 * sending the update command.
	 */
	public EPPRegistryUpdateCmd() {
		zone = null;
	}

	/**
	 * Creates a new EPPRegistryUpdateCmd object that will update a registry
	 * object based on the info in {@code zone}.
	 * 
	 * @param aTransId
	 *            Transaction Id associated with command.
	 * @param zone
	 *            Instance of {@link EPPRegistryZoneInfo} to update
	 */
	public EPPRegistryUpdateCmd(String aTransId, EPPRegistryZoneInfo zone) {
		super(aTransId);
		this.zone = zone;
	}

	/**
	 * Get the EPP command Namespace associated with EPPRegistryUpdateCmd.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Validate the state of the <code>EPPRegistryUpdateCmd</code> instance. A
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
			throw new EPPCodecException("zone required attribute is not set");
		}
	}

	/**
	 * Compare an instance of <code>EPPRegistryUpdateCmd</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryUpdateCmd)) {
			cat.error("EPPRegistryUpdateCmd.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPRegistryUpdateCmd instance");

			return false;
		}
		if (!super.equals(aObject)) {
			cat.error("EPPRegistryUpdateCmd.equals(): super class not equal");

			return false;
		}

		EPPRegistryUpdateCmd theComp = (EPPRegistryUpdateCmd) aObject;
		if (!((zone == null) ? (theComp.zone == null) : zone
				.equals(theComp.zone))) {
			cat.error("EPPRegistryUpdateCmd.equals(): zone not equal");

			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryUpdateCmd</code>.
	 * 
	 * @return clone of <code>EPPRegistryUpdateCmd</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryUpdateCmd clone = (EPPRegistryUpdateCmd) super.clone();

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
	 * Encode a DOM Element tree from the attributes of the {@code EPPRegistryUpdateCmd}
	 * instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Root DOM Element representing the {@code EPPRegistryUpdateCmd} instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode {@code EPPRegistryUpdateCmd} instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			// Validate States
			validateState();
		} catch (EPPCodecException e) {
			cat.error("EPPRegistryUpdateCmd.doEncode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException("EPPRegistryUpdateCmd invalid state: "
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
	 * Decode the {@code EPPRegistryUpdateCmd} attributes from the aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryUpdateCmd} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		zone = (EPPRegistryZoneInfo) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryZoneInfo.ELM_NAME,
				EPPRegistryZoneInfo.class);
	}

	/**
	 * Get zone to update.
	 * 
	 * @return Instance of {@link EPPRegistryZoneInfo} to update
	 */
	public EPPRegistryZoneInfo getZone() {
		return zone;
	}

	/**
	 * Set zone to update.
	 * 
	 * @param zone
	 *            Instance of {@link EPPRegistryZoneInfo} to update
	 */
	public void setZone(EPPRegistryZoneInfo zone) {
		this.zone = zone;
	}
}