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
 * Represents an EPP EmailFwd &lt;update&gt; command. The EPP &lt;update&gt;
 * command provides a transform operation that allows a client to modify the
 * attributes of a emailFwd object.  In addition to the standard EPP command
 * elements, the &lt;update&gt; command MUST contain a &lt;emailFwd:update&gt;
 * element that identifies the emailFwd namespace and the location of the
 * emailFwd schema. In addition to  The &lt;emailFwd:update&gt; element SHALL
 * contain the following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name of the object to be updated.  Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:add&gt; element that contains attribute values to
 * be added to the emailFwd object.  Use <code>getAdd</code> and
 * <code>setAdd</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:chg&gt; element that contains attribute values to
 * be change to the emailFwd object.  Use <code>getChange</code> and
 * <code>setChange</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;emailFwd:rem&gt; element that contains attribute values to
 * be removed from the emailFwd object.  Use <code>getRemove</code> and
 * <code>setRemove</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPEmailFwdUpdateCmd</code>.
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdAddRemove
 */
public class EPPEmailFwdUpdateCmd extends EPPUpdateCmd {
	/** XML Element Name of <code>EPPEmailFwdUpdateCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:update";

	/**
	 * XML Element Name of a emailFwd name in a
	 * <code>EPPEmailFwdDeleteCmd</code>.
	 */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** EmailFwd Name of emailFwd to update. */
	private String name = null;

	/** Attributes to add */
	private EPPEmailFwdAddRemove add = null;

	/** Attributes to remove */
	private EPPEmailFwdAddRemove remove = null;

	/** Attributes to change */
	private EPPEmailFwdAddRemove change = null;

