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
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.util.EPPCatFactory;

/**
 * EPPCodecComponent that encodes and decodes a COA Value Tag.
 * <p>
 * Title: EPP 1.0 Client Object Attribute - Value
 * </p>
 * <p>
 * Description: The Value tag represents the value of a Client Object Attribute.
 * It contains simply the Client Object Attribute value as text, and is only
 * used in the company of a Key tag which identifies it.
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
public class EPPCoaExtValue implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat =
			Logger.getLogger( EPPCoaExtValue.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Constant for the COA value tag
	 */
	public static final String ELM_NAME = "coa:value";

	/**
	 * The value to a key/value pair.
	 */
	private String value;


	/**
	 * Create an EPPCoaExtValue instance
	 */
	public EPPCoaExtValue () {
	}


	/**
	 * Create a EPPCoaExtValue intance with the given value
	 * 
	 * @param aValue
	 *        the value
	 */
	public EPPCoaExtValue ( String aValue ) {
		value = aValue;
	}


	/**
	 * Clone <code>EPPCoaExtValue</code>.
	 * 
	 * @return clone of <code>EPPCoaExtValue</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPCoaExtValue clone = null;

		clone = (EPPCoaExtValue) super.clone();
		clone.setValue( this.getValue() );

		return clone;
	}


	/**
	 * Sets all this instance's data in the given XML document
	 * 
	 * @param aDocument
	 *        a DOM Document to attach data to.
	 * @return The root element of this component.
	 * @throws EPPEncodeException
	 *         Thrown if any errors prevent encoding.
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {
		
		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " in EPPCoaExtValue.encode(Document)" );
		}
		
		try {
			// Validate States
			validateState();
		}
		catch ( EPPCodecException e ) {
			cat.error( "EPPCoaExtValue.encode(): Invalid state on encode: " + e );
			throw new EPPEncodeException( "EPPCoaExtValue invalid state: " + e );
		}

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPCoaExtValue.encode(Document)" );
		}

		Element root = aDocument.createElementNS( EPPCoaExtFactory.NS, ELM_NAME );

		// encode the string

		Text valueText = aDocument.createTextNode( value );

		root.appendChild( valueText );

		return root;
	}


	/**
	 * Decode the EPPCoaExtValue component
	 * 
	 * @param aElement
	 * @throws EPPDecodeException
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		Text valueText = (Text) aElement.getFirstChild();

		value = (valueText == null) ? null : valueText.getData();

	}


	/**
	 * Validate the state of the <code>EPPCoaExtValue</code> instance. A valid state
	 * means that all of the required attributes have been set. If validateState
	 * returns without an exception, the state is valid. If the state is not
	 * valid, the <code>EPPCodecException</code> will contain a description of the
	 * error. throws EPPCodecException State error. This will contain the name of
	 * the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	void validateState () throws EPPCodecException {
		// don't need to validate anything.
	}


	/**
	 * implements a deep <code>EPPCoaExtValue</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPCoaExtValue</code> instance to compare with
	 * @return true if equal false otherwise
	 */
	public boolean equals ( Object aObject ) {

		if ( !(aObject instanceof EPPCoaExtValue) ) {
			return false;
		}
		EPPCoaExtValue theComp = (EPPCoaExtValue) aObject;

		return (value.equalsIgnoreCase( theComp.getValue() ));
	}


	/**
	 * Returns the value to a key/value pair.
	 * 
	 * @return the value
	 */
	public String getValue () {
		return value;
	}


	/**
	 * Sets the Value
	 * 
	 * @param aValue
	 *        - The value to a key/value pair
	 */
	public void setValue ( String aValue ) {
		this.value = aValue;
	}

}