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
package com.verisign.epp.codec.host;

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
 * Represents an EPP Host &lt;host:chkData&gt; response to a
 * <code>EPPHostCheckCmd</code>. When a &lt;check&gt; command has been
 * processed successfully, the EPP &lt;resData&gt; element MUST contain a
 * child &lt;host:chkData&gt; element that identifies the host namespace and
 * the location of the host schema. The &lt;host:chkData&gt; element contains
 * one or more &lt;host:cd&gt; elements that contain the following child
 * elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains the fully qualified name of the
 * queried host object.  This element MUST contain an "avail" attribute whose
 * value indicates object availability at the moment the &lt;check&gt; command
 * was completed. A value of "1" or "true" menas that the object is availabe.
 * A value of "0" or "false" means that the object is not available.
 * </li>
 * <li>
 * An OPTIONAL &lt;host:reason&gt; element that MAY be provided when an object
 * is not available for provisioning.  If present, this element contains
 * server-specific text to help explain why the object is unavailable.  This
 * text MUST be represented in the response language previously negotiated
 * with the client; an OPTIONAL "lang" attribute MAY be present to identify
 * the language if the negotiated value is something other that a default
 * value of "en" (English).  Use <code>getCheckResults</code> and
 * <code>setCheckResults</code> to get and set the elements.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.host.EPPHostCheckCmd
 * @see com.verisign.epp.codec.host.EPPHostCheckResult
 */
public class EPPHostCheckResp extends EPPResponse {
	/** XML Element Name of <code>EPPHostCheckResp</code> root element. */
	final static String ELM_NAME = "host:chkData";

	/** Vector of <code>EPPCheckHostResult</code> instances. */
	private Vector results;

	/**
	 * <code>EPPHostCheckResp</code> default constructor.  It will set results
	 * attribute to an empty <code>Vector</code>.
	 */
	public EPPHostCheckResp() {
		results = new Vector();
	}

	// End EPPHostCheckResp()

	/**
	 * <code>EPPHostCheckResp</code> constructor that will set the result of an
	 * individual host.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param aResult Result of a single host name.
	 */
	public EPPHostCheckResp(EPPTransId aTransId, EPPHostCheckResult aResult) {
		super(aTransId);

		results = new Vector();
		results.addElement(aResult);
	}

	// End EPPHostCheckResp(EPPTransId, EPPCheckHostResult)

	/**
	 * <code>EPPHostCheckResp</code> constructor that will set the result of
	 * multiple hosts.
	 *
	 * @param aTransId Transaction Id associated with response.
	 * @param someResults Vector of EPPCheckHostResult instances.
	 */
	public EPPHostCheckResp(EPPTransId aTransId, Vector someResults) {
		super(aTransId);

		results = someResults;
	}

	// End EPPHostCheckResp.EPPHostCheckResp(EPPTransId, Vector)

	/**
	 * Get the EPP response type associated with <code>EPPHostCheckResp</code>.
	 *
	 * @return EPPHostCheckResp.ELM_NAME
	 */
	public String getType() {
		return ELM_NAME;
	}

	// End EPPHostCheckResp.getType()

	/**
	 * Get the EPP command Namespace associated with
	 * <code>EPPHostCheckResp</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostCheckResp.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostCheckResp</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    Root DOM Element representing the
	 * 		   <code>EPPHostCheckResp</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostCheckResp</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Results
		EPPUtil.encodeCompVector(aDocument, root, results);

		return root;
	}

	// End EPPHostCheckResp.doEncode(Document)

	/**
	 * Decode the <code>EPPHostCheckResp</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostCheckResp</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Results
		results =
			EPPUtil.decodeCompVector(
									 aElement, EPPHostMapFactory.NS,
									 EPPHostCheckResult.ELM_NAME,
									 EPPHostCheckResult.class);
	}

	// End EPPHostCheckResp.doDecode(Element)

	/**
	 * Compare an instance of <code>EPPHostCheckResp</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostCheckResp)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostCheckResp thePingData = (EPPHostCheckResp) aObject;

		// results
		if (!EPPUtil.equalVectors(results, thePingData.results)) {
			return false;
		}

		return true;
	}

	// End EPPHostCheckResp.equals(Object)

	/**
	 * Clone <code>EPPHostCheckResp</code>.
	 *
	 * @return clone of <code>EPPHostCheckResp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostCheckResp clone = (EPPHostCheckResp) super.clone();

		clone.results = (Vector) results.clone();

		for (int i = 0; i < results.size(); i++)
			clone.results.setElementAt(
									   ((EPPHostCheckResult) results.elementAt(i))
									   .clone(), i);

		return clone;
	}

	// End EPPHostCheckResp.clone()

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

	// End EPPHostCheckResp.toString()

	/**
	 * Set the results of a <code>EPPHostCheckResp</code> Response.  There is
	 * one <code>EPPCheckHostResult</code> instance in
	 * <code>someResults</code> for each     host requested in the
	 * <code>EPPHostCheckCmd</code> Command.
	 *
	 * @param someResults Vector of <code>EPPCheckHostResult</code> instances.
	 */
	public void setCheckResults(Vector someResults) {
		results = someResults;
	}

	// End EPPHostCheckResp.setCheckResults(Vector)

	/**
	 * Get the results of a EPPHostCheckResp Response.  There is     one
	 * <code>EPPCheckHostResult</code> instance in <code>someResults</code>
	 * for each     host requested in the <code>EPPCheckHostResult</code>
	 * Command.
	 *
	 * @return Vector of <code>EPPHostResult</code> instances.
	 */
	public Vector getCheckResults() {
		return results;
	}

	// End EPPHostCheckResp.getResults()
}
