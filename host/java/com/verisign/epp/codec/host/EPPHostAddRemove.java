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
 * Represents attributes to add, remove or change with a
 * <code>EPPHostUpdateCmd</code>.     In <code>EPPHostUpdateCmd</code>, an
 * instance of <code>EPPHostAddRemove</code> is used     to specify the
 * attributes to add, an instance of <code>EPPHostAddRemove</code> is used
 * to specify the attributes to remove and instance of
 * <code>EPPHostAddRemove</code> is used     to specify the attributes to change<br>
 * <br>
 * The &lt;host:add&gt; and &lt;host:rem&gt; elements SHALL contain the
 * following child elements: <br>
 * 
 * <ul>
 * <li>
 * One or more &lt;host:address&gt; elements     that contains the IP addresses
 * to be associated with or removed from     the host.  IP address
 * restrictions explained in the &lt;create&gt; command     mapping apply here
 * as well.
 * </li>
 * <li>
 * One or more &lt;host:status&gt; elements that contain status values to be
 * associated with or removed from the object.  When specifying a value to be
 * removed, only the attribute value is significant; element text is not
 * required to match a value for removal.
 * </li>
 * </ul>
 * 
 * <br> The &lt;host:chg&gt; element SHALL contain the following child
 * elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;host:name&gt; element that contains a new fully qualified host name by
 * which the host object will be known.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.host.EPPHostUpdateCmd
 * @see com.verisign.epp.codec.host.EPPHostAddress
 */
public class EPPHostAddRemove implements EPPCodecComponent {
	/** mode of <code>EPPHostAddRemove</code> is not specified. */
	final static short MODE_NONE = 0;

	/** mode of <code>EPPHostAddRemove</code> is to add attributes. */
	final static short MODE_ADD = 1;

	/** mode of <code>EPPHostAddRemove</code> is to remove attributes. */
	final static short MODE_REMOVE = 2;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPHostAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_ADD = "host:add";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPHostAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_REMOVE = "host:rem";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPHostAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_CHANGE = "host:chg";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPHostAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_NAME = "host:name";

	/** mode of <code>EPPHostAddRemove</code> is to change attributes. */
	final static short MODE_CHANGE = 3;

	/** name to change. */
	private String name = null;

	/** statuses to add or remove. */
	private Vector statuses = null;

	/**
	 * Mode of EPPHostAddRemove.  Must be <code>MODE_ADD</code> or
	 * <code>MODE_REMOVE</code> to be valid.  This     attribute will be set
	 * by the parent container <code>EPPCodecComponent</code>.  For example,
	 * <code>EPPHostUpdateCmd</code> will set the mode for its
	 * <code>EPPHostAddRemove</code> instances.
	 */
	private short mode = MODE_NONE;

	/** Addresses to add or remove. */
	private Vector addresses = null;

	/**
	 * Default constructor for <code>EPPHostAddRemove</code>.  The addresses
	 * attribute     defaults to <code>null</code>.
	 */
	public EPPHostAddRemove() {
		addresses     = null;
		statuses	  = null;
		name		  = null;
	}
	 // End EPPHostAddRemove()

	/**
	 * Constructor for <code>EPPHostAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aAddresses Vector of <code>EPPHostAddress</code> instances.  Is
	 * 		  <code>null</code> or empty for no modifications.
	 */
	public EPPHostAddRemove(Vector aAddresses) {
		setAddresses(aAddresses);
		statuses     = null;
		name		 = null;
	}
	 // EPPHostAddRemove(Vectors)

	/**
	 * Constructor for <code>EPPHostAddRemove</code>.  The addresses attribute
	 * defaults to <code>null</code>.
	 *
	 * @param aName DOCUMENT ME!
	 */
	public EPPHostAddRemove(String aName) {
		addresses     = null;
		statuses	  = null;
		name		  = aName;
	}
	 // End EPPHostAddRemove()

	/**
	 * Constructor for <code>EPPHostAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aAddresses Vector of <code>EPPHostAddress</code> instances.  Is
	 * 		  <code>null</code> or empty for no modifications.
	 * @param aStatuses Vector of <code>EPPHostStatus</code> instances. Is
	 * 		  <code>null</code> or empty for no modifications.
	 */
	public EPPHostAddRemove(Vector aAddresses, Vector aStatuses) {
		setAddresses(aAddresses);
		setStatuses(aStatuses);
		name = null;
	}
	 // EPPHostAddRemove(Vectors)

	/**
	 * Gets the addresses to add or remove.
	 *
	 * @return Vector <code>EPPHostAddress</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getAddresses() {
		return addresses;
	}
	 // End EPPHostAddRemove.getAddresses()

	/**
	 * Sets the addresses to add or remove.
	 *
	 * @param aAddresses Vector of <code>EPPHostAddress</code> instances.
	 */
	public void setAddresses(Vector aAddresses) {
		addresses = aAddresses;

		if ((addresses != null) && (addresses.size() == 0)) {
			addresses = null;
		}
	}
	 // End EPPHostAddRemove.setAddresses(Vector)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPHostAddRemove</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPHostAddRemove</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPHostAddRemove</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root;

		// Add or Remove node
		if (mode == MODE_ADD) {
			root = aDocument.createElementNS(EPPHostMapFactory.NS, ELM_ADD);
		}
		else if (mode == MODE_REMOVE) {
			root = aDocument.createElementNS(EPPHostMapFactory.NS, ELM_REMOVE);
		}
		else if (mode == MODE_CHANGE) {
			root = aDocument.createElementNS(EPPHostMapFactory.NS, ELM_CHANGE);
		}
		else
		{
			throw new EPPEncodeException("Invalid EPPHostAddRemove mode of "
										 + mode);
		}

