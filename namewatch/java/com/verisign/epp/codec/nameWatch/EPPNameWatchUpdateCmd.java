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
package com.verisign.epp.codec.nameWatch;

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
 * Represents an EPP NameWatch &lt;update&gt; command. The EPP &lt;update&gt;
 * command provides a transform operation that allows a client to modify the
 * attributes of a nameWatch object.  In addition to the standard EPP command
 * elements, the &lt;update&gt; command MUST contain a
 * &lt;nameWatch:update&gt; element that identifies the nameWatch namespace
 * and the location of the nameWatch schema. In addition to  The
 * &lt;nameWatch:update&gt; element SHALL contain the following child
 * elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the fully qualified nameWatch
 * roid of the object to be updated.  Use <code>getRoid</code> and
 * <code>setRoid</code>     to get and set the element.
 * </li>
 * <li>
 * A &lt;nameWatch:chg&gt; element that contains attribute values to be change
 * to the nameWatch object.  Use <code>getChange</code> and
 * <code>setChange</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPReponse</code> is the response associated     with
 * <code>EPPNameWatchUpdateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchAddRemove
 */
public class EPPNameWatchUpdateCmd extends EPPUpdateCmd {
	/** XML Element Name of <code>EPPNameWatchUpdateCmd</code> root element. */
	final static String ELM_NAME = "nameWatch:update";

	/**
	 * XML Element Name of a nameWatch name in a
	 * <code>EPPNameWatchUpdateCmd</code>.
	 */
	private final static String ELM_ROID = "nameWatch:roid";

	/** NameWatch Roid of nameWatch to update. */
	private String roid = null;

	/** Attributes to add */
	private EPPNameWatchAddRemove add = null;

	/** Attributes to remove */
	private EPPNameWatchAddRemove remove = null;

	/** Attributes to change */
	private EPPNameWatchAddRemove change = null;

	/**
	 * <code>EPPNameWatchUpdateCmd</code> default constructor.  The roid is
	 * initialized to <code>null</code>.     The roid and change attributes
	 * must be set before invoking <code>encode</code>.
	 */
	public EPPNameWatchUpdateCmd() {
	}

	// End EPPNameWatchUpdateCmd.EPPNameWatchUpdateCmd()

	/**
	 * Creates a new EPPNameWatchUpdateCmd object.
	 *
	 * @param aTransId DOCUMENT ME!
	 * @param aRoid DOCUMENT ME!
	 */
	public EPPNameWatchUpdateCmd(String aTransId, String aRoid) {
		super(aTransId);

		roid     = aRoid;

		change = null;
	}

	// End EPPNameWatchUpdateCmd.EPPNameWatchUpdateCmd()

	/**
	 * <code>EPPNameWatchUpdateCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aRoid NameWatch name to update.
	 * @param aAdd Attributes to add to the nameWatch.  <code>null</code> if no
	 * 		  additions.
	 * @param aRemove Attributes to remove to the nameWatch.<code>null</code>
	 * 		  if no additions.
	 * @param aChange Attributes to change to the nameWatch.  <code>null</code>
	 * 		  if no additions.
	 */
	public EPPNameWatchUpdateCmd(
								 String aTransId, String aRoid,
								 EPPNameWatchAddRemove aAdd,
								 EPPNameWatchAddRemove aRemove,
								 EPPNameWatchAddRemove aChange) {
		super(aTransId);

		roid     = aRoid;

		add = aAdd;

		add.setMode(EPPNameWatchAddRemove.MODE_ADD);

		remove = aRemove;

		remove.setMode(EPPNameWatchAddRemove.MODE_REMOVE);

		change = aChange;

		change.setMode(EPPNameWatchAddRemove.MODE_CHANGE);
	}

