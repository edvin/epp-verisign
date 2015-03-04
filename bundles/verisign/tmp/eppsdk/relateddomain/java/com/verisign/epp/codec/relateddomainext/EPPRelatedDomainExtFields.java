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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * EPPCodecComponent that encodes and decodes a <relDom:fields> tag
 * <p>
 * Title: EPP 1.0 Related Domain - fields tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtFields object represents the collection
 * of fields that must be synchronized across all the related domains in the
 * family. related domains. As such it is composed of a collection of
 * {@link EPPRelatedDomainExtField} objects. <br/>
 * As XML, it is represented by a <relDom:fields> element containing a number of
 * <relDom:field> elements.
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
public class EPPRelatedDomainExtFields implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtFields.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * Element tag name for the fields
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":fields";

	private final static java.lang.String ELM_IN_SYNC_ATTR = "inSync";

	/**
	 * List of fields represented by the {@link EPPRelatedDomainExtField} to be
	 * associated with the <relDom:fields> element
	 */
	private List<EPPRelatedDomainExtField> fields = null;

	/**
	 * Boolean attribute that defines the fields are synchronized across all of
	 * the related domains
	 */

	private boolean inSync;


	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtFields () {
	}

	/**
	 * Constructor that takes the required <code>inSync</code> attribute value.
	 * 
	 * @param aInSync Are the fields in sync?
	 */
	public EPPRelatedDomainExtFields (boolean aInSync) {
		this.inSync = aInSync;
	}
	
	/**
	 * Constructor that takes both attributes of the <code>EPPRelatedDomainExtFields</code> class including 
	 * the <code>inSync</code> attribute and the <code>fields</code> attribute.
	 * 
	 * @param aFields List of fields, represented with the {@link EPPRelatedDomainExtField}.  
	 * @param aInSync Are the fields in sync?
	 */
	public EPPRelatedDomainExtFields (List<EPPRelatedDomainExtField> aFields, boolean aInSync) {
		this.fields = aFields;
		this.inSync = aInSync;
	}


	/**
	 * A deep clone of the EPPRelatedDomainExtFields.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone () throws CloneNotSupportedException {
		final EPPRelatedDomainExtFields theClone = new EPPRelatedDomainExtFields();

		if ( this.fields != null ) {
			
			theClone.fields = new ArrayList<EPPRelatedDomainExtField>();
			
			for (EPPRelatedDomainExtField field : this.fields) {

				if (field != null) {
					theClone.fields.add((EPPRelatedDomainExtField) field
							.clone());
				}
				else {
					theClone.fields.add(null);
				}

			}
			
		}
		else {
			theClone.fields = null;
		}

		return theClone;
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
				EPPUtil.decodeCompList( aElement, EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtField.ELM_FIELD_NAME,
						EPPRelatedDomainExtField.class );
		this.inSync = EPPUtil.decodeBooleanAttr( aElement, ELM_IN_SYNC_ATTR );
	}


	/**
	 * Append all data from this EPPRelatedDomainExtFields to the given DOM
	 * Document
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
					+ " in EPPRelatedDomainExtFields.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( final EPPCodecException e ) {
			cat
					.error( "EPPRelatedDomainExtFields.encode(): Invalid state on encode: "
							+ e );
			throw new EPPEncodeException( "EPPRelatedDomainExtFields invalid state: "
					+ e );
		}

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		final String inSyncVal = this.inSync ? "true" : "false";
		root.setAttribute( ELM_IN_SYNC_ATTR, inSyncVal );
		// dsData
		EPPUtil.encodeCompList( aDocument, root, this.fields );
		return root;
	}


	/**
	 * A deep comparison of this with another EPPRelatedDomainExtFields.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals ( final Object aObj ) {

		if ( !(aObj instanceof EPPRelatedDomainExtFields) ) {
			return false;
		}

		final EPPRelatedDomainExtFields theComp = (EPPRelatedDomainExtFields) aObj;

		if ( !EPPUtil.equalLists( this.fields, theComp.fields ) ) {
			cat.error( "EPPRelatedDomainExtFields.equals(): fields not equal" );
			return false;
		}

		return true;
	}



	/**
	 * Adds a field represented by
	 * {@link EPPRelatedDomainExtField} to the list of fields.
	 * 
	 * @param aField Field to add to the list.
	 */
	public void addField(EPPRelatedDomainExtField aField) {
		if (this.fields == null) {
			this.fields = new ArrayList<EPPRelatedDomainExtField>();
		}
		
		this.fields.add(aField);
	}
	
	/**
	 * Are there any fields included in the list of fields?
	 * 
	 * @return <code>true</code> if the field list is not <code>null</code> and
	 *         there is at least one field in the list; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasFields() {
		if (this.fields != null && this.fields.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Returns the fields
	 * 
	 * @return the fields
	 */
	public List<EPPRelatedDomainExtField> getFields () {
		return this.fields;
	}



	/**
	 * Sets fields value to fields
	 * 
	 * @param fields
	 *        the fields to set
	 */
	public void setFields ( final List<EPPRelatedDomainExtField> fields ) {
		this.fields = fields;
	}


	/**
	 * Returns the inSync
	 * 
	 * @return the inSync
	 */
	public boolean isInSync () {
		return this.inSync;
	}

	
	/**
	 * Sets inSync value to aInSync
	 * 
	 * @param aInSync
	 *        the inSync to set
	 */
	public void setInSync ( final boolean aInSync ) {
		this.inSync = aInSync;
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
	 * Validate the state of the <code>EPPRelatedDomainExtFields</code> instance. A
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
		if ( (this.fields == null) || (this.fields.size() == 0) ) {
			throw new EPPCodecException(
					"EPPRelatedDomainExtFields contains no  elements." );
		}

	}

}
