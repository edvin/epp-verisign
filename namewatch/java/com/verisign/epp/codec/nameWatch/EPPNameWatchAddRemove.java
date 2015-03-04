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
 * Represents attributes to change with a <code>EPPNameWatchUpdateCmd</code>.
 * In <code>EPPNameWatchUpdateCmd</code>, an instance of
 * <code>EPPNameWatchAddRemove</code> is used     to specify the attributes to change<br>
 * <br>
 * The NameWatch Mapping Specification describes the following attributes:<br>
 * 
 * <ul>
 * <li>
 * Zero or more &lt;nameWatch:registrant&gt; element that contains the
 * identifier for the human or organizational social information (contact)
 * object to be associated with the nameWatch object as the object registrant.
 * This object identifier MUST be known to the server before the contact
 * object can be associated with the nameWatch object. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get and set
 * the element.
 * </li>
 * <li>
 * Zero or more &lt;nameWatch:rptTo&gt; elements that contain the email address
 * and frequency type to be associated with the nameWatch. Use
 * <code>getRptTo</code> and <code>setRptTo</code> to get and set the element.
 * </li>
 * <li>
 * One or two &lt;nameWatch:AuthInfo&gt; element that contains authorization
 * information for the nameWatch object. Use <code>getAuthInfo</code> and
 * <code>setAuthInfo</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the maximum number of nameWatch attribute
 * elements is subject to the number of values currently associated with the
 * nameWatch object.  <code>EPPNameWatchAddRemove</code> will delegate the
 * validation of     the cardinality of the nameWatch attributes elements to
 * the EPP Server.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchUpdateCmd
 */
public class EPPNameWatchAddRemove implements EPPCodecComponent {
	/** mode of <code>EPPNameWatchAddRemove</code> is not specified. */
	final static short MODE_NONE = 0;

	/** mode of <code>EPPNameWatchAddRemove</code> is to add attributes. */
	final static short MODE_ADD = 1;

	/** mode of <code>EPPNameWatchAddRemove</code> is to remove attributes. */
	final static short MODE_REMOVE = 2;

	/** mode of <code>EPPNameWatchAddRemove</code> is to change attributes. */
	final static short MODE_CHANGE = 3;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPNameWatchAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_ADD = "nameWatch:add";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPNameWatchAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_REMOVE = "nameWatch:rem";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPNameWatchAddRemove.MODE_CHANGE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_CHANGE = "nameWatch:chg";

	/** XML tag name for the <code>registrant</code> attribute. */
	private final static String ELM_REGISTRANT = "nameWatch:registrant";

	/** XML tag name for the <code>rptTo</code> attribute. */
	private final static String ELM_RPTTO = "nameWatch:rptTo";

	/** XML tag name for the <code>statuses</code> attribute. */
	private final static String ELM_STATUS = "nameWatch:status";

	/**
	 * Mode of EPPNameWatchAddRemove.  Must be <code>MODE_ADD</code> or
	 * <code>MODE_REMOVE</code> to be valid.  This     attribute will be set
	 * by the parent container <code>EPPCodecComponent</code>.  For example,
	 * <code>EPPDomainUpdateCmd</code> will set the mode for its
	 * <code>EPPNameWatchAddRemove</code> instances.
	 */
	private short mode = MODE_NONE;

	/** Status to add or remove. */
	private Vector statuses = null;

	/** registrant to change. */
	private String registrant = null;

	/** rptTo to change. */
	private EPPNameWatchRptTo rptTo = null;

	/** authorization information to change. */
	private EPPAuthInfo authInfo = null;

	/**
	 * Default constructor for <code>EPPNameWatchAddRemove</code>.  All of the
	 * attribute     default to <code>null</code> to indicate no modification.
	 */
	public EPPNameWatchAddRemove() {
		registrant     = null;
		authInfo	   = null;
		rptTo		   = null;
		statuses	   = null;
	}

	// End EPPNameWatchAddRemove()

	/**
	 * Constructor for <code>EPPNameWatchAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aStatuses <code>Vector</code> statuses
	 */
	public EPPNameWatchAddRemove(Vector aStatuses) {
		statuses = aStatuses;
	}

