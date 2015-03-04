/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

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

package com.verisign.epp.codec.whowas;

import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents a single history record for the given entity name or identifier.
 * This class is represented by &lt;whowas:rec&gt; element and is returned as
 * child element of &lt;whowas:history&gt; element. Each history record will
 * have transaction date, name, new name, roid, operation, client id and client
 * name attributes.
 * 
 * @author Deepak Deshpande
 * @version 1.0
 */
public class EPPWhoWasRecord implements EPPCodecComponent {

	/** The WhoWas record operation transaction date. */
	private Date transactionDate = null;

	/** The WhoWas record name. */
	private String name = null;

	/** The WhoWas record new name, if any. */
	private String newName = null;

	/** The WhoWas entity Registry Object IDentifier (roid). */
	private String roid = null;

	/** The WhoWas record operation name. */
	private String operation = null;

	/** The WhoWas record client ID performing the operation. */
	private String clientID = null;

	/** The WhoWas record client name for the client ID. */
	private String clientName = null;

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPWhoWasRecord.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	
	/**
	 * Default constructor
	 */
	public EPPWhoWasRecord () {
		// default constructor
	}


	/**
	 * Constructs {@link EPPWhoWasRecord} with passed in parameters.
	 * 
	 * @param aName
	 *        - Record Name
	 * @param aRoid
	 *        - Record Roid
	 * @param aTransactionDate
	 *        - Record Transaction Date
	 * @param aOperation
	 *        - Record Operation Name
	 * @param aClientID
	 *        - Record Client ID
	 * @param aClientName
	 *        - Record client name for the client ID.
	 */
	public EPPWhoWasRecord ( String aName, String aRoid, Date aTransactionDate,
			String aOperation, String aClientID, String aClientName ) {
		this( aName, aRoid, null, aTransactionDate, aOperation, aClientID,
				aClientName );
	}


	/**
	 * Constructs {@link EPPWhoWasRecord} with passed in parameters.
	 * 
	 * @param aName
	 *        - Record Name
	 * @param aRoid
	 *        - Record Roid
	 * @param aNewName
	 *        - Record New Name if any
	 * @param aTransactionDate
	 *        - Record Transaction Date
	 * @param aOperation
	 *        - Record Operation Name
	 * @param aClientID
	 *        - Record Client ID
	 * @param aClientName
	 *        - Record client name for the client ID.
	 */
	public EPPWhoWasRecord ( String aName, String aRoid, String aNewName,
			Date aTransactionDate, String aOperation, String aClientID,
			String aClientName ) {
		this.name = aName;
		this.roid = aRoid;
		this.newName = aNewName;
		this.transactionDate = aTransactionDate;
		this.operation = aOperation;
		this.clientID = aClientID;
		this.clientName = aClientName;
	}


