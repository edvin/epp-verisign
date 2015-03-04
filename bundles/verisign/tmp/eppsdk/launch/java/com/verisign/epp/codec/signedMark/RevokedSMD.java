/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2013 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */

package com.verisign.epp.codec.signedMark;

import java.util.Date;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * A revoked Signed Mark Data (SMD) that includes the attributes:<br>
 * <ul>
 * <li>Identifier of the revoked signed mark.</li>
 * <li>Revocation datetime in UTC of the signed mark.</li>
 * </ul>
 */
public class RevokedSMD implements java.io.Serializable, Cloneable {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(RevokedSMD.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Identifier of the revoked signed mark
	 */
	private String id;

	/**
	 * Revocation datetime in UTC of the SMD.
	 */
	private Date revokedDate;

	/**
	 * Default constructor. The <code>id</code> and the <code>revokedDate</code>
	 * attributes must be set prior to calling <code>encode()</code>.
	 */
	public RevokedSMD() {
	}

	/**
	 * Constructor that takes the required <code>id</code> and
	 * <code>revokedDate</code> attribute values.
	 * 
	 * @param aId
	 *            Identifier of the revoked signed mark.
	 * @param aRevokedDate
	 *            Revocation datetime of the signed mark.
	 */
	public RevokedSMD(String aId, Date aRevokedDate) {
		this.id = aId;
		this.revokedDate = aRevokedDate;
	}

	/**
	 * Gets the identifier of the revoked signed mark.
	 * 
	 * @return Identifier of the revoked signed mark.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the identifier of the revoked signed mark.
	 * 
	 * @param aId
	 *            Identifier of the revoked signed mark.
	 */
	public void setId(String aId) {
		this.id = aId;
	}

	/**
	 * Gets the revocation datetime of the signed mark.
	 * 
	 * @return Revocation datetime of the signed mark.
	 */
	public Date getRevokedDate() {
		return this.revokedDate;
	}

	/**
	 * Sets the revocation datetime of the signed mark.
	 * 
	 * @param aRevokedDate
	 *            Revocation datetime of the signed mark.
	 */
	public void setRevokedDate(Date aRevokedDate) {
		this.revokedDate = aRevokedDate;
	}

	/**
	 * Encodes the revoked SMD attributes into a revoked SMD line.
	 * 
	 * @return Encoded revoked SMD line
	 * 
	 * @throws EPPEncodeException
	 *             Error encoding the SMD line.
	 */
	public String encode() throws EPPEncodeException {
		cat.debug("encode(): enter");

		if (this.id == null) {
			throw new EPPEncodeException(
					"encode(): id required attribute is null.");
		}
		if (this.revokedDate == null) {
			throw new EPPEncodeException(
					"encode(): revokedDate required attribute is null.");
		}

		String ret = this.id + ","
				+ EPPUtil.encodeTimeInstant(this.revokedDate);

		cat.debug("encode(): Encoded value = \"" + ret + "\"");

		cat.debug("encode(): exit");
		return ret;
	}

	public void decode(String aLine) throws EPPDecodeException {
		cat.debug("decode(String): enter");

		int sepIndex = aLine.indexOf(',');

		if (sepIndex == -1) {
			throw new EPPDecodeException(
					"decode(): , seperator not found in revoked SMD line");
		}

		this.id = aLine.substring(0, sepIndex);

		String revokedDateStr = aLine.substring(sepIndex + 1);
		this.revokedDate = EPPUtil.decodeTimeInstant(revokedDateStr);

		if (this.revokedDate == null) {
			throw new EPPDecodeException(
					"decode(): Error decoding the revoked date");

		}

		cat.debug("decode(String): exit");
	}

	/**
	 * Clone <code>RevokedSMD</code>.
	 * 
	 * @return clone of <code>RevokedSMD</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		RevokedSMD clone = (RevokedSMD) super.clone();

		return clone;
	}

	/**
	 * implements a deep <code>RevokedSMD</code> compare.
	 * 
	 * @param aObject
	 *            <code>RevokedSMD</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof RevokedSMD)) {
			cat.error("RevokedSMD.equals(): aObject is not an RevokedSMD");
			return false;
		}

		RevokedSMD other = (RevokedSMD) aObject;

		// Id
		if (!EqualityUtil.equals(this.id, other.id)) {
			cat.error("RevokedSMD.equals(): id not equal");
			return false;
		}

		// Revoked Date
		if (!EqualityUtil.equals(this.revokedDate, other.revokedDate)) {
			cat.error("RevokedSMD.equals(): revokedDate not equal");
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in
	 * encoding the revoked SMD attributes into a revoked SMD line. If there is
	 * an error encoding the SMD line, a <code>RuntimeException</code> is
	 * thrown.
	 * 
	 * @return Encoded revoked SMD line
	 */
	public String toString() {

		try {
			return this.encode();
		}
		catch (EPPEncodeException e) {
			throw new RuntimeException(e);
		}

	}

}
