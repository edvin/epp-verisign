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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUpdateCmd;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP Host &lt;update&gt; command. The EPP &lt;update&gt;
 * command provides a transform operation that allows a client to modify the
 * attributes of a contact object.  In addition to the standard EPP command
 * elements, the &lt;update&gt; command MUST contain a &lt;contact:update&gt;
 * element that identifies the contact namespace and the location of the
 * contact schema. The &lt;contact:update&gt; element SHALL contain the
 * following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier of
 * the contact object to be updated. Use <code>getId</code> and
 * <code>setId</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:add&gt; element that contains attribute values to be
 * added to the host object.  Use <code>getAdd</code> and <code>setAdd</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:rem&gt; element that contains attribute values to be
 * removed from the host object.  Use <code>getRemove</code> and
 * <code>setRemove</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;contact:chg&gt; element that contains attribute values to be
 * changed to the host object.  Use <code>getChange</code> and
 * <code>setChange</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPContactUpdateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.contact.EPPContactAddChange
 */
public class EPPContactUpdateCmd extends EPPUpdateCmd {
	/** XML Element Name of <code>EPPHostUpdateCmd</code> root element. */
	final static String ELM_NAME = "contact:update";

	/** XML Element Name for the name attribute. */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** Attributes to add */
	private EPPContactAddChange add = null;

	/** Attributes to remove */
	private EPPContactAddChange remove = null;

	/** Attributes to change */
	private EPPContactAddChange change = null;

