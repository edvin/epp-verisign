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
 * Represents an EPP DefReg &lt;update&gt; command. The EPP &lt;update&gt;
 * command provides a transform operation that allows a client to modify the
 * attributes of a defReg object.  In addition to the standard EPP command
 * elements, the &lt;update&gt; command MUST contain a &lt;defReg:update&gt;
 * element that identifies the defReg namespace and the location of the defReg
 * schema. In addition to  The &lt;defReg:update&gt; element SHALL contain the
 * following child elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:roid&gt; element that contains the roid of the defReg object to
 * be updated.  Use <code>getRoid</code> and <code>setRoid</code> to get and
 * set the element.
 * </li>
 * <li>
 * An  &lt;defReg:chg&gt; element that contains attribute values to be change
 * to the defReg object.  Use <code>getChange</code> and
 * <code>setChange</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPDefRegUpdateCmd</code>.
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.defReg.EPPDefRegAddRemove
 */
public class EPPDefRegUpdateCmd extends EPPUpdateCmd {
	/** XML Element Name of <code>EPPDefRegUpdateCmd</code> root element. */
	final static String ELM_NAME = "defReg:update";

	/**
	 * XML Element Name of a defReg roid in a <code>EPPDefRegDeleteCmd</code>.
	 */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** DefReg Roid of defReg to update. */
	private String roid = null;

	/** Attributes to add */
	private EPPDefRegAddRemove add = null;

	/** Attributes to remove */
	private EPPDefRegAddRemove remove = null;

	/** Attributes to change */
	private EPPDefRegAddRemove change = null;

	/**
	 * <code>EPPDefRegUpdateCmd</code> default constructor.  The roid is
	 * initialized to <code>null</code>.     The roid must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDefRegUpdateCmd() {
	}

	// End EPPDefRegUpdateCmd.EPPDefRegUpdateCmd()

	/**
	 * <code>EPPDefRegUpdateCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aRoid DefReg roid to update.
	 * @param aAdd DOCUMENT ME!
	 * @param aRemove DOCUMENT ME!
	 * @param aChange Attributes to add to the defReg.  <code>null</code> if no
	 * 		  additions.
	 */
	public EPPDefRegUpdateCmd(
							  String aTransId, String aRoid,
							  EPPDefRegAddRemove aAdd,
							  EPPDefRegAddRemove aRemove,
							  EPPDefRegAddRemove aChange) {
		super(aTransId);

		roid     = aRoid;

		add = aAdd;

		add.setMode(EPPDefRegAddRemove.MODE_ADD);

		remove = aRemove;

		remove.setMode(EPPDefRegAddRemove.MODE_REMOVE);

		change = aChange;

		change.setMode(EPPDefRegAddRemove.MODE_CHANGE);
	}

	// End EPPDefRegUpdateCmd.EPPDefRegUpdateCmd(String, String,  EPPDefRegAddRemove)

	/**
	 * <code>EPPDefRegUpdateCmd</code> default constructor.  The roid is
	 * initialized to <code>null</code>.     The Roid must be set before
	 * invoking <code>encode</code>.
	 *
	 * @param aRoid DOCUMENT ME!
	 */
	public EPPDefRegUpdateCmd(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegUpdateCmd.EPPDefRegUpdateCmd()

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegUpdateCmd</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegUpdateCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegUpdateCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPDefRegUpdateCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegUpdateCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (roid == null) {
			throw new EPPEncodeException("roid required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);

		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_ROID);

		// Add
		EPPUtil.encodeComp(aDocument, root, add);

		// Remove
		EPPUtil.encodeComp(aDocument, root, remove);

		// Change
		EPPUtil.encodeComp(aDocument, root, change);

		return root;
	}

	// End EPPDefRegUpdateCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegUpdateCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPDefRegUpdateCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// DefReg Roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

		// Add
		add = (EPPDefRegAddRemove) EPPUtil.decodeComp(
													  aElement,
													  EPPDefRegMapFactory.NS,
													  EPPDefRegAddRemove.ELM_ADD,
													  EPPDefRegAddRemove.class);

		if (add != null) {
			add.setMode(EPPDefRegAddRemove.MODE_ADD);
		}

		// Remove
		remove =
			(EPPDefRegAddRemove) EPPUtil.decodeComp(
													aElement,
													EPPDefRegMapFactory.NS,
													EPPDefRegAddRemove.ELM_REMOVE,
													EPPDefRegAddRemove.class);

		if (remove != null) {
			remove.setMode(EPPDefRegAddRemove.MODE_REMOVE);
		}

		// Change
		change =
			(EPPDefRegAddRemove) EPPUtil.decodeComp(
													aElement,
													EPPDefRegMapFactory.NS,
													EPPDefRegAddRemove.ELM_CHANGE,
													EPPDefRegAddRemove.class);

		if (change != null) {
			change.setMode(EPPDefRegAddRemove.MODE_CHANGE);
		}
	}

