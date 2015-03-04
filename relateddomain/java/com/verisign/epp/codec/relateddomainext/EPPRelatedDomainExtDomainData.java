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

package com.verisign.epp.codec.relateddomainext;

import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * EPPCodecComponent that encodes and decodes a <relDom:domain> tag sent in a
 * response. This element contains a number of child elements. Not all of them
 * are required for all the responses listed below in which this element is
 * used.
 * <ul>
 * <li>Domain Create Response
 * <li>Domain Delete Response
 * <li>Domain Renew Response
 * <li>Domain Transfer Response
 * </ul>
 * <p>
 * Title: EPP 1.0 Related Domain - domain tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtDomainData object represents the
 * collection of domains that had been processed atomically. As XML, it is
 * represented by a <relDom:domain> element.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @author nchigurupati
 * @version 1.0
 */
public class EPPRelatedDomainExtDomainData implements EPPCodecComponent {
	
	/**
	 * Constant for the delete result when the domain is deleted.
	 */
	public final static String DELETE_DELETED = "deleted";
	
	/**
	 * 
	 */
	public final static String DELETE_PENDING_DELETE = "pendingDelete";
	
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPRelatedDomainExtDomainData.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );
	/**
	 * 
	 */
	private static final long serialVersionUID = 3151298616180707607L;

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_DOMAIN_NAME = "relDom:name";

	/**
	 * XML Element Name of <code>EPPRelatedDomainExtDomainData</code> root
	 * element.
	 */
	final static String ELM_NAME = "relDom:domain";

	/** XML tag name for the <code>requestClient</code> attribute. */
	private final static String ELM_REQUEST_CLIENT = "relDom:reID";

	/** XML tag name for the <code>actionClient</code> attribute. */
	private final static String ELM_ACTION_CLIENT = "relDom:acID";

	/** XML tag name for the <code>transferStatus</code> attribute. */
	private final static String ELM_TRANSFER_STATUS = "relDom:trStatus";

	/** XML tag name for the <code>requestDate</code> attribute. */
	private final static String ELM_REQUEST_DATE = "relDom:reDate";

	/** XML tag name for the <code>actionDate</code> attribute. */
	private final static String ELM_ACTION_DATE = "relDom:acDate";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_EXPIRATION_DATE = "relDom:exDate";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_CREATED_DATE = "relDom:crDate";

	/** XML tag name for the <code>expirationDate</code> attribute. */
	private final static String ELM_RESULT = "relDom:result";

	/** The fully qualified domain name. */
	private String name = null;

	/** The identifier of the client that initiated the transfer request. */
	private String requestClient = null;

	/**
	 * The identifier of the client that SHOULD respond to the transfer request.
	 */
	private String actionClient = null;

	/**
	 * The state of the most recent transfer request. This should be one of the
	 * <code>EPPRelatedDomainExtDomainData.STATUS</code> constants.
	 */
	private String transferStatus = null;

	/** The date and time that the transfer was requested. */
	private Date requestDate = null;

	/**
	 * The date and time of a required or completed response. For a STATUS_PENDING
	 * request, the value identifies the date and time by which a response is
	 * required before an automated response action MUST be taken by the server.
	 * For all other status types, the value identifies the date and time when the
	 * request was completed.
	 */
	private Date actionDate = null;

	/**
	 * An optional attribute that contains the end of the domain's validity period
	 * if the transfer command caused or causes a change in the validity period.
	 */
	private Date expirationDate = null;

	/**
	 * Domain Creation Date.
	 */
	private Date createdDate = null;

	/**
	 * The result of domain deletion.
	 */
	private String deleteResult = null;


	/**
	 * <code>EPPRelatedDomainExtDomainData</code> default constructor. Must call
	 * required setter methods before invoking <code>encode</code>, which
	 * <i>may</i> include:<br>
	 * <br>
	 * <ul>
	 * <li>name - <code>setName</code></li>
	 * <li>request client - <code>setRequestClient</code></li>
	 * <li>action client - <code>setActionClient</code></li>
	 * <li>transfer status - <code>setTransferStatus</code></li>
	 * <li>request date - <code>setReqeustDate</code></li>
	 * <li>action date - <code>setActionDate</code></li>
	 * <li>expiration date - <code>setExpirationDate</code></li>
	 * <li>created date - <code>setCreatedDate</code></li>
	 * <li>deletion result - <code>setDeleteResult</code></li>
	 * </ul>
	 */
	public EPPRelatedDomainExtDomainData () {
		// Values set in attribute definition.
	}


	/**
	 * Constructor which takes the name of domain.
	 * 
	 * @param aName
	 */
	public EPPRelatedDomainExtDomainData ( final String aName ) {
		this.name = aName;
	}


	/**
	 * Constructor which takes the name of domain, creation date and expiration
	 * date
	 * 
	 * @param aName
	 * @param aCreationDate
	 * @param aExpirationDate
	 */
	public EPPRelatedDomainExtDomainData ( final String aName,
			final Date aCreationDate, final Date aExpirationDate ) {
		this.name = aName;
		this.createdDate = aCreationDate;
		this.expirationDate = aExpirationDate;
	}


	/**
	 * Constructor which takes the name of domain and expiration date
	 * 
	 * @param aName
	 * @param aExpirationDate
	 */
	public EPPRelatedDomainExtDomainData ( final String aName,
			final Date aExpirationDate ) {
		this.name = aName;
		this.expirationDate = aExpirationDate;
	}


	/**
	 * Constructor which takes the name of domain and deletion result
	 * 
	 * @param aName
	 * @param aDeleteResult
	 */
	public EPPRelatedDomainExtDomainData ( final String aName,
			final String aDeleteResult ) {
		this.name = aName;
		this.deleteResult = aDeleteResult;
	}


	/**
	 * Constructor which takes the name of domain, transfer status, request
	 * client, request date, action client, action date and expiration date.
	 * 
	 * @param aName
	 * @param aTransferStatus
	 * @param aRequestClient
	 * @param aRequestDate
	 * @param aActionClient
	 * @param aActionDate
	 * @param aExpirationDate
	 */
	public EPPRelatedDomainExtDomainData ( final String aName,
			final String aTransferStatus, final String aRequestClient,
			final Date aRequestDate, final String aActionClient,
			final Date aActionDate, final Date aExpirationDate ) {
		this.name = aName;
		this.transferStatus = aTransferStatus;
		this.requestClient = aRequestClient;
		this.requestDate = aRequestDate;
		this.actionClient = aActionClient;
		this.actionDate = aActionDate;
		this.expirationDate = aExpirationDate;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRelatedDomainExtDomainData</code> instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built. Used as an Element factory.
	 * @return Root DOM Element representing the
	 *         <code>EPPRelatedDomainExtDomainData</code> instance.
	 * @exception EPPEncodeException
	 *            Unable to encode <code>EPPRelatedDomainExtDomainData</code>
	 *            instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		// Name
		if ( hasName() ) {
			EPPUtil.encodeString( aDocument, root, this.name,
					EPPRelatedDomainExtFactory.NS, ELM_DOMAIN_NAME );
		}

		if ( hasDeleteResult() ) {
			EPPUtil.encodeString( aDocument, root, this.deleteResult,
					EPPRelatedDomainExtFactory.NS, ELM_RESULT );
		}

		if ( hasTransferStatus() ) {
			// Transfer Status
			EPPUtil.encodeString( aDocument, root, this.transferStatus,
					EPPRelatedDomainExtFactory.NS, ELM_TRANSFER_STATUS );
		}

		// Request Client
		if ( hasRequestClient() ) {
			EPPUtil.encodeString( aDocument, root, this.requestClient,
					EPPRelatedDomainExtFactory.NS, ELM_REQUEST_CLIENT );
		}

		// Request Date
		if ( hasRequestDate() ) {
			EPPUtil.encodeTimeInstant( aDocument, root, this.requestDate,
					EPPRelatedDomainExtFactory.NS, ELM_REQUEST_DATE );
		}

		// Action Client
		if ( hasActionClient() ) {
			EPPUtil.encodeString( aDocument, root, this.actionClient,
					EPPRelatedDomainExtFactory.NS, ELM_ACTION_CLIENT );
		}

		// Action Date
		if ( hasActionDate() ) {
			EPPUtil.encodeTimeInstant( aDocument, root, this.actionDate,
					EPPRelatedDomainExtFactory.NS, ELM_ACTION_DATE );
		}

		// Created Date
		if ( hasCreatedDate() ) {
			EPPUtil.encodeTimeInstant( aDocument, root, this.createdDate,
					EPPRelatedDomainExtFactory.NS, ELM_CREATED_DATE );
		}

		// Expiration Date
		if ( hasExpirationDate() ) {
			EPPUtil.encodeTimeInstant( aDocument, root, this.expirationDate,
					EPPRelatedDomainExtFactory.NS, ELM_EXPIRATION_DATE );
		}

		return root;
	}


	/**
	 * Decode the <code>EPPRelatedDomainExtDomainData</code> attributes from the
	 * aElement DOM Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode
	 *        <code>EPPRelatedDomainExtDomainData</code> from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {
		// Name
		this.name =
				EPPUtil.decodeString( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_DOMAIN_NAME );

		this.deleteResult =
				EPPUtil.decodeString( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_RESULT );

		// Transfer Status
		this.transferStatus =
				EPPUtil.decodeString( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_TRANSFER_STATUS );

		// Request Client
		this.requestClient =
				EPPUtil.decodeString( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_REQUEST_CLIENT );

		// Request Date
		this.requestDate =
				EPPUtil.decodeTimeInstant( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_REQUEST_DATE );

		// Action Client
		this.actionClient =
				EPPUtil.decodeString( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_ACTION_CLIENT );

		this.createdDate =
				EPPUtil.decodeTimeInstant( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_CREATED_DATE );
		// Action Date
		this.actionDate =
				EPPUtil.decodeTimeInstant( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_ACTION_DATE );

		// Expiration Date
		this.expirationDate =
				EPPUtil.decodeTimeInstant( aElement, EPPRelatedDomainExtFactory.NS,
						ELM_EXPIRATION_DATE );
	}


	/**
	 * Compare an instance of <code>EPPRelatedDomainExtDomainData</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *        Object to compare with.
	 * @return DOCUMENT ME!
	 */
	@Override
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPRelatedDomainExtDomainData) ) {
			return false;
		}

		final EPPRelatedDomainExtDomainData theComp =
				(EPPRelatedDomainExtDomainData) aObject;

		// Name
		if ( !((this.name == null) ? (theComp.name == null) : this.name
				.equals( theComp.name )) ) {
			return false;
		}

		// Request Client
		if ( !((this.requestClient == null) ? (theComp.requestClient == null)
				: this.requestClient.equals( theComp.requestClient )) ) {
			return false;
		}

		// Action Client
		if ( !((this.actionClient == null) ? (theComp.actionClient == null)
				: this.actionClient.equals( theComp.actionClient )) ) {
			return false;
		}

		// Transfer Status
		if ( !((this.transferStatus == null) ? (theComp.transferStatus == null)
				: this.transferStatus.equals( theComp.transferStatus )) ) {
			return false;
		}

		// Request Date
		if ( !((this.requestDate == null) ? (theComp.requestDate == null)
				: this.requestDate.equals( theComp.requestDate )) ) {
			return false;
		}

		// Action Date
		if ( !((this.actionDate == null) ? (theComp.actionDate == null)
				: this.actionDate.equals( theComp.actionDate )) ) {
			return false;
		}
		// Expiration Date
		if ( !((this.createdDate == null) ? (theComp.createdDate == null)
				: this.createdDate.equals( theComp.createdDate )) ) {
			return false;
		}
		// Expiration Date
		if ( !((this.expirationDate == null) ? (theComp.expirationDate == null)
				: this.expirationDate.equals( theComp.expirationDate )) ) {
			return false;
		}

		return true;
	}


	/**
	 * Clone <code>EPPRelatedDomainExtDomainData</code>.
	 * 
	 * @return clone of <code>EPPRelatedDomainExtDomainData</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	@Override
	public Object clone () throws CloneNotSupportedException {
		final EPPRelatedDomainExtDomainData clone =
				(EPPRelatedDomainExtDomainData) super.clone();

		return clone;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString( this );
	}


	/**
	 * Gets the domain name
	 * 
	 * @return Domain Name if set; <code>null</code> otherwise.
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Sets the domain name.
	 * 
	 * @param aName
	 *        Domain Name
	 */
	public void setName ( final String aName ) {
		this.name = aName;
	}


	/**
	 * Gets the identifier of the client that initiated the transfer request.
	 * 
	 * @return The Request Client Id <code>String</code> if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getRequestClient () {
		return this.requestClient;
	}


	/**
	 * Sets the identifier of the client that initiated the transfer request.
	 * 
	 * @param aRequestClient
	 *        The Request Client Id <code>String</code>
	 */
	public void setRequestClient ( final String aRequestClient ) {
		this.requestClient = aRequestClient;
	}


	/**
	 * Gets the identifier of the client that SHOULD respond to the transfer
	 * request.
	 * 
	 * @return The Request Client Id <code>String</code> if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getActionClient () {
		return this.actionClient;
	}


	/**
	 * Sets the identifier of the client that SHOULD respond to the transfer
	 * request.
	 * 
	 * @param aActionClient
	 *        The Action Client Id <code>String</code>
	 */
	public void setActionClient ( final String aActionClient ) {
		this.actionClient = aActionClient;
	}


	/**
	 * Gets the state of the most recent transfer request. This should be one of
	 * the <code>EPPResponse.TRANSFER</code> constants.
	 * 
	 * @return The transfer status <code>String</code> if defined;
	 *         <code>null</code> otherwise.
	 */
	public String getTransferStatus () {
		return this.transferStatus;
	}


	/**
	 * Sets the state of the most recent transfer request. This should be one of
	 * the <code>EPPResponse.TRANSFER</code> constants.
	 * 
	 * @param aTransferStatus
	 *        The transfer status String (<code>EPPResponse.TRANSFER</code>)
	 */
	public void setTransferStatus ( final String aTransferStatus ) {
		this.transferStatus = aTransferStatus;
	}


	/**
	 * Gets the date and time that the transfer was requested.
	 * 
	 * @return The request date and time if defined; <code>null</code> otherwise.
	 */
	public Date getRequestDate () {
		return this.requestDate;
	}


	/**
	 * Sets the date and time that the transfer was requested.
	 * 
	 * @param aRequestDate
	 *        The request date and time
	 */
	public void setRequestDate ( final Date aRequestDate ) {
		this.requestDate = aRequestDate;
	}


	/**
	 * Gets the date and time of a required or completed response.
	 * 
	 * @return The required or complete response data and time if defined;
	 *         <code>null</code> otherwise.
	 */
	public Date getActionDate () {
		return this.actionDate;
	}


	/**
	 * Sets the date and time of a required or completed response.
	 * 
	 * @param aActionDate
	 *        The required or complete response data and time.
	 */
	public void setActionDate ( final Date aActionDate ) {
		this.actionDate = aActionDate;
	}


	/**
	 * Gets the optional attribute that contains the end of the domain's validity
	 * period if the transfer command caused or causes a change in the validity
	 * period.
	 * 
	 * @return Transfer expiration data and time if defined; <code>null</code>
	 *         otherwise.
	 */
	public Date getExpirationDate () {
		return this.expirationDate;
	}


	/**
	 * Sets the optional attribute that contains the end of the domain's validity
	 * period if the transfer command caused or causes a change in the validity
	 * period.
	 * 
	 * @param aExpirationDate
	 *        Transfer expiration data and time.
	 */
	public void setExpirationDate ( final Date aExpirationDate ) {
		this.expirationDate = aExpirationDate;
	}


	/**
	 * Returns the createdDate
	 * 
	 * @return the createdDate
	 */
	public Date getCreatedDate () {
		return this.createdDate;
	}


	/**
	 * Sets createdDate value to createdDate
	 * 
	 * @param aCreatedDate
	 *        the createdDate to set
	 */
	public void setCreatedDate ( final Date aCreatedDate ) {
		this.createdDate = aCreatedDate;
	}


	/**
	 * Returns the deleteResult
	 * 
	 * @return the deleteResult
	 */
	public String getDeleteResult () {
		return this.deleteResult;
	}


	/**
	 * Sets deleteResult value to deleteResult
	 * 
	 * @param aDeleteResult
	 *        the deleteResult to set
	 */
	public void setDeleteResult ( final String aDeleteResult ) {
		this.deleteResult = aDeleteResult;
	}


	/**
	 * Validate the state of this object when it is part of the Domain-Create
	 * response. The the name of domain and deletion result are required elements
	 * when used with the Domain-Create response.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForCreate () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}
		if ( !hasCreatedDate() ) {
			cat.error( "createdDate required attribute is not set" );
			throw new EPPCodecException( "createdDate required attribute is not set" );
		}
		return true;

	}


	/**
	 * Validate the state of this object when it is part of the Domain-Delete
	 * response. The the name of domain and deletion result are required elements
	 * when used with the Domain-Delete response.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForDelete () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}
		if ( !hasDeleteResult() ) {
			cat.error( "deleteResult required attribute is not set" );
			throw new EPPCodecException( "deleteResult required attribute is not set" );
		}
		return true;

	}


	/**
	 * Validate the state of this object when it is part of the Domain-Renew
	 * response. The the name of domain and expiration date are required elements
	 * when used with the Domain-Renew response.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForRenew () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}

		if ( !hasExpirationDate() ) {
			cat.error( "expirationDate required attribute is not set" );
			throw new EPPCodecException(
					"expirationDate required attribute is not set" );
		}

		return true;

	}


	/**
	 * Validate the state of this object when it is part of the Domain-Transfer
	 * response. The the name of domain, transfer status, request client, request
	 * date, action client and action date are required elements when used with
	 * the Domain-Transfer response.
	 * 
	 * @return
	 * @throws EPPCodecException
	 */
	boolean validateStateForTransfer () throws EPPCodecException {
		if ( !hasName() ) {
			cat.error( "name required attribute is not set" );
			throw new EPPCodecException( "name required attribute is not set" );
		}
		if ( !hasTransferStatus() ) {
			cat.error( "createdDate required attribute is not set" );
			throw new EPPCodecException( "createdDate required attribute is not set" );
		}
		if ( !hasRequestClient() ) {
			cat.error( "createdDate required attribute is not set" );
			throw new EPPCodecException( "createdDate required attribute is not set" );
		}
		if ( !hasRequestDate() ) {
			cat.error( "createdDate required attribute is not set" );
			throw new EPPCodecException( "createdDate required attribute is not set" );
		}
		if ( !hasActionClient() ) {
			cat.error( "createdDate required attribute is not set" );
			throw new EPPCodecException( "createdDate required attribute is not set" );
		}
		if ( !hasActionDate() ) {
			cat.error( "createdDate required attribute is not set" );
			throw new EPPCodecException( "createdDate required attribute is not set" );
		}
		return true;

	}


	/**
	 * @return <code>true</code> if {@linkplain #expirationDate} is not null.
	 */
	public boolean hasExpirationDate () {
		return this.expirationDate != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #actionDate} is not null.
	 */
	public boolean hasActionDate () {
		return this.actionDate != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #actionClient} is not null.
	 */
	public boolean hasActionClient () {
		return this.actionClient != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #requestDate} is not null.
	 */
	public boolean hasRequestDate () {
		return this.requestDate != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #requestClient} is not null.
	 */
	public boolean hasRequestClient () {
		return this.requestClient != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #transferStatus} is not null.
	 */
	public boolean hasTransferStatus () {
		return this.transferStatus != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #deleteResult} is not null.
	 */
	public boolean hasDeleteResult () {
		return this.deleteResult != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #createdDate} is not null.
	 */
	public boolean hasCreatedDate () {
		return this.createdDate != null;
	}


	/**
	 * @return <code>true</code> if {@linkplain #name} is not null.
	 */
	public boolean hasName () {
		return this.name != null;
	}

}
