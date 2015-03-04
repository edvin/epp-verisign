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
package com.verisign.epp.codec.signedMark;

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
public class EPPIssuer implements EPPCodecComponent {
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPIssuer.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the local name
	 */
	public static final String ELM_LOCALNAME = "issuerInfo";

	/**
	 * Constant for the tag name
	 */
	public static final String ELM_NAME = EPPSignedMark.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Element local name for the organization name of the issuer.
	 */
	private final static String ELM_ORG = "org";

	/** Element local name for issuer customer support email address. */
	private final static String ELM_EMAIL = "email";

	/** Element local name for HTTP URL of the issuer's site. */
	private final static String ELM_URL = "url";

	/** Element local name for issuer's voice telephone number. */
	private final static String ELM_VOICE = "voice";

	/**
	 * Attribute name of issuer identifier.
	 */
	private final static String ATTR_ID = "issuerID";

	/**
	 * XML Attribute Name for a voice extension.
	 */
	private final static String ATTR_EXT = "x";

	/**
	 * Issuer identifier
	 */
	private String id;

	/**
	 * The organization name of the issuer.
	 */
	private String org;

	/**
	 * The issuer customer support email address.
	 */
	private String email;

	/**
	 * The OPTIONAL HTTP URL of the issuer's site.
	 */
	private String url;

	/**
	 * The OPTIONAL issuer's voice telephone number.
	 */
	private String voice;

	/**
	 * The OPTIONAL issuer's voice telephone number extension.
	 */
	private String voiceExt;

	/**
	 * Default constructor for <code>EPPIssuer</code>.
	 */
	public EPPIssuer() {
	}

	/**
	 * Constructor for <code>EPPIssuer</code> that takes the required
	 * attributes.
	 * 
	 * @param aId
	 *            Issuer identifier
	 * @param aOrg
	 *            Organization name of the issuer
	 * @param aEmail Issuer customer support email address
	 */
	public EPPIssuer(String aId, String aOrg, String aEmail) {
		this.id = aId;
		this.org = aOrg;
		this.email = aEmail;
	}

	/**
	 * Constructor for <code>EPPIssuer</code> with all attributes.
	 * 
	 * @param aId
	 *            Issuer identifier
	 * @param aOrg
	 *            Organization name of the issuer
	 * @param aEmail
	 *            Issuer customer support email address
	 * @param aUrl
	 *            HTTP URL of the issuer's site
	 * @param aVoice
	 *            Issuer's voice telephone number.
	 * @param aVoiceExt
	 *            Issuer's voice telephone number extension.
	 */
	public EPPIssuer(String aId, String aOrg, String aEmail, String aUrl,
			String aVoice, String aVoiceExt) {
		this.id = aId;
		this.org = aOrg;
		this.email = aEmail;
		this.url = aUrl;
		this.voice = aVoice;
		this.voiceExt = aVoiceExt;
	}

