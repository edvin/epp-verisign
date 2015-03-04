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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * EPPCodecComponent that encodes and decodes a COA Update Tag.
 * <p>
 * Title: EPP 1.0 Client Object Attribute - Update
 * </p>
 * <p>
 * Description: The Update tag is used to represent the changes to an object's
 * Client Object Attributes being performed as part of an epp <update> command.
 * It consists of three collections, each of which are optional.<br/>
 * <ul>
 * <li>A collection of EPPCoaExtAttr objects representing new COAs being added
 * to the object or having their values changed.</li>
 * <li>A collection of EPPCoaExtKey objects identifying existing COAs which are
 * being removed from the object</li>
 * </ul>
 * <br>
 * As XML, it is represented by a <coa:update> element, which in turn contains
 * one or more of the following elements:<br/>
 * <ul>
 * <li>A <coa:put> element containing in turn one or more <coa:attr> elements,
 * each describing a new COA being added or updated.</li>
 * <li>A <coa:rem> element containing in turn one or more <coa:key> elements,
 * each identifying an existing COA being removed.</li>
 * </ul>
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
public class EPPCoaExtUpdate implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat =
			Logger.getLogger( EPPCoaExtUpdate.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Constant for the key tag
	 */
	public static final String ELM_NAME = EPPCoaExtFactory.NS_PREFIX + ":update";

	/**
	 * Element tag name for the put
	 */
	public static final String ELM_PUT = EPPCoaExtFactory.NS_PREFIX + ":put";

	/**
	 * Element tag name for the rem
	 */
	public static final String ELM_REM = EPPCoaExtFactory.NS_PREFIX + ":rem";

	/**
	 * Attributes to be associated with the updated object
	 */
	private List putAttrs = null;

	/**
	 * Attributes to be removed from the updated object
	 */
	private List remAttrs = null;


	/**
	 * Gets the list of Attributes to be added or updated.
	 * 
	 * @return <code>List</code> of attribute <code>EPPCoaExtAttr</code> instances
	 *         if defined; <code>null</code> otherwise.
	 */
	public List getPutAttrs () {
		return putAttrs;
	}


	/**
	 * Sets the <code>List</code> of attributes <code>EPPCoaExtAttr</code>
	 * instances to create.
	 * 
	 * @param aPutAttrs
	 *        <code>List</code> of <code>EPPCoaExtAttr</code> instances
	 */
	public void setPutAttrs ( List aPutAttrs ) {
		this.putAttrs = aPutAttrs;
	}


	/**
	 * Gets the list of Attribute keys to be removed.
	 * 
	 * @return <code>List</code> of attribute <code>EPPCoaExtKey</code> instances
	 *         if defined; <code>null</code> otherwise.
	 */
	public List getRemAttrs () {
		return remAttrs;
	}


	/**
	 * Sets the <code>List</code> of attribute key <code>EPPCoaExtKey</code>
	 * instances to remove.
	 * 
	 * @param remAttrs
	 *        <code>List</code> of <code>EPPCoaExtAttr</code> instances
	 */
	public void setRemAttrs ( List remAttrs ) {
		this.remAttrs = remAttrs;
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
	public void decode ( Element aElement ) throws EPPDecodeException {

		this.putAttrs = null;
		this.remAttrs = null;

		// rem
		Element remElm = EPPUtil.getElementByTagNameNS( aElement, EPPCoaExtFactory.NS, ELM_REM );
		if ( remElm != null ) {
			this.remAttrs =
					EPPUtil.decodeCompList( remElm, EPPCoaExtFactory.NS,
							EPPCoaExtKey.ELM_NAME, EPPCoaExtKey.class );
		}

		// put
		Element putElm = EPPUtil.getElementByTagNameNS( aElement, EPPCoaExtFactory.NS, ELM_PUT );
		if ( putElm != null ) {
			this.putAttrs =
					EPPUtil.decodeCompList( putElm, EPPCoaExtFactory.NS,
							EPPCoaExtAttr.ELM_NAME, EPPCoaExtAttr.class );
		}

	}


	/**
	 * Append all data from this COA update to the given DOM Document
	 * 
	 * @param aDocument
	 *        The DOM Document to append data to
	 * @return Encoded DOM <code>Element</code>
	 * @throws EPPEncodeException
	 *         Thrown when errors occur during the encode attempt or if the
	 *         instance is invalid.
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " in EPPCoaExtUpdate.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( EPPCodecException e ) {
			cat.error( "EPPCoaExtUpdate.encode(): Invalid state on encode: " + e );
			throw new EPPEncodeException( "EPPCoaExtUpdate invalid state: " + e );
		}

		// coa:update
		Element root = aDocument.createElementNS( EPPCoaExtFactory.NS, ELM_NAME );

		root.setAttribute( "xmlns:" + EPPCoaExtFactory.NS_PREFIX,
				EPPCoaExtFactory.NS );
        root.setAttributeNS(
                EPPCodec.NS_XSI, "xsi:schemaLocation",
                EPPCoaExtFactory.NS_SCHEMA);

		// rem
		if ( hasRemData() ) {
			Element rem = aDocument.createElementNS( EPPCoaExtFactory.NS, ELM_REM );
			root.appendChild( rem );

			EPPUtil.encodeCompList( aDocument, rem, this.remAttrs );
		}

		// put
		if ( hasPutData() ) {
			Element put = aDocument.createElementNS( EPPCoaExtFactory.NS, ELM_PUT );
			root.appendChild( put );

			EPPUtil.encodeCompList( aDocument, put, this.putAttrs );
		}

		return root;
	}


	/**
	 * Are there attributes contained in the attr remove list?
	 * 
	 * @return <code>true</code> if remove list of <code>EPPCoaExtKey</code> is
	 *         not <code>null</code> and not empty; <code>false</code> otherwise.
	 */
	private boolean hasRemData () {
		return this.remAttrs != null ? !this.remAttrs.isEmpty() : false;
	}


	/**
	 * Are there attributes contained in the attr put list?
	 * 
	 * @return <code>true</code> if remove list of <code>EPPCoaExtAttr</code> is
	 *         not <code>null</code> and not empty; <code>false</code> otherwise.
	 */
	private boolean hasPutData () {
		return this.putAttrs != null ? !this.putAttrs.isEmpty() : false;
	}


	/**
	 * Validate the state of the <code>EPPCoaCreate</code> instance. A valid state
	 * means that all of the required attributes have been set. If validateState
	 * returns without an exception, the state is valid. If the state is not
	 * valid, the <code>EPPCodecException</code> will contain a description of the
	 * error. throws EPPCodecException State error. This will contain the name of
	 * the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( !this.hasPutData() && !this.hasRemData() ) {
			throw new EPPCodecException( "EPPCoaExtUpdate contains no attr elements." );
		}

	}


	/**
	 * A deep clone of the EPPCoaCreate.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone () throws CloneNotSupportedException {
		EPPCoaExtUpdate theClone = new EPPCoaExtUpdate();

		if ( this.putAttrs != null ) {
			for ( Iterator iterator = this.putAttrs.iterator(); iterator.hasNext(); ) {
				Object attrObject = iterator.next();
				if ( attrObject != null ) {
					EPPCoaExtAttr attr = (EPPCoaExtAttr) attrObject;
					theClone.appendPutAttr( (EPPCoaExtAttr) attr.clone() );
				}
			}
		}

		if ( this.remAttrs != null ) {
			for ( Iterator iterator = this.remAttrs.iterator(); iterator.hasNext(); ) {
				Object keyObject = iterator.next();
				if ( keyObject != null ) {
					EPPCoaExtKey key = (EPPCoaExtKey) keyObject;
					theClone.appendRemAttr( (EPPCoaExtKey) key.clone() );
				}
			}
		}

		return theClone;
	}


	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals ( Object aObj ) {

		if ( !(aObj instanceof EPPCoaExtUpdate) ) {
			return false;
		}

		EPPCoaExtUpdate theComp = (EPPCoaExtUpdate) aObj;

		if ( !EPPUtil.equalLists( this.putAttrs, theComp.putAttrs ) ) {
			return false;
		}

		if ( !EPPUtil.equalLists( this.remAttrs, theComp.remAttrs ) ) {
			return false;
		}

		return true;
	}


	/**
	 * Appends to the <code>List</code> of attributes <code>EPPCoaExtAttr</code>
	 * instances to add or update.
	 * 
	 * @param aAttr
	 *        <code>EPPCoaExtAttr</code> instance
	 */
	public void appendPutAttr ( EPPCoaExtAttr aAttr ) {
		if ( this.putAttrs == null ) {
			this.putAttrs = new ArrayList();
		}

		this.putAttrs.add( aAttr );
	}


	/**
	 * Appends to the <code>List</code> of attribute keys
	 * <code>EPPCoaExtKey</code> instances to remove.
	 * 
	 * @param aKey
	 *        <code>EPPCoaExtKey</code> instance
	 */
	public void appendRemAttr ( EPPCoaExtKey aKey ) {
		if ( this.remAttrs == null ) {
			this.remAttrs = new ArrayList();
		}
		this.remAttrs.add( aKey );
	}

}
