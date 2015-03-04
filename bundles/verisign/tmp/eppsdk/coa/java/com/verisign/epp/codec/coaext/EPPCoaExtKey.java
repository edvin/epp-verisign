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
 * EPPCodecComponent that encodes and decodes a COA Key Tag.
 * <p>
 * Title: EPP 1.0 Client Object Attribute - Key
 * </p>
 * <p>
 * Description: The Key tag represents the name of a Client Object Attribute. It
 * contains simply the Client Object Attribute name as text.
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
public class EPPCoaExtKey implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat =
			Logger.getLogger( EPPCoaExtKey.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Constant for the key tag
	 */
	public static final String ELM_NAME = "coa:key";

	/**
	 * The key to a key/value pair.
	 */
	private String key;


	/**
	 * Create an EPPCoaExtKey instance
	 */
	public EPPCoaExtKey () {
	}


	/**
	 * Create a EPPCoaExtKey intance with the given key
	 * 
	 * @param aKey
	 *        the key
	 */
	public EPPCoaExtKey ( String aKey ) {
		key = aKey;
	}


	/**
	 * Clone <code>EPPCoaExtKey</code>.
	 * 
	 * @return clone of <code>EPPCoaExtKey</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPCoaExtKey clone = null;

		clone = (EPPCoaExtKey) super.clone();
		clone.setKey( this.getKey() );

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
					+ " in EPPCoaExtKey.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( EPPCodecException e ) {
			cat.error( "EPPCoaExtKey.encode(): Invalid state on encode: " + e );
			throw new EPPEncodeException( "EPPCoaExtKey invalid state: " + e );
		}

		Element root = aDocument.createElementNS( EPPCoaExtFactory.NS, ELM_NAME );

		// encode the string

		Text keyText = aDocument.createTextNode( key );

		root.appendChild( keyText );

		return root;
	}


	/**
	 * Decode the EPPCoaExtKey component
	 * 
	 * @param aElement
	 * @throws EPPDecodeException
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		if ( aElement != null ) {
			Text keyText = (Text) aElement.getFirstChild();

			this.key = (keyText == null) ? null : keyText.getData();
		}

	}


	/**
	 * Validate the state of the <code>EPPCoaExtKey</code> instance. A valid state
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
	 * implements a deep <code>EPPCoaExtKey</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPCoaExtKey</code> instance to compare with
	 * @return true if equal false otherwise
	 */
	public boolean equals ( Object aObject ) {

		if ( !(aObject instanceof EPPCoaExtKey) ) {
			return false;
		}
		EPPCoaExtKey theComp = (EPPCoaExtKey) aObject;

		return (key.equalsIgnoreCase( theComp.getKey() ));
	}


	/**
	 * Returns the key to a key/value pair.
	 * 
	 * @return the key
	 */
	public String getKey () {
		return key;
	}


	/**
	 * Sets the Key
	 * 
	 * @param aKey
	 */
	public void setKey ( String aKey ) {
		this.key = aKey;
	}

}