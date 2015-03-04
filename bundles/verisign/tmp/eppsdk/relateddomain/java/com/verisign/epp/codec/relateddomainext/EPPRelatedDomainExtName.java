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
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * EPPCodecComponent that encodes and decodes a <relDom:name> tag
 * <p>
 * Title: EPP 1.0 Related Domain - name tag
 * </p>
 * <p>
 * Description: The EPPRelatedDomainExtName object represents the domain name.
 * As XML, it is represented by a <relDom:name> element.
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
public class EPPRelatedDomainExtName implements
		com.verisign.epp.codec.gen.EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPRelatedDomainExtName.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * 
	 */
	private static final long serialVersionUID = -4632606931436150525L;

	final static java.lang.String ELM_DOMAIN_NAME = "relDom:name";

	/** Domain name. */
	private String domainName;


	/**
	 * Default constructor
	 */

	public EPPRelatedDomainExtName () {

	}


	/**
	 * @param aDomainName
	 */
	public EPPRelatedDomainExtName ( final String aDomainName ) {
		this.domainName = aDomainName;
	}


	/**
	 * Clone <code>EPPRelatedDomainExtName</code>.
	 * 
	 * @return clone of <code>EPPRelatedDomainExtName</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {
		EPPRelatedDomainExtName clone = null;

		clone = (EPPRelatedDomainExtName) super.clone();

		return clone;
	}


	/**
	 * Decode the EPPRelatedDomainExtName attributes from the aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *        - Root DOM Element to decode EPPRelatedDomainExtName from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {
		this.domainName = aElement.getFirstChild().getNodeValue();
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPRelatedDomainExtName instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return Element - Root DOM Element representing the EPPRelatedDomainExtName
	 *         instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode EPPRelatedDomainExtName instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		// Period with Attribute of Unit
		final Element root =
				aDocument.createElementNS( EPPRelatedDomainExtFactory.NS,
						ELM_DOMAIN_NAME );

		// add value
		final Text currVal = aDocument.createTextNode( this.domainName );

		// append child
		root.appendChild( currVal );

		return root;
	}


	/**
	 * implements a deep <code>EPPRelatedDomainExtName</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPRelatedDomainExtName</code> instance to compare with
	 * @return true if both instances of the EPPRelatedDomainExtName are equal.
	 */
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPRelatedDomainExtName) ) {
			return false;
		}

		final EPPRelatedDomainExtName theComp = (EPPRelatedDomainExtName) aObject;

		if ( !EqualityUtil.equals( this.domainName, theComp.domainName ) ) {
			cat.error( "EPPRelatedDomainExtName.equals(): name not equal" );
			return false;
		}
		return true;
	}


	/**
	 * Returns the domainName
	 * 
	 * @return the domainName
	 */
	public String getDomainName () {
		return this.domainName;
	}


	/**
	 * Sets domainName value to domainName
	 * 
	 * @param domainName
	 *        the domainName to set
	 */
	public void setDomainName ( final String domainName ) {
		this.domainName = domainName;
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
