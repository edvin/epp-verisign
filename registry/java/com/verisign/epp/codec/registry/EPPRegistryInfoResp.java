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
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents an EPP Registry &lt;registry:infData&gt; response to an
 * <code>EPPRegistryInfoCmd</code>. When an &lt;info&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a child
 * &lt;registry:infData&gt; element that identifies the registry namespace and
 * the location of the registry schema. <br>
 * <br>
 * 
 * If the corresponding registry create command contains &lt;registry:all&gt;
 * element, The &lt;registry:infData&gt; element must contain the following
 * child elements: <br>
 * <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:zoneList&gt; element that contains the list of supported zones
 * by the server with a set of summary attributes per zone. Each set of summary
 * attributes are enclosed in the &lt;registry:zone&gt; element.</li>
 * </ul>
 * <br>
 * If the corresponding registry create command contains &lt;registry:name&gt;
 * element, The &lt;registry:infData&gt; element must contain the following
 * child elements: <br>
 * <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:zone&gt; element that contains the detail info of a zone
 * object.</li>
 * </ul>
 * 
 * <br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryInfoCmd
 * @see com.verisign.epp.codec.registry.EPPRegistryZoneList
 * @see com.verisign.epp.codec.registry.EPPRegistryZone
 * @see com.verisign.epp.codec.registry.EPPRegistryZoneInfo
 */
public class EPPRegistryInfoResp extends EPPResponse {
	private static final long serialVersionUID = -2683693517713467339L;

	/** XML Element Name of <code>EPPRegistryInfoResp</code> root element. */
	final static String ELM_NAME = "registry:infData";

	/** {@code List} of zone summary attributes */
	private EPPRegistryZoneList zoneList = null;

	/** {@code zoneInfo} instance that contains detailed zone object info */
	private EPPRegistryZoneInfo zoneInfo = null;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryInfoResp.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * <code>EPPRegistryInfoResp</code> default constructor. Must call either
	 * {@code setZoneList} or {@code setZoneInfo} setter methods before invoking
	 * <code>encode</code>.
	 */
	public EPPRegistryInfoResp() {
	}

	/**
	 * Create a {@code EPPRegistryInfoResp} instance with a set of zone summary
	 * attributes. Use this to construct response to command
	 * {@link EPPRegistryInfoCmd} with {@code all} set to {@code true}.
	 * 
	 * @param transId
	 *            transaction Id associated with response
	 * @param zoneList
	 *            object containing a list of zone summary attributes
	 */
	public EPPRegistryInfoResp(EPPTransId transId, EPPRegistryZoneList zoneList) {
		super(transId);
		this.zoneList = zoneList;
		this.zoneInfo = null;
	}

	/**
	 * Create a {@code EPPRegistryInfoResp} instance with a set of zone summary
	 * attributes. Use this to construct response to command
	 * {@link EPPRegistryInfoCmd} with {@code all} set to {@code false}.
	 * 
	 * @param transId
	 *            transaction Id associated with response
	 * @param zoneInfo
	 *            object containing detailed zone info
	 */
	public EPPRegistryInfoResp(EPPTransId transId, EPPRegistryZoneInfo zoneInfo) {
		super(transId);
		this.zoneInfo = zoneInfo;
		this.zoneList = null;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryInfoResp</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the EPPRegistryInfoResp
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode EPPRegistryInfoResp instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			cat.error("EPPRegistryInfoResp.doEncode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryInfoResp.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		if (zoneList != null) {
			EPPUtil.encodeComp(aDocument, root, zoneList);
		}
		if (zoneInfo != null) {
			EPPUtil.encodeComp(aDocument, root, zoneInfo);
		}

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryInfoResp</code> attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryInfoResp</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		zoneList = (EPPRegistryZoneList) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryZoneList.ELM_NAME,
				EPPRegistryZoneList.class);
		zoneInfo = (EPPRegistryZoneInfo) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryZoneInfo.ELM_NAME,
				EPPRegistryZoneInfo.class);
	}

	/**
	 * Validate the state of the <code>EPPRegistryInfoResp</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if ((zoneList == null && zoneInfo == null)
				|| (zoneList != null && zoneInfo != null)) {
			throw new EPPCodecException(
					"One and only one zoneList or zoneInfo can exist");
		}
	}

	/**
	 * Clone <code>EPPRegistryInfoResp</code>.
	 * 
	 * @return clone of <code>EPPRegistryInfoResp</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryInfoResp clone = (EPPRegistryInfoResp) super.clone();

		if (zoneList != null) {
			clone.zoneList = (EPPRegistryZoneList) zoneList.clone();
		}
		if (zoneInfo != null) {
			clone.zoneInfo = (EPPRegistryZoneInfo) zoneInfo.clone();
		}

		return clone;
	}

	/**
	 * Gets the EPP response type associated with
	 * <code>EPPRegistryInfoResp</code>.
	 * 
	 * @return <code>EPPRegistryInfoResp.ELM_NAME</code>
	 */
	public String getType() {
		return ELM_NAME;
	}

	/**
	 * Gets the EPP command namespace associated with
	 * <code>EPPRegistryInfoResp</code>.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Compare an instance of <code>EPPRegistryInfoResp</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryInfoResp)) {
			return false;
		}

		EPPRegistryInfoResp theComp = (EPPRegistryInfoResp) aObject;

		if (!((zoneList == null) ? (theComp.zoneList == null) : zoneList
				.equals(theComp.zoneList))) {
			cat.error("EPPRegistryInfoResp.equals(): zoneList not equal");
			return false;
		}

		if (!((zoneInfo == null) ? (theComp.zoneInfo == null) : zoneInfo
				.equals(theComp.zoneInfo))) {
			cat.error("EPPRegistryInfoResp.equals(): zoneInfo not equal");
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

	public EPPRegistryZoneList getZoneList() {
		return zoneList;
	}

	public void setZoneList(EPPRegistryZoneList zoneList) {
		this.zoneList = zoneList;
	}

	public EPPRegistryZoneInfo getZoneInfo() {
		return zoneInfo;
	}

	public void setZoneInfo(EPPRegistryZoneInfo zoneInfo) {
		this.zoneInfo = zoneInfo;
	}
}