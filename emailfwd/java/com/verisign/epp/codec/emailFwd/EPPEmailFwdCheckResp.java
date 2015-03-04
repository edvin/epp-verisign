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
package com.verisign.epp.codec.emailFwd;

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
 * Represents an EPP EmailFwd &lt;emailFwd:chkData&gt; response to a
 * <code>EPPEmailFwdCheckCmd</code>.     When a &lt;check&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;emailFwd:chkData&gt; element that identifies the emailFwd
 * namespace and the location of the emailFwd schema. The
 * &lt;emailFwd:chkData&gt; elements that contain the following child
 * elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified name of
 * the queried emailFwd object.  This element MUST contain an "avail"
 * attribute whose value indicates object availability at the moment the
 * &lt;check&gt; command was completed. A value of "1" or "true" menas that
 * the object is availabe.  A value of "0" or "false" means that the object is
 * not available.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:reason&gt; element that MAY be provided when an
 * object is not available for provisioning.  If present, this element
 * contains server-specific text to help explain why the object is
 * unavailable.  This text MUST be represented in the response language
 * previously negotiated with the client; an OPTIONAL "lang" attribute MAY be
 * present to identify the language if the negotiated value is something other
 * that a default value of "en" (English).
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckCmd
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResult
 */
public class EPPEmailFwdCheckResp extends EPPResponse {
	/** XML Element Name of <code>EPPEmailFwdCheckResp</code> root element. */
	final static String ELM_NAME = "emailFwd:chkData";

	/** Vector of <code>EPPEmailFwdCheckResult</code> instances. */
	private Vector results;

	/**
	 * <code>EPPEmailFwdCheckResp</code> default constructor.  It will set
	 * results attribute to an empty <code>Vector</code>.
	 */
	public EPPEmailFwdCheckResp() {
		results = new Vector();
	}

	// End EPPEmailFwdCheckResp()

	/**
	 * <code>EPPEmailFwdCheckResp</code> constructor that will set the result
	 * of an individual emailFwd.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aResult Result of a single emailFwd name.
	 */
	public EPPEmailFwdCheckResp(
								EPPTransId aTransId,
								EPPEmailFwdCheckResult aResult) {
		super(aTransId);

		results = new Vector();
		results.addElement(aResult);
	}

	// End EPPEmailFwdCheckResp(EPPTransId, EPPEmailFwdCheckResp)

	/**
	 * <code>EPPEmailFwdCheckResp</code> constructor that will set the result
	 * of multiple emailFwds.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param someResults Vector of EPPEmailFwdCheckResult instances.
	 */
	public EPPEmailFwdCheckResp(EPPTransId aTransId, Vector someResults) {
		super(aTransId);

		results = someResults;
	}

	// End EPPEmailFwdCheckResp.EPPEmailFwdCheckResp(EPPTransId, Vector)

	/**
	 * Get the EPP response type associated with
	 * <code>EPPEmailFwdCheckResp</code>.
	 *
	 * @return EPPEmailFwdCheckResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPEmailFwdCheckResp.getType()

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPEmailFwdCheckResp</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdCheckResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPEmailFwdCheckResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdCheckResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdCheckResp theCheckData = (EPPEmailFwdCheckResp) aObject;

		// results
		if (!EPPUtil.equalVectors(results, theCheckData.results)) {
			return false;
		}

		return true;
	}

	// End EPPEmailFwdCheckResp.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdCheckResp</code>.
	 *
	 * @return clone of <code>EPPEmailFwdCheckResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdCheckResp clone = (EPPEmailFwdCheckResp) super.clone();

		clone.results = (Vector) results.clone();

		for (int i = 0; i < results.size(); i++)
			clone.results.setElementAt(
									   ((EPPEmailFwdCheckResult) results
										.elementAt(i)).clone(), i);

		return clone;
	}

	// End EPPEmailFwdCheckResp.clone()

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

	// End EPPEmailFwdCheckResp.toString()

	/**
	 * Set the results of a <code>EPPEmailFwdCheckResp</code> Response.  There
	 * is     one <code>EPPEmailFwdCheckResult</code> instance in
	 * <code>someResults</code> for each     emailFwd requested in the
	 * <code>EPPEmailFwdCheckCmd</code> Command.
	 *
	 * @param someResults Vector of <code>EPPEmailFwdCheckResult</code>
	 * 		  instances.
	 */
	public void setCheckResults(Vector someResults) {
		results = someResults;
	}

	// End EPPEmailFwdCheckResp.setCheckResults(Vector)

	/**
	 * Get the results of a <code>EPPEmailFwdCheckResp</code> Response.  There
	 * is     one <code>EPPEmailFwdCheckResult</code> instance in
	 * <code>someResults</code> for each     emailFwd requested in the
	 * <code>EPPEmailFwdCheckResult</code> Command.
	 *
	 * @return Vector of <code>EPPEmailFwdCheckResult</code> instances.
	 */
	public Vector getCheckResults() {
		return results;
	}

	// End EPPEmailFwdCheckResp.getResults()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdCheckResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPEmailFwdCheckResp</code> instance. emailFwd
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdCheckResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Results
		EPPUtil.encodeCompVector(aDocument, root, results);

		return root;
	}

	// End EPPEmailFwdCheckResp.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdCheckResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdCheckResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Results
		results =
			EPPUtil.decodeCompVector(
									 aElement, EPPEmailFwdMapFactory.NS,
									 EPPEmailFwdCheckResult.ELM_NAME,
									 EPPEmailFwdCheckResult.class);
	}

	// End EPPEmailFwdCheckResp.doDecode(Element)
}
