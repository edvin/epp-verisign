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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class that holds the attributes for a Signed Mark Data (SMD) Revocation List
 * and provides the following:<br>
 * <ol>
 * <li>Encode the SMD Revocation List to a <code>String</code>, that can be
 * written to a file.
 * <li>Decode the SMD Revocation List from a <code>String</code> or an
 * <code>InputStream</code>.
 * <li>Check if a specific {@link EPPSignedMark} is revoked.
 * </ol>
 */
public class SMDRevocationList {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(SMDRevocationList.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Default value of the <code>version</code> attribute.
	 */
	public static final int DEFAULT_VERSION = 1;

	/**
	 * Value of the header line (second line in file).
	 */
	private static final String HEADER_LINE = "smd-id,insertion-datetime";

	/**
	 * Version of the SMD Revocation List format.
	 */
	private int version = DEFAULT_VERSION;

	/**
	 * Datetime in UTC that the SMD Revocation List was created.
	 */
	private Date createdDate;

	/**
	 * List of revoked SMD's
	 */
	private List<RevokedSMD> revokedSMDs = new ArrayList<RevokedSMD>();

	/**
	 * Default constructor. The createdDate must be set prior to calling the
	 * <code>encode()</code> method.
	 */
	public SMDRevocationList() {
	}

	/**
	 * Constructor that takes the requirement attribute value. The version
	 * defaults to the <code>DEFAULT_VERSION</code> value, and the removed SMD
	 * list defaults to an empty list.
	 * 
	 * @param aCreatedDate
	 *            Datetime in UTC that the SMD Revocation List was created.
	 */
	public SMDRevocationList(Date aCreatedDate) {
		this.createdDate = aCreatedDate;
	}

	/**
	 * Constructor that takes the requirement created date attribute value and
	 * the optional list of revoked SMD's. The version defaults to the
	 * <code>DEFAULT_VERSION</code> value.
	 * 
	 * @param aCreatedDate
	 *            Datetime in UTC that the SMD Revocation List was created.
	 * @param aRevokedSMDs
	 *            List of revoked SMD's
	 */
	public SMDRevocationList(Date aCreatedDate, List<RevokedSMD> aRevokedSMDs) {
		this.createdDate = aCreatedDate;
		this.setRevokedSMDs(revokedSMDs);
	}

	/**
	 * Gets the version of the SMD Revocation List format.
	 * 
	 * @return Version of the SMD Revocation List format with the default of
	 *         <code>DEFAULT_VERSION</code>.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * Sets the version of the SMD Revocation List format.
	 * 
	 * @param aVersion
	 *            Version of the SMD Revocation List format
	 */
	public void setVersion(int aVersion) {
		this.version = aVersion;
	}

	/**
	 * Gets the datetime in UTC that the SMD Revocation List was created.
	 * 
	 * @return Datetime in UTC that the SMD Revocation List was created.
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	 * Sets the datetime in UTC that the SMD Revocation List was created.
	 * 
	 * @param aCreatedDate
	 *            Datetime in UTC that the SMD Revocation List was created.
	 */
	public void setCreatedDate(Date aCreatedDate) {
		this.createdDate = aCreatedDate;
	}

	/**
	 * Gets the list of revoked SMD's.
	 * 
	 * @return List of revoked SMD's
	 */
	public List<RevokedSMD> getRevokedSMDs() {
		return this.revokedSMDs;
	}

	/**
	 * Sets the list of revoked SMD's.
	 * 
	 * @param aRevokedSMDs
	 *            List of revoked SMD's
	 */
	public void setRevokedSMDs(List<RevokedSMD> aRevokedSMDs) {
		if (aRevokedSMDs == null) {
			this.revokedSMDs = new ArrayList<RevokedSMD>();
		}
		else {
			this.revokedSMDs = aRevokedSMDs;
		}
	}

	/**
	 * Adds a revoked SMD to the list of revoked SMD's.
	 * 
	 * @param aRevokedSMD
	 *            Revoked SMD to add to the list of revoked SMD's
	 */
	public void addRevokedSMD(RevokedSMD aRevokedSMD) {
		this.revokedSMDs.add(aRevokedSMD);
	}

	/**
	 * Is the passed signed mark revoked?
	 * 
	 * @param aSignedMark
	 *            Signed mark to check if revoked.
	 * 
	 * @return <code>true</code> if the signed mark is revoked;
	 *         <code>false</code> otherwise.
	 */
	public boolean isRevoked(EPPSignedMark aSignedMark) {
		cat.debug("isRevoked(EPPSignedMark): enter");

		String signedMarkId = aSignedMark.getId();

		for (RevokedSMD currSMD : this.revokedSMDs) {

			if (currSMD.getId().equals(signedMarkId)) {
				cat.debug("isRevoked(EPPSignedMark): Signed Mark Id = "
						+ signedMarkId + " is revoked");
				cat.debug("isRevoked(EPPSignedMark): exit");
				return true;
			}
		}

		cat.debug("isRevoked(EPPSignedMark): Signed Mark Id = " + signedMarkId
				+ " is not revoked");
		cat.debug("isRevoked(EPPSignedMark): exit");
		return false;
	}

	/**
	 * Encodes the SMD Revocation List to a <code>String</code>.
	 * 
	 * @return Encoded revoked SMD Revocation List
	 * 
	 * @throws EPPEncodeException
	 *             Error encoding the SMD Revocation List.
	 */
	public String encode() throws EPPEncodeException {
		cat.debug("encode(): enter");

		if (this.createdDate == null) {
			throw new EPPEncodeException(
					"encode(): create date required attribute is null.");
		}

		StringBuffer strBuffer = new StringBuffer();

		// Add first line with version and created date
		strBuffer.append(Integer.toString(this.version));
		strBuffer.append(',');
		strBuffer.append(EPPUtil.encodeTimeInstant(this.createdDate));
		strBuffer.append('\n');

		// Add second header line
		strBuffer.append(HEADER_LINE);
		strBuffer.append('\n');

		for (RevokedSMD currRevokedSMD : this.revokedSMDs) {
			strBuffer.append(currRevokedSMD.encode());
			strBuffer.append('\n');
		}

		cat.debug("encode(): exit");
		return strBuffer.toString();
	}

	/**
	 * Decodes the SMD Revocation List from a <code>String</code>.
	 * 
	 * @param aSMDRevocationListStr
	 *            String containing the full SMD Revocation List.
	 * 
	 * @throws EPPDecodeException
	 *             Error decoding the SMD Revocation List
	 */
	public void decode(String aSMDRevocationListStr) throws EPPDecodeException {
		this.decode(new ByteArrayInputStream(aSMDRevocationListStr.getBytes()));
	}

	/**
	 * Decodes the SMD Revocation List from an <code>InputStream</code>.
	 * 
	 * @param aSMDRevocationListStream
	 *            <code>InputStream</code> containing the full SMD Revocation
	 *            List.
	 * 
	 * @throws EPPDecodeException
	 *             Error decoding the SMD Revocation List
	 */
	public void decode(InputStream aSMDRevocationListStream)
			throws EPPDecodeException {
		cat.debug("decode(InputStream): enter");

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(aSMDRevocationListStream));
		String currLine;

		try {
			// Read version and created date line
			currLine = bufferedReader.readLine();

			if (currLine == null) {
				throw new EPPDecodeException(
						"decode(InputStream): Empty SMD revocation list.");
			}

			// Parse version and created date
			int sepIndex = currLine.indexOf(',');

			if (sepIndex == -1) {
				throw new EPPDecodeException(
						"decode(InputStream): , seperator first line of SMD revocation list");
			}

			// Version
			String versionStr = currLine.substring(0, sepIndex);
			this.version = Integer.parseInt(versionStr);

			// Created Date
			String createdDateStr = currLine.substring(sepIndex + 1);
			this.createdDate = EPPUtil.decodeTimeInstant(createdDateStr);

			// Read header line
			currLine = bufferedReader.readLine();

			if (currLine == null) {
				throw new EPPDecodeException(
						"decode(InputStream): Missing SMD revocation list header line.");
			}

			if (!currLine.equals(HEADER_LINE)) {
				throw new EPPDecodeException(
						"decode(InputStream): Invalid or missing SMD revocation list header line: "
								+ currLine);
			}

			// Read the revoked SMD's

			while ((currLine = bufferedReader.readLine()) != null) {
				RevokedSMD revokedSMD = new RevokedSMD();

				revokedSMD.decode(currLine);

				this.addRevokedSMD(revokedSMD);
			}
		}
		catch (IOException e) {
			throw new EPPDecodeException("Error reading SMD: " + e);
		}

		cat.debug("decode(String): exit");
	}

