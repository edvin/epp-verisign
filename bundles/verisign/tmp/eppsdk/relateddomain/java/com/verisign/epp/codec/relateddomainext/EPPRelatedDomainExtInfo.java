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
 * EPPCodecComponent that encodes and decodes a <relDom:info> tag
 * <p>
 * Title: EPP 1.0 Related Domain - info tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtInfo object indicates to the server to
 * include the related domain information in the response. As XML, it is
 * represented by a <relDom:info> element.
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
public class EPPRelatedDomainExtInfo implements
		com.verisign.epp.codec.gen.EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtInfo.class
			.getName(), EPPCatFactory.getInstance().getFactory() );
	
	/**
	 * The "type" attribute name
	 */
	private static final String ATTR_TYPE = "type";	
	
	
	/**
	 * Constant for the type attribute that defines the use of the Domain Info
	 * Form. With this form, a successful response will include the domain info
	 * of the domain name specified in the domain info command along with the
	 * related domain information in the extension.
	 */
	public static String TYPE_DOMAIN = "domain";

	/**
	 * Constant for the type attribute that defines the use of the Related Info
	 * Form. With this form, a successful response will include just the related
	 * domain information associated with the domain name specified in the
	 * domain info command. The domain name in the domain info command does not
	 * have to exist and the response is a standard EPP response with the
	 * related domain extension.
	 */
	public static String TYPE_RELATED = "related";

	/**
	 * Attributes that defines the form of the related domain extension, being
	 * either the Domain Info Form as represented by the
	 * <code>TYPE_DOMAIN</code> constant or the Related Info Form as represented
	 * by the <code>TYPE_RELATED</code>. The default form is the Domain Info
	 * Form.
	 */
	private String type = TYPE_DOMAIN;

	/**
	 * Serialization identifier.
	 */
	private static final long serialVersionUID = -4632606931436150525L;

	final static java.lang.String ELM_INFO = "relDom:info";


	/**
	 * Default constructor, where the <code>type</code> is set to <code>TYPE_DOMAIN</code>.
	 */
	public EPPRelatedDomainExtInfo () {
	}


	/**
	 * Constructor that takes the desired type of the extension using either the
	 * Domain Info Form, represented by the <code>TYPE_DOMAIN</code> constant,
	 * or the Related Info Form, represented by the <code>TYPE_RELATED</code>
	 * constant.
	 * 
	 * @param aType
	 *            Type of form to use which must be either the
	 *            <code>TYPE_DOMAIN</code> constant for the Domain Info Form or
	 *            the <code>TYPE_RELATED</code> constant for the Related Info
	 *            Form.
	 */
	public EPPRelatedDomainExtInfo(String aType) {
		this.type = aType;
	}
	
	
	/**
	 * Gets the form type of the extension, which must be 
	 * either the <code>TYPE_DOMAIN</code> constant for the Domain Info Form, 
	 * and the <code>TYPE_RELATED</code> constant for the Related Info Form.
	 * 
	 * @return Either <code>TYPE_DOMAIN</code> or <code>TYPE_RELATED</code>.
	 */
	public String getType() {
		return this.type;
	}


	/**
	 * Sets the form type of the extension, which must be 
	 * either the <code>TYPE_DOMAIN</code> constant for the Domain Info Form, 
	 * and the <code>TYPE_RELATED</code> constant for the Related Info Form.
	 * 
	 * @param aType Either <code>TYPE_DOMAIN</code> or <code>TYPE_RELATED</code>.
	 */
	public void setType(String aType) {
		this.type = aType;
	}


	/**
	 * Clone <code>EPPRelatedDomainExtInfo</code>.
	 * 
	 * @return clone of <code>EPPRelatedDomainExtInfo</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {
		EPPRelatedDomainExtInfo clone = null;

		clone = (EPPRelatedDomainExtInfo) super.clone();

		return clone;
	}


	/**
	 * Decode the EPPRelatedDomainExtInfo attributes from the aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *        - Root DOM Element to decode EPPRelatedDomainExtInfo from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {
		
		// Type
		this.type = aElement.getAttribute(ATTR_TYPE);
		if (this.type.isEmpty()) {
			this.type = TYPE_DOMAIN;
		}
		
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPRelatedDomainExtInfo instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return Element - Root DOM Element representing the EPPRelatedDomainExtInfo
	 *         instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode EPPRelatedDomainExtInfo instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		// Create the element relDom:Info
		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS, ELM_INFO );
		
		// Type
		if (this.type == null
				|| (!this.type.equals(TYPE_DOMAIN) && !this.type
						.equals(TYPE_RELATED))) {
			throw new EPPEncodeException(
					"EPPRelatedDomainExtInfo type attribute with invalid value of: "
							+ this.type);
		}

		root.setAttribute(ATTR_TYPE, this.type);
		
		return root;
	}


	/**
	 * implements a deep <code>EPPRelatedDomainExtInfo</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPRelatedDomainExtInfo</code> instance to compare with
	 * @return DOCUMENT ME!
	 */
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPRelatedDomainExtInfo) ) {
			cat
					.error( "EPPRelatedDomainExtInfo.equals(): not instanceof EPPRelatedDomainExtInfo" );
			return false;
		}
		
		EPPRelatedDomainExtInfo other = (EPPRelatedDomainExtInfo) aObject;

		// Type
		if (!EqualityUtil.equals(this.type, other.type)) {
			cat.error("EPPRelatedDomainExtInfo.equals(): type not equal");
			return false;
		}		
		
		return true;
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