	// End EPPNameWatchAddRemove(Vector)

	/**
	 * Constructor for <code>EPPNameWatchAddRemove</code> that includes the
	 * attributes as arguments.
	 *
	 * @param aRegistrant <code>String</code>            registrant
	 * @param aRptTo <code>EPPNameWatchRptTo</code>  reply to
	 * @param aAuthInfo <code>EPPAuthInfo</code>    authorization information
	 */
	public EPPNameWatchAddRemove(
								 String aRegistrant, EPPNameWatchRptTo aRptTo,
								 EPPAuthInfo aAuthInfo) {
		registrant     = aRegistrant;
		rptTo		   = aRptTo;
		setAuthInfo(aAuthInfo);
	}

	// End EPPNameWatchAddRemove(String, EPPNameWatchRptTo, EPPAuthInfo)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchAddRemove</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchAddRemove</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchAddRemove</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = null;

		// Change mode
		if (mode == MODE_CHANGE) {
			root =
				aDocument.createElementNS(
										  EPPNameWatchMapFactory.NS, ELM_CHANGE);

			// Registrant
			if (registrant != null) {
				EPPUtil.encodeString(
									 aDocument, root, registrant,
									 EPPNameWatchMapFactory.NS, ELM_REGISTRANT);
			}

			// rptTo
			if ((rptTo != null) && !rptTo.isRptToUnspec()) {
				EPPUtil.encodeComp(aDocument, root, rptTo);
			}

			// authInfo
			if (authInfo != null) {
				EPPUtil.encodeComp(aDocument, root, authInfo);
			}

			return root;
		}

