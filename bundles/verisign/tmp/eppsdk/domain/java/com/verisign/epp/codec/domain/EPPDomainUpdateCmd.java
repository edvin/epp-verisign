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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Domain &lt;update&gt; command. The EPP &lt;update&gt;
 * command provides a transform operation that allows a client to modify the
 * attributes of a domain object.  In addition to the standard EPP command
 * elements, the &lt;update&gt; command MUST contain a &lt;domain:update&gt;
 * element that identifies the domain namespace and the location of the domain
 * schema. In addition to  The &lt;domain:update&gt; element SHALL contain the
 * following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;domain:name&gt; element that contains the fully qualified domain name
 * of the object to be updated.  Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:add&gt; element that contains attribute values to be
 * added to the domain object.  Use <code>getAdd</code> and
 * <code>setAdd</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:chg&gt; element that contains attribute values to be
 * change to the domain object.  Use <code>getChange</code> and
 * <code>setChange</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;domain:rem&gt; element that contains attribute values to be
 * removed from the domain object.  Use <code>getRemove</code> and
 * <code>setRemove</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPDomainUpdateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.domain.EPPDomainAddRemove
 */
public class EPPDomainUpdateCmd extends EPPUpdateCmd {
	/** XML Element Name of <code>EPPDomainUpdateCmd</code> root element. */
	final static String ELM_NAME = "domain:update";

	/**
	 * XML Element Name of a domain name in a <code>EPPDomainDeleteCmd</code>.
	 */
	private final static String ELM_DOMAIN_NAME = "domain:name";

	/** Domain Name of domain to update. */
	private String name = null;

	/** Attributes to add */
	private EPPDomainAddRemove add = null;

	/** Attributes to remove */
	private EPPDomainAddRemove remove = null;

	/** Attributes to change */
	private EPPDomainAddRemove change = null;

	/**
	 * <code>EPPDomainUpdateCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDomainUpdateCmd() {
	}

	// End EPPDomainUpdateCmd.EPPDomainUpdateCmd()

	/**
	 * <code>EPPDomainUpdateCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 *
	 * @param aName DOCUMENT ME!
	 */
	public EPPDomainUpdateCmd(String aName) {
		name = aName;
	}

	// End EPPDomainUpdateCmd.EPPDomainUpdateCmd()

	/**
	 * <code>EPPDomainUpdateCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aName Domain name to update.
	 * @param aAdd Attributes to add to the domain.  <code>null</code> if no
	 * 		  additions.
	 * @param aRemove Attributes to remove from the domain.  <code>null</code>
	 * 		  if no removals.
	 * @param aChange DOCUMENT ME!
	 */
	public EPPDomainUpdateCmd(
							  String aTransId, String aName,
							  EPPDomainAddRemove aAdd,
							  EPPDomainAddRemove aRemove,
							  EPPDomainAddRemove aChange) {
		super(aTransId);

		name = aName;

		setAdd(aAdd);
		setRemove(aRemove);
		setChange(aChange);
	}

