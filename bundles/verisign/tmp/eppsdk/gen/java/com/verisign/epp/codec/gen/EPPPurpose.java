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
package com.verisign.epp.codec.gen;


// Log4j Imports
import org.apache.log4j.Logger;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.verisign.epp.util.EPPCatFactory;


/**
 * The &lt;purpose&gt; element MUST contain one or more of the following child
 * elements that describe the purposes for which data is collected: <br>
 * 
 * <ul>
 * <li>
 * &lt;admin/&gt;: Administrative purposes.  Information can be used for
 * administrative and technical support of the provisioning system.
 * </li>
 * <li>
 * &lt;contact/&gt;: Contact for marketing purposes.  Information can be used
 * to contact individuals, through a communications channel other than the
 * protocol, for the promotion of a product or service.
 * </li>
 * <li>
 * &lt;prov/&gt;: Object provisioning purposes.  Information can be used to
 * identify objects and inter-object relationships.
 * </li>
 * <li>
 * &lt;other/&gt;: Other purposes.  Information may be used in other ways not
 * captured by the above definitions.
 * </li>
 * </ul>
 * 
 *
 * @author $Author: jim $
 * @version $Revision: 1.6 $
 *
 * @see com.verisign.epp.codec.gen.EPPStatement
 */
public class EPPPurpose implements EPPCodecComponent {
	/** Default XML root tag name for <code>purpose</code>element. */
	final static String ELM_NAME = "purpose";

	/** XML tag name for the <code>admin</code> attribute. */
	private static final String ELM_ADMIN = "admin";

	/** XML tag name for the <code>contact</code> element. */
	private static final String ELM_CONTACT = "contact";

	/** XML tag name for the <code>other</code> attribute. */
	private static final String ELM_OTHER = "other";

	/** XML tag name for the <code>tmReg</code> element. */
	private static final String ELM_PROV = "prov";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPPurpose.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Administrative purposes.  Information can be used for administrative and
	 * technical support of the provisioning system.
	 */
	private boolean admin = false;

	/**
	 * Contact for marketing purposes.  Information can be used to contact
	 * individuals, through a communications channel other than the protocol,
	 * for the promotion of a product or service.
	 */
	private boolean contact = false;

	/**
	 * Other purposes.  Information may be used in other ways not captured by
	 * the above definitions.
	 */
	private boolean other = false;

	/**
	 * Object provisioning purposes.  Information can be used to identify
	 * objects and inter-object relationships.
	 */
	private boolean prov = false;

	/**
	 * Default Constructor
	 */
	public EPPPurpose() {
	}

	/**
	 * Allocates a new <code>EPPPurpose</code> with different child elements
	 * based on the input parameters.
	 *
	 * @param aAdmin If set to <code>true</code> then the element
	 * 		  <code>admin</code> is created
	 * @param aContact If set to <code>true</code> then the element
	 * 		  <code>contact</code> is created
	 * @param aOther If set to <code>true</code> then the element
	 * 		  <code>other</code> is created
	 * @param aProv If set to <code>true</code> then the element
	 * 		  <code>prov</code> is created
	 */
	public EPPPurpose(
					  boolean aAdmin, boolean aContact, boolean aOther,
					  boolean aProv) {
		this.admin		 = aAdmin;
		this.contact     = aContact;
		this.other		 = aOther;
		this.prov		 = aProv;
	}

	// End EPPPurpose.EPPPurpose(boolean, boolean, boolean, boolean)

	/**
	 * &lt;admin&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isAdmin() {
		return this.admin;
	}

	/**
	 * Sets the &lt;admin&gt; element to specify contact for administrative
	 * purposes.
	 *
	 * @param aAdmin <code>true</code> to include the &lt;admin&gt; element;
	 * 		  <code>false</code> otherwise.
	 */
	public void setAdmin(boolean aAdmin) {
		this.admin = aAdmin;
	}

	/**
	 * &lt;contact&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isContact() {
		return this.contact;
	}

	/**
	 * Sets the &lt;contact&gt; element to specify contact for marketing
	 * purposes.
	 *
	 * @param aContact <code>true</code> to include the &lt;contact&gt;
	 * 		  element; <code>false</code> otherwise.
	 */
	public void setContact(boolean aContact) {
		this.contact = aContact;
	}

