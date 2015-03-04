/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2007 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */

package com.verisign.epp.codec.premiumdomain;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents an EPP Domain &lt;premiumdomain:chkData&gt; extension response to a
 * <code>EPPDomainCheckCmd</code>. When a &lt;check&gt; command has been
 * processed successfully, the EPP response may contain a child &lt;premiumdomain:chkData&gt; 
 * extension element that identifies the premium domain namespace and the location of the 
 * premium domain schema. The &lt;premiumdomain:chkData&gt; elements contains one or more 
 * &lt;premiumdomain:cd&gt; elements that contain the following child elements: <br>
 * 
 * <ul>
 * <li>
 * A &lt;premiumdomain:name&gt; element that contains the fully qualified name of the
 * queried domain object. This element MUST contain an "isPremium" attribute
 * whose value indicates if an object is premium at the moment the &lt;check&gt;
 * command was completed. A value of "1" menas that the object is premium.
 * A value of "0" means that the object is not a premium.
 * </li>
 * <li>
 * An OPTIONAL &lt;premiumdomain:price&gt; element that MAY be provided when an
 * object is premium and available for provisioning.
 * </li>
 * </ul>
 * 
 */

public class EPPPremiumDomainCheckResp implements EPPCodecComponent {

	private static final long serialVersionUID = -2490520038846085829L;

	/**
	 * XML tag name of <code>EPPPremiumDomainCheckResp</code> root element
	 */
	public static final String ELM_NAME = EPPPremiumDomainExtFactory.NS_PREFIX
			+ ":chkData";

	/**
	 * Vector of <code>EPPPremiumDomainCheckResult</code> instances.
	 */
	private Vector results;


	/**
	 * <code>EPPPremiumDomainCheckResp</code> default constructor. It will set
	 * results attribute to an empty <code>Vector</code>.
	 */
	public EPPPremiumDomainCheckResp () {
		results = new Vector();
	}


	/**
	 * <code>EPPPremiumDomainCheckResp</code> constructor that will set the
	 * result of an individual domain.
	 * 
	 * @param aResult
	 *        Result of a single premium domain name
	 */
	public EPPPremiumDomainCheckResp ( EPPPremiumDomainCheckResult aResult ) {
		results = new Vector();
		results.addElement( aResult );
	}


	/**
	 * <code>EPPPremiumDomainCheckResp</code> constructor that will set the
	 * result of multiple domains.
	 * 
	 * @param aResults
	 *        Results Vector of EPPDomainCheckResult instances.
	 */
	public EPPPremiumDomainCheckResp ( Vector aResults ) {
		results = aResults;
	}


	/**
	 * Clone <code>EPPPremiumDomainCheckResp</code>.
	 * 
	 * @return clone of <code>EPPPremiumDomainCheckResp</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPPremiumDomainCheckResp clone = (EPPPremiumDomainCheckResp) super
				.clone();

		clone.results = (Vector) results.clone();

		for ( int i = 0; i < results.size(); i++ )
			clone.results.setElementAt( ((EPPPremiumDomainCheckResult) results
					.elementAt( i )).clone(), i );

		return clone;
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPPremiumDomainCheckResp</code> instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built
	 * @return Element Root DOM Element representing the
	 *         <code>EPPPremiumDomainCheckResp</code> instance.
	 * @exception EPPEncodeException
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPPremiumDomainCheckResp.encode(Document)" );
		}

		Element root = aDocument.createElementNS(
				EPPPremiumDomainExtFactory.NS, ELM_NAME );
		root
				.setAttribute( "xmlns:premiumdomain",
						EPPPremiumDomainExtFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPPremiumDomainExtFactory.NS_SCHEMA );

		// Results
		EPPUtil.encodeCompVector( aDocument, root, results );

		return root;
	}


	/**
	 * Decode the <code>EPPPremiumDomainCheckResp</code> attributes from the
	 * <code>aElement</code> DOM Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode <code>EPPPremiumDomainCheckResp</code>
	 *        from.
	 * @exception EPPDecodeException
	 *            Unable to decode <code>aElement</code>
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		// Results
		results = EPPUtil.decodeCompVector( aElement,
				EPPPremiumDomainExtFactory.NS,
				EPPPremiumDomainCheckResult.ELM_NAME,
				EPPPremiumDomainCheckResult.class );

	}


	/**
	 * Compare an instance of <code>EPPPremiumDomainCheckResp</code> with this
	 * instance.
	 * 
	 * @param aObject
	 *        Object to compare with.
	 * @return true if equal false otherwise
	 */
	public boolean equals ( Object aObject ) {

		if ( !(aObject instanceof EPPPremiumDomainCheckResp) ) {
			return false;
		}

		EPPPremiumDomainCheckResp theComp = (EPPPremiumDomainCheckResp) aObject;
		
		// results
		if ( !EPPUtil.equalVectors( results, theComp.results ) ) {
			return false;
		}
		
		return true;
	}


	/**
	 * Set the results of a <code>EPPPremiumDomainCheckResp</code> Response.
	 * There is one <code>EPPPremiumDomainCheckResult</code> instance in
	 * <code>aResults</code> for each domain requested in the
	 * <code>EPPDomainCheckCmd</code> Command.
	 * 
	 * @param aResults
	 *        Vector of <code>EPPPremiumDomainCheckResult</code> instances.
	 */
	public void setCheckResults ( Vector aResults ) {
		results = aResults;
	}

	/**
	 * Get the results of a <code>EPPPremiumDomainCheckResp</code> Response.
	 * There is one <code>EPPPremiumDomainCheckResult</code> instance in
	 * <code>someResults</code> for each domain requested in the
	 * <code>EPPPremiumDomainCheckResult</code> Command.
	 * 
	 * @return Vector of <code>EPPPremiumDomainCheckResult</code> instances.
	 */
	public Vector getCheckResults () {
		return results;
	}

}