	// End EPPDefRegUpdateCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPDefRegUpdateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegUpdateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegUpdateCmd theComp = (EPPDefRegUpdateCmd) aObject;

		// Roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
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

	// End EPPDefRegUpdateCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegUpdateCmd</code>.
	 *
	 * @return clone of <code>EPPDefRegUpdateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegUpdateCmd clone = (EPPDefRegUpdateCmd) super.clone();

		if (clone.change != null) {
			clone.change = (EPPDefRegAddRemove) change.clone();
		}

		return clone;
	}

	// End EPPDefRegRenewCmd.clone()

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

	// End EPPDefRegRenewCmd.toString()

	/**
	 * Gets the defReg roid to update.
	 *
	 * @return DefReg roid    if defined; <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegUpdateCmd.getRoid()

	/**
	 * Sets the defReg roid to update.
	 *
	 * @param aRoid DefReg Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegUpdateCmd.setRoid(String)

	/**
	 * Gets the items to change to the defReg.
	 *
	 * @return Object that contains the list of elements to change to the
	 * 		   defReg if defined; <code>null</code> otherwise.
	 */
	public EPPDefRegAddRemove getChange() {
		return change;
	}

	// End EPPDefRegUpdateCmd.getChange()

	/**
	 * Sets the items to change to the defReg.
	 *
	 * @param newChange Object that contains the list of elements to change to
	 * 		  the defRegdefRegdefReg.
	 */
	public void setChange(EPPDefRegAddRemove newChange) {
		change = newChange;

		if (change != null) {
			change.setMode(EPPDefRegAddRemove.MODE_CHANGE);
		}
	}

	// End EPPDefRegUpdateCmd.setChange(EPPDefRegAddRemove)

	/**
	 * Gets the items to add to the DefReg.
	 *
	 * @return Object that contains the list of elements to add to the DefReg
	 * 		   if defined; <code>null</code> otherwise.
	 */
	public EPPDefRegAddRemove getAdd() {
		return add;
	}

	// End EPPDefRegUpdateCmd.getAdd()

	/**
	 * Sets the items to add to the DefReg.
	 *
	 * @param aAdd Object that contains the list of elements to add to the
	 * 		  DefReg.
	 */
	public void setAdd(EPPDefRegAddRemove aAdd) {
		add = aAdd;

		if (add != null) {
			add.setMode(EPPDefRegAddRemove.MODE_ADD);
		}
	}

	// End EPPDefRegUpdateCmd.setAdd(EPPDefRegAddRemove)

	/**
	 * Gets the items to remove from the DefReg.
	 *
	 * @return Object that contains the list of elements to remove from the
	 * 		   DefReg if defined; <code>null</code> otherwise.
	 */
	public EPPDefRegAddRemove getRemove() {
		return remove;
	}

	// End EPPDefRegUpdateCmd.getRemove()

	/**
	 * Sets the items to remove from the DefReg.
	 *
	 * @param aRemove Object that contains the list of elements to remove from
	 * 		  the DefReg.
	 */
	public void setRemove(EPPDefRegAddRemove aRemove) {
		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPDefRegAddRemove.MODE_REMOVE);
		}
	}

	// End EPPDefRegUpdateCmd.setRemove(EPPDefRegAddRemove)
}