	/**
	 * Clone <code>SMDRevocationList</code>.
	 * 
	 * @return clone of <code>SMDRevocationList</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		SMDRevocationList clone = (SMDRevocationList) super.clone();

		return clone;
	}

	/**
	 * implements a deep <code>SMDRevocationList</code> compare.
	 * 
	 * @param aObject
	 *            <code>SMDRevocationList</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof SMDRevocationList)) {
			cat.error("SMDRevocationList.equals(): aObject is not an SMDRevocationList");
			return false;
		}

		SMDRevocationList other = (SMDRevocationList) aObject;

		// Version
		if (!EqualityUtil.equals(this.version, other.version)) {
			cat.error("SMDRevocationList.equals(): version not equal");
			return false;
		}

		// Created Date
		if (!EqualityUtil.equals(this.createdDate, other.createdDate)) {
			cat.error("SMDRevocationList.equals(): createdDate not equal");
			return false;
		}

		// Revoked SMD's
		if (!EqualityUtil.equals(this.revokedSMDs, other.revokedSMDs)) {
			cat.error("EPPMark.equals(): revokedSMDs not equal");
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in the
	 * full SMD revocation list being converted to a <code>String</code>. If
	 * there is an error encoding the Revocation List, a
	 * <code>RuntimeException</code> is thrown.
	 * 
	 * @return Encoded SMD Revocation List
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
