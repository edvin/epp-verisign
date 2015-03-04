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
package com.verisign.epp.codec.gen;


// Log4j Imports
import org.apache.log4j.Logger;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
import java.util.HashMap;

import com.verisign.epp.util.EPPCatFactory;


/**
 * Describe data collection purposes, data recipients, and data retention. Each
 * &lt;statement&gt; element MUST contain a &lt;purpose&gt; element, a
 * &lt;recipient&gt; element, and a &lt;retention&gt; element <br>
 * <br>
 * Use methods <code>getPurpose</code> and <code>setPurpose </code> to get and
 * set the purpose element(s), Use <code>getRecipient</code> and
 * <code>setRecipient</code> to get and set the recipient element.  Use
 * methods <code>getRetention></code> and <code>setRetention</code> to get and
 * set the retention element.
 *
 * @author $Author: jim $
 * @version $Revision: 1.6 $
 *
 * @see com.verisign.epp.codec.gen.EPPDcp
 */
public class EPPStatement implements EPPCodecComponent {
	/**
	 * Constant used with the Retention attribute that means data persists per
	 * business practices.
	 */
	public static final short RETENTION_BUSINESS = 0;

	/**
	 * Constant used with the Retention attribute that means data persists
	 * indefinitely.
	 */
	public static final short RETENTION_INDEFINITE = 1;

	/**
	 * Constant used with the Retention attribute that means data persists per
	 * legal requirements.
	 */
	public static final short RETENTION_LEGAL = 2;

	/**
	 * Constant used with the Retention attribute that means Data is not
	 * persistent, and is not retained for more than a brief period of time
	 * necessary to make use of it during the course of a single online
	 * interaction
	 */
	public static final short RETENTION_NONE = 3;

	/**
	 * Constant used with the Retention attribute that means Data persists to
	 * meet the stated purpose
	 */
	public static final short RETENTION_STATED = 4;

	/** Look up retention constant given retention value tag name */
	private static HashMap retentionElmHash;

	/** Default XML root tag name for <code>statement</code>element. */
	final static String ELM_NAME = "statement";

	/** Default XML root tag name for <code>retention</code>element. */
	private static final String ELM_RETENTION = "retention";

	/** XML tag name for the rentention <code>business</code> element. */
	private static final String ELM_RETENTION_BUSINESS = "business";

	/** XML tag name for the rentention <code>indefinite</code> element. */
	private static final String ELM_RETENTION_INDEFINITE = "indefinite";

	/** XML tag name for the rentention <code>legal</code> element. */
	private static final String ELM_RETENTION_LEGAL = "legal";

	/** XML tag name for the rentention <code>none</code> element. */
	private static final String ELM_RETENTION_NONE = "none";

	/** XML tag name for the rentention <code>stated</code> element. */
	private static final String ELM_RETENTION_STATED = "stated";

	/**
	 * Elements that match the <code>RETENTION_</code> constant values of the
	 * Access attribute.
	 */
	private final static String[] retentionElms =
	{
		ELM_RETENTION_BUSINESS, ELM_RETENTION_INDEFINITE, ELM_RETENTION_LEGAL,
		ELM_RETENTION_NONE, ELM_RETENTION_STATED
	};

	static {
		// Setup access element hash
		retentionElmHash = new HashMap();

		retentionElmHash.put(EPPUtil.getLocalName(ELM_RETENTION_BUSINESS),
				new Short(RETENTION_BUSINESS));
		retentionElmHash.put(EPPUtil.getLocalName(ELM_RETENTION_INDEFINITE),
				new Short(RETENTION_INDEFINITE));
		retentionElmHash.put(EPPUtil.getLocalName(ELM_RETENTION_LEGAL),
				new Short(RETENTION_LEGAL));
		retentionElmHash.put(EPPUtil.getLocalName(ELM_RETENTION_NONE),
				new Short(RETENTION_NONE));
		retentionElmHash.put(EPPUtil.getLocalName(ELM_RETENTION_STATED),
				new Short(RETENTION_STATED));
	}

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPStatement.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** <code>EPPPurpose</code> instances. */
	private EPPPurpose purpose = null;

	/** <code>EPPRecipient</code> instances. */
	private EPPRecipient recipient = null;

	/** Describes data retention practices. */
	private short retention = RETENTION_BUSINESS;

	/**
	 * Default constructor.
	 */
	public EPPStatement() {
	}

	// End EPPStatement.EPPStatement()

	/**
	 * Allocates a new <code>EPPStatement</code> and sets all of the required
	 * attributes to the arguments values.
	 *
	 * @param aRecipient Describes the recipients of collected data
	 * @param aPurpose Describe the purposes for which data is collected
	 * @param aRetention Describes data retention practices using one of the
	 * 		  <code>RETENTION_</code> constants.
	 */
	public EPPStatement(
						EPPRecipient aRecipient, EPPPurpose aPurpose,
						short aRetention) {
		this.recipient     = aRecipient;
		this.purpose	   = aPurpose;
		this.retention     = aRetention;
	}

	/*
	 * Gets the Recipient object associated with the EPPStatement object.
	 *
	 * @return Recipients associated with the collected data.
	 */
	public EPPRecipient getRecipient() {
		return this.recipient;
	}

	/*
	 * Sets the Recipient object,and associate it with the <code>EPPStatement</code> object
	 *
	 * @param aRecipient Describes the recipients of collected data
	 */
	public void setRecipient(EPPRecipient aRecipient) {
		this.recipient = aRecipient;
	}

