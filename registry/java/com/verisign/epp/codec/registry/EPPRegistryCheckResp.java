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

import java.util.ArrayList;
import java.util.List;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents an EPP Registry &lt;registry:chkData&gt; response to a
 * <code>EPPRegistryCheckCmd</code>. When a &lt;check&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a child
 * &lt;registry:chkData&gt; element that identifies the registry namespace and
 * the location of the registry schema. The &lt;registry:chkData&gt; elements
 * that contain the following child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;registry:name&gt; element that contains the fully qualified name of the
 * queried zone object. This element MUST contain an "avail" attribute whose
 * value indicates object availability at the moment the &lt;check&gt; command
 * was completed. A value of "1" or "true" means that the object is availabe. A
 * value of "0" or "false" means that the object is not available.</li>
 * <li>
 * An OPTIONAL &lt;registry:reason&gt; element that MAY be provided when an
 * object is not available for provisioning. If present, this element contains
 * server-specific text to help explain why the object is unavailable. This text
 * MUST be represented in the response language previously negotiated with the
 * client; an OPTIONAL "lang" attribute MAY be present to identify the language
 * if the negotiated value is something other that a default value of "en"
 * (English).</li>
 * </ul>
 * 
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryCheckCmd
 * @see com.verisign.epp.codec.registry.EPPRegistryCheckResult
 */
public class EPPRegistryCheckResp extends EPPResponse {
	private static final long serialVersionUID = -7688907204275319672L;

	/** XML Element Name of <code>EPPRegistryCheckResp</code> root element. */
	final static String ELM_NAME = "registry:chkData";

	private static Logger cat = Logger.getLogger(EPPRegistryCheckResp.class);

	/** {@code List} of {@link EPPRegistryCheckResult} instances. */
	private List checkResults;

	/**
	 * <code>EPPRegistryCheckResp</code> default constructor. It will set
	 * results attribute to an empty <code>List</code>.
	 */
	public EPPRegistryCheckResp() {
		checkResults = new ArrayList();
	}

	/**
	 * <code>EPPRegistryCheckResp</code> constructor that will set the result of
	 * an individual zone object.
	 * 
	 * @param aTransId
	 *            transaction Id associated with response
	 * @param aResult
	 *            {@link EPPRegistryCheckResult} instance of a single registry
	 *            name
	 */
	public EPPRegistryCheckResp(EPPTransId aTransId,
			EPPRegistryCheckResult aResult) {
		super(aTransId);

		checkResults = new ArrayList();
		checkResults.add(aResult);
	}

	/**
	 * <code>EPPRegistryCheckResp</code> constructor that will set the result of
	 * multiple zone objects.
	 * 
	 * @param aTransId
	 *            transaction Id associated with response
	 * @param someResults
	 *            {@code List} of {@link EPPRegistryCheckResult} instances
	 */
	public EPPRegistryCheckResp(EPPTransId aTransId, List someResults) {
		super(aTransId);

		checkResults = someResults;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryCheckResp</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         <code>EPPRegistryCheckResp</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryCheckResp</code>
	 *                instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		if (checkResults == null || checkResults.size() == 0) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryInfoResp.encode: chekResults is empty");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		root.setAttribute("xmlns:registry", EPPRegistryMapFactory.NS);
		root.setAttributeNS(EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPRegistryMapFactory.NS_SCHEMA);

		// Results
		EPPUtil.encodeCompList(aDocument, root, checkResults);

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryCheckResp</code> attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryCheckResp</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Results
		checkResults = EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryCheckResult.ELM_NAME,
				EPPRegistryCheckResult.class);
	}

	/**
	 * Get the EPP response type associated with
	 * <code>EPPRegistryCheckResp</code>.
	 * 
	 * @return {@code EPPRegistryCheckResp.ELM_NAME}
	 */
	public String getType() {
		return ELM_NAME;
	}

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPRegistryCheckResp</code>.
	 * 
	 * @return <code>EPPRegistryMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Compare an instance of <code>EPPRegistryCheckResp</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *            Object to compare with.
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryCheckResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPRegistryCheckResp theCheckData = (EPPRegistryCheckResp) aObject;

		// results
		if (!EPPUtil.equalLists(checkResults, theCheckData.checkResults)) {
			cat.error("EPPRegistryCheckResp: checkResults not equal");
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryCheckResp</code>.
	 * 
	 * @return clone of <code>EPPRegistryCheckResp</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryCheckResp clone = (EPPRegistryCheckResp) super.clone();

		clone.checkResults = (List) ((ArrayList) checkResults).clone();

		for (int i = 0; i < checkResults.size(); i++)
			if (checkResults.get(i) == null) {
				clone.checkResults.set(i, null);
			} else {
				clone.checkResults.set(i,
						((EPPRegistryCheckResult) (checkResults.get(i)))
								.clone());
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
	 * Get the check results of zone objects.
	 * 
	 * @return {@code List} of {@link EPPRegistryCheckResult}
	 */
	public List getCheckResults() {
		return checkResults;
	}

	/**
	 * Set the check results of zone objects.
	 * 
	 * @param results
	 *            {@code List} of {@link EPPRegistryCheckResult}
	 */
	public void setCheckResults(List results) {
		this.checkResults = results;
	}
}
