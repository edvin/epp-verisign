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

import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents attributes to add, remove or change with a
 * <code>EPPContactUpdateCmd</code>. In <code>EPPContactUpdateCmd</code>,  an
 * instance of <code>EPPContactAddRemove</code> is used  to specify the
 * attributes to add, an instance of <code>EPPContactAddRemove</code> is used
 * to specify the attributes to remove and an instance of
 * <code>EPPContactAddRemove</code> is used  o specify the attributes to
 * change. <br>
 * <br>
 * <br>
 * The &lt;contact:add&gt; and &lt;contact:rem&gt; elements SHALL contain the
 * following child elements: <br>
 * 
 * <ul>
 * <li>
 * One or more &lt;contact:status&gt; elements that contain status values to be
 * associated with or removed from the object.  When specifying a value to be
 * removed, only the attribute value is significant; element text is not
 * required to match a value for removal.
 * </li>
 * </ul>
 * 
 * <br><br>
 * A &lt;contact:chg&gt; element SHALL contain the following OPTIONAL child
 * elements: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:postalInfo&gt; element that contains the postal contacts.  Use
 * <code>getPostalInfo</code>, <code>addPostalInfo</code> and
 * <code>setPostalInfo</code> to get, add and set the elements.
 * </li>
 * <li>
 * An &lt;contact:voice&gt; element that contains the contact's voice telephone
 * number. Use <code>getVoice</code> and <code>setVoice</code> to get and set
 * the elements.
 * </li>
 * <li>
 * An &lt;contact:fax&gt; element that contains the contact's facsimile
 * telephone number. Use <code>getFax</code> and <code>setFax</code> to get
 * and set the elements.
 * </li>
 * <li>
 * A &lt;contact:email&gt; element that contains the contact's e-mail address.
 * Use <code>getEmail</code> and <code>setEmail</code> to get and set the
 * elements.
 * </li>
 * </ul>
 * 
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactUpdateCmd
 * @see com.verisign.epp.codec.contact.EPPContactAddress
 */
public class EPPContactAddChange implements EPPCodecComponent {
	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	/** mode of <code>EPPContactAddRemove</code> is not specified. */
	final static short MODE_NONE = 0;

	/** mode of <code>EPPContactAddRemove</code> is to add attributes. */
	final static short MODE_ADD = 1;

	/** mode of <code>EPPContactAddRemove</code> is to remove attributes. */
	final static short MODE_REMOVE = 2;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_ADD</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_ADD = "contact:add";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_REMOVE = "contact:rem";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	final static String ELM_CHANGE = "contact:chg";

	/** mode of <code>EPPContactAddRemove</code> is to change attributes. */
	final static short MODE_CHANGE = 3;

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	private final static String ELM_CONTACT_POSTAL_INFO = "contact:postalInfo";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	private final static String ELM_CONTACT_AUTHINFO = "contact:authInfo";

	/** XML tag name for the <code>disclose</code> element. */
	private final static String ELM_CONTACT_DISCLOSE = "contact:disclose";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	private final static String ELM_CONTACT_EMAIL = "contact:email";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	private final static String ELM_CONTACT_FAX = "contact:fax";

	/**
	 * XML tag name when the <code>mode</code> attribute is
	 * <code>EPPContactAddRemove.MODE_REMOVE</code>.  This     is a package
	 * constant, so the container <code>EPPCodecComponent</code> can use it on
	 * a decode operation.
	 */
	private final static String ELM_CONTACT_VOICE = "contact:voice";

