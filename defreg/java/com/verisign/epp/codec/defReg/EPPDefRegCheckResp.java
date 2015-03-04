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
package com.verisign.epp.codec.defReg;

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
 * Represents an EPPDefReg &lt;defReg:chkData&gt; response to a
 * <code>EPPDefRegFwdCheckCmd</code>.     When a &lt;check&gt; command has
 * been processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;defReg:chkData&gt; element that identifies the defReg namespace
 * and the location of the defReg schema. The &lt;defReg:chkData&gt; elements
 * that contain the following child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:name&gt; element that contains the fully qualified name of the
 * queried defReg object with a attribute  level whose value is of either
 * "premium" or "standard". This element MUST contain an "avail" attribute
 * whose value indicates object availability at the moment the &lt;check&gt;
 * command was completed. A value of "1" or "true" menas that the object is
 * availabe.  A value of "0" or "false" means that the object is not
 * available.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:reason&gt; element that MAY be provided when an
 * object is not available for provisioning.  If present,EPPDefReg this
 * element contains server-specific text to help explain why the object is
 * unavailable.  This text MUST be represented in the response language
 * previously negotiated with the client; an OPTIONAL "lang" attribute MAY be
 * present to identify the language if the negotiated value is something other
 * that a default value of "en" (English).
 * </li>
 * </ul>
 * 
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegCheckCmd
 * @see com.verisign.epp.codec.defReg.EPPDefRegCheckResult
 */
public class EPPDefRegCheckResp extends EPPResponse {
	/** XML Element Name of <code>EPPDefRegFwdCheckResp</code> root element. */
	final static String ELM_NAME = "defReg:chkData";

	/** Vector of <code>EPPDefRegFwdCheckResult</code> instances. */
	private Vector results;

	/**
	 * <code>EPPDefRegFwdCheckResp</code> default constructor.  It will set
	 * results attribute to an empty <code>Vector</code>.
	 */
	public EPPDefRegCheckResp() {
		results = new Vector();
	}

	// End EPPDefRegFwdCheckResp()

	/**
	 * <code>EPPDefRegFwdCheckResp</code> constructor that will set the result
	 * of an individual defReg.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aResult EPPDefRegCheckResult results.
	 */
	public EPPDefRegCheckResp(
							  EPPTransId aTransId, EPPDefRegCheckResult aResult) {
		super(aTransId);

		results = new Vector();
		results.addElement(aResult);
	}

	// End EPPDefRegFwdCheckResp(EPPTransId, EPPDefRegFwdCheckResp)

	/**
	 * <code>EPPDefRegFwdCheckResp</code> constructor that will set the result
	 * of multiple defRegs.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param someResults Vector of EPPDefRegFwdCheckResult instances.
	 */
	public EPPDefRegCheckResp(EPPTransId aTransId, Vector someResults) {
		super(aTransId);

		results = someResults;
	}

	// End EPPDefRegFwdCheckResp.EPPDefRegFwdCheckResp(EPPTransId, Vector)

	/**
	 * Get the EPP response type associated with
	 * <code>EPPDefRegFwdCheckResp</code>.
	 *
	 * @return EPPDefRegFwdCheckResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPDefRegFwdCheckResp.getType()

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPDefRegFwdCheckResp</code>.
	 *
	 * @return <code>EPPDefRegFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegFwdCheckResp.getNamespace()

	/**
	 * Compare an instance of <code>EPPDefRegFwdCheckResp</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegCheckResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegCheckResp theCheckData = (EPPDefRegCheckResp) aObject;

		// results
		if (!EPPUtil.equalVectors(results, theCheckData.results)) {
			return false;
		}

		return true;
	}

	// End EPPDefRegFwdCheckResp.equals(Object)

	/**
	 * Clone <code>EPPDefRegFwdCheckResp</code>.
	 *
	 * @return clone of <code>EPPDefRegFwdCheckResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegCheckResp clone = (EPPDefRegCheckResp) super.clone();
		clone.results = (Vector) results.clone();

		for (int i = 0; i < results.size(); i++) {
			clone.results.setElementAt(
									   ((EPPDefRegCheckResult) results
										.elementAt(i)).clone(), i);
		}

		return clone;
	}

	// End EPPDefRegFwdCheckResp.clone()

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

	// End EPPDefRegFwdCheckResp.toString()

	/**
	 * Set the results of a <code>EPPDefRegFwdCheckResp</code> Response.  There
	 * is     one <code>EPPDefRegFwdCheckResult</code> instance in
	 * <code>someResults</code> for each     defReg requested in the
	 * <code>EPPDefRegFwdCheckCmd</code> Command.
	 *
	 * @param someResults Vector of <code>EPPDefRegFwdCheckResult</code>
	 * 		  instances.
	 */
	public void setCheckResults(Vector someResults) {
		results = someResults;
	}

	// End EPPDefRegFwdCheckResp.setCheckResults(Vector)

	/**
	 * Get the results of a <code>EPPDefRegFwdCheckResp</code> Response.  There
	 * is     one <code>EPPDefRegFwdCheckResult</code> instance in
	 * <code>someResults</code> for each     defReg requested in the
	 * <code>EPPDefRegFwdCheckResult</code> Command.
	 *
	 * @return Vector of <code>EPPDefRegFwdCheckResult</code> instances.
	 */
	public Vector getCheckResults() {
		return results;
	}

	// End EPPDefRegFwdCheckResp.getResults()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegFwdCheckResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPDefRegFwdCheckResp</code> instance. defReg
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegFwdCheckResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);
		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Results
		EPPUtil.encodeCompVector(aDocument, root, results);

		return root;
	}

	// End EPPDefRegFwdCheckResp.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegFwdCheckResp</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegFwdCheckResp</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Results
		results =
			EPPUtil.decodeCompVector(
									 aElement, EPPDefRegMapFactory.NS,
									 EPPDefRegCheckResult.ELM_NAME,
									 EPPDefRegCheckResult.class);
	}

	// End EPPDefRegFwdCheckResp.doDecode(Element)
}
