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

package com.verisign.epp.codec.coaext;

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
 * EPPCodecComponent that encodes and decodes a COA Attr Tag.
 * <p>
 * Title: EPP 1.0 Client Object Attribute - Attr
 * </p>
 * <p>
 * Description: Each EPPCoaExtAttr object represents a single Client Object Attribute. As
 * such it contains a single key-value pair, represented by one EPPCoaExtKey and
 * one EPPCoaExtValue element. <br/>
 * As XML, is is represented by a <coa:attr> element
 * containing a single <coa:key> element and a single <coa:value> element.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: VeriSign
 * </p>
 * 
 * @author jfaust
 * @version 1.0
 */
public class EPPCoaExtAttr implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat =
			Logger.getLogger( EPPCoaExtAttr.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Constant for the key tag
	 */
	public static final String ELM_NAME = "coa:attr";

	/**
	 * The key in this key/value pair.
	 */
	private EPPCoaExtKey key = null;

	/**
	 * The value in this key/value pair.
	 */
	private EPPCoaExtValue value = null;


	/**
	 * Default constructor
	 */
	public EPPCoaExtAttr () {
	}


	/**
	 * Convenience constructor specifying the key and value as arguments.
	 * 
	 * @param aKey
	 * @param aValue
	 */
	public EPPCoaExtAttr ( String aKey, String aValue ) {
		this.key = new EPPCoaExtKey( aKey );
		this.value = new EPPCoaExtValue( aValue );
	}


	/**
	 * A deep clone of the EPPCoaExtAttr.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPCoaExtAttr theClone = (EPPCoaExtAttr) super.clone();

		if ( this.getKey() != null ) {
			theClone.setKey( (EPPCoaExtKey) this.getKey().clone() );
		}

		if ( this.getValue() != null ) {
			theClone.setValue( (EPPCoaExtValue) this.getValue().clone() );
		}

		return theClone;
	}


	/**
	 * A deep comparison of this to another EPPCoaExtAttr.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals ( Object aComp ) {

		if ( !(aComp instanceof EPPCoaExtAttr) ) {
			return false;
		}

		EPPCoaExtAttr theComp = (EPPCoaExtAttr) aComp;
		
		if (this.getKey() == null) {
			return false;
		}

		if ( !this.getKey().equals( theComp.getKey() ) ) {
			return false;
		}

		if ( this.getValue() == null && theComp.getValue() != null) {
			return false;
		}
		
		if ( !this.getValue().equals( theComp.getValue() ) ) {
			return false;
		}

		return true;
	}


	/**
	 * Decode the EPPCoaExtAttr element by decoding its <coa:key> and <coa:value>
	 * subelements.
	 * 
	 * @see com.verisign.epp.codec.gen.EPPCodecComponent#decode(org.w3c.dom.Element)
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		this.key =
				(EPPCoaExtKey) EPPUtil.decodeComp( aElement, EPPCoaExtFactory.NS,
						EPPCoaExtKey.ELM_NAME, EPPCoaExtKey.class );

		this.value =
				(EPPCoaExtValue) EPPUtil.decodeComp( aElement, EPPCoaExtFactory.NS,
						EPPCoaExtValue.ELM_NAME, EPPCoaExtValue.class );

	}


	/**
	 * Encode an EPPCoaExtAttr by encoding its EPPCoaExtKey and EPPCoaExtValue
	 * attributes.
	 * 
	 * @see com.verisign.epp.codec.gen.EPPCodecComponent#encode(org.w3c.dom.Document)
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {
		
		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " in EPPCoaExtAttr.encode(Document)" );
		}
		
		try {
			// Validate States
			validateState();
		}
		catch ( EPPCodecException e ) {
			cat.error( "EPPCoaExtAttr.encode(): Invalid state on encode: " + e );
			throw new EPPEncodeException( "EPPCoaExtAttr invalid state: " + e );
		}

		// coa:attr
		Element root = aDocument.createElementNS( EPPCoaExtFactory.NS, ELM_NAME );

		root.appendChild( this.key.encode( aDocument ) );

		root.appendChild( this.value.encode( aDocument ) );

		return root;
	}


	/**
	 * Validate the state of the <code>EPPCoaExtAttr</code> instance. A valid
	 * state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( this.key == null ) {
			throw new EPPCodecException(
					"EPPCoaExtAttr required element key is not set" );
		}

		if ( this.value == null ) {
			throw new EPPCodecException(
					"EPPCoaExtAttr required element value is not set" );
		}

	}


	/**
	 * Get method for the EPPCoaExtKey attribute.
	 * @return EPPCoaExtKey
	 */
	public EPPCoaExtKey getKey () {
		return key;
	}


	/**
	 * Set method for the EPPCoaExtKey attribute.
	 * @param aKey
	 */
	public void setKey ( EPPCoaExtKey aKey ) {
		this.key = aKey;
	}


	/**
	 * Get method for the EPPCoaExtValue attribute.
	 * @return EPPCoaExtValue
	 */
	public EPPCoaExtValue getValue () {
		return value;
	}


	/**
	 * Set method for the EPPCoaExtValue attribute.
	 * @param aValue
	 */
	public void setValue ( EPPCoaExtValue aValue ) {
		this.value = aValue;
	}

}