	/**
	 * &lt;other&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isOther() {
		return this.other;
	}

	/**
	 * Sets the &lt;other&gt; element to specify Other purposes.
	 *
	 * @param aOther <code>true</code> to include the &lt;other&gt; element;
	 * 		  <code>false</code> otherwise.
	 */
	public void setOther(boolean aOther) {
		this.other = aOther;
	}

	/**
	 * &lt;prov&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isProv() {
		return this.prov;
	}

	/**
	 * Sets the &lt;prov&gt; element to specify object provisioning purposes.
	 *
	 * @param aProv <code>true</code> to include the &lt;prov&gt; element;
	 * 		  <code>false</code> otherwise.
	 */
	public void setProv(boolean aProv) {
		this.prov = aProv;
	}

	/**
	 * encode <code>EPPPurpose</code> into a DOM element tree.  The "purpose"
	 * element is created and the child nodes are     appended as children.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return purpose root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate that at least one recipient is set
		if (!this.admin && !this.contact && !this.other && !this.prov) {
			cat.error("EPPPurpose.encode(): At least one purpose must be set");
			throw new EPPEncodeException("EPPPurpose.encode(): At least one purpose must be set");
		}

		Element theElm = null;
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Admin
		if (this.admin) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_ADMIN);
			root.appendChild(theElm);
		}

		// Contact
		if (this.contact) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_CONTACT);
			root.appendChild(theElm);
		}

		// Other
		if (this.other) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_OTHER);
			root.appendChild(theElm);
		}

		// Prov
		if (this.prov) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_PROV);
			root.appendChild(theElm);
		}

		return root;
	}

	// End EPPPurpose.encode(Document)

	/**
	 * Reset all attribute to their initial values.
	 */
	private void reset() {
		this.admin		 = false;
		this.contact     = false;
		this.other		 = false;
		this.prov		 = false;
	}

	// End reset()

	/**
	 * decode <code>EPPPurpose</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the &ltpurpose&gt element
	 * for a <code>EPPPurpose</code>
	 *
	 * @param aElement root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		NodeList theList = aElement.getChildNodes();

		reset();

		// For each child node
		for (int i = 0; i < theList.getLength(); i++) {
			Node theNode = theList.item(i);

			if (theNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element theElm = (Element) theNode;

			if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_ADMIN))) {
				this.admin = true;
			}
			else if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_CONTACT))) {
				this.contact = true;
			}
			else if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_OTHER))) {
				this.other = true;
			}
			else if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_PROV))) {
				this.prov = true;
			}
			else {
				cat.error("EPPPurpose.decode(): Unknown element " + theElm);
				throw new EPPDecodeException("EPPPurpose.decode(): Unknown element "
											 + theElm);
			}
		}

		// end for 
	}

	// End EPPPurpose.decode(Element)

	/**
	 * implements a  <code>EPPPurpose</code> compare.
	 *
	 * @param aObject <code>EPPPurpose</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPPurpose)) {
			cat.error("EPPPurpose.equals(): " + aObject.getClass().getName()
					  + " not EPPPurpose instance");

			return false;
		}

		EPPPurpose thePurpose = (EPPPurpose) aObject;

		if (this.admin != thePurpose.admin) {
			cat.error("EPPPurpose.equals(): admin not equal");

			return false;
		}

		if (this.contact != thePurpose.contact) {
			cat.error("EPPPurpose.equals(): contact not equal");

			return false;
		}

		if (this.other != thePurpose.other) {
			cat.error("EPPPurpose.equals(): other not equal");

			return false;
		}

		if (this.prov != thePurpose.prov) {
			cat.error("EPPPurpose.equals(): prov not equal");

			return false;
		}

		else {
			return true;
		}
	}

	// End EPPPurpose.equals(Object)

	/**
	 * Clone <code>EPPPurpose</code>.
	 *
	 * @return clone of <code>EPPPurpose</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPPurpose clone = null;

		clone = (EPPPurpose) super.clone();

		return clone;
	}

	// End EPPPurpose.clone()*/

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

	// End EPPPurpose.toString()
}


// End class EPPPurpose