	/**
	 * XML Attribute Name for a phone extension, which applies to  fax and
	 * voice numbers.
	 */
	private final static String ATTR_EXT = "x";
	
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPContactAddChange.class.getName(),
						 EPPCatFactory.getInstance().getFactory());


	/** postal contacts */
	private java.util.Vector postalContacts = new Vector();

	/** authorization information for contact change */
	private com.verisign.epp.codec.gen.EPPAuthInfo authInfo = null;

	/** disclose information of contact */
	private com.verisign.epp.codec.contact.EPPContactDisclose disclose = null;

	/** email */
	private String email = null;

	/** fax number */
	private String fax = null;

	/** fax extension number for contact */
	private String faxExt = null;

	/** contact statuses */
	private java.util.Vector statuses = null;

	/** voice number */
	private String voice = null;

	/** voice extension number */
	private String voiceExt = null;

	/**
	 * Mode of EPPContactAddRemove.  Must be <code>MODE_ADD</code> or
	 * <code>MODE_REMOVE</code> to be valid.  This     attribute will be set
	 * by the parent container <code>EPPCodecComponent</code>.  For example,
	 * <code>EPPContactUpdateCmd</code> will set the mode for its
	 * <code>EPPContactAddRemove</code> instances.
	 */
	private short mode = MODE_NONE;

	/**
	 * Default constructor for <code>EPPContactAddChange</code>.
	 */
	public EPPContactAddChange() {
	}

	// End EPPContactAddChange()

	/**
	 * Constructor for <code>EPPContactAddChange</code>.
	 *
	 * @param newStatuses Vector of statuses for contact add.
	 */
	public EPPContactAddChange(Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPContactAddChange(Vector)

	/**
	 * Constructor for <code>EPPContactAddChange</code>.
	 *
	 * @param aPostalInfo EPPContactPostalDefinition, defining a series objects
	 * 		  for contact changes.
	 * @param aVoice Voice number
	 * @param aAuthInfo Authorization information for contact change.
	 */
	public EPPContactAddChange(
							   EPPContactPostalDefinition aPostalInfo,
							   String aVoice, EPPAuthInfo aAuthInfo) {
		voice     = aVoice;
		fax		  = null;
		email     = null;

		addPostalInfo(aPostalInfo);
		setAuthInfo(aAuthInfo);
	}

	// End EPPContactAddChange(EPPContactPostalDefinition, String, EPPAuthInfo)

	/**
	 * Constructor for <code>EPPContactAddChange</code>.
	 *
	 * @param aPostalInfo EPPContactPostalDefinition, defining a series objects
	 * 		  for contact changes.
	 * @param aVoice Voice number
	 * @param aFax Fax number
	 * @param aEmail Email
	 * @param aAuthInfo Authorization information for contact change.
	 */
	public EPPContactAddChange(
							   EPPContactPostalDefinition aPostalInfo,
							   String aVoice, String aFax, String aEmail,
							   EPPAuthInfo aAuthInfo) {
		voice     = aVoice;
		fax		  = aFax;
		email     = aEmail;

		addPostalInfo(aPostalInfo);
		setAuthInfo(aAuthInfo);
	}

	// End EPPContactAddChange(EPPContactPostalDefinition, String, String, String, EPPAuthInfo)

	/**
	 * Constructor for <code>EPPContactAddChange</code>.
	 *
	 * @param aPostalInfo Vector, defining a series objects for contact
	 * 		  changes.
	 * @param aVoice Voice number
	 * @param aFax Fax number
	 * @param aEmail Email
	 * @param aAuthInfo Authorization information for contact change.
	 */
	public EPPContactAddChange(
							   Vector aPostalInfo, String aVoice, String aFax,
							   String aEmail, EPPAuthInfo aAuthInfo) {
		voice     = aVoice;
		fax		  = aFax;
		email     = aEmail;

		setPostalInfo(aPostalInfo);
		setAuthInfo(aAuthInfo);
	}

	// End EPPContactAddChange(Vector, String, String, String, EPPAuthInfo)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactAddChange</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPContactAddChange</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactAddChange</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element currElm = null;
		Text    currVal = null;

		Element root;

		// Add or Remove node
		if (mode == MODE_ADD) {
			root = aDocument.createElementNS(EPPContactMapFactory.NS, ELM_ADD);
		}
		else if (mode == MODE_REMOVE) {
			root =
				aDocument.createElementNS(EPPContactMapFactory.NS, ELM_REMOVE);
		}
		else if (mode == MODE_CHANGE) {
			root =
				aDocument.createElementNS(EPPContactMapFactory.NS, ELM_CHANGE);
		}
		else {
			throw new EPPEncodeException("Invalid EPPContactAddRemove mode of "
										 + mode);
		}

		if (mode == MODE_CHANGE) {
			// postalInfo
			EPPUtil.encodeCompVector(aDocument, root, postalContacts);

			// voice
			if (voice != null) {
				currElm =
					aDocument.createElementNS(
											  EPPContactMapFactory.NS,
											  ELM_CONTACT_VOICE);
				currVal = aDocument.createTextNode(voice);

				// voiceExt
				if (voiceExt != null) {
					currElm.setAttribute(ATTR_EXT, voiceExt);
				}

				currElm.appendChild(currVal);
				root.appendChild(currElm);
			}

			// fax
			if (fax != null) {
				currElm =
					aDocument.createElementNS(
											  EPPContactMapFactory.NS,
											  ELM_CONTACT_FAX);
				currVal = aDocument.createTextNode(fax);

				// faxExt
				if (faxExt != null) {
					currElm.setAttribute(ATTR_EXT, faxExt);
				}

				currElm.appendChild(currVal);
				root.appendChild(currElm);
			}

			// email
			if (email != null) {
				EPPUtil.encodeString(
									 aDocument, root, email,
									 EPPContactMapFactory.NS, ELM_CONTACT_EMAIL);
			}

			// authInfo
			if (authInfo != null) {
				EPPUtil.encodeComp(aDocument, root, authInfo);
			}

			// disclose
			EPPUtil.encodeComp(aDocument, root, disclose);
		}
		else {
			// statuses
			if (statuses == null) {
				throw new EPPEncodeException("statuses required attribute is not set");
			}

			EPPUtil.encodeCompVector(aDocument, root, statuses);
		}

		return root;
	}

	// End EPPContactAddChange.encode(Document)

	/**
	 * Decode the <code>EPPContactAddChange</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPContactAddChange</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode <code>aElement</code>.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		Element currElm = null;

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
			throw new EPPDecodeException("Invalid EPPContactAddRemove mode of "
										 + aElement.getLocalName());
		}

		if (mode == MODE_CHANGE) {
			// postalInfo
			postalContacts =
				EPPUtil.decodeCompVector(
										 aElement, EPPContactMapFactory.NS,
										 ELM_CONTACT_POSTAL_INFO,
										 EPPContactPostalDefinition.class);

			// voice
			voice =
				EPPUtil.decodeString(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_VOICE);

			// voiceExt
			if (voice != null) {
				currElm =
					EPPUtil.getElementByTagNameNS(aElement, EPPContactMapFactory.NS, ELM_CONTACT_VOICE);
				voiceExt = currElm.getAttribute(ATTR_EXT);

				if (voiceExt.length() == 0) {
					voiceExt = null;
				}
			}
			else {
				voiceExt = null;
			}

			// fax
			fax = EPPUtil.decodeString(
									   aElement, EPPContactMapFactory.NS,
									   ELM_CONTACT_FAX);

			// faxExt
			if (fax != null) {
				currElm =
					EPPUtil.getElementByTagNameNS(aElement, EPPContactMapFactory.NS, ELM_CONTACT_FAX);
				faxExt = currElm.getAttribute(ATTR_EXT);

				if (faxExt.length() == 0) {
					faxExt = null;
				}
			}
			else {
				faxExt = null;
			}

			// email
			email =
				EPPUtil.decodeString(
									 aElement, EPPContactMapFactory.NS,
									 ELM_CONTACT_EMAIL);

			// authInfo
			authInfo =
				(EPPAuthInfo) EPPUtil.decodeComp(
												 aElement,
												 EPPContactMapFactory.NS,
												 ELM_CONTACT_AUTHINFO,
												 EPPAuthInfo.class);

			// disclose
			disclose =
				(EPPContactDisclose) EPPUtil.decodeComp(
														aElement,
														EPPContactMapFactory.NS,
														ELM_CONTACT_DISCLOSE,
														EPPContactDisclose.class);
		}
		else {
			// statuses
			statuses =
				EPPUtil.decodeCompVector(
										 aElement, EPPContactMapFactory.NS,
										 EPPContactStatus.ELM_NAME,
										 EPPContactStatus.class);
		}
	}

	// End EPPContactAddChange.decode(Element)

	/**
	 * implements a deep <code>EPPContactAddChange</code> compare.
	 *
	 * @param aObject <code>EPPContactAddChange</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		cat.debug("equals(Object): enter");
		
		if (!(aObject instanceof EPPContactAddChange)) {
			cat.error("equals(Object): aObject not EPPContactAddChange");
			return false;
		}

		EPPContactAddChange theComp = (EPPContactAddChange) aObject;

		// Mode
		if (mode != theComp.mode) {
			cat.error("equals(Object): mode not equal");
			return false;
		}

		// statuses
		if (!EPPUtil.equalVectors(statuses, theComp.statuses)) {
			cat.error("equals(Object): statuses not equal");
			return false;
		}

		// postalContacts
		if (!EPPUtil.equalVectors(postalContacts, theComp.postalContacts)) {
			cat.error("equals(Object): postalContacts not equal");
			return false;
		}

		// voice
		if (
			!(
					(voice == null) ? (theComp.voice == null)
										: voice.equals(theComp.voice)
				)) {
			cat.error("equals(Object): voice not equal");
			return false;
		}

		// voiceExt
		if (
			!(
					(voiceExt == null) ? (theComp.voiceExt == null)
										   : voiceExt.equals(theComp.voiceExt)
				)) {
			cat.error("equals(Object): voiceExt not equal");
			return false;
		}

		// fax
		if (!((fax == null) ? (theComp.fax == null) : fax.equals(theComp.fax))) {
			cat.error("equals(Object): fax not equal");
			return false;
		}

		// faxExt
		if (
			!(
					(faxExt == null) ? (theComp.faxExt == null)
										 : faxExt.equals(theComp.faxExt)
				)) {
			cat.error("equals(Object): faxExt not equal");
			return false;
		}

		// email
		if (
			!(
					(email == null) ? (theComp.email == null)
										: email.equals(theComp.email)
				)) {
			cat.error("equals(Object): email not equal");
			return false;
		}

		// authInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			cat.error("equals(Object): authInfo not equal");
			return false;
		}

		// disclose
		if (
			!(
					(disclose == null) ? (theComp.disclose == null)
										   : disclose.equals(theComp.disclose)
				)) {
			cat.error("equals(Object): disclose not equal");
			return false;
		}

		return true;
	}

	// End EPPContactAddChange.equals(Object)

	/**
	 * Clone <code>EPPContactAddChange</code>.
	 *
	 * @return clone of <code>EPPContactAddChange</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactAddChange clone = null;

		clone = (EPPContactAddChange) super.clone();

		if (statuses != null) {
			clone.statuses = (Vector) statuses.clone();

			for (int i = 0; i < statuses.size(); i++)
				clone.statuses.setElementAt(
											((EPPContactStatus) statuses
											 .elementAt(i)).clone(), i);
		}

		if (postalContacts != null) {
			clone.postalContacts = (Vector) postalContacts.clone();

			for (int i = 0; i < postalContacts.size(); i++)
				clone.postalContacts.setElementAt(
												  ((EPPContactPostalDefinition) postalContacts
												   .elementAt(i)).clone(), i);
		}

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (disclose != null) {
			clone.disclose = (EPPContactDisclose) disclose.clone();
		}

		return clone;
	}

	// End EPPContactAddChange.clone()

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

	// End EPPContactAddChange.toString()

	/**
	 * Gets the mode of <code>EPPContactAddChange</code>.  There are two valid
	 * modes <code>EPPContactAddChange.MODE_ADD</code> and
	 * <code>EPPContactAddChange.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPContactAddChange.MODE_NONE</code>.
	 *
	 * @return One of the <code>EPPContactAddChange_MODE</code> constants.
	 */
	short getMode() {
		return mode;
	}

	// End EPPContactAddChange.getMode()

	/**
	 * Sets the mode of <code>EPPContactAddChange</code>.  There are two valid
	 * modes <code>EPPContactAddChange.MODE_ADD</code> and
	 * <code>EPPContactAddChange.MODE_REMOVE</code>.     If no mode has been
	 * satisfied, than the mode is set to
	 * <code>EPPContactAddChange.MODE_NONE</code>
	 *
	 * @param aMode <code>EPPContactAddChange.MODE_ADD</code> or
	 * 		  <code>EPPContactAddChange.MODE_REMOVE</code>.
	 */
	void setMode(short aMode) {
		mode = aMode;
	}

	// End EPPContactAddChange.setMode(short)

	/**
	 * Get postalInfo elements of contact change.
	 *
	 * @return Vector
	 */
	public Vector getPostalInfo() {
		return postalContacts;
	}

	// End EPPContactAddChange.getPostalInfo()

	/**
	 * Set contact postalInfo.
	 *
	 * @param newPostalContacts java.util.Vector
	 */
	public void setPostalInfo(java.util.Vector newPostalContacts) {
		
		for( int i=0; i<newPostalContacts.size(); i++){
			if( newPostalContacts.elementAt(i) != null )
				addPostalInfo((EPPContactPostalDefinition)newPostalContacts.elementAt(i));
		}		
	}

	// End EPPContactAddChange.setPostalInfo(Vector)

	/**
	 * Adds contact postalInfo.
	 *
	 * @param newPostalInfo
	 * 		  com.verisign.epp.codec.contact.EPPContactPostalDefinition
	 */
	public void addPostalInfo(EPPContactPostalDefinition newPostalInfo) {
		// clone necessary here
		EPPContactPostalDefinition aPostalInfo = null;

		if (newPostalInfo != null) {
			try {
				aPostalInfo =
					(EPPContactPostalDefinition) newPostalInfo.clone();
			}
			 catch (CloneNotSupportedException e) {
				// Nothing needs to be done here
			}

			//postalInfo = aPostalInfo;
			//postalInfo.setRootName(EPPContactPostalDefinition.ELM_NAME_POSTAL_INFO);
			newPostalInfo.setValidatedFlag(false);
			postalContacts.add(newPostalInfo);

			//postalInfo.setValidatedFlag(false);
		}
	}

	// End EPPContactAddChange.addPostalInfo(EPPContactPostalDefinition)	

	/**
	 * Get autorization information for contact change.
	 *
	 * @return com.verisign.epp.codec.gen.EPPAuthInfo
	 */
	public com.verisign.epp.codec.gen.EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPContactAddChange.getAuthInfo()

	/**
	 * Get disclose information.
	 *
	 * @return Disclose information if defined; <code>null</code> otherwise;
	 */
	public com.verisign.epp.codec.contact.EPPContactDisclose getDisclose() {
		return disclose;
	}

	// End EPPContactAddChange.getDisclose()

	/**
	 * Set disclose information.
	 *
	 * @param newDisclose com.verisign.epp.codec.gen.EPPContactDisclose
	 */
	public void setDisclose(com.verisign.epp.codec.contact.EPPContactDisclose newDisclose) {
		if (newDisclose != null) {
			disclose = newDisclose;
			disclose.setRootName(ELM_CONTACT_DISCLOSE);
		}
	}

	// End EPPContactAddChange.setDisclose(EPPContactDisclose)

	/**
	 * Get email.
	 *
	 * @return String  email
	 */
	public String getEmail() {
		return email;
	}

	// End EPPContactAddChange.getEmail()

	/**
	 * Get fax number.
	 *
	 * @return String  fax number
	 */
	public String getFax() {
		return fax;
	}

	// End EPPContactAddChange.getFax()

	/**
	 * Get fax number extension.
	 *
	 * @return fax number extension if defined; <code>null</code> otherwise.
	 */
	public String getFaxExt() {
		return faxExt;
	}

	// End EPPContactAddChange.getFaxExt()

	/**
	 * Get a vector of contact statuses.
	 *
	 * @return java.util.Vector
	 */
	public java.util.Vector getStatuses() {
		return statuses;
	}

	// End EPPContactAddChange.getStatuses()

	/**
	 * Get voice number.
	 *
	 * @return String  fax number
	 */
	public String getVoice() {
		return voice;
	}

	// End EPPContactAddChange.getVoice()

	/**
	 * Get voice number extension.
	 *
	 * @return Voice number extension if defined; <code>null</code> otherwise.
	 */
	public String getVoiceExt() {
		return voiceExt;
	}

	// End EPPContactAddChange.getVoiceExt()

	/**
	 * Set authorization information for contact change.
	 *
	 * @param newAuthInfo com.verisign.epp.codec.gen.EPPAuthInfo
	 */
	public void setAuthInfo(com.verisign.epp.codec.gen.EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPContactMapFactory.NS, ELM_CONTACT_AUTHINFO);
		}
	}

	// End EPPContactAddChange.setAuthInfo(EPPAuthInfo)

	/**
	 * Set email.
	 *
	 * @param newEmail String
	 */
	public void setEmail(String newEmail) {
		email = newEmail;
	}

	// End EPPContactAddChange.setEmail(String)

	/**
	 * Set fax number.
	 *
	 * @param newFax String
	 */
	public void setFax(String newFax) {
		fax = newFax;
	}

	// End EPPContactAddChange.setFax(String)

	/**
	 * Set fax number extension.
	 *
	 * @param newFaxExt Fax number extension
	 */
	public void setFaxExt(String newFaxExt) {
		faxExt = newFaxExt;
	}

	// End EPPContactAddChange.setFaxExt(String)

	/**
	 * Set a vector of statuses for contact add.
	 *
	 * @param newStatuses java.util.Vector
	 */
	public void setStatuses(java.util.Vector newStatuses) {
		statuses = newStatuses;
	}

	// End EPPContactAddChange.setStatuses(Vector)

	/**
	 * Set voice number.
	 *
	 * @param newVoice String
	 */
	public void setVoice(String newVoice) {
		voice = newVoice;
	}

	// End EPPContactAddChange.setVoice(String)

	/**
	 * Set contact voice extension.
	 *
	 * @param newVoiceExt voice extension
	 */
	public void setVoiceExt(String newVoiceExt) {
		voiceExt = newVoiceExt;
	}

	// End EPPContactAddChange.setVoiceExt(String)
}