	/**
	 * <code>EPPEmailFwdUpdateCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPEmailFwdUpdateCmd() {
	}

	// End EPPEmailFwdUpdateCmd.EPPEmailFwdUpdateCmd()

	/**
	 * <code>EPPEmailFwdUpdateCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aName EmailFwd name to update.
	 * @param aAdd Attributes to add to the emailFwd.  <code>null</code> if no
	 * 		  additions.
	 * @param aRemove Attributes to remove from the emailFwd. <code>null</code>
	 * 		  if no removals.
	 * @param aChange DOCUMENT ME!
	 */
	public EPPEmailFwdUpdateCmd(
								String aTransId, String aName,
								EPPEmailFwdAddRemove aAdd,
								EPPEmailFwdAddRemove aRemove,
								EPPEmailFwdAddRemove aChange) {
		super(aTransId);

		name     = aName;

		add = aAdd;

		if (add != null) {
			add.setMode(EPPEmailFwdAddRemove.MODE_ADD);
		}

		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPEmailFwdAddRemove.MODE_REMOVE);
		}

		change = aChange;

		if (change != null) {
			change.setMode(EPPEmailFwdAddRemove.MODE_CHANGE);
		}
	}

	// End EPPEmailFwdUpdateCmd.EPPEmailFwdUpdateCmd(String, String, EPPEmailFwdAddRemove, EPPEmailFwdAddRemove, EPPEmailFwdAddRemove)

	/**
	 * <code>EPPEmailFwdUpdateCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 *
	 * @param aName DOCUMENT ME!
	 */
	public EPPEmailFwdUpdateCmd(String aName) {
		name = aName;
	}

	// End EPPEmailFwdUpdateCmd.EPPEmailFwdUpdateCmd()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdUpdateCmd</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdUpdateCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdUpdateCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPEmailFwdUpdateCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdUpdateCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);

		// Add
		EPPUtil.encodeComp(aDocument, root, add);

		// Remove
		EPPUtil.encodeComp(aDocument, root, remove);

		// Change
		EPPUtil.encodeComp(aDocument, root, change);

		return root;
	}

	// End EPPEmailFwdUpdateCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdUpdateCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdUpdateCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// EmailFwd Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);

		// Add
		add = (EPPEmailFwdAddRemove) EPPUtil.decodeComp(
														aElement,
														EPPEmailFwdMapFactory.NS,
														EPPEmailFwdAddRemove.ELM_ADD,
														EPPEmailFwdAddRemove.class);

		if (add != null) {
			add.setMode(EPPEmailFwdAddRemove.MODE_ADD);
		}

		// Remove
		remove =
			(EPPEmailFwdAddRemove) EPPUtil.decodeComp(
													  aElement,
													  EPPEmailFwdMapFactory.NS,
													  EPPEmailFwdAddRemove.ELM_REMOVE,
													  EPPEmailFwdAddRemove.class);

		if (remove != null) {
			remove.setMode(EPPEmailFwdAddRemove.MODE_REMOVE);
		}

		// Change
		change =
			(EPPEmailFwdAddRemove) EPPUtil.decodeComp(
													  aElement,
													  EPPEmailFwdMapFactory.NS,
													  EPPEmailFwdAddRemove.ELM_CHANGE,
													  EPPEmailFwdAddRemove.class);

		if (change != null) {
			change.setMode(EPPEmailFwdAddRemove.MODE_CHANGE);
		}
	}

	// End EPPEmailFwdUpdateCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPEmailFwdUpdateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdUpdateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdUpdateCmd theComp = (EPPEmailFwdUpdateCmd) aObject;

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

	// End EPPEmailFwdUpdateCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdUpdateCmd</code>.
	 *
	 * @return clone of <code>EPPEmailFwdUpdateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdUpdateCmd clone = (EPPEmailFwdUpdateCmd) super.clone();

		if (clone.add != null) {
			clone.add = (EPPEmailFwdAddRemove) add.clone();
		}

		if (clone.remove != null) {
			clone.remove = (EPPEmailFwdAddRemove) remove.clone();
		}

		if (clone.change != null) {
			clone.change = (EPPEmailFwdAddRemove) change.clone();
		}

		return clone;
	}

	// End EPPEmailFwdRenewCmd.clone()

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

	// End EPPEmailFwdRenewCmd.toString()

	/**
	 * Gets the emailFwd name to update.
	 *
	 * @return EmailFwd Name    if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdUpdateCmd.getName()

	/**
	 * Sets the emailFwd name to update.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdUpdateCmd.setName(String)

	/**
	 * Gets the items to add to the emailFwd.
	 *
	 * @return Object that contains the list of elements to add to the emailFwd
	 * 		   if defined; <code>null</code> otherwise.
	 */
	public EPPEmailFwdAddRemove getAdd() {
		return add;
	}

	// End EPPEmailFwdUpdateCmd.getAdd()

	/**
	 * Sets the items to add to the emailFwd.
	 *
	 * @param aAdd Object that contains the list of elements to add to the
	 * 		  emailFwd.
	 */
	public void setAdd(EPPEmailFwdAddRemove aAdd) {
		add = aAdd;

		if (add != null) {
			add.setMode(EPPEmailFwdAddRemove.MODE_ADD);
		}
	}

	// End EPPEmailFwdUpdateCmd.setAdd(EPPEmailFwdAddRemove)

	/**
	 * Gets the items to remove from the emailFwd.
	 *
	 * @return Object that contains the list of elements to remove from the
	 * 		   emailFwd if defined; <code>null</code> otherwise.
	 */
	public EPPEmailFwdAddRemove getRemove() {
		return remove;
	}

	// End EPPEmailFwdUpdateCmd.getRemove()

	/**
	 * Sets the items to remove from the emailFwd.
	 *
	 * @param aRemove Object that contains the list of elements to remove from
	 * 		  the emailFwd.
	 */
	public void setRemove(EPPEmailFwdAddRemove aRemove) {
		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPEmailFwdAddRemove.MODE_REMOVE);
		}
	}

	// End EPPEmailFwdUpdateCmd.setRemove(EPPEmailFwdAddRemove)

	/**
	 * Gets the items to change to the emailFwd.
	 *
	 * @return Object that contains the list of elements to change to the
	 * 		   emailFwd if defined; <code>null</code> otherwise.
	 */
	public EPPEmailFwdAddRemove getChange() {
		return change;
	}

	// End EPPEmailFwdUpdateCmd.getChange()

	/**
	 * Sets the items to change to the emailFwd.
	 *
	 * @param newChange Object that contains the list of elements to change to
	 * 		  the emailFwdemailFwdemailFwd.
	 */
	public void setChange(EPPEmailFwdAddRemove newChange) {
		change = newChange;

		if (change != null) {
			change.setMode(EPPEmailFwdAddRemove.MODE_CHANGE);
		}
	}

	// End EPPEmailFwdUpdateCmd.setChange(EPPEmailFwdAddRemove)
}
