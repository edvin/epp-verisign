/***********************************************************
Copyright (C) 2007 VeriSign, Inc.

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

package com.verisign.epp.codec.jobscontact;

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
 * Extension to the contact update command   
 * 
 * @see com.verisign.epp.codec.jobscontact.EPPJobsContactUpdateCmd
 */
public class EPPJobsContactUpdateCmd implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPJobsContactUpdateCmd.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the JobsContact update extension tag
	 */
	public static final String ELM_NAME = EPPJobsContactExtFactory.NS_PREFIX + ":update";

	/**
	 * XML tag name for the title
	 */
	private static final String ELM_TITLE= EPPJobsContactExtFactory.NS_PREFIX + ":title";


	/**
	 * XML tag name for the Website
	 */
	private static final String ELM_WEBSITE = EPPJobsContactExtFactory.NS_PREFIX + ":website";
	
	/**
	 * XML tag name for the industryType
	 */
	private static final String ELM_INDUSTRY_TYPE = EPPJobsContactExtFactory.NS_PREFIX + ":industryType";
	
	/**
	 * XML tag name for isAdminContact
	 */
	private static final String ELM_IS_ADMIN_CONTACT = EPPJobsContactExtFactory.NS_PREFIX + ":isAdminContact";
	
	
	/**
	 * XML tag name for the AssociateMember
	 */
	private static final String ELM_IS_ASSOCIATION_MEMBER = EPPJobsContactExtFactory.NS_PREFIX + ":isAssociationMember";
	

	/**
	 * The contact title
	 */
	private String title = null;
	
	/**
	 * The contact website
	 */
	private String website = null;
	
	/**
	 * The contact industry type
	 */
	private String industryType = null;
	
	/**
	 * Admin Contact 
	 */
	private String isAdminContact = null;
	
	/**
	 * The association member
	 */
	private String isAssociationMember = null;

	/**
	 * Create an <code>EPPJobContact</code>  instance
	 */
	public EPPJobsContactUpdateCmd() {
	}
		
	/**
	 * Create a <code>EPPJobsContact</code> instance with the most common
	 * attributes
	 * 
	 * @param aTitle Contact title
	 * @param aWebsite Contact website
	 * @param aIndustryType Contact industry type
	 * @param aIsAdminContact Contact admin type
	 * @param aIsAssociationMember Contact whether member of HR association
	 */
	public EPPJobsContactUpdateCmd(String aTitle, String aWebsite, String aIndustryType, String aIsAdminContact, String aIsAssociationMember) {
		this.title= aTitle;
		this.website = aWebsite;
		this.industryType = aIndustryType;
		this.isAdminContact = aIsAdminContact;
		this.isAssociationMember = aIsAssociationMember;
	}
	
	/**
	 * Clone <code>EPPJobsContactUpdateCmd</code>.
	 * 
	 * @return clone of <code>EPPJobsContactUpdateCmd</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPJobsContactUpdateCmd clone = null;

		clone = (EPPJobsContactUpdateCmd) super.clone();

		return clone;
	}
	
	/**
	 * Sets all this instance's data in the given XML document
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
	
		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPJobsContact.encode(Document)");
		}

		Element root = aDocument.createElementNS(EPPJobsContactExtFactory.NS,
				ELM_NAME);
		root.setAttribute("xmlns:jobsContact", EPPJobsContactExtFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPJobsContactExtFactory.NS_SCHEMA);

		// dotJobs Contact title 
		EPPUtil.encodeString(aDocument, root, this.title, EPPJobsContactExtFactory.NS, ELM_TITLE);

		// dotJobs Contact website 
		EPPUtil.encodeString(aDocument, root, this.website, EPPJobsContactExtFactory.NS, ELM_WEBSITE);

		// dotJobs Contact industryType 
		EPPUtil.encodeString(aDocument, root, this.industryType, EPPJobsContactExtFactory.NS, ELM_INDUSTRY_TYPE);

		// dotJobs Contact Admin Type 
		EPPUtil.encodeString(aDocument, root, this.isAdminContact, EPPJobsContactExtFactory.NS, ELM_IS_ADMIN_CONTACT);		
		
		// dotJobs Contact Association Member 
		EPPUtil.encodeString(aDocument, root, this.isAssociationMember, EPPJobsContactExtFactory.NS, ELM_IS_ASSOCIATION_MEMBER);
		
		return root;
	}

	/**
	 * Decode the EPPJobsContactUpdateCmd component
	 * 
	 * @param aElement
	 * @throws EPPDecodeException
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Title
		this.title = EPPUtil.decodeString(aElement, EPPJobsContactExtFactory.NS,
				ELM_TITLE);

		// Website
		this.website = EPPUtil.decodeString(aElement, EPPJobsContactExtFactory.NS,
				ELM_WEBSITE);
		
		// IndustryType
		this.industryType = EPPUtil.decodeString(aElement, EPPJobsContactExtFactory.NS,
				ELM_INDUSTRY_TYPE);
		
		// IsAdminContact
		this.isAdminContact = EPPUtil.decodeString(aElement, EPPJobsContactExtFactory.NS,
				ELM_IS_ADMIN_CONTACT);
		
		// Is Association Member
		this.isAssociationMember = EPPUtil.decodeString(aElement, EPPJobsContactExtFactory.NS,
				ELM_IS_ASSOCIATION_MEMBER);
		
	}

	/**
	 * implements a deep <code>EPPJobsContact</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPJobsContact</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPJobsContactUpdateCmd)) {
			return false;
		}

		EPPJobsContactUpdateCmd theComp = (EPPJobsContactUpdateCmd) aObject;

		// Title
		if (!((this.title == null) ? (theComp.title == null) : this.title
				.equals(theComp.title))) {
			cat.error("EPPJobsContact.equals(): title not equal");
			return false;
		}

		// Website
		if (!((this.website == null) ? (theComp.website == null) : this.website
				.equals(theComp.website))) {
			cat.error("EPPJobsContact.equals(): website not equal");
			return false;
		}
		
		// IndustryType
		if (!((this.industryType == null) ? (theComp.industryType == null) : this.industryType
				.equals(theComp.industryType))) {
			cat.error("EPPJobsContact.equals(): Industry Type not equal");
			return false;
		}
		
		// AdminType
		if (!((this.isAdminContact == null) ? (theComp.isAdminContact == null) : this.isAdminContact
				.equals(theComp.isAdminContact))) {
			cat.error("EPPJobsContact.equals(): isAdminContact not equal");
			return false;
		}
		
		// Association Member
		if (!((this.isAssociationMember == null) ? (theComp.isAssociationMember == null) : this.isAssociationMember
				.equals(theComp.isAssociationMember))) {
			cat.error("EPPJobsContact.equals(): Association Member not equal");
			return false;
		}		
		
		return true;
	}

	/**
	 * Returns the title
	 * 
	 * @return the title if set;<code>null</code> otherwise
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title
	 * 
	 * @param aTitle
	 *            Contact title
	 */
	public void setTitle(String aTitle) {
		this.title = aTitle;
	}

	
	/**
	 * Returns the contact website
	 * 
	 * @return the contact website if set;<code>null</code> otherwise
	 */
	public String getWebsite() {
		return this.website;
	}

	/**
	 * Sets the Contact website
	 * 
	 * @param aWebsite
	 *            Contact website 
	 */
	public void setWebsite(String aWebsite) {
		this.website = aWebsite;
	}
	
	
	/**
	 * Returns the Contact industry type
	 * 
	 * @return the contact industry type if set;<code>null</code> otherwise
	 */
	public String getIndustryType() {
		return this.industryType;
	}

	/**
	 * Sets the Contact industry type
	 * 
	 * @param aIndustryType
	 *            Contact industry type
	 */
	public void setIndustry(String aIndustryType) {
		this.industryType = aIndustryType;
	}


	/**
	 * Returns whether the contact is of admin type
	 * 
	 * @return the contact type if set;<code>null</code> otherwise
	 */
	public String isAdminContact() {
		return this.isAdminContact;
	}

	/**
	 * Sets the contact type information
	 * 
	 * @param aIsAdminContact
	 *            Contact Association membership
	 */
	public void setAdminContact(String aIsAdminContact) {
		this.isAdminContact = aIsAdminContact;
	}

	
	/**
	 * Returns whether the contact is a member of HR association
	 * 
	 * @return the contact association membership information if set;<code>null</code> otherwise
	 */
	public String isAssociationMember() {
		return this.isAssociationMember;
	}

	/**
	 * Sets the membership information for the contact
	 * 
	 * @param aIsAssociationMember
	 *            Contact Association membership
	 */
	public void setAssociationMember(String aIsAssociationMember) {
		this.isAssociationMember = aIsAssociationMember;
	}
	
	
}