	/**
	 * Decodes the {@link EPPWhoWasRecord} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *        - Root DOM Element to decode {@link EPPWhoWasRecord} from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		this.transactionDate =
				EPPUtil.decodeTimeInstant( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC_DATE );

		this.name =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_NAME );

		this.newName =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC_NEW_NAME );

		this.roid =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_ROID );

		this.operation =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC_OP );
		this.clientID =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC_CLID );

		this.clientName =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC_CLNAME );
	}


	/**
	 * Returns the Root DOM Element tree from the attributes of the
	 * {@link EPPWhoWasRecord} instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return the Root DOM Element representing the {@link EPPWhoWasRecord}
	 *         instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode {@link EPPWhoWasRecord} instance.
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		// Required name or roid choice attributes not set?
		if ( this.name == null && this.roid == null ) {
			throw new EPPEncodeException(
					"EPPWhoWasRecord name or roid attributes must be set" );
		}

		Element root =
				aDocument.createElementNS( EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC );

		EPPUtil.encodeTimeInstant( aDocument, root, this.transactionDate,
				EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_REC_DATE );

		EPPUtil.encodeString( aDocument, root, this.name, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_NAME );

		EPPUtil.encodeString( aDocument, root, this.newName,
				EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_REC_NEW_NAME );

		EPPUtil.encodeString( aDocument, root, this.roid, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_ROID );

		EPPUtil.encodeString( aDocument, root, this.operation,
				EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_REC_OP );

		EPPUtil.encodeString( aDocument, root, this.clientID,
				EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_REC_CLID );

		EPPUtil.encodeString( aDocument, root, this.clientName,
				EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_REC_CLNAME );

		return root;
	}


	/**
	 * Does a deep clone of the {@link EPPWhoWasRecord} instance.
	 * 
	 * @return Cloned instance
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPWhoWasRecord clone = (EPPWhoWasRecord) super.clone();

		clone.name = this.name;
		clone.roid = this.roid;
		clone.newName = this.newName;
		clone.transactionDate = this.transactionDate;
		clone.operation = this.operation;
		clone.clientID = this.clientID;
		clone.clientName = this.clientName;

		return clone;
	}


	/**
	 * Compares two {@link EPPWhoWasRecord} instances.
	 * 
	 * @return <code>true</code> if equal;<code>false</code> otherwise.
	 */
	public boolean equals ( Object o ) {

		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {

			EPPWhoWasRecord other = (EPPWhoWasRecord) o;

			if ( !EqualityUtil.equals( this.transactionDate, other.transactionDate ) ) {
				cat.error("EPPWhoWasRecord.equals(): transactionDate not equal");
				return false;
			}

			if ( !EqualityUtil.equals( this.name, other.name ) ) {
				cat.error("EPPWhoWasRecord.equals(): name not equal");
				return false;
			}

			if ( !EqualityUtil.equals( this.newName, other.newName ) ) {
				cat.error("EPPWhoWasRecord.equals(): newName not equal");
				return false;
			}

			if ( !EqualityUtil.equals( this.roid, other.roid ) ) {
				cat.error("EPPWhoWasRecord.equals(): roid not equal");
				return false;
			}

			if ( !EqualityUtil.equals( this.operation, other.operation ) ) {
				cat.error("EPPWhoWasRecord.equals(): operation not equal");
				return false;
			}

			if ( !EqualityUtil.equals( this.clientID, other.clientID ) ) {
				cat.error("EPPWhoWasRecord.equals(): clientID not equal");
				return false;
			}

			if ( !EqualityUtil.equals( this.clientName, other.clientName ) ) {
				cat.error("EPPWhoWasRecord.equals(): clientName not equal");
				return false;
			}

			return true;
		}
		else {
			cat.error("EPPWhoWasRecord.equals(): not EPPDomainInfoResp instance");
			return false;
		}
	}


	/**
	 * Returns the transactionDate
	 * 
	 * @return the transactionDate
	 */
	public Date getTransactionDate () {
		return this.transactionDate;
	}


	/**
	 * Sets transactionDate value to <code>aTransactionDate</code>.
	 * 
	 * @param aTransactionDate
	 *        the transactionDate to set
	 */
	public void setTransactionDate ( Date aTransactionDate ) {
		this.transactionDate = aTransactionDate;
	}


	/**
	 * Returns the name
	 * 
	 * @return the name
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Sets name value to <code>aName</code>.
	 * 
	 * @param aName
	 *        the name to set
	 */
	public void setName ( String aName ) {
		this.name = aName;
	}


	/**
	 * Returns the newName
	 * 
	 * @return the newName
	 */
	public String getNewName () {
		return this.newName;
	}


	/**
	 * Sets newName value to <code>aNewName</code>
	 * 
	 * @param aNewName
	 *        the newName to set
	 */
	public void setNewName ( String aNewName ) {
		this.newName = aNewName;
	}


	/**
	 * Returns the Registry Object IDentifier (roid).
	 * 
	 * @return the roid
	 */
	public String getRoid () {
		return this.roid;
	}


	/**
	 * Sets Registry Object IDentifier (roid) value to <code>aRoid</code>.
	 * 
	 * @param aRoid
	 *        the roid to set
	 */
	public void setRoid ( String aRoid ) {
		this.roid = aRoid;
	}


	/**
	 * Returns the operation
	 * 
	 * @return the operation
	 */
	public String getOperation () {
		return this.operation;
	}


	/**
	 * Sets operation value to <code>aOperation</code>.
	 * 
	 * @param aOperation
	 *        the operation to set
	 */
	public void setOperation ( String aOperation ) {
		this.operation = aOperation;
	}


	/**
	 * Returns the clientID
	 * 
	 * @return the clientID
	 */
	public String getClientID () {
		return this.clientID;
	}


	/**
	 * Sets clientID value to <code>aClientID</code>.
	 * 
	 * @param aClientID
	 *        the clientID to set
	 */
	public void setClientID ( String aClientID ) {
		this.clientID = aClientID;
	}


	/**
	 * Returns the clientName
	 * 
	 * @return the clientName
	 */
	public String getClientName () {
		return this.clientName;
	}


	/**
	 * Sets clientName value to <code>aClientName</code>.
	 * 
	 * @param aClientName
	 *        the clientName to set
	 */
	public void setClientName ( String aClientName ) {
		this.clientName = aClientName;
	}
}
