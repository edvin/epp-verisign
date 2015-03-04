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


// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP Host &lt;update&gt; command, which provides a transform
 * operation that allows a client to modify the attributes of a host object.
 * In addition to the standard EPP command elements, the &lt;update&gt;
 * command MUST contain a &lt;host:update&gt; element that identifies the host
 * namespace and the location of the host schema. The &lt;host:update&gt;
 * element SHALL contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains the fully qualified host name of
 * the object to be updated.  Use <code>getName</code> and
 * <code>setName</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;host:add&gt; element that contains attribute values to be
 * added to the host object.  Use <code>getAdd</code> and <code>setAdd</code>
 * to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;host:rem&gt; element that contains attribute values to be
 * removed from the host object.  Use <code>getRemove</code> and
 * <code>setRemove</code>     to get and set the element.
 * </li>
 * <li>
 * An OPTIONAL &lt;host:chg&gt; element that contains attribute values to be
 * changed from the host object.  Use <code>getChange</code> and
 * <code>setChange</code>     to get and set the element.
 * </li>
 * </ul>
 * 
 * <br>     Host name changes MAY require the addition or removal of IP
 * addresses to be accepted by the server.  If a new host name exists in a TLD
 * for which the server is not authoritative, then the host object MUST NOT
 * have any associated IP addresses.  If a new host name exists in a TLD for
 * which the server is authoritative, then the host object MAY have associated
 * IP addresses. Host name changes MAY have an impact on associated objects
 * that refer to the host object.  A host name change SHOULD not require
 * additional updates of associated objects to preserve existing associations. <br>
 * <br>
 * <code>EPPReponse</code> is the response associated     with
 * <code>EPPHostUpdateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.host.EPPHostAddRemove
 */
public class EPPHostUpdateCmd extends EPPUpdateCmd {
	/** XML Element Name of <code>EPPHostUpdateCmd</code> root element. */
	final static String ELM_NAME = "host:update";

	/** XML Element Name for the name attribute. */
	private final static String ELM_HOST_NAME = "host:name";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPHostUpdateCmd.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Host Name of host to update. */
	private String name = null;

	/** Attributes to add */
	private EPPHostAddRemove add = null;

	/** Attributes to remove */
	private EPPHostAddRemove remove = null;

	/** Attributes to change */
	private EPPHostAddRemove change = null;

	/**
	 * <code>EPPHostUpdateCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPHostUpdateCmd() {
	}

	// End EPPHostUpdateCmd.EPPHostUpdateCmd()

	/**
	 * <code>EPPHostUpdateCmd</code> constructor.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aName Host name to update.
	 */
	public EPPHostUpdateCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPHostUpdateCmd.EPPHostUpdateCmd(String, String)

	/**
	 * <code>EPPHostUpdateCmd</code> constructor that takes the required
	 * attributes as arguments.
	 *
	 * @param aTransId Transaction Id associated with the command.
	 * @param aName Host name to update.
	 * @param aAdd Attributes to add to the host.  <code>null</code> if no
	 * 		  additions.
	 * @param aRemove Attributes to remove from the host.  <code>null</code> if
	 * 		  no removals.
	 * @param aChange Attributes to change from the host.  <code>null</code> if
	 * 		  no changes.
	 */
	public EPPHostUpdateCmd(
							String aTransId, String aName, EPPHostAddRemove aAdd,
							EPPHostAddRemove aRemove, EPPHostAddRemove aChange) {
		super(aTransId);

		name     = aName;

		add = aAdd;

		if (add != null) {
			add.setMode(EPPHostAddRemove.MODE_ADD);
		}

		remove = aRemove;

		if (remove != null) {
			remove.setMode(EPPHostAddRemove.MODE_REMOVE);
		}

		change = aChange;

		if (change != null) {
			change.setMode(EPPHostAddRemove.MODE_CHANGE);
		}
	}