	/*
	 * Gets the purpose object associated with the <code>EPPStatement</code> object.
	 *
	 * @return Purposes for which data is collected
	 */
	public EPPPurpose getPurpose() {
		return this.purpose;
	}

	/*
	 * Sets the Purpose object,and associate it with the <code>EPPStatement</code> object
	 *
	 * @param aPurpose Describe the purposes for which data is collected
	 */
	public void setPurpose(EPPPurpose apurpouse) {
		this.purpose = apurpouse;
	}

	/*
	 * Gets the data retention policy.
	 *
	 * @return One of the <code>RETENTION_</code> constants.
	 */
	public short getRetention() {
		return this.retention;
	}

	/*
	 * Sets the data retention policy.
	 *
	 * @param aRetention One of the <code>RETENTION_</code> constants.
	 */
	public void setRetention(short aRetention) {
		this.retention = aRetention;
	}

	/**
	 * encode <code>EPPStatement</code> into a DOM element tree.  The
	 * "statment"     element is created and the attribute nodes are appended
	 * as children.
	 *
	 * @param aDocument DOM Document being built
	 *
	 * @return statement root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element theElm = null;
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Validate that all required attributes are set.
		if ((this.purpose == null) || (this.recipient == null)) {
			cat.error("EPPStatement.encode(): purpose, recipient, and retention must not be null");
			throw new EPPEncodeException("EPPStatement.encode(): purpose, recipient, and retention must not be null");
		}

		EPPUtil.encodeComp(aDocument, root, this.purpose);

		EPPUtil.encodeComp(aDocument, root, this.recipient);

		// Access
		if ((retention < 0) || (retention >= retentionElms.length)) {
			cat.error("EPPStatement.encode(): Unknown retention value of "
					  + retention);
			throw new EPPEncodeException("EPPStatement.encode(): Unknown retention value of "
										 + retention);
		}

		Element theRetentionElm =
			aDocument.createElementNS(EPPCodec.NS, ELM_RETENTION);
		root.appendChild(theRetentionElm);
		theElm =
			aDocument.createElementNS(EPPCodec.NS, retentionElms[retention]);
		theRetentionElm.appendChild(theElm);

		return root;
	}

	// End EPPStatement.encode(Document)

	/**
	 * decode <code>EPPStatement</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the &ltstatement&gt element
	 *
	 * @param aElement root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		Element theElm = null;

		// Purpose
		this.purpose =
			(EPPPurpose) EPPUtil.decodeComp(
											aElement, EPPCodec.NS,
											EPPPurpose.ELM_NAME,
											EPPPurpose.class);

		// Recipient
		this.recipient =
			(EPPRecipient) EPPUtil.decodeComp(
											  aElement, EPPCodec.NS,
											  EPPRecipient.ELM_NAME,
											  EPPRecipient.class);

		// Retention
		theElm = EPPUtil.getElementByTagNameNS(aElement, EPPCodec.NS, ELM_RETENTION);

		if (theElm == null) {
			cat.error("EPPStatement.decode(): could not find retention element");
			throw new EPPDecodeException("EPPStatement could not find retention element");
		}

		Element theRetentionValueElm = EPPUtil.getFirstElementChild(theElm);

		if (theRetentionValueElm == null) {
			cat.error("EPPStatement.decode(): could not find retention element value");
			throw new EPPDecodeException("EPPStatement could not find retention element value");
		}

		Short theRetentionValue =
			(Short) retentionElmHash.get(theRetentionValueElm.getLocalName());

		if (theRetentionValue == null) {
			cat.error("EPPStatement.decode(): could not find valid retention element value");
			throw new EPPDecodeException("EPPStatement could not find valid retention element value");
		}

		this.retention = theRetentionValue.shortValue();
	}

	// End EPPStatement.decode(Element)

	/**
	 * implements a <code>EPPStatment</code> compare.
	 *
	 * @param aObject <code>EPPStatement</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPStatement)) {
			cat.error("EPPStatement.equals(): " + aObject.getClass().getName()
					  + " not EPPStatement instance");

			return false;
		}

		EPPStatement theStatment = (EPPStatement) aObject;

		// Purpose
		if (
			!(
					(this.purpose == null) ? (theStatment.purpose == null)
											   : this.purpose.equals(theStatment.purpose)
				)) {
			cat.error("EPPStatement.equals(): purpose not equal");

			return false;
		}

		// Recipient
		if (
			!(
					(this.recipient == null) ? (theStatment.recipient == null)
												 : this.recipient.equals(theStatment.recipient)
				)) {
			cat.error("EPPStatement.equals(): recipient not equal");

			return false;
		}

		// Retention
		if (this.retention != theStatment.retention) {
			cat.error("EPPStatement.equals(): retention not equal");

			return false;
		}

		return true;
	}

	// End EPPStatement.equals(Object)

	/**
	 * Clone <code>EPPStatement</code>.
	 *
	 * @return clone of <code>EPPStatement</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPStatement clone = null;
		clone     = (EPPStatement) super.clone();

		clone.purpose	    = (EPPPurpose) this.purpose.clone();
		clone.recipient     = (EPPRecipient) this.recipient.clone();
		clone.retention     = this.retention;

		return clone;
	}

	// End EPPStatement.clone()*/

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

	// End EPPStatement.toString()
}


// End class EPPStatement
