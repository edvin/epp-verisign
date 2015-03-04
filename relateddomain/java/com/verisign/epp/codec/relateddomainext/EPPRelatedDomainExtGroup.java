/**************************************************************************
 *                                                                        *
 * The information in this document is proprietary to VeriSign, Inc.      *
 * It may not be used, reproduced or disclosed without the written        *
 * approval of VeriSign.                                                  *
 *                                                                        *
 * VERISIGN PROPRIETARY & CONFIDENTIAL INFORMATION                        *
 *                                                                        *
 *                                                                        *
 * Copyright (c) 2011 VeriSign, Inc.  All rights reserved.                *
 *                                                                        *
 *************************************************************************/

package com.verisign.epp.codec.relateddomainext;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * EPPCodecComponent that encodes and decodes a <relDom:group> tag
 * <p>
 * Title: EPP 1.0 Related Domain - group tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtGroup object represents the collection of
 * domains that are available for registration in a family of related domains.
 * As such it is composed of {@link EPPRelatedDomainExtFields},
 * {@link EPPRelatedDomainExtAvailable} and
 * {@link EPPRelatedDomainExtRegistered} objects. <br/>
 * As XML, it is represented by a <relDom:available> element containing a number
 * of <relDom:name> elements.
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
public class EPPRelatedDomainExtGroup implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtGroup.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * Element tag name for the group
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":group";
	
	/**
	 * Relationship of domains is based on Top Level Domain (TLD) relationship. 
	 */
	public static final String TYPE_TLD = "tld";
	
	/**
	 * Relationship of domains is based on variant relationship within a Top Level Domain (TLD).
	 */
	public static final String TYPE_VARIANT = "variant";

	/**
	 * The type of the group element.
	 */
	private final static java.lang.String ELM_TYPE_ATTR = "type";
	
	/**
	 * The type of the group element.
	 */
	private String type;
	
	/**
	 *  The fields in the group
	 */
	private EPPRelatedDomainExtFields fields;
	
	/**
	 *  The domain names available for registration in the related domain family
	 */
	private EPPRelatedDomainExtAvailable available;
	
	/**
	 *  The domain names registered in the related domain family
	 */
	private EPPRelatedDomainExtRegistered registered;


	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtGroup () {
	}

	/**
	 * Constructor that takes the required attributes including the 
	 * <code>type</code> and the <code>fields</code>.
	 * 
	 * @param aType
	 *            What type of group is it? One of the <code>TYPE</code>
	 *            constants may be used.
	 * @param aFields The field information for the group
	 */
	public EPPRelatedDomainExtGroup(String aType, EPPRelatedDomainExtFields aFields) {
		this.type = aType;
		this.fields = aFields;
	}


	/**
	 * Constructor that takes all of the attributes as parameters.
	 * 
	 * @param aType
	 *            What type of group is it? One of the <code>TYPE</code>
	 *            constants may be used.
	 * @param aFields
	 *            The field information for the group
	 * @param aAvailable
	 *            The list of available related names for the group.
	 * @param aRegistered
	 *            The list of registered related name for the group.
	 */
	public EPPRelatedDomainExtGroup(String aType,
			EPPRelatedDomainExtFields aFields,
			EPPRelatedDomainExtAvailable aAvailable,
			EPPRelatedDomainExtRegistered aRegistered) {
		this.type = aType;
		this.fields = aFields;
		this.available = aAvailable;
		this.registered = aRegistered;
	}

	/**
	 * A deep clone of the EPPRelatedDomainAvailable
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone () throws CloneNotSupportedException {
		final EPPRelatedDomainExtGroup clone =
				(EPPRelatedDomainExtGroup) super.clone();

		if ( this.fields != null ) {
			clone.fields = (EPPRelatedDomainExtFields) this.fields.clone();
		}

		if ( this.available != null ) {
			clone.available = (EPPRelatedDomainExtAvailable) this.available.clone();
		}

		if ( this.registered != null ) {
			clone.registered =
					(EPPRelatedDomainExtRegistered) this.registered.clone();
		}

		return clone;
	}


	/**
	 * Populate the data of this instance with the data stored in the given
	 * Element of the DOM tree
	 * 
	 * @param aElement
	 *        The root element of the report fragment of XML
	 * @throws EPPDecodeException
	 *         Thrown if any errors occur during decoding.
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {

		this.fields =
				(EPPRelatedDomainExtFields) EPPUtil.decodeComp( aElement,
						EPPRelatedDomainExtFactory.NS, EPPRelatedDomainExtFields.ELM_NAME,
						EPPRelatedDomainExtFields.class );
		this.registered =
				(EPPRelatedDomainExtRegistered) EPPUtil.decodeComp( aElement,
						EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtRegistered.ELM_NAME,
						EPPRelatedDomainExtRegistered.class );
		this.available =
				(EPPRelatedDomainExtAvailable) EPPUtil.decodeComp( aElement,
						EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtAvailable.ELM_NAME,
						EPPRelatedDomainExtAvailable.class );
		this.type = aElement.getAttribute( ELM_TYPE_ATTR );
	}


	/**
	 * Append all data from this group to the given DOM Document
	 * 
	 * @param aDocument
	 *        The DOM Document to append data to
	 * @return Encoded DOM <code>Element</code>
	 * @throws EPPEncodeException
	 *         Thrown when errors occur during the encode attempt or if the
	 *         instance is invalid.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " in EPPRelatedDomainExtAvailable.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( final EPPCodecException e ) {
			cat
					.error( "EPPRelatedDomainExtAvailable.encode(): Invalid state on encode: "
							+ e );
			throw new EPPEncodeException(
					"EPPRelatedDomainExtAvailable invalid state: " + e );
		}

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		// dsData
		EPPUtil.encodeComp( aDocument, root, this.fields );
		EPPUtil.encodeComp( aDocument, root, this.registered );
		EPPUtil.encodeComp( aDocument, root, this.available );
		root.setAttribute( ELM_TYPE_ATTR, this.type );
		return root;
	}


	public boolean equals ( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( !(obj instanceof EPPRelatedDomainExtGroup) ) {
			return false;
		}
		final EPPRelatedDomainExtGroup other = (EPPRelatedDomainExtGroup) obj;
		if ( !EqualityUtil.equals( this.available, other.available ) ) {
			cat.error( "EPPRelatedDomainExtGroup.equals(): available not equal" );
			return false;
		}
		if ( !EqualityUtil.equals( this.fields, other.fields ) ) {
			cat.error( "EPPRelatedDomainExtGroup.equals(): fields not equal" );
			return false;
		}

		if ( !EqualityUtil.equals( this.registered, other.registered ) ) {
			cat.error( "EPPRelatedDomainExtGroup.equals(): registered not equal" );
			return false;
		}
		if ( !EqualityUtil.equals( this.type, other.type ) ) {
			cat.error( "EPPRelatedDomainExtGroup.equals(): type not equal" );
			return false;
		}

		return true;
	}


	/**
	 * Returns the available
	 * 
	 * @return the available
	 */
	public EPPRelatedDomainExtAvailable getAvailable () {
		return this.available;
	}


	/**
	 * Returns the fields
	 * 
	 * @return the fields
	 */
	public EPPRelatedDomainExtFields getFields () {
		return this.fields;
	}


	/**
	 * Returns the registered
	 * 
	 * @return the registered
	 */
	public EPPRelatedDomainExtRegistered getRegistered () {
		return this.registered;
	}


	/**
	 * Returns the type
	 * 
	 * @return the type
	 */
	public String getType () {
		return this.type;
	}


	/**
	 * Sets available value to aAvailable
	 * 
	 * @param aAvailable
	 *        the available to set
	 */
	public void setAvailable ( final EPPRelatedDomainExtAvailable aAvailable ) {
		this.available = aAvailable;
	}


	/**
	 * Sets fields value to aFields
	 * 
	 * @param aFields
	 *        the fields to set
	 */
	public void setFields ( final EPPRelatedDomainExtFields aFields ) {
		this.fields = aFields;
	}


	/**
	 * Sets registered value to aRegistered
	 * 
	 * @param aRegistered
	 *        the registered to set
	 */
	public void setRegistered ( final EPPRelatedDomainExtRegistered aRegistered ) {
		this.registered = aRegistered;
	}


	/**
	 * Sets type value to aType
	 * 
	 * @param aType
	 *        the type to set
	 */
	public void setType ( final String aType ) {
		this.type = aType;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	@Override
	public String toString() {
		return EPPUtil.toString(this);
	}


	/**
	 * Validate the state of the <code>EPPRelatedDomainExtGroup</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( this.type == null ) {
			throw new EPPCodecException( "Required attribute type is null." );
		}
		if ( this.fields == null ) {
			throw new EPPCodecException( "Required attribute fields is null." );
		}

	}

}
