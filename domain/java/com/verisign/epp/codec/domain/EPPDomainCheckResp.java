/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.codec.domain;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Domain &lt;domain:chkData&gt; response to a
 * <code>EPPDomainCheckCmd</code>.     When a &lt;check&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;domain:chkData&gt; element that identifies the domain namespace
 * and the location of the domain schema. The &lt;domain:chkData&gt; elements
 * that contain the following child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified name of the
 * queried domain object.  This element MUST contain an "avail" attribute
 * whose value indicates object availability at the moment the &lt;check&gt;
 * command was completed. A value of "1" or "true" menas that the object is
 * availabe.  A value of "0" or "false" means that the object is not
 * available.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:reason&gt; element that MAY be provided when an
 * object is not available for provisioning.  If present, this element
 * contains server-specific text to help explain why the object is
 * unavailable.  This text MUST be represented in the response language
 * previously negotiated with the client; an OPTIONAL "lang" attribute MAY be
 * present to identify the language if the negotiated value is something other
 * that a default value of "en" (English).
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.domain.EPPDomainCheckCmd
 * @see com.verisign.epp.codec.domain.EPPDomainCheckResult
 */
public class EPPDomainCheckResp extends EPPResponse {
	/** XML Element Name of <code>EPPDomainCheckResp</code> root element. */
	final static String ELM_NAME = "domain:chkData";

	/** Vector of <code>EPPDomainCheckResult</code> instances. */
	private Vector<EPPDomainCheckResult> results;

	/**
	 * <code>EPPDomainCheckResp</code> default constructor.  It will set
	 * results attribute to an empty <code>Vector</code>.
	 */
	public EPPDomainCheckResp() {
		results = new Vector<>();
	}

	// End EPPDomainCheckResp()

	/**
	 * <code>EPPDomainCheckResp</code> constructor that will set the result of
	 * an individual domain.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aResult Result of a single domain name.
	 */
	public EPPDomainCheckResp(
							  EPPTransId aTransId, EPPDomainCheckResult aResult) {
		super(aTransId);

		results = new Vector();
		results.addElement(aResult);
	}

	// End EPPDomainCheckResp(EPPTransId, EPPDomainCheckResp)

	/**
	 * <code>EPPDomainCheckResp</code> constructor that will set the result of
	 * multiple domains.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param someResults Vector of EPPDomainCheckResult instances.
	 */
	public EPPDomainCheckResp(EPPTransId aTransId, Vector someResults) {
		super(aTransId);

		results = someResults;
	}

	// End EPPDomainCheckResp.EPPDomainCheckResp(EPPTransId, Vector)

	/**
	 * Get the EPP response type associated with
	 * <code>EPPDomainCheckResp</code>.
	 *
	 * @return EPPDomainCheckResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDomainCheckResp.getType()

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPDomainCheckResp</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainCheckResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDomainCheckResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainCheckResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainCheckResp theCheckData = (EPPDomainCheckResp) aObject;

		// results
		if (!EPPUtil.equalVectors(results, theCheckData.results)) {
			return false;
		}

		return true;
	}

	// End EPPDomainCheckResp.equals(Object)

	/**
	 * Clone <code>EPPDomainCheckResp</code>.
	 *
	 * @return clone of <code>EPPDomainCheckResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainCheckResp clone = (EPPDomainCheckResp) super.clone();

		clone.results = (Vector) results.clone();

		for (int i = 0; i < results.size(); i++)
			clone.results.setElementAt(
				(EPPDomainCheckResult) results
					.elementAt(i).clone(), i);

		return clone;
	}

	// End EPPDomainCheckResp.clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPDomainCheckResp.toString()

	/**
	 * Set the results of a <code>EPPDomainCheckResp</code> Response.  There is
	 * one <code>EPPDomainCheckResult</code> instance in
	 * <code>someResults</code> for each     domain requested in the
	 * <code>EPPDomainCheckCmd</code> Command.
	 *
	 * @param someResults Vector of <code>EPPDomainCheckResult</code>
	 * 		  instances.
	 */
	public void setCheckResults(Vector<EPPDomainCheckResult> someResults) {
		results = someResults;
	}

	// End EPPDomainCheckResp.setCheckResults(Vector)

	/**
	 * Get the results of a <code>EPPDomainCheckResp</code> Response.  There is
	 * one <code>EPPDomainCheckResult</code> instance in
	 * <code>someResults</code> for each     domain requested in the
	 * <code>EPPDomainCheckResult</code> Command.
	 *
	 * @return Vector of <code>EPPDomainCheckResult</code> instances.
	 */
	public Vector<EPPDomainCheckResult> getCheckResults() {
		return results;
	}

	// End EPPDomainCheckResp.getResults()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainCheckResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDomainCheckResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainCheckResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Results
		EPPUtil.encodeCompVector(aDocument, root, results);

		return root;
	}

	// End EPPDomainCheckResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainCheckResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainCheckResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Results
		results =
			EPPUtil.decodeCompVector(
									 aElement, EPPDomainMapFactory.NS,
									 EPPDomainCheckResult.ELM_NAME,
									 EPPDomainCheckResult.class);
	}

	// End EPPDomainCheckResp.doDecode(Element)
}
