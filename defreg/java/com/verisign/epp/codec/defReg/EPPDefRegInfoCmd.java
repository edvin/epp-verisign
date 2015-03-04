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
package com.verisign.epp.codec.defReg;

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
 * Represents an EPPDefReg &lt;info&gt; command that is used to retrieve
 * information associated with  a defReg.  The &lt;defReg:info&gt; element
 * MUST     contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * An &ltdefReg:roid&gt element that contains the roid of the object to be
 * queried
 * </li>
 * </ul>
 * 
 * <br><br><br>
 * 
 * <ul>
 * <li>
 * Use <code>getRoid</code> and <code>setRoid</code> to get and set the roid
 * element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPDefRegInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPDefRegInfoCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegInfoResp
 */
public class EPPDefRegInfoCmd extends EPPInfoCmd {
	/** XML Element Roid for the <code>name</code> attribute. */
	private final static String ELM_DEFREG_ROID = "defReg:roid";

	/** XML Element Name of <code>EPPDefRegInfoCmd</code> root element. */
	final static String ELM_NAME = "defReg:info";

	/** DEFREG Roid to get information on. */
	private String roid;
	
		//EPPUPGRADE10
        /** authorization information. */
        private EPPAuthInfo authInfo = null;


	/**
	 * <code>EPPDefRegInfoCmd</code> default constructor.  The roid is
	 * initialized to <code>null</code>.     The roid must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPDefRegInfoCmd() {
		roid = null;
	}

	// End EPPDefRegInfoCmd.EPPDefRegInfoCmd()

	/**
	 * <code>EPPDefRegInfoCmd</code> constructor that takes the defReg roid as
	 * an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid DefReg roid to get information on.
	 */
	public EPPDefRegInfoCmd(String aTransId, String aRoid) {
		super(aTransId);

		roid = aRoid;
	}

		 public EPPDefRegInfoCmd(String aTransId, String aRoid, EPPAuthInfo aAuthInfo) {
                this(aTransId, aRoid);

                authInfo = aAuthInfo;
                authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
        }

	// End EPPDefRegInfoCmd.EPPDefRegInfoCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPDefRegInfoCmd</code>.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegInfoCmd.getNamespace()

	/**
	 * Gets the defReg roid to get information on.
	 *
	 * @return DefReg roid    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPDefRegInfoCmd.getName()

	/**
	 * Sets the defReg roid to get information on.
	 *
	 * @param aRoid DefReg Name
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}

	// End EPPDefRegInfoCmd.setRoid(String)

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPDefRegInfoCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the <code>EPPDefRegInfoCmd</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPDefRegInfoCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (roid == null) {
			throw new EPPEncodeException("required attribute roid is not set");
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		// Roid DEFREG
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPDefRegMapFactory.NS,
						 ELM_DEFREG_ROID);
			
		// Authorization Info
                if (authInfo != null) {
                        EPPUtil.encodeComp(aDocument, root, authInfo);
                }

		return root;
	}

	// End EPPDefRegInfoCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPDefRegInfoCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode <code>EPPDefRegInfoCmd</code>
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// DefReg Roid
		roid =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ROID);

			// Authorization Info
                authInfo =
                        (EPPAuthInfo) EPPUtil.decodeComp( aElement, EPPDefRegMapFactory.NS,
                                               EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO, EPPAuthInfo.class);
		}


	// End EPPDefRegInfoCmd.doDecode(Node)

	/**
         * Get authorization information
         *
         * @return Authorization information if defined; <code>null</code>
         *                 otherwise;
         */
        public EPPAuthInfo getAuthInfo() {
                return authInfo;
        }

        // End EPPDomainInfoCmd.getAuthInfo()
	
       	 /**
         * Set authorization information
         *
         * @param aAuthInfo EPPAuthInfo
         */
        public void setAuthInfo(EPPAuthInfo aAuthInfo) {
                if (aAuthInfo != null) {
                        authInfo = aAuthInfo;
                        authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
                }
        }

        // End EPPDomainInfoCmd.setAuthInfo(EPPAuthInfo)



	/**
	 * Compare an instance of <code>EPPDefRegInfoCmd</code> with this instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegInfoCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegInfoCmd theComp = (EPPDefRegInfoCmd) aObject;

		// Roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
				)) {
			return false;
		}
		
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

	// End EPPDefRegInfoCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegInfoCmd</code>.
	 *
	 * @return clone of <code>EPPDefRegInfoCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegInfoCmd clone = (EPPDefRegInfoCmd) super.clone();
				if (authInfo != null) {
                        clone.authInfo = (EPPAuthInfo) authInfo.clone();
                }


		return clone;
	}

	// End EPPDefRegInfoCmd.clone()

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

	// End EPPDefRegInfoCmd.toString()
}
