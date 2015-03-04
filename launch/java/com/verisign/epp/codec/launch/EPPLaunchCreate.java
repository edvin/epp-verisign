/***********************************************************
Copyright (C) 2012 VeriSign, Inc.

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
 ***********************************************************/

package com.verisign.epp.codec.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.signedMark.EPPEncodedSignedMark;
import com.verisign.epp.codec.signedMark.EPPSignedMark;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Extension to the domain create command to create a launch phase application.
 */
public class EPPLaunchCreate implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchCreate.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant used to specify to create an application.
	 */
	public static final String TYPE_APPLICATION = "application";

	/**
	 * Constant used to specify to create a registration.
	 */
	public static final String TYPE_REGISTRATION = "registration";

	/**
	 * Constant for the launch phase create extension local name
	 */
	public static final String ELM_LOCALNAME = "create";

	/**
	 * Constant for the launch phase info extension tag
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * The "type" attribute name
	 */
	private static final String ATTR_TYPE = "type";

	/**
	 * The phase during which the application was submitted or is associated
	 * with.
	 */
	EPPLaunchPhase phase;

	/**
	 * Signed mark used for the Sunrise Create Form
	 */
	List<EPPSignedMark> signedMarks;

	/**
	 * Code mark used for the Sunrise Create Form
	 */
	List<EPPLaunchCodeMark> codeMarks;

	/**
	 * Claims notice information used for Claims Create Form
	 */
	EPPLaunchNotice notice;

	/**
	 * Defines the expected type of object to create. The default value is
	 * <code>null</code>, meaning to not include the attribute in the command.
	 */
	String type;

	/**
	 * Create an <code>EPPLaunchCreate</code> instance
	 */
	public EPPLaunchCreate() {
	}

	/**
	 * Create an <code>EPPLaunchCreate</code> instance with the required phase
	 * attribute.
	 * 
	 * @param aPhase
	 *            Phase associated with the create.
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase) {
		this.phase = aPhase;
	}

	/**
	 * Create an <code>EPPLaunchCreate</code> instance with the required phase
	 * attribute and optional type.
	 * 
	 * @param aPhase
	 *            Phase associated with the create.
	 * @param aType
	 *            Type of object to create (<code>TYPE_APPLICATION</code> or
	 *            <code>TYPE_REGISTRATION</code>)
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, String aType) {
		this.phase = aPhase;
		this.type = aType;
	}

	/**
	 * Create a <code>EPPLaunchCreate</code> instance with the required phase
	 * and a single signed mark.
	 * 
	 * @param aPhase
	 *            Phase of the create
	 * @param aSignedMark
	 *            Signed mark used with Sunrise Create Form
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, EPPSignedMark aSignedMark) {
		this.phase = aPhase;
		this.setSignedMark(aSignedMark);
	}

	/**
	 * Create a <code>EPPLaunchCreate</code> instance with the required phase
	 * and a single signed mark and optional type.
	 * 
	 * @param aPhase
	 *            Phase of the create
	 * @param aSignedMark
	 *            Signed mark used with Sunrise Create Form
	 * @param aType
	 *            Type of object to create (<code>TYPE_APPLICATION</code> or
	 *            <code>TYPE_REGISTRATION</code>)
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, EPPSignedMark aSignedMark, String aType) {
		this.phase = aPhase;
		this.setSignedMark(aSignedMark);
		this.type = aType;
	}
	
	
	/**
	 * Create a <code>EPPLaunchCreate</code> instance with the required phase
	 * and a single code mark.
	 * 
	 * @param aPhase
	 *            Phase of the create
	 * @param aCodeMark
	 *            Code mark used with Sunrise Create Form
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, EPPLaunchCodeMark aCodeMark) {
		this.phase = aPhase;
		this.setCodeMark(aCodeMark);
	}
	
	/**
	 * Create a <code>EPPLaunchCreate</code> instance with the required phase
	 * and a single code mark and the optional type.
	 * 
	 * @param aPhase
	 *            Phase of the create
	 * @param aCodeMark
	 *            Code mark used with Sunrise Create Form
	 * @param aType
	 *            Type of object to create (<code>TYPE_APPLICATION</code> or
	 *            <code>TYPE_REGISTRATION</code>)
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, EPPLaunchCodeMark aCodeMark, String aType) {
		this.phase = aPhase;
		this.setCodeMark(aCodeMark);
		this.type = aType;
	}
	

	/**
	 * Create a <code>EPPLaunchCreate</code> instance with the required phase
	 * and the claims notice information for Claims Create Form.
	 * 
	 * @param aPhase
	 *            Phase of the create
	 * @param aNotice
	 *            Claims notice information
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, EPPLaunchNotice aNotice) {
		this.phase = aPhase;
		this.notice = aNotice;
	}
	
	/**
	 * Create a <code>EPPLaunchCreate</code> instance with the required phase
	 * and the claims notice information for Claims Create Form and optional type.
	 * 
	 * @param aPhase
	 *            Phase of the create
	 * @param aNotice
	 *            Claims notice information
	 * @param aType
	 *            Type of object to create (<code>TYPE_APPLICATION</code> or
	 *            <code>TYPE_REGISTRATION</code>)
	 */
	public EPPLaunchCreate(EPPLaunchPhase aPhase, EPPLaunchNotice aNotice, String aType) {
		this.phase = aPhase;
		this.notice = aNotice;
		this.type = aType;
	}
	

	/**
	 * Is the type defined?
	 * 
	 * @return <code>true</code> if the type is defined;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasType() {
		return (this.type != null ? true : false);
	}
	
	
	/**
	 * Gets the type of the object to create, which should be either
	 * <code>TYPE_APPLICATION</code>, <code>TYPE_REGISTRATION</code>, or
	 * <code>null</code> for undefined.
	 * 
	 * @return <code>TYPE_APPLICATION</code>, <code>TYPE_REGISTRATION</code>, or
	 *         <code>null</code> for undefined.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Sets the type of the object to create, with the default being
	 * <code>null</code> or undefined.
	 * 
	 * @param aType
	 *            Type of object to create.
	 */
	public void setType(String aType) {
		this.type = aType;
	}

	/**
	 * Gets phase associated with the create command.
	 * 
	 * @return phase associated with the create if defined; <code>null</code>
	 *         otherwise.
	 */
	public EPPLaunchPhase getPhase() {
		return this.phase;
	}

	/**
	 * Sets the phase associated with the create command.
	 * 
	 * @param aPhase
	 *            Phase associated with the create.
	 */
	public void setPhase(EPPLaunchPhase aPhase) {
		this.phase = aPhase;
	}

	/**
	 * Sets the phase with one of the <code>EPPLaunchPhase</code>
	 * <code>PHASE</code> constants.
	 * 
	 * @param aPhaseString
	 *            One of the <code>EPPLaunchPhase</code> <code>PHASE</code>
	 *            constants.
	 */
	public void setPhase(String aPhaseString) {
		this.phase = new EPPLaunchPhase(aPhaseString);
	}

	/**
	 * Are signed marks defined?
	 * 
	 * @return <code>true</code> if signed marks are defined; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasSignedMarks() {
		return (this.signedMarks != null ? true : false);
	}

	/**
	 * Gets the list of signed marks.
	 * 
	 * @return List of signed marks if set; <code>null</code> otherwise.
	 */
	public List<EPPSignedMark> getSignedMarks() {
		return this.signedMarks;
	}

	/**
	 * Sets the list of signed marks.
	 * 
	 * @param aSignedMarks
	 *            List of signed marks
	 */
	public void setSignedMarks(List<EPPSignedMark> aSignedMarks) {
		this.signedMarks = aSignedMarks;
	}

	/**
	 * Gets the signed mark when using the Sunrise Create Form.
	 * 
	 * @return Signed mark if defined; <code>null</code> otherwise.
	 */
	public EPPSignedMark getSignedMark() {
		if (this.signedMarks != null && this.signedMarks.size() > 0) {
			return this.signedMarks.get(0);
		}
		else {
			return null;
		}
	}

	/**
	 * Sets the signed mark when using the Sunrise Create Form.
	 * 
	 * @param aSignedMark
	 *            Signed mark when using Sunrise Create Form
	 */
	public void setSignedMark(EPPSignedMark aSignedMark) {
		this.signedMarks = new ArrayList<EPPSignedMark>();
		this.signedMarks.add(aSignedMark);
	}

	/**
	 * Adds a signed mark to the list of signed marks.
	 * 
	 * @param aSignedMark
	 *            Signed mark to add to the list of signed marks.
	 */
	public void addSignedMark(EPPSignedMark aSignedMark) {
		if (this.signedMarks == null) {
			this.signedMarks = new ArrayList<EPPSignedMark>();
		}

		this.signedMarks.add(aSignedMark);
	}

	/**
	 * Are code marks defined?
	 * 
	 * @return <code>true</code> if code marks are defined; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasCodeMarks() {
		return (this.codeMarks != null ? true : false);
	}

	/**
	 * Gets the list of code marks.
	 * 
	 * @return List of code marks if set; <code>null</code> otherwise.
	 */
	public List<EPPLaunchCodeMark> getCodeMarks() {
		return this.codeMarks;
	}

	/**
	 * Sets the list of code marks.
	 * 
	 * @param aCodeMarks
	 *            List of code marks
	 */
	public void setCodeMarks(List<EPPLaunchCodeMark> aCodeMarks) {
		this.codeMarks = aCodeMarks;
	}

	/**
	 * Gets the code mark when using the Sunrise Create Form.
	 * 
	 * @return Code mark if defined; <code>null</code> otherwise.
	 */
	public EPPLaunchCodeMark getCodeMark() {
		if (this.codeMarks != null && this.codeMarks.size() > 0) {
			return this.codeMarks.get(0);
		}
		else {
			return null;
		}
	}

	/**
	 * Sets the code mark when using the Sunrise Create Form.
	 * 
	 * @param aCodeMark
	 *            Code mark when using Sunrise Create Form
	 */
	public void setCodeMark(EPPLaunchCodeMark aCodeMark) {
		this.codeMarks = new ArrayList<EPPLaunchCodeMark>();
		this.codeMarks.add(aCodeMark);
	}

	public void addCodeMark(EPPLaunchCodeMark aCodeMark) {
		if (this.codeMarks == null) {
			this.codeMarks = new ArrayList<EPPLaunchCodeMark>();
		}

		this.codeMarks.add(aCodeMark);
	}

	/**
	 * Is the claims notice defined?
	 * 
	 * @return <code>true</code> if the claims notice is defined;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasNotice() {
		return (this.notice != null ? true : false);
	}

	/**
	 * Gets the claims notice information in Claims Create Form.
	 * 
	 * @return Claims notice information if set; <code>null</code> otherwise.
	 */
	public EPPLaunchNotice getNotice() {
		return this.notice;
	}

	/**
	 * Sets the claims notice information in Claims Create Form.
	 * 
	 * @param aNotice
	 *            Claims notice information.
	 */
	public void setNotice(EPPLaunchNotice aNotice) {
		this.notice = aNotice;
	}

	/**
	 * Clone <code>EPPLaunchCreate</code>.
	 * 
	 * @return clone of <code>EPPLaunchCreate</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPLaunchCreate clone = (EPPLaunchCreate) super.clone();
		return clone;
	}

	/**
	 * Encode the <code>EPPLaunchCreate</code> to a DOM Element
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPLaunchCreate.encode(Document)");
		}

		// Required phase attribute not set?
		if (this.phase == null) {
			throw new EPPEncodeException(
					"EPPLaunchCreate phase attribute must be set.");
		}

		boolean extTypeFound = false;

		if (this.signedMarks != null && this.codeMarks != null) {
			throw new EPPEncodeException(
					"EPPLaunchCreate more than one type of extension defined (signedMark and codeMark).");
		}

		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);

		// Type
		if (this.type != null) {
			if (!this.type.equals(TYPE_APPLICATION)
					&& !this.type.equals(TYPE_REGISTRATION)) {
				throw new EPPEncodeException(
						"EPPLaunchCreate type attribute with invalid value of: "
								+ this.type);
			}

			root.setAttribute(ATTR_TYPE, this.type);
		}

		// Phase
		EPPUtil.encodeComp(aDocument, root, this.phase);

		// The elements below are mutually exclusive

		// Signed Mark
		EPPUtil.encodeCompList(aDocument, root, this.signedMarks);

		// Code Mark
		EPPUtil.encodeCompList(aDocument, root, this.codeMarks);

		// Claim Notice
		EPPUtil.encodeComp(aDocument, root, this.notice);

		return root;
	}

	/**
	 * Decode the DOM element to the <code>EPPLaunchCreate</code>.
	 * 
	 * @param aElement
	 *            DOM Element to decode the attribute values
	 * @throws EPPDecodeException
	 *             Error decoding the DOM Element
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Phase
		this.phase = (EPPLaunchPhase) EPPUtil.decodeComp(aElement,
				EPPLaunchExtFactory.NS, EPPLaunchPhase.ELM_NAME,
				EPPLaunchPhase.class);

		if (this.phase == null) {
			throw new EPPDecodeException(
					"EPPLaunchCreate phase element not found.");
		}

		// Type
		this.type = aElement.getAttribute(ATTR_TYPE);
		if (this.type.isEmpty()) {
			this.type = null;
		}

		// Signed Mark List (XML and Base64)
		if (this.signedMarks == null) {
			this.signedMarks = this.decodeSignedMarkList(aElement);
			if (this.signedMarks != null && this.signedMarks.isEmpty()) {
				this.signedMarks = null;
			}
		}

		// Code Mark
		this.codeMarks = EPPUtil.decodeCompList(aElement,
				EPPLaunchExtFactory.NS, EPPLaunchCodeMark.ELM_NAME,
				EPPLaunchCodeMark.class);
		if (this.codeMarks != null && this.codeMarks.isEmpty()) {
			this.codeMarks = null;
		}

		// Claim Notice
		this.notice = (EPPLaunchNotice) EPPUtil.decodeComp(aElement,
				EPPLaunchExtFactory.NS, EPPLaunchNotice.ELM_NAME,
				EPPLaunchNotice.class);
	}

	
	
	/**
	 * implements a deep <code>EPPLaunchCreate</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchCreate</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPLaunchCreate)) {
			cat.error("EPPLaunchCreate.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPLaunchCreate instance");

			return false;
		}

		EPPLaunchCreate other = (EPPLaunchCreate) aObject;

		// Type
		if (!EqualityUtil.equals(this.type, other.type)) {
			cat.error("EPPLaunchCreate.equals(): type not equal");
			return false;
		}

		// Phase
		if (!EqualityUtil.equals(this.phase, other.phase)) {
			cat.error("EPPLaunchCreate.equals(): phase not equal");
			return false;
		}

		// Signed Mark
		if (!EqualityUtil.equals(this.signedMarks, other.signedMarks)) {
			cat.error("EPPLaunchCreate.equals(): signedMark not equal");
			return false;
		}

		// Code Mark
		if (!EqualityUtil.equals(this.codeMarks, other.codeMarks)) {
			cat.error("EPPLaunchCreate.equals(): codeMark not equal");
			return false;
		}

		// Claim Notice
		if (!EqualityUtil.equals(this.notice, other.notice)) {
			cat.error("EPPLaunchCreate.equals(): notice not equal");
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}
	
	/**
	 * Decodes the list of signed marks that can be either 
	 * encoded signed marks or signed marks.  
	 * 
	 * @param aElement Parent element containing the list of signed marks
	 * @return List of {@link EPPImmutableSignedMark} instances.  
	 * @throws EPPDecodeException Error decoding the signed mark
	 */
	private List decodeSignedMarkList(Element aElement)
			throws EPPDecodeException {
		List retVal = new ArrayList();

		Vector theChildren = EPPUtil.getElementsByTagNameNS(aElement,
				EPPSignedMark.NS,
				EPPEncodedSignedMark.ELM_ENCODED_SIGNED_MARK_LOCALNAME);
		
		if (theChildren.size() != 0) {
			// For each signed mark (encoded or not)
			for (int i = 0; i < theChildren.size(); i++) {
				EPPSignedMark signedMark;
				try {
					signedMark = new EPPEncodedSignedMark();
					signedMark.decode((Element) theChildren.elementAt(i));	
				}
				catch (EPPException e) {
					throw new EPPDecodeException("Error decoding signed mark: " + e);
				}
				retVal.add(signedMark);
			}
		}
		else {
			theChildren = EPPUtil.getElementsByTagNameNS(aElement,
					EPPSignedMark.NS, EPPSignedMark.ELM_SIGNED_MARK_LOCALNAME);
		
			// For each signed mark (encoded or not)
			for (int i = 0; i < theChildren.size(); i++) {
				EPPSignedMark signedMark;
				try {
					signedMark = new EPPSignedMark();
					signedMark.decode((Element) theChildren.elementAt(i));
				}
				catch (EPPException e) {
					throw new EPPDecodeException("Error decoding signed mark: " + e);
				}
				retVal.add(signedMark);
			}
			
		}

		return retVal;
	}
	

}