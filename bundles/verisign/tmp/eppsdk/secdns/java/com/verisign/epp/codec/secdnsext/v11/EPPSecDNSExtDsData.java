/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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

package com.verisign.epp.codec.secdnsext.v11;

// W3C Imports
import org.w3c.dom.*;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtFactory;

/**
 * The EPPSecDNSExtDsData is the EPPCodecComponent that knows how to encode and
 * decode secDNS dsData elements from/to XML and object instance.
 * 
 * <p>
 * Title: EPP 1.0 secDNS
 * </p>
 * <p>
 * Description: secDNS Extension to the EPP SDK
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @version 1.1
 */

public class EPPSecDNSExtDsData implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPSecDNSExtDsData.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** XML Element Name of <code>EPPSecDNSExtDsData</code> root element. */
	static final String ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":dsData";

	/**
	 * The element tag name for keyTag
	 */
	static final String ELM_KEY_TAG = EPPSecDNSExtFactory.NS_PREFIX + ":keyTag";

	/**
	 * The element tag name for algorithm
	 */
	private static final String ELM_ALG = EPPSecDNSExtFactory.NS_PREFIX
			+ ":alg";

	/**
	 * The element tag name for digestType
	 */
	private static final String ELM_DIGEST_TYPE = EPPSecDNSExtFactory.NS_PREFIX
			+ ":digestType";

	/**
	 * The element tag name for digest
	 */
	private static final String ELM_DIGEST = EPPSecDNSExtFactory.NS_PREFIX
			+ ":digest";

	/** Unspecified int */
	private static final int UNSPEC_INT = -1;

	/** Unspecified keyTag value */
	public static final int UNSPEC_KEY_TAG = UNSPEC_INT;

	/** Minimum keyTag value */
	public static final int MIN_KEY_TAG = 0;

	/** Maximum keyTag value */
	public static final int MAX_KEY_TAG = 65535;

	/** Unspecified alg value */
	public static final int UNSPEC_ALG = UNSPEC_INT;

	/** Minimum alg value */
	public static final int MIN_ALG = 0;

	/** Maximum alg value */
	public static final int MAX_ALG = 255;

	/** Unspecified digestType value */
	public static final int UNSPEC_DIGEST_TYPE = UNSPEC_INT;

	/** Minimum digestType value */
	public static final int MIN_DIGEST_TYPE = 0;

	/** Maximum digestType value */
	public static final int MAX_DIGEST_TYPE = 65535;

	/**
	 * http://www.ietf.org/rfc/rfc4034.txt<br/>
	 * A.2. DNSSEC Digest Types<br/>
	 * <br/>
	 * A "Digest Type" field in the DS resource record types identifies the
	 * cryptographic digest algorithm used by the resource record. The following
	 * table lists the currently defined digest algorithm types. <br/>
	 * <table>
	 * <tr>
	 * <td>VALUE</td>
	 * <td>Algorithm</td>
	 * <td>STATUS</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>Reserved</td>
	 * <td>-</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>SHA-1</td>
	 * <td>MANDATORY</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>SHA-256</td>
	 * <td>-</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>SHA-512</td>
	 * <td>-</td>
	 * </tr>
	 * <tr>
	 * <td>2-255</td>
	 * <td>Unassigned</td>
	 * <td>-</td>
	 * </tr>
	 * </table>
	 */
	public static final int SHA1_DIGEST_TYPE = 1;
	
	/**
	 * SHA-256 Digest Type 
	 */
	public static final int SHA256_DIGEST_TYPE = 2;

	/**
	 * GOST R 34.11-94 
	 */
	public static final int GOST3411_DIGEST_TYPE = 3;
	
	/**
	 * SHA-384 Digest Type 
	 */
	public static final int SHA384_DIGEST_TYPE = 4;
	
	/**
	 * The keyTag field value of this dsData component
	 */
	private int keyTag = UNSPEC_KEY_TAG;

	/**
	 * The algorithm value of this dsData component
	 */
	private int alg = UNSPEC_ALG;

	/**
	 * The digestType value of this dsData component
	 */
	private int digestType = UNSPEC_DIGEST_TYPE;

	/**
	 * The digest value of this dsData component
	 */
	private String digest = null;

	/**
	 * The keyData value of this dsData component
	 */
	private EPPSecDNSExtKeyData keyData = null;

	/**
	 * Create a new empty instance of EPPSecDNSExtDsData
	 */
	public EPPSecDNSExtDsData() {
	}

	/**
	 * Create a new instance (copy) of EPPSecDNSExtDsData
	 * 
	 * @param dsData
	 *            <code>EPPSecDNSExtDsData</code> instance
	 */
	public EPPSecDNSExtDsData(EPPSecDNSExtDsData dsData) {
		this(dsData.getKeyTag(), dsData.getAlg(), dsData.getDigestType(),
				dsData.getDigest(), dsData.getKeyData());

	}

	/**
	 * Create a new instance of EPPSecDNSExtDsData with the given values. This
	 * constructor includes all secDNS:dsData elements.
	 * 
	 * @param keyTag
	 *            the keyTag value to use for this instance.
	 * @param alg
	 *            the algorithm value to use for this instance.
	 * @param digestType
	 *            the digestType value to use for this instance.
	 * @param digest
	 *            the digest value to use for this instance.
	 * @param keyData
	 *            the keyData value to use for this instance.
	 */
	public EPPSecDNSExtDsData(int keyTag, int alg, int digestType,
			String digest, EPPSecDNSExtKeyData keyData) {
		setKeyTag(keyTag);
		setAlg(alg);
		setDigestType(digestType);
		setDigest(digest);
		setKeyData(keyData);
	}

	/**
	 * Create a new instance of EPPSecDNSExtDsData with the given values. This
	 * constructor includes only the required secDNS:dsData elements.
	 * 
	 * @param keyTag
	 *            the keyTag value to use for this instance.
	 * @param alg
	 *            the algorithm value to use for this instance.
	 * @param digestType
	 *            the digestType value to use for this instance.
	 * @param digest
	 *            the digest value to use for this instance.
	 */
	public EPPSecDNSExtDsData(int keyTag, int alg, int digestType, String digest) {
		setKeyTag(keyTag);
		setAlg(alg);
		setDigestType(digestType);
		setDigest(digest);
	}

	/**
	 * Create a new instance of EPPSecDNSExtDsData with the given values. This
	 * convenience constructor includes only the secDNS:keyTag element required
	 * for the secDNS:update/secDNS:rem extension.
	 * 
	 * @param keyTag
	 *            an <code>int</code> value representing the secDNS:keyTag to
	 *            use for this instance.
	 */
	public EPPSecDNSExtDsData(int keyTag) {
		setKeyTag(keyTag);
	}

	/**
	 * Create a new instance of EPPSecDNSExtDsData with the given values. This
	 * convenience constructor includes only the secDNS:keyTag element required
	 * for the secDNS:update/secDNS:rem extension.
	 * 
	 * @param keyTag
	 *            an <code>Integer</code> instance representing the
	 *            secDNS:keyTag to use for this instance.
	 */
	public EPPSecDNSExtDsData(Integer keyTag) {
		setKeyTag(keyTag);
	}

	/**
	 * Append all data from this secDNS:dsData to the given DOM Document
	 * 
	 * @param aDocument
	 *            The DOM Document to append data to
     * @return Encoded DOM <code>Element</code>
	 * @throws EPPEncodeException
	 *             Thrown when errors occur during the encode attempt or if the
	 *             instance is invalid.
	 */

	public Element encode(Document aDocument) throws EPPEncodeException {

		try {
			// Validate States
			validateState();
		}
		catch (EPPCodecException e) {
			cat.error("EPPSecDNSExtDsData.encode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException("EPPSecDNSExtDsData invalid state: "
					+ e);
		}

		// secDNS:dsData
		Element root = aDocument.createElementNS(EPPSecDNSExtFactory.NS,
				ELM_NAME);

		// secDNS:keyTag
		EPPUtil.encodeString(aDocument, root, keyTag + "",
				EPPSecDNSExtFactory.NS, ELM_KEY_TAG);

		// secDNS:alg
		EPPUtil.encodeString(aDocument, root, alg + "", EPPSecDNSExtFactory.NS,
				ELM_ALG);

		// secDNS:digestType
		EPPUtil.encodeString(aDocument, root, digestType + "",
				EPPSecDNSExtFactory.NS, ELM_DIGEST_TYPE);

		// secDNS:digest
		EPPUtil.encodeString(aDocument, root, digest, EPPSecDNSExtFactory.NS,
				ELM_DIGEST);

		// secDNS:pubKey
		EPPUtil.encodeComp(aDocument, root, keyData);

		return root;
	}

	/**
	 * Populate the data of this instance with the data stored in the given
	 * Element of the DOM tree
	 * 
	 * @param aElement
	 *            The root element of the fragment of XML
	 * @throws EPPDecodeException
	 *             Thrown if any errors occur during decoding.
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		Integer keyTagInt = EPPUtil.decodeInteger(aElement,
				EPPSecDNSExtFactory.NS, ELM_KEY_TAG);
		if (keyTagInt == null) {
			keyTag = UNSPEC_INT;
		}
		else {
			keyTag = keyTagInt.intValue();
		}

		Integer algInt = EPPUtil.decodeInteger(aElement,
				EPPSecDNSExtFactory.NS, ELM_ALG);
		if (algInt == null) {
			alg = UNSPEC_INT;
		}
		else {
			alg = algInt.intValue();
		}

		Integer digestTypeInt = EPPUtil.decodeInteger(aElement,
				EPPSecDNSExtFactory.NS, ELM_DIGEST_TYPE);
		if (digestTypeInt == null) {
			digestType = UNSPEC_INT;
		}
		else {
			digestType = digestTypeInt.intValue();
		}

		setDigest(EPPUtil.decodeString(aElement, EPPSecDNSExtFactory.NS,
				ELM_DIGEST));

		keyData = (EPPSecDNSExtKeyData) EPPUtil.decodeComp(aElement,
				EPPSecDNSExtFactory.NS, EPPSecDNSExtKeyData.ELM_NAME,
				EPPSecDNSExtKeyData.class);
	}

	/**
	 * implements a deep <code>EPPSecDNSExtDsData</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPSecDNSExtDsData</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPSecDNSExtDsData)) {
			return false;
		}

		EPPSecDNSExtDsData theComp = (EPPSecDNSExtDsData) aObject;

		// keyTag
		if (keyTag != theComp.keyTag) {
			return false;
		}

		// alg
		if (alg != theComp.alg) {
			return false;
		}

		// digestType
		if (digestType != theComp.digestType) {
			return false;
		}

		// digest
		if (!((digest == null) ? (theComp.digest == null) : digest
				.equals(theComp.digest))) {
			return false;
		}

		// keyData
		if (!((keyData == null) ? (theComp.keyData == null) : keyData
				.equals(theComp.keyData))) {
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPSecDNSExtDsData</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *             Thrown if the instance is in an invalid state
	 */
	void validateState() throws EPPCodecException {
		// keyTag: required unsignedShort
		if (keyTag == UNSPEC_ALG) {
			throw new EPPCodecException(
					"EPPSecDNSExtDsData required element keyTag is not set");
		}
		else if ((keyTag < MIN_KEY_TAG) && (keyTag > MAX_KEY_TAG)) {
			throw new EPPCodecException("EPPSecDNSExtDsData keyTag of "
					+ keyTag + " is out of range, must be between "
					+ MIN_KEY_TAG + " and " + MAX_KEY_TAG);

		}

		// alg: required unsignedByte
		if (alg == UNSPEC_ALG) {
			throw new EPPCodecException(
					"EPPSecDNSExtDsData required element alg is not set");
		}
		else if ((alg < MIN_ALG) && (alg > MAX_ALG)) {
			throw new EPPCodecException("EPPSecDNSExtDsData alg of " + alg
					+ " is out of range, must be between " + MIN_ALG + " and "
					+ MAX_ALG);

		}

		// digestType: required unsignedByte
		if (digestType == UNSPEC_ALG) {
			throw new EPPCodecException(
					"EPPSecDNSExtDsData required element digestType is not set");
		}
		else if ((digestType < MIN_DIGEST_TYPE)
				&& (digestType > MAX_DIGEST_TYPE)) {
			throw new EPPCodecException("EPPSecDNSExtDsData digestType of "
					+ digestType + " is out of range, must be between "
					+ MIN_DIGEST_TYPE + " and " + MAX_DIGEST_TYPE);

		}

		// digest: required hexBinary
		if (digest == null) {
			throw new EPPCodecException(
					"EPPSecDNSExtDsData required attribute digest is not set");
		}
		else {
			// validate hexBinary digest?
		}

		// keyData: optional EPPSecDNSExtKeyData

	}

	/**
	 * Clone <code>EPPSecDNSExtDsData</code>.
	 * 
	 * @return clone of <code>EPPSecDNSExtDsData</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPSecDNSExtDsData clone = null;

		clone = (EPPSecDNSExtDsData) super.clone();

		clone.keyTag = keyTag;
		clone.alg = alg;
		clone.digestType = digestType;
		clone.digest = digest;
		clone.keyData = keyData;

		return clone;
	}

	/**
	 * Get secDNS:keyTag value
	 * 
	 * @return an <code>int</code> value representing secDNS:keyTag
	 * @see <code>UNSPEC_KEY_TAG</code>
	 */
	public int getKeyTag() {
		return keyTag;
	}

	/**
	 * Set secDNS:keyTag value
	 * 
	 * @param keyTag
	 *            an <code>int</code> value representing secDNS:keyTag
	 */
	public void setKeyTag(int keyTag) {
		this.keyTag = keyTag;
	}

	/**
	 * Set secDNS:keyTag value
	 * 
	 * @param keyTag
	 *            an <code>Integer</code> instance representing secDNS:keyTag
	 */
	public void setKeyTag(Integer keyTag) {
		if (keyTag == null) {
			setKeyTag(UNSPEC_KEY_TAG);
		}
		else {
			setKeyTag(keyTag.intValue());
		}

	}

	/**
	 * Get secDNS:alg value
	 * 
	 * @return an <code>int</code> value representing secDNS:alg
	 * @see <code>UNSPEC_ALG</code>
	 */
	public int getAlg() {
		return alg;
	}

	/**
	 * Set secDNS:alg value
	 * 
	 * @param alg
	 *            an <code>int</code> value representing secDNS:alg
	 */
	public void setAlg(int alg) {
		this.alg = alg;
	}

	/**
	 * Get secDNS:digestType value
	 * 
	 * @return an <code>int</code> value representing secDNS:digestType
	 * @see <code>UNSPEC_DIGEST_TYPE</code>
	 */
	public int getDigestType() {
		return digestType;
	}

	/**
	 * Set secDNS:digestType value
	 * 
	 * @param digestType
	 *            an <code>int</code> value representing secDNS:digestType
	 */
	public void setDigestType(int digestType) {
		this.digestType = digestType;
	}

	/**
	 * Get secDNS:digest value
	 * 
	 * @return the canonical representation of the hexBinary secDNS:digest
	 * @see com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData#setDigest(String
	 *      digest)
	 */
	public String getDigest() {
		return digest;
	}

	/**
	 * Set secDNS:digest value This method will remove all whitespace and force
	 * UPPER case in an effort to produce the canonical representation for
	 * hexBinary as defined by http://www.w3.org/TR/xmlschema-2/#hexBinary
	 * 
	 * @param digest
	 *            an <code>String</code> value representing the hexBinary
	 *            secDNS:digest
	 * @see com.verisign.epp.codec.gen.EPPUtil#removeWhitespace(String inString)
	 */
	public void setDigest(String digest) {
		this.digest = EPPUtil.removeWhitespace(digest);
		if (this.digest != null) {
			this.digest = this.digest.toUpperCase();
		}
	}

	/**
	 * Get secDNS:keyData value
	 * 
	 * @return an <code>EPPSecDNSExtKeyData</code> representing secDNS:keyData
	 */
	public EPPSecDNSExtKeyData getKeyData() {
		return keyData;
	}

	/**
	 * Set secDNS:keyData value
	 * 
	 * @param keyData
	 *            an <code>EPPSecDNSExtKeyData</code> value representing
	 *            secDNS:keyData
	 */
	public void setKeyData(EPPSecDNSExtKeyData keyData) {
		this.keyData = keyData;
	}

	/**
	 * Does secDNS:dsData include optional secDNS:keyData?
	 * 
	 * @return true if secDNS:keyData exists (non-null), otherwise false
	 */
	public boolean hasKeyData() {
		if (keyData == null) {
			return false;
		}
		else {
			return true;
		}
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