		// Add or Remove mode
		if (mode == MODE_ADD) {
			root =
				aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_ADD);
		}
		else if (mode == MODE_REMOVE) {
			root =
				aDocument.createElementNS(
										  EPPNameWatchMapFactory.NS, ELM_REMOVE);
		}
		else {
			throw new EPPEncodeException("Invalid EPPNameWatchAddRemove mode of "
										 + mode);
		}

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, statuses);

		return root;
	}

	// End EPPNameWatchAddRemove.encode(Document)

	/**
	 * Decode the <code>EPPNameWatchAddRemove</code> attributes from the
	 * aElement DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchAddRemove</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement.
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
		else {
			throw new EPPDecodeException("Invalid EPPNameWatchAddRemove mode of "
										 + aElement.getLocalName());
		}

		// Change Mode
		if (mode == MODE_CHANGE) {
			// Registrant
			registrant =
				EPPUtil.decodeString(
									 aElement, EPPNameWatchMapFactory.NS,
									 ELM_REGISTRANT);

			// rptTo
			rptTo =
				(EPPNameWatchRptTo) EPPUtil.decodeComp(
													   aElement,
													   EPPNameWatchMapFactory.NS,
													   EPPNameWatchRptTo.ELM_NAME,
													   EPPNameWatchRptTo.class);

			// AuthInfo
			authInfo =
				(EPPAuthInfo) EPPUtil.decodeComp(
												 aElement,
												 EPPNameWatchMapFactory.NS,
												 EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO,
												 EPPAuthInfo.class);
		}
		else { // Add & Remove Mode

			// Statuses
			statuses =
				EPPUtil.decodeCompVector(
										 aElement, EPPNameWatchMapFactory.NS,
										 ELM_STATUS, EPPNameWatchStatus.class);
		}
	}

	// End EPPNameWatchAddRemove.decode(Element)

	/**
	 * implements a deep <code>EPPNameWatchAddRemove</code> compare.
	 *
	 * @param aObject <code>EPPNameWatchAddRemove</code> instance to compare
	 * 		  with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchAddRemove)) {
			return false;
		}

		EPPNameWatchAddRemove theComp = (EPPNameWatchAddRemove) aObject;

		// Mode
		if (mode != theComp.mode) {
			return false;
		}

		// Statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
			return false;
		}

		// Registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
				)) {
			return false;
		}

		// rptTo
		if (
			!(
					(rptTo == null) ? (theComp.rptTo == null)
										: rptTo.equals(theComp.rptTo)
				)) {
			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			return false;
		}

		return true;
	}

	// End EPPNameWatchAddRemove.equals(Object)

	/**
	 * Clone <code>EPPNameWatchAddRemove</code>.
	 *
	 * @return clone of <code>EPPNameWatchAddRemove</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchAddRemove clone = null;

		clone = (EPPNameWatchAddRemove) super.clone();

		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();
		}

		if (registrant != null) {
			clone.registrant = registrant;
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (rptTo != null) {
			clone.rptTo = (EPPNameWatchRptTo) rptTo.clone();
		}

		return clone;
	}

	// End EPPNameWatchAddRemove.clone()

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

	// End EPPNameWatchAddRemove.toString()

	/**
	 * Get authorization information
	 *
	 * @return Instance of <code>EPPAuthInfo</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPNameWatchAddRemove.getAuthInfo()

	/**
	 * Get registrant
	 *
	 * @return registrant if defined; <code>null</code> otherwise.
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPNameWatchAddRemove.getRegistrant()

	/**
	 * Gets the rptTo
	 *
	 * @return Instance of <code>EPPNameWatchRptTo</code> if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public EPPNameWatchRptTo getRptTo() {
		return rptTo;
	}

	// End EPPNameWatchAddRemove.getRptTo()

	/**
	 * Gets the statuses to add or remove.  The
	 * <code>EPPNameWatchStatus.STATUS_</code>     constants can be used for
	 * the statuses.
	 *
	 * @return Vector of status <code>String</code> instances.
	 */
	public Vector getStatuses() {
		return statuses;
	}

	// End EPPDomainAddRemove.getStatuses()

	/**
	 * Sets the statuses to add or remove.  The
	 * <code>EPPNameWatchStatus.STATUS_</code>     constants can be used for
	 * the statuses.
	 *
	 * @param aStatuses Vector of status <code>String</code> instances.
	 */
	public void setStatuses(Vector aStatuses) {
		statuses = aStatuses;
	}

	// End EPPNameWatchAddRemove.setStatuses(Vector)

	/**
	 * Sets the rptTo
	 *
	 * @param aRptTo EPPNameWatchRptTo
	 */
	public void setRptTo(EPPNameWatchRptTo aRptTo) {
		if (aRptTo != null) {
			rptTo = aRptTo;
		}
	}

	// End EPPNameWatchAddRemove.setRptTo(EPPNameWatchRptTo)

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo EPPAuthInfo
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
		}
	}

	// End EPPNameWatchAddRemove.setAuthInfo(EPPAuthInfo)

	/**
	 * Set registrant
	 *
	 * @param newRegistrant registrant
	 */
	public void setRegistrant(String newRegistrant) {
		registrant = newRegistrant;
	}

	// End EPPNameWatchAddRemove.setRegistrant(String)

	/**
	 * Gets the mode of <code>EPPNameWatchAddRemove</code>.  There are two
	 * valid modes <code>EPPNameWatchAddRemove.MODE_ADD</code> and
	 * <code>EPPNameWatchAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPNameWatchAddRemove.MODE_NONE</code>.
	 *
	 * @return One of the <code>EPPNameWatchAddRemove_MODE</code> constants.
	 */
	short getMode() {
		return mode;
	}

	// End EPPNameWatchAddRemove.getMode()

	/**
	 * Sets the mode of <code>EPPNameWatchAddRemove</code>.  There are two
	 * valid modes <code>EPPNameWatchAddRemove.MODE_ADD</code> and
	 * <code>EPPNameWatchAddRemove.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPNameWatchAddRemove.MODE_NONE</code>
	 *
	 * @param aMode <code>EPPNameWatchAddRemove.MODE_ADD</code> or
	 * 		  <code>EPPNameWatchAddRemove.MODE_REMOVE</code>.
	 */
	void setMode(short aMode) {
		mode = aMode;
	}

	// End EPPNameWatchAddRemove.setMode(short)
}