	// End EPPNameWatchUpdateCmd.EPPNameWatchUpdateCmd(String, String, EPPNameWatchAddRemove)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchUpdateCmd</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchUpdateCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchUpdateCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchUpdateCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchUpdateCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		//Validate States
		if (roid == null) {
			throw new EPPEncodeException("roid required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);

		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// Roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Add
		EPPUtil.encodeComp(aDocument, root, add);

		// Remove
		EPPUtil.encodeComp(aDocument, root, remove);

		// Change
		EPPUtil.encodeComp(aDocument, root, change);

		return root;
	}

	// End EPPNameWatchUpdateCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchUpdateCmd</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchUpdateCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		// Add
		add = (EPPNameWatchAddRemove) EPPUtil.decodeComp(
														 aElement,
														 EPPNameWatchMapFactory.NS,
														 EPPNameWatchAddRemove.ELM_ADD,
														 EPPNameWatchAddRemove.class);

		if (add != null) {
			add.setMode(EPPNameWatchAddRemove.MODE_ADD);
		}

		// Remove
		remove =
			(EPPNameWatchAddRemove) EPPUtil.decodeComp(
													   aElement,
													   EPPNameWatchMapFactory.NS,
													   EPPNameWatchAddRemove.ELM_REMOVE,
													   EPPNameWatchAddRemove.class);

		if (remove != null) {
			remove.setMode(EPPNameWatchAddRemove.MODE_REMOVE);
		}

		// Change
		change =
			(EPPNameWatchAddRemove) EPPUtil.decodeComp(
													   aElement,
													   EPPNameWatchMapFactory.NS,
													   EPPNameWatchAddRemove.ELM_CHANGE,
													   EPPNameWatchAddRemove.class);

		if (change != null) {
			change.setMode(EPPNameWatchAddRemove.MODE_CHANGE);
		}

		// Validate Attributes
		if (roid == null) {
			throw new EPPDecodeException("roid required attribute is not set");
		}
	}

	// End EPPNameWatchUpdateCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPNameWatchUpdateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchUpdateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchUpdateCmd theComp = (EPPNameWatchUpdateCmd) aObject;

		// roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
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

	// End EPPNameWatchUpdateCmd.equals(Object)

	/**
	 * Clone <code>EPPNameWatchUpdateCmd</code>.
	 *
	 * @return clone of <code>EPPNameWatchUpdateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchUpdateCmd clone = (EPPNameWatchUpdateCmd) super.clone();

		if (clone.add != null) {
			clone.add = (EPPNameWatchAddRemove) add.clone();
		}

		if (clone.remove != null) {
			clone.remove = (EPPNameWatchAddRemove) remove.clone();
		}

		if (clone.change != null) {
			clone.change = (EPPNameWatchAddRemove) change.clone();
		}

		return clone;
	}

	// End EPPNameWatchRenewCmd.clone()

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

	// End EPPNameWatchRenewCmd.toString()

	/**
	 * Gets the nameWatch name to update.
	 *
	 * @return NameWatch Roid    if defined; <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchUpdateCmd.getRoid()

	/**
	 * Sets the nameWatch roid to update.
	 *
	 * @param aRoid NameWatch Roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPNameWatchUpdateCmd.setRoid(String)

	/**
	 * Gets the items to change to the nameWatch.
	 *
	 * @return Object that contains the list of elements to change to the
	 * 		   nameWatch if defined; <code>null</code> otherwise.
	 */
	public EPPNameWatchAddRemove getChange() {
		return change;
	}

	// End EPPNameWatchUpdateCmd.getChange()

	/**
	 * Sets the items to change to the nameWatch.
	 *
	 * @param newChange Object that contains the list of elements to change to
	 * 		  the nameWatch.
	 */
	public void setChange(EPPNameWatchAddRemove newChange) {
		change = newChange;

		if (change != null) {
			change.setMode(EPPNameWatchAddRemove.MODE_CHANGE);
		}
	}

	// End EPPNameWatchUpdateCmd.setChange(EPPNameWatchAddRemove)

	/**
	 * Gets the items to add to the nameWatch.
	 *
	 * @return Object that contains the list of elements to add to the
	 * 		   nameWatch if defined; <code>null</code> otherwise.
	 */
	public EPPNameWatchAddRemove getAdd() {
		return add;
	}

	// End EPPNameWatchUpdateCmd.getAdd()

	/**
	 * Sets the items to add to the nameWatch.
	 *
	 * @param aAdd Object that contains the list of elements to add to the
	 * 		  nameWatch.
	 */
	public void setAdd(EPPNameWatchAddRemove aAdd) {
		add = aAdd;

		if (add != null) {
			add.setMode(EPPNameWatchAddRemove.MODE_ADD);
		}
	}

	// End EPPNameWatchUpdateCmd.setAdd(EPPNameWatchAddRemove)

	/**
	 * Gets the items to remove from the nameWatch.
	 *
	 * @return Object that contains the list of elements to remove from the
	 * 		   nameWatch if defined; <code>null</code> otherwise.
	 */
	public EPPNameWatchAddRemove getRemove() {
		return remove;
	}

	// End EPPNameWatchUpdateCmd.getRemove()

	/**
	 * Sets the items to remove from the nameWatch.
	 *
	 * @param aRemove Object that contains the list of elements to remove from
	 * 		  the nameWatch.
	 */
	public void setRemove(EPPNameWatchAddRemove aRemove) {
		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPNameWatchAddRemove.MODE_REMOVE);
		}
	}

	// End EPPNameWatchUpdateCmd.setRemove(EPPNameWatchAddRemove)
}
