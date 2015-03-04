/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.codec.contact;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP Contact &lt;info&gt; command that is used to retrieve
 * information associated with a contact. In addition to the standard EPP
 * command elements, the &lt;info&gt; command MUST contain a
 * &lt;contact:info&gt; element that identifies the contact namespace and the
 * location of the contact schema. The &lt;contact:info&gt; element MUST
 * contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;contact:id&gt; element that contains the server-unique identifier for
 * the queried contact.  Use <code>getId</code> and <code>setId</code>     to
 * get and set the element.
 * </li>
 * </ul>
 * 
 * <br><br>
 * <code>EPPContactInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPContactInfoCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.contact.EPPContactInfoResp
 */
public class EPPContactInfoCmd extends EPPInfoCmd {
	/** XML Element Id of <code>EPPContactInfoCmd</code> root element. */
	final static String ELM_NAME = "contact:info";

	/** XML Element Id for the <code>id</code> attribute. */
	private final static String ELM_CONTACT_ID = "contact:id";

	/** Contact Id to get information on. */
	private String id;


	/** authorization information. */
	//EPPUPGRADE10
        private EPPAuthInfo authInfo = null;


	/**
	 * <code>EPPContactInfoCmd</code> default constructor.  The id is
	 * initialized to <code>null</code>.     The id must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPContactInfoCmd() {
		id = null;
	}

	// End EPPContactInfoCmd.EPPContactInfoCmd()

	/**
	 * <code>EPPContactInfoCmd</code> constructor that takes the contact id as
	 * an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aId Contact id to get information on.
	 */
	public EPPContactInfoCmd(String aTransId, String aId) {
		super(aTransId);

		id = aId;
	}

		/**
         * <code>EPPDomainInfoCmd</code> constructor that takes the domain name as
         * an argument.
         *
         * @param aTransId Transaction Id associated with command.
         * @param aName Domain name to get information on.
         */
	//EPPUPGRADE10
        public EPPContactInfoCmd(String aTransId, String aName, EPPAuthInfo aAuthInfo) {
                this(aTransId, aName);

                authInfo = aAuthInfo;
                authInfo.setRootName(EPPContactMapFactory.NS, EPPContactMapFactory.ELM_CONTACT_AUTHINFO);
        }


	// End EPPContactInfoCmd.EPPContactInfoCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPContactInfoCmd</code>.
	 *
	 * @return <code>EPPContactMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPContactMapFactory.NS;
	}

	// End EPPContactInfoCmd.getIdspace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPContactInfoCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPContactInfoCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPContactInfoCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (id == null) {
			throw new EPPEncodeException("required attribute id is not set");
		}

		Element root =
			aDocument.createElementNS(EPPContactMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:contact", EPPContactMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPContactMapFactory.NS_SCHEMA);

		// Id
		EPPUtil.encodeString(
							 aDocument, root, id, EPPContactMapFactory.NS,
							 ELM_CONTACT_ID);


	//EPPUPGRADE10		
		// Authorization Info
                if (authInfo != null) {
                        EPPUtil.encodeComp(aDocument, root, authInfo);
                }

		return root;
	}

	// End EPPContactInfoCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPContactInfoCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement - Root DOM Element to decode
	 * 		  <code>EPPContactInfoCmd</code> from.
	 *
	 * @exception EPPDecodeException - Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// Contact Id
		id = EPPUtil.decodeString(
								  aElement, EPPContactMapFactory.NS,
								  ELM_CONTACT_ID);

		// EPPUPGRADE10
	// Authorization Info
                authInfo =
                        (EPPAuthInfo) EPPUtil.decodeComp(
                             aElement, EPPContactMapFactory.NS,EPPContactMapFactory.ELM_CONTACT_AUTHINFO, EPPAuthInfo.class);



	}

	// End EPPContactInfoCmd.doDecode(Node)

	/**
	 * Gets the contact id to get information on.
	 *
	 * @return Contact Id    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getId() {
		return id;
	}

	// End EPPContactInfoCmd.getId()

	/**
	 * Sets the contact id to get information on.
	 *
	 * @param aId Contact Id
	 */
	public void setId(String aId) {
		id = aId;
	}


	 /**
         * Get authorization information
         *
         * @return Authorization information if defined; <code>null</code>
         *                 otherwise;
         */
	//EPPUPGRADE10
        public EPPAuthInfo getAuthInfo() {
                return authInfo;
        }

        // End EPPContactInfoCmd.getAuthInfo()

	/**
         * Set authorization information
         *
         * @param aAuthInfo EPPAuthInfo
         */
	//EPPUPGRADE10
        public void setAuthInfo(EPPAuthInfo aAuthInfo) {
                if (aAuthInfo != null) {
                        authInfo = aAuthInfo;
                        authInfo.setRootName(EPPContactMapFactory.NS, EPPContactMapFactory.ELM_CONTACT_AUTHINFO);
                }
        }// End EPPContactInfoCmd.setAuthInfo(EPPAuthInfo)


	// End EPPContactInfoCmd.setId(String)

	/**
	 * Compare an instance of <code>EPPContactInfoCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPContactInfoCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPContactInfoCmd theComp = (EPPContactInfoCmd) aObject;

		// Id
		if (!((id == null) ? (theComp.id == null) : id.equals(theComp.id))) {
			return false;
		}

		//EPPUPGRADE10
		 // Authorization Info
                if (
                        !(
                                (authInfo == null) ? (theComp.authInfo == null)
                                                                                   : authInfo.equals(theComp.authInfo)
                                )) {
                        return false;
                }

		return true;
	}

	// End EPPContactInfoCmd.equals(Object)

	/**
	 * Clone <code>EPPContactInfoCmd</code>.
	 *
	 * @return clone of <code>EPPContactInfoCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPContactInfoCmd clone = (EPPContactInfoCmd) super.clone();
		
			if (authInfo != null) {
                        clone.authInfo = (EPPAuthInfo) authInfo.clone();
                }

		return clone;
	}

	// End EPPContactInfoCmd.clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPContactInfoCmd.toString()
}