	/** Contact ID */
	private java.lang.String id = null;
	
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPContactUpdateCmd.class.getName(),
						 EPPCatFactory.getInstance().getFactory());
	

	/**
	 * <code>EPPContactUpdateCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPContactUpdateCmd() {
	}

	// End EPPContactUpdateCmd.EPPContactUpdateCmd()

	/**
	 * <code>EPPContactUpdateCmd</code> constructor.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aId Contact ID
	 */
	public EPPContactUpdateCmd(String aTransId, String aId) {
		super(aTransId);

		id = aId;
	}

	// End EPPContactUpdateCmd.EPPContactUpdateCmd(String, String)

	/**
	 * <code>EPPContactUpdateCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aId Contact ID.
	 * @param aAdd Attributes to add to the contact.  <code>null</code> if no
	 * 		  additions.
	 * @param aRemove Attributes to remove from the contact.  <code>null</code>
	 * 		  if no removals.
	 * @param aChange Attributes to remove from the contact.  <code>null</code>
	 * 		  if no changes.
	 */
	public EPPContactUpdateCmd(
							   String aTransId, String aId,
							   EPPContactAddChange aAdd,
							   EPPContactAddChange aRemove,
							   EPPContactAddChange aChange) {
		super(aTransId);

		id     = aId;

		add = aAdd;

		if (add != null) {
			add.setMode(EPPContactAddChange.MODE_ADD);
		}

		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPContactAddChange.MODE_REMOVE);
		}

		change = aChange;

		if (change != null) {
			change.setMode(EPPContactAddChange.MODE_CHANGE);
		}
	}

	// End EPPContactUpdateCmd.EPPContactUpdateCmd(String, String, EPPContactAddChange, EPPContactAddChange, EPPContactAddChange)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactUpdateCmd</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactUpdateCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactUpdateCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPContactUpdateCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactUpdateCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (id == null) {
			throw new EPPEncodeException("required attribute id is not set");
		}

		if ((add == null) && (remove == null) && (change == null)) {
			throw new EPPEncodeException("at least one from [add, remove, change] needs to be set");
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// id
		EPPUtil.encodeString(
							 aDocument, root, id, EPPContactMapFactory.NS,
							 ELM_CONTACT_ID);

		// Add
		EPPUtil.encodeComp(aDocument, root, add);

		// Remove
		EPPUtil.encodeComp(aDocument, root, remove);

		// Change
		EPPUtil.encodeComp(aDocument, root, change);

		return root;
	}

	// End EPPContactUpdateCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPContactUpdateCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactUpdateCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Host Name
		id     = EPPUtil.decodeString(
									  aElement, EPPContactMapFactory.NS,
									  ELM_CONTACT_ID);

		// Add
		add = (EPPContactAddChange) EPPUtil.decodeComp(
													   aElement,
													   EPPContactMapFactory.NS,
													   EPPContactAddChange.ELM_ADD,
													   EPPContactAddChange.class);

		if (add != null) {
			add.setMode(EPPContactAddChange.MODE_ADD);
		}

		// Remove
		remove =
			(EPPContactAddChange) EPPUtil.decodeComp(
													 aElement,
													 EPPContactMapFactory.NS,
													 EPPContactAddChange.ELM_REMOVE,
													 EPPContactAddChange.class);

		if (remove != null) {
			remove.setMode(EPPContactAddChange.MODE_REMOVE);
		}

		// Change
		change =
			(EPPContactAddChange) EPPUtil.decodeComp(
													 aElement,
													 EPPContactMapFactory.NS,
													 EPPContactAddChange.ELM_CHANGE,
													 EPPContactAddChange.class);

		if (change != null) {
			change.setMode(EPPContactAddChange.MODE_CHANGE);
		}
	}

	// End EPPContactUpdateCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPContactUpdateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		cat.debug("equals(Object): enter");
		
		if (!(aObject instanceof EPPContactUpdateCmd)) {
			cat.error("equals(Object): aObject not EPPContactUpdateCmd");
			return false;
		}

		if (!super.equals(aObject)) {
			cat.error("equals(Object): parent not equal");
			return false;
		}

		EPPContactUpdateCmd theComp = (EPPContactUpdateCmd) aObject;

		// Id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			cat.error("equals(Object): id not equal");
			return false;
		}

		// Add
		if (!((add == null) ? (theComp.add == null) : add.equals(theComp.add))) {
			cat.error("equals(Object): add not equal");
			return false;
		}

		// Remove
		if (
			!(
					(remove == null) ? (theComp.remove == null)
										 : remove.equals(theComp.remove)
				)) {
			cat.error("equals(Object): remove not equal");
			return false;
		}

		// Change
		if (
			!(
					(change == null) ? (theComp.change == null)
										 : change.equals(theComp.change)
				)) {
			cat.error("equals(Object): change not equal");
			return false;
		}

		cat.debug("equals(Object): exit, return true");
		return true;
	}

	// End EPPContactUpdateCmd.equals(Object)

	/**
	 * Clone <code>EPPContactUpdateCmd</code>.
	 *
	 * @return clone of <code>EPPContactUpdateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactUpdateCmd clone = (EPPContactUpdateCmd) super.clone();

		if (clone.add != null) {
			clone.add = (EPPContactAddChange) add.clone();
		}

		if (clone.remove != null) {
			clone.remove = (EPPContactAddChange) remove.clone();
		}

		if (clone.change != null) {
			clone.change = (EPPContactAddChange) change.clone();
		}

		return clone;
	}

	// End EPPContactUpdateCmd.clone()

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

	// End EPPContactUpdateCmd.toString()

	/**
	 * Gets the items to add to the contact.
	 *
	 * @return Object that contains the list of elements to add to the contact
	 * 		   if defined; <code>null</code> otherwise.
	 */
	public EPPContactAddChange getAdd() {
		return add;
	}

	// End EPPContactUpdateCmd.getAdd()

	/**
	 * Gets the items to change to the contact.
	 *
	 * @return Object that contains the list of elements to change to the
	 * 		   contact if defined; <code>null</code> otherwise.
	 */
	public EPPContactAddChange getChange() {
		return change;
	}

	// End EPPContactUpdateCmd.getChange()

	/**
	 * Get contact id.
	 *
	 * @return String  Contact ID
	 */
	public java.lang.String getId() {
		return id;
	}

	// End EPPContactUpdateCmd.getId()

	/**
	 * Gets the items to remove to the contact.
	 *
	 * @return Object that contains the list of elements to remove to the
	 * 		   contact if defined; <code>null</code> otherwise.
	 */
	public EPPContactAddChange getRemove() {
		return remove;
	}

	// End EPPContactUpdateCmd.getRemove()

	/**
	 * Sets the items to add to the contact.
	 *
	 * @param newAdd Object that contains the list of elements to add to the
	 * 		  contact.
	 */
	public void setAdd(EPPContactAddChange newAdd) {
		add = newAdd;

		if (add != null) {
			add.setMode(EPPContactAddChange.MODE_ADD);
		}
	}

	// End EPPContactUpdateCmd.setAdd(EPPContactAddChange)

	/**
	 * Sets the items to change to the contact.
	 *
	 * @param newChange Object that contains the list of elements to change to
	 * 		  the contact.
	 */
	public void setChange(EPPContactAddChange newChange) {
		change = newChange;

		if (change != null) {
			change.setMode(EPPContactAddChange.MODE_CHANGE);
		}
	}

	// End EPPContactUpdateCmd.setChange(EPPContactAddChange)

	/**
	 * Set contact id.
	 *
	 * @param newId String
	 */
	public void setId(String newId) {
		id = newId;
	}

	// End EPPContactUpdateCmd.setId(String)

	/**
	 * Sets the items to remove to the contact.
	 *
	 * @param newRemove Object that contains the list of elements to remove to
	 * 		  the contact.
	 */
	public void setRemove(EPPContactAddChange newRemove) {
		remove = newRemove;

		if (remove != null) {
			remove.setMode(EPPContactAddChange.MODE_REMOVE);
		}
	}

	// End EPPContactUpdateCmd.setRemove(EPPContactAddChange)
}