		if (mode == MODE_CHANGE) {
			// name
			EPPUtil.encodeString(
								 aDocument, root, name, EPPHostMapFactory.NS,
								 ELM_NAME);
		}
		else {
			// Addresses
			EPPUtil.encodeCompVector(aDocument, root, addresses);

			// Statuses
			EPPUtil.encodeCompVector(aDocument, root, statuses);
		}

		return root;
	}
	 // End EPPHostAddRemove.encode(Document)

	/**
	 * Decode the <code>EPPHostAddRemove</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPHostAddRemove</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		if (aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_ADD))) {
			mode = MODE_ADD;
		}
		else if (aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_REMOVE))) {
			mode = MODE_REMOVE;
		}
		else if (aElement.getLocalName().equals(EPPUtil.getLocalName(ELM_CHANGE))) {
			mode = MODE_CHANGE;
		}
		else
		{
			throw new EPPDecodeException("Invalid EPPHostAddRemove mode of "
										 + aElement.getLocalName());
		}

		if (mode == MODE_CHANGE) {
			//name
			name =
				EPPUtil.decodeString(aElement, EPPHostMapFactory.NS, ELM_NAME);
		}
		else {
			// Addresses
			addresses =
				EPPUtil.decodeCompVector(
										 aElement, EPPHostMapFactory.NS,
										 EPPHostAddress.ELM_NAME,
										 EPPHostAddress.class);

			if (addresses.size() == 0) {
				addresses = null;
			}

			// statuses
			statuses =
				EPPUtil.decodeCompVector(
										 aElement, EPPHostMapFactory.NS,
										 EPPHostStatus.ELM_NAME,
										 EPPHostStatus.class);

			if (statuses.size() == 0) {
				statuses = null;
			}
		}
	}
	 // End EPPHostAddRemove.decode(Element)

	/**
	 * implements a deep <code>EPPHostAddRemove</code> compare.
	 *
	 * @param aObject <code>EPPHostAddRemove</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPHostAddRemove)) {
			return false;
		}

		EPPHostAddRemove theComp = (EPPHostAddRemove) aObject;

		// Mode
		if (mode != theComp.mode) {
			return false;
		}

		// Addresses
		if (!EPPUtil.equalVectors(addresses, theComp.addresses)) {
			return false;
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
			return false;
		}

		// name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		return true;
	}
	 // End EPPHostAddRemove.equals(Object)

	/**
	 * Clone <code>EPPHostAddRemove</code>.
	 *
	 * @return clone of <code>EPPHostAddRemove</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPHostAddRemove clone = null;

		clone = (EPPHostAddRemove) super.clone();

		// Addresses
		if (addresses != null) {
			clone.addresses = (Vector) addresses.clone();

			for (int i = 0; i < addresses.size(); i++)
				clone.addresses.setElementAt(
											 ((EPPHostAddress) addresses
											  .elementAt(i)).clone(), i);
		}

		// Statuses
		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();

			for (int i = 0; i < statuses.size(); i++)
				clone.statuses.setElementAt(
											((EPPHostStatus) statuses.elementAt(i))
											.clone(), i);
		}

		return clone;
	}
	 // End EPPHostAddRemove.clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in
	 * an indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}
	 // End EPPHostAddRemove.toString()

	/**
	 * Gets the mode of <code>EPPHostAddRemove</code>.  There are two     valid
	 * modes <code>EPPHostAddRemove.MODE_ADD</code> and
	 * <code>EPPHostAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPHostAddRemove.MODE_NONE</code>.
	 *
	 * @return One of the <code>EPPHostAddRemove_MODE</code> constants.
	 */
	short getMode() {
		return mode;
	}
	 // End EPPHostAddRemove.getMode()

	/**
	 * Sets the mode of <code>EPPHostAddRemove</code>.  There are two     valid
	 * modes <code>EPPHostAddRemove.MODE_ADD</code> and
	 * <code>EPPHostAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPHostAddRemove.MODE_NONE</code>
	 *
	 * @param aMode <code>EPPHostAddRemove.MODE_ADD</code> or
	 * 		  <code>EPPHostAddRemove.MODE_REMOVE</code>.
	 */
	void setMode(short aMode) {
		mode = aMode;
	}
	 // End EPPHostAddRemove.setMode(short)

	/**
	 * Gets name.
	 *
	 * @return String  Name
	 */
	public String getName() {
		return name;
	}
	 // End EPPHostAddRemove.getName()

	/**
	 * Gets statuses.
	 *
	 * @return <code>Vector</code> of <code>EPPHostStatus</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getStatuses() {
		return statuses;
	}
	 // End EPPHostAddRemove.getStatuses()

	/**
	 * Sets name.
	 *
	 * @param aName String
	 */
	public void setName(String aName) {
		name = aName;
	}
	 // End EPPHostAddRemove.setName(String)

	/**
	 * Sets statuses.
	 *
	 * @param aStatuses <code>Vector</code> of  <code>EPPHostStatus</code>
	 * 		  instances
	 */
	public void setStatuses(Vector aStatuses) {
		statuses = aStatuses;

		if ((statuses != null) && (statuses.size() == 0)) {
			statuses = null;
		}
	}
	 // End EPPHostAddRemove.setStatuses(Vector)
}
