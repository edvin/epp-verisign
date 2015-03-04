/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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
package com.verisign.epp.codec.contact;

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
 * Represents an EPP Contact &lt;contact:chkData&gt; response to a
 * <code>EPPContactCheckCmd</code>. When a &lt;check&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;contact:chkData&gt; element that identifies the contact namespace
 * and the location of the contact schema. The &lt;contact:chkData&gt; element
 * contains one or more &lt;contact:cd&gt; elements that contain the following
 * child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that identifies the queried object.  This
 * element MUST contain an "avail" attribute whose value indicates object
 * availablity at the moment the &lt;check&gt; command was completed.  A value
 * of "1" or "true" means that the object is available.  A value of "0" or
 * "false" means that the object is not available.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:reason&gt; element that MAY be provided when an
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
 * @see com.verisign.epp.codec.contact.EPPContactCheckCmd
 * @see com.verisign.epp.codec.contact.EPPContactCheckResult
 */
public class EPPContactCheckResp extends EPPResponse {
	/** XML Element Name of <code>EPPContactPingResp</code> root element. */
	final static String ELM_NAME = "contact:chkData";

	/** Vector of <code>EPPContactResult</code> instances. */
	private Vector results;

	/**
	 * <code>EPPContactCheckResp</code> default constructor.  It will set
	 * results attribute to an empty <code>Vector</code>.
	 */
	public EPPContactCheckResp() {
		results = new Vector();
	}

	// End EPPContactCheckResp()

	/**
	 * <code>EPPContactCheckResp</code> constructor that will set the result of
	 * an individual contact.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aResult Result of a single contact name.
	 */
	public EPPContactCheckResp(
							   EPPTransId aTransId,
							   EPPContactCheckResult aResult) {
		super(aTransId);

		results = new Vector();
		results.addElement(aResult);
	}

	// End EPPContactCheckResp(EPPTransId, EPPContactCheckResult)

	/**
	 * <code>EPPContactCheckResp</code> constructor that will set the result of
	 * multiple contacts.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param someResults Vector of EPPResult instances.
	 */
	public EPPContactCheckResp(EPPTransId aTransId, Vector someResults) {
		super(aTransId);

		results = someResults;
	}

	// End EPPContactCheckResp.EPPContactCheckResp(EPPTransId, Vector)

	/**
	 * Get the EPP response type associated with
	 * <code>EPPContactCheckResp</code>.
	 *
	 * @return EPPContactPingResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPContactCheckResp.getType()

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPContactCheckResp</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactCheckResp.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactCheckResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPContactCheckResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactCheckResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// Results
		EPPUtil.encodeCompVector(aDocument, root, results);

		return root;
	}

	// End EPPContactCheckResp.doEncode(Document)

	/**
	 * Decode the <code>EPPContactCheckResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactCheckResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Results
		results =
			EPPUtil.decodeCompVector(
									 aElement, EPPContactMapFactory.NS,
									 EPPContactCheckResult.ELM_NAME,
									 EPPContactCheckResult.class);
	}

	// End EPPContactCheckResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPContactCheckResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactCheckResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactCheckResp thePingData = (EPPContactCheckResp) aObject;

		// results
		if (!EPPUtil.equalVectors(results, thePingData.results)) {
			return false;
		}

		return true;
	}

	// End EPPContactCheckResp.equals(Object)

	/**
	 * Clone <code>EPPContactCheckResp</code>.
	 *
	 * @return clone of <code>EPPContactCheckResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactCheckResp clone = (EPPContactCheckResp) super.clone();

		clone.results = (Vector) results.clone();

		for (int i = 0; i < results.size(); i++)
			clone.results.setElementAt(
									   ((EPPContactCheckResult) results
										.elementAt(i)).clone(), i);

		return clone;
	}

	// End EPPContactCheckResp.clone()

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

	// End EPPContactCheckResp.toString()

	/**
	 * Set the results of a <code>EPPContactCheckResp</code> Response.  There
	 * is     one <code>EPPContactResult</code> instance in
	 * <code>someResults</code> for each     contact requested in the
	 * <code>EPPContactCheckCmd</code> Command.
	 *
	 * @param someResults Vector of <code>EPPContactResult</code> instances.
	 */
	public void setCheckResults(Vector someResults) {
		results = someResults;
	}

	// End EPPContactCheckResp.setCheckResults(Vector)

	/**
	 * Get the results of a <code>EPPContactCheckResp</code> Response.  There
	 * is     one <code>EPPContactResult</code> instance in
	 * <code>someResults</code> for each     contact requested in the
	 * <code>EPPContactResult</code> Command.
	 *
	 * @return Vector of <code>EPPContactResult</code> instances.
	 */
	public Vector getCheckResults() {
		return results;
	}

	// End EPPContactCheckResp.getResults()
}