	// End EPPDomainUpdateCmd.EPPDomainUpdateCmd(String, String, EPPDomainAddRemove, EPPDomainAddRemove, EPPDomainAddRemove)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDomainUpdateCmd</code>.
	 *
	 * @return <code>EPPDomainMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDomainMapFactory.NS;
	}

	// End EPPDomainUpdateCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDomainUpdateCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDomainUpdateCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDomainUpdateCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDomainMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:domain", EPPDomainMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDomainMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPDomainMapFactory.NS,
							 ELM_DOMAIN_NAME);
		
		
		// All add, remove, and change attribute are null?
		if (add == null && remove == null && change == null) {
			// Create empty change element to support update extensions
			// like rgp and sync.
			this.setChange(new EPPDomainAddRemove());
			EPPUtil.encodeComp(aDocument, root, change);			
		}
		else {
			// Add
			EPPUtil.encodeComp(aDocument, root, add);

			// Remove
			EPPUtil.encodeComp(aDocument, root, remove);

			// Change
			EPPUtil.encodeComp(aDocument, root, change);			
		}


		return root;
	}

	// End EPPDomainUpdateCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDomainUpdateCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDomainUpdateCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Domain Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPDomainMapFactory.NS,
								 ELM_DOMAIN_NAME);

		// Add
		add = (EPPDomainAddRemove) EPPUtil.decodeComp(
													  aElement,
													  EPPDomainMapFactory.NS,
													  EPPDomainAddRemove.ELM_ADD,
													  EPPDomainAddRemove.class);

		if (add != null) {
			add.setMode(EPPDomainAddRemove.MODE_ADD);
		}

		// Remove
		remove =
			(EPPDomainAddRemove) EPPUtil.decodeComp(
													aElement,
													EPPDomainMapFactory.NS,
													EPPDomainAddRemove.ELM_REMOVE,
													EPPDomainAddRemove.class);

		if (remove != null) {
			remove.setMode(EPPDomainAddRemove.MODE_REMOVE);
		}

		// Change
		change =
			(EPPDomainAddRemove) EPPUtil.decodeComp(
													aElement,
													EPPDomainMapFactory.NS,
													EPPDomainAddRemove.ELM_CHANGE,
													EPPDomainAddRemove.class);

		if (change != null) {
			change.setMode(EPPDomainAddRemove.MODE_CHANGE);
		}
	}

	// End EPPDomainUpdateCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPDomainUpdateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDomainUpdateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDomainUpdateCmd theComp = (EPPDomainUpdateCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// Add
		if (!((add == null) ? (theComp.add == null) : add.equals(theComp.add))) {
			return false;
		}

		// Remove
		if (
			!(
					(remove == null) ? (theComp.remove == null)
										 : remove.equals(theComp.remove)
				)) {
			return false;
		}

		// Change
		if (
			!(
					(change == null) ? (theComp.change == null)
										 : change.equals(theComp.change)
				)) {
			return false;
		}

		return true;
	}

	// End EPPDomainUpdateCmd.equals(Object)

	/**
	 * Clone <code>EPPDomainUpdateCmd</code>.
	 *
	 * @return clone of <code>EPPDomainUpdateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDomainUpdateCmd clone = (EPPDomainUpdateCmd) super.clone();

		if (clone.add != null) {
			clone.add = (EPPDomainAddRemove) add.clone();
		}

		if (clone.remove != null) {
			clone.remove = (EPPDomainAddRemove) remove.clone();
		}

		if (clone.change != null) {
			clone.change = (EPPDomainAddRemove) change.clone();
		}

		return clone;
	}

	// End EPPDomainRenewCmd.clone()

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

	// End EPPDomainRenewCmd.toString()

	/**
	 * Gets the domain name to update.
	 *
	 * @return Domain Name    if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPDomainUpdateCmd.getName()

	/**
	 * Sets the domain name to update.
	 *
	 * @param aName Domain Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPDomainUpdateCmd.setName(String)

	/**
	 * Gets the items to add to the domain.
	 *
	 * @return Object that contains the list of elements to add to the domain
	 * 		   if defined; <code>null</code> otherwise.
	 */
	public EPPDomainAddRemove getAdd() {
		return add;
	}

	// End EPPDomainUpdateCmd.getAdd()

	/**
	 * Sets the items to add to the domain.
	 *
	 * @param aAdd Object that contains the list of elements to add to the
	 * 		  domain.
	 */
	public void setAdd(EPPDomainAddRemove aAdd) {
		add = aAdd;

		if (add != null) {
			add.setMode(EPPDomainAddRemove.MODE_ADD);
		}
	}

	// End EPPDomainUpdateCmd.setAdd(EPPDomainAddRemove)

	/**
	 * Gets the items to remove from the domain.
	 *
	 * @return Object that contains the list of elements to remove from the
	 * 		   domain if defined; <code>null</code> otherwise.
	 */
	public EPPDomainAddRemove getRemove() {
		return remove;
	}

	// End EPPDomainUpdateCmd.getRemove()

	/**
	 * Sets the items to remove from the domain.
	 *
	 * @param aRemove Object that contains the list of elements to remove from
	 * 		  the domain.
	 */
	public void setRemove(EPPDomainAddRemove aRemove) {
		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPDomainAddRemove.MODE_REMOVE);
		}
	}

	// End EPPDomainUpdateCmd.setRemove(EPPDomainAddRemove)

	/**
	 * Gets the items to change to the domain.
	 *
	 * @return Object that contains the list of elements to change to the
	 * 		   domain if defined; <code>null</code> otherwise.
	 */
	public EPPDomainAddRemove getChange() {
		return change;
	}

	// End EPPDomainUpdateCmd.getChange()

	/**
	 * Sets the items to change to the domain.
	 *
	 * @param newChange Object that contains the list of elements to change to
	 * 		  the domain.
	 */
	public void setChange(EPPDomainAddRemove newChange) {
		change = newChange;

		if (change != null) {
			change.setMode(EPPDomainAddRemove.MODE_CHANGE);
		}
	}

	// End EPPDomainUpdateCmd.setChange(EPPDomainAddRemove)
}