	/**
	 * Clone <code>EPPIssuer</code>.
	 * 
	 * @return clone of <code>EPPIssuer</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPIssuer clone = (EPPIssuer) super.clone();

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
					+ " on in EPPIssuer.encode(Document)");
		}

		// Validate required attributes
		if (this.id == null) {
			throw new EPPEncodeException("Issuer id is required.");
		}
		if (this.email == null) {
			throw new EPPEncodeException("Issuer email is required.");
		}
		if (this.org == null) {
			throw new EPPEncodeException("Issuer org is required.");
		}

		Element root = aDocument.createElementNS(EPPSignedMark.NS, ELM_NAME);

		// Id
		root.setAttribute(ATTR_ID, this.id);

		// Org
		EPPUtil.encodeString(aDocument, root, this.org, EPPSignedMark.NS,
				EPPSignedMark.NS_PREFIX + ":" + ELM_ORG);

		// Email
		EPPUtil.encodeString(aDocument, root, this.email, EPPSignedMark.NS,
				EPPSignedMark.NS_PREFIX + ":" + ELM_EMAIL);

		// Url
		EPPUtil.encodeString(aDocument, root, this.url, EPPSignedMark.NS,
				EPPSignedMark.NS_PREFIX + ":" + ELM_URL);

		// Voice
		if (this.voice != null) {
			Element currElm = aDocument.createElementNS(EPPSignedMark.NS,
					EPPSignedMark.NS_PREFIX + ":" + ELM_VOICE);
			Text currVal = aDocument.createTextNode(this.voice);

			if (currVal != null) {
				currElm.setAttribute(ATTR_EXT, this.voiceExt);
			}

			currElm.appendChild(currVal);
			root.appendChild(currElm);
		}

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

		// Id
		this.id = aElement.getAttribute(ATTR_ID);

		// Org
		this.org = EPPUtil.decodeString(aElement, EPPSignedMark.NS, ELM_ORG);

		// Email
		this.email = EPPUtil
				.decodeString(aElement, EPPSignedMark.NS, ELM_EMAIL);

		// Url
		this.url = EPPUtil.decodeString(aElement, EPPSignedMark.NS, ELM_URL);

		// Voice
		this.voice = EPPUtil
				.decodeString(aElement, EPPSignedMark.NS, ELM_VOICE);

		if (this.voice != null) {
			Element currElm = EPPUtil.getElementByTagNameNS(aElement,
					EPPSignedMark.NS, ELM_VOICE);
			this.voiceExt = currElm.getAttribute(ATTR_EXT);

			if (this.voiceExt.isEmpty()) {
				this.voiceExt = null;
			}
		}
		else {
			this.voiceExt = null;
		}
	}

	/**
	 * implements a deep <code>EPPIssuer</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPIssuer</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPIssuer)) {
			cat.error("EPPIssuer.equals(): aObject is not an EPPIssuer");
			return false;
		}

		EPPIssuer other = (EPPIssuer) aObject;

		// Id
		if (!EqualityUtil.equals(this.id, other.id)) {
			cat.error("EPPIssuer.equals(): id not equal");
			return false;
		}

		// Org
		if (!EqualityUtil.equals(this.org, other.org)) {
			cat.error("EPPIssuer.equals(): org not equal");
			return false;
		}

		// Email
		if (!EqualityUtil.equals(this.email, other.email)) {
			cat.error("EPPIssuer.equals(): email not equal");
			return false;
		}

		// Url
		if (!EqualityUtil.equals(this.url, other.url)) {
			cat.error("EPPIssuer.equals(): url not equal");
			return false;
		}

		// Voice
		if (!EqualityUtil.equals(this.voice, other.voice)) {
			cat.error("EPPIssuer.equals(): voice not equal");
			return false;
		}

		// VoiceExt
		if (!EqualityUtil.equals(this.voiceExt, other.voiceExt)) {
			cat.error("EPPIssuer.equals(): voiceExt not equal");
			return false;
		}

		return true;
	}

	/**
	 * Gets the issuer identifier.
	 * 
	 * @return The issuer identifier if set; <code>null</code> otherwise.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the issuer identifier.
	 * 
	 * @param aId
	 *            The issuer identifier
	 */
	public void setId(String aId) {
		this.id = aId;
	}

	/**
	 * Gets the organization name of the issuer.
	 * 
	 * @return The organization name of the issuer if set; <code>null</code>
	 *         otherwise.
	 */
	public String getOrg() {
		return this.org;
	}

	/**
	 * Sets the organization name of the issuer.
	 * 
	 * @param aOrg
	 *            The organization name of the issuer.
	 */
	public void setOrg(String aOrg) {
		this.org = aOrg;
	}

	/**
	 * Gets the issuer customer support email address.
	 * 
	 * @return Issuer customer support email address if set; <code>null</code>
	 *         otherwise.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the issuer customer support email address.
	 * 
	 * @param aEmail
	 *            Issuer customer support email address
	 */
	public void setEmail(String aEmail) {
		this.email = aEmail;
	}

	/**
	 * Gets HTTP URL of the issuer's site.
	 * 
	 * @return HTTP URL of the issuer's site if set; <code>null</code>
	 *         otherwise.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Sets HTTP URL of the issuer's site.
	 * 
	 * @param aUrl
	 *            Gets HTTP URL of the issuer's site.
	 */
	public void setUrl(String aUrl) {
		this.url = aUrl;
	}

	/**
	 * Gets the issuer's voice telephone number.
	 * 
	 * @return The issuer's voice telephone number if defined; <code>null</code>
	 *         otherwise.
	 */
	public String getVoice() {
		return this.voice;
	}

	/**
	 * Sets the issuer's voice telephone number.
	 * 
	 * @param aVoice
	 *            The issuer's voice telephone number.
	 */
	public void setVoice(String aVoice) {
		this.voice = aVoice;
	}

	/**
	 * Gets the issuer's voice telephone number extension.
	 * 
	 * @return The issuer's voice telephone number extension if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getVoiceExt() {
		return this.voiceExt;
	}

	/**
	 * Sets the issuer's voice telephone extension number.
	 * 
	 * @param aVoiceExt
	 *            The issuer's voice telephone extension number.
	 */
	public void setVoiceExt(String aVoiceExt) {
		this.voiceExt = aVoiceExt;
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
