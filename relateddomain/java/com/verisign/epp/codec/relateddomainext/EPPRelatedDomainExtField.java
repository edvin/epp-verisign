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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * EPPCodecComponent that encodes and decodes a <relDom:field> tag
 * <p>
 * Title: EPP 1.0 Related Domain - field tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtField has two attributes. The inSync
 * boolean attribute specifies whether or not the name attribute is in
 * synchronized with the rest of the related domains in the family. <br/>
 * As XML, it is represented by a <relDom:field> element with two attributes:
 * name and inSync
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
public class EPPRelatedDomainExtField implements
		com.verisign.epp.codec.gen.EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtField.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * 
	 */
	private static final long serialVersionUID = 8458235783317620385L;

	/** XML Element Name of <code>EPPRelatedDomainExtField</code> root element. */
	final static java.lang.String ELM_FIELD_NAME = "relDom:field";

	/** XML attribute name for the <code>name</code> element. */
	private final static java.lang.String ELM_FIELD_NAME_ATTR = "name";

	/** XML attribute name for the <code>inSync</code> element. */
	private final static java.lang.String ELM_IN_SYNC_ATTR = "inSync";

	/**
	 * The name of the field
	 */
	private String name;

	/**
	 * Boolean attribute that defines the field is synchronized across all of the
	 * related domains
	 */
	private boolean inSync;


	/**
	 * <code>EPPRelatedDomainExtField</code> default constructor
	 */

	public EPPRelatedDomainExtField () {

	}


	/**
	 * <code>EPPRelatedDomainExtField</code> constructor that accepts the name of
	 * the field and the boolean inSync attribute.
	 * 
	 * @param aFieldName
	 *        Name of the field
	 * @param aInSync
	 *        Boolean attribute that defines the field is synchronized across all
	 *        of the related domains
	 */
	public EPPRelatedDomainExtField ( final String aFieldName,
			final boolean aInSync ) {
		this.name = aFieldName;
		this.inSync = aInSync;
	}


	/**
	 * A deep clone of the EPPRelatedDomainExtField
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone () throws CloneNotSupportedException {
		EPPRelatedDomainExtField clone = null;

		clone = (EPPRelatedDomainExtField) super.clone();

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
		this.name = aElement.getAttribute( ELM_FIELD_NAME_ATTR );
		this.inSync = EPPUtil.decodeBooleanAttr( aElement, ELM_IN_SYNC_ATTR );
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPRelatedDomainExtField instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return Element - Root DOM Element representing the EPPDomainPeriod
	 *         instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode EPPDomainPeriod instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS,
						ELM_FIELD_NAME );

		// Check the name attribute
		if ( this.name == null ) {
			throw new EPPEncodeException(
					"EPPRelatedDomainExtField: name should not be null" );
		}
		else {
			// add attribute here
			root.setAttribute( ELM_FIELD_NAME_ATTR, this.name );
		}

		// set the inSync attribute
		final String inSyncVal = this.inSync ? "true" : "false";
		root.setAttribute( ELM_IN_SYNC_ATTR, inSyncVal );

		return root;
	}


	/**
	 * implements a deep <code>EPPRelatedDomainExtField</code> compare.
	 * 
	 * @param obj
	 *        <code>EPPRelatedDomainExtField</code> instance to compare with
	 * @return true if the objects are equal
	 */
	public boolean equals ( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( !(obj instanceof EPPRelatedDomainExtField) ) {
			return false;
		}
		final EPPRelatedDomainExtField other = (EPPRelatedDomainExtField) obj;

		if ( !EqualityUtil.equals( this.name, other.name ) ) {
			cat.error( "EPPRelatedDomainExtField.equals(): name not equal" );
			return false;
		}
		if ( !EqualityUtil.equals( this.inSync, other.inSync ) ) {
			cat.error( "EPPRelatedDomainExtField.equals(): inSync not equal" );
			return false;
		}
		return true;
	}


	/**
	 * Returns the inSync attribute
	 * 
	 * @return the inSync attribute
	 */
	public boolean getInSync () {
		return this.inSync;
	}


	/**
	 * Returns the name of the filed
	 * 
	 * @return the name of the field
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Sets inSync attribute
	 * 
	 * @param aInSync
	 *        the inSync attribute to set
	 */
	public void setInSync ( final boolean aInSync ) {
		this.inSync = aInSync;
	}


	/**
	 * Sets aName value to name attribute
	 * 
	 * @param aName
	 *        the name of the field to set
	 */
	public void setName ( final String aName ) {
		this.name = aName;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	public String toString () {
		return EPPUtil.toString( this );
	}

}
