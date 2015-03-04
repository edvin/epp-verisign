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
 * EPPCodecComponent that encodes and decodes a <relDom:infData> tag
 * <p>
 * Title: EPP 1.0 Related Domain - infData tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtInfData object represents the collection
 * of domains in a family of related domains. As such it is composed of a
 * collection of {@link EPPRelatedDomainExtGroup} objects. <br/>
 * As XML, it is represented by a <relDom:infData> element containing a number
 * of <relDom:group> elements.
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
public class EPPRelatedDomainExtInfData implements EPPCodecComponent {

	/**
	 * Serial version id - increment this if the structure changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The groups of related domains in the family.
	 */
	private List<EPPRelatedDomainExtGroup> relatedDomainGroup;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(
			EPPRelatedDomainExtInfData.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Element tag name for the create
	 */
	public static final String ELM_NAME = EPPRelatedDomainExtFactory.NS_PREFIX
			+ ":infData";


	/**
	 * Default constructor
	 */
	public EPPRelatedDomainExtInfData () {
	}


	/**
	 * A deep clone of the EPPRelatedDomainExtInfData.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone () throws CloneNotSupportedException {
		final EPPRelatedDomainExtInfData theClone =
				new EPPRelatedDomainExtInfData();
		

		if ( this.relatedDomainGroup != null ) {
			
			theClone.relatedDomainGroup = new ArrayList<EPPRelatedDomainExtGroup>();
			
			for (EPPRelatedDomainExtGroup group : this.relatedDomainGroup) {

				if (group != null) {
					theClone.relatedDomainGroup.add((EPPRelatedDomainExtGroup) group
							.clone());
				}
				else {
					theClone.relatedDomainGroup.add(null);
				}

			}
			
		}
		else {
			theClone.relatedDomainGroup = null;
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
		this.relatedDomainGroup =
				EPPUtil.decodeCompList( aElement, EPPRelatedDomainExtFactory.NS,
						EPPRelatedDomainExtGroup.ELM_NAME, EPPRelatedDomainExtGroup.class );
	}


	/**
	 * Append all data from this infData to the given DOM Document
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
					+ " in EPPRelatedDomainExtInfData.encode(Document)" );
		}

		try {
			// Validate States
			validateState();
		}
		catch ( final EPPCodecException e ) {
			cat
					.error( "EPPRelatedDomainExtInfData.encode(): Invalid state on encode: "
							+ e );
			throw new EPPEncodeException(
					"EPPRelatedDomainExtInfData invalid state: " + e );
		}

		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_NAME );

		EPPUtil.encodeCompList( aDocument, root, this.relatedDomainGroup );
		return root;
	}


	public boolean equals ( final Object aObj ) {
		if ( !(aObj instanceof EPPRelatedDomainExtInfData) ) {
			return false;
		}

		final EPPRelatedDomainExtInfData theComp =
				(EPPRelatedDomainExtInfData) aObj;

		if ( !EPPUtil.equalLists( this.relatedDomainGroup,
				theComp.relatedDomainGroup ) ) {
			cat
					.error( "EPPRelatedDomainExtInfData.equals(): relatedDomainGroup not equal" );
			return false;
		}

		return true;
	}

	
	/**
	 * Adds a domain represented by
	 * {@link EPPRelatedDomainExtGroup} to the list of groups.
	 * 
	 * @param aGroup A group to add to the list.
	 */
	public void addGroup(EPPRelatedDomainExtGroup aGroup) {
		if (this.relatedDomainGroup == null) {
			this.relatedDomainGroup = new ArrayList<EPPRelatedDomainExtGroup>();
		}
		
		this.relatedDomainGroup.add(aGroup);
	}

	/**
	 * @return the group
	 */
	public List<EPPRelatedDomainExtGroup> getGroup () {
		return this.relatedDomainGroup;
	}


	/**
	 * @param aGroup
	 */
	public void setGroup ( final List<EPPRelatedDomainExtGroup> aGroup ) {
		this.relatedDomainGroup = aGroup;
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
	 * Validate the state of the <code>EPPRelatedDomainExtInfData</code> instance.
	 * A valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *         Thrown if the instance is in an invalid state
	 */
	private void validateState () throws EPPCodecException {
		if ( (this.relatedDomainGroup == null) ) {
			throw new EPPCodecException( "EPPRelatedDomainExtGroup is null." );
		}
	}

}
