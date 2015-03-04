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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.codec.mark.EPPMarkContact;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class for an address within an {@link EPPMarkContact}.
 */
public class EPPLaunchCodeMark implements EPPCodecComponent {
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchCodeMark.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the local name
	 */
	public static final String ELM_LOCALNAME = "codeMark";

	/**
	 * Constant for the tag name
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for the OPTIONAL code element
	 */
	private static final String ELM_CODE = "code";
	
	/**
	 * OPTIONAL &quot;validatorID&quot; attribute name that is used to define the 
	 * Validator Identifier of the Trademark Validator of the &lt;launch:code&gt; 
	 * element.  
	 */
	private final static String ATTR_VALIDATOR_ID = "validatorID";
	

	/**
	 * OPTIONAL mark code used to validate the mark information. The mark code
	 * can be a mark specific secret value that the server can verify against a
	 * third party.
	 */
	String code;
	
	/**
	 * OPTIONAL &quot;validatorID&quot; attribute that is used to define the 
	 * Validator Identifier of the Trademark Validator of the <code>code</code>.  
	 */
	private String validatorId = null;
	

	/**
	 * OPTIONAL mark information.
	 */
	EPPMark mark;

	/**
	 * Default constructor for <code>EPPLaunchCodeMark</code>.
	 */
	public EPPLaunchCodeMark() {
	}

	/**
	 * Constructor that takes just the code attribute of the
	 * <code>EPPLaunchCodeMark</code>.
	 * 
	 * 
	 * @param aCode
	 *            Mark code
	 */
	public EPPLaunchCodeMark(String aCode) {
		this.code = aCode;
	}

	/**
	 * Constructor that takes just the code attribute of the
	 * <code>EPPLaunchCodeMark</code> and the validator identifier of the code.
	 * 
	 * 
	 * @param aCode
	 *            Mark code
	 * @param aValidatorId
	 *            Identifier of the Trademark Validator that <code>aCode</code>
	 *            originated from.
	 */
	public EPPLaunchCodeMark(String aCode, String aValidatorId) {
		this.code = aCode;
		this.validatorId = aValidatorId;
	}	
	
	/**
	 * Constructor that takes just the mark attribute of the
	 * <code>EPPLaunchCodeMark</code>.
	 * 
	 * 
	 * @param aMark
	 *            Mark information
	 */
	public EPPLaunchCodeMark(EPPMark aMark) {
		this.mark = aMark;
	}


	/**
	 * Constructor that takes both the code and mark attributes of the
	 * <code>EPPLaunchCodeMark</code>.
	 * 
	 * 
	 * @param aCode
	 *            Mark code
	 * @param aMark
	 *            Mark information
	 */
	public EPPLaunchCodeMark(String aCode, EPPMark aMark) {
		this.code = aCode;
		this.mark = aMark;
	}
	
