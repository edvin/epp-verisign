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


// W3C Imports
import org.w3c.dom.*;

import java.util.Iterator;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Import
import java.util.Vector;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.host.EPPHostAddress;


/**
 * DOCUMENT ME!
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPHostAttr implements EPPCodecComponent {
	/** XML Element Name of <code>EPPHostAttr</code> root element. */
	final static java.lang.String ELM_NAME = "domain:hostAttr";

	/** XML tag name for the <code>hostName</code> element. */
	private final static java.lang.String ELM_HOST_NAME = "domain:hostName";

	/** XML attribute name for the <code>hostAttr</code> element. */
	private final static java.lang.String ELM_ADDRESS = "domain:hostAddr";

	/** Host name */
	private String name = null;

	/**
	 * Host addresses as a <code>Vector</code> of <code>EPPHostAddress</code>
	 * instances.
	 */
	private Vector addresses = null;

	/**
	 * <code>EPPHostAttr</code> default constructor.
	 */
	public EPPHostAttr() {
	}

	// End EPPHostAttr.EPPHostAttr()

	/**
	 * <code>EPPHostAttr</code> constructor the takes the host name.
	 *
	 * @param aName the fully qualified name of a host
	 */
	public EPPHostAttr(String aName) {
		this.name = aName;
	}

	// End EPPHostAttr.EPPHostAttr(String)

	/**
	 * <code>EPPHostAttr</code> constructor the takes the host name and a
	 * <code>Vector</code> of host addresses.
	 *
	 * @param aName the fully qualified name of a host
	 * @param aAddresses <code>Vector</code> of <code>EPPHostAddress</code>
	 * 		  instances
	 */
	public EPPHostAttr(String aName, Vector aAddresses) {
		this.name = aName;
		this.setAddresses(aAddresses);
	}

	// End EPPHostAttr.EPPHostAttr(String, Vector)

	/**
	 * Get the host name.
	 *
	 * @return Host Name
	 */
	public String getName() {
		return this.name;
	}

	// End EPPHostAttr.getName()

	/**
	 * Set the host name.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	// End EPPHostAttr.setName(String)

	/**
	 * Gets the host addresses.
	 *
	 * @return Vector of <code>EPPHostAddress</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getAddresses() {
		return this.addresses;
	}

	// End EPPHostAttr.getAddresses()

	/**
	 * Sets the host addresses.
	 *
	 * @param aAddresses Vector of <code>EPPHostAddress</code> instances.
	 */
	public void setAddresses(Vector aAddresses) {
		this.addresses = aAddresses;

		// Set room tag of addresses
		if (this.addresses != null) {
			Iterator theIter = this.addresses.iterator();

			while (theIter.hasNext()) {
				EPPHostAddress currAddress = (EPPHostAddress) theIter.next();

				currAddress.setRootName(EPPDomainMapFactory.NS, ELM_ADDRESS);
			}
		}
	}

	// End EPPHostAttr.setAddresses(Vector)

	/**
	 * Clone <code>EPPHostAttr</code>.
	 *
	 * @return clone of <code>EPPHostAttr</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostAttr clone = (EPPHostAttr) super.clone();

		if (this.addresses != null) {
			clone.addresses = (Vector) this.addresses.clone();

			for (int i = 0; i < this.addresses.size(); i++)
				clone.addresses.setElementAt(
											 ((EPPHostAddress) this.addresses
											  .elementAt(i)).clone(), i);
		}

		return clone;
	}

	// End EPPHostAttr.clone()

	/**
	 * Decode the EPPHostAttr attributes from the aElement DOM Element tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPHostAttr from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Host Name
		this.name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS, ELM_HOST_NAME);

		// Addresses
		this.addresses =
			EPPUtil.decodeCompVector(
									 aElement, EPPDomainMapFactory.NS,
									 ELM_ADDRESS, EPPHostAddress.class);

		if (this.addresses.size() == 0) {
			this.addresses = null;
		}
	}

	// End EPPHostAttr.decode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPHostAttr
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built. Used as an Element
	 * 		  factory.
	 *
	 * @return Element - Root DOM Element representing the EPPHostAttr
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPHostAttr instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		if (this.name == null) {
			throw new EPPEncodeException("EPPHostAttr: Host name is required");
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_HOST_NAME);

		// Addresses
		EPPUtil.encodeCompVector(aDocument, root, this.addresses);

		return root;
	}

	// End EPPHostAttr.encode(Document)

	/**
	 * implements a deep <code>EPPHostAttr</code> compare.
	 *
	 * @param aObject <code>EPPHostAttr</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostAttr)) {
			return false;
		}

		EPPHostAttr theComp = (EPPHostAttr) aObject;

		// Name
		if (
			!(
					(this.name == null) ? (theComp.name == null)
											: this.name.equals(theComp.name)
				)) {
			return false;
		}

		// Addresses
		if (!EPPUtil.equalVectors(this.addresses, theComp.addresses)) {
			return false;
		}

		return true;
	}

	// End EPPHostAttr.equals(Object)

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

	// End EPPHostAttr.toString()
}


// End class EPPHostAttr