	// End EPPHostUpdateCmd.EPPHostUpdateCmd(String, String, EPPHostAddRemove, EPPHostAddRemove, EPPHostAddRemove)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPHostUpdateCmd</code>.
	 *
	 * @return <code>EPPHostMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPHostMapFactory.NS;
	}

	// End EPPHostUpdateCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostUpdateCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPHostUpdateCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostUpdateCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("required attribute name is not set");
		}

		Element root =
			aDocument.createElementNS(EPPHostMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:host", EPPHostMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPHostMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPHostMapFactory.NS,
							 ELM_HOST_NAME);

		// Add
		EPPUtil.encodeComp(aDocument, root, add);

		// Remove
		EPPUtil.encodeComp(aDocument, root, remove);

		// Change
		EPPUtil.encodeComp(aDocument, root, change);

		return root;
	}

	// End EPPHostUpdateCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPHostUpdateCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostUpdateCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Host Name
		name =
			EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_HOST_NAME);

		// Add
		add = (EPPHostAddRemove) EPPUtil.decodeComp(
													aElement,
													EPPHostMapFactory.NS,
													EPPHostAddRemove.ELM_ADD,
													EPPHostAddRemove.class);

		if (add != null) {
			add.setMode(EPPHostAddRemove.MODE_ADD);
		}

		// Remove
		remove =
			(EPPHostAddRemove) EPPUtil.decodeComp(
												  aElement, EPPHostMapFactory.NS,
												  EPPHostAddRemove.ELM_REMOVE,
												  EPPHostAddRemove.class);

		if (remove != null) {
			remove.setMode(EPPHostAddRemove.MODE_REMOVE);
		}

		// Change
		change =
			(EPPHostAddRemove) EPPUtil.decodeComp(
												  aElement, EPPHostMapFactory.NS,
												  EPPHostAddRemove.ELM_CHANGE,
												  EPPHostAddRemove.class);

		if (change != null) {
			change.setMode(EPPHostAddRemove.MODE_CHANGE);
		}
	}

	// End EPPHostUpdateCmd.doDecode(Node)

	/**
	 * Compare an instance of <code>EPPHostUpdateCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostUpdateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPHostUpdateCmd theComp = (EPPHostUpdateCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			cat.error("Name " + name + " != " + theComp.name);

			return false;
		}

		// Add
		if (!((add == null) ? (theComp.add == null) : add.equals(theComp.add))) {
			cat.error("Add " + add + " != " + theComp.add);

			return false;
		}

		// Remove
		if (
			!(
					(remove == null) ? (theComp.remove == null)
										 : remove.equals(theComp.remove)
				)) {
			cat.error("Remove " + remove + " != " + theComp.remove);

			return false;
		}

		// Change
		if (
			!(
					(change == null) ? (theComp.change == null)
										 : change.equals(theComp.change)
				)) {
			cat.error("Change " + change + " != " + theComp.change);

			return false;
		}

		return true;
	}

	// End EPPHostUpdateCmd.equals(Object)

	/**
	 * Clone <code>EPPHostUpdateCmd</code>.
	 *
	 * @return clone of <code>EPPHostUpdateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostUpdateCmd clone = (EPPHostUpdateCmd) super.clone();

		if (clone.add != null) {
			clone.add = (EPPHostAddRemove) add.clone();
		}

		if (clone.remove != null) {
			clone.remove = (EPPHostAddRemove) remove.clone();
		}

		if (clone.change != null) {
			clone.change = (EPPHostAddRemove) change.clone();
		}

		return clone;
	}

	// End EPPHostUpdateCmd.clone()

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

	// End EPPHostUpdateCmd.toString()

	/**
	 * Gets the host name to update.
	 *
	 * @return Host Name    if defined; <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPHostUpdateCmd.getName()

	/**
	 * Sets the host name to update.
	 *
	 * @param aName Host Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPHostUpdateCmd.setName(String)

	/**
	 * Gets the items to add to the host.
	 *
	 * @return Object that contains the list of elements to add to the host if
	 * 		   defined; <code>null</code> otherwise.
	 */
	public EPPHostAddRemove getAdd() {
		return add;
	}

	// End EPPHostUpdateCmd.getAdd()

	/**
	 * Sets the items to add to the host.
	 *
	 * @param aAdd Object that contains the list of elements to add to the
	 * 		  host.
	 */
	public void setAdd(EPPHostAddRemove aAdd) {
		add = aAdd;
		add.setMode(EPPHostAddRemove.MODE_ADD);
	}

	// End EPPHostUpdateCmd.setAdd(EPPHostAddRemove)

	/**
	 * Gets the items to remove from the host.
	 *
	 * @return Object that contains the list of elements to remove from the
	 * 		   host if defined; <code>null</code> otherwise.
	 */
	public EPPHostAddRemove getRemove() {
		return remove;
	}

	// End EPPHostUpdateCmd.getRemove()

	/**
	 * Sets the items to remove from the host.
	 *
	 * @param aRemove Object that contains the list of elements to remove from
	 * 		  the host.
	 */
	public void setRemove(EPPHostAddRemove aRemove) {
		remove = aRemove;
		remove.setMode(EPPHostAddRemove.MODE_REMOVE);
	}

	// End EPPHostUpdateCmd.setRemove(EPPHostAddRemove)

	/**
	 * Gets the items to change to the host.
	 *
	 * @return Object that contains the list of elements to change to the host
	 * 		   if defined; <code>null</code> otherwise.
	 */
	public EPPHostAddRemove getChange() {
		return change;
	}

	// End EPPHostUpdateCmd.getChange()

	/**
	 * Sets the items to add to the host.
	 *
	 * @param newChange Object that contains the list of elements to change to
	 * 		  the host.
	 */
	public void setChange(EPPHostAddRemove newChange) {
		change = newChange;

		if (change != null) {
			change.setMode(EPPHostAddRemove.MODE_CHANGE);
		}
	}

	// End EPPHostUpdateCmd.setAdd(EPPHostAddRemove)
}