	/**
	 * Constructor that takes both the code, code validator identifier, and mark
	 * attributes of the <code>EPPLaunchCodeMark</code>.
	 * 
	 * 
	 * @param aCode
	 *            Mark code
	 * @param aValidatorId
	 *            Identifier of the Trademark Validator that <code>aCode</code>
	 *            originated from.
	 * @param aMark
	 *            Mark information
	 */
	public EPPLaunchCodeMark(String aCode, String aValidatorId, EPPMark aMark) {
		this.code = aCode;
		this.validatorId = aValidatorId;
		this.mark = aMark;
	}
	
	
	/**
	 * Clone <code>EPPLaunchCodeMark</code>.
	 * 
	 * @return clone of <code>EPPLaunchCodeMark</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPLaunchCodeMark clone = (EPPLaunchCodeMark) super.clone();

		// Mark
		if (this.mark != null) {
			clone.mark = (EPPMark) this.mark.clone();
		}

		return clone;
	}

	/**
	 * Sets all this instance's data in the given XML document
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
					+ " on in EPPLaunchCodeMark.encode(Document)");
		}

		if (this.code == null && this.mark == null) {
			throw new EPPEncodeException(
					"Both the codeMark attributes are not set.");
		}

		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);

		// Code
		if (this.code != null) {
			Element codeElm = aDocument.createElementNS(EPPLaunchExtFactory.NS,
					EPPLaunchExtFactory.NS_PREFIX + ":" + ELM_CODE);
			root.appendChild(codeElm);
			Text textNode = aDocument.createTextNode(this.code);
			codeElm.appendChild(textNode);

			// Validator Id
			if (this.validatorId != null) {
				codeElm.setAttribute(ATTR_VALIDATOR_ID, this.validatorId);
			}
		}

		// Mark
		EPPUtil.encodeComp(aDocument, root, this.mark);

		return root;
	}

	/**
	 * Decode the <code>EPPMark</code> component
	 * 
	 * @param aElement
	 *            Root element of the <code>EPPMark</code>
	 * @throws EPPDecodeException
	 *             Error decoding the <code>EPPMark</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Code
		this.code = EPPUtil.decodeString(aElement, EPPLaunchExtFactory.NS,
				ELM_CODE);
		
		// Validator Id
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPLaunchExtFactory.NS, ELM_CODE);
		if (currElm != null && currElm.hasAttribute(ATTR_VALIDATOR_ID)) {
			this.validatorId = currElm.getAttribute(ATTR_VALIDATOR_ID);
			if (this.validatorId != null && this.validatorId.length() == 0) {
				this.validatorId = null;
			}
		}
		else {
			this.validatorId = null;
		}
		

		// Mark
		this.mark = (EPPMark) EPPUtil.decodeComp(aElement, EPPMark.NS,
				EPPMark.ELM_LOCALNAME, EPPMark.class);
	}

	/**
	 * implements a deep <code>EPPLaunchCodeMark</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchCodeMark</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPLaunchCodeMark)) {
			cat
					.error("EPPLaunchCodeMark.equals(): aObject is not an EPPLaunchCodeMark");
			return false;
		}

		EPPLaunchCodeMark other = (EPPLaunchCodeMark) aObject;

		// Code
		if (!EqualityUtil.equals(this.code, other.code)) {
			cat.error("EPPLaunchCodeMark.equals(): code not equal");
			return false;
		}
		
		// Validator Id
		if (!EqualityUtil.equals(this.validatorId, other.validatorId)) {
			cat.error("EPPLaunchCodeMark.equals(): validatorId not equal");
			return false;
		}

		// Mark
		if (!EqualityUtil.equals(this.mark, other.mark)) {
			cat.error("EPPMark.equals(): mark not equal");
			return false;
		}

		return true;
	}

	/**
	 * Is both the mark code and mark set?
	 * 
	 * @return <code>true</code> if the mark code and mark are set;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasCodeMark() {
		return (this.code != null && this.mark != null) ? true : false;

	}

	/**
	 * Is the mark code set?
	 * 
	 * @return <code>true</code> if the mark code is set; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasCode() {
		return (this.code != null) ? true : false;
	}

	/**
	 * Gets the mark code used to validate the mark information.
	 * 
	 * @return The mark code if defined; <code>null</code> otherwise.
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Sets the mark code used to validate the mark information.
	 * 
	 * @param aCode
	 *            The mark code
	 */
	public void setCode(String aCode) {
		this.code = aCode;
	}

	/**
	 * Gets the OPTIONAL Validator Identifier, which is the unique identifier 
	 * for the Trademark Validator that the <code>code</code> originates from. 
	 * 
	 * @return The Validator Identifier if defined; otherwise <code>null</code>. 
	 */
	public String getValidatorId() {
		return this.validatorId;
	}

	/**
	 * Sets the OPTIONAL Validator Identifier, which is the unique identifier 
	 * for the Trademark Validator that the <code>code</code> originates from. 
	 * 
	 * @param aValidatorId Validator Identifier
	 */
	public void setValidatorId(String aValidatorId) {
		this.validatorId = aValidatorId;
	}
	
	/**
	 * Is the Validator Identifier defined?
	 * 
	 * @return <code>true</code> if the Validator Identifier is defined;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasValidatorId() {
		return (this.validatorId != null ? true : false);
	}
	
	
	/**
	 * Is the mark set?
	 * 
	 * @return <code>true</code> if the mark is set; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasMark() {
		return (this.mark != null) ? true : false;
	}

	/**
	 * Gets the mark information.
	 * 
	 * @return the mark information if defined; <code>null</code> otherwise.
	 */
	public EPPMark getMark() {
		return this.mark;
	}

	/**
	 * Sets the mark information.
	 * 
	 * @param aMark
	 *            The mark information
	 */
	public void setMark(EPPMark aMark) {
		this.mark = aMark;
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

}
